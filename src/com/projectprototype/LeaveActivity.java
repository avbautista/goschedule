package com.projectprototype;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
		setContentView(R.layout.activity_leave);
		
		Intent intentReceived = getIntent();
		
		
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
		
		boolean logStatus = db.createLog(name.getText().toString(),date.getText().toString(),item);
		Intent back = new Intent(this, MainActivity.class);
			if (logStatus){
				Toast.makeText(getApplicationContext(), "Added Leave!", Toast.LENGTH_LONG).show();
				startActivity(back);
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
        Intent intent1 = new Intent(this, MainActivity.class);
        intent1.putExtra("message", "Canceled.");
        startActivity(intent1);
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


	