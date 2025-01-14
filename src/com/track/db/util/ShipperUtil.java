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
import com.track.dao.PrdDeptDAO;
import com.track.dao.ShipperDAO;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class ShipperUtil {
	  private CustMstDAO _CustMstDAO=null;
		private ShipperDAO shipperDAO = new ShipperDAO();
		private CoaDAO coaDAO = new CoaDAO();
		private MLogger mLogger = new MLogger();

		public MLogger getmLogger() {
			return mLogger;
		}

		public void setmLogger(MLogger mLogger) {
			this.mLogger = mLogger;
		}

		public ShipperUtil() { 
		_CustMstDAO = new CustMstDAO();
		}

		/**
		 * method : insertCustomer(Hashtable ht) description : insert new record
		 * into CUSTMST
		 * 
		 * @param ht
		 * @return
		 */
		public boolean isExistShipper(String aCustCode, String plant) {
			boolean exists = false;
			try {
				shipperDAO.setmLogger(mLogger);
				exists = shipperDAO.isExistShipper(aCustCode, plant);

			} catch (Exception e) {
			}
			return exists;
		}
		
		  public boolean isExistShipperName(String aVendname, String plant) {
	            boolean exists = false;
	            try {
	                   shipperDAO.setmLogger(mLogger);
	                    exists = shipperDAO.isExistsShipperName(aVendname, plant);

	            } catch (Exception e) {
	            }
	            return exists;
	    }
		  
			public boolean updateShipper(Hashtable htUpdate, Hashtable htCondition) {
				boolean update = false;
				try {
					shipperDAO.setmLogger(mLogger);
					update = shipperDAO.updateShipper(htUpdate, htCondition);

				} catch (Exception e) {
				}
				return update;
			}
			public boolean deleteShipper(String aCustno, String plant) {
				boolean deleted = false;
				try {
					shipperDAO.setmLogger(mLogger);
					deleted = shipperDAO.deleteShipper(aCustno, plant);
				} catch (Exception e) {
				}
				return deleted;
			}
			 public ArrayList getShipperDetails(String aCustno, String plant) {
		            ArrayList arrList = new ArrayList();
		            try {
		                    shipperDAO.setmLogger(mLogger);
		                    arrList = shipperDAO.getShipperDetails(aCustno, plant);
		            } catch (Exception e) {
		            }
		            return arrList;
		    }
			 
			 public ArrayList getShipperListStartsWithName(String aCustName,
						String plant, String cond) throws Exception {
					ArrayList arrList = new ArrayList();
					try {
						shipperDAO.setmLogger(mLogger);
						arrList = shipperDAO.getShipperListStartsWithName(aCustName,
								plant, cond);
					} catch (Exception e) {
					}
					return arrList;
				}
			 
			 public ArrayList getShipperListsWithName(String aCustName,
						String plant, String cond) throws Exception {
					ArrayList arrList = new ArrayList();
					try {
						shipperDAO.setmLogger(mLogger);
						arrList = shipperDAO.getShipperListssWithName(aCustName,
								plant, cond);
					} catch (Exception e) {
					}
					return arrList;
				}
		
			 
			 public boolean insertShipper(Hashtable ht) {
					boolean inserted = false;
					try {
						shipperDAO.setmLogger(mLogger);
						inserted = shipperDAO.insertIntoShipper(ht);
						if(inserted)
						{
							CoaUtil coaUtil = new CoaUtil();
							DateUtils dateutils = new DateUtils();
							Hashtable<String, String> accountHt = new Hashtable<>();
							accountHt.put("PLANT", ht.get("PLANT").toString());
							accountHt.put("ACCOUNTTYPE", "6");
							accountHt.put("ACCOUNTDETAILTYPE", "18");
							accountHt.put("ACCOUNT_NAME", ht.get("SHIPPERNO").toString()+"-"+ht.get("SHIPPERNAME").toString());
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
		

}
