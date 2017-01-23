package com.vishesh.eight_puzzle;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button play=(Button) findViewById(R.id.PlayButton);
        play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), Game.class);
				startActivity(intent);
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
		case R.id.helpmenu:
			Intent intent = new Intent(getApplicationContext(), Help.class);
			startActivity(intent);
			break;
		case R.id.creditsmenu:
			Intent i = new Intent(getApplicationContext(), Credits.class);
			startActivity(i);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}