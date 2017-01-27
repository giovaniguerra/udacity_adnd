package br.com.gbguerra.popularfilms.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.gbguerra.popularfilms.R;
import br.com.gbguerra.popularfilms.fragments.FilmFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main, new FilmFragment())
                    .commit();
        }

    }

}
