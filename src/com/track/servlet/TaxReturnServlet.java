package com.track.servlet;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.TaxReturnDAO;
import com.track.dao.TaxReturnFilingDAO;
import com.track.dao.TblControlDAO;
import com.track.db.object.TaxReturnFillDet;
import com.track.db.object.TaxReturnFillHdr;
import com.track.db.util.PlantMstUtil;
import com.track.gates.DbBean;
import com.track.gates.userBean;
import com.track.util.DateUtils;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * Servlet implementation class TaxReturnServlet
 */
@WebServlet("/TaxReturnServlet")
public class TaxReturnServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TaxReturnServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			StrUtils strUtils = new StrUtils();
			String sUserId = strUtils.fString(request.getParameter("LOGIN_USER"));
			String res = "";
			String action = "";			
			String sDate = "", sRef = "", amount = "", taxAmount = "", sAccount = "", sReason = "",sType="",sCurency="",Tranid="",taxheader_id="";
			
			DateUtils dateutils = new DateUtils();
			JSONObject jsonObjectResult = new JSONObject();
			UserTransaction ut = null;
			PlantMstDAO _PlantMstDAO = new PlantMstDAO();
			TaxReturnDAO _TaxReturnDAO = new TaxReturnDAO(); 
			
			action = strUtils.fString(request.getParameter("action"));
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String region = StrUtils.fString((String) request.getSession().getAttribute("REGION")).trim();
						
			sDate = strUtils.InsertQuotes(strUtils.fString(request.getParameter("DATE")));
			sRef = strUtils.InsertQuotes(strUtils.fString(request.getParameter("REFERENCE")));
			amount = strUtils.fString(request.getParameter("AMOUNT"));
			taxAmount = strUtils.fString(request.getParameter("TAXAMOUNT"));
			sAccount = strUtils.InsertQuotes(strUtils.fString(request.getParameter("ACCOUNT")));
			sReason = strUtils.InsertQuotes(strUtils.fString(request.getParameter("REASON")));
			sType = StrUtils.fString(request.getParameter("TYPE"));
			sCurency = StrUtils.fString(request.getParameter("CURENCY"));
			Tranid = StrUtils.fString(request.getParameter("TRANID"));
			taxheader_id = StrUtils.fString(request.getParameter("modaltaxheaderid"));
			
			MovHisDAO mdao = new MovHisDAO(plant);
			
			if (action.equalsIgnoreCase("create")) {
				
				ut = DbBean.getUserTranaction();
				ut.begin();
				Hashtable<String, String> accountHt = new Hashtable<>();
				accountHt.put("PLANT", plant);
				accountHt.put("TAXHDR_ID", taxheader_id);
				accountHt.put("ADJUSTMENTDATE", sDate);
				accountHt.put("REFERENCE", sRef);
				accountHt.put("AMOUNT", amount);
				accountHt.put("TAXAMOUNT", taxAmount);
				accountHt.put("ACCOUNT_NAME", sAccount);
				accountHt.put("REASON", sReason);
				
				/*Hashtable<String, String> ht = new Hashtable<>();
				ht.put("PLANT", plant);
				boolean chktaxret =  _TaxReturnDAO.isExists(ht, "FINTAXFILEADJUSTMENT", plant, "");*/ 
				
				boolean taxretCreated = _TaxReturnDAO.addTaxReturn(accountHt, plant, "FINTAXFILEADJUSTMENT");
				 if (taxretCreated) {
						DbBean.CommitTran(ut);
						if(region.equalsIgnoreCase("GCC"))
						jsonObjectResult.put("MESSAGE", "Tax Return created successfully");
						else if(region.equalsIgnoreCase("ASIA PACIFIC"))
						jsonObjectResult.put("MESSAGE", "GST Return created successfully");
						jsonObjectResult.put("STATUS", "SUCCESS");
					}else {
						DbBean.RollbackTran(ut);
						/*if (chktaxret)
							jsonObjectResult.put("MESSAGE", "Tax Return Not Possible");
						else*/
						if(region.equalsIgnoreCase("GCC"))
							jsonObjectResult.put("MESSAGE", "Tax Return creation failed");
							else if(region.equalsIgnoreCase("ASIA PACIFIC"))
							jsonObjectResult.put("MESSAGE", "GST Return creation failed");
						jsonObjectResult.put("STATUS", "FAIL");
					}
				
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} else if (action.equalsIgnoreCase("createForSG")) {

				ut = DbBean.getUserTranaction();
				ut.begin();
				String boxno = strUtils.fString(request.getParameter("POPBOX"));
				String taxid = strUtils.fString(request.getParameter("adtaxid"));
				String taxname = strUtils.fString(request.getParameter("Adjustmenttax"));
				TaxReturnFilingDAO taxReturnDAO = new TaxReturnFilingDAO();
				PlantMstUtil plantmstutil = new PlantMstUtil();
				String COUNTRYCODE = "";
				List viewlistQry = plantmstutil.getPlantMstDetails(plant);
				for (int i = 0; i < viewlistQry.size(); i++) {
					Map map = (Map) viewlistQry.get(i);
					COUNTRYCODE = StrUtils.fString((String) map.get("COUNTRY_CODE"));
				}
				Hashtable<String, String> accountHt = new Hashtable<>();
				accountHt.put("PLANT", plant);
				accountHt.put("TAXHDR_ID", taxheader_id);
				accountHt.put("ADJUSTMENTDATE", sDate);
				accountHt.put("REFERENCE", sRef);
				accountHt.put("AMOUNT", amount);
				accountHt.put("TAXAMOUNT", taxAmount);
				accountHt.put("ACCOUNT_NAME", sAccount);
				accountHt.put("REASON", sReason);
				accountHt.put("TAXID", taxid);
				accountHt.put("TAXNAME", taxname);

				int taxretCreated = _TaxReturnDAO.addTaxReturnForSG(accountHt, plant, "FINTAXFILEADJUSTMENT");
				if (taxretCreated > 0) {
					TaxReturnFillDet taxRetDet = new TaxReturnFillDet();
					taxRetDet.setPLANT(plant);
					taxRetDet.setTAXHDR_ID(Integer.valueOf(taxheader_id));
					taxRetDet.setCOUNTRY_CODE(COUNTRYCODE);
					taxRetDet.setBOX(boxno);
					taxRetDet.setTRANSACTION_ID(String.valueOf(taxretCreated));
					taxRetDet.setTAXABLE_AMOUNT(Double.parseDouble("0"));
					taxRetDet.setTAX_AMOUNT(Double.parseDouble(taxAmount));
					taxRetDet.setDATE(sDate);
					taxRetDet.setTRANSACTION_TYPE("GST Adjustments");
					taxRetDet.setISTAXPREVIOUS(false);

					int tid = taxReturnDAO.addTaxReturnDet(taxRetDet);
					
					TaxReturnFillHdr taxRtHdr=taxReturnDAO.getTaxReturnHdrById(plant, taxheader_id);
					double taxpayable =  taxRtHdr.getTOTAL_TAXPAYABLE();
					double balancedue = taxRtHdr.getBALANCEDUE();
					if(boxno.equalsIgnoreCase("6")) {
						taxpayable = taxpayable + Double.parseDouble(taxAmount);
						balancedue = balancedue + Double.parseDouble(taxAmount);
						String updateItems="TOTAL_TAXPAYABLE="+taxpayable;
						taxReturnDAO.updateTaxHeader(plant, String.valueOf(taxheader_id), updateItems);
						taxReturnDAO.updateTaxBalanceDue(plant, String.valueOf(taxheader_id), String.valueOf(balancedue));
					}
					
					if(boxno.equalsIgnoreCase("7")) {
						taxpayable = taxpayable - Double.parseDouble(taxAmount);
						balancedue = balancedue - Double.parseDouble(taxAmount);
						String updateItems="TOTAL_TAXPAYABLE="+taxpayable;
						taxReturnDAO.updateTaxHeader(plant, String.valueOf(taxheader_id), updateItems);
						taxReturnDAO.updateTaxBalanceDue(plant, String.valueOf(taxheader_id), String.valueOf(balancedue));
					}
					
					if (tid > 0) {
						MovHisDAO movHisDao = new MovHisDAO();
						Hashtable htMovHis = new Hashtable();
						htMovHis.clear();
						htMovHis.put(IDBConstants.PLANT, plant);					
						htMovHis.put("DIRTYPE", TransactionConstants.CREATE_TAXFILE_ADJUSTMENT);	
						htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(sDate));														
						htMovHis.put(IDBConstants.MOVHIS_ORDNUM, taxretCreated);
						htMovHis.put(IDBConstants.CREATED_BY, username);		
						htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
						htMovHis.put("REMARKS",COUNTRYCODE+""+taxretCreated+"-"+taxAmount);
						movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS

						DbBean.CommitTran(ut);
						jsonObjectResult.put("MESSAGE", "GST Return created successfully");
						jsonObjectResult.put("STATUS", "SUCCESS");
					} else {
						DbBean.RollbackTran(ut);
						jsonObjectResult.put("MESSAGE", "GST Return creation failed");
						jsonObjectResult.put("STATUS", "FAIL");
					}
				} else {
					DbBean.RollbackTran(ut);
					jsonObjectResult.put("MESSAGE", "GST Return creation failed");
					jsonObjectResult.put("STATUS", "FAIL");
				}

				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObjectResult.toString());
				response.getWriter().flush();
				response.getWriter().close();
			}
			else if (action.equalsIgnoreCase("VIEW_TAXFILEADJUSTMENT_SUMMARY")) {
				
				JSONArray jsonArray = new JSONArray();
		        JSONArray jsonArrayErr = new JSONArray();
		        
				 ArrayList movQryList  = new ArrayList();
				 StringBuffer sql;
				 String sCondition = "",dtCondStr="",extraCon="";
				 Hashtable htData = new Hashtable();
			        String decimalZeros = "";
			        for (int i = 0; i < Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY); i++) {
			            decimalZeros += "#";
			        }
			        DecimalFormat decformat = new DecimalFormat("#,##0." + decimalZeros);
			        try {
			        	
			         String year = dateutils.getYear();
			        	
			        	if(sType.equalsIgnoreCase("This Year"))
			        	sCondition=sCondition+" AND YEAR(ADJUSTMENTDATE)='"+year+"'";
			        	else if(sType.equalsIgnoreCase("Previous Year"))
			        	sCondition=sCondition+" AND YEAR(ADJUSTMENTDATE)<'"+year+"'";
			        	
			        	sql = new StringBuffer("select A.ID as ID,ADJUSTMENTDATE,REFERENCE,AMOUNT,TAXAMOUNT,ACCOUNT_NAME,REASON");
			        	sql.append(" from " + plant +"_FINTAXFILEADJUSTMENT A WHERE A.PLANT='"+ plant+"'" + sCondition);
				
			        	movQryList = _TaxReturnDAO.selectForReport(sql.toString(), htData, extraCon);	
			 		   if (movQryList.size() > 0) {
			             int iIndex = 0,Index = 0;
			              int irow = 0;
			              
			              for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
	                            String result="";
	                            Map lineArr = (Map) movQryList.get(iCnt);                            
	                               
	                            String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
	                            JSONObject resultJsonInt = new JSONObject();
	                   			String unitCostValue = (String)lineArr.get("AMOUNT");
	                   			
	                            float unitCostVal ="".equals(unitCostValue) ? 0.0f :  Float.parseFloat(unitCostValue);
	                            if(unitCostVal==0f){
	                            	unitCostValue="0.00000";
	                            }else{
	                            	unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }
	                            
	                            String taxCostValue = (String)lineArr.get("TAXAMOUNT");
	                   			
	                            float taxCostVal ="".equals(taxCostValue) ? 0.0f :  Float.parseFloat(taxCostValue);
	                            if(taxCostVal==0f){
	                            	taxCostValue="0.00000";
	                            }else{
	                            	taxCostValue=taxCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	                            }
	                            
	                      
	                    	 Index = Index + 1;
	                    	 resultJsonInt.put("Index",(Index));
	                    	 resultJsonInt.put("id",StrUtils.fString((String)lineArr.get("ID")));
	                    	 resultJsonInt.put("date",StrUtils.fString((String)lineArr.get("ADJUSTMENTDATE")));
	                    	 resultJsonInt.put("taxauthority","FTA");
	                    	 resultJsonInt.put("ref",StrUtils.fString((String)lineArr.get("REFERENCE")));
	                    	 resultJsonInt.put("vendno",StrUtils.fString((String)lineArr.get("VENDNO")));
	                    	 resultJsonInt.put("reason",StrUtils.fString((String)lineArr.get("REASON")));
	                    	 resultJsonInt.put("currency",sCurency);
	                    	 resultJsonInt.put("taxamount",taxCostValue);
	                    	 resultJsonInt.put("amount",unitCostValue);                    	 
	                         jsonArray.add(resultJsonInt);
	                         }
			              	jsonObjectResult.put("items", jsonArray);
		                    JSONObject resultJsonInt = new JSONObject();
		                    resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
		                    resultJsonInt.put("ERROR_CODE", "100");
		                    jsonArrayErr.add(resultJsonInt);
		                    jsonObjectResult.put("errors", jsonArrayErr);

			              
			 		   }else {
		                    JSONObject resultJsonInt = new JSONObject();
		                    resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
		                    resultJsonInt.put("ERROR_CODE", "99");
		                    jsonArrayErr.add(resultJsonInt);
		                    jsonArray.add("");
		                    jsonObjectResult.put("items", jsonArray);
		                    jsonObjectResult.put("errors", jsonArrayErr);
		            }
			    
			        } catch (Exception e) {
		        		jsonArray.add("");
		        		jsonObjectResult.put("items", jsonArray);
		        		jsonObjectResult.put("SEARCH_DATA", "");
		                JSONObject resultJsonInt = new JSONObject();
		                resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
		                resultJsonInt.put("ERROR_CODE", "98");
		                jsonArrayErr.add(resultJsonInt);
		                jsonObjectResult.put("ERROR", jsonArrayErr);
		        }
			        
			        response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(jsonObjectResult.toString());
					response.getWriter().flush();
					response.getWriter().close();
				
			}
			else if (action.equalsIgnoreCase("Remove")) {
				boolean isAdded = _TaxReturnDAO.deleteInvoiceDet(plant, Tranid);
				if(isAdded)
				{
					if(region.equalsIgnoreCase("GCC"))
						response.sendRedirect("jsp/taxAdjustmentSummary.jsp?result=Tax Adjustment Removed");
					else if(region.equalsIgnoreCase("ASIA PACIFIC"))
						response.sendRedirect("jsp/sg-taxAdjustmentSummary.jsp?result=GST Adjustment Removed");
				}
				}
				else
				{
					if(region.equalsIgnoreCase("GCC"))
						response.sendRedirect("jsp/taxAdjustmentSummary.jsp?result=Removing Tax Adjustment Failed");
					else if(region.equalsIgnoreCase("ASIA PACIFIC"))
						response.sendRedirect("jsp/sg-taxAdjustmentSummary.jsp?result=Removing GST Adjustment Failed");
				}
			
		}catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
