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
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class CustUtil {
    private CustMstDAO _CustMstDAO=null;
	private CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
	private boolean printLog = MLoggerConstant.CUSTOMERBEANDAO_PRINTPLANTMASTERLOG;
	private CoaDAO coaDAO = new CoaDAO();
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public CustUtil() { 
	_CustMstDAO = new CustMstDAO();
	}

	/**
	 * method : insertCustomer(Hashtable ht) description : insert new record
	 * into CUSTMST
	 * 
	 * @param ht
	 * @return
	 */
	public boolean insertCustomer(Hashtable ht) {
		boolean inserted = false;
		try {
			customerBeanDAO.setmLogger(mLogger);
			inserted = customerBeanDAO.insertIntoCustomer(ht);
			if(inserted)
			{
				boolean chkvl= ht.containsKey("USER_ID");
				String CUSTOMER_CODE = "";
				if(chkvl)
					CUSTOMER_CODE = ht.get("USER_ID").toString();
				else
					CUSTOMER_CODE = ht.get(IDBConstants.CUSTOMER_CODE).toString();
					
				CoaUtil coaUtil = new CoaUtil();
				DateUtils dateutils = new DateUtils();
				Hashtable<String, String> accountHt = new Hashtable<>();
				accountHt.put("PLANT", ht.get("PLANT").toString());
				accountHt.put("ACCOUNTTYPE", "3");
				accountHt.put("ACCOUNTDETAILTYPE", "7");
				accountHt.put("ACCOUNT_NAME", CUSTOMER_CODE+"-"+ht.get("CNAME").toString());
				accountHt.put("DESCRIPTION", "");
				accountHt.put("ISSUBACCOUNT", "0");
				accountHt.put("SUBACCOUNTNAME", "");
				accountHt.put("OPENINGBALANCE", "");
				accountHt.put("OPENINGBALANCEDATE", "");
				accountHt.put("CRAT", dateutils.getDateTime());
				accountHt.put("CRBY", ht.get("CRBY").toString());
				accountHt.put("UPAT", dateutils.getDateTime());
				
				String gcode = coaDAO.GetGCodeById("3", ht.get("PLANT").toString());
				String dcode = coaDAO.GetDCodeById("7", ht.get("PLANT").toString());
				List<Map<String, String>> listQry = coaDAO.getMaxAccoutCode(ht.get("PLANT").toString(), "3", "7");
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

	public boolean insertVendor(Hashtable ht) {
		boolean inserted = false;
		try {
			customerBeanDAO.setmLogger(mLogger);
			inserted = customerBeanDAO.insertIntoVendor(ht);
			if(inserted)
			{
				CoaUtil coaUtil = new CoaUtil();
				DateUtils dateutils = new DateUtils();
				Hashtable<String, String> accountHt = new Hashtable<>();
				accountHt.put("PLANT", ht.get("PLANT").toString());
				accountHt.put("ACCOUNTTYPE", "6");
				accountHt.put("ACCOUNTDETAILTYPE", "18");
				accountHt.put("ACCOUNT_NAME", ht.get("VENDNO").toString()+"-"+ht.get("VNAME").toString());
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

	/**
	 * method : deleteItem(Hashtable ht) description : Delete record from
	 * CUSTMST
	 * 
	 * @param aCustno
	 * @return boolean
	 */
	public boolean deleteCustomer(String aCustno) {
		boolean deleted = false;
		try {
			customerBeanDAO.setmLogger(mLogger);
			deleted = customerBeanDAO.deleteCustomer(aCustno);
		} catch (Exception e) {
		}
		return deleted;
	}

	public boolean deleteCustomer(String aCustno, String plant) {
		boolean deleted = false;
		try {
			customerBeanDAO.setmLogger(mLogger);
			deleted = customerBeanDAO.deleteCustomer(aCustno, plant);
		} catch (Exception e) {
		}
		return deleted;
	}

	public boolean deleteVendor(String aCustno, String plant) {
		boolean deleted = false;
		try {
			customerBeanDAO.setmLogger(mLogger);
			deleted = customerBeanDAO.deleteVendor(aCustno, plant);
		} catch (Exception e) {
		}
		return deleted;
	}

	/**
	 * method : getCustomerDetails(String aCustno) description : get Customer
	 * details for the given customer code
	 * 
	 * @param aCustno
	 * @return ArrayList
	 */
	public ArrayList getCustomerDetails(String aCustno) {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getCustomerDetails(aCustno);
		} catch (Exception e) {
		}
		return arrList;
	}
	
	public ArrayList getUserCustDetails(String CUSTNO ,Hashtable ht,String plant) throws Exception {
		Hashtable htCondition = new Hashtable();
		ArrayList alResult = new ArrayList();
		try {
			htCondition.put("PLANT", plant);
			String loctypeid = ht.get("CUSTNO").toString();
			String loctypeid2 = ht.get("CNAME").toString();
			String loctypeid3 = ht.get("CUSTOMER_TYPE").toString();
			if(loctypeid.length()>0){
			htCondition.put("CUSTNO", loctypeid);}
			if(loctypeid2.length()>0){
			htCondition.put("CNAME", loctypeid2);}
			if(loctypeid3.length()>0){
				htCondition.put("CUSTOMER_TYPE", loctypeid3);}
			customerBeanDAO.setmLogger(mLogger);
			//alResult is changed by Bruhan
			
			alResult = customerBeanDAO.getUserCustDetails(
					"CUSTNO,ISNULL(CNAME,'') CNAME,isnull(CUSTOMER_TYPE_ID,'')as CUSTOMER_TYPE,isnull(EMAIL,'') as EMAIL,ISACTIVE",htCondition,"");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return alResult;
	}
	 public boolean isValidUserAssignedCus(String aPlant,String aUser, String aLoc) throws Exception {
         
         boolean isValidLoc = false;
         customerBeanDAO.setmLogger(mLogger);
         try {
             Hashtable htloc = new Hashtable();
             
             htloc.clear();
             htloc.put(IDBConstants.PLANT, aPlant);
         
             isValidLoc = customerBeanDAO.isAlreadtUserCustExists(aUser,aLoc,aPlant);
            

         } catch (Exception e) {
         throw e;
         }
         return isValidLoc;
 }
	 public boolean deleteUserCus(String user, String plant) throws Exception {
			
			boolean flag = false;
			try {
				customerBeanDAO.setmLogger(mLogger);
				flag = customerBeanDAO.deleteUserCustomer(user, plant);
				
			} catch (Exception e) {
				
				throw e;
			}
			return flag;
		}
		public boolean addUserCust(Hashtable ht) throws Exception {
			boolean flag = false;
			try {
				customerBeanDAO.setmLogger(mLogger);
				flag = customerBeanDAO.insertIntoUserCustMst(ht);

			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}

			return flag;
		}

	public ArrayList getCustomerDetails(String aCustno, String plant) {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getCustomerDetails(aCustno, plant);
		} catch (Exception e) {
		}
		return arrList;
	}
	public ArrayList getCustomerDetailsLoan(String aCustno, String plant) {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getCustomerDetailsLoan(aCustno, plant);
		} catch (Exception e) {
		}
		return arrList;
	}
	public ArrayList getCustomerDetailsByIDORHP(String aCustno, String plant) {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getCustomerDetailsByIDORHP(aCustno, plant);
		} catch (Exception e) {
		}
		return arrList;
	}
	public ArrayList getVendorDetailsForPO( String plant,String pono) {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getVendorDetailsForPO( plant,pono);
		} catch (Exception e) {
		}
		return arrList;
	}
	
	//Resvi Add this code for PurchaseEstimate Supplier Details
	public ArrayList getVendorDetailsForPOEST( String plant,String POESTNO) {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getVendorDetailsForPOEST( plant,POESTNO);
		} catch (Exception e) {
		}
		return arrList;
	}
	
//	ends
	
	/*public ArrayList getVendorDetailsForPOMULTIEST( String plant,String aPOMULTIESTNO) {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getVendorDetailsForPOMULTIEST( plant,aPOMULTIESTNO);
		} catch (Exception e) {
		}
		return arrList;
	}*/
	
	
	public ArrayList getVendorDetailsForBILL( String plant,String bill) {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getVendorDetailsForBILL( plant,bill);
		} catch (Exception e) {
		}
		return arrList;
	}    
    public ArrayList getVendorDetails(String aCustno, String plant) {
            ArrayList arrList = new ArrayList();
            try {
                    customerBeanDAO.setmLogger(mLogger);
                    arrList = customerBeanDAO.getVendorDetails(aCustno, plant);
            } catch (Exception e) {
            }
            return arrList;
    }
    public ArrayList getVendorDetailsbyName(String aCustno, String plant) {
    	ArrayList arrList = new ArrayList();
    	try {
    		customerBeanDAO.setmLogger(mLogger);
    		arrList = customerBeanDAO.getVendorDetailsbyName(aCustno, plant);
    	} catch (Exception e) {
    	}
    	return arrList;
    }

	/**
	 * method : getCustomerList4ConMent() description : get Customer List
	 * 
	 * @param
	 * @return ArrayList
	 */
	public ArrayList getCustomerList4ConMent() {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getCustomerList4ConMent();
		} catch (Exception e) {
		}
		return arrList;
	}

	/**
	 * method : getCustomerList() description : get Customer List
	 * 
	 * @param
	 * @return ArrayList
	 */
	public ArrayList getCustomerList() {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getCustomerList();
		} catch (Exception e) {
		}
		return arrList;
	}

	/**
	 * method : isExistCustomer(String aCustCode) description :
	 * 
	 * @param ht
	 * @return
	 */
	public boolean isExistCustomer(String aCustCode) {
		boolean exists = false;
		try {
			customerBeanDAO.setmLogger(mLogger);
			exists = customerBeanDAO.isExistsCustomer(aCustCode);

		} catch (Exception e) {
		}
		return exists;
	}

	public boolean isExistCustomerCode(String aCustCode, String plant) {
		boolean exists = false;
		try {
			customerBeanDAO.setmLogger(mLogger);
			exists = customerBeanDAO.isExistsCustomerCode(aCustCode, plant);

		} catch (Exception e) {
		}
		return exists;
	}

	public boolean isExistCustomer(String aCustCode, String plant) {
		boolean exists = false;
		try {
			customerBeanDAO.setmLogger(mLogger);
			exists = customerBeanDAO.isExistsCustomer(aCustCode, plant);
			
		} catch (Exception e) {
		}
		return exists;
	}
	
	public boolean isExistAppCustomer(String aCustCode,String item,String plant) {
		boolean exists = false;
		try {
			customerBeanDAO.setmLogger(mLogger);
			exists = customerBeanDAO.isExistsAppCustomer(aCustCode,item,plant);
			
		} catch (Exception e) {
		}
		return exists;
	}
	
	public boolean isExistCustomerLoan(String aCustCode, String plant) {
		boolean exists = false;
		try {
			customerBeanDAO.setmLogger(mLogger);
			exists = customerBeanDAO.isExistsCustomerLoan(aCustCode, plant);

		} catch (Exception e) {
		}
		return exists;
	}

	public boolean isExistVendor(String aCustCode, String plant) {
		boolean exists = false;
		try {
			customerBeanDAO.setmLogger(mLogger);
			exists = customerBeanDAO.isExistsVendor(aCustCode, plant);

		} catch (Exception e) {
		}
		return exists;
	}
        
    public boolean isExistVendorName(String aVendname, String plant) {
            boolean exists = false;
            try {
                    customerBeanDAO.setmLogger(mLogger);
                    exists = customerBeanDAO.isExistsVendorName(aVendname, plant);

            } catch (Exception e) {
            }
            return exists;
    }

	/**
	 * method : isExistCustomer(String aCustCode) description :
	 * 
	 * @param ht
	 * @return
	 */
	public boolean updateCustomer(Hashtable htUpdate, Hashtable htCondition) {
		boolean update = false;
		try {
			customerBeanDAO.setmLogger(mLogger);
			update = customerBeanDAO.updateCustomer(htUpdate, htCondition);

		} catch (Exception e) {
		}
		return update;
	}

	public boolean isExistLoanAssignee(String aConsCode, String plant) {
		boolean exists = false;
		try {
			customerBeanDAO.setmLogger(mLogger);
			exists = customerBeanDAO.isExistsLoanAssignee(aConsCode, plant);

		} catch (Exception e) {
		}
		return exists;
	}

	public boolean insertLoanAssignee(Hashtable ht) {
		boolean inserted = false;
		try {
			customerBeanDAO.setmLogger(mLogger);
			inserted = customerBeanDAO.insertIntoLoanAssignee(ht);
		} catch (Exception e) {
		}
		return inserted;
	}

	public boolean updateLoanAssignee(Hashtable htUpdate, Hashtable htCondition) {
		boolean update = false;
		try {
			customerBeanDAO.setmLogger(mLogger);
			update = customerBeanDAO.updateLoanAssignee(htUpdate, htCondition);

		} catch (Exception e) {
		}
		return update;
	}

	public ArrayList getOutGoingToAssigneeDetails(String plant,
			String aCustName, String cond) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getOutGoingToAssigneeDetails(plant,
					aCustName, cond);
		} catch (Exception e) {
		}
		return arrList;
	}

	public boolean updateToAssignee(Hashtable htUpdate, Hashtable htCondition) {
		boolean update = false;
		try {
			customerBeanDAO.setmLogger(mLogger);
			update = customerBeanDAO.updateToAssignee(htUpdate, htCondition);

		} catch (Exception e) {
		}
		return update;
	}

	public ArrayList getToAssingeListStartsWithName(String aCustName,
			String plant) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getToAssingeListStartsWithName(aCustName,
					plant);
		} catch (Exception e) {
		}
		return arrList;
	}

	public boolean isExistToAssignee(String aCustCode, String plant) {
		boolean exists = false;
		try {
			customerBeanDAO.setmLogger(mLogger);
			exists = customerBeanDAO.isExistsToAssignee(aCustCode, plant);

		} catch (Exception e) {
		}
		return exists;
	}

	public ArrayList getToAssigneeDetails(String aCustno, String plant) {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getToAssigneeDetails(aCustno, plant);
		} catch (Exception e) {
		}
		return arrList;
	}

	public boolean deleteToAssignee(String aCustno, String plant) {
		boolean deleted = false;
		try {
			customerBeanDAO.setmLogger(mLogger);
			deleted = customerBeanDAO.deleteToAssignee(aCustno, plant);
		} catch (Exception e) {
		}
		return deleted;
	}

	public boolean insertToAssignee(Hashtable ht) {
		boolean inserted = false;
		try {
			customerBeanDAO.setmLogger(mLogger);
			inserted = customerBeanDAO.insertIntoToAssignee(ht);
		} catch (Exception e) {
		}
		return inserted;
	}

	public boolean deleteLoanAssignee(String aCustno, String plant) {
		boolean deleted = false;
		try {
			customerBeanDAO.setmLogger(mLogger);
			deleted = customerBeanDAO.deleteLoanAssignee(aCustno, plant);
		} catch (Exception e) {
		}
		return deleted;
	}

	public ArrayList getLoanAssigneeDetails(String aConsignee, String plant) {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getLoanAssigneeDetails(aConsignee, plant);
		} catch (Exception e) {
		}
		return arrList;
	}

	public ArrayList getLoanAssigneeListStartsWithName(String aCustName,
			String aCustCode, String plant, String cond) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getLoanAssigneeListStartsWithName(
					aCustName, aCustCode, plant, cond);
		} catch (Exception e) {
		}
		return arrList;
	}

	public boolean updateVendor(Hashtable htUpdate, Hashtable htCondition) {
		boolean update = false;
		try {
			customerBeanDAO.setmLogger(mLogger);
			update = customerBeanDAO.updateVendor(htUpdate, htCondition);

		} catch (Exception e) {
		}
		return update;
	}

	public ArrayList getCustomerListStartsWithName(String aCustName)
			throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getCustomerListStartsWithName(aCustName);
		} catch (Exception e) {
		}
		return arrList;
	}

	public ArrayList getCustomerListStartsWithName(String aCustName,
			String plant) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getCustomerListStartsWithName(aCustName,
					plant);
		} catch (Exception e) {
		}
		return arrList;
	}
	
	public ArrayList getCustomerListStartsWithNamePeppol(String aCustName,
			String plant) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getCustomerListStartsWithNamePeppol(aCustName,
					plant);
		} catch (Exception e) {
		}
		return arrList;
	}
		public ArrayList getCustomerListStartsWithNameActive(String aCustName,
			String plant) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getCustomerListStartsWithNameActive(aCustName,
					plant);
		} catch (Exception e) {
		}
		return arrList;
	}

	public ArrayList getOutGoingCustomerDetails(String plant, String aCustName,
			String cond) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getOutGoingCustomerDetails(plant,
					aCustName, cond);
		} catch (Exception e) {
		}
		return arrList;
	}

	public ArrayList getVendorListStartsWithName(String aCustName,
			String plant, String cond) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getVendorListStartsWithName(aCustName,
					plant, cond);
		} catch (Exception e) {
		}
		return arrList;
	}
	
	public ArrayList getVendorListsWithName(String aCustName,
			String plant, String cond) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getVendorListssWithName(aCustName,
					plant, cond);
		} catch (Exception e) {
		}
		return arrList;
	}
	
	public ArrayList getVendorListsWithNamePeppol(String aCustName,
			String plant, String cond) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getVendorListssWithNamePeppol(aCustName,
					plant, cond);
		} catch (Exception e) {
		}
		return arrList;
	}
	
	public ArrayList getVendorListsWithNameActive(String aCustName,
			String plant, String cond) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getVendorListssWithNameActive(aCustName,
					plant, cond);
		} catch (Exception e) {
		}
		return arrList;
	}

	public ArrayList getPeppolVendorListsWithName(String aCustName,
			String plant,String suppliertype, String cond) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getPeppolVendorListssWithName(aCustName,
					plant,suppliertype, cond);
		} catch (Exception e) {
		}
		return arrList;
	}

	public ArrayList getVendorTypeListStartsWithName(String aCustName,
			String plant, String cond) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getVendorTypeListStartsWithName(aCustName,
					plant, cond);
		} catch (Exception e) {
		}
		return arrList;
	}
	
	public ArrayList getVendorByName(String aCustName, String plant)
			throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getVendorByName(aCustName, plant);
		} catch (Exception e) {
		}
		return arrList;
	}

	// /////////////////////////////////PDA methods///////////////////////////
	public String getCusermerListPDA() {
		String xmlStr = "";
		try {
			ArrayList arrCust = this.getCustomerList4ConMent();

			xmlStr += XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("Customer total ='"
					+ String.valueOf(arrCust.size()) + "'");
			for (int i = 0; i < arrCust.size(); i++) {
				ArrayList arrLine = (ArrayList) arrCust.get(i);
				xmlStr += XMLUtils.getStartNode("record");
				xmlStr += XMLUtils.getXMLNode("CUST_CODE", (String) arrLine
						.get(0));
				xmlStr += XMLUtils.getXMLNode("CUST_NAME", StrUtils
						.replaceCharacters2Send((String) arrLine.get(1)));
				xmlStr += XMLUtils.getEndNode("record");
			}
			xmlStr += XMLUtils.getEndNode("Customer");
		
		}

		catch (Exception e) {
		}
		return xmlStr;
	}

    public ArrayList getCustomerDetailsForDO(String aDono, String plant) {
            ArrayList arrList = new ArrayList();
            try {
                    customerBeanDAO.setmLogger(mLogger);
                    arrList = customerBeanDAO.getCustomerDetailsForDO(aDono, plant);
            } catch (Exception e) {
            }
            return arrList;
    }
    
    public ArrayList getCustomerDetailsForInvoice(String aInoice, String plant) {
        ArrayList arrList = new ArrayList();
        try {
                customerBeanDAO.setmLogger(mLogger);
                arrList = customerBeanDAO.getCustomerDetailsForInvoice(aInoice, plant);
        } catch (Exception e) {
        }
        return arrList;
}
    
    public ArrayList getCustomerDetailsForBill(String plant, String aBill) {
    	ArrayList arrList = new ArrayList();
    	try {
    		customerBeanDAO.setmLogger(mLogger);
    		arrList = customerBeanDAO.getCustomerDetailsForBill(plant, aBill);
    	} catch (Exception e) {
    	}
    	return arrList;
    }
    
    public ArrayList getCustomerDetailsForBillVendmst(String plant, String aBill) {
    	ArrayList arrList = new ArrayList();
    	try {
    		customerBeanDAO.setmLogger(mLogger);
    		arrList = customerBeanDAO.getCustomerDetailsForBillVendmst(plant, aBill);
    	} catch (Exception e) {
    	}
    	return arrList;
    }
    
    //imtinavas detail page
    public ArrayList getCustomerDetailsForTO(String aTono, String plant) {
        ArrayList arrList = new ArrayList();
        try {
                customerBeanDAO.setmLogger(mLogger);
                arrList = customerBeanDAO.getCustomerDetailsForTO(aTono, plant);
        } catch (Exception e) {
        }
        return arrList;
}
    
    public ArrayList getCustomerDetailsForRegister(String custcode, String plant) {
        ArrayList arrList = new ArrayList();
        try {
                customerBeanDAO.setmLogger(mLogger);
                arrList = customerBeanDAO.getCustomerDetailsForRegister(custcode,plant);
        } catch (Exception e) {
        }
        return arrList;
}
    
    public ArrayList getAssigneeDetailsForTO(String aTono, String plant) {
            ArrayList arrList = new ArrayList();
            try {
                    customerBeanDAO.setmLogger(mLogger);
                    arrList = customerBeanDAO.getAssigneeDetailsForTO(aTono, plant);
            } catch (Exception e) {
            }
            return arrList;
    }
    
    public ArrayList getAssigneeDetailsForTONEW(String aTono, String plant) {
        ArrayList arrList = new ArrayList();
        try {
                customerBeanDAO.setmLogger(mLogger);
                arrList = customerBeanDAO.getAssigneeDetailsForTONEW(aTono, plant);
        } catch (Exception e) {
        }
        return arrList;
}
    
    public ArrayList getAssigneeDetailsForLoan(String aOrdno, String plant) {
            ArrayList arrList = new ArrayList();
            try {
                    customerBeanDAO.setmLogger(mLogger);
                    arrList = customerBeanDAO.getAssigneeDetailsforLoan(aOrdno, plant);
            } catch (Exception e) {
            }
            return arrList;
    }
    //---Added by Bruhan on March 18 2014, Description: For Work Order Print Work Order report --ManufacturingModuelsChange 
    public ArrayList getCustomerDetailsForWO(String aWono, String plant) {
        ArrayList arrList = new ArrayList();
        try {
                customerBeanDAO.setmLogger(mLogger);
                arrList = customerBeanDAO.getCustomerDetailsForWO(aWono, plant);
        } catch (Exception e) {
        }
        return arrList;
   }
    //---Added by Bruhan on March 18 2014 end

/* getMobileOrderCustomerDetails() 
     * Created By Bruhan,June 19 2014, To get Mobile Order Customer details
     *  ************Modification History*********************************
     * Bruhan, Aug 20 2014, To handle special characters
     */
  public String getMobileSalesOrderCustomerDetails(String aPlant, String aCustname,String aSearchBy) {
		String xmlStr = "";
        String customerstatusid="",customerstatusdesc="";
		ArrayList al = null;
		try {
			al = customerBeanDAO.selectmobileordercustomerdetails(aPlant, aCustname,aSearchBy, " AND ISACTIVE='Y'");
			if (al.size() > 0) {
			    String trantype="";
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("CustomerDetails total='"+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
		    		Map map = (Map) al.get(i);
					customerstatusid  = (String) map.get("customerstatusid");
					if(customerstatusid == null || customerstatusid.equals(""))
					{
						customerstatusdesc="";
					}
					else
					{
						customerstatusdesc = customerBeanDAO.getCustomerStatusDesc(aPlant,customerstatusid);
					}
		
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("custcode", (String) map.get("CUSTNO"));
					xmlStr += XMLUtils.getXMLNode("custname", StrUtils.replaceCharacters2SendPDA((String)map.get("CNAME")));
					xmlStr += XMLUtils.getXMLNode("custnamelast", StrUtils.replaceCharacters2SendPDA((String)map.get("LNAME")));
					xmlStr += XMLUtils.getXMLNode("contactname", StrUtils.replaceCharacters2SendPDA((String)map.get("CNAME")));
					xmlStr += XMLUtils.getXMLNode("name", StrUtils.replaceCharacters2SendPDA((String)map.get("NAME")));
					xmlStr += XMLUtils.getXMLNode("telno",  (String) map.get("TELNO"));
					xmlStr += XMLUtils.getXMLNode("email", StrUtils.replaceCharacters2SendPDA((String)map.get("EMAIL")));
					xmlStr += XMLUtils.getXMLNode("addr1", StrUtils.replaceCharacters2SendPDA((String)map.get("ADDR1")));
					xmlStr += XMLUtils.getXMLNode("addr2", StrUtils.replaceCharacters2SendPDA((String)map.get("ADDR2")));
					xmlStr += XMLUtils.getXMLNode("addr3", StrUtils.replaceCharacters2SendPDA((String)map.get("ADDR3")));
					xmlStr += XMLUtils.getXMLNode("addr4", StrUtils.replaceCharacters2SendPDA((String)map.get("ADDR4")));
					xmlStr += XMLUtils.getXMLNode("country",  (String) map.get("COUTNRY"));
					xmlStr += XMLUtils.getXMLNode("zip",  (String) map.get("ZIP"));
					xmlStr += XMLUtils.getXMLNode("remarks", StrUtils.replaceCharacters2SendPDA((String)map.get("REMARKS")));
					xmlStr += XMLUtils.getXMLNode("custstatus", StrUtils.replaceCharacters2SendPDA(customerstatusdesc));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("CustomerDetails");
		
			}
		} catch (Exception e) {
			
		}
		return xmlStr;
	}
    
 public ArrayList getCustomerDetailsForEST(String aEstno, String plant) {
        ArrayList arrList = new ArrayList();
        try {
                customerBeanDAO.setmLogger(mLogger);
                arrList = customerBeanDAO.getCustomerDetailsForEST(aEstno, plant);
        } catch (Exception e) {
        }
        return arrList;
}   
public boolean isExistsCustomerType(Hashtable htCT) throws Exception {
		boolean isExists = false;
		try {
			isExists = customerBeanDAO.isExistsCustomerType(htCT);
		} catch (Exception e) {
			throw e;
		}
		return isExists;
	}
	public boolean isExistCustomerType(String customer_type_id,String customer_type_desc, String plant) {
	boolean Exists = false;
	try {
		CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
		customerBeanDAO.setmLogger(mLogger);
		Exists = customerBeanDAO.isExistCustomerType(customer_type_id,customer_type_desc,plant);

	} catch (Exception e) {
	}
	return Exists;
}
	
	public boolean isExistsSupplierType(Hashtable htCT) throws Exception {
	boolean isExists = false;
	try {
		isExists = customerBeanDAO.isExistsSupplierType(htCT);
	} catch (Exception e) {
		throw e;
	}
	return isExists;
}
	
	 public boolean isExistSupplierType(String supplier_type_id,String supplier_type_desc, String plant) {
			boolean Exists = false;
			try {
				CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
				customerBeanDAO.setmLogger(mLogger);
				Exists = customerBeanDAO.isExistSupplierType(supplier_type_id,supplier_type_desc,plant);

			} catch (Exception e) {
			}
			return Exists;
		}
 
public boolean insertSupplierTypeMst(Hashtable ht) throws Exception {
	boolean inserted = false;
	try {
		inserted = customerBeanDAO.insertIntoSupplierTypeMst(ht);
	} catch (Exception e) {
		throw e;
	}
	return inserted;
}

 
 public boolean insertCustomerTypeMst(Hashtable ht) throws Exception {
		boolean inserted = false;
		try {
			inserted = customerBeanDAO.insertIntoCustomerTypeMst(ht);
		} catch (Exception e) {
			throw e;
		}
		return inserted;
	}

	 public boolean deleteSupplierTypeId(Hashtable ht) throws Exception {
		boolean deleted = false;
		try {
			deleted =customerBeanDAO.deleteSupplierTypeId(ht);
		} catch (Exception e) {
			throw e;
		}
		return deleted;
	}
 
 public boolean updateSupplierTypeId(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean update = false;
		try {
			//LocTypeDAO LocDao = new LocTypeDAO();
			//LocDao.setmLogger(mLogger);
			update = customerBeanDAO.updateSupplierTypeMst(htUpdate, htCondition);

		} catch (Exception e) {
			throw e;
		}
		return update;
	}

	
 public boolean deleteCustomerTypeId(Hashtable ht) throws Exception {
		boolean deleted = false;
		try {
			deleted =customerBeanDAO.deleteCustomerTypeId(ht);
		} catch (Exception e) {
			throw e;
		}
		return deleted;
	}
	public boolean updateCustomerTypeId(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean update = false;
		try {
			//LocTypeDAO LocDao = new LocTypeDAO();
			//LocDao.setmLogger(mLogger);
			update = customerBeanDAO.updateCustomerTypeMst(htUpdate, htCondition);

		} catch (Exception e) {
			throw e;
		}
		return update;
	}
	
	public ArrayList getCustTypeList(String aCustTypeId, String plant, String cond) {

		ArrayList al = null;
		try {
			al = customerBeanDAO.getCustomerTypeDetails(aCustTypeId, plant, cond);

		} catch (Exception e) {
		}

		return al;
	}
	
	public ArrayList getVendorTypeList(String aCustTypeId, String plant, String cond) {

		ArrayList al = null;
		try {
			al = customerBeanDAO.getSupplierTypeDetails(aCustTypeId, plant, cond);

		} catch (Exception e) {
		}

		return al;
	}
	
	public ArrayList getSupplierTypeSummary(String aCustTypeId, String plant, String cond) {

		ArrayList al = null;
		try {
			al = customerBeanDAO.getSupplierTypeSummaryDetails(aCustTypeId, plant, cond);

		} catch (Exception e) {
		}

		return al;
	}
	
	
	public ArrayList getSupplierTypeList(String aCustTypeId, String plant, String cond) {

		ArrayList al = null;
		try {
			al = customerBeanDAO.getSupplierTypeDetails(aCustTypeId, plant, cond);

		} catch (Exception e) {
		}

		return al;
	}
	
	public ArrayList getCustomerListWithType(String aCustName,
			String plant,String aCustType) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getCustomerListWithType(aCustName,
					plant,aCustType);
		} catch (Exception e) {
		}
		return arrList;
	}

	public ArrayList getPeppolCustomerListWithType(String aCustName,
			String plant,String aCustType) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getPeppolCustomerListWithType(aCustName,
					plant,aCustType);
		} catch (Exception e) {
		}
		return arrList;
	}
	
	public boolean addCustomerStatus(Hashtable ht) throws Exception {
		boolean flag = false;
		try {
			customerBeanDAO.setmLogger(mLogger);
			flag = customerBeanDAO.insertIntoCustomerStatus(ht);

		} catch (Exception e) {
			//this.mLogger.exception(this.printLog, "", e);
		}

		return flag;
	}
	public ArrayList getAllCustomerStatusDetails(String plant, String cond,String user)
		throws Exception {
		boolean flag = false;
		Hashtable htCondition = new Hashtable();
		ArrayList alResult = new ArrayList();
		htCondition.put("PLANT", plant);
		try {
			customerBeanDAO.setmLogger(mLogger);
			alResult =customerBeanDAO.getCustomerStatusDetails(
					"ISNULL(CUSTOMER_STATUS_ID,'') CUSTOMERSTATUID,ISNULL(CUSTOMER_STATUS_DESC,'') CUSTOMERSTATUSDESC,ISNULL(REMARKS,'') REMARKS,ISNULL(ISACTIVE,'') ISACTIVE" +
					"", htCondition, " ORDER BY CUSTOMER_STATUS_ID ");

		} catch (Exception e) {

			//this.mLogger.exception(this.printLog, "", e);
		}
  	return alResult;
	}
	
	public ArrayList getCustomerStatusDetails(String customerstatusid, String plant,String extraCond,Hashtable ht) throws Exception {
		Hashtable htCondition = new Hashtable();
		ArrayList alResult = new ArrayList();
		try {
			htCondition.put("PLANT", plant);
			alResult =customerBeanDAO.getCustomerStatusDetails(
					"ISNULL(CUSTOMER_STATUS_ID,'') CUSTOMERSTATUID,ISNULL(CUSTOMER_STATUS_DESC,'') CUSTOMERSTATUSDESC,ISNULL(REMARKS,'') REMARKS,ISNULL(ISACTIVE,'') ISACTIVE" +
					"", htCondition, " and CUSTOMER_STATUS_ID like '%" + customerstatusid + "%'  OR CUSTOMER_STATUS_DESC LIKE '" + customerstatusid + "%'  ORDER BY CUSTOMER_STATUS_ID ");

			
		} catch (Exception e) {
			//this.mLogger.exception(this.printLog, "", e);
		}

		return alResult;
	}
	
	
	public ArrayList getCustStatusList(String aCustStatusId, String plant, String cond) {

		ArrayList al = null;
			try {
			al = customerBeanDAO.getCustomerStatusDetails(aCustStatusId, plant, cond);

		} catch (Exception e) {
		}

		return al;
	}
	
	 public boolean isValidCustomerStatusInmst(String aPlant,String aCustomerStatus) throws Exception {
         boolean isValidStatus= false;
     CustomerBeanDAO dao = new CustomerBeanDAO();
     dao.setmLogger(mLogger);
         try {
                 Hashtable ht = new Hashtable();
                  ht.clear();
                 ht.put(IDBConstants.PLANT, aPlant);
                 if(aCustomerStatus.length()>0){
                 ht.put(IDBConstants.CUSTOMERSTATUSID,aCustomerStatus);
                 //isValidStatus = dao.isExisit(ht,"  ISACTIVE ='Y' ");
                 isValidStatus = dao.isExisit(ht,"");
              }
             } catch (Exception e) {
         throw e;
         }
         return isValidStatus;
 }
	 
	 public boolean updateCustomerStatus(Hashtable htValues, Hashtable htCondition,
				String extCond) throws Exception {
			boolean flag = false;
			try {
				StringBuffer sb = new StringBuffer(" set ");
				sb.append("CUSTOMER_STATUS_DESC='" + htValues.get("CUSTOMER_STATUS_DESC") + "'");
				sb.append(",REMARKS='" + htValues.get("REMARKS") + "'");
				sb.append(",ISACTIVE='" + htValues.get("ISACTIVE") + "'");
				sb.append(",UPAT='" + htValues.get("UPAT") + "'");
				sb.append(",UPBY='" + htValues.get("UPBY") + "'");
				customerBeanDAO.setmLogger(mLogger);

				flag = customerBeanDAO.updateCustomerStatus(sb.toString(), htCondition, "");

			} catch (Exception e) {
				//this.mLogger.exception(this.printLog, "", e);
				throw e;
			}

			return flag;
		}
		public boolean deleteCustomerStatus(String custStatusId, String plant) throws Exception {

			boolean flag = false;
			try {
				customerBeanDAO.setmLogger(mLogger);
				flag = customerBeanDAO.deleteCustomerStatus(custStatusId, plant);

			} catch (Exception e) {

				throw e;
			}
			return flag;
		}
	
public boolean isExistCustomerName(String aCustname, String plant) {
            boolean exists = false;
            try {
                    customerBeanDAO.setmLogger(mLogger);
                    exists = customerBeanDAO.isExistsCustomerName(aCustname, plant);

            } catch (Exception e) {
            }
            return exists;
    }
	public ArrayList getCustomerDetailsForTaxInv(String aDono, String plant) {
    ArrayList arrList = new ArrayList();
    try {
            customerBeanDAO.setmLogger(mLogger);
            arrList = customerBeanDAO.getCustomerDetailsForTaxInv(aDono, plant);
    } catch (Exception e) {
    }
    return arrList;
}
	public ArrayList getTopCustomer(String plant, String period, String fromDate, String toDate, String numberOfDecimal) throws Exception {
		ArrayList al = null;
		try {
			_CustMstDAO.setmLogger(mLogger);
			al = _CustMstDAO.getTopCustomer(plant, fromDate, toDate, numberOfDecimal);
		} catch (Exception e) {
			throw e;
		}
		return al;
	}
	public ArrayList getNewCustomers(String plant) throws Exception {
		ArrayList al = null;
		try {
			_CustMstDAO.setmLogger(mLogger);
			al = _CustMstDAO.getNewCustomers(plant);
		} catch (Exception e) {
			throw e;
		}
		return al;
	}
	public ArrayList getCustomerTypeListStartsWithName(String aCustName,
			String plant, String cond) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getCustomerTypeListStartsWithName(aCustName,
					plant, cond);
		} catch (Exception e) {
		}
		return arrList;
	}
	public ArrayList getCustomerListStartsWithName(String aCustName,
			String plant, String cond) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getCustomerListStartsWithName(aCustName,
					plant, cond);
		} catch (Exception e) {
		}
		return arrList;
	}
	public ArrayList getEmployeeListNameForPOSCashier(String aCustName,
			String plant, String cond) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getEmployeeListNameForPOSCashier(aCustName,
					plant, cond);
		} catch (Exception e) {
		}
		return arrList;
	}
	public ArrayList getEmployeeListNameForPOSsales(String aCustName,
			String plant, String cond) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getEmployeeListNameForPOSsales(aCustName,
					plant, cond);
		} catch (Exception e) {
		}
		return arrList;
	}
	public ArrayList getEmployeeListStartsWithName(String aCustName,
			String plant, String cond) throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			customerBeanDAO.setmLogger(mLogger);
			arrList = customerBeanDAO.getEmployeeListStartsWithName(aCustName,
					plant, cond);
		} catch (Exception e) {
		}
		return arrList;
	}
	
	
}
