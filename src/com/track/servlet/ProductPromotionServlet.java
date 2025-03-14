// PAGE CREATED BY : IMTHI
// DATE 14-04-2022
// DESC : Product Promotion Servlet

package com.track.servlet;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.ItemMstDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PosItemPromotionDetDAO;
import com.track.dao.PosItemPromotionHdrDAO;
import com.track.db.object.PosItemPromotionDet;
import com.track.db.object.PosItemPromotionHdr;
import com.track.db.util.EmailMsgUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.ItemUtil;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.StrUtils;
import com.track.util.ThrowableUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/productpromotion/*")
public class ProductPromotionServlet extends HttpServlet implements IMLogger {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		
		PosItemPromotionHdrDAO PosItemHdrDAO= new PosItemPromotionHdrDAO();
		PosItemPromotionDetDAO poDetDAO = new PosItemPromotionDetDAO();
		MovHisDAO movHisDao = new MovHisDAO();
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		if (action.equalsIgnoreCase("save") || action.equalsIgnoreCase("edit")) {

			UserTransaction ut = null;
			JSONObject resultJson = new JSONObject();
			try{
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				PosItemPromotionHdr promotionHdr = new PosItemPromotionHdr();
				
				List Lnno = new ArrayList(), buyitem = new ArrayList(), buyqty = new ArrayList(), getitem = new ArrayList(), getqty = new ArrayList(),
					 promotiontype = new ArrayList(), promotionvalue = new ArrayList(),limitusage = new ArrayList(),chkdDoNo = new ArrayList(),unitprice = new ArrayList();
				int lnnocount = 0,CHKDcount=0;
				String editID="",promoname="";
				
				

				if (isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);				
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					ut = DbBean.getUserTranaction();
					ut.begin();
					while (iterator.hasNext()) {
						FileItem fileItem = (FileItem) iterator.next();
						if (fileItem.isFormField()) {
							/* POHDR */
							if (fileItem.getFieldName().equalsIgnoreCase("plant")) {
								promotionHdr.setPLANT(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (action.equalsIgnoreCase("edit")) {
								if (fileItem.getFieldName().equalsIgnoreCase("HDRID")) {
//								promotionHdr.setID(Integer.parseInt(StrUtils.fString(fileItem.getString()).trim()));
									editID = StrUtils.fString(fileItem.getString()).trim();
								}
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("PROMOTION_NAME")) {
								promotionHdr.setPROMOTION_NAME(StrUtils.fString(fileItem.getString()).trim());
								promoname = StrUtils.fString(fileItem.getString()).trim();
							}
							
							
							if (fileItem.getFieldName().equalsIgnoreCase("PROMOTION_DESC")) {
								promotionHdr.setPROMOTION_DESC(StrUtils.fString(fileItem.getString()).trim());
							}
							if (fileItem.getFieldName().equalsIgnoreCase("OUTCODE")) {
								promotionHdr.setOUTLET(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("BYQTY")) {
								promotionHdr.setBY_VALUE(Integer.valueOf(StrUtils.fString(fileItem.getString()).trim()));
								int byqty = (Integer.valueOf(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("CUSTOMER_TYPE")) {
								promotionHdr.setCUSTOMER_TYPE_ID(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("START_DATE")) {
								promotionHdr.setSTART_DATE(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("START_TIME")) {
								promotionHdr.setSTART_TIME(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("END_DATE")) {
								promotionHdr.setEND_DATE(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("END_TIME")) {
								promotionHdr.setEND_TIME(StrUtils.fString(fileItem.getString()).trim());
							}
							
//							if (fileItem.getFieldName().equalsIgnoreCase("LIMIT_USAGE")) {
//								promotionHdr.setLIMIT_OF_USAGE(Double.valueOf(StrUtils.fString(fileItem.getString()).trim()));
//							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("ACTIVE")) {
								promotionHdr.setIsActive(StrUtils.fString(fileItem.getString()).trim());
								//promotionHdr.setIsActive(StrUtils.fString((fileItem.getString() != null) ? "Y": "N").trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("NOTES")) {
								promotionHdr.setNOTES(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("UNITPRICE")) {
								promotionHdr.setUNITPRICE(Double.parseDouble(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("DQTY")) {
								promotionHdr.setQTY(Double.parseDouble(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("DPRICE")) {
								promotionHdr.setDISCOUNT(Double.parseDouble(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("DTYPE")) {
								promotionHdr.setDISCOUNT_TYPE(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("DLimitUsage")) {
								promotionHdr.setLIMIT_OF_USAGE(Double.parseDouble(StrUtils.fString(fileItem.getString()).trim()));
							}
							
							/* PODET */
							
							if (fileItem.getFieldName().equalsIgnoreCase("lnno")) {
								Lnno.add(StrUtils.fString(fileItem.getString()).trim());
								lnnocount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("item")) {
								buyitem.add(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("qty")) {
								buyqty.add(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("get_item")) {
								getitem.add(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("get_qty")) {
								getqty.add(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("promotion_type")) {
								promotiontype.add(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("promotionvalue")) {
								promotionvalue.add(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("LIMIT_USAGE")) {
								limitusage.add(StrUtils.fString(fileItem.getString()).trim());
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("chkdDoNo")) {
								chkdDoNo.add(StrUtils.fString(fileItem.getString()).trim());
								CHKDcount++;
							}
							
							if (fileItem.getFieldName().equalsIgnoreCase("get_price")) {
								unitprice.add(StrUtils.fString(fileItem.getString()).trim());
							}
					}
					}
					promotionHdr.setCRBY(username);
					promotionHdr.setCRAT(DateUtils.getDateTime());
					
					if (action.equalsIgnoreCase("edit")) {
						promotionHdr.setUPAT(DateUtils.getDateTime());
						promotionHdr.setUPBY(username);
						
					}
					boolean issadded = false; 
					int hdrid = 0;
					String message = "",hdid="";
					int promoid = promotionHdr.getID();
					if (action.equalsIgnoreCase("edit")) {
						
						for (Object outletObj : chkdDoNo) {
				            String outlet = (String) outletObj;
//				            String[] Spparts = outlet.split("$");
				            String[] Spparts = outlet.split("\\$");

				            if (Spparts.length > 1 && !Spparts[1].isEmpty()) {
				                promoid = Integer.valueOf(Spparts[1]);
				            } else {
				                promoid = 0;
				            }
				            outlet = outlet.split("\\$")[0];
					        	  if(promoid==0) {
					        		  promotionHdr.setOUTLET(outlet);
					        		  hdrid = PosItemHdrDAO.addPosProductHdrReturnKey(promotionHdr);
					        		  promoid = hdrid;
					        		  issadded = true;
					        	  }else {
					        		  promotionHdr.setID(promoid);
						        	  promotionHdr.setOUTLET(outlet);
					        		  issadded = PosItemHdrDAO.updateProductHdr(promotionHdr,promoid);  
					        		  poDetDAO.DeletePromotionDet(plant, promoid);
					        	  }
					        	  
					        	  List<String> extractedIDsList = (List<String>)chkdDoNo.stream().map(item -> {String[] parts = ((String) item).split("\\$");
					        	                return parts.length > 1 && !parts[1].isEmpty() ? parts[1] : "0";}).collect(Collectors.toList());
					        	  
					        	  List<String> editIDList = Arrays.asList(editID.split(","));
					        	  List<String> idsToDelete = editIDList.stream().filter(id -> !extractedIDsList.contains(id)).collect(Collectors.toList());

					        	    // Perform deletion for each unmatched ID
					        	    for (String idToDelete : idsToDelete) {
					        	        int deleteID = Integer.parseInt(idToDelete);
					        	        PosItemHdrDAO.DeletePromotionHdr(plant, deleteID);
					        	        poDetDAO.DeletePromotionDet(plant, deleteID);
					        	        issadded = true;
					        	    }
					        	    
					        	    
					        	    
					        	    List<PosItemPromotionDet> podetlist = new ArrayList<PosItemPromotionDet>();
									
									for (int i = 0; i < lnnocount; i++) {
										int index = Integer.parseInt((String) Lnno.get(i)) - 1;
										int lnno = i + 1;
										PosItemPromotionDet poDet = new PosItemPromotionDet();
										poDet.setPLANT(plant);
										poDet.setLNNO(lnno);
										poDet.setHDRID(promoid);
										poDet.setBUY_ITEM((String) buyitem.get(index));
										poDet.setBUY_QTY((String) buyqty.get(index));
										if(promotionHdr.getBY_VALUE()==0) {
											poDet.setGET_ITEM((String) getitem.get(index));
											poDet.setGET_QTY(BigDecimal.valueOf(Double.parseDouble((String) getqty.get(index))));
										}else if(promotionHdr.getBY_VALUE()==1) {
											poDet.setGET_ITEM("");
											poDet.setGET_QTY(BigDecimal.valueOf(Double.parseDouble("0.0")));
										}
										if(promotionHdr.getBY_VALUE()==0) {
											poDet.setPROMOTION_TYPE("");
											poDet.setPROMOTION(BigDecimal.valueOf(Double.parseDouble("0.0")));
										}else if(promotionHdr.getBY_VALUE()==1) {
											poDet.setPROMOTION_TYPE((String) promotiontype.get(index));
											poDet.setPROMOTION(BigDecimal.valueOf(Double.parseDouble((String) promotionvalue.get(index))));
										}else {
											poDet.setUNITPRICE(Double.parseDouble((String) unitprice.get(index)));
											
											poDet.setBUY_QTY("");
											poDet.setPROMOTION_TYPE("");
											poDet.setPROMOTION(BigDecimal.valueOf(Double.parseDouble("0.0")));
											poDet.setGET_ITEM("");
											poDet.setGET_QTY(BigDecimal.valueOf(Double.parseDouble("0.0")));
										}
										poDet.setLIMIT_OF_USAGE(Double.parseDouble((String) limitusage.get(index)));
										poDet.setCRBY(username);
										poDet.setCRAT(DateUtils.getDateTime());
										poDetDAO.addPosProductDet(poDet);
										podetlist.add(poDet);
									}
					        	    
					        	  
				        }
						
					}else {
						for(int k=0;k<CHKDcount;k++)
						{
							promotionHdr.setOUTLET((String)chkdDoNo.get(k));
							hdrid = PosItemHdrDAO.addPosProductHdrReturnKey(promotionHdr);
						 
						
						if(hdrid > 0) {
							issadded = true;
						}
						
						if(issadded) {
							List<PosItemPromotionDet> podetlist = new ArrayList<PosItemPromotionDet>();
							
								for (int i = 0; i < lnnocount; i++) {
									int index = Integer.parseInt((String) Lnno.get(i)) - 1;
									int lnno = i + 1;
									PosItemPromotionDet poDet = new PosItemPromotionDet();
									poDet.setPLANT(plant);
									poDet.setLNNO(lnno);
									if (action.equalsIgnoreCase("edit")) {
										poDet.setHDRID(promoid);
									}else {
										poDet.setHDRID(hdrid);
									}
									poDet.setBUY_ITEM((String) buyitem.get(index));
									poDet.setBUY_QTY((String) buyqty.get(index));
									if(promotionHdr.getBY_VALUE()==0) {
										poDet.setGET_ITEM((String) getitem.get(index));
										poDet.setGET_QTY(BigDecimal.valueOf(Double.parseDouble((String) getqty.get(index))));
									}else if(promotionHdr.getBY_VALUE()==1) {
										poDet.setGET_ITEM("");
										poDet.setGET_QTY(BigDecimal.valueOf(Double.parseDouble("0.0")));
									}
									if(promotionHdr.getBY_VALUE()==0) {
										poDet.setPROMOTION_TYPE("");
										poDet.setPROMOTION(BigDecimal.valueOf(Double.parseDouble("0.0")));
									}else if(promotionHdr.getBY_VALUE()==1) {
										poDet.setPROMOTION_TYPE((String) promotiontype.get(index));
										poDet.setPROMOTION(BigDecimal.valueOf(Double.parseDouble((String) promotionvalue.get(index))));
									}else {
										poDet.setUNITPRICE(Double.parseDouble((String) unitprice.get(index)));
										
										poDet.setBUY_QTY("");
										poDet.setPROMOTION_TYPE("");
										poDet.setPROMOTION(BigDecimal.valueOf(Double.parseDouble("0.0")));
										poDet.setGET_ITEM("");
										poDet.setGET_QTY(BigDecimal.valueOf(Double.parseDouble("0.0")));
									}
									poDet.setLIMIT_OF_USAGE(Double.parseDouble((String) limitusage.get(index)));
									poDet.setCRBY(username);
									poDet.setCRAT(DateUtils.getDateTime());
									poDetDAO.addPosProductDet(poDet);
									podetlist.add(poDet);
								}
								

							Hashtable htRecvHis = new Hashtable();
							htRecvHis.clear();
							htRecvHis.put(IDBConstants.PLANT, plant);
							if (action.equalsIgnoreCase("edit")) {
								htRecvHis.put(IDBConstants.DIRTYPE, TransactionConstants.EDIT_PRD_PRM);
							}else {
								htRecvHis.put(IDBConstants.DIRTYPE, TransactionConstants.CREATE_PRD_PRM);
							}
							htRecvHis.put(IDBConstants.CUSTOMER_CODE, promotionHdr.getPROMOTION_NAME());
							htRecvHis.put(IDBConstants.POHDR_JOB_NUM, promotionHdr.getBY_VALUE());
							htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, promotionHdr.getPROMOTION_DESC());
							htRecvHis.put(IDBConstants.CREATED_BY, username);
							htRecvHis.put("MOVTID", "");
							htRecvHis.put("RECID", "");
							htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							boolean flag = movHisDao.insertIntoMovHis(htRecvHis);

							DbBean.CommitTran(ut);
							
							if (action.equalsIgnoreCase("edit")) 
							 message = "Product Promotion Updated Successfully.";
							else
							 message = "Product Promotion Added Successfully.";
								
						} 
						}
						response.sendRedirect("../productpromotion/summary?result=" + message);
					}
					
					if(issadded) {
						if (action.equalsIgnoreCase("edit"))
						{
							
						
//						List<PosItemPromotionDet> podetlist = new ArrayList<PosItemPromotionDet>();
//						
//							for (int i = 0; i < lnnocount; i++) {
//								int index = Integer.parseInt((String) Lnno.get(i)) - 1;
//								int lnno = i + 1;
//								for (Object outletObj : chkdDoNo) {
//						            String outlet = (String) outletObj;
//						            String[] Spparts = outlet.split("\\$");
//
//						            if (Spparts.length > 1 && !Spparts[1].isEmpty()) {
//						                promoid = Integer.valueOf(Spparts[1]);
//						            } else {
//						                promoid = hdrid;
//						            }
//						            
//						            outlet = outlet.split("\\$")[0];
////						            Hashtable htData = new Hashtable();
////									String quer="SELECT  ID FROM ["+plant+"_POSITEMPROMOTIONHDR] WHERE OUTLET = '"+outlet+"' and PROMOTION_NAME = '"+promoname+"' and plant ='"+plant+"' ";
////						        	  ArrayList arrLi = new ItemMstUtil().selectForReport(quer,htData,"");
////						        	  if (arrLi.size() > 0) {
////							        	  Map mst = (Map) arrLi.get(0);
////							        	  promoid =  Integer.valueOf((String) mst.get("ID"));
////						        	  }
//								PosItemPromotionDet poDet = new PosItemPromotionDet();
//								poDet.setPLANT(plant);
//								poDet.setLNNO(lnno);
//								if (action.equalsIgnoreCase("edit")) {
//									poDet.setHDRID(promoid);
//								}else {
//									poDet.setHDRID(hdrid);
//								}
//								poDet.setBUY_ITEM((String) buyitem.get(index));
//								poDet.setBUY_QTY((String) buyqty.get(index));
//								if(promotionHdr.getBY_VALUE()==0) {
//									poDet.setGET_ITEM((String) getitem.get(index));
//									poDet.setGET_QTY(BigDecimal.valueOf(Double.parseDouble((String) getqty.get(index))));
//								}else if(promotionHdr.getBY_VALUE()==1) {
//									poDet.setGET_ITEM("");
//									poDet.setGET_QTY(BigDecimal.valueOf(Double.parseDouble("0.0")));
//								}
//								if(promotionHdr.getBY_VALUE()==0) {
//									poDet.setPROMOTION_TYPE("");
//									poDet.setPROMOTION(BigDecimal.valueOf(Double.parseDouble("0.0")));
//								}else if(promotionHdr.getBY_VALUE()==1) {
//									poDet.setPROMOTION_TYPE((String) promotiontype.get(index));
//									poDet.setPROMOTION(BigDecimal.valueOf(Double.parseDouble((String) promotionvalue.get(index))));
//								}else {
//									poDet.setUNITPRICE(Double.parseDouble((String) unitprice.get(index)));
//									
//									poDet.setBUY_QTY("");
//									poDet.setPROMOTION_TYPE("");
//									poDet.setPROMOTION(BigDecimal.valueOf(Double.parseDouble("0.0")));
//									poDet.setGET_ITEM("");
//									poDet.setGET_QTY(BigDecimal.valueOf(Double.parseDouble("0.0")));
//								}
//								poDet.setLIMIT_OF_USAGE(Double.parseDouble((String) limitusage.get(index)));
//								poDet.setCRBY(username);
//								poDet.setCRAT(DateUtils.getDateTime());
//								poDetDAO.addPosProductDet(poDet);
//								podetlist.add(poDet);
//							}
//							}
							

						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, plant);
						if (action.equalsIgnoreCase("edit")) {
							htRecvHis.put(IDBConstants.DIRTYPE, TransactionConstants.EDIT_PRD_PRM);
						}else {
							htRecvHis.put(IDBConstants.DIRTYPE, TransactionConstants.CREATE_PRD_PRM);
						}
						htRecvHis.put(IDBConstants.CUSTOMER_CODE, promotionHdr.getPROMOTION_NAME());
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, promotionHdr.getBY_VALUE());
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, promotionHdr.getPROMOTION_DESC());
						htRecvHis.put(IDBConstants.CREATED_BY, username);
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						boolean flag = movHisDao.insertIntoMovHis(htRecvHis);

						DbBean.CommitTran(ut);
						/* String message = ""; */
						if (action.equalsIgnoreCase("edit")) 
						 message = "Product Promotion Updated Successfully.";
						else
						 message = "Product Promotion Added Successfully.";
							response.sendRedirect("../productpromotion/summary?result=" + message);
						}
						} else {
						DbBean.RollbackTran(ut);
						/* String message = ""; */
						if (action.equalsIgnoreCase("edit")) 
						 message = "Unable To Update Product Promotion.";
						else
						 message = "Unable To Add Product Promotion.";
						if (ajax) {
							resultJson.put("MESSAGE", message);
							resultJson.put("ERROR_CODE", "99");
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(resultJson.toString());
							response.getWriter().flush();
							response.getWriter().close();
						}else {
							response.sendRedirect("../productpromotion/summary?result=" + message);
						}
					}

				}
			}catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				if (ajax) {
					resultJson.put("MESSAGE", ThrowableUtil.getMessage(e));
					resultJson.put("ERROR_CODE", "98");
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(resultJson.toString());
					response.getWriter().flush();
					response.getWriter().close();
				}else {
					response.sendRedirect("../productpromotion/summary?result=" + ThrowableUtil.getMessage(e));
				}
			}

		}
		
		if (action.equalsIgnoreCase("LoadEditDetails")) {
			JSONObject jsonObjectResult = new JSONObject();
			jsonObjectResult = getEditPrdPromotionDetails(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}

}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();

			if(action.equalsIgnoreCase("new")) {
				try {
					String msg = StrUtils.fString(request.getParameter("msg"));
					request.setAttribute("Msg", msg);
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/createProductPromotion.jsp");
				rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
			if(action.equalsIgnoreCase("summary")) {
				try {
					String msg = StrUtils.fString(request.getParameter("msg"));
					request.setAttribute("Msg", msg);
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/ProductPromotionSummary.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
			if(action.equalsIgnoreCase("edit")) {
				try {
					String ID = StrUtils.fString(request.getParameter("ID"));
					request.setAttribute("ID", ID);
					String msg = StrUtils.fString(request.getParameter("msg"));
					request.setAttribute("Msg", msg);
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/EditProductPromotion.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	}
	
	private JSONObject getEditPrdPromotionDetails(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		PosItemPromotionDetDAO posPromotionDetDAO = new PosItemPromotionDetDAO();
		PosItemPromotionHdrDAO posPromotionHdrDAO = new PosItemPromotionHdrDAO();
		ItemMstDAO itemMstDAO = new ItemMstDAO();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		ItemUtil itemUtil = new ItemUtil();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			String id = (StrUtils.fString(request.getParameter("ID")).trim());
			String firstId = id.split(",")[0];
			id = firstId;
//			int id = Integer.parseInt(StrUtils.fString(request.getParameter("ID")).trim());

			String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			ArrayList podetail = posPromotionDetDAO.getPrdPrmDetById(plant, id);
			String lnno = "",buyitem = "",getitem ="",promotype ="";
			
			double limit_of_usage= 0 ;
			double unitprice= 0 ;
			double promo = 0;
			double getqty = 0;
			double buyqty = 0;
			if (podetail.size() > 0) {
				for (int i=0;i<podetail.size();i++) {
					
					 Map map = (Map) podetail.get(i);
					JSONObject resultJsonInt = new JSONObject();

					String catlogpath = itemMstDAO.getcatlogpath(plant, buyitem);
					String cpath = ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png"
							: "/track/ReadFileServlet/?fileLocation=" + catlogpath);

			        lnno = StrUtils.fString((String)map.get("LNNO"));
			        buyitem = StrUtils.fString((String)map.get("BUY_ITEM"));
			        buyqty = Double.parseDouble(StrUtils.fString((String)map.get("BUY_QTY")));
			        getitem = StrUtils.fString((String)map.get("GET_ITEM"));
			        getqty = Double.parseDouble(StrUtils.fString((String)map.get("GET_QTY")));
			        promotype = StrUtils.fString((String)map.get("PROMOTION_TYPE"));
			        promo = Double.parseDouble(StrUtils.fString((String)map.get("PROMOTION")));
			        limit_of_usage = Double.parseDouble(StrUtils.fString((String)map.get("LIMIT_OF_USAGE")));
			        unitprice = Double.parseDouble(StrUtils.fString((String)map.get("UNITPRICE")));
			        
			        String itemprice =new ItemMstDAO().getItemPrice(plant,buyitem);
					
					resultJsonInt.put("LNNO", lnno);
					resultJsonInt.put("BUY_ITEM", buyitem);
					resultJsonInt.put("BUY_QTY", StrUtils.addZeroes(buyqty, com.track.gates.DbBean.NOOFDECIMALPTSFORWEIGHT));
					resultJsonInt.put("GET_ITEM", getitem);
					resultJsonInt.put("GET_QTY", StrUtils.addZeroes(getqty, com.track.gates.DbBean.NOOFDECIMALPTSFORWEIGHT));
					resultJsonInt.put("PROMOTION_TYPE", promotype);
					resultJsonInt.put("PROMOTION", StrUtils.addZeroes(promo, numberOfDecimal));
					resultJsonInt.put("LIMIT_OF_USAGE", StrUtils.addZeroes(limit_of_usage, com.track.gates.DbBean.NOOFDECIMALPTSFORWEIGHT));
					resultJsonInt.put("UNITPRICE", StrUtils.addZeroes(unitprice, com.track.gates.DbBean.NOOFDECIMALPTSFORWEIGHT));
					resultJsonInt.put("CATLOGPATH", cpath);
					resultJsonInt.put("BASEPRICE", StrUtils.addZeroes(Double.parseDouble(itemprice) , numberOfDecimal));
					jsonArray.add(resultJsonInt);
				}
					resultJson.put("orders", jsonArray);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
					resultJsonInt.put("ERROR_CODE", "100");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("errors", jsonArrayErr);
				} else {
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
					resultJsonInt.put("ERROR_CODE", "99");
					jsonArrayErr.add(resultJsonInt);
					jsonArray.add("");
					resultJson.put("items", jsonArray);
					resultJson.put("errors", jsonArrayErr);
				}
			} catch (Exception e) {
					resultJson.put("SEARCH_DATA", "");
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", ThrowableUtil.getMessage(e));
					resultJsonInt.put("ERROR_CODE", "98");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("ERROR", jsonArrayErr);
			}
				return resultJson;
	}
	
	@Override
	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		// TODO Auto-generated method stub
		
	}

}

		