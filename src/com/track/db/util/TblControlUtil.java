package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.TblControlBeanDAO;
import com.track.dao.TblControlDAO;
import com.track.util.DateUtils;
import com.track.util.MLogger;

public class TblControlUtil {
	private TblControlBeanDAO tblControlBeanDAO = new TblControlBeanDAO();
	private MLogger mLogger = new MLogger();
	private boolean printLog = MLoggerConstant.TblControlUtil_PRINTPLANTMASTERLOG;
	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public synchronized String getNextSeqNo4RecHis() {
		String nextSeqno = "";
		try {
			tblControlBeanDAO.setmLogger(mLogger);
			nextSeqno = tblControlBeanDAO.getNextSeqNo(IConstants.RECVHIS_FUNC);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return nextSeqno;
	}
        
        
        public synchronized String getNextSeqNo4RecvByRange(String Plant,String userID) {
                String nextSeqno = "";
                try {
                
                    Hashtable htTblCnt = new Hashtable();
                    TblControlDAO _TblControlDAO =new TblControlDAO();
                    _TblControlDAO.setmLogger(mLogger);
            
                    htTblCnt.put(IDBConstants.PLANT,Plant);
                    htTblCnt.put(IDBConstants.TBL_FUNCTION,IConstants.RECVBYRANGE_FUNC);
                    htTblCnt.put(IDBConstants.TBL_PREFIX1,"");
                    htTblCnt.put(IDBConstants.TBL_MIN_SEQ,"0000");
                    htTblCnt.put(IDBConstants.TBL_MAX_SEQ,"9999");
                    htTblCnt.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
                    htTblCnt.put(IDBConstants.CREATED_BY,userID );
                    htTblCnt.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
                    nextSeqno= _TblControlDAO.getTableControlDetails(htTblCnt);

                } catch (Exception e) {
                        this.mLogger.exception(this.printLog, "", e);
                }
                return nextSeqno;
        }

	public boolean isExistInTblControl(String aFunc) {
		boolean exists = false;
		try {
			tblControlBeanDAO.setmLogger(mLogger);
			exists = tblControlBeanDAO.isExistInTblControl(aFunc);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return exists;

	}

	/**
	 * method : insertTblControl(Hashtable ht) description : insert new record
	 * into CUSTMST
	 * 
	 * @param ht
	 * @return
	 */
	public boolean insertTblControl(Hashtable ht) {
		boolean inserted = false;
		try {
			tblControlBeanDAO.setmLogger(mLogger);
			inserted = tblControlBeanDAO.insertIntoTblControl(ht);
		} catch (Exception e) {
		}
		return inserted;
	}

	/**
	 * method : deleteTblControl(String aFunc) description : insert new record
	 * into TBLCONTROL
	 * 
	 * @param ht
	 * @return
	 */
	public boolean deleteTblControl(String aFunc) {
		boolean inserted = false;
		try {
			tblControlBeanDAO.setmLogger(mLogger);
			inserted = tblControlBeanDAO.deleteTblControl(aFunc);
		} catch (Exception e) {
		}
		return inserted;
	}

	/**
	 * method : deleteTblControl(String aFunc) description : insert new record
	 * into TBLCONTROL
	 * 
	 * @param ht
	 * @return
	 */
	public boolean updateTblControl(Hashtable htUpdate, Hashtable htCondition) {
		boolean inserted = false;
		try {
			tblControlBeanDAO.setmLogger(mLogger);
			inserted = tblControlBeanDAO
					.updateTblControl(htUpdate, htCondition);
		} catch (Exception e) {
		}
		return inserted;
	}

	public ArrayList getTblControlDetails(String aFunc) {
		ArrayList arrTblControlDetails = new ArrayList();
		try {
			tblControlBeanDAO.setmLogger(mLogger);
			arrTblControlDetails = tblControlBeanDAO
					.getTblControlDetails(aFunc);
		} catch (Exception e) {
		}
		return arrTblControlDetails;
	}

	public ArrayList getTblControlList(String aFunc) {
		ArrayList arrTblControl = new ArrayList();
		try {
			tblControlBeanDAO.setmLogger(mLogger);
			arrTblControl = tblControlBeanDAO.getTblControlList(aFunc);
		} catch (Exception e) {
		}
		return arrTblControl;

	}
     public boolean updateTblControlSeqNo(String plant,String aFunc,String type,String orderNo) throws Exception {
    	 return updateTblControlSeqNo(plant, aFunc, type, orderNo, false);
    }
     public boolean updateTblControlSaleEstiamateAndRentalServiceSeqNo(String plant,String aFunc,String type,String orderNo) throws Exception {
    	 return updateTblControlSaleEstiamateAndRentalServiceSeqNo(plant, aFunc, type, orderNo, false);
    }
     /**
      * This method will be called from WEB.
      * @param plant
      * @param aFunc
      * @param type
      * @param orderNo
      * @param isFromWeb
      * @return
      * @throws Exception
      */
     public boolean updateTblControlSeqNo(String plant,String aFunc,String type,String orderNo,boolean isFromMobile) throws Exception {
    	 boolean exists = true;
         
         String dt =new DateUtils().getGeneralDate("MMyy");
         if (orderNo.length()>=12){ // order number length for auto generation is 12, example I/O/T/L followed by yymmdd follwed by 5 digits
         if((
        		 !isFromMobile && orderNo.substring(0,1).equalsIgnoreCase(type) && orderNo.substring(1,5).equalsIgnoreCase(dt))
        	|| (isFromMobile && orderNo.substring(0,2).equalsIgnoreCase(type) && orderNo.substring(2,6).equalsIgnoreCase(dt)) 	 
        		 ){ 
         try{
             int seq = new Integer(orderNo.substring(5)).intValue();
             
             String seqNo =new Integer(seq).toString();
             new TblControlDAO().updateLatestSeqNo(aFunc, plant,seqNo);
         }catch(Exception e){}
         }   
         }else 
         {
        	 
         }
         return exists;
    }
     
     public boolean updateTblControlPDASeqNo(String plant,String aFunc,String type,String orderNo,boolean isFromMobile) throws Exception {
    	 boolean exists = true;
         
         String dt =new DateUtils().getGeneralDate("MMyy");
         if (orderNo.length()>=12){ // order number length for auto generation is 12, example I/O/T/L followed by yymmdd follwed by 5 digits
         if((
        	orderNo.substring(0,2).equalsIgnoreCase(type) && orderNo.substring(2,6).equalsIgnoreCase(dt)) 	 
        		 ){ 
         try{
             
             new TblControlDAO().updateLatestSeqNo(aFunc, plant,"");
         }catch(Exception e){}
         }   
         }else 
         {
        	 
         }
         return exists;
    }
     public boolean updateTblControlSaleEstiamateAndRentalServiceSeqNo(String plant,String aFunc,String type,String orderNo,boolean isFromMobile) throws Exception {
    	 boolean exists = true;
         
         String dt =new DateUtils().getGeneralDate("MMyy");
         if (orderNo.length()>=12){ // order number length for auto generation is 12, example I/O/T/L followed by yymmdd follwed by 5 digits
        	
         /*if((
        		 !isFromMobile && orderNo.substring(0,2).equalsIgnoreCase(type) && orderNo.substring(2,6).equalsIgnoreCase(dt))
        	|| (isFromMobile && orderNo.substring(0,2).equalsIgnoreCase(type) && orderNo.substring(2,6).equalsIgnoreCase(dt)) 	 
        		 )*/{ //&& orderNo.substring(7).length()==5)
         try{
             int seq = new Integer(orderNo.substring(6)).intValue();
             String seqNo =new Integer(seq).toString();
             System.out.println("seqNo:"+seqNo);
             new TblControlDAO().updateLatestSeqNo(aFunc, plant,seqNo);
         }catch(Exception e){}
         }   
         }else 
         {
        	 
         }
         return exists;
    }
     public boolean updateTblControlSeqNoWithoutorder(String plant,String aFunc,String type) throws Exception {
    	 boolean exists = true;
         
         String dt =new DateUtils().getGeneralDate("MMyy");
         try{
              new TblControlDAO().updateSeqNo(aFunc, plant);
         }catch(Exception e){}
        // }   
         //}
         return exists;
    }
    
    
    public boolean updateTblControlIESeqNo(String plant,String aFunc,String type,String orderNo) throws Exception {
        boolean exists = true;
            String dt =new DateUtils().getGeneralDate("MMyy");
            if (orderNo.length()>=12){ // order number length for auto generation is 12, example I/O/T/L followed by yymmdd follwed by 5 digits
            
            if(orderNo.substring(0,2).equalsIgnoreCase(type) && orderNo.substring(2,6).equalsIgnoreCase(dt )&& orderNo.substring(6).length()==7){
            try{
                int seq = new Integer(orderNo.substring(6)).intValue();
                String seqNo =new Integer(seq).toString();
                new TblControlDAO().updateLatestSeqNo(aFunc, plant,seqNo);
            }catch(Exception e){}
            }   
            }
            return exists;

}
    
    public boolean updateTblControlIESeqNoreturn(String plant,String aFunc,String type,String orderNo) throws Exception {
        boolean exists = true;
            String dt =new DateUtils().getGeneralDate("MMyy");
            if (orderNo.length()>=13){ // order number length for auto generation is 12, example I/O/T/L followed by yymmdd follwed by 5 digits
            	
            if(orderNo.substring(0,3).equalsIgnoreCase(type) && orderNo.substring(3,7).equalsIgnoreCase(dt )&& orderNo.substring(7).length()==7){
            try{
                int seq = new Integer(orderNo.substring(7)).intValue();
                String seqNo =new Integer(seq).toString();
                new TblControlDAO().updateLatestSeqNo(aFunc, plant,seqNo);
            }catch(Exception e){}
            }   
            }
            return exists;

}

    
    public boolean updateTblControlOESeqNo(String plant,String aFunc,String type,String orderNo) throws Exception {
        boolean exists = true;
       
            String dt =new DateUtils().getGeneralDate("MMyy");
            if (orderNo.length()>=12){ // order number length for auto generation is 12, example I/O/T/L followed by yymmdd follwed by 5 digits 
            if(orderNo.substring(0,2).equalsIgnoreCase(type) && orderNo.substring(2,6).equalsIgnoreCase(dt )&& orderNo.substring(6).length()==7){
            try{
                int seq = new Integer(orderNo.substring(6)).intValue();
                String seqNo =new Integer(seq).toString();
                new TblControlDAO().updateLatestSeqNo(aFunc, plant,seqNo);
            }catch(Exception e){}
            }   
            }
            return exists;
}
    

}