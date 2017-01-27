package br.com.gbguerra.popularfilms.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import br.com.gbguerra.popularfilms.R;
import br.com.gbguerra.popularfilms.fragments.FilmDetailFragment;
import br.com.gbguerra.popularfilms.fragments.FilmFragment;
import br.com.gbguerra.popularfilms.models.Film;

public class FilmDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Film film = getIntent().getExtras().getParcelable("film");

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.film_detail_conteiner, FilmDetailFragment.newInstance(film))
                    .commit();
        }
    }
}
