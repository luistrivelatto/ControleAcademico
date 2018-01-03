package com.example.luigi.controleacademico;

import android.content.res.Resources;
import android.support.v4.content.res.ResourcesCompat;

import junit.framework.Assert;

/**
 * Created by Luigi on 02/01/2018.
 */

public class Constants {

    public static final int corFrequenciaPresente = 0xFF6fd66f;
    public static final int corFrequenciaAusente = 0xFFf77773;
    public static final int corItemNaoSelecionado = 0xFFFFFFFF;
    public static final int corItemSelecionado = 0xFFbebebe;

    public static void assertConstants(Resources res) {
        Assert.assertEquals(corFrequenciaPresente, ResourcesCompat.getColor(res, R.color.frequenciaPresente, null));
        Assert.assertEquals(corFrequenciaAusente, ResourcesCompat.getColor(res, R.color.frequenciaAusente, null));
        Assert.assertEquals(corItemNaoSelecionado, ResourcesCompat.getColor(res, R.color.itemNaoSelecionado, null));
        Assert.assertEquals(corItemSelecionado, ResourcesCompat.getColor(res, R.color.itemSelecionado, null));
    }

}
