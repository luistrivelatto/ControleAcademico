package com.example.luigi.controleacademico.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luigi.controleacademico.BD.BDCore;
import com.example.luigi.controleacademico.Constants;
import com.example.luigi.controleacademico.Model.Disciplina;
import com.example.luigi.controleacademico.Model.Frequencia;
import com.example.luigi.controleacademico.R;

import java.util.TreeSet;

public class ListaFrequenciaFragment extends Fragment implements DialogNovaFrequencia.DialogNovaFrequenciaListener {

    int idDisciplina;
    BDCore bd;

    View view;
    TextView tvFaltas, tvMaxFaltas;
    ImageButton botaoAddFrequencia;
    ListView listaFrequencias;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListaFrequenciaFragment() {
    }

    private static final String ARGUMENT_ID_DISCIPLINA = "ARGUMENT_ID_DISCIPLINA";
    private static final String TAG_DIALOG_NOVA_FREQUENCIA = "TAG_DIALOG_NOVA_FREQUENCIA";

    public static ListaFrequenciaFragment newInstance(int idDisciplina) {
        ListaFrequenciaFragment fragment = new ListaFrequenciaFragment();
        Bundle args = new Bundle();
        args.putInt(ARGUMENT_ID_DISCIPLINA, idDisciplina);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idDisciplina = getArguments().getInt(ARGUMENT_ID_DISCIPLINA);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lista_frequencia, container, false);

        bd = new BDCore(getContext());

        botaoAddFrequencia = view.findViewById(R.id.button_add_frequencia);
        botaoAddFrequencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogNovaFrequencia dialog = DialogNovaFrequencia.newInstance();
                dialog.setTargetFragment(ListaFrequenciaFragment.this, 0);
                dialog.show(getActivity().getSupportFragmentManager(), TAG_DIALOG_NOVA_FREQUENCIA);
            }
        });

        tvFaltas = view.findViewById(R.id.faltas);
        tvMaxFaltas = view.findViewById(R.id.max_faltas);

        listaFrequencias = view.findViewById(R.id.lista_frequencias);

        ListaFrequenciaAdapter adapter = new ListaFrequenciaAdapter(getContext());
        listaFrequencias.setAdapter(adapter);
        listaFrequencias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Frequencia freq = (Frequencia) parent.getItemAtPosition(position);
                if(freq.hasObservacoesAbreviado()) {
                    Toast.makeText(getContext(), freq.getObservacoes(), Toast.LENGTH_LONG).show();
                }
            }
        });

        listaFrequencias.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listaFrequencias.setMultiChoiceModeListener(new ListaFrequenciaMultiChoiceModeListener(adapter));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        FragmentManager fm = getActivity().getSupportFragmentManager();

        DialogNovaFrequencia dialogNovaFrequencia = (DialogNovaFrequencia) fm.findFragmentByTag(TAG_DIALOG_NOVA_FREQUENCIA);
        if(dialogNovaFrequencia != null) {
            dialogNovaFrequencia.setCallback(this);
        }
    }

    void updateUI() {
        Disciplina disc = bd.getDisciplina(idDisciplina);

        tvFaltas.setText("Faltas: " + disc.calcularFaltas());
        tvMaxFaltas.setText("Máx.: " + disc.getMaxFaltas());

        ListaFrequenciaAdapter adapter = (ListaFrequenciaAdapter) listaFrequencias.getAdapter();
        adapter.clear();
        adapter.addAll(disc.getFrequencias());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogPositiveClick(DialogNovaFrequencia dialog) {
        if(dialog.checkForm()) {
            bd.insertFrequencia(dialog.getFrequencia(), idDisciplina);
            updateUI();

            View view = getLayoutInflater().inflate(R.layout.dialog_imagem, null);
            ((ImageView) view.findViewById(R.id.imagem_dialog)).setImageResource(
                    dialog.getFrequencia().isFrequentou() ? R.mipmap.presenca : R.mipmap.falta
            );
            new AlertDialog.Builder(getContext()).setView(view).show();

        } else {
            String mensagem = dialog.getErros();
            new AlertDialog.Builder(getContext()).setMessage(mensagem).setPositiveButton("Voltar", null).show();
        }
    }

    private class ListaFrequenciaAdapter extends ArrayAdapter<Frequencia> {

        TreeSet<Integer> selecionados;

        ListaFrequenciaAdapter(Context context) {
            super(context, R.layout.item_lista_frequencia);
            selecionados = new TreeSet<>();
        }

        void apagarSelecionados() {
            for(Integer pos : selecionados) {
                bd.deleteFrequencia(getItem(pos), idDisciplina);
            }
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_lista_frequencia, parent, false);
            }

            final Frequencia freq = getItem(position);

            int cor = freq.isFrequentou() ? Constants.corFrequenciaPresente : Constants.corFrequenciaAusente;
            if(selecionados.contains(position)) {
                cor = Constants.corItemSelecionado;
            }

            convertView.setBackgroundColor(cor);

            ((TextView) convertView.findViewById(R.id.data)).setText(freq.getData().string_dd_mm());
            ((TextView) convertView.findViewById(R.id.horario)).setText(freq.getHorario());
            ((TextView) convertView.findViewById(R.id.presenca)).setText(freq.getFrequenciaString());
            ((TextView) convertView.findViewById(R.id.observacoes)).setText(freq.getObservacoesAbreviado());

            return convertView;
        }
    }

    private class ListaFrequenciaMultiChoiceModeListener implements AbsListView.MultiChoiceModeListener {

        ListaFrequenciaAdapter adapter;

        ListaFrequenciaMultiChoiceModeListener(ListaFrequenciaAdapter adapter) {
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
