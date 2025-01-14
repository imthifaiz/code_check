package com.track.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.db.object.HrEmpDepartmentMst;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;
import com.track.util.StrUtils;

public class MasterDAO extends BaseDAO{
	
	private MLogger mLogger = new MLogger();
	private boolean printQuery = MLoggerConstant.MasterDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.MasterDAO_PRINTPLANTMASTERLOG;

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

	public MasterDAO() {

	}
	
	public boolean InsertFooter(Hashtable ht) throws Exception {
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
					+ "FOOTERMST" + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, query);
			System.out.println("InsertFooter"+query);
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
	//Resvi Add For Department Display -08/10/21
	public ArrayList getDepDispDetails(String departmentdisplay, String plant, String cond)
			throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "select distinct DEPT_DISPLAY_ID,DEPT_DISPLAY_DESC,ISACTIVE from "
					+ "["
					+ plant
					+ "_"
					+ "DEPT_DISPLAY_MST] where DEPT_DISPLAY_ID like '"
					+ departmentdisplay
					+ "%' " + cond
					+ " ORDER BY DEPT_DISPLAY_ID ";
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
	
	//Ends

	public boolean deleteFooter(java.util.Hashtable ht) throws Exception {
		MLogger.log(1, this.getClass() + " deleteFooter()");
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
          	StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "FOOTERMST"
					+ "]");
			sql.append(" WHERE " + formCondition(ht));
			this.mLogger.query(this.printQuery, sql.toString());
			System.out.println("DeleteFooter"+sql.toString());
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
	
	public ArrayList getFooterDetails(String plant,String extraCon) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			String sQry = "select ID,ISNULL(FOOTER,'') FOOTER from ["+plant+"_FOOTERMST]  " +
					"where FOOTER <> 'NOFOOTERDETAILS' order by ID";
		    this.mLogger.query(this.printQuery, sQry);
		    System.out.println("MasterDAO"+ sQry);
			al = selectData(con, sQry);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			System.out.println("MasterDAO getFooterDetails()"+e.getMessage());
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return al;
	}
	
	
	
	public boolean InsertRemarks(Hashtable ht) throws Exception {
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
					+ "REMARKSMST" + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, query);
			System.out.println("InsertRemarks"+query);
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

	public boolean deleteRemarks(java.util.Hashtable ht) throws Exception {
		MLogger.log(1, this.getClass() + " deleteRemarks()");
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
          	StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "REMARKSMST"
					+ "]");
			sql.append(" WHERE " + formCondition(ht));
			this.mLogger.query(this.printQuery, sql.toString());
			System.out.println("DeleteRemarks"+sql.toString());
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
	
	
//	Resvi Starts TransportMode
	public boolean deleteTransportMode(java.util.Hashtable ht) throws Exception {
		MLogger.log(1, this.getClass() + " deleteTransportMode()");
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
          	StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "TRANSPORT_MODE_MST"
					+ "]");
			sql.append(" WHERE " + formCondition(ht));
			this.mLogger.query(this.printQuery, sql.toString());
			System.out.println("DeleteTransportMode"+sql.toString());
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
	
	public boolean isExisitRemarks(Hashtable ht, String extCond) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "["+ht.get("PLANT")+"_REMARKSMST]");
			sql.append(" WHERE  " + formCondition(ht));

			if (extCond.length() > 0)
				sql.append(" and " + extCond);

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
	
//	Resvi Starts TransportMode
	
	public ArrayList getTransportModeDetails(String plant,String extraCon) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			StringBuffer sql = new StringBuffer("select ID,ISNULL(TRANSPORT_MODE,'') TRANSPORT_MODE from ["+plant+"_TRANSPORT_MODE_MST] ");
			sql.append(" where TRANSPORT_MODE <>'NOTRANSPORTMODEDETAILS '");
			
			if (extraCon.length() > 0)
				sql.append(" and " + extraCon + " order by ID");
			else
			  sql.append(" order by ID");
		    this.mLogger.query(this.printQuery, sql.toString());
		    System.out.println("MasterDAO getTransportModeDetails():"+ sql.toString());
			al = selectData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			System.out.println("MasterDAO getTransportModeDetails():"+e.getMessage());
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return al;
	}
//ENDS
	
	
	public ArrayList getRemarksDetails(String plant,String extraCon) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			StringBuffer sql = new StringBuffer("select ID,ISNULL(REMARKS,'') REMARKS from ["+plant+"_REMARKSMST] ");
			sql.append(" where REMARKS <>'NOREMARKSDETAILS '");
			
			if (extraCon.length() > 0)
				sql.append(" and " + extraCon + " order by ID");
			else
			  sql.append(" order by ID");
		    this.mLogger.query(this.printQuery, sql.toString());
		    System.out.println("MasterDAO getRemarksDetails():"+ sql.toString());
			al = selectData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			System.out.println("MasterDAO getRemarksDetails():"+e.getMessage());
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return al;
	}
	


	public boolean InsertINCOTERMS(Hashtable ht) throws Exception {
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
					+ "INCOTERMSMST" + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, query);
			System.out.println("InsertINCOTERMS"+query);
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
	

	public boolean isExisitINCOTERMMST(Hashtable ht, String extCond) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "["+ht.get("PLANT")+"_INCOTERMSMST]");
			sql.append(" WHERE  " + formCondition(ht));

			if (extCond.length() > 0)
				sql.append(" and " + extCond);

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

	public boolean deleteINCOTERMS(java.util.Hashtable ht) throws Exception {
		MLogger.log(1, this.getClass() + " deleteINCOTERMS()");
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
          	StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "INCOTERMSMST"
					+ "]");
			sql.append(" WHERE " + formCondition(ht));
			this.mLogger.query(this.printQuery, sql.toString());
			System.out.println("DeleteINCOTERMS"+sql.toString());
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
	
	public ArrayList getINCOTERMSDetails(String plant,String extraCon) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			StringBuffer sql = new StringBuffer("select ID,ISNULL(INCOTERMS,'') INCOTERMS from ["+plant+"_INCOTERMSMST]" );
			sql.append("where INCOTERMS <> 'NOINCOTERMSDETAILS' ");
			if (extraCon.length() > 0)
				sql.append(" and " + extraCon + " order by ID");
			else
			  sql.append(" order by ID");
		    this.mLogger.query(this.printQuery, sql.toString());
		    System.out.println("MasterDAO getINCOTERMSDetails():"+ sql.toString());
			al = selectData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			System.out.println("MasterDAO getINCOTERMSDetails():"+e.getMessage());
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return al;
	}
	
	public boolean InsertShippingDetails(Hashtable ht) throws Exception {
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
					+ "SHIPPINGMST" + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, query);
			System.out.println("InsertShippingDetails"+query);
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

	
	 public boolean isExistsShippingDetails(String customername, String plant)
             throws Exception {
     PreparedStatement ps = null;
     ResultSet rs = null;
     boolean isExists = false;
     Connection con = null;
     try {
             con = DbBean.getConnection();
             String sQry = "SELECT COUNT(*) FROM " + "[" + plant + "_" + "CUSTMST"
                             + "]" + " WHERE CNAME = '"
                             + customername.toUpperCase() + "'";
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
	 
	public boolean deleteShippingDetails(java.util.Hashtable ht) throws Exception {
		MLogger.log(1, this.getClass() + " deleteShippingDetails()");
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
          	StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "SHIPPINGMST"
					+ "]");
			sql.append(" WHERE " + formCondition(ht));
			this.mLogger.query(this.printQuery, sql.toString());
			System.out.println("DeleteShippingDetailsS"+sql.toString());
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
	
	/*public ArrayList getShippingDetails(String plant,String extraCon) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			StringBuffer sql = new StringBuffer("select ID,ISNULL(CUSTOMERNAME,'') CUSTOMERNAME,ISNULL(CONTACTNAME,'') CONTACTNAME," );
			sql.append("ISNULL(TELEPHONE,'') TELEPHONE,ISNULL(HANDPHONE,'') HANDPHONE," ) ;
			sql.append("ISNULL(FAX,'') FAX,ISNULL(EMAIL,'') EMAIL," );
			sql.append("ISNULL(UNITNO,'') UNITNO,ISNULL(BUILDING,'') BUILDING,"); 
			sql.append("ISNULL(STREET,'') STREET,ISNULL(CITY,'') CITY,");
			sql.append("ISNULL(STATE,'') STATE,ISNULL(COUNTRY,'') COUNTRY,");
			sql.append("ISNULL(POSTALCODE,'') POSTALCODE");
			sql.append(" from ["+plant+"_SHIPPINGMST]  where customername<>'NOSHIPPINGDETAILS' ");
			if (extraCon.length() > 0)
				sql.append(" and " + extraCon + " order by customername");
			else
				sql.append(" order by customername");
			this.mLogger.query(this.printQuery, sql.toString());
		    System.out.println("MasterDAO getShippingDetails():"+ sql.toString());
			al = selectData(con,sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			System.out.println("MasterDAO getShippingDetails):"+e.getMessage());
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return al;
	}*/
	
	public ArrayList getShippingDetails(String plant,String extraCon) throws Exception {
	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();
		boolean flag = false;
		StringBuffer sql = new StringBuffer("select CUSTNO,ISNULL(CNAME,'') CUSTOMERNAME" );
		//,ISNULL(CONTACTNAME,'') CONTACTNAME," );
		//sql.append("ISNULL(TELEPHONE,'') TELEPHONE,ISNULL(HANDPHONE,'') HANDPHONE," ) ;
		//sql.append("ISNULL(FAX,'') FAX,ISNULL(EMAIL,'') EMAIL," );
		//sql.append("ISNULL(UNITNO,'') UNITNO,ISNULL(BUILDING,'') BUILDING,"); 
		//sql.append("ISNULL(STREET,'') STREET,ISNULL(CITY,'') CITY,");
		//sql.append("ISNULL(STATE,'') STATE,ISNULL(COUNTRY,'') COUNTRY,");
		//sql.append("ISNULL(POSTALCODE,'') POSTALCODE");
		sql.append(" from ["+plant+"_CUSTMST]  where custno<>'' ");
		if (extraCon.length() > 0)
			sql.append(" and " + extraCon + " order by cname");
		else
			sql.append(" order by cname");
		this.mLogger.query(this.printQuery, sql.toString());
	    System.out.println("MasterDAO getShippingDetails():"+ sql.toString());
		al = selectData(con,sql.toString());

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		System.out.println("MasterDAO getShippingDetails):"+e.getMessage());
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}

	return al;
}


	 public ArrayList getEstimateShippingDetails(String orderno, String shippingid,String plant)
	            throws Exception {
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    ArrayList arrCust = new ArrayList();
	    Connection con = null;
	    try {
	            con = DbBean.getConnection();
	            String sQry = "SELECT " +
	          		      "CUSTNO AS ID," +
	          		      "ISNULL(CNAME,'') CUSTOMERNAME," +
	          		      "ISNULL(NAME,'') CONTACTNAME," +
	          		      "ISNULL(TELNO,'') TELEPHONE," +
	          		      "ISNULL(HPNO,'') HANDPHONE," +
	          		      "ISNULL(FAX,'')FAX," +
	          		      "ISNULL(EMAIL,'')EMAIL," +
	          		      "ISNULL(ADDR1,'') UNTINO," +
	          		      "ISNULL(ADDR2,'') BUILDING," +
	          		      "ISNULL(ADDR3,'') STREET," +
	          		      "ISNULL(ADDR4,'') CITY," +
	          		      "ISNULL(STATE,'') STATE," +
	          		      "ISNULL(COUNTRY,'') COUNTRY," +
	          		      "ISNULL(ZIP,'') POSTALCODE FROM "
	                            + "[" + plant+ "_" + "CUSTMST"+ "] A , "+"[" + plant+ "_" + "ESTHDR"+ "] B"
	                            + " WHERE A.PLANT=B.PLANT and  A.CUSTNO=B.SHIPPINGID  and  B.ESTNO ='"+orderno+"' and A.CUSTNO ='"+shippingid+"'" ;
	          
	            ps = con.prepareStatement(sQry);
	            System.out.println(sQry);
	            rs = ps.executeQuery();
	            while (rs.next()) {
	                    arrCust.add(0, StrUtils.fString(rs.getString(1))); // id
	                    arrCust.add(1, StrUtils.fString(rs.getString(2))); // customername
	                    arrCust.add(2, StrUtils.fString(rs.getString(3))); // contactname
	                    arrCust.add(3, StrUtils.fString(rs.getString(4))); // telephone
	                    arrCust.add(4, StrUtils.fString(rs.getString(5))); // handphone
	                    arrCust.add(5, StrUtils.fString(rs.getString(6))); // fax
	                    arrCust.add(6, StrUtils.fString(rs.getString(7))); // email
	                    arrCust.add(7, StrUtils.fString(rs.getString(8))); // unitno
	                    arrCust.add(8, StrUtils.fString(rs.getString(9)));// building
	                    arrCust.add(9, StrUtils.fString(rs.getString(10)));// street
	                    arrCust.add(10, StrUtils.fString(rs.getString(11)));// city
	                    arrCust.add(11, StrUtils.fString(rs.getString(12)));// state
	                    arrCust.add(12, StrUtils.fString(rs.getString(13)));//country
	                    arrCust.add(13, StrUtils.fString(rs.getString(14)));// postalcode
	                  }
	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	    } finally {
	            DbBean.closeConnection(con, ps);
	    }
	    return arrCust;
	}
	 
	 public ArrayList getRentalShippingDetails(String orderno, String shippingid,String plant)
	            throws Exception {
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    ArrayList arrCust = new ArrayList();
	    Connection con = null;
	    
	    try {
	            con = DbBean.getConnection();
	           // shippingid ="SELECT SHIPPINGCUSTOMER FROM "+ plant +"_LOANHDR WHERE ORDNO='"+orderno+"'";
	            String sQry = "SELECT " +
       		      "CUSTNO AS ID," +
       		      "ISNULL(CNAME,'') CUSTOMERNAME," +
       		      "ISNULL(NAME,'') CONTACTNAME," +
       		      "ISNULL(TELNO,'') TELEPHONE," +
       		      "ISNULL(HPNO,'') HANDPHONE," +
       		      "ISNULL(FAX,'')FAX," +
       		      "ISNULL(EMAIL,'')EMAIL," +
       		      "ISNULL(ADDR1,'') UNTINO," +
       		      "ISNULL(ADDR2,'') BUILDING," +
       		      "ISNULL(ADDR3,'') STREET," +
       		      "ISNULL(ADDR4,'') CITY," +
       		      "ISNULL(STATE,'') STATE," +
       		      "ISNULL(COUNTRY,'') COUNTRY," +
       		      "ISNULL(ZIP,'') POSTALCODE FROM "
	                            + "[" + plant+ "_" + "CUSTMST"+ "] A , "+"[" + plant+ "_" + "LOANHDR"+ "] B"
	                            + " WHERE A.PLANT=B.PLANT and  A.CNAME=B.SHIPPINGCUSTOMER  and  B.ORDNO ='"+orderno+"'" ;
	                            //+ " WHERE A.PLANT=B.PLANT and  A.CNAME=B.SHIPPINGCUSTOMER  and  B.ORDNO ='"+orderno+"' and A.CNAME ='"+shippingid+"'" ;
	          
	            ps = con.prepareStatement(sQry);
	            System.out.println(sQry);
	            rs = ps.executeQuery();
	            while (rs.next()) {
	                    arrCust.add(0, StrUtils.fString(rs.getString(1))); // id
	                    arrCust.add(1, StrUtils.fString(rs.getString(2))); // customername
	                    arrCust.add(2, StrUtils.fString(rs.getString(3))); // contactname
	                    arrCust.add(3, StrUtils.fString(rs.getString(4))); // telephone
	                    arrCust.add(4, StrUtils.fString(rs.getString(5))); // handphone
	                    arrCust.add(5, StrUtils.fString(rs.getString(6))); // fax
	                    arrCust.add(6, StrUtils.fString(rs.getString(7))); // email
	                    arrCust.add(7, StrUtils.fString(rs.getString(8))); // unitno
	                    arrCust.add(8, StrUtils.fString(rs.getString(9)));// building
	                    arrCust.add(9, StrUtils.fString(rs.getString(10)));// street
	                    arrCust.add(10, StrUtils.fString(rs.getString(11)));// city
	                    arrCust.add(11, StrUtils.fString(rs.getString(12)));// state
	                    arrCust.add(12, StrUtils.fString(rs.getString(13)));//country
	                    arrCust.add(13, StrUtils.fString(rs.getString(14)));// postalcode
	                  }
	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	    } finally {
	            DbBean.closeConnection(con, ps);
	    }
	    return arrCust;
	}
	 
	 public ArrayList getOutboundShippingDetails(String orderno, String shippingid,String plant)
	            throws Exception {
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    ArrayList arrCust = new ArrayList();
	    Connection con = null;
	    try {
	            con = DbBean.getConnection();
	            String sQry = "SELECT " +
          		      "CUSTNO AS ID," +
          		      "ISNULL(CNAME,'') CUSTOMERNAME," +
          		    "ISNULL(B.SHIPCONTACTNAME,A.NAME) SHIPCONTACTNAME," +
      		      "ISNULL(B.SHIPWORKPHONE,A.TELNO) SHIPWORKPHONE," +
      		      "ISNULL(B.SHIPHPNO,A.HPNO) SHIPHPNO," +
      		      "ISNULL(B.SHIPDESGINATION,A.DESGINATION) SHIPDESGINATION," +
      		      "ISNULL(B.SHIPEMAIL,A.EMAIL) SHIPEMAIL," +
      		      "ISNULL(B.SHIPADDR1,A.ADDR1) SHIPUNTINO," +
      		      "ISNULL(B.SHIPADDR2,A.ADDR2) SHIPBUILDING," +
      		      "ISNULL(B.SHIPADDR3,A.ADDR3) SHIPSTREET," +
      		      "ISNULL(B.SHIPADDR4,A.ADDR4) SHIPCITY," +
      		      "ISNULL(B.SHIPSTATE,A.STATE) SHIPSTATE," +
      		      "ISNULL(B.SHIPCOUNTRY,A.COUNTRY) SHIPCOUNTRY," +
      		      "ISNULL(B.SHIPZIP,A.ZIP) SHIPZIP, "+
          		      "ISNULL(NAME,'') CONTACTNAME," +
          		      "ISNULL(TELNO,'') TELEPHONE," +
          		      "ISNULL(HPNO,'') HANDPHONE," +
          		      "ISNULL(FAX,'')FAX," +
          		      "ISNULL(EMAIL,'')EMAIL," +
          		      "ISNULL(ADDR1,'') UNTINO," +
          		      "ISNULL(ADDR2,'') BUILDING," +
          		      "ISNULL(ADDR3,'') STREET," +
          		      "ISNULL(ADDR4,'') CITY," +
          		      "ISNULL(STATE,'') STATE," +
          		      "ISNULL(COUNTRY,'') COUNTRY," +
          		      "ISNULL(ZIP,'') POSTALCODE FROM "
	                            + "[" + plant+ "_" + "CUSTMST"+ "] A , "+"[" + plant+ "_" + "DOHDR"+ "] B"
	                            + " WHERE A.PLANT=B.PLANT and  A.CUSTNO=B.CustCode  and  B.DONO ='"+orderno+"' and A.CUSTNO ='"+shippingid+"'" ;
	          
	            ps = con.prepareStatement(sQry);
	            System.out.println(sQry);
	            rs = ps.executeQuery();
	            while (rs.next()) {
	                    arrCust.add(0, StrUtils.fString(rs.getString(1))); // id
	                    arrCust.add(1, StrUtils.fString(rs.getString(2))); // customername
	                    arrCust.add(2, StrUtils.fString(rs.getString(3))); // shipcontactname
	                    arrCust.add(3, StrUtils.fString(rs.getString(4))); // shiptelephone
	                    arrCust.add(4, StrUtils.fString(rs.getString(5))); // shiphandphone
	                    arrCust.add(5, StrUtils.fString(rs.getString(6))); // shipfax
	                    arrCust.add(6, StrUtils.fString(rs.getString(7))); // shipemail
	                    arrCust.add(7, StrUtils.fString(rs.getString(8))); // shipunitno
	                    arrCust.add(8, StrUtils.fString(rs.getString(9)));// shipbuilding
	                    arrCust.add(9, StrUtils.fString(rs.getString(10)));// shipstreet
	                    arrCust.add(10, StrUtils.fString(rs.getString(11)));// shipcity
	                    arrCust.add(11, StrUtils.fString(rs.getString(12)));// shipstate
	                    arrCust.add(12, StrUtils.fString(rs.getString(13)));//shipcountry
	                    arrCust.add(13, StrUtils.fString(rs.getString(14)));// shippostalcode
	                    arrCust.add(14, StrUtils.fString(rs.getString(15))); // contactname
	                    arrCust.add(15, StrUtils.fString(rs.getString(16))); // telephone
	                    arrCust.add(16, StrUtils.fString(rs.getString(17))); // handphone
	                    arrCust.add(17, StrUtils.fString(rs.getString(18))); // fax
	                    arrCust.add(18, StrUtils.fString(rs.getString(19))); // email
	                    arrCust.add(19, StrUtils.fString(rs.getString(20))); // unitno
	                    arrCust.add(20, StrUtils.fString(rs.getString(21)));// building
	                    arrCust.add(21, StrUtils.fString(rs.getString(22)));// street
	                    arrCust.add(22, StrUtils.fString(rs.getString(23)));// city
	                    arrCust.add(23, StrUtils.fString(rs.getString(24)));// state
	                    arrCust.add(24, StrUtils.fString(rs.getString(25)));//country
	                    arrCust.add(25, StrUtils.fString(rs.getString(26)));// postalcode
	                  }
	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	    } finally {
	            DbBean.closeConnection(con, ps);
	    }
	    return arrCust;
	}
	 
	 public ArrayList getPOESTShippingDetails(String orderno, String shippingid,String plant)
	            throws Exception {
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    ArrayList arrCust = new ArrayList();
	    Connection con = null;
	    try {
	            con = DbBean.getConnection();
	            String sQry = "SELECT " +
       		      "CUSTNO AS ID," +
       		      "ISNULL(CNAME,'') CUSTOMERNAME," +
       		    "ISNULL(B.SHIPCONTACTNAME,'') SHIPCONTACTNAME," +
   		      "ISNULL(B.SHIPWORKPHONE,'') SHIPWORKPHONE," +
   		      "ISNULL(B.SHIPHPNO,'') SHIPHPNO," +
   		      "ISNULL(B.SHIPDESGINATION,'') SHIPDESGINATION," +
   		      "ISNULL(B.SHIPEMAIL,'') SHIPEMAIL," +
   		      "ISNULL(B.SHIPADDR1,'') SHIPUNTINO," +
   		      "ISNULL(B.SHIPADDR2,'') SHIPBUILDING," +
   		      "ISNULL(B.SHIPADDR3,'') SHIPSTREET," +
   		      "ISNULL(B.SHIPADDR4,'') SHIPCITY," +
   		      "ISNULL(B.SHIPSTATE,'') SHIPSTATE," +
   		      "ISNULL(B.SHIPCOUNTRY,'') SHIPCOUNTRY," +
   		      "ISNULL(B.SHIPZIP,'') SHIPZIP, "+
       		      "ISNULL(NAME,'') CONTACTNAME," +
       		      "ISNULL(TELNO,'') TELEPHONE," +
       		      "ISNULL(HPNO,'') HANDPHONE," +
       		      "ISNULL(FAX,'')FAX," +
       		      "ISNULL(EMAIL,'')EMAIL," +
       		      "ISNULL(ADDR1,'') UNTINO," +
       		      "ISNULL(ADDR2,'') BUILDING," +
       		      "ISNULL(ADDR3,'') STREET," +
       		      "ISNULL(ADDR4,'') CITY," +
       		      "ISNULL(STATE,'') STATE," +
       		      "ISNULL(COUNTRY,'') COUNTRY," +
       		      "ISNULL(ZIP,'') POSTALCODE FROM "
	                            + "[" + plant+ "_" + "CUSTMST"+ "] A , "+"[" + plant+ "_" + "POESTHDR"+ "] B"
	                            + " WHERE A.PLANT=B.PLANT and  A.CUSTNO=B.SHIPPINGID  and  B.POESTNO ='"+orderno+"' and A.CUSTNO ='"+shippingid+"'" ;
	          
	            ps = con.prepareStatement(sQry);
	            System.out.println(sQry);
	            rs = ps.executeQuery();
	            while (rs.next()) {
	                    arrCust.add(0, StrUtils.fString(rs.getString(1))); // id
	                    arrCust.add(1, StrUtils.fString(rs.getString(2))); // customername
	                    arrCust.add(2, StrUtils.fString(rs.getString(3))); // shipcontactname
	                    arrCust.add(3, StrUtils.fString(rs.getString(4))); // shiptelephone
	                    arrCust.add(4, StrUtils.fString(rs.getString(5))); // shiphandphone
	                    arrCust.add(5, StrUtils.fString(rs.getString(6))); // shipfax
	                    arrCust.add(6, StrUtils.fString(rs.getString(7))); // shipemail
	                    arrCust.add(7, StrUtils.fString(rs.getString(8))); // shipunitno
	                    arrCust.add(8, StrUtils.fString(rs.getString(9)));// shipbuilding
	                    arrCust.add(9, StrUtils.fString(rs.getString(10)));// shipstreet
	                    arrCust.add(10, StrUtils.fString(rs.getString(11)));// shipcity
	                    arrCust.add(11, StrUtils.fString(rs.getString(12)));// shipstate
	                    arrCust.add(12, StrUtils.fString(rs.getString(13)));//shipcountry
	                    arrCust.add(13, StrUtils.fString(rs.getString(14)));// shippostalcode
	                    arrCust.add(14, StrUtils.fString(rs.getString(15))); // contactname
	                    arrCust.add(15, StrUtils.fString(rs.getString(16))); // telephone
	                    arrCust.add(16, StrUtils.fString(rs.getString(17))); // handphone
	                    arrCust.add(17, StrUtils.fString(rs.getString(18))); // fax
	                    arrCust.add(18, StrUtils.fString(rs.getString(19))); // email
	                    arrCust.add(19, StrUtils.fString(rs.getString(20))); // unitno
	                    arrCust.add(20, StrUtils.fString(rs.getString(21)));// building
	                    arrCust.add(21, StrUtils.fString(rs.getString(22)));// street
	                    arrCust.add(22, StrUtils.fString(rs.getString(23)));// city
	                    arrCust.add(23, StrUtils.fString(rs.getString(24)));// state
	                    arrCust.add(24, StrUtils.fString(rs.getString(25)));//country
	                    arrCust.add(25, StrUtils.fString(rs.getString(26)));// postalcode
	                  }
	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	    } finally {
	            DbBean.closeConnection(con, ps);
	    }
	    return arrCust;
	}
	 
	 //imtinavas dertail jsp
	 public ArrayList getTransferShippingDetails(String orderno, String shippingid,String plant)
	            throws Exception {
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    ArrayList arrCust = new ArrayList();
	    Connection con = null;
	    try {
	            con = DbBean.getConnection();
	            String sQry = "SELECT " +
       		      "CUSTNO AS ID," +
       		      "ISNULL(CNAME,'') CUSTOMERNAME," +
       		      "ISNULL(NAME,'') CONTACTNAME," +
       		      "ISNULL(TELNO,'') TELEPHONE," +
       		      "ISNULL(HPNO,'') HANDPHONE," +
       		      "ISNULL(FAX,'')FAX," +
       		      "ISNULL(EMAIL,'')EMAIL," +
       		      "ISNULL(ADDR1,'') UNTINO," +
       		      "ISNULL(ADDR2,'') BUILDING," +
       		      "ISNULL(ADDR3,'') STREET," +
       		      "ISNULL(ADDR4,'') CITY," +
       		      "ISNULL(STATE,'') STATE," +
       		      "ISNULL(COUNTRY,'') COUNTRY," +
       		      "ISNULL(ZIP,'') POSTALCODE FROM "
	                            + "[" + plant+ "_" + "CUSTMST"+ "] A , "+"[" + plant+ "_" + "TOHDR"+ "] B"
	                            + " WHERE A.PLANT=B.PLANT and  A.CUSTNO=B.CustCode  and  B.TONO ='"+orderno+"' and A.CUSTNO ='"+shippingid+"' " ;
	          
	            ps = con.prepareStatement(sQry);
	            System.out.println(sQry);
	            rs = ps.executeQuery();
	            while (rs.next()) {
	                    arrCust.add(0, StrUtils.fString(rs.getString(1))); // id
	                    arrCust.add(1, StrUtils.fString(rs.getString(2))); // customername
	                    arrCust.add(2, StrUtils.fString(rs.getString(3))); // contactname
	                    arrCust.add(3, StrUtils.fString(rs.getString(4))); // telephone
	                    arrCust.add(4, StrUtils.fString(rs.getString(5))); // handphone
	                    arrCust.add(5, StrUtils.fString(rs.getString(6))); // fax
	                    arrCust.add(6, StrUtils.fString(rs.getString(7))); // email 
	                    arrCust.add(7, StrUtils.fString(rs.getString(8))); // unitno
	                    arrCust.add(8, StrUtils.fString(rs.getString(9)));// building
	                    arrCust.add(9, StrUtils.fString(rs.getString(10)));// street
	                    arrCust.add(10, StrUtils.fString(rs.getString(11)));// city
	                    arrCust.add(11, StrUtils.fString(rs.getString(12)));// state
	                    arrCust.add(12, StrUtils.fString(rs.getString(13)));//country
	                    arrCust.add(13, StrUtils.fString(rs.getString(14)));// postalcode
	                  }
	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	    } finally {
	            DbBean.closeConnection(con, ps);
	    }
	    return arrCust;
	}
	 /*public ArrayList getInboundShippingDetails(String orderno, String shippingid,String plant)
	            throws Exception {
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    ArrayList arrCust = new ArrayList();
	    Connection con = null;
	    try {
	            con = DbBean.getConnection();
	            String sQry = "SELECT " +
	            		      "ID," +
	            		      "ISNULL(CUSTOMERNAME,'') CUSTOMERNAME," +
	            		      "ISNULL(CONTACTNAME,'') CONTACTNAME," +
	            		      "ISNULL(TELEPHONE,'') TELEPHONE," +
	            		      "ISNULL(HANDPHONE,'') HANDPHONE," +
	            		      "ISNULL(FAX,'')FAX," +
	            		      "ISNULL(EMAIL,'')EMAIL," +
	            		      "ISNULL(UNITNO,'') UNTINO," +
	            		      "ISNULL(BUILDING,'') BUILDING," +
	            		      "ISNULL(STREET,'') STREET," +
	            		      "ISNULL(CITY,'') CITY," +
	            		      "ISNULL(STATE,'') STATE," +
	            		      "ISNULL(COUNTRY,'') COUNTRY," +
	            		      "ISNULL(POSTALCODE,'') POSTALCODE FROM "
	                            + "[" + plant+ "_" + "SHIPPINGMST"+ "] A , "+"[" + plant+ "_" + "POHDR"+ "] B"
	                            + " WHERE A.PLANT=B.PLANT and  A.ID=B.SHIPPINGID  and  B.PONO ='"+orderno+"' and A.ID ='"+shippingid+"'" ;
	          
	            ps = con.prepareStatement(sQry);
	            System.out.println(sQry);
	            rs = ps.executeQuery();
	            while (rs.next()) {
	                    arrCust.add(0, StrUtils.fString(rs.getString(1))); // id
	                    arrCust.add(1, StrUtils.fString(rs.getString(2))); // customername
	                    arrCust.add(2, StrUtils.fString(rs.getString(3))); // contactname
	                    arrCust.add(3, StrUtils.fString(rs.getString(4))); // telephone
	                    arrCust.add(4, StrUtils.fString(rs.getString(5))); // handphone
	                    arrCust.add(5, StrUtils.fString(rs.getString(6))); // fax
	                    arrCust.add(6, StrUtils.fString(rs.getString(7))); // email
	                    arrCust.add(7, StrUtils.fString(rs.getString(8))); // unitno
	                    arrCust.add(8, StrUtils.fString(rs.getString(9)));// building
	                    arrCust.add(9, StrUtils.fString(rs.getString(10)));// street
	                    arrCust.add(10, StrUtils.fString(rs.getString(11)));// city
	                    arrCust.add(11, StrUtils.fString(rs.getString(12)));// state
	                    arrCust.add(12, StrUtils.fString(rs.getString(13)));//country
	                    arrCust.add(13, StrUtils.fString(rs.getString(14)));// postalcode
	                  }
	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	    } finally {
	            DbBean.closeConnection(con, ps);
	    }
	    return arrCust;
	}*/
	 
	 public ArrayList getInboundShippingDetails(String orderno, String shippingid,String plant)
	            throws Exception {
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    ArrayList arrCust = new ArrayList();
	    Connection con = null;
	    try {
	            con = DbBean.getConnection();
	            String sQry = "SELECT " +
	            		      //"VENDNO AS ID," +
	            		      //"ISNULL(VNAME,'') CUSTOMERNAME," +
	            		      "CUSTNO AS ID," + //0
	            		      "ISNULL(CNAME,'') CUSTOMERNAME," + //1
	            		      "ISNULL(B.SHIPCONTACTNAME,'') SHIPCONTACTNAME," + //2
	            		      "ISNULL(B.SHIPWORKPHONE,'') SHIPWORKPHONE," + //3
	            		      "ISNULL(B.SHIPHPNO,'') SHIPHPNO," +
	            		      "ISNULL(B.SHIPDESGINATION,'') SHIPDESGINATION," + 
	            		      "ISNULL(B.SHIPEMAIL,'') SHIPEMAIL," +
	            		      "ISNULL(B.SHIPADDR1,'') SHIPUNTINO," +
	            		      "ISNULL(B.SHIPADDR2,'') SHIPBUILDING," +
	            		      "ISNULL(B.SHIPADDR3,'') SHIPSTREET," +
	            		      "ISNULL(B.SHIPADDR4,'') SHIPCITY," +
	            		      "ISNULL(B.SHIPSTATE,'') SHIPSTATE," +
	            		      "ISNULL(B.SHIPCOUNTRY,'') SHIPCOUNTRY," +
	            		      "ISNULL(B.SHIPZIP,'') SHIPZIP, "+
	            		      "ISNULL(NAME,'') CONTACTNAME," +
	            		      "ISNULL(TELNO,'') TELEPHONE," +
	            		      "ISNULL(HPNO,'') HANDPHONE," +
	            		      "ISNULL(FAX,'')FAX," +
	            		      "ISNULL(EMAIL,'')EMAIL," +
	            		      "ISNULL(ADDR1,'') UNTINO," +
	            		      "ISNULL(ADDR2,'') BUILDING," +
	            		      "ISNULL(ADDR3,'') STREET," +
	            		      "ISNULL(ADDR4,'') CITY," +
	            		      "ISNULL(STATE,'') STATE," +
	            		      "ISNULL(COUNTRY,'') COUNTRY," +
	            		      "ISNULL(ZIP,'') POSTALCODE "
	                            + "FROM [" + plant+ "_" + "CUSTMST"+ "] A , "+"[" + plant+ "_" + "POHDR"+ "] B"	                            
	                            + " WHERE A.PLANT=B.PLANT and  A.CUSTNO=B.SHIPPINGID  and  B.PONO ='"+orderno+"' and A.CUSTNO ='"+shippingid+"'" ;
	                           //+ "[" + plant+ "_" + "VENDMST"+ "] A , "+"[" + plant+ "_" + "POHDR"+ "] B"
	                           //+ " WHERE A.PLANT=B.PLANT and  A.VENDNO=B.SHIPPINGID  and  B.PONO ='"+orderno+"' and A.VENDNO ='"+shippingid+"'" ;
	          
	            ps = con.prepareStatement(sQry);
	            System.out.println(sQry);
	            rs = ps.executeQuery();
	            while (rs.next()) {
	                    arrCust.add(0, StrUtils.fString(rs.getString(1))); // id
	                    arrCust.add(1, StrUtils.fString(rs.getString(2))); // customername
	                    arrCust.add(2, StrUtils.fString(rs.getString(3))); // shipcontactname
	                    arrCust.add(3, StrUtils.fString(rs.getString(4))); // shiptelephone
	                    arrCust.add(4, StrUtils.fString(rs.getString(5))); // shiphandphone
	                    arrCust.add(5, StrUtils.fString(rs.getString(6))); // shipfax
	                    arrCust.add(6, StrUtils.fString(rs.getString(7))); // shipemail
	                    arrCust.add(7, StrUtils.fString(rs.getString(8))); // shipunitno
	                    arrCust.add(8, StrUtils.fString(rs.getString(9)));// shipbuilding
	                    arrCust.add(9, StrUtils.fString(rs.getString(10)));// shipstreet
	                    arrCust.add(10, StrUtils.fString(rs.getString(11)));// shipcity
	                    arrCust.add(11, StrUtils.fString(rs.getString(12)));// shipstate
	                    arrCust.add(12, StrUtils.fString(rs.getString(13)));//shipcountry
	                    arrCust.add(13, StrUtils.fString(rs.getString(14)));// shippostalcode
	                    arrCust.add(14, StrUtils.fString(rs.getString(15))); // contactname
	                    arrCust.add(15, StrUtils.fString(rs.getString(16))); // telephone
	                    arrCust.add(16, StrUtils.fString(rs.getString(17))); // handphone
	                    arrCust.add(17, StrUtils.fString(rs.getString(18))); // fax
	                    arrCust.add(18, StrUtils.fString(rs.getString(19))); // email
	                    arrCust.add(19, StrUtils.fString(rs.getString(20))); // unitno
	                    arrCust.add(20, StrUtils.fString(rs.getString(21)));// building
	                    arrCust.add(21, StrUtils.fString(rs.getString(22)));// street
	                    arrCust.add(22, StrUtils.fString(rs.getString(23)));// city
	                    arrCust.add(23, StrUtils.fString(rs.getString(24)));// state
	                    arrCust.add(24, StrUtils.fString(rs.getString(25)));//country
	                    arrCust.add(25, StrUtils.fString(rs.getString(26)));// postalcode
	                  }
	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	    } finally {
	            DbBean.closeConnection(con, ps);
	    }
	    return arrCust;
	}
	 
	 public ArrayList getInboundShippingDetailsVendmst(String orderno, String shippingid,String plant)
			 throws Exception {
		 PreparedStatement ps = null;
		 ResultSet rs = null;
		 ArrayList arrCust = new ArrayList();
		 Connection con = null;
		 try {
			 con = DbBean.getConnection();
			 String sQry = "SELECT " +
					 "VENDNO AS ID," +
					 "ISNULL(VNAME,'') CUSTOMERNAME," +
//					 "CUSTNO AS ID," + //0
//					 "ISNULL(CNAME,'') CUSTOMERNAME," + //1
					 "ISNULL(B.SHIPCONTACTNAME,A.NAME) SHIPCONTACTNAME," + //2
					 "ISNULL(B.SHIPWORKPHONE,A.TELNO) SHIPWORKPHONE," + //3
					 "ISNULL(B.SHIPHPNO,A.HPNO) SHIPHPNO," +
					 "ISNULL(B.SHIPDESGINATION,A.DESGINATION) SHIPDESGINATION," + 
					 "ISNULL(B.SHIPEMAIL,A.EMAIL) SHIPEMAIL," +
					 "ISNULL(B.SHIPADDR1,A.ADDR1) SHIPUNTINO," +
					 "ISNULL(B.SHIPADDR2,A.ADDR2) SHIPBUILDING," +
					 "ISNULL(B.SHIPADDR3,A.ADDR3) SHIPSTREET," +
					 "ISNULL(B.SHIPADDR4,A.ADDR4) SHIPCITY," +
					 "ISNULL(B.SHIPSTATE,A.STATE) SHIPSTATE," +
					 "ISNULL(B.SHIPCOUNTRY,A.COUNTRY) SHIPCOUNTRY," +
					 "ISNULL(B.SHIPZIP,A.ZIP) SHIPZIP, "+
					 "ISNULL(NAME,'') CONTACTNAME," +
					 "ISNULL(TELNO,'') TELEPHONE," +
					 "ISNULL(HPNO,'') HANDPHONE," +
					 "ISNULL(FAX,'')FAX," +
					 "ISNULL(EMAIL,'')EMAIL," +
					 "ISNULL(ADDR1,'') UNTINO," +
					 "ISNULL(ADDR2,'') BUILDING," +
					 "ISNULL(ADDR3,'') STREET," +
					 "ISNULL(ADDR4,'') CITY," +
					 "ISNULL(STATE,'') STATE," +
					 "ISNULL(COUNTRY,'') COUNTRY," +
					 "ISNULL(ZIP,'') POSTALCODE "
//					 + "FROM [" + plant+ "_" + "VENDMST"+ "] A , "+"[" + plant+ "_" + "POHDR"+ "] B"	                            
//					 + " WHERE A.PLANT=B.PLANT and  A.CUSTNO=B.SHIPPINGID  and  B.PONO ='"+orderno+"' and A.CUSTNO ='"+shippingid+"'" ;
			 + "FROM [" + plant+ "_" + "VENDMST"+ "] A , "+"[" + plant+ "_" + "POHDR"+ "] B"
			 + " WHERE A.PLANT=B.PLANT and  A.VENDNO=B.SHIPPINGID  and  B.PONO ='"+orderno+"' and A.VENDNO ='"+shippingid+"'" ;
			 
			 ps = con.prepareStatement(sQry);
			 System.out.println(sQry);
			 rs = ps.executeQuery();
			 while (rs.next()) {
				 arrCust.add(0, StrUtils.fString(rs.getString(1))); // id
				 arrCust.add(1, StrUtils.fString(rs.getString(2))); // customername
				 arrCust.add(2, StrUtils.fString(rs.getString(3))); // shipcontactname
				 arrCust.add(3, StrUtils.fString(rs.getString(4))); // shiptelephone
				 arrCust.add(4, StrUtils.fString(rs.getString(5))); // shiphandphone
				 arrCust.add(5, StrUtils.fString(rs.getString(6))); // shipfax
				 arrCust.add(6, StrUtils.fString(rs.getString(7))); // shipemail
				 arrCust.add(7, StrUtils.fString(rs.getString(8))); // shipunitno
				 arrCust.add(8, StrUtils.fString(rs.getString(9)));// shipbuilding
				 arrCust.add(9, StrUtils.fString(rs.getString(10)));// shipstreet
				 arrCust.add(10, StrUtils.fString(rs.getString(11)));// shipcity
				 arrCust.add(11, StrUtils.fString(rs.getString(12)));// shipstate
				 arrCust.add(12, StrUtils.fString(rs.getString(13)));//shipcountry
				 arrCust.add(13, StrUtils.fString(rs.getString(14)));// shippostalcode
				 arrCust.add(14, StrUtils.fString(rs.getString(15))); // contactname
				 arrCust.add(15, StrUtils.fString(rs.getString(16))); // telephone
				 arrCust.add(16, StrUtils.fString(rs.getString(17))); // handphone
				 arrCust.add(17, StrUtils.fString(rs.getString(18))); // fax
				 arrCust.add(18, StrUtils.fString(rs.getString(19))); // email
				 arrCust.add(19, StrUtils.fString(rs.getString(20))); // unitno
				 arrCust.add(20, StrUtils.fString(rs.getString(21)));// building
				 arrCust.add(21, StrUtils.fString(rs.getString(22)));// street
				 arrCust.add(22, StrUtils.fString(rs.getString(23)));// city
				 arrCust.add(23, StrUtils.fString(rs.getString(24)));// state
				 arrCust.add(24, StrUtils.fString(rs.getString(25)));//country
				 arrCust.add(25, StrUtils.fString(rs.getString(26)));// postalcode
			 }
		 } catch (Exception e) {
			 this.mLogger.exception(this.printLog, "", e);
		 } finally {
			 DbBean.closeConnection(con, ps);
		 }
		 return arrCust;
	 }
	 
	 //Resvi Starts
	 public ArrayList getPOMULTIEDTShippingDetails(String orderno, String shippingid,String plant)
	            throws Exception {
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    ArrayList arrCust = new ArrayList();
	    Connection con = null;
	    try {
	            con = DbBean.getConnection();
	            String sQry = "SELECT " +
	            		      "VENDNO AS ID," +
	            		      "ISNULL(VNAME,'') CUSTOMERNAME," +
	            		      "ISNULL(NAME,'') CONTACTNAME," +
	            		      "ISNULL(TELNO,'') TELEPHONE," +
	            		      "ISNULL(HPNO,'') HANDPHONE," +
	            		      "ISNULL(FAX,'')FAX," +
	            		      "ISNULL(EMAIL,'')EMAIL," +
	            		      "ISNULL(ADDR1,'') UNTINO," +
	            		      "ISNULL(ADDR2,'') BUILDING," +
	            		      "ISNULL(ADDR3,'') STREET," +
	            		      "ISNULL(ADDR4,'') CITY," +
	            		      "ISNULL(STATE,'') STATE," +
	            		      "ISNULL(COUNTRY,'') COUNTRY," +
	            		      "ISNULL(ZIP,'') POSTALCODE FROM "
	                            //+ "[" + plant+ "_" + "CUSTMST"+ "] A , "+"[" + plant+ "_" + "POHDR"+ "] B"	                            
	                           // + " WHERE A.PLANT=B.PLANT and  A.CUSTNO=B.SHIPPINGID  and  B.PONO ='"+orderno+"' and A.CUSTNO ='"+shippingid+"'" ;
	                           + "[" + plant+ "_" + "VENDMST"+ "] A , "+"[" + plant+ "_" + "PO_MULTI_ESTHDR"+ "] B"
	                           + " WHERE A.PLANT=B.PLANT and  A.VENDNO=B.SHIPPINGID  and  B.POMULTIESTNO ='"+orderno+"' and A.VENDNO ='"+shippingid+"'" ;
	          
	            ps = con.prepareStatement(sQry);
	            System.out.println(sQry);
	            rs = ps.executeQuery();
	            while (rs.next()) {
	                    arrCust.add(0, StrUtils.fString(rs.getString(1))); // id
	                    arrCust.add(1, StrUtils.fString(rs.getString(2))); // customername
	                    arrCust.add(2, StrUtils.fString(rs.getString(3))); // contactname
	                    arrCust.add(3, StrUtils.fString(rs.getString(4))); // telephone
	                    arrCust.add(4, StrUtils.fString(rs.getString(5))); // handphone
	                    arrCust.add(5, StrUtils.fString(rs.getString(6))); // fax
	                    arrCust.add(6, StrUtils.fString(rs.getString(7))); // email
	                    arrCust.add(7, StrUtils.fString(rs.getString(8))); // unitno
	                    arrCust.add(8, StrUtils.fString(rs.getString(9)));// building
	                    arrCust.add(9, StrUtils.fString(rs.getString(10)));// street
	                    arrCust.add(10, StrUtils.fString(rs.getString(11)));// city
	                    arrCust.add(11, StrUtils.fString(rs.getString(12)));// state
	                    arrCust.add(12, StrUtils.fString(rs.getString(13)));//country
	                    arrCust.add(13, StrUtils.fString(rs.getString(14)));// postalcode
	                  }
	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	    } finally {
	            DbBean.closeConnection(con, ps);
	    }
	    return arrCust;
	}
	 
	 
	 //Ends
	 
	 
	 
	 public boolean isExisitShippingDetails(Hashtable ht, String extCond) throws Exception {
			boolean flag = false;
			java.sql.Connection con = null;
			try {
				con = com.track.gates.DbBean.getConnection();

				StringBuffer sql = new StringBuffer(" SELECT ");
				sql.append("COUNT(*) ");
				sql.append(" ");
				sql.append(" FROM " + "["+ht.get("PLANT")+"_SHIPPINGMST]");
				sql.append(" WHERE  " + formCondition(ht));

				if (extCond.length() > 0)
					sql.append(" and " + extCond);

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

// Check duplicate for INCOTERM 17.10.18 changes by Azees	 
		 public boolean isExistsINCOTERM(String aINCOTERM, String plant) throws Exception {
				PreparedStatement ps = null;
				ResultSet rs = null;
				boolean isExists = false;
				Connection con = null;
				try {
					con = DbBean.getConnection();
					String sQry = "SELECT COUNT(*) FROM " +  "[" + plant + "_" + "INCOTERMSMST" + "]" + " WHERE "
							+ IConstants.INCOTERMS + " = '"
							+ aINCOTERM.toUpperCase() + "'";
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
		 
		 public boolean isExistsREMARKS(String aRemarks, String plant) throws Exception {
				PreparedStatement ps = null;
				ResultSet rs = null;
				boolean isExists = false;
				Connection con = null;
				try {
					con = DbBean.getConnection();
					String sQry = "SELECT COUNT(*) FROM " +  "[" + plant + "_" + "REMARKSMST" + "]" + " WHERE "
							+ IConstants.REMARKS + " = '"
							+ aRemarks.toUpperCase() + "'";
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
		 
//			Resvi Starts TransportMode
		 public boolean isExistsTRANSPORTMODE(String aTransportMode, String plant) throws Exception {
				PreparedStatement ps = null;
				ResultSet rs = null;
				boolean isExists = false;
				Connection con = null;
				try {
					con = DbBean.getConnection();
					String sQry = "SELECT COUNT(*) FROM " +  "[" + plant + "_" + "TRANSPORT_MODE_MST" + "]" + " WHERE "
							+ IConstants.TRANSPORT_MODE + " = '"
							+ aTransportMode.toUpperCase() + "'";
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
//		 ENDS
			public boolean isExistsFOOTER(String aFOOTER, String plant) throws Exception {
				PreparedStatement ps = null;
				ResultSet rs = null;
				boolean isExists = false;
				Connection con = null;
				try {
					con = DbBean.getConnection();
					String sQry = "SELECT COUNT(*) FROM " +  "[" + plant + "_" + "FOOTERMST" + "]" + " WHERE "
							+ IConstants.FOOTER + " = '"
							+ aFOOTER.toUpperCase() + "'";
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
			
			 
			public ArrayList getTaxInvShippingDetails(String orderno, String shippingid,String plant)
		            throws Exception {
		    PreparedStatement ps = null;
		    ResultSet rs = null;
		    ArrayList arrCust = new ArrayList();
		    Connection con = null;
		    try {
		            con = DbBean.getConnection();
		            String sQry = "SELECT " +
	          		      "CUSTNO AS ID," +
	          		      "ISNULL(CNAME,'') CUSTOMERNAME," +
	          		      "ISNULL(NAME,'') CONTACTNAME," +
	          		      "ISNULL(TELNO,'') TELEPHONE," +
	          		      "ISNULL(HPNO,'') HANDPHONE," +
	          		      "ISNULL(FAX,'')FAX," +
	          		      "ISNULL(EMAIL,'')EMAIL," +
	          		      "ISNULL(ADDR1,'') UNTINO," +
	          		      "ISNULL(ADDR2,'') BUILDING," +
	          		      "ISNULL(ADDR3,'') STREET," +
	          		      "ISNULL(ADDR4,'') CITY," +
	          		      "ISNULL(STATE,'') STATE," +
	          		      "ISNULL(COUNTRY,'') COUNTRY," +
	          		      "ISNULL(ZIP,'') POSTALCODE FROM "
		                            + "[" + plant+ "_" + "CUSTMST"+ "] A , "+"[" + plant+ "_" + "POSHDR"+ "] B"
		                            + " WHERE A.PLANT=B.PLANT and  A.CUSTNO=B.SHIPPINGID  and  B.POSTRANID ='"+orderno+"' and A.CUSTNO ='"+shippingid+"'" ;
		          
		            ps = con.prepareStatement(sQry);
		            System.out.println(sQry);
		            rs = ps.executeQuery();
		            while (rs.next()) {
		                    arrCust.add(0, StrUtils.fString(rs.getString(1))); // id
		                    arrCust.add(1, StrUtils.fString(rs.getString(2))); // customername
		                    arrCust.add(2, StrUtils.fString(rs.getString(3))); // contactname
		                    arrCust.add(3, StrUtils.fString(rs.getString(4))); // telephone
		                    arrCust.add(4, StrUtils.fString(rs.getString(5))); // handphone
		                    arrCust.add(5, StrUtils.fString(rs.getString(6))); // fax
		                    arrCust.add(6, StrUtils.fString(rs.getString(7))); // email
		                    arrCust.add(7, StrUtils.fString(rs.getString(8))); // unitno
		                    arrCust.add(8, StrUtils.fString(rs.getString(9)));// building
		                    arrCust.add(9, StrUtils.fString(rs.getString(10)));// street
		                    arrCust.add(10, StrUtils.fString(rs.getString(11)));// city
		                    arrCust.add(11, StrUtils.fString(rs.getString(12)));// state
		                    arrCust.add(12, StrUtils.fString(rs.getString(13)));//country
		                    arrCust.add(13, StrUtils.fString(rs.getString(14)));// postalcode
		                  }
		    } catch (Exception e) {
		            this.mLogger.exception(this.printLog, "", e);
		    } finally {
		            DbBean.closeConnection(con, ps);
		    }
		    return arrCust;
		}
			
			/**************************PAYMENTTYPE***********************************************************************************************************/		
			public boolean isExistsPaymentType(String aPaymentType, String plant) throws Exception {
				PreparedStatement ps = null;
				ResultSet rs = null;
				boolean isExists = false;
				Connection con = null;
				try {
					con = DbBean.getConnection();
					String sQry = "SELECT COUNT(*) FROM " +  "[" + plant + "_" + "PAYMENTTYPEMST" + "]" + " WHERE "
							+ IConstants.PAYMENTTYPE + " = '"
							+ aPaymentType.toUpperCase() + "'";
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
		 
		 
			
			public boolean InsertPaymentType(Hashtable ht) throws Exception {
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
							+ "PAYMENTTYPEMST" + "]" + "("
							+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
							+ VALUES.substring(0, VALUES.length() - 1) + ")";
					this.mLogger.query(this.printQuery, query);
					System.out.println("InsertPaymentType"+query);
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
			
			public boolean deletePaymentType(java.util.Hashtable ht) throws Exception {
				MLogger.log(1, this.getClass() + " deletePaymentType()");
				boolean delete = false;
				java.sql.Connection con = null;
				try {
					con = DbBean.getConnection();
		          	StringBuffer sql = new StringBuffer(" DELETE ");
					sql.append(" ");
					sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "PAYMENTTYPEMST"
							+ "]");
					sql.append(" WHERE " + formCondition(ht));
					this.mLogger.query(this.printQuery, sql.toString());
					System.out.println("DeletePaymentType"+sql.toString());
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
			
			public ArrayList getPaymentTypeDetails(String plant,String extraCon) throws Exception {
				java.sql.Connection con = null;
				ArrayList al = new ArrayList();
				try {
					con = com.track.gates.DbBean.getConnection();
					boolean flag = false;
					StringBuffer sql = new StringBuffer("select ID,ISNULL(PAYMENTTYPE,'') PAYMENTTYPE from ["+plant+"_PAYMENTTYPEMST] ");
					sql.append(" where PAYMENTTYPE <>'NOPAYMENTTYPEDETAILS '");
					
					if (extraCon.length() > 0)
						sql.append(" and " + extraCon + " order by ID");
					else
					  sql.append(" order by ID");
				    this.mLogger.query(this.printQuery, sql.toString());
				    System.out.println("MasterDAO  getPaymentTypeDetails():"+ sql.toString());
					al = selectData(con, sql.toString());

				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					System.out.println("MasterDAO  getPaymentTypeDetails():"+e.getMessage());
					throw e;
				} finally {
					if (con != null) {
						DbBean.closeConnection(con);
					}
				}

				return al;
			}
			
			
			public boolean getPaymentTypeisExist(String plant,String paymenttype) throws Exception {
				java.sql.Connection con = null;
				boolean al = false;
				try {
					con = com.track.gates.DbBean.getConnection();
					boolean flag = false;
					StringBuffer sql = new StringBuffer("select ID,ISNULL(PAYMENTTYPE,'') PAYMENTTYPE from ["+plant+"_PAYMENTTYPEMST] ");
					sql.append(" where PAYMENTTYPE ='"+paymenttype+"'");
				
				    this.mLogger.query(this.printQuery, sql.toString());
				    System.out.println("MasterDAO  getPaymentTypeDetails():"+ sql.toString());
					al = isExists(con, sql.toString());

				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					System.out.println("MasterDAO  getPaymentTypeDetails():"+e.getMessage());
					throw e;
				} finally {
					if (con != null) {
						DbBean.closeConnection(con);
					}
				}

				return al;
			}
			
			
			/**************************PAYMENTMODE***********************************************************************************************************/		
			public boolean isExistsPaymentMode(String aPaymentType, String plant) throws Exception {
				PreparedStatement ps = null;
				ResultSet rs = null;
				boolean isExists = false;
				Connection con = null;
				try {
					con = DbBean.getConnection();
					String sQry = "SELECT COUNT(*) FROM " +  "[" + plant + "_" + "FINPAYMENTMODEMST" + "]" + " WHERE "
							+ IConstants.PAYMENTMODE + " = '"
							+ aPaymentType.toUpperCase() + "'";
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
			
			
			public boolean InsertPaymentMode(Hashtable ht) throws Exception {
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
							+ "FINPAYMENTMODEMST" + "]" + "("
							+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
							+ VALUES.substring(0, VALUES.length() - 1) + ")";
					this.mLogger.query(this.printQuery, query);
					System.out.println("InsertPaymentMode"+query);
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
			
			public ArrayList getPaymentModeDetails(String plant,String extraCon) throws Exception {
				java.sql.Connection con = null;
				ArrayList al = new ArrayList();
				try {
					con = com.track.gates.DbBean.getConnection();
					boolean flag = false;
					StringBuffer sql = new StringBuffer("select ID,ISNULL(PAYMENTMODE,'') PAYMENTMODE,ISNULL(PAYMENTMODESERIAL,'0') PAYMENTMODESERIAL from ["+plant+"_FINPAYMENTMODEMST] ");
					sql.append(" where PAYMENTMODE <>'NOPAYMENTTYPEDETAILS '");
					
					if (extraCon.length() > 0)
						sql.append(" and " + extraCon + " order by ID");
					else
						sql.append(" order by ID");
					this.mLogger.query(this.printQuery, sql.toString());
					System.out.println("MasterDAO  getPaymentModeDetails():"+ sql.toString());
					al = selectData(con, sql.toString());
					
				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					System.out.println("MasterDAO  getPaymentModeDetails():"+e.getMessage());
					throw e;
				} finally {
					if (con != null) {
						DbBean.closeConnection(con);
					}
				}
				
				return al;
			}
			
			public boolean getPaymentModeisExist(String plant,String paymenttype) throws Exception {
				java.sql.Connection con = null;
				boolean al = false;
				try {
					con = com.track.gates.DbBean.getConnection();
					boolean flag = false;
					StringBuffer sql = new StringBuffer("select ID,ISNULL(PAYMENTMODE,'') PAYMENTMODE from ["+plant+"_FINPAYMENTMODEMST] ");
					sql.append(" where PAYMENTMODE ='"+paymenttype+"'");
					
					this.mLogger.query(this.printQuery, sql.toString());
					System.out.println("MasterDAO  getPaymentModeDetails():"+ sql.toString());
					al = isExists(con, sql.toString());
					
				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					System.out.println("MasterDAO  getPaymentModeDetails():"+e.getMessage());
					throw e;
				} finally {
					if (con != null) {
						DbBean.closeConnection(con);
					}
				}
				
				return al;
			}
			
			public boolean deletePaymentMode(java.util.Hashtable ht) throws Exception {
				MLogger.log(1, this.getClass() + " deletePaymentMode()");
				boolean delete = false;
				java.sql.Connection con = null;
				try {
					con = DbBean.getConnection();
					StringBuffer sql = new StringBuffer(" DELETE ");
					sql.append(" ");
					sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "FINPAYMENTMODEMST"
							+ "]");
					sql.append(" WHERE " + formCondition(ht));
					this.mLogger.query(this.printQuery, sql.toString());
					System.out.println("DeletePaymentMode"+sql.toString());
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
			
			//**************************PAYMENTTERM***********************************************************************************************************/		
			
			public boolean InsertPaymentTerm(Hashtable ht) throws Exception {
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
							+ "FINPAYMENTTERMS" + "]" + "("
							+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
							+ VALUES.substring(0, VALUES.length() - 1) + ")";
					this.mLogger.query(this.printQuery, query);
					System.out.println("InsertPaymentTerm"+query);
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
			public boolean isExistsPaymentTerm(String aPaymentType, String plant) throws Exception {
				PreparedStatement ps = null;
				ResultSet rs = null;
				boolean isExists = false;
				Connection con = null;
				try {
					con = DbBean.getConnection();
					String sQry = "SELECT COUNT(*) FROM " +  "[" + plant + "_" + "FINPAYMENTTERMS" + "]" + " WHERE "
							+ IConstants.PAYMENT_TERMS + " = '"
							+ aPaymentType.toUpperCase() + "'";
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
			
			public ArrayList getPaymentTermDetails(String plant,String extraCon) throws Exception {
				java.sql.Connection con = null;
				ArrayList al = new ArrayList();
				try {
					con = com.track.gates.DbBean.getConnection();
					boolean flag = false;
					StringBuffer sql = new StringBuffer("select ID,ISNULL(PAYMENT_TERMS,'') PAYMENT_TERMS,ISNULL(NO_DAYS,'') NO_DAYS from ["+plant+"_FINPAYMENTTERMS] ");
					sql.append(" where PAYMENT_TERMS <>'NOPAYMENTTYPEDETAILS '");
					
					if (extraCon.length() > 0)
						sql.append(" and " + extraCon + " order by ID");
					else
						sql.append(" order by ID");
					this.mLogger.query(this.printQuery, sql.toString());
					System.out.println("MasterDAO  getPaymentTermDetails():"+ sql.toString());
					al = selectData(con, sql.toString());
					
				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					System.out.println("MasterDAO  getPaymentTermDetails():"+e.getMessage());
					throw e;
				} finally {
					if (con != null) {
						DbBean.closeConnection(con);
					}
				}
				
				return al;
			}
			
			public boolean deletePaymentTerm(java.util.Hashtable ht) throws Exception {
				MLogger.log(1, this.getClass() + " deletePaymentTerm()");
				boolean delete = false;
				java.sql.Connection con = null;
				try {
					con = DbBean.getConnection();
					StringBuffer sql = new StringBuffer(" DELETE ");
					sql.append(" ");
					sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "FINPAYMENTTERMS"
							+ "]");
					sql.append(" WHERE " + formCondition(ht));
					this.mLogger.query(this.printQuery, sql.toString());
					System.out.println("DeletePaymentTerm"+sql.toString());
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
			
			//**************************HSCODE***********************************************************************************************************/		
			public boolean isExistsHSCODE(String aHSCODE, String plant) throws Exception {
				PreparedStatement ps = null;
				ResultSet rs = null;
				boolean isExists = false;
				Connection con = null;
				try {
					con = DbBean.getConnection();
					String sQry = "SELECT COUNT(*) FROM " +  "[" + plant + "_" + "HSCODEMST" + "]" + " WHERE "
							+ IConstants.HSCODE + " = '"
							+ aHSCODE.toUpperCase() + "'";
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
		 
		 
			
			public boolean InsertHSCODE(Hashtable ht) throws Exception {
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
							+ "HSCODEMST" + "]" + "("
							+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
							+ VALUES.substring(0, VALUES.length() - 1) + ")";
					this.mLogger.query(this.printQuery, query);
					System.out.println("InsertHSCODE"+query);
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
			
			public boolean deleteHSCODE(java.util.Hashtable ht) throws Exception {
				MLogger.log(1, this.getClass() + " deletePaymentType()");
				boolean delete = false;
				java.sql.Connection con = null;
				try {
					con = DbBean.getConnection();
		          	StringBuffer sql = new StringBuffer(" DELETE ");
					sql.append(" ");
					sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "HSCODEMST"
							+ "]");
					sql.append(" WHERE " + formCondition(ht));
					this.mLogger.query(this.printQuery, sql.toString());
					System.out.println("DeleteHSCODE"+sql.toString());
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
			
			public ArrayList getHSCODEDetails(String plant,String extraCon) throws Exception {
				java.sql.Connection con = null;
				ArrayList al = new ArrayList();
				try {
					con = com.track.gates.DbBean.getConnection();
					boolean flag = false;
					StringBuffer sql = new StringBuffer("select ID,ISNULL(HSCODE,'') HSCODE from ["+plant+"_HSCODEMST] ");
					sql.append(" where HSCODE <>'NOHSCODEDETAILS '");
					
					if (extraCon.length() > 0)
						sql.append(" and " + extraCon + " order by ID");
					else
					  sql.append(" order by ID");
				    this.mLogger.query(this.printQuery, sql.toString());
				    System.out.println("MasterDAO  getHSCODEDetails():"+ sql.toString());
					al = selectData(con, sql.toString());

				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					System.out.println("MasterDAO  getHSCODEDetails():"+e.getMessage());
					throw e;
				} finally {
					if (con != null) {
						DbBean.closeConnection(con);
					}
				}

				return al;
			}
			
			
			//**************************COO***********************************************************************************************************/		
			public boolean isExistsCOO(String aCOO, String plant) throws Exception {
				PreparedStatement ps = null;
				ResultSet rs = null;
				boolean isExists = false;
				Connection con = null;
				try {
					con = DbBean.getConnection();
					String sQry = "SELECT COUNT(*) FROM " +  "[" + plant + "_" + "COOMST" + "]" + " WHERE "
							+ IConstants.COO + " = '"
							+ aCOO.toUpperCase() + "'";
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
		 
		 
			
			public boolean InsertCOO(Hashtable ht) throws Exception {
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
							+ "COOMST" + "]" + "("
							+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
							+ VALUES.substring(0, VALUES.length() - 1) + ")";
					this.mLogger.query(this.printQuery, query);
					System.out.println("InsertCOO"+query);
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
			
			public boolean deleteCOO(java.util.Hashtable ht) throws Exception {
				MLogger.log(1, this.getClass() + " deletePaymentType()");
				boolean delete = false;
				java.sql.Connection con = null;
				try {
					con = DbBean.getConnection();
		          	StringBuffer sql = new StringBuffer(" DELETE ");
					sql.append(" ");
					sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + "COOMST"
							+ "]");
					sql.append(" WHERE " + formCondition(ht));
					this.mLogger.query(this.printQuery, sql.toString());
					System.out.println("DeleteCOO"+sql.toString());
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
			
			public ArrayList getCOODetails(String plant,String extraCon) throws Exception {
				java.sql.Connection con = null;
				ArrayList al = new ArrayList();
				try {
					con = com.track.gates.DbBean.getConnection();
					boolean flag = false;
					StringBuffer sql = new StringBuffer("select ID,ISNULL(COO,'') COO from ["+plant+"_COOMST] ");
					sql.append(" where COO <>'NOCOODETAILS '");
					
					if (extraCon.length() > 0)
						sql.append(" and " + extraCon + " order by ID");
					else
					  sql.append(" order by ID");
				    this.mLogger.query(this.printQuery, sql.toString());
				    System.out.println("MasterDAO  getCOODetails():"+ sql.toString());
					al = selectData(con, sql.toString());

				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					System.out.println("MasterDAO  getCOODetails():"+e.getMessage());
					throw e;
				} finally {
					if (con != null) {
						DbBean.closeConnection(con);
					}
				}

				return al;
			}
			public boolean isExisitAccount(Hashtable ht, String extCond) throws Exception {
				boolean flag = false;
				java.sql.Connection con = null;
				try {
					con = com.track.gates.DbBean.getConnection();

					StringBuffer sql = new StringBuffer(" SELECT ");
					sql.append("COUNT(*) ");
					sql.append(" ");
					sql.append(" FROM " + "["+ht.get("PLANT")+"_ACCOUNT]");
					sql.append(" WHERE  " + formCondition(ht));

					if (extCond.length() > 0)
						sql.append(" and " + extCond);

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
			public boolean InsertAccount(Hashtable ht,String Acctid,String custName,String supName,String custCode,String user_id,int Acctype,int Accpart) throws Exception {
				boolean insertedInv = false;
				java.sql.Connection con = null;
				try {
					con = DbBean.getConnection();				
					
					
					String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_"
							+ "ACCOUNT" + "] (PLANT,ACCOUNT_NO,ACCOUNT_TITLE,ACCOUNT_TYPE,PARENT_ACCOUNT_NO,CUSTOMER_NO,VENDOR_NO,CRBY,CRAT ) "
							+ "VALUES ('"+ ht.get("PLANT") +"','"+Acctid+"','"+custName+"',"+Acctype+","+Accpart+",'"+custCode+"','"+supName+"',0,CURRENT_TIMESTAMP)";
					this.mLogger.query(this.printQuery, query);
					System.out.println("InsertAccount"+query);
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
			public boolean addShipment(Hashtable htShipment, String plant)throws Exception {
				boolean flag = false;
				Connection connection = null;
				PreparedStatement ps = null;
				List<String> args = null;
				String query = "";
				try {		
					/*Instantiate the list*/
					args = new ArrayList<String>();
					
					connection = DbBean.getConnection();
					
					
						String FIELDS = "", VALUES = "";
						Enumeration enumeration = htShipment.keys();
						for (int i = 0; i < htShipment.size(); i++) {
							String key = StrUtils.fString((String) enumeration.nextElement());
							String value = StrUtils.fString((String) htShipment.get(key));
							args.add(value);
							FIELDS += key + ",";
							VALUES += "?,";
						}		
						query = "INSERT INTO ["+ plant +"_SHIPMENT]  ("
						+ FIELDS.substring(0, FIELDS.length() - 1)
						+ ") VALUES ("
						+ VALUES.substring(0, VALUES.length() - 1) + ")";
						
						if(connection != null){
							/*Create  PreparedStatement object*/
							ps = connection.prepareStatement(query);
							this.mLogger.query(this.printQuery, query);
							flag = execute_NonSelectQuery(ps, args);
							if(flag){
								args.clear();
							}
						}
					
				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					throw e;
				} finally {
					if (connection != null) {
						DbBean.closeConnection(connection);
					}
				}
				return flag;
			}
			
			public Boolean updateShipment(String query, Hashtable htCondition, String extCond)
		            throws Exception {
		    boolean flag = false;
		    java.sql.Connection con = null;
		    try {
		            con = com.track.gates.DbBean.getConnection();
		            StringBuffer sql = new StringBuffer(" UPDATE " + "["
		                            + htCondition.get("PLANT") + "_SHIPMENT" + "]");
		            sql.append(" ");
		            sql.append(query);
		            
		            sql.append(" WHERE ");
		            String conditon = formCondition(htCondition);
		            sql.append(conditon);

		            if (extCond.length() != 0) {
		                    sql.append(extCond);
		            }
		            
		            	flag = updateData(con, sql.toString());
		            	
		                this.mLogger.query(this.printQuery, sql.toString());
					

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
			
			
			public boolean isExisitShipment(Hashtable ht, String extCond) throws Exception {
				boolean flag = false;
				java.sql.Connection con = null;
				try {
					con = com.track.gates.DbBean.getConnection();

					StringBuffer sql = new StringBuffer(" SELECT ");
					sql.append("COUNT(*) ");
					sql.append(" ");
					sql.append(" FROM " + "["+ht.get("PLANT")+"_SHIPMENT]");
					sql.append(" WHERE  " + formCondition(ht));

					if (extCond.length() > 0)
						sql.append(" and " + extCond);

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
			
			public ArrayList selectForReport(String query, Hashtable ht, String extCond)
					throws Exception {
//				boolean flag = false;
				ArrayList al = new ArrayList();
				java.sql.Connection con = null;
				try {
					con = com.track.gates.DbBean.getConnection();
					StringBuffer sql = new StringBuffer(query);
					String conditon = "";
					if (ht.size() > 0) {
						sql.append(" AND ");
						conditon = formCondition(ht);
						sql.append(" " + conditon);
					}

					if (extCond.length() > 0) {
						sql.append("  ");

						sql.append(" " + extCond);
					}

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
			
	public ArrayList getShipmentRefForBill(String plant, String pono, String extraCon) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			StringBuffer sql = new StringBuffer("select ID,ISNULL(SHIPMENT_CODE,'') SHIPMENT_CODE from ["+plant+"_FINEXPENSESHDR] ");
			sql.append(" WHERE PONO = '"+pono+"' AND STATUS='UNBILLED'");
			
			if (extraCon.length() > 0)
				sql.append(" and " + extraCon + " order by ID");
			else
			  sql.append(" order by ID");
		    this.mLogger.query(this.printQuery, sql.toString());
		    System.out.println("MasterDAO  getShipmentRefForBill():"+ sql.toString());
			al = selectData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			System.out.println("MasterDAO  getShipmentRefForBill():"+e.getMessage());
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return al;
	}
	
	public ArrayList getExpenseDetailForBill(String plant, String id) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			StringBuffer sql = new StringBuffer("SELECT EXPENSES_ACCOUNT,TAX_TYPE,AMOUNT,TOTAL_AMOUNT,CURRENCYTOBASE,BASETOORDERCURRENCY FROM ["+plant+"_FINEXPENSESHDR] a join ["+plant+"_FINEXPENSESDET] b  ");
			sql.append(" ON a.ID=b.EXPENSESHDRID  WHERE EXPENSESHDRID = '"+id+"' AND a.PLANT='"+plant+"'");
			
		    this.mLogger.query(this.printQuery, sql.toString());
		    System.out.println("MasterDAO  getExpenseDetailForBill():"+ sql.toString());
			al = selectData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			System.out.println("MasterDAO  getExpenseDetailForBill():"+e.getMessage());
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return al;
	}
	
	public ArrayList getExpenseTaxDetailForBill(String plant, String id) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			StringBuffer sql = new StringBuffer("SELECT TAX_TYPE,SUM(AMOUNT) AS TAX_TOTAL FROM ["+plant+"_FINEXPENSESDET]  ");
			sql.append(" WHERE EXPENSESHDRID = '"+id+"' AND PLANT='"+plant+"'  GROUP BY TAX_TYPE");
			
		    this.mLogger.query(this.printQuery, sql.toString());
		    System.out.println("MasterDAO  getExpenseTaxDetailForBill():"+ sql.toString());
			al = selectData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			System.out.println("MasterDAO  getExpenseTaxDetailForBill():"+e.getMessage());
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return al;
	}
	
	public ArrayList getShipmentDetail(String plant, String pono, String extraCon) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			StringBuffer sql = new StringBuffer("select ID,ISNULL(SHIPMENT_CODE,'') SHIPMENT_CODE from ["+plant+"_FINEXPENSESHDR] ");
			sql.append(" WHERE PONO = '"+pono+"' ");
			
			if (extraCon.length() > 0)
				sql.append(" and " + extraCon + " order by ID");
			else
			  sql.append(" order by ID");
		    this.mLogger.query(this.printQuery, sql.toString());
		    System.out.println("MasterDAO  getShipmentRefForBill():"+ sql.toString());
			al = selectData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			System.out.println("MasterDAO  getShipmentRefForBill():"+e.getMessage());
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return al;
	}
	public ArrayList getSalesLocationList(String CustName, String plant,String country) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "SELECT  COUNTRY,STATE,PREFIX  FROM "
					+ "FINSALESLOCATION "  
					+ "WHERE (COUNTRY LIKE '%" + country + "%' and STATE LIKE '%" + CustName + "%')"
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
	
	public ArrayList getSalesLocationListByCode(String CustName, String plant,String countrycode) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "SELECT  COUNTRY,STATE,PREFIX  FROM "
					+ "FINSALESLOCATION "  
					+ "WHERE COUNTRY = '"+countrycode+"' AND (STATE LIKE '%" + CustName + "%')"
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

	public ArrayList getSalesLocationName(String CustName, String plant,String country) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "SELECT  COUNTRY,STATE,PREFIX  FROM "
					+ "FINSALESLOCATION "  
					+ "WHERE (COUNTRY LIKE '%" + country + "%' and PREFIX ='" + CustName + "')"
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
	
	public ArrayList getExpenseDetailusingPonoandsno(String plant, String pono, String shipmentcode) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			StringBuffer sql = new StringBuffer("SELECT a.ID,EXPENSES_ACCOUNT,TAX_TYPE,AMOUNT,TAXAMOUNT,SUB_TOTAL,TOTAL_AMOUNT,CURRENCYTOBASE,BASETOORDERCURRENCY,a.STATUS FROM ["+plant+"_FINEXPENSESHDR] a join ["+plant+"_FINEXPENSESDET] b  ");
			sql.append(" ON a.ID=b.EXPENSESHDRID  WHERE SHIPMENT_CODE = '"+shipmentcode+"' AND a.PLANT='"+plant+"'AND PONO = '"+pono+"'");
			
		    this.mLogger.query(this.printQuery, sql.toString());
		    System.out.println("MasterDAO  getExpenseDetailForBill():"+ sql.toString());
			al = selectData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			System.out.println("MasterDAO  getExpenseDetailForBill():"+e.getMessage());
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return al;
	}
	
	public ArrayList getExpenseDetailusingbillanddnol(String plant, String bill, String shipmentcode) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			StringBuffer sql = new StringBuffer("SELECT a.ID,EXPENSES_ACCOUNT,TAX_TYPE,AMOUNT,TAXAMOUNT,SUB_TOTAL,TOTAL_AMOUNT,CURRENCYTOBASE,BASETOORDERCURRENCY,a.STATUS FROM ["+plant+"_FINEXPENSESHDR] a join ["+plant+"_FINEXPENSESDET] b  ");
			sql.append(" ON a.ID=b.EXPENSESHDRID  WHERE SHIPMENT_CODE = '"+shipmentcode+"' AND a.PLANT='"+plant+"'AND BILL = '"+bill+"'");
			
		    this.mLogger.query(this.printQuery, sql.toString());
		    System.out.println("MasterDAO  getExpenseDetailForBill():"+ sql.toString());
			al = selectData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			System.out.println("MasterDAO  getExpenseDetailForBill():"+e.getMessage());
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return al;
	}
	public ArrayList getTaxTreatmentList(String Taxtrt, String plant,String country) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "SELECT  COUNTRY,TAXTREATMENT  FROM "
					+ "FINTAXTREATMENT "  
					+ "WHERE COUNTRY LIKE '%" + country + "%' ";					
					if(Taxtrt.length()>0)
						sQry=sQry+"and TAXTREATMENT='" + Taxtrt + "'";
					
						sQry=sQry+ " ORDER BY ID ";
			
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
	public ArrayList getSalesLocationByState(String CustName, String plant,String country) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "SELECT  COUNTRY,STATE,PREFIX  FROM "
					+ "FINSALESLOCATION "  
					+ "WHERE (COUNTRY LIKE '%" + country + "%' and STATE='" + CustName + "')"
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
	public ArrayList getSalesLocationByPrefix(String CustName, String plant,String country) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "SELECT  COUNTRY,STATE,PREFIX  FROM "
					+ "FINSALESLOCATION "  
					+ "WHERE (COUNTRY LIKE '%" + country + "%' and (PREFIX LIKE '%" + CustName + "%' or STATE LIKE '%" + CustName + "%') )"
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

	public ArrayList getCountryList(String acountry, String plant,String region) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "SELECT COUNTRY_CODE,COUNTRYNAME FROM "
					+ plant+"_COUNTRYMASTER "  
					+ "WHERE PLANT='"+plant+"' ";
					if(acountry.length()>0)
						sQry=sQry+"and COUNTRYNAME='" + acountry + "'";
			
					if(region.length()>0)
						sQry=sQry+"and REGION='" + region + "'";
					
						sQry=sQry+ " ORDER BY ID ";
			
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
	public ArrayList getStateList(String astate, String plant,String acountry) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "SELECT COUNTRY_CODE,STATE FROM "
					+ plant+"_STATEMASTER "  
					+ "WHERE PLANT='"+plant+"' ";
					if(astate.length()>0)
						sQry=sQry+"and STATE='" + astate + "'";
					
					if(acountry.length()>0)
						sQry=sQry+"and COUNTRY_CODE='" + acountry + "'";
					
						sQry=sQry+ " ORDER BY ID ";
			
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
	public ArrayList getBankList(String abank, String plant) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "SELECT NAME,BRANCH_NAME,BRANCH FROM "
					+ plant+"_BANKMST "  
					+ "WHERE PLANT='"+plant+"' ";
					if(abank.length()>0)
						sQry=sQry+"and NAME='" + abank + "'";
					
						sQry=sQry+ " ORDER BY ID ";
			
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
	public ArrayList getRegionList(String region) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "SELECT REGION FROM REGIONMASTER";
					if(region.length()>0)
						sQry=sQry+"and REGION='" + region + "'";
					
						sQry=sQry+ " ORDER BY ID ";
			
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
	
	public ArrayList getItemMasterList(String plant,String extraCon) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			StringBuffer sql = new StringBuffer("select  distinct MODEL FROM ["+plant+"_ITEMMST]" );
			sql.append("where ISNULL(MODEL,'') <> '' ");
			if (extraCon.length() > 0)
				sql.append(" and " + extraCon );
		    this.mLogger.query(this.printQuery, sql.toString());
		    System.out.println("MasterDAO getItemMasterList():"+ sql.toString());
			al = selectData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			System.out.println("MasterDAO getItemMasterList():"+e.getMessage());
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return al;
	}
	
/*	public ArrayList getDepartmentList(String department, String plant) throws Exception {

		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();

			boolean flag = false;
			String sQry = "SELECT DEPARTMENT FROM "
					+ plant+"_HREMPDEPARTMENTMST "  
					+ "WHERE PLANT='"+plant+"' ";
					if(department.length()>0)
						sQry=sQry+"and DEPARTMENT='" + department + "'";					
						sQry=sQry+ " ORDER BY ID ";
			
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
	}*/
	public int getidbyDepartment(String plant, String dept) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    int id=0;
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_" +"HREMPDEPARTMENTMST"+"]"+"WHERE DEPARTMENT='"+ dept+"'";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrEmpDepartmentMst hrEmpType=new HrEmpDepartmentMst();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrEmpType);
	                   id = hrEmpType.getID();
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
		return id;
	}
	public ArrayList getDepartmentList(String department,
			String plant, String cond) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Hashtable ht= new Hashtable();
		Connection con = null;		
		try {
			con = DbBean.getConnection();

					String sQry = "SELECT DEPARTMENT FROM "
					+ "["
					+ plant
					+ "_"
					+ "HREMPDEPARTMENTMST"
					+ "]"
			+ " WHERE DEPARTMENT LIKE '" + department + "%'" + cond;
			arrList = selectForReport(sQry, ht, "");
			this.mLogger.query(this.printQuery, sQry);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}
	public ArrayList getBanknameList(String bankname,
			String plant, String cond) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Hashtable ht= new Hashtable();
		Connection con = null;		
		try {
			con = DbBean.getConnection();

					String sQry = "SELECT NAME,BRANCH_NAME FROM "
					+ "["
					+ plant
					+ "_"
					+ "BANKMST"
					+ "]"
			+ " WHERE NAME LIKE '" + bankname + "%'" + cond;
			arrList = selectForReport(sQry, ht, "");
			this.mLogger.query(this.printQuery, sQry);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}

	public ArrayList getBarcodePrint(String Print_Type,
			String plant, String cond) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Hashtable ht= new Hashtable();
		Connection con = null;		
		try {
			con = DbBean.getConnection();

					String sQry = "SELECT * FROM "
					+ "["
					+ plant
					+ "_"
					+ "BARCODEPRINTDETAILS"
					+ "]"
			+ " WHERE PRINTID =(SELECT max(printId) FROM "+plant+"_BARCODEPRINTDETAILS WHERE PAGE_TYPE = '"+Print_Type+"')" + cond;
			arrList = selectForReport(sQry, ht, "");
			this.mLogger.query(this.printQuery, sQry);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;

	}

	public ArrayList getBarcodeProductPrint(String Print_Type,
			String plant, String cond) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		Hashtable ht= new Hashtable();
		Connection con = null;		
		try {
			con = DbBean.getConnection();
			
			String sQry = "SELECT TOP 1 * FROM "
					+ "["
					+ plant
					+ "_"
					+ "BARCODEPRODUCTDETAILS"
					+ "]"
					+ " order by PRINTID DESC" + cond;
			arrList = selectForReport(sQry, ht, "");
			this.mLogger.query(this.printQuery, sQry);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return arrList;
		
	}
	
	public boolean InsertBarcode(Hashtable ht) throws Exception {
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
					+ "BARCODEDETAILS" + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, query);
			System.out.println("InsertBarcodeDetails"+query);
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
	
	public boolean InsertBarcodePrint(Hashtable ht) throws Exception {
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
					+ "BARCODEPRINTDETAILS" + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, query);
			System.out.println("InsertBarcodeDetails"+query);
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
	
	public boolean InsertManualBarcode(Hashtable ht) throws Exception {
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
					+ "BARCODEMANUALDETAILS" + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, query);
			System.out.println("InsertBarcodeDetails"+query);
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
	
	public boolean InsertProductBarcode(Hashtable ht) throws Exception {
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
					+ "BARCODEPRODUCTDETAILS" + "]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";
			this.mLogger.query(this.printQuery, query);
			System.out.println("InsertBarcodeDetails"+query);
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
	
	public Map selectRow(String query, Hashtable ht, String TABLE_NAME) throws Exception {
		Map map = new HashMap();

		java.sql.Connection con = null;
		try {

			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
					+ TABLE_NAME);
			sql.append(" WHERE ");
			String conditon = formCondition(ht);
			sql.append(conditon);
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
	

	public String GetTaxType(String Plant)
	{	
		String TAXTYPE = "";
		try {		

		Hashtable ht = new Hashtable();
		ht.put("plant", Plant);

		String query = " ISNULL(TAXBYLABELORDERMANAGEMENT,'VAT') AS TAXTYPE ";
		this.mLogger.query(this.printQuery, query);
		Map m = selectRow(query, ht,"PLNTMST");

		TAXTYPE = (String) m.get("TAXTYPE");
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);			
		}
		return TAXTYPE;
	}
	
	public boolean isExisitBarcode(Hashtable ht, String extCond) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + "["+ht.get("PLANT")+"_BARCODEDETAILS]");
			sql.append(" WHERE  " + formCondition(ht));

			if (extCond.length() > 0)
				sql.append(" and " + extCond);

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
	
	public ArrayList getProductAssignedSupplier(String plant,String item) throws Exception {
		java.sql.Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = com.track.gates.DbBean.getConnection();
			boolean flag = false;
			String sQry = "SELECT VENDNO FROM "
					+ plant+"_ITEM_SUPPLIER "  
					+ "WHERE PLANT='"+plant+"' AND ITEM ='"+item+"' ";
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


}
