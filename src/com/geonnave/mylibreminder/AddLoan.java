package com.geonnave.mylibreminder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class AddLoan extends Activity  implements OnSeekBarChangeListener {
	

	private SeekBar radiusBar;
	private TextView radiusText;
	
	private TextView replaceDate;
	private EditText editBook, editLibrary;
	private int rollDays, INITRD = 7;
	
	private Button buttonSave;
	
	LoanDAO loanDAO;
 
	private final Calendar c = Calendar.getInstance();
	
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_loan);
		intent = getIntent();
		loanDAO = LoanDAO.getInstance(getBaseContext());
		rollDays = INITRD;
		setupRadiusBar();
		setCurrentDateOnView();
		AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.library_name);
		String[] libraries = {"Unicentro CEDETEG", "Unicentro Santa Cruz"};
		ArrayAdapter<String> adapter = 
		        new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, libraries);
		textView.setAdapter(adapter);
		buttonSave = (Button) findViewById(R.id.button_save);
		editBook = (EditText) findViewById(R.id.book_name);
		editLibrary = (EditText) findViewById(R.id.library_name);
		String book = intent.getStringExtra("BookName");
		String library = intent.getStringExtra("LibraryName");
		editBook.setText(book);
		editLibrary.setText(library);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_loan, menu);
		return true;
	}
	
	public void setupRadiusBar() {
		radiusBar = (SeekBar) findViewById(R.id.seek_days);
		radiusText = (TextView) findViewById(R.id.replace_n_days);
		
		radiusBar.setOnSeekBarChangeListener(this);
		radiusBar.setMax(27);
		radiusBar.setProgress(INITRD);
		radiusText.setText(Integer.toString(INITRD));
	}

	public void setCurrentDateOnView() {
		replaceDate = (TextView) findViewById(R.id.replace_date);
		Calendar c1 = (Calendar) c.clone();
		c1.add(Calendar.DAY_OF_MONTH, rollDays);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		replaceDate.setText("("+sdf.format(c1.getTime())+")");
	}
	
	public void save(View view) {
		Calendar remindDate = new GregorianCalendar();
		remindDate.add(Calendar.DAY_OF_MONTH, rollDays);
		String bn = editBook.getText().toString();
		String ln = editLibrary.getText().toString();
		int id = loanDAO.getNIds();
		if (bn.length() > 1 && ln.length() > 1) {
			Loan loan = new Loan(intent.getIntExtra("LoanID", 1), bn, ln, remindDate);
			if (intent.getStringExtra("BookName") == null)
				loanDAO.save(loan);
			else {
				loanDAO.delete(intent.getIntExtra("LoanID", 1));
				loanDAO.save(loan);
			}
			Toast.makeText(this, R.string.reminder_saved, Toast.LENGTH_LONG).show();
			finish();
		} else {
			Toast.makeText(this, R.string.not_null, Toast.LENGTH_LONG).show();
		}
	}
	
	public void cancel(View view) {
		finish();
	}
	
	public void delete(View view) {
//		loanDAO.deleteAll();
		if (intent.getStringExtra("BookName") != null) {
			loanDAO.delete(intent.getIntExtra("LoanID", 1));
			Toast.makeText(this, R.string.reminder_deleted, Toast.LENGTH_LONG).show();
			finish();
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		progress++;
		radiusText.setText(Integer.toString(progress));
		rollDays = progress;
		setCurrentDateOnView();
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		
	}
	
	@Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
	
	
}
