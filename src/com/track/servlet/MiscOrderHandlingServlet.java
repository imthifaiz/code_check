package com.track.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.DoDetDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.LocTypeDAO;
import com.track.dao.RsnMst;
import com.track.dao.TblControlDAO;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.POUtil;
import com.track.db.util.UserLocUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class MiscOrderHandlingServlet
 */
public class MiscOrderHandlingServlet extends HttpServlet implements IMLogger {
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.MiscOrderHandlingServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.MiscOrderHandlingServlet_PRINTPLANTMASTERINFO;
	/*Bruhan change code:001  modifed method to get des,uom,item_loc in one query instead of getting in separate queries
	 * 
	 */

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MiscOrderHandlingServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	        //StrUtils strUtils = new StrUtils();
		String action = StrUtils.fString(request.getParameter("ACTION")).trim();
		String plant = StrUtils.fString(
				(String) request.getSession().getAttribute("PLANT")).trim();
		String userName = StrUtils.fString(
				(String) request.getSession().getAttribute("LOGIN_USER"))
				.trim();
		this.setMapDataToLogger(this.populateMapData(plant, userName));
		if (this.printInfo) {
			diaplayInfoLogs(request);
		}
		JSONObject jsonObjectResult = new JSONObject();
		if (action.equals("VALIDATE_PRODUCT")) {
			String item = StrUtils.fString(request.getParameter("ITEM")).trim();
			jsonObjectResult = this.validateProduct(plant, item);

		}
		
		if (action.equals("VALIDATE_PRODUCT_AGAINST_PICKLIST")) {
			String item = StrUtils.fString(request.getParameter("ITEM")).trim();
			String dono = StrUtils.fString(request.getParameter("DONO")).trim();
			jsonObjectResult = this.validateProductAgainstPickList(plant, item,dono);

		}
	    if (action.equals("VALIDATE_PRODUCT_GETDETAIL")) {
	            String item = StrUtils.fString(request.getParameter("ITEM")).trim();
	            String desc = StrUtils.fString(request.getParameter("DESC")).trim();
	            String pono = StrUtils.fString(request.getParameter("PONO")).trim();
	            jsonObjectResult = this.validateProductGetDetail(plant,pono, item,desc);

	    }
	    if (action.equals("VALIDATE_PRODUCT_BYLOC")) { 
	            String item = StrUtils.fString(request.getParameter("ITEM")).trim();
	         String fromloc = StrUtils.fString(request.getParameter("FROMLOC")).trim();
	            jsonObjectResult = this.validateProductByLOC(plant, item,fromloc);

	    }
		if (action.equals("VALIDATE_LOCATION")) {
			String locationId = StrUtils.fString(request.getParameter("LOC"))
					.trim();
			jsonObjectResult = this.validateLocation(plant, userName,
					locationId);
		}
		
		if (action.equals("GENERATE_BATCH")) {
			jsonObjectResult = this.generateBatch(request);
		}
		if (action.equals("VALIDATE_BATCH")) {
			jsonObjectResult = this.validateBatch(request);
		}
		if (action.equals("VALIDATE_LOCTYPE")) {
			String loctypeId = StrUtils.fString(request.getParameter("LOCTYPE"))
					.trim();
			jsonObjectResult = this.validateLocationtype(plant,loctypeId);
		}
		if (action.equals("VALIDATE_RSNCODE")) {
			String rsncode = StrUtils.fString(request.getParameter("RSNCODE"))
					.trim();
			jsonObjectResult = this.validateRsncode(plant,rsncode);
		}
		if (action.equals("VALIDATE_PRODUCT_UOM")) {
            String item = StrUtils.fString(request.getParameter("ITEM")).trim();
            String desc = StrUtils.fString(request.getParameter("DESC")).trim();
            String uom = StrUtils.fString(request.getParameter("UOM")).trim();
            String pono = StrUtils.fString(request.getParameter("PONO")).trim();
            String type = StrUtils.fString(request.getParameter("TYPE")).trim();
            String disc = StrUtils.fString(request.getParameter("DISC")).trim();
            String minprs = StrUtils.fString(request.getParameter("MINPRICE")).trim();
            jsonObjectResult = this.validateProductUomDetail(plant,pono,uom, item,desc,type,disc,minprs);

		}
		
		if (action.equals("VALIDATE_PRODUCT_UOM_CURRENCY_PURCHASE")) {
            String item = StrUtils.fString(request.getParameter("ITEM")).trim();
            String desc = StrUtils.fString(request.getParameter("DESC")).trim();
            String uom = StrUtils.fString(request.getParameter("UOM")).trim();
            String currency = StrUtils.fString(request.getParameter("CURRENCY")).trim();
            String type = StrUtils.fString(request.getParameter("TYPE")).trim();
            String disc = StrUtils.fString(request.getParameter("DISC")).trim();
            String minprs = StrUtils.fString(request.getParameter("MINPRICE")).trim();
            jsonObjectResult = this.validateProductUomDetailpurcurrency(plant,currency,uom, item,desc,type,disc,minprs);

		}
		if (action.equals("VALIDATE_PRODUCT_UOM_BY_CURRENCY")) {
            String item = StrUtils.fString(request.getParameter("ITEM")).trim();
            String price = StrUtils.fString(request.getParameter("PRICE")).trim();
            String cost = StrUtils.fString(request.getParameter("COST")).trim();
            String desc = StrUtils.fString(request.getParameter("DESC")).trim();
            String uom = StrUtils.fString(request.getParameter("UOM")).trim();
            String currencyID = StrUtils.fString(request.getParameter("CURRENCYID")).trim();
            String type = StrUtils.fString(request.getParameter("TYPE")).trim();
            String disc = StrUtils.fString(request.getParameter("DISC")).trim();
            String minprs = StrUtils.fString(request.getParameter("MINPRICE")).trim();
            //jsonObjectResult = this.validateProductUomDetailByCurrency(plant,currencyID,uom, item,desc,type,disc,minprs);
            jsonObjectResult = this.validateProductUomDetailByCurrencyItemPrice(plant,currencyID,uom, item,desc,type,disc,minprs,price,cost);
            

		}
		this.mLogger.auditInfo(this.printInfo, "[JSON OUTPUT] "
				+ jsonObjectResult);
		response.setContentType("application/json");
		
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonObjectResult.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}

	@SuppressWarnings("unchecked")
	private JSONObject validateBatch(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			String plant = StrUtils.fString(
					(String) request.getSession().getAttribute("PLANT")).trim();
			String ISNONSTKFLG="";
			String item = StrUtils.fString(request.getParameter("ITEM")).trim();
			String loc = StrUtils.fString(request.getParameter("LOC")).trim();
			String batch = StrUtils.fString(request.getParameter("BATCH"))
					.trim();
			ItemMstDAO itemMstDAO = new ItemMstDAO();
			itemMstDAO.setmLogger(mLogger);

			String scannedItemNo = itemMstDAO.getItemIdFromAlternate(plant,
					item);
			if (scannedItemNo == null) {
				throw new Exception("Not a valid product");
			}
			item = scannedItemNo;
			Hashtable ht = new Hashtable();
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.ITEM, item);
			ht.put(IDBConstants.LOC, loc);
			ht.put(IDBConstants.USERFLD4, batch);
			ISNONSTKFLG = new ItemMstDAO().getNonStockFlag(plant, item);
			InvMstDAO invMstDAO = new InvMstDAO();
			invMstDAO.setmLogger(mLogger);
	if(ISNONSTKFLG.equalsIgnoreCase("N"))
			{
				if (invMstDAO.isExisit(ht, " QTY >0 ")) {
					resultJson.put("status", "100");
					JSONObject resultObjectJson = new JSONObject();
					resultObjectJson.put("batchCode", batch);
					String availqty = invMstDAO.getBatchQty(plant, item, batch, loc, " QTY >0 ");
					double availabqty = Double.parseDouble(availqty);
					availabqty = StrUtils.RoundDB(availabqty, IConstants.DECIMALPTS);
					availqty = String.valueOf(availabqty);
					resultObjectJson.put("availableQty", availqty );
					resultJson.put("result", resultObjectJson);
			} else {
				resultJson.put("status", "99");
			}
				
			}
			else
			{
				resultJson.put("status", "100");
				JSONObject resultObjectJson = new JSONObject();
				resultObjectJson.put("batchCode", batch);
				resultJson.put("result", resultObjectJson);
				
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}

	private JSONObject generateBatch(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			String batchCode = this.generateBatchCode(request);

			if (batchCode.length() > 0) {
				resultJson.put("status", "100");
				JSONObject resultObjectJson = new JSONObject();
				resultObjectJson.put("batchCode", batchCode);
				resultJson.put("result", resultObjectJson);
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}

	@SuppressWarnings("unchecked")
	private JSONObject validateLocation(String plant, String userId,
			String locationId) {
		JSONObject resultJson = new JSONObject();
		try {

			UserLocUtil userLocUtil = new UserLocUtil();
			userLocUtil.setmLogger(mLogger);
			String extraConforUserLoc = userLocUtil.getUserLocAssigned(userId,
					plant, "LOC");
			LocMstDAO locMstDAO = new LocMstDAO();
			locMstDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.LOC, locationId);
			if (locMstDAO.isExisit(ht,"  ISACTIVE ='Y' AND LOC NOT LIKE 'SHIPPINGAREA%' AND LOC NOT LIKE 'TEMP_TO%'"+ extraConforUserLoc)) 
			{	
				JSONObject resultObjectJson = new JSONObject();
				String locdesc = locMstDAO.getLocDesc(plant, locationId);
				resultObjectJson.put("locdesc", locdesc);
				resultJson.put("result", resultObjectJson);
				resultJson.put("status", "100");
				
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}

	@SuppressWarnings("unchecked")
	private String generateBatchCode(HttpServletRequest request)
			throws IOException, ServletException, Exception {
		String plant = StrUtils.fString(
				(String) request.getSession().getAttribute("PLANT")).trim();
		String userName = StrUtils.fString(
				(String) request.getSession().getAttribute("LOGIN_USER"))
				.trim();

		String BATCHDATE = "";

		String sBatchSeq = "";

		String rtnBatch = "";
		String sZero = "";

		try {

			TblControlDAO _TblControlDAO = new TblControlDAO();
			_TblControlDAO.setmLogger(mLogger);
			BATCHDATE = _TblControlDAO.getBatchDate();
			Hashtable ht = new Hashtable();

			String query = " isnull(NXTSEQ,'') as NXTSEQ";
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.TBL_FUNCTION, IDBConstants.TBL_BATCH_CAPTION);
			ht.put(IDBConstants.TBL_PREFIX1, BATCHDATE.substring(0, 6));
			boolean exitFlag = _TblControlDAO.isExisit(ht, "", plant);
			if (exitFlag == false) {

				rtnBatch = BATCHDATE.substring(0, 6) + "-" + "0000"
						+ IDBConstants.TBL_FIRST_NEX_SEQ;

				Hashtable htTblCntInsert = new Hashtable();
				htTblCntInsert.put(IDBConstants.PLANT, (String) plant);
				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,
						(String) IDBConstants.TBL_BATCH_CAPTION);
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, (String) BATCHDATE
						.substring(0, 6));
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,
						(String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, (String) userName);
				new DateUtils();
				htTblCntInsert.put(IDBConstants.CREATED_AT, (String) DateUtils
						.getDateTime());
				_TblControlDAO.insertTblControl(htTblCntInsert, plant);

			} else {

				Map m = _TblControlDAO.selectRow(query, ht, "");
				sBatchSeq = (String) m.get("NXTSEQ");
				if (sBatchSeq.length() == 1) {
					sZero = "0000";
				} else if (sBatchSeq.length() == 2) {
					sZero = "000";
				} else if (sBatchSeq.length() == 3) {
					sZero = "00";
				} else if (sBatchSeq.length() == 4) {
					sZero = "0";
				}

				int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim()
						.toString())) + 1;

				String updatedSeq = Integer.toString(inxtSeq);
				rtnBatch = BATCHDATE.substring(0, 6) + "-" + sZero + updatedSeq;
				Hashtable htTblCntUpdate = new Hashtable();
				htTblCntUpdate.put(IDBConstants.PLANT, plant);
				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,
						IDBConstants.TBL_BATCH_CAPTION);
				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, BATCHDATE
						.substring(0, 6));
				StringBuffer updateQyery = new StringBuffer("set ");
				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"
						+ (String) updatedSeq.toString() + "'");

				_TblControlDAO.update(updateQyery.toString(), htTblCntUpdate,
						"", plant);

			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return rtnBatch;
	}

	private JSONObject validateProduct(String plant, String item) {
		JSONObject resultJson = new JSONObject();
		try {

			ItemMstDAO itemMstDAO = new ItemMstDAO();
			itemMstDAO.setmLogger(mLogger);

			boolean itemFound = true;
			String scannedItemNo = itemMstDAO.getItemIdFromAlternate(plant,
					item);
			if (scannedItemNo == null) {
				itemFound = false;
			}

			if (itemFound) {
				resultJson.put("status", "100");
				JSONObject resultObjectJson = new JSONObject();
				resultObjectJson.put("item", scannedItemNo);
				resultObjectJson.put("discription", itemMstDAO.getItemDesc(plant, scannedItemNo));
				resultObjectJson.put("uom", itemMstDAO.getItemPurchaseUOM(plant, scannedItemNo));
				resultObjectJson.put("isNonStk", itemMstDAO.getNonStockFlag(plant, scannedItemNo));
				resultObjectJson.put("detaildesc", itemMstDAO.getItemDetailDesc(plant, scannedItemNo));	
				resultJson.put("result", resultObjectJson);
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}
	
	private JSONObject validateProductAgainstPickList(String plant, String item,String dono) {
		JSONObject resultJson = new JSONObject();
		try {

			ItemMstDAO itemMstDAO = new ItemMstDAO();
			itemMstDAO.setmLogger(mLogger);
			
			  //boolean itemFound = true;
              String scannedItemNo = itemMstDAO.getItemIdFromAlternate(plant, item);
              if (scannedItemNo == null) {
                //      itemFound = false;
              }
              else{
            	  item = scannedItemNo;
              }
			 DoDetDAO  _DoDetDAO  = new DoDetDAO();  
			List listQry =  _DoDetDAO.getOutBoundPickingItemDetailsByWMS(plant,dono,item);
			if(listQry.size() >0){
				for (int iCnt =0; iCnt<listQry.size(); iCnt++){
				Map lineArr = (Map) listQry.get(iCnt);
				
                 String itemdesc = itemMstDAO.getItemDesc(plant ,item);
                 String uom = StrUtils.fString((String)lineArr.get("unitmo"));
              
				resultJson.put("status", "100");
				JSONObject resultObjectJson = new JSONObject();
				resultObjectJson.put("item", item);
				resultObjectJson.put("discription", itemdesc);
				resultObjectJson.put("uom", uom );		
				resultJson.put("result", resultObjectJson);
			}
			}
			else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}
	
    private JSONObject validateProductGetDetail(String plant,String aPono, String item,String desc) {
            JSONObject resultJson = new JSONObject();
            try {
            	String IBDiscount="";
                    ItemMstDAO itemMstDAO = new ItemMstDAO();
                   ItemMstUtil itemutil= new ItemMstUtil();
                    itemMstDAO.setmLogger(mLogger);
                    if(item.length()==0 && desc.length()>0){
                    	item = itemMstDAO.getItemFromDesc(plant,desc);
                       
                    }
                    boolean itemFound = true;
                    String scannedItemNo = itemMstDAO.getItemIdFromAlternate(plant,
                                    item);
                    if (scannedItemNo == null) {
                            itemFound = false;
                    }
               
                    if (itemFound) {
                        ArrayList listQry = itemutil.getItemList(plant,item,desc," and  isnull(itemmst.userfld1,'N')='N' ");
                           IBDiscount=new POUtil().getIBDiscountSelectedItem(plant,aPono,item);  
                           String discounttype="";
                           int plusIndex = IBDiscount.indexOf("%");
                           if (plusIndex != -1) {
                          	 	IBDiscount = IBDiscount.substring(0, plusIndex);
                          	 	discounttype = "BYPERCENTAGE";
                               }
                            resultJson.put("status", "100");
                            JSONObject resultObjectJson = new JSONObject();
                            resultObjectJson.put("item", scannedItemNo);
                            resultObjectJson.put("discription", itemMstDAO.getItemDesc(
                                            plant, scannedItemNo));
                                            if(listQry.size()>0){
                             Map m=(Map)listQry.get(0);
                              resultObjectJson.put("detaildesc",StrUtils.fString((String)m.get("REMARK1")));  
                              resultObjectJson.put("unitcost",(String)m.get("UNITCOST"));
                              resultObjectJson.put("manufacturer",StrUtils.fString((String)m.get("MANUFACTURER"))); 
                              resultObjectJson.put("uom",StrUtils.fString((String)m.get("PURCHASEUOM")));
                              resultObjectJson.put("ConvertedUnitCost",new POUtil().getConvertedUnitCostForProduct(plant,aPono,scannedItemNo));
                              resultObjectJson.put("ConvertedUnitCostWTC",new POUtil().getConvertedUnitCostForProductWTC(plant,aPono,scannedItemNo));
							  resultObjectJson.put("MINSTKQTY",StrUtils.fString((String)m.get("MINSTKQTY")));
                              resultObjectJson.put("MAXSTKQTY",StrUtils.fString((String)m.get("MAXSTKQTY")));
                              resultObjectJson.put("STOCKONHAND",StrUtils.fString((String)m.get("STOCKONHAND")));
                              resultObjectJson.put("INCOMINGQTY",StrUtils.fString((String)m.get("INCOMINGQTY")));
                              resultObjectJson.put("incomingIBDiscount",IBDiscount);
                              resultObjectJson.put("IBDiscountType",discounttype);
                              resultObjectJson.put("PRODGST",StrUtils.fString((String)m.get("PRODGST")));
                              
                              
                            }
                            resultJson.put("result", resultObjectJson);
                    } else {
                            resultJson.put("status", "99");
                    }
                    return resultJson;
            } catch (Exception daRE) {
                    resultJson.put("status", "99");
                    return resultJson;
            }
    }
    private JSONObject validateProductByLOC(String plant, String item,String fromloc) {
            JSONObject resultJson = new JSONObject();
            try {

                    ItemMstDAO itemMstDAO = new ItemMstDAO();
                    itemMstDAO.setmLogger(mLogger);

                    boolean itemFound = true;
                    String scannedItemNo = itemMstDAO.getItemIdFromAlternate(plant,
                                    item);
                    if (scannedItemNo == null) {
                            itemFound = false;
                    }
                //String sBatch="",sQty="",uom="";
                InvMstDAO  _InvMstDAO  = new InvMstDAO();  
                    _InvMstDAO.setmLogger(mLogger);
                    String desc="",item_loc="",StkUom="",nonstockflag="";
                    if(itemFound){
             /*  List listQry =  _InvMstDAO.getLocationTransferBatch(plant,item,fromloc,"");
                      Map m=(Map)listQry.get(0);
                             sBatch    = (String)m.get("batch");
                             sQty=(String)m.get("qty");*/
                        
                 	Map mitem = itemMstDAO.getItemDetailDescription(plant, scannedItemNo);
                        if(mitem.size()>0){
                            desc = StrUtils.fString((String) mitem.get("itemDesc"));
                            StkUom  =StrUtils.fString((String) mitem.get("uom"));
                            item_loc = StrUtils.fString((String) mitem.get("ITEM_LOC"));
                            nonstockflag = StrUtils.fString((String) mitem.get("NONSTKFLAG"));
                        }
                 
                    }

                    if (itemFound) {
                            resultJson.put("status", "100");
                            JSONObject resultObjectJson = new JSONObject();
                            resultObjectJson.put("item", scannedItemNo);
                            resultObjectJson.put("discription", desc);
                            resultObjectJson.put("uom",StkUom); 
                            resultObjectJson.put("itemloc",item_loc); //change code:001
                       //resultObjectJson.put("qty",sQty);  by Bruhan to get inventory qty for nobatch
                            resultObjectJson.put("qty",_InvMstDAO.getBatchQty(plant, scannedItemNo, "nobatch", fromloc, ""));
                            resultObjectJson.put("nonstockflag",nonstockflag);
                            
                               resultJson.put("result", resultObjectJson);
                    } else {
                            resultJson.put("status", "99");
                    }
                    return resultJson;
            } catch (Exception daRE) {
                    resultJson.put("status", "99");
                    return resultJson;
            }
    }
    @SuppressWarnings("unchecked")
	private JSONObject validateLocationtype(String plant,String loctypeId) {
		JSONObject resultJson = new JSONObject();
		try {

			LocTypeDAO loctypeDAO = new LocTypeDAO();
			loctypeDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.LOCTYPEID, loctypeId);
			ht.put("IsActive", "Y");
			if (loctypeDAO.isExists(ht)) 
			{	
				JSONObject resultObjectJson = new JSONObject();
				String loctypedesc = loctypeDAO.getLocTypeDesc(plant, loctypeId);
				resultObjectJson.put("loctypedesc", loctypedesc);
				resultJson.put("result", resultObjectJson);
				resultJson.put("status", "100");
				
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}
    @SuppressWarnings("unchecked")
	private JSONObject validateRsncode(String plant,String rsncode) {
		JSONObject resultJson = new JSONObject();
		try {

			RsnMst rsnMst = new RsnMst();
			rsnMst.setmLogger(mLogger);
			Hashtable htRsnMst = new Hashtable();
			htRsnMst.put(IConstants.PLANT, plant);
			htRsnMst.put(IConstants.RSNCODE, rsncode);
			if (rsnMst.isExists(htRsnMst)) {
				resultJson.put("status", "100");
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}
    private JSONObject validateProductUomDetail(String plant,String aPono,String uom, String item,String desc,String type,String disc,String minprs ) {
        JSONObject resultJson = new JSONObject();
        try {
        	String IBDiscount="";
                ItemMstDAO itemMstDAO = new ItemMstDAO();
               ItemMstUtil itemutil= new ItemMstUtil();
                itemMstDAO.setmLogger(mLogger);
                
                
                    ArrayList listQry = itemutil.getItemUOMCOSTList(plant,aPono,item,desc,uom,type,disc,minprs," and  isnull(itemmst.userfld1,'N')='N' ");
                       
                        resultJson.put("status", "100");
                        JSONObject resultObjectJson = new JSONObject();
                        
                                        if(listQry.size()>0){
                         Map m=(Map)listQry.get(0);
                            
                          resultObjectJson.put("unitcost",(String)m.get("UNITCOST"));
                          
                          resultObjectJson.put("ConvertedUnitCost",(String)m.get("ConvertedUnitCost"));
                          resultObjectJson.put("ConvertedUnitCostWTC",(String)m.get("ConvertedUnitCostWTC"));
                          resultObjectJson.put("ConvertedDiscWTC",(String)m.get("ConvertedDiscWTC"));
                          resultObjectJson.put("MinPriceWTC",(String)m.get("MinPriceWTC"));
                        }
                        resultJson.put("result", resultObjectJson);
                
                return resultJson;
        } catch (Exception daRE) {
                resultJson.put("status", "99");
                return resultJson;
        }
}
    private JSONObject validateProductUomDetailpurcurrency(String plant,String currency,String uom, String item,String desc,String type,String disc,String minprs ) {
        JSONObject resultJson = new JSONObject();
        try {
        	String IBDiscount="";
                ItemMstDAO itemMstDAO = new ItemMstDAO();
               ItemMstUtil itemutil= new ItemMstUtil();
                itemMstDAO.setmLogger(mLogger);
                
                
                    ArrayList listQry = itemutil.getItemUOMCOSTListcurrency(plant,currency,item,desc,uom,type,disc,minprs," and  isnull(itemmst.userfld1,'N')='N' ");
                       
                        resultJson.put("status", "100");
                        JSONObject resultObjectJson = new JSONObject();
                        
                                        if(listQry.size()>0){
                         Map m=(Map)listQry.get(0);
                            
                          resultObjectJson.put("unitcost",(String)m.get("UNITCOST"));
                          
                          resultObjectJson.put("ConvertedUnitCost",(String)m.get("ConvertedUnitCost"));
                          resultObjectJson.put("ConvertedUnitCostWTC",(String)m.get("ConvertedUnitCostWTC"));
                          resultObjectJson.put("ConvertedDiscWTC",(String)m.get("ConvertedDiscWTC"));
                          resultObjectJson.put("MinPriceWTC",(String)m.get("MinPriceWTC"));
                        }
                        resultJson.put("result", resultObjectJson);
                
                return resultJson;
        } catch (Exception daRE) {
                resultJson.put("status", "99");
                return resultJson;
        }
}
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
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
	
	private JSONObject validateProductUomDetailByCurrency(String plant,String currencyID,String uom, String item,String desc,String type,String disc,String minprs ) {
        JSONObject resultJson = new JSONObject();
        try {
        	String IBDiscount="";
                ItemMstDAO itemMstDAO = new ItemMstDAO();
               ItemMstUtil itemutil= new ItemMstUtil();
                itemMstDAO.setmLogger(mLogger);
                
                
                    ArrayList listQry = itemutil.getItemUOMCOSTListByCurrency(plant,currencyID,item,desc,uom,type,disc,minprs," and  isnull(itemmst.userfld1,'N')='N' ");
                       
                        resultJson.put("status", "100");
                        JSONObject resultObjectJson = new JSONObject();
                        
                                        if(listQry.size()>0){
                         Map m=(Map)listQry.get(0);
                            
                          resultObjectJson.put("unitcost",(String)m.get("UNITCOST"));
                          
                          resultObjectJson.put("ConvertedUnitCost",(String)m.get("ConvertedUnitCost"));
                          resultObjectJson.put("ConvertedUnitCostWTC",(String)m.get("ConvertedUnitCostWTC"));
                          resultObjectJson.put("ConvertedDiscWTC",(String)m.get("ConvertedDiscWTC"));
                          resultObjectJson.put("MinPriceWTC",(String)m.get("MinPriceWTC"));
                        }
                        resultJson.put("result", resultObjectJson);
                
                return resultJson;
        } catch (Exception daRE) {
                resultJson.put("status", "99");
                return resultJson;
        }
	}
	
	private JSONObject validateProductUomDetailByCurrencyItemPrice(String plant,String currencyID,String uom, String item,String desc,String type,String disc,String minprs,String itemprice,String itemcost) {
        JSONObject resultJson = new JSONObject();
        try {
        	String IBDiscount="";
                ItemMstDAO itemMstDAO = new ItemMstDAO();
               ItemMstUtil itemutil= new ItemMstUtil();
                itemMstDAO.setmLogger(mLogger);
                
                
                    ArrayList listQry = itemutil.getItemUOMCOSTListByCurrencyItemPrice(plant,currencyID,item,desc,uom,type,disc,minprs," and  isnull(itemmst.userfld1,'N')='N' ",itemprice);
                    ArrayList listQrycost = itemutil.getItemUOMCOSTListByCurrencyItemCost(plant,currencyID,item,desc,uom,type,disc,minprs," and  isnull(itemmst.userfld1,'N')='N' ",itemcost);
                       
                        resultJson.put("status", "100");
                        JSONObject resultObjectJson = new JSONObject();
                        
                                        if(listQry.size()>0){
                         Map m=(Map)listQry.get(0);
                            
                          resultObjectJson.put("unitcost",(String)m.get("UNITCOST"));
                          
                          resultObjectJson.put("ConvertedUnitCost",(String)m.get("ConvertedUnitCost"));
                          resultObjectJson.put("ConvertedUnitCostWTC",(String)m.get("ConvertedUnitCostWTC"));
                          resultObjectJson.put("ConvertedDiscWTC",(String)m.get("ConvertedDiscWTC"));
                          resultObjectJson.put("MinPriceWTC",(String)m.get("MinPriceWTC"));
                        }
                        String cost ="0";              
                        if(listQrycost.size()>0){
                        	 Map mc=(Map)listQrycost.get(0);
                        	cost = (String)mc.get("UNITCOST");	
                        }
                        resultObjectJson.put("cost",cost);
                                        
                        resultJson.put("result", resultObjectJson);
                
                return resultJson;
        } catch (Exception daRE) {
                resultJson.put("status", "99");
                return resultJson;
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
