package com.example.luigi.controleacademico.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
