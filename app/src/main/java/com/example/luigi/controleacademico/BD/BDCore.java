package com.example.luigi.controleacademico.BD;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.luigi.controleacademico.Model.Avaliacao;
import com.example.luigi.controleacademico.Model.Data;
import com.example.luigi.controleacademico.Model.Disciplina;
import com.example.luigi.controleacademico.Model.Frequencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Luigi on 12/12/2017.
 */

public class BDCore extends SQLiteOpenHelper {

    private static final boolean DEBUG_POPULATE_DATABASE = false;

    private static final String NOME_BD = "controle";
    private static final int VERSAO_BD = 20;

    public BDCore(Context ctx) {
        super(ctx, NOME_BD, null, VERSAO_BD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int oldVersion, int newVersion) {
        bd.execSQL("DROP TABLE IF EXISTS `" + DisciplinaBD.TABLE_NAME + "`;");
        bd.execSQL("DROP TABLE IF EXISTS `" + AvaliacaoBD.TABLE_NAME + "`;");
        bd.execSQL("DROP TABLE IF EXISTS `" + FrequenciaBD.TABLE_NAME + "`;");
        onCreate(bd);
    }

    @Override
    public void onCreate(SQLiteDatabase bd) {
        bd.execSQL(DisciplinaBD.CREATE_TABLE);
        bd.execSQL(AvaliacaoBD.CREATE_TABLE);
        bd.execSQL(FrequenciaBD.CREATE_TABLE);

        if(DEBUG_POPULATE_DATABASE) {
            Disciplina[] disciplinas = arrayDisciplinasTeste();
            for (Disciplina d : disciplinas) {
                insertDisciplina(d, bd);
            }
        }
    }

    public void insertDisciplina(Disciplina disc) {
        SQLiteDatabase bd = getWritableDatabase();
        insertDisciplina(disc, bd);
        bd.close();
    }

    public void insertDisciplina(Disciplina disc, SQLiteDatabase bd) {
        DisciplinaBD.insertDisciplina(bd, disc);
    }

    long getId(Avaliacao av, long idDisciplina) {
        SQLiteDatabase bd = getReadableDatabase();
        long ret = AvaliacaoBD.getId(bd, av, idDisciplina);
        bd.close();
        return ret;
    }

    public void insertAvaliacao(Avaliacao av, long idDisciplina) {
        SQLiteDatabase bd = getWritableDatabase();
        insertAvaliacao(av, idDisciplina, bd);
        bd.close();
    }

    private void insertAvaliacao(Avaliacao av, long idDisciplina, SQLiteDatabase bd) {
        AvaliacaoBD.insertAvaliacao(bd, av, idDisciplina);
    }

    public void updateAvaliacao(Avaliacao old, Avaliacao novo, long idDisciplina) {
        long id = getId(old, idDisciplina);
        SQLiteDatabase bd = getWritableDatabase();
        AvaliacaoBD.updateAvaliacao(bd, novo, id);
        bd.close();
    }

    public void deleteAvaliacao(Avaliacao av, long idDisciplina) {
        long id = getId(av, idDisciplina);
        SQLiteDatabase bd = getWritableDatabase();
        AvaliacaoBD.deleteAvaliacao(bd, id);
        bd.close();
    }

    long getId(Frequencia freq, long idDisciplina) {
        SQLiteDatabase bd = getReadableDatabase();
        long ret = FrequenciaBD.getId(bd, freq, idDisciplina);
        bd.close();
        return ret;
    }

    public void insertFrequencia(Frequencia freq, long idDisciplina) {
        SQLiteDatabase bd = getWritableDatabase();
        insertFrequencia(freq, idDisciplina, bd);
        bd.close();
    }

    void insertFrequencia(Frequencia freq, long idDisciplina, SQLiteDatabase bd) {
        FrequenciaBD.insertFrequencia(bd, freq, idDisciplina);
    }

    public void deleteFrequencia(Frequencia freq, long idDisciplina) {
        long id = getId(freq, idDisciplina);
        SQLiteDatabase bd = getWritableDatabase();
        FrequenciaBD.deleteFrequencia(bd, id);
        bd.close();
    }

    public List<Disciplina> queryDisciplinas() {
        final SQLiteDatabase bd = getReadableDatabase();
        List<Disciplina> ret = DisciplinaBD.queryDisciplinas(bd);
        bd.close();
        return ret;
    }

    public Disciplina getDisciplina(long id) {
        final SQLiteDatabase bd = getReadableDatabase();
        Disciplina ret = DisciplinaBD.getDisciplina(bd, id);
        bd.close();
        return ret;
    }

    public void deleteDisciplina(Disciplina disc) {
        SQLiteDatabase bd = getWritableDatabase();
        DisciplinaBD.deleteDisciplina(bd, disc.getId());
        bd.close();
    }

    protected static <T> List<T> query(String queryStr, SQLiteDatabase bd, BDReadable<T> construtor) {
        Cursor c = bd.rawQuery(queryStr, null);

        ArrayList<T> ret = new ArrayList<>();
        while(c.moveToNext()) {
            ret.add(construtor.getObject(c));
        }
        c.close();

        return ret;
    }

    protected interface BDReadable<T> {
        T getObject(Cursor c);
    }

    Disciplina[] arrayDisciplinasTeste() {
        Disciplina compiladores = new Disciplina("Compiladores", 25,
                new ArrayList<>(Arrays.<Avaliacao>asList(
                        new Avaliacao("Trabalho 1", new Data(14, 8, 2017), 5, 5, true, 5),
                        new Avaliacao("Prova 1",    new Data(28, 8, 2017), 9, 20, true, 20),
                        new Avaliacao("Trabalho 2", new Data(12, 9, 2017), 9, 10, true, 10),
                        new Avaliacao("Trabalho 3", new Data(31, 10, 2017), 10, 10, true, 10),
                        new Avaliacao("Prova 2",    new Data(16, 11, 2017), 14, 20, true, 20),
                        new Avaliacao("Prova 3",    new Data(12, 12, 2017), 0, 20, false, 20),
                        new Avaliacao("Trabalho 4", new Data(19, 12, 2017), 0, 15, false, 15)
                )),
                new ArrayList<>(Arrays.<Frequencia>asList(
                        new Frequencia(new Data(12, 12, 2017), "08:00", "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz", true),
                        new Frequencia(new Data(13, 12, 2017), "08:00", "obs2", true),
                        new Frequencia(new Data(14, 12, 2017), "08:00", "obs3", false),
                        new Frequencia(new Data(15, 12, 2017), "08:00", "obs4", true),
                        new Frequencia(new Data(15, 12, 2017), "08:00", "obs4", true),
                        new Frequencia(new Data(15, 12, 2017), "08:00", "obs4", false),
                        new Frequencia(new Data(15, 12, 2017), "08:00", "obs4", false),
                        new Frequencia(new Data(15, 12, 2017), "08:00", "obs4", true),
                        new Frequencia(new Data(15, 12, 2017), "08:00", "obs4", false),
                        new Frequencia(new Data(15, 12, 2017), "08:00", "obs4", true),
                        new Frequencia(new Data(15, 12, 2017), "08:00", "obs4", true),
                        new Frequencia(new Data(15, 12, 2017), "08:00", "obs4", true)
                ))
        );

        Disciplina redes = new Disciplina("Redes de Computadores",  34,
                new ArrayList<>(Arrays.<Avaliacao>asList(
                        new Avaliacao("Prova 1", new Data(15, 5, 2017), 68, 13.333, true),
                        new Avaliacao("Prova 2", new Data(12, 6, 2017), 83, 13.333, true),
                        new Avaliacao("Prova 3", new Data(1, 8, 2017), 46, 13.333, true),
                        new Avaliacao("Prova 4", new Data(25, 8, 2017), 51, 13.333, true),
                        new Avaliacao("Relatórios", new Data(19, 9, 2017), 40, 13.333, true),
                        new Avaliacao("Trabalho prático", new Data(20, 9, 2017), 92, 13.333, true),
                        new Avaliacao("Prova 5", new Data(25, 9, 2017), 80, 20, true)
                )),
                new ArrayList<Frequencia>()
        );

        Disciplina pid = new Disciplina("Processamento de Imagens Digitais", 17,
                new ArrayList<>(Arrays.<Avaliacao>asList(
                        new Avaliacao("Extra 1", new Data(6, 6, 2017), 70, 2, true),
                        new Avaliacao("Prova 1", new Data(4, 7, 2017), 73, 20, true),
                        new Avaliacao("Trabalho 1", new Data(1, 8, 2017), 100, 15, true),
                        new Avaliacao("Prova 2", new Data(3, 11, 2017), 100, 25, true),
                        new Avaliacao("Trabalho 2", new Data(10, 12, 2017), 0, 15, false),
                        new Avaliacao("Prova 3", new Data(11, 12, 2017), 0, 25, false)
                )),
                new ArrayList<Frequencia>()
        );

        Disciplina disciplinas[] = new Disciplina[] {
                compiladores,
                redes,
                pid
        };

        return disciplinas;
    }
}