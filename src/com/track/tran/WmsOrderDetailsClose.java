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

public class WmsOrderDetailsClose implements WmsTran, IMLogger {
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

	public WmsOrderDetailsClose() {
		dateUtils = new DateUtils();
	}

	public boolean processWmsTran(Map m) throws Exception {
		boolean flag = false;
		try {
	        List list = new ArrayList();
		List listline = new ArrayList();
	        String listDet ="";
                String moduleName= (String) m.get("MODULE_NAME");
                if(moduleName.equalsIgnoreCase("INBOUND")){
                
                    String lines =(String)m.get("ORDERLINES");
                    StringTokenizer parser = new StringTokenizer(lines,",");
                    while (parser.hasMoreTokens()) {
                    list.add(parser.nextToken());                     
                    }
                    for (int i =0; i<list.size(); i++){
                     listDet=(String)list.get(i);
                     StringTokenizer parser1 = new StringTokenizer(listDet,"||");
                    // Doubly-linked list
                      listline.clear();
                      while (parser1.hasMoreTokens()) {
                      listline.add(parser1.nextToken());                     
                      }
                       
                        m.put(IConstants.ORDERLNO,(String)listline.get(0));
                        m.put(IConstants.ITEM,(String)listline.get(1));
                        flag =  processCloseInBoundOrderItems(m);
                    }
                    
                    if(flag){
                    flag =  processCloseInBoundOrder(m);
                    }
                    
                }else if(moduleName.equalsIgnoreCase("OUTBOUND")){
                
                    String lines =(String)m.get("ORDERLINES");
                    StringTokenizer parser = new StringTokenizer(lines,",");
                    while (parser.hasMoreTokens()) {
                    list.add(parser.nextToken());                     
                    }
                    for (int i =0; i<list.size(); i++){
                     listDet=(String)list.get(i);
                     StringTokenizer parser1 = new StringTokenizer(listDet,"||");
                    // Doubly-linked list
                      listline.clear();
                      while (parser1.hasMoreTokens()) {
                      listline.add(parser1.nextToken());                     
                      }
                       
                        m.put(IConstants.ORDERLNO,(String)listline.get(0));
                        m.put(IConstants.ITEM,(String)listline.get(1));
                        flag =  processCloseOutBoundOrderItems(m);
                    }
                    
                    if(flag){
                    flag =  processCloseOutBoundOrder(m);
                    }
                }
                else if(moduleName.equalsIgnoreCase("TRANSFER")){
                
                    String lines =(String)m.get("ORDERLINES");
                    StringTokenizer parser = new StringTokenizer(lines,",");
                    while (parser.hasMoreTokens()) {
                    list.add(parser.nextToken());                     
                    }
                    for (int i =0; i<list.size(); i++){
                     listDet=(String)list.get(i);
                     StringTokenizer parser1 = new StringTokenizer(listDet,"||");
                    // Doubly-linked list
                      listline.clear();
                      while (parser1.hasMoreTokens()) {
                      listline.add(parser1.nextToken());                     
                      }
                       
                        m.put(IConstants.ORDERLNO,(String)listline.get(0));
                        m.put(IConstants.ITEM,(String)listline.get(1));
                        flag =  processCloseTransferOrderItems(m);
                    }
                    
                    if(flag){
                    flag =  processCloseTransferOrder(m);
                    }
                  
                }
                else if(moduleName.equalsIgnoreCase("LOAN")){
                    String lines =(String)m.get("ORDERLINES");
                    StringTokenizer parser = new StringTokenizer(lines,",");
                    while (parser.hasMoreTokens()) {
                    list.add(parser.nextToken());                     
                    }
                    for (int i =0; i<list.size(); i++){
                     listDet=(String)list.get(i);
                     StringTokenizer parser1 = new StringTokenizer(listDet,"||");
                    // Doubly-linked list
                      listline.clear();
                      while (parser1.hasMoreTokens()) {
                      listline.add(parser1.nextToken());                     
                      }
                       
                        m.put(IConstants.ORDERLNO,(String)listline.get(0));
                        m.put(IConstants.ITEM,(String)listline.get(1));
                        flag = processCloseLoanOrderItems(m);
                    }
                    
                    if(flag){
                    flag = processCloseLoanOrder(m);
                    }
                } /*if(moduleName.equalsIgnoreCase("WORKORDER")){
                
                    String lines =(String)m.get("ORDERLINES");
                    StringTokenizer parser = new StringTokenizer(lines,",");
                    while (parser.hasMoreTokens()) {
                    list.add(parser.nextToken());                     
                    }
                    for (int i =0; i<list.size(); i++){
                     listDet=(String)list.get(i);
                     StringTokenizer parser1 = new StringTokenizer(listDet,"||");
                    // Doubly-linked list
                      listline.clear();
                      while (parser1.hasMoreTokens()) {
                      listline.add(parser1.nextToken());                     
                      }
                       
                        m.put(IConstants.ORDERLNO,(String)listline.get(0));
                        m.put(IConstants.ITEM,(String)listline.get(1));
                        flag =  processCloseWorkOrderItems(m);
                    }
                    
                    if(flag){
                    flag =  processCloseWorkOrder(m);
                    }
                    
                }*/
                

                

		
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}
    
    public boolean processCloseInBoundOrderItems(Map map) throws Exception {

            boolean flag = false;
            try {
            
                    PoDetDAO _poDetDAO = new PoDetDAO();
                    _poDetDAO.setmLogger(mLogger);
                    
                    PoHdrDAO _poHdrDAO = new PoHdrDAO();
                    _poHdrDAO.setmLogger(mLogger);
                
                  
                        Hashtable htDet = new Hashtable();
                        htDet.put("PLANT", map.get(IConstants.PLANT));
                        htDet.put(IConstants.PODET_PONUM, map.get(IConstants.ORDERNO));
                        htDet.put(IConstants.PODET_POLNNO, map.get(IConstants.ORDERLNO));
                        htDet.put(IConstants.PODET_ITEM, map.get(IConstants.ITEM));
                    
                        
                         String queryDet = "";
                         queryDet = "set LNSTAT='C'";
                         flag = _poDetDAO.update(queryDet,htDet,"");
                        
                        if (!flag) {
                                throw new Exception("Failed to Close the Details for Order Line No "+ (String) map.get(IConstants.ORDERLNO) );
                        }else{
                            flag= processMovHisOrderItems(map);
                        }
                    
             
            
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e ;
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
                    String queryHdr="";
                  
                    Hashtable htCondiPoHdr = new Hashtable();
                    htCondiPoHdr.put("PLANT", map.get("PLANT"));
                    htCondiPoHdr.put(IConstants.PODET_PONUM, map.get(IConstants.ORDERNO));
    
                    flag = _poDetDAO.isExisit(htCondiPoHdr, "lnstat in ('O','N')");
         
                    if (!flag){
                            queryHdr = "set STATUS='C' ";
                            flag = _poHdrDAO.updatePO(queryHdr, htCondiPoHdr, "");
                            if(flag){
                               flag =  processMovHisOrder(map);
                            }
                
                    }else{
                        flag=true;  
                    }
            
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e ;
            }
            return flag;
    }
             
    public boolean processCloseOutBoundOrderItems(Map map) throws Exception {

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
                
                
                    Hashtable htDet = new Hashtable();
                    htDet.put("PLANT", map.get(IConstants.PLANT));
                    htDet.put(IConstants.DODET_DONUM, map.get(IConstants.ORDERNO));
                    htDet.put(IConstants.DODET_DOLNNO, map.get(IConstants.ORDERLNO));
                    
                    boolean isExitsRec = _doDetDAO.isExisit(htDet,"  ISNULL(QTYPICK,0) > ISNULL(QTYIS,0) ");
                   
                    if(isExitsRec){
                        throw new Exception("Order Line No :"+ map.get(IConstants.ORDERLNO)+" Already picked,Cannot proceed to Close");
                    }
                    String queryDet = "",queryHdr="";
                    queryDet = "set LNSTAT='C',PickStatus='C' ";
                
                    flag = _doDetDAO.update(queryDet,htDet,"");
                    if(flag){
                        flag=_DoTransferDetDAO.update(queryDet,htDet,"");
                    }
                    if (!flag) {
                            throw new Exception("Failed to Close the Details for Order "+ (String) map.get(IConstants.ORDERLNO) );
                    }else{
                        flag= processMovHisOrderItems(map);
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
                
                    Hashtable htDet = new Hashtable();
                    htDet.put("PLANT", map.get(IConstants.PLANT));
                    htDet.put(IConstants.DODET_DONUM, map.get(IConstants.ORDERNO));
                    
                    String queryDet = "",queryHdr="";

                    flag = _doDetDAO.isExisit(htDet, "pickStatus in ('O','N') ");
                    if (!flag){
                            queryHdr = "set PickStaus='C',STATUS='C' ";
                            flag = _DoHdrDAO.update(queryHdr, htDet, "");
                       if(flag){
                            flag=_DoTransferHdrDAO.update(queryHdr, htDet, "");
                       }
                        if(flag){
                           flag =  processMovHisOrder(map);
                        }
                    } else{
                        flag=true;  
                    }
                
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e ;
            }
            return flag;
    }
    
   
    public boolean processCloseTransferOrderItems(Map map) throws Exception {

            boolean flag = false;
            try {
            
                    ToDetDAO _ToDetDAO = new ToDetDAO();
                    ToHdrDAO _ToHdrDAO = new ToHdrDAO();
                    _ToDetDAO.setmLogger(mLogger);
                    _ToHdrDAO.setmLogger(mLogger);
                
                 
                    Hashtable htDet = new Hashtable();
                   
                    htDet.put("PLANT", map.get(IConstants.PLANT));
                    htDet.put(IConstants.TODET_TONUM, map.get(IConstants.ORDERNO));
                    htDet.put(IConstants.TODET_TOLNNO, map.get(IConstants.ORDERLNO));
                     
                    boolean isExitsRec = _ToDetDAO.isExisit(htDet,"  ISNULL(QTYPICK,0) > ISNULL(QTYRC,0) ");
                        
                    if(isExitsRec){
                        throw new Exception("Order Line No :"+ map.get(IConstants.ORDERLNO)+" Already picked,Cannot proceed to Close");
                    }
                    String queryDet = "",queryHdr="";
                   
                    queryDet = " SET  PICKSTATUS ='C',LNSTAT='C' ";
                    String extraCond = "";//" AND "+IConstants.LOANDET_ORDLNNO+" IN ("+map.get("LINES") +")";
                    flag = _ToDetDAO.update(queryDet, htDet, extraCond);
                    if (!flag) {
                            throw new Exception("Failed to Close the Details for Order Line No :"+ (String) map.get(IConstants.ORDERLNO) );
                    }else{
                        flag= processMovHisOrderItems(map);
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
                  
                    Hashtable htDet = new Hashtable();
                   
                    htDet.put("PLANT", map.get(IConstants.PLANT));
                    htDet.put(IConstants.TODET_TONUM, map.get(IConstants.ORDERNO));
                    String queryDet = "",queryHdr="";
                    flag = _ToDetDAO.isExisit(htDet, "lnstat in ('O','N')");
                
                    if (!flag){
                            queryHdr = "set STATUS='C',PickStaus='C' ";
                           
                    flag = _ToHdrDAO.update(queryHdr, htDet, "");
                        if(flag){
                             flag =  processMovHisOrder(map);
                        }
                    }else{
                        flag=true;  
                    }


            
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e ;
            }
            return flag;
    }
    
    
    public boolean processCloseLoanOrderItems(Map map) throws Exception {

            boolean flag = false;
            try {
                    LoanDetDAO _loanDetDAO = new LoanDetDAO();
                   _loanDetDAO.setmLogger(mLogger);
                   
                    LoanHdrDAO _loanHdrDAO = new LoanHdrDAO();
                    _loanHdrDAO.setmLogger(mLogger);
                  
                    Hashtable htDet = new Hashtable();
                   
                    htDet.put("PLANT", map.get(IConstants.PLANT));
                    htDet.put(IConstants.LOANDET_ORDNO, map.get(IConstants.ORDERNO));
                    htDet.put(IConstants.LOANDET_ORDLNNO, map.get(IConstants.ORDERLNO));
                    
                    boolean isExitsRec = _loanDetDAO.isExisit(htDet,"  ISNULL(QTYIS,0) > ISNULL(QTYRC,0) ");
                    
                    if(isExitsRec){
                        throw new Exception("Order Line No :"+ map.get(IConstants.ORDERLNO)+" Already picked,Cannot proceed to Close");
                    }
                    String queryloanDet = "",queryloanHdr="";
                   
                    queryloanDet = " SET  PICKSTATUS ='C',RECVSTATUS='C',LNSTAT='C' ";
                    String extraCond = "";//" AND "+IConstants.LOANDET_ORDLNNO+" IN ("+map.get("LINES") +")";
                    flag = _loanDetDAO.update(queryloanDet, htDet, extraCond);
                    if (!flag) {
                            throw new Exception("Failed to Close the Details for Order Line No : "+ (String) map.get(IConstants.ORDERLNO) );
                    }else{
                       flag= processMovHisOrderItems(map);
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
                    LoanDetDAO _loanDetDAO = new LoanDetDAO();
                   _loanDetDAO.setmLogger(mLogger);
                   
                    LoanHdrDAO _loanHdrDAO = new LoanHdrDAO();
                    _loanHdrDAO.setmLogger(mLogger);
                  
                    Hashtable htDet = new Hashtable();
                   
                    htDet.put("PLANT", map.get(IConstants.PLANT));
                    htDet.put(IConstants.LOANDET_ORDNO, map.get(IConstants.ORDERNO));
                    String queryloanDet = "",queryloanHdr="";
                   
                   flag = _loanDetDAO .isExisit( htDet, " (isnull(pickStatus,'') in ('','O','N') or isnull(RECVSTATUS,'') in ('','O','N'))");

                    if (!flag){
                            queryloanHdr = "set STATUS='C' ";
                           flag = _loanHdrDAO.update(queryloanHdr, htDet, "");
                           if(flag){
                               flag =  processMovHisOrder(map);
                           }
                    }else{
                        flag=true;  
                    }
                   

            
            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e ;
            }
            return flag;
    }
    
    /*public boolean processCloseWorkOrderItems(Map map) throws Exception {

        boolean flag = false;
        try {
        
        	WorkOrderDetDAO _woDetDAO = new WorkOrderDetDAO();
            _woDetDAO.setmLogger(mLogger);
            
            WorkOrderHdrDAO _woHdrDAO = new WorkOrderHdrDAO();
            _woHdrDAO.setmLogger(mLogger);
            
              
                    Hashtable htDet = new Hashtable();
                    htDet.put("PLANT", map.get(IConstants.PLANT));
                    htDet.put(IConstants.WODET_WONUM, map.get(IConstants.ORDERNO));
                    htDet.put(IConstants.WODET_WOLNNO, map.get(IConstants.ORDERLNO));
                    htDet.put(IConstants.WODET_ITEM, map.get(IConstants.ITEM));
                
                    
                     String queryDet = "";
                     queryDet = "set WSTATUS ='C',LNSTAT='C'";
                     flag = _woDetDAO.update(queryDet,htDet,"");
                    
                    if (!flag) {
                            throw new Exception("Failed to Close the Details for Order Line No "+ (String) map.get(IConstants.ORDERLNO) );
                    }else{
                        flag= processMovHisOrderItems(map);
                    }
                
         
        
        } catch (Exception e) {
                this.mLogger.exception(this.printLog, "", e);
                throw e ;
        }
        return flag;
}


public boolean processCloseWorkOrder(Map map) throws Exception {

        boolean flag = false;
        try {
        
                WorkOrderDetDAO _woDetDAO = new WorkOrderDetDAO();
                _woDetDAO.setmLogger(mLogger);
                
                WorkOrderHdrDAO _woHdrDAO = new WorkOrderHdrDAO();
                _woHdrDAO.setmLogger(mLogger);
                String queryHdr="";
              
                Hashtable htCondiwoHdr = new Hashtable();
                htCondiwoHdr.put("PLANT", map.get("PLANT"));
                htCondiwoHdr.put(IConstants.WODET_WONUM, map.get(IConstants.ORDERNO));

                flag = _woDetDAO.isExisit(htCondiwoHdr, "lnstat in ('O','N')");
     
                if (!flag){
                        queryHdr = "set STATUS='C' ";
                        flag = _woHdrDAO.update(queryHdr, htCondiwoHdr, "");
                        if(flag){
                           flag =  processMovHisOrder(map);
                        }
            
                }else{
                    flag=true;  
                }
        
        } catch (Exception e) {
                this.mLogger.exception(this.printLog, "", e);
                throw e ;
        }
        return flag;
}*/


	public boolean processMovHisOrder(Map map) throws Exception {
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
        
    public boolean processMovHisOrderItems(Map map) throws Exception {
            boolean flag = false;
            MovHisDAO movHisDao = new MovHisDAO();
            movHisDao.setmLogger(mLogger);
            
            try {
                    Hashtable htRecvHis = new Hashtable();
                    htRecvHis.clear();
                    htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
                    htRecvHis.put("DIRTYPE", TransactionConstants.ORDER_ITEM_CLS);
                    htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
                    htRecvHis.put("MOVTID", "");
                    htRecvHis.put("RECID", "");
                    htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.ORDERNO));
                    htRecvHis.put(IDBConstants.MOVHIS_ORDLNO, map.get(IConstants.ORDERLNO));
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
