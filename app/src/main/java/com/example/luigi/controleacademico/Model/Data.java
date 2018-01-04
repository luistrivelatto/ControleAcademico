package com.example.luigi.controleacademico.Model;

import android.support.annotation.NonNull;

import static com.example.luigi.controleacademico.Constants.LOCALE_BRAZIL;

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
        this.ano = Integer.valueOf(aaaa_mm_dd.substring(0, 4));
        this.mes = Integer.valueOf(aaaa_mm_dd.substring(5, 7));
        this.dia = Integer.valueOf(aaaa_mm_dd.substring(8, 10));
    }

    public int getDia() {
        return dia;
    }

    public int getMes() {
        return mes;
    }

    public int getAno() {
        return ano;
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
        if(ano != o.ano) return Integer.valueOf(ano).compareTo(o.ano);
        if(mes != o.mes) return Integer.valueOf(mes).compareTo(o.mes);
        return Integer.valueOf(dia).compareTo(o.dia);
    }

    @Override
    public String toString() {
        return String.format(LOCALE_BRAZIL, "%04d-%02d-%02d", ano, mes, dia);
    }

    public String string_dd_mm() {
        return String.format(LOCALE_BRAZIL, "%02d/%02d", dia, mes);
    }
}
