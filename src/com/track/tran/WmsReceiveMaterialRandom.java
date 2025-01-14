package com.track.tran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.CustMstDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.RecvDetDAO;
import com.track.db.util.POUtil;
import com.track.db.util.TblControlUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class WmsReceiveMaterialRandom implements WmsTran, IMLogger{
	
	private boolean printLog = MLoggerConstant.WmsReceiveMaterialRandom_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsReceiveMaterialRandom_PRINTPLANTMASTERINFO;
	DateUtils dateUtils = null;
        StrUtils su = new StrUtils();
        POUtil poutil = new POUtil();
	private MLogger mLogger = new MLogger();

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
	
	public WmsReceiveMaterialRandom() {
		dateUtils = new DateUtils();
	}
	
	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		try {
			
			String nonstocktype= StrUtils.fString((String) m.get("NONSTKFLAG"));
			if(!nonstocktype.equalsIgnoreCase("Y"))	
		    {
				flag = processInvMst(m);
		    }else{
		    	flag=true;
		    }            
			flag = processPodetForReceive(m);		 
			if (flag) {

				flag = processMovHis_IN(m);

			}
		   
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}

	public boolean processInvMst(Map map) throws Exception {

		boolean flag = false;

		InvMstDAO _InvMstDAO = new InvMstDAO((String) map.get(IConstants.PLANT));
		_InvMstDAO.setmLogger(mLogger);
		try {
			Hashtable htInvMst = new Hashtable();
			htInvMst.clear();

			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.INV_BATCH));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));

			//No need to check now if batch is provided. Check only for NOBATCH.
			if ("NOBATCH".equals(map.get(IConstants.INV_BATCH))) {
				flag = _InvMstDAO.isExisit(htInvMst, "");				
			}
			double UOMQTY=1;
			Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
			MovHisDAO movHisDao1 = new MovHisDAO();
			ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+map.get(IConstants.PLANT)+"_UOM where UOM='"+String.valueOf(map.get(IConstants.UNITMO))+"'",htTrand1);
			if(getuomqty.size()>0)
			{
			Map mapval = (Map) getuomqty.get(0);
			UOMQTY=Double.valueOf((String)mapval.get("UOMQTY"));
			}
			double invumqty = Double.valueOf((String)map.get(IConstants.INV_QTY)) * UOMQTY;
			if (flag) {
				
				StringBuffer sql = new StringBuffer(" SET ");
				sql.append(IDBConstants.QTY + " = QTY +'"
						+ invumqty + "'");
				sql.append("," + IDBConstants.USERFLD3 + " = '"
						+ map.get(IConstants.INV_EXP_DATE) + "'");
				sql.append("," + IDBConstants.USERFLD4 + " = '"
						+ map.get(IConstants.INV_BATCH) + "'");
				sql.append("," + IDBConstants.UPDATED_AT + " = '"
						+ dateUtils.getDateTime() + "'");
				sql.append("," + IDBConstants.EXPIREDATE + " = '"
						+ map.get(IConstants.EXPIREDATE) + "'");
				flag = _InvMstDAO.update(sql.toString(), htInvMst, "");

			}  else {
				htInvMst.put(IDBConstants.EXPIREDATE, map.get(IConstants.EXPIREDATE));
				htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
				htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.INV_BATCH));
				htInvMst.put(IDBConstants.QTY, String.valueOf(invumqty));
				htInvMst.put(IDBConstants.USERFLD3, map.get(IConstants.INV_EXP_DATE));
				htInvMst.put(IDBConstants.CREATED_AT, map.get(IConstants.RECVDATE).toString().replaceAll("/", "") + "000000");

				htInvMst.put(IDBConstants.STATUS, "");

				flag = _InvMstDAO.insertInvMst(htInvMst);

			}
                     

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}

	public boolean processMovHis_IN(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		try {
			Hashtable htRecvHis = new Hashtable();
			htRecvHis.clear();
			htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvHis.put("DIRTYPE", TransactionConstants.ORD_RECV);
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvHis.put("MOVTID", "IN");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.CUSTOMER_CODE, map.get(IConstants.CUSTOMER_CODE));
			htRecvHis.put(IDBConstants.POHDR_JOB_NUM, map.get(IConstants.JOB_NUM));
			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.PODET_PONUM));
			
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));

			htRecvHis.put("BATNO", map.get(IConstants.INV_BATCH));
			htRecvHis.put("QTY", map.get(IConstants.INV_QTY));
			htRecvHis.put(IDBConstants.TRAN_DATE,  map.get(IConstants.TRAN_DATE));
			htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			//htRecvHis.put("REMARKS", su.InsertQuotes((String)map.get(IConstants.REMARKS)));
			String empno=(String)map.get(IConstants.EMPNO);
			if(empno.length()>0) {
				htRecvHis.put(IDBConstants.REMARKS, su.InsertQuotes((String)map.get(IConstants.REMARKS))+","+empno);
          	}
            else
     	     {
        	    htRecvHis.put(IDBConstants.REMARKS, su.InsertQuotes((String)map.get(IConstants.REMARKS)));
     	     }

			flag = movHisDao.insertIntoMovHis(htRecvHis);

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

	public boolean processPodetForReceive(Map map) throws Exception {

		boolean flag = false;
		try {
			String POLNO="";
			
			// update receive qty in podet
			PoDetDAO _PoDetDAO = new PoDetDAO((String) map.get("PLANT"));
			PoHdrDAO _PoHdrDAO = new PoHdrDAO((String) map.get("PLANT"));
			RecvDetDAO _RecvDetDAO = new RecvDetDAO();

			_PoDetDAO.setmLogger(mLogger);
			_PoHdrDAO.setmLogger(mLogger);
			_RecvDetDAO.setmLogger(mLogger);
			poutil.setmLogger(mLogger);
			Hashtable htCondiPoDet = new Hashtable();
			
					
			//process podet qty start
			boolean insertFlag=false;
			double totBal = 0;
        	double orderqty = 0;		 
		    double receivedqty = 0;
		    double balQty=0;
		    String RECEIVING_QTY = (String)map.get(IConstants.RECV_QTY);
		    double receivingQty = Double.parseDouble(((String) RECEIVING_QTY.trim().toString()));
			receivingQty = StrUtils.RoundDB(receivingQty, IConstants.DECIMALPTS);
			double sumqty =0;
				
			ArrayList alResult = new ArrayList();
			alResult = poutil.getPODetDetailsRandomMutiUOM((String)map.get("PLANT"),(String) map.get("PONO"),(String) map.get("ITEM"),(String) map.get("UNITMO"));
			 if(alResult.size()>=1){
				
				 
				  for (int i = 0; i < alResult.size(); i++) {    
					  Map mapLn = (Map) alResult.get(i);
					  totBal = Double.parseDouble((String)mapLn.get("totBal"));
	            	  orderqty = Double.parseDouble((String)mapLn.get("Qtyor"));			 
	            	  receivedqty  = Double.parseDouble((String)mapLn.get("QtyRc"));
	            	  
	            	  balQty = orderqty - receivedqty;
	 		          POLNO =  StrUtils.fString((String)mapLn.get("polnno"));
	 		          map.put(IConstants.PODET_POLNNO,POLNO);
	            	  map.put(IConstants.ORD_QTY,Double.toString(orderqty));
	            	  //calculation line qty
	            	  if ((receivingQty==balQty) && (receivingQty >0)  ){   
	            	    	map.put(IConstants.RECV_QTY, Double.toString((receivingQty)));
	            	    	 sumqty= receivedqty + receivingQty ;
	            	    	 sumqty = StrUtils.RoundDB(sumqty,IConstants.DECIMALPTS );
	            	    	 receivingQty = 0;
	            	    
	            	    	
	 		          }else if((receivingQty < balQty) && (receivingQty >0)  ){   
		 		           	 map.put(IConstants.RECV_QTY , Double.toString((receivingQty)));
		 		             sumqty= receivedqty + receivingQty;
		 		             sumqty = StrUtils.RoundDB(sumqty,IConstants.DECIMALPTS );
		 		             receivingQty = 0;
			 		      
			 		   }
	            	  else if((receivingQty > balQty) && (receivingQty >0)  ){   
	 		                  	
	 		        	  map.put(IConstants.RECV_QTY, Double.toString((balQty)));
	 		        	  receivingQty = receivingQty-balQty;
	 		        		 		        	  sumqty= receivedqty+balQty;
	 		        	  sumqty = StrUtils.RoundDB(sumqty,IConstants.DECIMALPTS );
	 		           	  insertFlag=false;
	 		          }
	            	//calculation line qty end
	            	  
						
			String queryPoDet = "";
			String queryPoHdr = "";

			if(insertFlag==false){
			htCondiPoDet.put("PLANT", map.get("PLANT"));
			htCondiPoDet.put("pono", map.get("PONO"));
			htCondiPoDet.put("polnno",  POLNO);	  
			htCondiPoDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htCondiPoDet.put(IDBConstants.UNITMO, map.get(IConstants.UNITMO));
			if (orderqty == sumqty) {
				queryPoDet = " set qtyrc= isNull(qtyrc,0) + "
						+ map.get(IConstants.RECV_QTY) + " , LNSTAT='C' ";

			} else {
				queryPoDet = " set qtyrc= isNull(qtyrc,0) + "
						+ map.get(IConstants.RECV_QTY) + " , LNSTAT='O' ";

			}
			// Added By Samatha extracond to Controll the Receive Qty Excced the
			// PickQty Aug 24 1010
			String extraCond = " AND  QtyOr >= isNull(qtyrc,0) + "
					+ map.get(IConstants.RECV_QTY);
			flag = _PoDetDAO.update(queryPoDet, htCondiPoDet, extraCond);
			if (flag && insertFlag==false) {
				Hashtable htCondiPoHdr = new Hashtable();
				htCondiPoHdr.put("PLANT", map.get(IConstants.PLANT));
				htCondiPoHdr.put("pono", map.get(IConstants.PONO));
			
				/*if((i+1)==alResult.size())
                {
	
					flag = _PoDetDAO.isExisit(htCondiPoHdr, "lnstat in ('O','N')");
	
					if (flag)
						queryPoHdr = "set STATUS='O' ";
					else
						queryPoHdr = "set STATUS='C' ";
	
					flag = _PoHdrDAO.updatePO(queryPoHdr, htCondiPoHdr, "");
                }*/
			}
			
			Hashtable htRecvDet = new Hashtable();
			
			htRecvDet.clear();
			htRecvDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvDet.put(IDBConstants.PODET_PONUM, map.get(IConstants.PODET_PONUM));
			htRecvDet.put("LNNO", map.get(IConstants.PODET_POLNNO));
			htRecvDet.put(IDBConstants.CUSTOMER_NAME, su.InsertQuotes((String)map.get(IConstants.CUSTOMER_NAME)));
			htRecvDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvDet.put(IDBConstants.ITEM_DESC,su.InsertQuotes((String) map.get(IConstants.ITEM_DESC)));
			htRecvDet.put("BATCH", map.get(IConstants.BATCH));
			htRecvDet.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htRecvDet.put("ORDQTY", map.get(IConstants.ORD_QTY));
			htRecvDet.put(IDBConstants.CURRENCYID,poutil.getCurrencyID((String)map.get(IConstants.PLANT), (String)map.get(IConstants.PODET_PONUM)));
			Hashtable pohashtable = new Hashtable();
			pohashtable.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			pohashtable.put(IDBConstants.PODET_PONUM, map.get(IConstants.PODET_PONUM));
			pohashtable.put("POLNNO", map.get(IConstants.PODET_POLNNO));
			pohashtable.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvDet.put("UNITCOST",poutil.getPodetColdata(pohashtable,"UNITCOST"));
			htRecvDet.put("MANUFACTURER",su.InsertQuotes(poutil.getPodetColdata(pohashtable,"USERFLD4")));
			htRecvDet.put("REMARK",su.InsertQuotes((String) map.get(IConstants.REMARKS)));
			htRecvDet.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htRecvDet.put(IDBConstants.CREATED_BY, map.get(IConstants.CREATED_BY));
		    htRecvDet.put(IDBConstants.RECVDET_TRANTYPE, "IB");
		    htRecvDet.put(IDBConstants.RECVDATE,  map.get(IConstants.RECVDATE));
		    htRecvDet.put("REVERSEQTY",  "0");
		    htRecvDet.put(IDBConstants.GRNO,  map.get(IConstants.GRNO));
		    htRecvDet.put(IDBConstants.EMPNO,  map.get(IConstants.EMPNO));
		    htRecvDet.put("EXPIRYDAT", map.get(IConstants.EXPIREDATE));
		    
		    String nonstocktype= StrUtils.fString((String) map.get("NONSTKFLAG"));
			if(nonstocktype.equalsIgnoreCase("Y"))	
		    {
				htRecvDet.put("RECQTY", String.valueOf(map.get(IConstants.RECV_QTY)));
				flag = _RecvDetDAO.insertRecvDet(htRecvDet);
		    }
			else
			{			
			Hashtable htInvMst = new Hashtable();				
			htInvMst.clear();
			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
			String extCond = "";
			extCond = "QTY > 0";
			InvMstDAO _InvMstDAO = new InvMstDAO();
			ArrayList alStock = _InvMstDAO.selectInvMstDes("ID, CRAT, QTY", htInvMst, extCond);
			if (!alStock.isEmpty()) {
				double quantityToAdjust = Double.parseDouble("" + map.get(IConstants.RECV_QTY));
				Iterator iterStock = alStock.iterator();
				while(quantityToAdjust > 0.0 && iterStock.hasNext()) {
					Map mapIterStock = (Map)iterStock.next();
					double currRecordQuantity = Double.parseDouble("" + mapIterStock.get(IConstants.QTY));
					double adjustedQuantity = quantityToAdjust > currRecordQuantity ? currRecordQuantity : quantityToAdjust;
			
			htRecvDet.put("RECQTY", String.valueOf(adjustedQuantity));			
		    htRecvDet.put(IDBConstants.INVID, mapIterStock.get(IDBConstants.INVID));
			flag = _RecvDetDAO.insertRecvDet(htRecvDet);
			if(flag)
			{    //new TblControlUtil().updateTblControlSeqNoWithoutorder(PLANT,"GRRECEIPT","GR");  
				 new TblControlUtil().updateTblControlSeqNo((String)map.get(IConstants.PLANT),"GRRECEIPT","GI",(String)map.get(IConstants.PODET_PONUM));
			}
			quantityToAdjust -= adjustedQuantity;
			if(receivingQty==0)
			{
				insertFlag=true;
			}
				}
			}
			}
			}// if flag end
		  }//alResult for loop end
				  String queryPoDet = "";
					String queryPoHdr = "";
				  Hashtable htCondiPoHdr = new Hashtable();
					htCondiPoHdr.put("PLANT", map.get(IConstants.PLANT));
					htCondiPoHdr.put("pono", map.get(IConstants.PONO));
				  flag = _PoDetDAO.isExisit(htCondiPoHdr, "lnstat in ('O','N')");
					
					if (flag)
						queryPoHdr = "set STATUS='O' ";
					else
						queryPoHdr = "set STATUS='C' ";
	
					flag = _PoHdrDAO.updatePO(queryPoHdr, htCondiPoHdr, "");
     	 }// alResut end
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return flag;
	}




}
