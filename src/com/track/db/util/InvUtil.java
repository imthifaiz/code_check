package com.track.db.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.BaseDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.InvSesBeanDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.PlantMstDAO;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

@SuppressWarnings({"rawtypes", "unchecked"})
public class InvUtil extends BaseDAO {
	
	private InvSesBeanDAO invSesBeanDAO = new InvSesBeanDAO();
	private InvMstDAO _InvMstDAO = null;
	private MLogger mLogger = new MLogger();
	private boolean printLog = MLoggerConstant.InvUtil_PRINTPLANTMASTERLOG;

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public InvUtil() {
		_InvMstDAO = new InvMstDAO();
	}

	

	/**
	 * method : insertIntoInvmst(Hashtable ht) description : insert new record
	 * into INVMST
	 * 
	 * @param ht
	 * @return
	 */
	public boolean insertIntoInvmst(Hashtable ht) {
		boolean inserted = false;
		try {
			invSesBeanDAO.setmLogger(mLogger);
			inserted = invSesBeanDAO.insertIntoInvsmt(ht);
		} catch (Exception e) {
		}
		return inserted;
	}


	public ArrayList getInvListSummary(Hashtable ht, String aPlant,
			String aItem, String aLoc, String aBatch) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);

			sCondition = " group by a.item,a.loc,a.userfld4,b.itemdesc";

			String aQuery = "select a.ITEM,b.ITEMDESC,UPPER(a.LOC) LOC,a.UserFld4 as BATCH,  sum(QTY)as QTY ,sum(b.STKQTY) STKQTY from "
					+ "["
					+ aPlant
					+ "_"
					+ "invmst"
					+ "]"
					+ " A,"
					+ "["
					+ aPlant
					+ "_"
					+ "itemmst"
					+ "]"
					+ " as b where a.item=b.item and QTY>0 ";
			arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}

	public ArrayList getInvListSummary(Hashtable ht, String aPlant,
			String aItem, String aLoc, String aBatch, String desc) {
		ArrayList arrList = new ArrayList();

		//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
		String sCondition ="";
        if (desc.length() > 0 ) {
	        if (desc.indexOf("%") != -1) {
	        	desc = desc.substring(0, desc.indexOf("%") - 1);
	        }
	        sCondition = " and replace(A.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(desc.replaceAll(" ","")) + "%' ";
        }
		try {
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
			sCondition = " group by a.item,a.loc,a.userfld4,b.itemdesc,b.prd_cls_id,b.itemtype,b.stkuom order by a.item,a.loc";

			// Added stkuom to the query
			String aQuery = "select a.ITEM,b.ITEMDESC,b.prd_cls_id as PRDCLSID,b.stkuom as STKUOM,b.itemtype as ITEMTYPE,a.LOC,a.UserFld4 as BATCH,  sum(QTY)as QTY ,sum(b.STKQTY) STKQTY from "
					+ "["
					+ aPlant
					+ "_"
					+ "invmst"
					+ "]"
					+ " A,"
					+ "["
					+ aPlant
					+ "_"
					+ "itemmst"
					+ "]"
					+ " as b where a.item=b.item and QTY>0 ";
			arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}

//  Start code modified by Bruhan for product brand on 11/9/12 

	public ArrayList getInvListSummaryForItemMinStockQty(Hashtable ht,
			String aPlant, String productDesc) {
		ArrayList arrList = new ArrayList();
		 //CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
		 String sCondition=""; 
	    if (productDesc.length() > 0 ) {
             if (productDesc.indexOf("%") != -1) {
                     productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
             }
		 sCondition = " and replace(C.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
	    }
//		String extraCond = "";
		try {

			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);

			StringBuffer sql = new StringBuffer(" SELECT ");

			sql
					.append("    DISTINCT C.PLANT, C.ITEM,ISNULL(C.ITEMDESC,'') AS ITEMDESC,ISNULL(C.STKUOM,'') AS STKUOM,ISNULL(C.PRD_CLS_ID,'') AS PRDCLSID,ISNULL(C.PRD_BRAND_ID,'') AS PRD_BRAND_ID,ISNULL(C.ITEMTYPE,'') AS ITEMTYPE,");
			sql
					.append(" ISNULL(D.QTY,0) as QTY,ISNULL(C.STKQTY,0) as STKQTY,ISNULL(D.BATCH,'') AS BATCH, ISNULL(D.LOC,'') AS LOC ");
			sql.append(" FROM " + "[" + aPlant + "_" + "ITEMMST" + "]"
					+ " AS C LEFT  OUTER JOIN  ");
			sql
					.append(" (SELECT  DISTINCT A.LOC as LOC,A.ITEM as ITEM,B.ITEMDESC as ITEMDESC,B.PRD_CLS_ID,B.ITEMTYPE as ITEMTYPE,B.PRD_BRAND_ID,A.USERFLD4 as BATCH,QTY  ");
			sql.append("  from " + "[" + aPlant + "_" + "INVMST" + "]" + " A,"
					+ "[" + aPlant + "_" + "ITEMMST" + "]"
					+ "  B where A.PLANT =B.PLANT AND A.PLANT ='" + aPlant
					+ "' ");
			sql
					.append("  AND  A.ITEM =B.ITEM  ) AS D on  C.ITEM=D.ITEM WHERE C.PLANT='"
							+ aPlant + "'");

			arrList = _InvMstDAO
					.selectForReport(sql.toString(), ht, sCondition);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}
	//below method chaned by Bruhan to get shippingarea,temp locations having qty in invmst if loc,loctypeid search is available on 15/jan/2014
	public ArrayList getInvListSummaryForItemMinStockQtyForExcel(Hashtable ht,
			String aPlant, String productDesc, boolean isWithZero,String loctypeid,String loc) throws Exception {
		ArrayList arrList = new ArrayList();
		//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
        String sCondition=""; 
	    if (productDesc.length() > 0 ) {
                if (productDesc.indexOf("%") != -1) {
                        productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
                }
         
		 sCondition = " and replace(C.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
	    }

		 if (loc != null && loc!="") {
         	sCondition = sCondition+" AND LOC like '%"+loc+"%'";
         }
         
         if (loctypeid != null && loctypeid!="") {
 			sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType(aPlant,loctypeid)+" )";
 		}
//		String extraCond = "";
		try {

			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);

			if (!isWithZero) {

				sCondition = " and ISNULL((SELECT SUM(QTY) FROM ["
						+ aPlant
						+ "_invmst]    WHERE ITEM=A.ITEM  GROUP BY ITEM  ),0)<>'0'";
			}

			String aQuery = "select ITEM FROM " + "[" + aPlant + "_"
					+ "ITEMMST" + "]"
					+ " A WHERE STKQTY>ISNULL((SELECT SUM(QTY) FROM " + "["
					+ aPlant + "_" + "invmst" + "]"
					+ " WHERE ITEM=A.ITEM  GROUP BY ITEM ),0) " + sCondition;

			StringBuffer sql = new StringBuffer(" SELECT ");
			//---Modified by Bruhan on MARCH 03 2014, Description: To display location data's in uppercase

			sql
					.append("    DISTINCT C.PLANT, C.ITEM,ISNULL(C.ITEMDESC,'') AS ITEMDESC,ISNULL(C.STKUOM,'') AS STKUOM,ISNULL(C.PRD_CLS_ID,'') AS PRDCLSID,ISNULL(C.PRD_BRAND_ID,'') AS PRD_BRAND_ID,ISNULL(C.ITEMTYPE,'') AS ITEMTYPE,");
			sql
					.append(" ISNULL(D.QTY,0) as QTY,ISNULL(C.STKQTY,0) as STKQTY,ISNULL(D.BATCH,'') AS BATCH, ISNULL(UPPER(D.LOC),'') AS LOC ");
			sql.append(" FROM " + "[" + aPlant + "_" + "ITEMMST" + "]"
					+ " AS C LEFT  OUTER JOIN  ");
			sql
					.append(" (SELECT  DISTINCT UPPER(A.LOC) as LOC,A.ITEM as ITEM,B.ITEMDESC as ITEMDESC,B.PRD_CLS_ID,B.PRD_BRAND_ID,B.ITEMTYPE as ITEMTYPE,A.USERFLD4 as BATCH,QTY  ");
			sql.append("  from " + "[" + aPlant + "_" + "INVMST" + "]" + " A,"
					+ "[" + aPlant + "_" + "ITEMMST" + "]"
					+ "  B where A.PLANT =B.PLANT AND A.PLANT ='" + aPlant
					+ "' ");
			sql
					.append("  AND  A.ITEM =B.ITEM  ) AS D on  C.ITEM=D.ITEM WHERE C.PLANT='"
							+ aPlant + "' AND C.ITEM IN(" + aQuery + ")" + "");

			arrList = _InvMstDAO.selectForReport(sql.toString(), ht, "");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}

	public ArrayList getInvListSummary(Hashtable ht, String aPlant,
			String aItem, String aLoc, String aBatch, String prdid,
			String productDesc, boolean iswithzeroQty,String loctypeid) throws Exception {
		ArrayList arrList = new ArrayList();
		//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
		String sCondition="";
		 if (productDesc.length() > 0 ) {
             if (productDesc.indexOf("%") != -1) {
                     productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
             }
      
		 sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
	    }

 		if (aLoc != null && aLoc!="") {
         	sCondition = sCondition+" AND LOC like '%"+aLoc+"%'";
         }
         
         if (loctypeid != null && loctypeid!="") {
 			sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType(aPlant,loctypeid)+" )";
 		}
		String extraCond = "";
		try {

			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
			sCondition = sCondition
					+ " group by a.item,a.loc,a.userfld4,b.itemdesc,b.prd_cls_id,b.prd_brand_id,b.itemtype,b.stkuom order by a.item,a.loc";
			if (!iswithzeroQty) {
				//extraCond = " and QTY>0 ";
				extraCond = " and a.plant <> '' ";
			}
			// Added stkuom to the query
			//---Modified by Bruhan on Feb 28 2014, Description: To display location data's in uppercase
			String aQuery = "select a.ITEM,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,b.stkuom as STKUOM,b.itemtype as ITEMTYPE,UPPER(a.LOC) LOC,a.UserFld4 as BATCH,  sum(QTY)as QTY ,sum(b.STKQTY) STKQTY ,(select sum(QTY) from "
					+ "["
					+ aPlant
					+ "_"
					+ "invmst"
					+ "]"
					+ "where item=a.item group by item) as ITEMQTY  from "
					+ "["
					+ aPlant
					+ "_"
					+ "invmst"
					+ "]"
					+ " A,"
					+ "["
					+ aPlant
					+ "_"
					+ "itemmst"
					+ "]"
					+ " as b where a.item=b.item  " + extraCond;
			arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}

	 //below method chaned by Bruhan to get shippingarea,temp locations having qty in invmst if loc,loctypeid search is available on 15/jan/2014
    public ArrayList getInvListSummaryWithMinStock(Hashtable ht, String aPlant,
                    String aItem, String productDesc, boolean iswithzeroQty,String loctypeid,String loctypeid2,String loctypeid3,String loc,String viewstatus,String UOM,String vendname) throws Exception {
            ArrayList arrList = new ArrayList();
          //CHNAGECODE-001 DESC CHANGE BY BRUHAN INCLUDE REPLACE FUNCTION IN PRODDESC
            String sCondition=""; 
    	    if (productDesc.length() > 0 ) {
                    if (productDesc.indexOf("%") != -1) {
                            productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
                    }
             
    		 sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
    	    }
          
            if (loc != null && loc!="") {
            	sCondition = sCondition+" AND LOC like '%"+loc+"%'";
            }
            
            if (loctypeid != null && loctypeid!="") {
    			sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType(aPlant,loctypeid)+" )";
    		}
            
            if (loctypeid2 != null && loctypeid2!="") {
    			sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType2(aPlant,loctypeid2)+" )";
    		}
            if (loctypeid3 != null && loctypeid3!="") {
				sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType3(aPlant,loctypeid3)+" )";
			}
            if (vendname.length()>0){
	        	   vendname = StrUtils.InsertQuotes(vendname);
//      	sCondition = sCondition + " AND VNAME LIKE '%"+vendname+"%' " ;
      	sCondition= sCondition+" AND VENDNO in (select VENDNO from "+aPlant+"_VENDMST where VNAME like '"+vendname+"%')";
       }
            
//           String extraCond = "";
            try {

                    InvMstDAO _InvMstDAO = new InvMstDAO();
                    _InvMstDAO.setmLogger(mLogger);
                    if(viewstatus.equals("ByZeroQty"))
                    {
                     sCondition = sCondition
                                    + " AND QTY=0 AND LOC NOT LIKE '%SHIPPINGAREA%' AND LOC NOT LIKE '%TEMP_TO%' group by a.item,a.loc,b.itemdesc,b.prd_cls_id,b.prd_dept_id,b.prd_brand_id,b.cost,b.itemtype,b.INVENTORYUOM,a.plant,B.VENDNO "; 
                     			    
                    }
                    if(viewstatus.equals("ByMinQty"))
                    {
                     sCondition = sCondition
                                    + " group by a.item,a.loc,b.itemdesc,b.prd_cls_id,b.prd_dept_id,b.prd_brand_id,b.cost,b.itemtype,b.INVENTORYUOM,a.plant,B.VENDNO " +
                                    "    having (select sum(QTY) from ["+ aPlant + "_" + "Invmst] where item=a.item group by item) < (Select STKQTY from"  + "["+ aPlant + "_" + "itemmst] where ITEM=a.ITEM and STKQTY > 0 ) ";
                    }
                    if(viewstatus.equals("ByMaxQty"))
                    {
                     sCondition = sCondition
                                    + " group by a.item,a.loc,b.itemdesc,b.prd_cls_id,b.prd_dept_id,b.prd_brand_id,b.cost,b.itemtype,b.INVENTORYUOM,a.plant,B.VENDNO " +
                                    "    having (select sum(QTY) from ["+ aPlant + "_" + "Invmst] where item=a.item group by item) > (Select MAXSTKQTY from"  + "["+ aPlant + "_" + "itemmst] where ITEM=a.ITEM and MAXSTKQTY > 0 ) ";
                    }
                    String allqty ="";
                    if(viewstatus.equals("ByAllQty"))
                    {
                     sCondition = sCondition
                                    + " group by a.item,a.loc,b.itemdesc,b.prd_cls_id,b.prd_dept_id,b.prd_brand_id,b.cost,b.itemtype,b.INVENTORYUOM,a.plant,B.VENDNO ";
                     allqty=" AND (STKQTY!=0 OR MAXSTKQTY!=0) ";
                    }
                    if (!iswithzeroQty) {
                            //extraCond = " and QTY>0 ";
//                    	extraCond = " and a.plant <> '' ";
                    }
                     // Added stkuom to the query
                    //---Modified by Bruhan on Feb 27 2014, Description: To display location data's in uppercase
                  //---Modified by Azees 31/8/19 Muti UOM
                    String aQuery ="";
                    if(UOM.length()>0){
                    aQuery = "with tempdb as ( "
                    				+ "select a.ITEM,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_dept_id,'') as PRD_DEPT_ID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,convert(float,isnull(b.COST,'')) as TOTCOST,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ aPlant +"_UOM u where u.UOM='"+UOM+"'),'') as STKUOM,b.itemtype as ITEMTYPE,UPPER(a.LOC) LOC,sum(QTY)as QTY ,max(b.STKQTY) STKQTY,max(isnull(b.MAXSTKQTY,0)) MAXSTKQTY,"
                    						+ "ISNULL((SELECT LOCDESC  FROM ["+ aPlant +"_LOCMST] D WHERE D.LOC= A.LOC),'') LOCDESC ,"
                    						+ "ISNULL((SELECT LOC_TYPE_ID  FROM ["+ aPlant +"_LOCMST] D WHERE D.LOC= A.LOC),'') LOC_TYPE_ID,(select sum(QTY) from"
                                    + "["
                                    + aPlant
                                    + "_"
                                    + "invmst"
                                    + "]"
                                    + "where item=a.item group by item) as ITEMQTY,a.plant,'"+UOM+"' as  INVENTORYUOM,ISNULL((select u.QPUOM from "+ aPlant +"_UOM u where u.UOM='"+UOM+"'),1) as UOMQTY,B.VENDNO from "
                                    + "["
                                    + aPlant
                                    + "_"
                                    + "invmst"
                                    + "]"
                                    + " A,"
                                    + "["
                                    + aPlant
                                    + "_"
                                    + "itemmst"
                                    + "] "
                                    
                    + " as b where a.item=b.item  and a.item in (SELECT ITEM FROM "
                        + aPlant
                        + "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"+ aItem + "%')" + sCondition + ")"
                        + " select plant,ITEM,ITEMDESC,PRDCLSID,PRD_DEPT_ID,PRD_BRAND_ID,TOTCOST,ITEMTYPE,LOC,INVENTORYUOM,STKUOM"
							/*
							 * +
							 * " ,convert(int,(STKQTY/UOMQTY)) as INVMINQTY,case when UOMQTY<=STKQTY then STKQTY-(convert(int,(STKQTY/UOMQTY))*UOMQTY) else STKQTY end MINQTY "
							 * +
							 * " ,convert(int,(MAXSTKQTY/UOMQTY)) as INVMAXQTY,case when UOMQTY<=MAXSTKQTY then MAXSTKQTY-(convert(int,(MAXSTKQTY/UOMQTY))*UOMQTY) else MAXSTKQTY end MAXQTY"
							 * +
							 * " ,convert(int,(QTY/UOMQTY)) as INVQTY,case when UOMQTY<=QTY then QTY-(convert(int,(QTY/UOMQTY))*UOMQTY) else QTY end QTY"
							 */
						+" ,case when 0<ISNULL(UOMQTY,0) then (convert(float,(STKQTY/UOMQTY))) else STKQTY end INVMINQTY, STKQTY as MINQTY"	 
						+" ,case when 0<ISNULL(UOMQTY,0) then (convert(float,(MAXSTKQTY/UOMQTY))) else MAXSTKQTY end INVMAXQTY, MAXSTKQTY as MAXQTY,LOCDESC,LOC_TYPE_ID"	 
						+" ,case when 0<ISNULL(UOMQTY,0) then (convert(float,(QTY/UOMQTY))) else QTY end INVQTY ,QTY"
						+" ,ISNULL((SELECT VNAME  FROM ["+aPlant+"_VENDMST] D WHERE D.VENDNO= a.VENDNO),'') VNAME"
                        + " from tempdb a Where plant<> '' AND (case when 0<ISNULL(UOMQTY,0) then (convert(float,(QTY/UOMQTY))) else QTY end) >= 0 "+allqty;
                    }
                    else
                    {
                    	aQuery = "with tempdb as ( "
            				+ "select a.ITEM,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_dept_id,'') as PRD_DEPT_ID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,convert(float,isnull(b.COST,'')) as TOTCOST,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ aPlant +"_UOM u where u.UOM=(case when ''='' then ISNULL(b.INVENTORYUOM,'') else '' end)),'') as STKUOM,b.itemtype as ITEMTYPE,UPPER(a.LOC) LOC,sum(QTY)as QTY ,max(b.STKQTY) STKQTY,max(isnull(b.MAXSTKQTY,0)) MAXSTKQTY,ISNULL((SELECT LOCDESC  FROM ["+ aPlant +"_LOCMST] D WHERE D.LOC= A.LOC),'') LOCDESC,"
            						+ "ISNULL((SELECT LOC_TYPE_ID  FROM ["+ aPlant +"_LOCMST] D WHERE D.LOC= A.LOC),'') LOC_TYPE_ID ,(select sum(QTY) from"
                            + "["
                            + aPlant
                            + "_"
                            + "invmst"
                            + "]"
                            + "where item=a.item group by item) as ITEMQTY,a.plant,ISNULL(b.INVENTORYUOM,'') as  INVENTORYUOM,ISNULL((select u.QPUOM from "+ aPlant +"_UOM u where u.UOM=b.INVENTORYUOM),1) as UOMQTY,B.VENDNO from "
                            + "["
                            + aPlant
                            + "_"
                            + "invmst"
                            + "]"
                            + " A,"
                            + "["
                            + aPlant
                            + "_"
                            + "itemmst"
                            + "] "
                            
            + " as b where a.item=b.item  and a.item in (SELECT ITEM FROM "
                + aPlant
                + "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"+ aItem + "%')" + sCondition + ")"
                + " select plant,ITEM,ITEMDESC,PRDCLSID,PRD_DEPT_ID,PRD_BRAND_ID,TOTCOST,ITEMTYPE,LOC,INVENTORYUOM,STKUOM"
								/*
								 * +
								 * " ,convert(int,(STKQTY/UOMQTY)) as INVMINQTY,case when UOMQTY<=STKQTY then STKQTY-(convert(int,(STKQTY/UOMQTY))*UOMQTY) else STKQTY end MINQTY "
								 * +
								 * " ,convert(int,(MAXSTKQTY/UOMQTY)) as INVMAXQTY,case when UOMQTY<=MAXSTKQTY then MAXSTKQTY-(convert(int,(MAXSTKQTY/UOMQTY))*UOMQTY) else MAXSTKQTY end MAXQTY"
								 * +
								 * " ,convert(int,(QTY/UOMQTY)) as INVQTY,case when UOMQTY<=QTY then QTY-(convert(int,(QTY/UOMQTY))*UOMQTY) else QTY end QTY"
								 */
                +" ,case when 0<ISNULL(UOMQTY,0) then (convert(float,(STKQTY/UOMQTY))) else STKQTY end INVMINQTY, STKQTY as MINQTY"	 
				+" ,case when 0<ISNULL(UOMQTY,0) then (convert(float,(MAXSTKQTY/UOMQTY))) else MAXSTKQTY end INVMAXQTY, MAXSTKQTY as MAXQTY,LOCDESC,LOC_TYPE_ID"	 
				+" ,case when 0<ISNULL(UOMQTY,0) then (convert(float,(QTY/UOMQTY))) else QTY end INVQTY ,QTY"
				+" ,ISNULL((SELECT VNAME  FROM ["+aPlant+"_VENDMST] D WHERE D.VENDNO= a.VENDNO),'') VNAME"
                + " from tempdb a Where plant<> '' AND (case when 0<ISNULL(UOMQTY,0) then (convert(float,(QTY/UOMQTY))) else QTY end) >= 0 "+allqty;
            }	
                   /* + "AND LOC =CASE WHEN (SELECT COUNT(LOC) FROM ["+ aPlant +"_LOCMST] WHERE LOC = A.LOC) >0 THEN (SELECT LOC FROM ["+ aPlant+"_INVMST] WHERE   QTY >= 0 AND ITEM=A.ITEM AND LOC=A.LOC  GROUP BY ITEM,LOC)"
                    + " WHEN (SELECT COUNT(LOC) FROM ["+ aPlant +"_LOCMST] WHERE LOC = A.LOC) = 0 THEN (SELECT LOC FROM ["+ aPlant+"_INVMST] WHERE   QTY > 0 AND ITEM=A.ITEM AND LOC=A.LOC GROUP BY ITEM,LOC)"
                    +" END";*/
                    sCondition=" order by a.item,a.loc";
                    arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
            }
            return arrList;
    }
    
    public ArrayList getInvListSummaryWithMinStock(Hashtable ht, String aPlant,
            String aItem, String productDesc, boolean iswithzeroQty,String loctypeid,String loctypeid2,String loctypeid3,String loc,String viewstatus,String UOM,String PARENT_PLANT,String vendname,String PARENT_PLANT1) throws Exception {
    ArrayList arrList = new ArrayList();
  //CHNAGECODE-001 DESC CHANGE BY BRUHAN INCLUDE REPLACE FUNCTION IN PRODDESC
    String sCondition=""; 
    if (productDesc.length() > 0 ) {
            if (productDesc.indexOf("%") != -1) {
                    productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
            }
     
	 sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
    }
  
    if (loc != null && loc!="") {
    	sCondition = sCondition+" AND LOC like '%"+loc+"%'";
    }
    
    if (loctypeid != null && loctypeid!="") {
		sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType(aPlant,loctypeid)+" )";
	}
    
    if (loctypeid2 != null && loctypeid2!="") {
		sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType2(aPlant,loctypeid2)+" )";
	}
    if (loctypeid3 != null && loctypeid3!="") {
		sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType3(aPlant,loctypeid3)+" )";
	}
    if (vendname.length()>0){
 	   vendname = StrUtils.InsertQuotes(vendname);
//	sCondition = sCondition + " AND VNAME LIKE '%"+vendname+"%' " ;
	sCondition= sCondition+" AND VENDNO in (select VENDNO from "+aPlant+"_VENDMST where VNAME like '"+vendname+"%')";
}
    
//   String extraCond = "";
    try {

            InvMstDAO _InvMstDAO = new InvMstDAO();
            _InvMstDAO.setmLogger(mLogger);
            if(viewstatus.equals("ByZeroQty"))
            {
             sCondition = sCondition
                            + " AND QTY=0 AND LOC NOT LIKE '%SHIPPINGAREA%' AND LOC NOT LIKE '%TEMP_TO%' group by a.item,a.loc,b.itemdesc,b.prd_cls_id,b.prd_dept_id,b.prd_brand_id,b.cost,b.itemtype,b.INVENTORYUOM,a.plant,B.VENDNO "; 
             			    
            }
            if(viewstatus.equals("ByMinQty"))
            {
             sCondition = sCondition
                            + " group by a.item,a.loc,b.itemdesc,b.prd_cls_id,b.prd_dept_id,b.prd_brand_id,b.cost,b.itemtype,b.INVENTORYUOM,a.plant,B.VENDNO " +
                            "    having (select sum(QTY) from ["+ aPlant + "_" + "Invmst] where item=a.item group by item) < (Select STKQTY from"  + "["+ aPlant + "_" + "itemmst] where ITEM=a.ITEM and STKQTY > 0 ) ";
            }
            if(viewstatus.equals("ByMaxQty"))
            {
             sCondition = sCondition
                            + " group by a.item,a.loc,b.itemdesc,b.prd_cls_id,b.prd_dept_id,b.prd_brand_id,b.cost,b.itemtype,b.INVENTORYUOM,a.plant,B.VENDNO " +
                            "    having (select sum(QTY) from ["+ aPlant + "_" + "Invmst] where item=a.item group by item) > (Select MAXSTKQTY from"  + "["+ aPlant + "_" + "itemmst] where ITEM=a.ITEM and MAXSTKQTY > 0 ) ";
            }
            String allqty="";
            if(viewstatus.equals("ByAllQty"))
            {
             sCondition = sCondition
                            + " group by a.item,a.loc,b.itemdesc,b.prd_cls_id,b.prd_dept_id,b.prd_brand_id,b.cost,b.itemtype,b.INVENTORYUOM,a.plant,B.VENDNO ";
             allqty=" AND (STKQTY!=0 OR MAXSTKQTY!=0) ";
            }
            if (!iswithzeroQty) {
                    //extraCond = " and QTY>0 ";
//            	extraCond = " and a.plant <> '' ";
            }
             // Added stkuom to the query
            //---Modified by Bruhan on Feb 27 2014, Description: To display location data's in uppercase
          //---Modified by Azees 31/8/19 Muti UOM
            String aQuery ="";
            String parentplant1empty="";
            if (!PARENT_PLANT1.equalsIgnoreCase("")) {
        		parentplant1empty = " case when 0<ISNULL(UOMQTY,0) then (convert(float,( isnull((select sum(QTY) from ["+PARENT_PLANT1+"_invmst] where item=a.item and loc like '%WAREHOUSE%'),0) /UOMQTY))) else isnull((select sum(QTY) from ["+PARENT_PLANT1+"_invmst] where item=a.item and loc like '%WAREHOUSE%'),0) end PARENT_INVQTY1";
        	} else {
        		parentplant1empty = " 0 as PARENT_INVQTY1";  // Default value or logic when PARENT_PLANT1 is empty
        	}
            if(UOM.length()>0){
            aQuery = "with tempdb as ( "
            				+ "select a.ITEM,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_dept_id,'') as PRD_DEPT_ID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,convert(float,isnull(b.COST,'')) as TOTCOST,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ aPlant +"_UOM u where u.UOM='"+UOM+"'),'') as STKUOM,b.itemtype as ITEMTYPE,UPPER(a.LOC) LOC,sum(QTY)as QTY ,"
            						/*+ "max(b.STKQTY) STKQTY,max(isnull(b.MAXSTKQTY,0)) MAXSTKQTY,"*/
            						+ "max(ISNULL(O.MINQTY,b.STKQTY)) STKQTY,max(ISNULL(O.MAXQTY,b.MAXSTKQTY)) MAXSTKQTY,"
            						+ "ISNULL((SELECT LOCDESC  FROM ["+ aPlant +"_LOCMST] D WHERE D.LOC= A.LOC),'') LOCDESC ,"
            						+ "ISNULL((SELECT LOC_TYPE_ID  FROM ["+ aPlant +"_LOCMST] D WHERE D.LOC= A.LOC),'') LOC_TYPE_ID,(select sum(QTY) from"
                            + "["
                            + aPlant
                            + "_"
                            + "invmst"
                            + "]"
                            + "where item=a.item group by item) as ITEMQTY,a.plant,'"+UOM+"' as  INVENTORYUOM,ISNULL((select u.QPUOM from "+ aPlant +"_UOM u where u.UOM='"+UOM+"'),1) as UOMQTY,B.VENDNO from "
                            /*+ "["
                            + aPlant
                            + "_"
                            + "invmst"
                            + "]"
                            + " A,"
                            + "["
                            + aPlant
                            + "_"
                            + "itemmst"
                            + "] "
                            
            + " as b where a.item=b.item */
                            + " ["+aPlant+"_invmst] A JOIN ["+aPlant+"_itemmst] as b ON a.item=b.item LEFT JOIN POS_OUTLET_MINMAX O ON O.ITEM=b.ITEM AND A.LOC=O.OUTLET AND O.PLANT=b.PLANT "                            
            +" WHERE a.item in (SELECT ITEM FROM "
                + aPlant
                + "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"+ aItem + "%')" + sCondition + ")"
                + " select plant,ITEM,ITEMDESC,PRDCLSID,PRD_DEPT_ID,PRD_BRAND_ID,TOTCOST,ITEMTYPE,LOC,INVENTORYUOM,STKUOM"
					/*
					 * +
					 * " ,convert(int,(STKQTY/UOMQTY)) as INVMINQTY,case when UOMQTY<=STKQTY then STKQTY-(convert(int,(STKQTY/UOMQTY))*UOMQTY) else STKQTY end MINQTY "
					 * +
					 * " ,convert(int,(MAXSTKQTY/UOMQTY)) as INVMAXQTY,case when UOMQTY<=MAXSTKQTY then MAXSTKQTY-(convert(int,(MAXSTKQTY/UOMQTY))*UOMQTY) else MAXSTKQTY end MAXQTY"
					 * +
					 * " ,convert(int,(QTY/UOMQTY)) as INVQTY,case when UOMQTY<=QTY then QTY-(convert(int,(QTY/UOMQTY))*UOMQTY) else QTY end QTY"
					 */
				+" ,case when 0<ISNULL(UOMQTY,0) then (convert(float,(STKQTY/UOMQTY))) else STKQTY end INVMINQTY, STKQTY as MINQTY"	 
				+" ,case when 0<ISNULL(UOMQTY,0) then (convert(float,(MAXSTKQTY/UOMQTY))) else MAXSTKQTY end INVMAXQTY, MAXSTKQTY as MAXQTY,LOCDESC,LOC_TYPE_ID"	 
				+" ,case when 0<ISNULL(UOMQTY,0) then (convert(float,(QTY/UOMQTY))) else QTY end INVQTY ,QTY"
				+" ,case when 0<ISNULL(UOMQTY,0) then (convert(float,( isnull((select sum(QTY) from ["+PARENT_PLANT+"_invmst] where item=a.item and loc!='Chinatown'),0) /UOMQTY))) else isnull((select sum(QTY) from ["+PARENT_PLANT+"_invmst] where item=a.item and loc!='Chinatown'),0) end PARENT_INVQTY"
				+" "+parentplant1empty+" "
				+" ,ISNULL((SELECT VNAME  FROM ["+aPlant+"_VENDMST] D WHERE D.VENDNO= a.VENDNO),'') VNAME"
                + " from tempdb a Where plant<> '' AND (case when 0<ISNULL(UOMQTY,0) then (convert(float,(QTY/UOMQTY))) else QTY end) >= 0 "+allqty;
            }
            else
            {
            	aQuery = "with tempdb as ( "
    				+ "select a.ITEM,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_dept_id,'') as PRD_DEPT_ID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,convert(float,isnull(b.COST,'')) as TOTCOST,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ aPlant +"_UOM u where u.UOM=(case when ''='' then ISNULL(b.INVENTORYUOM,'') else '' end)),'') as STKUOM,b.itemtype as ITEMTYPE,UPPER(a.LOC) LOC,sum(QTY)as QTY ,"
    						/*+ "max(b.STKQTY) STKQTY,max(isnull(b.MAXSTKQTY,0)) MAXSTKQTY,"*/
    						+ "max(ISNULL(O.MINQTY,b.STKQTY)) STKQTY,max(ISNULL(O.MAXQTY,b.MAXSTKQTY)) MAXSTKQTY,"
    						+ "ISNULL((SELECT LOCDESC  FROM ["+ aPlant +"_LOCMST] D WHERE D.LOC= A.LOC),'') LOCDESC,"
    						+ "ISNULL((SELECT LOC_TYPE_ID  FROM ["+ aPlant +"_LOCMST] D WHERE D.LOC= A.LOC),'') LOC_TYPE_ID ,(select sum(QTY) from"
                    + "["
                    + aPlant
                    + "_"
                    + "invmst"
                    + "]"
                    + "where item=a.item group by item) as ITEMQTY,a.plant,ISNULL(b.INVENTORYUOM,'') as  INVENTORYUOM,ISNULL((select u.QPUOM from "+ aPlant +"_UOM u where u.UOM=b.INVENTORYUOM),1) as UOMQTY,B.VENDNO from "
                    /*+ "["
                    + aPlant
                    + "_"
                    + "invmst"
                    + "]"
                    + " A,"
                    + "["
                    + aPlant
                    + "_"
                    + "itemmst"
                    + "] "
                    
    + " as b where a.item=b.item "*/
                    + " ["+aPlant+"_invmst] A JOIN ["+aPlant+"_itemmst] as b ON a.item=b.item LEFT JOIN POS_OUTLET_MINMAX O ON O.ITEM=b.ITEM AND A.LOC=O.OUTLET AND O.PLANT=b.PLANT "
    + " WHERE a.item in (SELECT ITEM FROM "
        + aPlant
        + "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"+ aItem + "%')" + sCondition + ")"
        + " select plant,ITEM,ITEMDESC,PRDCLSID,PRD_DEPT_ID,PRD_BRAND_ID,TOTCOST,ITEMTYPE,LOC,INVENTORYUOM,STKUOM"
						/*
						 * +
						 * " ,convert(int,(STKQTY/UOMQTY)) as INVMINQTY,case when UOMQTY<=STKQTY then STKQTY-(convert(int,(STKQTY/UOMQTY))*UOMQTY) else STKQTY end MINQTY "
						 * +
						 * " ,convert(int,(MAXSTKQTY/UOMQTY)) as INVMAXQTY,case when UOMQTY<=MAXSTKQTY then MAXSTKQTY-(convert(int,(MAXSTKQTY/UOMQTY))*UOMQTY) else MAXSTKQTY end MAXQTY"
						 * +
						 * " ,convert(int,(QTY/UOMQTY)) as INVQTY,case when UOMQTY<=QTY then QTY-(convert(int,(QTY/UOMQTY))*UOMQTY) else QTY end QTY"
						 */
        +" ,case when 0<ISNULL(UOMQTY,0) then (convert(float,(STKQTY/UOMQTY))) else STKQTY end INVMINQTY, STKQTY as MINQTY"	 
		+" ,case when 0<ISNULL(UOMQTY,0) then (convert(float,(MAXSTKQTY/UOMQTY))) else MAXSTKQTY end INVMAXQTY, MAXSTKQTY as MAXQTY,LOCDESC,LOC_TYPE_ID"	 
		+" ,case when 0<ISNULL(UOMQTY,0) then (convert(float,(QTY/UOMQTY))) else QTY end INVQTY ,QTY"
		+" ,case when 0<ISNULL(UOMQTY,0) then (convert(float,( isnull((select sum(QTY) from ["+PARENT_PLANT+"_invmst] where item=a.item and loc!='Chinatown'),0) /UOMQTY))) else isnull((select sum(QTY) from ["+PARENT_PLANT+"_invmst] where item=a.item and loc!='Chinatown'),0) end PARENT_INVQTY"
		+" ,"+parentplant1empty+" "
		+" ,ISNULL((SELECT VNAME  FROM ["+aPlant+"_VENDMST] D WHERE D.VENDNO= a.VENDNO),'') VNAME"
        + " from tempdb a Where plant<> '' AND (case when 0<ISNULL(UOMQTY,0) then (convert(float,(QTY/UOMQTY))) else QTY end) >= 0 "+allqty;
    }	
           /* + "AND LOC =CASE WHEN (SELECT COUNT(LOC) FROM ["+ aPlant +"_LOCMST] WHERE LOC = A.LOC) >0 THEN (SELECT LOC FROM ["+ aPlant+"_INVMST] WHERE   QTY >= 0 AND ITEM=A.ITEM AND LOC=A.LOC  GROUP BY ITEM,LOC)"
            + " WHEN (SELECT COUNT(LOC) FROM ["+ aPlant +"_LOCMST] WHERE LOC = A.LOC) = 0 THEN (SELECT LOC FROM ["+ aPlant+"_INVMST] WHERE   QTY > 0 AND ITEM=A.ITEM AND LOC=A.LOC GROUP BY ITEM,LOC)"
            +" END";*/
            sCondition=" order by a.item,a.loc";
            arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
    }
    return arrList;
}
    
    
 //below method chaned by Bruhan to get shippingarea,temp locations having qty in invmst if loc,loctypeid search is available on 14/jan/2014   
    public ArrayList getInvListSummaryWithOutPrice(Hashtable ht, String aPlant,String aItem,String productDesc,String loctypeid,String loctypeid2,String loctypeid3,String loc) throws Exception {
            ArrayList arrList = new ArrayList();
          //CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
            String sCondition ="";
    	    if (productDesc.length() > 0 ) {
            if (productDesc.indexOf("%") != -1) {
                    productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
            }
            
             sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
    	    }
    	  	        
            if (!loc.equalsIgnoreCase("") && !loc.equalsIgnoreCase("")) {
            	sCondition = sCondition+" AND LOC like '%"+loc+"%'";
            }
            
            if (!loctypeid.equalsIgnoreCase("") && !loctypeid.equalsIgnoreCase("")) {
    			sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType(aPlant,loctypeid)+" )";
    		}
            
            if (!loctypeid2.equalsIgnoreCase("") && !loctypeid2.equalsIgnoreCase("")) {
    			sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType2(aPlant,loctypeid2)+" )";
    		}
            if (!loctypeid3.equalsIgnoreCase("") && !loctypeid3.equalsIgnoreCase("")) {
				sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType3(aPlant,loctypeid3)+" )";
			}


            try {
                    InvMstDAO _InvMstDAO = new InvMstDAO();
                    _InvMstDAO.setmLogger(mLogger);
                   sCondition = sCondition  + " group by a.item,a.loc,a.userfld4,b.itemdesc,b.prd_cls_id,b.prd_dept_id,b.MODEL,b.itemtype,b.prd_brand_id,b.stkuom order by a.item ,a.loc  ";

                    // Added stkuom to the query
                    //-----Modified by Bruhan on Feb 27 2014, Description: To display location data's in uppercase
                    String aQuery = "select a.ITEM,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_dept_id,'') as PRD_DEPT_ID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,isnull(b.MODEL,'') as MODEL,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ aPlant +"_UOM u where u.UOM=b.STKUOM),'') as STKUOM,b.itemtype as ITEMTYPE,UPPER(a.LOC) LOC,a.UserFld4 as BATCH, sum(QTY) QTY,sum(b.UNITPRICE)as UNITPRICE   "
                                    + " from["
                                    + aPlant
                                    + "_"
                                    + "invmst"
                                    + "]"
                                    + " A,"
                                    + "["
                                    + aPlant
                                    + "_"
                                    + "itemmst"
                                    + "]"
                                    + " as b where a.item=b.item  and a.item in (SELECT ITEM FROM "
                                    + aPlant
                                    + "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"+ aItem + "%')"
                                    + " and b.NONSTKFLAG<>'Y' and QTY>0 ";
                    arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
            }
            return arrList;
    }
    
	 /* ************Modification History*********************************
	   Sep 23 2014, Description: To include CRBY
	    April 29 2015, Description: To include REMARKS
	*/
    public ArrayList getStockTakeDetails(Hashtable ht, String aPlant,String aItem,String productDesc,String aPrdclsId,String aPrdBrandId,String aItemType,String afrmDate,String atoDate,String loctypeid,String aUser) throws Exception {
           ArrayList arrList = new ArrayList();
           
           if (productDesc.indexOf("%") != -1) {
                   productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
           }
           //String sCondition = " and A.ITEMDESC like '%"  + StrUtils.InsertQuotes(productDesc) + "%' ";
           String sCondition = "  ";
           if (loctypeid != null && loctypeid!="") {
   			sCondition = sCondition + "  AND LOC in(select LOC from " +aPlant+"_LOCMST where LOC_TYPE_ID like '%"+loctypeid+"%')";
   									}
           
           try {
                                       
                   InvMstDAO _InvMstDAO = new InvMstDAO();
                   _InvMstDAO.setmLogger(mLogger);
                   //sCondition = sCondition  + " group by a.plant,a.item,a.loc,a.userfld4,b.itemdesc,b.prd_cls_id,b.itemtype,b.prd_brand_id,b.stkuom order by a.item ,a.loc  ";
                   if(aItem.length()>0)
                                   {
                             sCondition=sCondition + " " + "AND UPPER(A.ITEM) LIKE '"+ aItem + "%' ";
                             sCondition=sCondition + " " + "AND A.ITEM IN (SELECT ITEM FROM ["+ aPlant +"_ALTERNATE_ITEM_MAPPING] WHERE UPPER(ALTERNATE_ITEM_NAME) LIKE '"+ aItem + "%') ";
                                   }
                   if(productDesc.length()>0)
                                   {
                                     sCondition=sCondition + " " + "AND A.ITEM IN (SELECT ITEM FROM ["+ aPlant +"_ITEMMST] WHERE PLANT ='"+aPlant+"' AND ITEM LIKE '"+ aItem + "%' AND REPLACE(ITEMDESC,' ','') LIKE '"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%')";
                                   }
                   if(aPrdclsId.length()>0)
                                   {
                                     sCondition=sCondition + " " + "AND A.ITEM IN (SELECT ITEM FROM ["+ aPlant +"_ITEMMST] WHERE PLANT ='"+aPlant+"' AND ITEM LIKE '"+ aItem + "%' AND PRD_CLS_ID LIKE '"+ aPrdclsId + "%')";
                                   }
                   if(aPrdBrandId.length()>0)
                                   {
                                     sCondition=sCondition + " " + "AND A.ITEM IN (SELECT ITEM FROM ["+ aPlant +"_ITEMMST] WHERE PLANT ='"+aPlant+"' AND ITEM LIKE '"+ aItem + "%' AND PRD_BRAND_ID LIKE '"+ aPrdBrandId + "%')";
                                   }
                   if(aItemType.length()>0)
                                   {
                                     sCondition=sCondition + " " + "AND A.ITEM IN (SELECT ITEM FROM ["+ aPlant +"_ITEMMST] WHERE PLANT ='"+aPlant+"' AND ITEM LIKE '"+ aItem + "%' AND ITEMTYPE LIKE '"+ aItemType + "%')";
                                   }
                   
                                   if (afrmDate.length() > 0) {
                                       afrmDate=  afrmDate.substring(6)+afrmDate.substring(3,5) +afrmDate.substring(0,2);
                                       sCondition = sCondition + " AND    substring(a.TRANDATE,7,4)+substring(a.TRANDATE,4,2) +substring(a.TRANDATE,1,2) >= '" + afrmDate
                                                       + "'  ";
                                       if (atoDate.length() > 0) {
                                               atoDate=  atoDate.substring(6)+atoDate.substring(3,5) +atoDate.substring(0,2);
                                               sCondition =  sCondition + " AND     substring(a.TRANDATE,7,4)+substring(a.TRANDATE,4,2) +substring(a.TRANDATE,1,2)  <= '" + atoDate
                                                               + "'  ";
                                           }
                                   } else {
                                           if (atoDate.length() > 0) {
                                                   atoDate=  atoDate.substring(6)+atoDate.substring(3,5) +atoDate.substring(0,2);
                                                   sCondition = sCondition + " AND     substring(a.TRANDATE,7,4)+substring(a.TRANDATE,4,2) +substring(a.TRANDATE,1,2)  <= '" + atoDate
                                                               + "'  ";
                                           }
                                   }
                 
					if(aUser.length()>0)
                    {
                        sCondition=sCondition + " " + " AND A.USERID='" + aUser + "'";
                    }    
                   sCondition= sCondition  + "  AND (A.LOC NOT LIKE  'SHIPPINGAREA_%' OR A.LOC NOT LIKE  'TEMP_TO_%') ORDER BY substring(a.TRANDATE,7,4)+substring(a.TRANDATE,4,2) +substring(a.TRANDATE,1,2) desc,A.ITEM asc,A.LOC asc";
                 //---Modified by Bruhan on Feb 27 2014, Description: To display location data's in uppercase               
                  String aQuery = "select *," 
                		  +" convert(int,(INVQTY/(ISNULL((select u.QPUOM from "+ aPlant +"_UOM u where u.UOM=A.STKUOM),1)))) QTY,"
                		  //+" STOCKQTY-(convert(int,(INVQTY/(ISNULL((select u.QPUOM from "+ aPlant +"_UOM u where u.UOM=A.STKUOM),1))))) as DIFFQTY,"
                		  +" case when (ISNULL((select u.QPUOM from "+ aPlant +"_UOM u where u.UOM=A.STKUOM),1))<=INVQTY then INVQTY - (convert(int,(INVQTY/(ISNULL((select u.QPUOM from "+ aPlant +"_UOM u where u.UOM=A.STKUOM),1))))*(ISNULL((select u.QPUOM from "+ aPlant +"_UOM u where u.UOM=A.STKUOM),1))) else INVQTY end PCSQTY,"
                      +"  (Select ITEMDESC FROM ["+ aPlant +"_ITEMMST] WHERE PLANT = A.PLANT AND ITEM=A.ITEM)  ITEMDESC,"
                              +"  (Select prd_cls_id FROM ["+ aPlant +"_ITEMMST] WHERE PLANT = A.PLANT AND  ITEM=A.ITEM)   PRDCLSID,"
                              +"  (Select PRD_BRAND_ID FROM ["+ aPlant +"_ITEMMST] WHERE PLANT = A.PLANT AND  ITEM=A.ITEM)  PRD_BRAND_ID,"
                              +"  (Select ITEMTYPE FROM ["+ aPlant +"_ITEMMST] WHERE PLANT = A.PLANT AND  ITEM=A.ITEM) ITEMTYPE,"
                              +"  STKUOM,"
                              +"  isnull((Select ISNULL(COST,'') COST FROM ["+ aPlant +"_ITEMMST] WHERE PLANT = A.PLANT AND  ITEM=A.ITEM),0)  UNITCOST"
                              +   " from ( "
                                      +     "SELECT distinct ISNULL(I.PLANT,S.PLANT)as PLANT, ISNULL(I.ITEM,S.ITEM)as ITEM, "
                                      +" ISNULL(S.CR_DATE,'') TRANDATE," 
                                      +" ISNULL(UPPER(I.LOC),UPPER(S.LOC))as LOC,"
                                      +" ISNULL(I.USERFLD4,S.BATCH) as BATCH,"
                                      +" ISNULL(SUM(I.QTY), 0) AS INVQTY, "
                                      +" ISNULL(S.QTY, 0) AS STOCKQTY," 
                                      +" ISNULL(S.DIFFQTY, 0) AS STOCKDIFFQTY," 
                                                        
                                   //+" ISNULL(S.QTY, 0)  - ISNULL(I.QTY, 0)   AS DIFFQTY,"
								   +" ISNULL(S.CRBY,'') USERID, "
								   +" ISNULL(S.REMARKS,'') REMARKS, "
								   +" (ISNULL(S.UOM,(Select INVENTORYUOM FROM ["+ aPlant +"_ITEMMST] WHERE PLANT = I.PLANT AND  ITEM=I.ITEM))) STKUOM,S.STKNO,S.STATUS  "
                                   +" FROM ["+ aPlant +"_stktake] S"
                                   +" FULL OUTER JOIN ["+ aPlant +"_invmst] I"
                                   +" ON  s.PLANT = I.PLANT  AND s.ITEM = I.ITEM AND s.LOC = I.LOC AND "
                               +" S.BATCH =I.USERFLD4  GROUP BY I.PLANT,S.PLANT,S.CR_DATE  , s.ITEM , I.ITEM , s.LOC , I.LOC ,  S.BATCH ,I.USERFLD4 ,S.QTY,S.CRBY,S.REMARKS,S.UOM,S.DIFFQTY,S.STKNO,S.STATUS  )A where A.PLANT='" + aPlant + "' " ; // AND  A.TRANDATE ='11/12/2013'
                               
                   arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

           } catch (Exception e) {
                   this.mLogger.exception(this.printLog, "", e);
           }
           return arrList;
    }


    
    //below method chaned by Bruhan to get shippingarea,temp locations having qty in invmst if loc,loctypeid search is available on 14/jan/2014
    public ArrayList getInvListSummaryWithAverageCost(Hashtable ht, String afrmDate,String atoDate,String plant,String aItem,String productDesc,String currencyid,String baseCurrency,String loctypeid,String loc) throws Exception {
        ArrayList arrList = new ArrayList();
      //CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
        String sCondition ="";//,sDateCod="";
        if (productDesc.length() > 0 ) {
	        if (productDesc.indexOf("%") != -1) {
	                productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
	        }
            sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
        }
       String sRecvCondition=""; 
        if (afrmDate.length() > 0) {
                sRecvCondition = sRecvCondition + " and substring(CRAT,1,8)  >= '" + afrmDate
                                + "'  ";
                if (atoDate.length() > 0) {
                        sRecvCondition = sRecvCondition + " and substring(CRAT,1,8)   <= '" + atoDate
                                        + "'  ";
                }
        } else {
                if (atoDate.length() > 0) {
                        sRecvCondition = sRecvCondition + " and substring(CRAT,1,8) <= '" + atoDate
                                        + "'  ";
                }
        }
    /*    if (afrmDate.length() > 0) {
                sCondition = sCondition + " and A.CRAT  >= '" + afrmDate
                                + "'  ";
                if (atoDate.length() > 0) {
                        sCondition = sCondition + " and A.CRAT   <= '" + atoDate
                                        + "'  ";
                }
        } else {
                if (atoDate.length() > 0) {
                        sCondition = sCondition + " and A.CRAT <= '" + atoDate
                                        + "'  ";
                }
        }*/
        
        if (loc != null && loc!="") {
        	sCondition = sCondition+" AND LOC like '%"+loc+"%'";
        }
        
        if (loctypeid != null && loctypeid!="") {
			sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType(plant,loctypeid)+" )";
		}
 
      String ConvertUnitCostToOrderCurrency = " (CAST(ISNULL(UNITCOST,0) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] " +
                                            "   WHERE  CURRENCYID='"+baseCurrency+"')*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] " +
                                            "   WHERE  CURRENCYID=ISNULL((SELECT CURRENCYID from "+plant+"_POHDR WHERE PONO =R.PONO),'"+baseCurrency+"')) AS DECIMAL(20,5)) ) ";
    
      StringBuffer sql = new StringBuffer(" (SELECT CASE WHEN  (SELECT COUNT(CURRENCYID) FROM ["+plant+"_RECVDET] WHERE ITEM=B.ITEM AND CURRENCYID IS NOT NULL AND TRAN_TYPE IN('IB')  AND UNITCOST >0 AND UNITCOST IS NOT NULL "+sRecvCondition+")>0  THEN ");
    sql.append(" (Select ISNULL(CAST(ISNULL(SUM(A.UNITCOST),0)/SUM(A.RECQTY) AS DECIMAL(20,5)),0) AS AVERGAGE_COST from ");
    sql.append(" (select RECQTY,CAST("+ConvertUnitCostToOrderCurrency+" * CAST((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')AS DECIMAL(20,5)) ");
    sql.append(" / CAST((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=(SELECT CURRENCYID from "+plant+"_POHDR WHERE PONO =R.PONO)) AS DECIMAL(20,5)) ");
    sql.append("   * RECQTY AS DECIMAL(20,5)) AS UNITCOST ");
    sql.append("   from "+plant+"_RECVDET R where item =b.ITEM AND UNITCOST IS NOT NULL  AND UNITCOST >0  AND UNITCOST <> ''  AND TRAN_TYPE ='IB' AND ORDQTY >0 " + sRecvCondition +"  ) A )  ");
    //Commented by samatha on 29102013 as conversion is wrong
    //  sql.append("   ELSE   CAST(((B.COST)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+baseCurrency+"')) /(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')AS DECIMAL(20,4))   END ) AS AVERAGE_COST   ");
    sql.append("   ELSE   CAST(((B.COST)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')) AS DECIMAL(20,5))   END ) AS AVERAGE_COST   ");

        
        try {
                InvMstDAO _InvMstDAO = new InvMstDAO();
                _InvMstDAO.setmLogger(mLogger);
               sCondition = sCondition  + " group by a.item,a.loc,a.userfld4,b.itemdesc,b.prd_cls_id,b.prd_brand_id,b.itemtype,b.stkuom,b.INVENTORYUOM ) B order by item ,LOC ";

                // Added stkuom to the query
               //---Modified by Bruhan on Feb 27 2014, Description: To display location data's in uppercase
                 String aQuery = "SELECT *,case when 0<ISNULL(UOMQTY,0) then (convert(int,(QTY*UOMQTY))) else QTY end STKQTY,convert(int,QTY/UOMQTY) as INVUOMQTY,"
                 		+ ""+sql.toString()+" FROM (select a.ITEM,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ plant +"_UOM u where u.UOM=b.STKUOM),'') as STKUOM,b.itemtype as ITEMTYPE,UPPER(a.LOC) LOC,a.UserFld4 as BATCH, sum(QTY) QTY,ISNULL(CAST(((sum(b.UNITPRICE))*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')) AS DECIMAL(20,4)),0) as LIST_PRICE,sum(b.COST) as COST,ISNULL(b.INVENTORYUOM,'') as  INVENTORYUOM," + 
                 		" ISNULL((select u.QPUOM from [" +plant+ "_UOM] u where u.UOM=b.INVENTORYUOM),1) as UOMQTY    from "
                                + "["
                                + plant
                                + "_"
                                + "invmst"
                                + "]"
                                + " A,"
                                + "["
                                + plant
                                + "_"
                                + "itemmst"
                                + "]"
                                + " as b where a.item=b.item  and a.item in (SELECT ITEM FROM "
                                + plant
                                + "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"+ aItem + "%')"
                                + "and QTY>0 ";
                arrList = _InvMstDAO.selectForReport(aQuery, ht,sCondition);

        } catch (Exception e) {
                this.mLogger.exception(this.printLog, "", e);
        }
        return arrList;
}
    
    

	
 


    
     	



 //below method chaned by Bruhan to get shippingarea,temp locations having qty in invmst if loc,loctypeid search is available on 14/jan/2014     
    //Multi UOM Changes by Azees 1.9.19
public ArrayList getInvListSummaryWithExpireDate(Hashtable ht, String aPlant,String aItem,String productDesc,String expireDt,String loctypeid,String loctypeid2,String loctypeid3,String loc,String viewstatus,String UOM) throws Exception {
            ArrayList arrList = new ArrayList();
          //CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
            String sCondition ="";
            if (productDesc.length() > 0 ) {
            if (productDesc.indexOf("%") != -1) {
                    productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
            }
            
             sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
            }
            
            if (loctypeid != null && loctypeid!="") {
    			sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType(aPlant,loctypeid)+" )";
    		}
            if (loctypeid2 != null && loctypeid2!="") {
    			sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType2(aPlant,loctypeid2)+" )";
    		}
            if (loctypeid3 != null && loctypeid3!="") {
				sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType3(aPlant,loctypeid3)+" )";
			}

            String SysDate = DateUtils.getDate();
                        
            if (expireDt.length() > 0 && (viewstatus.equals("ByExpiryDate"))) {
            	String expireFDt="";
            	if (loc.equals("ByMail")) {
            		expireFDt =   " AND ISNULL(A.EXPIREDATE,'')<>'' AND  SUBSTRING(A.EXPIREDATE, 7, 4) +  SUBSTRING(A.EXPIREDATE, 4, 2)+ SUBSTRING(A.EXPIREDATE, 1, 2)  "+
            				" > = SUBSTRING('"+SysDate+"', 7, 4) +  SUBSTRING('"+SysDate+"', 4, 2)+ SUBSTRING('"+SysDate+"', 1, 2)  ";
            		loc="";
            	}
             expireDt = expireFDt+  " AND ISNULL(A.EXPIREDATE,'')<>'' AND  SUBSTRING(A.EXPIREDATE, 7, 4) +  SUBSTRING(A.EXPIREDATE, 4, 2)+ SUBSTRING(A.EXPIREDATE, 1, 2)  "+
                " < = SUBSTRING('"+expireDt+"', 7, 4) +  SUBSTRING('"+expireDt+"', 4, 2)+ SUBSTRING('"+expireDt+"', 1, 2)  ";
             
            }
            /*else if (expireDt.length() == 0 && (viewstatus.equals("ByExpiryDate")))
            {
            	  expireDt =   " AND ISNULL(A.EXPIREDATE,'')<>'' AND  SUBSTRING(A.EXPIREDATE, 7, 4) +  SUBSTRING(A.EXPIREDATE, 4, 2)+ SUBSTRING(A.EXPIREDATE, 1, 2)  "+
                          " < = SUBSTRING('"+SysDate+"', 7, 4) +  SUBSTRING('"+SysDate+"', 4, 2)+ SUBSTRING('"+SysDate+"', 1, 2)  ";
            }*/
            else if (expireDt.length() == 0 && (viewstatus.equals("ByExpiryDate")))
            {
            	  expireDt =   " AND ISNULL(A.EXPIREDATE,'')<>'' AND  SUBSTRING(A.EXPIREDATE, 7, 4) +  SUBSTRING(A.EXPIREDATE, 4, 2)+ SUBSTRING(A.EXPIREDATE, 1, 2)  "+
                          " < = SUBSTRING('"+SysDate+"', 7, 4) +  SUBSTRING('"+SysDate+"', 4, 2)+ SUBSTRING('"+SysDate+"', 1, 2)  ";
            }
            else if (expireDt.length() > 0 && (viewstatus.equals("ViewAll")))
            {
            	  expireDt =   "";
            }
            if (loc != null && loc!="") {
            	sCondition = sCondition+" AND LOC like '%"+loc+"%'";
            }
            try {
                    InvMstDAO _InvMstDAO = new InvMstDAO();
                    _InvMstDAO.setmLogger(mLogger);
                   sCondition = sCondition  + " group by a.item,a.loc,a.userfld4,b.itemdesc,b.prd_cls_id,b.prd_dept_id,b.itemtype,b.prd_brand_id,b.INVENTORYUOM,a.EXPIREDATE order by a.item,a.loc ";

                    // Added stkuom to the query
                    //---Modified by Bruhan on Feb 28 2014, Description: To display location data's in uppercase
                     String aQuery = "";
                     if(UOM.length()>0){
                    	 aQuery = "select a.ITEM,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_dept_id,'') as PRD_DEPT_ID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ aPlant +"_UOM u where u.UOM='"+UOM+"'),'') as STKUOM,b.itemtype as ITEMTYPE,UPPER(a.LOC) LOC,a.UserFld4 as BATCH, sum(QTY) STKQTY,sum(b.UNITPRICE)as UNITPRICE, isnull(a.EXPIREDATE, '') as EXPIREDATE ," +
                                 " '"+UOM+"' as  INVENTORYUOM,convert(int,(sum(QTY)/(ISNULL((select u.QPUOM from " +aPlant+ "_UOM u where u.UOM='"+UOM+"'),1)))) as INVUOMQTY," +
                                 " case when (ISNULL((select u.QPUOM from " +aPlant+ "_UOM u where u.UOM='"+UOM+"'),1))<=sum(QTY) then sum(QTY)-(convert(int,(sum(QTY)/(ISNULL((select u.QPUOM from " +aPlant+ "_UOM u where u.UOM='"+UOM+"'),1))))*(ISNULL((select u.QPUOM from " +aPlant+ "_UOM u where u.UOM='"+UOM+"'),1))) else sum(QTY) end QTY,ISNULL((SELECT LOCDESC  FROM ["+ aPlant +"_LOCMST] D WHERE D.LOC= A.LOC),'') LOCDESC, " +
                                 " CASE WHEN ISNULL(A.EXPIREDATE,'')='' then '#000000'" + 
                                 " WHEN SUBSTRING(A.EXPIREDATE, 7, 4) +  SUBSTRING(A.EXPIREDATE, 4, 2)+ SUBSTRING(A.EXPIREDATE, 1, 2)  <= CONVERT(VARCHAR(8), GETDATE(), 112) then '#FF0000'" + 
                                 " else '#000000' END AS COLORCODE from"
                                                + "["
                                                + aPlant
                                                + "_"
                                                + "invmst"
                                                + "]"
                                                + " A,"
                                                + "["
                                                + aPlant
                                                + "_"
                                                + "itemmst"
                                                + "]"
                                                + " as b where a.item=b.item  and a.item in (SELECT ITEM FROM "
                                                + aPlant
                                                + "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"+ aItem + "%')"
                                                + " and b.NONSTKFLAG<>'Y' and QTY>0  "+expireDt;
                     }
                     else {
                     aQuery = "select a.ITEM,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_dept_id,'') as PRD_DEPT_ID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ aPlant +"_UOM u where u.UOM=(case when '"+UOM+"'='' then ISNULL(b.INVENTORYUOM,'') else '"+UOM+"' end)),'') as STKUOM,b.itemtype as ITEMTYPE,UPPER(a.LOC) LOC,a.UserFld4 as BATCH, sum(QTY) STKQTY,sum(b.UNITPRICE)as UNITPRICE, isnull(a.EXPIREDATE, '') as EXPIREDATE ," +
                     " ISNULL(b.INVENTORYUOM,'') as  INVENTORYUOM,sum(QTY) as INVUOMQTY," +
                     " case when (1 <= ISNULL((select u.QPUOM from " +aPlant+ "_UOM u where u.UOM=b.INVENTORYUOM),1)) then sum(QTY)*(ISNULL((select u.QPUOM from " +aPlant+ "_UOM u where u.UOM=b.INVENTORYUOM),1)) else sum(QTY) end QTY,ISNULL((SELECT LOCDESC  FROM ["+ aPlant +"_LOCMST] D WHERE D.LOC= A.LOC),'') LOCDESC,ISNULL((SELECT LOC_TYPE_ID  FROM ["+ aPlant +"_LOCMST] D WHERE D.LOC= A.LOC),'') LOC_TYPE_ID, " +
                     " CASE WHEN ISNULL(A.EXPIREDATE,'')='' then '#000000'" + 
                     " WHEN SUBSTRING(A.EXPIREDATE, 7, 4) +  SUBSTRING(A.EXPIREDATE, 4, 2)+ SUBSTRING(A.EXPIREDATE, 1, 2)  <= CONVERT(VARCHAR(8), GETDATE(), 112) then '#FF0000'" + 
                     " else '#000000' END AS COLORCODE from "
                                    + "["
                                    + aPlant
                                    + "_"
                                    + "invmst"
                                    + "]"
                                    + " A,"
                                    + "["
                                    + aPlant
                                    + "_"
                                    + "itemmst"
                                    + "]"
                                    + " as b where a.item=b.item  and a.item in (SELECT ITEM FROM "
                                    + aPlant
                                    + "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"+ aItem + "%')"
                                    + " and b.NONSTKFLAG<>'Y' and QTY>0  "+expireDt;                     
                     }
                    arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
            }
            return arrList;
    }


	// End code modified by Bruhan for product brand on 11/9/12
	public ArrayList getItemListForMinStkQty(Hashtable ht, String aPlant,
			boolean isWithZero) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
			if (!isWithZero) {

				sCondition = " and ISNULL((SELECT SUM(QTY) FROM ["
						+ aPlant
						+ "_invmst]    WHERE ITEM=A.ITEM  GROUP BY ITEM  ),0)<>'0'";
			}

			String aQuery = "select ITEM,ITEMDESC,STKQTY,STKUOM FROM " + "["
					+ aPlant + "_" + "ITEMMST" + "]"
					+ " A WHERE STKQTY>ISNULL((SELECT SUM(QTY) FROM " + "["
					+ aPlant + "_" + "invmst" + "]"
					+ " WHERE ITEM=A.ITEM  GROUP BY ITEM ),0) " + sCondition;
			arrList = _InvMstDAO.selectForReport(aQuery, ht, "");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}

	public ArrayList getInvListSumTotalByItem(Hashtable ht, String aPlant,
			String aFlag) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
			sCondition = " group by a.item";
			if (aFlag.equalsIgnoreCase("Y")) {
				sCondition = sCondition + " having sum(b.stkqty)>sum(a.QTY) ";
			}

			String aQuery = "select a.ITEM,  sum(QTY)as QTY,sum(b.stkqty) from "
					+ "["
					+ aPlant
					+ "_"
					+ "invmst"
					+ "]"
					+ " A,"
					+ "["
					+ aPlant
					+ "_"
					+ "itemmst"
					+ "]"
					+ " as b where a.item=b.item  ";
			arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}

	public ArrayList getInvListSumTotalByItemForCond(Hashtable ht,String aPlant, String prodDesc) {// ,String
		// aFlag
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		String prodCond = "";
		
		try {
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
			sCondition = " group by a.item order by a.item";
			
			

			//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
			if (prodDesc.length() > 0 ) {
				prodDesc = StrUtils.InsertQuotes(prodDesc);
				if (prodDesc.indexOf("%") != -1) {
					prodDesc = prodDesc.substring(0, prodDesc.indexOf("%") - 1);
				}
				prodCond = " AND a.ITEM in (select ITEM FROM " + aPlant
						+ "_ITEMMST WHERE REPLACE(ITEMDESC,' ','') LIKE '%"+StrUtils.InsertQuotes(prodDesc.replaceAll(" ",""))+"%')" ;	
                                             
			}

			String aQuery = "select a.ITEM,  sum(QTY)as QTY,sum(b.stkqty) from "
					+ "["
					+ aPlant
					+ "_"
					+ "invmst"
					+ "]"
					+ " A,"
					+ "["
					+ aPlant
					+ "_"
					+ "itemmst"
					+ "]"
					+ " as b where a.item=b.item  " + prodCond;
			arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}
	
	public ArrayList getInvListSumTotalByItemForCondWithExpDate(Hashtable ht,
			String aPlant, String prodDesc) {// ,String
		// aFlag
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		String prodCond = "";
		try {
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
			sCondition = " group by a.item order by a.item";

			//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
			if (prodDesc.length() > 0 ) {
				prodDesc = StrUtils.InsertQuotes(prodDesc);
				if (prodDesc.indexOf("%") != -1) {
					prodDesc = prodDesc.substring(0, prodDesc.indexOf("%") - 1);
				}
				prodCond = " AND a.ITEM in (select ITEM FROM " + aPlant
						+ "_ITEMMST WHERE REPLACE(ITEMDESC,' ','') LIKE '%"+StrUtils.InsertQuotes(prodDesc.replaceAll(" ",""))+"%')" ;
			}

			String aQuery = "select a.ITEM,  sum(QTY)as QTY,sum(b.stkqty) from "
					+ "["
					+ aPlant
					+ "_"
					+ "invmst"
					+ "]"
					+ " A,"
					+ "["
					+ aPlant
					+ "_"
					+ "itemmst"
					+ "]"
					+ " as b where a.item=b.item  " + prodCond;
			arrList = _InvMstDAO.selectForReportWithExpDate(aQuery, ht, sCondition);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}

	public ArrayList getInvListSummaryGroupByProd(String aPlant, String aItem,
	String productDesc, Hashtable ht,String loc,String loctypeid,String loctypeid2,String loctypeid3) throws Exception {
	ArrayList arrList = new ArrayList();
	//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
	    String sCondition=""; 
            String aLocCond ="";
	    if (productDesc.length() > 0 ) {
                if (productDesc.indexOf("%") != -1) {
                        productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
                }
         
		 sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
	    }
	    if (!loctypeid.equalsIgnoreCase("") && !loctypeid.equalsIgnoreCase("")) {
			sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType(aPlant,loctypeid)+" )";
		}
	    
	    if (!loctypeid2.equalsIgnoreCase("") && !loctypeid2.equalsIgnoreCase("")) {
			sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType2(aPlant,loctypeid2)+" )";
		}
	    if (!loctypeid3.equalsIgnoreCase("") && !loctypeid3.equalsIgnoreCase("")) {
			sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType3(aPlant,loctypeid3)+" )";
		}


            if(loc.length()>0){
            loc = "  AND A.LOC like'%"+loc+"%'";
                aLocCond = ", a.loc ";
            }
		try {
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
			sCondition = sCondition
				+ " group by a.item,a.loc,b.ITEMDESC,b.prd_cls_id,b.prd_dept_id,b.MODEL,b.itemtype,b.prd_brand_id,b.stkuom"+aLocCond +" Order by a.item,a.loc";

			// Added stkuom to the query
			////-----Modified by Bruhan on Feb 27 2014, Description: To display location data's in uppercase
			String aQuery = "select a.ITEM,UPPER(a.LOC) LOC,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_dept_id,'') as PRD_DEPT_ID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,isnull(b.MODEL,'') as MODEL,b.itemtype as ITEMTYPE,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ aPlant +"_UOM u where u.UOM=b.STKUOM),'') as STKUOM, sum(QTY) QTY  from "
					+ "["
					+ aPlant
					+ "_"
					+ "invmst"
					+ "]"
					+ " A,"
					+ "["
					+ aPlant
					+ "_"
					+ "itemmst"
					+ "]"
					+ " as b where a.item=b.item and a.PLANT = b.PLANT and a.PLANT = '"+ aPlant+"' and a.item in (SELECT ITEM FROM "
					+ aPlant
					+ "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"
					+ aItem + "%') AND b.NONSTKFLAG<>'Y' AND  QTY>0 "+loc;
			arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}
	
	
	
	public ArrayList getInvWithOpenCloseBal(String aPlant, String aItem,String productDesc, Hashtable ht,String afrmDate,String atoDate) throws Exception {
		ArrayList arrList = new ArrayList();
		//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
		    String sCondition=""; 
		    String sDtCond="",sAfterDtCond="";//,sBomDtCond="",sAfterBomDtCond=""; 
//	            String aLocCond ="";
		    if (productDesc.length() > 0 ) {
	                if (productDesc.indexOf("%") != -1) {
	                        productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
	                }
	         
			 sCondition = " and replace(ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
		    }
		    if (afrmDate.length() > 0) {

		    	sDtCond = sDtCond + " AND   REPLACE(TRANDATE,'-','') >= '"+ afrmDate + "'  ";
				if (atoDate.length() > 0) {
					sDtCond = sDtCond + " AND  REPLACE(TRANDATE,'-','')<= '" + atoDate + "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sDtCond = sDtCond + "   REPLACE(TRANDATE,'-','') <= '" + atoDate + "'  ";
				}
			}
		    if (atoDate.length() > 0) {
		    	sAfterDtCond = sAfterDtCond + " AND  REPLACE(TRANDATE,'-','') > '" + atoDate + "'  ";
			}
		    
		    
		    	String locCond ="  " ; //and LOC in (select LOC from ["+ aPlant+ "_LOCMST])
		        
			try {
				InvMstDAO _InvMstDAO = new InvMstDAO();
				_InvMstDAO.setmLogger(mLogger);
				
				String aQuery = "select plant,ITEM,ITEMDESC,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ aPlant +"_UOM u where u.UOM=a.STKUOM),'') as STKUOM,PRD_CLS_ID,ITEMTYPE,PRD_BRAND_ID,(STOCKONHAND+(TOTISS-TOTISSREV)-(TOTRECV-TOTRECVREV))+(CTOTISS-CTOTISSREV)-(CTOTRECV-CTOTRECVREV) as OPENING,(TOTRECV-TOTRECVREV) AS TOTRECV,(TOTISS) AS TOTAL_ISS,TOTISSREV,  "
						+ " (STOCKONHAND +(CTOTISS-CTOTISSREV)-(CTOTRECV-CTOTRECVREV)) AS CLOSING,(CTOTISS-CTOTISSREV) as ISSUED_AFTER,(CTOTRECV-CTOTRECVREV) as RECVED_AFTER ,STOCKONHAND from ("
						+ "  select plant,item,ITEMDESC,PRD_CLS_ID,ITEMTYPE,PRD_BRAND_ID,ISNULL((SELECT SUM(QTY) from ["+aPlant+"_INVMST] where PLANT =a.PLANT and ITEM =a.ITEM),0) AS STOCKONHAND,"
						+ " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM and DIRTYPE in ('MISC_RECV','ORD_RECV','REPORTING') "+sDtCond+" "+locCond+"  group by item),0)as TOTRECV, "
						+ " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM and DIRTYPE in ('IB_REVERSE','OBRECEIVE_REVERSE','REPORTING_REV') "+sDtCond+" "+locCond+" group by item),0)as TOTRECVREV, "
						+ " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM  "+sDtCond+" "+locCond+" AND   DIRTYPE in ('MISC_ISSUE','ORD_PICK_ISSUE','ORD_ISSUE','ADD_KITTING','POS_TRANSACTION','MOVETOWIP') group by item),0)as TOTISS , "
						+ " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM  "+sDtCond+" "+locCond+" AND   DIRTYPE in ('OBISSUE_REVERSE','POS_REFUND','WIP_REVERSE') group by item),0)as TOTISSREV , "
						+ " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM and DIRTYPE in ('MISC_RECV','ORD_RECV','REPORTING') "+sAfterDtCond+" "+locCond+" group by item),0)as CTOTRECV, "
						+ " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM and DIRTYPE in ('IB_REVERSE','OBRECEIVE_REVERSE','REPORTING_REV') "+sAfterDtCond+" "+locCond+"  group by item),0)as CTOTRECVREV, "
						+ " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM  "+sAfterDtCond+"  "+locCond+" and DIRTYPE in ('MISC_ISSUE','ORD_PICK_ISSUE','ORD_ISSUE','ADD_KITTING','POS_TRANSACTION','MOVETOWIP') group by item),0)as CTOTISS, "
						+ " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM  "+sAfterDtCond+" "+locCond+" AND   DIRTYPE in ('OBISSUE_REVERSE','POS_REFUND','WIP_REVERSE') group by item),0)as CTOTISSREV  "
						+ "  from ["+ aPlant+ "_"+ "ITEMMST"+ "] a where  PLANT = '"+aPlant +"' and ISNULL(NONSTKFLAG,'')<>'Y'  group by plant,item,ITEMDESC,PRD_CLS_ID,ITEMTYPE,PRD_BRAND_ID )A "
						+" WHERE  PLANT = '"+aPlant +"' and  item in (SELECT ITEM FROM ["+ aPlant+ "_ALTERNATE_ITEM_MAPPING] WHERE ALTERNATE_ITEM_NAME like '"+ aItem + "%') ";
				arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);
			

			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}
			return arrList;
		}

	public ArrayList getInvWithOpenCloseBalByLoc(String aPlant, String aItem,String productDesc, Hashtable ht,String afrmDate,String atoDate,String loc,String loctypeid,String rptType) throws Exception {
		ArrayList arrList = new ArrayList();
		//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
		    String sCondition=""; 
		    String sDtCond="",sAfterDtCond=""; 
	        String baseCurrency ="SGD";
	        String AvgCostCurencyID ="SGD";
	        
		    if (productDesc.length() > 0 ) {
	                if (productDesc.indexOf("%") != -1) {
	                        productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
	                }
	         
			 sCondition = " and replace(ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
		    }
		    
		    String sRecvCondition=""; 
	     /*   if (afrmDate.length() > 0) {
	        	String currentdate = new DateUtils().getDateFormatyyyyMMdd();
			     if( Long.parseLong(afrmDate)< Long.parseLong(currentdate)){
			    	 sRecvCondition = sRecvCondition + " and substring(CRAT,1,8)  >= '" + afrmDate
		                     + "'  ";
				     if (atoDate.length() > 0) {
				             sRecvCondition = sRecvCondition + " and substring(CRAT,1,8)   <= '" + atoDate + "'  ";
				     }
			     }else{ sRecvCondition = sRecvCondition + " and substring(CRAT,1,8)   <= '" + atoDate   + "'  ";
			     }
	               
	        } else {
	                if (atoDate.length() > 0) {
	                        sRecvCondition = sRecvCondition + " and substring(CRAT,1,8) <= '" + atoDate  + "'  ";
	                }
	     }*/
		    
		    if (loctypeid != null && loctypeid!="") {
				sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType(aPlant,loctypeid)+" )";
			}

	            if(loc.length()>0){
	            loc = "  AND LOC like'%"+loc+"%'";
	            }
		    if (afrmDate.length() > 0) {

		    	sDtCond = sDtCond + " AND   REPLACE(TRANDATE,'-','') >= '"+ afrmDate + "'  ";
				if (atoDate.length() > 0) {
					sDtCond = sDtCond + " AND  REPLACE(TRANDATE,'-','')<= '" + atoDate + "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sDtCond = sDtCond + "   REPLACE(TRANDATE,'-','') <= '" + atoDate + "'  ";
				}
			}
		    if (atoDate.length() > 0) {
		    	sAfterDtCond = sAfterDtCond + " AND  REPLACE(TRANDATE,'-','') > '" + atoDate + "'  ";
			}
		    
		   
		        
			try {
				InvMstDAO _InvMstDAO = new InvMstDAO();
				_InvMstDAO.setmLogger(mLogger);
				
				String ConvertUnitCostToOrderCurrency = " (CAST(ISNULL(UNITCOST,0) *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST] " +
                        "   WHERE  CURRENCYID='"+baseCurrency+"')*(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST] " +
                        "   WHERE  CURRENCYID=ISNULL((SELECT CURRENCYID from "+aPlant+"_POHDR WHERE PONO =R.PONO),'"+baseCurrency+"')) AS DECIMAL(20,4)) ) ";
				
				StringBuffer aQuery = new StringBuffer();
				if(rptType.equalsIgnoreCase("WITH_AVGCOST")){
					aQuery.append( " SELECT *, (SELECT CASE WHEN  (SELECT COUNT(CURRENCYID) FROM ["+ aPlant+ "_"+ "RECVDET"+ "] WHERE ITEM=MAIN.ITEM AND CURRENCYID IS NOT NULL AND TRAN_TYPE IN('IB') ");
					aQuery.append( " AND UNITCOST >0 AND UNITCOST IS NOT NULL )>0  THEN  (Select ISNULL(CAST(ISNULL(SUM(AVGC.UNITCOST),0)/SUM(AVGC.RECQTY) AS DECIMAL(20,6)),0) AS AVERGAGE_COST ");
					aQuery.append( " from  (select RECQTY,CAST( "+ConvertUnitCostToOrderCurrency+"  "); 
					aQuery.append( "  * CAST((SELECT CURRENCYUSEQT  FROM ["+ aPlant+ "_"+ "CURRENCYMST"+ "] WHERE  CURRENCYID='"+AvgCostCurencyID+"')AS DECIMAL(20,5)) ");
					aQuery.append( " / CAST((SELECT CURRENCYUSEQT  FROM ["+ aPlant+ "_"+ "CURRENCYMST"+ "] WHERE  CURRENCYID=(SELECT CURRENCYID from ["+ aPlant+ "_"+ "POHDR"+ "] WHERE PONO =R.PONO)) AS DECIMAL(20,5))  ");
					aQuery.append( " * RECQTY AS DECIMAL(20,5)) AS UNITCOST    from ["+ aPlant+ "_"+ "RECVDET"+ "] R where item =MAIN.ITEM AND UNITCOST IS NOT NULL  AND UNITCOST >0  AND UNITCOST <> '' " + sRecvCondition);
					aQuery.append( " AND TRAN_TYPE ='IB' AND ORDQTY >0   ) AVGC )     ELSE   CAST(((MAIN.COST)*(SELECT CURRENCYUSEQT  FROM ["+ aPlant+ "_"+ "CURRENCYMST"+ "] WHERE  CURRENCYID='"+AvgCostCurencyID+"')) AS DECIMAL(20,5))");
					aQuery.append( "  END ) AS AVERAGE_COST   FROM (	");
				}
				aQuery.append(" select plant,ITEM,LOC,ITEMDESC,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ aPlant +"_UOM u where u.UOM=a.STKUOM),'') as STKUOM,PRD_CLS_ID,ITEMTYPE,PRD_BRAND_ID,(STOCKONHAND+((TOTISS+TOTLTISS))-((TOTRECV+TOTLTRECV)-TOTRECVREV))+(CTOTISS+CTOTLTISS)-((CTOTRECV+CTOTLTRECV)-CTOTRECVREV) as OPENING,(TOTRECV-TOTRECVREV) AS TOTRECV,TOTLTRECV,TOTLTISS,(TOTISS) AS TOTAL_ISS,  ");
						aQuery.append( " (STOCKONHAND +((CTOTISS+CTOTLTISS))-((CTOTRECV+CTOTLTRECV)-CTOTRECVREV)) AS CLOSING,(CTOTRECV-CTOTRECVREV) as RECVED_AFTER ,CTOTLTRECV,CTOTLTISS,(CTOTISS) as ISSUED_AFTER,STOCKONHAND ,COST,LIST_PRICE  from (");
						aQuery.append( "   select distinct  a.plant,a.item,isnull(i.loc,'')as loc,ITEMDESC,PRD_CLS_ID,ITEMTYPE,PRD_BRAND_ID,ISNULL((SELECT SUM(QTY) from ["+aPlant+"_INVMST] where PLANT =a.PLANT and ITEM =a.ITEM and loc =i.loc),0) AS STOCKONHAND," );
						aQuery.append( "  ISNULL(CAST(((a.UNITPRICE)*(SELECT CURRENCYUSEQT  FROM ["+ aPlant+ "_"+ "CURRENCYMST"+ "] WHERE  CURRENCYID='"+AvgCostCurencyID+"')) AS DECIMAL(20,4)),0) as LIST_PRICE, a.COST as COST," );
						aQuery.append( " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM and loc =i.loc and DIRTYPE in ('ORD_RECV','MISC_RECV','KIT_PARENT','REPORTING') and MOVTID='IN'  "+sDtCond+"  group by item,loc),0) as TOTRECV ,  " );
					//	aQuery.append( " isnull((SELECT top 1 SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM and loc =i.loc and DIRTYPE in ('CNT_INV_UPLOAD') and MOVTID='IN'  "+sDtCond+"  group by item,loc,crat order by crat DESC ),0)as TOTRECV, " );
						aQuery.append( " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM and loc =i.loc and DIRTYPE in ('IB_REVERSE','OBRECEIVE_REVERSE','DELETE_DEKITTING_INV','REPORTING_REV')   "+sDtCond+"  group by item,loc),0)as TOTRECVREV, " );
						aQuery.append( " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM and loc =i.loc and  DIRTYPE in ('LT_TRAN_IN','LOAN_PICK_IN','PIC_TO_IN','LOAN_RECV_IN','TO_REVERSE','TO_RECV','PIC_TRAN_IN','OB_REVERSE','OB_TRANSFER_PIC_IN','OBISSUE_REVERSE','WIP_REVERSE','POS_REFUND')  and MOVTID='IN'  "+sDtCond+" group by item,loc),0) as TOTLTRECV," );
						aQuery.append( " -(isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM and loc =i.loc and  DIRTYPE in ('LT_TRAN_OUT','LOAN_RECV_OUT','PIC_TO_OUT','TO_REVERSE','PIC_TRAN_OUT','OB_REVERSE_OUT','OB_REVERSE','OB_TRANSFER_PIC_OUT') and MOVTID ='OUT'  "+sDtCond+" group by item,loc),0)) as TOTLTISS , " );
						aQuery.append( " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM   and loc =i.loc and  DIRTYPE in ('MISC_ISSUE','ORD_PICK_ISSUE','ORD_ISSUE','ADD_KITTING','POS_TRANSACTION','MOVETOWIP') and MOVTID ='OUT' "+sDtCond+" group by item,loc),0) - isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM   and loc =i.loc and  DIRTYPE in ('POS_REFUND','DEKITTING') and MOVTID ='IN' "+sDtCond+" group by item,loc),0) as TOTISS , " );
						
						aQuery.append( " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM and loc =i.loc and DIRTYPE in ('ORD_RECV','MISC_RECV','KIT_PARENT','REPORTING') and MOVTID='IN'  "+sAfterDtCond+"  group by item,loc),0) as CTOTRECV  , " );
						//aQuery.append( " isnull((SELECT top 1 SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM and loc =i.loc and DIRTYPE in ('CNT_INV_UPLOAD') and MOVTID='IN'  "+sAfterDtCond+"  group by item,loc,crat order by crat DESC ),0)as cTOTRECV, " );
						aQuery.append( " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM and loc =i.loc and DIRTYPE in ('IB_REVERSE','OBRECEIVE_REVERSE','DELETE_DEKITTING_INV','REPORTING_REV')   "+sAfterDtCond+"  group by item,loc),0)as CTOTRECVREV, " );
						aQuery.append( " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM and loc =i.loc and  DIRTYPE in ('LT_TRAN_IN','LOAN_PICK_IN','PIC_TO_IN','LOAN_RECV_IN','TO_REVERSE','TO_RECV','PIC_TRAN_IN','OB_REVERSE','OB_TRANSFER_PIC_IN','OBISSUE_REVERSE','WIP_REVERSE','POS_REFUND')  and MOVTID='IN'  "+sAfterDtCond+" group by item,loc),0) as CTOTLTRECV," );
						aQuery.append( " -(isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM and loc =i.loc and  DIRTYPE in ('LT_TRAN_OUT','LOAN_RECV_OUT','PIC_TO_OUT','TO_REVERSE','PIC_TRAN_OUT','OB_REVERSE_OUT','OB_REVERSE','OB_TRANSFER_PIC_OUT') and MOVTID ='OUT'  "+sAfterDtCond+" group by item,loc),0)) as CTOTLTISS , " );
						aQuery.append( " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM   and loc =i.loc and  DIRTYPE in ('MISC_ISSUE','ORD_PICK_ISSUE','ORD_ISSUE','ADD_KITTING','POS_TRANSACTION','MOVETOWIP') and MOVTID ='OUT' "+sAfterDtCond+" group by item,loc),0)- isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM   and loc =i.loc and  DIRTYPE in ('POS_REFUND','DEKITTING') and MOVTID ='IN' "+sAfterDtCond+" group by item,loc),0) as CTOTISS  " );

						aQuery.append( " from ["+ aPlant+ "_"+ "ITEMMST"+ "] a  full outer join ["+ aPlant+ "_"+ "MOVHIS"+ "] i on a.PLANT =i.PLANT and a.ITEM=i.item where  a.PLANT = '"+aPlant+"' and i.movtid in ('IN','OUT')  and isnull(i.LOC,'') <> ''and ISNULL(a.NONSTKFLAG,'')<>'Y' and  a.item in (SELECT ITEM FROM ["+ aPlant+ "_ALTERNATE_ITEM_MAPPING] WHERE ALTERNATE_ITEM_NAME like '"+ aItem + "%')"+loc+" )B " );
						aQuery.append( " WHERE  PLANT = '"+aPlant +"'  AND ((TOTRECV-TOTRECVREV)+TOTLTRECV+TOTLTISS+TOTISS+(CTOTRECV-CTOTRECVREV)+CTOTLTRECV+CTOTLTISS+CTOTISS+STOCKONHAND) >0 " );
						
				if(rptType.equalsIgnoreCase("WITH_AVGCOST")){
					aQuery.append( " AND LOC =CASE WHEN (SELECT COUNT(LOC) FROM ["+ aPlant+ "_"+ "LOCMST"+ "] WHERE LOC = B.LOC) >0 THEN (SELECT LOC FROM ["+ aPlant+ "_"+ "INVMST"+ "] WHERE   QTY >= 0 AND ITEM=B.ITEM AND LOC=B.LOC  GROUP BY ITEM,LOC)  " );
					aQuery.append( "WHEN (SELECT COUNT(LOC) FROM ["+ aPlant+ "_"+ "LOCMST"+ "] WHERE LOC = B.LOC) = 0 THEN (SELECT LOC FROM ["+ aPlant+ "_"+ "INVMST"+ "] WHERE   QTY > 0 AND ITEM=B.ITEM AND LOC=B.LOC GROUP BY ITEM,LOC) END ) MAIN WHERE  PLANT = '"+aPlant +"' " );
				}else{
					aQuery.append( " AND LOC =CASE WHEN (SELECT COUNT(LOC) FROM ["+ aPlant+ "_"+ "LOCMST"+ "] WHERE LOC = B.LOC) >0 THEN (SELECT LOC FROM ["+ aPlant+ "_"+ "INVMST"+ "] WHERE   QTY >= 0 AND ITEM=B.ITEM AND LOC=B.LOC  GROUP BY ITEM,LOC)  " );
					aQuery.append( "WHEN (SELECT COUNT(LOC) FROM ["+ aPlant+ "_"+ "LOCMST"+ "] WHERE LOC = B.LOC) = 0 THEN (SELECT LOC FROM ["+ aPlant+ "_"+ "INVMST"+ "] WHERE   QTY > 0 AND ITEM=B.ITEM AND LOC=B.LOC GROUP BY ITEM,LOC) END  " );//order by item
				
				}
				arrList = _InvMstDAO.selectForReport(aQuery.toString(), ht, sCondition);
				
						
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}
			return arrList;
		}
	
	public ArrayList getInvMovHisByLoc(String aPlant, String aItem, Hashtable ht,String afrmDate,String atoDate,String loc,String rptType) throws Exception {
		ArrayList arrList = new ArrayList();
		//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
		String sCondition=""; 
		String sDtCond="",sAfterDtCond=""; 

		
		
		String sRecvCondition=""; 

		
		if(loc.length()>0){
			loc = "  AND (LOC like'%"+loc+"%' OR   LOC='RESERVE_KITCHEN'  OR   LOC='RESERVE_KITTING')";
		}
		if (afrmDate.length() > 0) {
			
			sDtCond = sDtCond + " AND   REPLACE(TRANDATE,'-','') >= '"+ afrmDate + "'  ";
			if (atoDate.length() > 0) {
				sDtCond = sDtCond + " AND  REPLACE(TRANDATE,'-','')<= '" + atoDate + "'  ";
			}
		} else {
			if (atoDate.length() > 0) {
				sDtCond = sDtCond + "   REPLACE(TRANDATE,'-','') <= '" + atoDate + "'  ";
			}
		}
		if (atoDate.length() > 0) {
			sAfterDtCond = sAfterDtCond + " AND  REPLACE(TRANDATE,'-','') < '" + afrmDate + "'  ";
		}
		
		
		
		try {
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
		
			StringBuffer aQuery = new StringBuffer();
			
			aQuery.append(" WITH A AS( ");
			aQuery.append(" SELECT SUBSTRING('"+ afrmDate + "',1,4)+'-'+SUBSTRING('"+ afrmDate + "',5,2)+'-'+SUBSTRING('"+ afrmDate + "',7,2) AS TRANDATE ,'' as ORDNUM,'Opening' DIRTYPE,'' as CUSTOMER,SUBSTRING('"+ afrmDate + "',1,4)+SUBSTRING('"+ afrmDate + "',5,2)+SUBSTRING('"+ afrmDate + "',7,2)+'000000' CRAT,");
			aQuery.append("ISNULL((SELECT SUM(ISNULL(QTY,0) * ISNULL(U.QPUOM,1)) AS QTY FROM  ["+ aPlant+ "_MOVHIS] B JOIN "+aPlant+"_UOM U ON ISNULL(U.UOM,'')=(CASE WHEN ISNULL(B.UOM,'PCS')='' THEN 'PCS' ELSE ISNULL(B.UOM,'PCS') END) WHERE B.PLANT='"+ aPlant+ "'  AND  ITEM='"+ aItem + "'  "+loc+" "+sAfterDtCond+" ");
			aQuery.append("AND DIRTYPE IN ('ORDER_RECV','GOODSRECEIPT','CONSIGNMENT_ORDER_RECV','KITTING_IN','STOCK_MOVE_IN','DE-KITTING_IN','CNT_STOCK_TAKE_IMPORT_DATA','SALES_RETURN','CNT_INVENTORY_IMPORT_DATA','SALES_ORDER_PICK_TRAN_IN','SALES_ORDER_REVERSE','POS_VOID_SALES_ORDER','POS_SALES_RETURN','POS_EXCHANGE','CONSIGNMENT_REVERSE_IN','STOCK_TAKE')),0) ");
			aQuery.append("-ISNULL((SELECT SUM(ISNULL(REPLACE(QTY,'-',''),0) * ISNULL(U.QPUOM,1)) AS QTY FROM  ["+ aPlant+ "_MOVHIS] B JOIN "+aPlant+"_UOM U ON ISNULL(U.UOM,'')=(CASE WHEN ISNULL(B.UOM,'PCS')='' THEN 'PCS' ELSE ISNULL(B.UOM,'PCS') END) WHERE B.PLANT='"+ aPlant+ "'  AND  ITEM='"+ aItem + "'  "+loc+" "+sAfterDtCond+" ");
			aQuery.append("AND DIRTYPE IN ('ORD_PICK_ISSUE','GOODSISSUE','PICK_CONSIGNMENT_ORDER_OUT','KITTING_OUT','STOCK_MOVE_OUT','DE-KITTING_OUT','PURCHASE_RETURN','SALES_ORDER_PICK_TRAN_OUT','ORDER_ISSUE','OB_REVERSE_OUT','CONSIGNMENT_REVERSE_OUT','STOCK_TAKE_OUT')),0) QTY ,'' as UOM ");
			aQuery.append("UNION ");
			aQuery.append("SELECT ISNULL(TRANDATE,'') AS TRANDATE ,CASE WHEN (DIRTYPE='ORD_PICK_ISSUE' AND ORDNUM='') THEN REMARKS ELSE ISNULL(ORDNUM,'') END as ORDNUM,DIRTYPE,isnull(CUSTNO,'') as CUSTOMER,REPLACE(TRANDATE,'-','')+SUBSTRING(CRAT,9,8) AS CRAT,ISNULL(QTY,0) AS QTY,isnull(UOM,'') as UOM ");
			aQuery.append("FROM  ["+ aPlant+ "_MOVHIS] B ");
			aQuery.append("WHERE PLANT='"+ aPlant+ "'  AND  ITEM='"+ aItem + "' "+loc+" "+sDtCond+" ");  
			aQuery.append("AND DIRTYPE IN ('ORDER_RECV','GOODSRECEIPT','ORD_PICK_ISSUE','GOODSISSUE','PICK_CONSIGNMENT_ORDER_OUT','CONSIGNMENT_ORDER_RECV','KITTING_OUT','KITTING_IN','STOCK_MOVE_OUT','STOCK_MOVE_IN','DE-KITTING_OUT','DE-KITTING_IN','CNT_STOCK_TAKE_IMPORT_DATA','PURCHASE_RETURN','SALES_RETURN','CNT_INVENTORY_IMPORT_DATA','SALES_ORDER_PICK_TRAN_IN','SALES_ORDER_PICK_TRAN_OUT','ORDER_ISSUE','OB_REVERSE_OUT','SALES_ORDER_REVERSE','POS_SALES_RETURN','POS_VOID_SALES_ORDER','POS_EXCHANGE','CONSIGNMENT_REVERSE_IN','CONSIGNMENT_REVERSE_OUT','STOCK_TAKE','STOCK_TAKE_OUT') ");
			aQuery.append(") SELECT * FROM A ");
			aQuery.append("order by CRAT ASC ");
			
			arrList = _InvMstDAO.selectForReport(aQuery.toString(), ht, sCondition);
			
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}

	public ArrayList getInvMovHisByPrd(String aPlant, String aItem, Hashtable ht,String afrmDate,String atoDate,String loc,String rptType) throws Exception {
		ArrayList arrList = new ArrayList();
		//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
		String sCondition=""; 
		String sDtCond="",sAfterDtCond=""; 
		
		
		
		String sRecvCondition=""; 
		
		
		if(loc.length()>0){
			loc = "  AND (LOC like'%"+loc+"%' OR   LOC='RESERVE_KITCHEN'  OR   LOC='RESERVE_KITTING')";
		}
		if (afrmDate.length() > 0) {
			
			sDtCond = sDtCond + " AND   REPLACE(TRANDATE,'-','') >= '"+ afrmDate + "'  ";
			if (atoDate.length() > 0) {
				sDtCond = sDtCond + " AND  REPLACE(TRANDATE,'-','')<= '" + atoDate + "'  ";
			}
		} else {
			if (atoDate.length() > 0) {
				sDtCond = sDtCond + "   REPLACE(TRANDATE,'-','') <= '" + atoDate + "'  ";
			}
		}
		if (atoDate.length() > 0) {
			sAfterDtCond = sAfterDtCond + " AND  REPLACE(TRANDATE,'-','') < '" + afrmDate + "'  ";
		}
		
		
		
		try {
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
			
			StringBuffer aQuery = new StringBuffer();
			
			aQuery.append("SELECT DIRTYPE,SUM(ISNULL(QTY,0)) AS QTY,isnull(UOM,'') as UOM,ISNULL((select ISNULL(QPUOM,1) as UOMQTY from "+aPlant+"_UOM where UOM=isnull(B.UOM,'')),1) AS UOMQTY,MIN(CRAT) FROM  ["+ aPlant+ "_MOVHIS] B WHERE PLANT='"+ aPlant+ "'  AND  ITEM='"+ aItem+ "' ");   
			aQuery.append("AND DIRTYPE IN ('ORDER_RECV','GOODSRECEIPT','ORD_PICK_ISSUE','GOODSISSUE','PICK_CONSIGNMENT_ORDER_OUT','CONSIGNMENT_ORDER_RECV','KITTING_OUT','KITTING_IN','STOCK_MOVE_OUT','STOCK_MOVE_IN','DE-KITTING_OUT','DE-KITTING_IN','CNT_STOCK_TAKE_IMPORT_DATA','PURCHASE_RETURN','SALES_RETURN','CNT_INVENTORY_IMPORT_DATA','SALES_ORDER_PICK_TRAN_IN','SALES_ORDER_PICK_TRAN_OUT','ORDER_ISSUE','OB_REVERSE_OUT','SALES_ORDER_REVERSE','POS_SALES_RETURN','POS_VOID_SALES_ORDER','POS_EXCHANGE','CONSIGNMENT_REVERSE_IN','CONSIGNMENT_REVERSE_OUT','STOCK_TAKE','STOCK_TAKE_OUT') ");
			aQuery.append("GROUP BY DIRTYPE,UOM ORDER BY MIN(CRAT) ASC");
			
			arrList = _InvMstDAO.selectForReport(aQuery.toString(), ht, sCondition);
			
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}
	
	/*
	  public ArrayList getInvWithOpenCloseBalByLoc(String aPlant, String aItem,String productDesc, Hashtable ht,String afrmDate,String atoDate,String loc,String loctypeid,String rptType) throws Exception {
		ArrayList arrList = new ArrayList();
		//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
		    String sCondition=""; 
		    String sDtCond="",sAfterDtCond=""; 
	        String baseCurrency ="SGD";
	        String AvgCostCurencyID ="SGD";
	        
		    if (productDesc.length() > 0 ) {
	                if (productDesc.indexOf("%") != -1) {
	                        productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
	                }
	         
			 sCondition = " and replace(ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
		    }
		    
		    if (loctypeid != null && loctypeid!="") {
				sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType(aPlant,loctypeid)+" )";
			}

	            if(loc.length()>0){
	            loc = "  AND LOC like'%"+loc+"%'";
	            }
		    if (afrmDate.length() > 0) {

		    	sDtCond = sDtCond + " AND  substring(crat,1,8) >= '"+ afrmDate + "'  ";
				if (atoDate.length() > 0) {
					sDtCond = sDtCond + " AND substring(crat,1,8)<= '" + atoDate + "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sDtCond = sDtCond + "  substring(crat,1,8) <= '" + atoDate + "'  ";
				}
			}
		    if (atoDate.length() > 0) {
		    	sAfterDtCond = sAfterDtCond + " AND substring(crat,1,8)> '" + atoDate + "'  ";
			}
		    
		   
		        
			try {
				InvMstDAO _InvMstDAO = new InvMstDAO();
				_InvMstDAO.setmLogger(mLogger);
				
				String ConvertUnitCostToOrderCurrency = " (CAST(ISNULL(UNITCOST,0) *(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST] " +
                        "   WHERE  CURRENCYID='"+baseCurrency+"')*(SELECT CURRENCYUSEQT  FROM ["+aPlant+"_CURRENCYMST] " +
                        "   WHERE  CURRENCYID=ISNULL((SELECT CURRENCYID from "+aPlant+"_POHDR WHERE PONO =R.PONO),'"+baseCurrency+"')) AS DECIMAL(20,4)) ) ";
				
				StringBuffer aQuery = new StringBuffer();
				if(rptType.equalsIgnoreCase("WITH_AVGCOST")){
					aQuery.append( " SELECT *, (SELECT CASE WHEN  (SELECT COUNT(CURRENCYID) FROM ["+ aPlant+ "_"+ "RECVDET"+ "] WHERE ITEM=MAIN.ITEM AND CURRENCYID IS NOT NULL AND TRAN_TYPE IN('IB') ");
					aQuery.append( " AND UNITCOST >0 AND UNITCOST IS NOT NULL )>0  THEN  (Select ISNULL(CAST(ISNULL(SUM(AVGC.UNITCOST),0)/SUM(AVGC.RECQTY) AS DECIMAL(20,4)),0) AS AVERGAGE_COST ");
					aQuery.append( " from  (select RECQTY,CAST( "+ConvertUnitCostToOrderCurrency+"  "); 
					aQuery.append( "  * CAST((SELECT CURRENCYUSEQT  FROM ["+ aPlant+ "_"+ "CURRENCYMST"+ "] WHERE  CURRENCYID='"+AvgCostCurencyID+"')AS DECIMAL(20,4)) ");
					aQuery.append( " / CAST((SELECT CURRENCYUSEQT  FROM ["+ aPlant+ "_"+ "CURRENCYMST"+ "] WHERE  CURRENCYID=(SELECT CURRENCYID from ["+ aPlant+ "_"+ "POHDR"+ "] WHERE PONO =R.PONO)) AS DECIMAL(20,4))  ");
					aQuery.append( " * RECQTY AS DECIMAL(20,4)) AS UNITCOST    from ["+ aPlant+ "_"+ "RECVDET"+ "] R where item =MAIN.ITEM AND UNITCOST IS NOT NULL  AND UNITCOST >0  AND UNITCOST <> '' ");
					aQuery.append( " AND TRAN_TYPE ='IB' AND ORDQTY >0   ) AVGC )     ELSE   CAST(((MAIN.COST)*(SELECT CURRENCYUSEQT  FROM ["+ aPlant+ "_"+ "CURRENCYMST"+ "] WHERE  CURRENCYID='"+AvgCostCurencyID+"')) AS DECIMAL(20,4))");
					aQuery.append( "  END ) AS AVERAGE_COST   FROM (	");
				}
				aQuery.append(" select plant,ITEM,LOC,ITEMDESC,PRD_CLS_ID,ITEMTYPE,PRD_BRAND_ID,(STOCKONHAND+(TOTISS-TOTISSREV)-(TOTRECV-TOTRECVREV))+(CTOTISS-CTOTISSREV)-(CTOTRECV-CTOTRECVREV) as OPENING,(TOTRECV-TOTRECVREV) AS TOTRECV,(TOTISS-TOTISSREV) AS TOTAL_ISS,  ");
						aQuery.append( " (STOCKONHAND +(CTOTISS-CTOTISSREV)-(CTOTRECV-CTOTRECVREV)) AS CLOSING,(CTOTISS-CTOTISSREV) as ISSUED_AFTER,(CTOTRECV-CTOTRECVREV) as RECVED_AFTER ,STOCKONHAND ,COST,LIST_PRICE from (");
						aQuery.append( "   select distinct  a.plant,a.item,isnull(i.loc,'')as loc,ITEMDESC,PRD_CLS_ID,ITEMTYPE,PRD_BRAND_ID,ISNULL((SELECT SUM(QTY) from ["+aPlant+"_INVMST] where PLANT =a.PLANT and ITEM =a.ITEM and loc =i.loc),0) AS STOCKONHAND," );
						aQuery.append( "  ISNULL(CAST(((a.UNITPRICE)*(SELECT CURRENCYUSEQT  FROM ["+ aPlant+ "_"+ "CURRENCYMST"+ "] WHERE  CURRENCYID='"+AvgCostCurencyID+"')) AS DECIMAL(20,4)),0) as LIST_PRICE, a.COST as COST," );
						aQuery.append( " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM and loc =i.loc and DIRTYPE in ('ORD_RECV','MISC_RECV' ,'LT_TRAN_IN','OB_TRANSFER_PIC_IN','PIC_TO_IN','PIC_TRAN_IN','TO_RECV','KIT_PARENT','LOAN_PICK_IN') and MOVTID='IN'  "+sDtCond+"  group by item,loc),0)as TOTRECV, " );
						aQuery.append( " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM and loc =i.loc and DIRTYPE in ('IB_REVERSE','OBRECEIVE_REVERSE','DELETE_DEKITTING_INV')   "+sDtCond+"  group by item,loc),0)as TOTRECVREV, " );
						aQuery.append( " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM and loc =i.loc and  DIRTYPE in ('MISC_ISSUE','ORD_PICK_ISSUE','ORD_ISSUE','ADD_KITTING','POS_TRANSACTION','OB_REVERSE')  and MOVTID ='OUT'  "+sDtCond+" group by item,loc),0)- (" );
						aQuery.append( " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM and loc =i.loc and  DIRTYPE in ('LOAN_RECV_OUT','LT_TRAN_OUT','OB_TRANSFER_PIC_OUT','PIC_TO_OUT','PIC_TRAN_OUT','TO_REVERSE','LOAN_RECV_OUT','OB_REVERSE_OUT') and MOVTID ='OUT'  "+sDtCond+" group by item,loc),0))as TOTISS , " );
						aQuery.append( " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM   and loc =i.loc and  DIRTYPE in ('OBISSUE_REVERSE','POS_REFUND','LOAN_RECV_IN','DEKITTING','TO_REVERSE','OB_REVERSE') and MOVTID ='IN' "+sDtCond+" group by item,loc),0)as TOTISSREV , " );
						
						aQuery.append( " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM and loc =i.loc and DIRTYPE in ('ORD_RECV','MISC_RECV' ,'LT_TRAN_IN','OB_TRANSFER_PIC_IN','PIC_TO_IN','PIC_TRAN_IN','TO_RECV','KIT_PARENT','LOAN_PICK_IN') and MOVTID='IN'  "+sAfterDtCond+"  group by item,loc),0)as CTOTRECV, " );
						aQuery.append( " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM and loc =i.loc and DIRTYPE in ('IB_REVERSE','OBRECEIVE_REVERSE','DELETE_DEKITTING_INV')   "+sAfterDtCond+"  group by item,loc),0)as CTOTRECVREV, " );
						aQuery.append( " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM and loc =i.loc and  DIRTYPE in ('MISC_ISSUE','ORD_PICK_ISSUE','ORD_ISSUE','ADD_KITTING','POS_TRANSACTION','OB_REVERSE')  and MOVTID ='OUT'  "+sAfterDtCond+" group by item,loc),0)- (" );
						aQuery.append( " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM and loc =i.loc and  DIRTYPE in ('LOAN_RECV_OUT','LT_TRAN_OUT','OB_TRANSFER_PIC_OUT','PIC_TO_OUT','PIC_TRAN_OUT','TO_REVERSE','LOAN_RECV_OUT','OB_REVERSE_OUT') and MOVTID ='OUT'  "+sAfterDtCond+" group by item,loc),0))as CTOTISS , " );
						aQuery.append( " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM   and loc =i.loc and  DIRTYPE in ('OBISSUE_REVERSE','POS_REFUND','LOAN_RECV_IN','DEKITTING','TO_REVERSE','OB_REVERSE') and MOVTID ='IN' "+sAfterDtCond+" group by item,loc),0)as CTOTISSREV  " );
						aQuery.append( " from ["+ aPlant+ "_"+ "ITEMMST"+ "] a  full outer join ["+ aPlant+ "_"+ "MOVHIS"+ "] i on a.PLANT =i.PLANT and a.ITEM=i.item where  a.PLANT = '"+aPlant+"' and i.movtid in ('IN','OUT')  and isnull(i.LOC,'') <> ''and ISNULL(a.NONSTKFLAG,'')<>'Y' and  a.item in (SELECT ITEM FROM ["+ aPlant+ "_ALTERNATE_ITEM_MAPPING] WHERE ALTERNATE_ITEM_NAME like '"+ aItem + "%')"+loc+" )B " );
						aQuery.append( " WHERE  PLANT = '"+aPlant +"' " );
						
				if(rptType.equalsIgnoreCase("WITH_AVGCOST")){
					aQuery.append( " AND LOC =CASE WHEN (SELECT COUNT(LOC) FROM ["+ aPlant+ "_"+ "LOCMST"+ "] WHERE LOC = B.LOC) >0 THEN (SELECT LOC FROM ["+ aPlant+ "_"+ "INVMST"+ "] WHERE   QTY >= 0 AND ITEM=B.ITEM AND LOC=B.LOC  GROUP BY ITEM,LOC)  " );
					aQuery.append( "WHEN (SELECT COUNT(LOC) FROM ["+ aPlant+ "_"+ "LOCMST"+ "] WHERE LOC = B.LOC) = 0 THEN (SELECT LOC FROM ["+ aPlant+ "_"+ "INVMST"+ "] WHERE   QTY > 0 AND ITEM=B.ITEM AND LOC=B.LOC GROUP BY ITEM,LOC) END ) MAIN WHERE  PLANT = '"+aPlant +"' " );
				}else{
					aQuery.append( " AND LOC =CASE WHEN (SELECT COUNT(LOC) FROM ["+ aPlant+ "_"+ "LOCMST"+ "] WHERE LOC = B.LOC) >0 THEN (SELECT LOC FROM ["+ aPlant+ "_"+ "INVMST"+ "] WHERE   QTY >= 0 AND ITEM=B.ITEM AND LOC=B.LOC  GROUP BY ITEM,LOC)  " );
					aQuery.append( "WHEN (SELECT COUNT(LOC) FROM ["+ aPlant+ "_"+ "LOCMST"+ "] WHERE LOC = B.LOC) = 0 THEN (SELECT LOC FROM ["+ aPlant+ "_"+ "INVMST"+ "] WHERE   QTY > 0 AND ITEM=B.ITEM AND LOC=B.LOC GROUP BY ITEM,LOC) END  " );//order by item
				
				}
				arrList = _InvMstDAO.selectForReport(aQuery.toString(), ht, sCondition);
				
						
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}
			return arrList;
		}
	

	/**
	 * @method : getInvDetails4PK(String aItem, String aLoc)
	 * @description : get the inventory details for the given primary key
	 * @param aItem
	 * @param aLoc
	 * @return
	 * @throws Exception
	 */
	public ArrayList getInvDetails4PK(String aItem, String aLoc)
			throws Exception {
		ArrayList arrList = new ArrayList();
		try {
			invSesBeanDAO.setmLogger(mLogger);
			arrList = invSesBeanDAO.getInvDetails(aItem, aLoc);
		} catch (Exception e) {
		}
		return arrList;
	}

	/**
	 * @method : getLocList4Item(String aItem)
	 * @description :get location list from inv
	 * @param aItem
	 * @return
	 * @throws Exception
	 */
	public ArrayList getLocList4Item(String aItem) throws Exception {
		ArrayList arrLoc = new ArrayList();
		try {
			invSesBeanDAO.setmLogger(mLogger);
			arrLoc = invSesBeanDAO.getLocList4Item(aItem);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrLoc;
	}

	/**
	 * @method : getLocList4Item(String aItem)
	 * @description :get location list an d Qty from inv
	 * @param aItem
	 * @return
	 * @throws Exception
	 */
	public ArrayList getLocList4ItemQty(String aItem) throws Exception {
		ArrayList arrLoc = new ArrayList();
		try {
			invSesBeanDAO.setmLogger(mLogger);
			arrLoc = invSesBeanDAO.getLocList4ItemQty(aItem);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrLoc;
	}

	/**
	 * 
	 * @param aLoc
	 * @param aItem
	 * @return
	 * @throws Exception
	 */
	/*---Update on 20100521.............................................................#############################*/
	public ArrayList getInvList(String aPlant, String aLoc, String aItem,
			String aBatch) throws Exception {
		ArrayList arrInvList = new ArrayList();
		try {
			invSesBeanDAO.setmLogger(mLogger);

			Hashtable ht = new Hashtable();
			ht.put("PLANT", aPlant);
			if (StrUtils.fString(aLoc).length() > 0)
				ht.put("LOC", aLoc);
			if (StrUtils.fString(aItem).length() > 0)
				ht.put("ITEM", aItem);
			if (StrUtils.fString(aBatch).length() > 0)
				ht.put("USERFLD4", aBatch);

			String extracond = "";

			ArrayList fieldsList = new ArrayList();
			fieldsList.add("inv." + IConstants.ITEM);

			fieldsList.add("inv." + IConstants.LOC);

			fieldsList.add("isnull(inv." + IConstants.USERFLD3 + ",'') as "
					+ IConstants.USERFLD3);
			fieldsList.add(IConstants.QTY);
			fieldsList.add("inv.USERFLD4");

			arrInvList = invSesBeanDAO.queryInvsmt(fieldsList, ht, extracond
					+ " and qty>0");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrInvList;
	}

	// ///////////PDA Methods//////////////////////
	/**
	 * 
	 * @param aItem
	 * @param aLoc
	 * @return
	 */
	public String getItemDetails4Outbound(String aItem, String aLoc) {
		String xmlStr = "";
		try {
			ArrayList arrList = getInvDetails4PK(aItem, aLoc);
			xmlStr += XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("record");
			xmlStr += XMLUtils.getXMLNode("ITEM", aItem);
			xmlStr += XMLUtils.getXMLNode("DESCRIPTION", StrUtils
					.replaceCharacters2Send((String) arrList.get(1)));
			xmlStr += XMLUtils.getXMLNode("UOM", (String) arrList.get(2));
			xmlStr += XMLUtils.getXMLNode("LOC", aLoc);
			xmlStr += XMLUtils.getXMLNode("AVLQTY", (String) arrList.get(4));
			xmlStr += XMLUtils.getEndNode("record");
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return xmlStr.trim();
	}

	/**
	 * 
	 * @param aItem
	 * @return
	 */
	public String getLocationList4ItemPDA(String aItem) {
		String xmlStr = "";
		try {
			ArrayList arrLoc = this.getLocList4Item(aItem);

			xmlStr += XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("Location total ='"
					+ String.valueOf(arrLoc.size()) + "'");
			for (int i = 0; i < arrLoc.size(); i++) {
				ArrayList arrLine = (ArrayList) arrLoc.get(i);
				xmlStr += XMLUtils.getStartNode("record");
				xmlStr += XMLUtils.getXMLNode("LOC", (String) arrLine.get(0));
				xmlStr += XMLUtils.getXMLNode("QTY", (String) arrLine.get(1));
				xmlStr += XMLUtils.getEndNode("record");
			}
			xmlStr += XMLUtils.getEndNode("Location");
		} catch (Exception e) {
		}
		return xmlStr;
	}

	

	public ArrayList getItemListFromInv(String plant, String item,
			String itemDesc) throws Exception {
		ArrayList listItem = new ArrayList();
		String extraCond = "";
		Hashtable ht = new Hashtable();
		ht.put("plant", plant);
		if (item.length() > 0)
			extraCond = "  and altitem.item like'" + item + "%'";
		//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
		if (itemDesc.length() > 0)
			extraCond = " and altitem.item in ( select ITEM from " + plant
					+ "_ITEMMST WHERE REPLACE(ITEMDESC,' ','') LIKE '%"+StrUtils.InsertQuotes(itemDesc.replaceAll(" ",""))+"%')" ;

		String query = "distinct invmst.item item,isnull((select itemdesc from "
				+ "["
				+ ht.get("plant")
				+ "_"
				+ "ITEMMST]"
				+ " where item= invmst.item),'') as itemDesc ,"
				+ " isnull((select unitprice from "
				+ "["
				+ ht.get("plant")
				+ "_"
				+ "ITEMMST]"
				+ " where item= invmst.item),'') as unitPrice ";

		try {
			_InvMstDAO.setmLogger(mLogger);
			listItem = _InvMstDAO.selectInvMstWithAlternateItem(query, ht,
					extraCond);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return listItem;
	}

	public ArrayList getHQList(Hashtable ht, String aPlant, String fromdate,
			String todate) throws Exception {
		ArrayList arrList = new ArrayList();

		String sCondition = " ";

		try {
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
			sCondition = sCondition + " group by a.item,b.itemdesc";

			// Added stkuom to the query
			String aQuery = "select a.ITEM as ITEM,b.ITEMDESC as ITEMDESC, sum(QTY) QTY,sum(b.UNITPRICE)as UNITPRICE  from "
					+ "["
					+ aPlant
					+ "_"
					+ "invmst"
					+ "]"
					+ " A,"
					+ "["
					+ aPlant
					+ "_"
					+ "itemmst"
					+ "]"
					+ " as b where a.item=b.item and QTY>0 ";
			if (fromdate.length() > 0)
				aQuery = aQuery + " and LTRIM(RTRIM(A.EXPIREDATE)) <> '' AND A.EXPIREDATE is not null AND convert(datetime,A.EXPIREDATE,103) >= convert(datetime,'" + fromdate.toUpperCase() + "',103) ";
						
			
			if (todate.length() > 0)
				aQuery = aQuery + " and LTRIM(RTRIM(A.EXPIREDATE)) <> '' AND A.EXPIREDATE is not null AND convert(datetime,A.EXPIREDATE,103) <= convert(datetime,'" + fromdate.toUpperCase() + "',103) ";
			
		
			arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}

	public int getCountofExpdt(Hashtable ht, String aPlant, String aItem,
			String fromdt, String todt) throws Exception {

		int expdtcnt = 0;

		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			con = DbBean.getConnection();
			String aQuery = "select COUNT(expiredate) " + " from " + "["
					+ aPlant + "_invmst]";
			aQuery = aQuery + "where  item='" + aItem + "'";
			aQuery = aQuery
					+ " and REPLACE(isnull(EXPIREDATE,''),'-','')<convert(varchar(8),GETDATE(),112) and expiredate<>'' AND qty>0 ";
			if (fromdt.length() > 0)
				aQuery = aQuery + " AND EXPIREDATE > '"
						+ DateUtils.getDateinyyyy_mm_dd(fromdt) + "'";
			if (todt.length() > 0)
				aQuery = aQuery + " AND EXPIREDATE < '"
						+ DateUtils.getDateinyyyy_mm_dd(todt) + "'";

			ps = con.prepareStatement(aQuery);
			rs = ps.executeQuery();
			while (rs.next()) {

				expdtcnt = rs.getInt(1);
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return expdtcnt;
	}

	public ArrayList getInvListSumTotalByItemWithAccumulated(Hashtable ht,
			String aPlant, String aFlag) {
		ArrayList arrList = new ArrayList();
		String sCondition = "";
		try {
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
			sCondition = " group by a.item";
			if (aFlag.equalsIgnoreCase("Y")) {
				sCondition = sCondition + " having sum(b.stkqty)>sum(a.QTY) ";
			}

			String aQuery = "select a.ITEM,  sum(QTY)as QTY,sum(b.stkqty) from "
					+ "["
					+ aPlant
					+ "_"
					+ "invmst"
					+ "]"
					+ " A,"
					+ "["
					+ aPlant
					+ "_"
					+ "itemmst"
					+ "]"
					+ " as b where a.item=b.item  ";
			arrList = _InvMstDAO.selectForReportWithExpDate(aQuery, ht,
					sCondition);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}
        
	//below method chaned by Bruhan to get shippingarea,temp locations having qty in invmst if loc,loctypeid search is available on 14/jan/2014
	public ArrayList getInvListSummaryWithExpireDateCondition(Hashtable ht,
			String aPlant, String aItem, String aLoc, String aBatch,
			String prdid, String productDesc,String expireDt,String loctypeid) throws Exception {
		ArrayList arrList = new ArrayList();
		//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
	    String sCondition=""; 
	    if (productDesc.length() > 0 ) {
                if (productDesc.indexOf("%") != -1) {
                        productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
                }
         
		 sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
	    }

	    if (expireDt.length() > 0) {
	     expireDt =   " AND ISNULL(A.EXPIREDATE,'')<>'' AND  SUBSTRING(A.EXPIREDATE, 7, 4) +  SUBSTRING(A.EXPIREDATE, 4, 2)+ SUBSTRING(A.EXPIREDATE, 1, 2)  "+
	        " < = SUBSTRING('"+expireDt+"', 7, 4) +  SUBSTRING('"+expireDt+"', 4, 2)+ SUBSTRING('"+expireDt+"', 1, 2)  ";
	    }
	    if (aLoc != null && aLoc!="") {
        	sCondition = sCondition+" AND LOC like '%"+aLoc+"%'";
        }
        
        if (loctypeid != null && loctypeid!="") {
			sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType(aPlant,loctypeid)+" )";
		}


		try {
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
			sCondition = sCondition
					+ " group by a.item,a.loc,a.userfld4,b.itemdesc,b.prd_cls_id,b.prd_brand_id,b.itemtype,b.stkuom,a.EXPIREDATE";

			// Added stkuom to the query
			String aQuery = "select a.ITEM,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,b.stkuom as STKUOM,b.itemtype as ITEMTYPE,a.LOC,a.UserFld4 as BATCH, sum(QTY) QTY,sum(b.UNITPRICE)as UNITPRICE, isnull(a.EXPIREDATE, '') as EXPIREDATE  from "
					+ "["
					+ aPlant
					+ "_"
					+ "invmst"
					+ "]"
					+ " A,"
					+ "["
					+ aPlant
					+ "_"
					+ "itemmst"
					+ "]"
					+ " as b where a.item=b.item and QTY>0 "+expireDt;
			arrList = _InvMstDAO.selectForReportWithExpDate(aQuery, ht,
					sCondition);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}
        
        
    //Not in Use
    public ArrayList getInvListSummaryWithPriceAverageCost(Hashtable ht, String afrmDate,String atoDate,String aPlant,
                    String aItem, String aLoc, String aBatch, String prdid,
                    String productDesc) throws Exception {
            ArrayList arrList = new ArrayList();
          //CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
            String sCondition=""; 
    	    if (productDesc.length() > 0 ) {
                    if (productDesc.indexOf("%") != -1) {
                            productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
                    }
             
    		 sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
    	    }

            
            if (afrmDate.length() > 0) {
                    sCondition = sCondition + " and A.CRAT  >= '" + afrmDate
                                    + "'  ";
                    if (atoDate.length() > 0) {
                            sCondition = sCondition + " and A.CRAT   <= '" + atoDate
                                            + "'  ";
                    }
            } else {
                    if (atoDate.length() > 0) {
                            sCondition = sCondition + " and A.CRAT <= '" + atoDate
                                            + "'  ";
                    }
            }

            try {
                    InvMstDAO _InvMstDAO = new InvMstDAO();
                    _InvMstDAO.setmLogger(mLogger);
                    sCondition = sCondition
                                    + " group by a.item,a.loc,a.userfld4,b.itemdesc,b.prd_cls_id,b.itemtype,b.stkuom order by a.item ,a.loc  ";

                    // Added stkuom to the query
                    String aQuery = "select a.ITEM,b.ITEMDESC,b.prd_cls_id as PRDCLSID,b.stkuom as STKUOM,b.itemtype as ITEMTYPE,a.LOC,a.UserFld4 as BATCH, sum(QTY) QTY,sum(b.UNITPRICE)as UNITPRICE  from "
                                    + "["
                                    + aPlant
                                    + "_"
                                    + "invmst"
                                    + "]"
                                    + " A,"
                                    + "["
                                    + aPlant
                                    + "_"
                                    + "itemmst"
                                    + "]"
                                    + " as b where a.item=b.item and QTY>0 ";
                    arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);
                    
                    

            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
            }
            return arrList;
    }
    
    // Not in Use for track kept this for any existing customers
            public ArrayList getInvListSummaryWithPrice(Hashtable ht, String aPlant,
                            String aItem, String aLoc, String aBatch, String prdid,
                            String productDesc) throws Exception {
                    ArrayList arrList = new ArrayList();

                  //CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
                    String sCondition=""; 
	        	    if (productDesc.length() > 0 ) {
	                        if (productDesc.indexOf("%") != -1) {
	                                productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
	                        }
	                 
	        		 sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
	        	    }

                    try {
                            InvMstDAO _InvMstDAO = new InvMstDAO();
                            _InvMstDAO.setmLogger(mLogger);
                            sCondition = sCondition
                                            + " group by a.item,a.loc,a.userfld4,b.itemdesc,b.prd_cls_id,b.itemtype,b.stkuom order by a.item ,a.loc  ";

                            // Added stkuom to the query
                            String aQuery = "select a.ITEM,b.ITEMDESC,b.prd_cls_id as PRDCLSID,b.stkuom as STKUOM,b.itemtype as ITEMTYPE,a.LOC,a.UserFld4 as BATCH, sum(QTY) QTY,sum(b.UNITPRICE)as UNITPRICE  from "
                                            + "["
                                            + aPlant
                                            + "_"
                                            + "invmst"
                                            + "]"
                                            + " A,"
                                            + "["
                                            + aPlant
                                            + "_"
                                            + "itemmst"
                                            + "]"
                                            + " as b where a.item=b.item and QTY>0 ";
                            arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);
                            
                            

                    } catch (Exception e) {
                            this.mLogger.exception(this.printLog, "", e);
                    }
                    return arrList;
            }
            
// Not in Use
 public ArrayList getInvListSumTotalByItemForCondAverageCost(Hashtable ht,
                 String afrmDate,String atoDate,String aPlant, String prodDesc) {// ,String
         // aFlag
         ArrayList arrList = new ArrayList();
         String sCondition = "";
         String prodCond = "";
         
         try {
                 InvMstDAO _InvMstDAO = new InvMstDAO();
                 _InvMstDAO.setmLogger(mLogger);
                 
                 
                 if (afrmDate.length() > 0) {
                         sCondition = sCondition + " AND A.crat  >= '" + afrmDate
                                         + "'  ";
                         if (atoDate.length() > 0) {
                                 sCondition =  sCondition + " AND A.crat   <= '" + atoDate
                                                 + "'  ";
                         }
                 } else {
                         if (atoDate.length() > 0) {
                                 sCondition = sCondition + " AND A.crat  <= '" + atoDate
                                                 + "'  ";
                         }
                 }

                 sCondition =  sCondition + " group by a.item order by a.item";
               //CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
                 if (prodDesc.length() > 0 ) {
                         prodDesc = StrUtils.InsertQuotes(prodDesc);
                         if (prodDesc.indexOf("%") != -1) {
                                 prodDesc = prodDesc.substring(0, prodDesc.indexOf("%") - 1);
                         }
                         prodCond = " AND a.ITEM in (select ITEM FROM " + aPlant
                                         + "_ITEMMST WHERE REPLACE(ITEMDESC,' ','') LIKE '%"+StrUtils.InsertQuotes(prodDesc.replaceAll(" ",""))+"%')" ;
                                      
                 }

                 String aQuery = "select a.ITEM,  sum(QTY)as QTY,sum(b.stkqty) from "
                                 + "["
                                 + aPlant
                                 + "_"
                                 + "invmst"
                                 + "]"
                                 + " A,"
                                 + "["
                                 + aPlant
                                 + "_"
                                 + "itemmst"
                                 + "]"
                                 + " as b where a.item=b.item  " + prodCond;
                 arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

         } catch (Exception e) {
                 this.mLogger.exception(this.printLog, "", e);
         }
         return arrList;
 }

	//below method chaned by Bruhan to get shippingarea,temp locations having qty in invmst if loc,loctypeid search is available on 15/jan/2014
	public ArrayList getInvListSummaryWithMinQty(Hashtable ht, String aPlant,
			String aItem, String aLoc,  String prdid,
			String productDesc, boolean iswithzeroQty,String loctypeid) throws Exception {
		ArrayList arrList = new ArrayList();
		//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
        String sCondition=""; 
	    if (productDesc.length() > 0 ) {
                if (productDesc.indexOf("%") != -1) {
                        productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
                }
         
		 sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
	    }

		 if (aLoc != null && aLoc!="") {
         	sCondition = sCondition+" AND LOC like '%"+aLoc+"%'";
         }
         
         if (loctypeid != null && loctypeid!="") {
 			sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType(aPlant,loctypeid)+" )";
 		}
		String extraCond = "";
		try {

			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
			sCondition = sCondition
					+ " group by a.item,a.loc,b.itemdesc,b.prd_cls_id,b.prd_brand_id,b.itemtype,b.stkuom order by a.item,a.loc";
			if (!iswithzeroQty) {
				//extraCond = " and QTY>0 ";
				extraCond = " and a.plant <> '' ";
			}
			// Added stkuom to the query
			//---Modified by Bruhan on Feb 27 2014, Description: To display location data's in uppercase
			String aQuery = "select a.ITEM,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,b.stkuom as STKUOM,b.itemtype as ITEMTYPE,UPPER(a.LOC) LOC, sum(QTY)as QTY ,max(b.STKQTY) STKQTY ,(select sum(QTY) from "
					+ "["
					+ aPlant
					+ "_"
					+ "invmst"
					+ "]"
					+ "where item=a.item group by item) as ITEMQTY  from "
					+ "["
					+ aPlant
					+ "_"
					+ "invmst"
					+ "]"
					+ " A,"
					+ "["
					+ aPlant
					+ "_"
					+ "itemmst"
					+ "] "
                   
					+ " as b where a.item=b.item  " //+ extraCond;
					+ " AND LOC =CASE WHEN (SELECT COUNT(LOC) FROM ["+ aPlant +"_LOCMST] WHERE LOC = A.LOC) >0 THEN (SELECT LOC FROM ["+ aPlant+"_INVMST] WHERE   QTY >= 0 AND ITEM=A.ITEM AND LOC=A.LOC  GROUP BY ITEM,LOC)"
	                + " WHEN (SELECT COUNT(LOC) FROM ["+ aPlant +"_LOCMST] WHERE LOC = A.LOC) = 0 THEN (SELECT LOC FROM ["+ aPlant+"_INVMST] WHERE   QTY > 0 AND ITEM=A.ITEM AND LOC=A.LOC GROUP BY ITEM,LOC)"
	                +" END" + extraCond;
			arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}
	/*---------------not used -------------*/
	public ArrayList getInvListSummaryLocZeroQty(String aPlant, Hashtable ht) throws Exception {
			ArrayList arrList = new ArrayList();
			    String sCondition=""; 
		           
				try {
					InvMstDAO _InvMstDAO = new InvMstDAO();
					_InvMstDAO.setmLogger(mLogger);
					
					String aQuery = "select loc,locdesc,loc_type_id,(select LOC_TYPE_DESC from  "+aPlant+"_LOC_TYPE_MST where LOC_TYPE_ID=a.LOC_TYPE_ID)as loc_type_desc,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ aPlant +"_UOM u where u.UOM='PCS'),'') as STKUOM  from "
							+ "["
							+ aPlant
							+ "_"
							+ "locmst"
							+ "]"
							+ " a "
							+ "where PLANT = '"+ aPlant+"' and loc not in(select loc from  "+aPlant+"_INVMST group by loc having sum(QTY)>0) "
							+ "AND ISACTIVE ='Y' and loc not like 'SHIPPINGAREA%' and loc not like 'TEMP_TO%' ";
							
					arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
				}
				return arrList;
			}
	
	public ArrayList getInvListWithExpireDategroupbybatch(Hashtable ht, String aPlant,String aItem,String productDesc,String expireDt) throws Exception {
        ArrayList arrList = new ArrayList();
        String sCondition ="";
        if (productDesc.length() > 0 ) {
        if (productDesc.indexOf("%") != -1) {
                productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
        }
        
         sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
        }
        
//        String SysDate = DateUtils.getDate();
                    
        if (expireDt.length() > 0 ) {
         expireDt =   " AND ISNULL(A.EXPIREDATE,'')<>'' AND  SUBSTRING(A.EXPIREDATE, 7, 4) +  SUBSTRING(A.EXPIREDATE, 4, 2)+ SUBSTRING(A.EXPIREDATE, 1, 2)  "+
            " < = SUBSTRING('"+expireDt+"', 7, 4) +  SUBSTRING('"+expireDt+"', 4, 2)+ SUBSTRING('"+expireDt+"', 1, 2)  ";
        }
        
        
        try {
                InvMstDAO _InvMstDAO = new InvMstDAO();
                _InvMstDAO.setmLogger(mLogger);
               sCondition = sCondition  + " group by a.item,a.userfld4,b.itemdesc,b.prd_cls_id,b.itemtype,b.prd_brand_id,b.stkuom,a.EXPIREDATE order by a.item,a.userfld4 ";

               String aQuery = "select ROW_NUMBER() OVER(ORDER BY a.ITEM ASC) AS Sno,a.ITEM,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_dept_id,'') as PRD_DEPT_ID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,b.stkuom as STKUOM,b.itemtype as ITEMTYPE,a.UserFld4 as BATCH, sum(QTY) QTY,isnull(a.EXPIREDATE, '') as EXPIREDATE ," + 
                 " CASE WHEN ISNULL(A.EXPIREDATE,'')='' then '#000000'" + 
                 " WHEN SUBSTRING(A.EXPIREDATE, 7, 4) +  SUBSTRING(A.EXPIREDATE, 4, 2)+ SUBSTRING(A.EXPIREDATE, 1, 2)  <= CONVERT(VARCHAR(8), GETDATE(), 112) then '#FF0000'" + 
                 " else '#000000' END AS COLORCODE from "
                                + "["
                                + aPlant
                                + "_"
                                + "invmst"
                                + "]"
                                + " A,"
                                + "["
                                + aPlant
                                + "_"
                                + "itemmst"
                                + "]"
                                + " as b where a.item=b.item  and a.item in (SELECT ITEM FROM "
                                + aPlant
                                + "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"+ aItem + "%')"
                                + "and QTY>0  "+expireDt;
                arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

        } catch (Exception e) {
                this.mLogger.exception(this.printLog, "", e);
        }
        return arrList;
}
	
	public ArrayList getInvListAgingSummaryByCost(String aPlant, String aItem,
			String productDesc, Hashtable ht,String loc,String loctypeid,String atoDate) throws Exception {
			ArrayList arrList = new ArrayList();
			    String sCondition=""; 
		            String aLocCond ="";//,dtCondStr="",searchCondord="";
		            
//	            dtCondStr = "  AND CAST((SUBSTRING(c.recvdate,7,4) + '-' + SUBSTRING(c.recvdate,4,2) + '-' + SUBSTRING(c.recvdate,1,2)) AS date) ";
	            
	            if (atoDate.length() > 0) {
//					searchCondord = dtCondStr+ " <= '"  + atoDate + "'  ";
					
				}
			    if (productDesc.length() > 0 ) {
		                if (productDesc.indexOf("%") != -1) {
		                        productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
		                }
		         
				 sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
			    }
			    if (loctypeid != null && loctypeid!="") {
					sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType(aPlant,loctypeid)+" )";
				}

		            if(loc.length()>0){
		            loc = "  AND A.LOC like'%"+loc+"%'";
		                aLocCond = ", a.loc ";
		            }
				try {
					InvMstDAO _InvMstDAO = new InvMstDAO();
					_InvMstDAO.setmLogger(mLogger);
					sCondition = sCondition
							+ " group by a.item,a.loc,c.ITEMDESC,c.stkuom,a.userfld4,b.recvdate,QTY,b.RECQTY"+aLocCond +" Order by ITEM,LOC";

					// Added stkuom to the query
					////-----Modified by Bruhan on Feb 27 2014, Description: To display location data's in uppercase
					String aQuery = "select isnull(a.ITEM,'') ITEM,UPPER(a.LOC) LOC,isnull(c.ITEMDESC,'') ITEMDESC,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ aPlant +"_UOM u where u.UOM=c.STKUOM),'') as STKUOM,isnull(a.userfld4,'') BATCH,QTY, SUM(b.RECQTY) RECQTY,isnull(b.recvdate,'') RECVDATE  from "
							+ "["
							+ aPlant
							+ "_"
							+ "invmst"
							+ "]"
							+ " A,"
							+ "["
							+ aPlant
							+ "_"
							+ "recvdet"
							+ "]"
							+ " as b,"
							+ "["
							+ aPlant
							+ "_"
							+ "itemmst"
							+ "]"
							+ " as c"
							+" where a.item=b.item and b.item=c.item and a.PLANT = b.PLANT and a.PLANT = c.PLANT and a.PLANT = '"+ aPlant+"' and a.item in (SELECT ITEM FROM "
							+ aPlant
							+ "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"
							//+ aItem + "%') AND b.NONSTKFLAG<>'Y' AND  QTY>0 and c.tran_type in('MR','IB') "+loc+searchCondord;
							+ aItem + "%') AND c.NONSTKFLAG<>'Y' AND  QTY>0 and b.tran_type in('MR','IB') "+loc;
					arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
				}
				return arrList;
			}
	public ArrayList getInvVsOutgoing(String aPlant, String aItem,String productDesc, Hashtable ht,String afrmDate,String atoDate) throws Exception {
		ArrayList arrList = new ArrayList();
		//CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
		    String sCondition=""; 
		    String sDtCond="",sAfterDtCond="";//,sBomDtCond="",sAfterBomDtCond=""; 
//	            String aLocCond ="";
		    if (productDesc.length() > 0 ) {
	                if (productDesc.indexOf("%") != -1) {
	                        productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
	                }
	         
			 sCondition = " and replace(ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
		    }
		    if (afrmDate.length() > 0) {

		    	sDtCond = sDtCond + " AND   REPLACE(TRANDATE,'-','') >= '"+ afrmDate + "'  ";
				if (atoDate.length() > 0) {
					sDtCond = sDtCond + " AND  REPLACE(TRANDATE,'-','')<= '" + atoDate + "'  ";
				}
			} else {
				if (atoDate.length() > 0) {
					sDtCond = sDtCond + "   REPLACE(TRANDATE,'-','') <= '" + atoDate + "'  ";
				}
			}
		    if (atoDate.length() > 0) {
		    	sAfterDtCond = sAfterDtCond + " AND  REPLACE(TRANDATE,'-','') > '" + atoDate + "'  ";
			}
		    
		    
		    	String locCond ="  " ; //and LOC in (select LOC from ["+ aPlant+ "_LOCMST])
		        
			try {
				InvMstDAO _InvMstDAO = new InvMstDAO();
				_InvMstDAO.setmLogger(mLogger);
				
				String aQuery = "select plant,ITEM,ITEMDESC,PRD_CLS_ID,ITEMTYPE,PRD_BRAND_ID,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ aPlant +"_UOM u where u.UOM=a.STKUOM),'') as STKUOM,(STOCKONHAND+(TOTISS-TOTISSREV)-(TOTRECV-TOTRECVREV))+(CTOTISS-CTOTISSREV)-(CTOTRECV-CTOTRECVREV) as OPENING,(TOTRECV-TOTRECVREV) AS TOTRECV,(TOTISS) AS TOTAL_ISS,TOTISSREV,  "
						+ " (STOCKONHAND +(CTOTISS-CTOTISSREV)-(CTOTRECV-CTOTRECVREV)) AS CLOSING,(CTOTISS-CTOTISSREV) as ISSUED_AFTER,(CTOTRECV-CTOTRECVREV) as RECVED_AFTER ,STOCKONHAND from ("
						+ "  select plant,item,ITEMDESC,PRD_CLS_ID,ITEMTYPE,PRD_BRAND_ID,ISNULL((SELECT SUM(QTY) from ["+aPlant+"_INVMST] where PLANT =a.PLANT and ITEM =a.ITEM),0) AS STOCKONHAND,"
						+ " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM and DIRTYPE in ('MISC_RECV','ORD_RECV','REPORTING') "+sDtCond+" "+locCond+"  group by item),0)as TOTRECV, "
						+ " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM and DIRTYPE in ('IB_REVERSE','OBRECEIVE_REVERSE','REPORTING_REV') "+sDtCond+" "+locCond+" group by item),0)as TOTRECVREV, "
						+ " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM  "+sDtCond+" "+locCond+" AND   DIRTYPE in ('MISC_ISSUE','ORD_PICK_ISSUE','ORD_ISSUE','ADD_KITTING','POS_TRANSACTION','MOVETOWIP') group by item),0)as TOTISS , "
						+ " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM  "+sDtCond+" "+locCond+" AND   DIRTYPE in ('OBISSUE_REVERSE','POS_REFUND','WIP_REVERSE') group by item),0)as TOTISSREV , "
						+ " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM and DIRTYPE in ('MISC_RECV','ORD_RECV','REPORTING') "+sAfterDtCond+" "+locCond+" group by item),0)as CTOTRECV, "
						+ " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM and DIRTYPE in ('IB_REVERSE','OBRECEIVE_REVERSE','REPORTING_REV') "+sAfterDtCond+" "+locCond+"  group by item),0)as CTOTRECVREV, "
						+ " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM  "+sAfterDtCond+"  "+locCond+" and DIRTYPE in ('MISC_ISSUE','ORD_PICK_ISSUE','ORD_ISSUE','ADD_KITTING','POS_TRANSACTION','MOVETOWIP') group by item),0)as CTOTISS, "
						+ " isnull((SELECT SUM(QTY) from ["+ aPlant+ "_"+ "MOVHIS"+ "] where PLANT=a.plant and ITEM =a.ITEM  "+sAfterDtCond+" "+locCond+" AND   DIRTYPE in ('OBISSUE_REVERSE','POS_REFUND','WIP_REVERSE') group by item),0)as CTOTISSREV  "
						+ "  from ["+ aPlant+ "_"+ "ITEMMST"+ "] a where  PLANT = '"+aPlant +"' and ISNULL(NONSTKFLAG,'')<>'Y'  group by plant,item,ITEMDESC,PRD_CLS_ID,ITEMTYPE,PRD_BRAND_ID )A "
						+" WHERE  PLANT = '"+aPlant +"' and  item in (SELECT ITEM FROM ["+ aPlant+ "_ALTERNATE_ITEM_MAPPING] WHERE ALTERNATE_ITEM_NAME like '"+ aItem + "%') ";
				arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);
			

			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}
			return arrList;
		}
 
	public ArrayList getInvListSummaryAlternateBrand(String aPlant, String aItem,
			String productDesc, Hashtable ht,String loc,String loctypeid,String VinNo,String Model,String cname,String loctypeid2) throws Exception {
			ArrayList arrList = new ArrayList();
			    String sCondition=""; 
//		            String aLocCond ="";
			   /* if (productDesc.length() > 0 ) {
		                if (productDesc.indexOf("%") != -1) {
		                        productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
		                }
		         
				 sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
			    }*/
		            if(VinNo.length()>0){
		            	sCondition = sCondition + "AND A.ALTERNATEITEM IN (SELECT ITEM FROM  [" + aPlant + "_" + "ITEMMST" + "] WHERE VINNO ='"+VinNo+"')";
				    }
		            if(Model.length()>0){
		            	sCondition = sCondition + "AND A.ALTERNATEITEM IN (SELECT ITEM FROM  [" + aPlant + "_" + "ITEMMST" + "] WHERE MODEL ='"+Model+"')";
				    }
			    if (loctypeid != null && loctypeid!="") {
					sCondition = sCondition + " AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(aPlant,loctypeid)+" )";
				}
			    
			    if (loctypeid2 != null && loctypeid2!="") {
					sCondition = sCondition + " AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(aPlant,loctypeid2)+" )";
				}

		            if(loc.length()>0){
		            loc = "  AND A.LOC ='"+loc+"'";
//		                aLocCond = ", A.loc ";
		            }
		            
				try {
					InvMstDAO _InvMstDAO = new InvMstDAO();
					_InvMstDAO.setmLogger(mLogger);
					sCondition = sCondition
							+ " ORDER BY A.ITEM,A.BRAND,A.ALTERNATEITEM,A.ALTERNATEBRAND";
     				String aQuery = "with temptable as( SELECT ISNULL(A.ITEM,'') ITEM, "
							+ "(SELECT ISNULL(ITEMDESC,'')   FROM  [" + aPlant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) ITEMDESC, "
							+ "ISNULL(A.PRD_BRAND_ID,'') BRAND ,"
							+ "ISNULL(A.ALTERNATE_ITEM_NAME,'') ALTERNATEITEM,"
							+ "(SELECT ISNULL(ITEMDESC,'')   FROM [" + aPlant + "_" + "ITEMMST" + "] WHERE ITEM=A.ALTERNATE_ITEM_NAME) ALTERNATEITEMDESC,"
							+ "(SELECT ISNULL(UnitPrice,0)   FROM  [" + aPlant + "_" + "ITEMMST" + "] WHERE ITEM=A.ALTERNATE_ITEM_NAME) UNITPRICE, "
							+ "ISNULL((Select M.OBDISCOUNT from ["+ aPlant +"_CUSTMST] C join ["+ aPlant +"_MULTI_PRICE_MAPPING] M on C.CUSTOMER_TYPE_ID=M.CUSTOMER_TYPE_ID WHERE M.ITEM=A.ALTERNATE_ITEM_NAME AND C.CNAME='"+cname+"'),'') as DISCOUNTBY, "
							//+ "((SELECT ISNULL(UnitPrice,0)   FROM  [" + aPlant + "_" + "ITEMMST" + "] WHERE ITEM=A.ALTERNATE_ITEM_NAME) - ((SELECT ISNULL(UnitPrice,0)   FROM  [" + aPlant + "_" + "ITEMMST" + "] WHERE ITEM=A.ALTERNATE_ITEM_NAME)*(ISNULL((Select REPLACE(M.OBDISCOUNT, '%', '') from ["+ aPlant +"_CUSTMST] C join ["+ aPlant +"_MULTI_PRICE_MAPPING] M on C.CUSTOMER_TYPE_ID=M.CUSTOMER_TYPE_ID WHERE M.ITEM=A.ALTERNATE_ITEM_NAME AND C.CNAME='"+cname+"'),0)))/100) AS UNITPRICE, "
							+ "ISNULL(A.ALTERNATE_ITEM_BRAND_ID,'') ALTERNATEBRAND,"
							+ "(SELECT ISNULL(VINNO,'')   FROM  [" + aPlant + "_" + "ITEMMST" + "] WHERE ITEM=A.ALTERNATE_ITEM_NAME) VINNO, "
							+ "(SELECT ISNULL(MODEL,'')   FROM  [" + aPlant + "_" + "ITEMMST" + "] WHERE ITEM=A.ALTERNATE_ITEM_NAME) MODEL, "
							+ "ISNULL((SELECT ISNULL('/track/ReadFileServlet/?fileLocation='+CATLOGPATH,'../jsp/dist/img/NO_IMG.png')  FROM  [" + aPlant + "_" + "CATALOGMST" + "] WHERE PRODUCTID=A.ALTERNATE_ITEM_NAME),'../jsp/dist/img/NO_IMG.png') CATALOG, "
							+ "ISNULL(B.LOC,'') LOC,"
							+ "SUM(ISNULL(B.QTY,0)) QTY"
							+ " FROM [" + aPlant + "_" + "ALTERNATE_BRAND_ITEM_MAPPING" + "] A LEFT JOIN   [" + aPlant + "_" + "INVMST" + "] B "
							+ " ON A.ALTERNATE_ITEM_NAME=B.ITEM WHERE LOC NOT LIKE 'SHIPPINGAREA%' and LOC NOT LIKE 'TEMP_TO%' OR LOC is null "
							+ " GROUP BY A.ITEM,A.PRD_BRAND_ID,A.ALTERNATE_ITEM_NAME,A.ALTERNATE_ITEM_BRAND_ID,B.LOC "
							+ " ) SELECT * FROM temptable A WHERE A.ITEM !='' "+loc;
							arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
				}
				return arrList;
			}
			
			public ArrayList getSummaryAlternateBrand(String aPlant, String aItem,
			String productDesc, Hashtable ht) throws Exception {
			ArrayList arrList = new ArrayList();
			    String sCondition=""; 
//		            String aLocCond ="";
			   
				try {
					InvMstDAO _InvMstDAO = new InvMstDAO();
					_InvMstDAO.setmLogger(mLogger);
					sCondition = sCondition
							+ " GROUP BY A.ITEM,A.PRD_BRAND_ID,A.ALTERNATE_ITEM_NAME,A.ALTERNATE_ITEM_BRAND_ID ORDER BY A.ITEM,A.PRD_BRAND_ID,A.ALTERNATE_ITEM_NAME,A.ALTERNATE_ITEM_BRAND_ID";
     				String aQuery = "SELECT ISNULL(A.ITEM,'') ITEM, "
							+ "(SELECT ISNULL(ITEMDESC,'')   FROM  [" + aPlant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) ITEMDESC, "
							+ "(SELECT ISNULL(VINNO,'')   FROM  [" + aPlant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) VINNO, "
							+ "(SELECT ISNULL(MODEL,'')   FROM  [" + aPlant + "_" + "ITEMMST" + "] WHERE ITEM=A.ITEM) MODEL, "
							+ "(SELECT ISNULL(UnitPrice,0)   FROM  [" + aPlant + "_" + "ITEMMST" + "] WHERE ITEM=A.ALTERNATE_ITEM_NAME) UNITPRICE, "
							+ "ISNULL(A.PRD_BRAND_ID,'') BRAND ,"
							+ "ISNULL(A.ALTERNATE_ITEM_NAME,'') ALTERNATEITEM,"
							+ "(SELECT ISNULL(ITEMDESC,'')   FROM [" + aPlant + "_" + "ITEMMST" + "] WHERE ITEM=A.ALTERNATE_ITEM_NAME) ALTERNATEITEMDESC,"
							+ "(SELECT ISNULL(VINNO,'')   FROM [" + aPlant + "_" + "ITEMMST" + "] WHERE ITEM=A.ALTERNATE_ITEM_NAME) ALTERNATEITEMVINNO,"
							+ "(SELECT ISNULL(MODEL,'')   FROM [" + aPlant + "_" + "ITEMMST" + "] WHERE ITEM=A.ALTERNATE_ITEM_NAME) ALTERNATEMODEL,"
							+ "ISNULL(A.ALTERNATE_ITEM_BRAND_ID,'') ALTERNATEBRAND"
							+ " FROM [" + aPlant + "_" + "ALTERNATE_BRAND_ITEM_MAPPING" + "] A ";
							
							arrList = _InvMstDAO.selectReport(aQuery, ht, sCondition);

				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
				}
				return arrList;
			}

	public ArrayList getItemList(String pLANT, Hashtable ht,String productDesc,String cname) {
		ArrayList arrList = new ArrayList();
		 String sCondition=" GROUP BY A.ITEM,ITEMDESC,A.PRD_BRAND_ID,VINNO,MODEL,UnitPrice,i.LOC"; 
		 String aQuery ="";
		 InvMstDAO _InvMstDAO = new InvMstDAO();
		 _InvMstDAO.setmLogger(mLogger);
		 if(productDesc.length()>0){
			 productDesc = "  AND A.ITEMDESC like'%"+productDesc+"%'";	                
	            }
		 try {
			 aQuery = "SELECT ISNULL(A.ITEM,'') ITEM,ISNULL(ITEMDESC,'') ITEMDESC,ISNULL(A.PRD_BRAND_ID,'') BRAND ,ISNULL(VINNO,'') VINNO,ISNULL(MODEL,'') MODEL,ISNULL(UnitPrice,0) UNITPRICE "
					 + ",ISNULL((Select M.OBDISCOUNT from ["+ pLANT +"_CUSTMST] C join ["+ pLANT +"_MULTI_PRICE_MAPPING] M on C.CUSTOMER_TYPE_ID=M.CUSTOMER_TYPE_ID WHERE M.ITEM=A.ITEM AND C.CNAME='"+cname+"'),'') as DISCOUNTBY "
					 //+ ",(UnitPrice - (UnitPrice*(ISNULL((Select REPLACE(M.OBDISCOUNT, '%', '') from ["+ pLANT +"_CUSTMST] C join ["+ pLANT +"_MULTI_PRICE_MAPPING] M on C.CUSTOMER_TYPE_ID=M.CUSTOMER_TYPE_ID WHERE M.ITEM=A.ITEM AND C.CNAME='"+cname+"'),0)))/100) AS UNITPRICE "
					 //+ ",ISNULL((SELECT SUM(i.QTY) FROM [" + pLANT + "_" + "INVMST" + "] i where i.ITEM=A.ITEM),0) QTY"
					 + ",i.LOC,SUM(QTY) as QTY"
					 + ",ISNULL((SELECT ISNULL('/track/ReadFileServlet/?fileLocation='+CATLOGPATH,'../jsp/dist/img/NO_IMG.png')  FROM  [" + pLANT + "_" + "CATALOGMST" + "] WHERE PRODUCTID=A.ITEM),'../jsp/dist/img/NO_IMG.png') CATALOG "
					 + " FROM [" + pLANT + "_" + "ITEMMST" + "] A join [" + pLANT + "_" + "INVMST" + "] i on i.ITEM=A.ITEM WHERE A.ITEM NOT IN (select ALTERNATE_ITEM_NAME from "+pLANT+"_ALTERNATE_BRAND_ITEM_MAPPING) AND LOC NOT LIKE 'SHIPPINGAREA%' and LOC NOT LIKE 'TEMP_TO%' AND A.ITEM!='' "+productDesc;
			 //+ " FROM [" + pLANT + "_" + "ITEMMST" + "] A WHERE A.ITEM IN (SELECT ISNULL(ITEM,'') ITEM FROM [" + pLANT + "_" + "ALTERNATE_BRAND_ITEM_MAPPING" + "] ) "+productDesc;
		arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);
		 	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		 	}
		 	return arrList;
	}
	public ArrayList getItemInvList(String pLANT,String loc, Hashtable ht) {	
		ArrayList arrList = new ArrayList();
		 String sCondition=""; 
		 String aQuery ="";
		 if(loc.length()>0){
	            loc = "  AND b.LOC ='"+loc+"'";	                
	            }
		 InvMstDAO _InvMstDAO = new InvMstDAO();
		 _InvMstDAO.setmLogger(mLogger);
		 try {
			 sCondition = sCondition
						+ " GROUP BY A.ITEM,A.PRD_BRAND_ID,B.LOC ORDER BY A.ITEM,A.PRD_BRAND_ID";
			 aQuery = "SELECT ISNULL(A.ITEM,'') ITEM,ISNULL(ITEMDESC,'') ITEMDESC,ISNULL(A.PRD_BRAND_ID,'') BRAND ,ISNULL(VINNO,'') VINNO,ISNULL(MODEL,'') MODEL,ISNULL(UnitPrice,0) UNITPRICE, "
					 + "ISNULL((SELECT ISNULL('/track/ReadFileServlet/?fileLocation='+CATLOGPATH,'../jsp/dist/img/NO_IMG.png')  FROM  [" + pLANT + "_" + "CATALOGMST" + "] WHERE PRODUCTID=A.ITEM),'../jsp/dist/img/NO_IMG.png') CATALOG, "
					 + "ISNULL(B.LOC,'') LOC,"
					 + "SUM(ISNULL(B.QTY,0)) QTY"
					 + " FROM [" + pLANT + "_" + "ITEMMST" + "] A LEFT JOIN   [" + pLANT + "_" + "INVMST" + "] B "
					 + " ON A.ITEM=B.ITEM WHERE LOC NOT LIKE 'SHIPPINGAREA%' and LOC NOT LIKE 'TEMP_TO%' OR LOC is null "+loc;
		arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);
		 	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		 	}
		 	return arrList;
	}
	public ArrayList getInvListSummaryDetail(String aPlant, String aItem,
			String productDesc, Hashtable ht,String loc,String loctypeid,String VinNo,String Model,String cname) throws Exception {
			ArrayList arrList = new ArrayList();
			    String sCondition=""; 
//		            String aLocCond ="";
		            
		            if(loc.length()>0){
			            loc = " AND LOC ='"+loc+"'";
//			                aLocCond = ", b.loc ";
			            }
		            
		            if(productDesc.length()>0){
		   			 productDesc = "  AND A.ITEMDESC like'%"+productDesc+"%'";	                
		   	            }
		            
		            if(VinNo.length()>0){
		            	sCondition = sCondition + "AND VINNO ='"+VinNo+"'";
				    }
		            if(Model.length()>0){
		            	sCondition = sCondition + "AND  MODEL ='"+Model+"'";
				    }
			    if (loctypeid != null && loctypeid!="") {
					sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType(aPlant,loctypeid)+" )";
				}

		            
		            
				try {
					InvMstDAO _InvMstDAO = new InvMstDAO();
					_InvMstDAO.setmLogger(mLogger);
					sCondition = sCondition
							+ " ORDER BY A.ITEM,BRAND";
					String aQuery ="with temptable as( SELECT ISNULL(A.ITEM,'') ITEM,ISNULL(ITEMDESC,'') ITEMDESC,ISNULL(A.PRD_BRAND_ID,'') BRAND ,ISNULL(VINNO,'') VINNO,ISNULL(MODEL,'') MODEL,ISNULL(UnitPrice,0) UNITPRICE, "
							+ "ISNULL((Select M.OBDISCOUNT from ["+ aPlant +"_CUSTMST] C join ["+ aPlant +"_MULTI_PRICE_MAPPING] M on C.CUSTOMER_TYPE_ID=M.CUSTOMER_TYPE_ID WHERE M.ITEM=A.ITEM AND C.CNAME='"+cname+"'),'') as DISCOUNTBY, "
							//+ "(UnitPrice - (UnitPrice*(ISNULL((Select REPLACE(M.OBDISCOUNT, '%', '') from ["+ aPlant +"_CUSTMST] C join ["+ aPlant +"_MULTI_PRICE_MAPPING] M on C.CUSTOMER_TYPE_ID=M.CUSTOMER_TYPE_ID WHERE M.ITEM=A.ITEM AND C.CNAME='"+cname+"'),0)))/100) AS UNITPRICE, "
							+ "ISNULL((SELECT ISNULL('/track/ReadFileServlet/?fileLocation='+CATLOGPATH,'../jsp/dist/img/NO_IMG.png')  FROM  [" + aPlant + "_" + "CATALOGMST" + "] WHERE PRODUCTID=A.ITEM),'../jsp/dist/img/NO_IMG.png') CATALOG, "
							+ "ISNULL(B.LOC,'') LOC,SUM(ISNULL(B.QTY,0)) QTY "
							+ "FROM [" + aPlant + "_" + "ITEMMST" + "] A LEFT JOIN [" + aPlant + "_" + "INVMST" + "] B "
							+ " ON A.ITEM=B.ITEM WHERE LOC NOT LIKE 'SHIPPINGAREA%' and LOC NOT LIKE 'TEMP_TO%' OR LOC is null "
							+ "GROUP BY A.ITEM,A.ITEMDESC,A.VINNO,A.MODEL,A.UnitPrice,A.PRD_BRAND_ID,B.LOC "
							+ ") SELECT * FROM temptable A WHERE A.ITEM !='' "+loc+productDesc;
					arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);
				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
				}
				return arrList;
			}
	public ArrayList getInvListSummaryGroupByProdMultiUom(String aPlant, String aItem,
			String productDesc, Hashtable ht,String loc,String loctypeid,String loctypeid2,String loctypeid3,String UOM,String sort) throws Exception {
			ArrayList arrList = new ArrayList();			
			    String sCondition=""; 
		            String aLocCond ="";
		            String COMP_INDUSTRY =new PlantMstDAO().getCOMP_INDUSTRY(aPlant);
			    if (productDesc.length() > 0 ) {
		                if (productDesc.indexOf("%") != -1) {
		                        productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
		                }
		         
				 sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
			    }
			    if (loctypeid != null && loctypeid!="") {
					sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType(aPlant,loctypeid)+" )";
				}
			    if (loctypeid2 != null && loctypeid2!="") {
					sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType2(aPlant,loctypeid2)+" )";
				}
			    if (loctypeid3 != null && loctypeid3!="") {
					sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType3(aPlant,loctypeid3)+" )";
				}

		            if(loc.length()>0){
		            loc = "  AND A.LOC like'%"+loc+"%'";
		                aLocCond = ", a.loc ";
		            }
				try {
					InvMstDAO _InvMstDAO = new InvMstDAO();
					_InvMstDAO.setmLogger(mLogger);
					sCondition = sCondition
						+ " Order by item,loc";
					
					String aQty="";
					//String aQty=" AND  QTY>0 ";
					//if(COMP_INDUSTRY.equals("Retail")) 
						//aQty=" AND  QTY!=0 ";
					
					String Sortby="";
					if(sort.equalsIgnoreCase("POSITIVE"))
						Sortby=" AND  isnull(STKQTY,0)>0 ";
//						Sortby=" AND  isnull(STKQTY,0)>=0 ";
					else if(sort.equalsIgnoreCase("NAGATIVE"))
						Sortby=" AND  isnull(STKQTY,0)<0 ";
					else if(sort.equalsIgnoreCase("ZERO"))
						Sortby=" AND  isnull(STKQTY,0)=0 ";
					else
						Sortby="";
					
					String aQuery="";
					if(UOM.length()>0){
					 aQuery = "with tempdb as ( "
							+"select a.ITEM,UPPER(a.LOC) LOC,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_dept_id,'') as PRD_DEPT_ID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,isnull(b.MODEL,'') as MODEL,b.itemtype as ITEMTYPE,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ aPlant +"_UOM u where u.UOM='"+UOM+"'),'') as STKUOM, '"+UOM+"' as  INVENTORYUOM, ISNULL((select u.QPUOM from "+ aPlant +"_UOM u where u.UOM='"+UOM+"'),1) as UOMQTY, sum(QTY) STKQTY,"
							+ "ISNULL((SELECT LOCDESC  FROM ["+ aPlant +"_LOCMST] D WHERE D.LOC= A.LOC),'') LOCDESC,"
							+ "ISNULL((SELECT LOC_TYPE_ID  FROM ["+ aPlant +"_LOCMST] D WHERE D.LOC= A.LOC),'') LOC_TYPE_ID from "
							+ "["
							+ aPlant
							+ "_"
							+ "invmst"
							+ "]"
							+ " A,"
							+ "["
							+ aPlant
							+ "_"
							+ "itemmst"
							+ "]"
							+ " as b where a.item=b.item and a.PLANT = b.PLANT and a.PLANT = '"+ aPlant+"' and a.item in (SELECT ITEM FROM "
							+ aPlant
							+ "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"
							+ aItem + "%') AND b.NONSTKFLAG<>'Y' "
							+ "AND b.IsActive='Y' "
							+ aQty+" "+loc +" group by a.item,a.loc,b.ITEMDESC,b.prd_cls_id,b.prd_dept_id,b.MODEL,b.itemtype,b.prd_brand_id,b.stkuom "+aLocCond+")"
							+ "select  ITEM,LOC,ITEMDESC,PRDCLSID,PRD_DEPT_ID,PRD_BRAND_ID,MODEL,ITEMTYPE,INVENTORYUOM,convert(int,(STKQTY/UOMQTY)) as INVUOMQTY,STKUOM,"
							+ "case when INVENTORYUOM!=STKUOM then (case when UOMQTY<=STKQTY then STKQTY-(convert(int,(STKQTY/UOMQTY))*UOMQTY) else STKQTY end) else 0 end QTY,LOCDESC,LOC_TYPE_ID"
							+ " from tempdb b where item !='' "+Sortby;
					}
					else
					{
						aQuery = "with tempdb as ( "
								+"select a.ITEM,UPPER(a.LOC) LOC,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_dept_id,'') as PRD_DEPT_ID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,isnull(b.MODEL,'') as MODEL,b.itemtype as ITEMTYPE,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ aPlant +"_UOM u where u.UOM=(case when '"+UOM+"'='' then ISNULL(b.INVENTORYUOM,'') else '"+UOM+"' end)),'') as STKUOM, ISNULL(b.INVENTORYUOM,'') as  INVENTORYUOM, ISNULL((select u.QPUOM from "+ aPlant +"_UOM u where u.UOM=b.INVENTORYUOM),1) as UOMQTY, sum(QTY) STKQTY,"
								+ "ISNULL((SELECT LOCDESC  FROM ["+ aPlant +"_LOCMST] D WHERE D.LOC= A.LOC),'') LOCDESC,"
								+ "ISNULL((SELECT LOC_TYPE_ID  FROM ["+ aPlant +"_LOCMST] D WHERE D.LOC= A.LOC),'') LOC_TYPE_ID from "
								+ "["
								+ aPlant
								+ "_"
								+ "invmst"
								+ "]"
								+ " A,"
								+ "["
								+ aPlant
								+ "_"
								+ "itemmst"
								+ "]"
								+ " as b where a.item=b.item and a.PLANT = b.PLANT and a.PLANT = '"+ aPlant+"' and a.item in (SELECT ITEM FROM "
								+ aPlant
								+ "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"
								+ aItem + "%') AND b.NONSTKFLAG<>'Y' "
								+ "AND b.IsActive='Y' "
								+ aQty+" "+loc +" group by a.item,a.loc,b.ITEMDESC,b.prd_cls_id,b.prd_dept_id,b.MODEL,b.itemtype,b.prd_brand_id,b.stkuom,b.INVENTORYUOM "+aLocCond+")"
								+ "select  ITEM,LOC,ITEMDESC,PRDCLSID,PRD_DEPT_ID,PRD_BRAND_ID,MODEL,ITEMTYPE,INVENTORYUOM,isnull(STKQTY,0) as QTY,STKUOM,"
								+ "case when 0<ISNULL(UOMQTY,0) then (convert(float,(STKQTY/UOMQTY))) else STKQTY end INVUOMQTY,LOCDESC,LOC_TYPE_ID"
								+ " from tempdb b where item !='' "+Sortby;
					}
					arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);
					
				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
				}
				return arrList;
			}
	public ArrayList getInvListSummaryGroupByProdMultiUomNew(String aPlant, String aItem,
			String productDesc, Hashtable ht,String loc,String loctypeid,String loctypeid2,String loctypeid3,String UOM,String sort) throws Exception {
		ArrayList arrList = new ArrayList();			
		String sCondition=""; 
		String aLocCond ="";
		String COMP_INDUSTRY =new PlantMstDAO().getCOMP_INDUSTRY(aPlant);
		if (productDesc.length() > 0 ) {
			if (productDesc.indexOf("%") != -1) {
				productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
			}
			
			sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
		}
		if (loctypeid != null && loctypeid!="") {
			sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType(aPlant,loctypeid)+" )";
		}
		if (loctypeid2 != null && loctypeid2!="") {
			sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType2(aPlant,loctypeid2)+" )";
		}
		if (loctypeid3 != null && loctypeid3!="") {
			sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType3(aPlant,loctypeid3)+" )";
		}
		
		if(loc.length()>0){
			loc = "  AND A.LOC like'%"+loc+"%'";
		}
		try {
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
			sCondition = sCondition
					+ " Order by item";
			
			String aQty="";
			//String aQty=" AND  QTY>0 ";
			//if(COMP_INDUSTRY.equals("Retail")) 
				//aQty="";
			//aQty=" AND  QTY!=0 ";
			
			String Sortby="";
			if(sort.equalsIgnoreCase("POSITIVE"))
				Sortby=" AND  isnull(STKQTY,0)>0 ";
//				Sortby=" AND  isnull(STKQTY,0)>=0 ";
			else if(sort.equalsIgnoreCase("NAGATIVE"))
				Sortby=" AND  isnull(STKQTY,0)<0 ";
			else if(sort.equalsIgnoreCase("ZERO"))
				Sortby=" AND  isnull(STKQTY,0)=0 ";
			else
				Sortby="";
			
			String aQuery="";
			/*if(UOM.length()>0){
				aQuery = "with tempdb as ( "
						+"select a.ITEM,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_dept_id,'') as PRD_DEPT_ID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,isnull(b.MODEL,'') as MODEL,b.itemtype as ITEMTYPE,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ aPlant +"_UOM u where u.UOM='"+UOM+"'),'') as STKUOM, '"+UOM+"' as  INVENTORYUOM, ISNULL((select u.QPUOM from "+ aPlant +"_UOM u where u.UOM='"+UOM+"'),1) as UOMQTY, sum(QTY) STKQTY, b.UnitPrice,b.COST from"
						+ "["
						+ aPlant
						+ "_"
						+ "invmst"
						+ "]"
						+ " A,"
						+ "["
						+ aPlant
						+ "_"
						+ "item"
						+ "mst"
						+ "]"
						+ " as b where a.item=b.item and a.PLANT = b.PLANT and a.PLANT = '"+ aPlant+"' and a.item in (SELECT ITEM FROM "
						+ aPlant
						+ "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"
						+ aItem + "%') AND b.NONSTKFLAG<>'Y' "+aQty+" "+loc +" group by a.item,b.ITEMDESC,b.prd_cls_id,b.prd_dept_id,b.MODEL,b.itemtype,b.prd_brand_id,b.stkuom,b.UnitPrice,b.COST "+aLocCond+")"
						+ "select  ITEM,ITEMDESC,PRDCLSID,PRD_DEPT_ID,PRD_BRAND_ID,MODEL,ITEMTYPE,INVENTORYUOM,convert(int,(STKQTY/UOMQTY)) as INVUOMQTY,STKUOM,"
						+ "case when INVENTORYUOM!=STKUOM then (case when UOMQTY<=STKQTY then STKQTY-(convert(int,(STKQTY/UOMQTY))*UOMQTY) else STKQTY end) else 0 end QTY,UnitPrice,COST"
						+ " from tempdb b where item !='' "+Sortby;
			}
			else
			{
				aQuery = "with tempdb as ( "
						+"select a.ITEM,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_dept_id,'') as PRD_DEPT_ID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,isnull(b.MODEL,'') as MODEL,b.itemtype as ITEMTYPE,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ aPlant +"_UOM u where u.UOM=(case when '"+UOM+"'='' then ISNULL(b.INVENTORYUOM,'') else '"+UOM+"' end)),'') as STKUOM, ISNULL(b.INVENTORYUOM,'') as  INVENTORYUOM, ISNULL((select u.QPUOM from "+ aPlant +"_UOM u where u.UOM=b.INVENTORYUOM),1) as UOMQTY, sum(QTY) STKQTY, b.UnitPrice,b.COST from"
						+ "["
						+ aPlant
						+ "_"
						+ "invmst"
						+ "]"
						+ " A,"
						+ "["
						+ aPlant
						+ "_"
						+ "itemmst"
						+ "]"
						+ " as b where a.item=b.item and a.PLANT = b.PLANT and a.PLANT = '"+ aPlant+"' and a.item in (SELECT ITEM FROM "
						+ aPlant
						+ "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"
						+ aItem + "%') AND b.NONSTKFLAG<>'Y' "+aQty+" "+loc +" group by a.item,b.ITEMDESC,b.prd_cls_id,b.prd_dept_id,b.MODEL,b.itemtype,b.prd_brand_id,b.stkuom,b.INVENTORYUOM,b.UnitPrice,b.COST "+aLocCond+")"
						+ "select  ITEM,ITEMDESC,PRDCLSID,PRD_DEPT_ID,PRD_BRAND_ID,MODEL,ITEMTYPE,INVENTORYUOM,isnull(STKQTY,0) as QTY,STKUOM,"
						+ "case when 0<ISNULL(UOMQTY,0) then (convert(float,(STKQTY/UOMQTY))) else STKQTY end INVUOMQTY,UnitPrice,COST"
						+ " from tempdb b where item !='' "+Sortby;
			}*/
			if(UOM.length()>0){
				aQuery = "with tempdb as ( "
						+"select b.ITEM,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_dept_id,'') as PRD_DEPT_ID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,isnull(b.MODEL,'') as MODEL,b.itemtype as ITEMTYPE,ISNULL('/track/ReadFileServlet/?fileLocation='+CATLOGPATH,'../jsp/dist/img/NO_IMG.png') AS CATALOGPATH,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ aPlant +"_UOM u where u.UOM='"+UOM+"'),'') as STKUOM, '"+UOM+"' as  INVENTORYUOM, ISNULL((select u.QPUOM from "+ aPlant +"_UOM u where u.UOM='"+UOM+"'),1) as UOMQTY, ISNULL(sum(QTY),0) STKQTY, b.UnitPrice,b.COST from"
						+ "["
						+ aPlant
						+ "_"
						+ "itemmst"
						+ "]"
						+ " b left join "
						+ "["
						+ aPlant
						+ "_"
						+ "invmst"
						+ "]"
						+ " as a on a.item=b.item and a.PLANT = b.PLANT where b.PLANT = '"+ aPlant+"' and b.item in (SELECT ITEM FROM "
						+ aPlant
						+ "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"
						+ aItem + "%') AND b.NONSTKFLAG<>'Y' "
						+ "AND b.IsActive='Y' "
						+ aQty+" "+loc +" group by b.item,b.ITEMDESC,b.prd_cls_id,b.prd_dept_id,b.MODEL,b.itemtype,CATALOGPATH,b.prd_brand_id,b.stkuom, b.UnitPrice,b.COST "+aLocCond+")"
						+ "select  ITEM,ITEMDESC,PRDCLSID,PRD_DEPT_ID,PRD_BRAND_ID,MODEL,ITEMTYPE,CATALOGPATH,INVENTORYUOM,convert(int,(STKQTY/UOMQTY)) as INVUOMQTY,STKUOM,"
						+ "case when INVENTORYUOM!=STKUOM then (case when UOMQTY<=STKQTY then STKQTY-(convert(int,(STKQTY/UOMQTY))*UOMQTY) else STKQTY end) else 0 end QTY,UnitPrice,COST"
						+ " from tempdb b where item !='' "+Sortby;
			}
			else
			{
				aQuery = "with tempdb as ( "
						+"select b.ITEM,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_dept_id,'') as PRD_DEPT_ID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,isnull(b.MODEL,'') as MODEL,b.itemtype as ITEMTYPE,ISNULL('/track/ReadFileServlet/?fileLocation='+CATLOGPATH,'../jsp/dist/img/NO_IMG.png') AS CATALOGPATH,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ aPlant +"_UOM u where u.UOM=(case when '"+UOM+"'='' then ISNULL(b.INVENTORYUOM,'') else '"+UOM+"' end)),'') as STKUOM, ISNULL(b.INVENTORYUOM,'') as  INVENTORYUOM, ISNULL((select u.QPUOM from "+ aPlant +"_UOM u where u.UOM=b.INVENTORYUOM),1) as UOMQTY, ISNULL(sum(QTY),0) STKQTY, b.UnitPrice,b.COST from"
						+ "["
						+ aPlant
						+ "_"
						+ "itemmst"
						+ "]"
						+ " b left join "
						+ "["
						+ aPlant
						+ "_"
						+ "invmst"
						+ "]"
						+ " as a on a.item=b.item and a.PLANT = b.PLANT where b.PLANT = '"+ aPlant+"' and b.item in (SELECT ITEM FROM "
						+ aPlant
						+ "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"
						+ aItem + "%') AND b.NONSTKFLAG<>'Y' "
						+ "AND b.IsActive='Y' "
						+ aQty+" "+loc +" group by b.item,b.ITEMDESC,b.prd_cls_id,b.prd_dept_id,b.MODEL,b.itemtype,CATLOGPATH,b.prd_brand_id,b.stkuom,b.INVENTORYUOM, b.UnitPrice,b.COST "+aLocCond+")"
						+ "select  ITEM,ITEMDESC,PRDCLSID,PRD_DEPT_ID,PRD_BRAND_ID,MODEL,ITEMTYPE,CATALOGPATH,INVENTORYUOM,isnull(STKQTY,0) as QTY,STKUOM,"
						+ "case when 0<ISNULL(UOMQTY,0) then (convert(float,(STKQTY/UOMQTY))) else STKQTY end INVUOMQTY,UnitPrice,COST"
						+ " from tempdb b where item !='' "+Sortby;
			}
			arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
	}
	
	
	public ArrayList getInvListValuationSummary(String plant,String PRD_DEPT_ID,String PRD_CLS_ID,String PRD_BRAND_ID,String LOC,String sort) throws Exception {
		ArrayList alData = new ArrayList();
		java.sql.Connection con = null;
		String extCond = "";
		
		
		if (PRD_DEPT_ID.length() > 0 ) {
			if (PRD_DEPT_ID.indexOf("%") != -1) {
				PRD_DEPT_ID = PRD_DEPT_ID.substring(0, PRD_DEPT_ID.indexOf("%") - 1);
			}
			extCond = " and replace(PRD_DEPT_ID,' ','') like '%"+ StrUtils.InsertQuotes(PRD_DEPT_ID.replaceAll(" ","")) + "%' ";
		}

		if (PRD_CLS_ID.length() > 0 ) {
			if (PRD_CLS_ID.indexOf("%") != -1) {
				PRD_CLS_ID = PRD_CLS_ID.substring(0, PRD_CLS_ID.indexOf("%") - 1);
			}
			extCond = " and replace(PRD_CLS_ID,' ','') like '%"+ StrUtils.InsertQuotes(PRD_CLS_ID.replaceAll(" ","")) + "%' ";
		}
		
		
		if (PRD_BRAND_ID.length() > 0 ) {
			if (PRD_BRAND_ID.indexOf("%") != -1) {
				PRD_BRAND_ID = PRD_BRAND_ID.substring(0, PRD_BRAND_ID.indexOf("%") - 1);
			}
			extCond = " and replace(PRD_BRAND_ID,' ','') like '%"+ StrUtils.InsertQuotes(PRD_BRAND_ID.replaceAll(" ","")) + "%' ";
		}
		
		
		String bcondition = "";
		StringBuffer sql = new StringBuffer();
			if(sort.equalsIgnoreCase("Department")){
				sql.append(" select a.PRD_DEPT_ID as NAME,SUM(QTY) TOTALQTY from " + plant +"_invmst i join " + plant +"_itemmst a on a.item=i.item AND PRD_DEPT_ID!='' and I.LOC like '%" + LOC +"%' "+extCond+" GROUP BY a.PRD_DEPT_ID,I.LOC  ORDER BY PRD_DEPT_ID");
			}else if(sort.equalsIgnoreCase("Category")){
				
				sql.append(" select a.PRD_CLS_ID as NAME,SUM(QTY) TOTALQTY from " + plant +"_invmst i join " + plant +"_itemmst a on a.item=i.item AND PRD_CLS_ID!='' and I.LOC like '%" + LOC +"%' "+extCond+" GROUP BY a.PRD_CLS_ID,I.LOC  ORDER BY PRD_CLS_ID");
			}else if(sort.equalsIgnoreCase("Brand")){
			
				sql.append(" select a.PRD_BRAND_ID as NAME,SUM(QTY) TOTALQTY from " + plant +"_invmst i join " + plant +"_itemmst a on a.item=i.item AND PRD_BRAND_ID!='' and I.LOC like '%" + LOC +"%' "+extCond+" GROUP BY a.PRD_BRAND_ID,I.LOC  ORDER BY PRD_BRAND_ID");
			}
				try {
			con = com.track.gates.DbBean.getConnection();
			
			alData = selectData(con, sql.toString());
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return alData;
		
	}
	
	public ArrayList getInvListSummaryWithOutPriceMultiUom(Hashtable ht, String aPlant,String aItem,String productDesc,String loctypeid,String loctypeid2,String loctypeid3,String loc,String UOM) throws Exception {
        ArrayList arrList = new ArrayList();        
      //CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
        String sCondition ="";
	    if (productDesc.length() > 0 ) {
        if (productDesc.indexOf("%") != -1) {
                productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
        }
        
         sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
	    }
	  	        
        if (!loc.equalsIgnoreCase("") && !loc.equalsIgnoreCase("")) {
        	sCondition = sCondition+" AND LOC like '%"+loc+"%'";
        }
        
        if (!loctypeid.equalsIgnoreCase("") && !loctypeid.equalsIgnoreCase("")) {
			sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType(aPlant,loctypeid)+" )";
		}
        
        if (!loctypeid2.equalsIgnoreCase("") && !loctypeid2.equalsIgnoreCase("")) {
			sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType2(aPlant,loctypeid2)+" )";
		}
        if (!loctypeid3.equalsIgnoreCase("") && !loctypeid3.equalsIgnoreCase("")) {
			sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType3(aPlant,loctypeid3)+" )";
		}


        try {
                InvMstDAO _InvMstDAO = new InvMstDAO();
                _InvMstDAO.setmLogger(mLogger);
               sCondition = sCondition  + "  order by item ,loc  ";

                // Added stkuom to the query
                //-----Modified by Bruhan on Feb 27 2014, Description: To display location data's in uppercase
               String aQuery="";
				if(UOM.length()>0){
				 aQuery = "with tempdb as ( "
						 		+"select a.ITEM,isnull(b.REMARK1,'') as REMARK1,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_dept_id,'') as PRD_DEPT_ID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,isnull(b.MODEL,'') as MODEL,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ aPlant +"_UOM u where u.UOM='"+UOM+"'),'') as STKUOM,b.itemtype as ITEMTYPE,UPPER(a.LOC) LOC,a.UserFld4 as BATCH, sum(QTY) STKQTY,sum(b.UNITPRICE)as UNITPRICE, '"+UOM+"' as  INVENTORYUOM, ISNULL((select u.QPUOM from "+ aPlant +"_UOM u where u.UOM='"+UOM+"'),1) as UOMQTY"
                                + "ISNULL((SELECT LOCDESC  FROM ["+ aPlant +"_LOCMST] D WHERE D.LOC= A.LOC),'') LOCDESC,"
                                + "ISNULL((SELECT LOC_TYPE_ID  FROM ["+ aPlant +"_LOCMST] D WHERE D.LOC= A.LOC),'') LOC_TYPE_ID from["
                                + aPlant
                                + "_"
                                + "invmst"
                                + "]"
                                + " A,"
                                + "["
                                + aPlant
                                + "_"
                                + "itemmst"
                                + "]"
                                + " as b where a.item=b.item  and a.item in (SELECT ITEM FROM "
                                + aPlant
                                + "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"+ aItem + "%')"
                                + " and b.NONSTKFLAG<>'Y' and QTY>0 group by a.item,b.REMARK1,a.loc,a.userfld4,b.itemdesc,b.prd_cls_id,b.prd_dept_id,b.MODEL,b.itemtype,b.prd_brand_id,b.stkuom,b.INVENTORYUOM ) "
                                + "select  ITEM,LOC,REMARK1,ITEMDESC,PRDCLSID,PRD_DEPT_ID,PRD_BRAND_ID,MODEL,ITEMTYPE,BATCH,UNITPRICE,INVENTORYUOM,convert(int,(STKQTY/UOMQTY)) as INVUOMQTY,STKUOM,"
								+ "case when INVENTORYUOM!=STKUOM then (case when UOMQTY<=STKQTY then STKQTY-(convert(int,(STKQTY/UOMQTY))*UOMQTY) else STKQTY end) else 0 end QTY,LOCDESC,LOC_TYPE_ID"
								+ " from tempdb b where ITEM!='' ";
				}
				else
				{
					aQuery = "with tempdb as ( "
					+"select a.ITEM,isnull(b.REMARK1,'') as REMARK1,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_dept_id,'') as PRD_DEPT_ID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,isnull(b.MODEL,'') as MODEL,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ aPlant +"_UOM u where u.UOM=(case when '"+UOM+"'='' then ISNULL(b.INVENTORYUOM,'') else '"+UOM+"' end)),'') as STKUOM,b.itemtype as ITEMTYPE,UPPER(a.LOC) LOC,a.UserFld4 as BATCH, sum(QTY) STKQTY,sum(b.UNITPRICE)as UNITPRICE, ISNULL(b.INVENTORYUOM,'') as  INVENTORYUOM, ISNULL((select u.QPUOM from "+ aPlant +"_UOM u where u.UOM=b.INVENTORYUOM),1) as UOMQTY,"
                            +"ISNULL((SELECT LOCDESC  FROM ["+ aPlant +"_LOCMST] D WHERE D.LOC= A.LOC),'') LOCDESC,"
                            + "ISNULL((SELECT LOC_TYPE_ID  FROM ["+ aPlant +"_LOCMST] D WHERE D.LOC= A.LOC),'') LOC_TYPE_ID from["
                            + aPlant
                            + "_"
                            + "invmst"
                            + "]"
                            + " A,"
                            + "["
                            + aPlant
                            + "_"
                            + "itemmst"
                            + "]"
                            + " as b where a.item=b.item  and a.item in (SELECT ITEM FROM "
                            + aPlant
                            + "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"+ aItem + "%')"
                            + " and b.NONSTKFLAG<>'Y' and QTY>0 group by a.item,b.REMARK1,a.loc,a.userfld4,b.itemdesc,b.prd_cls_id,b.prd_dept_id,b.MODEL,b.itemtype,b.prd_brand_id,b.stkuom,b.INVENTORYUOM ) "
                            + "select  ITEM,LOC,REMARK1,ITEMDESC,PRDCLSID,PRD_DEPT_ID,PRD_BRAND_ID,MODEL,ITEMTYPE,BATCH,UNITPRICE,INVENTORYUOM,isnull(STKQTY,0) as QTY,STKUOM,"
							+ "case when 0<ISNULL(UOMQTY,0) then (convert(float,(STKQTY/UOMQTY))) else STKQTY end INVUOMQTY,LOCDESC,LOC_TYPE_ID"
							+ " from tempdb b where ITEM!='' ";
				}
                arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

        } catch (Exception e) {
                this.mLogger.exception(this.printLog, "", e);
        }
        return arrList;
}
	

	public ArrayList getInvListManualSummaryWithOutPriceMultiUom(Hashtable ht, String aPlant,String aItem,String productDesc,String loctypeid,String loctypeid2,String loctypeid3,String loc,String UOM) throws Exception {
        ArrayList arrList = new ArrayList();        
      //CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
        String sCondition ="";
	    if (productDesc.length() > 0 ) {
        if (productDesc.indexOf("%") != -1) {
                productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
        }
        
         sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
	    }
	  	        
        if (!loc.equalsIgnoreCase("") && !loc.equalsIgnoreCase("")) {
        	sCondition = sCondition+" AND LOC like '%"+loc+"%'";
        }
        
        if (!loctypeid.equalsIgnoreCase("") && !loctypeid.equalsIgnoreCase("")) {
			sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType(aPlant,loctypeid)+" )";
		}
        
        if (!loctypeid2.equalsIgnoreCase("") && !loctypeid2.equalsIgnoreCase("")) {
			sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType2(aPlant,loctypeid2)+" )";
		}
        if (!loctypeid3.equalsIgnoreCase("") && !loctypeid3.equalsIgnoreCase("")) {
			sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType3(aPlant,loctypeid3)+" )";
		}


        try {
                InvMstDAO _InvMstDAO = new InvMstDAO();
                _InvMstDAO.setmLogger(mLogger);
               sCondition = sCondition  + "  order by item ,loc  ";

                // Added stkuom to the query
                //-----Modified by Bruhan on Feb 27 2014, Description: To display location data's in uppercase
               String aQuery="";
				if(UOM.length()>0){
				 aQuery = "with tempdb as ( "
						 		+"select a.ITEM,isnull(b.REMARK1,'') as REMARK1,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_dept_id,'') as PRD_DEPT_ID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,isnull(b.MODEL,'') as MODEL,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ aPlant +"_UOM u where u.UOM='"+UOM+"'),'') as STKUOM,b.itemtype as ITEMTYPE,UPPER(a.LOC) LOC,a.UserFld4 as BATCH, sum(QTY) STKQTY,sum(b.UNITPRICE)as UNITPRICE, '"+UOM+"' as  INVENTORYUOM, ISNULL((select u.QPUOM from "+ aPlant +"_UOM u where u.UOM='"+UOM+"'),1) as UOMQTY"
                                + " from["
                                + aPlant
                                + "_"
                                + "invmst"
                                + "]"
                                + " A,"
                                + "["
                                + aPlant
                                + "_"
                                + "itemmst"
                                + "]"
                                + " as b where a.item=b.item  and a.item in (SELECT ITEM FROM "
                                + aPlant
                                + "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"+ aItem + "%')"
                                + " and b.NONSTKFLAG<>'Y' group by a.item,b.REMARK1,a.loc,a.userfld4,b.itemdesc,b.prd_cls_id,b.prd_dept_id,b.MODEL,b.itemtype,b.prd_brand_id,b.stkuom,b.INVENTORYUOM ) "
                                + "select  ITEM,LOC,REMARK1,ITEMDESC,PRDCLSID,PRD_DEPT_ID,PRD_BRAND_ID,MODEL,ITEMTYPE,BATCH,UNITPRICE,INVENTORYUOM,convert(int,(STKQTY/UOMQTY)) as INVUOMQTY,STKUOM,"
								+ "case when INVENTORYUOM!=STKUOM then (case when UOMQTY<=STKQTY then STKQTY-(convert(int,(STKQTY/UOMQTY))*UOMQTY) else STKQTY end) else 0 end QTY"
								+ ",ISNULL((select u.CR_DATE from "+aPlant+"_STKTAKE u where u.ITEM=b.ITEM and u.BATCH=b.BATCH and u.LOC=b.LOC and u.STATUS='N'),'') as CRDATE "
								+ ",ISNULL((select sum(u.QTY) from "+aPlant+"_STKTAKE u where u.ITEM=b.ITEM and u.BATCH=b.BATCH and u.LOC=b.LOC and u.STATUS='N'),0) as STKTAKEQTY "
								+ ",ISNULL((select sum(u.DIFFQTY) from "+aPlant+"_STKTAKE u where u.ITEM=b.ITEM and u.BATCH=b.BATCH and u.LOC=b.LOC and u.STATUS='N'),0) as DIFFQTY "
								+ ",ISNULL((select u.INVFLAG from "+aPlant+"_STKTAKE u where u.ITEM=b.ITEM and u.BATCH=b.BATCH and u.LOC=b.LOC and u.STATUS='N'),'') as INVFLAG "
								+ ",ISNULL((select u.CRBY from "+aPlant+"_STKTAKE u where u.ITEM=b.ITEM and u.BATCH=b.BATCH and u.LOC=b.LOC and u.STATUS='N'),'') as CRBY "
								+ ",ISNULL((select u.REMARKS from "+aPlant+"_STKTAKE u where u.ITEM=b.ITEM and u.BATCH=b.BATCH and u.LOC=b.LOC and u.STATUS='N'),'') as REMARKS "
								+ " from tempdb b where ITEM!='' ";
				}
				else
				{
					aQuery = "with tempdb as ( "
					+"select a.ITEM,isnull(b.REMARK1,'') as REMARK1,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_dept_id,'') as PRD_DEPT_ID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,isnull(b.MODEL,'') as MODEL,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ aPlant +"_UOM u where u.UOM=(case when '"+UOM+"'='' then ISNULL(b.INVENTORYUOM,'') else '"+UOM+"' end)),'') as STKUOM,b.itemtype as ITEMTYPE,UPPER(a.LOC) LOC,a.UserFld4 as BATCH, sum(QTY) STKQTY,sum(b.UNITPRICE)as UNITPRICE, ISNULL(b.INVENTORYUOM,'') as  INVENTORYUOM, ISNULL((select u.QPUOM from "+ aPlant +"_UOM u where u.UOM=b.INVENTORYUOM),1) as UOMQTY"
                            + " from["
                            + aPlant
                            + "_"
                            + "invmst"
                            + "]"
                            + " A,"
                            + "["
                            + aPlant
                            + "_"
                            + "itemmst"
                            + "]"
                            + " as b where a.item=b.item  and a.item in (SELECT ITEM FROM "
                            + aPlant
                            + "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"+ aItem + "%')"
                            + " and b.NONSTKFLAG<>'Y' group by a.item,b.REMARK1,a.loc,a.userfld4,b.itemdesc,b.prd_cls_id,b.prd_dept_id,b.MODEL,b.itemtype,b.prd_brand_id,b.stkuom,b.INVENTORYUOM ) "
                            + "select  ITEM,LOC,REMARK1,ITEMDESC,PRDCLSID,PRD_DEPT_ID,PRD_BRAND_ID,MODEL,ITEMTYPE,BATCH,UNITPRICE,INVENTORYUOM,isnull(STKQTY,0) as QTY,STKUOM,"
							+ "case when 0<ISNULL(UOMQTY,0) then (convert(float,(STKQTY/UOMQTY))) else STKQTY end INVUOMQTY "
							+ ",ISNULL((select u.CR_DATE from "+aPlant+"_STKTAKE u where u.ITEM=b.ITEM and u.BATCH=b.BATCH and u.LOC=b.LOC and u.STATUS='N'),'') as CRDATE "
							+ ",ISNULL((select sum(u.QTY) from "+aPlant+"_STKTAKE u where u.ITEM=b.ITEM and u.BATCH=b.BATCH and u.LOC=b.LOC and u.STATUS='N'),0) as STKTAKEQTY "
							+ ",ISNULL((select sum(u.DIFFQTY) from "+aPlant+"_STKTAKE u where u.ITEM=b.ITEM and u.BATCH=b.BATCH and u.LOC=b.LOC and u.STATUS='N'),0) as DIFFQTY "
							+ ",ISNULL((select u.INVFLAG from "+aPlant+"_STKTAKE u where u.ITEM=b.ITEM and u.BATCH=b.BATCH and u.LOC=b.LOC and u.STATUS='N'),'') as INVFLAG "
							+ ",ISNULL((select u.CRBY from "+aPlant+"_STKTAKE u where u.ITEM=b.ITEM and u.BATCH=b.BATCH and u.LOC=b.LOC and u.STATUS='N'),'') as CRBY "
							+ ",ISNULL((select u.REMARKS from "+aPlant+"_STKTAKE u where u.ITEM=b.ITEM and u.BATCH=b.BATCH and u.LOC=b.LOC and u.STATUS='N'),'') as REMARKS "
							+ " from tempdb b where ITEM!='' ";
				}
                arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

        } catch (Exception e) {
                this.mLogger.exception(this.printLog, "", e);
        }
        return arrList;
}
	
	public ArrayList getManualStktakeByStatus(String aPlant,String status)
	{
		ArrayList arrList = new ArrayList();
		try {
			Hashtable ht = new Hashtable();
			String aQuery="select ID,ISNULL(ITEM,'') as ITEM,ISNULL(LOC,'') as LOC,ISNULL(BATCH,'') as BATCH,ISNULL(STATUS,'') as STATUS,ISNULL(QTY,'0') as QTY,ISNULL(UOM,'') as UOM,ISNULL(DIFFQTY,'0') as DIFFQTY,ISNULL(INVFLAG,'0') as INVFLAG,ISNULL(UOM,'') as UOM,ISNULL(REMARKS,'') REMARKS FROM "+aPlant+"_STKTAKE WHERE PLANT='"+aPlant+"' AND STATUS='"+status+"'";
			String sCondition="";
			arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}
		return arrList;
		
	}
	
	public ArrayList getManualStktakeByStatusNloc(String aPlant,String status,String Loc)
	{
		ArrayList arrList = new ArrayList();
		try {
			Hashtable ht = new Hashtable();
			String aQuery="select ID,ISNULL(ITEM,'') as ITEM,ISNULL(LOC,'') as LOC,ISNULL(BATCH,'') as BATCH,ISNULL(STATUS,'') as STATUS,ISNULL(QTY,'0') as QTY,ISNULL(UOM,'') as UOM,ISNULL(DIFFQTY,'0') as DIFFQTY,ISNULL(INVFLAG,'0') as INVFLAG,ISNULL(UOM,'') as UOM,ISNULL(REMARKS,'') REMARKS FROM "+aPlant+"_STKTAKE WHERE PLANT='"+aPlant+"' AND STATUS='"+status+"' AND LOC='"+Loc+"'";
			String sCondition="";
			arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
		
	}
	
	public ArrayList getManualStktakeByStatus(String aPlant,String status,int id)
	{
		ArrayList arrList = new ArrayList();
		try {
			Hashtable ht = new Hashtable();
			String aQuery="select ID,ISNULL(ITEM,'') as ITEM,ISNULL(LOC,'') as LOC,ISNULL(BATCH,'') as BATCH,ISNULL(STATUS,'') as STATUS,ISNULL(QTY,'0') as QTY,ISNULL(UOM,'') as UOM,ISNULL(DIFFQTY,'0') as DIFFQTY,ISNULL(INVFLAG,'0') as INVFLAG,ISNULL(UOM,'') as UOM,ISNULL(REMARKS,'') REMARKS FROM "+aPlant+"_STKTAKE WHERE PLANT='"+aPlant+"' AND STATUS='"+status+"' AND ID='"+id+"'";
			String sCondition="";
			arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return arrList;
		
	}
	
			public ArrayList getItemListFromAlterItem(String aPlant,String aItem)
			{
				ArrayList arrList = new ArrayList();
				try {
					Hashtable ht = new Hashtable();
					String aQuery="select ISNULL(ITEM,'') as ITEM FROM "+aPlant+"_ALTERNATE_BRAND_ITEM_MAPPING WHERE PLANT='"+aPlant+"' AND ALTERNATE_ITEM_NAME='"+aItem+"'";
					String sCondition="";
					arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);
					} catch (Exception e) {
						this.mLogger.exception(this.printLog, "", e);
					}
				return arrList;
				
			}
			
			
			public ArrayList getItemListUom(String pLANT, Hashtable ht,String productDesc,String cname,String UOM) {
				ArrayList arrList = new ArrayList();
				 String INVENTORYUOM = "";
				   String tableUOM="";
				   if(0<UOM.length())
				   {
					   INVENTORYUOM = "'"+UOM+"'"+" as INVENTORYUOM";
					   tableUOM ="'"+ UOM+"'";
				   }
				   else
				   {
					   INVENTORYUOM = "ISNULL(A.INVENTORYUOM,'') as  INVENTORYUOM";
					   tableUOM = "A.INVENTORYUOM";
				   }
				
				 String sCondition=" GROUP BY A.ITEM,ITEMDESC,A.PRD_BRAND_ID,VINNO,MODEL,UnitPrice,i.LOC,A.INVENTORYUOM)SELECT *,case when 0<ISNULL(UOMQTY,0) then (convert(int,(QTY*UOMQTY))) else QTY end STKQTY,convert(int,QTY/UOMQTY) as INVUOMQTY" + 
				 		"   FROM temptable A"; 
				 String aQuery ="";
				 InvMstDAO _InvMstDAO = new InvMstDAO();
				 _InvMstDAO.setmLogger(mLogger);
				 if(productDesc.length()>0){
					 productDesc = "  AND A.ITEMDESC like'%"+productDesc+"%'";	                
			            }
				 try {
					 aQuery = "with temptable as(SELECT ISNULL(A.ITEM,'') ITEM,ISNULL(ITEMDESC,'') ITEMDESC,ISNULL(A.PRD_BRAND_ID,'') BRAND ,ISNULL(VINNO,'') VINNO,ISNULL(MODEL,'') MODEL,ISNULL(UnitPrice,0) UNITPRICE "
							 + ",ISNULL((Select M.OBDISCOUNT from ["+ pLANT +"_CUSTMST] C join ["+ pLANT +"_MULTI_PRICE_MAPPING] M on C.CUSTOMER_TYPE_ID=M.CUSTOMER_TYPE_ID WHERE M.ITEM=A.ITEM AND C.CNAME='"+cname+"'),'') as DISCOUNTBY "
							 + ",i.LOC,SUM(QTY) as QTY,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ pLANT +"_UOM u where u.UOM='"+UOM+"'),'')" + 
							 " as STKUOM,ISNULL((select u.QPUOM from [" +pLANT+ "_UOM] u where u.UOM="+tableUOM+"),1) as UOMQTY,"+INVENTORYUOM
							 + ",ISNULL((SELECT ISNULL('/track/ReadFileServlet/?fileLocation='+CATLOGPATH,'../jsp/dist/img/NO_IMG.png')  FROM  [" + pLANT + "_" + "CATALOGMST" + "] WHERE PRODUCTID=A.ITEM),'../jsp/dist/img/NO_IMG.png') CATALOG "
							 + " FROM [" + pLANT + "_" + "ITEMMST" + "] A join [" + pLANT + "_" + "INVMST" + "] i on i.ITEM=A.ITEM WHERE A.ITEM NOT IN (select ALTERNATE_ITEM_NAME from "+pLANT+"_ALTERNATE_BRAND_ITEM_MAPPING) AND LOC NOT LIKE 'SHIPPINGAREA%' and LOC NOT LIKE 'TEMP_TO%' AND A.ITEM!='' "+productDesc;
				arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);
				 	} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				 	}
				 	return arrList;
			}





public ArrayList getInvListSummaryWithAverageCostWithUOM(Hashtable ht, String afrmDate,String atoDate,String plant,String aItem,String productDesc,String currencyid,String baseCurrency,String loctypeid,String loctypeid2,String loctypeid3,String loc,String UOM) throws Exception {
    ArrayList arrList = new ArrayList();
  //CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
    String sCondition ="";//,sDateCod="";
    if (productDesc.length() > 0 ) {
        if (productDesc.indexOf("%") != -1) {
                productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
        }
        sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
    }
   String sRecvCondition="";
   String INVENTORYUOM = "";
   String tableUOM="";
   if(0<UOM.length())
   {
	   INVENTORYUOM = "'"+UOM+"'"+" as INVENTORYUOM";
	   tableUOM ="'"+ UOM+"'";
   }
   else
   {
	   INVENTORYUOM = "ISNULL(b.INVENTORYUOM,'') as  INVENTORYUOM";
	   tableUOM = "b.INVENTORYUOM";
   }
   
    if (afrmDate.length() > 0) {
            sRecvCondition = sRecvCondition + " and substring(R.CRAT,1,8)  >= '" + afrmDate
                            + "'  ";
            if (atoDate.length() > 0) {
                    sRecvCondition = sRecvCondition + " and substring(R.CRAT,1,8)   <= '" + atoDate
                                    + "'  ";
            }
    } else {
            if (atoDate.length() > 0) {
                    sRecvCondition = sRecvCondition + " and substring(R.CRAT,1,8) <= '" + atoDate
                                    + "'  ";
            }
    }
    
    if (loc != null && loc!="") {
    	sCondition = sCondition+" AND LOC like '%"+loc+"%'";
    }
    
    if (loctypeid != null && loctypeid!="") {
		sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType(plant,loctypeid)+" )";
	}
    if (loctypeid2 != null && loctypeid2!="") {
		sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,loctypeid2)+" )";
	}
    if (loctypeid3 != null && loctypeid3!="") {
		sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,loctypeid3)+" )";
	}
    String ConvertUnitCostToOrderCurrency = "";
   
    	ConvertUnitCostToOrderCurrency = " (CAST(ISNULL("
    			+ "CASE WHEN b.PURCHASEUOM = '"+UOM+"' then ISNULL(T.UNITCOST_AOD, T.UNITCOST) else ISNULL((select ISNULL(QPUOM,1) "
    			+ "from [" +plant+ "_UOM] where UOM=(case when '"+UOM+"'='' then ISNULL(b.INVENTORYUOM,'') else '"+UOM+"' end)),1) * (ISNULL(R.UNITCOST, 0) / (ISNULL((select ISNULL(QPUOM,1) from [" +plant+ "_UOM] "
    			+ "where UOM=b.PURCHASEUOM),1))) end ,0) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] " +
                "   WHERE  CURRENCYID='"+baseCurrency+"')*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] " +
                "   WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')) AS DECIMAL(20,5)) ) ";
    	
  StringBuffer sql = new StringBuffer(" ISNULL((CASE WHEN  (SELECT COUNT(CURRENCYID) FROM "+plant+"_RECVDET R WHERE ITEM=B.ITEM AND CURRENCYID IS NOT NULL AND tran_type IN('IB','GOODSRECEIPTWITHBATCH','INVENTORYUPLOAD','DE-KITTING','KITTING') )>0 THEN (Select ISNULL(CAST(ISNULL(SUM(CASE WHEN A.TRAN_TYPE='GOODSRECEIPTWITHBATCH' THEN 0");
sql.append(" ELSE A.UNITCOST END),0)/SUM(VC) AS DECIMAL(20,5)),0) AS AVERGAGE_COST from (select TRAN_TYPE,RECQTY VC,CASE WHEN TRAN_TYPE IN ('INVENTORYUPLOAD','DE-KITTING','KITTING') THEN (isnull(R.unitcost*(SELECT CURRENCYUSEQT  FROM "+plant+"_CURRENCYMST WHERE");
sql.append(" CURRENCYID=ISNULL(R.CURRENCYID,'"+currencyid+"')),0)*R.RECQTY) ELSE CAST( (CAST(ISNULL(ISNULL((select ISNULL(QPUOM,1) from "+plant+"_UOM where UOM=''),1) * ( ISNULL(((isnull(R.unitcost*(SELECT CURRENCYUSEQT  FROM "+plant+"_CURRENCYMST WHERE CURRENCYID=ISNULL(P.CURRENCYID,'"+currencyid+"')),0)+ ISNULL((SELECT (SUM(E.QTY*LANDED_COST)/SUM(E.QTY)) FROM "+plant+"_RECVDET c left join "+plant+"_FINBILLHDR d on");
sql.append(" c.PONO = d.PONO and c.GRNO = d.GRNO left join "+plant+"_FINBILLDET e on d.ID = e.BILLHDRID where c.pono =R.pono and c.LNNO=R.LNNO and e.ITEM = B.ITEM OR c.TRAN_TYPE='GOODSRECEIPTWITHBATCH' AND c.item = B.ITEM),0) +  (ISNULL(R.unitcost*(SELECT CURRENCYUSEQT  FROM "+plant+"_CURRENCYMST WHERE CURRENCYID=ISNULL(P.CURRENCYID,'"+currencyid+"')),0) * (((ISNULL(P.LOCALEXPENSES,0)+ CASE WHEN (SELECT SUM(LANDED_COST) FROM "+plant+"_FINBILLHDR c join");
sql.append(" "+plant+"_FINBILLDET d  ON c.ID = d.BILLHDRID and c.PONO = R.PONO and d.LNNO = R.LNNO) is null THEN P.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from "+plant+"_podet s where s.pono=R.pono),0)),0))/100))/ (SELECT CURRENCYUSEQT  FROM "+plant+"_CURRENCYMST WHERE");
sql.append(" CURRENCYID=ISNULL(P.CURRENCYID,'"+currencyid+"'))),0) / (ISNULL((select ISNULL(QPUOM,1) from "+plant+"_UOM where UOM=S.UNITMO),1))) ,0) *(SELECT CURRENCYUSEQT FROM "+plant+"_CURRENCYMST    WHERE  CURRENCYID='"+currencyid+"')*(SELECT CURRENCYUSEQT  FROM "+plant+"_CURRENCYMST WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+currencyid+"')) AS DECIMAL(20,5)) )  * CAST((SELECT CURRENCYUSEQT  ");
sql.append(" FROM "+plant+"_CURRENCYMST WHERE  CURRENCYID='"+currencyid+"')AS DECIMAL(20,5))  / CAST((SELECT CURRENCYUSEQT FROM "+plant+"_CURRENCYMST WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+currencyid+"')) AS DECIMAL(20,5)) * RECQTY AS DECIMAL(20,5)) END AS UNITCOST from "+plant+"_RECVDET R LEFT JOIN "+plant+"_POHDR P ON R.PONO = P.PONO");
sql.append(" LEFT JOIN "+plant+"_podet s ON s.pono=R.pono AND R.item=s.item where R.item =B.ITEM AND ISNULL(R.UNITCOST,0) != 0 AND tran_type IN('IB','GOODSRECEIPTWITHBATCH','INVENTORYUPLOAD','DE-KITTING','KITTING')    ) A) ELSE (SELECT CASE WHEN (SELECT COUNT(*) FROM "+plant+"_RECVDET WHERE ITEM=B.ITEM AND tran_type IN('INVENTORYUPLOAD','DE-KITTING','KITTING') )>0 THEN (SELECT SUM(UNITCOST) FROM "+plant+"_RECVDET C where item = B.ITEM");
sql.append(" AND ISNULL(C.UNITCOST,0) != 0 AND tran_type IN('INVENTORYUPLOAD','DE-KITTING','KITTING')) ELSE CAST(((SELECT M.COST / ISNULL((select ISNULL(QPUOM,1) from "+plant+"_UOM where UOM=M.SALESUOM),1) FROM "+plant+"_ITEMMST M WHERE M.ITEM = B.ITEM)*(SELECT CURRENCYUSEQT  FROM "+plant+"_CURRENCYMST WHERE  CURRENCYID='"+currencyid+"')) AS DECIMAL(20,5))   END) END),0)  * (ISNULL((select ISNULL(QPUOM,1) from "+plant+"_UOM where UOM=B.INVENTORYUOM),1)) AS AVERAGE_COST ");

//StringBuffer sql = new StringBuffer(" ((SELECT CASE WHEN  (SELECT COUNT(CURRENCYID) FROM ["+plant+"_RECVDET] R WHERE ITEM=B.ITEM AND CURRENCYID IS NOT NULL AND TRAN_TYPE IN('IB','GOODSRECEIPTWITHBATCH','INVENTORYUPLOAD','DE-KITTING','KITTING') "+sRecvCondition+")>0  THEN ");
//sql.append(" (Select ISNULL(CAST(ISNULL(SUM(A.UNITCOST),0)/SUM(A.RECQTY) AS DECIMAL(20,5)),0) AS AVERGAGE_COST from ");
//sql.append(" (select RECQTY,CAST("+ConvertUnitCostToOrderCurrency+" * CAST((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')AS DECIMAL(20,5)) ");
//sql.append(" / CAST((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')) AS DECIMAL(20,5)) ");
//sql.append("   * RECQTY AS DECIMAL(20,5)) AS UNITCOST ");
//sql.append("   from ["+plant+"_RECVDET] R LEFT JOIN ["+plant+"_POHDR] P ON R.PONO = P.PONO LEFT JOIN ["+plant+"_PODET] T ON T.PONO = P.PONO AND R.ITEM=T.ITEM where R.item =b.ITEM AND ISNULL(R.UNITCOST,0) != 0 AND TRAN_TYPE IN('IB','GOODSRECEIPTWITHBATCH','INVENTORYUPLOAD','DE-KITTING','KITTING') " + sRecvCondition +"  ) A )  ");
////Commented by samatha on 29102013 as conversion is wrong
////  sql.append("   ELSE   CAST(((B.COST)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+baseCurrency+"')) /(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')AS DECIMAL(20,4))   END ) AS AVERAGE_COST   ");
////sql.append("   ELSE   CAST(((B.COST)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')) AS DECIMAL(20,5))   END ) AS AVERAGE_COST   ");
//sql.append("   ELSE   (SELECT CASE WHEN (SELECT COUNT(*) FROM ["+plant+"_RECVDET] WHERE ITEM=b.ITEM AND tran_type IN('INVENTORYUPLOAD','DE-KITTING','KITTING') )>0 THEN (SELECT SUM(UNITCOST) FROM ["+plant+"_RECVDET] C where item = b.item and c.BATCH = b.BATCH and c.LOC=b.LOC AND tran_type IN('INVENTORYUPLOAD','DE-KITTING','KITTING')) ELSE CAST(((ISNULL((select ISNULL(QPUOM,1) from [" +plant+ "_UOM] where UOM=(case when '"+UOM+"'='' then ISNULL(b.INVENTORYUOM,'') else '"+UOM+"' end)),1) * (ISNULL(B.COST, 0) / (ISNULL((select ISNULL(QPUOM,1) from [" +plant+ "_UOM] where UOM=b.PURCHASEUOM),1))))*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')) AS DECIMAL(20,5))   END)   END ) ) AS AVERAGE_COST   ");

String ConvertUnitCostToOrderCurrency2 = "";

ConvertUnitCostToOrderCurrency2 = " (CAST(ISNULL("
		+ "CASE WHEN b.PURCHASEUOM = '1' then ISNULL(T.UNITCOST, 0) else "
		+ " (ISNULL(T.UNITCOST, 0) / (ISNULL((select ISNULL(QPUOM,1) from [" +plant+ "_UOM] "
		+ "where UOM=b.PURCHASEUOM),1))) end ,0) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] " +
        "   WHERE  CURRENCYID='"+baseCurrency+"')*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] " +
        "   WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')) AS DECIMAL(20,5)) ) ";

//StringBuffer sql2 = new StringBuffer(" ((SELECT CASE WHEN  (SELECT COUNT(CURRENCYID) FROM ["+plant+"_RECVDET] R WHERE ITEM=B.ITEM AND CURRENCYID IS NOT NULL AND TRAN_TYPE IN('IB','GOODSRECEIPTWITHBATCH') "+sRecvCondition+")>0  THEN ");
//sql2.append(" (Select ISNULL(CAST(ISNULL(SUM(A.UNITCOST),0)/SUM(RECQTY) AS DECIMAL(20,5)),0) AS AVERGAGE_COST from ");
//sql2.append(" (select RECQTY,CAST("+ConvertUnitCostToOrderCurrency2+" * CAST((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')AS DECIMAL(20,5)) ");
//sql2.append(" / CAST((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')) AS DECIMAL(20,5)) * RECQTY");
//sql2.append(" AS DECIMAL(20,5)) AS UNITCOST ");
//sql2.append(" from ["+plant+"_RECVDET] R LEFT JOIN ["+plant+"_POHDR] P ON R.PONO = P.PONO LEFT JOIN ["+plant+"_PODET] T ON T.PONO = P.PONO AND R.ITEM=T.ITEM where R.item =b.ITEM AND TRAN_TYPE IN('IB','GOODSRECEIPTWITHBATCH') " + sRecvCondition +"  ) A )  ");
StringBuffer sql2 = new StringBuffer(" ((SELECT CASE WHEN  (SELECT COUNT(CURRENCYID) FROM ["+plant+"_RECVDET] R WHERE ITEM=B.ITEM AND CURRENCYID IS NOT NULL AND TRAN_TYPE IN('IB','GOODSRECEIPTWITHBATCH','INVENTORYUPLOAD','DE-KITTING','KITTING') "+sRecvCondition+")>0  THEN ");
sql2.append(" (Select ISNULL(CAST(ISNULL(SUM(A.UNITCOST),0)/SUM(A.RECQTY) AS DECIMAL(20,5)),0) AS AVERGAGE_COST from ");
sql2.append(" (select RECQTY,CAST("+ConvertUnitCostToOrderCurrency+" * CAST((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')AS DECIMAL(20,5)) ");
sql2.append(" / CAST((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')) AS DECIMAL(20,5)) ");
sql2.append("   * RECQTY AS DECIMAL(20,5)) AS UNITCOST ");
sql2.append("   from ["+plant+"_RECVDET] R LEFT JOIN ["+plant+"_POHDR] P ON R.PONO = P.PONO LEFT JOIN ["+plant+"_PODET] T ON T.PONO = P.PONO AND R.ITEM=T.ITEM where R.item =b.ITEM AND ISNULL(R.UNITCOST,0) != 0 AND TRAN_TYPE IN('IB','GOODSRECEIPTWITHBATCH','INVENTORYUPLOAD','DE-KITTING','KITTING') " + sRecvCondition +"  ) A )  ");
/////////sql2.append(" ELSE CAST(((B.COST)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')) AS DECIMAL(20,5))   END ) AS COST_PC   ");
sql2.append(" ELSE (SELECT CASE WHEN (SELECT COUNT(*) FROM ["+plant+"_RECVDET] WHERE ITEM=b.ITEM AND tran_type IN('INVENTORYUPLOAD','DE-KITTING','KITTING') )>0 THEN (SELECT (SUM(UNITCOST)) FROM ["+plant+"_RECVDET] C where item = b.item and c.BATCH = b.BATCH and c.LOC=b.LOC AND tran_type IN('INVENTORYUPLOAD','DE-KITTING','KITTING')) ELSE CAST(((B.COST)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')) AS DECIMAL(20,5))   END)   END )/ISNULL((select u.QPUOM from ["+plant+"_UOM] u where u.UOM=b.PURCHASEUOM),1)) AS COST_PC   ");

    
    try {
            InvMstDAO _InvMstDAO = new InvMstDAO();
            _InvMstDAO.setmLogger(mLogger);
           sCondition = sCondition  + " group by a.item,a.loc,a.userfld4,b.itemdesc,b.prd_dept_id,b.prd_cls_id,b.prd_brand_id,b.itemtype,b.stkuom,b.INVENTORYUOM,b.PURCHASEUOM ) B order by item ,LOC ";

            // Added stkuom to the query
           //---Modified by Bruhan on Feb 27 2014, Description: To display location data's in uppercase
             String aQuery = "SELECT *,case when 0<ISNULL(UOMQTY,0) then (convert(float,(QTY/UOMQTY))) else QTY end INVUOMQTY,convert(float,QTY) as STKQTY,0 as BILLED_QTY,0 as UNBILLED_QTY,"
             		+ ""+sql.toString()+","+sql2.toString()+" FROM (select b.PURCHASEUOM,a.ITEM,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_dept_id,'') as PRD_DEPT_ID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ plant +"_UOM u where u.UOM=(case when '"+UOM+"'='' then ISNULL(b.INVENTORYUOM,'') else '"+UOM+"' end)),'') as STKUOM,b.itemtype as ITEMTYPE,UPPER(a.LOC) LOC,a.UserFld4 as BATCH, (sum(QTY)) QTY,ISNULL(CAST(((sum(b.UNITPRICE))*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')) AS DECIMAL(20,4)),0) as LIST_PRICE,sum(b.COST) as COST," +INVENTORYUOM+","+ 
             		" ISNULL((select u.QPUOM from [" +plant+ "_UOM] u where u.UOM="+tableUOM+"),1) as UOMQTY    from "
                            + "["
                            + plant
                            + "_"
                            + "invmst"
                            + "]"
                            + " A,"
                            + "["
                            + plant
                            + "_"
                            + "itemmst"
                            + "]"
                            + " as b where a.item=b.item  and a.item in (SELECT ITEM FROM "
                            + plant
                            + "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"+ aItem + "%')"
                            + "and QTY>0 ";
            arrList = _InvMstDAO.selectForReport(aQuery, ht,sCondition);

    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
    }
    return arrList;
}




/*
 * public ArrayList getInvListSummaryWithAverageCostWithUOM(Hashtable ht, String
 * afrmDate,String atoDate,String plant,String aItem,String productDesc,String
 * currencyid,String baseCurrency,String loctypeid,String loctypeid2,String
 * loctypeid3,String loc,String UOM) throws Exception { ArrayList arrList = new
 * ArrayList(); //CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION
 * IN PRODDESC String sCondition ="";//,sDateCod=""; if (productDesc.length() >
 * 0 ) { if (productDesc.indexOf("%") != -1) { productDesc =
 * productDesc.substring(0, productDesc.indexOf("%") - 1); } sCondition =
 * " and replace(b.ITEMDESC,' ','') like '%"+
 * StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' "; } String
 * sRecvCondition=""; String INVENTORYUOM = ""; String tableUOM="";
 * if(0<UOM.length()) { INVENTORYUOM = "'"+UOM+"'"+" as INVENTORYUOM"; tableUOM
 * ="'"+ UOM+"'"; } else { INVENTORYUOM =
 * "ISNULL(b.INVENTORYUOM,'') as  INVENTORYUOM"; tableUOM = "b.INVENTORYUOM"; }
 * 
 * if (afrmDate.length() > 0) { sRecvCondition = sRecvCondition +
 * " and substring(R.CRAT,1,8)  >= '" + afrmDate + "'  "; if (atoDate.length() >
 * 0) { sRecvCondition = sRecvCondition + " and substring(R.CRAT,1,8)   <= '" +
 * atoDate + "'  "; } } else { if (atoDate.length() > 0) { sRecvCondition =
 * sRecvCondition + " and substring(R.CRAT,1,8) <= '" + atoDate + "'  "; } }
 * 
 * if (loc != null && loc!="") { sCondition =
 * sCondition+" AND LOC like '%"+loc+"%'"; }
 * 
 * if (loctypeid != null && loctypeid!="") { sCondition = sCondition +
 * " AND LOC IN ( "+new LocMstDAO().getLocForLocType(plant,loctypeid)+" )"; } if
 * (loctypeid2 != null && loctypeid2!="") { sCondition = sCondition +
 * " AND LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,loctypeid2)+" )"; }
 * if (loctypeid3 != null && loctypeid3!="") { sCondition = sCondition +
 * " AND LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,loctypeid3)+" )"; }
 * String ConvertUnitCostToOrderCurrency = "";
 * 
 * ConvertUnitCostToOrderCurrency = " (CAST(ISNULL(" +
 * "CASE WHEN b.PURCHASEUOM = '"
 * +UOM+"' then ISNULL(T.UNITCOST_AOD, T.UNITCOST) else ISNULL((select ISNULL(QPUOM,1) "
 * + "from [" +plant+ "_UOM] where UOM=(case when '"
 * +UOM+"'='' then ISNULL(b.INVENTORYUOM,'') else '"
 * +UOM+"' end)),1) * (ISNULL(R.UNITCOST, 0) / (ISNULL((select ISNULL(QPUOM,1) from ["
 * +plant+ "_UOM] " +
 * "where UOM=b.PURCHASEUOM),1))) end ,0) *(SELECT CURRENCYUSEQT  FROM ["
 * +plant+"_CURRENCYMST] " +
 * "   WHERE  CURRENCYID='"+baseCurrency+"')*(SELECT CURRENCYUSEQT  FROM ["
 * +plant+"_CURRENCYMST] " + "   WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"
 * +baseCurrency+"')) AS DECIMAL(20,5)) ) ";
 * 
 * 
 * StringBuffer sql = new
 * StringBuffer(" ((SELECT CASE WHEN  (SELECT COUNT(CURRENCYID) FROM ["
 * +plant+"_RECVDET] R WHERE ITEM=B.ITEM AND CURRENCYID IS NOT NULL AND TRAN_TYPE IN('IB','GOODSRECEIPTWITHBATCH','INVENTORYUPLOAD','DE-KITTING','KITTING') "
 * +sRecvCondition+")>0  THEN "); sql.
 * append(" (Select ISNULL(CAST(ISNULL(SUM(A.UNITCOST),0)/SUM(A.RECQTY) AS DECIMAL(20,5)),0) AS AVERGAGE_COST from "
 * ); sql.append(" (select RECQTY,CAST("
 * +ConvertUnitCostToOrderCurrency+" * CAST((SELECT CURRENCYUSEQT  FROM ["
 * +plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')AS DECIMAL(20,5)) "
 * ); sql.append(" / CAST((SELECT CURRENCYUSEQT  FROM ["
 * +plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"
 * +baseCurrency+"')) AS DECIMAL(20,5)) ");
 * sql.append("   * RECQTY AS DECIMAL(20,5)) AS UNITCOST ");
 * sql.append("   from ["+plant+"_RECVDET] R LEFT JOIN ["
 * +plant+"_POHDR] P ON R.PONO = P.PONO LEFT JOIN ["
 * +plant+"_PODET] T ON T.PONO = P.PONO AND R.ITEM=T.ITEM where R.item =b.ITEM AND ISNULL(R.UNITCOST,0) != 0 AND TRAN_TYPE IN('IB','GOODSRECEIPTWITHBATCH','INVENTORYUPLOAD','DE-KITTING','KITTING') "
 * + sRecvCondition +"  ) A )  "); //Commented by samatha on 29102013 as
 * conversion is wrong //
 * sql.append("   ELSE   CAST(((B.COST)*(SELECT CURRENCYUSEQT  FROM ["
 * +plant+"_CURRENCYMST] WHERE  CURRENCYID='"
 * +baseCurrency+"')) /(SELECT CURRENCYUSEQT  FROM ["
 * +plant+"_CURRENCYMST] WHERE  CURRENCYID='"
 * +currencyid+"')AS DECIMAL(20,4))   END ) AS AVERAGE_COST   ");
 * //sql.append("   ELSE   CAST(((B.COST)*(SELECT CURRENCYUSEQT  FROM ["
 * +plant+"_CURRENCYMST] WHERE  CURRENCYID='"
 * +currencyid+"')) AS DECIMAL(20,5))   END ) AS AVERAGE_COST   ");
 * sql.append("   ELSE   (SELECT CASE WHEN (SELECT COUNT(*) FROM ["
 * +plant+"_RECVDET] WHERE ITEM=b.ITEM AND tran_type IN('INVENTORYUPLOAD','DE-KITTING','KITTING') )>0 THEN (SELECT SUM(UNITCOST) FROM ["
 * +plant+"_RECVDET] C where item = b.item and c.BATCH = b.BATCH and c.LOC=b.LOC AND tran_type IN('INVENTORYUPLOAD','DE-KITTING','KITTING')) ELSE CAST(((B.COST)*(SELECT CURRENCYUSEQT  FROM ["
 * +plant+"_CURRENCYMST] WHERE  CURRENCYID='"
 * +currencyid+"')) AS DECIMAL(20,5))   END)   END )/ISNULL((select u.QPUOM from ["
 * +plant+"_UOM] u where u.UOM=b.PURCHASEUOM),1) )*B.UOMQTY AS AVERAGE_COST   "
 * );
 * 
 * String ConvertUnitCostToOrderCurrency2 = "";
 * 
 * ConvertUnitCostToOrderCurrency2 = " (CAST(ISNULL(" +
 * "CASE WHEN b.PURCHASEUOM = '1' then ISNULL(T.UNITCOST, 0) else " +
 * " (ISNULL(T.UNITCOST, 0) / (ISNULL((select ISNULL(QPUOM,1) from [" +plant+
 * "_UOM] " +
 * "where UOM=b.PURCHASEUOM),1))) end ,0) *(SELECT CURRENCYUSEQT  FROM ["
 * +plant+"_CURRENCYMST] " +
 * "   WHERE  CURRENCYID='"+baseCurrency+"')*(SELECT CURRENCYUSEQT  FROM ["
 * +plant+"_CURRENCYMST] " + "   WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"
 * +baseCurrency+"')) AS DECIMAL(20,5)) ) ";
 * 
 * //StringBuffer sql2 = new
 * StringBuffer(" ((SELECT CASE WHEN  (SELECT COUNT(CURRENCYID) FROM ["
 * +plant+"_RECVDET] R WHERE ITEM=B.ITEM AND CURRENCYID IS NOT NULL AND TRAN_TYPE IN('IB','GOODSRECEIPTWITHBATCH') "
 * +sRecvCondition+")>0  THEN "); //sql2.
 * append(" (Select ISNULL(CAST(ISNULL(SUM(A.UNITCOST),0)/SUM(RECQTY) AS DECIMAL(20,5)),0) AS AVERGAGE_COST from "
 * ); //sql2.append(" (select RECQTY,CAST("
 * +ConvertUnitCostToOrderCurrency2+" * CAST((SELECT CURRENCYUSEQT  FROM ["
 * +plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')AS DECIMAL(20,5)) "
 * ); //sql2.append(" / CAST((SELECT CURRENCYUSEQT  FROM ["
 * +plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"
 * +baseCurrency+"')) AS DECIMAL(20,5)) * RECQTY");
 * //sql2.append(" AS DECIMAL(20,5)) AS UNITCOST ");
 * //sql2.append(" from ["+plant+"_RECVDET] R LEFT JOIN ["
 * +plant+"_POHDR] P ON R.PONO = P.PONO LEFT JOIN ["
 * +plant+"_PODET] T ON T.PONO = P.PONO AND R.ITEM=T.ITEM where R.item =b.ITEM AND TRAN_TYPE IN('IB','GOODSRECEIPTWITHBATCH') "
 * + sRecvCondition +"  ) A )  "); StringBuffer sql2 = new
 * StringBuffer(" ((SELECT CASE WHEN  (SELECT COUNT(CURRENCYID) FROM ["
 * +plant+"_RECVDET] R WHERE ITEM=B.ITEM AND CURRENCYID IS NOT NULL AND TRAN_TYPE IN('IB','GOODSRECEIPTWITHBATCH','INVENTORYUPLOAD','DE-KITTING','KITTING') "
 * +sRecvCondition+")>0  THEN "); sql2.
 * append(" (Select ISNULL(CAST(ISNULL(SUM(A.UNITCOST),0)/SUM(A.RECQTY) AS DECIMAL(20,5)),0) AS AVERGAGE_COST from "
 * ); sql2.append(" (select RECQTY,CAST("
 * +ConvertUnitCostToOrderCurrency+" * CAST((SELECT CURRENCYUSEQT  FROM ["
 * +plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')AS DECIMAL(20,5)) "
 * ); sql2.append(" / CAST((SELECT CURRENCYUSEQT  FROM ["
 * +plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"
 * +baseCurrency+"')) AS DECIMAL(20,5)) ");
 * sql2.append("   * RECQTY AS DECIMAL(20,5)) AS UNITCOST ");
 * sql2.append("   from ["+plant+"_RECVDET] R LEFT JOIN ["
 * +plant+"_POHDR] P ON R.PONO = P.PONO LEFT JOIN ["
 * +plant+"_PODET] T ON T.PONO = P.PONO AND R.ITEM=T.ITEM where R.item =b.ITEM AND ISNULL(R.UNITCOST,0) != 0 AND TRAN_TYPE IN('IB','GOODSRECEIPTWITHBATCH','INVENTORYUPLOAD','DE-KITTING','KITTING') "
 * + sRecvCondition +"  ) A )  ");
 * /////////sql2.append(" ELSE CAST(((B.COST)*(SELECT CURRENCYUSEQT  FROM ["
 * +plant+"_CURRENCYMST] WHERE  CURRENCYID='"
 * +currencyid+"')) AS DECIMAL(20,5))   END ) AS COST_PC   ");
 * sql2.append(" ELSE (SELECT CASE WHEN (SELECT COUNT(*) FROM ["
 * +plant+"_RECVDET] WHERE ITEM=b.ITEM AND tran_type IN('INVENTORYUPLOAD','DE-KITTING','KITTING') )>0 THEN (SELECT (SUM(UNITCOST)) FROM ["
 * +plant+"_RECVDET] C where item = b.item and c.BATCH = b.BATCH and c.LOC=b.LOC AND tran_type IN('INVENTORYUPLOAD','DE-KITTING','KITTING')) ELSE CAST(((B.COST)*(SELECT CURRENCYUSEQT  FROM ["
 * +plant+"_CURRENCYMST] WHERE  CURRENCYID='"
 * +currencyid+"')) AS DECIMAL(20,5))   END)   END )/ISNULL((select u.QPUOM from ["
 * +plant+"_UOM] u where u.UOM=b.PURCHASEUOM),1)) AS COST_PC   ");
 * 
 * 
 * try { InvMstDAO _InvMstDAO = new InvMstDAO(); _InvMstDAO.setmLogger(mLogger);
 * sCondition = sCondition +
 * " group by a.item,a.loc,a.userfld4,b.itemdesc,b.prd_dept_id,b.prd_cls_id,b.prd_brand_id,b.itemtype,b.stkuom,b.INVENTORYUOM,b.PURCHASEUOM ) B order by item ,LOC "
 * ;
 * 
 * // Added stkuom to the query //---Modified by Bruhan on Feb 27 2014,
 * Description: To display location data's in uppercase String aQuery =
 * "SELECT *,case when 0<ISNULL(UOMQTY,0) then (convert(float,(QTY/UOMQTY))) else QTY end INVUOMQTY,convert(float,QTY) as STKQTY,0 as BILLED_QTY,0 as UNBILLED_QTY,"
 * + ""+sql.toString()+","+sql2.toString()
 * +" FROM (select b.PURCHASEUOM,a.ITEM,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_dept_id,'') as PRD_DEPT_ID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,ISNULL((select ISNULL(u.CUOM,u.UOM) from "
 * + plant +"_UOM u where u.UOM=(case when '"
 * +UOM+"'='' then ISNULL(b.INVENTORYUOM,'') else '"
 * +UOM+"' end)),'') as STKUOM,b.itemtype as ITEMTYPE,UPPER(a.LOC) LOC,a.UserFld4 as BATCH, (sum(QTY)) QTY,ISNULL(CAST(((sum(b.UNITPRICE))*(SELECT CURRENCYUSEQT  FROM ["
 * +plant+"_CURRENCYMST] WHERE  CURRENCYID='"
 * +currencyid+"')) AS DECIMAL(20,4)),0) as LIST_PRICE,sum(b.COST) as COST,"
 * +INVENTORYUOM+","+ " ISNULL((select u.QPUOM from [" +plant+
 * "_UOM] u where u.UOM="+tableUOM+"),1) as UOMQTY    from " + "[" + plant + "_"
 * + "invmst" + "]" + " A," + "[" + plant + "_" + "itemmst" + "]" +
 * " as b where a.item=b.item  and a.item in (SELECT ITEM FROM " + plant +
 * "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"+ aItem + "%')" +
 * "and QTY>0 "; arrList = _InvMstDAO.selectForReport(aQuery, ht,sCondition);
 * 
 * } catch (Exception e) { this.mLogger.exception(this.printLog, "", e); }
 * return arrList; }
 */

/*Start code by Abhilash on 02-11-2019*/
public String getStockOnHandByProduct(String aItem, String plant) throws Exception {
	String stockOnHand = "";		
	try {
		_InvMstDAO.setmLogger(mLogger);
		stockOnHand = _InvMstDAO.getStockOnHandByProduct(aItem, plant);
	}catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	}
	return stockOnHand;
}
/*End code by Abhilash on 02-11-2019*/

public ArrayList getStockTakeDataDetails(Hashtable ht, String aPlant,String aItem) throws Exception {
    ArrayList arrList = new ArrayList();
    try {
    InvMstDAO _InvMstDAO = new InvMstDAO();
    _InvMstDAO.setmLogger(mLogger);
    String sCondition = "  ";
    if(aItem.length()>0)
    {
    	sCondition=sCondition + " " + "AND UPPER(A.ITEM) LIKE '"+ aItem + "%' ";
    }
    sCondition=sCondition +" order by A.ITEM,S.LOC,S.USERFLD4 ";
    String aQuery = "SELECT A.ITEM,ISNULL(S.LOC,'') as LOC,ISNULL(S.USERFLD4,'') as BATCH,ISNULL(A.INVENTORYUOM,'PCS') UOM from "+aPlant+"_ITEMMST A LEFT JOIN "+aPlant+"_INVMST S on S.ITEM=A.ITEM"
    		+" where (LOC is null or LOC not like '%SHIPPINGAREA%') and (LOC is null or LOC not like '%TO_LOC%') AND A.PLANT='"+aPlant+"' ";

    		arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

} catch (Exception e) {
        this.mLogger.exception(this.printLog, "", e);
}
return arrList;
}

public ArrayList getInvListSummaryWithAverageCostWithUOMLandedCost(Hashtable ht, String afrmDate,String atoDate,String plant,String aItem,String productDesc,String currencyid,String baseCurrency,String loctypeid,String loctypeid2,String loctypeid3,String loc,String UOM) throws Exception {
    ArrayList arrList = new ArrayList();
  //CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
    String sCondition ="";//,sDateCod="";
    if (productDesc.length() > 0 ) {
        if (productDesc.indexOf("%") != -1) {
                productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
        }
        sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
    }
   String sRecvCondition="";
   String INVENTORYUOM = "";
   String tableUOM="";
   if(0<UOM.length())
   {
	   INVENTORYUOM = "'"+UOM+"'"+" as INVENTORYUOM";
	   tableUOM ="'"+ UOM+"'";
   }
   else
   {
	   INVENTORYUOM = "ISNULL(b.INVENTORYUOM,'') as  INVENTORYUOM";
	   tableUOM = "b.INVENTORYUOM";
   }
   
    if (afrmDate.length() > 0) {
            sRecvCondition = sRecvCondition + " and substring(R.CRAT,1,8)  >= '" + afrmDate
                            + "'  ";
            if (atoDate.length() > 0) {
                    sRecvCondition = sRecvCondition + " and substring(R.CRAT,1,8)   <= '" + atoDate
                                    + "'  ";
            }
    } else {
            if (atoDate.length() > 0) {
                    sRecvCondition = sRecvCondition + " and substring(R.CRAT,1,8) <= '" + atoDate
                                    + "'  ";
            }
    }
    
    if (loc != null && loc!="") {
    	sCondition = sCondition+" AND LOC like '%"+loc+"%'";
    }
    
    if (loctypeid != null && loctypeid!="") {
		sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType(plant,loctypeid)+" )";
	}
    if (loctypeid2 != null && loctypeid2!="") {
		sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,loctypeid2)+" )";
	}
    if (loctypeid3 != null && loctypeid3!="") {
		sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,loctypeid3)+" )";
	}
    String ConvertUnitCostToOrderCurrency = "";
   
    	ConvertUnitCostToOrderCurrency = " (CAST(ISNULL("
    			+ "CASE WHEN b.PURCHASEUOM = '"+UOM+"' then "
				
    			+ " ISNULL(((isnull(R.unitcost*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0)+"
				+ " ISNULL((SELECT (SUM(E.QTY*LANDED_COST)/SUM(E.QTY)) FROM ["+plant+"_RECVDET] c left join ["+plant+"_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO left join ["+plant+"_FINBILLDET] e on d.ID = e.BILLHDRID where c.pono =R.pono and c.LNNO=R.LNNO and e.ITEM = b.ITEM OR c.TRAN_TYPE='GOODSRECEIPTWITHBATCH' AND c.item = b.item),0) + "
				+ " (ISNULL(R.unitcost*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0) * (((ISNULL(P.LOCALEXPENSES,0)+"
				+ " CASE WHEN (SELECT SUM(LANDED_COST) FROM ["+plant+"_FINBILLHDR] c join ["+plant+"_FINBILLDET] d "
				+ " ON c.ID = d.BILLHDRID and c.PONO = R.PONO and d.LNNO = R.LNNO) is null THEN P.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from ["+plant+"_podet] s where s.pono=R.pono),0)),0))/100))/"
				+ " (SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"'))),0) "
				
				+ " else ISNULL((select ISNULL(QPUOM,1) from ["+plant+"_UOM] where UOM='"+UOM+"'),1) * ("
	
				+ " ISNULL(((isnull(R.unitcost*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0)+"
				+ " ISNULL((SELECT (SUM(E.QTY*LANDED_COST)/SUM(E.QTY)) FROM ["+plant+"_RECVDET] c left join ["+plant+"_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO left join ["+plant+"_FINBILLDET] e on d.ID = e.BILLHDRID where c.pono =R.pono and c.LNNO=R.LNNO and e.ITEM = b.ITEM OR c.TRAN_TYPE='GOODSRECEIPTWITHBATCH' AND c.item = b.item),0) + "
				+ " (ISNULL(R.unitcost*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0) * (((ISNULL(P.LOCALEXPENSES,0)+"
				+ " CASE WHEN (SELECT SUM(LANDED_COST) FROM ["+plant+"_FINBILLHDR] c join ["+plant+"_FINBILLDET] d "
				+ " ON c.ID = d.BILLHDRID and c.PONO = R.PONO and d.LNNO = R.LNNO) is null THEN P.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from ["+plant+"_podet] s where s.pono=R.pono),0)),0))/100))/"
				+ " (SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"'))),0) "
				
    			+ "/ (ISNULL((select ISNULL(QPUOM,1) from ["+plant+"_UOM] "
    			+ "where UOM=b.PURCHASEUOM),1))) end ,0) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] " +
                "   WHERE  CURRENCYID='"+baseCurrency+"')*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] " +
                "   WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')) AS DECIMAL(20,5)) ) ";


  StringBuffer sql = new StringBuffer(" ISNULL((SELECT CASE WHEN  (SELECT COUNT(CURRENCYID) FROM ["+plant+"_RECVDET] R WHERE ITEM=B.ITEM AND CURRENCYID IS NOT NULL AND tran_type IN('IB','GOODSRECEIPTWITHBATCH','INVENTORYUPLOAD') "+sRecvCondition+")>0  THEN ");
sql.append(" (Select ISNULL(CAST(ISNULL(SUM(CASE WHEN A.TRAN_TYPE='GOODSRECEIPTWITHBATCH' THEN 0 ELSE A.UNITCOST END),0)/SUM(VC) AS DECIMAL(20,5)),0) AS AVERGAGE_COST from ");
sql.append(" (select TRAN_TYPE,RECQTY VC,CASE WHEN TRAN_TYPE = 'INVENTORYUPLOAD' THEN (isnull(R.unitcost*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE CURRENCYID=ISNULL(P.CURRENCYID,'SGD')),0)*R.RECQTY) ELSE CAST("+ConvertUnitCostToOrderCurrency+" * CAST((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')AS DECIMAL(20,5)) ");
sql.append(" / CAST((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')) AS DECIMAL(20,5)) ");
sql.append("   * RECQTY AS DECIMAL(20,5)) END AS UNITCOST ");
sql.append("   from "+plant+"_RECVDET R LEFT JOIN ["+plant+"_POHDR] P ON R.PONO = P.PONO  where item =b.ITEM AND ISNULL(R.UNITCOST,0) != 0  AND tran_type IN('IB','GOODSRECEIPTWITHBATCH','INVENTORYUPLOAD') " + sRecvCondition +"  ) A)  ");
sql.append("   ELSE   (SELECT CASE WHEN (SELECT COUNT(*) FROM ["+plant+"_RECVDET] WHERE ITEM=b.ITEM AND tran_type IN('INVENTORYUPLOAD') )>0 THEN (SELECT SUM(UNITCOST) FROM ["+plant+"_RECVDET] C where item = b.item and c.BATCH = b.BATCH and c.LOC=b.LOC AND tran_type IN('INVENTORYUPLOAD')) ELSE CAST(((B.COST)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+baseCurrency+"')) AS DECIMAL(20,5))   END)   END ),0) AS AVERAGE_COST   ");
String ConvertUnitCostToOrderCurrency2 = "";

ConvertUnitCostToOrderCurrency2 = " (CAST(ISNULL("
		+ "CASE WHEN b.PURCHASEUOM = '1' then "
		
		+ " ISNULL(((isnull(R.unitcost*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0)+"
		+ " ISNULL((SELECT (SUM(E.QTY*LANDED_COST)/SUM(E.QTY)) FROM ["+plant+"_RECVDET] c join ["+plant+"_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO join ["+plant+"_FINBILLDET] e on d.ID = e.BILLHDRID where c.pono =R.pono and c.LNNO=R.LNNO and e.ITEM = b.ITEM),0) + "
		+ " (ISNULL(R.unitcost*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0) * (((ISNULL(P.LOCALEXPENSES,0)+"
		+ " CASE WHEN (SELECT SUM(LANDED_COST) FROM ["+plant+"_FINBILLHDR] c join ["+plant+"_FINBILLDET] d "
		+ " ON c.ID = d.BILLHDRID and c.PONO = R.PONO and d.LNNO = R.LNNO) is null THEN P.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from ["+plant+"_podet] s where s.pono=R.pono),0)),0))/100))/"
		+ " (SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"'))),0) "
		
		+ "else "
		+ " ("

		+ " ISNULL(((isnull(R.unitcost*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0)+"
		+ " ISNULL((SELECT (SUM(E.QTY*LANDED_COST)/SUM(E.QTY)) FROM ["+plant+"_RECVDET] c join ["+plant+"_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO join ["+plant+"_FINBILLDET] e on d.ID = e.BILLHDRID where c.pono =R.pono and c.LNNO=R.LNNO and e.ITEM = b.ITEM),0) + "
		+ " (ISNULL(R.unitcost*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0) * (((ISNULL(P.LOCALEXPENSES,0)+"
		+ " CASE WHEN (SELECT SUM(LANDED_COST) FROM ["+plant+"_FINBILLHDR] c join ["+plant+"_FINBILLDET] d "
		+ " ON c.ID = d.BILLHDRID and c.PONO = R.PONO and d.LNNO = R.LNNO) is null THEN P.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from ["+plant+"_podet] s where s.pono=R.pono),0)),0))/100))/"
		+ " (SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"'))),0) "

		+ "/ (ISNULL((select ISNULL(QPUOM,1) from [" +plant+ "_UOM] "
		+ "where UOM=b.PURCHASEUOM),1))) end ,0) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] " +
        "   WHERE  CURRENCYID='"+baseCurrency+"')*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] " +
        "   WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')) AS DECIMAL(20,5)) ) ";

StringBuffer sql2 = new StringBuffer(" ISNULL((SELECT CASE WHEN  (SELECT COUNT(CURRENCYID) FROM ["+plant+"_RECVDET] R WHERE ITEM=B.ITEM AND CURRENCYID IS NOT NULL AND tran_type IN('IB','GOODSRECEIPTWITHBATCH')  AND UNITCOST >=0 AND UNITCOST IS NOT NULL "+sRecvCondition+")>0  THEN ");
sql2.append(" (Select ISNULL(CAST(ISNULL(SUM(CASE WHEN A.TRAN_TYPE='GOODSRECEIPTWITHBATCH' THEN 0 ELSE A.UNITCOST END),0)/SUM(VC) AS DECIMAL(20,5)),0) AS AVERGAGE_COST from ");
sql2.append(" (select TRAN_TYPE,RECQTY VC,CAST("+ConvertUnitCostToOrderCurrency2+" * CAST((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')AS DECIMAL(20,5)) ");
sql2.append(" / CAST((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')) AS DECIMAL(20,5)) * RECQTY");
sql2.append(" AS DECIMAL(20,5)) AS UNITCOST ");
sql2.append(" from "+plant+"_RECVDET R LEFT JOIN ["+plant+"_POHDR] P ON R.PONO = P.PONO where item =b.ITEM AND ISNULL(R.UNITCOST,0) != 0 AND tran_type IN('IB','GOODSRECEIPTWITHBATCH') " + sRecvCondition +"  ) A)  ");
sql2.append(" ELSE (SELECT CASE WHEN (SELECT COUNT(*) FROM ["+plant+"_RECVDET] WHERE ITEM=b.ITEM AND tran_type IN('INVENTORYUPLOAD') )>0 THEN (SELECT SUM(UNITCOST) FROM ["+plant+"_RECVDET] C where item = b.item and c.BATCH = b.BATCH and c.LOC=b.LOC AND tran_type IN('INVENTORYUPLOAD')) ELSE CAST(((B.COST)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+baseCurrency+"')) AS DECIMAL(20,5))   END)    END ),0) AS COST_PC   ");

    
    try {
            InvMstDAO _InvMstDAO = new InvMstDAO();
            _InvMstDAO.setmLogger(mLogger);
           sCondition = sCondition  + " group by a.item,a.loc,a.userfld4,b.itemdesc,b.prd_dept_id,b.prd_cls_id,b.prd_brand_id,b.itemtype,b.stkuom,b.INVENTORYUOM,b.PURCHASEUOM ) B order by item ,LOC ";

            // Added stkuom to the query
           //---Modified by Bruhan on Feb 27 2014, Description: To display location data's in uppercase
             String aQuery = "SELECT *,case when 0<ISNULL(UOMQTY,0) then (convert(int,(QTY*UOMQTY))) else QTY end STKQTY,convert(int,QTY/UOMQTY) as INVUOMQTY,"
            		 /*Bill Qty*/
            		 +"ISNULL((SELECT (SUM(BILLQTY)*ISNULL((select ISNULL(QPUOM,1) from [" +plant+ "_UOM] where UOM=b.PURCHASEUOM),1)) AS BILLQTY FROM ("
            		 +"SELECT (CASE WHEN TRAN_TYPE='IB' THEN ISNULL((SELECT (SUM(E.QTY)) "
            		 +"FROM [" +plant+ "_FINBILLHDR] d  join [" +plant+ "_FINBILLDET] e on d.ID = e.BILLHDRID and c.ITEM=e.ITEM "
            		 +"WHERE c.PONO = d.PONO and c.GRNO = d.GRNO and C.BILL_STATUS='BILLED'),0) ELSE "
            		 +"(CASE WHEN ISNULL(TRAN_TYPE,'') IN ('GOODSRECEIPTWITHBATCH','MR','INVENTORYUPLOAD','') THEN SUM(C.RECQTY) ELSE 0 END) "
            		 +" END) AS BILLQTY FROM [" +plant+ "_RECVDET] C where item = b.item and c.BATCH = b.BATCH and c.LOC=b.LOC "
            		 +"GROUP BY PONO,GRNO,TRAN_TYPE,item,BILL_STATUS) BB)- " + 
            		 "(SELECT ISNULL(SUM(ISNULL(B.QTY,0)),0) AS INVOICE_QTY FROM " + 
            		 "[" +plant+ "_FININVOICEHDR] A JOIN [" +plant+ "_FININVOICEDET] B ON A.ID =B.INVOICEHDRID WHERE A.BILL_STATUS <> 'Draft' " + 
            		 "AND ISNULL(B.IS_COGS_SET,'')='Y' " + 
            		 //"and b.ITEM='p0101'),0) AS BILLED_QTY," /*Author: Azees  Create date: Sep 10,2021  Description: p0101 HardCode*/
            		 "and b.ITEM='"+aItem+"'),0) AS BILLED_QTY,"
            		 /**/
            		 /*+"ISNULL((SELECT (SUM(E.QTY)*ISNULL((select ISNULL(QPUOM,1) from [TEST_UOM] where UOM=b.PURCHASEUOM),1)) FROM [" +plant+ "_RECVDET] c left join [" +plant+ "_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO left join [" +plant+ "_FINBILLDET] e on d.ID = e.BILLHDRID and c.ITEM=e.ITEM where c.item = b.item AND c.TRAN_TYPE IN ('GOODSRECEIPTWITHBATCH','IB')  and c.BATCH = b.BATCH and c.LOC=b.LOC and C.BILL_STATUS='BILLED'),0) as BILLED_QTY,"*/
            		 +"ISNULL((SELECT (SUM(C.RECQTY)*ISNULL((select ISNULL(QPUOM,1) from [" +plant+ "_UOM] where UOM=b.PURCHASEUOM),1)) FROM [" +plant+ "_RECVDET] c left join [" +plant+ "_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO left join [" +plant+ "_FINBILLDET] e on d.ID = e.BILLHDRID and c.ITEM=e.ITEM where c.item = b.item AND c.TRAN_TYPE IN ('IB')  and c.BATCH = b.BATCH and c.LOC=b.LOC and ISNULL(C.BILL_STATUS,'')<>'BILLED'),0) as UNBILLED_QTY,"
             		+ ""+sql.toString()+","+sql2.toString()+" FROM (select b.PURCHASEUOM,a.ITEM,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_dept_id,'') as PRD_DEPT_ID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ plant +"_UOM u where u.UOM=(case when '"+UOM+"'='' then ISNULL(b.INVENTORYUOM,'') else '"+UOM+"' end)),'') as STKUOM,b.itemtype as ITEMTYPE,UPPER(a.LOC) LOC,a.UserFld4 as BATCH, sum(QTY) QTY,ISNULL(CAST(((sum(b.UNITPRICE))*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')) AS DECIMAL(20,4)),0) as LIST_PRICE,sum(b.COST) as COST," +INVENTORYUOM+","+ 
             		" ISNULL((select u.QPUOM from [" +plant+ "_UOM] u where u.UOM="+tableUOM+"),1) as UOMQTY    from "
                            + "["
                            + plant
                            + "_"
                            + "invmst"
                            + "]"
                            + " A,"
                            + "["
                            + plant
                            + "_"
                            + "itemmst"
                            + "]"
                            + " as b where a.item=b.item  and a.item in (SELECT ITEM FROM "
                            + plant
                            + "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"+ aItem + "%')"
                            + "and QTY>0 ";
            arrList = _InvMstDAO.selectForReport(aQuery, ht,sCondition);
            
            //System.out.println("Inventory Summary With Avg Cost");
            //System.out.println("----------------------------------------------------");
           //System.out.println(aQuery);
            //System.out.println("----------------------------------------------------");

    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
    }
    return arrList;
	}
public ArrayList getInvListSummaryWithAverageCostWithUOMLandedCostRestructured(Hashtable ht, String afrmDate,String atoDate,String plant,String aItem,String productDesc,String currencyid,String baseCurrency,String loctypeid,String loctypeid2,String loc,String UOM) throws Exception {
    ArrayList arrList = new ArrayList();
  //CHNAGECODE-001 DESC CHANGE BY SAMATHA INCLUDE REPLACE FUNCTION IN PRODDESC
    String sCondition ="";//,sDateCod="";
    if (productDesc.length() > 0 ) {
        if (productDesc.indexOf("%") != -1) {
                productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
        }
        sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
    }
   String sRecvCondition="";
   String INVENTORYUOM = "";
   String tableUOM="";
   if(0<UOM.length())
   {
	   INVENTORYUOM = "'"+UOM+"'"+" as INVENTORYUOM";
	   tableUOM ="'"+ UOM+"'";
   }
   else
   {
	   INVENTORYUOM = "ISNULL(b.INVENTORYUOM,'') as  INVENTORYUOM";
	   tableUOM = "b.INVENTORYUOM";
   }
   
    if (afrmDate.length() > 0) {
            sRecvCondition = sRecvCondition + " and substring(CRAT,1,8)  >= '" + afrmDate
                            + "'  ";
            if (atoDate.length() > 0) {
                    sRecvCondition = sRecvCondition + " and substring(CRAT,1,8)   <= '" + atoDate
                                    + "'  ";
            }
    } else {
            if (atoDate.length() > 0) {
                    sRecvCondition = sRecvCondition + " and substring(CRAT,1,8) <= '" + atoDate
                                    + "'  ";
            }
    }
    
    if (loc != null && loc!="") {
    	sCondition = sCondition+" AND LOC like '%"+loc+"%'";
    }
    
    if (loctypeid != null && loctypeid!="") {
		sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType(plant,loctypeid)+" )";
	}
    if (loctypeid2 != null && loctypeid2!="") {
		sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,loctypeid2)+" )";
	}
    String ConvertUnitCostToOrderCurrency = "";
   
    	ConvertUnitCostToOrderCurrency = " (CAST(ISNULL("
    			+ "CASE WHEN b.PURCHASEUOM = '"+UOM+"' then "
				
    			+ " ISNULL(((isnull(R.unitcost*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0)+"
				+ " ISNULL((SELECT (SUM(E.QTY*LANDED_COST)/SUM(E.QTY)) FROM ["+plant+"_RECVDET] c left join ["+plant+"_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO left join ["+plant+"_FINBILLDET] e on d.ID = e.BILLHDRID where c.pono =R.pono and c.LNNO=R.LNNO and e.ITEM = b.ITEM OR c.TRAN_TYPE='GOODSRECEIPTWITHBATCH' AND c.item = b.item),0) + "
				+ " (ISNULL(R.unitcost*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0) * (((ISNULL(P.LOCALEXPENSES,0)+"
				+ " CASE WHEN (SELECT SUM(LANDED_COST) FROM ["+plant+"_FINBILLHDR] c join ["+plant+"_FINBILLDET] d "
				+ " ON c.ID = d.BILLHDRID and c.PONO = R.PONO and d.LNNO = R.LNNO) is null THEN P.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from ["+plant+"_podet] s where s.pono=R.pono),0)),0))/100))/"
				+ " (SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"'))),0) "
				
				+ " else ISNULL((select ISNULL(QPUOM,1) from ["+plant+"_UOM] where UOM='"+UOM+"'),1) * ("
	
				+ " ISNULL(((isnull(R.unitcost*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0)+"
				+ " ISNULL((SELECT (SUM(E.QTY*LANDED_COST)/SUM(E.QTY)) FROM ["+plant+"_RECVDET] c left join ["+plant+"_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO left join ["+plant+"_FINBILLDET] e on d.ID = e.BILLHDRID where c.pono =R.pono and c.LNNO=R.LNNO and e.ITEM = b.ITEM OR c.TRAN_TYPE='GOODSRECEIPTWITHBATCH' AND c.item = b.item),0) + "
				+ " (ISNULL(R.unitcost*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0) * (((ISNULL(P.LOCALEXPENSES,0)+"
				+ " CASE WHEN (SELECT SUM(LANDED_COST) FROM ["+plant+"_FINBILLHDR] c join ["+plant+"_FINBILLDET] d "
				+ " ON c.ID = d.BILLHDRID and c.PONO = R.PONO and d.LNNO = R.LNNO) is null THEN P.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from ["+plant+"_podet] s where s.pono=R.pono),0)),0))/100))/"
				+ " (SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"'))),0) "
				
    			+ "/ (ISNULL((select ISNULL(QPUOM,1) from ["+plant+"_UOM] "
    			+ "where UOM=b.PURCHASEUOM),1))) end ,0) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] " +
                "   WHERE  CURRENCYID='"+baseCurrency+"')*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] " +
                "   WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')) AS DECIMAL(20,5)) ) ";


  StringBuffer sql = new StringBuffer(" (SELECT CASE WHEN  (SELECT COUNT(CURRENCYID) FROM ["+plant+"_RECVDET] WHERE ITEM=B.ITEM AND CURRENCYID IS NOT NULL AND tran_type IN('IB','GOODSRECEIPTWITHBATCH') "+sRecvCondition+")>0  THEN ");
sql.append(" (Select ISNULL(CAST(ISNULL(SUM(CASE WHEN tr.TRAN_TYPE='GOODSRECEIPTWITHBATCH' THEN 0 ELSE A.UNITCOST END),0)/SUM(VC) AS DECIMAL(20,5)),0) AS AVERGAGE_COST from ");
sql.append(" (select RECQTY VC,CAST("+ConvertUnitCostToOrderCurrency+" * CAST((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')AS DECIMAL(20,5)) ");
sql.append(" / CAST((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')) AS DECIMAL(20,5)) ");
sql.append("   * RECQTY AS DECIMAL(20,5)) AS UNITCOST ");
sql.append("   from "+plant+"_RECVDET R JOIN ["+plant+"_POHDR] P ON R.PONO = P.PONO  where item =b.ITEM AND UNITCOST IS NOT NULL  AND UNITCOST >=0  AND UNITCOST <> ''  AND tran_type IN('IB','GOODSRECEIPTWITHBATCH') AND ORDQTY >0 " + sRecvCondition +"  ) A,["+plant+"_RECVDET] tr where tr.ITEM=b.ITEM)  ");
//Commented by samatha on 29102013 as conversion is wrong
//  sql.append("   ELSE   CAST(((B.COST)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+baseCurrency+"')) /(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')AS DECIMAL(20,4))   END ) AS AVERAGE_COST   ");
sql.append("   ELSE   CAST(((B.COST)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')) AS DECIMAL(20,5))   END ) AS AVERAGE_COST   ");

String ConvertUnitCostToOrderCurrency2 = "";

ConvertUnitCostToOrderCurrency2 = " (CAST(ISNULL("
		+ "CASE WHEN b.PURCHASEUOM = '1' then "
		
		+ " ISNULL(((isnull(R.unitcost*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0)+"
		+ " ISNULL((SELECT (SUM(E.QTY*LANDED_COST)/SUM(E.QTY)) FROM ["+plant+"_RECVDET] c join ["+plant+"_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO join ["+plant+"_FINBILLDET] e on d.ID = e.BILLHDRID where c.pono =R.pono and c.LNNO=R.LNNO and e.ITEM = b.ITEM),0) + "
		+ " (ISNULL(R.unitcost*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0) * (((ISNULL(P.LOCALEXPENSES,0)+"
		+ " CASE WHEN (SELECT SUM(LANDED_COST) FROM ["+plant+"_FINBILLHDR] c join ["+plant+"_FINBILLDET] d "
		+ " ON c.ID = d.BILLHDRID and c.PONO = R.PONO and d.LNNO = R.LNNO) is null THEN P.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from ["+plant+"_podet] s where s.pono=R.pono),0)),0))/100))/"
		+ " (SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"'))),0) "
		
		+ "else "
		+ " ("

		+ " ISNULL(((isnull(R.unitcost*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0)+"
		+ " ISNULL((SELECT (SUM(E.QTY*LANDED_COST)/SUM(E.QTY)) FROM ["+plant+"_RECVDET] c join ["+plant+"_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO join ["+plant+"_FINBILLDET] e on d.ID = e.BILLHDRID where c.pono =R.pono and c.LNNO=R.LNNO and e.ITEM = b.ITEM),0) + "
		+ " (ISNULL(R.unitcost*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0) * (((ISNULL(P.LOCALEXPENSES,0)+"
		+ " CASE WHEN (SELECT SUM(LANDED_COST) FROM ["+plant+"_FINBILLHDR] c join ["+plant+"_FINBILLDET] d "
		+ " ON c.ID = d.BILLHDRID and c.PONO = R.PONO and d.LNNO = R.LNNO) is null THEN P.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from ["+plant+"_podet] s where s.pono=R.pono),0)),0))/100))/"
		+ " (SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"'))),0) "

		+ "/ (ISNULL((select ISNULL(QPUOM,1) from [" +plant+ "_UOM] "
		+ "where UOM=b.PURCHASEUOM),1))) end ,0) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] " +
        "   WHERE  CURRENCYID='"+baseCurrency+"')*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] " +
        "   WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')) AS DECIMAL(20,5)) ) ";

StringBuffer sql2 = new StringBuffer(" (SELECT CASE WHEN  (SELECT COUNT(CURRENCYID) FROM ["+plant+"_RECVDET] WHERE ITEM=B.ITEM AND CURRENCYID IS NOT NULL AND tran_type IN('IB','GOODSRECEIPTWITHBATCH')  AND UNITCOST >=0 AND UNITCOST IS NOT NULL "+sRecvCondition+")>0  THEN ");
sql2.append(" (Select ISNULL(CAST(ISNULL(SUM(CASE WHEN tr.TRAN_TYPE='GOODSRECEIPTWITHBATCH' THEN 0 ELSE A.UNITCOST END),0)/SUM(VC) AS DECIMAL(20,5)),0) AS AVERGAGE_COST from ");
sql2.append(" (select RECQTY VC,CAST("+ConvertUnitCostToOrderCurrency2+" * CAST((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')AS DECIMAL(20,5)) ");
sql2.append(" / CAST((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')) AS DECIMAL(20,5)) * RECQTY");
sql2.append(" AS DECIMAL(20,5)) AS UNITCOST ");
sql2.append(" from "+plant+"_RECVDET R JOIN ["+plant+"_POHDR] P ON R.PONO = P.PONO where item =b.ITEM AND UNITCOST IS NOT NULL  AND UNITCOST >=0  AND UNITCOST <> ''  AND tran_type IN('IB','GOODSRECEIPTWITHBATCH') AND ORDQTY >0 " + sRecvCondition +"  ) A,["+plant+"_RECVDET] tr where tr.ITEM=b.ITEM )  ");
sql2.append(" ELSE CAST(((B.COST)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')) AS DECIMAL(20,5))   END ) AS COST_PC   ");

    
    try {
            InvMstDAO _InvMstDAO = new InvMstDAO();
            _InvMstDAO.setmLogger(mLogger);
           sCondition = sCondition  + " group by a.item,a.loc,a.userfld4,b.itemdesc,b.prd_cls_id,b.prd_brand_id,b.itemtype,b.stkuom,b.INVENTORYUOM,b.PURCHASEUOM ) B order by item ,LOC ";

            // Added stkuom to the query
           //---Modified by Bruhan on Feb 27 2014, Description: To display location data's in uppercase
             String aQuery = "SELECT *,case when 0<ISNULL(UOMQTY,0) then (convert(int,(QTY*UOMQTY))) else QTY end STKQTY,convert(int,QTY/UOMQTY) as INVUOMQTY,"
             		+ ""+sql.toString()+","+sql2.toString()+" FROM (select b.PURCHASEUOM,a.ITEM,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ plant +"_UOM u where u.UOM= (case when '"+UOM+"'='' then ISNULL(b.INVENTORYUOM,'') else '"+UOM+"' end)),'') as STKUOM,b.itemtype as ITEMTYPE,UPPER(a.LOC) LOC,a.UserFld4 as BATCH, sum(QTY) QTY,ISNULL(CAST(((sum(b.UNITPRICE))*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')) AS DECIMAL(20,4)),0) as LIST_PRICE,sum(b.COST) as COST," +INVENTORYUOM+","+ 
             		" ISNULL((select u.QPUOM from [" +plant+ "_UOM] u where u.UOM="+tableUOM+"),1) as UOMQTY    from "
                            + "["
                            + plant
                            + "_"
                            + "invmst"
                            + "]"
                            + " A,"
                            + "["
                            + plant
                            + "_"
                            + "itemmst"
                            + "]"
                            + " as b where a.item=b.item  and a.item in (SELECT ITEM FROM "
                            + plant
                            + "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"+ aItem + "%')"
                            + "and QTY>0 ";
            arrList = _InvMstDAO.selectForReport(aQuery, ht,sCondition);
            
            //System.out.println("Inventory Summary With Avg Cost");
            //System.out.println("----------------------------------------------------");
           System.out.println(aQuery);
            //System.out.println("----------------------------------------------------");

    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
    }
    return arrList;
	}

public ArrayList getParentChildProduct(String plant, Hashtable ht) throws Exception  {
	ArrayList arrList = new ArrayList();
 	String INVENTORYUOM = "";
   	String tableUOM="";
	String LOC = (String)ht.get("LOC");	
	String ITEM = (String)ht.get("ITEM");	
	String productDesc = (String)ht.get("ITEM_DESC");	
	String LOC_TYPE_ID = (String)ht.get("LOC_TYPE_ID");	
	String LOC_TYPE_ID2 = (String)ht.get("LOC_TYPE_ID2");	
	String LOC_TYPE_ID3 = (String)ht.get("LOC_TYPE_ID3");	
	String MODEL = (String)ht.get("MODEL");	
	String cname = (String)ht.get("CNAME");	
	String UOM = (String)ht.get("UOM");
	
	String condition="";
	
	/*if(ITEM.length() > 0) {
		condition += " AND b.ITEM='"+ITEM+"' ";
	}
	if(productDesc.length() > 0) {
		condition += " AND b.ITEMDESC='"+productDesc+"' ";
	}*/
	if(LOC.length() > 0) {
		condition += " AND a.LOC='"+LOC+"' ";
	}
	if(LOC_TYPE_ID.length() > 0) {
		condition += " AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,LOC_TYPE_ID)+" )";
	}
	if(LOC_TYPE_ID2.length() > 0) {
		condition += " AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,LOC_TYPE_ID2)+" )";
	}
	if(LOC_TYPE_ID3.length() > 0) {
		condition += " AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,LOC_TYPE_ID3)+" )";
	}
	if(MODEL.length() > 0) {
		condition += " AND b.MODEL='"+MODEL+"' ";
	}
	
   if(0<UOM.length())
   {
	   INVENTORYUOM = "'"+UOM+"'"+" as INVENTORYUOM";
	   tableUOM ="'"+ UOM+"'";
   }
   else
   {
	   INVENTORYUOM = "ISNULL(b.INVENTORYUOM,'') as  INVENTORYUOM";
	   tableUOM = "b.INVENTORYUOM";
   }
	
	 String aQuery ="";
	 InvMstDAO _InvMstDAO = new InvMstDAO();
	 _InvMstDAO.setmLogger(mLogger);
	 if(productDesc.length()>0){
		 productDesc = "  AND A.ITEMDESC like'%"+productDesc+"%'";	                
           }
	 try {
		 aQuery = "SELECT *,case when 0<ISNULL(UOMQTY,0) then (convert(int,(QTY*UOMQTY))) else QTY end STKQTY,"
		 + "CONVERT(DECIMAL(10,3),QTY/UOMQTY) as INVUOMQTY, (QTY - ORDQTY) AS AVAILABLE_QUANTITY FROM ("
		 + "SELECT b.ITEM,b.ITEMDESC ,ISNULL('/track/ReadFileServlet/?fileLocation='+b.CATLOGPATH,'../jsp/dist/img/NO_IMG.png') CATALOG,"
		 + "b.PRD_BRAND_ID,isnull(b.MODEL,'') MODEL,a.LOC,b.UnitPrice,b.NONSTKFLAG,"
		 + "ISNULL((Select M.OBDISCOUNT from ["+ plant +"_CUSTMST] C join ["+ plant +"_MULTI_PRICE_MAPPING] M on C.CUSTOMER_TYPE_ID=M.CUSTOMER_TYPE_ID WHERE M.ITEM=b.ITEM AND C.CNAME='"+cname+"'),'') as DISCOUNTBY, "
		 + "ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ plant +"_UOM u where u.UOM=(case when '"+UOM+"'='' then ISNULL(b.INVENTORYUOM,'') else '"+UOM+"' end)),'') as STKUOM,b.SALESUOM,"
		 + "ISNULL((select u.QPUOM from ["+plant+"_UOM] u where u.UOM="+tableUOM+"),1) as UOMQTY,"+INVENTORYUOM
		 + ",ISNULL((select SUM(QTYOR) from ["+plant+"_DODET] where item=B.ITEM and isnull(QTYOR,0) <>isnull(QTYIS,0) AND LNSTAT <> 'C'),0) ORDQTY,"
		 + "ISNULL((SELECT SUM(QTYOR) FROM ["+plant+"_ESTHDR] aa join ["+plant+"_ESTDET] bb on aa.ESTNO = bb.ESTNO WHERE ITEM=B.ITEM and "
		 + "CASE WHEN ((ISNULL(EXPIREDATE,'') <> '')) THEN "
		 + "CAST((SUBSTRING(isnull(EXPIREDATE,''), 7, 4) + '-' + SUBSTRING(isnull(EXPIREDATE,''), 4, 2) + '-' + SUBSTRING(isnull(EXPIREDATE,''), 1, 2)) AS date) "
		 + "ELSE convert(date, getdate(), 23) END >= convert(date, getdate(), 23)),0) ESTQTY,"
		 + "(select isnull(sum(qty),0) from ["+plant+"_INVMST] where item=b.ITEM and loc = a.LOC AND a.LOC NOT LIKE 'SHIPPINGAREA%' "
		 + "and a.LOC NOT LIKE 'TEMP_TO%') QTY,'B' as P_GROUP "
		 + "FROM ["+plant+"_LOCMST] a, ["+plant+"_ITEMMST] b right join ["+plant+"_ALTERNATE_BRAND_ITEM_MAPPING] d on d.ITEM = b.ITEM "
		 + "where isnull(b.ITEM,'') <> '' AND d.ALTERNATE_ITEM_NAME='"+ITEM+"' "+ condition
		 + "GROUP BY d.ITEM,b.ITEM,b.ITEMDESC,b.CATLOGPATH,b.INVENTORYUOM,b.PRD_BRAND_ID,b.MODEL,a.LOC,b.SALESUOM,b.UnitPrice,b.NONSTKFLAG "
		 + "UNION "
		 + "SELECT d.ALTERNATE_ITEM_NAME as ITEM,b.ITEMDESC ,ISNULL('/track/ReadFileServlet/?fileLocation='+b.CATLOGPATH,'../jsp/dist/img/NO_IMG.png') CATALOG,"
		 + "b.PRD_BRAND_ID,isnull(b.MODEL,'') MODEL,a.LOC,b.UnitPrice,b.NONSTKFLAG,"
		 + "ISNULL((Select M.OBDISCOUNT from ["+ plant +"_CUSTMST] C join ["+ plant +"_MULTI_PRICE_MAPPING] M on C.CUSTOMER_TYPE_ID=M.CUSTOMER_TYPE_ID WHERE M.ITEM=b.ITEM AND C.CNAME='"+cname+"'),'') as DISCOUNTBY, "
		 + "ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ plant +"_UOM u where u.UOM=(case when '"+UOM+"'='' then ISNULL(b.INVENTORYUOM,'') else '"+UOM+"' end)),'') as STKUOM,b.SALESUOM,"
		 + "ISNULL((select u.QPUOM from ["+plant+ "_UOM] u where u.UOM="+tableUOM+"),1) as UOMQTY,"+INVENTORYUOM
		 + ",ISNULL((select SUM(QTYOR) from ["+plant+"_DODET] where item=B.ITEM and isnull(QTYOR,0) <>isnull(QTYIS,0) AND LNSTAT <> 'C'),0) ORDQTY,"
		 + "ISNULL((SELECT SUM(QTYOR) FROM ["+plant+"_ESTHDR] aa join ["+plant+"_ESTDET] bb on aa.ESTNO = bb.ESTNO WHERE ITEM=B.ITEM and "
		 + "case when ((ISNULL(EXPIREDATE,'') <> '')) then "
		 + "CAST((SUBSTRING(isnull(EXPIREDATE,''), 7, 4) + '-' + SUBSTRING(isnull(EXPIREDATE,''), 4, 2) + '-' + SUBSTRING(isnull(EXPIREDATE,''), 1, 2)) AS date) "
		 + "ELSE convert(date, getdate(), 23) END >= convert(date, getdate(), 23)),0) ESTQTY,"
		 + "(select isnull(sum(qty),0) from ["+plant+"_INVMST] where item=b.ITEM and loc = a.LOC AND a.LOC NOT LIKE 'SHIPPINGAREA%' "
		 + "and a.LOC NOT LIKE 'TEMP_TO%') QTY,'B' as P_GROUP FROM ["+plant+"_LOCMST] a, ["+plant+"_ITEMMST] b "
		 + "RIGHT JOIN ["+plant+"_ALTERNATE_BRAND_ITEM_MAPPING] d on d.ALTERNATE_ITEM_NAME = b.ITEM WHERE isnull(b.ITEM,'') <> '' "
		 + " AND (d.ALTERNATE_ITEM_NAME <> '"+ITEM+"'  AND d.ITEM IN (SELECT ITEM FROM ["+plant+"_ALTERNATE_BRAND_ITEM_MAPPING] WHERE ALTERNATE_ITEM_NAME = '"+ITEM+"') " + 
		 " OR d.ITEM='"+ITEM+"') "
		 + condition
		 + "GROUP BY d.ITEM,d.ALTERNATE_ITEM_NAME,b.item,b.ITEMDESC ,b.CATLOGPATH,b.INVENTORYUOM,b.PRD_BRAND_ID,b.MODEL,a.LOC,b.SALESUOM,b.UnitPrice,b.NONSTKFLAG"
		 + ") A ORDER BY A.P_GROUP,A.ITEM asc";
	arrList = _InvMstDAO.selectForReport(aQuery);
	 	} catch (Exception e) {
	this.mLogger.exception(this.printLog, "", e);
	 	}
	 	return arrList;
}


public ArrayList getOtherProduct(String plant, Hashtable ht) throws Exception {
	ArrayList arrList = new ArrayList();
	 String INVENTORYUOM = "";
	   String tableUOM="";
	   String LOC = (String)ht.get("LOC");	
		String ITEM = (String)ht.get("ITEM");	
		String productDesc = (String)ht.get("ITEM_DESC");	
		String LOC_TYPE_ID = (String)ht.get("LOC_TYPE_ID");	
		String LOC_TYPE_ID2 = (String)ht.get("LOC_TYPE_ID2");	
		String LOC_TYPE_ID3 = (String)ht.get("LOC_TYPE_ID3");	
		String MODEL = (String)ht.get("MODEL");	
		String cname = (String)ht.get("CNAME");	
		String UOM = (String)ht.get("UOM");
		
		String condition="";
		
		if(ITEM.length() > 0) {
			condition += " AND b.ITEM='"+ITEM+"' ";
		}
		if(productDesc.length() > 0) {
			condition += " AND b.ITEMDESC='"+productDesc+"' ";
		}
		if(LOC.length() > 0) {
			condition += " AND a.LOC='"+LOC+"' ";
		}
		if(LOC_TYPE_ID.length() > 0) {
			condition += " AND A.LOC IN ( "+new LocMstDAO().getLocForLocType(plant,LOC_TYPE_ID)+" )";
		}
		if(LOC_TYPE_ID2.length() > 0) {
			condition += " AND A.LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,LOC_TYPE_ID2)+" )";
		}
		if(LOC_TYPE_ID3.length() > 0) {
			condition += " AND A.LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,LOC_TYPE_ID3)+" )";
		}
		if(MODEL.length() > 0) {
			condition += " AND b.MODEL='"+MODEL+"' ";
		}
	   if(0<UOM.length())
	   {
		   INVENTORYUOM = "'"+UOM+"'"+" as INVENTORYUOM";
		   tableUOM ="'"+ UOM+"'";
	   }
	   else
	   {
		   INVENTORYUOM = "ISNULL(b.INVENTORYUOM,'') as  INVENTORYUOM";
		   tableUOM = "b.INVENTORYUOM";
	   }
	
	 /*String sCondition=" GROUP BY A.ITEM,ITEMDESC,A.PRD_BRAND_ID,VINNO,MODEL,UnitPrice,i.LOC,A.INVENTORYUOM)SELECT *,case when 1<=ISNULL(UOMQTY,0) then (convert(int,(QTY*UOMQTY))) else QTY end STKQTY,convert(int,QTY/UOMQTY) as INVUOMQTY" + 
	 		"   FROM temptable A";*/ 
//	   String sCondition="";
	 String aQuery ="";
	 InvMstDAO _InvMstDAO = new InvMstDAO();
	 _InvMstDAO.setmLogger(mLogger);
	 if(productDesc.length()>0){
		 productDesc = "  AND A.ITEMDESC like'%"+productDesc+"%'";	                
            }
	 try {
		 /*aQuery = "with temptable as(SELECT ISNULL(A.ITEM,'') ITEM,ISNULL(ITEMDESC,'') ITEMDESC,ISNULL(A.PRD_BRAND_ID,'') BRAND ,ISNULL(VINNO,'') VINNO,ISNULL(MODEL,'') MODEL,ISNULL(UnitPrice,0) UNITPRICE "
				 + ",ISNULL((Select M.OBDISCOUNT from ["+ pLANT +"_CUSTMST] C join ["+ pLANT +"_MULTI_PRICE_MAPPING] M on C.CUSTOMER_TYPE_ID=M.CUSTOMER_TYPE_ID WHERE M.ITEM=A.ITEM AND C.CNAME='"+cname+"'),'') as DISCOUNTBY "
				 + ",i.LOC,SUM(QTY) as QTY,ISNULL((select TOP 1 u.UOM from [" +pLANT+ "_UOM] u where u.QPUOM=1),'')" + 
				 " as STKUOM,ISNULL((select u.QPUOM from [" +pLANT+ "_UOM] u where u.UOM="+tableUOM+"),1) as UOMQTY,"+INVENTORYUOM
				 + ",ISNULL((SELECT ISNULL('/track/ReadFileServlet/?fileLocation='+CATLOGPATH,'dist/img/NO_IMG.png')  FROM  [" + pLANT + "_" + "CATALOGMST" + "] WHERE PRODUCTID=A.ITEM),'dist/img/NO_IMG.png') CATALOG "
				 + " FROM [" + pLANT + "_" + "ITEMMST" + "] A join [" + pLANT + "_" + "INVMST" + "] i on i.ITEM=A.ITEM WHERE A.ITEM NOT IN (select ALTERNATE_ITEM_NAME from "+pLANT+"_ALTERNATE_BRAND_ITEM_MAPPING) AND LOC NOT LIKE 'SHIPPINGAREA%' and LOC NOT LIKE 'TEMP_TO%' AND A.ITEM!='' "+productDesc;*/
		 aQuery = "SELECT DISTINCT *,case when 0<ISNULL(UOMQTY,0) then (convert(int,(QTY*UOMQTY))) else QTY end STKQTY,"
		 + "CONVERT(DECIMAL(10,3),QTY/UOMQTY) as INVUOMQTY, (QTY - ORDQTY) AS AVAILABLE_QUANTITY FROM ("
		 + "SELECT b.ITEM as Parent,b.ITEM,b.ITEMDESC ,ISNULL('/track/ReadFileServlet/?fileLocation='+b.CATLOGPATH,'../jsp/dist/img/NO_IMG.png') CATALOG,"
		 + "b.PRD_BRAND_ID,isnull(b.MODEL,'') MODEL,a.LOC,b.UnitPrice,b.NONSTKFLAG,"
		 + "ISNULL((Select M.OBDISCOUNT from ["+ plant +"_CUSTMST] C join ["+ plant +"_MULTI_PRICE_MAPPING] M on C.CUSTOMER_TYPE_ID=M.CUSTOMER_TYPE_ID WHERE M.ITEM=b.ITEM AND C.CNAME='"+cname+"'),'') as DISCOUNTBY, "
		 + "ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ plant +"_UOM u where u.UOM=(case when '"+UOM+"'='' then ISNULL(b.INVENTORYUOM,'') else '"+UOM+"' end)),'') as STKUOM,b.SALESUOM,"
		 + "ISNULL((select u.QPUOM from ["+plant+"_UOM] u where u.UOM="+tableUOM+"),1) as UOMQTY,"+INVENTORYUOM
		 + ",ISNULL((select SUM(QTYOR) from ["+plant+"_DODET] where item=B.ITEM and isnull(QTYOR,0) <>isnull(QTYIS,0) AND LNSTAT <> 'C'),0) ORDQTY,"
		 + "ISNULL((SELECT SUM(QTYOR) FROM ["+plant+"_ESTHDR] aa join ["+plant+"_ESTDET] bb on aa.ESTNO = bb.ESTNO WHERE ITEM=B.ITEM and "
		 + "CASE WHEN ((ISNULL(EXPIREDATE,'') <> '')) THEN "
		 + "CAST((SUBSTRING(isnull(EXPIREDATE,''), 7, 4) + '-' + SUBSTRING(isnull(EXPIREDATE,''), 4, 2) + '-' + SUBSTRING(isnull(EXPIREDATE,''), 1, 2)) AS date) "
		 + "ELSE convert(date, getdate(), 23) END >= convert(date, getdate(), 23)),0) ESTQTY,"
		 + "(select isnull(sum(qty),0) from ["+plant+"_INVMST] where item=b.ITEM and loc = a.LOC AND a.LOC NOT LIKE 'SHIPPINGAREA%' "
		 + "and a.LOC NOT LIKE 'TEMP_TO%') QTY,'A' as P_GROUP "
		 + "FROM ["+plant+"_LOCMST] a, ["+plant+"_ITEMMST] b "
		 + "where isnull(b.ITEM,'') <> '' "+ condition 
		 + "GROUP BY b.ITEM,b.ITEMDESC,b.CATLOGPATH,b.INVENTORYUOM,b.PRD_BRAND_ID,b.MODEL,a.LOC,b.SALESUOM,b.UnitPrice,b.NONSTKFLAG "
		 + ") A ORDER BY A.Parent,A.P_GROUP asc";
		 arrList = _InvMstDAO.selectForReport(aQuery);
	 	} catch (Exception e) {
	 		this.mLogger.exception(this.printLog, "", e);
	 	}
	 	return arrList;
}

	public Map getAvailableQty(String item, String uom, String loc, String plant) {
//		String sCondition = "";
		Connection con = null;
		Map<String, String> map = new HashMap<>();
		try {
			con = DbBean.getConnection();
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
	
			String aQuery = "SELECT ISNULL(CONVERT(INT,(INVQTY/UOMQTY)) - (CONVERT(INT,(ORDQTY/UOMQTY))),0) AS AVAILABLEQTY " + 
					"FROM (SELECT " + 
					"(SELECT ISNULL(SUM(QTYOR),0) FROM ["+plant+"_DODET] WHERE ITEM='"+item+"' AND ISNULL(QTYOR,0) <>ISNULL(QTYIS,0) AND LNSTAT <> 'C') AS ORDQTY," + 
					"(SELECT ISNULL(SUM(QTY),0) FROM ["+plant+"_INVMST] WHERE ITEM='"+item+"' AND LOC = '"+loc+"' AND LOC NOT LIKE 'SHIPPINGAREA%' " + 
					"AND LOC NOT LIKE 'TEMP_TO%') AS INVQTY," + 
					"(SELECT ISNULL(U.QPUOM,1) FROM ["+plant+"_UOM] U WHERE U.UOM='"+uom+"') AS UOMQTY) A";
			System.out.println(aQuery);
			map = _InvMstDAO.getRowOfData(con, aQuery);
	
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return map;
	}
	public String getCostOfGoods(String item,String plant )
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		String Cost_Average=null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COS_AVG FROM " + "[" + plant + "_" + "FINITEMCOGSMAIN] WHERE ITEM ='"+item+"' ";
			System.out.println(sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				Cost_Average=rs.getString("COS_AVG");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return Cost_Average;
	}
	public String getItemMst(String item,String plant )
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		String Cost_Average=null;
		try {
			con = DbBean.getConnection();
			String sQry = "SELECT COST FROM " + "[" + plant + "_" + "ITEMMST] WHERE ITEM ='"+item+"' ";
			System.out.println(sQry);
			ps = con.prepareStatement(sQry);
			rs = ps.executeQuery();
			while (rs.next()) {
				Cost_Average=rs.getString("COST");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		} finally {
			DbBean.closeConnection(con, ps);
		}
		return Cost_Average;
	}
	
public String getAvgCostofItem(String item,String plant) throws Exception {
	String Avg=null;
	String slcQuery=null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	Connection con = null;
	try {
		con = DbBean.getConnection();
		slcQuery="SELECT *, " + 
				"       CASE " + 
				"         WHEN uomqty <= qty THEN qty - ( CONVERT(INT, ( qty / uomqty )) * uomqty " + 
				"                                       ) " + 
				"         ELSE qty " + 
				"       END                        STKQTY, " + 
				"       CONVERT(INT, qty / uomqty) AS INVUOMQTY, " + 
				"       (SELECT CASE " + 
				"                 WHEN (SELECT Count(currencyid) " + 
				"                       FROM   ["+plant+"_recvdet] " + 
				"                       WHERE  item = B.item " + 
				"                              AND currencyid IS NOT NULL " + 
				"                              AND tran_type IN( 'IB' ) " + 
				"                              AND unitcost > 0 " + 
				"                              AND unitcost IS NOT NULL) > 0 THEN (SELECT " + 
				"                 Isnull(Cast( " + 
				"                 Isnull(Sum(A.unitcost), 0) / Sum(A.recqty) AS DECIMAL(20, 5) " + 
				"                        ), 0) AS " + 
				"                 AVERGAGE_COST " + 
				"                                                                  FROM " + 
				"                 (SELECT recqty, " + 
				"                                                                 Cast(( " + 
				"                         Cast(Isnull(CASE " + 
				"                                       WHEN " + 
				"                              b.purchaseuom = '' " + 
				"                                     THEN " + 
				"               Isnull " + 
				"                 (( " + 
				"                    ( " + 
				"                 Isnull(R.unitcost*(SELECT " + 
				"                 currencyuseqt FROM " + 
				"                 ["+plant+"_currencymst] " + 
				"                 WHERE " + 
				"               currencyid " + 
				"               =Isnull(P.currencyid, 'SGD')), 0) " + 
				"               + Isnull((SELECT " + 
				"               (Sum(E.qty*landed_cost)/Sum(E.qty)) " + 
				"               FROM " + 
				"               ["+plant+"_recvdet] c " + 
				"               JOIN " + 
				"               ["+plant+"_finbillhdr] d ON c.pono = d.pono " + 
				"               AND c.grno = d.grno JOIN " + 
				"               ["+plant+"_finbilldet] e ON d.id = " + 
				"               e.billhdrid WHERE c.pono =R.pono AND " + 
				"               c.lnno=R.lnno " + 
				"               AND e.item = b.item), 0) + ( " + 
				"               Isnull(R.unitcost * (SELECT " + 
				"               currencyuseqt " + 
				"               FROM " + 
				"               ["+plant+"_currencymst] " + 
				"               WHERE " + 
				"               currencyid = Isnull(P.currencyid, 'SGD')), 0) * " + 
				"               ( ( " + 
				"               ( " + 
				"               Isnull(P.localexpenses, 0) + CASE WHEN ( " + 
				"               SELECT " + 
				"               Sum( " + 
				"               landed_cost) FROM " + 
				"               ["+plant+"_finbillhdr] c JOIN ["+plant+"_finbilldet] " + 
				"               d ON " + 
				"               c.id = d.billhdrid AND c.pono = R.pono AND " + 
				"               d.lnno " + 
				"               = " + 
				"               R.lnno) IS NULL THEN P.shippingcost ELSE 0 " + 
				"               END " + 
				"               ) " + 
				"               * 100 ) / NULLIF " + 
				"               (( " + 
				"               Isnull((SELECT " + 
				"               Sum( " + 
				"               s.qtyor * s.unitcost * s.currencyuseqt) " + 
				"               FROM   ["+plant+"_podet] s " + 
				"               WHERE  s.pono = R.pono), 0) ), 0) ) / 100 ) ) / (SELECT " + 
				"               currencyuseqt " + 
				"               FROM " + 
				"               ["+plant+"_currencymst] " + 
				"               WHERE " + 
				"               currencyid = Isnull(P.currencyid, 'SGD')) ), 0) " + 
				"               ELSE Isnull((SELECT Isnull(qpuom, 1) " + 
				"               FROM   ["+plant+"_uom] " + 
				"               WHERE  uom = ''), 1) * ( Isnull(( ( " + 
				"                            Isnull(R.unitcost*(SELECT " + 
				"               currencyuseqt FROM " + 
				"               ["+plant+"_currencymst] " + 
				"               WHERE " + 
				"               currencyid " + 
				"               =Isnull(P.currencyid, 'SGD')), 0) " + 
				"               + Isnull((SELECT " + 
				"               (Sum(E.qty*landed_cost)/Sum(E.qty)) FROM " + 
				"               ["+plant+"_recvdet] c " + 
				"               JOIN " + 
				"               ["+plant+"_finbillhdr] d ON c.pono = " + 
				"               d.pono AND c.grno = d.grno JOIN " + 
				"               ["+plant+"_finbilldet] e ON d.id = " + 
				"               e.billhdrid WHERE c.pono =R.pono AND " + 
				"               c.lnno=R.lnno " + 
				"               AND e.item = b.item), 0) + ( " + 
				"               Isnull(R.unitcost * (SELECT " + 
				"               currencyuseqt " + 
				"               FROM " + 
				"               ["+plant+"_currencymst] " + 
				"               WHERE " + 
				"               currencyid = Isnull(P.currencyid, 'SGD')), 0) * " + 
				"               ( ( " + 
				"               ( " + 
				"               Isnull(P.localexpenses, 0) + CASE WHEN ( " + 
				"               SELECT " + 
				"               Sum( " + 
				"               landed_cost) FROM " + 
				"               ["+plant+"_finbillhdr] c JOIN ["+plant+"_finbilldet] " + 
				"               d ON " + 
				"               c.id = d.billhdrid AND c.pono = R.pono AND " + 
				"               d.lnno " + 
				"               = " + 
				"               R.lnno) IS NULL THEN P.shippingcost ELSE 0 " + 
				"               END " + 
				"               ) " + 
				"               * 100 ) / NULLIF " + 
				"               (( " + 
				"               Isnull((SELECT " + 
				"               Sum( " + 
				"               s.qtyor * s.unitcost * s.currencyuseqt) " + 
				"               FROM   ["+plant+"_podet] s " + 
				"               WHERE  s.pono = R.pono), 0) ), 0) ) / 100 ) ) / ( " + 
				"               SELECT " + 
				"               currencyuseqt " + 
				"               FROM " + 
				"               ["+plant+"_currencymst] " + 
				"               WHERE " + 
				"               currencyid = Isnull(P.currencyid, 'SGD')) ), 0) / ( " + 
				"               Isnull((SELECT Isnull(qpuom, 1) " + 
				"               FROM   ["+plant+"_uom] " + 
				"               WHERE  uom = b.purchaseuom), 1) ) ) " + 
				"               END, 0) * (SELECT currencyuseqt " + 
				"               FROM   ["+plant+"_currencymst] " + 
				"               WHERE  currencyid = 'SGD') * (SELECT currencyuseqt " + 
				"               FROM   ["+plant+"_currencymst] " + 
				"               WHERE  currencyid = " + 
				"               Isnull(P.currencyid, 'SGD')) AS DECIMAL(20, 5)) ) * " + 
				"               Cast((SELECT currencyuseqt " + 
				"               FROM   ["+plant+"_currencymst] " + 
				"               WHERE  currencyid = 'SGD')AS DECIMAL(20, 5)) / " + 
				"               Cast((SELECT currencyuseqt " + 
				"               FROM   ["+plant+"_currencymst] " + 
				"               WHERE  currencyid = Isnull(P.currencyid, 'SGD')) " + 
				"               AS " + 
				"               DECIMAL(20, 5)) * " + 
				"               recqty AS DECIMAL(20, 5)) AS UNITCOST " + 
				"               FROM   "+plant+"_recvdet R " + 
				"               JOIN ["+plant+"_pohdr] P " + 
				"               ON R.pono = P.pono " + 
				"               WHERE  item = b.item " + 
				"               AND unitcost IS NOT NULL " + 
				"               AND unitcost > 0 " + 
				"               AND unitcost <> '' " + 
				"               AND tran_type = 'IB' " + 
				"               AND ordqty > 0) A) " + 
				"                 ELSE Cast(( ( B.cost ) * (SELECT currencyuseqt " + 
				"                                           FROM   ["+plant+"_currencymst] " + 
				"                                           WHERE  currencyid = 'SGD') ) AS " + 
				"                           DECIMAL(20, 5)) " + 
				"               END)               AS AVERAGE_COST, " + 
				"       (SELECT CASE " + 
				"                 WHEN (SELECT Count(currencyid) " + 
				"                       FROM   ["+plant+"_recvdet] " + 
				"                       WHERE  item = B.item " + 
				"                              AND currencyid IS NOT NULL " + 
				"                              AND tran_type IN( 'IB' ) " + 
				"                              AND unitcost > 0 " + 
				"                              AND unitcost IS NOT NULL) > 0 THEN (SELECT " + 
				"                 Isnull(Cast(Isnull(Sum(A.unitcost), 0) / Sum(recqty) AS DECIMAL " + 
				"                             (20, 5) " + 
				"                        ), " + 
				"                                                                   0) AS " + 
				"                 AVERGAGE_COST " + 
				"                                                                  FROM " + 
				"                 (SELECT recqty, " + 
				"                         Cast(( Cast(Isnull( " + 
				"                                CASE " + 
				"                                  WHEN " + 
				"       b.purchaseuom = " + 
				"       '1' THEN " + 
				"       Isnull(( " + 
				"       ( " + 
				"       Isnull(R.unitcost*(SELECT " + 
				"       currencyuseqt FROM " + 
				"       ["+plant+"_currencymst] " + 
				"       WHERE " + 
				"       currencyid " + 
				"       =Isnull(P.currencyid, 'SGD')), 0) " + 
				"       + Isnull((SELECT " + 
				"       (Sum(E.qty*landed_cost)/Sum(E.qty)) " + 
				"       FROM " + 
				"       ["+plant+"_recvdet] c " + 
				"       JOIN " + 
				"       ["+plant+"_finbillhdr] d ON c.pono = d.pono " + 
				"       AND c.grno = d.grno JOIN " + 
				"       ["+plant+"_finbilldet] e ON d.id = " + 
				"       e.billhdrid WHERE c.pono =R.pono AND " + 
				"       c.lnno=R.lnno " + 
				"       AND e.item = b.item), 0) + ( " + 
				"       Isnull(R.unitcost * (SELECT " + 
				"       currencyuseqt " + 
				"       FROM " + 
				"       ["+plant+"_currencymst] " + 
				"       WHERE " + 
				"       currencyid = Isnull(P.currencyid, 'SGD')), 0) * " + 
				"       ( ( " + 
				"       ( " + 
				"       Isnull(P.localexpenses, 0) + CASE WHEN ( " + 
				"       SELECT " + 
				"       Sum( " + 
				"       landed_cost) FROM " + 
				"       ["+plant+"_finbillhdr] c JOIN ["+plant+"_finbilldet] " + 
				"       d ON " + 
				"       c.id = d.billhdrid AND c.pono = R.pono AND " + 
				"       d.lnno " + 
				"       = " + 
				"       R.lnno) IS NULL THEN P.shippingcost ELSE 0 " + 
				"       END " + 
				"       ) " + 
				"       * 100 ) / NULLIF " + 
				"       (( " + 
				"       Isnull((SELECT " + 
				"       Sum( " + 
				"       s.qtyor * s.unitcost * s.currencyuseqt) " + 
				"       FROM   ["+plant+"_podet] s " + 
				"       WHERE  s.pono = R.pono), 0) ), 0) ) / 100 ) ) / ( " + 
				"       SELECT " + 
				"       currencyuseqt " + 
				"       FROM " + 
				"       ["+plant+"_currencymst] " + 
				"       WHERE " + 
				"       currencyid = Isnull(P.currencyid, 'SGD')) ), 0) " + 
				"       ELSE ( Isnull(( ( Isnull(R.unitcost*(SELECT " + 
				"       currencyuseqt " + 
				"       FROM " + 
				"       ["+plant+"_currencymst] " + 
				"       WHERE " + 
				"       currencyid " + 
				"       =Isnull(P.currencyid, 'SGD')), 0) " + 
				"       + Isnull((SELECT (Sum(E.qty*landed_cost)/Sum(E.qty)) " + 
				"       FROM " + 
				"       ["+plant+"_recvdet] c " + 
				"       JOIN " + 
				"       ["+plant+"_finbillhdr] d ON c.pono = d.pono AND c.grno = " + 
				"       d.grno " + 
				"       JOIN " + 
				"       ["+plant+"_finbilldet] e ON d.id = e.billhdrid WHERE " + 
				"       c.pono " + 
				"       =R.pono AND " + 
				"       c.lnno=R.lnno " + 
				"       AND e.item = b.item), 0) + ( Isnull( " + 
				"       R.unitcost * (SELECT currencyuseqt " + 
				"       FROM   ["+plant+"_currencymst] " + 
				"       WHERE " + 
				"       currencyid = Isnull(P.currencyid, 'SGD')), 0) * " + 
				"       ( ( " + 
				"       ( " + 
				"       Isnull(P.localexpenses, 0) + CASE WHEN ( " + 
				"       SELECT " + 
				"       Sum( " + 
				"       landed_cost) FROM " + 
				"       ["+plant+"_finbillhdr] c JOIN ["+plant+"_finbilldet] " + 
				"       d ON " + 
				"       c.id = d.billhdrid AND c.pono = R.pono AND " + 
				"       d.lnno " + 
				"       = " + 
				"       R.lnno) IS NULL THEN P.shippingcost ELSE 0 " + 
				"       END " + 
				"       ) " + 
				"       * 100 ) / NULLIF " + 
				"       (( " + 
				"       Isnull((SELECT " + 
				"       Sum( " + 
				"       s.qtyor * s.unitcost * s.currencyuseqt) " + 
				"       FROM   ["+plant+"_podet] s " + 
				"       WHERE " + 
				"       s.pono = R.pono), 0) ), 0) ) / 100 ) ) / (SELECT " + 
				"       currencyuseqt " + 
				"       FROM " + 
				"       ["+plant+"_currencymst] " + 
				"       WHERE " + 
				"       currencyid = Isnull(P.currencyid, 'SGD')) ), 0) / " + 
				"       ( Isnull((SELECT Isnull(qpuom, 1) " + 
				"       FROM   ["+plant+"_uom] " + 
				"       WHERE  uom = b.purchaseuom), 1) ) ) " + 
				"       END, 0) * (SELECT currencyuseqt " + 
				"       FROM   ["+plant+"_currencymst] " + 
				"       WHERE  currencyid = 'SGD') * " + 
				"       (SELECT currencyuseqt " + 
				"       FROM   ["+plant+"_currencymst] " + 
				"       WHERE  currencyid = " + 
				"       Isnull(P.currencyid, 'SGD')) AS " + 
				"       DECIMAL(20, 5)) ) * " + 
				"       Cast(( " + 
				"       SELECT currencyuseqt " + 
				"       FROM   ["+plant+"_currencymst] " + 
				"       WHERE  currencyid = 'SGD')AS DECIMAL(20, 5)) / " + 
				"       Cast((SELECT currencyuseqt " + 
				"       FROM   ["+plant+"_currencymst] " + 
				"       WHERE  currencyid = Isnull(P.currencyid, 'SGD')) " + 
				"       AS " + 
				"       DECIMAL(20, 5)) * " + 
				"       recqty AS DECIMAL(20, 5)) AS UNITCOST " + 
				"       FROM   "+plant+"_recvdet R " + 
				"       JOIN ["+plant+"_pohdr] P " + 
				"       ON R.pono = P.pono " + 
				"       WHERE  item = b.item " + 
				"       AND unitcost IS NOT NULL " + 
				"       AND unitcost > 0 " + 
				"       AND unitcost <> '' " + 
				"       AND tran_type = 'IB' " + 
				"       AND ordqty > 0) A) " + 
				"       ELSE Cast(( ( B.cost ) * (SELECT currencyuseqt " + 
				"       FROM   ["+plant+"_currencymst] " + 
				"       WHERE  currencyid = 'SGD') ) AS DECIMAL(20, 5)) " + 
				"       END)                       AS COST_PC " + 
				"FROM   (SELECT b.purchaseuom, " + 
				"               a.item, " + 
				"               b.itemdesc, " + 
				"               b.prd_cls_id                               AS PRDCLSID, " + 
				"               Isnull(b.prd_brand_id, '')                 AS PRD_BRAND_ID, " + 
				"               Isnull((SELECT TOP 1 u.uom " + 
				"                       FROM   "+plant+"_uom u " + 
				"                       WHERE  u.qpuom = 1), '')           AS STKUOM, " + 
				"               b.itemtype                                 AS ITEMTYPE, " + 
				"               Upper(a.loc)                               LOC, " + 
				"               a.userfld4                                 AS BATCH, " + 
				"               Sum(qty)                                   QTY, " + 
				"               Isnull(Cast(( ( Sum(b.unitprice) ) * (SELECT currencyuseqt " + 
				"                                                     FROM   ["+plant+"_currencymst] " + 
				"                                                     WHERE  currencyid = 'SGD') " + 
				"                           ) AS " + 
				"                           DECIMAL(20, 4)), 0)            AS LIST_PRICE, " + 
				"               Sum(b.cost)                                AS COST, " + 
				"               Isnull(b.inventoryuom, '')                 AS INVENTORYUOM, " + 
				"               Isnull((SELECT u.qpuom " + 
				"                       FROM   ["+plant+"_uom] u " + 
				"                       WHERE  u.uom = b.inventoryuom), 1) AS UOMQTY " + 
				"        FROM   ["+plant+"_invmst] A, " + 
				"               ["+plant+"_itemmst] AS b " + 
				"        WHERE  a.item = b.item " + 
				"               AND a.item IN (SELECT item " + 
				"                              FROM   "+plant+"_alternate_item_mapping " + 
				"                              WHERE  alternate_item_name LIKE '001HS10003000%') " + 
				"               AND qty > 0 " + 
				"               AND A.item = '"+item+"' " + 
				"               AND A.plant = '"+plant+"' " + 
				"        GROUP  BY a.item, " + 
				"                  a.loc, " + 
				"                  a.userfld4, " + 
				"                  b.itemdesc, " + 
				"                  b.prd_cls_id, " + 
				"                  b.prd_brand_id, " + 
				"                  b.itemtype, " + 
				"                  b.stkuom, " + 
				"                  b.inventoryuom, " + 
				"                  b.purchaseuom) B " + 
				"ORDER  BY item, " + 
				"          loc  ";
		ps = con.prepareStatement(slcQuery);
		rs = ps.executeQuery();
		while (rs.next()) {
			Avg=rs.getString("AVERAGE_COST");
		}
		
	}catch(Exception e) {
		e.printStackTrace();
	} finally {
		DbBean.closeConnection(con, ps);
	}
	return Avg;
	}

public ArrayList getInvListSummaryWithAverageCostWithUOMLandedCostForInventory(Hashtable ht, String afrmDate,String atoDate,String plant,String aItem,String productDesc,String currencyid,String baseCurrency,String loctypeid,String loctypeid2,String loctypeid3,String loc,String UOM) throws Exception {
    ArrayList arrList = new ArrayList();
    String sCondition ="";//,sDateCod="";
    if (productDesc.length() > 0 ) {
        if (productDesc.indexOf("%") != -1) {
                productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
        }
        sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
    }
   String sRecvCondition="";
   String INVENTORYUOM = "";
   String tableUOM="";
   if(0<UOM.length())
   {
	   INVENTORYUOM = "'"+UOM+"'"+" as INVENTORYUOM";
	   tableUOM ="'"+ UOM+"'";
   }
   else
   {
	   INVENTORYUOM = "ISNULL(b.INVENTORYUOM,'') as  INVENTORYUOM";
	   tableUOM = "b.INVENTORYUOM";
   }
   
    if (afrmDate.length() > 0) {
            sRecvCondition = sRecvCondition + " and substring(CRAT,1,8)  >= '" + afrmDate
                            + "'  ";
            if (atoDate.length() > 0) {
                    sRecvCondition = sRecvCondition + " and substring(CRAT,1,8)   <= '" + atoDate
                                    + "'  ";
            }
    } else {
            if (atoDate.length() > 0) {
                    sRecvCondition = sRecvCondition + " and substring(CRAT,1,8) <= '" + atoDate
                                    + "'  ";
            }
    }
    
    if (loc != null && loc!="") {
    	sCondition = sCondition+" AND LOC like '%"+loc+"%'";
    }
    
    if (loctypeid != null && loctypeid!="") {
		sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType(plant,loctypeid)+" )";
	}
    if (loctypeid2 != null && loctypeid2!="") {
		sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType2(plant,loctypeid2)+" )";
	}
    if (loctypeid3 != null && loctypeid3!="") {
		sCondition = sCondition + " AND LOC IN ( "+new LocMstDAO().getLocForLocType3(plant,loctypeid3)+" )";
	}
    String ConvertUnitCostToOrderCurrency = "";
   
    	ConvertUnitCostToOrderCurrency = " (CAST(ISNULL("
    			+ "CASE WHEN b.PURCHASEUOM = '"+UOM+"' then "
				
    			+ " ISNULL(((isnull(ISNULL(T.UNITCOST_AOD, T.UNITCOST)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0)+"
				+ " (ISNULL(ISNULL(T.UNITCOST_AOD, T.UNITCOST)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0) * (((ISNULL(P.LOCALEXPENSES,0)+"
				+ "  P.SHIPPINGCOST )*100)/NULLIF((ISNULL((select SUM(s.qtyor*ISNULL(s.UNITCOST_AOD, s.UNITCOST)*s.CURRENCYUSEQT) from ["+plant+"_podet] s where s.pono=R.pono),0)),0))/100))/"
				+ " (SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"'))),0) "
				
				+ " else ISNULL((select ISNULL(QPUOM,1) from ["+plant+"_UOM] where UOM='"+UOM+"'),1) * ("
	
				+ " ISNULL(((isnull(ISNULL(T.UNITCOST_AOD, T.UNITCOST)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0)+"
				+ " (ISNULL(ISNULL(T.UNITCOST_AOD, T.UNITCOST)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0) * (((ISNULL(P.LOCALEXPENSES,0)+"
				+ " P.SHIPPINGCOST)*100)/NULLIF((ISNULL((select SUM(s.qtyor*ISNULL(s.UNITCOST_AOD, s.UNITCOST)*s.CURRENCYUSEQT) from ["+plant+"_podet] s where s.pono=R.pono),0)),0))/100))/"
				+ " (SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"'))),0) "
				
    			+ "/ (ISNULL((select ISNULL(QPUOM,1) from ["+plant+"_UOM] "
    			+ "where UOM=b.PURCHASEUOM),1))) end ,0) AS DECIMAL(20,5)) ) ";


  StringBuffer sql = new StringBuffer("ISNULL((SELECT CASE WHEN (SELECT COUNT(CURRENCYID) FROM [" +plant+ "_RECVDET] R WHERE ITEM=B.ITEM AND CURRENCYID IS NOT NULL AND TRAN_TYPE IN('IB','GOODSRECEIPTWITHBATCH') )>0  THEN ");
  sql.append(" (Select ISNULL(CAST(ISNULL(SUM(CASE WHEN TRAN_TYPE='GOODSRECEIPTWITHBATCH' THEN 0 ELSE A.UNITCOST END),0)/SUM(VC) AS DECIMAL(20,5)),0) AS AVERGAGE_COST from ");
sql.append(" (select TRAN_TYPE,RECQTY VC,CAST("+ConvertUnitCostToOrderCurrency+" * RECQTY AS DECIMAL(20,5)) AS UNITCOST ");
sql.append("   from "+plant+"_RECVDET R JOIN ["+plant+"_POHDR] P ON R.PONO = P.PONO JOIN [" +plant+ "_PODET] T ON T.PONO = P.PONO  where R.item =b.ITEM AND R.ITEM=T.ITEM AND R.UNITCOST IS NOT NULL  AND R.UNITCOST >=0  AND R.UNITCOST <> ''  AND tran_type IN('IB','GOODSRECEIPTWITHBATCH') AND ORDQTY >0 " + sRecvCondition +"  ) A ) ");
sql.append(" ELSE ");
sql.append(" (SELECT CASE WHEN (SELECT COUNT(*) FROM ["+plant+"_RECVDET] WHERE ITEM=b.ITEM AND tran_type IN('INVENTORYUPLOAD') )>0 THEN  ");
sql.append(" (SELECT SUM(UNITCOST) FROM ["+plant+"_RECVDET] C ");
sql.append(" where item = b.item and c.BATCH = b.BATCH and c.LOC=b.LOC AND tran_type IN('INVENTORYUPLOAD')) ELSE ");
sql.append(" CAST(((B.COST)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+baseCurrency+"')) AS DECIMAL(20,5)) END) END ),0) AS AVERAGE_COST");
String ConvertUnitCostToOrderCurrency2 = "";

ConvertUnitCostToOrderCurrency2 = " (CAST(ISNULL("
		+ "CASE WHEN b.PURCHASEUOM = '1' then "
		
		+ " ISNULL(((isnull(ISNULL(T.UNITCOST_AOD, T.UNITCOST)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0)+"
		+ " (ISNULL(ISNULL(T.UNITCOST_AOD, T.UNITCOST)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0) * (((ISNULL(P.LOCALEXPENSES,0)+"
		+ " P.SHIPPINGCOST)*100)/NULLIF((ISNULL((select SUM(s.qtyor*ISNULL(s.UNITCOST_AOD, s.UNITCOST)*s.CURRENCYUSEQT) from ["+plant+"_podet] s where s.pono=R.pono),0)),0))/100))/"
		+ " (SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"'))),0) "
		
		+ "else "
		+ " ("

		+ " ISNULL(((isnull(ISNULL(T.UNITCOST_AOD, T.UNITCOST)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0)+"
		+ " (ISNULL(ISNULL(T.UNITCOST_AOD, T.UNITCOST)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0) * (((ISNULL(P.LOCALEXPENSES,0)+"
		+ " P.SHIPPINGCOST)*100)/NULLIF((ISNULL((select SUM(s.qtyor*ISNULL(s.UNITCOST_AOD, s.UNITCOST)*s.CURRENCYUSEQT) from ["+plant+"_podet] s where s.pono=R.pono),0)),0))/100))/"
		+ " (SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"'))),0) "

		+ "/ (ISNULL((select ISNULL(QPUOM,1) from [" +plant+ "_UOM] "
		+ "where UOM=b.PURCHASEUOM),1))) end ,0) AS DECIMAL(20,5)) ) ";

StringBuffer sql2 = new StringBuffer("ISNULL((SELECT CASE WHEN (SELECT COUNT(CURRENCYID) FROM [" +plant+ "_RECVDET] R WHERE ITEM=B.ITEM AND CURRENCYID IS NOT NULL AND TRAN_TYPE IN('IB','GOODSRECEIPTWITHBATCH') )>0  THEN ");
sql2.append(" (Select ISNULL(CAST(ISNULL(SUM(CASE WHEN TRAN_TYPE='GOODSRECEIPTWITHBATCH' THEN 0 ELSE A.UNITCOST END),0)/SUM(VC) AS DECIMAL(20,5)),0) AS AVERGAGE_COST from ");
sql2.append(" (select TRAN_TYPE,RECQTY VC,CAST("+ConvertUnitCostToOrderCurrency2+" * RECQTY");
sql2.append(" AS DECIMAL(20,5)) AS UNITCOST ");
sql2.append(" from "+plant+"_RECVDET R JOIN ["+plant+"_POHDR] P ON R.PONO = P.PONO JOIN [" +plant+ "_PODET] T ON T.PONO = P.PONO where R.item =b.ITEM AND R.ITEM=T.ITEM AND R.UNITCOST IS NOT NULL  AND R.UNITCOST >=0  AND R.UNITCOST <> ''  AND tran_type IN('IB','GOODSRECEIPTWITHBATCH') AND ORDQTY >0 " + sRecvCondition +"  ) A) ");
sql2.append(" ELSE ");
sql2.append(" (SELECT CASE WHEN (SELECT COUNT(*) FROM ["+plant+"_RECVDET] WHERE ITEM=b.ITEM AND tran_type IN('INVENTORYUPLOAD') )>0 THEN  ");
sql2.append(" (SELECT SUM(UNITCOST) FROM ["+plant+"_RECVDET] C ");
sql2.append(" where item = b.item and c.BATCH = b.BATCH and c.LOC=b.LOC AND tran_type IN('INVENTORYUPLOAD')) ELSE ");
sql2.append(" CAST(((B.COST)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+baseCurrency+"')) AS DECIMAL(20,5)) END) END ),0) AS COST_PC ");
    
    try {
            InvMstDAO _InvMstDAO = new InvMstDAO();
            _InvMstDAO.setmLogger(mLogger);
           sCondition = sCondition  + " group by a.item,a.loc,a.userfld4,b.itemdesc,b.prd_dept_id,b.prd_cls_id,b.prd_brand_id,b.itemtype,b.stkuom,b.INVENTORYUOM,b.PURCHASEUOM ) B order by item ,LOC ";

            // Added stkuom to the query
           //---Modified by Bruhan on Feb 27 2014, Description: To display location data's in uppercase
             String aQuery = "SELECT *,case when 0<ISNULL(UOMQTY,0) then (convert(int,(QTY*UOMQTY))) else QTY end STKQTY,convert(int,QTY/UOMQTY) as INVUOMQTY,0 as BILLED_QTY,0 as UNBILLED_QTY,"
             		+ ""+sql.toString()+","+sql2.toString()+" FROM (select b.PURCHASEUOM,a.ITEM,b.ITEMDESC,b.prd_cls_id as PRDCLSID,isnull(b.prd_dept_id,'') as PRD_DEPT_ID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ plant +"_UOM u where u.UOM=(case when '"+UOM+"'='' then ISNULL(b.INVENTORYUOM,'') else '"+UOM+"' end)),'') as STKUOM,b.itemtype as ITEMTYPE,UPPER(a.LOC) LOC,a.UserFld4 as BATCH, sum(QTY) QTY,ISNULL(CAST(((sum(b.UNITPRICE))*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')) AS DECIMAL(20,4)),0) as LIST_PRICE,sum(b.COST) as COST," +INVENTORYUOM+","+ 
             		" ISNULL((select u.QPUOM from [" +plant+ "_UOM] u where u.UOM="+tableUOM+"),1) as UOMQTY    from "
                            + "["
                            + plant
                            + "_"
                            + "invmst"
                            + "]"
                            + " A,"
                            + "["
                            + plant
                            + "_"
                            + "itemmst"
                            + "]"
                            + " as b where a.item=b.item  and a.item in (SELECT ITEM FROM "
                            + plant
                            + "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"+ aItem + "%')"
                            + "and QTY>0 ";
            arrList = _InvMstDAO.selectForReport(aQuery, ht,sCondition);
            

    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
    }
    return arrList;
	}


	public ArrayList getInvListSummaryWithAvlQtyMultiUom(Hashtable ht, String aPlant,String aItem,String productDesc,String UOM) throws Exception {
    ArrayList arrList = new ArrayList();        
    String sCondition ="";
    if(aItem.length() > 0) {
    	sCondition += " AND ITEM='"+aItem+"' ";
	}
    if (productDesc.length() > 0 ) {
    if (productDesc.indexOf("%") != -1) {
            productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
    }
    
     sCondition = " and replace(b.ITEMDESC,' ','') like '%"+ StrUtils.InsertQuotes(productDesc.replaceAll(" ","")) + "%' ";
    }
    
    try {
            InvMstDAO _InvMstDAO = new InvMstDAO();
            _InvMstDAO.setmLogger(mLogger);
           sCondition = sCondition  + "  order by a.item";
           String aQuery="";
			if(UOM.length()>0){
			 aQuery = "SELECT ITEM, ITEMDESC, PRDCLSID,PRD_DEPT_ID, PRD_BRAND_ID, ITEMTYPE, STKUOM, STKQTY, INVENTORYUOM, UOMQTY, convert(int,(ESTIMATEQTY/UOMQTY)) as ESTIMATEQTY, convert(int,(OUTBOUNDQTY/UOMQTY)) as OUTBOUNDQTY, convert(int,(AVAILABLEQTY/UOMQTY)) as AVAILABLEQTY,convert(int,(STKQTY/UOMQTY)) as QTY  FROM (" + 
			 		"select (" + 
			 		"SELECT ISNULL((" + 
			 		"(SELECT ISNULL(SUM(CASE WHEN QTY<0 THEN 0 ELSE QTY END),0) FROM "+aPlant+"_INVMST WHERE ITEM=b.item AND QTY > 0 AND PLANT='"+aPlant+"')" + 
			 		"-" + 
			 		"(" + 
			 		"((" + 
			 		"(SELECT ISNULL(SUM(QTYOR*ISNULL(QPUOM,1)),0) FROM ["+aPlant+"_ESTDET] E JOIN ["+aPlant+"_UOM] F ON E.UNITMO=F.UOM WHERE ITEM=b.item AND E.PLANT='"+aPlant+"')-" + 
			 		"(SELECT ISNULL(SUM(QTYIS*ISNULL(QPUOM,1)),0) FROM ["+aPlant+"_ESTDET] E JOIN ["+aPlant+"_UOM] F ON E.UNITMO=F.UOM WHERE ITEM=b.item AND E.PLANT='"+aPlant+"')" + 
			 		"))+" +  
			 		"((" + 
			 		"(SELECT ISNULL(SUM(QTYOR*ISNULL(QPUOM,1)),0) FROM ["+aPlant+"_DODET] E JOIN ["+aPlant+"_UOM] F ON E.UNITMO=F.UOM WHERE ITEM=b.item AND E.PLANT='"+aPlant+"' AND LNSTAT<>'C')-" + 
			 		"(SELECT ISNULL(SUM(QTYIS*ISNULL(QPUOM,1)),0) FROM ["+aPlant+"_DODET] E JOIN ["+aPlant+"_UOM] F ON E.UNITMO=F.UOM WHERE ITEM=b.item AND E.PLANT='"+aPlant+"' AND LNSTAT<>'C')" + 
			 		"))" + 
			 		")" + 
			 		"),0)" + 
			 		") AS AVAILABLEQTY," + 
			 		"(" + 
			 		"(SELECT ISNULL(SUM(QTYOR*ISNULL(QPUOM,1)),0) FROM "+aPlant+"_DODET E JOIN ["+aPlant+"_UOM] F ON E.UNITMO=F.UOM WHERE ITEM=b.item AND E.PLANT='"+aPlant+"' AND LNSTAT<>'C')-" + 
			 		"(SELECT ISNULL(SUM(QTYIS*ISNULL(QPUOM,1)),0) FROM "+aPlant+"_DODET E JOIN ["+aPlant+"_UOM] F ON E.UNITMO=F.UOM WHERE ITEM=b.item AND E.PLANT='"+aPlant+"' AND LNSTAT<>'C')) AS OUTBOUNDQTY," + 
			 		"((SELECT ISNULL(SUM(QTYOR*ISNULL(QPUOM,1)),0) FROM "+aPlant+"_ESTDET E JOIN ["+aPlant+"_UOM] F ON E.UNITMO=F.UOM WHERE ITEM=b.item AND E.PLANT='"+aPlant+"')-" + 
			 		"(SELECT ISNULL(SUM(QTYIS*ISNULL(QPUOM,1)),0) FROM "+aPlant+"_ESTDET E JOIN ["+aPlant+"_UOM] F ON E.UNITMO=F.UOM WHERE ITEM=b.item AND E.PLANT='"+aPlant+"')) AS ESTIMATEQTY," + 
			 		"b.ITEM,b.ITEMDESC,b.prd_cls_id as PRDCLSID,b.PRD_DEPT_ID as PRD_DEPT_ID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,b.itemtype as ITEMTYPE,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ aPlant +"_UOM u where u.UOM='"+UOM+"'),'') as STKUOM," + 
			 		"ISNULL(SUM(CASE WHEN QTY<0 THEN 0 ELSE QTY END),0) STKQTY,'"+UOM+"'INVENTORYUOM,"+
			 		"ISNULL((select u.QPUOM from "+aPlant+"_UOM u where u.UOM='"+UOM+"'),1) as UOMQTY from" + 
			 		"["+aPlant+"_invmst] a RIGHT JOIN ["+aPlant+"_itemmst] b on a.item=b.item and a.PLANT = b.PLANT" + 
			 		" where b.PLANT = '"+aPlant+"'" + 
			 		" and b.item in (SELECT ITEM FROM "+aPlant+"_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '%') AND b.NONSTKFLAG<>'Y'" + 
			 		" group by b.item,b.ITEMDESC,b.prd_cls_id,b.prd_dept_id,b.itemtype,b.prd_brand_id,b.stkuom,INVENTORYUOM) a" + 
			 		" WHERE (AVAILABLEQTY > 0 OR OUTBOUNDQTY > 0 OR ESTIMATEQTY > 0 OR STKQTY > 0) ";
			}
			else
			{
				aQuery = "SELECT ITEM, ITEMDESC, PRDCLSID, PRD_BRAND_ID,PRD_DEPT_ID, ITEMTYPE, STKUOM, STKQTY, INVENTORYUOM, UOMQTY, convert(int,(ESTIMATEQTY/UOMQTY)) as ESTIMATEQTY, convert(int,(OUTBOUNDQTY/UOMQTY)) as OUTBOUNDQTY, convert(int,(AVAILABLEQTY/UOMQTY)) as AVAILABLEQTY,convert(int,(STKQTY/UOMQTY)) as QTY FROM (" + 
				 		"select (" + 
				 		"SELECT ISNULL((" + 
				 		"(SELECT ISNULL(SUM(CASE WHEN QTY<0 THEN 0 ELSE QTY END),0) FROM "+aPlant+"_INVMST WHERE ITEM=b.item AND QTY > 0 AND PLANT='"+aPlant+"')" + 
				 		"-" + 
				 		"(" + 
				 		"((" + 
				 		"(SELECT ISNULL(SUM(QTYOR*ISNULL(QPUOM,1)),0) FROM ["+aPlant+"_ESTDET] E JOIN ["+aPlant+"_UOM] F ON E.UNITMO=F.UOM WHERE ITEM=b.item AND E.PLANT='"+aPlant+"')-" + 
				 		"(SELECT ISNULL(SUM(QTYIS*ISNULL(QPUOM,1)),0) FROM ["+aPlant+"_ESTDET] E JOIN ["+aPlant+"_UOM] F ON E.UNITMO=F.UOM WHERE ITEM=b.item AND E.PLANT='"+aPlant+"')" + 
				 		"))+" +  
				 		"((" + 
				 		"(SELECT ISNULL(SUM(QTYOR*ISNULL(QPUOM,1)),0) FROM ["+aPlant+"_DODET] E JOIN ["+aPlant+"_UOM] F ON E.UNITMO=F.UOM WHERE ITEM=b.item AND E.PLANT='"+aPlant+"' AND LNSTAT<>'C')-" + 
				 		"(SELECT ISNULL(SUM(QTYIS*ISNULL(QPUOM,1)),0) FROM ["+aPlant+"_DODET] E JOIN ["+aPlant+"_UOM] F ON E.UNITMO=F.UOM WHERE ITEM=b.item AND E.PLANT='"+aPlant+"' AND LNSTAT<>'C')" + 
				 		"))" + 
				 		")" + 
				 		"),0)" + 
				 		") AS AVAILABLEQTY," + 
				 		"(" + 
				 		"(SELECT ISNULL(SUM(QTYOR*ISNULL(QPUOM,1)),0) FROM "+aPlant+"_DODET E JOIN ["+aPlant+"_UOM] F ON E.UNITMO=F.UOM WHERE ITEM=b.item AND E.PLANT='"+aPlant+"' AND LNSTAT<>'C')-" + 
				 		"(SELECT ISNULL(SUM(QTYIS*ISNULL(QPUOM,1)),0) FROM "+aPlant+"_DODET E JOIN ["+aPlant+"_UOM] F ON E.UNITMO=F.UOM WHERE ITEM=b.item AND E.PLANT='"+aPlant+"' AND LNSTAT<>'C')) AS OUTBOUNDQTY," + 
				 		"((SELECT ISNULL(SUM(QTYOR*ISNULL(QPUOM,1)),0) FROM "+aPlant+"_ESTDET E JOIN ["+aPlant+"_UOM] F ON E.UNITMO=F.UOM WHERE ITEM=b.item AND E.PLANT='"+aPlant+"')-" + 
				 		"(SELECT ISNULL(SUM(QTYIS*ISNULL(QPUOM,1)),0) FROM "+aPlant+"_ESTDET E JOIN ["+aPlant+"_UOM] F ON E.UNITMO=F.UOM WHERE ITEM=b.item AND E.PLANT='"+aPlant+"')) AS ESTIMATEQTY," + 
				 		" b.ITEM,b.ITEMDESC,b.prd_cls_id as PRDCLSID,b.PRD_DEPT_ID as PRD_DEPT_ID,isnull(b.prd_brand_id,'') as PRD_BRAND_ID,b.itemtype as ITEMTYPE,"+
				 		"ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ aPlant +"_UOM u where u.UOM='"+UOM+"'),'') as STKUOM, ISNULL(b.INVENTORYUOM,'') as  INVENTORYUOM," + 
				 		" ISNULL(SUM(CASE WHEN QTY<0 THEN 0 ELSE QTY END),0) STKQTY, ISNULL((select u.QPUOM from "+ aPlant +"_UOM u where u.UOM=b.INVENTORYUOM),1) as UOMQTY from" + 
				 		" ["+aPlant+"_invmst] a RIGHT JOIN ["+aPlant+"_itemmst] b on a.item=b.item and a.PLANT = b.PLANT" + 
				 		" where b.PLANT = '"+aPlant+"'" + 
				 		" and b.item in (SELECT ITEM FROM "+aPlant+"_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '%') AND b.NONSTKFLAG<>'Y' " + 
				 		" group by b.item,b.ITEMDESC,b.prd_cls_id,b.itemtype,b.prd_dept_id,b.prd_brand_id,b.STKUOM,b.INVENTORYUOM) a" + 
				 		" WHERE (AVAILABLEQTY > 0 OR OUTBOUNDQTY > 0 OR ESTIMATEQTY > 0 OR STKQTY > 0) ";
			}
            arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

    } catch (Exception e) {
            this.mLogger.exception(this.printLog, "", e);
    }
    return arrList;
	}
	
	public ArrayList getInvListSummaryAvailableQtyforShopify(String aPlant, String aItem,
			String productDesc,Hashtable ht) throws Exception {

		ArrayList arrList = new ArrayList();
		
		    String sCondition=""; 
//	            String aLocCond ="";
		    if (productDesc.length() > 0 ) {
                if (productDesc.indexOf("%") != -1) {
                        productDesc = productDesc.substring(0, productDesc.indexOf("%") - 1);
                }
		    }
		    
			try {
				InvMstDAO _InvMstDAO = new InvMstDAO();
				_InvMstDAO.setmLogger(mLogger);
				sCondition = sCondition
						+ " group by b.item,b.ITEMDESC) a WHERE AVAILABLEQTY >= 0 OR QTY >= 0  Order by a.item";

				//old formula for available quantity.

				String aQuery = "SELECT * FROM (select (SELECT ISNULL(((SELECT ISNULL(SUM(CASE WHEN "+IConstants.QTY+"<0 THEN 0 ELSE "+IConstants.QTY+" END),0) FROM "+ aPlant+ "_INVMST"
						+ " WHERE "+ IConstants.ITEM+ "=b.item AND QTY > 0 AND "+ IConstants.PLANT
						+ "='"+ aPlant+ "')-"
						+"((((SELECT ISNULL(SUM(QTYOR),0) FROM ["+ aPlant+"_ESTDET] WHERE ITEM=b.item AND PLANT='"+ aPlant+ "')-" +
						"(SELECT ISNULL(SUM(QTYIS),0) FROM ["+ aPlant+"_ESTDET] WHERE ITEM=b.item AND PLANT='"+ aPlant+ "')))+"+
						"(((SELECT ISNULL(SUM(QTYOR),0) FROM [" + aPlant + "_DODET] WHERE ITEM=b.item AND PLANT='"+ aPlant+ "'  AND LNSTAT<>'C')-"+
						"(SELECT ISNULL(SUM(QTYIS),0) FROM [" + aPlant + "_DODET] WHERE ITEM=b.item AND PLANT='"+ aPlant+ "'  AND LNSTAT<>'C'))))),0)) AS AVAILABLEQTY," +
						" b.ITEM,b.ITEMDESC, ISNULL(SUM(CASE WHEN "+IConstants.QTY+"<0 THEN 0 ELSE "+IConstants.QTY+" END),0) QTY from "
						+ "["
						+ aPlant
						+ "_"
						+ "invmst"
						+ "]"
						+ " a RIGHT JOIN "
						+ "["
						+ aPlant
						+ "_"
						+ "itemmst"
						+ "] b"
						+ " on a.item=b.item and a.PLANT = b.PLANT where b.PLANT = '"+ aPlant+"' and b.item in (SELECT ITEM FROM "
						+ aPlant
						+ "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"
						+ aItem + "%') AND b.NONSTKFLAG<>'Y' ";
				
				arrList = _InvMstDAO.selectForReport(aQuery, ht, sCondition);

			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}
			return arrList;
		
	}
	
	public ArrayList getAverageCost(Hashtable ht,String plant,String aItem,String currencyid,String baseCurrency) throws Exception {
	    ArrayList arrList = new ArrayList();
	    String sCondition ="";//,sDateCod="";
	   String sRecvCondition="";
	   String INVENTORYUOM = "",UOM="";
	   String tableUOM="";
	   if(0<UOM.length())
	   {
		   INVENTORYUOM = "'"+UOM+"'"+" as INVENTORYUOM";
		   tableUOM ="'"+ UOM+"'";
	   }
	   else
	   {
		   INVENTORYUOM = "ISNULL(b.INVENTORYUOM,'') as  INVENTORYUOM";
		   tableUOM = "b.INVENTORYUOM";
	   }
	    
	    String ConvertUnitCostToOrderCurrency = "";
	    ConvertUnitCostToOrderCurrency = " (CAST(ISNULL("
		+ "CASE WHEN b.PURCHASEUOM = '"+UOM+"' then "
		
		+ " ISNULL(((isnull(R.unitcost*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0)+"
		+ " ISNULL((SELECT (SUM(E.QTY*LANDED_COST)/SUM(E.QTY)) FROM ["+plant+"_RECVDET] c left join ["+plant+"_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO left join ["+plant+"_FINBILLDET] e on d.ID = e.BILLHDRID where c.pono =R.pono and c.LNNO=R.LNNO and e.ITEM = b.ITEM OR c.TRAN_TYPE='GOODSRECEIPTWITHBATCH' AND c.item = b.item),0) + "
		+ " (ISNULL(R.unitcost*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0) * (((ISNULL(P.LOCALEXPENSES,0)+"
		+ " CASE WHEN (SELECT SUM(LANDED_COST) FROM ["+plant+"_FINBILLHDR] c join ["+plant+"_FINBILLDET] d "
		+ " ON c.ID = d.BILLHDRID and c.PONO = R.PONO and d.LNNO = R.LNNO) is null THEN P.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from ["+plant+"_podet] s where s.pono=R.pono),0)),0))/100))/"
		+ " (SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"'))),0) "
		
		+ " else ISNULL((select ISNULL(QPUOM,1) from ["+plant+"_UOM] where UOM='"+UOM+"'),1) * ("

		+ " ISNULL(((isnull(R.unitcost*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0)+"
		+ " ISNULL((SELECT (SUM(E.QTY*LANDED_COST)/SUM(E.QTY)) FROM ["+plant+"_RECVDET] c left join ["+plant+"_FINBILLHDR] d on c.PONO = d.PONO and c.GRNO = d.GRNO left join ["+plant+"_FINBILLDET] e on d.ID = e.BILLHDRID where c.pono =R.pono and c.LNNO=R.LNNO and e.ITEM = b.ITEM OR c.TRAN_TYPE='GOODSRECEIPTWITHBATCH' AND c.item = b.item),0) + "
		+ " (ISNULL(R.unitcost*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')),0) * (((ISNULL(P.LOCALEXPENSES,0)+"
		+ " CASE WHEN (SELECT SUM(LANDED_COST) FROM ["+plant+"_FINBILLHDR] c join ["+plant+"_FINBILLDET] d "
		+ " ON c.ID = d.BILLHDRID and c.PONO = R.PONO and d.LNNO = R.LNNO) is null THEN P.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from ["+plant+"_podet] s where s.pono=R.pono),0)),0))/100))/"
		+ " (SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"'))),0) "
		
		+ "/ (ISNULL((select ISNULL(QPUOM,1) from ["+plant+"_UOM] "
		+ "where UOM=b.PURCHASEUOM),1))) end ,0) *(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] " +
        "   WHERE  CURRENCYID='"+baseCurrency+"')*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] " +
        "   WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')) AS DECIMAL(20,5)) ) ";
	    
	    StringBuffer sql = new StringBuffer(" (SELECT CASE WHEN  (SELECT COUNT(CURRENCYID) FROM ["+plant+"_RECVDET] R WHERE ITEM=B.ITEM AND CURRENCYID IS NOT NULL AND tran_type IN('IB','GOODSRECEIPTWITHBATCH') "+sRecvCondition+")>0  THEN ");
		sql.append(" (Select ISNULL(CAST(ISNULL(SUM(CASE WHEN A.TRAN_TYPE='GOODSRECEIPTWITHBATCH' THEN 0 ELSE A.UNITCOST END),0)/SUM(VC) AS DECIMAL(20,5)),0) AS AVERGAGE_COST from ");
		sql.append(" (select TRAN_TYPE,RECQTY VC,CAST("+ConvertUnitCostToOrderCurrency+" * CAST((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')AS DECIMAL(20,5)) ");
		sql.append(" / CAST((SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID=ISNULL(P.CURRENCYID,'"+baseCurrency+"')) AS DECIMAL(20,5)) ");
		sql.append("   * RECQTY AS DECIMAL(20,5)) AS UNITCOST ");
		sql.append("   from "+plant+"_RECVDET R LEFT JOIN ["+plant+"_POHDR] P ON R.PONO = P.PONO  where item =b.ITEM AND tran_type IN('IB','GOODSRECEIPTWITHBATCH') " + sRecvCondition +"  ) A)  ");
		sql.append("   ELSE   (SELECT CASE WHEN (SELECT COUNT(*) FROM ["+plant+"_RECVDET] WHERE ITEM=b.ITEM AND tran_type IN('INVENTORYUPLOAD') )>0 THEN (SELECT SUM(UNITCOST) FROM ["+plant+"_RECVDET] C JOIN ["+plant+"_invmst] D ON C.ITEM = D.ITEM and c.BATCH = D.UserFld4 and c.LOC=D.LOC where C.item = b.item AND tran_type IN('INVENTORYUPLOAD')) ELSE CAST(((B.COST)*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+baseCurrency+"')) AS DECIMAL(20,5))   END)   END ) AS AVERAGE_COST   ");
    
	    try {
            InvMstDAO _InvMstDAO = new InvMstDAO();
            _InvMstDAO.setmLogger(mLogger);
            sCondition = sCondition  + " group by a.item,b.itemdesc,b.stkuom,b.INVENTORYUOM,b.PURCHASEUOM ) B order by item";
            String aQuery = "SELECT "
     		+sql.toString()+" FROM (select b.PURCHASEUOM,a.ITEM,b.ITEMDESC,ISNULL((select ISNULL(u.CUOM,u.UOM) from "+ plant +"_UOM u where u.UOM='"+UOM+"'),'')as STKUOM, sum(QTY) QTY,ISNULL(CAST(((sum(b.UNITPRICE))*(SELECT CURRENCYUSEQT  FROM ["+plant+"_CURRENCYMST] WHERE  CURRENCYID='"+currencyid+"')) AS DECIMAL(20,4)),0) as LIST_PRICE,sum(b.COST) as COST," +INVENTORYUOM+","+ 
     		" ISNULL((select u.QPUOM from [" +plant+ "_UOM] u where u.UOM="+tableUOM+"),1) as UOMQTY    from "
            + "["
            + plant
            + "_"
            + "invmst"
            + "]"
            + " A,"
            + "["
            + plant
            + "_"
            + "itemmst"
            + "]"
            + " as b where a.item=b.item  and a.item in (SELECT ITEM FROM "
            + plant
            + "_ALTERNATE_ITEM_MAPPING WHERE ALTERNATE_ITEM_NAME like '"+ aItem + "%')"
            + "and QTY>0 ";
            arrList = _InvMstDAO.selectForReport(aQuery, ht,sCondition);
	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	    }
	    return arrList;
	}
	
	
	public ArrayList getInvListSummaryWithcostpriceinventory(Hashtable ht, String plant,String aItem,String productDesc,String currencyid,String baseCurrency,String loc,
			String PRD_CLS_ID,String PRD_TYPE_ID,String PRD_BRAND_ID,String PRD_DEPT_ID) throws Exception {
	    ArrayList arrList = new ArrayList();
	    String sCondition ="";
	
	    if (aItem == null) {
	    	aItem="";
	    }
	    if (productDesc == null) {
	    	productDesc="";
	    }
	    if (PRD_CLS_ID == null) {
	    	PRD_CLS_ID="";
	    }
	    if (PRD_TYPE_ID == null) {
	    	PRD_TYPE_ID="";
	    }
	    if (PRD_BRAND_ID == null) {
	    	PRD_BRAND_ID="";
	    }
	    if (PRD_DEPT_ID == null) {
	    	PRD_DEPT_ID="";
	    }
	    if (loc == null) {
	    	loc="";
	    }
	    
	    sCondition = sCondition+"AND ISNULL(IT.PRD_CLS_ID,'') LIKE '%"+PRD_CLS_ID+"%' AND ISNULL(IT.PRD_DEPT_ID,'') LIKE "
	    		+ " '%"+PRD_DEPT_ID+"%' AND ISNULL(PRD_DEPT_ID,'') LIKE '%"+loc+"%' AND ISNULL(PRD_BRAND_ID,'') LIKE '%"+PRD_BRAND_ID+"%'"
	    		+ " AND ISNULL(ITEMTYPE,'') LIKE '%"+PRD_TYPE_ID+"%' AND ISNULL(IT.ITEM,'') "
	    		+ "LIKE'%"+aItem+"%'"; 
	 	    
	    try {
	            InvMstDAO _InvMstDAO = new InvMstDAO();
	            _InvMstDAO.setmLogger(mLogger);
	            String aQuery = "SELECT IT.ID AS id, IT.PLANT AS plant,ISNULL(IT.ITEM, '') AS item, ISNULL(IT.ITEMDESC, '') AS itemDesc, "
	            		+ " ISNULL(IT.PRD_CLS_ID, '') AS category, ISNULL(IT.PRD_DEPT_ID, '') AS department, "
	            		+ " ISNULL(IT.PRD_BRAND_ID, '') AS brand, ISNULL(IT.ITEMTYPE, '') AS subCategory, ISNULL(IT.SALESUOM, '') AS salesUom, "
	            		+ " ISNULL(IT.PURCHASEUOM, '') AS purchaseUom, ISNULL(IT.INVENTORYUOM, '') AS inventoryUom, ISNULL(IT.COST, '') AS unitCost," 
	            		+ " ISNULL((SELECT SUM(QTY) FROM " +plant+ "_INVMST AS IV WHERE IV.ITEM = IT.ITEM GROUP BY IV.ITEM ), 0) AS stockQty, "
	            		+ " CASE WHEN (ISNULL(IT.ISCOMPRO,0) = 1) THEN "
	            		+ " ( CASE WHEN ((select TOP 1 "
	            		+ " ISNULL(SHOWPREVIOUSSALESCOST,0) AS FLAG from " +plant+ "_OUTBOUND_RECIPT_INVOICE_HDR WHERE PLANT=IT.PLANT AND ORDERTYPE='Outbound Order') = 1) THEN (SELECT "
	            		+ " SUM(PDMDT.UNITPRICE * PDM.QTY) FROM " +plant+ "_DODET AS PDMDT JOIN " +plant+ "_PROD_BOM_MST AS PDM ON PDM.CITEM=PDMDT.ITEM AND PDM.PITEM=IT.ITEM and PDM.BOMTYPE='KIT'"
	            		+ "  WHERE PDMDT.ID IN (ISNULL((SELECT MAX(PDMDTI.ID) FROM " +plant+ "_DODET AS PDMDTI WHERE PDMDTI.ITEM=PDM.CITEM),0))) WHEN ((select TOP 1 "
	            		+ "  ISNULL(SHOWPREVIOUSSALESCOST,0) AS FLAG from " +plant+ "_OUTBOUND_RECIPT_INVOICE_HDR WHERE PLANT=IT.PLANT AND ORDERTYPE='Outbound Order') = 0) THEN ( SELECT "
	            		+ "  SUM(POMAVG.AVGE*POMAVG.QTY) FROM (SELECT (CASE WHEN IT.INCPRICEUNIT = '%' THEN (AVG.AVERAGE_COST + ((AVG.AVERAGE_COST/100)*IT.INCPRICE)) ELSE (AVG.AVERAGE_COST+ "
	            		+ "  IT.INCPRICE) END) AS AVGE,AVG.QTY FROM (SELECT (( (SELECT CASE WHEN (SELECT COUNT(CURRENCYID) FROM " +plant+ "_RECVDET R WHERE ITEM=PDM.CITEM AND CURRENCYID IS NOT "
	            		+ "  NULL AND tran_type IN( 'IB','GOODSRECEIPTWITHBATCH','INVENTORYUPLOAD','DE-KITTING','KITTING' ) )>0 THEN (Select ISNULL(CAST(ISNULL(SUM(CASE WHEN "
	            		+ "  A.TRAN_TYPE='GOODSRECEIPTWITHBATCH' THEN 0 ELSE A.UNITCOST END), 0)/SUM(VC) AS DECIMAL(20, 5)), 0) AS AVERGAGE_COST from (select TRAN_TYPE, RECQTY VC, CASE WHEN "
	            		+ "  TRAN_TYPE IN ('INVENTORYUPLOAD', 'DE-KITTING', 'KITTING') THEN (isnull(R.unitcost*(SELECT CURRENCYUSEQT FROM " +plant+ "_CURRENCYMST WHERE "
	            		+ "  CURRENCYID=ISNULL(P.CURRENCYID,'"+currencyid+"')), 0)*R.RECQTY) ELSE CAST( (CAST(ISNULL( ISNULL((select ISNULL(QPUOM, 1) from " +plant+ "_UOM where UOM=''), 1) * ( "
	            		+ "  ISNULL(((isnull(R.unitcost*(SELECT CURRENCYUSEQT FROM " +plant+ "_CURRENCYMST WHERE CURRENCYID=ISNULL(P.CURRENCYID,'"+currencyid+"')), 0)+ ISNULL((SELECT "
	            		+ " (SUM(E.QTY*LANDED_COST)/SUM(E.QTY)) FROM " +plant+ "_RECVDET c left join " +plant+ "_FINBILLHDR d on c.PONO = d.PONO and c.GRNO = d.GRNO left join " +plant+ "_FINBILLDET e "
	            		+ "  on d.ID = e.BILLHDRID where c.pono =R.pono and c.LNNO=R.LNNO and e.ITEM = PDM.CITEM OR c.TRAN_TYPE='GOODSRECEIPTWITHBATCH' AND c.item = PDM.CITEM), 0) + "
	            		+ "  (ISNULL(R.unitcost*(SELECT CURRENCYUSEQT FROM " +plant+ "_CURRENCYMST WHERE CURRENCYID=ISNULL(P.CURRENCYID,'"+currencyid+"')), 0) * (((ISNULL(P.LOCALEXPENSES, 0)+ CASE WHEN" 
	            		+ "  (SELECT SUM(LANDED_COST) FROM " +plant+ "_FINBILLHDR c join " +plant+ "_FINBILLDET d ON c.ID = d.BILLHDRID and c.PONO = R.PONO and d.LNNO = R.LNNO) is null THEN "
	            		+ "  P.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from " +plant+ "_podet s where s.pono=R.pono), 0)), 0))/100))/ (SELECT "
	            		+ "  CURRENCYUSEQT FROM " +plant+ "_CURRENCYMST WHERE CURRENCYID=ISNULL(P.CURRENCYID,'"+currencyid+"'))), 0)) , 0) *(SELECT CURRENCYUSEQT FROM " +plant+ "_CURRENCYMST WHERE "
	            		+ "  CURRENCYID='"+currencyid+"')*(SELECT CURRENCYUSEQT FROM " +plant+ "_CURRENCYMST WHERE CURRENCYID=ISNULL(P.CURRENCYID,'"+currencyid+"')) AS DECIMAL(20, 5)) ) * CAST((SELECT CURRENCYUSEQT" 
	            		+ "  FROM " +plant+ "_CURRENCYMST WHERE CURRENCYID='"+currencyid+"')AS DECIMAL(20, 5)) / CAST((SELECT CURRENCYUSEQT FROM " +plant+ "_CURRENCYMST WHERE "
	            		+ "  CURRENCYID=ISNULL(P.CURRENCYID,'"+currencyid+"')) AS DECIMAL(20, 5)) * RECQTY AS DECIMAL(20, 5)) END AS UNITCOST from " +plant+ "_RECVDET R LEFT JOIN " +plant+ "_POHDR P ON R.PONO "
	            		+ "  = P.PONO where item =PDM.CITEM AND ISNULL(R.UNITCOST,0) != 0 AND tran_type IN('IB','GOODSRECEIPTWITHBATCH','INVENTORYUPLOAD','DE-KITTING','KITTING') ) A) ELSE ( "
	            		+ " SELECT CASE WHEN (SELECT COUNT(*) FROM " +plant+ "_RECVDET WHERE ITEM=PDM.CITEM AND tran_type IN( 'INVENTORYUPLOAD','DE-KITTING','KITTING' ) )>0 THEN (SELECT "
	            		+ "  ISNULL(SUM(UNITCOST), 0) FROM " +plant+ "_RECVDET C where item = PDM.CITEM AND ISNULL(C.UNITCOST,0) != 0 AND tran_type IN('INVENTORYUPLOAD','DE-KITTING','KITTING')) "
	            		+ "  ELSE CAST(((SELECT M.COST FROM " +plant+ "_ITEMMST M WHERE M.ITEM = PDM.CITEM)*(SELECT CURRENCYUSEQT FROM " +plant+ "_CURRENCYMST WHERE CURRENCYID='"+currencyid+"')) AS DECIMAL(20,"
	            		+ "  5)) END) END AS AVERAGE_COST)/ (SELECT ISNULL(U.QPUOM,'1') FROM " +plant+ "_ITEMMST AS I LEFT JOIN " +plant+ "_UOM AS U ON I.PURCHASEUOM = U.UOM WHERE I.ITEM=PDM.CITEM))"
	            		+ "  * (SELECT ISNULL(U.QPUOM,'1') FROM " +plant+ "_ITEMMST AS I LEFT JOIN " +plant+ "_UOM AS U ON I.INVENTORYUOM = U.UOM WHERE I. ITEM=PDM.CITEM)) AS AVERAGE_COST,PDM.QTY "
	            		+ "  FROM " +plant+ "_PROD_BOM_MST AS PDM WHERE PDM.PITEM=IT.ITEM AND PDM.BOMTYPE='KIT' ) AS AVG ) POMAVG) ELSE (SELECT SUM(LPS.UNITPRICE) FROM (SELECT CASE WHEN "
	            				 + "  ULP.INCPRICEUNIT = '%' THEN (ULP.UnitPrice + ((ULP.UnitPrice/100)*ULP.INCPRICE)) ELSE (ULP.UnitPrice + ULP.INCPRICE) END AS UNITPRICE FROM " +plant+ "_ITEMMST AS ULP "
	            		+ "  JOIN " +plant+ "_PROD_BOM_MST AS PDM ON PDM.CITEM=ULP.ITEM AND PDM.PITEM=IT.ITEM AND PDM.BOMTYPE='KIT') AS LPS) END ) "
	            		+ "  ELSE "
	            		+ "  ( CASE WHEN ((select TOP 1 "
	            		+ "  ISNULL(SHOWPREVIOUSSALESCOST,0) AS FLAG from " +plant+ "_OUTBOUND_RECIPT_INVOICE_HDR WHERE PLANT=IT.PLANT AND ORDERTYPE='Outbound Order') = 1) THEN (SELECT "
	            		+ "  DT.UNITPRICE FROM " +plant+ "_DODET AS DT WHERE DT.ID IN (SELECT MAX(DTI.ID) FROM " +plant+ "_DODET AS DTI WHERE DTI.ITEM=IT.ITEM)) WHEN ((select TOP 1 "
	            		+ "  ISNULL(SHOWPREVIOUSSALESCOST,0) AS FLAG from " +plant+ "_OUTBOUND_RECIPT_INVOICE_HDR WHERE PLANT=IT.PLANT AND ORDERTYPE='Outbound Order') = 0) THEN (SELECT (CASE WHEN"
	            		+ "  IT.INCPRICEUNIT = '%' THEN (AVG.AVERAGE_COST + ((AVG.AVERAGE_COST/100)*IT.INCPRICE)) ELSE (AVG.AVERAGE_COST+ IT.INCPRICE) END) AS AVGE FROM (SELECT (( (SELECT CASE "
	            		+ "  WHEN (SELECT COUNT(CURRENCYID) FROM " +plant+ "_RECVDET R WHERE ITEM=IT.ITEM AND CURRENCYID IS NOT NULL AND tran_type IN( "
	            		+ "  'IB','GOODSRECEIPTWITHBATCH','INVENTORYUPLOAD','DE-KITTING','KITTING' ) )>0 THEN (Select ISNULL(CAST(ISNULL(SUM(CASE WHEN A.TRAN_TYPE='GOODSRECEIPTWITHBATCH' THEN"
	            		+ "  0 ELSE A.UNITCOST END), 0)/SUM(VC) AS DECIMAL(20, 5)), 0) AS AVERGAGE_COST from (select TRAN_TYPE, RECQTY VC, CASE WHEN TRAN_TYPE IN ('INVENTORYUPLOAD', "
	            		+ "  'DE-KITTING', 'KITTING') THEN (isnull(R.unitcost*(SELECT CURRENCYUSEQT FROM " +plant+ "_CURRENCYMST WHERE CURRENCYID=ISNULL(P.CURRENCYID,'"+currencyid+"')), 0)*R.RECQTY) ELSE "
	            		+ "  CAST( (CAST(ISNULL( ISNULL((select ISNULL(QPUOM, 1) from " +plant+ "_UOM where UOM=''), 1) * ( ISNULL(((isnull(R.unitcost*(SELECT CURRENCYUSEQT FROM "
	            		+ "  " +plant+ "_CURRENCYMST WHERE CURRENCYID=ISNULL(P.CURRENCYID,'"+currencyid+"')), 0)+ ISNULL((SELECT (SUM(E.QTY*LANDED_COST)/SUM(E.QTY)) FROM " +plant+ "_RECVDET c left join "
	            				 + "  " +plant+ "_FINBILLHDR d on c.PONO = d.PONO and c.GRNO = d.GRNO left join " +plant+ "_FINBILLDET e on d.ID = e.BILLHDRID where c.pono =R.pono and c.LNNO=R.LNNO and "
	            		+ "  e.ITEM = IT.ITEM OR c.TRAN_TYPE='GOODSRECEIPTWITHBATCH' AND c.item = IT.ITEM), 0) + (ISNULL(R.unitcost*(SELECT CURRENCYUSEQT FROM " +plant+ "_CURRENCYMST WHERE "
	            		+ "  CURRENCYID=ISNULL(P.CURRENCYID,'"+currencyid+"')), 0) * (((ISNULL(P.LOCALEXPENSES, 0)+ CASE WHEN (SELECT SUM(LANDED_COST) FROM " +plant+ "_FINBILLHDR c join " +plant+ "_FINBILLDET"
	            		+ "  d ON c.ID = d.BILLHDRID and c.PONO = R.PONO and d.LNNO = R.LNNO) is null THEN P.SHIPPINGCOST ELSE 0 END)*100)/NULLIF((ISNULL((select "
	            		+ "  SUM(s.qtyor*s.UNITCOST*s.CURRENCYUSEQT) from " +plant+ "_podet s where s.pono=R.pono), 0)), 0))/100))/ (SELECT CURRENCYUSEQT FROM " +plant+ "_CURRENCYMST WHERE "
	            		+ "  CURRENCYID=ISNULL(P.CURRENCYID,'"+currencyid+"'))), 0)) , 0) *(SELECT CURRENCYUSEQT FROM " +plant+ "_CURRENCYMST WHERE CURRENCYID='"+currencyid+"')*(SELECT CURRENCYUSEQT FROM "
	            		+ "  " +plant+ "_CURRENCYMST WHERE CURRENCYID=ISNULL(P.CURRENCYID,'"+currencyid+"')) AS DECIMAL(20, 5)) ) * CAST((SELECT CURRENCYUSEQT FROM " +plant+ "_CURRENCYMST WHERE "
	            		+ "  CURRENCYID='"+currencyid+"')AS DECIMAL(20, 5)) / CAST((SELECT CURRENCYUSEQT FROM " +plant+ "_CURRENCYMST WHERE CURRENCYID=ISNULL(P.CURRENCYID,'"+currencyid+"')) AS DECIMAL(20, 5)) * RECQTY"
	            		+ "  AS DECIMAL(20, 5)) END AS UNITCOST from " +plant+ "_RECVDET R LEFT JOIN " +plant+ "_POHDR P ON R.PONO = P.PONO where item =IT.ITEM AND ISNULL(R.UNITCOST,0) != 0 AND "
	            		+ "  tran_type IN('IB','GOODSRECEIPTWITHBATCH','INVENTORYUPLOAD','DE-KITTING','KITTING') ) A) ELSE ( SELECT CASE WHEN (SELECT COUNT(*) FROM " +plant+ "_RECVDET WHERE "
	            		+ "  ITEM=IT.ITEM AND tran_type IN( 'INVENTORYUPLOAD','DE-KITTING','KITTING' ) )>0 THEN (SELECT ISNULL(SUM(UNITCOST), 0) FROM " +plant+ "_RECVDET C where item = IT.ITEM "
	            		+ "  AND ISNULL(C.UNITCOST,0) != 0 AND tran_type IN('INVENTORYUPLOAD','DE-KITTING','KITTING')) ELSE CAST(((SELECT M.COST FROM " +plant+ "_ITEMMST M WHERE M.ITEM = "
	            		+ "  IT.ITEM)*(SELECT CURRENCYUSEQT FROM " +plant+ "_CURRENCYMST WHERE CURRENCYID='"+currencyid+"')) AS DECIMAL(20, 5)) END) END AS AVERAGE_COST)/ (SELECT ISNULL(U.QPUOM,'1') FROM "
	            		+ "  " +plant+ "_ITEMMST AS I LEFT JOIN " +plant+ "_UOM AS U ON I.PURCHASEUOM = U.UOM WHERE I.ITEM=IT.ITEM)) * (SELECT ISNULL(U.QPUOM,'1') FROM " +plant+ "_ITEMMST AS I LEFT "
	            		+ "  JOIN " +plant+ "_UOM AS U ON I.SALESUOM = U.UOM WHERE I. ITEM=IT.ITEM)) AS AVERAGE_COST) AS AVG) ELSE (CASE WHEN IT.INCPRICEUNIT = '%' THEN (IT.UnitPrice + "
	            		+ "  ((IT.UnitPrice/100)*IT.INCPRICE)) ELSE (IT.UnitPrice + IT.INCPRICE) END) END ) "
	            		+ "  END AS unitPrice "
	            		+ "  FROM " +plant+ "_ITEMMST AS IT WHERE IT.PLANT = '" +plant+ "'"; 
	            arrList = _InvMstDAO.selectForReport(aQuery, ht,sCondition);

	    } catch (Exception e) {
	            this.mLogger.exception(this.printLog, "", e);
	    }
	    return arrList;
	}

}