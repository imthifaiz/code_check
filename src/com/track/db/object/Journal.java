package com.track.db.object;

import java.util.List;

public class Journal {
	
	private JournalHeader journalHeader;
	
	private List<JournalDetail> journalDetails;
	
	private List<JournalAttachment> journalAttachment;

	public JournalHeader getJournalHeader() {
		return journalHeader;
	}

	public void setJournalHeader(JournalHeader journalHeader) {
		this.journalHeader = journalHeader;
	}

	public List<JournalDetail> getJournalDetails() {
		return journalDetails;
	}

	public void setJournalDetails(List<JournalDetail> journalDetails) {
		this.journalDetails = journalDetails;
	}

	public List<JournalAttachment> getJournalAttachment() {
		return journalAttachment;
	}

	public void setJournalAttachment(List<JournalAttachment> journalAttachment) {
		this.journalAttachment = journalAttachment;
	}

	@Override
	public String toString() {
		return "Journal [journalHeader=" + journalHeader + ", journalDetails=" + journalDetails + ", journalAttachment="
				+ journalAttachment + "]";
	}
	
	

}
