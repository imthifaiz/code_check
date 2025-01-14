package com.track.db.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.track.dao.EmployeeDAO;
import com.track.dao.RsnMst;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class EmployeeUtil {

	public EmployeeUtil() {

	}

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	public boolean insertEmployeeMst(Hashtable ht) throws Exception {
		boolean inserted = false;
		try {

			EmployeeDAO employeeDao = new EmployeeDAO();
			employeeDao.setmLogger(mLogger);
			inserted = employeeDao.insertIntoEmployeeMst(ht);
		} catch (Exception e) {
			throw e;
		}
		return inserted;
	}
	
	public int insertEmployeeMstretid(Hashtable ht) throws Exception {
		int inserted = 0;
		try {

			EmployeeDAO employeeDao = new EmployeeDAO();
			employeeDao.setmLogger(mLogger);
			inserted = employeeDao.insertIntoEmployeeMstretid(ht);
		} catch (Exception e) {
			throw e;
		}
		return inserted;
	}

	public boolean isExistsEmployee(Hashtable htemp) throws Exception {

		boolean isExists = false;
		EmployeeDAO employeeDao = new EmployeeDAO();
		employeeDao.setmLogger(mLogger);
		try {
			isExists = employeeDao.isExists(htemp);

		} catch (Exception e) {
			throw e;
		}

		return isExists;
	}

	public boolean updateEmployeeMst(Hashtable htUpdate, Hashtable htCondition)
			throws Exception {
		boolean update = false;
		try {
			EmployeeDAO employeeDao = new EmployeeDAO();
			employeeDao.setmLogger(mLogger);
			update = employeeDao.updateEmployeeMst(htUpdate, htCondition);

		} catch (Exception e) {
			throw e;
		}
		return update;
	}

	public boolean deleteEmployeeid(Hashtable ht) throws Exception {
		boolean deleted = false;
		try {
			EmployeeDAO employeeDao = new EmployeeDAO();
			employeeDao.setmLogger(mLogger);
			deleted = employeeDao.deleteEmployeeId(ht);
		} catch (Exception e) {
			throw e;
		}
		return deleted;
	}

	public ArrayList getEmployeeList(String aempno, String plant, String cond) {

		ArrayList al = null;
		EmployeeDAO employeeDao = new EmployeeDAO();
		employeeDao.setmLogger(mLogger);
		try {
			al = employeeDao.getEmployeeDetails(aempno, plant, cond);

		} catch (Exception e) {
		}

		return al;
	}
	public ArrayList getEmployeeListStartsWithName(String aCustName,
			String plant) throws Exception {
		ArrayList arrList = new ArrayList();
		EmployeeDAO employeeDao = new EmployeeDAO();
		employeeDao.setmLogger(mLogger);
		try {
			
			arrList = employeeDao.getEmployeeListStartsWithName(aCustName,plant);
		} catch (Exception e) {
		}
		return arrList;
	}
	
	public ArrayList getEmployeeListStartsWithNameempid(String aCustName,
			String plant,String empid) throws Exception {
		ArrayList arrList = new ArrayList();
		EmployeeDAO employeeDao = new EmployeeDAO();
		employeeDao.setmLogger(mLogger);
		try {
			
			arrList = employeeDao.getEmployeeListStartsWithNameempid(aCustName,plant,empid);
		} catch (Exception e) {
		}
		return arrList;
	}
	
public boolean isAlternateEmployeeAvailable(String plant, String emp) {
		try {
			boolean executeQuery = true;
			
			EmployeeDAO employeeDao = new EmployeeDAO();
			String sql = "SELECT COUNT(*) FROM [" + plant
					+ "_ALTERNATE_EMPLOYEE_MAPPING] WHERE PLANT='" + plant
					+ "' AND EMPNO='" + emp + "' ";
			executeQuery = employeeDao.isExisit(sql);
			return executeQuery;
		} catch (Exception e) {
			return false;
		}
	}
public boolean removeAlternateEmp(String plant, String emp,String Cond) {
	EmployeeDAO employeeDao = new EmployeeDAO();
	employeeDao.setmLogger(mLogger);
		return employeeDao.removeAlternateEmployee(plant, emp,Cond);
	}
public boolean insertAlternateEmpLists(String plant, String emp,
		List<String> alternateEmpLists) {
	try {
		boolean executeQuery = true;
		EmployeeDAO employeeDao = new EmployeeDAO();
		employeeDao.setmLogger(mLogger);
		for (String alternateEmp : alternateEmpLists) {
			executeQuery = executeQuery
					& employeeDao.insertAlternateEmployee(plant, emp,
							alternateEmp);
		}
		return executeQuery;
	} catch (Exception e) {
		return false;
	}
}
public boolean isAlternateexists(Hashtable ht) {
	try {
		boolean executeQuery = true;
		
		EmployeeDAO employeeDao = new EmployeeDAO();
		String sql = "SELECT 1 FROM [" + ht.get("PLANT")
				+ "_ALTERNATE_EMPLOYEE_MAPPING] WHERE PLANT='" + ht.get("PLANT")+"' and ALTERNATE_EMPLOYEE_NO='"+ht.get("ALTERNATE_EMPLOYEE_NO")+"'";
		executeQuery = employeeDao.isExisit(sql);
		return executeQuery;
	} catch (Exception e) {
		return false;
	}
}

public ArrayList getEmployeeDetails(String employee, String plant,String user) throws Exception {
	Hashtable htCondition = new Hashtable();
	ArrayList alResult = new ArrayList();
	String condAssignedLocforUser = " "; 
	try {
		EmployeeDAO employeeDao = new EmployeeDAO();
		htCondition.put("PLANT", plant);
		UserLocUtil userLocUtil = new UserLocUtil();
        userLocUtil.setmLogger(mLogger);
       	alResult = employeeDao.getEmployeeDetails(
				"ISNULL(EMPNO,'') EMPNO,ISNULL(FNAME,'') FNAME", htCondition,
				" and EMPNO like '" + employee + "%'"+  condAssignedLocforUser+ "  ORDER BY LOC ");

	} catch (Exception e) {
		
	}

	return alResult;
}
public String getPDAEmployeeDetails(String plant,String empno) {

	String xmlStr = "";
	ArrayList al = null;
	EmployeeDAO dao = new EmployeeDAO();
	StrUtils strUtils=new StrUtils();
	dao.setmLogger(mLogger);
	try {
		al = dao.getPDAEmployeDetails(plant,empno);

		if (al.size() > 0) {
			xmlStr += XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("EmployeeList total='"
					+ String.valueOf(al.size()) + "'");
			for (int i = 0; i < al.size(); i++) {
				Map map = (Map) al.get(i);
				xmlStr += XMLUtils.getStartNode("record");
				xmlStr += XMLUtils.getXMLNode("emono", (String) map.get("empno"));
				xmlStr += XMLUtils.getXMLNode("fname", (String)strUtils.replaceCharacters2SendPDA( map.get("fname").toString()));
				xmlStr += XMLUtils.getXMLNode("lname", (String)strUtils.replaceCharacters2SendPDA( map.get("lname").toString()));
				xmlStr += XMLUtils.getEndNode("record");
			}
			xmlStr += XMLUtils.getEndNode("EmployeeList");
		}
	} catch (Exception e) {
	}
	return xmlStr;
}
public boolean updateEmployeeMst(String plant,String empId,String catalogPath) throws Exception {
	boolean inserted = false;
	try {

		EmployeeDAO employeeDao = new EmployeeDAO();
		employeeDao.setmLogger(mLogger);
		inserted = employeeDao.updateEmployeeImage(plant,empId,catalogPath);
	} catch (Exception e) {
		throw e;
	}
	return inserted;
}

public boolean insertIntoEmployeeuseidMst(Hashtable ht) throws Exception {
	boolean inserted = false;
	try {

		EmployeeDAO employeeDao = new EmployeeDAO();
		employeeDao.setmLogger(mLogger);
		inserted = employeeDao.insertIntoEmployeeuseidMst(ht);
	} catch (Exception e) {
		throw e;
	}
	return inserted;
}

public boolean insertIntouserMst(Hashtable ht) throws Exception {
	boolean inserted = false;
	try {

		EmployeeDAO employeeDao = new EmployeeDAO();
		employeeDao.setmLogger(mLogger);
		inserted = employeeDao.insertIntouserMst(ht);
	} catch (Exception e) {
		throw e;
	}
	return inserted;
}


public boolean isExistEmployee(String employee, String plant) {
	boolean exists = false;
	EmployeeDAO employeeDAO = new EmployeeDAO();
	try {
		employeeDAO.setmLogger(mLogger);
		exists = employeeDAO.isExistEmployee(employee, plant);
		
	} catch (Exception e) {
	}
	return exists;
}

public boolean isExistIncoterms(String incoterms, String plant) {
	boolean exists = false;
	EmployeeDAO employeeDAO = new EmployeeDAO();
	try {
		employeeDAO.setmLogger(mLogger);
		exists = employeeDAO.isExistIncoterms(incoterms, plant);
		
	} catch (Exception e) {
	}
	return exists;
}
}
