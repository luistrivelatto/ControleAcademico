package com.example.luigi.controleacademico.Model;

import android.support.annotation.NonNull;

/**
 * Created by Luigi on 12/12/2017.
 */

public class Data implements Comparable<Data> {

    int dia, mes, ano;

    public Data(int dia, int mes, int ano) {
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
    }

    public Data(String aaaa_mm_dd) {
        this.ano = new Integer(aaaa_mm_dd.substring(0, 4));
        this.mes = new Integer(aaaa_mm_dd.substring(5, 7));
        this.dia = new Integer(aaaa_mm_dd.substring(8, 10));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Data)) return false;

        Data data = (Data) o;

        if (dia != data.dia) return false;
        if (mes != data.mes) return false;
        return ano == data.ano;
    }

    @Override
    public int compareTo(@NonNull Data o) {
        if(ano != o.ano) return new Integer(ano).compareTo(o.ano);
        if(mes != o.mes) return new Integer(mes).compareTo(o.mes);
        return new Integer(dia).compareTo(o.dia);
    }

    @Override
    public String toString() {
        return String.format("%04d-%02d-%02d", ano, mes, dia);
    }

    public String string_dd_mm() {
        return String.format("%02d/%02d", dia, mes);
    }
}
