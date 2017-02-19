package edu.fje.clot.puzzle.scores.db;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import edu.fje.clot.puzzle.scores.bean.Score;

/**
 * Classe d'utilitat de persistencia que representa la taula de puntuacions.
 * Created by m0r on 21/11/16.
 */

public class ScoreDbUtil extends SQLiteOpenHelper {
    /**
     * Objecte de resolucio de continguts.
     */
    private ContentResolver _contentResolver;
    /**
     * Context en el que es desenvolupen les operacions de persistencia.
     */
    private Context _context;
    /**
     * Nom de la base de dades.
     */
    private static final String DATABASE_NAME = "Score.db";
    /**
     * Versio de la base de dades.
     */
    private static final int DATABASE_VERSION = 1;
    /**
     * Indicador d'ordre de seleccio de dades.
     */
    private static final String ORDER = ScoreContract.ScoreTable.COLUMN_VALUE + " DESC";
    /**
     * Vector de projeccio de dades a la taula.
     */
    private static final String[] QUERY_PROJECTION = {
            ScoreContract.ScoreTable._ID,
            ScoreContract.ScoreTable.COLUMN_DATE,
            ScoreContract.ScoreTable.COLUMN_VALUE
    };
    /**
     * Vetor de projeccio de recompte de dades.
     */
    private static final String[] QUERY_COUNT = {ScoreContract.ScoreTable.COUNT};
    /**
     * Vector de projeccio d'identificador maxim.
     */
    private static final String[] QUERY_MAX_ID = {ScoreContract.ScoreTable.MAX_ID};
    /**
     * String de seleccio per identificador.
     */
    private static final String BY_ID = ScoreContract.ScoreTable._ID + "=?";
    /**
     * String SQL de creacio de la taula.
     */
    private static final String SQL_CREATE_TABLE = "CREATE TABLE " +
            ScoreContract.ScoreTable.TABLE_NAME + " (" +
            ScoreContract.ScoreTable._ID + " INTEGER PRIMARY KEY, " +
            ScoreContract.ScoreTable.COLUMN_DATE + " LONG DEFAULT 0, " +
            ScoreContract.ScoreTable.COLUMN_VALUE + " INTEGER DEFAULT 0)";
    /**
     * String SQL d'eliminacio de la taula en cas que existeixi.
     */
    private static final String SQL_DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS " + ScoreContract.ScoreTable.TABLE_NAME;

    /**
     * Constructor amb parametre de context. Assigna un objecte de resolucio de continguts a
     * mes del propi context.
     * @param context Context
     */
    public ScoreDbUtil(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setContentResolver(context.getContentResolver());
        setContext(context);
    }

    /**
     * Inserta un nou objecte Score a la base de dades. Retorna si el resultat de la insercio
     * es major que 0, es a dir, si la operacio ha sigut fructifera.
     * @param score Score
     * @return boolean
     */
    public boolean insert(Score score) {
        ContentValues values = new ContentValues();
        values.put(ScoreContract.ScoreTable._ID, score.getId());
        values.put(ScoreContract.ScoreTable.COLUMN_VALUE, score.getValue());
        values.put(ScoreContract.ScoreTable.COLUMN_DATE, score.getDate().getTime());
        return getWritableDatabase().insert(
                ScoreContract.ScoreTable.TABLE_NAME,
                ScoreContract.ScoreTable.COLUMN_NULL,
                values
        ) >= 0;
    }

    /**
     * Inserta un objecte de puntuacio com un event al calendari. Retorna false en cas que no
     * ho aconsegueixi.
     * @param score Score
     * @param context Context
     * @return boolean
     */
    /*public boolean insertToCalendar(Score score,Context context) {
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Events.CALENDAR_ID, 1); // Tipus de calendari
        cv.put(CalendarContract.Events.TITLE, "SUDOKU - Puntuacio: " + score.getValue());
        cv.put(CalendarContract.Events.DTSTART, Calendar.getInstance().getTimeInMillis());
        cv.put(CalendarContract.Events.DTEND, Calendar.getInstance().getTimeInMillis());
        cv.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Madrid");
      //  Application application = (Application) SudokuApplication.getContext();
       // SudokuApplication app = (SudokuApplication)application;
       // Context context= app.getContext();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions((Activity) getContext(),
                    new String[]{Manifest.permission.WRITE_CALENDAR},
                    1);


        Uri uri = getContentResolver().insert(CalendarContract.Events.CONTENT_URI, cv);
        int id;
        try {
            id = Integer.parseInt(uri.getLastPathSegment());
        } catch (NullPointerException npEx) {
            npEx.printStackTrace();
            return false;
        }
        Log.i("ScoreDb", "Inserted calendar event:" + id);
        return id > 0;
    }*/

    /**
     * Inserta en la base de datos la lista de puntuaciones pasada por parametro.
     * @param scores List<Score>
     * @return boolean.
     */
    public boolean insertAll(List<Score> scores) {
        boolean result = true;
        Collections.sort(scores);
        for(Score score : scores)
            result &= insert(score);
        return result;
    }

    /**
     * Troba un element en concret de la taula a partir del seu identificador.
     * @param _id int
     * @return Score
     */
    public Score find(int _id) {
        String[] args = { String.valueOf(_id) };
        Cursor cursor = getReadableDatabase().query(ScoreContract.ScoreTable.TABLE_NAME,
                QUERY_PROJECTION, BY_ID, args, null, null, null);
        Score result = null;
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = new Score(
                    cursor.getInt(cursor.getColumnIndex(ScoreContract.ScoreTable._ID)),
                    cursor.getInt(cursor.getColumnIndex(ScoreContract.ScoreTable.COLUMN_VALUE)),
                    new Date(cursor.getLong(cursor.getColumnIndex(ScoreContract.ScoreTable.COLUMN_DATE)))
            );
        }
        cursor.close();
        return result;
    }

    /**
     * Retorna el recompte d'elements a la taula.
     * @return int
     */
    public int count() {
        Cursor cursor = getReadableDatabase().query(ScoreContract.ScoreTable.TABLE_NAME,
                QUERY_COUNT, null, null, null, null, null);
        cursor.moveToFirst();
        int result = cursor.getInt(cursor.getColumnIndex(ScoreContract.ScoreTable.COUNT));
        cursor.close();
        return result;
    }

    /**
     * Retorna l'identificador amb valor maxim.
     * @return int
     */
    public int findMaxId() {
        Cursor cursor = getReadableDatabase().query(ScoreContract.ScoreTable.TABLE_NAME,
                QUERY_MAX_ID, null, null, null, null, null);
        cursor.moveToFirst();
        int result = cursor.getInt(cursor.getColumnIndex(ScoreContract.ScoreTable.MAX_ID));
        cursor.close();
        return result;
    }

    /**
     * Retorna la llista sencera d'objectes de puntuacio, ordenats a partir del valor
     * de forma descendent.
     * @return List<Score>
     */
    public List<Score> findAll() {
        List<Score> result = new ArrayList<Score>();
        Cursor cursor = getReadableDatabase().query(ScoreContract.ScoreTable.TABLE_NAME,
                QUERY_PROJECTION, null, null, null, null, ORDER);
        // 1 -> ID, 2 -> DATE, 3 -> VALUE
        if(cursor.getCount() > 0)
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
                result.add(new Score(
                        cursor.getInt(cursor.getColumnIndex(ScoreContract.ScoreTable._ID)),
                        cursor.getInt(cursor.getColumnIndex(ScoreContract.ScoreTable.COLUMN_VALUE)),
                        new Date(cursor.getLong(cursor.getColumnIndex(ScoreContract.ScoreTable.COLUMN_DATE)))
                ));
        cursor.close();
        return result;
    }

    /**
     * Retorna un numero determinat d'elements a partir del primer.
     * @param top int
     * @return List<Score>
     */
    public List<Score> findTop(int top) {
        List<Score> result = new ArrayList<Score>();
        final String LIMIT = " LIMIT" + top;
        Cursor cursor = getReadableDatabase().query(ScoreContract.ScoreTable.TABLE_NAME,
                QUERY_PROJECTION, null, null, null, null, ORDER, LIMIT);
        // 1 -> ID, 2 -> DATE, 3 -> VALUE
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
            result.add(new Score(
                    cursor.getInt(cursor.getColumnIndex(ScoreContract.ScoreTable._ID)),
                    cursor.getInt(cursor.getColumnIndex(ScoreContract.ScoreTable.COLUMN_VALUE)),
                    new Date(cursor.getLong(cursor.getColumnIndex(ScoreContract.ScoreTable.COLUMN_DATE)))
            ));
        cursor.close();
        return result;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //sqLiteDatabase.execSQL("DROP TABLE " + ScoreContract.ScoreTable.TABLE_NAME);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DROP_TABLE_IF_EXISTS);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onUpgrade(sqLiteDatabase, i, i1);
    }

    private void setContentResolver(ContentResolver contentResolver) {
        _contentResolver = contentResolver;
    }

    public ContentResolver getContentResolver() {
        return _contentResolver;
    }

    private void setContext(Context context) {
        _context = context;
    }

    public Context getContext() {
        return _context;
    }
}
