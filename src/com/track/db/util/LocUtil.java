package com.track.db.util;

import com.track.constants.IDBConstants;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.dao.LocMstBeanDAO;
import com.track.dao.LocMstDAO;
import com.track.util.MLogger;
import com.track.util.XMLUtils;
import com.track.util.StrUtils;

public class LocUtil {
	public LocUtil() {
	}

	private MLogger mLogger = new MLogger();
	private LocMstBeanDAO locMstBeanDAO = new LocMstBeanDAO();
	private boolean printLog = MLoggerConstant.LocUtil_PRINTPLANTMASTERLOG;
	
	StrUtils strUtils=new StrUtils();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public boolean addLocation(Hashtable ht) throws Exception {
		boolean flag = false;
		try {
			locMstBeanDAO.setmLogger(mLogger);
			flag = locMstBeanDAO.insertIntoLocMst(ht);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return flag;
	}

	public ArrayList getLocDetails(String locId) throws Exception {
		Hashtable htCondition = new Hashtable();
		ArrayList alResult = new ArrayList();
		try {
			locMstBeanDAO.setmLogger(mLogger);
			alResult = locMstBeanDAO.getLocDetails("LOC,LOCDESC,USERFLD1",
					htCondition, " and loc like ='" + locId + "%'"
							+ "ORDER BY LOC ");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return alResult;
	}

	public ArrayList getLocDetails(String locId, String plant,String extraCond,Hashtable ht) throws Exception {
		Hashtable htCondition = new Hashtable();
		ArrayList alResult = new ArrayList();
		try {
			htCondition.put("PLANT", plant);
			String loctypeid = ht.get("LOC_TYPE_ID").toString();
			String loctypeid2 = ht.get("LOC_TYPE_ID2").toString();
			String loctypeid3 = ht.get("LOC_TYPE_ID3").toString();
			if(loctypeid.length()>0){
			htCondition.put("LOC_TYPE_ID", loctypeid);}
			if(loctypeid2.length()>0){
			htCondition.put("LOC_TYPE_ID2", loctypeid2);}
			if(loctypeid3.length()>0){
				htCondition.put("LOC_TYPE_ID3", loctypeid3);}
			locMstBeanDAO.setmLogger(mLogger);
			//alResult is changed by Bruhan
			alResult = locMstBeanDAO.getLocDetails(
					"LOC,ISNULL(LOCDESC,'') LOCDESC,isnull(USERFLD1,'')as USERFLD1,ISACTIVE,COMNAME,RCBNO,isnull(ADD1,'')as ADD1,isnull(ADD2,'')as ADD2,isnull(ADD3,'')as ADD3,isnull(ADD4,'') as ADD4,isnull(STATE,'')as STATE,isnull(COUNTRY,'')as COUNTRY,isnull(ZIP,'') as ZIP,isnull(TELNO,'')as TELNO,isnull(FAX,'')as FAX,CHKSTATUS,isnull(LOC_TYPE_ID,'') as LOC_TYPE_ID,isnull(LOC_TYPE_ID2,'') as LOC_TYPE_ID2,isnull(LOC_TYPE_ID3,'') as LOC_TYPE_ID3,ISACTIVE", htCondition,
					" and loc like '%" + locId + "%'" +extraCond+ "ORDER BY LOC ");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return alResult;
	}

	public ArrayList getAllLocDetails() throws Exception {
		boolean flag = false;
		Hashtable htCondition = new Hashtable();
		ArrayList alResult = new ArrayList();

		htCondition.put("PLANT", "SIS");

		try {
			locMstBeanDAO.setmLogger(mLogger);
			alResult = locMstBeanDAO.getAllLocDetails("LOC,LOCDESC,USERFLD1",
					htCondition, " ORDER BY LOC ");

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);

		}

		return alResult;
	}

	public ArrayList getAllLocDetails(String plant, String cond,String user)
			throws Exception {
		boolean flag = false;
		Hashtable htCondition = new Hashtable();
		ArrayList alResult = new ArrayList();

		htCondition.put("PLANT", plant);

		try {
			locMstBeanDAO.setmLogger(mLogger);
                        UserLocUtil userLocUtil = new UserLocUtil();
                        userLocUtil.setmLogger(mLogger);
                        String condAssignedLocforUser = " "; 
                        condAssignedLocforUser =  userLocUtil.getUserLocAssigned(user,plant,"LOC");
            // alResult is changed by Bruhan to get all details frm locmst            
			alResult = locMstBeanDAO.getAllLocDetails(
					"LOC,LOCDESC,USERFLD1,ISACTIVE,COMNAME,RCBNO,ADD1,ADD2,ADD3,ADD4,STATE,COUNTRY,ZIP,TELNO,FAX,CHKSTATUS,ISNULL(LOC_TYPE_ID,'') LOC_TYPE_ID,ISNULL(LOC_TYPE_ID2,'') LOC_TYPE_ID2" +
					"", htCondition, cond+condAssignedLocforUser
							+ " ORDER BY LOC ");

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
		}

		return alResult;
	}
	
	public ArrayList getLocDetails(String locId, String plant,String user) throws Exception {
		Hashtable htCondition = new Hashtable();
		ArrayList alResult = new ArrayList();
		String condAssignedLocforUser = " "; 
		try {
			htCondition.put("PLANT", plant);
			locMstBeanDAO.setmLogger(mLogger);
			UserLocUtil userLocUtil = new UserLocUtil();
            userLocUtil.setmLogger(mLogger);
           
			 condAssignedLocforUser =  userLocUtil.getUserLocAssigned(user,plant,"LOC");
			 
			alResult = locMstBeanDAO.getLocDetails(
					"LOC,LOCDESC,USERFLD1,ISACTIVE", htCondition,
					" and loc like '" + locId + "%'"+  condAssignedLocforUser+ "  ORDER BY LOC ");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return alResult;
	}

	public ArrayList getLocListDetails(String plant, String loc)
			throws Exception {
		boolean flag = false;
		Hashtable htCondition = new Hashtable();
		ArrayList alResult = new ArrayList();

		htCondition.put("PLANT", plant);
		

		try {
			locMstBeanDAO.setmLogger(mLogger);
			//alresult is changed by Bruhan to get all details of locmst
			alResult = locMstBeanDAO.getLocList(
					"LOC,LOCDESC,isnull(USERFLD1,'') as USERFLD1,COMNAME,ISNULL(RCBNO,'') RCBNO,ISACTIVE,isnull(ADD1,'') as ADD1,isnull(ADD2,'') as ADD2," +
					"isnull(ADD3,'') as ADD3,isnull(ADD4,'') as ADD4,isnull(COUNTRY,'') as COUNTRY,isnull(ZIP,'') as ZIP," +
					"isnull(TELNO,'') as TELNO,isnull(FAX,'') as FAX,isnull(LOC_TYPE_ID,'') as LOC_TYPE_ID",
					htCondition, loc);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);

		}

		return alResult;
	}

	public boolean updateLocation(Hashtable htValues, Hashtable htCondition,
			String extCond) throws Exception {
		boolean flag = false;
		try {
			StringBuffer sb = new StringBuffer(" set ");
			sb.append("LOCDESC='" + htValues.get("LOCDESC") + "'");
			sb.append(",USERFLD1='" + htValues.get("USERFLD1") + "'");
			sb.append(",ISACTIVE='" + htValues.get("ISACTIVE") + "'");
			sb.append(",COMNAME='" + htValues.get("COMNAME") + "'");
			sb.append(",RCBNO='" + htValues.get("RCBNO") + "'");
			sb.append(",ADD1='" + htValues.get("ADD1") + "'");
			sb.append(",ADD2='" + htValues.get("ADD2") + "'");
			sb.append(",ADD3='" + htValues.get("ADD3") + "'");
			sb.append(",ADD4='" + htValues.get("ADD4") + "'");
			sb.append(",STATE='" + htValues.get("STATE") + "'");
			sb.append(",COUNTRY='" + htValues.get("COUNTRY") + "'");
			sb.append(",ZIP='" + htValues.get("ZIP") + "'");
			sb.append(",TELNO='" + htValues.get("TELNO") + "'");
			sb.append(",FAX='" + htValues.get("FAX") + "'");
			sb.append(",CHKSTATUS='" + htValues.get("CHKSTATUS") + "'");
			sb.append(",LOC_TYPE_ID='" + htValues.get("LOC_TYPE_ID") + "'");
			sb.append(",LOC_TYPE_ID2='" + htValues.get("LOC_TYPE_ID2") + "'");
			sb.append(",LOC_TYPE_ID3='" + htValues.get("LOC_TYPE_ID3") + "'");
			locMstBeanDAO.setmLogger(mLogger);

			flag = locMstBeanDAO.updateLocMst(sb.toString(), htCondition, "");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return flag;
	}

	public boolean deleteLocation(String custId, String plant) throws Exception {

		boolean flag = false;
		try {
			locMstBeanDAO.setmLogger(mLogger);
			flag = locMstBeanDAO.deleteLoc(custId, plant);

		} catch (Exception e) {

			throw e;
		}
		return flag;
	}

	public String getlocations(String plant,String User) {

		String xmlStr = "";
		ArrayList al = null;
		LocMstDAO dao = new LocMstDAO();
		XMLUtils xmlUtils = new XMLUtils();
		dao.setmLogger(mLogger);
		try {
			al = dao.getLocByWMS(plant,User);

			if (al.size() > 0) {
				xmlStr += xmlUtils.getXMLHeader();
				xmlStr += xmlUtils.getStartNode("locs total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += xmlUtils.getStartNode("record");
					xmlStr += xmlUtils.getXMLNode("Loc", (String) map
							.get("loc"));
					xmlStr += xmlUtils.getEndNode("record");
				}
				xmlStr += xmlUtils.getEndNode("locs");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return xmlStr;
	}

	public String getlocationsWithDesc(String plant1,String user) {

		String xmlStr = "";
		ArrayList al = null;
		LocMstDAO dao = new LocMstDAO();
		dao.setmLogger(mLogger);

		try {
			al = dao.getLocByWMS(plant1,user);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("locs total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("Loc", (String) map
							.get("loc"));
					 xmlStr += XMLUtils.getXMLNode("desc", strUtils.replaceCharacters2SendPDA((String) map.get("locdesc")));
					//  xmlStr += XMLUtils.getXMLNode("desc", "");
					  
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("locs");
				System.out.println(xmlStr);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return xmlStr;
	}
	
	public String getlocationsForInvPDA(String Plant,String start,String end,String itemNo) {

		String xmlStr = "";
		ArrayList al = null;
		LocMstDAO dao = new LocMstDAO();
	
		String query = "with S AS (SELECT (ROW_NUMBER() OVER ( ORDER BY IM.LOC )) AS ID,ISNULL((SELECT COUNT(*)  FROM ["+Plant+"_LOCMST] as IM where IM.PLANT='"+Plant+"' and IM.LOC LIKE '%"+itemNo+"%'),0) TOTPO , IM.LOC,LOCDESC FROM ["+Plant+"_LOCMST] as IM where IM.PLANT ='"+Plant+"' and IM.LOC LIKE '%"+itemNo+"%')SELECT * FROM S where ID >='"+start+"' and ID<='"+end+"'  ORDER BY LOC";
		String extCond = "";
		Hashtable ht = new Hashtable();

		//
		try {
			dao.setmLogger(mLogger);
			al =dao.selectLocForInvPDA(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("item", (String) map
							.get("LOC"));
					 xmlStr += XMLUtils.getXMLNode("itemDesc", strUtils.replaceCharacters2SendPDA((String) map.get("LOCDESC")));
					xmlStr += XMLUtils.getXMLNode("count", (String) map
							.get("TOTPO"));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
				System.out.println(xmlStr);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}
        

        public boolean isValidLocInLocmst(String aPlant,String aLoc) throws Exception {
                boolean isValidLoc = false;
            LocMstDAO dao = new LocMstDAO();
            dao.setmLogger(mLogger);
                try {
	                    Hashtable htloc = new Hashtable();
	                    
	                    htloc.clear();
	                    htloc.put(IDBConstants.PLANT, aPlant);
	                    if(aLoc.length()>0){
	                    htloc.put(IDBConstants.LOC,aLoc);
	                    isValidLoc = dao.isExisit(htloc,"  ISACTIVE ='Y' AND LOC NOT LIKE 'SHIPPINGAREA%' AND LOC NOT LIKE 'TEMP_TO%'");
	                 }
                    

                } catch (Exception e) {
                throw e;
                }
                return isValidLoc;
        }
        
		
		    public boolean isExistsLoc(Hashtable htLoc) throws Exception {

    		boolean isExists = false;
    		 LocMstDAO dao = new LocMstDAO();
    	        dao.setmLogger(mLogger);
    		try {
    			isExists = dao.isExistsLoc(htLoc);

    		} catch (Exception e) {
    			throw e;
    		}

    		return isExists;
    	}
		
  public boolean isValidLoc(String aPlant,String aLoc) throws Exception {
            boolean isValidLoc = false;
        LocMstDAO dao = new LocMstDAO();
        dao.setmLogger(mLogger);
            try {
                    Hashtable htloc = new Hashtable();
                    htloc.clear();
                    htloc.put(IDBConstants.PLANT, aPlant);
                    if(aLoc.length()>0){
                    htloc.put(IDBConstants.LOC,aLoc);
                    isValidLoc = dao.isExisit(htloc,"  LOC NOT LIKE 'SHIPPINGAREA%' AND LOC NOT LIKE 'TEMP_TO%'");
                 }
                

            } catch (Exception e) {
            throw e;
            }
            return isValidLoc;
    }
          
//created by vicky Used for PDA Location order AutoSuggestion
	public String getlocationsForInvPDAAutoSuggestion(String Plant,String loc) {

		String xmlStr = "";
		ArrayList al = null;
		LocMstDAO dao = new LocMstDAO();
	
		String query= "SELECT  LOC,LOCDESC FROM ["+Plant+"_LOCMST ]  WHERE  LOC LIKE '"+loc+"%' AND PLANT = '"+Plant+"'  GROUP BY  LOC,LOCDESC ORDER BY LOC ";
		String extCond = "";
		Hashtable ht = new Hashtable();

		//
		try {
			dao.setmLogger(mLogger);
			al =dao.selectLocForInvPDA(query, ht, extCond);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("item", (String) map
							.get("LOC"));
					 xmlStr += XMLUtils.getXMLNode("itemDesc", strUtils.replaceCharacters2SendPDA((String) map.get("LOCDESC")));
					
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
				System.out.println(xmlStr);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}
//Created by vicky Desc:Used for fetching All locations details for PDA			
	public String getlocationsWithDescForPda(String plant1,String user) {

		String xmlStr = "";
		ArrayList al = null;
		LocMstDAO dao = new LocMstDAO();
		dao.setmLogger(mLogger);

		try {
			al = dao.getLocByWMSPDA(plant1,user);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("locs total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map map = (Map) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("Loc", (String) map
							.get("loc"));
					 xmlStr += XMLUtils.getXMLNode("desc", strUtils.replaceCharacters2SendPDA((String) map.get("locdesc")));
					//  xmlStr += XMLUtils.getXMLNode("desc", "");
					  
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("locs");
				System.out.println(xmlStr);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return xmlStr;
	}    
	
	public boolean insertPrdLocMst(Hashtable ht) throws Exception {
		boolean inserted = false;
		try {
			LocMstDAO itemDao = new LocMstDAO();
			itemDao.setmLogger(mLogger);
			inserted = itemDao.insertLocMst(ht);
		} catch (Exception e) {
			throw e;
		}
		return inserted;
	}
	 public boolean isExistPRDLoc(String prd_loc_id,String prd_loc_desc, String plant) {
			boolean exists = false;
			try {
				LocMstDAO dao = new LocMstDAO();
				dao.setmLogger(mLogger);
				exists = dao.isExistPRDLoc(prd_loc_id,prd_loc_desc,plant);

			} catch (Exception e) {
			}
			return exists;
		}
}