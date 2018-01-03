package com.example.luigi.controleacademico.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.ListView;
import android.widget.TextView;

import com.example.luigi.controleacademico.Constants;
import com.example.luigi.controleacademico.Model.Avaliacao;
import com.example.luigi.controleacademico.BD.BDCore;
import com.example.luigi.controleacademico.Model.Disciplina;
import com.example.luigi.controleacademico.R;

import java.util.TreeSet;

public class ListaAvaliacoesFragment extends Fragment implements DialogNovaAvaliacao.DialogNovaAvaliacaoListener,
        DialogAlterarNota.DialogAlterarNotaListener {

    int idDisciplina;
    BDCore bd;

    View root;
    ImageButton botaoAddAvaliacao;
    TextView tvMedia;
    ListView listaAvaliacoes;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListaAvaliacoesFragment() {
    }

    private static final String ARGUMENT_ID_DISCIPLINA = "ARGUMENT_ID_DISCIPLINA";

    public static ListaAvaliacoesFragment newInstance(int idDisciplina) {
        ListaAvaliacoesFragment fragment = new ListaAvaliacoesFragment();
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
        root = inflater.inflate(R.layout.fragment_lista_avaliacoes, container, false);
        bd = new BDCore(getContext());

        botaoAddAvaliacao = root.findViewById(R.id.button_add_avaliacao);
        botaoAddAvaliacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogNovaAvaliacao dialog = DialogNovaAvaliacao.newInstance();
                dialog.setTargetFragment(ListaAvaliacoesFragment.this, 0);
                dialog.show(getActivity().getSupportFragmentManager(), "DialogNovaAvaliacao");
            }
        });

        tvMedia = root.findViewById(R.id.media);

        listaAvaliacoes = root.findViewById(R.id.lista_avaliacoes);

        ListaAvaliacaoAdapter adapter = new ListaAvaliacaoAdapter(getContext());
        listaAvaliacoes.setAdapter(adapter);
        listaAvaliacoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Avaliacao av = (Avaliacao) parent.getItemAtPosition(position);
                DialogAlterarNota dialog = DialogAlterarNota.newInstance(position, av.getNome());
                dialog.setTargetFragment(ListaAvaliacoesFragment.this, 0);
                dialog.show(getActivity().getSupportFragmentManager(), "DialogAlterarNota");
            }
        });

        listaAvaliacoes.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listaAvaliacoes.setMultiChoiceModeListener(new ListaAvaliacaoMultiChoiceModeListener(adapter));

        updateUI();

        return root;
    }

    void updateUI() {
        Disciplina disc = bd.getDisciplina(idDisciplina);

        tvMedia.setText("Média: " + Math.round(disc.calcularMedia()));

        ListaAvaliacaoAdapter adapter = (ListaAvaliacaoAdapter) listaAvaliacoes.getAdapter();
        adapter.clear();
        adapter.addAll(disc.getAvaliacoes());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogPositiveClick(DialogNovaAvaliacao dialog) {
        if(dialog.checkForm()) {
            bd.insertAvaliacao(dialog.getAvaliacao(), idDisciplina);
            updateUI();
        } else {
            String mensagem = dialog.getErros();
            new AlertDialog.Builder(getContext()).setMessage(mensagem).setPositiveButton("Voltar", null).show();
        }

    }

    @Override
    public void onDialogPositiveClick(DialogAlterarNota dialog) {
        if(dialog.checkForm()) {
            Avaliacao old = (Avaliacao) listaAvaliacoes.getAdapter().getItem(dialog.getPosicao());
            Avaliacao novo = new Avaliacao(old);

            String strNota = dialog.getStrNota();

            if(strNota.isEmpty()) {
                novo.setNaoRealizada();
            } else {
                novo.setNota(Double.parseDouble(strNota));
            }

            bd.updateAvaliacao(old, novo, idDisciplina);
            updateUI();
        } else {
            String mensagem = dialog.getErros();
            new AlertDialog.Builder(getContext()).setMessage(mensagem).setPositiveButton("Voltar", null).show();
        }
    }

    private class ListaAvaliacaoAdapter extends ArrayAdapter<Avaliacao> {

        TreeSet<Integer> selecionados;

        ListaAvaliacaoAdapter(Context context) {
            super(context, R.layout.item_lista_avaliacoes);
            selecionados = new TreeSet<>();
        }

        void apagarSelecionados() {
            for(Integer pos : selecionados) {
                bd.deleteAvaliacao(getItem(pos), idDisciplina);
            }
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_lista_avaliacoes, parent, false);
            }

            final Avaliacao av = getItem(position);

            ((TextView) convertView.findViewById(R.id.nome_avaliacao)).setText(av.getNome());
            ((TextView) convertView.findViewById(R.id.data)).setText(av.getData().string_dd_mm());
            ((TextView) convertView.findViewById(R.id.escala)).setText("" + Math.round(av.getEscala()));
            ((TextView) convertView.findViewById(R.id.nota)).setText(av.isRealizada() ? "" + Math.round(av.getNota()) : "");

            convertView.setBackgroundColor(selecionados.contains(position) ? Constants.corItemSelecionado : Constants.corItemNaoSelecionado);

            return convertView;
        }
    }

    private class ListaAvaliacaoMultiChoiceModeListener implements AbsListView.MultiChoiceModeListener {

        ListaAvaliacaoAdapter adapter;

        ListaAvaliacaoMultiChoiceModeListener(ListaAvaliacaoAdapter adapter) {
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