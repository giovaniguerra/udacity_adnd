package br.com.gbguerra.popularfilms.viewholders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import br.com.gbguerra.popularfilms.R;
import br.com.gbguerra.popularfilms.activities.FilmDetailActivity;
import br.com.gbguerra.popularfilms.models.Film;

/**
 * Created by Giovani Guerra on 07/11/2016.
 */

public class FilmViewHolder extends RecyclerView.ViewHolder {

    private Film film;
    private TextView title;
    private ImageView image;
    private View view;
    private Context ctx;

    public FilmViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        ctx = view.getContext();

        title = (TextView) view.findViewById(R.id.film_title);
        image = (ImageView) view.findViewById(R.id.film_image);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ctx, FilmDetailActivity.class)
                        .putExtra("film", film);
                ctx.startActivity(intent);
            }
        });
    }

    public void updateInterface(Film film) {
        this.film = film;
        title.setText(film.getTitle());



        Glide.with(view.getContext())
                .load(film.getPosterUrl())
                .fitCenter()
                .into(image);

    }

}
