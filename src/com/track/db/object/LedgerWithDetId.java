package com.track.db.object;

import java.util.List;

public class LedgerWithDetId {
	private String ACCOUNT;
	
	private List<LedgerDetailsWithId> LEDGER_DETAILS;
	
	private Double OPENING_BALANCE;
	
	private Double CLOSING_BALANCE;

	public String getACCOUNT() {
		return ACCOUNT;
	}

	public void setACCOUNT(String aCCOUNT) {
		ACCOUNT = aCCOUNT;
	}

	public List<LedgerDetailsWithId> getLEDGER_DETAILS() {
		return LEDGER_DETAILS;
	}

	public void setLEDGER_DETAILS(List<LedgerDetailsWithId> lEDGER_DETAILS) {
		LEDGER_DETAILS = lEDGER_DETAILS;
	}

	public Double getOPENING_BALANCE() {
		return OPENING_BALANCE;
	}

	public void setOPENING_BALANCE(Double oPENING_BALANCE) {
		OPENING_BALANCE = oPENING_BALANCE;
	}

	public Double getCLOSING_BALANCE() {
		return CLOSING_BALANCE;
	}

	public void setCLOSING_BALANCE(Double cLOSING_BALANCE) {
		CLOSING_BALANCE = cLOSING_BALANCE;
	}
	
	
}
