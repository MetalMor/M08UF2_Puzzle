package edu.fje.clot.puzzle;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import edu.fje.clot.puzzle.service.audio.MusicService;
import edu.fje.clot.puzzle.service.image.ImageService;

public class MainActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

		Button play = (Button) findViewById(R.id.PlayButton);
        play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intentService(MusicService.class);
				intentService(ImageService.class);
				startActivity(new Intent(getApplicationContext(), GameActivity.class));
			}
		});
    }
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.exitmenu:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private ComponentName intentService(Class service) {
		return startService(new Intent(getApplicationContext(), service));
	}

	private boolean destroyService(Class service) {
		return stopService(new Intent(getApplicationContext(), service));
	}
}