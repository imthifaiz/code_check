package com.track.db.util;

import com.track.constants.MLoggerConstant;
import com.track.util.MLogger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.dao.InstructionDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.TimeSlotDAO;
import com.track.dao.PoHdrDAO;

import com.track.util.MLogger;

public class TimeSlotUtil {
	private boolean printLog = MLoggerConstant.TimeSlotUtil_PRINTPLANTMASTERLOG;
	private TimeSlotDAO _TimeSlotDAO;  
	
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
	
	public TimeSlotUtil()
	{
		_TimeSlotDAO = new TimeSlotDAO();
	}
	
	public boolean insertMst(Hashtable ht) {
		boolean inserted = false;
		try {
			_TimeSlotDAO.setmLogger(mLogger);
			inserted = _TimeSlotDAO.saveMst(ht);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return inserted;
	}
	public boolean isExistIns(Hashtable ht,String ext) {
		boolean exist = false;
		try {
			_TimeSlotDAO.setmLogger(mLogger);
			exist = _TimeSlotDAO.isExisit(ht,ext);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return exist;
	}	
	public boolean updateMst(Hashtable htupdate,Hashtable htcondtn) {
		boolean updated = false;
		try {
			_TimeSlotDAO.setmLogger(mLogger);
			updated = _TimeSlotDAO.updateMst(htupdate, htcondtn);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return updated;
	}	
	
	public boolean deleteMst(String Plant,String TimeSlots) {
		boolean deleted = false;
		try {
			_TimeSlotDAO.setmLogger(mLogger);
			deleted = _TimeSlotDAO.deleteTimeSlots(Plant, TimeSlots);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return deleted;
	}	
	
	
	public ArrayList listInstrns(String query,Hashtable ht,String extcondtn) throws Exception {
        List lst =  new ArrayList();
        _TimeSlotDAO = new TimeSlotDAO();
        lst=_TimeSlotDAO.selectMst(query, ht, extcondtn);
         return (ArrayList) lst;
 }
	
	public List qryTimeSlot(String timeslot, String plant, String cond)
	throws Exception {
	List listQry = new ArrayList();
	try {
		
		listQry = _TimeSlotDAO.queryTimeSlot(timeslot, plant, cond);
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	}
	return listQry;
	}
	
	public List qryEditTimeSlot(String timeslot, String plant, String cond)
	throws Exception {
	List listQry = new ArrayList();
	try {
		
		listQry = _TimeSlotDAO.queryEditTimeSlot(timeslot, plant, cond);
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	}
	return listQry;
	}
	
	 public ArrayList getTimeSlots( String plant, String Cond) {

         ArrayList al = null;
        TimeSlotDAO dao = new TimeSlotDAO();
         dao.setmLogger(mLogger);
         try {
                 al = dao.getTimeSlotList(plant, Cond);

         } catch (Exception e) {

         }

         return al;
 }
	 
	 public ArrayList getTimeSlotDetails(String plant, String timeslot)
		throws Exception {
		boolean flag = false;
		Hashtable htCondition = new Hashtable();
		ArrayList alResult = new ArrayList();
	
		htCondition.put("PLANT", plant);
	
		try {
			_TimeSlotDAO.setmLogger(mLogger);
			alResult = _TimeSlotDAO.getDAOTimeSlotsDetails(
					"TIMESLOTS,ISNULL(QTY,0) as QTY,ISNULL(REMARKS,'') REMARKS",
					htCondition, timeslot);
	
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
	
		}
	
		return alResult;
}
	

}
