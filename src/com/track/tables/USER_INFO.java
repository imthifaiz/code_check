package com.track.tables;

// importing classes

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.gates.Generator;
import com.track.gates.sqlBean;
import com.track.util.MLogger;

public class USER_INFO {
	private boolean printQuery = MLoggerConstant.PLTMST_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.PLTMST_PRINTPLANTMASTERLOG;
	sqlBean sb;
	Generator gn;
	String sPLANT = "SIS";
	String sUSER_ID = "";
	String sUSER_NAME = "";
	String sEMAIL = "";
	int iId;
	
	public String getPLANT() {
		return sPLANT;
	}
	public void setPLANT(String aPLANT) {
		sPLANT= aPLANT;
	}
	public String getUSER_ID() {
		return sUSER_ID;
	}
	public void setUSER_ID(String aUSER_ID) {
		sUSER_ID= aUSER_ID;
	}
	public String getEMAIL() {
		return sEMAIL;
	}
	public void setEMAIL(String aEMAIL) {
		sEMAIL= aEMAIL;
	}
	public String getUSER_NAME() {
		return sUSER_NAME;
	}
	public void setUSER_NAME(String aUSER_NAME) {
		sUSER_NAME= aUSER_NAME;
	}
	public int getId() {
		return iId;
	}
	public void setId(int aId) {
		iId= aId;
	}
}
