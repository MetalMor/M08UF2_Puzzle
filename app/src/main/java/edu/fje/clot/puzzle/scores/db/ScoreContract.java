package edu.fje.clot.puzzle.scores.db;

import android.provider.BaseColumns;

/**
 * Classe de contracte per a la manipulacio de la taula de puntuacions.
 * Created by m0r on 20/11/16.
 */

final class ScoreContract {
    /**
     * Columnes de la taula de puntuacions.
     */
    static abstract class ScoreTable implements BaseColumns {
        /**
         * Nom de la taula.
         */
        static final String TABLE_NAME = "SCORE";
        /**
         * Nom de la columna de dates.
         */
        static final String COLUMN_DATE = "DATE";
        /**
         * Nom de la columna de valors de puntuacio.
         */
        static final String COLUMN_VALUE = "VALUE";
        /**
         * Indicador de columna nula.
         */
        static final String COLUMN_NULL = "NULL";
        /**
         * Selector de recompte de puntuacions.
         */
        static final String COUNT = "COUNT(*)";
        /**
         * Selector d'identificador maxim.
         */
        static final String MAX_ID = "MAX(" + _ID + ")";
    }
}
