package com.projectprototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;

public class MainActivity extends Activity {
	CalendarView calendar;
	Button logLeaveButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initializeCalendar();
		
		Button logLeaveButton = (Button) findViewById(R.id.leavebutton); 
		logLeaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	logLeave(v);
            }
        });
		
		Intent intentHome = getIntent();
		String message = intentHome.getStringExtra("message");
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
	
	public void logLeave(View view) {
        Intent intent1 = new Intent(this, LeaveActivity.class);
        intent1.putExtra("message", "Please Log Details");
        startActivity(intent1);
    }

	
	public void initializeCalendar() {
			calendar = (CalendarView) findViewById(R.id.calendar);

			// sets whether to show the week number.
			calendar.setShowWeekNumber(false);

			// sets the first day of week according to Calendar.
			// here we set Monday as the first day of the Calendar
			calendar.setFirstDayOfWeek(2);

			//The background color for the selected week.
			calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.green));
			
			//sets the color for the dates of an unfocused month. 
			calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));
		
			//sets the color for the separator line between weeks.
			calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));
			
			//sets the color for the vertical bar shown at the beginning and at the end of the selected date.
			calendar.setSelectedDateVerticalBar(R.color.darkgreen);
			
			//sets the listener to be notified upon selected date change.
			calendar.setOnDateChangeListener(new OnDateChangeListener() {
	                       //show the selected date as a toast
				public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
					month = month + 1;
					Intent intentView = new Intent(MainActivity.this, ViewLeaveActivity.class);
			        intentView.putExtra("year", year);
			        intentView.putExtra("month", month);
			        intentView.putExtra("day", day);
			        startActivity(intentView);
					Toast.makeText(getApplicationContext(), month + "/" + day + "/" + year, Toast.LENGTH_LONG).show();
				}
			});
		}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.main, menu);
	return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	// Handle action bar item clicks here. The action bar will
	// automatically handle clicks on the Home/Up button, so long
	// as you specify a parent activity in AndroidManifest.xml.
	int id = item.getItemId();
	if (id == R.id.action_settings) {
	return true;
	}
	return super.onOptionsItemSelected(item);
	}

	
	
	
	


	

	

		
	
}
