package edu.fje.clot.puzzle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.TextView;

import edu.fje.clot.puzzle.service.audio.MusicService;
import edu.fje.clot.puzzle.service.image.ImageService;
import edu.fje.clot.puzzle.statics.LayoutParamsLists;
import edu.fje.clot.puzzle.statics.Util;


@SuppressWarnings("deprecation")
public class GameActivity extends Activity {

	/**
	 * TODO parar musica al cerrar app
	 */

	public TextView moveCounter;
	public Button[] gameButtons;
	public Button soundButton;

	private static final String DEFAULT_IMAGE_URI = "android.resource://edu.fje.clot.puzzle/drawable/porky";
   	
	private List<Integer> cells = new ArrayList<Integer>();

	/**
	 * Inicializa la Activity, definiendo primero los botones, luego el contador de movimientos y
	 * finalmente el reproductor de audio para los movimientos.
	 * @param savedInstanceState Instancia guardada de la activity.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

		Intent pickImage = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(pickImage, 0);

	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case 0:
				initImage(data);
				initGameButtons();
				initSoundButton();
				initCounter();
				break;
			default:
				Log.d("Intent", "Unrecognized code");
				break;
		}
	}

	/**
	 * Inicializa la imagen del puzzle recibida en el <code>Intent</code>. Si es nulo,
	 * entonces utiliza la imagen por defecto.
	 * @param image <code>Intent</code> de la imagen de la galeria escogida.
     */
	private void initImage(Intent image) {
		try {
			ImageService.getInstance().setImage(
					Util.getBitmapFromUri(
							image != null ? image.getData() :
									Uri.parse(DEFAULT_IMAGE_URI), this)
			);
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		destroyService(MusicService.class);
	}

	/**
	 * Inicializa el contador de movimientos efectuados.
	 */
	private void initCounter() {
		moveCounter = (TextView) findViewById(R.id.move_counter);
		moveCounter.setText("0");
	}

	/**
	 * Inicializa el boton de sonido ON/OFF. Mientras este OFF, ni la
	 * musica de fondo ni los sonidos de mover pieza se reproduciran.
	 */
	private void initSoundButton() {
		soundButton = (Button) findViewById(R.id.sound_button);
		soundButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MusicService ms = MusicService.getInstance();
				if(ms.isOn()) ms.pause();
				else ms.play();
				soundButton.setBackground(
						getResources().getDrawable(ms.isOn() ?
								R.drawable.sound_on :
								R.drawable.sound_off)
				);
			}
		});
	}

	/**
	 * Inicializa los botones. Primero, encuentra la lista de botones del layout y despues
	 * rellena el array de celdas con los numeros desde el 0 hasta el 9 y los desordena. Estos
	 * numeros representan la posicion de los botones una vez desordenados, en base al indice
	 * numerico que los representa.
	 * Una vez tiene los botones y sus posiciones, rellena el cuadro del puzzle y define
	 * la accion de los botones al pulsarlos: llamar a <code>makeMove(int, int)</code> para
	 * intentar efectuar el movimiento.
	 */
	private void initGameButtons() {
		gameButtons = findButtons();

		for(int i = 0; i < 9; i++) cells.add(i);
		Collections.shuffle(cells);
		fillGrid();
		for (int i = 1; i < 9; i++) {
			gameButtons[i].setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					makeMove((Button) v);
				}
			});
		}
	}

	/**
	 * Encuentra los objetos Button del layout en base al array estatico de strings
	 * que contiene las IDs de los botones. Se encuentra en <code>values/gameButtons.xml</code>.
	 * @return Array de objetos Button en el layout.
     */
    public Button[] findButtons() {
		Button[] b = new Button[9];
		TypedArray layouts = getResources().obtainTypedArray(R.array.game_buttons);
		for(int i = 0; i < 9; i++)
			b[i] = (Button) findViewById(layouts.getResourceId(i, -1));
		layouts.recycle();
		return b;
	}

	/**
	 * Efectua el movimiento de una pieza a la posicion vacia, 0, en caso de que el boton
	 * se encuentre en una casilla contigua a ella. Si es asi, tambien reproducira el sonido
	 * caracteristico del movimiento de una pieza (en caso de que el servicio de musica no
	 * este en estado de pausa).
	 * @param b Boton pulsado.
     */
	public void makeMove(final Button b) {
		int btIndex,btPos,voidPos;
		btIndex = Integer.parseInt(String.valueOf(b.getTag()));
     	btPos = findPosition(btIndex);
   		voidPos = findPosition(0);

		if(checkMove(btPos, voidPos)) {

			MusicService.getInstance().playClickSound();
			// HOWDY Aqui animacion

			cells.remove(btPos); //quita el boton que has pulsado
			cells.add(btPos, 0); // pone el 0 en la posicion del boton
			cells.remove(voidPos); // quita el 0
			cells.add(voidPos, btIndex); // pone el boton en la posicion del 0

			fillGrid();
			moveCounter.setText(Integer.toString(Integer.parseInt((String) moveCounter.getText())+1));

			 for(int i = 0; i < 9; i++)
				if(!cells.get(i).equals(i)) return;
		}
	}

	/**
	 * Comprueba, en base a la posicion del boton y la posicion vacia - esto es el 0 - si
	 * es posible mover el boton, es decir, si las posiciones son contiguas (misma fila y columna
	 * + o - 1, o bien misma columna y fila + o - 1).
	 * @param btPos Posicion del boton.
	 * @param voidPos Posicion vacia, la del 0.
     * @return True si el movimiento es posible, false en caso contrario.
     */
	private boolean checkMove(int btPos, int voidPos) {
		int btRow = ImageService.getRowIndex(btPos),
				btColumn = ImageService.getColumnIndex(btPos),
				voidRow = ImageService.getRowIndex(voidPos),
				voidColumn = ImageService.getColumnIndex(voidPos);
		return voidRow == btRow && (btColumn + 1 == voidColumn || btColumn - 1 == voidColumn) ||
				voidColumn == btColumn && (btRow + 1 == voidRow || btRow - 1 == voidRow);
	}

	/**
	 * Coloca los botones en la posicion adecuada en funcion de su posicion numerica en
	 * el array de celdas. Si encuentra el indice numero 0 en el array de celdas, dejara
	 * esa posicion hueca, indicando que es la posicion a la que se pueden mover los demas
	 * botones. En caso contrario, colocara la imagen correspondiente al indice de la celda
	 * en la lista de imagenes generadas troceando la imagen seleccionada para el puzzle.
	 */
	public void fillGrid()
    {
		List<Integer> listX = LayoutParamsLists.getListX(),
				listY = LayoutParamsLists.getListY();
		int index;
		Button current;

		for (int i = 0; i < 9; i++) {
			index = cells.get(i);
			current = gameButtons[index];
			AbsoluteLayout.LayoutParams absParams =
					(AbsoluteLayout.LayoutParams) current.getLayoutParams();
			absParams.x = listX.get(i);
			absParams.y = listY.get(i);
			current.setLayoutParams(absParams);
			current.setText("");
			if(index > 0)
				current.setBackground(
						new BitmapDrawable(ImageService.getInstance().getChunks().get(index))
				);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		MusicService.getInstance().pause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		MusicService.getInstance().play();
	}

	private boolean destroyService(Class service) {
		return stopService(new Intent(this, service));
	}

	/**
	 * Encuentra la posicion del indice numerico de un elemento en el array de celdas. Esto es
	 * la posicion dentro del array de celdas en la que se encuentra el numero que hace referencia
	 * al boton que se esta buscando.
	 * @param element Identificador del boton.
	 * @return Posicion del boton en el array de celdas.
     */
	public int findPosition(int element) {
		 int i = 0;
		 while(i < 9) {
			 if(cells.get(i) == element) break;
			 i++;
		 }
		 return i;
	}
}
