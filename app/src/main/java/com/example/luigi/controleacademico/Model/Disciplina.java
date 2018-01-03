package com.example.luigi.controleacademico.Model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Luigi on 12/12/2017.
 */

public class Disciplina {

    private Integer id;
    private String nome;
    private int maxFaltas;
    private List<Avaliacao> avaliacoes;
    private List<Frequencia> frequencias;

    public Disciplina(Integer id, String nome, int maxFaltas, List<Avaliacao> avaliacoes, List<Frequencia> frequencias) {
        this.id = id;
        this.nome = nome;
        this.maxFaltas = maxFaltas;
        this.avaliacoes = avaliacoes;
        this.frequencias = frequencias;

        Collections.sort(this.avaliacoes, new Comparator<Avaliacao>() {
            @Override
            public int compare(Avaliacao o1, Avaliacao o2) {
                return o1.getData().compareTo(o2.getData());
            }
        });

        Collections.sort(this.frequencias, new Comparator<Frequencia>() {
            @Override
            public int compare(Frequencia o1, Frequencia o2) {
                if(!o1.getData().equals(o2.getData())) {
                    return o1.getData().compareTo(o2.getData());
                }
                return o1.getHorario().compareTo(o2.getHorario());
            }
        });
    }

    public Disciplina(String nome, int maxFaltas, List<Avaliacao> avaliacoes, List<Frequencia> frequencias) {
        this(null, nome, maxFaltas, avaliacoes, frequencias);
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getMaxFaltas() {
        return maxFaltas;
    }

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public List<Frequencia> getFrequencias() {
        return frequencias;
    }

    public int calcularFaltas() {
        int faltas = 0;

        for(Frequencia f : frequencias) {
            if(!f.isFrequentou()) {
                faltas++;
            }
        }

        return faltas;
    }

    public double calcularMedia() {
        double nota = 0;

        for(Avaliacao av : avaliacoes) {
            if(av.isRealizada()) {
                nota += av.getNota() / av.getEscala() * av.getPeso();
            }
        }

        return nota;
    }
}
