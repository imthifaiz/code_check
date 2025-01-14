
package com.track.tran;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.DoTransferDetDAO;
import com.track.dao.DoTransferHdrDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.BomDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.ShipHisDAO;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsIssueWOInvcheck implements WmsTran, IMLogger {
	private boolean printLog = MLoggerConstant.WmsIssueMaterial_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsIssueMaterial_PRINTPLANTMASTERINFO;
	DateUtils dateUtils = null;
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public WmsIssueWOInvcheck() {
		dateUtils = new DateUtils();
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		try {
			flag = processDodetForIssue(m);
			if (flag) {
                        System.out.println(" Wms Issue Skip Inventory");
				//flag = processInvMst(m);

			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}

	public boolean processInvMst(Map map) throws Exception {

		boolean flag = false;
		boolean flagshiphis = false;
		String extCond = "";
		String sBatch = "";
		String sLoc = "";
		String sLocShipHis = "";
		String sIssueQty = "";
		InvMstDAO _InvMstDAO = new InvMstDAO();
		DoDetDAO _DoDetDAO = new DoDetDAO();
		ShipHisDAO _ShipHisDAO = new ShipHisDAO();
		_InvMstDAO.setmLogger(mLogger);
		_DoDetDAO.setmLogger(mLogger);
		_ShipHisDAO.setmLogger(mLogger);
		String ExpDate="";
		try {
			// Get loc,batch from ShipHis
			Hashtable ht = new Hashtable();
			ht.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			ht.put("dono", map.get(IConstants.DODET_DONUM));
			ht.put("dolnno", map.get(IConstants.DODET_DOLNNO));
			ht.put(IDBConstants.ITEM, map.get(IConstants.ITEM));

			List listQry = _DoDetDAO.getIssueShipHisDetailByWMS(map.get(
					IConstants.PLANT).toString(), map.get(
					IConstants.DODET_DONUM).toString(), map.get(
					IConstants.DODET_DOLNNO).toString(), map.get(
					IConstants.ITEM).toString());
			
			for (int i = 0; i < listQry.size(); i++) {
				
				Map m = (Map) listQry.get(i);
				sLocShipHis = (String) m.get("loc");
				
				sBatch = (String) m.get("batch");
				sIssueQty = (String) m.get("pickqty");

			/*	Hashtable htInvMst = new Hashtable();
				htInvMst.clear();
				htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
				htInvMst.put(IDBConstants.USERFLD4, sBatch);
				htInvMst.put(IDBConstants.LOC, sLocShipHis);
				
				StringBuffer sql = new StringBuffer(" SET ");
				sql.append("QTY" + " = QTY - " + sIssueQty + " ");
				extCond = "AND QTY >='" + sIssueQty + "' ";
				flag = _InvMstDAO.update(sql.toString(), htInvMst, "");*/
				
				
			
				
					String strStatus = "C";
					Hashtable htShipHis = new Hashtable();
					htShipHis.clear();
					htShipHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
					htShipHis.put("dono", map.get(IConstants.DODET_DONUM));
					htShipHis.put("dolno", map.get(IConstants.DODET_DOLNNO));
					htShipHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
					htShipHis.put("BATCH", sBatch);
					htShipHis.put(IDBConstants.LOC, sLocShipHis);
					StringBuffer sqlshiphis = new StringBuffer(" SET ");
					sqlshiphis.append("STATUS" + " = '" + strStatus
							+ "',USERFLD1=  '" + map.get("SHIPPINGNO") + "'");
					String extraCon = "  AND ISNULL(STATUS,'')='O'";
					flagshiphis = _DoDetDAO.updateShipHis(
							sqlshiphis.toString(), htShipHis, extraCon);
					
					
					boolean bomexistsflag = false;
					boolean itemflag=false;
					
				
					String extcond="";
					
							
					ItemMstDAO _ItemMstDAO =new ItemMstDAO();
					BomDAO _BomDAO =new BomDAO() ;
					
					
					try {
					/*Hashtable htItemMst= new Hashtable();
					htItemMst.clear();
					
					htItemMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
					htItemMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
					
					extcond=" userfld1='K'";
					
					itemflag = _ItemMstDAO .isExisit(htItemMst, extcond);
				
					if(itemflag)
					{*/
						Hashtable htBomMst = new Hashtable();
						htBomMst.clear();
						htBomMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
						htBomMst.put("PARENT_PRODUCT", map.get(IConstants.ITEM));
						htBomMst.put("PARENT_PRODUCT_LOC", sLocShipHis);
						htBomMst.put("PARENT_PRODUCT_BATCH",sBatch);

						bomexistsflag = _BomDAO.isExisit(htBomMst);
					
						if (bomexistsflag) {

							/*Hashtable hrBomDelete = new Hashtable();
							hrBomDelete.put("PLANT", map.get(IConstants.PLANT));
							hrBomDelete.put("PARENT_PRODUCT", map.get(IConstants.ITEM));
							hrBomDelete.put("PARENT_PRODUCT_LOC",  sLocShipHis);
							hrBomDelete.put("PARENT_PRODUCT_BATCH", sBatch);
						
							Commanded on July071711 By Bruhan To disable remove item from Bom when Issuing Parent Item
						    resultBomDelete = _BomDAO.delete(hrBomDelete);*/
							
							
						 Hashtable htUpdateBOM = new Hashtable();
			    			 htUpdateBOM .put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			    			 htUpdateBOM .put("PARENT_PRODUCT", map.get(IConstants.ITEM));
			    			 htUpdateBOM.put("PARENT_PRODUCT_LOC",  sLocShipHis);
			    			 htUpdateBOM .put("PARENT_PRODUCT_BATCH", sBatch);
			    		
			    			 StringBuffer sql1 = new StringBuffer(" SET ");
			 				 sql1.append(" " +"STATUS" + " = 'C' ");
		    				 sql1.append("," + IDBConstants.UPDATED_AT1 + " = '"
			 						+ dateUtils.getDateTime() + "'");
			 				 flag=_BomDAO.update(sql1.toString(), htUpdateBOM, " ");
							
						}
					 //}
			//Commented by samatha as there is no reference why we are updating the status to O as MISC-ISSUE status should always be C			
				/*	//update reverseqty status to O
					String strrevStatus = "O";
					Hashtable htrevShipHis = new Hashtable();
					htrevShipHis.clear();
					htrevShipHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
					StringBuffer sqlrevshiphis = new StringBuffer(" SET ");
					sqlrevshiphis.append("STATUS" + " = '" + strrevStatus
							+ "'");
					String extrarevCon = "AND REVERSEQTY IS NOT NULL";
					boolean revqtyexists=  _ShipHisDAO.isExisit(htrevShipHis, " REVERSEQTY IS NOT NULL ");
					if(revqtyexists)
					{
					flagshiphis = _DoDetDAO.updateShipHis(sqlrevshiphis.toString(), htrevShipHis, extrarevCon);
					}
					//
				*/
						
					} //end try
			        catch (Exception e) {

						this.mLogger.exception(this.printLog, "Kitting Outbound Issue", e);
						throw e;

					}
								
			      //update bom for outbound issue
				

				if (flag) {
					MovHisDAO movHisDao = new MovHisDAO();
					movHisDao.setmLogger(mLogger);
					Hashtable htRecvHis = new Hashtable();
					htRecvHis.clear();
					htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
					htRecvHis.put("DIRTYPE", TransactionConstants.ORD_ISSUE);
					htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
					htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.DODET_DONUM));
					htRecvHis.put(IDBConstants.MOVHIS_ORDLNO, map.get(IConstants.DODET_DOLNNO));
					htRecvHis.put("MOVTID", "");
					htRecvHis.put("RECID", "");
					htRecvHis.put(IDBConstants.LOC, sLocShipHis);
					htRecvHis.put("BATNO", sBatch);
					htRecvHis.put("QTY", sIssueQty);
					htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
					htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
					htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
					htRecvHis.put(IDBConstants.REMARKS, map.get(IConstants.REMARKS));

					flag = movHisDao.insertIntoMovHis(htRecvHis);
				}

			}
			if(listQry.isEmpty())
			{
				
				throw new Exception(
				"Not Enough Inventory in Shipping Area To Issue");	
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw new Exception(
					"Not Enough Inventory in Shipping Area To Issue");
		}

		return flag;
	}

	public boolean processDodetForIssue(Map map) throws Exception {
		boolean flag = false;
		boolean dotransferdetflag=false;
		boolean dotransferhdrflag=false;
		try {

			// update Issue qty in dodet
			DoDetDAO _DoDetDAO = new DoDetDAO();
			DoHdrDAO _DoHdrDAO = new DoHdrDAO();
			DoTransferDetDAO _DoTransferDetDAO = new DoTransferDetDAO();
			DoTransferHdrDAO _DoTransferHdrDAO = new DoTransferHdrDAO();
			_DoDetDAO.setmLogger(mLogger);
			_DoHdrDAO.setmLogger(mLogger);
			_DoTransferDetDAO.setmLogger(mLogger);
			_DoTransferHdrDAO.setmLogger(mLogger);
			
		    Hashtable htCondiPoDet = new Hashtable();
			StringBuffer query = new StringBuffer("");

			htCondiPoDet.put("PLANT", map.get(IConstants.PLANT));
			htCondiPoDet.put("item", map.get(IConstants.ITEM));
			htCondiPoDet.put("dono", map.get(IConstants.DODET_DONUM));
			htCondiPoDet.put("dolnno", map.get(IConstants.DODET_DOLNNO));
			
		
			query.append("isnull(QtyOr,0) as QtyOr");
			query.append(",isnull(qtyPick,0) as qtyPick");
			query.append(",isnull(QtyIs,0) as QtyIs");

			Map mQty = _DoDetDAO.selectRow(query.toString(), htCondiPoDet);
			
			double ordQty = Double.parseDouble((String) mQty.get("QtyOr"));
			double pickQty = Double.parseDouble((String) mQty.get("qtyPick"));
			double isQty = Double.parseDouble((String) mQty.get("QtyIs"));

			double tranQty = Double.parseDouble((String) map
					.get(IConstants.QTY));
			tranQty = StrUtils.RoundDB(tranQty, IConstants.DECIMALPTS);
			pickQty = StrUtils.RoundDB(pickQty, IConstants.DECIMALPTS);
			String queryPoDet = "";
			String queryPoHdr = "";
			String queryDoTransferDet="";
			String queryDoTransferHdr="";
			double sumqty = isQty + tranQty;
			sumqty = StrUtils.RoundDB(sumqty, IConstants.DECIMALPTS);
			System.out.println("orderqty"+ordQty+"issueqty"+sumqty+"pickqty"+pickQty);			
			if ((ordQty == pickQty) && pickQty == sumqty) {

				queryPoDet = "set qtyis= isNull(qtyis,0) + "
						+ map.get(IConstants.QTY) + " , LNSTAT='C' ";
		
			} else {
				queryPoDet = "set qtyis= isNull(qtyis,0) + "
						+ map.get(IConstants.QTY) + " , LNSTAT='O' ";
		
			}

			// Added By Samatha extracond to Controll the issue Qty Excced the
			// PickQty Aug 24 1010
			String extraCond = " AND  convert(decimal,qtyPick) >= convert(decimal,isNull(qtyis,0) + "
					+ map.get(IConstants.QTY)+")";

			flag = _DoDetDAO.update(queryPoDet, htCondiPoDet, extraCond);
			
			if (flag) {
	
				 dotransferdetflag=_DoTransferDetDAO.update(queryPoDet, htCondiPoDet, "");
				//update Header Part
				Hashtable htCondiPoHdr = new Hashtable();
				htCondiPoHdr.put("PLANT", map.get(IConstants.PLANT));
				htCondiPoHdr.put("dono", map.get(IConstants.DODET_DONUM));
				flag = _DoDetDAO.isExisit(htCondiPoHdr, "lnstat in ('O','N')");
          
				if (flag)
					queryPoHdr = "set STATUS='O' ";
				else
					queryPoHdr = "set STATUS='C' ";
					

				flag = _DoHdrDAO.update(queryPoHdr, htCondiPoHdr, "");
				
				dotransferhdrflag=_DoTransferHdrDAO.update(queryPoHdr, htCondiPoHdr, "");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			System.out.println("Exception"+e.getMessage());			
			throw new Exception("Issue Qty Exceeded the Pick Qty to Issue");
		}
		
		return flag;
		
		
	}

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {

		return null;
	}

	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		this.mLogger.setLoggerConstans(dataForLogging);
	}
}