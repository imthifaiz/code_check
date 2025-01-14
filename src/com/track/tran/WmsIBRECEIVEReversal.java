package com.track.tran;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.BomDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.BomDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.RecvDetDAO; 
import com.track.db.util.POUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import java.util.ArrayList;

public class WmsIBRECEIVEReversal implements WmsTran, IMLogger{
	private boolean printLog = MLoggerConstant.WmsIBRECEIVEReversal_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsIBRECEIVEReversal_PRINTPLANTMASTERINFO;
	DateUtils dateUtils = null;
	StrUtils su = new StrUtils();
	POUtil poutil = new POUtil();
	private MLogger mLogger = new MLogger();
	ArrayList ALRecvddetails = new ArrayList();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		return null;
	}

	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		this.mLogger.setLoggerConstans(dataForLogging);
	}
	
	public WmsIBRECEIVEReversal() {
		dateUtils = new DateUtils();
		
	}
	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		
		try {
			
			flag = processPodetForReversal(m);
			if (flag) {
				Map mPrddet = new ItemMstDAO().getProductNonStockDetails((String)m.get(IConstants.PLANT),(String)m.get(IConstants.ITEM));
		        String nonstocktype= StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
		        if(!nonstocktype.equals("Y"))	
			    {
		        	flag = processInvRemove(m);
			    }else{
			    	flag=true;
			    }
			}
			
			if (flag) {

				{
					flag = processMovHis_OUT(m);

					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						this.mLogger.exception(this.printLog, "", e);
					}
				}
				
			}
					
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}
	
	public boolean processPodetForReversal(Map map) throws Exception {

		boolean flag = false;
		try {
			PoDetDAO _PoDetDAO= new PoDetDAO();
			PoHdrDAO _PoHdrDAO = new PoHdrDAO();
			RecvDetDAO _RecvDetDAO = new RecvDetDAO();
			_PoDetDAO.setmLogger(mLogger);
			_PoHdrDAO.setmLogger(mLogger);
			_RecvDetDAO.setmLogger(mLogger);
			
			Map mPrddet = new ItemMstDAO().getProductNonStockDetails((String)map.get(IConstants.PLANT),(String)map.get(IConstants.ITEM));
	        String nonstocktype= StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
			
			Hashtable htCondiRecvDet = new Hashtable();
			if(map.get(IConstants.BATCH).equals("NOBATCH")){
				StringBuffer query = new StringBuffer("");
				htCondiRecvDet.put("PLANT", map.get("PLANT"));
				htCondiRecvDet.put("pono", map.get("PONO"));
				htCondiRecvDet.put("lnno", map.get("POLNNO"));
				htCondiRecvDet.put("item", map.get("ITEM"));
				htCondiRecvDet.put("batch", map.get("BATCH"));
				htCondiRecvDet.put("LOC", map.get("LOC"));
			}
			else
			{
				StringBuffer query = new StringBuffer("");
				htCondiRecvDet.put("PLANT", map.get("PLANT"));
				htCondiRecvDet.put("pono", map.get("PONO"));
				htCondiRecvDet.put("lnno", map.get("POLNNO"));
				htCondiRecvDet.put("item", map.get("ITEM"));
				htCondiRecvDet.put("batch", map.get("BATCH"));
				htCondiRecvDet.put("LOC", map.get("LOC"));
				//htCondiRecvDet.put("EXPIRYDAT", map.get(IConstants.EXPIREDATE));
			}
			
			double reverseqty = Double.parseDouble((String) map.get("QTY"));
			String qty = "";
			if(!nonstocktype.equals("Y"))
					ALRecvddetails = _RecvDetDAO.getRcvdDetailsforreverse(map.get("PLANT").toString(), htCondiRecvDet);
			else
					ALRecvddetails = _RecvDetDAO.getRcvdDetailsforreverse_nonstock(map.get("PLANT").toString(), htCondiRecvDet);
					if (ALRecvddetails.size() > 0) {
						for (int i = 0; i < ALRecvddetails.size(); i++) {
							Map m = (Map) ALRecvddetails.get(i);
							
							if(reverseqty == 0) break;
							
							  String pono = (String)m.get("pono");
							  double recqty = Double.parseDouble((String)m.get("RECQTY"));
					          String crat = (String)m.get("CRAT");
					          String ID = (String)m.get("ID").toString();
					          /*if(reverseqty >= recqty)
					          {
					        	  reverseqty = (reverseqty-recqty);
					        	  qty = "0";
					        	  
					          }
					          else
					          {
					        	  qty = Double.toString(recqty - reverseqty);
					        	  reverseqty = 0;
					        	  
					          }*/
					          
					          String updaterecdet = "";
					          
					          //updaterecdet = "set recqty = " +qty +",UPAT='" +dateUtils.getDateTime()+"',UPBY='"+map.get(IConstants.LOGIN_USER)+"'";
					          if(reverseqty >= recqty)
					        	  updaterecdet = "set recqty = recqty-" +recqty +",UPAT='" +dateUtils.getDateTime()+"',UPBY='"+map.get(IConstants.LOGIN_USER)+"'";
					          else
					          {
					        	  updaterecdet = "set recqty = recqty-" +reverseqty +",UPAT='" +dateUtils.getDateTime()+"',UPBY='"+map.get(IConstants.LOGIN_USER)+"'";
					        	  recqty=reverseqty;
					          }
					        	  
					          htCondiRecvDet.put("CRAT", crat);
					          if(!ID.equalsIgnoreCase("0"))
					        	  htCondiRecvDet.put("ID", ID);
					          if(reverseqty>0)
					          {
					        	  boolean isexists = _RecvDetDAO.isExisit(htCondiRecvDet, map.get("PLANT").toString());
									 if(isexists)
									 {
								          flag = _RecvDetDAO.update(updaterecdet, htCondiRecvDet, "", map.get("PLANT").toString());
								          reverseqty=reverseqty-recqty;
									 }
					          }							
						}
					
				
					 htCondiRecvDet.remove("CRAT");
					 htCondiRecvDet.remove("ID");
					}
					 htCondiRecvDet.put("RECQTY", "0.000");
					 
					 boolean isexists = _RecvDetDAO.isExisit(htCondiRecvDet, map.get("PLANT").toString());
					 if(isexists)
					 {
						 flag = _RecvDetDAO.deleteRecvDet(htCondiRecvDet);
					 }
					
			if (!flag) {
				flag = false;
				throw new Exception("Product Reversed  Failed, Error In update  PODET :"+ " " + map.get("ITEM"));
				} else {
				flag = true;
		
			}
			if (flag) {
				//---Added by Bruhan on May 8 2014, Description:To check order equal to receive quantity
				Hashtable htCondiPoDet = new Hashtable();
				StringBuffer query1 = new StringBuffer("");
				// getQty from podet
				htCondiPoDet.put("PLANT", map.get("PLANT"));
				htCondiPoDet.put("pono", map.get("PONO"));
				htCondiPoDet.put("polnno", map.get("POLNNO"));

				query1.append("isnull(QtyOr,0) as QtyOr");
				query1.append(",isnull(qtyRc,0) as qtyRc");

				Map mQty = _PoDetDAO.selectRow(query1.toString(), htCondiPoDet);

				double ordQty = Double.parseDouble((String) mQty.get("QtyOr"));
				double qtyRC = Double.parseDouble((String) mQty.get("qtyRc"));
				
				double tranQty = Double.parseDouble((String) map.get(IConstants.QTY));
				//double sumqty =qtyRC + tranQty;
				
				
				String updatePoDet = "";
				if (qtyRC == tranQty) {
					updatePoDet = "set qtyrc= isNull(qtyrc,0) - " + map.get(IConstants.QTY)	+",lnstat='N' ";
				}
				else
				{
					updatePoDet = "set qtyrc= isNull(qtyrc,0) - " + map.get(IConstants.QTY)	+",lnstat='O' ";
				}
				//---End Added by Bruhan on May 8 2014, Description:To check order equal to receive quantity
				
				Hashtable htPoDet = new Hashtable();
				htPoDet.put("PLANT", map.get("PLANT"));
				htPoDet.put("pono", map.get("PONO"));
				htPoDet.put("polnno", map.get("POLNNO"));
				htPoDet.put("item", map.get("ITEM"));
				flag = _PoDetDAO.update(updatePoDet, htPoDet, "");
				if (!flag) {
					flag = false;
					throw new Exception("Product Reversed  Failed, Error In update  DODET :"+ " " + map.get("ITEM"));
				}						
			
			}	
			String updateDoHdr = "";
			Hashtable htConditoHdr = new Hashtable();
			htConditoHdr.put("PLANT", map.get("PLANT"));
			htConditoHdr.put("pono", map.get("PONO"));
			flag = _PoDetDAO.isExisit(htConditoHdr," isnull(LNSTAT,'') in ('O','C')");
			if (flag)
				updateDoHdr = "set  STATUS='O'";
			else
				updateDoHdr = "set STATUS='N'";
			flag = _PoHdrDAO.updatePO(updateDoHdr, htConditoHdr, "");

			if (!flag) {
				flag = false;
				throw new Exception("Product Reversed  Failed, Error In update  DONO :"	+ " " + map.get("PONO"));
			} else {
				flag = true;
			}
				
		} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
		
		}
		return flag;
	}

	private boolean processInvRemove(Map map) throws Exception {
		boolean flag = false;
		try {
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
			Hashtable htInvMst = new Hashtable();
			htInvMst.clear();
			if(map.get(IConstants.BATCH).equals("NOBATCH")){
				htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
			}
			else
			{
				htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
				//htInvMst.put(IDBConstants.EXPIREDATE, map.get(IConstants.EXPIREDATE));
			}
			double reverseqty = Double.parseDouble((String) map.get("QTY"));
			
			if (ALRecvddetails.size() > 0) {
				for (int i = 0; i < ALRecvddetails.size(); i++) {
					Map m = (Map) ALRecvddetails.get(i);
					{
						if(reverseqty==0) break;
						String ID = (String)m.get("ID").toString();
						String RQTY = (String)m.get("RECQTY").toString();
						double issueqty = Double.parseDouble(RQTY);
						htInvMst.put(IDBConstants.INVID, ID);
			String cond ="";
			 if(reverseqty >= issueqty)
				 {
				 double inqty = Double.valueOf(RQTY) * Double.valueOf((String)map.get("UOMQTY"));
				 cond =" QTY >="+ inqty;
				 }
			 else
			 {
				 double inqty = reverseqty * Double.valueOf((String)map.get("UOMQTY"));
				 cond =" QTY >="+ inqty;
			 }
			flag = _InvMstDAO.isExisit(htInvMst,cond );
			if (flag) {
				StringBuffer sql1 = new StringBuffer(" SET ");
				//sql1.append(IDBConstants.QTY + " = QTY -'" + map.get(IConstants.QTY) + "'");
				if(reverseqty >= issueqty)
				{
					double inqty = issueqty * Double.valueOf((String)map.get("UOMQTY"));
					sql1.append(IDBConstants.QTY + " = QTY -'" + inqty + "'");
				}
				else
				{
					double inqty = reverseqty * Double.valueOf((String)map.get("UOMQTY"));
					sql1.append(IDBConstants.QTY + " = QTY -'" + inqty + "'");
					issueqty=reverseqty;
				}
				if(reverseqty>0)
				{
				flag = _InvMstDAO.update(sql1.toString(), htInvMst, "");
				reverseqty = (reverseqty-issueqty);
				}
			} else {
				throw new Exception("Product Reversed  Failed, Error In update InvMst :"	+ " " + map.get(IConstants.ITEM));
			}
					}
					
				}
				ALRecvddetails = new ArrayList();
			}
		} catch (Exception e) {

		this.mLogger.exception(this.printLog, "", e);
		throw e;

	}
	return flag;
  }
	

	/* * ************Modification History*********************************
		Sep 25 2014 Bruhan, Description: To Change TRAN_DATE as Transaction date
	*/
	public boolean processMovHis_OUT(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		CustMstDAO _CustMstDAO = new CustMstDAO();
		movHisDao.setmLogger(mLogger);
		_CustMstDAO.setmLogger(mLogger);
		try {
			Hashtable htRecvHis = new Hashtable();
			htRecvHis.clear();
			htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvHis.put("DIRTYPE", "IB_REVERSE");
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvHis.put("BATNO", map.get(IConstants.BATCH));
			htRecvHis.put(IDBConstants.QTY, map.get(IConstants.QTY));
			htRecvHis.put("MOVTID", "OUT");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.PODET_PONUM));
			htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htRecvHis.put(IDBConstants.TRAN_DATE, map.get(IConstants.TRAN_DATE));
			htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htRecvHis.put("REMARKS", map.get(IConstants.REMARKS)+","+map.get(IConstants.RSNDESC));
			flag = movHisDao.insertIntoMovHis(htRecvHis);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
			

		}
		return flag;
	}


}
