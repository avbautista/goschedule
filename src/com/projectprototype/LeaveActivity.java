package com.projectprototype;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.firebase.client.Firebase;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class LeaveActivity extends Activity implements OnItemSelectedListener {
	DatabaseHelper db = new DatabaseHelper(this);
	Button submitButton;
	Button cancelButton;
	EditText name;
	EditText date;
	Spinner type;
	String item;
	Calendar myCalendar = Calendar.getInstance();
	
	
	
	DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			// TODO Auto-generated method stub
			myCalendar.set(Calendar.YEAR, year);
	        myCalendar.set(Calendar.MONTH, monthOfYear);
	        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
	        updateLabel();
		}
		
		
	};
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_leave);
		
		Intent intentReceived = getIntent();
		
		//Firebase.setAndroidContext(this);
		//Firebase f = new Firebase("https://goschedule-50998.firebaseio.com/");
		//f.setValue("Hello World! version 2.0");		
		
		submitButton = (Button) findViewById(R.id.leaveSubmit); 
		cancelButton = (Button) findViewById(R.id.leaveCancel); 
		
		name = (EditText) findViewById(R.id.leaveName);
		date = (EditText) findViewById(R.id.leaveDate);
		type = (Spinner) findViewById(R.id.leaveType);
		
		type.setOnItemSelectedListener(this);
		final Calendar myCalendar = Calendar.getInstance();
		
		date.setOnClickListener(new OnClickListener() {

	        @Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	            new DatePickerDialog(LeaveActivity.this, date2, myCalendar
	                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
	                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
	        }
	    });
		
		submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitLeave(v);
            }
        });
		cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelView(v);
            }
        });
		
		
				
	}
	
	private void updateLabel() {

	    String myFormat = "MM/dd/yyyy"; //In which you need put here
	    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

	    date.setText(sdf.format(myCalendar.getTime()));
	    }
	
	
	public void submitLeave(View view) {
		

		if (name.getText().toString().length() > 0 && date.getText().toString().length() > 0) {
		
		boolean logStatus = createLogFB(name.getText().toString(),date.getText().toString(),item);
		//Intent back = new Intent(this, MainActivity.class);
			if (logStatus){
				Toast.makeText(getApplicationContext(), "Added Leave!", Toast.LENGTH_LONG).show();
				//startActivity(back);
				finish();
			}
			else {
				Toast.makeText(getApplicationContext(), "Failed, please try again.", Toast.LENGTH_LONG).show();
			}
		}
		
		if (name.getText().toString().length() == 0) {
				Toast.makeText(getApplicationContext(), "EID is required.", Toast.LENGTH_LONG).show();
		}
		
		else if (date.getText().toString().length() == 0) {
			Toast.makeText(getApplicationContext(), "Date is required.", Toast.LENGTH_LONG).show();
	}
    }
	
	public void cancelView(View view) {
        //Intent intent1 = new Intent(this, MainActivity.class);
        //intent1.putExtra("message", "Canceled.");
        //startActivity(intent1);
		finish();
    }
	
	public boolean createLogFB(String name, String date, String type) {
		
		String[] dateArr = date.split("/");
		String monthyear = dateArr[0] + dateArr[2];
		date = dateArr[0] + "-" + dateArr[1] + "-" + dateArr[2];
		name = name.replace(".","-");
		
		Firebase.setAndroidContext(this);
		Firebase ref = new Firebase("https://goschedule-50998.firebaseio.com/");
		Firebase dateRef = ref.child("dates");
		
		Map<String, Object> dataput = new HashMap<String, Object>();
		dataput.put("name", name);
		dataput.put("type", type);
		dataput.put("date", date);
		dateRef.push().setValue(dataput);
			
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		item = parent.getItemAtPosition(position).toString();
		Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
}


	