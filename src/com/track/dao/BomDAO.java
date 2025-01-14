package com.track.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import net.sf.json.JSONObject;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
import com.track.util.MLogger;
import com.track.util.StrUtils;


public class BomDAO extends BaseDAO {
	private MLogger mLogger = new MLogger();
	public static final String TABLE_NAME = "BOM";
	private boolean printQuery = MLoggerConstant.INVMSTDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.INVMSTDAO_PRINTPLANTMASTERLOG;

	
	public boolean isPrintQuery() {
		return printQuery;
	}

	public void setPrintQuery(boolean printQuery) {
		this.printQuery = printQuery;
	}

	public boolean isPrintLog() {
		return printLog;
	}

	public void setPrintLog(boolean printLog) {
		this.printLog = printLog;
	}

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	@SuppressWarnings("unchecked")
	public ArrayList getDetails(String sqlQuery, Hashtable condition,
			String extraCondition) throws Exception {
		Connection con = null;
		ArrayList al = new ArrayList();
		try {
			con = DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT ");
			sql.append(sqlQuery);
			sql.append(" ");
			sql.append(" FROM [" + condition.get("PLANT") + "_BOM] ");
			sql.append(" WHERE ");
			String conditon = formCondition(condition);
			sql.append(conditon);
			if ((extraCondition != null) && (extraCondition.length() > 0)) {
				sql.append(" AND " + extraCondition);
			}
			this.mLogger.query(true, sql.toString());
			al = selectData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.exception(true, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return al;
	}

	@SuppressWarnings("unchecked")
	public Map selectRow(String query, Hashtable ht) throws Exception {
		Map map = new HashMap();
		Connection con = null;
		try {

			con = DbBean.getConnection();
			// SELECT ID ,CUSTOMER_CODE ,CUSTOMER_LOCATION_CODE ,BATCH_NO
			// ,STATUS FROM wks_ECRCYLINDER
			StringBuffer sql = new StringBuffer(" SELECT " + query + " FROM ["
					+ ht.get("PLANT") + "_BOM] ");
			sql.append(" WHERE ");
			String conditon = formCondition(ht);
			sql.append(conditon);
			this.mLogger.query(true, sql.toString());
			map = getRowOfData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(true, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}

		return map;
	}

	@SuppressWarnings("unchecked")
	public boolean isExisit(Hashtable ht) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT COUNT(*) FROM ["
					+ ht.get("PLANT") + "_BOM] ");
			sql.append(" WHERE  " + formCondition(ht));

			this.mLogger.query(true, sql.toString());
			flag = isExists(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(true, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;

	}
	
	
	@SuppressWarnings("unchecked")
	public boolean isExisitQty(Hashtable ht) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT COUNT(*) FROM ["
					+ ht.get("PLANT") + "_BOM] ");
			sql.append(" WHERE  QTY > 0 AND " + formCondition(ht));

			this.mLogger.query(true, sql.toString());
			flag = isExists(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(true, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;

	}
	
	public boolean isExisitInvQty(Hashtable ht) throws Exception {
		boolean flag = false;
		java.sql.Connection con = null;
		try {
			con = com.track.gates.DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" SELECT COUNT(*) FROM ["
					+ ht.get("PLANT") + "_BOM] ");
			sql.append(" WHERE  " + formCondition(ht));

			this.mLogger.query(true, sql.toString());
			flag = isExists(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(true, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return flag;

	}
	
	
	

	@SuppressWarnings("unchecked")
	public boolean insert(Hashtable ht) throws Exception {
		boolean insertFlag = false;
		Connection conn = null;
		try {
			conn = DbBean.getConnection();
			String FIELDS = "", VALUES = "";
			Enumeration enum1 = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enum1.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				FIELDS += key + ",";
				VALUES += "'" + value + "',";
			}
			String query = "INSERT INTO " + "[" + ht.get("PLANT") + "_BOM"
					+ "] " + "(" + FIELDS.substring(0, FIELDS.length() - 1)
					+ ") VALUES (" + VALUES.substring(0, VALUES.length() - 1)
					+ ")";
			this.mLogger.query(true, query);

			insertFlag = insertData(conn, query);

		} catch (Exception e) {
			this.mLogger.exception(true, "", e);
			throw e;
		} finally {
			if (conn != null) {
				DbBean.closeConnection(conn);
			}
		}
		return insertFlag;
	}

	@SuppressWarnings("unchecked")
	public boolean update(String query, Hashtable htCondition, String extCond)
			throws Exception {
		boolean flag = false;
		Connection con = null;
		try {
			con = DbBean.getConnection();

			StringBuffer sql = new StringBuffer(" UPDATE " + "["
					+ htCondition.get("PLANT") + "_BOM]");
			sql.append(" ");
			sql.append(query);
			sql.append(" WHERE ");

			String conditon = formCondition(htCondition);

			sql.append(conditon);

			if (extCond.length() != 0) {
				sql.append(extCond);
			}
			this.mLogger.query(true, sql.toString());
			flag = updateData(con, sql.toString());

		} catch (Exception e) {
			this.mLogger.exception(true, "", e);
			throw e;
		} finally {
			DbBean.closeConnection(con);
		}
		return flag;
	}

	@SuppressWarnings("unchecked")
	public boolean delete(Hashtable ht) throws Exception {
		boolean delete = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			StringBuffer sql = new StringBuffer(" DELETE ");
			sql.append(" ");
			sql.append(" FROM " + "[" + ht.get("PLANT") + "_BOM]");
			sql.append(" WHERE " + formCondition(ht));
			this.mLogger.query(true, sql.toString());
			
			delete = updateData(con, sql.toString());
			
		} catch (Exception e) {
			this.mLogger.exception(true, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return delete;
	}
	
	public boolean deleteWMSDekitting(Hashtable ht,String parentitem,String loc,String parentbatch) throws Exception {
		boolean delete = false;
		boolean deleteBOMTEMP = false;
		java.sql.Connection con = null;
		try {
			con = DbBean.getConnection();
			 CallableStatement cs=null;
			 
			 
			 cs = con.prepareCall("{call MULTI_COMP_INSERT_KITTING_TEMP_TABLE(?,?,?,?)}");
		     cs.setString(1,(String) ht.get("PLANT"));
	         cs.setString(2,parentitem);
	         cs.setString(3,loc);
	         cs.setString(4,parentbatch);
	         cs.executeUpdate();
	         
	         String childProduct ="", childLoc ="",childBatch="",qty="";
	         ArrayList list= this.listBomTempTable( (String)ht.get("PLANT"),parentitem,loc,parentbatch);
	         if (list.size() > 0) {
	         for (int iCnt =0; iCnt<list.size(); iCnt++){
     			int id=iCnt;
                  String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                 Map lineList = (Map) list.get(iCnt);
                 childProduct = StrUtils.fString((String)lineList.get("CHILD_PRODUCT")) ;
                 childLoc = loc ;
                 childBatch =  StrUtils.fString((String)lineList.get("CHILD_PRODUCT_BATCH"));
                 qty = StrUtils.fString((String)lineList.get("QTY"));
                               
                 cs = con.prepareCall("{call MULTI_COMP_UPDATE_INVMST_IMPORT_KITTINGDATA(?,?,?,?,?)}");
    		     cs.setString(1,(String) ht.get("PLANT"));
    	         cs.setString(2,childProduct);
    	         cs.setString(3,loc);
    	         cs.setString(4, childBatch);
    	         cs.setString(5, qty);
    	         cs.executeUpdate();
                        
	         }
	        }
				 
	         //Thread.sleep(10000); 
	         StringBuffer sql1 = new StringBuffer(" DELETE ");
			 sql1.append(" ");
			 sql1.append(" FROM " + "[" + ht.get("PLANT") + "_BOM_TEMP]");
			 sql1.append(" WHERE " + formCondition(ht));
			 this.mLogger.query(true, sql1.toString());
			 deleteBOMTEMP = updateData(con, sql1.toString());
			 
			 StringBuffer sql = new StringBuffer(" DELETE ");
			 sql.append(" ");
			 sql.append(" FROM " + "[" + ht.get("PLANT") + "_BOM]");
			 sql.append(" WHERE " + formCondition(ht));
			 this.mLogger.query(true, sql.toString());
			
			delete = updateData(con, sql.toString());
			
		} catch (Exception e) {
			this.mLogger.exception(true, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
		return delete;
	}
	
	public ArrayList listBomTempTable(String plant,String parentitem,String loc,String parentbatch)
		    throws Exception {

				Connection con = null;
				ArrayList al = new ArrayList();
				
				try {
					con = DbBean.getConnection();

					StringBuffer sQry = new StringBuffer(
							 "select PARENT_PRODUCT,PARENT_PRODUCT_LOC,PARENT_PRODUCT_BATCH,CHILD_PRODUCT,CHILD_PRODUCT_BATCH,QTY"
		          	 			+"  from "+plant+"_BOM_TEMP "	
								+ " where PARENT_PRODUCT = '" + parentitem + "' and PARENT_PRODUCT_LOC ='"
								+ loc
								+ "' and PARENT_PRODUCT_BATCH='"
								+parentbatch + "'");
									
					

					this.mLogger.query(this.printQuery, sQry.toString());

					al = selectData(con, sQry.toString());

				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					throw e;
				} finally {
					if (con != null) {
						DbBean.closeConnection(con);
					}
				}

				return al;
			}
	
	public String getChildDeKittingItem(String plant, String parentitem,String childitem) {
		boolean insertFlag = false;
		Connection conn = null;
		try {
			conn = DbBean.getConnection();

			String query = " SELECT CHILD_PRODUCT FROM [" + plant + "_"
					+ "BOM] WHERE PLANT='" + plant
					+ "' AND PARENT_PRODUCT='" +parentitem 
					+ "' AND CHILD_PRODUCT='" +childitem + "' ";
			
		

			//this.mLogger.query(this.printQuery, query);
			Map m = getRowOfData(conn, query);
			
			return (String) m.get("CHILD_PRODUCT");
		} catch (Exception e) {
			// this.mLogger.exception(this.printLog, "", e);
			return null;
		} finally {
			if (conn != null){
				DbBean.closeConnection(conn);
			}
		}

	}
		
	public String getChildDeKittingBatch(String aPlant, String aParentItem,String aChildItem,String aLoc,String aBatch,String aParentBatch) throws Exception {
		String batch = "";

		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);
		ht.put("parent_product", aParentItem);
		ht.put("parent_product_batch", aParentBatch);
		ht.put("child_product", aChildItem);
		ht.put("child_product_loc", aLoc);
		ht.put("child_product_batch", aBatch);
		
				
		String query = " isnull(CHILD_PRODUCT_BATCH,'') as Batch ";
		
		

		Map m = selectRow(query, ht, "");
		if (m.size() > 0) {
			batch = (String) m.get("Batch");
		} else {
			batch = "";
		}
		if (batch.equalsIgnoreCase(null) || batch.length() == 0) {
			batch = "";
		}
		return batch;
	}
	
	
	public String getChildDeKittingQty(String aPlant, String aParentItem,String  aChildItem ,String aLoc,String aBatch,String aParentBatch) throws Exception {
		String qty = "";

		Hashtable ht = new Hashtable();
		ht.put("plant", aPlant);
		ht.put("parent_product", aParentItem);
		ht.put("parent_product_batch", aParentBatch);
		ht.put("child_product", aChildItem);
		ht.put("child_product_loc", aLoc);
		ht.put("child_product_batch", aBatch);
		
		String query = " isnull(qty,0) qty ";

		Map m = selectRow(query, ht, "");
		if (m.size() > 0) {
			qty = (String) m.get("qty");
		} else {
			qty= "";
		}
		if (qty.equalsIgnoreCase(null) || qty.length() == 0) {
			qty = "";
		}
		return qty;
	}
	
	public Map selectRow(String query, Hashtable ht, String extCond)
	throws Exception {
        Map map = new HashMap();
        
        java.sql.Connection con = null;
        try {
        
                con = com.track.gates.DbBean.getConnection();
                StringBuffer sql = new StringBuffer(" SELECT " + query + " from "
                                + "[" + ht.get("plant") + "_" + "BOM" + "]");
                sql.append(" WHERE ");
                String conditon = formCondition(ht);
                sql.append(conditon);
                if (extCond.length() > 0)
                        sql.append(" and " + extCond);
                this.mLogger.query(this.printQuery, sql.toString());
                
        
                
                map = getRowOfData(con, sql.toString());
                
                } catch (Exception e) {
                        this.mLogger.exception(this.printLog, "", e);
                        throw e;
                } finally {
                        if (con != null) {
                                DbBean.closeConnection(con);
                        }
                }
                return map;
        }
	
	public ArrayList selectForReport(String query, Hashtable ht, String extCond)
		throws Exception {
	boolean flag = false;
	ArrayList al = new ArrayList();
	java.sql.Connection con = null;
	try {
		con = com.track.gates.DbBean.getConnection();
		StringBuffer sql = new StringBuffer(query);
		String conditon = "";
		if (ht.size() > 0) {
			sql.append(" AND ");
			conditon = formCondition(ht);
			sql.append(" " + conditon);
		}
	
		if (extCond.length() > 0) {
			sql.append("  ");
	
			sql.append(" " + extCond);
		}
	
		this.mLogger.query(this.printQuery, sql.toString());
		al = selectData(con, sql.toString());
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
	return al;
   }

	public ArrayList getKittingItemByWMS(String plant, String item,String afrmDate, String atoDate,String atoSort)
	throws Exception {
	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
	    con = com.track.gates.DbBean.getConnection();
		boolean flag = false;
		String sCondition="";
		/*if(atoSort.equals("ISSUEDPRODUCT"))
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
		}else*/ if(atoSort.equals("AVAILABLEINV"))
		{
			sCondition = sCondition + " AND STATUS='A' ";
		}
		String sQry = "select distinct(parent_product) item from " 
			    + "["
				+ plant
				+ "_" 
				+ "bom"
				+ "] "
				+ "  where parent_product like '"+ item	+"%'"+ sCondition
				+ " ORDER BY parent_product ";
		this.mLogger.query(this.printQuery, sQry);
		al = selectData(con, sQry);
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return al;
   }

	
	public ArrayList getKittingParentBatchByWMS(String plant, String item,String afrmDate, String atoDate,String pbatch)
	throws Exception {

	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
	  	con = com.track.gates.DbBean.getConnection();
		
		boolean flag = false;
		String sCondition="";
		
		if (afrmDate.length() > 0) {
			sCondition = sCondition + " AND CREATE_AT  >= '" + afrmDate
					+ "'  ";
			if (atoDate.length() > 0) {
				sCondition = sCondition + " AND CREATE_AT <= '" + atoDate
						+ "'  ";
			}
		} else {
			if (atoDate.length() > 0) {
				sCondition = sCondition + " AND CREATE_AT  <= '" + atoDate
						+ "'  ";
			}
		}
	
		String sQry = "select distinct(parent_product_batch) Batch,isnull(parent_product_qty,1)Qty from " 
			    + "["
				+ plant
				+ "_" 
				+ "bom" 
				+ "] "
				+ "  where parent_product= '"
				+ item
				//+"' and parent_product_batch like '" + pbatch
				+ "'" ;//  +  sCondition;
		
		

	
		this.mLogger.query(this.printQuery, sQry);
	
		al = selectData(con, sQry);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return al;
   }
	
	public ArrayList getKittingChildDetailsBatchByWMS(String plant, String item,String afrmDate, String atoDate,String cbatch)
			throws Exception {

			java.sql.Connection con = null;
			ArrayList al = new ArrayList();
			try {
			  	con = com.track.gates.DbBean.getConnection();
				
				boolean flag = false;
				String sCondition="";
				
				if (afrmDate.length() > 0) {
					sCondition = sCondition + " AND CREATE_AT  >= '" + afrmDate
							+ "'  ";
					if (atoDate.length() > 0) {
						sCondition = sCondition + " AND CREATE_AT <= '" + atoDate
								+ "'  ";
					}
				} else {
					if (atoDate.length() > 0) {
						sCondition = sCondition + " AND CREATE_AT  <= '" + atoDate
								+ "'  ";
					}
				}
			
				String sQry = "select distinct(child_product_batch) Batch,isnull(qty,1)Qty from " 
					    + "["
						+ plant
						+ "_" 
						+ "bom" 
						+ "] "
						+ "  where child_product= '"
						+ item
						//+"' and parent_product_batch like '" + pbatch
						+ "'" ;//  +  sCondition;
				
				

			
				this.mLogger.query(this.printQuery, sQry);
			
				al = selectData(con, sQry);

			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				throw e;
			} finally {
				if (con != null) {
					DbBean.closeConnection(con);
				}
			}
			return al;
		   }
	
	public ArrayList getKittingChildItemByWMS(String plant, String pitem,String pbatch,String citem,String afrmDate, String atoDate)
	throws Exception {

	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
	      /*  if(itemDesc.length()>0){
	            itemDesc = " AND altitem.ITEM in (SELECT ITEM FROM "+plant+"_ITEMMST WHERE ITEMDESC Like '%"+new StrUtils().InsertQuotes(itemDesc)+"%')";
	        }*/
		con = com.track.gates.DbBean.getConnection();
		
		boolean flag = false;
		String sCondition="";
		
		if (afrmDate.length() > 0) {
			sCondition = sCondition + " AND CREATE_AT >= '" + afrmDate
					+ "'  ";
			if (atoDate.length() > 0) {
				sCondition = sCondition + " AND CREATE_AT <= '" + atoDate
						+ "'  ";
			}
		} else {
			if (atoDate.length() > 0) {
				sCondition = sCondition + " AND CREATE_AT  <= '" + atoDate
						+ "'  ";
			}
		}
	
		String sQry = "select distinct(child_product) item from " 
			    + "["
				+ plant
				+ "_" 
				+ "bom" 
				+ "] "
				+ "  where child_product like '"
				+ citem
				+ "%' and parent_product =  '" + pitem 
				+ "' and parent_product_batch  like  '" + pbatch
				+"%' " // + sCondition
				+ " ORDER BY child_product ";
		
		
		
				
	
		this.mLogger.query(this.printQuery, sQry);
	
		al = selectData(con, sQry);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return al;
   }
	
	public ArrayList getKittingChildBatchByWMS(String plant, String pitem,String pbatch,String citem,String cbatch,String afrmDate, String atoDate)
	throws Exception {

	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
	      /*  if(itemDesc.length()>0){
	            itemDesc = " AND altitem.ITEM in (SELECT ITEM FROM "+plant+"_ITEMMST WHERE ITEMDESC Like '%"+new StrUtils().InsertQuotes(itemDesc)+"%')";
	        }*/
		con = com.track.gates.DbBean.getConnection();
		String sCondition="";
		
		
		
		boolean flag = false;
		
		if (afrmDate.length() > 0) {
			sCondition = sCondition + " AND CREATE_AT  >= '" + afrmDate
					+ "'  ";
			if (atoDate.length() > 0) {
				sCondition = sCondition + " AND CREATE_AT <= '" + atoDate
						+ "'  ";
			}
		} else {
			if (atoDate.length() > 0) {
				sCondition = sCondition + " AND CREATE_AT  <= '" + atoDate
						+ "'  ";
			}
		}
		// query
	
		String sQry = "select distinct(child_product_batch) Batch from " 
			    + "["
				+ plant
				+ "_" 
				+ "bom" 
				+ "] "
				+ "  where child_product= '"
				+ citem
				+ "' and parent_product =  '" + pitem 
				+ "' and parent_product_batch  =  '" + pbatch
				+ "' and child_product_batch  like  '" + cbatch
				+"%' "  ;//+ sCondition;
		
	
		
		
		
				
	
		this.mLogger.query(this.printQuery, sQry);
	
		al = selectData(con, sQry);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return al;
   }
		
	
	public ArrayList getKittingLocByWMS(String plant, String item,String loc)
	throws Exception {

	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();
	
		boolean flag = false;
		// query
	
		String sQry = "select distinct(parent_product_loc) loc from " + "["
		+ plant
		+ "_" + "bom" 
		+ "] "
		+ " where parent_product = '"
		+ item
		+ "' and parent_product_loc like '"+ loc 
		+ "%'";
		
			
		this.mLogger.query(this.printQuery, sQry);
	
		al = selectData(con, sQry);

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return al;
   }



public ArrayList getDeKittingParentBatch(String plant,String user,String parentloc,String parentproduct) throws Exception {

	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();   
						
		
		
		String sQry = "select distinct isnull(parent_product_batch,'') Batch from " + "["
		+ plant + "_" + "bom]  where plant='" + plant
		+ "'  and parent_product='" + parentproduct
		+ "'  and parent_product_loc='" + parentloc 
		+ "'  and qty>0   ";
		
		this.mLogger.query(this.printQuery, sQry);

		al = selectData(con, sQry);
		
	 	

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return al;
}

public ArrayList getDeKittingChildProducts(String plant,String user,String parentproduct,String parentloc,String parentbatch) throws Exception {

	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();   
						
		
		
		String sQry = "select distinct a.child_product as item, b.itemdesc  from " + "["
		+ plant + "_" + "bom] a, " + "[" + plant + "_" + "itemmst] b where a.plant='" + plant
		+ "'  and a.parent_product='" + parentproduct
		+ "'  and a.parent_product_loc='" + parentloc 
		+ "'  and parent_product_batch='" + parentbatch
		+ "'  and a.qty>0 and a.child_product=b.item ORDER BY a.child_product "; //and isnull(b.userfld1,'') not in('k')
		
		this.mLogger.query(this.printQuery, sQry);

		al = selectData(con, sQry);
		
		

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return al;
}



public ArrayList getDeKittingChildBatch(String plant,String user,String parentloc,String parentproduct,String childproduct,String parentbatch) throws Exception {

	java.sql.Connection con = null;
	ArrayList al = new ArrayList();
	try {
		con = com.track.gates.DbBean.getConnection();   
						
		
		
		String sQry = "select isnull(child_product_batch,'') Batch,qty from " + "["
		+ plant + "_" + "bom]  where plant='" + plant
		+ "'  and parent_product='" + parentproduct
		+ "'  and parent_product_loc='" + parentloc 
		+ "'  and parent_product_batch='" + parentbatch
		+ "'  and child_product='" + childproduct
		+ "'  and qty>0 ORDER BY child_product_batch ";
		
		this.mLogger.query(this.printQuery, sQry);

		al = selectData(con, sQry);
		
	 	

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return al;
}

public boolean isExisit(Hashtable ht, String extCond) throws Exception {
	boolean flag = false;
	java.sql.Connection con = null;
	try {
		con = com.track.gates.DbBean.getConnection();
		StringBuffer sql = new StringBuffer(" SELECT ");
		sql.append("COUNT(*) ");
		sql.append(" ");
		sql.append(" FROM " + "[" + ht.get("PLANT") + "_" + TABLE_NAME
				+ "]");
		sql.append(" WHERE  " + formCondition(ht));

		if (extCond.length() > 0)
			sql.append(" and " + extCond);

		this.mLogger.query(this.printQuery, sql.toString());
		flag = isExists(con, sql.toString());

	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
		throw e;
	} finally {
		if (con != null) {
			DbBean.closeConnection(con);
		}
	}
	return flag;

}

public int getCount(Hashtable ht) throws Exception {
	PreparedStatement ps = null;
	ResultSet rs = null;
	int iCount = 0;
	Connection con = null;
	try {
		con = DbBean.getConnection();
		String sQry = "SELECT COUNT(*) FROM ["+ ht.get("PLANT") + "_BOM]  where "+ formCondition(ht);
	    this.mLogger.query(this.printQuery, sQry);
		ps = con.prepareStatement(sQry);
		rs = ps.executeQuery();
		while (rs.next()) {
			iCount = rs.getInt(1);
		}
	} catch (Exception e) {
		this.mLogger.exception(this.printLog, "", e);
	} finally {
		DbBean.closeConnection(con, ps);
	}
	return iCount;

}


public String getChildDeKittingItemqtybyKONO(String plant, String kono,String childitem) {
	boolean insertFlag = false;
	Connection conn = null;
	String qty="0";
	try {
		conn = DbBean.getConnection();

		String query = " SELECT SUM(QTY) AS QTY FROM [" + plant + "_"
				+ "BOM] WHERE PLANT='" + plant
				+ "' AND KONO ='" +kono 
				+ "' AND CHILD_PRODUCT='" +childitem + "' ";
		
	

		//this.mLogger.query(this.printQuery, query);
		Map m = getRowOfData(conn, query);
		qty =(String) m.get("QTY");
		if(qty.length() == 0) {
			qty ="0";
		}
		return qty;
	} catch (Exception e) {
		// this.mLogger.exception(this.printLog, "", e);
		return null;
	} finally {
		if (conn != null){
			DbBean.closeConnection(conn);
		}
	}

}

}


