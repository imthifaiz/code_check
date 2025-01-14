package com.track.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JasperRunManager;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;

import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.ItemSesBeanDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.POSPaymentDetailDAO;
import com.track.dao.SalesDetailDAO;
import com.track.dao.TblControlDAO;
import com.track.db.util.CustUtil;
import com.track.db.util.GstTypeUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.LocUtil;
import com.track.db.util.MiscIssuingUtil;
import com.track.db.util.POSUtil;
import com.track.db.util.PlantMstUtil;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.gates.Generator;
import com.track.gates.selectBean;
import com.track.tables.ITEMMST;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * Servlet implementation class Refund
 */
public class RefundServlet extends HttpServlet implements IMLogger{
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.LoanOrderPickingServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.LoanOrderPickingServlet_PRINTPLANTMASTERINFO;
	DateUtils dateUtils = new DateUtils(); 
	GstTypeUtil _GstTypeUtil =new GstTypeUtil();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RefundServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		String action = "";
		HttpSession session = request.getSession(false);
		String login_user = "";
		ItemUtil itemUtil = new ItemUtil();
		StrUtils strUtils= new StrUtils();
		boolean match = false;
		String posRefundPageToDirect="";
		try {
			action = request.getParameter("action");
			String PLANT = session.getAttribute("PLANT").toString();
			String item = request.getParameter("ITEM").trim();
			String scanqty = request.getParameter("QTY").trim();
			//code by Bruhan to add batch in pos on 5 dec 13
			String batch = request.getParameter("BATCH_0").trim();
			Boolean batchChecked = false;
			if( request.getParameter("chkbatch")!=null){
				batchChecked = true;
			}
			//code by Bruhan to add batch in pos on 5 dec 13
		
			login_user = (String) session.getAttribute("LOGIN_USER");
			String loc = request.getParameter("LOC").trim();
			String remarks = request.getParameter("REMARKS").trim();
		        String reasonCode = request.getParameter("REASONCODE").trim();
                        String posType = StrUtils.fString(request.getParameter("POS_TYPE")).trim(); // whether to check against Inventory
                        
                        if(posType.equalsIgnoreCase("NO_INV_CHECK")){
                           posRefundPageToDirect ="posRefundWoInv.jsp";
                        }else{
                           posRefundPageToDirect ="posRefund.jsp";
                        }
                        
                        //total discount calculation
        			    String TOTAL = strUtils.fString(request.getParameter("TOTAL"));
        				String SUBTOTAL = strUtils.fString(request.getParameter("SUBTOTAL"));
        				String TAX = strUtils.fString(request.getParameter("TAX"));
        				String TOTALDISCOUNT= strUtils.fString(request.getParameter("TOTALDISCOUNT"));
        				String DISCOUNTSUBTOTAL= strUtils.fString(request.getParameter("DISCOUNTSUBTOTAL"));
        		    //
        		    
                        
			float scqty = Float.parseFloat(scanqty);   String editprice="";
			Vector buylist = (Vector) session.getValue("posreflist");
			this.setMapDataToLogger(this.populateMapData(PLANT, login_user));
			itemUtil.setmLogger(mLogger);
			String errmsg = "";
			/*if (item == "" || item == null) {
				response.sendRedirect("jsp/posRefund.jsp");
			}*/
		    if (action.equalsIgnoreCase("ADD")) {
		    	 session.setAttribute("RESULTTOTALDISCOUNT","");
            	 session.setAttribute("TOTALDISCOUNT", "");
                 session.setAttribute("TOTALSUBTOTAL", ""); 
                 session.setAttribute("TOTALTAX", ""); 
                 session.setAttribute("CALCDISCOUNT","");
               		            
		            if (loc.length() > 0) {
		                UserLocUtil uslocUtil = new UserLocUtil();
		                uslocUtil.setmLogger(mLogger);
		                boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,login_user,loc);
		                if(isvalidlocforUser){
		                	
		                    if (item.length() > 0) {
		                    
			                    	boolean itemFound = true;
			                    	ItemMstDAO itM = new ItemMstDAO();
			            			itM.setmLogger(mLogger);
			            			String scannedItemNo = itM.getItemIdFromAlternate(PLANT, item);
			            			if (scannedItemNo == null) {
			            				itemFound = false;
			            			}else{
			            				item = scannedItemNo;
			            			}
			            			 if (itemFound) { 
		                                    ITEMMST items = getItemDetails(request, item, scqty,batch);
		                                    if (buylist == null) {
		                                            match = false;
		                                            buylist = new Vector();
		                                            buylist.addElement(items);

		                                    } else {
		                                    	
		                                            for (int i = 0; i < buylist.size(); i++) {
		                                                    ITEMMST itembean = (ITEMMST) buylist
		                                                                    .elementAt(i);
		                                                    String price = String.valueOf(itembean.getUNITPRICE());
		                                                    String discount=String.valueOf(itembean.getDISCOUNT());
		                                                    float pricef = Float.parseFloat(price);
		                                                    Float discountf=Float.parseFloat(discount);
		                                                    float totprice= scqty*(pricef-(pricef*discountf/100));
		                                                  

		                                                    if (itembean.getITEM().equalsIgnoreCase(items.getITEM()) && itembean.getBATCH().equalsIgnoreCase(items.getBATCH())) {
		                                                    	
		                                                    	float totalpc = itembean.getTotalPrice()+totprice; 
		                                                        // above code chnaged by Bruhan
			                                                     	    itembean.setTotalPrice(totalpc);
			                                                            itembean.setDISCOUNT(Float.parseFloat(discount));
			                                                            if (scqty > 1){
			                                                                    itembean.setStkqty((itembean.getStkqty() + scqty));
			                                                                 scqty= itembean.getStkqty() ;   
	                                                                            }
			                                                            else{
			                                                                    itembean.setStkqty((itembean.getStkqty() + 1));
			                                                                scqty=itembean.getStkqty();                           
			                                                            }
			                                                            
			                                                                                                                   
			                                                            float gsttax=(itembean.getGSTTAX()/100);
			                                                           
			                                                            
			                                                            float totalgsttax=totalpc*gsttax;
			                                                            
			                                            				itembean.setPRICEWITHTAX(totalpc+totalgsttax);
			                                            				
			                                           				  
		                                                            buylist.setElementAt(itembean, i);
		                                                            match = true;
		                                                    }
		                                            }
		                                            if (!match) {
		                                                    buylist.addElement(items);

		                                            }
		                                    }
		                                    session.putValue("posreflist", buylist);
		                                    session.setAttribute("REFUNDLOC", loc);
		                            }

		                    }
		                    session.setAttribute("errmsgref", "");
		                    response.sendRedirect("jsp/"+posRefundPageToDirect+"?CHKBATCH="+batchChecked);

                             } else {
                                          session.setAttribute("errmsgref", "Location : "+loc+" is not a User Assigned Location/Valid Location ");
                                          response.sendRedirect("jsp/"+posRefundPageToDirect+"?CHKBATCH="+batchChecked);

                                      }        
		            } else {
		                    session.setAttribute("errmsgref", "Please Enter Location");
		                    response.sendRedirect("jsp/"+posRefundPageToDirect+"?CHKBATCH="+batchChecked);

		            }
		            
		            

		    } 
			else if (action.equalsIgnoreCase("delete")) {

				int iCnt = 0;String appenddisc="";
				String[] chkvalues = request.getParameterValues("chk");
				int indexarray[] = new int[chkvalues.length];
				Vector updatelist = new Vector();

				if (loc.length() > 0) {
					if (chkvalues.length > 0) {

						for (int j = 0; j < chkvalues.length; j++) {
							indexarray[iCnt] = Integer.parseInt(chkvalues[iCnt]);
							iCnt++;
						}
						for (int j = 0; j < buylist.size(); j++) {
							
							//ITEMMST itembeans = (ITEMMST)buylist.get(j);
						int index=	Arrays.binarySearch(indexarray, j);
								if(index<0)
								{
									ITEMMST itembeans = (ITEMMST)buylist.get(j);
									ITEMMST additem = new ITEMMST();
									additem.setITEM(itembeans.getITEM());
									additem.setITEMDESC(itembeans.getITEMDESC());
									additem.setUNITPRICE(itembeans.getUNITPRICE());
									additem.setStkqty(itembeans.getStkqty());
									additem.setTotalPrice(itembeans.getTotalPrice());
									additem.setNONSTOCKFLAG(itembeans.getNONSTOCKFLAG());//added by Bruhan
									additem.setBATCH(itembeans.getBATCH());
									updatelist.add(additem);
								}
							}
						buylist.removeAllElements();
						session.putValue("posreflist", updatelist);
						}
						
						session.setAttribute("errmsgref", "");
					} else {
						session
								.setAttribute("errmsgref",
										"Please select checkbox");
					}

				response.sendRedirect("jsp/"+posRefundPageToDirect+"?CHKBATCH="+batchChecked);

			} else if (action.equalsIgnoreCase("Refund")) {
				SalesDetailDAO salesdao = new SalesDetailDAO();
				POSPaymentDetailDAO paymentDAO = new POSPaymentDetailDAO();
				String Tranid = "";
				Generator gen = new Generator();
				DateUtils dateUtils = new DateUtils();
				//generate receipt 
				Tranid = genReceipt(request);
                                System.out.println("Tranid ::"+Tranid);
				boolean flag = false;
				boolean invflag = false;
				String xmlutil = "";
				HashMap htrecv = new HashMap();
				String paymentmode = "";
				MiscIssuingUtil miscutil = new MiscIssuingUtil();
				Hashtable htm = new Hashtable();
				Hashtable<String, String> insPaymentHt = new Hashtable<String, String>();
				String totalsum = request.getParameter("totalsum");
				String formatdt = DateUtils.getDate();
				htm.put("PURCHASEDATE", dateUtils.getDateinyyyy_mm_dd(formatdt));
				htm.put("PURCHASETIME", DateUtils.Time());
				htm.put(IConstants.REMARKS, remarks);
                htm.put("RSNCODE", reasonCode);
             
        		htrecv.put(IConstants.PLANT, PLANT);
				htrecv.put(IConstants.LOGIN_USER, (String) session.getAttribute("LOGIN_USER"));
				htrecv.put(IConstants.LOC, loc);
                htrecv.put(IConstants.REMARKS, remarks+","+reasonCode);
				miscutil.setmLogger(mLogger);
				salesdao.setmLogger(mLogger);

					String appenddisc="";
					for (int j = 0; j < buylist.size(); j++) {

						ITEMMST product = (ITEMMST) buylist.get(j);
						htm.put(IConstants.PLANT, PLANT);
						htm.put(IConstants.ITEM, product.getITEM());
						float discnt = product.getDISCOUNT();
						if(discnt>0)
                                                appenddisc = " + Discount "+String.valueOf(discnt)+"%"; 
						htm.put(IConstants.ITEM_DESC, product.getITEMDESC());
						htm.put(IConstants.BATCH, product.getBATCH());
						htm.put(IConstants.QTY, String.valueOf(product.getStkqty()));
						htm.put(IDBConstants.PRICE, String.valueOf(product.getTotalPrice()));
						htm.put(IDBConstants.POS_DISCOUNT, String.valueOf(product.getDISCOUNT()));
						htm.put(IDBConstants.LOC, loc);
					    htm.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
					    htm.put(IDBConstants.CREATED_BY,login_user);
						htm.put("TranId", Tranid);
						htm.put("TranType",TransactionConstants.POS_REFUND);						
						htrecv.put(IConstants.ITEM, product.getITEM());
						htrecv.put(IConstants.QTY, String.valueOf(product.getStkqty()));
						htrecv.put(IConstants.LOC, loc);
						htrecv.put(IConstants.BATCH, product.getBATCH());
						// added by Bruhan to check nonstock flag
					    String nonstock = product.getNONSTOCKFLAG();
					    if(!posType.equalsIgnoreCase("NO_INV_CHECK")&&!nonstock.equalsIgnoreCase("Y")){//end
                                                    //htrecv.put(IConstants.BATCH, "NOBATCH");
						// Add to inventory
							flag = addInvmst(htrecv);
                                                }else{
                                                    //htrecv.put(IConstants.BATCH, "");
                                                    flag=true;  
                                                }
						if(flag == true)
							flag = processMovHis(htrecv);

						salesdao.setmLogger(mLogger);
						if (flag == true)
							flag = salesdao.insertIntoSalesDet(htm);
					}
					insPaymentHt.put(IDBConstants.RECEIPTNO, Tranid);
					insPaymentHt.put(IDBConstants.PLANT, PLANT);							
					insPaymentHt.put(IDBConstants.PAYMENTMODE, "Cash");
					insPaymentHt.put(IDBConstants.AMOUNT, totalsum);	
					if (flag == true)
						flag = paymentDAO.insertPOSPaymentDetails(insPaymentHt);
					if (flag == true)
						buylist.removeAllElements();
					session.putValue("posreflist", buylist);
					if (flag == false) {
						session.setAttribute("errmsgref", errmsg);
						response.sendRedirect("jsp/"+posRefundPageToDirect+"?CHKBATCH="+batchChecked);
					} else {
						viewPOSReport(request, response, Tranid,remarks);
						session.setAttribute("errmsgref", "");

						response.setHeader("Refresh", "5;../jsp/posReport.jsp");
					}

				}
				


			
		} catch (Exception e) {
			if (action.equalsIgnoreCase("delete"))
				response.sendRedirect("jsp/"+posRefundPageToDirect);
			
			e.printStackTrace();
		}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	private ITEMMST getItemDetails(HttpServletRequest req, String item,
			float scanqty,String batch) throws Exception {
		HttpSession session1 = req.getSession();
		ITEMMST items = new ITEMMST();
		String itemdesc = "", qty = "", price = "",nonstockflag= "";//added by Bruhan
		float discount=0,fgsttax=0;String disccntstr = "";
		ItemSesBeanDAO itemDAO = new ItemSesBeanDAO();
		String PLANT = session1.getAttribute("PLANT").toString();
	
		itemDAO.setmLogger(mLogger);
		List prdlist = itemDAO.queryProducts(PLANT, " where item='" + item
				+ "'");
		for (int i = 0; i < 1; i++) {
			Vector vecItem = (Vector) prdlist.get(0);
			itemdesc = (String) vecItem.get(1);
			qty = (String) vecItem.get(8);
			//qty= StrUtils.TrunkateDecimalForImportData(qty);
			price = (String) vecItem.get(12);
			discount = Float.parseFloat((String) vecItem.get(13));
			nonstockflag = (String) vecItem.get(15); //added by Bruhan
		}
		if(discount>0)
		{
			items.setDISCOUNT(discount);
		
		}
		items.setITEM(item);
		items.setITEMDESC(itemdesc+disccntstr);
                items.setDISCOUNT(discount); // added by samatha as 0 was interested sales_details
		int minqty = 1;
		float pricef = Float.parseFloat(price);
		//start code by Bruhan to add batch in pos transaction on 5 dec 13
		items.setBATCH(batch);
		//end code by Bruhan to add batch in pos transaction on 5 dec 13
		// added by Bruhan
		items.setNONSTOCKFLAG(nonstockflag);
		//end
		items.setStkqty(scanqty);// }

			items.setUNITPRICE(pricef);
			if (scanqty > 1)
				items.setTotalPrice((pricef-pricef*discount/100) * scanqty);
			else
			items.setTotalPrice(pricef-pricef*discount/100);
			//getting tax(GST)
			List gsttax = _GstTypeUtil.qryGstType("POS",PLANT, " ");
			for (int i = 0; i < 1; i++) {
				Vector vecgsttax = (Vector) gsttax.get(0);
				fgsttax = Float.parseFloat((String)vecgsttax.get(2)); 
			}
			if(fgsttax>0)
			{
				items.setGSTTAX(fgsttax);
	
			}
			
		return items;
	}

	private String genReceipt(HttpServletRequest request)
	throws IOException, ServletException, Exception {
		String plant = StrUtils.fString(
		(String) request.getSession().getAttribute("PLANT")).trim();
		String userName = StrUtils.fString(
		(String) request.getSession().getAttribute("LOGIN_USER"))
		.trim();
		String DATE = "";
		String sBatchSeq = "";
		String rtnBatch = "";
		String sZero = "";

 try {

	TblControlDAO _TblControlDAO = new TblControlDAO();
	_TblControlDAO.setmLogger(mLogger);
	DATE = _TblControlDAO.getDate();
	Hashtable ht = new Hashtable();

	String query = " isnull(NXTSEQ,'') as NXTSEQ";
	ht.put(IDBConstants.PLANT, plant);
	ht.put(IDBConstants.TBL_FUNCTION, IDBConstants.TBL_POS_CAPTION);
	ht.put(IDBConstants.TBL_PREFIX1, DATE.substring(0, 6));
	boolean exitFlag = _TblControlDAO.isExisit(ht, "", plant);
	if (exitFlag == false) {

		rtnBatch = DATE.substring(0, 6) + "0000"
				+ IDBConstants.TBL_FIRST_NEX_SEQ;

		Hashtable htTblCntInsert = new Hashtable();
		htTblCntInsert.put(IDBConstants.PLANT, (String) plant);
		htTblCntInsert.put(IDBConstants.TBL_FUNCTION,
				(String) IDBConstants.TBL_POS_CAPTION);
		htTblCntInsert.put(IDBConstants.TBL_PREFIX1, (String) DATE
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
		rtnBatch = DATE.substring(0, 6) + sZero + updatedSeq;
		Hashtable htTblCntUpdate = new Hashtable();
		htTblCntUpdate.put(IDBConstants.PLANT, plant);
		htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,
				IDBConstants.TBL_POS_CAPTION);
		htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, DATE
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
        
	 public void viewPOSReport(HttpServletRequest request,
	                         HttpServletResponse response, String tranid,String Remarks) throws IOException,
	                         Exception {

	                 try {

	                         java.sql.Connection con = null;
                                 PlantMstUtil pmUtil = new PlantMstUtil();
                              // added by Bruhan
                                 LocUtil lcUtil = new LocUtil();
                                 //end
	                         HttpSession session = request.getSession();
	                         String CFAX = "", CWEBSITE = "", CRCBNO = "", CGSTREGNO = "", PLANTDESC = "", CADD1 = "", CADD3 = "", CADD2 = "", CADD4 = "", CCOUNTRY = "", CZIP = "", CTEL = "";
	                         String CONTACTNAME="",CHPNO="",CEMAIL="";
	                         String PLANT = (String) session.getAttribute("PLANT");
	                         String userid = (String) session.getAttribute("LOGIN_USER");
	                         String loc = request.getParameter("LOC");
	                          String   paymentmode = request.getParameter("payment");
	                         con = DbBean.getConnection();
	                         String SysDate = DateUtils.getDate();
	                         String SysTime = DateUtils.Time();
	                         String jasperPath = DbBean.JASPER_INPUT + "/" + "rptPOS";
	                         String imagePath =  DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + DbBean.LOGO_FILE;
	             			String imagePath1 =  DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + "Logo1.gif";
	             			
	             			File checkImageFile = new File(imagePath);
	             	        if (!checkImageFile.exists()) {
	             	           imagePath =  DbBean.COMPANY_LOGO_PATH + "/"+ DbBean.NO_LOGO_FILE; 
	             	        }
	             	        File checkImageFile1 = new File(imagePath1);
	             	        if (!checkImageFile1.exists()) {
	             	           imagePath1 =  DbBean.COMPANY_LOGO_PATH + "/"+ DbBean.NO_LOGO_FILE; 
	             	        }
	      	                         Map parameters = new HashMap();
	                         
	      	                       ArrayList listQryPlantMst = pmUtil.getPlantMstDetails(PLANT);
	      	                		for (int i = 0; i < listQryPlantMst.size(); i++) {
	      	                			Map map = (Map) listQryPlantMst.get(i);

	      	                			PLANTDESC  = (String) map.get("PLNTDESC");
	      	                			CADD1 = (String) map.get("ADD1");
	      	                			CADD2 = (String) map.get("ADD2");
	      	                			CADD3 = (String) map.get("ADD3");
	      	                			CADD4 = (String) map.get("ADD4");
	      	                			CCOUNTRY = (String) map.get("COUNTY");
	      	                			CZIP = (String) map.get("ZIP");
	      	                			CTEL = (String) map.get("TELNO");
	      	                			CFAX = (String) map.get("FAX");
	      	                			CRCBNO = StrUtils.fString((String) map.get("RCBNO"));
	      	                			CONTACTNAME = (String) map.get("NAME");
	      	                			CHPNO = (String) map.get("HPNO");
	      	                			CEMAIL = (String) map.get("EMAIL");
	      	                		}
	      	                 
	      	                 POSUtil posUtil = new POSUtil();
	      	                 Map mhdr= posUtil.getPosReceiptHdrDetails(PLANT);
	      	                 

	      	                 String   PaymentMode = (String) mhdr.get("PAYMENT_MODE");
	      	                 String   SalesRep = (String) mhdr.get("SALES_REP");
	      	                 String   SerialNo = (String) mhdr.get("SERIAL_NO");
	      	                 String   ReceiptNo = (String) mhdr.get("RECEIPT_NO");
	      	                 String   Product = (String) mhdr.get("PRODUCT");
	      	                 String   Description = (String) mhdr.get("PROD_DESC");
	      	                 String   UnitPrice = (String) mhdr.get("UNIT_PRICE");
	      	                 String   Qty = (String) mhdr.get("QTY");
	      	                 String   Discount = (String) mhdr.get("DISCOUNT");
	      	                 String   Total = (String) mhdr.get("TOTAL");
	      	                 String   Subtotal = (String) mhdr.get("SUBTOTAL");
	      	                 String   Tax = (String) mhdr.get("TAX");
	      	                 String   TotalAmt = (String) mhdr.get("TOTAL_AMT");
	      	                 String   PaymentPaid = (String) mhdr.get("AMOUNT_PAID");
	      	                 String   ChangeRemaining = (String) mhdr.get("CHANGE");
	      	                 String   DisplayByLoc = (String) mhdr.get("ADDRBYLOC");
	      	                 
	      	                 String HEADER = (String) mhdr.get("HDR");
	      	                 String  GREET1 = (String) mhdr.get("G1");
	      	                 String GREET2 = (String) mhdr.get("G2");
	      	                 String FOOT1 = (String) mhdr.get("F1");
	      	                 String   FOOT2 = (String) mhdr.get("F2");
	      	                 String msgWithcompanyAdrr =  GREET1 +"##"+ GREET2 +"##"+ FOOT1 +"##"+ FOOT2 +"##";
	      	                 if(DisplayByLoc.equals("1"))
	      	                 {
	      	                 	
	      	                 	if(PLANTDESC.length()>0)
	      	         		        msgWithcompanyAdrr=msgWithcompanyAdrr+PLANTDESC+",";
	      	         		        if(CADD1.length()>0)
	      	         		         msgWithcompanyAdrr=msgWithcompanyAdrr+" Address:"+CADD1+"," ;
	      	         		        
	      	         		        if(CADD2.length()>0)
	      	         		            msgWithcompanyAdrr=msgWithcompanyAdrr+CADD2 +"," ;
	      	         		        if(CADD3.length()>0)
	      	         		            msgWithcompanyAdrr=msgWithcompanyAdrr+CADD3 +",";
	      	         		        if(CADD4.length()>0) 
	      	         		        msgWithcompanyAdrr= msgWithcompanyAdrr+ CADD4 +",";
	      	         		        if(CCOUNTRY.length()>0)
	      	         		        msgWithcompanyAdrr= msgWithcompanyAdrr+ CCOUNTRY ;
	      	         		        if(CZIP.length()>0)
	      	         		        msgWithcompanyAdrr= msgWithcompanyAdrr  +"("+CZIP+")" ;
	      	         		        if(CTEL.length()>0)
	      	         		        msgWithcompanyAdrr= msgWithcompanyAdrr  +"Tel:"+CTEL+"," ; 
	      	         		        if(CFAX.length()>0)
	      	         		        msgWithcompanyAdrr= msgWithcompanyAdrr  +"Fax:"+CFAX+"##" ; 
	      	         		        if(CFAX=="null" || CFAX=="")
	      	         		       	msgWithcompanyAdrr= msgWithcompanyAdrr  +"##" ; 	
	      	         		        String strAdr2="";
	      	         		                
	      	         		        
	      	         		        if(CWEBSITE.length()>0)
	      	         		        msgWithcompanyAdrr= msgWithcompanyAdrr  +"Website:"+CWEBSITE ; 
	      	         		        strAdr2=strAdr2+CWEBSITE ;
	      	         		        if(strAdr2.length()>0){strAdr2 = ",";}
	      	         		        
	      	         		        if(CRCBNO.length()>0)
	      	         		        msgWithcompanyAdrr= msgWithcompanyAdrr  +strAdr2+" Business Registration No:"+CRCBNO ; 
	      	         		        
	      	         		        if(CGSTREGNO.length()>0)
	      	         		        msgWithcompanyAdrr= msgWithcompanyAdrr  +","+" GST Registration No: "+CGSTREGNO ; 
	      	         		        
	      	                 	   ArrayList listQry = lcUtil.getLocListDetails(PLANT, loc);
	      	             			for (int i = 0; i < listQry.size(); i++) {
	      	         					Map map1 = (Map) listQry.get(i);
	      	         							
	      	         					PLANTDESC  = (String) map1.get("COMNAME");
	      	         					CADD1 = (String) map1.get("ADD1");
	      	         					CADD2 = (String) map1.get("ADD2");
	      	         					CADD3 = (String) map1.get("ADD3");
	      	         					CADD4 = (String) map1.get("ADD4");
	      	         					CCOUNTRY = (String) map1.get("COUNTRY");
	      	         					CZIP = (String) map1.get("ZIP");
	      	         					CTEL = (String) map1.get("TELNO");
	      	         					CFAX = (String) map1.get("FAX");
	      	         					CRCBNO = StrUtils.fString((String) map1.get("RCBNO"));
	      	         					CONTACTNAME = "";
	      	         		   			CHPNO ="";
	      	         		   			CEMAIL = "";
	      	         					
	      	         			}
	      	                 }
	      	                      String[] receiptmsgarray = getReceiptMsg(msgWithcompanyAdrr);
	      	                 

	      	                 int index=1;
	      	                 // POS tran Details 
	      	                 for (int i = 0; i < receiptmsgarray.length; i++) {
	      	                         parameters.put("MSG"+index, receiptmsgarray[i]);
	      	                         index=index+1;
	      	                 }
	             		     parameters.put("imagePath", imagePath);
	             		     parameters.put("imagePath1", imagePath1);
	                         parameters.put("ReceiptNo", tranid);
	                         parameters.put("company", PLANT);
	                         parameters.put("user", userid);
	                         parameters.put("currentDate", SysDate);
	                         parameters.put("curTime", SysTime);
	                         parameters.put("paymentType", "");
	                         parameters.put("heading", " REFUND ");
	                         //below code is added by Bruhan for report headings
	                         parameters.put("lblPaymentMethods", PaymentMode);
	                         parameters.put("lblSalesRep", SalesRep);
	                         parameters.put("lblSerialNo", SerialNo);
	                         parameters.put("lblReceiptNo", ReceiptNo);        
	                         parameters.put("lblItem", Product);
	                         parameters.put("lblDescription", Description);
	                         parameters.put("lblUnitPrice", UnitPrice);
	                         parameters.put("lblQty", Qty);
	                         parameters.put("lblDiscount", Discount);
	                         parameters.put("lblTotal", Total);
	                         parameters.put("lblSubtotal", Subtotal);
	                         parameters.put("lblTax", Tax);
	                         parameters.put("lblTotalAmt", TotalAmt);
	                         parameters.put("lblPaymentPaid", PaymentPaid);
	                         //end
                                 String gstValue = new selectBean().getGST("POS",PLANT);
                                 double gst=new Double(gstValue).doubleValue()/100;
                                 parameters.put("Gst",gst);
	                         long start = System.currentTimeMillis();
	                         
	                         parameters.put("fromAddress_CompanyName", PLANTDESC);
	                 		parameters.put("fromAddress_BlockAddress", CADD1 + "  " + CADD2);
	                 		parameters.put("fromAddress_RoadAddress", CADD3 + "  " + CADD4);
	                 		parameters.put("fromAddress_Country", CCOUNTRY);
	                 		parameters.put("fromAddress_ZIPCode", CZIP);
	                 		parameters.put("fromAddress_TpNo", CTEL);
	                 		parameters.put("fromAddress_FaxNo", CFAX);
	                 		
	                 		 if(CONTACTNAME.length() > 1 ){
	                 			 parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
	                 		 }
	                 		 else
	                 		 {
	                 			 parameters.put("fromAddress_ContactPersonName","");  
	                 			
	                 		 }
	                 		 
	                 		 if(CHPNO.length() > 1 ){
	                 			 parameters.put("fromAddress_ContactPersonMobile", CHPNO);
	                 		 }
	                 		 else
	                 		 {
	                 			 parameters.put("fromAddress_ContactPersonMobile","");
	                 		 }
	                 			
	                 		 		 
	                 		 if(CEMAIL.length() > 1 ){
	                 			parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
	                 		 }
	                 		 else
	                 		 {
	                 			parameters.put("fromAddress_ContactPersonEmail", "");
	                 			
	                 		 }
	                 		 parameters.put("referanceNO",CRCBNO); 
	                         
	                       
	                 		 
	                         System.out.println("**************" + " Start Up Time : " + start
	                                         + "**********");

	                         byte[] bytes = JasperRunManager.runReportToPdf(jasperPath
	                                         + ".jasper", parameters, con);

	                         response.addHeader("Content-disposition",
	                                         "attachment;filename=posRefund.pdf");
	                         response.setContentLength(bytes.length);
	                         response.getOutputStream().write(bytes);
	                         response.setContentType("application/pdf");

	                 } catch (IOException e) {

	                         e.printStackTrace();

	                 }
	         }

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		// TODO Auto-generated method stub
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

	}
	public String[] getReceiptMsg(String msgkey)
	{
		String[] msgarray = null;
			msgarray = msgkey.split("##");
		
		return msgarray;
	}
        
	public boolean addInvmst(HashMap map)
	{
		boolean flag=false;
		InvMstDAO _InvMstDAO = new InvMstDAO();
		_InvMstDAO.setmLogger(mLogger);
		Hashtable htInvMst = new Hashtable();
		try {
			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htInvMst.put(IDBConstants.QTY, map.get(IConstants.QTY));
			htInvMst.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htInvMst.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			Hashtable htinv = new Hashtable();
			Hashtable htinvupd = new Hashtable();
			
			htinv.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htinv.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htinv.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htinv.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
			
			flag = _InvMstDAO.isExisit(htinv, "");
			String updqry = "set qty=qty+"+map.get(IConstants.QTY);
			
			if(flag)
				flag = _InvMstDAO.update(updqry, htinv, "");
			else
			flag = _InvMstDAO.insertInvMst(htInvMst);
		} catch (Exception e) {
			// TODO: handle exception
			this.mLogger.exception(this.printLog, "", e);
		}
		return flag; 
	}
        
	public boolean processMovHis(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		try {
			Hashtable htRecvHis = new Hashtable();
			htRecvHis.clear();
			htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvHis.put("DIRTYPE", TransactionConstants.POS_REFUND);
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvHis.put("MOVTID", "IN");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htRecvHis.put(IDBConstants.REMARKS, map.get(IConstants.REMARKS));
			htRecvHis.put("BATNO", map.get(IConstants.BATCH));
			htRecvHis.put("QTY", map.get(IConstants.QTY));
			htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
			htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());

			flag = movHisDao.insertIntoMovHis(htRecvHis);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}
	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		// TODO Auto-generated method stub
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
