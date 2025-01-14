package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.DoDetDAO;
import com.track.dao.VendMstDAO;
import com.track.util.MLogger;

public class VendUtil {

	private VendMstDAO vendDAO = new VendMstDAO();
	private MLogger mLogger = new MLogger();
	private boolean printLog = MLoggerConstant.VendUtil_PRINTPLANTMASTERLOG;

	public VendUtil() {
	}

	public boolean isExistsVendor(String vendor, String plant) throws Exception {
		boolean exists = false;
		try {
			vendDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.VENDNO, vendor);
			if (isExistsVendorMst(ht))
				exists = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return exists;
	}

	public boolean isExistsVendorMst(Hashtable ht) throws Exception {
		boolean exists = false;
		try {
			vendDAO.setmLogger(mLogger);
			if (vendDAO.getCountVendorMst(ht) > 0)
				exists = true;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return exists;
	}
	
	public ArrayList getNewSuppliers(String plant) throws Exception {
		ArrayList al = null;
		try {
			vendDAO.setmLogger(mLogger);
			al = vendDAO.getNewSuppliers(plant);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}
	
	public ArrayList getTopSupplier(String plant, String period, String fromDate, String toDate, String numberOfDecimal) throws Exception {
		ArrayList al = null;
		try {
			vendDAO.setmLogger(mLogger);
			al = vendDAO.getTopSupplier(plant, fromDate, toDate, numberOfDecimal);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}
	
	public ArrayList getPurchaseSummary(String plant, String period, String fromDate, String toDate, String numberOfDecimal) throws Exception {
		ArrayList al = null;
		String aQuery = "";
		try {
			if(period.equalsIgnoreCase("Last 30 days")) {
				aQuery ="with cte as" + 
						"  (" + 
						"  select CONVERT(DATE,getdate()) as   n" + 
						"  union all" + 
						"  select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)> DATEADD(month, -1, getdate())" + 
						"  )";
			} else if(period.equalsIgnoreCase("This Month")) {
				aQuery ="with cte as" + 
				" (" + 
				" select EOMONTH(GETDATE()) as   n" + 
				" union all" + 
				" select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)>= DATEADD(mm, DATEDIFF(mm, 0, GETDATE()), 0)" + 
				" )";
			} else if(period.equalsIgnoreCase("This quarter")) {
				aQuery ="with cte as" + 
						"  (" + 
						"  select getdate() as   n, datename(month,getdate()) as m" + 
						"  union all" + 
						"  select  dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(dd,-1,n)> DATEADD(month, -2, getdate())" + 
						"  )";
			} else if(period.equalsIgnoreCase("This year")) {
				aQuery =" with cte as" + 
						"  (" + 
						"  select DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1)   n, datename(month,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1)) as m" + 
						"  union all" + 
						"  select  dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(mm,-1,n)> DATEADD(year, 0, DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))" + 
						"  )";
			}  else if(period.equalsIgnoreCase("Last month")) {
				aQuery =" with cte as" + 
						"  (" + 
						"  select EOMONTH(GETDATE(),-1) as   n" + 
						"  union all" + 
						"  select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)>= dateadd(dd,1,EOMONTH(GETDATE(),-2)) " + 
						"  )";
			}  else if(period.equalsIgnoreCase("Last quarter")) {
				aQuery =" with cte as" + 
						"  (" + 
						"  select dateadd(MONTH,-3,getdate()) as   n, datename(month,dateadd(MONTH,-3,getdate()) ) as m" + 
						"  union all" + 
						"  select dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(MONTH,-1,n)>= DATEADD(month,-5,getdate())" + 
						"  )";
			}  else if(period.equalsIgnoreCase("Last year")) {
				aQuery =" with cte as" + 
						"  (" + 
						"  select DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)   n, datename(month,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) as m" + 
						"  union all" + 
						"  select dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(mm,-1,n)> DATEADD(year, -1, DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))" + 
						"  )";
			}
			
			if(period.equalsIgnoreCase("Last 30 days") || period.equalsIgnoreCase("This Month") || period.equalsIgnoreCase("Last month")) {
				aQuery += " SELECT CONVERT(nvarchar, n, 107) PURCHASE_DATE,ISNULL(TOTAL_COST,0) as TOTAL_COST from cte a left join (" + 
						" SELECT CONVERT(DATE, A.CollectionDate, 103) AS CDATE,SUM(CAST((UNITCOST * QTYOR) AS DECIMAL(18,"+numberOfDecimal+")) + CAST(ISNULL((((UNITCOST*INBOUND_GST)/100)*B.QTYOR),0)  AS DECIMAL(18,"+numberOfDecimal+")))" + 
						" AS TOTAL_COST FROM   ["+plant+"_POHDR] A JOIN ["+plant+"_PODET] B ON A.PONO=B.PONO  " + 
						" WHERE CONVERT(DATETIME, CollectionDate, 103) between '"+fromDate+"' and '"+toDate+"'" + 
						" GROUP BY CONVERT(DATE, A.CollectionDate, 103) )" + 
						" b on a.n = b.CDATE order by n";
			}else if(period.equalsIgnoreCase("This quarter") || period.equalsIgnoreCase("This year") || period.equalsIgnoreCase("Last quarter") || period.equalsIgnoreCase("Last year")){
				aQuery += " SELECT CONVERT(nvarchar, n, 107) PURCHASE_DATE,ISNULL(TOTAL_COST,0) as TOTAL_COST from cte a left join (" + 
						" SELECT datename(month, (CONVERT(DATE, A.CollectionDate, 103))) AS CDATE,SUM(CAST((UNITCOST * QTYOR) AS DECIMAL(18,"+numberOfDecimal+")) + CAST(ISNULL((((UNITCOST*INBOUND_GST)/100)*B.QTYOR),0)  AS DECIMAL(18,"+numberOfDecimal+")))" + 
						" AS TOTAL_COST FROM   ["+plant+"_POHDR] A JOIN ["+plant+"_PODET]B ON A.PONO=B.PONO " + 
						" WHERE CONVERT(DATETIME, CollectionDate, 103) between '"+fromDate+"' and '"+toDate+"'" + 
						" GROUP BY datename(month, (CONVERT(DATE, A.CollectionDate, 103))) )" + 
						" b on a.m = b.CDATE order by n";
			}
			al = vendDAO.selectForReport(aQuery);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}
	
	public ArrayList getPurchaseDeliveryDate(String plant) throws Exception {
		ArrayList al = null;
		try {
			vendDAO.setmLogger(mLogger);
			al = vendDAO.getPurchaseDeliveryDate(plant);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}
	
	public ArrayList getExpiredPurchaseOrder(String plant) throws Exception {
		ArrayList al = null;
		try {
			vendDAO.setmLogger(mLogger);
			al = vendDAO.getExpiredPurchaseOrder(plant);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}

	public ArrayList getPoWoPriceSummary(String plant, String period, String fromDate, String toDate, String numberOfDecimal) throws Exception {
		ArrayList al = null;
		String aQuery = "";
		try {
			if(period.equalsIgnoreCase("Last 30 days")) {
				aQuery ="with cte as" + 
						"  (" + 
						"  select CONVERT(DATE,getdate()) as   n" + 
						"  union all" + 
						"  select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)> DATEADD(month, -1, getdate())" + 
						"  )";
			} else if(period.equalsIgnoreCase("This Month")) {
				aQuery ="with cte as" + 
				" (" + 
				" select EOMONTH(GETDATE()) as   n" + 
				" union all" + 
				" select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)>= DATEADD(mm, DATEDIFF(mm, 0, GETDATE()), 0)" + 
				" )";
			} else if(period.equalsIgnoreCase("This quarter")) {
				aQuery ="with cte as" + 
						"  (" + 
						"  select getdate() as   n, datename(month,getdate()) as m" + 
						"  union all" + 
						"  select  dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(dd,-1,n)> DATEADD(month, -2, getdate())" + 
						"  )";
			} else if(period.equalsIgnoreCase("This year")) {
				aQuery =" with cte as" + 
						"  (" + 
						"  select DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1)   n, datename(month,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1)) as m" + 
						"  union all" + 
						"  select  dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(mm,-1,n)> DATEADD(year, 0, DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))" + 
						"  )";
			}  else if(period.equalsIgnoreCase("Last month")) {
				aQuery =" with cte as" + 
						"  (" + 
						"  select EOMONTH(GETDATE(),-1) as   n" + 
						"  union all" + 
						"  select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)>= dateadd(dd,1,EOMONTH(GETDATE(),-2)) " + 
						"  )";
			}  else if(period.equalsIgnoreCase("Last quarter")) {
				aQuery =" with cte as" + 
						"  (" + 
						"  select dateadd(MONTH,-3,getdate()) as   n, datename(month,dateadd(MONTH,-3,getdate()) ) as m" + 
						"  union all" + 
						"  select dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(MONTH,-1,n)>= DATEADD(month,-5,getdate())" + 
						"  )";
			}  else if(period.equalsIgnoreCase("Last year")) {
				aQuery =" with cte as" + 
						"  (" + 
						"  select DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)   n, datename(month,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) as m" + 
						"  union all" + 
						"  select dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(mm,-1,n)> DATEADD(year, -1, DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))" + 
						"  )";
			}
			
			if(period.equalsIgnoreCase("Last 30 days") || period.equalsIgnoreCase("This Month") || period.equalsIgnoreCase("Last month")) {
				aQuery += " SELECT CONVERT(nvarchar, n, 107) PURCHASE_DATE,CAST(ISNULL(TOTAL_RECV_QTY,0)  AS DECIMAL(18,3)) as TOTAL_RECV_QTY from cte a left join (" + 
						" SELECT CONVERT(DATE, A.CollectionDate, 103) AS CDATE, SUM(QTYOR) AS TOTAL_RECV_QTY" + 
						" FROM   ["+plant+"_POHDR] A JOIN ["+plant+"_PODET] B ON A.PONO=B.PONO  " + 
						" WHERE CONVERT(DATETIME, CollectionDate, 103) between '"+fromDate+"' and '"+toDate+"'" + 
						" GROUP BY CONVERT(DATE, A.CollectionDate, 103) )" + 
						" b on a.n = b.CDATE order by n";
			}else if(period.equalsIgnoreCase("This quarter") || period.equalsIgnoreCase("This year") || period.equalsIgnoreCase("Last quarter") || period.equalsIgnoreCase("Last year")){
				aQuery += " SELECT CONVERT(nvarchar, n, 107) PURCHASE_DATE,CAST(ISNULL(TOTAL_RECV_QTY,0)  AS DECIMAL(18,3)) as TOTAL_RECV_QTY from cte a left join (" + 
						" SELECT datename(month, (CONVERT(DATE, A.CollectionDate, 103))) AS CDATE, SUM(QTYOR) AS TOTAL_RECV_QTY" + 
						" FROM   ["+plant+"_POHDR] A JOIN ["+plant+"_PODET]B ON A.PONO=B.PONO " + 
						" WHERE CONVERT(DATETIME, CollectionDate, 103) between '"+fromDate+"' and '"+toDate+"'" + 
						" GROUP BY datename(month, (CONVERT(DATE, A.CollectionDate, 103))) )" + 
						" b on a.m = b.CDATE order by n";
			}
			al = vendDAO.selectForReport(aQuery);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}
	
	public ArrayList getGrnSummaryDashboard(String plant, String period, String fromDate, String toDate, String numberOfDecimal) throws Exception {
		ArrayList al = null;
		String aQuery = "";
		try {
			if(period.equalsIgnoreCase("Last 30 days")) {
				aQuery ="with cte as" + 
						"  (" + 
						"  select CONVERT(DATE,getdate()) as   n" + 
						"  union all" + 
						"  select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)> DATEADD(month, -1, getdate())" + 
						"  )";
			} else if(period.equalsIgnoreCase("This Month")) {
				aQuery ="with cte as" + 
				" (" + 
				" select EOMONTH(GETDATE()) as   n" + 
				" union all" + 
				" select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)>= DATEADD(mm, DATEDIFF(mm, 0, GETDATE()), 0)" + 
				" )";
			} else if(period.equalsIgnoreCase("This quarter")) {
				aQuery ="with cte as" + 
						"  (" + 
						"  select getdate() as   n, datename(month,getdate()) as m" + 
						"  union all" + 
						"  select  dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(dd,-1,n)> DATEADD(month, -2, getdate())" + 
						"  )";
			} else if(period.equalsIgnoreCase("This year")) {
				aQuery =" with cte as" + 
						"  (" + 
						"  select DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1)   n, datename(month,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1)) as m" + 
						"  union all" + 
						"  select  dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(mm,-1,n)> DATEADD(year, 0, DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))" + 
						"  )";
			}  else if(period.equalsIgnoreCase("Last month")) {
				aQuery =" with cte as" + 
						"  (" + 
						"  select EOMONTH(GETDATE(),-1) as   n" + 
						"  union all" + 
						"  select  dateadd(DAY,-1,n) from cte where dateadd(dd,-1,n)>= dateadd(dd,1,EOMONTH(GETDATE(),-2)) " + 
						"  )";
			}  else if(period.equalsIgnoreCase("Last quarter")) {
				aQuery =" with cte as" + 
						"  (" + 
						"  select dateadd(MONTH,-3,getdate()) as   n, datename(month,dateadd(MONTH,-3,getdate()) ) as m" + 
						"  union all" + 
						"  select dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(MONTH,-1,n)>= DATEADD(month,-5,getdate())" + 
						"  )";
			}  else if(period.equalsIgnoreCase("Last year")) {
				aQuery =" with cte as" + 
						"  (" + 
						"  select DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)   n, datename(month,DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1)) as m" + 
						"  union all" + 
						"  select dateadd(MONTH,-1,n), datename(month,dateadd(MONTH,-1,n)) as m from cte where dateadd(mm,-1,n)> DATEADD(year, -1, DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0))" + 
						"  )";
			}
			
			if(period.equalsIgnoreCase("Last 30 days") || period.equalsIgnoreCase("This Month") || period.equalsIgnoreCase("Last month")) {
				aQuery += " SELECT CONVERT(nvarchar, n, 107) RECVDATE,CAST(ISNULL(TOTAL_RECV_QTY,0)  AS DECIMAL(18,3)) as TOTAL_RECV_QTY from cte a left join (" + 
						" SELECT CONVERT(DATE, RECVDATE, 103) AS RECVDATE, SUM(B.QTY) TOTAL_RECV_QTY " + 
						" FROM   ["+plant+"_RECVDET] A JOIN ["+plant+"_FINGRNOTOBILL] B ON A.GRNO=B.GRNO  " + 
						" WHERE CONVERT(DATETIME, RECVDATE, 103) between '"+fromDate+"' and '"+toDate+"'" + 
						" GROUP BY CONVERT(DATE, RECVDATE, 103) )" + 
						" b on a.n = b.RECVDATE order by n";
			}else if(period.equalsIgnoreCase("This quarter") || period.equalsIgnoreCase("This year") || period.equalsIgnoreCase("Last quarter") || period.equalsIgnoreCase("Last year")){
				aQuery += " SELECT CONVERT(nvarchar, n, 107) RECVDATE,CAST(ISNULL(TOTAL_RECV_QTY,0)  AS DECIMAL(18,3)) as TOTAL_RECV_QTY from cte a left join (" + 
						" SELECT datename(month, (CONVERT(DATE, RECVDATE, 103))) AS RECVDATE, SUM(B.QTY) TOTAL_RECV_QTY " + 
						" FROM   ["+plant+"_RECVDET] A JOIN ["+plant+"_FINGRNOTOBILL] B ON A.GRNO=B.GRNO " + 
						" WHERE CONVERT(DATETIME, RECVDATE, 103) between '"+fromDate+"' and '"+toDate+"'" + 
						" GROUP BY datename(month, (CONVERT(DATE, RECVDATE, 103))) )" + 
						" b on a.m = b.RECVDATE order by n";
			}
			al = vendDAO.selectForReport(aQuery);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return al;
	}
}