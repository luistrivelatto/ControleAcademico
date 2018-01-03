package com.example.luigi.controleacademico.Model;

/**
 * Created by Luigi on 12/12/2017.
 */

public class Avaliacao {

    private String nome;
    private Data data;
    private double nota, escala, peso;
    private boolean realizada;

    public Avaliacao(String nome, Data data, double nota, double peso, boolean realizada, double escala) {
        this.nome = nome;
        this.data = data;
        this.nota = nota;
        this.peso = peso;
        this.realizada = realizada;
        this.escala = escala;
    }

    public Avaliacao(String nome, Data data, double nota, double peso, boolean realizada) {
        this(nome, data, nota, peso, realizada, 100);
    }

    public Avaliacao(Avaliacao av) {
        this(av.nome, av.data, av.nota, av.peso, av.realizada, av.escala);
    }

    public String getNome() {
        return nome;
    }

    public Data getData() {
        return data;
    }

    public double getNota() {
        return nota;
    }

    public double getPeso() {
        return peso;
    }

    public double getEscala() { return escala; }

    public boolean isRealizada() {
        return realizada;
    }

    public void setNota(double nota) {
        this.nota = nota;
        this.realizada = true;
    }

    public void setNaoRealizada() {
        this.realizada = false;
    }
}
