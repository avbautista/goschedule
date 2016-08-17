package com.projectprototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
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
	
	public void submitLeave(View view) {
		boolean logStatus = db.createLog(name.getText().toString(),date.getText().toString(),item);
		if (logStatus){
			Toast.makeText(getApplicationContext(), "Added Leave!", Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(getApplicationContext(), "Failed, please try again.", Toast.LENGTH_LONG).show();
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
