package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.naming.NamingException;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.db.object.HrEmpType;
import com.track.db.object.HrEmpUserInfo;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;
import com.track.util.StrUtils;

public class EmployeeDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.EmployeeDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.EmployeeDAO_PRINTPLANTMASTERLOG;

	public boolean isPrintQuery() {
		return printQuery;
	}

	public void setPrintQuery(boolean printQuery) {
		this.printQuery = printQuery;
	}

	public boolean isPrintLog() {
		
		
		
		return printLog;
	}

	public void setPrintLog(boolean printLog) {
		this.printLog = printLog;
	}

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public EmployeeDAO() {

	}

	public boolean insertIntoEmployeeMst(Hashtable ht) throws Exception {
		boolean insertedInv = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ "EMP_MST" + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, query);
			insertedInv = insertData(con, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return insertedInv;
	}
	
	public int insertIntoEmployeeMstretid(Hashtable ht) throws Exception {
		int insertedInv = 0;
		java.sql.Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
					+ "EMP_MST" + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, query);
			 ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			//insertedInv = insertData(con, query);
			 int count=ps.executeUpdate();
			   if(count>0)
			   {
				   ResultSet rs = ps.getGeneratedKeys();
	                if(rs.next())
	                {
	                	insertedInv = rs.getInt(1);   
	                }
			   }

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return insertedInv;
	}

	public boolean isExists(Hashtable ht) throws Exception {

		boolean flag = false;
		java.sql.Connection con = null;

		try {
			con = com.track.gates.DbBean.getConnection();

			// query
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append(" 1 ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "EMP_MST"
					+ "]");
			sql.append(" WHERE  " + formCondition(ht));
			this.mLogger.query(this.printQuery, sql.toString());
			flag = isExists(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;
	}

	public boolean updateEmployeeMst(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean update = false;
		PreparedStatement ps = null;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			String sUpdate = " ", sCondition = " ";

			Enumeration enumUpdate = htUpdate.keys();
			for (int i = 0; i < htUpdate.size(); i++) {
				String key = StrUtils
						.fString((String) enumUpdate.nextElement());
				String value = StrUtils.fString((String) htUpdate.get(key));
				sUpdate += key.toUpperCase() + " = '" + value + "',";
			}

			Enumeration enumCondition = htCondition.keys();
			for (int i = 0; i < htCondition.size(); i++) {
				String key = StrUtils.fString((String) enumCondition
						.nextElement());
				String value = StrUtils.fString((String) htCondition.get(key));
				sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
						+ "' AND ";

			}
			sUpdate = (sUpdate.length() > 0) ? " SET "
					+ sUpdate.substring(0, sUpdate.length() - 1) : "";
			sCondition = (sCondition.length() > 0) ? " WHERE  "
					+ sCondition.substring(0, sCondition.length() - 4) : "";

			String stmt = "UPDATE " + "[" + htCondition.get("PLANT") + "_"
					+ "EMP_MST" + "]" + sUpdate + sCondition;
			this.mLogger.query(this.printQuery, stmt);
			ps = con.prepareStatement(stmt);
			int iCnt = ps.executeUpdate();
			if (iCnt > 0)
				update = true;

		} catch (Exception e) {
			throw e;
		} finally {
			DbBean.closeConnection(con, ps);
		}

		return update;
	}

	public boolean deleteEmployeeId(java.util.Hashtable ht) throws Exception {
		MLogger.log(1, this.getClass() + " deletePrdId()");
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "EMP_MST"
					+ "]");
			sql.append(" WHERE " + formCondition(ht));

			this.mLogger.query(this.printQuery, sql.toString());
			delete = updateData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return delete;
	}

	public ArrayList getEmployeeDetails(String empno, String plant,
			String extraCon) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "SELECT ID,EMPNO,ISNULL(EMPUSERID,'')EMPUSERID,ISNULL(ISAUTOEMAIL,'0')ISAUTOEMAIL,ISNULL(PASSWORD,'')PASSWORD,ISNULL(REPORTING_INCHARGE,'')REPORTING_INCHARGE,ISNULL(FNAME,'')FNAME,ISNULL(LNAME,'')LNAME,ISNULL(EMPLOYEETYPEID,'0')EMPLOYEETYPEID,ISNULL(NUMBEROFMANDAYS,'0.0')NUMBEROFMANDAYS,ISNULL(GENDER,'')GENDER,ISNULL(DATEOFBIRTH,'')DOB,ISNULL(DEPTARTMENT,'')DEPT,ISNULL(DESGINATION,'')DESGINATION,ISNULL(DATEOFJOINING,'') DATEOFJOINING,ISNULL(DATEOFLEAVING,'') DATEOFLEAVING,ISNULL(NATIONALITY,'')NATIONALITY,ISNULL(TELNO,'')TELNO,ISNULL(HPNO,'')HPNO,ISNULL(EMAIL,'')EMAIL,ISNULL(SKYPEID,'')SKYPEID,ISNULL(FACEBOOKID,'')FACEBOOKID,ISNULL(TWITTERID,'')TWITTERID,ISNULL(LINKEDINID,'')LINKEDINID,ISNULL(PASSPORTNUMBER,'')PASSPORTNUMBER,ISNULL(COUNTRYOFISSUE,'')COUNTRYOFISSUE,ISNULL(PASSPORTEXPIRYDATE,'')PASSPORTEXPIRYDATE,ISNULL(UNITNO,'')ADDR1,ISNULL(BUILDING,'')ADDR2,ISNULL(STREET,'')ADDR3,ISNULL(CITY,'')ADDR4,ISNULL(COUNTRY,'')COUNTRY,ISNULL(ZIP,'')ZIP,ISNULL(EMIRATESID,'')EMIRATESID,ISNULL(EMIRATESIDEXPIRY,'')EMIRATESIDEXPIRY,ISNULL(VISANUMBER,'')VISANUMBER,ISNULL(VISAEXPIRYDATE,'')VISAEXPIRYDATE,ISNULL(LABOURCARDNUMBER,'')LABOURCARDNUMBER,ISNULL(WORKPERMITNUMBER,'')WORKPERMITNUMBER,ISNULL(CONTRACTSTARTDATE,'')CONTRACTSTARTDATE,ISNULL(CONTRACTENDDATE,'')CONTRACTENDDATE,ISNULL(BANKNAME,'')BANKNAME,ISNULL((select top 1 BRANCH_NAME from "+plant+"_BANKMST where Name=BANKNAME ),'')BRANCH,ISNULL(IBAN,'')IBAN,ISNULL(BANKROUTINGCODE,'')BANKROUTINGCODE,ISNULL(BASICSALARY,0)BASICSALARY,ISNULL(HOUSERENTALLOWANCE,0)HOUSERENTALLOWANCE,ISNULL(TRANSPORTALLOWANCE,0)TRANSPORTALLOWANCE,ISNULL(COMMUNICATIONALLOWANCE,0)COMMUNICATIONALLOWANCE,ISNULL(OTHERALLOWANCE,0)OTHERALLOWANCE,ISNULL(BONUS,0)BONUS,ISNULL(COMMISSION,0)COMMISSION,ISNULL(REMARKS,'')REMARKS,ISNULL(IsActive,'')IsActive,ISNULL(STATE,'') STATE,ISNULL(CATLOGPATH,'') CATLOGPATH,ISNULL((SELECT COUNTRY_CODE FROM "+ plant+"_countrymaster WHERE COUNTRYNAME=COUNTRY),'') COUNTRY_CODE,ISNULL(GRATUITY,0)GRATUITY,ISNULL(AIRTICKET,0)AIRTICKET,ISNULL(LEAVESALARY,0)LEAVESALARY,ISNULL(ISPOSCUSTOMER,'0')ISPOSCUSTOMER,ISNULL(ISEDITPOSPRODUCTPRICE,'0')ISEDITPOSPRODUCTPRICE,ISNULL(OUTLET,'')OUTLET,ISNULL(ISSALESMAN,'')ISSALESMAN,ISNULL(ISCREATEONUSERINFO,'')ISCREATEONUSERINFO,ISNULL(ISCASHIER,'')ISCASHIER,ISNULL((select top 1 OUTLET_NAME from "+plant+"_POSOUTLETS where OUTLET="+plant+"_EMP_MST.OUTLET ),'')OUTNAME,CASE WHEN ISNULL(DATEOFBIRTH,'')='' THEN 0 ELSE DATEDIFF(hour,(SUBSTRING(DATEOFBIRTH, 7, 4) + '-' + SUBSTRING(DATEOFBIRTH, 4, 2) + '-' + SUBSTRING(DATEOFBIRTH, 1, 2)),GETDATE())/8766 END EMP_AGE,ISNULL((select top 1 FNAME from "+plant+"_EMP_MST E where E.REPORTING_INCHARGE="+plant+"_EMP_MST.ID ),'')REPORTING_INCHARGENAME FROM "
					+ "["
					+ plant
					+ "_"
					+ "EMP_MST] where EMPNO like '"
					+ empno
					+ "%'   " + extraCon
					+ " ORDER BY EMPNO ";
			this.mLogger.query(this.printQuery, sQry);

			al = selectData(con, sQry);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return al;
	}

	public ArrayList getCPFCONTRIBUTIONDetails(String countrycode, String employeeage,
			String extraCon) throws Exception {
		
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			
			String sQry = "SELECT ID,COUNTRY_CODE,ISNULL(EMPLOYEE_AGE,'')EMPLOYEE_AGE,ISNULL(EMPLOYER_WAGE,'0')EMPLOYER_WAGE,ISNULL(EMPLOYEE_WAGE,'0')EMPLOYEE_WAGE,ISNULL(TOTAL_WAGE,'0')TOTAL_WAGE FROM "
					+ "[CPFCONTRIBUTION] where COUNTRY_CODE='"+countrycode+"' AND EMPLOYEE_AGE like '"
					+ employeeage
					+ "%'   " + extraCon
					+ " ORDER BY ID ";
			this.mLogger.query(this.printQuery, sQry);
			
			al = selectData(con, sQry);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		
		return al;
	}
	
	public ArrayList getEmployeeSalarydetails(String employeeno, String plant,
			String extraCon) throws Exception {
		
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			
			String sQry = "SELECT SALARYTYPE,SALARY,ISNULL(( SELECT H.ISPAYROLL_BY_BASIC_SALARY FROM ["+plant+"_HREMPSALARYMST] H WHERE H.SALARYTYPE=D.SALARYTYPE),0) ISPAYROLL_BY_BASIC_SALARY FROM "
					+ "["+plant+"_HREMPSALARYDET] D JOIN "+plant+"_EMP_MST E ON E.ID=D.EMPNOID where E.EMPNO='"+employeeno+"'"
					+ " " + extraCon
					+ " ORDER BY D.ID ";
			this.mLogger.query(this.printQuery, sQry);
			
			al = selectData(con, sQry);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		
		return al;
	}
	
	
	public ArrayList getEmployeeListStartsWithName(String CustName, String plant) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "SELECT ID,EMPNO,ISNULL(FNAME,'')FNAME,ISNULL(LNAME,'')LNAME,ISNULL(REPORTING_INCHARGE,'0') REPORTING_INCHARGE,ISNULL(EMPLOYEETYPEID,'0')EMPLOYEETYPEID,ISNULL(NUMBEROFMANDAYS,'0.0')NUMBEROFMANDAYS,ISNULL(GENDER,'')GENDER,ISNULL(DATEOFBIRTH,'')DOB,ISNULL(DEPTARTMENT,'')DEPT,ISNULL(DESGINATION,'')DESGINATION,ISNULL(DATEOFJOINING,'') DATEOFJOINING,ISNULL(DATEOFLEAVING,'') DATEOFLEAVING,ISNULL(NATIONALITY,'')NATIONALITY,ISNULL(TELNO,'')TELNO,ISNULL(HPNO,'')HPNO,ISNULL(EMAIL,'')EMAIL,ISNULL(SKYPEID,'')SKYPEID,ISNULL(FACEBOOKID,'')FACEBOOKID,ISNULL(TWITTERID,'')TWITTERID,ISNULL(LINKEDINID,'')LINKEDINID,ISNULL(PASSPORTNUMBER,'')PASSPORTNUMBER,ISNULL(COUNTRYOFISSUE,'')COUNTRYOFISSUE,ISNULL(PASSPORTEXPIRYDATE,'')PASSPORTEXPIRYDATE,ISNULL(UNITNO,'')ADDR1,ISNULL(BUILDING,'')ADDR2,ISNULL(STREET,'')ADDR3,ISNULL(CITY,'')ADDR4,ISNULL(COUNTRY,'')COUNTRY,ISNULL(ZIP,'')ZIP,ISNULL(EMIRATESID,'')EMIRATESID,ISNULL(EMIRATESIDEXPIRY,'')EMIRATESIDEXPIRY,ISNULL(VISANUMBER,'')VISANUMBER,ISNULL(VISAEXPIRYDATE,'')VISAEXPIRYDATE,ISNULL(LABOURCARDNUMBER,'')LABOURCARDNUMBER,ISNULL(WORKPERMITNUMBER,'')WORKPERMITNUMBER,ISNULL(CONTRACTSTARTDATE,'')CONTRACTSTARTDATE,ISNULL(CONTRACTENDDATE,'')CONTRACTENDDATE,ISNULL(BANKNAME,'')BANKNAME,ISNULL((select top 1 BRANCH from "+plant+"_BANKMST where Name=BANKNAME ),'')BRANCH,ISNULL(IBAN,'')IBAN,ISNULL(BANKROUTINGCODE,'')BANKROUTINGCODE,ISNULL(BASICSALARY,0)BASICSALARY,ISNULL(HOUSERENTALLOWANCE,0)HOUSERENTALLOWANCE,ISNULL(TRANSPORTALLOWANCE,0)TRANSPORTALLOWANCE,ISNULL(COMMUNICATIONALLOWANCE,0)COMMUNICATIONALLOWANCE,ISNULL(OTHERALLOWANCE,0)OTHERALLOWANCE,ISNULL(BONUS,0)BONUS,ISNULL(COMMISSION,0)COMMISSION,ISNULL(REMARKS,'')REMARKS,ISNULL(IsActive,'')IsActive,ISNULL(STATE,'') STATE,ISNULL(GRATUITY,0)GRATUITY,ISNULL(AIRTICKET,0)AIRTICKET,ISNULL(LEAVESALARY,0)LEAVESALARY   FROM "
					+ "["
					+ plant
					+ "_"
					+ "EMP_MST] " 
					//+ "where FNAME like ' "+CustName +" %'   " 
					+ "WHERE (FNAME LIKE '" + CustName + "%' or LNAME LIKE '" + CustName + "%' or EMPNO LIKE '%" + CustName + "%')"
					+ " ORDER BY FNAME ";
			/*String sQry = "SELECT EMPNO,ISNULL(FNAME,'')FNAME,ISNULL(LNAME,'')LNAME,ISNULL(GENDER,'')GENDER,ISNULL(DOB,'')DOB,ISNULL(DEPT,'')DEPT,ISNULL(DESGINATION,'')DESGINATION,ISNULL(NATIONALITY,'')NATIONALITY,ISNULL(A.TELNO,'')TELNO,ISNULL(HPNO,'')HPNO,ISNULL(A.FAX,'')FAX,ISNULL(EMAIL,'')EMAIL,ISNULL(A.ADDR1,'')ADDR1,ISNULL(A.ADDR2,'')ADDR2,ISNULL(A.ADDR3,'')ADDR3,ISNULL(A.ADDR4,'')ADDR4,ISNULL(A.COUNTRY,'')COUNTRY,ISNULL(A.ZIP,'')ZIP,ISNULL(A.REMARKS,'')REMARKS,ISNULL(A.IsActive,'')IsActive," +
					"ISNULL(B.TELNO,'')DTELNO,ISNULL(B.FAX,'')DFAX,ISNULL(B.ADD1,'')DADDR1,ISNULL(B.ADD2,'')DADDR2,ISNULL(B.ADD3,'')DADDR3,ISNULL(B.ADD4,'')DADDR4,ISNULL(B.COUNTRY,'')DCOUNTRY,ISNULL(B.ZIP,'')DZIP,ISNULL(B.REMARKS,'')DREMARKS FROM "
					+ "["
					+ plant
					+ "_"
					+ "EMP_MST] A LEFT JOIN "+ "["+ plant+ "_"+ "DEPTMST] B  ON A.DEPT = B.DEPTID " 
					//+ "where FNAME like ' "+CustName +" %'   " 
					+ "WHERE (FNAME LIKE '" + CustName + "%' or LNAME LIKE '" + CustName + "%' or EMPNO LIKE '%" + CustName + "%')"
					+ " ORDER BY FNAME ";*/
			this.mLogger.query(this.printQuery, sQry);

			al = selectData(con, sQry);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return al;
	}
	
	
	public ArrayList getEmployeeListStartsWithNameempid(String CustName, String plant,String empid) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "SELECT ID,EMPNO,ISNULL(FNAME,'')FNAME,ISNULL(LNAME,'')LNAME,ISNULL(EMPLOYEETYPEID,'0')EMPLOYEETYPEID,ISNULL(NUMBEROFMANDAYS,'0.0')NUMBEROFMANDAYS,ISNULL(GENDER,'')GENDER,ISNULL(DATEOFBIRTH,'')DOB,ISNULL(DEPTARTMENT,'')DEPT,ISNULL(DESGINATION,'')DESGINATION,ISNULL(DATEOFJOINING,'') DATEOFJOINING,ISNULL(DATEOFLEAVING,'') DATEOFLEAVING,ISNULL(NATIONALITY,'')NATIONALITY,ISNULL(TELNO,'')TELNO,ISNULL(HPNO,'')HPNO,ISNULL(EMAIL,'')EMAIL,ISNULL(SKYPEID,'')SKYPEID,ISNULL(FACEBOOKID,'')FACEBOOKID,ISNULL(TWITTERID,'')TWITTERID,ISNULL(LINKEDINID,'')LINKEDINID,ISNULL(PASSPORTNUMBER,'')PASSPORTNUMBER,ISNULL(COUNTRYOFISSUE,'')COUNTRYOFISSUE,ISNULL(PASSPORTEXPIRYDATE,'')PASSPORTEXPIRYDATE,ISNULL(UNITNO,'')ADDR1,ISNULL(BUILDING,'')ADDR2,ISNULL(STREET,'')ADDR3,ISNULL(CITY,'')ADDR4,ISNULL(COUNTRY,'')COUNTRY,ISNULL(ZIP,'')ZIP,ISNULL(EMIRATESID,'')EMIRATESID,ISNULL(EMIRATESIDEXPIRY,'')EMIRATESIDEXPIRY,ISNULL(VISANUMBER,'')VISANUMBER,ISNULL(VISAEXPIRYDATE,'')VISAEXPIRYDATE,ISNULL(LABOURCARDNUMBER,'')LABOURCARDNUMBER,ISNULL(WORKPERMITNUMBER,'')WORKPERMITNUMBER,ISNULL(CONTRACTSTARTDATE,'')CONTRACTSTARTDATE,ISNULL(CONTRACTENDDATE,'')CONTRACTENDDATE,ISNULL(BANKNAME,'')BANKNAME,ISNULL((select top 1 BRANCH from "+plant+"_BANKMST where Name=BANKNAME ),'')BRANCH,ISNULL(IBAN,'')IBAN,ISNULL(BANKROUTINGCODE,'')BANKROUTINGCODE,ISNULL(BASICSALARY,0)BASICSALARY,ISNULL(HOUSERENTALLOWANCE,0)HOUSERENTALLOWANCE,ISNULL(TRANSPORTALLOWANCE,0)TRANSPORTALLOWANCE,ISNULL(COMMUNICATIONALLOWANCE,0)COMMUNICATIONALLOWANCE,ISNULL(OTHERALLOWANCE,0)OTHERALLOWANCE,ISNULL(BONUS,0)BONUS,ISNULL(COMMISSION,0)COMMISSION,ISNULL(REMARKS,'')REMARKS,ISNULL(IsActive,'')IsActive,ISNULL(STATE,'') STATE   FROM "
					+ "["
					+ plant
					+ "_"
					+ "EMP_MST] " 
					//+ "where FNAME like ' "+CustName +" %'   " 
					+ "WHERE ID != '"+empid+"' AND (FNAME LIKE '" + CustName + "%' or LNAME LIKE '" + CustName + "%' or EMPNO LIKE '%" + CustName + "%')"
					+ " ORDER BY FNAME ";
			/*String sQry = "SELECT EMPNO,ISNULL(FNAME,'')FNAME,ISNULL(LNAME,'')LNAME,ISNULL(GENDER,'')GENDER,ISNULL(DOB,'')DOB,ISNULL(DEPT,'')DEPT,ISNULL(DESGINATION,'')DESGINATION,ISNULL(NATIONALITY,'')NATIONALITY,ISNULL(A.TELNO,'')TELNO,ISNULL(HPNO,'')HPNO,ISNULL(A.FAX,'')FAX,ISNULL(EMAIL,'')EMAIL,ISNULL(A.ADDR1,'')ADDR1,ISNULL(A.ADDR2,'')ADDR2,ISNULL(A.ADDR3,'')ADDR3,ISNULL(A.ADDR4,'')ADDR4,ISNULL(A.COUNTRY,'')COUNTRY,ISNULL(A.ZIP,'')ZIP,ISNULL(A.REMARKS,'')REMARKS,ISNULL(A.IsActive,'')IsActive," +
					"ISNULL(B.TELNO,'')DTELNO,ISNULL(B.FAX,'')DFAX,ISNULL(B.ADD1,'')DADDR1,ISNULL(B.ADD2,'')DADDR2,ISNULL(B.ADD3,'')DADDR3,ISNULL(B.ADD4,'')DADDR4,ISNULL(B.COUNTRY,'')DCOUNTRY,ISNULL(B.ZIP,'')DZIP,ISNULL(B.REMARKS,'')DREMARKS FROM "
					+ "["
					+ plant
					+ "_"
					+ "EMP_MST] A LEFT JOIN "+ "["+ plant+ "_"+ "DEPTMST] B  ON A.DEPT = B.DEPTID " 
					//+ "where FNAME like ' "+CustName +" %'   " 
					+ "WHERE (FNAME LIKE '" + CustName + "%' or LNAME LIKE '" + CustName + "%' or EMPNO LIKE '%" + CustName + "%')"
					+ " ORDER BY FNAME ";*/
			this.mLogger.query(this.printQuery, sQry);

			al = selectData(con, sQry);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return al;
	}
	public Map selectRow(String query, Hashtable ht, String extCond)
			throws Exception {
		Map map = new HashMap();
		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ "[" + ht.get("PLANT") + "_" + "EMP_MST" + "]");
			sql.append(" WHERE ");
			String conditon = formCondition(ht);
			sql.append(conditon);
			if (extCond.length() > 0)
				sql.append(" and " + extCond);
			this.mLogger.query(this.printQuery, sql.toString());

			map = getRowOfData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return map;
	}
	
	public String getEmpname(String aPlant, String aempno,String extCond) throws Exception {
		String empname = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("EMPNO", aempno);
		
		String query = " isnull(fname,'') fname ";
		Map m = selectRow(query, ht, extCond);
		empname = (String) m.get("fname");
		if (empname == "" || empname == null)
			empname = "";
		return empname;
	}	
	public String getEmplastname(String aPlant, String aempno,String extCond) throws Exception {
		String emplname = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("EMPNO", aempno);
		
		String query = " isnull(lname,'') lname ";
		Map m = selectRow(query, ht, extCond);
		emplname = (String) m.get("lname");
		if (emplname == "" || emplname == null)
			emplname = "";
		return emplname;
	}	

public List queryEmpMstDetails(String empno, String plant,String extraCon) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List listQty = new ArrayList();
        Connection con = null;
        
        try {
        //  Start code modified by Lakshmi for product brand on 11/9/12 
                con = DbBean.getConnection();
                String sQry = "SELECT EMPNO,ISNULL(FNAME,'')FNAME,ISNULL(LNAME,'')LNAME,ISNULL(GENDER,'')GENDER,ISNULL(DATEOFBIRTH,'')DOB,ISNULL(DEPTARTMENT,'')DEPT,ISNULL(DESGINATION,'')DESGINATION,ISNULL(DATEOFJOINING,'') DATEOFJOINING,ISNULL(DATEOFLEAVING,'') DATEOFLEAVING,ISNULL(NATIONALITY,'')NATIONALITY,ISNULL(TELNO,'')TELNO,ISNULL(HPNO,'')HPNO,ISNULL(EMAIL,'')EMAIL,ISNULL(SKYPEID,'')SKYPEID,ISNULL(FACEBOOKID,'')FACEBOOKID,ISNULL(TWITTERID,'')TWITTERID,ISNULL(LINKEDINID,'')LINKEDINID,ISNULL(PASSPORTNUMBER,'')PASSPORTNUMBER,ISNULL(COUNTRYOFISSUE,'')COUNTRYOFISSUE,ISNULL(PASSPORTEXPIRYDATE,'')PASSPORTEXPIRYDATE,ISNULL(UNITNO,'')ADDR1,ISNULL(BUILDING,'')ADDR2,ISNULL(STREET,'')ADDR3,ISNULL(CITY,'')ADDR4,ISNULL(COUNTRY,'')COUNTRY,ISNULL(ZIP,'')ZIP,ISNULL(EMIRATESID,'')EMIRATESID,ISNULL(EMIRATESIDEXPIRY,'')EMIRATESIDEXPIRY,ISNULL(VISANUMBER,'')VISANUMBER,ISNULL(VISAEXPIRYDATE,'')VISAEXPIRYDATE,ISNULL(LABOURCARDNUMBER,'')LABOURCARDNUMBER,ISNULL(WORKPERMITNUMBER,'')WORKPERMITNUMBER,ISNULL(CONTRACTSTARTDATE,'')CONTRACTSTARTDATE,ISNULL(CONTRACTENDDATE,'')CONTRACTENDDATE,ISNULL(BANKNAME,'')BANKNAME,ISNULL((select top 1 BRANCH from "+plant+"_BANKMST where Name=BANKNAME ),'')BRANCH,ISNULL(IBAN,'')IBAN,ISNULL(BANKROUTINGCODE,'')BANKROUTINGCODE,ISNULL(BASICSALARY,0)BASICSALARY,ISNULL(HOUSERENTALLOWANCE,0)HOUSERENTALLOWANCE,ISNULL(TRANSPORTALLOWANCE,0)TRANSPORTALLOWANCE,ISNULL(COMMUNICATIONALLOWANCE,0)COMMUNICATIONALLOWANCE,ISNULL(OTHERALLOWANCE,0)OTHERALLOWANCE,ISNULL(BONUS,0)BONUS,ISNULL(COMMISSION,0)COMMISSION,ISNULL(REMARKS,'')REMARKS,ISNULL(IsActive,'')IsActive,ISNULL(STATE,'') STATE,ISNULL(CATLOGPATH,'') CATLOGPATH,ISNULL((SELECT COUNTRY_CODE FROM "+ plant+"_countrymaster WHERE COUNTRYNAME=COUNTRY),'') COUNTRY_CODE,ISNULL(GRATUITY,0)GRATUITY,ISNULL(AIRTICKET,0)AIRTICKET,ISNULL(LEAVESALARY,0)LEAVESALARY   FROM "
    					+ "["
    					+ plant
    					+ "_"
    					+ "EMP_MST] where EMPNO like '"
    					+ empno
    					+ "%'   " + extraCon
    					+ " ORDER BY EMPNO ";
    			this.mLogger.query(this.printQuery, sQry);

                ps = con.prepareStatement(sQry);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Vector lineVec = new Vector();
                    lineVec.add(0, StrUtils.fString((String) rs.getString("EMPNO")));
                    lineVec.add(1, StrUtils.fString((String) rs.getString("FNAME")));
                    lineVec.add(2, StrUtils.fString((String) rs.getString("LNAME")));
                    lineVec.add(3, StrUtils.fString((String) rs.getString("GENDER")));
                    lineVec.add(4, StrUtils.fString((String) rs.getString("DOB")));
                    lineVec.add(5, StrUtils.fString((String) rs.getString("DEPT")));
                    lineVec.add(6, StrUtils.fString((String) rs.getString("DESGINATION")));
                    lineVec.add(7, StrUtils.fString((String) rs.getString("DATEOFJOINING")));
                    lineVec.add(8, StrUtils.fString((String) rs.getString("DATEOFLEAVING")));
                    lineVec.add(9, StrUtils.fString((String) rs.getString("NATIONALITY")));
                    lineVec.add(10, StrUtils.fString((String) rs.getString("TELNO")));
                    lineVec.add(11, StrUtils.fString((String) rs.getString("HPNO")));
                    //lineVec.add(10, StrUtils.fString((String) rs.getString("FAX")));
                    lineVec.add(12, StrUtils.fString((String) rs.getString("EMAIL")));
                    lineVec.add(13, StrUtils.fString((String) rs.getString("SKYPEID")));
                    lineVec.add(14, StrUtils.fString((String) rs.getString("FACEBOOKID")));
                    lineVec.add(15, StrUtils.fString((String) rs.getString("TWITTERID")));
                    lineVec.add(16, StrUtils.fString((String) rs.getString("LINKEDINID")));
                    lineVec.add(17, StrUtils.fString((String) rs.getString("PASSPORTNUMBER")));
                    lineVec.add(18, StrUtils.fString((String) rs.getString("COUNTRYOFISSUE")));
                    lineVec.add(19, StrUtils.fString((String) rs.getString("PASSPORTEXPIRYDATE")));
                    lineVec.add(20, StrUtils.fString((String) rs.getString("ADDR1")));
                    lineVec.add(21, StrUtils.fString((String) rs.getString("ADDR2")));
                    lineVec.add(22, StrUtils.fString((String) rs.getString("ADDR3")));
                    lineVec.add(23, StrUtils.fString((String) rs.getString("ADDR4")));
                    lineVec.add(24, StrUtils.fString((String) rs.getString("COUNTRY")));
                    lineVec.add(25, StrUtils.fString((String) rs.getString("ZIP")));                    
                    lineVec.add(26, StrUtils.fString((String) rs.getString("EMIRATESID")));
                    lineVec.add(27, StrUtils.fString((String) rs.getString("EMIRATESIDEXPIRY")));
                    lineVec.add(28, StrUtils.fString((String) rs.getString("VISANUMBER")));
                    lineVec.add(29, StrUtils.fString((String) rs.getString("VISAEXPIRYDATE")));
                    lineVec.add(30, StrUtils.fString((String) rs.getString("LABOURCARDNUMBER")));
                    lineVec.add(31, StrUtils.fString((String) rs.getString("WORKPERMITNUMBER")));
                    lineVec.add(32, StrUtils.fString((String) rs.getString("CONTRACTSTARTDATE")));
                    lineVec.add(33, StrUtils.fString((String) rs.getString("CONTRACTENDDATE")));
                    lineVec.add(34, StrUtils.fString((String) rs.getString("BANKNAME")));
                    lineVec.add(35, StrUtils.fString((String) rs.getString("BRANCH")));
                    lineVec.add(36, StrUtils.fString((String) rs.getString("IBAN")));
                    lineVec.add(37, StrUtils.fString((String) rs.getString("BANKROUTINGCODE")));                    
                    lineVec.add(38, StrUtils.fString((String) rs.getString("BASICSALARY")));
                    lineVec.add(39, StrUtils.fString((String) rs.getString("HOUSERENTALLOWANCE")));
                    lineVec.add(40, StrUtils.fString((String) rs.getString("TRANSPORTALLOWANCE")));
                    lineVec.add(41, StrUtils.fString((String) rs.getString("COMMUNICATIONALLOWANCE")));
                    lineVec.add(42, StrUtils.fString((String) rs.getString("OTHERALLOWANCE")));
                    lineVec.add(43, StrUtils.fString((String) rs.getString("BONUS")));
                    lineVec.add(44, StrUtils.fString((String) rs.getString("COMMISSION")));                    
                    lineVec.add(45, StrUtils.fString((String) rs.getString("REMARKS")));
                    lineVec.add(46, StrUtils.fString((String) rs.getString("IsActive")));
                    lineVec.add(47, StrUtils.fString((String) rs.getString("STATE")));
                    lineVec.add(48, StrUtils.fString((String) rs.getString("CATLOGPATH")));
                    lineVec.add(49, StrUtils.fString((String) rs.getString("COUNTRY_CODE")));
                    lineVec.add(50, StrUtils.fString((String) rs.getString("GRATUITY")));
                    lineVec.add(51, StrUtils.fString((String) rs.getString("AIRTICKET")));
                    lineVec.add(52, StrUtils.fString((String) rs.getString("LEAVESALARY")));
                    listQty.add(lineVec);
                //  End code modified by Lakshmi for product brand on 11/9/12 
                }
        } catch (Exception e) {
                this.mLogger.exception(this.printLog, "", e);
        } finally {
                DbBean.closeConnection(con, ps);
        }
        return listQty;
}

public List<String> getAllAssignedAlternateEmployees(String plant, String empid) {
		List<String> resultSet = new ArrayList<String>();
		Connection conn = null;
		try {
			conn = DbBean.getConnection();

			String query = " SELECT ALTERNATE_EMPLOYEE_NO FROM [" + plant + "_"
					+ "ALTERNATE_EMPLOYEE_MAPPING] WHERE PLANT='" + plant
					+ "' AND EMPNO='" + empid + "' " ;
					//"ORDER BY CASE WHEN ALTERNATE_ITEM_NAME ='"+item+"' THEN 1 ELSE 2 END, ALTERNATE_ITEM_NAME";

			this.mLogger.query(this.printQuery, query);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				resultSet.add(rs.getString(1));
			}
			return resultSet;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			return resultSet;
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}
	}
public boolean isExisit(String sql) throws Exception {
	boolean flag = false;
	java.sql.Connection con = null;
	try {
		con = com.track.gates.DbBean.getConnection();
		this.mLogger.query(this.printQuery, sql.toString());
		flag = isExists(con, sql.toString());

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return flag;

}
public boolean removeAlternateEmployee(String plant, String empno, String cond) {
	boolean insertFlag = false;
	Connection conn = null;
	try {
		conn = DbBean.getConnection();

		String query = " DELETE FROM [" + plant + "_"
				+ "ALTERNATE_EMPLOYEE_MAPPING] WHERE PLANT='" + plant
				+ "' AND EMPNO='" + empno + "'   " + cond;

		this.mLogger.query(this.printQuery, query);
		insertFlag = DeleteRow(conn, query);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		return insertFlag;
	} finally {
		if (conn != null) {
			DbBean.closeConnection(conn);
		}
	}
	return insertFlag;
}

public boolean insertAlternateEmployee(String plant, String empno,
		String alternateEmpNo) {

	boolean insertFlag = false;
	empno = StrUtils.fString((String) empno);
	alternateEmpNo = StrUtils.fString((String) alternateEmpNo);
	Connection conn = null;
	try {
		conn = DbBean.getConnection();

		String query = "INSERT INTO ["
				+ plant
				+ "_"
				+ "ALTERNATE_EMPLOYEE_MAPPING] ( PLANT, EMPNO, ALTERNATE_EMPLOYEE_NO ) "
				+ " VALUES('" + plant + "', '" + empno + "', '"
				+ alternateEmpNo + "' )";

		this.mLogger.query(this.printQuery, query);
		insertFlag = insertData(conn, query);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		return insertFlag;
	} finally {
		if (conn != null) {
			DbBean.closeConnection(conn);
		}
	}
	return insertFlag;
}
public String getEmpno(String Plant, String alternateempno,String extCond) throws Exception {
	String Empno = "";

	Hashtable ht = new Hashtable();
	ht.put("PLANT", Plant);
	ht.put("ALTERNATE_EMPLOYEE_NO", alternateempno);
	
	String query = " isnull(empno,'') empno ";
	Map m = selectempRow(query, ht, extCond);
	Empno = (String) m.get("empno");
	if (Empno == "" || Empno == null)
		Empno = "";
	return Empno;
}	

public Map selectempRow(String query, Hashtable ht, String extCond)
		throws Exception {
	Map map = new HashMap();
	java.sql.Connection con = null;
	try {

		con = com.track.gates.DbBean.getConnection();
		StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
				+ "[" + ht.get("PLANT") + "_" + "ALTERNATE_EMPLOYEE_MAPPING" + "]");
		sql.append(" WHERE ");
		String conditon = formCondition(ht);
		sql.append(conditon);
		if (extCond.length() > 0)
			sql.append(" and " + extCond);
		this.mLogger.query(this.printQuery, sql.toString());

		map = getRowOfData(con, sql.toString());

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return map;
}
public ArrayList getEmployeeName(String empno, String plant,
		String extraCon) throws Exception {

	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();

		boolean flag = false;
		String sQry = "SELECT ISNULL(FNAME,'')FNAME,ISNULL(LNAME,'')LNAME FROM "
				+ "["
				+ plant
				+ "_"
				+ "EMP_MST] where EMPNO = '"
				+ empno
				+ "'";
		this.mLogger.query(this.printQuery, sQry);

		al = selectData(con, sQry);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}

	return al;
}

public ArrayList getEmployeeDetails(String selectList, Hashtable ht,
		String extCond) throws Exception {
	boolean flag = false;

	Connection con = null;
	ArrayList alResult = new ArrayList();
	StringBuffer sql = new StringBuffer(" SELECT " + selectList + " from "
			+ "[" + ht.get("PLANT") + "_EMP_MST]");

	try {
		con = DbBean.getConnection();

		String conditon = "";
		if (ht.size() > 0) {
			sql.append(" WHERE ");
			conditon = formCondition(ht);
			sql.append(conditon);
		}
		if (extCond.length() > 0) {
			sql.append(" ");
			sql.append(extCond);
		}
		this.mLogger.query(this.printQuery, sql.toString());
		alResult = selectData(con, sql.toString());
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		DbBean.closeConnection(con);
	}

	return alResult;

}

public ArrayList getPDAEmployeDetails(String plant,String empno) throws Exception {

	MLogger.log(1, this.getClass() + " getEmployeeDetails()");
	ArrayList al = new ArrayList();
	java.sql.Connection con = null;

	try {
		con = DbBean.getConnection();
		StringBuffer sql = new StringBuffer("  SELECT  ");
		sql.append("empno,");
		sql.append("isnull(fname,'') fname,");
		sql.append("isnull(lname,'') lname");
		sql.append(" ");
		sql.append(" FROM " + "[" + plant + "_"
				+ "EMP_MST] WHERE EMPNO like '" + empno + "%'  and ISACTIVE='Y'");
		sql.append(" ORDER BY empno");
		this.mLogger.query(this.printQuery, sql.toString());
		al = selectData(con, sql.toString());

	} catch (Exception e) {

		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return al;

}
public boolean updateEmployeeImage(String plant,String empID,String catalogPath) {
	
	Connection con = null;
	boolean insertedItemImage = false;
	try {
		con = DbBean.getConnection();
		String query = "update "+plant+"_EMP_MST set CATLOGPATH ='"+catalogPath+"' where EMPNO ='"+empID+"'";
		
		this.mLogger.query(this.printQuery, query);
		PreparedStatement ps = con.prepareStatement(query);
		int iCnt = ps.executeUpdate();
		
		if (iCnt > 0)
			insertedItemImage = true;
	} catch (NamingException | SQLException e) {
		e.printStackTrace();
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}	
	}
	
	return insertedItemImage;
}

public String getEmpcode(String aPlant, String id,String extCond) throws Exception {
	String empname = "";

	Hashtable ht = new Hashtable();
	ht.put("PLANT", aPlant);
	ht.put("ID", id);
	
	String query = " isnull(EMPNO,'') EMPNO ";
	Map m = selectRow(query, ht, extCond);
	empname = (String) m.get("EMPNO");
	if (empname == "" || empname == null)
		empname = "";
	return empname;
}

public String getEmpid(String aPlant, String EMPNO,String extCond) throws Exception {
	String empid = "";

	Hashtable ht = new Hashtable();
	ht.put("PLANT", aPlant);
	ht.put("EMPNO", EMPNO);
	
	String query = "ID ";
	Map m = selectRow(query, ht, extCond);
	empid = (String) m.get("ID");
	if (empid == "" || empid == null)
		empid = "";
	return empid;
}

public String getEmpnamebyid(String aPlant, String id,String extCond) throws Exception {
	String empname = "";

	Hashtable ht = new Hashtable();
	ht.put("PLANT", aPlant);
	ht.put("ID", id);
	
	String query = " isnull(fname,'') fname ";
	Map m = selectRow(query, ht, extCond);
	empname = (String) m.get("fname");
	if (empname == "" || empname == null)
		empname = "";
	return empname;
}	

public boolean insertIntoEmployeeuseidMst(Hashtable ht) throws Exception {
	boolean insertedInv = false;
	java.sql.Connection con = null;
	try {
		con = DbBean.getConnection();
		String FIELDS = "", VALUES = "";
		Enumeration enum1 = ht.keys();
		for (int i = 0; i < ht.size(); i++) {
			String key = StrUtils.fString((String) enum1.nextElement());
			String value = StrUtils.fString((String) ht.get(key));
			FIELDS += key + ",";
			VALUES += "'" + value + "',";
		}
		String query = "INSERT INTO [HREMPUSERINFO] ("
				+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
				+ VALUES.substring(0, VALUES.length() - 1) + ")";
		this.mLogger.query(this.printQuery, query);
		insertedInv = insertData(con, query);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return insertedInv;
}

public boolean insertIntouserMst(Hashtable ht) throws Exception {
	boolean insertedInv = false;
	java.sql.Connection con = null;
	try {
		con = DbBean.getConnection();
		String FIELDS = "", VALUES = "";
		Enumeration enum1 = ht.keys();
		for (int i = 0; i < ht.size(); i++) {
			String key = StrUtils.fString((String) enum1.nextElement());
			String value = StrUtils.fString((String) ht.get(key));
			FIELDS += key + ",";
			VALUES += "'" + value + "',";
		}
		String query = "INSERT INTO [USER_INFO] ("
				+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
				+ VALUES.substring(0, VALUES.length() - 1) + ")";
		this.mLogger.query(this.printQuery, query);
		insertedInv = insertData(con, query);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return insertedInv;
}


public boolean IsUserName(String username) throws Exception {
	Connection connection = null;
	PreparedStatement ps = null;
	boolean status = false;
    String query = "";
	try {	    
		connection = DbBean.getConnection();
		query = "SELECT * FROM [HREMPUSERINFO] WHERE EMPUSERID='"+username+"'";

		if(connection != null){
			   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			   ResultSet rst = ps.executeQuery();
			   while (rst.next()) {
				   status = true;
                }   
		this.mLogger.query(this.printQuery, query);
		
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		
		throw e;
	} finally {
		if (connection != null) {
			DbBean.closeConnection(connection);
		}
	}
	return status;
}

public boolean IsChkEmpUserName(String username, String Plant, String EMPNO) throws Exception {
	Connection connection = null;
	PreparedStatement ps = null;
	boolean status = false;
	String query = "";
	try {	    
		connection = DbBean.getConnection();
		query = "SELECT * FROM [HREMPUSERINFO] H WHERE H.PLANT='"+Plant+"' AND H.EMPUSERID='"+username+"' AND EMPNOID IN (SELECT E.ID FROM "+Plant+"_EMP_MST E WHERE E.EMPNO='"+EMPNO+"') ";
		
		if(connection != null){
			ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet rst = ps.executeQuery();
			while (rst.next()) {
				status = true;
			}   
			this.mLogger.query(this.printQuery, query);
			
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		
		throw e;
	} finally {
		if (connection != null) {
			DbBean.closeConnection(connection);
		}
	}
	return status;
}

public String getidbyusername(String name,String extCond) throws Exception {
	Connection con = null;
	String uid = "";
	try {
		Hashtable ht = new Hashtable();
		ht.put("EMPUSERID", name);
		
		con = com.track.gates.DbBean.getConnection();
		StringBuffer sql = new StringBuffer(" SELECT ID from [HREMPUSERINFO]");
		sql.append(" WHERE ");
		String conditon = formCondition(ht);
		sql.append(conditon);
		if (extCond.length() > 0)
			sql.append(" and " + extCond);
		this.mLogger.query(this.printQuery, sql.toString());
	
		Map m = getRowOfData(con, sql.toString());
		uid = (String) m.get("ID");
		if (uid == "" || uid == null)
			uid = "";
	}catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}	
	}
	return uid;
}	

public boolean IsUserNameEdit(String username,String uid) throws Exception {
	Connection connection = null;
	PreparedStatement ps = null;
	boolean status = false;
    String query = "";
	try {	    
		connection = DbBean.getConnection();
		query = "SELECT * FROM [HREMPUSERINFO] WHERE EMPUSERID='"+username+"' AND ID !='"+uid+"'";

		if(connection != null){
			   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			   ResultSet rst = ps.executeQuery();
			   while (rst.next()) {
				   status = true;
                }   
		this.mLogger.query(this.printQuery, query);
		
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		
		throw e;
	} finally {
		if (connection != null) {
			DbBean.closeConnection(connection);
		}
	}
	return status;
}

public boolean updateusername(String ID,String uname) {
	
	Connection con = null;
	boolean insertedItemImage = false;
	try {
		con = DbBean.getConnection();
		String query = "update HREMPUSERINFO set EMPUSERID ='"+uname+"' where ID ='"+ID+"'";
		
		this.mLogger.query(this.printQuery, query);
		PreparedStatement ps = con.prepareStatement(query);
		int iCnt = ps.executeUpdate();
		
		if (iCnt > 0)
			insertedItemImage = true;
	} catch (NamingException | SQLException e) {
		e.printStackTrace();
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}	
	}
	
	return insertedItemImage;
}
public boolean updatesalesman(String ID,String uname) {
	
	Connection con = null;
	boolean insertedItemImage = false;
	try {
		con = DbBean.getConnection();
		String query = "update HREMPUSERINFO set ISSALESMAN ='"+uname+"' where ID ='"+ID+"'";
		
		this.mLogger.query(this.printQuery, query);
		PreparedStatement ps = con.prepareStatement(query);
		int iCnt = ps.executeUpdate();
		
		if (iCnt > 0)
			insertedItemImage = true;
	} catch (NamingException | SQLException e) {
		e.printStackTrace();
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}	
	}
	
	return insertedItemImage;
}
public boolean updatecashier(String ID,String uname) {
	
	Connection con = null;
	boolean insertedItemImage = false;
	try {
		con = DbBean.getConnection();
		String query = "update HREMPUSERINFO set ISCASHIER ='"+uname+"' where ID ='"+ID+"'";
		
		this.mLogger.query(this.printQuery, query);
		PreparedStatement ps = con.prepareStatement(query);
		int iCnt = ps.executeUpdate();
		
		if (iCnt > 0)
			insertedItemImage = true;
	} catch (NamingException | SQLException e) {
		e.printStackTrace();
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}	
	}
	
	return insertedItemImage;
}

public boolean updatepassword(String ID,String password) {
	
	Connection con = null;
	boolean insertedItemImage = false;
	try {
		con = DbBean.getConnection();
		String query = "update HREMPUSERINFO set PASSWORD ='"+password+"' where ID ='"+ID+"'";
		
		this.mLogger.query(this.printQuery, query);
		PreparedStatement ps = con.prepareStatement(query);
		int iCnt = ps.executeUpdate();
		
		if (iCnt > 0)
			insertedItemImage = true;
	} catch (NamingException | SQLException e) {
		e.printStackTrace();
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}	
	}
	
	return insertedItemImage;
}


public HrEmpUserInfo employeelogin(String userid, String password)throws Exception {
	Connection connection = null;
	PreparedStatement ps = null;
    String query = "";
    HrEmpUserInfo HrEmpUserInfo=new HrEmpUserInfo();
	try {	    
		connection = DbBean.getConnection();
		query = "SELECT * FROM [HREMPUSERINFO] WHERE EMPUSERID ='"+userid+"' AND PASSWORD ='"+password+"'";
		if(connection != null){
			   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			   ResultSet rst = ps.executeQuery();
			   while (rst.next()) {
                    ResultSetToObjectMap.loadResultSetIntoObject(rst, HrEmpUserInfo);
                }   
		this.mLogger.query(this.printQuery, query);
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		
		throw e;
	} finally {
		if (connection != null) {
			DbBean.closeConnection(connection);
		}
	}
	return HrEmpUserInfo;
}

public ArrayList getEmployeeListbyid(String id, String plant) throws Exception {

	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();

		boolean flag = false;
		String sQry = "SELECT ID,ISNULL(CATLOGPATH,'')CATLOGPATH,EMPNO,ISNULL(FNAME,'')FNAME,ISNULL(LNAME,'')LNAME,ISNULL(REPORTING_INCHARGE,'0') REPORTING_INCHARGE,ISNULL(EMPLOYEETYPEID,'0')EMPLOYEETYPEID,ISNULL(NUMBEROFMANDAYS,'0.0')NUMBEROFMANDAYS,ISNULL(GENDER,'')GENDER,ISNULL(DATEOFBIRTH,'')DOB,ISNULL(DEPTARTMENT,'')DEPT,ISNULL(DESGINATION,'')DESGINATION,ISNULL(DATEOFJOINING,'') DATEOFJOINING,ISNULL(DATEOFLEAVING,'') DATEOFLEAVING,ISNULL(NATIONALITY,'')NATIONALITY,ISNULL(TELNO,'')TELNO,ISNULL(HPNO,'')HPNO,ISNULL(EMAIL,'')EMAIL,ISNULL(SKYPEID,'')SKYPEID,ISNULL(FACEBOOKID,'')FACEBOOKID,ISNULL(TWITTERID,'')TWITTERID,ISNULL(LINKEDINID,'')LINKEDINID,ISNULL(PASSPORTNUMBER,'')PASSPORTNUMBER,ISNULL(COUNTRYOFISSUE,'')COUNTRYOFISSUE,ISNULL(PASSPORTEXPIRYDATE,'')PASSPORTEXPIRYDATE,ISNULL(UNITNO,'')ADDR1,ISNULL(BUILDING,'')ADDR2,ISNULL(STREET,'')ADDR3,ISNULL(CITY,'')ADDR4,ISNULL(COUNTRY,'')COUNTRY,ISNULL(ZIP,'')ZIP,ISNULL(EMIRATESID,'')EMIRATESID,ISNULL(EMIRATESIDEXPIRY,'')EMIRATESIDEXPIRY,ISNULL(VISANUMBER,'')VISANUMBER,ISNULL(VISAEXPIRYDATE,'')VISAEXPIRYDATE,ISNULL(LABOURCARDNUMBER,'')LABOURCARDNUMBER,ISNULL(WORKPERMITNUMBER,'')WORKPERMITNUMBER,ISNULL(CONTRACTSTARTDATE,'')CONTRACTSTARTDATE,ISNULL(CONTRACTENDDATE,'')CONTRACTENDDATE,ISNULL(BANKNAME,'')BANKNAME,ISNULL((select top 1 BRANCH from "+plant+"_BANKMST where Name=BANKNAME ),'')BRANCH,ISNULL(IBAN,'')IBAN,ISNULL(BANKROUTINGCODE,'')BANKROUTINGCODE,ISNULL(BASICSALARY,0)BASICSALARY,ISNULL(HOUSERENTALLOWANCE,0)HOUSERENTALLOWANCE,ISNULL(TRANSPORTALLOWANCE,0)TRANSPORTALLOWANCE,ISNULL(COMMUNICATIONALLOWANCE,0)COMMUNICATIONALLOWANCE,ISNULL(OTHERALLOWANCE,0)OTHERALLOWANCE,ISNULL(BONUS,0)BONUS,ISNULL(COMMISSION,0)COMMISSION,ISNULL(REMARKS,'')REMARKS,ISNULL(IsActive,'')IsActive,ISNULL(STATE,'') STATE   FROM "
				+ "["
				+ plant
				+ "_"
				+ "EMP_MST] " 
				//+ "where FNAME like ' "+CustName +" %'   " 
				+ "WHERE ID='"+id+"'"
				+ " ORDER BY FNAME ";
		/*String sQry = "SELECT EMPNO,ISNULL(FNAME,'')FNAME,ISNULL(LNAME,'')LNAME,ISNULL(GENDER,'')GENDER,ISNULL(DOB,'')DOB,ISNULL(DEPT,'')DEPT,ISNULL(DESGINATION,'')DESGINATION,ISNULL(NATIONALITY,'')NATIONALITY,ISNULL(A.TELNO,'')TELNO,ISNULL(HPNO,'')HPNO,ISNULL(A.FAX,'')FAX,ISNULL(EMAIL,'')EMAIL,ISNULL(A.ADDR1,'')ADDR1,ISNULL(A.ADDR2,'')ADDR2,ISNULL(A.ADDR3,'')ADDR3,ISNULL(A.ADDR4,'')ADDR4,ISNULL(A.COUNTRY,'')COUNTRY,ISNULL(A.ZIP,'')ZIP,ISNULL(A.REMARKS,'')REMARKS,ISNULL(A.IsActive,'')IsActive," +
				"ISNULL(B.TELNO,'')DTELNO,ISNULL(B.FAX,'')DFAX,ISNULL(B.ADD1,'')DADDR1,ISNULL(B.ADD2,'')DADDR2,ISNULL(B.ADD3,'')DADDR3,ISNULL(B.ADD4,'')DADDR4,ISNULL(B.COUNTRY,'')DCOUNTRY,ISNULL(B.ZIP,'')DZIP,ISNULL(B.REMARKS,'')DREMARKS FROM "
				+ "["
				+ plant
				+ "_"
				+ "EMP_MST] A LEFT JOIN "+ "["+ plant+ "_"+ "DEPTMST] B  ON A.DEPT = B.DEPTID " 
				//+ "where FNAME like ' "+CustName +" %'   " 
				+ "WHERE (FNAME LIKE '" + CustName + "%' or LNAME LIKE '" + CustName + "%' or EMPNO LIKE '%" + CustName + "%')"
				+ " ORDER BY FNAME ";*/
		this.mLogger.query(this.printQuery, sQry);

		al = selectData(con, sQry);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}

	return al;
}


public boolean isEmptypeExists(String id,String plant) throws Exception {

	boolean flag = false;
	java.sql.Connection con = null;

	try {
		con = com.track.gates.DbBean.getConnection();

		// query
		StringBuffer sql = new StringBuffer("SELECT * FROM [" + plant+ "_EMP_MST] WHERE EMPLOYEETYPEID ='"+id+"'");
		this.mLogger.query(this.printQuery, sql.toString());
		flag = isExists(con, sql.toString());

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return flag;
}

public ArrayList getAllEmployeeDetails(String plant) throws Exception {

	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();

		boolean flag = false;
		String sQry = "SELECT ID,EMPNO,ISNULL(EMPUSERID,'')EMPUSERID,ISNULL(PASSWORD,'')PASSWORD,ISNULL(REPORTING_INCHARGE,'')REPORTING_INCHARGE,ISNULL(FNAME,'')FNAME,ISNULL(LNAME,'')LNAME,ISNULL(EMPLOYEETYPEID,'0')EMPLOYEETYPEID,ISNULL(NUMBEROFMANDAYS,'0.0')NUMBEROFMANDAYS,ISNULL(GENDER,'')GENDER,ISNULL(DATEOFBIRTH,'')DOB,ISNULL(DEPTARTMENT,'')DEPT,ISNULL(DESGINATION,'')DESGINATION,ISNULL(DATEOFJOINING,'') DATEOFJOINING,ISNULL(DATEOFLEAVING,'') DATEOFLEAVING,ISNULL(NATIONALITY,'')NATIONALITY,ISNULL(TELNO,'')TELNO,ISNULL(HPNO,'')HPNO,ISNULL(EMAIL,'')EMAIL,ISNULL(SKYPEID,'')SKYPEID,ISNULL(FACEBOOKID,'')FACEBOOKID,ISNULL(TWITTERID,'')TWITTERID,ISNULL(LINKEDINID,'')LINKEDINID,ISNULL(PASSPORTNUMBER,'')PASSPORTNUMBER,ISNULL(COUNTRYOFISSUE,'')COUNTRYOFISSUE,ISNULL(PASSPORTEXPIRYDATE,'')PASSPORTEXPIRYDATE,ISNULL(UNITNO,'')ADDR1,ISNULL(BUILDING,'')ADDR2,ISNULL(STREET,'')ADDR3,ISNULL(CITY,'')ADDR4,ISNULL(COUNTRY,'')COUNTRY,ISNULL(ZIP,'')ZIP,ISNULL(EMIRATESID,'')EMIRATESID,ISNULL(EMIRATESIDEXPIRY,'')EMIRATESIDEXPIRY,ISNULL(VISANUMBER,'')VISANUMBER,ISNULL(VISAEXPIRYDATE,'')VISAEXPIRYDATE,ISNULL(LABOURCARDNUMBER,'')LABOURCARDNUMBER,ISNULL(WORKPERMITNUMBER,'')WORKPERMITNUMBER,ISNULL(CONTRACTSTARTDATE,'')CONTRACTSTARTDATE,ISNULL(CONTRACTENDDATE,'')CONTRACTENDDATE,ISNULL(BANKNAME,'')BANKNAME,ISNULL((select top 1 BRANCH from "+plant+"_BANKMST where Name=BANKNAME ),'')BRANCH,ISNULL(IBAN,'')IBAN,ISNULL(BANKROUTINGCODE,'')BANKROUTINGCODE,ISNULL(BASICSALARY,0)BASICSALARY,ISNULL(HOUSERENTALLOWANCE,0)HOUSERENTALLOWANCE,ISNULL(TRANSPORTALLOWANCE,0)TRANSPORTALLOWANCE,ISNULL(COMMUNICATIONALLOWANCE,0)COMMUNICATIONALLOWANCE,ISNULL(OTHERALLOWANCE,0)OTHERALLOWANCE,ISNULL(BONUS,0)BONUS,ISNULL(COMMISSION,0)COMMISSION,ISNULL(REMARKS,'')REMARKS,ISNULL(IsActive,'')IsActive,ISNULL(STATE,'') STATE,ISNULL(CATLOGPATH,'') CATLOGPATH,ISNULL((SELECT COUNTRY_CODE FROM "+ plant+"_countrymaster WHERE COUNTRYNAME=COUNTRY),'') COUNTRY_CODE,ISNULL(GRATUITY,0)GRATUITY,ISNULL(AIRTICKET,0)AIRTICKET,ISNULL(LEAVESALARY,0)LEAVESALARY   FROM "
				+ "["
				+ plant
				+ "_"
				+ "EMP_MST]";
		this.mLogger.query(this.printQuery, sQry);

		al = selectData(con, sQry);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}

	return al;
}

public String getActiveEmployee(String aPlant) throws Exception {
	String empcount = "0";

	Hashtable ht = new Hashtable();
	ht.put("PLANT", aPlant);
	ht.put("IsActive", "Y");
	
	String query = " COUNT(*) AS EMPCOUNT ";
	Map m = selectRow(query, ht, "");
	empcount = (String) m.get("EMPCOUNT");
	if (empcount == "" || empcount == null)
		empcount = "0";
	return empcount;
}

public int Employeecount(String plant)
		throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	int Employeecount = 0;
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_"
				+ "EMP_MST" + "]" + " WHERE " + IConstants.PLANT
				+ " = '" + plant.toUpperCase() + "'";
		this.mLogger.query(this.printQuery, sQry);
		ps = con.prepareStatement(sQry);
		rs = ps.executeQuery();
		while (rs.next()) {
			Employeecount = rs.getInt(1);
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	} finally {
		DbBean.closeConnection(con, ps);
	}
	return Employeecount;
}


public boolean isExistEmployee(String employee, String plant)
		throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	boolean isExists = false;
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_" + "EMP_MST"
				+ "]" + " WHERE " + IConstants.FNAME + " = '"
				+ employee.toUpperCase() + "'";
		this.mLogger.query(this.printQuery, sQry);
		ps = con.prepareStatement(sQry);
		rs = ps.executeQuery();
		while (rs.next()) {
			if (rs.getInt(1) > 0)
				isExists = true;
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	} finally {
		DbBean.closeConnection(con, ps);
	}
	return isExists;
}

public boolean isExistIncoterms(String incoterms, String plant)
		throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	boolean isExists = false;
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_" + "INCOTERMSMST"
				+ "]" + " WHERE " + IConstants.INCOTERMS + " = '"
				+ incoterms.toUpperCase() + "'";
		this.mLogger.query(this.printQuery, sQry);
		ps = con.prepareStatement(sQry);
		rs = ps.executeQuery();
		while (rs.next()) {
			if (rs.getInt(1) > 0)
				isExists = true;
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	} finally {
		DbBean.closeConnection(con, ps);
	}
	return isExists;
}

public String getEmpnameByempid(String aPlant, String empid,String extCond) throws Exception {
	String empname = "";

	Hashtable ht = new Hashtable();
	ht.put("PLANT", aPlant);
	ht.put("EMPUSERID", empid);
	
	String query = " isnull(fname,'') fname ";
	Map m = selectRow(query, ht, extCond);
	empname = (String) m.get("fname");
	if (empname == "" || empname == null)
		empname = "";
	return empname;
}	

}
