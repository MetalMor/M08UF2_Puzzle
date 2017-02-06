package edu.fje.clot.puzzle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.TextView;

import edu.fje.clot.puzzle.service.audio.MusicService;
import edu.fje.clot.puzzle.service.image.ImageService;
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

        for(int i = 0; i < 9; i++) cells.add(i);
        Collections.shuffle(cells);
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
		int btIndex,btPos,voidPos;
		btIndex = Integer.parseInt((String) b.getText());
     	btPos= findPosition(btIndex);
   		voidPos= findPosition(0);

		if(ImageService.getColumnIndex(btPos) == ImageService.getColumnIndex(voidPos) ||
				ImageService.getRowIndex(btPos) == ImageService.getRowIndex(voidPos)) {
			cells.remove(btPos); //quita el boton que has pulsado
			cells.add(btPos, 0); // pone el 0 en la posicion del boton
			cells.remove(voidPos); // quita el 0
			cells.add(voidPos, btIndex); // pone el boton en la posicion del 0

			fillGrid();
			moveCounter.setText(Integer.toString(Integer.parseInt((String) moveCounter.getText())+1));

			 for(int i=0;i<9;i++)
				if(cells.get(i) != goal[i]) return;
		}
	}
	
	public void fillGrid()
    {
		List<Integer> listX = LayoutParamsLists.getListX(),
				listY = LayoutParamsLists.getListY();
		int index;
		Button current;

		for (int i = 0; i < 9; i++) {
			index = cells.get(i);
			current = buttons[index];
			AbsoluteLayout.LayoutParams absParams =
					(AbsoluteLayout.LayoutParams) current.getLayoutParams();
			absParams.x = listX.get(i);
			absParams.y = listY.get(i);
			current.setLayoutParams(absParams);
			if(index > 0)
				current.setBackground(
						new BitmapDrawable(ImageService.getInstance().getChunks().get(index))
				);
		}
	}
	
	public int findPosition(int element) {
		 int i = 0;
		 while(i < 9) {
			 if(cells.get(i) == element) break;
			 i++;
		 }
		 return i;
	}
}

    