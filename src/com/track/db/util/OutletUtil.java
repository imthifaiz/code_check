package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.CoaDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.LocTypeDAO;
import com.track.dao.OutletBeanDAO;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class OutletUtil {
  //  private OutletMstDAO _OutletMstDAO=null;
	private OutletBeanDAO outletBeanDAO = new OutletBeanDAO();
	private CoaDAO coaDAO = new CoaDAO();
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	/*
	 * public OutletUtil() { _OutletMstDAO = new OutletMstDAO(); }
	 */
	
	public ArrayList getOutletListStartsWithName(String aCustName,
			String plant) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			outletBeanDAO.setmLogger(mLogger);
			arrList = outletBeanDAO.getOutletListStartsWithName(aCustName,
					plant);
		} catch (Exception e) {
		}
		return arrList;
	}
	public ArrayList getOutletListsWithName(String aCustName,
			String plant, String cond) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			outletBeanDAO.setmLogger(mLogger);
			arrList = outletBeanDAO.getOutletListssWithName(aCustName,
					plant, cond);
		} catch (Exception e) {
		}
		return arrList;
	}
	public boolean isExistOutlet(String aCustCode, String plant) {
		boolean exists = false;
		try {
			outletBeanDAO.setmLogger(mLogger);
			exists = outletBeanDAO.isExistsOutlet(aCustCode, plant);

		} catch (Exception e) {
		}
		return exists;
	}
	
	public boolean isExistOutletName(String aVendname, String plant) {
        boolean exists = false;
        try {
                outletBeanDAO.setmLogger(mLogger);
                exists = outletBeanDAO.isExistsOutletName(aVendname, plant);

        } catch (Exception e) {
        }
        return exists;
}
	public boolean updateOutlet(Hashtable htUpdate, Hashtable htCondition) {
		boolean update = false;
		try {
			outletBeanDAO.setmLogger(mLogger);
			update = outletBeanDAO.updateOutlet(htUpdate, htCondition);

		} catch (Exception e) {
		}
		return update;
	}
	public boolean deleteOutlet(String aCustno, String plant) {
		boolean deleted = false;
		try {
			outletBeanDAO.setmLogger(mLogger);
			deleted = outletBeanDAO.deleteOutlet(aCustno, plant);
		} catch (Exception e) {
		}
		return deleted;
	}
	 public ArrayList getOutletDetails(String sOutletCode, String plant) {
         ArrayList arrList = new ArrayList();
         try {
                 outletBeanDAO.setmLogger(mLogger);
                 arrList = outletBeanDAO.getOutletDetails(sOutletCode, plant);
         } catch (Exception e) {
         }
         return arrList;
 }

	
	
	public boolean insertOutlet(Hashtable ht) {
		boolean inserted = false;
		try {
			outletBeanDAO.setmLogger(mLogger);
			inserted = outletBeanDAO.insertIntoOutlet(ht);
			if(inserted)
			{
				CoaUtil coaUtil = new CoaUtil();
				DateUtils dateutils = new DateUtils();
				Hashtable<String, String> accountHt = new Hashtable<>();
				accountHt.put("PLANT", ht.get("PLANT").toString());
				accountHt.put("ACCOUNTTYPE", "6");
				accountHt.put("ACCOUNTDETAILTYPE", "18");
				accountHt.put("ACCOUNT_NAME", ht.get("OUTLET").toString()+"-"+ht.get("OUTLET_NAME").toString());
				accountHt.put("DESCRIPTION", "");
				accountHt.put("ISSUBACCOUNT", "0");
				accountHt.put("SUBACCOUNTNAME", "");
				accountHt.put("OPENINGBALANCE", "");
				accountHt.put("OPENINGBALANCEDATE", "");
				accountHt.put("CRAT", dateutils.getDateTime());
				accountHt.put("CRBY", ht.get("CRBY").toString());
				accountHt.put("UPAT", dateutils.getDateTime());
				
				String gcode = coaDAO.GetGCodeById("6", ht.get("PLANT").toString());
				String dcode = coaDAO.GetDCodeById("18", ht.get("PLANT").toString());
				List<Map<String, String>> listQry = coaDAO.getMaxAccoutCode(ht.get("PLANT").toString(), "6", "18");
				String maxid = "";
				String atcode = "";
				if(listQry.size() > 0) {
					for (int i = 0; i < listQry.size(); i++) {
						Map<String, String> m = listQry.get(i);
						maxid = m.get("CODE");
					}
				
					int count = Integer.valueOf(maxid);
					atcode = String.valueOf(count+1);
					if(atcode.length() == 1) {
						atcode = "0"+atcode;
					}
				}else {
					atcode = "01";
				}
				String accountCode = gcode+"-"+dcode+atcode;
				accountHt.put("ACCOUNT_CODE", accountCode);
				accountHt.put("CODE", atcode);
				
				coaUtil.addAccount(accountHt, ht.get("PLANT").toString());
			}
		} catch (Exception e) {
		}
		return inserted;
	}
	
	public boolean insertOutletTerminal(Hashtable ht) {
		boolean inserted = false;
		try {
			outletBeanDAO.setmLogger(mLogger);
			inserted = outletBeanDAO.insertIntoOutletTerminal(ht);
		} catch (Exception e) {
		}
		return inserted;
	}
	
	public ArrayList getOutletTerminalListStartsWithName(String Terminal,
			String plant) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			outletBeanDAO.setmLogger(mLogger);
			arrList = outletBeanDAO.getOutletTerminalListStartsWithName(Terminal,
					plant);
		} catch (Exception e) {
		}
		return arrList;
	}
	
	public ArrayList getOutletTerminalListsWithName(String Terminal,
			String plant, String cond) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			outletBeanDAO.setmLogger(mLogger);
			arrList = outletBeanDAO.getOutletTerminalListssWithName(Terminal,
					plant, cond);
		} catch (Exception e) {
		}
		return arrList;
	}
	
	public ArrayList getOutletTerminalListsWithOutlet(String Outlet,
			String plant, String cond) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			outletBeanDAO.setmLogger(mLogger);
			arrList = outletBeanDAO.getOutletTerminalListsWithOutlet(Outlet,
					plant, cond);
		} catch (Exception e) {
		}
		return arrList;
	}
	
	public ArrayList getOutletTerminalListsWithName(String Outlet,String Terminal,
			String plant, String cond) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			outletBeanDAO.setmLogger(mLogger);
			arrList = outletBeanDAO.getOutletTerminalListssWithName(Outlet,Terminal,
					plant, cond);
		} catch (Exception e) {
		}
		return arrList;
	}
	
	public boolean isExistTerminal(String Terminal, String plant) {
		boolean exists = false;
		try {
			outletBeanDAO.setmLogger(mLogger);
			exists = outletBeanDAO.isExistsTerminal(Terminal, plant);
			
		} catch (Exception e) {
		}
		return exists;
	}
	
	public boolean isExistTerminalName(String Terminal, String plant) {
		boolean exists = false;
		try {
			outletBeanDAO.setmLogger(mLogger);
			exists = outletBeanDAO.isExistsTerminalName(Terminal, plant);
			
		} catch (Exception e) {
		}
		return exists;
	}
	
	public boolean updateOutletTerminal(Hashtable htUpdate, Hashtable htCondition) {
		boolean update = false;
		try {
			outletBeanDAO.setmLogger(mLogger);
			update = outletBeanDAO.updateOutletTerminal(htUpdate, htCondition);
			
		} catch (Exception e) {
		}
		return update;
	}
	
	public boolean deleteOutletTerminal(String Terminal, String plant) {
		boolean deleted = false;
		try {
			outletBeanDAO.setmLogger(mLogger);
			deleted = outletBeanDAO.deleteOutletTerminal(Terminal, plant);
		} catch (Exception e) {
		}
		return deleted;
	}
	
	 public ArrayList getOutletTerminalDetails(String sOutletCode, String plant) {
		 ArrayList arrList = new ArrayList();
		 try {
			 outletBeanDAO.setmLogger(mLogger);
			 arrList = outletBeanDAO.getOutletTerminalDetails(sOutletCode, plant);
		 } catch (Exception e) {
		 }
		 return arrList;
	 }
	 public ArrayList getOutletTerminalTimeDetail(String sOutletCode, String plant,String day) {
		 ArrayList arrList = new ArrayList();
		 try {
			 outletBeanDAO.setmLogger(mLogger);
			 arrList = outletBeanDAO.getOutletTerminalTimeDetails(sOutletCode, plant,day);
		 } catch (Exception e) {
		 }
		 return arrList;
	 }
}


	/**
	 * method : insertCustomer(Hashtable ht) description : insert new record
	 * into CUSTMST
	 * 
	 * @param ht
	 * @return
	 */
	