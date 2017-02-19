package edu.fje.clot.puzzle.scores.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import edu.fje.clot.puzzle.R;
import edu.fje.clot.puzzle.scores.bean.Score;

/**
 * Created by oriol on 11/12/16.
 */

public class ScoreAdapter extends BaseAdapter {
    /**
     * Context a on s'allotja l'Adapter.
     */
    private Context context;
    /**
     * Llista activa de puntuacions.
     */
    private ScoreList scoreList;
    /**
     * Inflater emprat per introduir elements a la llista de puntuacions.
     */
    private LayoutInflater inflater;

    /**
     * Constructor que assigna context, sistema de puntuacions i recursos d'imatge.
     * @param ctxt Context
     */
    public ScoreAdapter(Context ctxt) {
        context = ctxt;
        scoreList = new ScoreList(context);
    }

    /**
     * Retorna el recompte de puntuacions.
     * @return int
     */
    public int getCount() {
        return scoreList.count();
    }

    /**
     * Retorna un item a la posicio especificada.
     * @param position int
     * @return Object
     */
    @Override
    public Object getItem(int position) {
        return scoreList.getItem(position);
    }

    /**
     * Retorna l'identificador de l'element a la posicio especificada.
     * @param position int
     * @return long
     */
    @Override
    public long getItemId(int position) {
        return ((Score) getItem(position)).getId();
    }

    /**
     * Retorna la vista corresponent als parametres especificats. Buscara a la llista de
     * puntuacions l'element que toca en cada moment, i l'insertara amb la imatge adient.
     * @param position int
     * @param convertView View
     * @param parent ViewGroup
     * @return View
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.scorelist_item, parent, false);
        Score score = scoreList.getItem(position);

        String itemScoreText = String.valueOf(score.getValue());
        String itemDateText = new SimpleDateFormat("dd-MM-yyyy").format(score.getDate());

        ((TextView) itemView.findViewById(R.id.item_score)).setText(itemScoreText);
        ((TextView) itemView.findViewById(R.id.item_date)).setText(itemDateText);

        return itemView;
    }
}