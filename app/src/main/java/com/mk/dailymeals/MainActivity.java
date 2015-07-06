package com.mk.dailymeals;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	private static final int RESULT_SETTINGS = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button add = (Button) findViewById(R.id.button_add);
		Button settings = (Button) findViewById(R.id.button_settings);
		Button list = (Button) findViewById(R.id.button_list);


        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addMeal();
            }
        });
		list.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openListOfMeals();
			}
		});
		settings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openSettings();
			}
		});
		showUserSettings();
	}
	private void addMeal() {
		Intent intent1 = new Intent(MainActivity.this,
				addMeal.class);
		startActivity(intent1);
	}
	private void openListOfMeals() {
		Intent intent2 = new Intent(MainActivity.this,
				Listofmeals.class);
		startActivity(intent2);
	}
	private void openSettings() {
		Intent intent3 = new Intent(this, SettingActivity.class);
		startActivityForResult(intent3, RESULT_SETTINGS);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SETTINGS:
			showUserSettings();
			break;

		}

	}

	private void showUserSettings() {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		Resources res = getResources();

		// powitanie Usera
		TextView usernametxt = (TextView) findViewById(R.id.hello);
		String currentUser = sharedPrefs.getString("nickname", "Janusz");
		String powitanie = String.format(res.getString(R.string.hello),
				currentUser);
		usernametxt.setText(powitanie);

		StringBuilder builder = new StringBuilder();
		builder.append(sharedPrefs.getString("ilosckalorii", "0")
				+ res.getString(R.string.infokcal) + " ");
		builder.append(sharedPrefs.getString("bialko", "0")
				+ res.getString(R.string.infobialko) + " ");
		builder.append(sharedPrefs.getString("wegle", "0")
				+ res.getString(R.string.infowegle) + " ");
		builder.append(sharedPrefs.getString("tluszcze", "0")
				+ res.getString(R.string.infotluszcze));
		String zformatowany = builder.toString();
		TextView settingsTextView = (TextView) findViewById(R.id.infoleft);
		settingsTextView.setText(Html.fromHtml(zformatowany));
		// jezyk

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings: {
			openSettings();
		}
		}
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.wyjscie) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
