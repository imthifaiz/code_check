package com.track.db.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.transaction.UserTransaction;

import org.apache.commons.lang.RandomStringUtils;

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.BaseDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.PlantMstDAO;
import com.track.gates.DbBean;
import com.track.gates.encryptBean;
import com.track.gates.userBean;
import com.track.tranaction.SendEmail;
import com.track.util.MLogger;

import com.track.util.StrUtils;
import com.track.util.XMLUtils;
import com.track.util.mail.SMTPClient;

import java.util.Map;

public class PlantMstUtil {
	public PlantMstUtil() {

	}
	private boolean printLog = MLoggerConstant.PlantMstUtil_PRINTPLANTMASTERLOG;
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public boolean insertPlantMst(Hashtable ht, String user) throws Exception {
		boolean inserted = false;
		UserTransaction ut = null;

		try {
			PlantMstDAO plantmst = new PlantMstDAO();
			plantmst.setmLogger(mLogger);
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();

			inserted = plantmst.insertPlnMst(ht, user);

			if (inserted) {
				DbBean.CommitTran(ut);
			} else {
				DbBean.RollbackTran(ut);
			}

		} catch (Exception e) {
			DbBean.RollbackTran(ut);
			throw e;
		}
		return inserted;
	}

	public boolean deletePlantMst(Hashtable ht, String query, String extcon)
			throws Exception {
		boolean updated = false;
		try {
			PlantMstDAO plantmst = new PlantMstDAO();
			plantmst.setmLogger(mLogger);
			updated = plantmst.update(query, ht, extcon);
		} catch (Exception e) {
			throw e;
		}
		return updated;
	}

	public boolean deletePlantMst(Hashtable ht) throws Exception {
		boolean deleted = false;
		try {
			PlantMstDAO plantmst = new PlantMstDAO();
			plantmst.setmLogger(mLogger);
			deleted = plantmst.deletePlntMst(ht);
		} catch (Exception e) {
			throw e;
		}
		return deleted;
	}
	

	public ArrayList getPlantMstDetails(String plant) {
		ArrayList al = null;
		PlantMstDAO plantmst = new PlantMstDAO();
		plantmst.setmLogger(mLogger);
		try {
			al = plantmst.getPlantMstDetails(plant);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return al;
	}
	
	public ArrayList getPlantMstDetailForCrm(String plant) {
		ArrayList al = null;
		PlantMstDAO plantmst = new PlantMstDAO();
		plantmst.setmLogger(mLogger);
		try {
			al = plantmst.getPlantMstDetailForCrm(plant);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return al;
	}
	
	public ArrayList getContactHdr(String plant) {
		ArrayList al = null;
		PlantMstDAO plantmst = new PlantMstDAO();
		plantmst.setmLogger(mLogger);
		try {
			al = plantmst.getContactHdr(plant);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return al;
	}
	
	public ArrayList getsalesorderdeliverysummary(String plant,String FROM_DATE,String TO_DATE,String dono,String invoice,String item,String status,String shipped,String deliverystatus) {
		ArrayList al = null;
		PlantMstDAO plantmst = new PlantMstDAO();
		plantmst.setmLogger(mLogger);
		try {
			al = plantmst.getsalesorderdeliverysummary(plant,FROM_DATE,TO_DATE,dono,invoice,item,status,shipped,deliverystatus);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return al;
	}
	
	public ArrayList getsalesorderdeliveryShippingsummary(String plant,String FROM_DATE,String TO_DATE,String dono,String invoice,String item,String status,String shipped,String intransit,String deliverystatus) {
		ArrayList al = null;
		PlantMstDAO plantmst = new PlantMstDAO();
		plantmst.setmLogger(mLogger);
		try {
			al = plantmst.getsalesorderdeliveryShippingsummary(plant,FROM_DATE,TO_DATE,dono,invoice,item,status,shipped,intransit,deliverystatus); 
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return al;
	}
	
	public ArrayList getContactDet(String plant) {
		ArrayList al = null;
		PlantMstDAO plantmst = new PlantMstDAO();
		plantmst.setmLogger(mLogger);
		try {
			al = plantmst.getContactDet(plant);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return al;
	}
	
	
	public ArrayList getContactHdr(String plant,String ID) {
		ArrayList al = null;
		PlantMstDAO plantmst = new PlantMstDAO();
		plantmst.setmLogger(mLogger);
		try {
			al = plantmst.getContactHdr(plant,ID);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return al;
	}
	
	public ArrayList getContactDet(String plant,String ID) {
		ArrayList al = null;
		PlantMstDAO plantmst = new PlantMstDAO();
		plantmst.setmLogger(mLogger);
		try {
			al = plantmst.getContactDet(plant,ID);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return al;
	}
	public boolean isExistsContactName(Hashtable htCT) throws Exception {
		boolean isExists = false;
		PlantMstDAO plantmst = new PlantMstDAO();
		plantmst.setmLogger(mLogger);
		try {
			isExists = plantmst.isExistsContactName(htCT);
		} catch (Exception e) {
			throw e;
		}
		return isExists;
	}
	
	 public boolean insertContactHdr(Hashtable ht) throws Exception {
			boolean inserted = false;
			PlantMstDAO plantmst = new PlantMstDAO();
			plantmst.setmLogger(mLogger);
			try {
				inserted = plantmst.insertIntoContactHdr(ht);
			} catch (Exception e) {
				throw e;
			}
			return inserted;
		}
	 
		public int insertContactHdr(Hashtable ht, String plant) {
			PlantMstDAO plantmst = new PlantMstDAO();
			int contacthdrID = 0;
			try {
				contacthdrID = plantmst.addContactHdr(ht, plant);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return contacthdrID;		
		}
		
	 public boolean insertContactDet(Hashtable ht) throws Exception {
		 boolean inserted = false;
		 PlantMstDAO plantmst = new PlantMstDAO();
		 plantmst.setmLogger(mLogger);
		 try {
			 inserted = plantmst.insertIntoContactDet(ht);
		 } catch (Exception e) {
			 throw e;
		 }
		 return inserted;
	 }
	 
	public ArrayList getPlantMstDetailsForMaintenanceExpDate(String plant) {
		ArrayList al = null;
		PlantMstDAO plantmst = new PlantMstDAO();
		plantmst.setmLogger(mLogger);
		try {
			al = plantmst.getPlantMstDetailsForMaintenanceExpDate(plant);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return al;
	}
	

	public ArrayList getPlantMstList(Hashtable ht, String aPlant,
			String afrmDate, String atoDate) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
			PlantMstDAO plantmst = new PlantMstDAO();
			plantmst.setmLogger(mLogger);
			if (afrmDate.length() > 0) {
				sCondition = sCondition + " AND STARTDATE  >= '" + afrmDate
						+ "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND EXPIRYDATE <= '" + atoDate
							+ "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND EXPIRYDATE  <= '" + atoDate
							+ "'  ";
				}
			}
			String queryPlant = "0";

			StringBuffer aQuery = new StringBuffer(
					"SELECT PLANT,ISNULL(PLNTDESC,'') PLNTDESC, CONVERT(VARCHAR(20), CONVERT(DATETIME, STARTDATE), 103) AS STARTDATE,");
			aQuery
					.append("CONVERT(VARCHAR(20), CONVERT(DATETIME, EXPIRYDATE), 103) AS EXPIRYDATE, ");
			aQuery
					.append("CONVERT(VARCHAR(20),DATEADD(MONTH, 1, EXPIRYDATE),103) AS ACTEXPIRYDATE,ISNULL(NAME,'') NAME,ISNULL(DESGINATION,'')DESGINATION, ");
			aQuery
					.append("ISNULL(EMAIL,'')EMAIL,ISNULL(TELNO,'')TELNO,ISNULL(HPNO,'') HPNO,ISNULL(ADD1,'') ADD1,ISNULL(ADD2,'') ADD2, ");
			aQuery
					.append("ISNULL(ADD3,'') ADD3,ADD4,COUNTY,ZIP,USERFLD1 AS REMARKS,USERFLD2 AS FAX FROM PLNTMST WHERE PLANT =  '"
							+ aPlant + "'");
			aQuery.append(sCondition);

			aQuery.append(" order by plant");
			arrList = plantmst.selectForReport(aQuery.toString(), ht);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}

	public ArrayList getUnauthPlantList(Hashtable ht, String aPlant,
			String afrmDate, String atoDate) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
			PlantMstDAO plantmst = new PlantMstDAO();
			plantmst.setmLogger(mLogger);
			String queryPlant = "0";

			StringBuffer aQuery = new StringBuffer(
					"SELECT PLANT,AUTHSTAT,ISNULL(PLNTDESC,'') PLNTDESC, CONVERT(VARCHAR(20), CONVERT(DATETIME, STARTDATE), 103) AS STARTDATE,");
			aQuery
					.append("CONVERT(VARCHAR(20), CONVERT(DATETIME, EXPIRYDATE), 103) AS EXPIRYDATE, ");
			aQuery
					.append("CONVERT(VARCHAR(20),DATEADD(MONTH, 1, EXPIRYDATE),103) AS ACTEXPIRYDATE,ISNULL(NAME,'') NAME,ISNULL(DESGINATION,'')DESGINATION, ");
			aQuery
					.append("ISNULL(EMAIL,'')EMAIL,ISNULL(TELNO,'')TELNO,ISNULL(HPNO,'') HPNO,ISNULL(ADD1,'') ADD1,ISNULL(ADD2,'') ADD2, ");
			aQuery
					.append("ISNULL(ADD3,'') ADD3,ADD4,COUNTY,ZIP,USERFLD1 AS REMARKS,USERFLD2 AS FAX,ISNULL(SALES_CHARGE_BY,'') AS SALES_CHARGE_BY,ISNULL(SALES_PERCENT,0) AS SALES_PERCENT,ISNULL(SALES_FR_DOLLARS,0) AS SALES_FR_DOLLARS,ISNULL(SALES_FR_CENTS,0) AS SALES_FR_CENTS,ISNULL(ENQUIRY_FR_DOLLARS,0) AS ENQUIRY_FR_DOLLARS,ISNULL(ENQUIRY_FR_CENTS,0) AS ENQUIRY_FR_CENTS FROM PLNTMST WHERE authstat =  '" + "0" + "'");
			aQuery.append(sCondition);

			aQuery.append(" order by plant");
			arrList = plantmst.selectForReport(aQuery.toString(), ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}

//2.

	public ArrayList getauthPlantList(Hashtable ht, String aPlant,
			String afrmDate, String atoDate) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
			PlantMstDAO plantmst = new PlantMstDAO();
			plantmst.setmLogger(mLogger);
			if (afrmDate.length() > 0) {
				sCondition = sCondition + " AND STARTDATE  >= '" + afrmDate
						+ "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND EXPIRYDATE <= '" + atoDate
							+ "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND EXPIRYDATE  <= '" + atoDate
							+ "'  ";
				}
			}
			String queryPlant = "0";

			StringBuffer aQuery = new StringBuffer(
					"SELECT PLANT,AUTHSTAT,ISNULL(PLNTDESC,'') PLNTDESC, CONVERT(VARCHAR(20), CONVERT(DATETIME, STARTDATE), 103) AS STARTDATE,");
			aQuery
					.append("CONVERT(VARCHAR(20), CONVERT(DATETIME, EXPIRYDATE), 103) AS EXPIRYDATE, ");
			aQuery
					.append("CONVERT(VARCHAR(20),DATEADD(MONTH, 1, EXPIRYDATE),103) AS ACTEXPIRYDATE,ISNULL(NAME,'') NAME,ISNULL(DESGINATION,'')DESGINATION, ");
			aQuery
					.append("ISNULL(EMAIL,'')EMAIL,ISNULL(TELNO,'')TELNO,ISNULL(HPNO,'') HPNO,ISNULL(ADD1,'') ADD1,ISNULL(ADD2,'') ADD2, ");
			//Start code modified by Bruhan for base Currency inclusion on Aug 15 2012 
			aQuery
					.append("ISNULL(ADD3,'') ADD3,ADD4,COUNTY,ZIP,USERFLD1 AS REMARKS,USERFLD2 AS FAX,SALES_CHARGE_BY,SALES_PERCENT,SALES_FR_DOLLARS,SALES_FR_CENTS,ENQUIRY_FR_DOLLARS,ENQUIRY_FR_CENTS,ISNULL(NUMBER_OF_CATALOGS,'') NUMBER_OF_CATALOGS , BASE_CURRENCY,ISNULL(NUMBER_OF_SIGNATURES,'') NUMBER_OF_SIGNATURES," +
							"ISNULL(TAXBY,'') TAXBY,ISNULL(STATE,'') STATE,ISNULL(TAXBYLABEL,'') TAXBYLABEL FROM PLNTMST WHERE PLANT =  '"
							+ aPlant + "'" + " and AUTHSTAT=1");
			//End code modified by Bruhan for base Currency inclusion on Aug 15 2012 
			aQuery.append(sCondition);

			aQuery.append(" order by plant");
			arrList = plantmst.selectForReport(aQuery.toString(), ht);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;

	}
	public ArrayList getClearPlantMstList(Hashtable ht, String aPlant,
			String afrmDate, String atoDate) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
			PlantMstDAO plantmst = new PlantMstDAO();
			plantmst.setmLogger(mLogger);
			if (afrmDate.length() > 0) {
				sCondition = sCondition + " AND STARTDATE  >= '" + afrmDate
						+ "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND EXPIRYDATE <= '" + atoDate
							+ "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND EXPIRYDATE  <= '" + atoDate
							+ "'  ";
				}
			}
			String queryPlant = "0";

			StringBuffer aQuery = new StringBuffer(
					"SELECT PLANT,ISNULL(PLNTDESC,'') PLNTDESC, CONVERT(VARCHAR(20), CONVERT(DATETIME, STARTDATE), 103) AS STARTDATE,");
			aQuery
					.append("CONVERT(VARCHAR(20), CONVERT(DATETIME, EXPIRYDATE), 103) AS EXPIRYDATE, ");
			aQuery
					.append("CONVERT(VARCHAR(20),DATEADD(MONTH, 1, EXPIRYDATE),103) AS ACTEXPIRYDATE,ISNULL(NAME,'') NAME,ISNULL(DESGINATION,'')DESGINATION, ");
			aQuery
					.append("ISNULL(EMAIL,'')EMAIL,ISNULL(TELNO,'')TELNO,ISNULL(HPNO,'') HPNO,ISNULL(ADD1,'') ADD1,ISNULL(ADD2,'') ADD2, ");
			aQuery
					.append("ISNULL(ADD3,'') ADD3,ADD4,COUNTY,ZIP,USERFLD1 AS REMARKS,USERFLD2 AS FAX FROM PLNTMST WHERE PLANT =  '"
							+ aPlant + "'");
			aQuery.append(sCondition);

			aQuery.append(" order by plant");
			arrList = plantmst.selectForReport(aQuery.toString(), ht);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}
        
    public ArrayList getCompanyDetails(String plant) {
            ArrayList al = null;
            PlantMstDAO plantmst = new PlantMstDAO();
            plantmst.setmLogger(mLogger);
            try {
                    al = plantmst.getCompanyDetails(plant);

            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
            }
            return al;
    }
    
    public String getEnquiryFlatrate(String aPlant) throws Exception {
        PlantMstDAO plantmst = new PlantMstDAO();
        plantmst.setmLogger(mLogger);
        String flatRate = "";
        Hashtable ht = new Hashtable();
        ht.put("PLANT", aPlant);
        String query = " CAST( ENQUIRY_FR_DOLLARS AS VARCHAR)+'.'+ CAST(ENQUIRY_FR_CENTS AS VARCHAR)  AS  FLATRATE ";
        Map m = plantmst.selectRow(query, ht);
        flatRate = (String) m.get("FLATRATE");
        return flatRate;
    }
    
    public String getEmailAddresstoCopyTo(String aPlant) throws Exception {
        PlantMstDAO plantmst = new PlantMstDAO();
        plantmst.setmLogger(mLogger);
        String email = "";
        Hashtable ht = new Hashtable();
        ht.put("PLANT", aPlant);
        String query = " ISNULL(EMAIL,'')EMAIL ";
        Map m = plantmst.selectRow(query, ht);
        email = (String) m.get("EMAIL");
        return email;
    }
    
    
    public String checkPlant(String aPlant)
    		throws Exception {
    			String result = "";
    			ArrayList<Map<String, String>> alData = new ArrayList<>();
    			java.sql.Connection con = null;
    			        UserLocUtil userLocUtil = new UserLocUtil();
    			        userLocUtil.setmLogger(mLogger);
    			         StringBuffer sql = new StringBuffer("SELECT ISNULL(PLANT,'') PLANT from "
    					+  "PLNTMST WHERE PLANT='" + aPlant + "' ");
    			try {
    				con = DbBean.getConnection();
    				PlantMstDAO itM = new PlantMstDAO();
    				this.mLogger.query(itM.isPrintQuery(), sql.toString());
    			    System.out.println("plnat"+sql.toString());
    				alData = itM.selectData(con, sql.toString());
    				if (alData.size() > 0) {
    					result = XMLUtils.getXMLHeader();
    					result += XMLUtils.getStartNode("plantDetail total='"
    							+ alData.size() + "'");
    			
    					for (Map<String, String> hashMap : alData) {
    						result += XMLUtils.getStartNode("record");
    						result += XMLUtils.getXMLNode("plant", hashMap.get("PLANT"));
    						result += XMLUtils.getEndNode("record");
    					}
    					result += XMLUtils.getEndNode("plantDetail");
    				}
    				return result;
    			} catch (Exception e) {
    				this.mLogger.exception(this.printLog, "", e);
    				throw e;
    			} finally {
    				DbBean.closeConnection(con);
    			}
    			
    	   }
 public int getSignatureCount(Hashtable ht,String extcondtn) throws Exception {
   	 int noofrecords=0;
   	PlantMstDAO itM = new PlantMstDAO();
   	itM.setmLogger(mLogger);
    noofrecords=itM.getSignatureCount(ht, extcondtn);
       return noofrecords;
   }

 public ArrayList validateUser(String userid,String pwd)
			throws Exception {
		ArrayList al = new ArrayList();
		PlantMstDAO plantmst = new PlantMstDAO();
	        plantmst.setmLogger(mLogger);
		try {
			plantmst .setmLogger(mLogger);
			al = plantmst.validateUser(userid,pwd);
		} catch (Exception e) {

			throw e;
		}
		return al;
	}
 
 public boolean isInventoryModuleEnabled(String plant) throws Exception{
	 PlantMstDAO plantmst = new PlantMstDAO();
     plantmst.setmLogger(mLogger);
     return plantmst.isInventoryModuleEnabled(plant);
 }
 
 public boolean isAccountingModuleEnabled(String plant) throws Exception{
	 PlantMstDAO plantmst = new PlantMstDAO();
     plantmst.setmLogger(mLogger);
     return plantmst.isAccountingModuleEnabled(plant);
 }

 public boolean isPayrollModuleEnabled(String plant) throws Exception{
	 PlantMstDAO plantmst = new PlantMstDAO();
	 plantmst.setmLogger(mLogger);
	 return plantmst.isPayrollModuleEnabled(plant);
 }
 
 public ArrayList validateUser(String userid)
			throws Exception {
		ArrayList al = new ArrayList();
		PlantMstDAO plantmst = new PlantMstDAO();
	        plantmst.setmLogger(mLogger);
		try {
			plantmst .setmLogger(mLogger);
			al = plantmst.validateUser(userid);
		} catch (Exception e) {

			throw e;
		}
		return al;
	}
 
	public String sendUserPassword(String userid) throws Exception {
		ArrayList al = new ArrayList();
		PlantMstDAO plantmst = new PlantMstDAO();
		plantmst.setmLogger(mLogger);
		UserTransaction ut = null;
		try {
			plantmst.setmLogger(mLogger);
			al = plantmst.getUserPassword(userid);
			
			//TODO : Send email
			//SMTPClient smtpClient = new SMTPClient(senderAddress, recipientAddress, subject, content);
			//smtpClient.send();
			if(al.size() > 0) {
                ut = com.track.gates.DbBean.getUserTranaction();
                ut.begin();
				String randomPassword = RandomStringUtils.randomAlphanumeric(10);
				Map m = (Map) al.get(0);
				String from = "info@vision-softtech.com";
							   
				String oldPwd = (String) m.get("PASSWORD");
				String plant = (String) m.get("DEPT");
				String newPwd = new encryptBean().encrypt(randomPassword);
				String subject = "Your Password has been reset";
				String content = "Hi, this is your new password: " + randomPassword
		         + "\nNote: for security reason, "
		         + "you must change your password after logging in.";
				int n = new userBean().changePassword(userid, newPwd, oldPwd,plant);
				if(n==1) {
					SendEmail sendmail = new SendEmail();
					sendmail.sendTOMail(from, userid, "", "", subject, content, "");
				}
				ut.commit();
				return "Password sent to email address successfully";
			}else {
				return "Email address does not exists.";
			}			
		} catch (Exception e) {
			ut.rollback();
			return "Password not sent to email address successfully";
		}
	}

	public boolean addAttachments(List<Hashtable<String, String>>  AttachmentList, String plant)throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
		List<String> args = null;
		String query = "";
		try {		
			/*Instantiate the list*/
			args = new ArrayList<String>();
			
			connection = DbBean.getConnection();
			
			for (Hashtable<String, String> billDetInfo : AttachmentList) {
				String FIELDS = "", VALUES = "";
				Enumeration enumeration = billDetInfo.keys();
				for (int i = 0; i < billDetInfo.size(); i++) {
					String key = StrUtils.fString((String) enumeration.nextElement());
					String value = StrUtils.fString((String) billDetInfo.get(key));
					args.add(value);
					FIELDS += key + ",";
					VALUES += "?,";
				}		
				query = "INSERT INTO [PLANTMST_ATTACHMENTS]  ("
				+ FIELDS.substring(0, FIELDS.length() - 1)
				+ ") VALUES ("
				+ VALUES.substring(0, VALUES.length() - 1) + ")";
				
				if(connection != null){
					/*Create  PreparedStatement object*/
					ps = connection.prepareStatement(query);
					BaseDAO basedao = new BaseDAO();
					flag = basedao.execute_NonSelectQuery(ps, args);
					if(flag){
						args.clear();
					}
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
	
	public int getPlantMstMaxId()  throws Exception {	
	PlantMstDAO plantmst = new PlantMstDAO();
    plantmst.setmLogger(mLogger);
    int plantId = 0;
    Hashtable ht = new Hashtable();
    String query = " MAX(ID)  AS  ID ";
    Map m = plantmst.selectRow(query, ht);
    plantId = Integer.parseInt((String) m.get("ID"));
    return plantId;
	}
	
	public String getPeppolId(String aPlant) throws Exception {
        PlantMstDAO plantmst = new PlantMstDAO();
        plantmst.setmLogger(mLogger);
        String peppolid = "";
        Hashtable ht = new Hashtable();
        ht.put("PLANT", aPlant);
        String query = " ISNULL(PEPPOL_ID,'')PEPPOL_ID ";
        Map m = plantmst.selectRow(query, ht);
        peppolid = (String) m.get("PEPPOL_ID");
        return peppolid;
    }
	
	public String getPeppolUkey(String aPlant) throws Exception {
        PlantMstDAO plantmst = new PlantMstDAO();
        plantmst.setmLogger(mLogger);
        String peppolkey = "";
        Hashtable ht = new Hashtable();
        ht.put("PLANT", aPlant);
        String query = " ISNULL(PEPPOL_UKEY,'')PEPPOL_UKEY ";
        Map m = plantmst.selectRow(query, ht);
        peppolkey = (String) m.get("PEPPOL_UKEY");
        return peppolkey;
    }
}
