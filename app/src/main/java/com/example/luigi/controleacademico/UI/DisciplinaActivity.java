package com.example.luigi.controleacademico.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.luigi.controleacademico.R;

public class DisciplinaActivity extends AppCompatActivity {

    int idDisciplina;

    private static final String EXTRA_ID_DISCIPLINA = "EXTRA_ID_DISCIPLINA";
    private static final String EXTRA_NOME_DISCIPLINA = "EXTRA_NOME_DISCIPLINA";

    public static Intent getStartIntent(Context context, int idDisciplina, String nomeDisciplina) {
        return new Intent(context, DisciplinaActivity.class)
                .putExtra(EXTRA_ID_DISCIPLINA, idDisciplina)
                .putExtra(EXTRA_NOME_DISCIPLINA, nomeDisciplina);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplina);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent intent = getIntent();
        if(intent == null || !intent.hasExtra(EXTRA_ID_DISCIPLINA) || !intent.hasExtra(EXTRA_NOME_DISCIPLINA)) {
            return;
        }

        getSupportActionBar().setTitle(intent.getStringExtra(EXTRA_NOME_DISCIPLINA));
        idDisciplina = intent.getIntExtra(EXTRA_ID_DISCIPLINA, 0);

        navigation.setSelectedItemId(R.id.navigation_aulas);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_aulas:
                    changeFragment(ListaFrequenciaFragment.newInstance(idDisciplina));
                    return true;
                case R.id.navigation_notas:
                    changeFragment(ListaAvaliacoesFragment.newInstance(idDisciplina));
                    return true;
            }
            return false;
        }
    };

    private void changeFragment(Fragment newFragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, newFragment).commitNow();
    }
}
