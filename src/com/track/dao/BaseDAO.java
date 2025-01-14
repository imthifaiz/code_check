package com.track.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.track.util.MLogger;
import com.track.util.StrUtils;

public class BaseDAO {
	protected StrUtils _StrUtils = null;

	public BaseDAO() {
		_StrUtils = new StrUtils();
	}

	public ArrayList<Map<String, String>> selectData(Connection conn, String query) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		Map<String, String> map = null;

		ArrayList<Map<String, String>> arrayList = new ArrayList<>();
		try {

			stmt = conn.prepareStatement(query);
			rs = stmt.executeQuery();
			while (rs.next()) {
				map = new HashMap<>();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {

					map
							.put(rs.getMetaData().getColumnLabel(i), rs
									.getString(i));
				}
				arrayList.add(map);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		return arrayList;
	}

	public boolean insertData(Connection conn, String query) throws Exception {

		boolean flag = false;
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		try {
			pStmt = conn.prepareStatement(query);
			int iCnt = pStmt.executeUpdate();
			if (iCnt > 0) {
				flag = true;
			}
		}

		catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pStmt != null) {
					pStmt.close();
				}
			} catch (Exception e) {
				throw e;
			}
		}

		return flag;
	}

	public boolean DeleteRow(Connection conn, String query) throws Exception {

		boolean flag = false;
		PreparedStatement pStmt = null;

		ResultSet rs = null;
		try {

			pStmt = conn.prepareStatement(query);
			int iCnt = pStmt.executeUpdate();

			if (iCnt > 0) {
				flag = true;

			}
		}

		catch (Exception e) {
			MLogger.log(0, "Exception" + e.getMessage());
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pStmt != null) {
					pStmt.close();
				}
			} catch (Exception e) {
				throw e;
			}
		}

		return flag;
	}

	public boolean updateData(Connection conn, String sql) throws Exception {		
		boolean flag = false;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int updateCount = 0;
		try {
			stmt = conn.prepareStatement(sql);
			updateCount = stmt.executeUpdate();
			if (updateCount > 0) {
				flag = true;

			}
			else
				throw new Exception("Unable to update!");
			
		} catch (Exception e) {
			MLogger.log(0, "Exception" + e.getMessage());
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
				throw e;
			}
		}
		return flag;
	}
	
	public boolean updateConData(Connection conn, String sql) throws Exception {		
		boolean flag = false;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int updateCount = 0;
		try {
			stmt = conn.prepareStatement(sql);
			updateCount = stmt.executeUpdate();
			if (updateCount > 0) {
				flag = true;

			}
			else
				throw new Exception("Unable to update!");
			
		} catch (Exception e) {
			MLogger.log(0, "Exception" + e.getMessage());
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
				throw e;
			}
		}
		return flag;
	}

	public Map<String, String> getRowOfData(Connection conn, String query) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;
		Map<String, String> map = new HashMap<>();

		try {

			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				map = new HashMap<>();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {

					map
							.put(rs.getMetaData().getColumnLabel(i), rs
									.getString(i));

				}

			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		return map;
	}

	public String formCondition(HashMap<String, String> hashMap)
			throws Exception {
		String sCondition = "";
		try {

			Set<String> keySet = hashMap.keySet();
			for (String stringKey : keySet) {
				String key = StrUtils.fString(stringKey);
				String value = StrUtils.fString(hashMap.get(key));
				sCondition = sCondition + " " + key.toUpperCase() + " = '"
						+ value.toUpperCase() + "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
		} catch (Exception e) {
			throw e;
		}

		return sCondition;
	}
	public int countRows(Connection conn, String sql) throws Exception {
		PreparedStatement pStmt = null;
		ResultSet rs = null;
//		boolean exists = false;
		int noofrows=0;
		try {
			pStmt = conn.prepareStatement(sql);
			rs = pStmt.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) > 0)
					noofrows = rs.getInt(1);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pStmt != null) {
					pStmt.close();
				}
			} catch (Exception e) {
				throw e;
			}
		}
		return noofrows;
	}
	public String formCondition(Hashtable<String, String> ht) throws Exception {
		String sCondition = "";
		try {
			Enumeration<String> enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enumeration
						.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
						+ "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
		} catch (Exception e) {
			throw e;
		}
		return sCondition;
	}
	
	public String formConditioninvoice(Hashtable<String, String> ht) throws Exception {
		String sCondition = "";
		try {
			Enumeration<String> enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enumeration
						.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				sCondition += "A."+key.toUpperCase() + " = '" + value.toUpperCase()
						+ "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
		} catch (Exception e) {
			throw e;
		}
		return sCondition;
	}
	public String formNegCondition(Hashtable<String, String> ht) throws Exception {
		String sCondition = "";
		try {
			Enumeration<String> enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enumeration
						.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				sCondition += key.toUpperCase() + " <> '" + value.toUpperCase()
						+ "' AND ";
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
		} catch (Exception e) {
			throw e;
		}
		return sCondition;
	}
	
	public String formConditionWithExoDate(Hashtable<String, String> ht) throws Exception {
		String sCondition = "";
		try {
			Enumeration<String> enumeration = ht.keys();
			for (int i = 0; i < ht.size(); i++) {
				String key = StrUtils.fString((String) enumeration
						.nextElement());
				String value = StrUtils.fString((String) ht.get(key));
				
				if(key.equalsIgnoreCase("a.EXPIREDATE")){
					sCondition += " LTRIM(RTRIM(A.EXPIREDATE)) <> '' AND A.EXPIREDATE is not null AND convert(datetime,A.EXPIREDATE,103) <= convert(datetime,'" + value.toUpperCase() + "',103) AND ";
				}else{
					sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
					+ "' AND ";
				}
				
			}
			sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
					sCondition.length() - 4) : "";
		} catch (Exception e) {
			throw e;
		}
		return sCondition;
	}

	public boolean isExists(Connection conn, String sql) throws Exception {
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		boolean exists = false;
		try {
			pStmt = conn.prepareStatement(sql);
			rs = pStmt.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) > 0)
					exists = true;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pStmt != null) {
					pStmt.close();
				}
			} catch (Exception e) {
				throw e;
			}
		}
		return exists;
	}
	
	public static boolean execute_NonSelectQuery(PreparedStatement ps, List args) throws SQLException,Exception {
	    int i = 1;
	    int count = 0;
	   
		/* Setting the query param values */
	   try{
	    for (Object arg : args) {         
	    if (arg instanceof Integer) {
	        ps.setInt(i++, (Integer) arg);
	    } else if (arg instanceof Long) {
	        ps.setLong(i++, (Long) arg);
	    } else if (arg instanceof Double) {
	        ps.setDouble(i++, (Double) arg);
	    } else if (arg instanceof Float) {
	        ps.setFloat(i++, (Float) arg);
	    } else {
	        ps.setNString(i++, (String) arg);
	    }
	    
	   }
	    /* Execute the Query */  
		   count = ps.executeUpdate();
		   System.out.println(count+" row are affected by this query.");
		   if(count > 0 ) {
			   return true;
		   }else {
			   return false;
		   }
	   }catch(SQLException se){
			  System.out.println("Exception occure in BaseDAO :: execute_NonSelectQuery()");
			  System.out.println("Exception occure due to "+se.getMessage());
			  throw se;
		}catch(Exception e){
			  System.out.println("Exception occure in BaseDAO :: execute_NonSelectQuery()");
			  System.out.println("Exception occure due to "+e.getMessage());
			  throw e;
		 }
		 finally{	  
			  if(ps != null) {
				  try {
					  ps.close();
				  }catch(SQLException se) {
					  System.out.println("Exception occure in BaseDAO :: execute_NonSelectQuery()");
					  System.out.println("Exception occure due to "+se.getMessage());
					  se.printStackTrace();
					  throw se;
				  }
			  }
		  }
	  }
	
	public static int execute_NonSelectQueryGetLastInsert(PreparedStatement ps, List args) throws SQLException,Exception {
	    int i = 1;
	    int id = 0,count = 0;
	   
		/* Setting the query param values */
	   try{
	    for (Object arg : args) {    
	    	System.out.println(arg);
	    if (arg instanceof Integer) {
	        ps.setInt(i++, (Integer) arg);
	    } else if (arg instanceof Long) {
	        ps.setLong(i++, (Long) arg);
	    } else if (arg instanceof Double) {
	        ps.setDouble(i++, (Double) arg);
	    } else if (arg instanceof Float) {
	        ps.setFloat(i++, (Float) arg);
	    } else {
	        ps.setNString(i++, (String) arg);
	    }
	    
	   }
	    /* Execute the Query */  
	    count = ps.executeUpdate();
		   ResultSet rs = ps.getGeneratedKeys();
		   if (rs.next()){
		       id = rs.getInt(1);
		   }
		   return id;		   
	   }catch(SQLException se){
			  System.out.println("Exception occure in BaseDAO :: execute_NonSelectQuery()");
			  System.out.println("Exception occure due to "+se.getMessage());
			  throw se;
		}catch(Exception e){
			  System.out.println("Exception occure in BaseDAO :: execute_NonSelectQuery()");
			  System.out.println("Exception occure due to "+e.getMessage());
			  throw e;
		 }
		 finally{	  
			  if(ps != null) {
				  try {
					  ps.close();
				  }catch(SQLException se) {
					  System.out.println("Exception occure in BaseDAO :: execute_NonSelectQuery()");
					  System.out.println("Exception occure due to "+se.getMessage());
					  se.printStackTrace();
					  throw se;
				  }
			  }
		  }
	  }
	
	
	public static List<Map<String,String>> selectData(PreparedStatement ps, List args) throws SQLException,Exception {
	    int i = 1;
	    ResultSet rs = null;
	    Map<String,String> map = null;
	    List<Map<String,String>> list = null;
	    
		/* Instantiat the List */
	    list = new ArrayList<Map<String,String>>();
	  try{
	    for (Object arg : args) {         
	    if (arg instanceof Integer) {
	        ps.setInt(i++, (Integer) arg);
	    } else if (arg instanceof Long) {
	        ps.setLong(i++, (Long) arg);
	    } else if (arg instanceof Double) {
	        ps.setDouble(i++, (Double) arg);
	    } else if (arg instanceof Float) {
	        ps.setFloat(i++, (Float) arg);
	    } else {
	        ps.setNString(i++, (String) arg);
	    }
	   }
	    /* Execute the Query */  
		   rs = ps.executeQuery();
		   
		   while(rs.next()) {
			   map = new HashMap<String,String>(); 
			 for (int j = 1; j <= rs.getMetaData().getColumnCount(); j++) {
			   map .put(rs.getMetaData().getColumnLabel(j), rs .getString(j)); 
			 }
			    list.add(map); 
			  }
	  }catch(SQLException se){
		  System.out.println("Exception occure in BaseDAO :: selectData()");
		  System.out.println("Exception occure due to "+se.getMessage());
		  throw se;
	  }catch(Exception e){
		  System.out.println("Exception occure in BaseDAO :: selectData()");
		  System.out.println("Exception occure due to "+e.getMessage());
		  throw e;
	   }
	  finally{
		  if(rs != null) {
			  try {
				  rs.close();
			  }catch(SQLException se) {
				  System.out.println("Exception occure in BaseDAO.selectData()");
				  System.out.println("Exception occure due to "+se.getMessage());
				  se.printStackTrace();
				  throw se;
			  }
		  }
		  if(ps != null) {
			  try {
				  ps.close();
			  }catch(SQLException se) {
				  System.out.println("Exception occure in BaseDAO.selectData()");
				  System.out.println("Exception occure due to "+se.getMessage());
				  se.printStackTrace();
				  throw se;
			  }
		  }
	  }
		   return list;
	}
	
	

}
