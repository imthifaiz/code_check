package com.track.db.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.transaction.UserTransaction;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.SConstant;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.DNPLDetDAO;
import com.track.dao.DNPLHdrDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.DoTransferDetDAO;
import com.track.dao.DoTransferHdrDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.LoanDetDAO;
import com.track.dao.LoanHdrDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.ToDetDAO;
import com.track.dao.ToHdrDAO;
import com.track.gates.DbBean;
import com.track.gates.sqlBean;
import com.track.tran.WmsLoanOrderPicking;
import com.track.tran.WmsLoanOrderReceving;
import com.track.tran.WmsTran;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class LoanUtil {

	LoanHdrDAO _loanHdrDAO = null;
	LoanDetDAO _loanDetDAO = null;
	PoDetDAO _PoDetDAO = null;
	StrUtils strUtils = new StrUtils();
	private boolean printLog = MLoggerConstant.LoanUtil_PRINTPLANTMASTERLOG;
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public LoanUtil() {
		_PoDetDAO = new PoDetDAO();
		_loanHdrDAO = new LoanHdrDAO();
		_loanDetDAO = new LoanDetDAO();
		_loanDetDAO.setmLogger(mLogger);
		_loanHdrDAO.setmLogger(mLogger);

	}

	public ArrayList getLoanAssigneeHdrDetails(String query, Hashtable ht,
			String extCond) throws Exception {
		ArrayList al = new ArrayList();

		try {
			_loanHdrDAO.setmLogger(mLogger);
			al = _loanHdrDAO.selectLoanAssigneeHdrDetails(query, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}

	/**********************  Modification History  ******************************
		 *** Bruhan,Nov 10 2014,Description: To include QTYIS,QTYRC in link
	 */ 
	public String listLoanDET(String aOrder, String plant, String rFlag)
			throws Exception {
		String q = "";
		String link = "";
		String result = "";

		try {
			Hashtable htCond = new Hashtable();
			htCond.put("ORDNO", aOrder);
			q = "ORDNO,ORDLNNO,lnstat,PickStatus,item,isnull(unitmo,'') UOM,isnull(LISTPRICE,0) LISTPRICE,isnull(RENTALPRICE,0) RENTALPRICE,ISNULL((select ISNULL(SUM(QTYOR),0)-ISNULL(SUM(qtyis),0) from [" + plant + "_LOANDET] where ITEM=A.ITEM   and PICKSTATUS <>'C' group by item),0) OUTGOINGQTY,isnull((select ISNULL(SUM(QTY),0) from " + plant + "_INVMST where ITEM=A.ITEM  group by item),0)as STOCKONHAND,isnull(PRODGST,'') as PRODGST,isnull(itemdesc,'') as itemdesc,isnull(qtyor,0) as qtyor,isnull(qtyis,0) as qtyis,isnull(qtyrc,0) as qtyrc ";
			ArrayList al = _loanDetDAO.selectLoanDet(q, htCond,
					" plant <> '' order by ORDLNNO", plant);

			if (al.size() > 0) {
				ItemMstDAO _ItemMstDAO = new ItemMstDAO();
				_ItemMstDAO.setmLogger(mLogger);
				for (int i = 0; i < al.size(); i++) {
					Map m = (Map) al.get(i);
					String dono = (String) m.get("ORDNO");
					String dolnno = (String) m.get("ORDLNNO");
					String item = (String) m.get("item");
					String itemdesc = (String) m.get("itemdesc");
					String PRODGST = (String) m.get("PRODGST");
					String OUTGOINGQTY = (String) m.get("OUTGOINGQTY");
					String STOCKONHAND = (String) m.get("STOCKONHAND");
					String uom = (String) m.get("UOM");
					String PRDREMARKS =(String) m.get("PRDREMARKS");
					String RENTALPRICE = (String) m.get("RENTALPRICE");
					String LISTPRICE = (String) m.get("LISTPRICE");
					String qtyor = StrUtils.formatNum((String) m.get("qtyor"));
					String qtyis = StrUtils.formatNum((String) m.get("qtyis"));
					String qtyrc = StrUtils.formatNum((String) m.get("qtyrc"));
					String desc = _ItemMstDAO.getItemDesc(plant, item);
				//	String uom = _ItemMstDAO.getItemUOM(plant, item);
					String lnstat = (String) m.get("lnstat");
					String pickstatus = (String) m.get("PickStatus");
					link = "<a href=\"" + "modiLOANDET.jsp?DONO=" + dono
							+ "&DOLNNO=" + dolnno + "&ITEM=" + item + "&ITEMDESC=" + itemdesc + "&UOM=" + uom + "&LISTPRICE=" + LISTPRICE + "&RENTALPRICE=" + RENTALPRICE + "&PRODGST=" + PRODGST + "&PRDREMARKS=" + PRDREMARKS + "&OUTGOINGQTY=" + OUTGOINGQTY + "&qtyor=" + qtyor + "&STOCKONHAND=" + STOCKONHAND + "&QTYPICK=" + qtyis+"&QTYRC=" + qtyrc+"&PICKSTATUS=" + pickstatus +"&RFLAG=" 
							+ rFlag + "\">";
					String select = "";

					select = "<INPUT Type=Checkbox  style=\""
							+ "border:0;background=#dddddd\"" + " name=\""
							+ "chkdDoNo\"" + " value=\"" + item
							+ "\">&nbsp;&nbsp;&nbsp;";
					if (lnstat.equals("N")) {
						result += "<tr valign=\"middle\">"
								+ "<td align=\"center\" width=\"10%\">"
								+ dolnno
								+ "</td>"
								+ "<td width=\"17%\">"
								+ link
								+ "<FONT COLOR=\"blue\">"
								+ sqlBean.formatHTML(item)
								+ "</a></font>"
								+ "</td>"
								+ "<td width=\"27%\">"
								+ sqlBean.formatHTML(desc)
								+ "</td>"
								+ "<td width=\"12%\" align = \"center\">"
								+ sqlBean.formatHTML(qtyor)
								+ "</td>"
								+ "<td width=\"12%\" align = \"center\">"
								+ sqlBean.formatHTML(qtyis)
								+ "</td>"
								+ "<td width=\"12%\" align = \"center\" >"
								+ sqlBean.formatHTML(qtyrc)
								+ "</td>"
								+ "<td width=\"5%\" align = \"center\">"
								+ uom
								+ "</td>"
								+ "<td width=\"5%\" align = \"center\">"
								+ lnstat
								+ "</td>"
								+ "</tr>";
					} else {
						result += "<tr valign=\"middle\">"
								+ "<td align=\"center\" width=\"10%\">"
								+ dolnno
								+ "</td>"
								+ "<td width=\"17%\">"
								+ link
								+ "<FONT COLOR=\"blue\">"
								+ sqlBean.formatHTML(item)
								+ "</a></font>"
								+ "</td>"
								+ "<td width=\"27%\">"
								+ sqlBean.formatHTML(desc)
								+ "</td>"
								+ "<td width=\"12%\" align = \"center\">"
								+ sqlBean.formatHTML(qtyor)
								+ "</td>"
								+ "<td width=\"12%\" align = \"center\" >"
								+ sqlBean.formatHTML(qtyis)
								+ "</td>"
								+ "<td width=\"12%\" align = \"center\">"
								+ sqlBean.formatHTML(qtyrc)
								+ "</td>"
								+ "<td width=\"5%\" align = \"center\">"
								+ uom
								+ "</td>"
								+ "<td width=\"5%\" align = \"center\">"
								+ lnstat
								+ "</td>"
								+ "</tr>";

					}

				}
			} else {

				result += "<tr valign=\"middle\"><td colspan=\"8\" align=\"center\">"
						+ " Please add in Products " + "</td></tr>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return result;
	}

	public String listLoanDETToPrint(String aOrder, String plant, String rFlag)
			throws Exception {
		String q = "";
		String link = "";
		String result = "";

		try {
			Hashtable htCond = new Hashtable();
			htCond.put("ORDNO", aOrder);
			q = "ORDNO,ORDLNNO,lnstat,item,isnull(qtyor,0) as qtyor,isnull(qtyis,0) as qtyis,isnull(qtyrc,0) as qtyrc ";
			ArrayList al = _loanDetDAO.selectLoanDet(q, htCond,
					" plant <> '' order by ORDLNNO", plant);

			if (al.size() > 0) {
				ItemMstDAO _ItemMstDAO = new ItemMstDAO();
				_ItemMstDAO.setmLogger(mLogger);
				for (int i = 0; i < al.size(); i++) {
					Map m = (Map) al.get(i);
					String dono = (String) m.get("ORDNO");
					String dolnno = (String) m.get("ORDLNNO");
					String item = (String) m.get("item");
					String qtyor = (String) m.get("qtyor");

					String desc = _ItemMstDAO.getItemDesc(plant, item);

					String lnstat = (String) m.get("lnstat");
					link = "<a href=\"" + "modiLOANDET.jsp?DONO=" + dono
							+ "&DOLNNO=" + dolnno + "&ITEM=" + item + "&RFLAG="
							+ rFlag + "\">";
					link = "";
					String select = "";

					select = "<INPUT Type=Checkbox  style=\""
							+ "border:0;background=#dddddd\"" + " name=\""
							+ "chkdDoNo\"" + " value=\"" + item
							+ "\">&nbsp;&nbsp;&nbsp;";

					result += "<tr valign=\"middle\">"
							+ "<td align=\"center\" width=\"10%\">" + dolnno 
							+ "</td>" + "<td width=\"17%\">" + link
							+ "<FONT COLOR=\"blue\">"
							+ sqlBean.formatHTML(item)+ "</font>" + "</td>"
							+ "<td width=\"27%\">" + sqlBean.formatHTML(desc)
							+ "</td>" + "<td width=\"17%\" align = right>"
							+ sqlBean.formatHTML(qtyor) + "</td>" + "</tr>";

				}
			} else {

				result += "<tr valign=\"middle\"><td colspan=\"8\" align=\"center\">"
						+ " Please add in Products " + "</td></tr>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return result;
	}

	public ArrayList getLoanHdrList(String query, Hashtable ht, String extracon)
			throws Exception {
		ArrayList al = new ArrayList();

		try {
			_loanHdrDAO.setmLogger(mLogger);
			al = _loanHdrDAO.selectHdr(query, ht, extracon);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}

	public ArrayList getLoanHdrDetails(String query, Hashtable ht,
			String extCond) throws Exception {
		ArrayList al = new ArrayList();

		try {
			_loanHdrDAO.setmLogger(mLogger);
			al = _loanHdrDAO.selectLoanAssigneeHdrDetails(query, ht, extCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}

	public boolean updateLoanHdr(Hashtable ht) throws Exception {
		boolean flag = false;

		Hashtable htCond = new Hashtable();
		htCond.put(IDBConstants.PLANT, (String) ht.get(IDBConstants.PLANT));
		htCond.put(IDBConstants.LOANDET_ORDNO, (String) ht
				.get(IDBConstants.LOANDET_ORDNO));
		try {

			StringBuffer updateQyery = new StringBuffer("set ");
			updateQyery.append(IDBConstants.LOANHDR_JOB_NUM + " = '"
					+ (String) ht.get(IDBConstants.LOANHDR_JOB_NUM) + "'");
			updateQyery.append("," + IDBConstants.LOANHDR_CONTACT_NUM + " = '"
					+ (String) ht.get(IDBConstants.LOANHDR_CONTACT_NUM) + "'");
			updateQyery.append("," + IDBConstants.LOANHDR_COL_DATE + " = '"
					+ (String) ht.get(IDBConstants.LOANHDR_COL_DATE) + "'");
			updateQyery.append("," + IDBConstants.LOANHDR_REMARK1 + " = '"
					+ (String) ht.get(IDBConstants.LOANHDR_REMARK1) + "'");
			updateQyery.append("," + IDBConstants.LOANHDR_REMARK2 + " = '"
					+ (String) ht.get(IDBConstants.LOANHDR_REMARK2) + "'");
			updateQyery.append("," + IDBConstants.LOANHDR_CUST_NAME + " = '"
					+ (String) ht.get(IDBConstants.LOANHDR_CUST_NAME) + "'");
			updateQyery.append("," + IDBConstants.LOANHDR_PERSON_INCHARGE
					+ " = '"
					+ (String) ht.get(IDBConstants.LOANHDR_PERSON_INCHARGE)
					+ "'");
			updateQyery.append("," + IDBConstants.LOANHDR_ADDRESS + " = '"
					+ (String) ht.get(IDBConstants.LOANHDR_ADDRESS) + "'");
			updateQyery.append("," + IDBConstants.LOANHDR_ADDRESS2 + " = '"
					+ (String) ht.get(IDBConstants.LOANHDR_ADDRESS2) + "'");
			updateQyery.append("," + IDBConstants.LOANHDR_ADDRESS3 + " = '"
					+ (String) ht.get(IDBConstants.LOANHDR_ADDRESS3) + "'");
			updateQyery.append("," + IDBConstants.LOANHDR_COL_TIME + " = '"
					+ (String) ht.get(IDBConstants.LOANHDR_COL_TIME) + "'");
			updateQyery.append(",LOC = '" + (String) ht.get("LOC") + "'");
			updateQyery.append(",LOC1 = '" + (String) ht.get("LOC1") + "'");
			updateQyery.append(",EXPIREDATE = '" + (String) ht.get("EXPIREDATE") + "'");
			
			updateQyery.append("," + IDBConstants.ORDERTYPE + " = '"
					+ (String) ht.get(IDBConstants.ORDERTYPE) + "'");
			updateQyery.append("," + IDBConstants.CURRENCYID + " = '"
					+ (String) ht.get(IDBConstants.CURRENCYID) + "'");
			updateQyery.append("," + IDBConstants.LOANHDR_GST + " = '"
					+ (String) ht.get(IDBConstants.LOANHDR_GST) + "'");
			updateQyery.append("," + IDBConstants.LOANHDR_EMPNO + " = '"
					+ (String) ht.get(IDBConstants.LOANHDR_EMPNO) + "'");
			updateQyery.append("," + IDBConstants.DELIVERYDATE + " = '"
					+ (String) ht.get(IDBConstants.DELIVERYDATE) + "'");
			updateQyery.append("," + IDBConstants.PAYMENTTYPE + " = '"
					+ (String) ht.get(IDBConstants.PAYMENTTYPE) + "'");
			updateQyery.append("," + IDBConstants.SHIPPINGCUSTOMER + " = '"
					+ (String) ht.get(IDBConstants.SHIPPINGCUSTOMER) + "'");
			updateQyery.append("," + IDBConstants.ORDERDISCOUNT + " = '"
					+ (String) ht.get(IDBConstants.ORDERDISCOUNT) + "'");
			updateQyery.append("," + IDBConstants.SHIPPINGCOST + " = '"
					+ (String) ht.get(IDBConstants.SHIPPINGCOST) + "'");
			updateQyery.append("," + "DAYS" + " = '"
					+ (String) ht.get("DAYS") + "'");
			updateQyery.append("," + IDBConstants.LOANHDR_DELIVERYDATEFORMAT + " = '"
					+ (String) ht.get(IDBConstants.LOANHDR_DELIVERYDATEFORMAT) + "'");
			updateQyery.append("," + IDBConstants.LOANHDR_EXPIRYDATEFORMAT + " = '"
					+ (String) ht.get(IDBConstants.LOANHDR_EXPIRYDATEFORMAT) + "'");
			
			_loanHdrDAO.setmLogger(mLogger);
			flag = _loanHdrDAO.update(updateQyery.toString(), htCond, " AND STATUS ='N'");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		    throw new Exception("Processed Loan Order Cannot be modified");
		}
		return flag;
	}

	public boolean saveLoanHdrDetails(Hashtable ht) throws Exception {
		boolean flag = false;

		try {
			_loanHdrDAO.setmLogger(mLogger);
			flag = _loanHdrDAO.insertLoanHdr(ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			//throw e;
	throw new Exception("Loan order created already");
		}
		return flag;
	}
	
	public boolean updateLoanDet(Hashtable ht) throws Exception {
		boolean flag = false;

		Hashtable htCond = new Hashtable();
		htCond.put(IDBConstants.PLANT, (String) ht.get(IDBConstants.PLANT));
		htCond.put(IDBConstants.LOANDET_ORDNO, (String) ht
				.get(IDBConstants.LOANDET_ORDNO));
		try {

			StringBuffer updateQyery = new StringBuffer("set ");
			
			updateQyery.append("," + IDBConstants.LOANDET_RENTALPRICE + " = '"
					+ (String) ht.get(IDBConstants.LOANDET_RENTALPRICE) + "'");
			
			updateQyery.append("," + IDBConstants.LOANDET_QTYOR + " = '"
					+ (String) ht.get(IDBConstants.LOANDET_QTYOR) + "'");
			
			_loanDetDAO.setmLogger(mLogger);
			flag = _loanDetDAO.update(updateQyery.toString(), htCond, " AND STATUS ='N'");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		    throw new Exception("Processed Loan Order Cannot be modified");
		}
		return flag;
	}

	public boolean saveLoanDetDetails(Hashtable ht) throws Exception {
		boolean flag = false;

		try {
			LoanDetDAO _LoanDetDAO = new LoanDetDAO();
			_LoanDetDAO.setmLogger(mLogger);
			Hashtable htPoDet = new Hashtable();
			htPoDet.put(IDBConstants.PLANT, ht.get(IDBConstants.PLANT));
			htPoDet.put(IDBConstants.LOANDET_ORDNO, ht
					.get(IDBConstants.LOANDET_ORDNO));
			htPoDet.put(IDBConstants.ITEM, ht.get(IDBConstants.ITEM));

			flag = _LoanDetDAO.insertLoanDet(ht);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}

    public ArrayList getLineDetailsForLoanOrder(String Query, Hashtable ht,String ExtraCond) {

            ArrayList al = null;

            try {
                LoanDetDAO _LoanDetDAO = new LoanDetDAO();
                    _LoanDetDAO.setmLogger(mLogger);
                    al = _LoanDetDAO.selectLoanDet(Query, ht, ExtraCond);

                    return al;

            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    return null;
            }

    }
	public boolean deleteLoanDetLineDetails(Hashtable ht) throws Exception {
		boolean flag = false;

		try {
			LoanDetDAO _LoanDetDAO = new LoanDetDAO();
			RecvDetDAO recvdetDAO = new RecvDetDAO();
			_LoanDetDAO.setmLogger(mLogger);
			Hashtable htPoDet = new Hashtable();
			htPoDet.put(IDBConstants.PLANT, ht.get(IDBConstants.PLANT));
			htPoDet.put(IDBConstants.LOANDET_ORDNO, ht.get(IDBConstants.LOANDET_ORDNO));
			htPoDet.put(IDBConstants.LOANDET_ORDLNNO, ht.get(IDBConstants.LOANDET_ORDLNNO));
			htPoDet.put(IDBConstants.LOANDET_ITEM, ht.get(IDBConstants.LOANDET_ITEM));

			flag = _LoanDetDAO.delete(htPoDet);
			try{
    		    //To arrange the line no's
				htPoDet.remove(IDBConstants.LOANDET_ORDLNNO);
				htPoDet.remove(IDBConstants.LOANDET_ITEM);
    		        String  sql =  "SET ORDLNNO =ORDLNNO-1 ";
	                String extraCond=" AND ORDLNNO > '" + ht.get(IDBConstants.LOANDET_ORDLNNO) + "' ";
	                _LoanDetDAO.update(sql,htPoDet,extraCond);
	                
	                //To arrange the line no's in loanpick
                    boolean loanpickflag = false;
                    loanpickflag = _LoanDetDAO.isExisitLOPick(htPoDet);
                     if(loanpickflag)
                     { 
                       sql =  "SET ORDLNNO =ORDLNNO-1 ";
                       extraCond=" AND CAST(ORDLNNO as int) > " + ht.get(IDBConstants.LOANDET_ORDLNNO) + " ";
                       _LoanDetDAO.updateLOPickTable(sql,htPoDet,extraCond);
                     } 
                     
                   //To arrange the line no's in recvdet 
                     boolean recvdetflag = false;
                     Hashtable htDet = new Hashtable();
                     htDet.put(IDBConstants.PLANT, ht.get(IDBConstants.PLANT));
                     htDet.put(IDBConstants.PODET_PONUM, ht.get(IDBConstants.LOANDET_ORDNO));
                   
                     recvdetflag = recvdetDAO.isExisit(htDet, (String)ht.get(IDBConstants.PLANT));
                   if(recvdetflag)
                   {
                 	  sql =  "SET LNNO =LNNO-1 ";
                 	  extraCond=" AND CAST(LNNO as int) > " + ht.get(IDBConstants.LOANDET_ORDLNNO)  + " ";
                 	  recvdetDAO.update(sql,htDet,extraCond,(String)ht.get(IDBConstants.PLANT));
                   } 
                       
                       
	              
            }catch(Exception e){}
			if (flag) {

				MovHisDAO movhisDao = new MovHisDAO();
				movhisDao.setmLogger(mLogger);
				Hashtable htmovHis = new Hashtable();
				htmovHis.clear();
				htmovHis.put(IDBConstants.PLANT, (String) ht.get(IDBConstants.PLANT));
				htmovHis.put("DIRTYPE", "LOAN_DEL_ITEM");
				htmovHis.put(IDBConstants.ITEM, (String) ht.get(IDBConstants.LOANDET_ITEM));
				htmovHis.put(IDBConstants.MOVHIS_ORDNUM, (String) ht
						.get(IDBConstants.LOANDET_ORDNO));
				htmovHis.put(IDBConstants.MOVHIS_ORDLNO, (String) ht
						.get(IDBConstants.LOANDET_ORDLNNO));
				htmovHis.put(IDBConstants.CREATED_BY, (String) ht
						.get(IDBConstants.CREATED_BY));
				htmovHis.put("MOVTID", "");
				htmovHis.put("RECID", "");
				htmovHis.put(IDBConstants.TRAN_DATE, new DateUtils()
						.getDateinyyyy_mm_dd(new DateUtils().getDate()));
				htmovHis.put(IDBConstants.CREATED_AT, new DateUtils()
						.getDateTime());

				flag = movhisDao.insertIntoMovHis(htmovHis);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}
 
//---Modified by Bruhan on June 11 2014, Description: To include toexceltransactiondate
	public ArrayList getLoanOrderSummary(Hashtable ht, String afrmDate,
			String atoDate, String itemDesc, String plant,String custname) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
			LoanDetDAO loanDAO = new LoanDetDAO();
			loanDAO.setmLogger(mLogger);
                        
		   
			//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
	            sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ new StrUtils().InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
		    //start code by Bruhan for wildcard search loan assignee name for loan order summary  on 26 aug 13
            if (custname.length()>0){
             	custname = new StrUtils().InsertQuotes(custname);
             	sCondition = sCondition + " AND A.CUSTNAME LIKE '%"+custname+"%' " ;
             }
            
          //start code by Bruhan for wildcard search loan assignee name for loan order summary  on 26 aug 13
     
			if (afrmDate.length() > 0) {
				sCondition = sCondition + " AND  B.TRANDATE  >= '" + afrmDate
						+ "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND B.TRANDATE <= '" + atoDate
							+ "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + "  B.TRANDATE  <= '" + atoDate
							+ "'  ";
				}
			}
			
			

				String aQuery = " select b.Ordno ,a.jobNum,a.custname,b.item,b.itemdesc," 
					+ "a.collectionDate," 
					+ " CAST((SUBSTRING(a.collectionDate, 7, 4) + SUBSTRING(a.collectionDate, 4, 2) + SUBSTRING(a.collectionDate, 1, 2)) AS date)  AS toexceltransactiondate,"
					+"a.status as lnstat ,b.qtyor,isnull(a.CRBY,'') as users,isnull(b.UNITMO,'') as UNITMO,isnull(b.qtyis,0) as qtyis,isnull(b.qtyrc,0) as qtyrc,isnull(a.expiredate,'') expiredate,c.prd_brand_id,c.prd_cls_id,c.itemtype from "
					+ "["
					+ plant
					+ "_"
					+ "loanhdr] a,"
					+ "["
					+ plant
					+ "_"
					+ "loandet] b," 
					+ "["
					+ plant
					+ "_"
					+ "ITEMMST]c where a.ordno=b.ordno and b.ITEM=c.item  " + sCondition;
			
			
			
			arrList = loanDAO.selectForReport(aQuery, ht);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}

	public ArrayList getLoanOrderSummaryWithPrice(Hashtable ht, String afrmDate,
			String atoDate, String itemDesc, String plant,String custname) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
			LoanDetDAO loanDAO = new LoanDetDAO();
			loanDAO.setmLogger(mLogger);
                        
		   
			//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
	            sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ new StrUtils().InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
		    //start code by Bruhan for wildcard search loan assignee name for loan order summary  on 26 aug 13
            if (custname.length()>0){
             	custname = new StrUtils().InsertQuotes(custname);
             	sCondition = sCondition + " AND A.CUSTNAME LIKE '%"+custname+"%' " ;
             }
            
          //start code by Bruhan for wildcard search loan assignee name for loan order summary  on 26 aug 13
     
			if (afrmDate.length() > 0) {
				sCondition = sCondition + " AND  B.TRANDATE  >= '" + afrmDate
						+ "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND B.TRANDATE <= '" + atoDate
							+ "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + "  B.TRANDATE  <= '" + atoDate
							+ "'  ";
				}
			}
			
			

				String aQuery = " select b.Ordno ,a.jobNum,a.custname,b.item,b.itemdesc," 
					+ "a.collectionDate," 
					+ " CAST((SUBSTRING(a.collectionDate, 7, 4) + SUBSTRING(a.collectionDate, 4, 2) + SUBSTRING(a.collectionDate, 1, 2)) AS date)  AS toexceltransactiondate,"
					+"a.status as lnstat ,b.qtyor,isnull(a.CRBY,'') as users,isnull(b.UNITMO,'') as UNITMO,isnull(b.qtyis,0) as qtyis,isnull(b.qtyrc,0) as qtyrc,isnull(a.expiredate,'') expiredate,isnull(b.RENTALPRICE,'') RENTALPRICE,isnull(b.PRODGST,'') prodgst,isnull(b.RENTALPRICE*b.qtyor,'') orderprice,ISNULL((((b.RENTALPRICE*b.PRODGST)/100)*b.qtyor),0) as taxval,isnull(b.RENTALPRICE*b.qtyor+(b.RENTALPRICE*b.PRODGST*b.qtyor)/100,0)as totalwithtax,isnull(a.EMPNO,'') employeeid,c.prd_brand_id,c.prd_cls_id,c.itemtype from "
					+ "["
					+ plant
					+ "_"
					+ "loanhdr] a,"
					+ "["
					+ plant
					+ "_"
					+ "loandet] b," 
					+ "["
					+ plant
					+ "_"
					+ "ITEMMST]c where a.ordno=b.ordno and b.ITEM=c.item  " + sCondition;
			
			
			
			arrList = loanDAO.selectForReport(aQuery, ht);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}

	public ArrayList getLoanOrderSummaryByPrice(Hashtable ht, String afrmDate,
			String atoDate, String itemDesc, String plant,String custname) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
			LoanDetDAO loanDAO = new LoanDetDAO();
			loanDAO.setmLogger(mLogger);
                        
		   
			
	        if (itemDesc.length() > 0 ) {
		        if (itemDesc.indexOf("%") != -1) {
		        	itemDesc = itemDesc.substring(0, itemDesc.indexOf("%") - 1);
		        }
	            sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ new StrUtils().InsertQuotes(itemDesc.replaceAll(" ","")) + "%' ";
	        }
		    
            if (custname.length()>0){
             	custname = new StrUtils().InsertQuotes(custname);
             	sCondition = sCondition + " AND A.CUSTNAME LIKE '%"+custname+"%' " ;
             }
                   
     
			if (afrmDate.length() > 0) {
				sCondition = sCondition + " AND  B.TRANDATE  >= '" + afrmDate
						+ "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND B.TRANDATE <= '" + atoDate
							+ "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + "  B.TRANDATE  <= '" + atoDate
							+ "'  ";
				}
			}
			
				String aQuery = " select b.Ordno ,a.jobNum,a.custname,b.item,b.itemdesc," 
					+ "a.collectionDate," 
					+ " CAST((SUBSTRING(a.collectionDate, 7, 4) + SUBSTRING(a.collectionDate, 4, 2) + SUBSTRING(a.collectionDate, 1, 2)) AS date)  AS toexceltransactiondate,"
					+"isnull(b.PICKSTATUS,0) as pickstatus ,b.qtyor,isnull(b.UNITMO,0) as UNITMO,isnull(b.qtyis,0) as qtyis,isnull(b.qtyrc,0) as qtyrc,isnull(a.expiredate,'') expiredate,isnull(b.RENTALPRICE,'') RENTALPRICE,isnull(b.PRODGST,'') prodgst,isnull(b.RENTALPRICE*b.qtyor,'') orderprice,ISNULL((((b.RENTALPRICE*b.PRODGST)/100)*b.qtyor),0) as taxval,isnull(b.RENTALPRICE*b.qtyor+(b.RENTALPRICE*b.PRODGST*b.qtyor)/100,0)as totalwithtax,isnull(a.EMPNO,'') employeeid,c.prd_brand_id,c.prd_cls_id,c.itemtype from "
					+ "["
					+ plant
					+ "_"
					+ "loanhdr] a,"
					+ "["
					+ plant
					+ "_"
					+ "loandet] b," 
					+ "["
					+ plant
					+ "_"
					+ "ITEMMST]c where a.ordno=b.ordno and b.PICKSTATUS != 'N' and b.ITEM=c.item  " + sCondition;
			
			
			
			arrList = loanDAO.selectForReport(aQuery, ht);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}
	
	public ArrayList getLoanHdrDetails(String query, Hashtable ht)
			throws Exception {
		ArrayList al = new ArrayList();

		try {
			_loanHdrDAO.setmLogger(mLogger);
			al = _loanHdrDAO.selectLoanAssigneeHdrDetails(query, ht, "");
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}

	public ArrayList getLoanHdrDetailsFromLoanHdr(String query, Hashtable ht)
			throws Exception {
		ArrayList al = new ArrayList();

		try {
			_loanHdrDAO.setmLogger(mLogger);
			al = _loanHdrDAO.selectLoanHdr(query, ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}

	public String getCustCode(String aPlant, String aOrdno) throws Exception {
		String custCode = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("ORDNO", aOrdno);

		String query = " ISNULL( " + "custCode" + ",'') as " + "custCode" + " ";
		_loanHdrDAO.setmLogger(mLogger);
		Map m = _loanHdrDAO.selectRow(query, ht);

		custCode = (String) m.get("custCode");
		return custCode;
	}

	public String getJobNum(String aPlant, String aPONO) throws Exception {
		String custCode = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("ORDNO", aPONO);
		String query = " ISNULL( " + "jobNum" + ",'') as " + "jobNum" + " ";
		_loanHdrDAO.setmLogger(mLogger);
		Map m = _loanHdrDAO.selectRow(query, ht);

		custCode = (String) m.get("jobNum");
		return custCode;
	}
	

	public String getExpireDate(String aPlant, String aPONO) throws Exception {
		String expiredate = "";

		Hashtable ht = new Hashtable();
		ht.put("PLANT", aPlant);
		ht.put("ORDNO", aPONO);
		String query = " ISNULL(expiredate,'') as expiredate  ";
		_loanHdrDAO.setmLogger(mLogger);
		Map m = _loanHdrDAO.selectRow(query, ht);

		expiredate = (String) m.get("expiredate");
		return expiredate;
	}

	public boolean process_LoanOrderPicking(Map obj) throws Exception {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			this.mLogger.auditInfo(SConstant.PRINTFLAG, SConstant.LOANPICK
					+ " :: " + SConstant.ORDERNO + " : "
					+ (String) obj.get(IConstants.LOANDET_ORDNO) + " :: "
					+ SConstant.ORDERLN + " : "
					+ (String) obj.get(IConstants.LOANDET_ORDLNNO) + " :: "
					+ SConstant.ITEM + " : "
					+ (String) obj.get(IConstants.ITEM) + " :: "
					+ SConstant.BATCH + " : "
					+ (String) obj.get(IConstants.BATCH) + " :: "
					+ SConstant.QTY + " : "
					+ (String) obj.get(IConstants.QTY_ISSUE));

			flag = process_Wms_LoanOrderPicking(obj);
			this.mLogger.auditInfo(SConstant.PRINTFLAG, SConstant.LOANPICK
					+ " :: END : Transction returned : " + flag);
			if (flag == true) {
				DbBean.CommitTran(ut);
				flag = true;
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
			}
		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			throw e;
		}

		return flag;
	}

	public String process_LoanOrderPickingReversal(Map obj) {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();

			if (flag == true) {
				DbBean.CommitTran(ut);
				flag = true;
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
			}
		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
		}

		String xmlStr = "";
		if (flag == true) {
			xmlStr = XMLUtils.getXMLMessage(1, "Product : "
					+ obj.get(IConstants.ITEM) + "  issued successfully!");
		} else {
			xmlStr = XMLUtils.getXMLMessage(0, "Error in issuing Product : "
					+ obj.get("item") + " Order");
		}
		return xmlStr;
	}

	public boolean process_LoanOrderReceving (Map obj) throws Exception{
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();

			this.mLogger.auditInfo(SConstant.PRINTFLAG, SConstant.LOANRECV
					+ " :: " + SConstant.ORDERNO + " : "
					+ (String) obj.get(IConstants.LOANDET_ORDNO) + " :: "
					+ SConstant.ORDERLN + " : "
					+ (String) obj.get(IConstants.LOANDET_ORDLNNO) + " :: "
					+ SConstant.ITEM + " : "
					+ (String) obj.get(IConstants.ITEM) + " :: "
					+ SConstant.BATCH + " : "
					+ (String) obj.get(IConstants.BATCH) + " :: "
					+ SConstant.QTY + " : "
					+ (String) obj.get(IConstants.RECV_QTY));

			flag = process_Wms_LoanOrderReceiving(obj);
			this.mLogger.auditInfo(SConstant.PRINTFLAG, SConstant.LOANRECV
					+ " :: END : Transction returned : " + flag);
			if (flag == true) {
				DbBean.CommitTran(ut);
				flag = true;
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
			}
		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			throw e;
		}

		return flag;
	}

	public String process_LoanOrderReceivingReversal(Map obj) {
		boolean flag = false;
		UserTransaction ut = null;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();

			if (flag == true) {
				DbBean.CommitTran(ut);
				flag = true;
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
			}
		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
		}

		String xmlStr = "";
		if (flag == true) {
			xmlStr = XMLUtils.getXMLMessage(1, "Product : "
					+ obj.get(IConstants.ITEM) + "  issued successfully!");
		} else {
			xmlStr = XMLUtils.getXMLMessage(0,
					"Error in Receiving reversal Product : " + obj.get("item")
							+ " Order");
		}
		return xmlStr;
	}

	private boolean process_Wms_LoanOrderPicking(Map map) throws Exception {
		boolean flag = false;
		WmsTran tran = new WmsLoanOrderPicking();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);
		return flag;
	}

	private boolean process_Wms_LoanOrderReceiving(Map map) throws Exception {

		boolean flag = false;

		WmsTran tran = new WmsLoanOrderReceving();
		((IMLogger) tran).setMapDataToLogger(this.mLogger.getLoggerConstans());
		flag = tran.processWmsTran(map);
		return flag;
	}

	public ArrayList loadLoanOrderDetailstoPickorRecv(String plant,
			String aDONO, String type) throws Exception {
		String result = "";
		ArrayList al = null;

		try {
			_loanDetDAO.setmLogger(mLogger);
			al = _loanDetDAO.getLoanOrderItemDetails(plant, aDONO, type);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}

	public String getOpenLoanOrderSupDetailsPda(String plant, String toNum,
			Boolean isIssue) {

		String xmlStr = "";
		ArrayList<Map<String, String>> al = null;
		String query = " a.ORDNO,a.CustName,a.Remark1 ";
		String extCond = " AND a.ORDNO = '" + toNum + "' and a.status <> 'C'";
		Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put("PLANT", plant);
		try {
			_loanHdrDAO.setmLogger(mLogger);
			al = _loanHdrDAO.selectHdrForPda(query, ht, extCond, isIssue);
			MLogger.log(0, "Record size() :: " + al.size());
			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails");
				for (int i = 0; i < al.size(); i++) {

					Map<String, String> map = (Map<String, String>) al.get(i);
					xmlStr += XMLUtils.getXMLNode("status", "0");
					xmlStr += XMLUtils.getXMLNode("description", "");
					xmlStr += XMLUtils.getXMLNode("supplier", (String) strUtils.replaceCharacters2SendPDA(map
							.get("CustName").toString()));
					xmlStr += XMLUtils.getXMLNode("remarks", (String) strUtils.replaceCharacters2SendPDA(map
							.get("Remark1").toString()));

				}
				xmlStr += XMLUtils.getEndNode("ItemDetails");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}

	public String getOpenLoanOrderSupDetails(String plant, String toNum) {

		String xmlStr = "";
		ArrayList<Map<String, String>> al = null;
		String query = " ORDNO,CustName,Remark1 ";
		String extCond = " AND ORDNO = '" + toNum + "' and status <> 'C'";
		Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put("PLANT", plant);
		try {
			_loanHdrDAO.setmLogger(mLogger);
			al = _loanHdrDAO.selectHdr(query, ht, extCond);
			MLogger.log(0, "Record size() :: " + al.size());
			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails");
				for (int i = 0; i < al.size(); i++) {

					Map<String, String> map = (Map<String, String>) al.get(i);
					xmlStr += XMLUtils.getXMLNode("status", "0");
					xmlStr += XMLUtils.getXMLNode("description", "");
					xmlStr += XMLUtils.getXMLNode("supplier", (String) strUtils.replaceCharacters2SendPDA(map
							.get("CustName").toString()));
					xmlStr += XMLUtils.getXMLNode("remarks", (String) strUtils.replaceCharacters2SendPDA(map
							.get("Remark1").toString()));

				}
				xmlStr += XMLUtils.getEndNode("ItemDetails");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr;
	}

	public String getLoanOrderItemDetailsPda(String Plant, String orderNo,
			Boolean isReceived) {
		String xmlStr = "";
		ArrayList<Map<String, String>> al = null;
		String receivedsql = "isnull(QTYIS,0) AS QTYIS";
		if (isReceived) {
			receivedsql = "isnull(qtyrc,0) as QTYIS";
		}
		String query = " ORDNO, ORDLNNO ,ITEM,ITEMDESC,QTYOR, " + receivedsql
				+ " ,UNITMO ,ISNULL(USERFLD2,'') AS REMARKS  ";
		String extCond = " lnstat <> 'C'";
		if (!isReceived) {
			extCond = extCond + " AND pickstatus <> 'C' ";
		}
		
		extCond = extCond + " ORDER BY ITEM ";
		
		Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put("ORDNO", orderNo);
		ht.put("PLANT", Plant);
		try {
			_loanHdrDAO.setmLogger(mLogger);
			_loanDetDAO.setmLogger(mLogger);
			al = _loanDetDAO.selectLoanDet(query, ht, extCond);
			MLogger.log(0, "Record size() :: " + al.size());
			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map<String, String> map = (Map<String, String>) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("ORDNO", (String) map
							.get("ORDNO"));
					xmlStr += XMLUtils.getXMLNode("ORDLNNO", (String) map
							.get("ORDLNNO"));
					xmlStr += XMLUtils.getXMLNode("item", (String) map
							.get("ITEM"));
					xmlStr += XMLUtils.getXMLNode("itemDesc", (String) strUtils.replaceCharacters2SendPDA(map
							.get("ITEMDESC").toString()));
					xmlStr += XMLUtils.getXMLNode("qtyor", StrUtils.formatNum((String) map
							.get("QTYOR")));
					xmlStr += XMLUtils.getXMLNode("qtypk", StrUtils.formatNum((String) map
							.get("QTYIS")));
					xmlStr += XMLUtils.getXMLNode("uom", (String) map
							.get("UNITMO"));
					xmlStr += XMLUtils.getXMLNode("remarks", "");
					xmlStr += XMLUtils.getXMLNode("fromLoc", _loanHdrDAO
							.getLocation(Plant, orderNo, "LOC"));
					xmlStr += XMLUtils.getXMLNode("toLoc", _loanHdrDAO
							.getLocation(Plant, orderNo, "LOC1"));

					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return xmlStr;
	}

	public String getLoanOrderItemDetails(String Plant, String orderNo,
			Boolean isReceived) {
		String xmlStr = "";
		ArrayList<Map<String, String>> al = null;
		String receivedsql = "isnull(QTYIS,0) AS QTYIS";
		if (isReceived) {
			receivedsql = "isnull(qtyrc,0) as QTYIS";
		}
		String query = " ORDNO, ORDLNNO ,ITEM,ITEMDESC,QTYOR, " + receivedsql
				+ " ,UNITMO ,ISNULL(USERFLD2,'') AS REMARKS  ";
		String extCond = " lnstat <> 'C'";
		Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put("ORDNO", orderNo);
		ht.put("PLANT", Plant);
		try {
			_loanDetDAO.setmLogger(mLogger);
			al = _loanDetDAO.selectLoanDet(query, ht, extCond);
			MLogger.log(0, "Record size() :: " + al.size());
			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("itemDetails total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map<String, String> map = (Map<String, String>) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("ORDNO", (String) map
							.get("ORDNO"));
					xmlStr += XMLUtils.getXMLNode("ORDLNNO", (String) map
							.get("ORDLNNO"));
					xmlStr += XMLUtils.getXMLNode("item", (String) map
							.get("ITEM"));
					xmlStr += XMLUtils.getXMLNode("itemDesc", (String) map
							.get("ITEMDESC"));
					xmlStr += XMLUtils.getXMLNode("qtyor", (String) map
							.get("QTYOR"));
					xmlStr += XMLUtils.getXMLNode("qtypk", (String) map
							.get("QTYIS"));
					xmlStr += XMLUtils.getXMLNode("uom", (String) map
							.get("UNITMO"));
					xmlStr += XMLUtils.getXMLNode("remarks", "");
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("itemDetails");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return xmlStr;
	}

	public String getLoanOrders(String plant, Boolean isIssue) {

		String xmlStr = "";
		ArrayList<Map<String, String>> al = null;

		String query = " a.ORDNO ,a.CustName,a.Remark1 ";
		String extCond = " and isnull(a.STATUS,'') <> 'C'";
		Hashtable<String, String> ht = new Hashtable<String, String>();

		ht.put("PLANT", plant);
		try {
			_loanHdrDAO.setmLogger(mLogger);
			al = _loanHdrDAO.selectHdrForPda(query, ht, extCond, isIssue);

			if (al.size() > 0) {
				xmlStr += XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("orders total='"
						+ String.valueOf(al.size()) + "'");
				for (int i = 0; i < al.size(); i++) {
					Map<String, String> map = (Map<String, String>) al.get(i);
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("ORDNO", (String) map
							.get("ORDNO"));
					xmlStr += XMLUtils.getXMLNode("supplier", (String) strUtils.replaceCharacters2SendPDA(map
							.get("CustName").toString()));
					xmlStr += XMLUtils.getXMLNode("remarks", (String) strUtils.replaceCharacters2SendPDA(map
							.get("Remark1").toString()));
					xmlStr += XMLUtils.getEndNode("record");
				}
				xmlStr += XMLUtils.getEndNode("orders");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return xmlStr;
	}

	@SuppressWarnings("all")
	public String getBatchDetail(String plant, String itemNo, String orderNo,
			String ordlnNo) throws Exception {
		String result = "";
		ArrayList<Map<String, String>> alData = new ArrayList<Map<String, String>>();
		try {
			_loanDetDAO.setmLogger(mLogger);
			alData = this._loanDetDAO.getLoanOrderBatchListToRecv(plant,
					orderNo, ordlnNo, itemNo, "", "");
			if (alData.size() > 0) {
				result = XMLUtils.getXMLHeader();
				result += XMLUtils.getStartNode("BatchDetails total='"
						+ alData.size() + "'");

				for (Map<String, String> hashMap : alData) {
					result += XMLUtils.getStartNode("record");
					result += XMLUtils
							.getXMLNode("BATCH", hashMap.get("batch"));
					result += XMLUtils.getXMLNode("PICK_QTY", hashMap
							.get("pickQty"));
					result += XMLUtils.getXMLNode("RECEIVED_QTY", hashMap
							.get("recQty"));
					result += XMLUtils.getEndNode("record");
				}

				result += XMLUtils.getEndNode("BatchDetails");
			}

			return result;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}
        
    @SuppressWarnings("all")
    public String getBatchQtytoRecv(String plant, String itemNo, String orderNo,
                    String ordlnNo,String loc,String batch) throws Exception {
            String xmlStr = "";
            ArrayList<Map<String, String>> alData = new ArrayList<Map<String, String>>();
            try {
                    _loanDetDAO.setmLogger(mLogger);
                    alData = this._loanDetDAO.getNewLoanOrderBatchListToRecv(plant,
                                    orderNo, ordlnNo, itemNo, loc, batch);
                if (alData.size() > 0) {
                   
                    ItemMstDAO itemMstDAO = new ItemMstDAO();
                    itemMstDAO.setmLogger(mLogger);
                    for (Map<String, String> hashMap : alData) {

                            
                            String spickQty = (String) hashMap.get("pickQty");
                            String srecvQty = (String) hashMap.get("recQty");

                         double availQty = Double.parseDouble(spickQty)
                                            - Double.parseDouble(srecvQty);
                         availQty = StrUtils.RoundDB(availQty, IConstants.DECIMALPTS);
                         if(availQty>0){
                            xmlStr = XMLUtils.getXMLHeader();
                            xmlStr += XMLUtils.getStartNode("LineAvailQty");
                            xmlStr += XMLUtils.getXMLNode("status", "0");
                            xmlStr += XMLUtils.getXMLNode("description", "");
                            xmlStr += XMLUtils.getXMLNode("PICK_QTY", hashMap
                                            .get("pickQty"));
                            xmlStr += XMLUtils.getXMLNode("RECEIVED_QTY", hashMap
                                            .get("recQty"));
                            xmlStr += XMLUtils.getXMLNode("uom", itemMstDAO
                                            .getItemUOM(plant, itemNo));
                          
                            xmlStr += XMLUtils.getEndNode("LineAvailQty");
                        }else{
                            xmlStr = XMLUtils.getXMLMessage(1, "Batch Details Not found");
                        }
                       }
                        

                } else {
                        xmlStr = XMLUtils.getXMLMessage(1, "Not a valid batch ");
                }
                

                    return xmlStr;
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e;
            }
    }

	public Boolean removeRow(String plant, String dono, String userId) {
		UserTransaction ut = null;
		Boolean flag = Boolean.valueOf(false);
		try {
			MovHisDAO movHisDao = new MovHisDAO();
			movHisDao.setmLogger(mLogger);
			_loanHdrDAO.setmLogger(mLogger);
			_loanDetDAO.setmLogger(mLogger);

			ut = DbBean.getUserTranaction();
			ut.begin();

			Boolean removeHeader = this._loanHdrDAO.removeOrder(plant, dono);
			Boolean removeDetails = this._loanDetDAO.removeOrderDetails(plant,
					dono);
			Hashtable htMovHis = createMoveHisHashtable(plant, dono, userId);
			Boolean movementHiss = movHisDao.insertIntoMovHis(htMovHis);
			if (removeHeader & removeDetails & movementHiss) {
				DbBean.CommitTran(ut);
				flag = Boolean.valueOf(true);
			} else {
				DbBean.RollbackTran(ut);
				flag = Boolean.valueOf(false);
			}
		} catch (Exception e) {
			DbBean.RollbackTran(ut);
			flag = Boolean.valueOf(false);
		}
		return flag;
	}

	private Hashtable createMoveHisHashtable(String plant, String dono,
			String userId) {
		Hashtable<String, String> htMovHis = new Hashtable<String, String>();

		htMovHis.put(IDBConstants.PLANT, plant);
		htMovHis.put("DIRTYPE", "DELETE_RENTAL_AND_SERVICE_ORDER");
		htMovHis.put(IDBConstants.ITEM, " ");
		htMovHis.put("BATNO", " ");
		htMovHis.put("ORDNUM", dono);

		htMovHis.put("MOVTID", " ");
		htMovHis.put("RECID", " ");
		htMovHis.put(IDBConstants.LOC, " ");

		htMovHis.put(IDBConstants.CREATED_BY, userId);
		htMovHis.put(IDBConstants.TRAN_DATE, DateUtils
				.getDateinyyyy_mm_dd(DateUtils.getDate()));
		htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

		return htMovHis;
	}
        

        public boolean isOpenLoanOrder(String plant,String ordno) throws Exception {

                boolean flag = false;
                try {
                       
                    _loanHdrDAO.setmLogger(mLogger);

                     
                                Hashtable htCondiHdr = new Hashtable();
                                htCondiHdr.put("PLANT",plant);
                                htCondiHdr.put("ordno", ordno);
                                
                                flag = _loanHdrDAO.isExisit(htCondiHdr, " STATUS ='N'");
                            
                                if (!flag){
                                    throw new Exception(" Processed Loan Order Cannot be modified");
                                }
                       

                } catch (Exception e) {
                        this.mLogger.exception(this.printLog, "", e);
                        throw e;
                }

                return flag;
        }
        
    public ArrayList listLoanDetilstoPrint(String plant, String aOrderNo)
                    throws Exception {
            String q = "";
            ArrayList al = null;
            LoanDetDAO loanDao = new LoanDetDAO();
            loanDao.setmLogger(mLogger);
            try {
                  al = loanDao.getLoanDetailToPrint(plant,aOrderNo);

            } catch (Exception e) {
                    throw e;
            }

            return al;
    }
    
    public ArrayList listLoanDetilsWithPrice(String ITEM,String CUST_NAME,String JOBNO,String plant, String aOrderNo,String afrmDate,
			String atoDate)
            throws Exception {
    String q = "";
    ArrayList al = null;
    LoanDetDAO loanDao = new LoanDetDAO();
    loanDao.setmLogger(mLogger);
    try {
    	  al = loanDao.getLoanDetailWithPrice(ITEM,CUST_NAME,JOBNO,plant,aOrderNo,afrmDate,atoDate);

    } catch (Exception e) {
            throw e;
    }

    return al;
}
@SuppressWarnings("all")
  public String validateBatchToRecv(String plant, String orderNo,
                    String ordlnNo,String itemNo,String loc,String batch) throws Exception {
            String xmlStr = "";
        	LoanDetDAO loanMstDAO = new LoanDetDAO();
        	List resultList = loanMstDAO.getNewLoanOrderBatchListToRecv(plant,
        			orderNo, ordlnNo, itemNo, loc, batch);
            try {
                    _loanDetDAO.setmLogger(mLogger);
                 
                    if (resultList.size() > 0) {
        				Map m = (Map) resultList.get(0);
        				
        			
        				String sBatch = (String) m.get("batch");
        				String spickQty = (String) m.get("pickQty");
        				String srecvQty = (String) m.get("recQty");
                        
                            xmlStr = XMLUtils.getXMLHeader();
                            xmlStr += XMLUtils.getStartNode("BatchDetails");
                            xmlStr += XMLUtils.getXMLNode("status", "0");
                            xmlStr += XMLUtils.getXMLNode("description", "");
                            xmlStr += XMLUtils.getXMLNode("BATCH", sBatch);
                             xmlStr += XMLUtils.getEndNode("BatchDetails");
                        

                } else {
                        xmlStr = XMLUtils.getXMLMessage(1, "Not a valid batch ");
                }
                

                    return xmlStr;
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e;
            }
    }
    
  @SuppressWarnings("all")
	public String getLoanRecvBatchDetail(String plant, String itemNo, String orderNo,
			String ordlnNo) throws Exception {
		String result = "";
		ArrayList<Map<String, String>> alData = new ArrayList<Map<String, String>>();
		try {
			_loanDetDAO.setmLogger(mLogger);
			alData = this._loanDetDAO.getLoanOrderBatchListToRecv(plant,
					orderNo, ordlnNo, itemNo, "", "");
			if (alData.size() > 0) {
				result = XMLUtils.getXMLHeader();
				result += XMLUtils.getStartNode("BatchDetails total='"
						+ alData.size() + "'");

				for (Map<String, String> hashMap : alData) {
					 String sBatch   = (String)hashMap.get("batch");
				     String spickQty   = (String)hashMap.get("pickQty");
				     String srecvQty   = (String) hashMap.get("recQty");
				    
				     
				    double availQty = Double.parseDouble(spickQty) - Double.parseDouble(srecvQty);
				 	availQty = StrUtils.RoundDB(availQty,IConstants.DECIMALPTS);
				 	if(availQty>0){
						result += XMLUtils.getStartNode("record");
						result += XMLUtils
								.getXMLNode("BATCH", hashMap.get("batch"));
						result += XMLUtils.getXMLNode("PICK_QTY", hashMap
								.get("pickQty"));
						result += XMLUtils.getXMLNode("RECEIVED_QTY", hashMap
								.get("recQty"));
						result += XMLUtils.getEndNode("record");
				 	}
				}

				result += XMLUtils.getEndNode("BatchDetails");
			}

			return result;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}
    
  public boolean saveLoanMultiRemarks(Hashtable ht) throws Exception {
		boolean flag = false;
		
		try {
			LoanDetDAO _LoanDetDAO = new LoanDetDAO();
			_LoanDetDAO.setmLogger(mLogger);
			flag = _LoanDetDAO.insertLoanMultiRemarks(ht);
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return flag;
	}
	
	 public ArrayList listLoandetMultiRemarks(String plant, String aDONO,String aDOLNNO)
			throws Exception {
		String q = "";
		String link = "";
		String result = "";
		String where = "";
		ArrayList al = null;

		try {
			Hashtable htCond = new Hashtable();
			htCond.put("PLANT", plant);
			htCond.put(IDBConstants.LOANDET_ORDNO, aDONO);
			htCond.put(IDBConstants.LOANDET_ORDLNNO, aDOLNNO);
			q = "isnull(remarks,'') remarks";
			LoanDetDAO _LoanDetDAO = new LoanDetDAO();
			_LoanDetDAO.setmLogger(mLogger);
			al = _LoanDetDAO.selectLoanMultiRemarks(q,htCond," plant <> '' order by id_remarks");
		} catch (Exception e) {
			throw e;
		}
		return al;
	}
  
  public boolean isExitsDoLine(Hashtable htdodet)
          throws Exception {

  boolean flag = false;
  try {
	  LoanDetDAO _LoanDetDAO = new LoanDetDAO();
		_LoanDetDAO.setmLogger(mLogger);
         /* DoDetDAO _dodetDAO = new DoDetDAO();
          _dodetDAO.setmLogger(mLogger);*/
          /*flag = _dodetDAO.isExisit(ht);*/
		  flag = _LoanDetDAO.isExisit(htdodet,"");
  } catch (Exception e) {
          this.mLogger.exception(this.printLog, "", e);
          throw e;
  }

  return flag;
}
  
  public boolean isNewStatusDONO(String pono, String plant) throws Exception {
		boolean exists = false;
		 
		try {
			String extCon = " STATUS IN ('O','C') ";
			_loanDetDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.LOANHDR_ORDNO, pono);
			if (isExistOntable(ht, extCon))
				exists = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return exists;
	}
    
  private boolean isExistOntable(Hashtable ht, String extCon) throws Exception {
		boolean exists = false;
		try {
			_loanDetDAO.setmLogger(mLogger);
			if (_loanDetDAO.getCountDoNo(ht, extCon) > 0)
				exists = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return exists;
	}
  
  public boolean isExistLONO(String pono, String plant) throws Exception {
		boolean exists = false;
		try {
			_loanDetDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.LOANHDR_ORDNO, pono);
			if (isExistOntable(ht, ""))
				exists = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return exists;
	}
  
  public String getConvertedUnitCostForProduct(String aPlant, String doNO,String aItem) throws Exception {
      String ConvertedUnitCost="";
        try{
             ConvertedUnitCost= _loanHdrDAO.getUnitCostBasedOnCurIDSelectedForOrder(aPlant, doNO,aItem);
        }catch(Exception e){
            throw e;
        }
      return ConvertedUnitCost;
  }
  
	public String getConvertedUnitCostForProductWTC(String aPlant, String doNO,String aItem) throws Exception {
        String ConvertedUnitCost="";
          try{
               ConvertedUnitCost= _loanHdrDAO.getUnitCostBasedOnCurIDSelectedForOrderWTC(aPlant, doNO,aItem);
          }catch(Exception e){
              throw e;
          }
        return ConvertedUnitCost;
    }
	
	 public String getOBDiscountSelectedItem(String aPlant, String doNO,String aItem,String aType) throws Exception {
	        String ConvertedUnitCost="";
	          try{
	               ConvertedUnitCost= _loanHdrDAO.getOBDiscountSelectedItem(aPlant, doNO,aItem,aType);
	          }catch(Exception e){
	              throw e;
	          }
	        return ConvertedUnitCost;
	    }	
  
    public Map getLOReceiptHdrDetails(String aplant) throws Exception {
        Map m =  new HashMap();
     
        m=_loanHdrDAO.getLOReciptHeaderDetails(aplant);
         return m;
 }

}