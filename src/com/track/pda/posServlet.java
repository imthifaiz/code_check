
package com.track.pda;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.ItemMstDAO;
import com.track.dao.POSDetDAO;
import com.track.dao.POSHdrDAO;
import com.track.db.util.InvMstUtil;

import com.track.db.util.LocUtil;
import com.track.db.util.POSUtil;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.tables.ITEMMST;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

import java.util.Vector;

public class posServlet extends HttpServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.PosServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.PosServlet_PRINTPLANTMASTERINFO;

	private static final long serialVersionUID = -1710895417460542434L;
	private InvMstUtil _InvMstUtil = null;
        StrUtils su =null;
	private POSHdrDAO _posMstDAO = new POSHdrDAO();
	private String xmlStr = "";
	private String action = "";
	private static final String CONTENT_TYPE = "text/xml";

	public void init() throws ServletException {
		_InvMstUtil = new InvMstUtil();
                su = new StrUtils();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();

		try {
			action = request.getParameter("action").trim();
			this.setMapDataToLogger(this.populateMapData(StrUtils.fString(request.getParameter("PLANT")), StrUtils.fString(request.getParameter("LOGIN_USER"))+ " PDA_USER"));
			_posMstDAO.setmLogger(mLogger);
			_InvMstUtil.setmLogger(mLogger);
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			if (action.equalsIgnoreCase("getProductDetailsForTranId")) { // for Viewing
				xmlStr = "";
				xmlStr = this.load_item_detailsforTranID(request, response);
			}
		        else if (action.equalsIgnoreCase("generate_TranID")) { // generate new TRANID
                               xmlStr = "";
                               xmlStr = generate_TranID(request, response);
		        }
                        else if (action.equalsIgnoreCase("get_Tranid_list")) { // to List open and Hold TranId to View
				xmlStr = "";
				xmlStr = this.load_transactionList(request, response);
			}

			else if (action.equalsIgnoreCase("get_product_list")) { // get Product List
				xmlStr = "";
				xmlStr = load_products(request, response);
			}
		    else if (action.equalsIgnoreCase("load_locations")) {
		            xmlStr = "";
		            xmlStr = load_locations(request, response);
		    }
                        else if (action.equalsIgnoreCase("add_products")) { // add product
                                xmlStr = "";
                                xmlStr = process_Add_Item(request, response);
                        }
			else if (action.equalsIgnoreCase("holdTranIdDetails")) {
				xmlStr = "";
				xmlStr =holdTranIdDetails(request, response);
			} else if (action.equalsIgnoreCase("delete_product_in_TranID")) {
				xmlStr = "";
				xmlStr = deleteTranIdDetails(request, response);
			} else if (action.equalsIgnoreCase("validate_product")) {
				xmlStr = "";
				xmlStr = validate_item(request, response);
			} 
                        else if (action.equalsIgnoreCase("validate_tranId")) {
                                    xmlStr = "";
                                    xmlStr = validate_TranId(request, response);
                        } else {
				throw new Exception("The requested action not found in the server ");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		out.write(xmlStr);
		out.close();
	}

	
    private String validate_item(HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException,
                    Exception {
            String str = "", aPlant = "", item = "";
            try {

                    aPlant = StrUtils.fString(request.getParameter("PLANT"));
                    item = StrUtils.fString(request.getParameter("ITEM_NUM"));

                    Hashtable ht = new Hashtable();
                    ht.put("PLANT", aPlant);
                    ht.put("ITEM", item);
                    ItemMstDAO itM = new ItemMstDAO();
                    itM.setmLogger(mLogger);
                    boolean itemFound = true;
                    String scannedItemNo = itM.getItemIdFromAlternate(aPlant, item);
                    if (scannedItemNo == null) {
                            itemFound = false;
                    }
                    xmlStr = XMLUtils.getXMLHeader();
                    xmlStr = xmlStr + XMLUtils.getStartNode("itemDetails");

                    if (itemFound) {
                            xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
                            xmlStr = xmlStr
                                            + XMLUtils.getXMLNode("description", "Product found");
                    } else {
                            xmlStr = xmlStr + XMLUtils.getXMLNode("status", "1");
                            xmlStr = xmlStr
                                            + XMLUtils.getXMLNode("description",
                                                            "Product not found");
                    }

                    xmlStr = xmlStr + XMLUtils.getEndNode("itemDetails");

                    if (xmlStr.equalsIgnoreCase("")) {
                            throw new Exception("Product not found");
                    }
            } catch (Exception e) {
                    throw e;
            }
            // MLogger.log(0, "validate_item() Ends");
            return xmlStr;
    }


    private String validate_TranId(HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException,
                    Exception {
            String str = "", aPlant = "", tranID = "";
            try {

                    aPlant = StrUtils.fString(request.getParameter("PLANT"));
                    tranID = StrUtils.fString(request.getParameter("TRANID"));

                   
                    POSHdrDAO dao = new POSHdrDAO();
                    dao.setmLogger(mLogger);
                     Hashtable ht = new Hashtable();
                     ht.put("PLANT",aPlant);
                     ht.put("POSTRANID",tranID);
                     String extraCon ="  ISNULL(STATUS,'') NOT IN ('C') ";
                     boolean isDataPresent = dao.isExisit(ht, extraCon);
                    
                    xmlStr = XMLUtils.getXMLHeader();
                    xmlStr = xmlStr + XMLUtils.getStartNode("tranDetails");

                    if (isDataPresent) {
                            xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
                            xmlStr = xmlStr + XMLUtils.getXMLNode("description", "TranId found");
                    } else {
                            xmlStr = xmlStr + XMLUtils.getXMLNode("status", "1");
                            xmlStr = xmlStr+ XMLUtils.getXMLNode("description","Not a Valid TranID");
                    }

                    xmlStr = xmlStr + XMLUtils.getEndNode("tranDetails");

                    if (xmlStr.equalsIgnoreCase("")) {
                            throw new Exception("Not a Valid TranID");
                    }
            } catch (Exception e) {
                    throw e;
            }
            // MLogger.log(0, "validate_item() Ends");
            return xmlStr;
    }

	private String getLocationsFromLocationMaster(HttpServletRequest request,
			HttpServletResponse response) {
		String str = "", aPlant = "", aUser = "";
		try {

			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aUser = StrUtils.fString(request.getParameter("LOGIN_USER"));
			_InvMstUtil.setmLogger(mLogger);

			str = _InvMstUtil.loadLocationsXml(aPlant, aUser);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "No locations found ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}
		return str;
	}

	private String getLocationsFromLocationMaster1(HttpServletRequest request,
			HttpServletResponse response) {
		String str = "", aPlant = "", userID = "";
		try {

			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			LocUtil _locUtil = new LocUtil();
			_locUtil.setmLogger(mLogger);

			str = _locUtil.getlocationsWithDesc(aPlant, userID);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "No loc found ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}
		return str;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

	private String load_item_detailsforTranID(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
                        
		String xmlStr = "", aPlant = "", aTranID = "";
		try {
			
			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aTranID = StrUtils.fString(request.getParameter("TRANID"));
                        POSDetDAO posdetDAO = new POSDetDAO();
                        posdetDAO.setmLogger(mLogger);
                        List prdlist = posdetDAO.listProductsForPOSTranID(aPlant, " where POSTRANID='" + aTranID + "' AND TRANSTATUS<>'C'");
                        xmlStr += XMLUtils.getXMLHeader();
                        xmlStr += XMLUtils.getStartNode("POSTOTAL total ='"+ String.valueOf(prdlist.size()) + "'");
                        if (prdlist.size() > 0) {
                             
                        for (int i = 0; i < prdlist.size(); i++) {
                            Vector vecItem = (Vector) prdlist.get(i);
                            String item= (String) vecItem.get(0);
                            String itemdesc = (String) vecItem.get(1);
                            String qty = (String) vecItem.get(2);
                            String price = (String) vecItem.get(3);
                            String TotalPrice = (String) vecItem.get(4);
                            String batch = (String) vecItem.get(6);
                               
    
                            xmlStr += XMLUtils.getStartNode("record");
                            xmlStr += XMLUtils.getXMLNode("item", item);
                            xmlStr += XMLUtils.getXMLNode("itemDesc", StrUtils.replaceCharacters2Send(itemdesc));
                            xmlStr += XMLUtils.getXMLNode("batch", batch);
                            xmlStr += XMLUtils.getXMLNode("qty", qty);
                            xmlStr += XMLUtils.getXMLNode("price", price);
                            xmlStr += XMLUtils.getXMLNode("TotalPrice", TotalPrice);
                            xmlStr += XMLUtils.getEndNode("record");
         
                        } 
                        xmlStr += XMLUtils.getEndNode("POSTOTAL");
                        }	
			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Details Not found ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return xmlStr;
	}

	
	private String generate_TranID(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String xmlStr = "", PLANT = "", LOC = "",LOGIN_USER="",tranID="";
		try {
		        String genPosTranid="";
		        POSHdrDAO poshdr =  new POSHdrDAO();
		        UserLocUtil uslocUtil = new UserLocUtil();
		        uslocUtil.setmLogger(mLogger);
		        poshdr.setmLogger(mLogger);
                        PLANT = StrUtils.fString(request.getParameter("PLANT"));
                        LOC = StrUtils.fString(request.getParameter("LOC"));
                        LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
                        tranID=StrUtils.fString(request.getParameter("TRANID"));
		        boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,LOGIN_USER,LOC);
		        if(isvalidlocforUser){
		             
		        	//To check transaction already exists in POSHeader
		        	
					Hashtable<String, String> htTrandId = new Hashtable<String, String>();
					htTrandId.put("POSTRANID",tranID);
					boolean istranidExist=isExisit(PLANT, htTrandId);
					if(istranidExist==false)
					{
						 genPosTranid =poshdr.genPosManualTranId(PLANT, LOGIN_USER,LOC,tranID);
					}
					else
					{
						genPosTranid =poshdr.genPosTranId(PLANT,LOGIN_USER,LOC);
                    }
		        }
                        
		    if (genPosTranid.length() > 0) {
                            xmlStr = XMLUtils.getXMLHeader();
                            xmlStr += XMLUtils.getStartNode("ItemDetails");
                            xmlStr += XMLUtils.getXMLNode("status", "0");
                            xmlStr += XMLUtils.getXMLNode("description", "");
                            xmlStr += XMLUtils.getXMLNode("TRANID",genPosTranid );
                            xmlStr += XMLUtils.getEndNode("ItemDetails");
		    }else{
                        xmlStr = XMLUtils.getXMLMessage(1, "Failed to generate new TranID "); 
                    }
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);

			throw e;

		}
		return xmlStr;
	}

    private String load_locations(HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException,
                    Exception {
            String str = "";

            String PLANT = "", userID = "";
            try {
                    PLANT = StrUtils.fString(request.getParameter("PLANT"));
                    userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
                    LocUtil locUtil = new LocUtil();
                    locUtil.setmLogger(mLogger);
                    str = locUtil.getlocationsWithDesc(PLANT, userID);
                    if (str.equalsIgnoreCase("")) {

                            str = XMLUtils.getXMLMessage(1, "Details not found");
                    }
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e;
            }
            return str;
    }
	private String load_products(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", aPlant = "",aItem="";
		try {
			aPlant = StrUtils.fString(request.getParameter("PLANT"));
		        aItem = StrUtils.fString(request.getParameter("ITEM_NUM"));
                        POSUtil posUtil = new POSUtil();
                        posUtil.setmLogger(mLogger);
		        str = posUtil.getItemList(aPlant,aItem, " AND ISACTIVE='Y'");
			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "No product details found ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}

		return str;
	}
        
           private String load_transactionList(HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException,
                    Exception {
            String str = "", aPlant = "",aLoc="";
            try {
                    aPlant = StrUtils.fString(request.getParameter("PLANT"));
                    aLoc = StrUtils.fString(request.getParameter("LOC"));
                    POSUtil posUtil = new POSUtil();
                    posUtil.setmLogger(mLogger);
                    str = posUtil.getTransactionList(aPlant,aLoc);
                    if (str.equalsIgnoreCase("")) {
                            str = XMLUtils.getXMLMessage(1, "No TranId details found ");
                    }
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e;

            }

            return str;
    }

	

	private String process_Add_Item(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		// MLogger.log(0, "process_Add_Item() Starts");
                String xmlStr="";
		 POSUtil posUtil = new POSUtil();
		 posUtil.setmLogger(mLogger);
		POSDetDAO posdet = new POSDetDAO();
		posdet.setmLogger(mLogger);
                POSHdrDAO poshdr = new POSHdrDAO();
                poshdr.setmLogger(mLogger);
		Map<String, String> pos_HM = null;
		String PLANT = "",  ITEM_NUM = "",ITEM_DESC="", QTY = "", TRANID = "",LOC="",UNITPRICE="";
		String LOGIN_USER = "";
		String BATCH = "", Qty = "";

		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEM_NUM"));
			BATCH = StrUtils.fString(request.getParameter("BATCH"));
			QTY = StrUtils.fString(request.getParameter("QTY"));
			TRANID = StrUtils.fString(request.getParameter("TRANID"));
			LOC = StrUtils.fString(request.getParameter("LOC"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
                        int quantity = Integer.parseInt(QTY);
                     
                      
                       ItemMstDAO itM = new ItemMstDAO();
                       itM.setmLogger(mLogger);
                     
                       String scannedItemNo = itM.getItemIdFromAlternate(PLANT, ITEM_NUM);
                       if (scannedItemNo == null) {
                              throw new Exception("Not a valid product ");
                       }
		  
		    ITEMMST items =posUtil.getItemDetails(PLANT, scannedItemNo, quantity);
		   
		    Hashtable ht = new Hashtable();
		    ht.put(IDBConstants.PLANT,PLANT);
		    ht.put(IDBConstants.POS_TRANID,TRANID);
		    String QryUpdate =" SET STATUS ='O',UPAT ='"+new DateUtils().getDate()+"' ";
		    boolean posHsrUpt=  poshdr.update(QryUpdate,ht,"");
    		    ht.put(IDBConstants.POS_ITEM,ITEM_NUM);
    		    ht.put(IDBConstants.POS_BATCH,BATCH);
    		    ht.put(IDBConstants.POS_LOC,LOC);
		    boolean flag =  posdet.isExisit(ht,"");
                    if(!flag){
                       
                        ht.put(IDBConstants.POS_ITEMDESC,su.InsertQuotes(items.getITEMDESC()));
                        ht.put(IDBConstants.POS_BATCH,BATCH);
                        ht.put(IDBConstants.POS_QTY,String.valueOf(items.getStkqty()));
                        ht.put(IDBConstants.POS_LOC,LOC);
                        ht.put(IDBConstants.POSDET_STATUS,"N");
                        ht.put(IDBConstants.POS_UNITPRICE,String.valueOf(items.getUNITPRICE()));
                        ht.put(IDBConstants.POS_TOTALPRICE,String.valueOf(items.getTotalPrice()));
                        ht.put(IDBConstants.POS_TRANDT, new DateUtils().getDate());
                        ht.put(IDBConstants.POS_TRANTM, new DateUtils().Time());
                        ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
                        ht.put(IDBConstants.CREATED_BY,LOGIN_USER);
                        flag = posdet.insertIntoPosDet(ht);
                    }else{
                        String Qry = " SET QTY =QTY + "+quantity+ ",TOTALPRICE = TOTALPRICE + "+items.getTotalPrice()+" " ;
                        flag=  posdet.update(Qry,ht,"");
                    }


			if (flag) {
			    xmlStr = XMLUtils.getXMLMessage(1, "Product Added Successfully");
				
			} else {
				throw new Exception("Failed to add product details");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(0, e.getMessage());
		}

		return xmlStr;
	}
   

    private String holdTranIdDetails(HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException {
            // MLogger.log(0, "saveTranIdDetails() Starts");
            String str="";
            POSHdrDAO poshdr = new POSHdrDAO();
            poshdr.setmLogger(mLogger);
            Map<String, String> pos_HM = null;
            String PLANT = "",   TRANID = "";
            String LOGIN_USER = "";
          

            try {
                    PLANT = StrUtils.fString(request.getParameter("PLANT"));
                    TRANID = StrUtils.fString(request.getParameter("TRANID"));
                    LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));

                boolean posHsrUpt=false;
                //update poshdr set status ='H' and display new page
                 Hashtable ht = new Hashtable();
                 ht.put(IDBConstants.PLANT,PLANT);
                 ht.put(IDBConstants.POS_TRANID,TRANID);
                 String QryUpdate =" SET STATUS ='H',UPAT ='"+new DateUtils().getDate()+"' ";
                 try{
                     posHsrUpt= poshdr.update(QryUpdate,ht," AND STATUS <>'C'");
                 }catch(Exception e){
                     
                 }
                    
                
                    if (posHsrUpt) {
                        str = XMLUtils.getXMLMessage(1, "Tran Id Holded Successfully");
                            
                    } else {
                            throw new Exception("Failed to Hold TranId");
                    }
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    str = XMLUtils.getXMLMessage(0, e.getMessage());
            }

            return str;
    }
    
    private String deleteTranIdDetails(HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException {
            // MLogger.log(0, "saveTranIdDetails() Starts");
            String str="";
            POSDetDAO posdet = new POSDetDAO();
            posdet.setmLogger(mLogger);
            Map<String, String> pos_HM = null;
            String PLANT = "",   TRANID = "",ITEM_NUM="",BATCH="";
            String LOGIN_USER = "";
          

            try {
                    PLANT = StrUtils.fString(request.getParameter("PLANT"));
                    TRANID = StrUtils.fString(request.getParameter("TRANID"));
                    ITEM_NUM = StrUtils.fString(request.getParameter("ITEM_NUM"));
                    BATCH = StrUtils.fString(request.getParameter("BATCH"));
                    LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));

                boolean posdetUpt=false;
                //update poshdr set status ='H' and display new page
                 Hashtable ht = new Hashtable();
                 ht.put(IDBConstants.PLANT,PLANT);
                 ht.put(IDBConstants.POS_TRANID,TRANID);
                 try{
                    
                  //  posdetUpt= posdet.deleteProductInForTranId(PLANT,ITEM_NUM,TRANID);
                	 posdetUpt= posdet.deleteProductForTranId(PLANT,ITEM_NUM,TRANID,BATCH);
                	 
                 }catch(Exception e){
                     
                 }
                    
                
                    if (posdetUpt) {
                            str = XMLUtils.getXMLMessage(1, "product deleted Successfully");
                    } else {
                            throw new Exception("Failed to Delete Product");
                    }
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    str = XMLUtils.getXMLMessage(0, e.getMessage());
            }

            return str;
    }
 public boolean isExists(Connection conn, String sql) throws Exception {
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		boolean exists = false;
		try {
			pStmt = conn.prepareStatement(sql);
			rs = pStmt.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) > 0)
					exists = true;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pStmt != null) {
					pStmt.close();
				}
			} catch (Exception e) {
				throw e;
			}
		}
		return exists;
	}

    
    public String formCondition(Hashtable ht) throws Exception {
		String sCondition = "";
		try {
			StrUtils _StrUtils=new StrUtils();
			Enumeration enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = _StrUtils.fString((String) enumeration
						.nextElement());
				String value = _StrUtils.fString((String) ht.get(key));
				sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
						+ "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
		} catch (Exception e) {
			throw e;
		}
		return sCondition;
	}

	public boolean isExisit(String plant,Hashtable ht) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		String TABLE_NAME = "[" + plant + "_" + "POSHDR" + "]";
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + TABLE_NAME);
			sql.append(" WHERE  " + formCondition(ht));
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
	public boolean isRecvtranIDExisit(String plant,Hashtable ht) throws Exception {
		boolean flag = false;
		String STATUS ="C";
		java.sql.Connection con = null;
		String TABLE_NAME = "[" + plant + "_" + "POSHDR" + "]";
		try {
			con = com.track.gates.DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append("COUNT(*) ");
			sql.append(" ");
			sql.append(" FROM " + TABLE_NAME);
			sql.append(" WHERE  "+ formCondition(ht) +"AND STATUS='" + STATUS+"'");
			flag = isExists(con, sql.toString());
			
			this.mLogger.query(this.printInfo, sql.toString());
			/*if(flag){
				throw new Exception();
			}*/

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