package com.example.luigi.controleacademico;

import android.content.res.Resources;
import android.support.v4.content.res.ResourcesCompat;

import junit.framework.Assert;

import java.util.Locale;

/**
 * Created by Luigi on 02/01/2018.
 */

public class Constants {

    public static final Locale LOCALE_BRAZIL = new Locale("pt", "BR");

    public static final int corFrequenciaPresente = 0xFF6fd66f;
    public static final int corFrequenciaAusente = 0xFFf77773;
    public static final int corItemNaoSelecionado = 0xFFFFFFFF;
    public static final int corItemSelecionado = 0xFFbebebe;
    public static final int corNumFaltasNormal = 0xFF808080;
    public static final int corNumFaltasPertoLimite = 0xFFc97808;
    public static final int corNumFaltasEstorou = 0xFFFF0000;

    public static void assertConstants(Resources res) {
        // po, olha o tamanho dessa expressão pra pegar a cor do resources, eu sei que não faz sentido reescrever
        // as constantes aqui mas vai assim, pelo menos o código fica mais legível
        Assert.assertEquals(corFrequenciaPresente, ResourcesCompat.getColor(res, R.color.frequenciaPresente, null));
        Assert.assertEquals(corFrequenciaAusente, ResourcesCompat.getColor(res, R.color.frequenciaAusente, null));
        Assert.assertEquals(corItemNaoSelecionado, ResourcesCompat.getColor(res, R.color.itemNaoSelecionado, null));
        Assert.assertEquals(corItemSelecionado, ResourcesCompat.getColor(res, R.color.itemSelecionado, null));
        Assert.assertEquals(corNumFaltasNormal, ResourcesCompat.getColor(res, R.color.numFaltasNormal, null));
        Assert.assertEquals(corNumFaltasPertoLimite, ResourcesCompat.getColor(res, R.color.numFaltasPertoLimite, null));
        Assert.assertEquals(corNumFaltasEstorou, ResourcesCompat.getColor(res, R.color.numFaltasEstorou, null));
    }

}
