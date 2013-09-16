package com.geonnave.mylibreminder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Loan {
	
	private int id;
	private String book, library;
	private Calendar date;
	
	public Loan() {
		// TODO Auto-generated constructor stub
	}
	
	public Loan(int id, String book, String library, Calendar date ){
		this.id = id;
		this.book = book;
		this.library = library;
		this.date = date;
	}
	
	public String getBook() {
		return book;
	}

	public void setBook(String book) {
		this.book = book;
	}

	public String getLibrary() {
		return library;
	}

	public void setLibrary(String library) {
		this.library = library;
	}

	public Calendar getDate() {
		return date;
	}
	
	public String getFormatedDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());
	}

	public void setDate(Calendar date) {
		this.date = date;
	}
	
	public void setDate(int year, int month, int day) {
		date.set(year, month, day);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String toString(){
		return "Restore book "+book + " at "+date.getTime()+" to library "+library;
	}

//	public static void Main(String[] args) {
//		Calendar c1 = new GregorianCalendar(); 
//		Loan l1 = new Loan("Book1", "Lirary1", c1);
//		System.out.println(l1);
//	}
	
}
