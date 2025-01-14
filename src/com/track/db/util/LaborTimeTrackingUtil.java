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
import com.track.dao.LaborTimeTrackingDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.TimeTrackingDAO;



import com.track.util.MLogger;

public class LaborTimeTrackingUtil {
	
	private boolean printLog = MLoggerConstant.LaborTimeTrackingUtil_PRINTPLANTMASTERLOG;
	private LaborTimeTrackingDAO _LaborTimeTrackingDAO;  
	
	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
	
	public LaborTimeTrackingUtil()
	{
		_LaborTimeTrackingDAO = new LaborTimeTrackingDAO();
	}
	
	
	public ArrayList getLaborTimeTrackingReport(String aPlant, Hashtable ht,String afrmDate,String atoDate,String empno,String wono,String opseq,String extcond) throws Exception {
		ArrayList arrList = new ArrayList();
		    String sCondition=""; 
		    String sDtCond="",sAfterDtCond="";
		  
		          
		    if (afrmDate.length() > 0) {

				sCondition = sCondition + " AND  substring(a.crat,1,8) >= '"// 
						+ afrmDate + "'  ";
				if (atoDate.length() > 0) {
					sCondition = sCondition + " AND substring(a.crat,1,8)<= '"
							+ atoDate + "'  ";
				}
			    } else {
				if (atoDate.length() > 0) {
					sCondition = sCondition + "  substring(a.crat,1,8) <= '" 
							+ atoDate + "'  ";
				}
			   }
		   if(empno.length()>0){
			   
			   sCondition = sCondition+" AND A.EMPNO = '" +empno+"'";
		   }
		      
		   if(wono.length()>0){
			   
			   sCondition = sCondition+" AND WONO = '" +wono+"'";
		   }

		   if(opseq.length()>0){
	   
			   sCondition = sCondition+" AND OPSEQ = '" +opseq+"'";
		   }
			try {
	
				_LaborTimeTrackingDAO.setmLogger(mLogger);
				
						
				StringBuffer aQuery = new StringBuffer();
				aQuery.append( " SELECT EMPNO,WONO,PITEM,OPSEQ,QTY,TRANDATE,INTIME,OUTTIME,TOTAL_TIME,EMPNAME from( ");
				aQuery.append( " SELECT 2 AS rank,A.EMPNO,A.EVENT as WONO,'N.A.' as PITEM,'N.A.' as OPSEQ,0 as QTY,DATE as TRANDATE, ");
				aQuery.append( " CONVERT(VARCHAR(8), A.INTIME, 108) AS INTIME,CONVERT(VARCHAR(8), A.OUTTIME, 108) AS  OUTTIME, ");
				aQuery.append( " CAST((A.outtime-A.intime) as time(0)) TOTAL_TIME, ");
				aQuery.append( " (SELECT ISNULL(FNAME,'')  FROM ["+ aPlant+"_EMP_MST] WHERE EMPNO =A.EMPNO) AS EMPNAME ");
				aQuery.append( " from ["+ aPlant+ "_"+ "TIME_TRACKING]A,["+ aPlant+ "_"+ "EVENT_MST]B,["+ aPlant+ "_"+ "LABOR_TIME_TRACKING] C ");
				aQuery.append( " WHERE A.EMPNO =C.EMPNO AND A.DATE=C.TRANDATE AND A.EVENT=B.EVENT_ID AND TYPE=0 AND");
				aQuery.append( " A.INTIME IS NOT NULL AND A.OUTTIME IS NOT NULL "+sCondition);
				aQuery.append( " UNION ");
				aQuery.append( " SELECT 1 AS rank,A.EMPNO,WONO,ISNULL(PITEM,'')PITEM,OPSEQ,ISNULL(QTY,0)QTY,TRANDATE, ");
				aQuery.append( " CONVERT(VARCHAR(8), A.INTIME, 108) AS INTIME,CONVERT(VARCHAR(8), A.OUTTIME, 108) AS  OUTTIME, ");
				//aQuery.append( " (SELECT CASE  WHEN TYPE IN ('1') THEN '+' ELSE '-' END FROM  ["+ aPlant+ "_"+ "EVENT_MST"+ "] WHERE EVENT_ID=A.EVENT) AS EVENTTYPE,"); 
				//aQuery.append( " SUM(CONVERT(NUMERIC(18, 2),(DATEDIFF(MI,intime,outtime)/60+DATEDIFF(MI,intime,outtime)%60)/100.0)) TOTAL_TIME, ");
				//aQuery.append( " CONVERT(CHAR(8), CAST(CONVERT(varchar(23),outtime,121) AS DATETIME)-CAST(CONVERT(varchar(23),intime,121)AS DATETIME),8) TOTAL_TIME, ");
				aQuery.append( " CAST((A.outtime-A.intime) as time(0)) TOTAL_TIME, ");
				aQuery.append( " (SELECT ISNULL(FNAME,'')  FROM ["+ aPlant+ "_"+ "EMP_MST"+ "] WHERE EMPNO =A.EMPNO) AS EMPNAME ");
				aQuery.append( " FROM  ["+ aPlant+ "_"+ "LABOR_TIME_TRACKING"+ "] A WHERE  A.INTIME IS NOT NULL AND A.OUTTIME IS NOT NULL "+sCondition );
				
				
				
				sCondition = ")dt ORDER BY EMPNO,TRANDATE,INTIME,OUTTIME,rank,WONO ";

				arrList = _LaborTimeTrackingDAO.selectForReport(aQuery.toString(), ht, sCondition);
				
						
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
	
				_LaborTimeTrackingDAO.setmLogger(mLogger);
								
				 StringBuffer aQuery = new StringBuffer();
				    aQuery.append( " SELECT  ");
					aQuery.append( " SUM(DATEDIFF(s,intime,outtime)/3600)HH,SUM(DATEDIFF(s,intime,outtime)%3600/60)MI,SUM(DATEDIFF(s,intime,outtime)%60)SS ");
					aQuery.append( " FROM  ["+ aPlant+ "_"+ "LABOR_TIME_TRACKING"+ "] A WHERE   EMPNO=A.EMPNO AND WONO=A.WONO AND INTIME IS NOT NULL AND OUTTIME IS NOT NULL " + 	sCondition);
					//aQuery.append( " AND EVENT IN(SELECT EVENT_ID FROM ["+ aPlant+ "_"+ "EVENT_MST"+ "] WHERE TYPE=1) ");
					
					arrList = _LaborTimeTrackingDAO.selectForReport(aQuery.toString(), ht, sAfterDtCond);
				
						
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}
			return arrList;
		}
	public ArrayList getOutTimeTotal(String aPlant, Hashtable ht,String afrmDate,String atoDate,String wono,String opseq,String emp,String extcond) throws Exception {
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
	
				_LaborTimeTrackingDAO.setmLogger(mLogger);
								
				   StringBuffer aQuery = new StringBuffer();
				    aQuery.append( " SELECT  ");
					aQuery.append( " SUM(DATEDIFF(s,intime,outtime)/3600)HH,SUM(DATEDIFF(s,intime,outtime)%3600/60)MI,SUM(DATEDIFF(s,intime,outtime)%60)SS ");
					aQuery.append( " FROM  ["+ aPlant+ "_"+ "TIME_TRACKING"+ "] A WHERE   EMPNO=A.EMPNO AND EVENT=A.EVENT AND INTIME IS NOT NULL AND OUTTIME IS NOT NULL " + 	sCondition);
					aQuery.append( " AND EVENT IN(SELECT EVENT_ID FROM ["+ aPlant+ "_"+ "EVENT_MST"+ "] WHERE TYPE=0) ");
					aQuery.append( " AND DATE IN(SELECT TRANDATE FROM ["+ aPlant+ "_"+ "LABOR_TIME_TRACKING"+ "]B WHERE A.DATE=B.TRANDATE AND WONO like '"+wono+"%' AND OPSEQ like '"+opseq+"%' AND EMPNO='"+emp+"')");
									
				   arrList = _LaborTimeTrackingDAO.selectForReport(aQuery.toString(), ht, sAfterDtCond);
					
						
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}
			return arrList;
		}


}
