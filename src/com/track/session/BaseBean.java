package com.track.session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.track.util.StrUtils;

@Deprecated
public class BaseBean {
	public BaseBean() {
	}

	public ArrayList selectData(Connection conn, String query) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;
		Map map = null;

		ArrayList arrayList = new ArrayList();
		try {

			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				map = new HashMap();
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

	public boolean updateData(Connection conn, String sql) throws Exception {
		boolean flag = false;
		Statement stmt = null;
		int updateCount = 0;
		try {
			stmt = conn.createStatement();
			updateCount = stmt.executeUpdate(sql);
			if (updateCount <= 0) {
				flag = false;
				throw new Exception("Unable to update!");
			} else {
				flag = true;
			}
		} catch (Exception e) {
			System.out
					.println(" ############# Exceptin : updateData() #############"
							+ e.getMessage());
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
				throw e;
			}
		}
		return flag;
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
			System.out
					.println(" ############# Exceptin : isExists() #############"
							+ e.getMessage());
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

	public Map selectRowOfData(Connection conn, String query) throws Exception {
		// Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map map = null;
		map = new HashMap();

		try {

			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				map = new HashMap();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {

					map
							.put(rs.getMetaData().getColumnLabel(i), rs
									.getString(i));

				}

			}
		} catch (Exception e) {
			System.out
					.println(" ############# Exceptin : getRowOfData() #############"
							+ e.getMessage());
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
		} catch (Exception e) {
			System.out
					.println(" ############# Exceptin : insertData() #############"
							+ e.getMessage());
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
			System.out
					.println(" ############# Exceptin : DeleteRow() #############"
							+ e.getMessage());
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

	public String formCondition(Hashtable ht) {
		String sCondition = "";
		Enumeration enum1 = ht.keys();
		for (int i = 0; i < ht.size(); i++) {
			StrUtils strUtils = new StrUtils();
			String key = strUtils.fString((String) enum1.nextElement());
			String value = strUtils.fString((String) ht.get(key));
			sCondition += key.toUpperCase() + " = '" + value.toUpperCase()
					+ "' AND ";

		}
		sCondition = (sCondition.length() > 0) ? sCondition.substring(0,
				sCondition.length() - 4) : "";
		return sCondition;
	}

	public String formUpdateValues(Hashtable htUpdate) {
		StrUtils strUtils = new StrUtils();
		String sCondition = "";
		Enumeration enumUpdate = htUpdate.keys();
		for (int i = 0; i < htUpdate.size(); i++) {
			String key = strUtils.fString((String) enumUpdate.nextElement());
			String value = strUtils.fString((String) htUpdate.get(key));
			sCondition += key.toUpperCase() + " = '" + value + "',";
		}
		return sCondition;
	}

	public String formUpdateQuery(String TABLE_NAME, Hashtable htUpdate,
			Hashtable htCondition) {
		String sCondition = "Update " + TABLE_NAME;
		sCondition = sCondition + " SET ";
		sCondition = sCondition + formUpdateValues(htUpdate);
		sCondition = sCondition + formCondition(htCondition);
		return sCondition;
	}

}