package com.example.luigi.controleacademico.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.luigi.controleacademico.R;

/**
 * Created by Luigi on 01/01/2018.
 */

public class DialogAlterarNota extends DialogFragment {

    DialogAlterarNotaListener callback;
    public interface DialogAlterarNotaListener {
        void onDialogPositiveClick(DialogAlterarNota dialog);
    }

    private static final String ARGUMENT_POSICAO = "ARGUMENT_POSICAO";
    private static final String ARGUMENT_NOME_AVALIACAO = "ARGUMENT_NOME_AVALIACAO";

    public static DialogAlterarNota newInstance(int posicao, String nomeAvaliacao) {
        DialogAlterarNota dialog = new DialogAlterarNota();
        Bundle args = new Bundle();
        args.putInt(ARGUMENT_POSICAO, posicao);
        args.putString(ARGUMENT_NOME_AVALIACAO, nomeAvaliacao);
        dialog.setArguments(args);
        return dialog;
    }

    int getPosicao() {
        return getArguments().getInt(ARGUMENT_POSICAO);
    }

    View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callback = (DialogAlterarNotaListener) getTargetFragment();
        } catch(ClassCastException e) {
            throw new ClassCastException(getTargetFragment().toString() + " must implement DialogAlterarNotaListener");
        }
    }

    // Na primeira vez em que o Dialog é mostrado, o callback é setado no método 'onAttach'.
    // Nas vezes seguintes (por exemplo, após rotacionar o celular), o fragment pai (no caso, ListaAvaliacoesFragment)
    // confere se o Dialog existe no FragmentManager; se sim, o fragment pai se coloca como callback chamando 'setCallback'.
    // É uma gambiarra? Pra mim toda essa forma de comunicação usando targetFragment é uma gambiarra, mas é assim
    // que o developer.android.com recomenda. Dentro disso achei essa solução até elegante.
    public void setCallback(DialogAlterarNotaListener callback) {
        this.callback = callback;

        // O TargetFragment do dialog é um Fragment que foi invalidado (por exemplo devido a rotação), então
        // precisamos setar como null para que o app não crashe ao salvar o estado do dialog
        // procurando o Fragment invalidado no Fragment Manager
        setTargetFragment(null, 0);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        view = inflater.inflate(R.layout.dialog_alterar_nota, null);

        String nomeAvaliacao = getArguments().getString(ARGUMENT_NOME_AVALIACAO);

        builder.setView(view).
                setTitle("Alterar Nota - " + nomeAvaliacao).
                setPositiveButton("Alterar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onDialogPositiveClick(DialogAlterarNota.this);
                    }
                }).
                setNegativeButton("Voltar", null);

        return builder.create();
    }

    String strNota;

    boolean checkForm() {
        strNota = ((EditText) view.findViewById(R.id.form_nota)).getText().toString();

        return getErros().equals("");
    }

    String getErros() {
        String erros = "";

        if(!strNota.isEmpty()) {
            try {
                Double.parseDouble(strNota);
            } catch (NumberFormatException e) {
                erros += "- Nota deve ser um número (opcionalmente com ponto).\n";
            }
        }

        return erros;
    }

    String getStrNota() {
        return strNota;
    }
}
