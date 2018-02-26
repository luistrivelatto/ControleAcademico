package com.example.luigi.controleacademico.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.luigi.controleacademico.Model.Avaliacao;
import com.example.luigi.controleacademico.Model.Data;
import com.example.luigi.controleacademico.R;

import java.util.Calendar;

/**
 * Created by Luigi on 01/01/2018.
 */

public class DialogNovaAvaliacao extends DialogFragment {

    DialogNovaAvaliacaoListener callback;
    public interface DialogNovaAvaliacaoListener {
        void onDialogPositiveClick(DialogNovaAvaliacao dialog);
    }

    public static DialogNovaAvaliacao newInstance() {
        return new DialogNovaAvaliacao();
    }

    View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callback = (DialogNovaAvaliacaoListener) getTargetFragment();
        } catch(ClassCastException e) {
            throw new ClassCastException(getTargetFragment().toString() + " must implement DialogNovaAvaliacaoListener");
        }
    }

    public void setCallback(DialogNovaAvaliacaoListener callback) {
        this.callback = callback;
        setTargetFragment(null, 0);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        view = inflater.inflate(R.layout.dialog_nova_avaliacao, null);

        builder.setView(view).
                setTitle("Nova Avaliação").
                setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onDialogPositiveClick(DialogNovaAvaliacao.this);
                    }
                }).
                setNegativeButton("Cancelar", null);

        return builder.create();
    }

    String strNome, strData, strEscala, strPeso;

    boolean checkForm() {
        strNome = ((EditText) view.findViewById(R.id.form_avaliacao_nome)).getText().toString();
        strData = ((EditText) view.findViewById(R.id.form_avaliacao_data)).getText().toString();
        strEscala = ((EditText) view.findViewById(R.id.form_avaliacao_escala)).getText().toString();
        strPeso = ((EditText) view.findViewById(R.id.form_avaliacao_peso)).getText().toString();

        return getErros().equals("");
    }

    String getErros() {
        String erros = "";

        if(strNome.isEmpty()) {
            erros += "- Nome não pode ser vazio.\n";
        }
        if(!strData.matches("\\d\\d.\\d\\d") && !strData.matches("\\d\\d/\\d\\d")) {
            erros += "- Data deve estar no formato dd.mm ou dd/mm.\n";
        }

        if(strEscala.isEmpty()) {
            strEscala = "100";
        } else {
            try {
                Double.parseDouble(strEscala);
            } catch (NumberFormatException e) {
                erros += "- Escala deve ser um número (opcionalmente com ponto).\n";
            }
        }

        try {
            double d = Double.parseDouble(strPeso);
            if(d > 100) {
                erros += "- Peso não pode ser maior que 100.\n";
            }
        } catch(NumberFormatException e) {
            erros += "- Peso deve ser um número (opcionalmente com ponto).\n";
        }

        return erros;
    }

    Avaliacao getAvaliacao() {
        String nome = strNome;

        int dia = Integer.parseInt(strData.substring(0, 2));
        int mes = Integer.parseInt(strData.substring(3, 5));
        int ano = Calendar.getInstance().get(Calendar.YEAR);
        Data data = new Data(dia, mes, ano);

        double escala = Double.parseDouble(strEscala);
        double peso = Double.parseDouble(strPeso);

        return new Avaliacao(nome, data, 0, peso, false, escala);
    }
}
