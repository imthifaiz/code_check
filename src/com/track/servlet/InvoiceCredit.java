package com.track.servlet;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jrefinery.chart.DateUnit;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.CustomerCreditnoteDAO;
import com.track.dao.InvoiceDAO;
import com.track.dao.InvoicePaymentDAO;
import com.track.dao.MovHisDAO;
import com.track.db.object.InvPaymentDetail;
import com.track.db.object.InvPaymentHeader;
import com.track.db.util.ExceptionUtil;
import com.track.db.util.InvoicePaymentUtil;
import com.track.db.util.InvoiceUtil;
import com.track.db.util.ExceptionUtil.Result;
import com.track.util.DateUtils;
import com.track.util.StrUtils;

/**
 * Servlet implementation class InvoiceCredit
 */
@WebServlet("/InvoiceCredit")
public class InvoiceCredit extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InvoiceCredit() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = StrUtils.fString(request.getParameter("cmd")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		DateUtils dateutils = new DateUtils(); 
		InvoicePaymentDAO InvPaymentdao = new InvoicePaymentDAO();
		MovHisDAO movHisDao = new MovHisDAO();
		CustomerCreditnoteDAO customerCreditnoteDAO= new CustomerCreditnoteDAO();
		ExceptionUtil exceptionUtil=null;
		
		if(action.equalsIgnoreCase("Apply_Credit")) {
			System.out.println(StrUtils.fString(request.getParameter("ordno")).trim());
			InvPaymentDetail invoicePaymentDetail=new InvPaymentDetail();
			InvoicePaymentUtil invPaymentUtil=new InvoicePaymentUtil();
			InvoiceDAO invoicedao = new InvoiceDAO();
			invoicePaymentDetail.setBALANCE(0.0);
			invoicePaymentDetail.setPLANT(plant);
			invoicePaymentDetail.setINVOICEHDRID(Integer.parseInt(request.getParameter("inid")));
			invoicePaymentDetail.setTYPE("REGULAR"); 
            invoicePaymentDetail.setDONO(request.getParameter("ordno")); 
			invoicePaymentDetail.setRECEIVEHDRID(Integer.parseInt(request.getParameter("rid")));
			invoicePaymentDetail.setAMOUNT(Double.valueOf(request.getParameter("camot")));
			invoicePaymentDetail.setCREDITAPPLYKEY(request.getParameter("uid"));
			invoicePaymentDetail.setCURRENCYUSEQT(Double.parseDouble(request.getParameter("CURRENCYUSEQT")));
			try {
				invoicePaymentDetail.setLNNO(InvPaymentdao.getmaxlineno(Integer.parseInt(request.getParameter("rid")), plant) + 1);
			}catch (Exception e) {
				System.out.println(e);
				invoicePaymentDetail.setLNNO(1);
			}
			
			invoicePaymentDetail.setCRBY(username);
			invoicePaymentDetail.setCRAT(dateutils.getDateTime());
			invoicePaymentDetail.setTOTAL_PAYING(Double.parseDouble("0"));
			System.out.println(invoicePaymentDetail);
			exceptionUtil=invPaymentUtil.addInvoicePaymentDet(invoicePaymentDetail, plant, username);
			
			if(exceptionUtil.getStatus()==Result.OK)
			{
				try {
					InvPaymentDetail invPaymentDetail = InvPaymentdao.getInvoicePaymentDetailsbyid(Integer.parseInt(request.getParameter("pid")), plant);
					InvPaymentHeader invPaymentHeader = InvPaymentdao.getInvoicePaymentById(invPaymentDetail.getRECEIVEHDRID(), plant, "");
					Hashtable ht = new Hashtable();
					ht.put("ID", String.valueOf(invPaymentDetail.getCREDITNOTEHDRID()));
					ht.put("PLANT", plant);
					
					System.out.println(invPaymentDetail.toString());
					System.out.println(Double.valueOf(request.getParameter("camot")));
					System.out.println(invPaymentDetail.getBALANCE());
					
					Double bal = invPaymentDetail.getBALANCE() - Double.valueOf(request.getParameter("camot"));
					
					if(bal <= 0) {
						invPaymentDetail.setBALANCE(bal);
						try {
							if(invPaymentDetail.getCREDITNOTEHDRID() > 0) {
								/*List CustcrdnoteHdrList =  customerCreditnoteDAO.getCustCreditnoteHdrByIdforupdate(ht);
								Hashtable<String, String> CustcrdnoteHdr = new Hashtable<String, String>();
								 CustcrdnoteHdr.putAll((Map)CustcrdnoteHdrList.get(0));
								CustcrdnoteHdr.put("CREDIT_STATUS", "Applied");
								customerCreditnoteDAO.updateCreditNoteHdr(CustcrdnoteHdr);*/
								String crquery = " SET CREDIT_STATUS='Applied'";	
								customerCreditnoteDAO.update(crquery, ht, "");
							}
						}catch (Exception e) {
							System.out.println(e);
						}
					}else {
						invPaymentDetail.setBALANCE(bal);
						try {
							if(invPaymentDetail.getCREDITNOTEHDRID() > 0) {
								
								/*List CustcrdnoteHdrList =  customerCreditnoteDAO.getCustCreditnoteHdrByIdforupdate(ht);
								Hashtable<String, String> CustcrdnoteHdr = new Hashtable<String, String>();
								 CustcrdnoteHdr.putAll((Map)CustcrdnoteHdrList.get(0));
								CustcrdnoteHdr.put("CREDIT_STATUS", "Partially Applied");
								customerCreditnoteDAO.updateCreditNoteHdr(CustcrdnoteHdr);*/
								String crquery = " SET CREDIT_STATUS='Partially Applied'";	
								customerCreditnoteDAO.update(crquery, ht, "");
							}
						}catch (Exception e) {
							System.out.println(e);
						}
					}
				
					
					System.out.println(invPaymentDetail.getBALANCE());
					invPaymentUtil.updateInvoicePaymentDet(invPaymentDetail, plant, username);
					invPaymentHeader.setAMOUNTUFP(bal);
					invPaymentUtil.updateInvoicePaymentHdr(invPaymentHeader, plant, username);
					
					String invoiceid = request.getParameter("inid");
					double paymentmade=InvPaymentdao.getbalacedue(plant, invoiceid);
					
					
					InvoiceUtil invoiceUtil = new InvoiceUtil(); 
					
					 Hashtable ht1 = new Hashtable(); 
					  ht1.put("ID", invoiceid); ht1.put("PLANT", plant); 
				
					  List invoiceHdrList = invoiceUtil.getInvoiceHdrById(ht1); 
					  Map invoiceHdrmap=(Map)invoiceHdrList.get(0);
					  
					/*
					 * Hashtable invoiceHdr =new Hashtable(); invoiceHdr.put("PLANT", plant);
					 * invoiceHdr.put("CUSTNO", invoiceHdrmap.get("CUSTNO"));
					 * invoiceHdr.put("INVOICE", invoiceHdrmap.get("INVOICE"));
					 * invoiceHdr.put("DONO", invoiceHdrmap.get("DONO"));
					 * invoiceHdr.put("INVOICE_DATE", invoiceHdrmap.get("INVOICE_DATE"));
					 * invoiceHdr.put("DUE_DATE", invoiceHdrmap.get("DUE_DATE"));
					 * invoiceHdr.put("PAYMENT_TERMS", invoiceHdrmap.get("PAYMENT_TERMS"));
					 * invoiceHdr.put("EMPNO", invoiceHdrmap.get("EMPNO"));
					 * invoiceHdr.put("ITEM_RATES", invoiceHdrmap.get("ITEM_RATES"));
					 * invoiceHdr.put("DISCOUNT", invoiceHdrmap.get("DISCOUNT"));
					 * invoiceHdr.put("DISCOUNT_TYPE", invoiceHdrmap.get("DISCOUNT_TYPE"));
					 * invoiceHdr.put("DISCOUNT_ACCOUNT", invoiceHdrmap.get("DISCOUNT_ACCOUNT"));
					 * invoiceHdr.put("SHIPPINGCOST", invoiceHdrmap.get("SHIPPINGCOST"));
					 * invoiceHdr.put("ADJUSTMENT", invoiceHdrmap.get("ADJUSTMENT"));
					 * invoiceHdr.put("SUB_TOTAL", invoiceHdrmap.get("SUB_TOTAL"));
					 * invoiceHdr.put("TOTAL_AMOUNT", invoiceHdrmap.get("TOTAL_AMOUNT"));
					 */
					  double tamount = Double.parseDouble((String)invoiceHdrmap.get("TOTAL_AMOUNT")); 
					  double tdiff = tamount - paymentmade;
					  
					  Hashtable htgi = new Hashtable();	
					  String sqlgi="";
					  if(tdiff > 0) {
						   sqlgi = "UPDATE "+ plant+"_FININVOICEHDR SET BILL_STATUS='Partially Paid',UPAT='"+dateutils.getDateTime()+"',UPBY='"+username+"' WHERE PLANT='"+ plant+"' AND ID='"+invoiceid+"'";
					  }else {
						   sqlgi = "UPDATE "+ plant+"_FININVOICEHDR SET BILL_STATUS='Paid',UPAT='"+dateutils.getDateTime()+"',UPBY='"+username+"' WHERE PLANT='"+ plant+"' AND ID='"+invoiceid+"'";
					  }
					  invoicedao.updategino(sqlgi, htgi, "");
					/*
					 * invoiceHdr.put("NOTE",invoiceHdrmap.get("NOTE"));
					 * invoiceHdr.put("TERMSCONDITIONS", invoiceHdrmap.get("TERMSCONDITIONS"));
					 * invoiceHdr.put("CRAT",invoiceHdrmap.get("CRAT"));
					 * invoiceHdr.put("CRBY",invoiceHdrmap.get("CRBY"));
					 * invoiceHdr.put("UPAT",dateutils.getDateTime());
					 * invoiceHdr.put("ID",invoiceid); invoiceHdr.put("UPBY",username);
					 * 
					 * invoiceUtil.updateInvoiceHdr(invoiceHdr);
					 */
					  
					  String billno = (String)invoiceHdrmap.get("INVOICE");
					  String pordno = (String)invoiceHdrmap.get("DONO");
					  if(!pordno.equalsIgnoreCase("")) {
							pordno = pordno+",";
						}
						Hashtable htMovHis = new Hashtable();
						htMovHis.clear();
					    htMovHis.put(IDBConstants.PLANT, plant);
					    htMovHis.put("DIRTYPE", TransactionConstants.APPLY_CREDIT_PAYMENT_RECEIVED);
						htMovHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));																		
						htMovHis.put("RECID", "");
					    htMovHis.put(IDBConstants.MOVHIS_ORDNUM, String.valueOf(invPaymentDetail.getRECEIVEHDRID()));
						htMovHis.put(IDBConstants.CREATED_BY, username);		
						htMovHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
						htMovHis.put("REMARKS",pordno+billno+","+request.getParameter("camot"));
						movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			
			
			//response.sendRedirect("jsp/invoiceDetail.jsp?dono="+request.getParameter("ordno")+"&INVOICE_HDR="+request.getParameter("inid"));
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
