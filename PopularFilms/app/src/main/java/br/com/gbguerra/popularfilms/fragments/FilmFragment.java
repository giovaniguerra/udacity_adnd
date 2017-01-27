package br.com.gbguerra.popularfilms.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import br.com.gbguerra.popularfilms.BuildConfig;
import br.com.gbguerra.popularfilms.R;
import br.com.gbguerra.popularfilms.activities.SettingsActivity;
import br.com.gbguerra.popularfilms.adapters.FilmAdapter;
import br.com.gbguerra.popularfilms.models.Film;
import br.com.gbguerra.popularfilms.util.Utils;


/**
 * A simple {@link Fragment} subclass.
 */
public class FilmFragment extends Fragment {

    private FilmAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private View view;

    public FilmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateFilms();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_film, container, false);

        layoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_films);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(
                new MarginItemDecoration(getContext(), R.dimen.recycler_horizontal_margin));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filmfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_sort) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateFilms(){
        if(Utils.isOnline(getActivity())) {
            FetchFilmTask filmTask = new FetchFilmTask();
            filmTask.execute();
        } else {
            Snackbar.make(view, "No internet connection!", Snackbar.LENGTH_LONG).show();
        }
    }

    private class FetchFilmTask extends AsyncTask<String, Void, Film[]> {

        private final String LOG_TAG = FetchFilmTask.class.getSimpleName();

        private Film[] getFilmsFromJson(String jsonStr) throws JSONException {

            final String TMDB_RESULTS = "results";
            final String TMDB_ID = "id";
            final String TMDB_TITLE = "title";
            final String TMDB_OVERVIEW = "overview";
            final String TMDB_VOTE_AVERAGE = "vote_average";
            final String TMDB_POSTER_PATH = "poster_path";
            final String TMDB_RELEASE_DATE = "release_date";

            JSONObject filmJson = new JSONObject(jsonStr);
            JSONArray filmsArray = filmJson.getJSONArray(TMDB_RESULTS);

            Film[] resultFilms = new Film[20];
            final String THUMBNAIL_BASE_URL = "http://image.tmdb.org/t/p/w185/";
//            final String THUMBNAIL_SIZE = "w185/";

            for(int i = 0; i < filmsArray.length(); i++) {

                int id;
                String title;
                String overview;
                String posterUrl;
                String releaseDate;
                double voteAverage;

                // Get the JSON object representing the film
                JSONObject filmObject = filmsArray.getJSONObject(i);

                id = filmObject.getInt(TMDB_ID);
                title = filmObject.getString(TMDB_TITLE);
                overview = filmObject.getString(TMDB_OVERVIEW);
                posterUrl = THUMBNAIL_BASE_URL + filmObject.getString(TMDB_POSTER_PATH);
                releaseDate = filmObject.getString(TMDB_RELEASE_DATE);
                voteAverage = filmObject.getDouble(TMDB_VOTE_AVERAGE);

                resultFilms[i] = new Film(id, title, overview, posterUrl, releaseDate, voteAverage);
            }
            return resultFilms;
        }

        @Override
        protected Film[] doInBackground(String... params) {

            // Default search type value
            SharedPreferences sharedPrefs =
                    PreferenceManager.getDefaultSharedPreferences(getActivity());
            String searchType = sharedPrefs.getString(
                    getString(R.string.pref_film_sort_key),
                    getString(R.string.pref_text_popular));

            // Check for search type
            if(params.length != 0) {
                searchType = params[0];
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String filmJsonStr = null;

            try {
                // Construct the URL for the TheMovieDB query
                final String THE_MOVIE_DB_BASE_URL =
                        "http://api.themoviedb.org/3/movie/" + searchType + "?";

                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(THE_MOVIE_DB_BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                // Create the request to TMDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                filmJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getFilmsFromJson(filmJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(Film[] films) {
            if (films != null) {
                if(adapter == null) {
                    adapter = new FilmAdapter(Arrays.asList(films));
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.setFilmList(Arrays.asList(films));
                    adapter.notifyDataSetChanged();
                }
                // New data is back from the server.  Hooray!
            }
        }
    }

    class MarginItemDecoration extends RecyclerView.ItemDecoration {

        private int mItemOffset;

        public MarginItemDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public MarginItemDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }

}
