package com.track.db.util;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.constants.MLoggerConstant;
import com.track.dao.InstructionDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.TimeTrackingDAO;



import com.track.util.MLogger;

public class TimeTrackingUtil {
	
	private boolean printLog = MLoggerConstant.TimeTrackingUtil_PRINTPLANTMASTERLOG;
	private TimeTrackingDAO _TimeTrackingDAO;  
	
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
	
	public TimeTrackingUtil()
	{
		_TimeTrackingDAO = new TimeTrackingDAO();
	}
	
	
	public ArrayList getTimeTrackingReport(String aPlant, Hashtable ht,String afrmDate,String atoDate,String extcond) throws Exception {
		ArrayList arrList = new ArrayList();
		    String sCondition=""; 
		    String sDtCond="",sAfterDtCond="";
		  
		          
		    if (afrmDate.length() > 0) {

				sCondition = sCondition + " AND  substring(crat,1,8) >= '"// 
						+ afrmDate + "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND substring(crat,1,8)<= '"
							+ atoDate + "'  ";
				}
			    } else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + "  substring(crat,1,8) <= '" 
							+ atoDate + "'  ";
				}
			   }
		   
		      
			try {
	
				_TimeTrackingDAO.setmLogger(mLogger);
				
						
				StringBuffer aQuery = new StringBuffer();
			   /* aQuery.append( " SELECT EMPNO,EVENT AS EMP_EVENT,ISNULL(ALTERNATE_EMPLOYEE_NO,'')AS ALTERNATEEMP,DATE AS EVENTDATE, ");
				aQuery.append( " CONVERT(VARCHAR(8), INTIME, 108) AS INTIME,CONVERT(VARCHAR(8), OUTTIME, 108) AS  OUTTIME, ");
				aQuery.append( " (SELECT CASE  WHEN TYPE IN ('1') THEN '+' ELSE '-' END FROM  ["+ aPlant+ "_"+ "EVENT_MST"+ "] WHERE EVENT_ID=A.EVENT) AS EVENTTYPE,"); 
				//aQuery.append( " SUM(CONVERT(NUMERIC(18, 2),(DATEDIFF(MI,intime,outtime)/60+DATEDIFF(MI,intime,outtime)%60)/100.0)) TOTAL_TIME, ");
				//aQuery.append( " CONVERT(CHAR(8), CAST(CONVERT(varchar(23),outtime,121) AS DATETIME)-CAST(CONVERT(varchar(23),intime,121)AS DATETIME),8) TOTAL_TIME, ");
				aQuery.append( " CAST((outtime-intime) as time(0)) TOTAL_TIME, ");
				aQuery.append( " (SELECT ISNULL(FNAME,'')  FROM ["+ aPlant+ "_"+ "EMP_MST"+ "] WHERE EMPNO =A.EMPNO) AS EMPNAME ");
				//aQuery.append( " FROM  ["+ aPlant+ "_"+ "TIME_TRACKING"+ "] A WHERE  INTIME IS NOT NULL AND OUTTIME IS NOT NULL " );
				aQuery.append( " FROM  ["+ aPlant+ "_"+ "TIME_TRACKING"+ "] A WHERE  INTIME IS NOT NULL  " );
				*/
				aQuery.append( " SELECT EMPNO,EVENT AS EMP_EVENT,ISNULL(ALTERNATE_EMPLOYEE_NO,'')AS ALTERNATEEMP,DATE AS EVENTDATE, ");
				aQuery.append( " INTIME,OUTTIME, ");
				aQuery.append( " (SELECT CASE  WHEN TYPE IN ('1') THEN '+' ELSE '-' END FROM  ["+ aPlant+ "_"+ "EVENT_MST"+ "] WHERE EVENT_ID=A.EVENT) AS EVENTTYPE,"); 
				aQuery.append( " convert(varchar(5),DateDiff(s, INTIME,OUTTIME)/3600)+':'+convert(varchar(5),DateDiff(s, INTIME,OUTTIME)%3600/60)+':'+convert(varchar(5),(DateDiff(s,INTIME,OUTTIME)%60)) as TOTAL_TIME, ");
				aQuery.append( " (SELECT ISNULL(FNAME,'')  FROM ["+ aPlant+ "_"+ "EMP_MST"+ "] WHERE EMPNO =A.EMPNO) AS EMPNAME ");
				aQuery.append( " FROM  ["+ aPlant+ "_"+ "TIME_TRACKING"+ "] A WHERE  INTIME IS NOT NULL  " );
		
				sCondition = sCondition + " GROUP BY EMPNO,ALTERNATE_EMPLOYEE_NO,DATE,INTIME,OUTTIME,EVENT ORDER BY EMPNO,DATE,INTIME,OUTTIME";
				arrList = _TimeTrackingDAO.selectForReport(aQuery.toString(), ht, sCondition);
				
						
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}
			return arrList;
		}

	public ArrayList getInTimeTotal(String aPlant, Hashtable ht,String afrmDate,String atoDate,String extcond) throws Exception {
		ArrayList arrList = new ArrayList();
		    String sCondition=""; 
		    String sDtCond="",sAfterDtCond="";
		  
		          
		    if (afrmDate.length() > 0) {

				sCondition = sCondition + " AND  substring(crat,1,8) >= '"// 
						+ afrmDate + "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND substring(crat,1,8)<= '"
							+ atoDate + "'  ";
				}
			    } else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + "  substring(crat,1,8) <= '" 
							+ atoDate + "'  ";
				}
			   }
		   
		    sAfterDtCond =  " GROUP BY EMPNO";
			try {
	
				_TimeTrackingDAO.setmLogger(mLogger);
								
				 StringBuffer aQuery = new StringBuffer();
				    aQuery.append( " SELECT  ");
					aQuery.append( " SUM(DATEDIFF(s,intime,outtime)/3600)HH,SUM(DATEDIFF(s,intime,outtime)%3600/60)MI,SUM(DATEDIFF(s,intime,outtime)%60)SS ");
					aQuery.append( " FROM  ["+ aPlant+ "_"+ "TIME_TRACKING"+ "] A WHERE   EMPNO=A.EMPNO AND EVENT=A.EVENT AND INTIME IS NOT NULL AND OUTTIME IS NOT NULL " + 	sCondition);
					aQuery.append( " AND EVENT IN(SELECT EVENT_ID FROM ["+ aPlant+ "_"+ "EVENT_MST"+ "] WHERE TYPE=1) ");
					
					arrList = _TimeTrackingDAO.selectForReport(aQuery.toString(), ht, sAfterDtCond);
				
						
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}
			return arrList;
		}
	public ArrayList getOutTimeTotal(String aPlant, Hashtable ht,String afrmDate,String atoDate,String extcond) throws Exception {
		ArrayList arrList = new ArrayList();
		    String sCondition=""; 
		    String sDtCond="",sAfterDtCond="";
		  
		          
		    if (afrmDate.length() > 0) {

				sCondition = sCondition + " AND  substring(crat,1,8) >= '"// 
						+ afrmDate + "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND substring(crat,1,8)<= '"
							+ atoDate + "'  ";
				}
			    } else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + "  substring(crat,1,8) <= '" 
							+ atoDate + "'  ";
				}
			   }
		   
		    sAfterDtCond =  " GROUP BY EMPNO";
			try {
	
				_TimeTrackingDAO.setmLogger(mLogger);
								
				   StringBuffer aQuery = new StringBuffer();
				    aQuery.append( " SELECT  ");
					aQuery.append( " SUM(DATEDIFF(s,intime,outtime)/3600)HH,SUM(DATEDIFF(s,intime,outtime)%3600/60)MI,SUM(DATEDIFF(s,intime,outtime)%60)SS ");
					aQuery.append( " FROM  ["+ aPlant+ "_"+ "TIME_TRACKING"+ "] A WHERE   EMPNO=A.EMPNO AND EVENT=A.EVENT AND INTIME IS NOT NULL AND OUTTIME IS NOT NULL " + 	sCondition);
					aQuery.append( " AND EVENT IN(SELECT EVENT_ID FROM ["+ aPlant+ "_"+ "EVENT_MST"+ "] WHERE TYPE=0) ");
									
				   arrList = _TimeTrackingDAO.selectForReport(aQuery.toString(), ht, sAfterDtCond);
					
						
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}
			return arrList;
		}


}
