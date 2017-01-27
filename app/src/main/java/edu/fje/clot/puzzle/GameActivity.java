package edu.fje.clot.puzzle;

import java.util.ArrayList;
import java.util.Collections;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.TextView;


@SuppressWarnings("deprecation")
public class GameActivity extends Activity {
   
	private TextView moveCounter;
	private MediaPlayer mediaPlayer;
 	private Button[] buttons;
    private Boolean bad_move=false;
   	private static final Integer[] goal = new Integer[] {0,1,2,3,4,5,6,7,8};
   	
	private ArrayList<Integer> cells = new ArrayList<Integer>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        buttons=findButtons();
       
        for(int i=0;i<9;i++)
            cells.add(i);
        Collections.shuffle(cells); //random cells array
        
        fill_grid();
       
        
       moveCounter = (TextView) findViewById(R.id.MoveCounter);
		mediaPlayer = MediaPlayer.create(this,R.raw.click);
		for (int i = 1; i < 9; i++) {
			buttons[i].setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					mediaPlayer.start();
					// Aqui efecto de sonido (y posiblemente animacion ???)
					makeMove((Button) v);
				}
			});
		}
		
		
		moveCounter.setText("0");
    
}
    public Button[] findButtons() {
		Button[] b = new Button[9];
		
		b[0] = (Button) findViewById(R.id.Button00);
		b[1] = (Button) findViewById(R.id.Button01);
		b[2] = (Button) findViewById(R.id.Button02);
		b[3] = (Button) findViewById(R.id.Button03);
		b[4] = (Button) findViewById(R.id.Button04);
		b[5] = (Button) findViewById(R.id.Button05);
		b[6] = (Button) findViewById(R.id.Button06);
		b[7] = (Button) findViewById(R.id.Button07);
		b[8] = (Button) findViewById(R.id.Button08);
		return b;
	}
   
	public void makeMove(final Button b) {
        bad_move=true;
		int b_text,b_pos,zuk_pos;
		// Cambiar esto por una manera de coger la posicion del boton que picas
		b_text=Integer.parseInt((String) b.getText());
     	b_pos=find_pos(b_text);
   		zuk_pos=find_pos(0);

   		cells.remove(b_pos);
   		cells.add(b_pos, 0);
   		cells.remove(zuk_pos);
   		cells.add(zuk_pos,b_text);
   		
	
    	fill_grid();
		moveCounter.setText(Integer.toString(Integer.parseInt((String) moveCounter.getText())+1));

		 for(int i=0;i<9;i++)
	        {
	           if(cells.get(i)!=goal[i])
	           {
	        	        	   return;
	           }
	        }
	}
	
	public void fill_grid()
    {
	 for(int i=0;i<9;i++) {
		 int text=cells.get(i);
		 AbsoluteLayout.LayoutParams absParams = 
			    (AbsoluteLayout.LayoutParams)buttons[text].getLayoutParams();
		 switch(i)
		 {case(0):
		 
			absParams.x = 5;
			absParams.y = 5;
			buttons[text].setLayoutParams(absParams);
			break;
		 case(1):
			
				absParams.x = 110;
				absParams.y = 5;
				buttons[text].setLayoutParams(absParams);
				break;
		 case(2):
			
				absParams.x = 215;
				absParams.y = 5;
				buttons[text].setLayoutParams(absParams);
				break;
		 case(3):
			
				absParams.x = 5;
				absParams.y = 110;
				buttons[text].setLayoutParams(absParams);
				break;
		 case(4):
			
				absParams.x =110;
				absParams.y =110;
				buttons[text].setLayoutParams(absParams);
				break;
		 case(5):
			 
				absParams.x = 215;
				absParams.y =110;
				buttons[text].setLayoutParams(absParams);
				break;
		 case(6):
			 
				absParams.x = 5;
				absParams.y = 215;
				buttons[text].setLayoutParams(absParams);
				break;
		 case(7):
			 
				absParams.x = 110;
				absParams.y = 215;
				buttons[text].setLayoutParams(absParams);
				break;
		 case(8):
			 
				absParams.x = 215;
				absParams.y = 215;
				buttons[text].setLayoutParams(absParams);
				break;

		 }

		}
		
	}
	
	public int find_pos(int element) {
		 int i = 0;
		 for(;i<9;i++)
	           if(cells.get(i)==element) break;
		 return i;
	}
	}

    