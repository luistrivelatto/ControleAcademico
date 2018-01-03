package com.example.luigi.controleacademico.BD;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.luigi.controleacademico.Model.Data;
import com.example.luigi.controleacademico.Model.Frequencia;

import java.util.List;

/**
 * Created by Luigi on 02/01/2018.
 */

class FrequenciaBD {

    public static final String TABLE_NAME = "frequencias";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `" + TABLE_NAME + "` (" +
            " id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " id_disciplina INTEGER," +
            " data TEXT," +
            " horario TEXT," +
            " observacoes TEXT," +
            " frequentou INT," +
            " FOREIGN KEY(id_disciplina) REFERENCES " + DisciplinaBD.TABLE_NAME + "(id)" +
            ")";

    public static long getId(SQLiteDatabase bd, Frequencia freq, long idDisciplina) {
        Cursor c = bd.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE id_disciplina = ? AND data = ? AND horario = ?",
                new String[]{"" + idDisciplina, freq.getData().toString(), freq.getHorario()});

        long ret = -1;
        if(c.moveToNext()) {
            ret = c.getLong(0);
        }

        c.close();
        return ret;
    }

    public static List<Frequencia> queryFrequencias(SQLiteDatabase bd, int idDisciplina) {
        String queryStr = "SELECT * FROM " + TABLE_NAME + " WHERE id_disciplina = '" + idDisciplina + "'";
        return BDCore.query(queryStr, bd, new BDCore.BDReadable<Frequencia>() {
            @Override
            public Frequencia getObject(Cursor c) {
                Data data = new Data(c.getString(2));
                String horario = c.getString(3);
                String observacoes = c.getString(4);
                boolean frequentou = c.getInt(5) != 0;

                return new Frequencia(data, horario, observacoes, frequentou);
            }
        });
    }

    public static void insertFrequencia(SQLiteDatabase bd, Frequencia freq, long idDisciplina) {
        ContentValues frequenciaCV = new ContentValues();
        frequenciaCV.putNull("id");
        frequenciaCV.put("id_disciplina", idDisciplina);
        frequenciaCV.put("data", freq.getData().toString());
        frequenciaCV.put("horario", freq.getHorario());
        frequenciaCV.put("observacoes", freq.getObservacoes());
        frequenciaCV.put("frequentou", freq.isFrequentou() ? 1 : 0);

        bd.insert(TABLE_NAME, null, frequenciaCV);
    }

    public static void deleteFrequencia(SQLiteDatabase bd, long id) {
        bd.delete(TABLE_NAME, "id = ?", new String[]{"" + id});
    }
}
