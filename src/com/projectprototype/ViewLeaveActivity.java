package com.projectprototype;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ViewLeaveActivity extends ListActivity implements OnItemClickListener{
	DatabaseHelper db = new DatabaseHelper(this);
	List<String> listLeave;
	ListView lv;
	ArrayAdapter<String> myAdapter;
	String monthString;
	String dayString;
	String dateString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_leave);
		
		Intent intentDateReceived = getIntent();
		Integer year = intentDateReceived.getExtras().getInt("year");
		Integer month = intentDateReceived.getExtras().getInt("month");
		Integer day = intentDateReceived.getExtras().getInt("day");
		
		if (month < 10){
			monthString = Integer.toString(month);
			monthString = "0"+monthString;	
		}
		else{
			monthString = Integer.toString(month);
		}
		
		if (day < 10){
			dayString = Integer.toString(day);
			dayString = "0"+dayString;	
		}
		else{
			dayString = Integer.toString(day);
		}
		
		dateString = monthString + "/" + dayString + "/" + Integer.toString(year);
		
		listLeave = db.getAllInDate(dateString);
		
		//Log.i("Adap", listLeave.get(0));
		
		lv = (ListView) findViewById(android.R.id.list);
		myAdapter = new ArrayAdapter<String>(ViewLeaveActivity.this, android.R.layout.simple_list_item_1, listLeave);
		getListView().setOnItemClickListener(this);
		lv.setAdapter(myAdapter);
		
		Toast.makeText(getApplicationContext(), dateString, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
}
