package edu.fje.clot.puzzle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.TextView;

import edu.fje.clot.puzzle.statics.LayoutParamsLists;


@SuppressWarnings("deprecation")
public class GameActivity extends Activity {
   
	private MediaPlayer mediaPlayer;
	public TextView moveCounter;
	public Button[] buttons;
   	private static final Integer[] goal = new Integer[] {0,1,2,3,4,5,6,7,8};
   	
	private List<Integer> cells = new ArrayList<Integer>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
		buttons = findButtons();

        for(int i=0;i<9;i++) cells.add(i);
        Collections.shuffle(cells); //random cells array
        fillGrid();

		mediaPlayer = MediaPlayer.create(this, R.raw.click);

		moveCounter = (TextView) findViewById(R.id.MoveCounter);
		moveCounter.setText("0");


		for (int i = 1; i < 9; i++) {
			buttons[i].setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					mediaPlayer.start();
					// Aqui animacion (?)
					makeMove((Button) v);
				}
			});
		}
	}

    public Button[] findButtons() {
		Button[] b = new Button[9];
		TypedArray layouts = getResources().obtainTypedArray(R.array.buttons);
		for(int i = 0; i < 9; i++)
			b[i] = (Button) findViewById(layouts.getResourceId(i, -1));
		layouts.recycle();
		return b;
	}
   
	public void makeMove(final Button b) {
		int b_text,b_pos,zuk_pos;
		b_text=Integer.parseInt((String) b.getText());
     	b_pos= findPosition(b_text);
   		zuk_pos= findPosition(0);

   		cells.remove(b_pos);
   		cells.add(b_pos, 0);
   		cells.remove(zuk_pos);
   		cells.add(zuk_pos,b_text);
   		
	
    	fillGrid();
		moveCounter.setText(Integer.toString(Integer.parseInt((String) moveCounter.getText())+1));

		 for(int i=0;i<9;i++)
	        if(cells.get(i) != goal[i]) return;
	}
	
	public void fillGrid()
    {
		List<Integer> listX = LayoutParamsLists.getListX(),
				listY = LayoutParamsLists.getListY();

		for(int i=0;i<9;i++) {
			int text=cells.get(i);
		 	AbsoluteLayout.LayoutParams absParams =
				(AbsoluteLayout.LayoutParams) buttons[text].getLayoutParams();
			absParams.x = listX.get(i);
			absParams.y = listY.get(i);
			buttons[text].setLayoutParams(absParams);
		}

	}
	
	public int findPosition(int element) {
		 int i = 0;
		 for(;i<9;i++) if(cells.get(i)==element) break;
		 return i;
	}
}

    