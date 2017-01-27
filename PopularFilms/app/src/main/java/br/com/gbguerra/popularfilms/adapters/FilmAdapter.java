package br.com.gbguerra.popularfilms.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.gbguerra.popularfilms.R;
import br.com.gbguerra.popularfilms.models.Film;
import br.com.gbguerra.popularfilms.viewholders.FilmViewHolder;

/**
 * Created by Giovani Guerra on 07/11/2016.
 */

public class FilmAdapter extends RecyclerView.Adapter<FilmViewHolder> {

    private List<Film> filmList;

    public FilmAdapter(List<Film> filmList){
        this.filmList = filmList;
    }

    public List<Film> getFilmList() {
        return filmList;
    }

    public void setFilmList(List<Film> filmList) {
        this.filmList = filmList;
    }

    @Override
    public FilmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_film, parent, false);
        return new FilmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FilmViewHolder holder, int position) {
        holder.updateInterface(filmList.get(position));
    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }
}
