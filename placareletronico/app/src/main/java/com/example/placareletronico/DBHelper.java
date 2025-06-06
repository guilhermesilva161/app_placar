package com.example.placareletronico;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "scoreDB";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "matches";
    private static final String COL_ID = "id";
    public static final String COL_TEAM_LEFT = "team_left";
    public static final String COL_TEAM_RIGHT = "team_right";
    public static final String COL_SCORE_LEFT = "score_left";
    public static final String COL_SCORE_RIGHT = "score_right";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_TEAM_LEFT + " TEXT," +
                COL_TEAM_RIGHT + " TEXT," +
                COL_SCORE_LEFT + " INTEGER," +
                COL_SCORE_RIGHT + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Se precisar atualizar a versão do DB
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // CREATE: salvar novo jogo
    public long saveMatch(String teamLeft, String teamRight, int scoreLeft, int scoreRight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TEAM_LEFT, teamLeft);
        cv.put(COL_TEAM_RIGHT, teamRight);
        cv.put(COL_SCORE_LEFT, scoreLeft);
        cv.put(COL_SCORE_RIGHT, scoreRight);
        return db.insert(TABLE_NAME, null, cv);
    }

    // READ: buscar último jogo salvo (exemplo de recuperação)
    public Cursor getAllMatches() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, COL_ID + " DESC");
    }

    public void clearHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null); // deleta todas as linhas da tabela
        db.close();
    }
}
