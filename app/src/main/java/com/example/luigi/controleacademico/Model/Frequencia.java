package com.example.luigi.controleacademico.Model;

/**
 * Created by Luigi on 12/12/2017.
 */

public class Frequencia {

    private Data data;
    private String horario, observacoes;
    private boolean frequentou;

    public Frequencia(Data data, String horario, String observacoes, boolean frequentou) {
        this.data = data;
        this.horario = horario;
        this.observacoes = observacoes;
        this.frequentou = frequentou;
    }

    public Data getData() {
        return data;
    }

    public String getHorario() {
        return horario;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public boolean hasObservacoesAbreviado() {
        return observacoes.length() > 24;
    }

    public String getObservacoesAbreviado() {
        return hasObservacoesAbreviado() ? observacoes.substring(0, 24) + "..." : observacoes;
    }

    public boolean isFrequentou() {
        return frequentou;
    }

    public String getFrequenciaString() {
        return frequentou ? "C" : "F";
    }
}
