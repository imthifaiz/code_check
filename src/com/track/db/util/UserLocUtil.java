package com.track.db.util;

import com.track.constants.IDBConstants;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.dao.LocMstBeanDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.UserAssignedLocDAO;
import com.track.util.MLogger;
import com.track.util.XMLUtils;
import com.track.util.StrUtils;

public class UserLocUtil {
	public UserLocUtil() {
	}

	private MLogger mLogger = new MLogger();
	private UserAssignedLocDAO userlocDAO = new UserAssignedLocDAO();
	private boolean printLog = MLoggerConstant.LocUtil_PRINTPLANTMASTERLOG;
	
	StrUtils strUtils=new StrUtils();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public boolean addUserLocation(Hashtable ht) throws Exception {
		boolean flag = false;
		try {
			userlocDAO.setmLogger(mLogger);
			flag = userlocDAO.insertIntoUserLocMst(ht);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return flag;
	}
	
	public boolean addInvLocation(Hashtable ht) throws Exception {
		boolean flag = false;
		try {
			userlocDAO.setmLogger(mLogger);
			flag = userlocDAO.insertIntoInvLocMst(ht);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		
		return flag;
	}

	public ArrayList getUserLocDetails(String locId) throws Exception {
		Hashtable htCondition = new Hashtable();
		ArrayList alResult = new ArrayList();
		try {
			userlocDAO.setmLogger(mLogger);
			alResult = userlocDAO.getUserLocDetails("LOC",
					htCondition, " and UserID like ='" + locId + "%'"
							+ "ORDER BY LOC ");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return alResult;
	}

	public ArrayList getUserLocDetails(String user, String plant) throws Exception {
		Hashtable htCondition = new Hashtable();
		ArrayList alResult = new ArrayList();
		try {
			htCondition.put("PLANT", plant);
			userlocDAO.setmLogger(mLogger);
			alResult = userlocDAO.getUserLocDetails(
					"LOC", htCondition,
					" and USERID in (select UI_PKEY from USER_INFO u where u.USER_ID= '" + user + "')" + " ORDER BY LOC ");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return alResult;
	}
        
    public String getUserLocAssigned(String user, String plant,String column) throws Exception {
            ArrayList alResult = new ArrayList();
           String condAssignedLocforUser = " "; 
            try {
                   
                    alResult = getUserLocDetails(user,plant);
                    if(alResult.size()>0){
                    condAssignedLocforUser = " AND "+column+" IN( ";  
                    String locList ="",loc="";
                    for (int iCnt =0; iCnt<alResult.size(); iCnt++){
                        Map lineArr = (Map) alResult.get(iCnt);
                        loc= (String)lineArr.get("LOC");
                        if(locList.length()>0){
                            locList = locList+",'"+loc+"'";
                        }else{
                            locList = "'"+loc+"'";
                        }
                       
                    }
                    condAssignedLocforUser = condAssignedLocforUser + locList+")";
                   
                }else{
                    condAssignedLocforUser="";
                }                

            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
            }

            return condAssignedLocforUser;
    }



	public boolean deleteUserLocation(String user,String loc, String plant) throws Exception {

		boolean flag = false;
		try {
			userlocDAO.setmLogger(mLogger);
			flag = userlocDAO.deleteUserLoc(user,loc, plant);

		} catch (Exception e) {

			throw e;
		}
		return flag;
	}
//	IMTI UNASSIGN DELETE START
	public boolean deleteUserLoc(String user, String plant) throws Exception {
		
		boolean flag = false;
		try {
			userlocDAO.setmLogger(mLogger);
			flag = userlocDAO.deleteUserLocation(user, plant);
			
		} catch (Exception e) {
			
			throw e;
		}
		return flag;
	}
//	IMTI UNASSIGN DELETE END
	
//	IMTI UNASSIGN DELETE START
	public boolean deleteInvLoc(String loc, String plant) throws Exception {
		
		boolean flag = false;
		try {
			userlocDAO.setmLogger(mLogger);
			flag = userlocDAO.deleteInvLocation(loc, plant);
			
		} catch (Exception e) {
			
			throw e;
		}
		return flag;
	}
//	IMTI UNASSIGN DELETE END
	
//	IMTI UNASSIGN DELETE START
	public boolean deleteInvLoc(String plant) throws Exception {
		
		boolean flag = false;
		try {
			userlocDAO.setmLogger(mLogger);
			flag = userlocDAO.deleteInvLocation(plant);
			
		} catch (Exception e) {
			
			throw e;
		}
		return flag;
	}
//	IMTI UNASSIGN DELETE END
	
   public boolean isValidUserAssignedLoc(String aPlant,String aUser, String aLoc) throws Exception {
           
                boolean isValidLoc = false;
               userlocDAO.setmLogger(mLogger);
                try {
                    Hashtable htloc = new Hashtable();
                    
                    htloc.clear();
                    htloc.put(IDBConstants.PLANT, aPlant);
                
                    isValidLoc = userlocDAO.isAlreadtUserLocExists(aUser,aLoc,aPlant);
                   

                } catch (Exception e) {
                throw e;
                }
                return isValidLoc;
        }
   
   public boolean isAlreadyInvLocExist(String aPlant,String aLoc) throws Exception {
	   
	   boolean isValidLoc = false;
	   userlocDAO.setmLogger(mLogger);
	   try {
		   Hashtable htloc = new Hashtable();
		   
		   htloc.clear();
		   htloc.put(IDBConstants.PLANT, aPlant);
		   
		   isValidLoc = userlocDAO.isAlreadyInvLocExists(aLoc,aPlant);
		   
		   
	   } catch (Exception e) {
		   throw e;
	   }
	   return isValidLoc;
   }
       
    public boolean isValidLocInLocmstForUser(String aPlant,String user,String aLoc) throws Exception {
            boolean isValidLoc = false;
        LocMstDAO dao = new LocMstDAO();
        dao.setmLogger(mLogger);
            try {
         
                String extraCon= "";//getUserLocAssigned(user,aPlant,"LOC");
                Hashtable htloc = new Hashtable();
                 htloc.clear();
                htloc.put(IDBConstants.PLANT, aPlant);
                if(aLoc.length()>0){
                htloc.put(IDBConstants.LOC,aLoc);
                isValidLoc = dao.isExisit(htloc,"  ISACTIVE ='Y' AND LOC NOT LIKE 'SHIPPINGAREA%' AND LOC NOT LIKE 'TEMP_TO%'"+extraCon);
            
                }
                

            } catch (Exception e) {
            throw e;
            }
            return isValidLoc;
    }
        
        
        
}