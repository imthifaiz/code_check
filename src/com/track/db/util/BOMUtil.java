package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.BomDAO;

import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class BOMUtil {

	private BomDAO bomDAO = null;
	private MLogger mLogger = new MLogger();
	private boolean printLog = MLoggerConstant.KITTING_PRODUCTHANDLERLOG;

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public BOMUtil() {
	  bomDAO = new BomDAO();
	}
	
	public ArrayList getKittingSummary(Hashtable ht, String aPlant,String afrmDate, String atoDate,String atoSort) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
			BomDAO bomDAO = new BomDAO();
			bomDAO .setmLogger(mLogger);
			sCondition = " ";
			if(atoSort.equals("ISSUEDPRODUCT"))
				{
					if (afrmDate.length() > 0) {
						sCondition = sCondition + " AND CREATE_AT  >= '" + afrmDate
								+ "'  ";
						if (atoDate.length() > 0) {
							sCondition = sCondition + " AND CREATE_AT  <= '" + atoDate
									+ "'  ";
						}
					} else {
						if (atoDate.length() > 0) {
							sCondition = sCondition + " AND CREATE_AT <= '" + atoDate
									+ "'  ";
						}
				}
					sCondition = sCondition + " AND A.STATUS='C'";
			}
			else
			{
				sCondition = " AND A.STATUS='A'";
			}
			
			//ORDER BY A.CHILD_PRODUCT,A.STATUS
			//sCondition = sCondition + " ORDER BY A.PARENT_PRODUCT,A.PARENT_PRODUCT_LOC,A.PARENT_PRODUCT_BATCH";
			sCondition = sCondition + " ORDER BY A.KONO DESC";
			//---Modified by Bruhan on Feb 27 2014, Description: To display location data's in uppercase
			String aQuery = "select ISNULL(KONO,'') KONO,A.PARENT_PRODUCT,A.PARENT_PRODUCT_BATCH,isnull(A.PARENT_PRODUCT_QTY,1)PARENT_PRODUCT_QTY,A.CHILD_PRODUCT,UPPER(A.CHILD_PRODUCT_LOC)CHILD_PRODUCT_LOC,A.CHILD_PRODUCT_BATCH,A.QTY,ISNULL(A.REMARKS,'') REMARKS,ISNULL(A.RSNCODE,'') RSNCODE,B.ITEMDESC,A.STATUS,isnull(A.SCANITEM,'')SCANITEM  from " + "["
			+ aPlant + "_" + "bom" + "]" + " A," + "[" + aPlant
			+ "_" + "itemmst" + "]" + " as b where a.CHILD_PRODUCT=b.item and isnull(b.USERFLD1,'')  <> 'K' ";
			arrList = bomDAO.selectForReport(aQuery, ht, sCondition);
			

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}
	
	


}
