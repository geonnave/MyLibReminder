package com.geonnave.mylibreminder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private ArrayList<Loan> loans = new ArrayList<Loan>();
	
    private ListView lv;

    private SimpleAdapter sadapter;

    private ArrayList<HashMap<String, String>> productList;
    
	ArrayList<HashMap<String, String>> linesList = new ArrayList<HashMap<String, String>>();
	
	private int nLoans, hourOfDay = 9;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		loadData();
		if (loans.size() > 0)
			SetMyAlarm(loans.get(0));
		constructMap();
		setupListView();
	}
	
	private void setupListView() {
		nLoans = linesList.size();
		lv = (ListView) findViewById(R.id.list_reminders);
		sadapter = new SimpleAdapter(this, linesList, R.layout.list_loans, new String[] {"book", "library", "date"}, 
									 new int[] {R.id.book_name, R.id.library, R.id.time_restore});
		lv.setAdapter(sadapter);
		lv.setOnItemClickListener(new OnItemClickListener(){
	        @Override
	        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
	                long arg3) {
	        	Intent intent = new Intent(view.getContext(), AddLoan.class);
	        	int id = loans.get(arg2).getId();
	        	intent.putExtra("LoanID", id);
	        	intent.putExtra("BookName", loans.get(arg2).getBook());
	        	intent.putExtra("LibraryName", loans.get(arg2).getLibrary());
	        	intent.putExtra("Date", loans.get(arg2).getDate());
//	        	Toast.makeText(getBaseContext(), "item "+id, Toast.LENGTH_SHORT).show();
    			startActivity(intent);
	        }
	    });
	}
	
	private void loadData(){
		LoanDAO loanDAO = LoanDAO.getInstance(getBaseContext());
		loans = (ArrayList<Loan>) loanDAO.getAll();
		Collections.sort(loans, new LoanComparator());
//		if (loans.size() > 0)
//			SetMyAlarm(loans.get(0));
	}
	
	private void constructMap() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		linesList.clear();
//		Collections.sort(loans);
		for (int i = 0; i < loans.size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
//			map.put("book", Integer.toString(loans.get(i).getId()));
			map.put("book", loans.get(i).getBook());
			map.put("library", loans.get(i).getLibrary());
			map.put("date", sdf.format(loans.get(i).getDate().getTime()));
			linesList.add(map);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    public void addLoan(View view){
    	Intent intent = new Intent(MainActivity.this, AddLoan.class);
    	intent.putExtra("LoanID", nLoans+1);
    	startActivity(intent);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	loadData();
    	constructMap();
		nLoans = linesList.size();
    	lv.invalidateViews();
    }
    
    public void SetMyAlarm(Loan loan) {
    	Calendar cal = new GregorianCalendar();
    	Calendar dateAlarm = loan.getDate();
    	dateAlarm.set(Calendar.HOUR_OF_DAY, hourOfDay);
//    	dateAlarm.set(Calendar.HOUR_OF_DAY, 22);
		dateAlarm.set(Calendar.MINUTE, 0);
		dateAlarm.set(Calendar.SECOND, 0);

//		Toast.makeText(this, "time now is "+cal.get(Calendar.MINUTE), Toast.LENGTH_LONG).show();
		
		if (cal.get(Calendar.DAY_OF_MONTH) == dateAlarm.get(Calendar.DAY_OF_MONTH) && 
			cal.get(Calendar.HOUR_OF_DAY) < dateAlarm.get(Calendar.HOUR_OF_DAY)) {
			long AlarmTime = dateAlarm.getTimeInMillis();
	    	
	        Intent intent = new Intent(this, AlarmReceiverActivity.class); // The broadcast receiver that will handle my alarm 
	
	        int UniqueID = 8194; // id for this specific alarm, use a different id for each separate alarm
	
	        PendingIntent sender = PendingIntent.getActivity(this, UniqueID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	        
	        AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
	        am.set(AlarmManager.RTC_WAKEUP, AlarmTime, sender);
		}
//        sender.
    }

}
