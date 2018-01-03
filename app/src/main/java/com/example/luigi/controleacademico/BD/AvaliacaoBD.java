package com.example.luigi.controleacademico.BD;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.luigi.controleacademico.Model.Avaliacao;
import com.example.luigi.controleacademico.Model.Data;

import java.util.List;

/**
 * Created by Luigi on 02/01/2018.
 */

class AvaliacaoBD {

    public static final String TABLE_NAME = "avaliacoes";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            " id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " id_disciplina INTEGER," +
            " nome TEXT," +
            " data TEXT," +
            " nota REAL," +
            " escala REAL," +
            " peso REAL," +
            " realizada INTEGER," +
            " FOREIGN KEY(id_disciplina) REFERENCES " + DisciplinaBD.TABLE_NAME + "(id)" +
            ")";

    public static long getId(SQLiteDatabase bd, Avaliacao av, long idDisciplina) {
        Cursor c = bd.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE id_disciplina = ? AND nome = ? AND data = ?",
                new String[]{"" + idDisciplina, av.getNome(), av.getData().toString()});

        long ret = -1;
        if(c.moveToNext()) {
            ret = c.getLong(0);
        }

        c.close();
        return ret;
    }

    public static List<Avaliacao> queryAvaliacoes(SQLiteDatabase bd, int idDisciplina) {
        String queryStr = "SELECT * FROM " + TABLE_NAME + " WHERE id_disciplina = '" + idDisciplina + "'";
        return BDCore.query(queryStr, bd, new BDCore.BDReadable<Avaliacao>() {
            @Override
            public Avaliacao getObject(Cursor c) {
                String nome = c.getString(2);
                Data data = new Data(c.getString(3));
                double nota = c.getDouble(4);
                double escala = c.getDouble(5);
                double peso = c.getDouble(6);
                boolean realizada = c.getInt(7) != 0;

                return new Avaliacao(nome, data, nota, peso, realizada, escala);
            }
        });
    }

    public static void insertAvaliacao(SQLiteDatabase bd, Avaliacao av, long idDisciplina) {
        ContentValues avaliacaoCV = new ContentValues();
        avaliacaoCV.putNull("id");
        avaliacaoCV.put("id_disciplina", idDisciplina);
        avaliacaoCV.put("nome", av.getNome());
        avaliacaoCV.put("data", av.getData().toString());
        avaliacaoCV.put("nota", av.getNota());
        avaliacaoCV.put("escala", av.getEscala());
        avaliacaoCV.put("peso", av.getPeso());
        avaliacaoCV.put("realizada", av.isRealizada());

        bd.insert(TABLE_NAME, null, avaliacaoCV);
    }

    public static void updateAvaliacao(SQLiteDatabase bd, Avaliacao av, long id) {
        ContentValues avaliacaoCV = new ContentValues();
        avaliacaoCV.put("nome", av.getNome());
        avaliacaoCV.put("data", av.getData().toString());
        avaliacaoCV.put("nota", av.getNota());
        avaliacaoCV.put("escala", av.getEscala());
        avaliacaoCV.put("peso", av.getPeso());
        avaliacaoCV.put("realizada", av.isRealizada());

        bd.update(TABLE_NAME, avaliacaoCV, "id = ?", new String[]{"" + id});
    }

    public static void deleteAvaliacao(SQLiteDatabase bd, long id) {
        bd.delete(TABLE_NAME, "id = ?", new String[]{"" + id});
    }
}
