package com.track.tran;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.DoTransferDetDAO;
import com.track.dao.DoTransferHdrDAO;
import com.track.dao.LoanDetDAO;
import com.track.dao.LoanHdrDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.ToDetDAO;
import com.track.dao.ToHdrDAO;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class WmsOrderClose implements WmsTran, IMLogger {
	private boolean printLog = MLoggerConstant.WmsOrderDetailsClose_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.WmsOrderDetailsClose_PRINTPLANTMASTERINFO;
	DateUtils dateUtils = null;
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

	public WmsOrderClose() {
		dateUtils = new DateUtils();
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		try {
                List list = new ArrayList();
                String listDet ="";
                String moduleName= (String) m.get("MODULE_NAME");
                if(moduleName.equalsIgnoreCase("INBOUND")){
                    String lines =(String)m.get("ORDERS");
                    StringTokenizer parser = new StringTokenizer(lines,",");
                    while (parser.hasMoreTokens()) {
                    list.add(parser.nextToken());                     
                    }

                    for (int i =0; i<list.size(); i++){
                        listDet=(String)list.get(i);
                        m.put(IConstants.ORDERNO,listDet);
                        flag =  processCloseInBoundOrder(m);
                    }
                    
                }else if(moduleName.equalsIgnoreCase("OUTBOUND")){
                    String lines =(String)m.get("ORDERS");
                    StringTokenizer parser = new StringTokenizer(lines,",");
                    while (parser.hasMoreTokens()) {
                    list.add(parser.nextToken());                     
                    }

                    for (int i =0; i<list.size(); i++){
                        listDet=(String)list.get(i);
                        m.put(IConstants.ORDERNO,listDet);
                        flag =  processCloseOutBoundOrder(m);
                    }
                }
                else if(moduleName.equalsIgnoreCase("TRANSFER")){
                    String lines =(String)m.get("ORDERS");
                    StringTokenizer parser = new StringTokenizer(lines,",");
                    while (parser.hasMoreTokens()) {
                    list.add(parser.nextToken());                     
                    }

                    for (int i =0; i<list.size(); i++){
                        listDet=(String)list.get(i);
                        m.put(IConstants.ORDERNO,listDet);
                        flag =  processCloseTransferOrder(m);
                    }
                  
                }
                else if(moduleName.equalsIgnoreCase("LOAN")){
                  
                    String lines =(String)m.get("ORDERS");
                    StringTokenizer parser = new StringTokenizer(lines,",");
                    while (parser.hasMoreTokens()) {
                    list.add(parser.nextToken());                     
                    }

                    for (int i =0; i<list.size(); i++){
                        listDet=(String)list.get(i);
                        m.put(IConstants.ORDERNO,listDet);
                        flag = processCloseLoanOrder(m);
                    }
                    
                } /*else if(moduleName.equalsIgnoreCase("WORKORDER")){
                  
                    String lines =(String)m.get("ORDERS");
                    StringTokenizer parser = new StringTokenizer(lines,",");
                    while (parser.hasMoreTokens()) {
                    list.add(parser.nextToken());                     
                    }

                    for (int i =0; i<list.size(); i++){
                        listDet=(String)list.get(i);
                        m.put(IConstants.ORDERNO,listDet);
                        flag = processCloseWorkOrder(m);
                    }
                    
                }*/
                

                

               

		
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}
    
    public boolean processCloseInBoundOrder(Map map) throws Exception {

            boolean flag = false;
            try {
            
                    PoDetDAO _poDetDAO = new PoDetDAO();
                    _poDetDAO.setmLogger(mLogger);
                    
                    PoHdrDAO _poHdrDAO = new PoHdrDAO();
                    _poHdrDAO.setmLogger(mLogger);
                
                    String queryDet = "",queryHdr="";
                    Hashtable htDet = new Hashtable();
                    
                    htDet.put("PLANT", map.get(IConstants.PLANT));
                    htDet.put(IConstants.PODET_PONUM, map.get(IConstants.ORDERNO));
                    
                  if(_poDetDAO.isExisit(htDet,"")){
                     queryDet = "set LNSTAT='C'";
                
                    flag = _poDetDAO.update(queryDet,htDet,"");
                    
                    if (!flag) {
                            throw new Exception("Failed to Close the Details for Order "+ (String) map.get(IConstants.ORDERNO) );
                    }
                  }
                   queryHdr = "set STATUS='C',ORDER_STATUS='FORCE CLOSE' "; //resvi
                   flag = _poHdrDAO.updatePO(queryHdr, htDet, "");
                
                    if (!flag) {
                            throw new Exception("Failed to Close Order "+ (String) map.get(IConstants.ORDERNO) );
                    }else{
                        flag = processMovHis(map);
                    }

            
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e ;
            }
            return flag;
    }
           
    
    public boolean processCloseOutBoundOrder(Map map) throws Exception {

            boolean flag = false;
            try {
            
                    DoDetDAO _doDetDAO = new DoDetDAO();
                    _doDetDAO.setmLogger(mLogger);
                    
                    DoHdrDAO _DoHdrDAO = new DoHdrDAO();
                    _DoHdrDAO.setmLogger(mLogger);
                    
                    DoTransferDetDAO _DoTransferDetDAO =new DoTransferDetDAO();
                    _DoTransferDetDAO.setmLogger(mLogger);
                    
                    DoTransferHdrDAO _DoTransferHdrDAO = new DoTransferHdrDAO();
                    _DoTransferHdrDAO.setmLogger(mLogger);
                
                    String queryDet = "",queryHdr="";
                    Hashtable htDet = new Hashtable();
                    htDet.put("PLANT", map.get(IConstants.PLANT));
                    htDet.put(IConstants.DODET_DONUM, map.get(IConstants.ORDERNO));
                    String extracond =" ISNULL(QTYPICK,0) > ISNULL(QTYIS,0) ";
                    boolean isItemsFound =_doDetDAO.isExisit(htDet,extracond);
                    if(isItemsFound){
                    throw new Exception("Order No : "+map.get(IConstants.ORDERNO)+" Already picked,Cannot proceed to Close");
                    }else{
                    try{
                        queryDet = "set LNSTAT='C',PickStatus='C' ";
                        flag=_doDetDAO.update(queryDet,htDet,"");
                        if(flag){
                            flag=_DoTransferDetDAO.update(queryDet,htDet,"");
                        }
                        }catch(Exception e){
                            flag=true;
                        }
                    if (!flag) {
                            throw new Exception("Failed to Close the Details for Order "+ (String) map.get(IConstants.ORDERNO) );
                    }
                    }
                  
                   queryHdr = "set STATUS='C',pickStaus='C',ORDER_STATUS='FORCE CLOSE' " ; //resvi
                   flag = _DoHdrDAO.update(queryHdr, htDet, "");
                   if(flag){
                      flag=_DoTransferHdrDAO.update(queryHdr, htDet, "");
                   }
                    
                   
                    if (!flag) {
                            throw new Exception("Failed to Close Order "+ (String) map.get(IConstants.ORDERNO) );
                    }else{
                        flag = processMovHis(map);
                    }

            
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e ;
            }
            return flag;
    }
    
        
    public boolean processCloseTransferOrder(Map map) throws Exception {

            boolean flag = false;
            try {
            
                ToDetDAO _ToDetDAO = new ToDetDAO();
                ToHdrDAO _ToHdrDAO = new ToHdrDAO();
                _ToDetDAO.setmLogger(mLogger);
                _ToHdrDAO.setmLogger(mLogger);
                
                
                String queryDet = "",queryHdr="";
                    Hashtable htDet = new Hashtable();
                   
                    htDet.put("PLANT", map.get(IConstants.PLANT));
                    htDet.put(IConstants.TODET_TONUM, map.get(IConstants.ORDERNO));
                String extracond =" ISNULL(QTYPICK,0) > ISNULL(QTYRC,0) ";
                boolean isItemsFound =_ToDetDAO.isExisit(htDet,extracond);
                if(isItemsFound){
                    throw new Exception("Order No : "+map.get(IConstants.ORDERNO)+" Already picked,Cannot proceed to Close");
                }else{
                try{
                  
                   
                    queryDet = " SET  PICKSTATUS ='C',LNSTAT='C' ";
                    String extraCond = "";//" AND "+IConstants.LOANDET_ORDLNNO+" IN ("+map.get("LINES") +")";
                    flag = _ToDetDAO.update(queryDet, htDet, extraCond);
                    if (!flag) {
                            throw new Exception("Failed to Close the Details for Order "+ (String) map.get(IConstants.ORDERNO) );
                    }
                  }catch(Exception e){
                      flag=true;
                  }
                }
                   queryHdr = "set STATUS='C',PickStaus='C',ORDER_STATUS='FORCE CLOSE' "; //resvi
                            
                    flag = _ToHdrDAO.update(queryHdr, htDet, "");
                    if (!flag) {
                            throw new Exception("Failed to Close Order "+ (String) map.get(IConstants.ORDERNO) );
                    }else{
                        flag = processMovHis(map);
                    }

            
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e ;
            }
            return flag;
    }
        
    public boolean processCloseLoanOrder(Map map) throws Exception {

            boolean flag = false;
            try {
                    String queryloanDet = "",queryloanHdr="";
                    LoanDetDAO _loanDetDAO = new LoanDetDAO();
                   _loanDetDAO.setmLogger(mLogger);
                   
                    LoanHdrDAO _loanHdrDAO = new LoanHdrDAO();
                    _loanHdrDAO.setmLogger(mLogger);
                  
                    Hashtable htDet = new Hashtable();
                   
                    htDet.put("PLANT", map.get(IConstants.PLANT));
                    htDet.put(IConstants.LOANDET_ORDNO, map.get(IConstants.ORDERNO));
                    
                String extracond =" ISNULL(QTYIS,0) > ISNULL(QTYRC,0) ";
                boolean isItemsFound =_loanDetDAO.isExisit(htDet,extracond);
                if(isItemsFound){
                    throw new Exception("Order No : "+map.get(IConstants.ORDERNO)+" Already picked,Cannot proceed to Close");
                }else{
                try{
                  
                   
                    queryloanDet = " SET  PICKSTATUS ='C',RECVSTATUS='C',LNSTAT='C' ";
                    String extraCond = "";//" AND "+IConstants.LOANDET_ORDLNNO+" IN ("+map.get("LINES") +")";
                    flag = _loanDetDAO.update(queryloanDet, htDet, extraCond);
                    if (!flag) {
                            throw new Exception("Failed to Close the Details for Order "+ (String) map.get(IConstants.ORDERNO) );
                    }
                    }catch(Exception e){
                        flag=true;
                    }
                }
                    queryloanHdr = "set STATUS='C' ";
                            
                    flag = _loanHdrDAO.update(queryloanHdr, htDet, "");
                    if (!flag) {
                            throw new Exception("Failed to Close Order "+ (String) map.get(IConstants.ORDERNO) );
                    }else{
                        flag = processMovHis(map);
                    }

            
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e ;
            }
            return flag;
    }

    /*public boolean processCloseWorkOrder(Map map) throws Exception {

        boolean flag = false;
        try {
        
	        	WorkOrderDetDAO _woDetDAO = new WorkOrderDetDAO();
	        	_woDetDAO.setmLogger(mLogger);
                
                WorkOrderHdrDAO _woHdrDAO = new WorkOrderHdrDAO();
                _woHdrDAO.setmLogger(mLogger);
                
              
                String queryDet = "",queryHdr="";
                Hashtable htDet = new Hashtable();
                htDet.put("PLANT", map.get(IConstants.PLANT));
                htDet.put(IConstants.WODET_WONUM, map.get(IConstants.ORDERNO));
              
                if(_woDetDAO.isExisit(htDet,"")){
                    queryDet = "set WSTATUS ='C',LNSTAT='C'";
               
                   flag = _woDetDAO.update(queryDet,htDet,"");
                   
                   if (!flag) {
                           throw new Exception("Failed to Close the Details for Order "+ (String) map.get(IConstants.ORDERNO) );
                   }
                 }
                  queryHdr = "set STATUS='C' ";
                  flag = _woHdrDAO.update(queryHdr, htDet, "");
               
                   if (!flag) {
                           throw new Exception("Failed to Close Order "+ (String) map.get(IConstants.ORDERNO) );
                   }else{
                       flag = processMovHis(map);
                   }

        
        } catch (Exception e) {
                this.mLogger.exception(this.printLog, "", e);
                throw e ;
        }
        return flag;
}*/


	public boolean processMovHis(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		
		try {
			Hashtable htRecvHis = new Hashtable();
			htRecvHis.clear();
			htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvHis.put("DIRTYPE", TransactionConstants.ORDER_CLS);
			htRecvHis.put(IDBConstants.ITEM, "");
			htRecvHis.put("MOVTID", "");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.ORDERNO));
		        htRecvHis.put(IDBConstants.REMARKS, map.get(IConstants.REMARKS));
			htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
			htRecvHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
                        flag = movHisDao.insertIntoMovHis(htRecvHis);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}
}

