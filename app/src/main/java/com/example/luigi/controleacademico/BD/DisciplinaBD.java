package com.example.luigi.controleacademico.BD;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.luigi.controleacademico.Model.Avaliacao;
import com.example.luigi.controleacademico.Model.Disciplina;
import com.example.luigi.controleacademico.Model.Frequencia;

import java.util.List;

/**
 * Created by Luigi on 02/01/2018.
 */

class DisciplinaBD {

    public static final String TABLE_NAME = "disciplinas";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `" + TABLE_NAME + "` (" +
            " id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " nome TEXT," +
            " max_faltas INT" +
            ")";

    public static void insertDisciplina(SQLiteDatabase bd, Disciplina disc) {
        ContentValues disciplinaCV = new ContentValues();
        disciplinaCV.putNull("id");
        disciplinaCV.put("nome", disc.getNome());
        disciplinaCV.put("max_faltas", disc.getMaxFaltas());

        long id = bd.insert(TABLE_NAME, null, disciplinaCV);

        for(Avaliacao av : disc.getAvaliacoes()) {
            AvaliacaoBD.insertAvaliacao(bd, av, id);
        }

        for(Frequencia freq : disc.getFrequencias()) {
            FrequenciaBD.insertFrequencia(bd, freq, id);
        }
    }

    public static List<Disciplina> queryDisciplinas(final SQLiteDatabase bd) {
        String queryStr = "SELECT * FROM " + TABLE_NAME;

        return BDCore.query(queryStr, bd, new BDCore.BDReadable<Disciplina>() {
            @Override
            public Disciplina getObject(Cursor c) {
                int idDisciplina = c.getInt(0);

                int id = c.getInt(0);
                String nome = c.getString(1);
                int maxFaltas = c.getInt(2);
                List<Avaliacao> avaliacoes = AvaliacaoBD.queryAvaliacoes(bd, idDisciplina);
                List<Frequencia> frequencias = FrequenciaBD.queryFrequencias(bd, idDisciplina);

                return new Disciplina(id, nome, maxFaltas, avaliacoes, frequencias);
            }
        });
    }

    public static Disciplina getDisciplina(final SQLiteDatabase bd, long id) {
        List<Disciplina> disciplinas = queryDisciplinas(bd);

        for(Disciplina d : disciplinas) {
            if(d.getId() == id) {
                return d;
            }
        }

        return null;
    }

    public static void deleteDisciplina(SQLiteDatabase bd, long id) {
        bd.delete(TABLE_NAME, "id = ?", new String[]{"" + id});
    }
}
