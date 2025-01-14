package com.track.tables;

// importing classes

import java.sql.Connection;
import java.sql.ResultSet;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.gates.Generator;
import com.track.gates.sqlBean;
import com.track.util.MLogger;

public class TOHDR {
	private boolean printQuery = MLoggerConstant.TOHDR_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.TOHDR_PRINTPLANTMASTERLOG;
	sqlBean sb;
	Generator gn;
	private String createBy = "";
	private String createDate = "";
	private String updateBy = "";
	private String updateDate = "";
	private String recStatus = "";
	private String userFiled1 = "";
	private String userFiled2 = "";
	private String userFiled3 = "";
	private String userFiled4 = "";
	private String userFiled5 = "";
	private String userFiled6 = "";
	private String userFlag1 = "";
	private String userFlag2 = "";
	private String userFlag3 = "";
	private String userFlag4 = "";
	private String userFlag5 = "";
	private String userFlag6 = "";
	private String userTime1 = "";
	private String userTime2 = "";
	private String userTime3 = "";
	private float userDbl1;
	private float userDbl2;
	private float userDbl3;
	private float userDbl4;
	private float userDbl5;
	private float userDbl6;
	private String plant = "";
	private String toNo = "";
	private String soNo = "";
	private String custNo = "";
	private String ordDate = "";
	private String delDate = "";
	private String shipMode = "";
	private String shipVia = "";
	private String terms = "";
	private String toType = "";
	private String toStatus = "";
	private double totQty = 0.0;
	private double totWeight = 0.0;
	private String comment1 = "";
	private String comment2 = "";
	private String lblGroup = "";
	private int lblCat = 0;
	private int lblType = 0;
	private int priority;
	private String ordTrak1 = "";
	private String ordTrak2 = "";

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getRecStatus() {
		return recStatus;
	}

	public void setRecStatus(String recStatus) {
		this.recStatus = recStatus;
	}

	public String getUserFiled1() {
		return userFiled1;
	}

	public void setUserFiled1(String userFiled1) {
		this.userFiled1 = userFiled1;
	}

	public String getUserFiled2() {
		return userFiled2;
	}

	public void setUserFiled2(String userFiled2) {
		this.userFiled2 = userFiled2;
	}

	public String getUserFiled3() {
		return userFiled3;
	}

	public void setUserFiled3(String userFiled3) {
		this.userFiled3 = userFiled3;
	}

	public String getUserFiled4() {
		return userFiled4;
	}

	public void setUserFiled4(String userFiled4) {
		this.userFiled4 = userFiled4;
	}

	public String getUserFiled5() {
		return userFiled5;
	}

	public void setUserFiled5(String userFiled5) {
		this.userFiled5 = userFiled5;
	}

	public String getUserFiled6() {
		return userFiled6;
	}

	public void setUserFiled6(String userFiled6) {
		this.userFiled6 = userFiled6;
	}

	public String getUserFlag1() {
		return userFlag1;
	}

	public void setUserFlag1(String userFlag1) {
		this.userFlag1 = userFlag1;
	}

	public String getUserFlag2() {
		return userFlag2;
	}

	public void setUserFlag2(String userFlag2) {
		this.userFlag2 = userFlag2;
	}

	public String getUserFlag3() {
		return userFlag3;
	}

	public void setUserFlag3(String userFlag3) {
		this.userFlag3 = userFlag3;
	}

	public String getUserFlag4() {
		return userFlag4;
	}

	public void setUserFlag4(String userFlag4) {
		this.userFlag4 = userFlag4;
	}

	public String getUserFlag5() {
		return userFlag5;
	}

	public void setUserFlag5(String userFlag5) {
		this.userFlag5 = userFlag5;
	}

	public String getUserFlag6() {
		return userFlag6;
	}

	public void setUserFlag6(String userFlag6) {
		this.userFlag6 = userFlag6;
	}

	public String getUserTime1() {
		return userTime1;
	}

	public void setUserTime1(String userTime1) {
		this.userTime1 = userTime1;
	}

	public String getUserTime2() {
		return userTime2;
	}

	public void setUserTime2(String userTime2) {
		this.userTime2 = userTime2;
	}

	public String getUserTime3() {
		return userTime3;
	}

	public void setUserTime3(String userTime3) {
		this.userTime3 = userTime3;
	}

	public float getUserDbl1() {
		return userDbl1;
	}

	public void setUserDbl1(float userDbl1) {
		this.userDbl1 = userDbl1;
	}

	public float getUserDbl2() {
		return userDbl2;
	}

	public void setUserDbl2(float userDbl2) {
		this.userDbl2 = userDbl2;
	}

	public float getUserDbl3() {
		return userDbl3;
	}

	public void setUserDbl3(float userDbl3) {
		this.userDbl3 = userDbl3;
	}

	public float getUserDbl4() {
		return userDbl4;
	}

	public void setUserDbl4(float userDbl4) {
		this.userDbl4 = userDbl4;
	}

	public float getUserDbl5() {
		return userDbl5;
	}

	public void setUserDbl5(float userDbl5) {
		this.userDbl5 = userDbl5;
	}

	public float getUserDbl6() {
		return userDbl6;
	}

	public void setUserDbl6(float userDbl6) {
		this.userDbl6 = userDbl6;
	}

	public String getPlant() {
		return plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getToNo() {
		return toNo;
	}

	public void setToNo(String toNo) {
		this.toNo = toNo;
	}

	public String getSoNo() {
		return soNo;
	}

	public void setSoNo(String soNo) {
		this.soNo = soNo;
	}

	public String getCustNo() {
		return custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public String getOrdDate() {
		return ordDate;
	}

	public void setOrdDate(String ordDate) {
		this.ordDate = ordDate;
	}

	public String getDelDate() {
		return delDate;
	}

	public void setDelDate(String delDate) {
		this.delDate = delDate;
	}

	public String getShipMode() {
		return shipMode;
	}

	public void setShipMode(String shipMode) {
		this.shipMode = shipMode;
	}

	public String getShipVia() {
		return shipVia;
	}

	public void setShipVia(String shipVia) {
		this.shipVia = shipVia;
	}

	public String getTerms() {
		return terms;
	}

	public void setTerms(String terms) {
		this.terms = terms;
	}

	public String getToType() {
		return toType;
	}

	public void setToType(String toType) {
		this.toType = toType;
	}

	public String getToStatus() {
		return toStatus;
	}

	public void setToStatus(String toStatus) {
		this.toStatus = toStatus;
	}

	public double getTotQty() {
		return totQty;
	}

	public void setTotQty(double totQty) {
		this.totQty = totQty;
	}

	public double getTotWeight() {
		return totWeight;
	}

	public void setTotWeight(double totWeight) {
		this.totWeight = totWeight;
	}

	public String getComment1() {
		return comment1;
	}

	public void setComment1(String comment1) {
		this.comment1 = comment1;
	}

	public String getComment2() {
		return comment2;
	}

	public void setComment2(String comment2) {
		this.comment2 = comment2;
	}

	public String getLblGroup() {
		return lblGroup;
	}

	public void setLblGroup(String lblGroup) {
		this.lblGroup = lblGroup;
	}

	public int getLblCat() {
		return lblCat;
	}

	public void setLblCat(int lblCat) {
		this.lblCat = lblCat;
	}

	public int getLblType() {
		return lblType;
	}

	public void setLblType(int lblType) {
		this.lblType = lblType;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getOrdTrak1() {
		return ordTrak1;
	}

	public void setOrdTrak1(String ordTrak1) {
		this.ordTrak1 = ordTrak1;
	}

	public String getOrdTrak2() {
		return ordTrak2;
	}

	public void setOrdTrak2(String ordTrak2) {
		this.ordTrak2 = ordTrak2;
	}

	/**
	 * constructor method TODET sb - SQL Bean gn - Utilites
	 */
	public TOHDR() throws Exception {
		sb = new sqlBean();
		gn = new Generator();
	}

	/**
	 * DBConnection() get connection pool from the database
	 * 
	 */
	public Connection getDBConnection() throws Exception {
		Connection con = DbBean.getConnection();
		return con;
	}

	/**
	 * Insert Command - TODET
	 * 
	 * 
	 */
	public int insertTOHDR() throws Exception {
		String sql;
		Connection con = getDBConnection();
		ResultSet rs = null;
		int returnCode = 1;
		if (con == null) {
			returnCode = 100; // Could not get Database Connection..Please check
			// the DSN, UserName and Password in the
			// db_param file
		}
		try {

			sql = "INSERT INTO TOHDR values('COM', '" + toNo + "','" + soNo
					+ "','" + custNo + "','" + ordDate + "','" + delDate
					+ "', '" + shipMode + "', '" + shipVia + "', '" + terms
					+ "', '" + toType + "', '" + toStatus + "','" + totQty
					+ "','" + totWeight + "','" + comment1 + "', '" + comment2
					+ "', '" + lblGroup + "', '" + lblCat + "', '" + lblType
					+ "', '" + ordTrak1 + "', '" + ordTrak2 + "', '" + priority
					+ "', '" + createDate + "', '" + createBy + "', '"
					+ updateDate + "', '" + updateBy + "', '" + recStatus
					+ "', '" + userFiled1 + "', '" + userFiled2 + "', '"
					+ userFiled3 + "', '" + userFiled4 + "', '" + userFiled5
					+ "', '" + userFiled6 + "', '" + userFlag1 + "', '"
					+ userFlag2 + "', '" + userFlag3 + "', '" + userFlag4
					+ "', '" + userFlag5 + "', '" + userFlag6 + "', '"
					+ userTime1 + "','" + userTime2 + "','" + userTime3 + "','"
					+ userDbl1 + "', '" + userDbl2 + "', '" + userDbl3 + "','"
					+ userDbl4 + "','" + userDbl5 + "','" + userDbl6 + "')";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	public int updateSOHDR() throws Exception {
		String sql;
		Connection con = getDBConnection();
		ResultSet rs = null;
		int returnCode = 1;
		if (con == null) {
			returnCode = 100; // Could not get Database Connection..Please check
			// the DSN, UserName and Password in the
			// db_param file
		}
		try {
			sql = "Update SOHDR set PLANT= 'COM',TONO= '" + toNo
					+ "',CUSTNO= '" + custNo + "',ORDDATE= '" + ordDate
					+ "',DELDATE= '" + delDate + "',SHIPMOD= '" + shipMode
					+ "',SHIPVIA= '" + shipVia + "',TERMS= '" + terms
					+ "',TOTYPE= '" + toType + "',TOSTAT= '" + toStatus
					+ "',TOTQTY = " + totQty + ",TOTWGT = " + totWeight
					+ ",COMMENT1= '" + comment1 + "',COMMENT2= '" + comment2
					+ "',LBLGROUP= '" + lblGroup + "',LBLCAT = " + lblCat
					+ ",LBLTYPE = " + lblType + ",ORDTRK1= '" + ordTrak1
					+ "',ORDTRK2= '" + ordTrak2 + "',PRIORTY = " + priority
					+ ",CRAT = '" + createDate + "',CRBY= '" + createBy
					+ "',UPAT = '" + updateDate + "',UPBY= '" + updateBy
					+ "',RECSTAT= '" + recStatus + "',USERFLD1= '" + userFiled1
					+ "',USERFLD2= '" + userFiled2 + "',USERFLD3= '"
					+ userFiled3 + "',USERFLD4= '" + userFiled4
					+ "',USERFLD5= '" + userFiled5 + "',USERFLD6= '"
					+ userFiled6 + "',USERFLG1= '" + userFlag1
					+ "',USERFLG2= '" + userFlag2 + "',USERFLG3= '" + userFlag3
					+ "',USERFLG4= '" + userFlag4 + "',USERFLG5= '" + userFlag5
					+ "',USERFLG6= '" + userFlag6 + "',USERTIME1 = '"
					+ userTime1 + "',USERTIME2 = '" + userTime2
					+ "',USERTIME3 = '" + userTime3 + "',USERDBL1 = "
					+ userDbl1 + ",USERDBL2 = " + userDbl2 + ",USERDBL3 = "
					+ userDbl3 + ",USERDBL4 = " + userDbl4 + ",USERDBL5 = "
					+ userDbl5 + ",USERDBL6 = " + userDbl6 + " where";
			sb.setmLogger(mLogger);
			returnCode = sb.insertRecords(sql);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con);
		}
		return returnCode;
	}

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

}
