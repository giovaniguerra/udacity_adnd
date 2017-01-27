package br.com.gbguerra.popularfilms.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import br.com.gbguerra.popularfilms.R;
import br.com.gbguerra.popularfilms.models.Film;

import static android.content.ContentValues.TAG;
import static br.com.gbguerra.popularfilms.R.id.toolbar;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilmDetailFragment extends Fragment {

    private Film film;

    private ImageView imageFilm;
    private TextView titleFilm;
    private TextView synopsisFilm;
    private TextView ratingsFilm;
    private TextView releaseDateFilm;

    private Context ctx;

    public FilmDetailFragment() {
        // Required empty public constructor
    }

    public static FilmDetailFragment newInstance(Film film) {
        FilmDetailFragment fragment = new FilmDetailFragment();

        Bundle args = new Bundle();
        args.putParcelable("film", film);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            film = getArguments().getParcelable("film");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updateInterface();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_film_detail, container, false);

        ctx = view.getContext();

        imageFilm = (ImageView) view.findViewById(R.id.film_image);
//        titleFilm = (TextView) view.findViewById(R.id.film_title);
        synopsisFilm = (TextView) view.findViewById(R.id.film_synopsis);
        ratingsFilm = (TextView) view.findViewById(R.id.film_ratings);
        releaseDateFilm = (TextView) view.findViewById(R.id.film_release_date);

        return view;
    }

    private void updateInterface() {
        if (film == null) {
            Log.e(TAG, "Film is null!");
            return;
        }

        // Poster
        Glide.with(ctx)
                .load(film.getPosterUrl())
                .into(imageFilm);

        // Title
//        titleFilm.setText(film.getTitle());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(film.getTitle());

        // Synopsis
        synopsisFilm.setText(film.getOverview());

        // Ratings
        ratingsFilm.setText("" + film.getVoteAverage());

        // Release Date
        releaseDateFilm.setText(film.getReleaseDate());

    }
}
