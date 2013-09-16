package com.geonnave.mylibreminder;

import java.util.Comparator;

public class LoanComparator implements Comparator<Loan> {

	@Override
	public int compare(Loan lhs, Loan rhs) {
		// TODO Auto-generated method stub
		return lhs.getDate().compareTo(rhs.getDate());
	}

}
