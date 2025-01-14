package com.track.dao;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;

import com.track.util.MLogger;
import com.track.util.StrUtils;


import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class EmailMsgDAO extends BaseDAO {
    StrUtils _StrUtils = null;
    private MLogger mLogger = new MLogger();
    private boolean printQuery = MLoggerConstant.EmailMsgDAO_PRINTPLANTMASTERLOG;
    private boolean printLog = MLoggerConstant.EmailMsgDAO_PRINTPLANTMASTERQUERY;

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

    public EmailMsgDAO() {
            _StrUtils = new StrUtils();
    }
    public boolean updateEmailMessage(String query, Hashtable<String, String> htCondition, String extCond)
    throws Exception {
            boolean flag = false;
            java.sql.Connection con = null;
            try {
            con = com.track.gates.DbBean.getConnection();
            StringBuffer sql = new StringBuffer(" UPDATE " + "["
                            + htCondition.get("PLANT") + "_EMAIL_MSG_DET]");
                            sql.append(" ");
                            sql.append(query);
            
                            sql.append(" WHERE ");
                            String conditon = formCondition(htCondition);
                            sql.append(conditon);
            
            
            
            if (extCond.length() != 0) {
                    sql.append(extCond);
            }
            this.mLogger.query(this.printQuery, sql.toString());
            flag = updateData(con, sql.toString());
            
            
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
    
    
    public Map<String, String> getEmailMsgDetails(String plant,String ModuleType) throws Exception {

    MLogger.log(1, this.getClass() + " getEmailMsgDetails()");
    Map<String, String> m = new HashMap<>();
    java.sql.Connection con = null;
    
    try {
            
            con = DbBean.getConnection();
            //Start modified by Bruhan for Email Notification on 11 July 2012.
            StringBuffer sql = new StringBuffer("  SELECT  ");
            sql.append("ISNULL(EMAIL_FROM,'') as EMAIL_FROM,ISNULL(EMAIL_TO,'') as EMAIL_TO,ISNULL(SUBJECT,'') AS SUBJECT,ISNULL(BODY1,'') AS BODY1 ,ISNULL(ORDERNO,'') AS ORDERNO,ISNULL(BODY2,'') AS BODY2,ISNULL(WEB_LINK,'') AS WEB_LINK,");
            sql.append("ISNULL(IS_CC_CHECKED,'N') AS IS_CC_CHECKED,ISNULL(CC_EMAILTO,'') AS CC_EMAILTO,ISNULL(CC_SUBJECT,'') AS CC_SUBJECT,ISNULL(CC_BODY1,'') AS CC_BODY1 ,ISNULL(CC_BODY2,'') AS CC_BODY2,ISNULL(CC_WEB_LINK,'') AS CC_WEB_LINK,ISNULL(ISAUTOEMAIL,'N') AS ISAUTOEMAIL,");
            sql.append("ISNULL(send_attachment,'') AS send_attachment, ISNULL(UPONCREATION,'0') AS UPONCREATION,ISNULL(CONVERTFROMEST,'0') AS CONVERTFROMEST,ISNULL(UPONAPPROVE,'0') AS UPONAPPROVE,ISNULL(EXPIRYIN,'0') AS EXPIRYIN,ISNULL(TIME,'00:00') AS TIME  ");
            sql.append(" ");
            sql.append(" FROM " + "[" + plant + "_"  + "EMAIL_MSG_DET] where plant='"+plant+"' and MODULE ='"+ModuleType+"'");
           //End modified by Bruhan for Email Notification on 11 July 2012.
            System.out.println(sql.toString());
            this.mLogger.query(this.printQuery, sql.toString());
            m = getRowOfData(con, sql.toString());

    } catch (Exception e) {

            this.mLogger.exception(this.printLog, "", e);
            throw e;
    } finally {
            if (con != null) {
                    DbBean.closeConnection(con);
            }
    }
    return m;

    }
    /**
	 * Author Bruhan. 11 july 2012
	 * method : getIsAutoEmailDetails(String plant,String ModuleType) description : Retrieves whether
	 * autoemail is set or not)
	 * 
	 * @param : String plant,String ModuleType
	 * @return : String - Y / N
	 * @throws Exception
	 */
    public String getIsAutoEmailDetails(String plant,String ModuleType) throws Exception {

        MLogger.log(1, this.getClass() + " getIsAutoEmailDetails()");
        java.sql.Connection con = null;
        String isAutoEmail = "";
        
        try {                
                con = DbBean.getConnection();
                Statement stmt = con.createStatement();
                String sql = "SELECT ISNULL(ISAUTOEMAIL,'N') AS ISAUTOEMAIL FROM " + "[" + plant + "_"  + "EMAIL_MSG_DET] where plant='"+plant+"' and MODULE ='"+ModuleType+"'" ;  
                
                System.out.println(sql.toString());
                this.mLogger.query(this.printQuery, sql.toString());
                ResultSet rs = stmt.executeQuery(sql);
    			int n = 1;
    			while (rs.next()) {
    				isAutoEmail = rs.getString("ISAUTOEMAIL");

        } }catch (Exception e) {

                this.mLogger.exception(this.printLog, "", e);
                throw e;
        } finally {
                if (con != null) {
                        DbBean.closeConnection(con);
                }
        }
        return isAutoEmail;

        }
    public ArrayList getConfigApprovalDetails(String plant,String Type,String UserType,String UserId) throws Exception {

        MLogger.log(1, this.getClass() + " getConfigApprovalDetails()");
        ArrayList arrList = new ArrayList();
        java.sql.Connection con = null;
        
        try {
                
                con = DbBean.getConnection();
                //Start modified by Azees for ConfigApproval on 15 Oct 2019.
                StringBuffer sql = new StringBuffer("  SELECT  ");
                sql.append("ISNULL(EMAIL,'') as EMAIL,ISNULL(USERID,'') as USERID,ISNULL(ORDERTYPE,'') AS ORDERTYPE,ISNULL(USERTYPE,'') AS USERTYPE,");
                sql.append("ISNULL(ISACTIVE,'N') AS ISACTIVE,ID");
                sql.append(" ");
                sql.append(" FROM " + "[" + plant + "_"  + "CONFIGAPPROVAL] where plant='"+plant+"' and ORDERTYPE ='"+Type+"' and USERTYPE ='"+UserType+"'");
                if(UserId.length()>0)
                	sql.append(" and USERID ='"+UserId+"'");
                	
               //End modified by Azees for ConfigApproval on 15 Oct 2019.
                System.out.println(sql.toString());
                this.mLogger.query(this.printQuery, sql.toString());
                Hashtable ht = new Hashtable();
                arrList = selectForReport(sql.toString(),ht,"");

        } catch (Exception e) {

                this.mLogger.exception(this.printLog, "", e);
                throw e;
        } finally {
                if (con != null) {
                        DbBean.closeConnection(con);
                }
        }
        return arrList;

        }
    public ArrayList selectForReport(String query, Hashtable ht, String extCond)
			throws Exception {

		boolean flag = false;
		ArrayList al = new ArrayList();
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(query);
			String conditon = "";
			if (ht.size() > 0) {
				conditon = formCondition(ht);
			}

			if (extCond.length() > 0) {
				sql.append("  ");

				sql.append(" " + extCond);
			}

			this.mLogger.query(this.printQuery, sql.toString());
			al = selectData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.log(0, "" + e.getMessage());
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}

		return al;
	}
    //Added by Azees for ConfigApprovalEmail on 29 Oct 2019.
    public ArrayList getConfigApprovalEmailDetails(String plant,String Type,String EmailType) throws Exception {

        MLogger.log(1, this.getClass() + " getConfigApprovalEmailDetails()");
        ArrayList arrList = new ArrayList();
        java.sql.Connection con = null;
        
        try {
                
                con = DbBean.getConnection();
                //Start modified by Azees for ConfigApproval on 29 Oct 2019.
                StringBuffer sql = new StringBuffer("  SELECT  ");
                sql.append("ID,ISNULL(EMAIL_TO,'') as EMAIL_TO,ISNULL(ATTACHMENT,'') as ATTACHMENT,ISNULL(ATTACHMENT_TO,'') as ATTACHMENT_TO,ISNULL(ORDERTYPE,'') AS ORDERTYPE,ISNULL(EMAILTYPE,'') AS EMAILTYPE,");
                sql.append("ISNULL(ISAUTOEMAIL,'N') AS ISAUTOEMAIL");
                sql.append(" ");
                sql.append(" FROM " + "[" + plant + "_"  + "CONFIGAPPROVALEMAIL] where plant='"+plant+"' and ORDERTYPE ='"+Type+"' and EMAILTYPE ='"+EmailType+"'");
                
                	
               //End modified by Azees for ConfigApproval on 29 Oct 2019.
                System.out.println(sql.toString());
                this.mLogger.query(this.printQuery, sql.toString());
                Hashtable ht = new Hashtable();
                arrList = selectForReport(sql.toString(),ht,"");

        } catch (Exception e) {

                this.mLogger.exception(this.printLog, "", e);
                throw e;
        } finally {
                if (con != null) {
                        DbBean.closeConnection(con);
                }
        }
        return arrList;

        }
    public boolean updateConfigApproval(String query, Hashtable htCondition, String extCond)
    	    throws Exception {
    	            boolean flag = false;
    	            java.sql.Connection con = null;
    	            try {
    	            con = com.track.gates.DbBean.getConnection();
    	            StringBuffer sql = new StringBuffer(" UPDATE " + "["
    	                            + htCondition.get("PLANT") + "_CONFIGAPPROVAL]");
    	                            sql.append(" ");
    	                            sql.append(query);
    	            
    	                            sql.append(" WHERE ");
    	                            String conditon = formCondition(htCondition);
    	                            sql.append(conditon);
    	            
    	            
    	            
    	            if (extCond.length() != 0) {
    	                    sql.append(extCond);
    	            }
    	            System.out.println("sql.toString():::"+sql.toString());
    	            this.mLogger.query(this.printQuery, sql.toString());
    	            flag = updateData(con, sql.toString());
    	            
    	            
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
    public boolean insConfigApproval(Hashtable ht) throws Exception {

		boolean insertFlag = false;
		java.sql.Connection conn = null;
		try {
			conn = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enumeration
						.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO " + "["+ht.get("PLANT")+"_CONFIGAPPROVAL]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			this.mLogger.query(this.printQuery, query);
			insertFlag = insertData(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}

		return insertFlag;
	}
    public boolean deleteConfigApproval(String query,String PLANT)
    	    throws Exception {
    	            boolean flag = false;
    	            java.sql.Connection con = null;
    	            try {
    	            con = com.track.gates.DbBean.getConnection();
    	            StringBuffer sql = new StringBuffer(" DELETE FROM " + "["
    	                            + PLANT + "_CONFIGAPPROVAL]");
    	                            sql.append(" ");
    	                            
    	            
    	                            sql.append(" WHERE PLANT='"+PLANT+"' ");
    	                            sql.append(query);
    	            
    	            
    	            System.out.println("sql.toString():::"+sql.toString());
    	            this.mLogger.query(this.printQuery, sql.toString());
    	            flag = updateData(con, sql.toString());
    	            
    	            
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
    public boolean insConfigApprovalEmail(Hashtable ht) throws Exception {

		boolean insertFlag = false;
		java.sql.Connection conn = null;
		try {
			conn = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enumeration
						.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO " + "["+ht.get("PLANT")+"_CONFIGAPPROVALEMAIL]" + "("
					+ FIELDS.substring(0, FIELDS.length() - 1) + ") VALUES ("
					+ VALUES.substring(0, VALUES.length() - 1) + ")";

			this.mLogger.query(this.printQuery, query);
			insertFlag = insertData(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}

		return insertFlag;
	}
    public boolean updateConfigApprovalEmail(String query, Hashtable htCondition, String extCond)
    	    throws Exception {
    	            boolean flag = false;
    	            java.sql.Connection con = null;
    	            try {
    	            con = com.track.gates.DbBean.getConnection();
    	            StringBuffer sql = new StringBuffer(" UPDATE " + "["
    	                            + htCondition.get("PLANT") + "_CONFIGAPPROVALEMAIL]");
    	                            sql.append(" ");
    	                            sql.append(query);
    	            
    	                            sql.append(" WHERE ");
    	                            String conditon = formCondition(htCondition);
    	                            sql.append(conditon);
    	            
    	            
    	            
    	            if (extCond.length() != 0) {
    	                    sql.append(extCond);
    	            }
    	            System.out.println("sql.toString():::"+sql.toString());
    	            this.mLogger.query(this.printQuery, sql.toString());
    	            flag = updateData(con, sql.toString());
    	            
    	            
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
}
