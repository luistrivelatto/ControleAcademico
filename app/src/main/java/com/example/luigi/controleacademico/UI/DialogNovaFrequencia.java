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
import android.widget.Switch;

import com.example.luigi.controleacademico.Model.Data;
import com.example.luigi.controleacademico.Model.Frequencia;
import com.example.luigi.controleacademico.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Luigi on 01/01/2018.
 */

public class DialogNovaFrequencia extends DialogFragment {

    DialogNovaFrequenciaListener callback;
    public interface DialogNovaFrequenciaListener {
        void onDialogPositiveClick(DialogNovaFrequencia dialog);
    }

    public static DialogNovaFrequencia newInstance() {
        return new DialogNovaFrequencia();
    }

    View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callback = (DialogNovaFrequenciaListener) getTargetFragment();
        } catch(ClassCastException e) {
            throw new ClassCastException(getTargetFragment().toString() + " must implement DialogNovaFrequenciaListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        view = inflater.inflate(R.layout.dialog_nova_frequencia,null);

        Locale locale = new Locale("pt", "BR");
        String strData = new SimpleDateFormat("dd.MM", locale).format(Calendar.getInstance().getTime());
        ((EditText) view.findViewById(R.id.form_frequencia_data)).setText(strData);

        String strHorario = new SimpleDateFormat("HH:mm", locale).format(Calendar.getInstance().getTime());
        ((EditText) view.findViewById(R.id.form_frequencia_horario)).setText(strHorario);

        builder.setView(view).
                setTitle("Nova Frequência").
                setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onDialogPositiveClick(DialogNovaFrequencia.this);
                    }
                }).
                setNegativeButton("Cancelar", null);

        return builder.create();
    }

    String strData, strHorario, strObservacoes;
    boolean isFrequentou;

    boolean checkForm() {
        strData = ((EditText) view.findViewById(R.id.form_frequencia_data)).getText().toString();
        strHorario = ((EditText) view.findViewById(R.id.form_frequencia_horario)).getText().toString();
        strObservacoes = ((EditText) view.findViewById(R.id.form_frequencia_observacoes)).getText().toString();
        isFrequentou = ((Switch) view.findViewById(R.id.form_frequencia_presenca)).isChecked();

        return getErros().equals("");
    }

    String getErros() {
        String erros = "";

        if(!strData.matches("\\d\\d.\\d\\d") && !strData.matches("\\d\\d/\\d\\d")) {
            erros += "- Data deve estar no formato dd.mm ou dd/mm.\n";
        }
        if(!strHorario.matches("\\d\\d:\\d\\d")) {
            erros += "- Horário deve estar no formato hh:mm.\n";
        }

        return erros;
    }

    Frequencia getFrequencia() {
        int dia = Integer.parseInt(strData.substring(0, 2));
        int mes = Integer.parseInt(strData.substring(3, 5));
        int ano = Calendar.getInstance().get(Calendar.YEAR);
        Data data = new Data(dia, mes, ano);

        return new Frequencia(data, strHorario, strObservacoes, isFrequentou);
    }
}
