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

import com.example.luigi.controleacademico.Model.Avaliacao;
import com.example.luigi.controleacademico.Model.Disciplina;
import com.example.luigi.controleacademico.Model.Frequencia;
import com.example.luigi.controleacademico.R;

import java.util.ArrayList;

/**
 * Created by Luigi on 01/01/2018.
 */

public class DialogNovaDisciplina extends DialogFragment {

    DialogNovaDisciplinaListener callback;
    public interface DialogNovaDisciplinaListener {
        void onDialogPositiveClick(DialogNovaDisciplina dialog);
    }

    public static DialogNovaDisciplina newInstance() {
        return new DialogNovaDisciplina();
    }

    View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callback = (DialogNovaDisciplinaListener) context;
        } catch(ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DialogNovaDisciplinaListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        view = inflater.inflate(R.layout.dialog_nova_disciplina, null);

        builder.setView(view).
                setTitle("Nova Disciplina").
                setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onDialogPositiveClick(DialogNovaDisciplina.this);
                    }
                }).
                setNegativeButton("Cancelar", null);

        return builder.create();
    }

    String strNome, strMaxFaltas;

    boolean checkForm() {
        strNome = ((EditText) view.findViewById(R.id.form_disciplina_nome)).getText().toString();
        strMaxFaltas = ((EditText) view.findViewById(R.id.form_disciplina_max_faltas)).getText().toString();

        return getErros().equals("");
    }

    String getErros() {
        String erros = "";

        if(strNome.isEmpty()) {
            erros += "- Nome não pode ser vazio.\n";
        }
        if(strMaxFaltas.isEmpty()) {
            erros += "- Máx. faltas não pode ser vazio.\n";
        }
        else {
            try {
                Integer.parseInt(strMaxFaltas);
            } catch(NumberFormatException e) {
                erros += " Máx. faltas deve ser um número inteiro.\n";
            }
        }

        return erros;
    }

    Disciplina getDisciplina() {
        int maxFaltas = Integer.parseInt(strMaxFaltas);

        return new Disciplina(null, strNome, maxFaltas, new ArrayList<Avaliacao>(), new ArrayList<Frequencia>());
    }
}
