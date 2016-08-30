package com.projectprototype;

import java.util.ArrayList;
import java.util.Calendar;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

//import com.examples.android.calendar.CalendarAdapter;
//import com.examples.android.calendar.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class MainActivity extends Activity {
	DatabaseHelper db = new DatabaseHelper(this);
	Button logLeaveButton;
	public Calendar month;
	public CalendarAdapter adapter;
	public Handler handler;
	public ArrayList<String> items;
	
	//Resource class for Firebase use
	public static class Resource {
		  String date;
		  String name;
		  String type;
		  	  
		  public Resource() {
		    // empty default constructor, necessary for Firebase to be able to deserialize blog posts
		  }
		  
		  public String getDate() {
			    return date;
		  }
		  public String getName() {
		    return name;
		  }
		  public String getType() {
		    return type;
		  }
		  
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		//Initialize Calendar View with Gridview
		month = Calendar.getInstance();
		setDateToday();
		
		items = new ArrayList<String>();
		adapter = new CalendarAdapter(this, month);
		   
		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(adapter);
		
		//Get Firebase Data
		Firebase.setAndroidContext(this);
		Firebase ref = new Firebase("https://goschedule-50998.firebaseio.com/dates");
		
		ref.addValueEventListener(new ValueEventListener() {
		    @Override
		    public void onDataChange(DataSnapshot snapshot) {
		        //System.out.println("There are " + snapshot.getChildrenCount() + " blog posts");
		    	db.deleteAll();
		        for (DataSnapshot personSnapshot: snapshot.getChildren()) {
		          Resource person = personSnapshot.getValue(Resource.class);
		          db.createLog(person.getName(),person.getDate(),person.getType());
		          handler = new Handler();
		  		  handler.post(calendarUpdater);
		          //System.out.println(post.getAuthor() + " - " + post.getTitle());
		        }
		    }
		    @Override
		    public void onCancelled(FirebaseError firebaseError) {
		          System.out.println("The read failed: " + firebaseError.getMessage());
		    }
		});
		
		   
		TextView title  = (TextView) findViewById(R.id.title);
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
		   
		TextView previous  = (TextView) findViewById(R.id.previous);
		previous.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(month.get(Calendar.MONTH)== month.getActualMinimum(Calendar.MONTH)) { 
					month.set((month.get(Calendar.YEAR)-1),month.getActualMaximum(Calendar.MONTH),1);
				} else {
					month.set(Calendar.MONTH,month.get(Calendar.MONTH)-1);
				}
				refreshCalendar();
			}
		});
		
		TextView next  = (TextView) findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(month.get(Calendar.MONTH)== month.getActualMaximum(Calendar.MONTH)) { 
					month.set((month.get(Calendar.YEAR)+1),month.getActualMinimum(Calendar.MONTH),1);
				} else {
					month.set(Calendar.MONTH,month.get(Calendar.MONTH)+1);
				}
				refreshCalendar();
			}
		});
		
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				TextView date = (TextView)v.findViewById(R.id.date);
				if(date instanceof TextView && !date.getText().equals("")) {
				        
				        Intent intent = new Intent(MainActivity.this, ViewLeaveActivity.class);
				        String day = date.getText().toString();
				        if(day.length()==1) {
				        	day = "0"+day;
				        }
				        String years = "" + month.get(Calendar.YEAR);
				        String months = "" + (month.get(Calendar.MONTH)+1);
				        if(months.length()==1) {
				        	months = "0"+months;
				        }
				        // return chosen date as string format 
				        //intent.putExtra("date", android.text.format.DateFormat.format("yyyy-MM", month)+"-"+day);
				        //String[] dateArr = android.text.format.DateFormat.format("yyyy-MM", month).toString().split("-");
				        intent.putExtra("year", years);
				        intent.putExtra("month", months);
				        intent.putExtra("day", day);
				        startActivity(intent);
				}
				       
			}
		});		
		
		Button logLeaveButton = (Button) findViewById(R.id.leavebutton); 
		logLeaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	//Log.i("network frag list"," test convert view");
            	logLeave(v);
            }
        });
		
		
		  
		//Intent intentHome = getIntent();
		//String message = intentHome.getStringExtra("message");
		//Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
	
	public void logLeave(View view) {
        Intent intent1 = new Intent(this, LeaveActivity.class);
        intent1.putExtra("message", "Please Log Details");
        startActivity(intent1);
    }

	public void refreshCalendar()
	{
		TextView title  = (TextView) findViewById(R.id.title);
		adapter.refreshDays();
		adapter.notifyDataSetChanged(); 
		handler.post(calendarUpdater); // generate some random calendar items 
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	}
	
	//public void onNewIntent(Intent intent) {
	//String date = intent.getStringExtra("date");
	//String[] dateArr = date.split("-"); // date format is yyyy-mm-dd
	//test date today
	//month.set(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1]), Integer.parseInt(dateArr[2]));
	//} 
	public void setDateToday() {
		Calendar calendar = Calendar.getInstance();
		int thisYear = calendar.get(Calendar.YEAR);
		int thisMonth = calendar.get(Calendar.MONTH);
		int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
		month.set(thisYear, thisMonth, thisDay);
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

	public Runnable calendarUpdater = new Runnable() {
		
		@Override
		public void run() {
			items.clear();
			String toPass;
			String years = "" + month.get(Calendar.YEAR);
	        String months = "" + (month.get(Calendar.MONTH)+1);
	        if(months.length()==1) {
	        	months = "0"+months;
	        }
	        Log.i("TestMain",months+years);
	        
			// format random values. You can implement a dedicated class to provide real values
			for(int i=0;i<=31;i++) {
				
				toPass = Integer.toString(i);
				if (toPass.length() == 1){
					toPass = "0" + toPass;
				}
				
				if (db.dateHit(toPass, months+years)){
					items.add(Integer.toString(i));
				}
				
			}

			adapter.setItems(items);
			adapter.notifyDataSetChanged();
		}
	};

		
	
}
