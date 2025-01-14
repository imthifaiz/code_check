package com.track.pda;


/* ********** Modification History *************************************
 *  Bruhan on July 9 2014, Description:To include LocTypeDAO
 * 
 * 
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.MovHisDAO;
import com.track.db.util.InvMstUtil;
import com.track.db.util.LocUtil;
import com.track.db.util.POUtil;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;
import com.track.dao.LocTypeDAO;

import java.util.ArrayList;

public class InvQueryServlet extends HttpServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.InvQueryServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.InvQueryServlet_PRINTPLANTMASTERINFO;
	private static final long serialVersionUID = 7464773384488791527L;
	private POUtil _POUtil = null;
	private InvMstUtil _InvMstUtil = null;
	private InvMstDAO _InvMstDAO = null;
	private String PLANT = "";
	private String xmlStr = "";
	private String action = "";

	private static final String CONTENT_TYPE = "text/xml";

	public void init() throws ServletException {
		_POUtil = new POUtil();
		_InvMstDAO = new InvMstDAO();
		_InvMstUtil = new InvMstUtil();
	}

	 /********** Modification History *************************************
	 * Bruhan on July 9 2014, Description:To include 'load_Inv_details_For_Loc_With_LocType' for Mobile sales Inventory Query by Loc
	 * Bruhan on July 9 2014, Description:To include 'validate_location_type' for Mobile sales Inventory Query by Loc
	 * *Bruhan on July 9 2014, Description:To include 'load_item_details_with_types' for Mobile sales Inventory Query by product
	 */
		
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		try {
			action = request.getParameter("action").trim();
			this.setMapDataToLogger(this.populateMapData(StrUtils
					.fString(request.getParameter("PLANT")), StrUtils
					.fString(request.getParameter("LOGIN_USER"))
					+ " PDA_USER"));
			_POUtil.setmLogger(mLogger);
			_InvMstDAO.setmLogger(mLogger);
			_InvMstUtil.setmLogger(mLogger);
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			if (action.equalsIgnoreCase("load_item_details")) {
				xmlStr = "";
				xmlStr = load_item_Details(request, response);
			}
		    if (action.equalsIgnoreCase("validate_location")) {
		            xmlStr = "";
		            xmlStr = validate_location(request, response);
		    }

		    
           if (action.equalsIgnoreCase("load_Inv_details_For_Loc")) {
                    xmlStr = "";
                    xmlStr = load_Inv_Details_For_Loc(request, response);
           }
		   else if (action.equalsIgnoreCase("process_receiveMaterial")) {
				xmlStr = process_orderReceiving(request, response);
			}else if(action.equalsIgnoreCase("loadAvailableInvQty")) {
                   xmlStr = loadAvailableInvQty(request, response);
            }
			else if (action.equalsIgnoreCase("load_Inv_details_For_Loc_With_LocType")) {
               xmlStr = "";
               xmlStr = load_Inv_Details_For_Loc_With_LocType(request, response);
           }
			else if (action.equalsIgnoreCase("validate_location_type")) {
	               xmlStr = "";
	               xmlStr = validate_location_type(request, response);
	       }
			else if (action.equalsIgnoreCase("load_item_details_with_types")) {
				xmlStr = "";
				xmlStr = load_item_Details_with_types(request, response);
			}
			else if (action.equalsIgnoreCase("Update_ExpireDate")) {
				xmlStr = "";
				xmlStr = UpdateInvItemExpiryDate(request, response);
			}
			else if (action.equalsIgnoreCase("load_Inv_details_For_Loc_PDA")) {
               xmlStr = "";
               xmlStr = load_Inv_Details_For_Loc_PDA(request, response);
      }
			else if (action.equalsIgnoreCase("load_Allitem_details_PDA")) {
	               xmlStr = "";
	               xmlStr = load_Allitem_details_PDA(request, response);
	      }
			else if (action.equalsIgnoreCase("load_Inv_search_details_For_Loc_PDA")) {
	               xmlStr = "";
	               xmlStr = load_Inv_Search_Details_For_Loc_PDA(request, response);
	      }
			else if (action.equalsIgnoreCase("load_item_Details_PDA")) {
	               xmlStr = "";
	               xmlStr = load_item_Details_PDA(request, response);
	      }
			else if (action.equalsIgnoreCase("load_Inv_Search_Details_For_Prd_Loc_PDA")) {
	               xmlStr = "";
	               xmlStr = load_Inv_Search_Details_For_Prd_Loc_PDA(request, response);
	      }
			else if (action.equalsIgnoreCase("load_Inv_Details_For_Loc_PDA_AutoSuggestion")) {
	               xmlStr = "";
	               xmlStr = load_Inv_Details_For_Loc_PDA_AutoSuggestion(request, response);
	      }
           
           
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(1, "Unable to process? "
					+ e.getMessage());
		}
		out.write(xmlStr);
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}
        
       private String loadAvailableInvQty(HttpServletRequest request,
                HttpServletResponse response) {
        String xmlStr = "";
        java.sql.Connection con = null;
        try {

                String aPlant = StrUtils.fString(request.getParameter("PLANT"));
                String aItem = StrUtils.fString(request.getParameter("ITEM"));
                String batch = StrUtils.fString(request.getParameter("BATCH"));
                String loc = StrUtils.fString(request.getParameter("LOC"));
                String userID = StrUtils.fString(request.getParameter("LOGIN_USER"));

                UserLocUtil userLocUtil = new UserLocUtil();
                userLocUtil.setmLogger(mLogger);
                String condAssignedLocforUser = " "; 
                condAssignedLocforUser =  userLocUtil.getUserLocAssigned(userID,aPlant,"LOC");
                ArrayList<Map<String, String>> resultV = new ArrayList<>();
              
                StringBuffer sql = new StringBuffer("SELECT USERFLD4 , QTY from "
                                + aPlant + "_INVMST WHERE LOC='" + loc + "' AND USERFLD4='"+batch+"' AND PLANT='"
                                + aPlant + "' AND ITEM='" + aItem + "' AND QTY >0 "+condAssignedLocforUser);

           
                    con = DbBean.getConnection();
                    InvMstDAO itM = new InvMstDAO();
                    ItemMstDAO itemM = new ItemMstDAO();
                    itemM.setmLogger(mLogger);
                    itM.setmLogger(mLogger);
                    this.mLogger.query(itM.isPrintQuery(), sql.toString());

                    resultV = itM.selectData(con, sql.toString());
                if (resultV.size() > 0) {
                        xmlStr = XMLUtils.getXMLHeader();
                        xmlStr += XMLUtils.getStartNode("InvAvailQty");
                        for (Map<String, String> map : resultV) {
                                xmlStr += XMLUtils.getXMLNode("status", "0");
                                xmlStr += XMLUtils.getXMLNode("description", "");
                                xmlStr += XMLUtils.getXMLNode("AVAILQTY", (String) map.get("QTY"));
                                

                        }
                        xmlStr += XMLUtils.getEndNode("InvAvailQty");

                } else {
                        xmlStr = XMLUtils.getXMLMessage(1, "Not a valid batch ");
                }

        }

        catch (Exception e) {
                this.mLogger.exception(this.printLog, "", e);
                xmlStr = XMLUtils.getXMLMessage(1, e.getMessage());
        }finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
        return xmlStr;
    }


	private String load_item_Details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aItemNum = "",UOM = "";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aItemNum = StrUtils.fString(request.getParameter("ITEM_NUM"));
			UOM=StrUtils.fString(request.getParameter("UOM"));
			ItemMstDAO itemMstDAO = new ItemMstDAO();
			itemMstDAO.setmLogger(mLogger);

			aItemNum = itemMstDAO.getItemIdFromAlternate(PLANT, aItemNum);

			if (aItemNum == null) {
				str = XMLUtils.getXMLMessage(1, "Product details not found!");
			} else {

				Hashtable<String, String> ht = new Hashtable<String, String>();
				ht.put("plant", PLANT);
				ht.put("item", aItemNum);
				_InvMstUtil.setmLogger(mLogger);
				str = _InvMstUtil.getItemDetails(PLANT, aItemNum, UOM);
			}
			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Product details not found");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}
		return str;
	}
        
        
    private String validate_location(HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException,
                    Exception {
           
            try {

                    PLANT = StrUtils.fString(request.getParameter("PLANT"));
                    String loc = StrUtils.fString(request.getParameter("LOC"));
                    String userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
                    
                    UserLocUtil uslocUtil = new UserLocUtil();
                    uslocUtil.setmLogger(mLogger);
                    boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,userID,loc);
                    if (!isvalidlocforUser) {
                        throw new Exception(" Loc :"+loc+" is not Valid Location/Not a User Assigned Location");
                    }
                   
                        xmlStr = XMLUtils.getXMLHeader();
                        xmlStr = xmlStr + XMLUtils.getStartNode("locationDetails");
                        xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
                        xmlStr = xmlStr+ XMLUtils.getXMLNode("description", "Location found");
                        xmlStr = xmlStr + XMLUtils.getEndNode("locationDetails");
                    
                    

                    if (xmlStr.equalsIgnoreCase("")) {
                            throw new Exception("Location not found");
                    }
            } catch (Exception e) {
                xmlStr = XMLUtils.getXMLHeader();
                xmlStr = xmlStr + XMLUtils.getStartNode("locationDetails");
                xmlStr = xmlStr + XMLUtils.getXMLNode("status", "1");
                xmlStr = xmlStr+ XMLUtils.getXMLNode("description",e.getMessage());
                xmlStr = xmlStr + XMLUtils.getEndNode("locationDetails");
            }
            // MLogger.log(0, "validate_location() Ends");
            return xmlStr;
    }
        
    private String load_Inv_Details_For_Loc(HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException {
            String str = "", aLoc = "" , uom ="";
            try {

                    PLANT = StrUtils.fString(request.getParameter("PLANT"));
                    aLoc = StrUtils.fString(request.getParameter("LOC"));
                    uom = StrUtils.fString(request.getParameter("UOM"));
                    _InvMstUtil.setmLogger(mLogger);
                   
                    str = _InvMstUtil.getInvItemDetailsForLoc(PLANT, aLoc, uom);
                  
                    if (str.equalsIgnoreCase("")) {
                            str = XMLUtils.getXMLMessage(1, "Location details not found");
                    }

            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    str = XMLUtils.getXMLMessage(1, e.getMessage());
            }
            return str;
    }

	private String process_orderReceiving(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		Map<String, String> receiveMaterial_HM = null;
		String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "";
		String LOGIN_USER = "";
		String ITEM_BATCH = "", ITEM_QTY = "", ITEM_EXPDATE = "", ITEM_LOC = "";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			PO_NUM = StrUtils.fString(request.getParameter("PO_NUM"));
			PO_LN_NUM = StrUtils.fString(request.getParameter("PO_LN_NUM"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEM_NUM"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));

			ITEM_BATCH = StrUtils.fString(request.getParameter("ITEM_BATCH"));
			ITEM_QTY = StrUtils.fString(request.getParameter("ITEM_QTY"));
			ITEM_EXPDATE = StrUtils.fString(request
					.getParameter("ITEM_EXPDATE"));

			ITEM_LOC = StrUtils.fString(request.getParameter("ITEM_LOC"));

			if (ITEM_EXPDATE.length() > 8) {
				ITEM_EXPDATE = ITEM_EXPDATE.substring(6, 10) + "-"
						+ ITEM_EXPDATE.substring(3, 5) + "-"
						+ ITEM_EXPDATE.substring(0, 2);
			}

			receiveMaterial_HM = new HashMap<String, String>();
			receiveMaterial_HM.put(IConstants.PLANT, PLANT);
			receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
			receiveMaterial_HM.put(IConstants.PODET_PONUM, PO_NUM);
			receiveMaterial_HM.put(IConstants.PODET_POLNNO, PO_LN_NUM);
			receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _POUtil
					.getCustCode(PLANT, PO_NUM));
			receiveMaterial_HM.put(IConstants.INV_BATCH, ITEM_BATCH);
			receiveMaterial_HM.put(IConstants.INV_QTY, ITEM_QTY);
			receiveMaterial_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);

			receiveMaterial_HM.put(IConstants.JOB_NUM, _POUtil.getJobNum(PLANT,
					PO_NUM));

			xmlStr = "";

			// check for item in location
			Hashtable<String, String> htLocMst = new Hashtable<String, String>();
			htLocMst.put("plant", receiveMaterial_HM.get(IConstants.PLANT));
			htLocMst.put("item", receiveMaterial_HM.get(IConstants.ITEM));
			htLocMst.put("loc", receiveMaterial_HM.get(IConstants.LOC));
			boolean flag = true;

			if (flag) {

				xmlStr = _POUtil.process_ReceiveMaterial(receiveMaterial_HM);
			} else {
				throw new Exception("Product Received already ");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);

			throw e;
		}

		return xmlStr;
	}

	@SuppressWarnings("unchecked")
	private void diaplayInfoLogs(HttpServletRequest request) {
		try {
			Map requestParameterMap = request.getParameterMap();
			Set<String> keyMap = requestParameterMap.keySet();
			StringBuffer requestParams = new StringBuffer();
			requestParams.append("Class Name : " + this.getClass() + "\n");
			requestParams.append("Paramter Mapping : \n");
			for (String key : keyMap) {
				requestParams.append("[" + key + " : "
						+ request.getParameter(key) + "] ");
			}
			this.mLogger.auditInfo(this.printInfo, requestParams.toString());

		} catch (Exception e) {

		}

	}
	 /*---Added by Bruhan on July 9 2014, Description:PDA Inventory Query by location with location type
	 ********** Modification History *************************************
	 * 
	 * 
	 * 
	 */
	private String load_Inv_Details_For_Loc_With_LocType(HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
    String str = "", aLoc = "",aLocType="";
    try {

            PLANT = StrUtils.fString(request.getParameter("PLANT"));
            aLoc = StrUtils.fString(request.getParameter("LOC"));
            aLocType = StrUtils.fString(request.getParameter("LOC_TYPE"));
           
            _InvMstUtil.setmLogger(mLogger);
           
            str = _InvMstUtil.getInvItemDetailsForLocWithLocType(PLANT, aLoc,aLocType);
          
            if (str.equalsIgnoreCase("")) {
                    str = XMLUtils.getXMLMessage(1, "Product details not found!");
            }

    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
            str = XMLUtils.getXMLMessage(1, e.getMessage());
    }
    return str;
	}
	
	 /*---Added by Bruhan on July 9 2014, Description:To PDA Inventory Query by location with product class,type and brand
	 ********** Modification History *************************************
	 * 
	 * 
	 * 
	 */
	private String load_item_Details_with_types(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aItemNum = "",searchbyone="",searchvalueone="";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aItemNum = StrUtils.fString(request.getParameter("ITEM_NUM"));
			searchbyone= StrUtils.fString(request.getParameter("SEARCH_BY_ONE"));
			searchvalueone= StrUtils.fString(request.getParameter("SEARCH_VALUE_ONE"));
			
			ItemMstDAO itemMstDAO = new ItemMstDAO();
			itemMstDAO.setmLogger(mLogger);
			 
			 if(aItemNum.length() > 0)
			 {
				 aItemNum = itemMstDAO.getItemIdFromAlternate(PLANT, aItemNum);
				 if (aItemNum == null ) {
					str = XMLUtils.getXMLMessage(1, "Product details not found!");
				  } 
		      }
	
			Hashtable<String, String> ht = new Hashtable<String, String>();
			ht.put("plant", PLANT);
			ht.put("item", aItemNum);
			_InvMstUtil.setmLogger(mLogger);
			str = _InvMstUtil.getItemDetailsWithType(PLANT, aItemNum,searchbyone,searchvalueone);
			
			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Product details not found!");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}
		return str;
	}
	 /*---Added by Bruhan on July 9 2014, Description:To validate Location Type 
	 ********** Modification History *************************************
	 * 
	 * 
	 * 
	 */
	 private String validate_location_type(HttpServletRequest request,
             HttpServletResponse response) throws IOException, ServletException,
             Exception {
         try {

             PLANT = StrUtils.fString(request.getParameter("PLANT"));
             String loctype = StrUtils.fString(request.getParameter("LOC_TYPE"));
             String userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
              LocTypeDAO _LocTypeDAO = new LocTypeDAO();
             _LocTypeDAO.setmLogger(mLogger);
             Hashtable ht = new Hashtable();
             ht.put("PLANT", PLANT);
             ht.put("LOC_TYPE_ID", loctype);
             boolean isExists =  _LocTypeDAO.isExists(ht);
             if (!isExists) {
                 throw new Exception(loctype+" is not valid location type");
             }
             else
             {
                 xmlStr = XMLUtils.getXMLHeader();
                 xmlStr = xmlStr + XMLUtils.getStartNode("locationTypeDetails");
                 xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
                 xmlStr = xmlStr+ XMLUtils.getXMLNode("description", "Location Type found");
                 xmlStr = xmlStr + XMLUtils.getEndNode("locationTypeDetails");
             }
             if (xmlStr.equalsIgnoreCase("")) {
                     throw new Exception("Location Type not found");
             }
     } catch (Exception e) {
         xmlStr = XMLUtils.getXMLHeader();
         xmlStr = xmlStr + XMLUtils.getStartNode("locationTypeDetails");
         xmlStr = xmlStr + XMLUtils.getXMLNode("status", "1");
         xmlStr = xmlStr+ XMLUtils.getXMLNode("description",e.getMessage());
         xmlStr = xmlStr + XMLUtils.getEndNode("locationTypeDetails");
     }
     // MLogger.log(0, "validate_location() Ends");
     return xmlStr;
}
 private String UpdateInvItemExpiryDate(HttpServletRequest request,
				HttpServletResponse response) throws ServletException, IOException,
				Exception {
			
			boolean flag = false;
			StrUtils StrUtils = new StrUtils();
			String PLANT = "", INVITEM = "",ITEM_BATCH="",EXPIRYDATE="",REMARKS="",RSNCODE="",OLDEXPIRY="";
			String LOGIN_USER="",SNO="";
			UserTransaction ut = null;
			MovHisDAO movHisDao = new MovHisDAO();
			DateUtils dateUtils = new DateUtils();
			try {

				HttpSession session = request.getSession();
	            
	            
				PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
				String[] chkditems  = request.getParameterValues("chkitem");
				LOGIN_USER = StrUtils.fString((String) session.getAttribute("LOGIN_USER")).trim();
				REMARKS = StrUtils.fString(request.getParameter("REMARKS"));
				RSNCODE = StrUtils.fString(request.getParameter("REASONCODE"));
				
				InvMstDAO invMstDAO = new InvMstDAO();
				invMstDAO.setmLogger(mLogger);
				
				 ut = DbBean.getUserTranaction();
		         ut.begin();
				process: 	
				if (chkditems != null)    {     
					for (int i = 0; i < chkditems.length; i++)       { 
						String item = chkditems[i];
						String itemArray[] = item.split(",");
						SNO = itemArray[0];
						INVITEM = itemArray[1];
						ITEM_BATCH = itemArray[2];
						//String batch = ITEM_BATCH.replaceAll("\\W", ""); 
						//String lnno = INVITEM+"_"+batch;
						
						EXPIRYDATE = StrUtils.fString(request.getParameter("EXPIRYDATE_"+SNO));
						OLDEXPIRY = StrUtils.fString(request.getParameter("OLDEXPIRE_"+SNO));
						
						
					Hashtable htexpiry = new Hashtable();
					htexpiry.put(IConstants.PLANT, PLANT);
					htexpiry.put(IConstants.ITEM, INVITEM);
					htexpiry.put("USERFLD4", ITEM_BATCH);
					
					String UpdateInvexpiry = "SET EXPIREDATE ='" +EXPIRYDATE +"' ";
					
					flag =  invMstDAO.update(UpdateInvexpiry, htexpiry, "");
					if(flag)
					{
						Hashtable htmovHis = new Hashtable();
						htmovHis.clear();
						htmovHis.put(IDBConstants.PLANT, PLANT);
						htmovHis.put("DIRTYPE", TransactionConstants.UPDATE_EXPIREDATE);
						htmovHis.put(IDBConstants.ITEM, INVITEM);
						htmovHis.put("MOVTID", "");
						htmovHis.put("RECID", "");
						htmovHis.put(IDBConstants.LOC, "");
						htmovHis.put(IDBConstants.CREATED_BY, LOGIN_USER);
						if(OLDEXPIRY.length()>0)
						{
							htmovHis.put(IDBConstants.REMARKS, "Old:"+OLDEXPIRY+",New:"+EXPIRYDATE+" "+REMARKS+" "+RSNCODE);
						}
						else
						{
							htmovHis.put(IDBConstants.REMARKS, EXPIRYDATE+" "+REMARKS+" "+RSNCODE);
						}
						htmovHis.put("BATNO", ITEM_BATCH);
						htmovHis.put("QTY", "0");
						htmovHis.put(IDBConstants.TRAN_DATE,  DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htmovHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());

		            	flag = movHisDao.insertIntoMovHis(htmovHis);
					}
					if(!flag)
						break process;
					}
				}
		
				if (flag) {
					
					DbBean.CommitTran(ut);				
					request.getSession().setAttribute(
							"RESULT",
							"Expire Date Updated successfully!");
					response.sendRedirect("../purchasetransaction/inventoryexpdate?action=view&result=result&PLANT="
							+ PLANT );
				} else {
					DbBean.RollbackTran(ut);
					request.getSession()
							.setAttribute(
									"RESULTERROR",
									"Failed to update the Expire Date ");
											
					response.sendRedirect("../purchasetransaction/inventoryexpdate?result=resulterror");
				}
				
				}
				
			 catch (Exception e) {
				DbBean.RollbackTran(ut);
				this.mLogger.exception(this.printLog, "", e);
				request.getSession().setAttribute("CATCHERROR", e.getMessage());
				response
						.sendRedirect("../purchasetransaction/inventoryexpdate?action=view&result=catchrerror&PLANT="
								+ PLANT  
								+"&result=catchrerror");
				throw e;
			}
			
			return xmlStr;
		}

	private String load_Inv_Details_For_Loc_PDA(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "";

		String PLANT = "", userID = "",loc = "",start="",end="";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			loc = StrUtils.fString(request.getParameter("LOC"));
			start = StrUtils.fString(request.getParameter("START"));
			end = StrUtils.fString(request.getParameter("END"));
			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("ITEMNO", loc);
			ht.put("START", start);
			ht.put("END", end);
			LocUtil locUtil = new LocUtil();
			locUtil.setmLogger(mLogger);
			str = locUtil.getlocationsForInvPDA(PLANT,start,end,loc);
			//str = locUtil.getlocationsWithDesc(PLANT, userID);
			if (str.equalsIgnoreCase("")) {

				str = XMLUtils.getXMLMessage(1, "Details not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return str;
	}
	private String load_Allitem_details_PDA(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "",start="",end="",itemNo;
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			itemNo = StrUtils.fString(request.getParameter("ITEMNO"));
			start = StrUtils.fString(request.getParameter("START"));
			end = StrUtils.fString(request.getParameter("END"));
			
			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("ITEMNO", itemNo);
			ht.put("START", start);
			ht.put("END", end);

			str = _POUtil.getItemFromProductMst(PLANT,start,end,itemNo);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Item details not found?");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}
		return str;
	}
    private String load_Inv_Search_Details_For_Loc_PDA(HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
    String str = "", aLoc = "" , uom ="",item ="";
    try {

            PLANT = StrUtils.fString(request.getParameter("PLANT"));
            aLoc = StrUtils.fString(request.getParameter("LOC"));
            uom = StrUtils.fString(request.getParameter("UOM"));
            item = StrUtils.fString(request.getParameter("ITEM"));
            _InvMstUtil.setmLogger(mLogger);
           
            str = _InvMstUtil.getInvItemDetailsForLoc_PDA(PLANT, aLoc, uom , item);
          
            if (str.equalsIgnoreCase("")) {
                    str = XMLUtils.getXMLMessage(1, "Location details not found");
            }

    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
            str = XMLUtils.getXMLMessage(1, e.getMessage());
    }
    return str;
}
	private String load_item_Details_PDA(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aItemNum = "",UOM = "",LOC;
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aItemNum = StrUtils.fString(request.getParameter("ITEM_NUM"));
			UOM=StrUtils.fString(request.getParameter("UOM"));
			LOC=StrUtils.fString(request.getParameter("LOC"));
			
			ItemMstDAO itemMstDAO = new ItemMstDAO();
			itemMstDAO.setmLogger(mLogger);

			aItemNum = itemMstDAO.getItemIdFromAlternate(PLANT, aItemNum);

			if (aItemNum == null) {
				str = XMLUtils.getXMLMessage(1, "Product details not found!");
			} else {

				Hashtable<String, String> ht = new Hashtable<String, String>();
				ht.put("plant", PLANT);
				ht.put("item", aItemNum);
				_InvMstUtil.setmLogger(mLogger);
				str = _InvMstUtil.getItemDetails_PDA(PLANT, aItemNum, UOM, LOC);
			}
			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Product details not found");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}
		return str;
	}
	 private String load_Inv_Search_Details_For_Prd_Loc_PDA(HttpServletRequest request,
	            HttpServletResponse response) throws IOException, ServletException {
	    String str = "", aLoc = "" , uom ="",item ="";
	    try {

	            PLANT = StrUtils.fString(request.getParameter("PLANT"));
	            aLoc = StrUtils.fString(request.getParameter("LOC"));
	            uom = StrUtils.fString(request.getParameter("UOM"));
	            item = StrUtils.fString(request.getParameter("ITEM"));
	            _InvMstUtil.setmLogger(mLogger);
	           
	            str = _InvMstUtil.getInvItemDetailsFor_Prd_Loc_PDA(PLANT, aLoc, uom , item);
	          
	            if (str.equalsIgnoreCase("")) {
	                    str = XMLUtils.getXMLMessage(1, "Location details not found");
	            }

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	            str = XMLUtils.getXMLMessage(1, e.getMessage());
	    }
	    return str;
	}
	//created by vicky Used for PDA Location order AutoSuggestion
	 private String load_Inv_Details_For_Loc_PDA_AutoSuggestion(HttpServletRequest request,
				HttpServletResponse response) throws IOException, ServletException,
				Exception {
			String str = "";

			String PLANT = "", userID = "",loc = "",start="",end="";
			try {
				PLANT = StrUtils.fString(request.getParameter("PLANT"));
				userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
				loc = StrUtils.fString(request.getParameter("LOC"));
				Hashtable ht = new Hashtable();
				ht.put("PLANT", PLANT);
				ht.put("ITEMNO", loc);
				LocUtil locUtil = new LocUtil();
				locUtil.setmLogger(mLogger);
				str = locUtil.getlocationsForInvPDAAutoSuggestion(PLANT,loc);
				if (str.equalsIgnoreCase("")) {

					str = XMLUtils.getXMLMessage(1, "Details not found");
				}
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			}
			return str;
		}

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

	}

	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		this.mLogger.setLoggerConstans(dataForLogging);
	}

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

}