package com.track.tran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.BomDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.BomDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.ShipHisDAO;
import com.track.db.util.DOUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

/* Change History
 * 18/06/2014 - included insert into inventory if the inventory record doesn't exists in processInvAdd()- By Samatha
 * 
 */
public class WmsOBISSUEReversal implements WmsTran, IMLogger{
	private boolean printLog = MLoggerConstant.WmsOBISSUEReversal_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsOBISSUEReversal_PRINTPLANTMASTERINFO;
	DateUtils dateUtils = null;
	StrUtils su = new StrUtils();
	DOUtil doutil = new DOUtil();
	private MLogger mLogger = new MLogger();
	ArrayList ALIssueddetails = new ArrayList();

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
	
	public WmsOBISSUEReversal() {
		dateUtils = new DateUtils();
		
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		
		try {
			
			flag = processDodetForReversal(m);
			
			
			if (flag) {
				Map mPrddet = new ItemMstDAO().getProductNonStockDetails((String)m.get(IConstants.PLANT),(String)m.get(IConstants.ITEM));
		        String nonstocktype= StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
		        if(!nonstocktype.equals("Y"))	
			    {
				flag = processInvAdd(m);
			    }else{
			    	flag=true;
			    }
			}
		
			if (flag) {

				{

					flag = processMovHis_IN(m);

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
	
	public boolean processDodetForReversal(Map map) throws Exception {

		boolean flag = false;
		try {
			// update receive qty in podet
			DoDetDAO _DoDetDAO = new DoDetDAO();
			DoHdrDAO _DoHdrDAO = new DoHdrDAO();
			ShipHisDAO _ShipHisDAO = new ShipHisDAO();
			_DoDetDAO.setmLogger(mLogger);
			_DoHdrDAO.setmLogger(mLogger);
			_ShipHisDAO.setmLogger(mLogger);
			
			
			Hashtable htCondiShipHis = new Hashtable();
			StringBuffer query = new StringBuffer("");
			
			htCondiShipHis.put("PLANT", map.get("PLANT"));
			htCondiShipHis.put("dono", map.get("DONO"));
			if((String) map.get("ISPDA") != "GIWP")
			htCondiShipHis.put("dolno", map.get("DOLNNO"));
			else
				htCondiShipHis.put("UNITPRICE", map.get("UNITPRICE"));
			htCondiShipHis.put("item", map.get("ITEM"));
			htCondiShipHis.put("batch", map.get("BATCH"));
			htCondiShipHis.put("LOC1", map.get("LOC"));
			htCondiShipHis.put("LOC", map.get("LOC"));
			htCondiShipHis.put("STATUS","C");
			String qty = String.valueOf(map.get("QTY"));
			double reverseqty = Double.parseDouble(qty);
			
			ALIssueddetails = new ArrayList();
			ALIssueddetails = _ShipHisDAO.getIssuedDetailsforreverse(map.get("PLANT").toString(), htCondiShipHis);
			if (ALIssueddetails.size() > 0) {
				for (int i = 0; i < ALIssueddetails.size(); i++) {
					Map m = (Map) ALIssueddetails.get(i);
					
					if(reverseqty == 0) break;
					
					  String dono = (String)m.get("dono");
					  double issueqty = Double.parseDouble((String)m.get("PICKQTY"));
			          String crat = (String)m.get("CRAT");
			          String ID = (String)m.get("ID").toString();
			          
			          /*if(reverseqty >= issueqty)
			          {
			        	  reverseqty = (reverseqty-issueqty);
			        	  qty = "0";
			        	  
			          }
			          else
			          {
			        	  qty = Double.toString(issueqty - reverseqty);
			        	  reverseqty = 0;
			        	  
			          }*/
			          
			          String updateissuedet = "";
			          if(reverseqty >= issueqty)
			          updateissuedet = "set pickqty =pickqty -" + issueqty +",UPAT='" +dateUtils.getDateTime()+"',UPBY='"+map.get(IConstants.LOGIN_USER)+"'";
			          else
			          {
			        	  updateissuedet = "set pickqty =pickqty -" + reverseqty +",UPAT='" +dateUtils.getDateTime()+"',UPBY='"+map.get(IConstants.LOGIN_USER)+"'";
			        	  issueqty=reverseqty;
			          }
			          htCondiShipHis.put("CRAT", crat);
			          if(!ID.equalsIgnoreCase("0"))
			        	  htCondiShipHis.put("ID", ID);
			          if(reverseqty>0)
			          {
			          boolean isexists = _ShipHisDAO.isExisit(htCondiShipHis, "");
						 if(isexists)
						 {        			
							 
					        	  flag = _ShipHisDAO.update(updateissuedet, htCondiShipHis, "");
					        	  reverseqty=reverseqty-issueqty;
					     }
					 }
				}
				htCondiShipHis.remove("CRAT");
				htCondiShipHis.remove("ID");
			}
		
			
			htCondiShipHis.put("PICKQTY", "0.000");
			boolean isexists = _ShipHisDAO.isExisit(htCondiShipHis, "");
			 if(isexists)
			 {
				 flag = _ShipHisDAO.deleteSHIPHIS(htCondiShipHis);
			 } 
			
			if (!flag) {
				flag = false;
				throw new Exception(
						"Product Reversed  Failed, Error In update Ships :"
								+ " " + map.get("ITEM"));
			} else {
				flag = true;
				
				

			}
			
			if((String) map.get("ISPDA") != "GIWP")
			{
			if (flag) {
				
			String updateDoDet = "";

			updateDoDet = "set qtypick= isNull(qtypick,0) - " + map.get(IConstants.QTY)
			+" , qtyis= isNull(qtyis,0) - " + map.get(IConstants.QTY)	
			+ " , pickstatus='O',lnstat='O' ";
			
			Hashtable htDoDet = new Hashtable();
			htDoDet.put("PLANT", map.get("PLANT"));
			htDoDet.put("dono", map.get("DONO"));
			if((String) map.get("ISPDA") != "GIWP")
			htDoDet.put("dolnno", map.get("DOLNNO"));
			htDoDet.put("item", map.get("ITEM"));
		
			flag = _DoDetDAO.update(updateDoDet, htDoDet, "");
			
			if (!flag) {
				flag = false;
				throw new Exception(
						"Product Reversed  Failed, Error In update  DODET :"
								+ " " + map.get("ITEM"));
			} else {
				flag = true;
				 boolean isExists = _DoDetDAO.isExisit(htDoDet,"qtypick =CAST('0' AS DECIMAL(18,3)) ");
                 if(isExists){
                	 updateDoDet = "";

                	 updateDoDet = "set pickstatus='N',lnstat='N'  ";
                     flag = _DoDetDAO.update(updateDoDet, htDoDet, "");   
                 }

			}
						
			
			}	
			
			
			String updateDoHdr = "";
			Hashtable htConditoHdr = new Hashtable();
			htConditoHdr.put("PLANT", map.get("PLANT"));
			htConditoHdr.put("dono", map.get("DONO"));
			
			flag = _DoDetDAO.isExisit(htConditoHdr," isnull(pickstatus,'') in ('O','C') and isnull(LNSTAT,'') in ('O','C')");

			if (flag)
				updateDoHdr = "set  STATUS='O',PickStaus='O' ";
			else
				updateDoHdr = "set STATUS='N',PickStaus='N' ";

			flag = _DoHdrDAO.update(updateDoHdr, htConditoHdr, "");

			if (!flag) {
				flag = false;
				throw new Exception(
						"Product Reversed  Failed, Error In update  DONO :"
								+ " " + map.get("DONO"));
			} else {
				flag = true;
			}
			}
					
		} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;

	}

	return flag;
}
	
	
	
private boolean processInvAdd(Map map) throws Exception {
		boolean flag = false;
		try {
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
			Hashtable htInvMst = new Hashtable();

			htInvMst.clear();
			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
			String qty = String.valueOf(map.get("QTY"));
			double reverseqty = Double.parseDouble(qty);
			
			if (ALIssueddetails.size() > 0) {
				for (int i = 0; i < ALIssueddetails.size(); i++) {
					Map m = (Map) ALIssueddetails.get(i);
					{
						if(reverseqty==0) break;
						String ID = (String)m.get("ID").toString();
						String PQTY = (String)m.get("PICKQTY").toString();
						double issueqty = Double.parseDouble(PQTY);
						htInvMst.put(IDBConstants.INVID, ID);
						
				        	  
				        	
			StringBuffer sql1 = new StringBuffer(" SET ");
			//sql1.append(IDBConstants.QTY + " = QTY +'" + map.get(IConstants.QTY) + "'");
			 if(reverseqty >= issueqty)
			 {
				 double inqty = Double.valueOf(PQTY) * Double.valueOf((String)map.get("UOMQTY"));
				 sql1.append(IDBConstants.QTY + " = QTY +'" + inqty + "'");
			 }
			 else
			 {
				 double inqty = Double.valueOf(PQTY) * Double.valueOf((String)map.get("UOMQTY"));
				 sql1.append(IDBConstants.QTY + " = QTY +'" + inqty + "'");
				 issueqty=reverseqty;
			 }
			 
			sql1.append("," + IDBConstants.UPDATED_AT + " = '"
					+ dateUtils.getDateTime() + "'");

			flag = _InvMstDAO.isExisit(htInvMst, "");
			if(flag){
				if(reverseqty>0)
				{
					flag = _InvMstDAO.update(sql1.toString(), htInvMst, "");
				reverseqty = (reverseqty-issueqty);
				}

			} else {
				
				//htInvMst.put(IDBConstants.QTY, map.get(IConstants.QTY));
				if(reverseqty >= issueqty)
				{
					double inqty = issueqty * Double.valueOf((String)map.get("UOMQTY"));
					htInvMst.put(IDBConstants.QTY, inqty);
				}
				else
				{
					double inqty = reverseqty * Double.valueOf((String)map.get("UOMQTY"));
					htInvMst.put(IDBConstants.QTY, inqty);
				}
				htInvMst.put(IDBConstants.USERFLD3,"");
				htInvMst.put(IDBConstants.CREATED_AT,  map.get(IConstants.TRAN_DATE).toString().replaceAll("-", "").replaceAll("/", "") + "000000");
				htInvMst.put(IDBConstants.STATUS, "");
				htInvMst.put(IConstants.EXPIREDATE,"");
				flag = _InvMstDAO.insertInvMst(htInvMst);
				
			}
					}
				}
				 ALIssueddetails = new ArrayList();
				}
			if((String) map.get("ISPDA") != "GIWP")
			{
			if(flag)
			{
				boolean bomexistsflag = false;
				boolean bomupdateflag = false;
				BomDAO _BomDAO =new BomDAO() ;
				Hashtable htBomMst = new Hashtable();
				htBomMst.clear();
				htBomMst.put(IDBConstants.PLANT,map.get(IConstants.PLANT));
				htBomMst.put("PARENT_PRODUCT", map.get(IConstants.ITEM));
				htBomMst.put("PARENT_PRODUCT_LOC",  map.get(IConstants.LOC));
				htBomMst.put("PARENT_PRODUCT_BATCH", map.get(IConstants.BATCH));

				bomexistsflag = _BomDAO.isExisit(htBomMst);
			
			if(bomexistsflag)
			{
				Hashtable htBomUpdateMst = new Hashtable();
				htBomUpdateMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htBomUpdateMst.put("PARENT_PRODUCT", map.get(IConstants.ITEM));
				htBomUpdateMst.put("PARENT_PRODUCT_LOC",  map.get(IConstants.LOC));
				htBomUpdateMst.put("PARENT_PRODUCT_BATCH",  map.get(IConstants.BATCH));
				
				StringBuffer bomsql = new StringBuffer(" SET ");
				bomsql.append(" " +"STATUS" + " = 'A' ");
				
				bomupdateflag= _BomDAO.update(bomsql.toString(),htBomUpdateMst,"");
			}
		  }
			}
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}
/* * ************Modification History*********************************
Oct 21 2014 Bruhan, Description: To Change TRAN_DATE as Transaction date
*/
public boolean processMovHis_IN(Map map) throws Exception {
	boolean flag = false;
	MovHisDAO movHisDao = new MovHisDAO();
	CustMstDAO _CustMstDAO = new CustMstDAO();
	movHisDao.setmLogger(mLogger);
	_CustMstDAO.setmLogger(mLogger);
	try {

		Hashtable htMovHis = new Hashtable();
		htMovHis.clear();
		htMovHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
		if((String) map.get("ISPDA") == "GIWP")
		{
			htMovHis.put("DIRTYPE", TransactionConstants.REVERSE_DIRECT_TAX_INVOICE);
			htMovHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd((String) map.get("ISSUEDQTY")));
		}
		else
		{
			htMovHis.put("DIRTYPE", "OBISSUE_REVERSE");
			htMovHis.put(IDBConstants.TRAN_DATE, map.get(IConstants.TRAN_DATE));
		}
		htMovHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
		htMovHis.put("BATNO", map.get(IConstants.BATCH));
		String qty = String.valueOf(map.get("QTY"));
		htMovHis.put(IDBConstants.QTY, qty);
		htMovHis.put("MOVTID", "IN");
		htMovHis.put("RECID", "");
		htMovHis.put(IDBConstants.LOC, map.get(IConstants.LOC));
		htMovHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.DODET_DONUM));
		htMovHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));		
		htMovHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
		htMovHis.put("REMARKS", map.get(IConstants.REMARKS)+","+map.get(IConstants.RSNDESC));

		flag = movHisDao.insertIntoMovHis(htMovHis);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;

	}
	return flag;
}


}
