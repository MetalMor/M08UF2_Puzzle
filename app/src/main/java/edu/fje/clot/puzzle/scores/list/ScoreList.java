package edu.fje.clot.puzzle.scores.list;

import android.content.Context;

import java.util.Collections;
import java.util.List;

import edu.fje.clot.puzzle.scores.bean.Score;
import edu.fje.clot.puzzle.scores.db.ScoreDbUtil;

/**
 * Control de llistes de puntuacions persistents.
 * Created by m0R on 11/21/16.
 */


public class ScoreList {

    /**
     * Objecte taula de dades.
     */
    private ScoreDbUtil _table;
    /**
     * Llista de puntuacions en memoria.
     */
    private List<Score> _list;

    /**
     * Constructor amb parametre context per assignar l'objecte de taula de dades.
     * @param context
     */
    public ScoreList(Context context){
        setTable(new ScoreDbUtil(context));
    }

    /**
     * Retorna els primers n elements de la taula de puntuacions, segons l'ordre
     * descendent de puntuacions.
     * @param n int
     * @return List<Score>
     */
    public List<Score> getTop(int n){
        return getTable().findTop(n);
    }

    /**
     * Retorna un objecte a la posicio n de la taula ordenada per valors descendents.
     * @param n int
     * @return Score
     */
    public Score getItem(int n){
        return getList().get(n);
    }

    /**
     * Retorna el recompte de puntuacions de la taula.
     * @return int
     */
    public int count() {
        return getTable().count();
    }

    /**
     * Retorna la llista persistida d'elements. La taula sera refrescada cada vegada que no
     * coincideixi el seu tamany en memoria amb el de la base de dades.
     * @return List<Score>
     */
    public List<Score> getList() {
        if (isUpdateRequired()) {
            setList(getTable().findAll());
            Collections.sort(getList());
        }
        return _list;
    }

    /**
     * Assigna un valor com a llista persistida en memoria.
     * @param list List<Score>
     */
    public void setList(List<Score> list) { _list = list; }

    /**
     * Retorna l'objecte de taula de dades.
     * @return ScoreDbUtil
     */
    public ScoreDbUtil getTable() { return _table; }

    /**
     * Assigna l'objecte de taula de dades.
     * @param table ScoreDbUtil
     */
    private void setTable(ScoreDbUtil table) { _table = table; }

    /**
     * Comprueba si hace falta volver a solicitar la lista de puntuaciones a la BD.
     * @return Flag requiere actualizacion de la BD.
     */
    private boolean isUpdateRequired() {
        return _list == null ||
                _list.isEmpty() ||
                getTable().count() != _list.size();
    }
}
