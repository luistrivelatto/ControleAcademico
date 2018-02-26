package com.example.luigi.controleacademico.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.luigi.controleacademico.BD.BDCore;
import com.example.luigi.controleacademico.Constants;
import com.example.luigi.controleacademico.Model.Disciplina;
import com.example.luigi.controleacademico.R;

import java.util.TreeSet;

public class MainActivity extends AppCompatActivity implements DialogNovaDisciplina.DialogNovaDisciplinaListener {

    BDCore bd;
    ListView listaDisciplinas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.assertConstants(getResources());

        setContentView(R.layout.activity_main);

        bd = new BDCore(this);

        listaDisciplinas = findViewById(R.id.lista_disciplinas);

        ListaDisciplinasAdapter adapter = new ListaDisciplinasAdapter(this);
        listaDisciplinas.setAdapter(adapter);
        listaDisciplinas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Disciplina disc = (Disciplina) parent.getItemAtPosition(position);
                Intent disciplinaIntent = DisciplinaActivity.getStartIntent(getBaseContext(), disc.getId(), disc.getNome());
                startActivity(disciplinaIntent);
            }
        });

        listaDisciplinas.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listaDisciplinas.setMultiChoiceModeListener(new ListaDisciplinasMultiChoiceModeListener(adapter));
    }

    void updateUI() {
        ListaDisciplinasAdapter adapter = (ListaDisciplinasAdapter) listaDisciplinas.getAdapter();
        adapter.clear();
        adapter.addAll(bd.queryDisciplinas());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_nova_disciplina:
                addNovaDisciplina();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    void addNovaDisciplina() {
        DialogNovaDisciplina dialog = DialogNovaDisciplina.newInstance();
        dialog.show(getSupportFragmentManager(), "DialogNovaDisciplina");
    }

    @Override
    public void onDialogPositiveClick(DialogNovaDisciplina dialog) {
        if(dialog.checkForm()) {
            bd.insertDisciplina(dialog.getDisciplina());
            updateUI();
        } else {
            String mensagem = dialog.getErros();
            new AlertDialog.Builder(this).setMessage(mensagem).setPositiveButton("Voltar", null).show();
        }
    }

    private class ListaDisciplinasAdapter extends ArrayAdapter<Disciplina> {

        TreeSet<Integer> selecionados;

        ListaDisciplinasAdapter(Context context) {
            super(context, R.layout.item_lista_disciplinas);
            selecionados = new TreeSet<>();
        }

        void apagarSelecionados() {
            for(Integer pos : selecionados) {
                bd.deleteDisciplina(getItem(pos));
            }
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_lista_disciplinas, parent, false);
            }

            Disciplina disc = getItem(position);

            int faltas = disc.calcularFaltas();
            int tvFaltasCor;

            if(faltas > disc.getMaxFaltas()) {
                tvFaltasCor = Constants.corNumFaltasEstorou;
            } else if(faltas >= disc.getMaxFaltas() - 10) {
                tvFaltasCor = Constants.corNumFaltasPertoLimite;
            } else {
                tvFaltasCor = Constants.corNumFaltasNormal;
            }

            ((TextView) convertView.findViewById(R.id.nome)).setText(disc.getNome());

            TextView tvFaltas = convertView.findViewById(R.id.faltas);
            tvFaltas.setText("" + faltas);
            tvFaltas.setTextColor(tvFaltasCor);

            ((TextView) convertView.findViewById(R.id.faltas)).setText("" + disc.calcularFaltas());
            ((TextView) convertView.findViewById(R.id.max_faltas)).setText("" + disc.getMaxFaltas());
            ((TextView) convertView.findViewById(R.id.media)).setText("" + Math.round(disc.calcularMedia()));

            convertView.setBackgroundColor(selecionados.contains(position) ? Constants.corItemSelecionado : Constants.corItemNaoSelecionado);

            return convertView;
        }
    }

    private class ListaDisciplinasMultiChoiceModeListener implements AbsListView.MultiChoiceModeListener {

        ListaDisciplinasAdapter adapter;

        ListaDisciplinasMultiChoiceModeListener(ListaDisciplinasAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            if(checked) {
                adapter.selecionados.add(position);
            } else {
                adapter.selecionados.remove(position);
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.context_menu_listview_apagar, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch(item.getItemId()) {
                case R.id.menu_apagar:
                    adapter.apagarSelecionados();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.selecionados.clear();
            adapter.notifyDataSetChanged();
            updateUI();
        }
    }
}
