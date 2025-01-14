package com.track.tran;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.BomDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.DoTransferDetDAO;
import com.track.dao.DoTransferHdrDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.ShipHisDAO;
import com.track.dao.TblControlDAO;
import com.track.db.util.DOTransferUtil;
import com.track.db.util.DOUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.TblControlUtil;
import com.track.db.util.TempUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.tables.DODET;



public class WmsMobileSales implements WmsTran, IMLogger {
	private boolean printLog = MLoggerConstant.WmsMobileSales_PRINTPLANTMASTERLOG;
	
	DateUtils dateUtils = null;
    StrUtils su = new StrUtils();
    DOUtil doutil = new DOUtil();
    private MLogger mLogger = new MLogger();
	
	public MLogger getmLogger() {
		return mLogger;
	}
	
	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
	
	public boolean processWmsTran(Map map) throws Exception {
			/*if(map.get("PROCESSING_MODE").equals("Create"))
			{
				return process(map);
			}*/
	
			return process(map);
				
	}
	
	@SuppressWarnings("unchecked")
	private boolean process(Map map) throws Exception {
	
		Boolean result = false;
		String plant="",queryUpdateInv="",strPersonincharge="",remark1="",custName,price="",tempprice="",dono="";
		DoHdrDAO _DOHdrDAO = new DoHdrDAO();
		DoDetDAO _DODetDAO = new DoDetDAO();
		DOUtil _dOUtil = new DOUtil();
		DOTransferUtil _DOTransferUtil  = new DOTransferUtil();
		ShipHisDAO shipdao = new ShipHisDAO();
		MovHisDAO movHisDao = new MovHisDAO();
		StrUtils strUtils = new StrUtils();
		String strEmpty="",processno="",updateDono="";
		TblControlDAO _TblControlDAO = new TblControlDAO();
		tempprice="0";
					
		Boolean dotransferhdr=false;
		try {
			if(map.get("PROCESSING_MODE").equals("Create")){
			
			
			int intQty = 1;
			//DOHDER hastable;
			Hashtable hrHdr = new Hashtable();
			hrHdr.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			hrHdr.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			hrHdr.put(IDBConstants.DONO, map.get(IConstants.DONO));
			hrHdr.put(IDBConstants.DOHDR_CUST_CODE, map.get(IConstants.CUSTOMER_CODE));
			hrHdr.put(IDBConstants.DOHDR_CUST_NAME, strUtils.InsertQuotes((String)map.get(IConstants.CUSTOMER_NAME)));
			hrHdr.put(IDBConstants.DOHDR_COL_DATE, map.get(IConstants.ORDERDATE));
			hrHdr.put(IDBConstants.DOHDR_COL_TIME, strEmpty);
			hrHdr.put(IDBConstants.DELIVERYDATE, strEmpty) ;
			strPersonincharge=(String)map.get("PERSONINCHARGE");
			hrHdr.put(IDBConstants.DOHDR_PERSON_INCHARGE, strUtils.InsertQuotes((String)map.get("PERSONINCHARGE")));
			hrHdr.put(IDBConstants.DOHDR_CONTACT_NUM,"");
			hrHdr.put(IDBConstants.DOHDR_ADDRESS, (String)map.get("ADDRESS"));
			hrHdr.put(IDBConstants.DOHDR_ADDRESS2, (String)map.get("ADDRESS2"));
			hrHdr.put(IDBConstants.DOHDR_ADDRESS3, (String)map.get("ADDRESS3"));
			hrHdr.put(IDBConstants.DOHDR_REMARK2,  strUtils.InsertQuotes((String)map.get("REMARK2")));
			hrHdr.put(IDBConstants.DOHDR_JOB_NUM, map.get(IConstants.JOB_NUM));
			hrHdr.put(IDBConstants.DOHDR_REMARK1,  strUtils.InsertQuotes((String)map.get(IConstants.REMARK)));
			hrHdr.put(IDBConstants.ORDERTYPE, map.get(IConstants.ORDERTYPE));
			hrHdr.put(IDBConstants.DOHDR_GST, map.get("OUTBOUND_GST"));
			hrHdr.put(IDBConstants.CURRENCYID, map.get(IConstants.CURRENCYID));
			hrHdr.put("STATUS", map.get("STATUS"));
			hrHdr.put(IDBConstants.ORDSTATUSID, "");
			hrHdr.put(IDBConstants.DOHDR_SCUST_NAME, map.get("SCUST_NAME"));
			hrHdr.put(IDBConstants.DOHDR_SCONTACT_NAME, map.get("SCONTACT_NAME"));
			hrHdr.put(IDBConstants.DOHDR_SADDR1, strUtils.InsertQuotes((String)map.get("SADDR1")));
			hrHdr.put(IDBConstants.DOHDR_SADDR2, strUtils.InsertQuotes((String)map.get("SADDR2")));
			hrHdr.put(IDBConstants.DOHDR_SCITY, map.get("SCITY"));
			hrHdr.put(IDBConstants.DOHDR_SCOUNTRY, map.get("SCOUNTRY"));
			hrHdr.put(IDBConstants.DOHDR_SZIP, map.get("SZIP"));
			hrHdr.put(IDBConstants.DOHDR_STELNO, map.get("STELNO"));
			hrHdr.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			hrHdr.put(IDBConstants.CREATED_BY,map.get(IConstants.LOGIN_USER));
			
			
			Hashtable hdrCheck = new Hashtable();
			hdrCheck.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			hdrCheck.put(IDBConstants.DONO, map.get(IConstants.DONO));
			//check dono already existe then create new dono
			if(!_DOHdrDAO.isExisit(hdrCheck))
			{
				//insert into DOHDER
				result=_DOHdrDAO.insertDoHdr(hrHdr);
			}
			else
			{
				processno=(String)map.get(IConstants.DONO);
				updateDono = _TblControlDAO.getNextOrder((String)map.get(IConstants.PLANT),(String)map.get(IConstants.LOGIN_USER),IConstants.OUTBOUND);
				//insert old and new dono into dodet_temp table 
				Hashtable htDetTemp = new Hashtable();
				htDetTemp.put(IDBConstants.PLANT, (String)map.get(IConstants.PLANT));
				htDetTemp.put(IDBConstants.DODET_DONUM, updateDono);
				htDetTemp.put(IDBConstants.DODET_ITEM, processno);
				htDetTemp.put("ITEMDESC","NEWDO");
				htDetTemp.put("ITEMDETAILDESC", "");
				htDetTemp.put(IDBConstants.DODET_QTYOR,  tempprice);
				htDetTemp.put("UNITMO", "");
				htDetTemp.put("LISTPRICE", tempprice);
				htDetTemp.put(IDBConstants.DODET_UNITPRICE, "");
				htDetTemp.put(IDBConstants.REMARKS, "");
				htDetTemp.put(IDBConstants.CREATED_BY,map.get(IConstants.LOGIN_USER));
				htDetTemp.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
				result = _dOUtil.saveDoDetTempDetails(htDetTemp);
				//insert old and new dono into dodet_temp table 
				
				//insert into DOHDER
				hrHdr.remove(IDBConstants.DONO);
				hrHdr.put(IDBConstants.DONO, updateDono);
				result=_DOHdrDAO.insertDoHdr(hrHdr);
			}
//			///check dono already existe then create new dono end
			
			hrHdr.remove(IDBConstants.ORDERTYPE);
            hrHdr.remove(IDBConstants.DOHDR_SCUST_NAME);
            hrHdr.remove(IDBConstants.DOHDR_SCONTACT_NAME);
            hrHdr.remove(IDBConstants.DOHDR_SADDR1);
            hrHdr.remove(IDBConstants.DOHDR_SADDR2);
            hrHdr.remove(IDBConstants.DOHDR_SCITY);
		    hrHdr.remove(IDBConstants.DOHDR_SCOUNTRY);
            hrHdr.remove(IDBConstants.DOHDR_SZIP);
            hrHdr.remove(IDBConstants.DOHDR_STELNO);
            hrHdr.remove(IDBConstants.ORDSTATUSID);
			if(result){
				//insert into dotransfer header
				result = _DOTransferUtil.saveDoTransferHdrDetails(hrHdr);
				//insert into movhis
				Hashtable htRecvHis = new Hashtable();
				remark1=(String)map.get("REMARK");
				custName=(String)map.get(IConstants.CUSTOMER_NAME);
				htRecvHis.clear();
				htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
				htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_OB);
				htRecvHis.put(IDBConstants.CUSTOMER_CODE, map.get(IConstants.CUSTOMER_CODE));
				htRecvHis.put(IDBConstants.POHDR_JOB_NUM, map.get(IConstants.JOB_NUM));
				if(!updateDono.equals(""))
				{
					 dono= updateDono;
					 htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, updateDono);
				}
				else
				{
					htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.DONO));
				}
				
				htRecvHis.put(IDBConstants.CREATED_BY,map.get(IConstants.LOGIN_USER));
				htRecvHis.put("MOVTID", "");
				htRecvHis.put("RECID", "");
			    if (!remark1.equals("")) {
					htRecvHis.put(IDBConstants.REMARKS, strUtils.InsertQuotes(custName) + ","+ strUtils.InsertQuotes(remark1));
				} else {
					htRecvHis.put(IDBConstants.REMARKS, strUtils.InsertQuotes(custName));
				}

				htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
				htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());

				result = movHisDao.insertIntoMovHis(htRecvHis);
				
				if(!result) // to check header Movement History Insert 
				{
					throw new Exception("Error in Creating" + dono);
				}
		
			}
			else //DOHDR else part 
			{
				throw new Exception("Error in Creating" + dono);
			}	
			
			//DODET hastable
			if (result)
			{
			   String strCount="",item="",itemDesc="",itemDetailDesc="",unitmo="",job_num="",customer_name="",det_remark="";
				Integer itemCount = new Integer(StrUtils.fString((String) map.get("ITEM_COUNTS")).trim());
			    	int intDolnno=1;
				 
					for (int i = 0; i < itemCount; i++) {
					try{
					Hashtable htDet = new Hashtable();
					Hashtable htShiphis = new Hashtable();
					//strCount=Integer.toString(i+1); //to save line number for DODET and SHIPHIS
					strCount=Integer.toString(intDolnno); //to save line number for DODET and SHIPHIS
					plant=(String)map.get(IConstants.PLANT);
					if(!updateDono.equals(""))
					{
						 dono= updateDono;
					}
					else
					{
					   dono= (String)map.get(IConstants.DONO);
					}
					item=(String)map.get("ITEM_" + i);
					itemDetailDesc=(String)map.get("ITEM_DETAIL_DESC" + i);
					job_num=(String)map.get("JOB_NUM");
                    unitmo=(String)map.get("UNITMO_"+ i);
                    det_remark=(String)map.get("DETREMARK_"+ i);
                    customer_name=(String)map.get(IConstants.CUSTOMER_NAME);
                  
                    //check valid product
                    ItemMstUtil itemMstUtil = new ItemMstUtil();
					itemMstUtil.setmLogger(mLogger);
					String itemItem = itemMstUtil.isPDAAddProductValidAlternateItemInItemmst(plant, item);
					if(itemItem != null || !itemItem.equals("")){
					//if(itemItem!="" || !itemItem.equalsIgnoreCase("null")){
						item = itemItem;
					}else{
												
						throw new Exception("Product not found....!");
					}
					Map mPrddet = new ItemMstDAO().getProductNonStockDetails(plant,item);
                    String nonstocktype= StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
                    String nonstocktypeDesc= StrUtils.fString((String) mPrddet.get("STOCKTYPEDESC"));
                    itemDesc =StrUtils.fString((String) mPrddet.get("ITEMDESC"));	
                    htDet.put(IDBConstants.PLANT, plant);
					htDet.put(IDBConstants.DODET_DONUM, dono);
					htDet.put(IDBConstants.DODET_ITEM, item);
					htDet.put("ITEMDESC", strUtils.InsertQuotes(itemDesc));
					htDet.put("UNITMO", strUtils.InsertQuotes(unitmo));
					htDet.put(IDBConstants.DODET_ITEM_DESC,strUtils.InsertQuotes(itemDesc));
					htDet.put(IDBConstants.DODET_ITEM_DESC,strUtils.InsertQuotes(itemDesc));
					htDet.put(IDBConstants.DODET_JOB_NUM, strUtils.InsertQuotes(job_num));
					htDet.put(IDBConstants.DODET_QTYOR,  map.get("QTY_ORDER_" + i));
					htDet.put("QTYPICK", "0");
					htDet.put(IDBConstants.DODET_QTYIS, "0");
					price=(String)map.get("UNITPRICE_" + i);
			    	price = new DOUtil().getConvertedUnitCostToLocalCurrency(plant,dono,price) ;
					htDet.put(IDBConstants.DODET_UNITPRICE, price);
					//htDet.put(IDBConstants.DODET_COMMENT1,strUtils.InsertQuotes(det_remark));
					htDet.put(IDBConstants.DODET_DOLNNO, strCount);
					htDet.put(IDBConstants.DODET_LNSTATUS, "N");
				    htDet.put(IDBConstants.DODET_PICKSTATUS, "N");
					String CURRENCYUSEQT = new DOUtil().getCurrencyUseQT(plant, dono);
					htDet.put(IDBConstants.CURRENCYUSEQT, CURRENCYUSEQT);
					htDet.put(IDBConstants.CREATED_BY,map.get(IConstants.LOGIN_USER));
					htDet.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
					
					//shiphis hastable
					htShiphis.put(IDBConstants.PLANT, plant);
					htShiphis.put(IDBConstants.DODET_DONUM, dono);
					htShiphis.put("DOLNO",strCount);
					htShiphis.put(IDBConstants.CUSTOMER_NAME, strUtils.InsertQuotes(customer_name));
					htShiphis.put(IDBConstants.ITEM, item);
     				htShiphis.put(IDBConstants.ITEM_DESC, strUtils.InsertQuotes(itemDesc));//strUtils.InsertQuotes(map.get(IConstants.ITEM_DESC )));
					htShiphis.put("BATCH", "NOBATCH");
					htShiphis.put(IDBConstants.LOC, "");
					htShiphis.put("ORDQTY",  map.get("QTY_ORDER_"+i));
					htShiphis.put("PICKQTY",   map.get("QTY_ORDER_"+i));
					htShiphis.put("REVERSEQTY","0");
					htShiphis.put(IDBConstants.DODET_UNITPRICE, price);
					htShiphis.put("EXPIRYDAT", "");
					htShiphis.put("STATUS","C");
					htShiphis.put(IDBConstants.CREATED_BY ,map.get(IConstants.LOGIN_USER));
					htShiphis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
					//check non stock item
					
	                    if(nonstocktype.equals("Y"))	
						{
		                    if(nonstocktypeDesc.equalsIgnoreCase("discount")||nonstocktypeDesc.equalsIgnoreCase("Credit/Refund")){ 
		                       htDet.put(IDBConstants.DODET_UNITPRICE, "-"+price);
		                    }
			
					    }
												
					java.util.Date dt = new java.util.Date();
					SimpleDateFormat dfVisualDate = new SimpleDateFormat("dd/MM/yyyy");
					String today = dfVisualDate.format(dt);

					htDet.put(IDBConstants.TRAN_DATE, dateUtils	.getDateinyyyy_mm_dd(dateUtils.getDate()));
					_dOUtil.setmLogger(mLogger);
					//insert into multiremarks
				    result = _dOUtil.saveDoDetDetails(htDet);
				    if(result){
						Hashtable htRemarks = new Hashtable();
						htRemarks.put(IDBConstants.PLANT, plant);
						htRemarks.put(IDBConstants.DODET_DONUM, dono);
						htRemarks.put(IDBConstants.DODET_DOLNNO, strCount);
						htRemarks.put(IDBConstants.DODET_ITEM, item);
						htRemarks.put(IDBConstants.REMARKS,strUtils.InsertQuotes(det_remark));
						htRemarks.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
						htRemarks.put(IDBConstants.CREATED_BY,map.get(IConstants.LOGIN_USER));
						result = _dOUtil.saveDoMultiRemarks(htRemarks);
				    }
					
					if (result) {
						
						   //insert into dotransfer detail
						result = _DOTransferUtil.saveDoTransferDetDetails(htDet);
					
						 //insert into movement history
						Hashtable htRecvHis = new Hashtable();
					    htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put("DIRTYPE", TransactionConstants.OB_ADD_ITEM);
						htRecvHis.put(IDBConstants.CUSTOMER_CODE,  map.get(IConstants.CUSTOMER_CODE ));
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, strUtils.InsertQuotes(job_num));
						htRecvHis.put(IDBConstants.ITEM,  item);
						htRecvHis.put(IDBConstants.QTY, map.get("QTY_ORDER_"+i));
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, dono);
						htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
						if (result)
						{
							result=movHisDao.insertIntoMovHis(htRecvHis);
							//to update nextseq number in TBLCONTROL
							new TblControlUtil().updateTblControlSeqNo(plant,IConstants.OUTBOUND,"O",dono);
						}
							

						if (!result) {
							throw new Exception("Product not found");
						}	
						
				    }
					intDolnno=intDolnno+1;
					} catch (Exception e) { // end of try catch for loop  to save dodetail
						//insert into dodet_temp table
							Hashtable htDetTemp = new Hashtable();
							htDetTemp.put(IDBConstants.PLANT, plant);
							htDetTemp.put(IDBConstants.DODET_DONUM, dono);
							htDetTemp.put(IDBConstants.DODET_ITEM, item);
							htDetTemp.put("ITEMDESC", strUtils.InsertQuotes(itemDesc));
							htDetTemp.put("ITEMDETAILDESC", strUtils.InsertQuotes(itemDetailDesc));
							htDetTemp.put(IDBConstants.DODET_QTYOR,  map.get("QTY_ORDER_" + i));
							htDetTemp.put("UNITMO", strUtils.InsertQuotes(unitmo));
							price=(String)map.get("UNITPRICE_" + i);
							htDetTemp.put("LISTPRICE", price);
							htDetTemp.put(IDBConstants.DODET_UNITPRICE, price);
							htDetTemp.put(IDBConstants.REMARKS, strUtils.InsertQuotes(det_remark));
							htDetTemp.put(IDBConstants.CREATED_BY,map.get(IConstants.LOGIN_USER));
							htDetTemp.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
							result = _dOUtil.saveDoDetTempDetails(htDetTemp);
						    //insert into dodet_temp table end
						//insert into movhis
							Hashtable htRecvHis = new Hashtable();
							//remark1=(String)map.get("REMARK");
							//custName=(String)map.get(IConstants.CUSTOMER_NAME);
							htRecvHis.clear();
							htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
							htRecvHis.put("DIRTYPE", "REJECTED_OB_ITEM");
							htRecvHis.put("CUSTNO", map.get(IConstants.CUSTOMER_CODE));
							htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, dono);
							htRecvHis.put(IDBConstants.ITEM, item);
							htRecvHis.put(IDBConstants.CREATED_BY,map.get(IConstants.LOGIN_USER));
							htRecvHis.put("MOVTID", "");
							htRecvHis.put("RECID", "");
							htRecvHis.put(IDBConstants.REMARKS, strUtils.InsertQuotes(det_remark));
						   	htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
							htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
							result = movHisDao.insertIntoMovHis(htRecvHis);
						continue;
				 }
					
				 }// end of for loop
				
			}else// dodet else part 
			{
				if (!result) {
					throw new Exception("Error in adding Product");
				}	
			}
			
			}if(map.get("PROCESSING_MODE").equals("Edit")){// end of to check processing mode Create
				result= processEditAddProduct(map);
			}// end of to check processing mode Edit
			return result;
		} catch (Exception e) {
			throw e;
			/*if(e.getMessage().equalsIgnoreCase("Outbound order created already"))
			{
				processno=dono;
				dono = _TblControlDAO.getNextOrder(plant,user_id,IConstants.OUTBOUND);
			}
			else
			{
				throw e;
			}*/
		 }


	}


	@SuppressWarnings("unchecked")
	private boolean processEditAddProduct(Map map) throws Exception {
	
		Boolean result = false;
		try {
			
			DoHdrDAO _DOHdrDAO = new DoHdrDAO();
			DoDetDAO _DODetDAO = new DoDetDAO();
			DOUtil _dOUtil = new DOUtil();
			DOTransferUtil _DOTransferUtil  = new DOTransferUtil();
			ShipHisDAO shipdao = new ShipHisDAO();
			MovHisDAO movHisDao = new MovHisDAO();
			StrUtils strUtils = new StrUtils();
			DODET _DODET=new DODET();
			Boolean dotransferhdr=false;
			String plant="",queryUpdateInv="",strPersonincharge="",remark1="",custName,price="",dono="";
			int intQty = 1;
			//DOHDER hastable;
			Hashtable hrHdr = new Hashtable();
			hrHdr.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			hrHdr.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			
			   String strCount="",item="",itemDesc="",itemDetailDesc="",unitmo="",job_num="",customer_name="",det_remark="";
				Integer itemCount = new Integer(StrUtils.fString((String) map.get("ITEM_COUNTS")).trim());
				
				for (int i = 0; i < itemCount; i++) {
					try{
					Hashtable htDet = new Hashtable();
					Hashtable htShiphis = new Hashtable();
					strCount=Integer.toString(i+1); //to save line number for DODET and SHIPHIS
					plant=(String)map.get(IConstants.PLANT);
					dono= (String)map.get(IConstants.DONO);
					item=(String)map.get("ITEM_" + i);
					itemDetailDesc=(String)map.get("ITEM_DETAIL_DESC" + i);
					job_num=(String)map.get("JOB_NUM");
                    unitmo=(String)map.get("UNITMO_"+ i);
                    det_remark=(String)map.get("DETREMARK_"+ i);
                    customer_name=(String)map.get(IConstants.CUSTOMER_NAME);
                  
                    //check valid product
                    ItemMstUtil itemMstUtil = new ItemMstUtil();
					itemMstUtil.setmLogger(mLogger);
					String itemItem = itemMstUtil.isPDAAddProductValidAlternateItemInItemmst(plant, item);
					if(itemItem != null || !itemItem.equals("")){
						item = itemItem;
					}else{
													
						throw new Exception("Product not found....!");
					}
					Map mPrddet = new ItemMstDAO().getProductNonStockDetails(plant,item);
                    String nonstocktype= StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
                    String nonstocktypeDesc= StrUtils.fString((String) mPrddet.get("STOCKTYPEDESC"));
                    itemDesc =StrUtils.fString((String) mPrddet.get("ITEMDESC"));	
                    htDet.put(IDBConstants.PLANT, plant);
					htDet.put(IDBConstants.DODET_DONUM, dono);
					htDet.put(IDBConstants.DODET_ITEM, item);
					htDet.put("ITEMDESC", strUtils.InsertQuotes(itemDesc));
					htDet.put("UNITMO", strUtils.InsertQuotes(unitmo));
					htDet.put(IDBConstants.DODET_ITEM_DESC,strUtils.InsertQuotes(itemDesc));
					htDet.put(IDBConstants.DODET_ITEM_DESC,strUtils.InsertQuotes(itemDesc));
					htDet.put(IDBConstants.DODET_JOB_NUM, strUtils.InsertQuotes(job_num));
					htDet.put(IDBConstants.DODET_QTYOR,  map.get("QTY_ORDER_" + i));
					htDet.put("QTYPICK", "0");
					htDet.put(IDBConstants.DODET_QTYIS, "0");
					//requestData.put("UNITPRICE_" + i, StrUtils.removeFormat(StrUtils.fString(request.getParameter("UNITPRICE_" + i)).trim()));
					//price= StrUtils.removeFormat((String)map.get("UNITPIRCE_" + i));
					price=(String)map.get("UNITPRICE_" + i);
			    	price = new DOUtil().getConvertedUnitCostToLocalCurrency(plant,dono,price) ;
					htDet.put(IDBConstants.DODET_UNITPRICE, price);
					htDet.put(IDBConstants.DODET_QTYIS, "0");
				    //htDet.put(IDBConstants.DODET_COMMENT1,strUtils.InsertQuotes(det_remark));
					htDet.put(IDBConstants.DODET_DOLNNO, _DODET.getNextLineNo(dono,plant));
					htDet.put(IDBConstants.DODET_LNSTATUS, "N");
			    	htDet.put(IDBConstants.DODET_PICKSTATUS, "N");
					String CURRENCYUSEQT = new DOUtil().getCurrencyUseQT(plant, dono);
					htDet.put(IDBConstants.CURRENCYUSEQT, CURRENCYUSEQT);
					htDet.put(IDBConstants.CREATED_BY,map.get(IConstants.LOGIN_USER));
					htDet.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
					
					//shiphis hastable
					htShiphis.put(IDBConstants.PLANT, plant);
					htShiphis.put(IDBConstants.DODET_DONUM, dono);
					htShiphis.put("DOLNO",_DODET.getNextLineNo(dono,plant));
					htShiphis.put(IDBConstants.CUSTOMER_NAME, strUtils.InsertQuotes(customer_name));
					htShiphis.put(IDBConstants.ITEM, item);
     				htShiphis.put(IDBConstants.ITEM_DESC, strUtils.InsertQuotes(itemDesc));//strUtils.InsertQuotes(map.get(IConstants.ITEM_DESC )));
					htShiphis.put("BATCH", "NOBATCH");
					htShiphis.put(IDBConstants.LOC, "");
					htShiphis.put("ORDQTY",  map.get("QTY_ORDER_"+i));
					htShiphis.put("PICKQTY",   map.get("QTY_ORDER_"+i));
					htShiphis.put("REVERSEQTY","0");
					htShiphis.put(IDBConstants.DODET_UNITPRICE, price);
					htShiphis.put("EXPIRYDAT", "");
					htShiphis.put("STATUS","C");
					htShiphis.put(IDBConstants.CREATED_BY ,map.get(IConstants.LOGIN_USER));
					htShiphis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
					
					
					//check non stock item
	                    if(nonstocktype.equals("Y"))	
						{
						   if(nonstocktypeDesc.equalsIgnoreCase("discount")||nonstocktypeDesc.equalsIgnoreCase("Credit/Refund")){ 
		                       htDet.put(IDBConstants.DODET_UNITPRICE, "-"+price);
		                     
		                    }
			
					    }
						    
						
					java.util.Date dt = new java.util.Date();
					SimpleDateFormat dfVisualDate = new SimpleDateFormat("dd/MM/yyyy");
					String today = dfVisualDate.format(dt);

					htDet.put(IDBConstants.TRAN_DATE, dateUtils
							.getDateinyyyy_mm_dd(dateUtils.getDate()));
					_dOUtil.setmLogger(mLogger);
									   
				    // insert into multi remarks
				  
					 Hashtable htRemarks = new Hashtable();
					 htRemarks.put(IDBConstants.PLANT, plant);
					 htRemarks.put(IDBConstants.DODET_DONUM, dono);
					 htRemarks.put(IDBConstants.DODET_DOLNNO, _DODET.getNextLineNo(dono,plant));
					 htRemarks.put(IDBConstants.DODET_ITEM, item);
					 htRemarks.put(IDBConstants.REMARKS,strUtils.InsertQuotes(det_remark));
					 htRemarks.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
					 htRemarks.put(IDBConstants.CREATED_BY,map.get(IConstants.LOGIN_USER));
										   
					//insert into DODET
				    result = _dOUtil.saveDoDetDetails(htDet);
				    if(result){
				    	result = _dOUtil.saveDoMultiRemarks(htRemarks);
				    }
				
	              //delete & insert dodet multi remarks end
					if (result) {
						//insert into ship history
					
					     //insert into dotransfer detail
						 result = _DOTransferUtil.saveDoTransferDetDetails(htDet);
						
						 //insert into movement history
						Hashtable htRecvHis = new Hashtable();
					    htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put("DIRTYPE", TransactionConstants.OB_ADD_ITEM);
						htRecvHis.put(IDBConstants.CUSTOMER_CODE,  map.get(IConstants.CUSTOMER_CODE ));
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, "");
						htRecvHis.put(IDBConstants.ITEM,  item);
						htRecvHis.put(IDBConstants.QTY, map.get("QTY_ORDER_"+i));
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, dono);
						htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
						if (result)
						{
							result=movHisDao.insertIntoMovHis(htRecvHis);
							//to update nextseq number in TBLCONTROL
							//new TblControlUtil().updateTblControlSeqNo(plant,IConstants.OUTBOUND,"O",dono);
						}
							

						if (!result) {
							throw new Exception("Error in adding Product");
						}						
				    }
					
					} catch (Exception e) { // end of for loop try catch
						//insert into dodet_temp table
						Hashtable htDetTemp = new Hashtable();
						htDetTemp.put(IDBConstants.PLANT, plant);
						htDetTemp.put(IDBConstants.DODET_DONUM, dono);
						htDetTemp.put(IDBConstants.DODET_ITEM, item);
						htDetTemp.put("ITEMDESC", strUtils.InsertQuotes(itemDesc));
						htDetTemp.put("ITEMDETAILDESC", strUtils.InsertQuotes(itemDetailDesc));
						htDetTemp.put(IDBConstants.DODET_QTYOR,  map.get("QTY_ORDER_" + i));
						htDetTemp.put("UNITMO", strUtils.InsertQuotes(unitmo));
						price=(String)map.get("UNITPRICE_" + i);
						htDetTemp.put("LISTPRICE", price);
						htDetTemp.put(IDBConstants.DODET_UNITPRICE, price);
						htDetTemp.put(IDBConstants.REMARKS, strUtils.InsertQuotes(det_remark));
						htDetTemp.put(IDBConstants.CREATED_BY,map.get(IConstants.LOGIN_USER));
						htDetTemp.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
						result = _dOUtil.saveDoDetTempDetails(htDetTemp);
					//insert into dodet_temp table end
						Hashtable htRecvHis = new Hashtable();
						//remark1=(String)map.get("REMARK");
						//custName=(String)map.get(IConstants.CUSTOMER_NAME);
						htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
						htRecvHis.put("DIRTYPE", "REJECTED_OB_ITEM");
						htRecvHis.put("CUSTNO", map.get(IConstants.CUSTOMER_CODE));
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, dono);
						htRecvHis.put(IDBConstants.ITEM, item);
						htRecvHis.put(IDBConstants.CREATED_BY,map.get(IConstants.LOGIN_USER));
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						htRecvHis.put(IDBConstants.REMARKS, strUtils.InsertQuotes(det_remark));
					   	htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
						result = movHisDao.insertIntoMovHis(htRecvHis);
					continue;
				 }
					continue;
			  }// end of for loop
				
			return result;
			
		} catch (Exception e) {
			throw e;
		 }
	

	}
	
	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		this.mLogger.setLoggerConstans(dataForLogging);
	}

}
