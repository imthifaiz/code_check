package com.track.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.track.constants.MLoggerConstant;
import com.track.db.object.HrEmpType;
import com.track.db.object.HrLeaveType;
import com.track.db.object.HrPayrollDET;
import com.track.db.object.HrPayrollHDR;
import com.track.db.object.HrPayrollPaymentHdr;
import com.track.db.object.LeaveTypePojo;
import com.track.db.object.MonthYearPojo;
import com.track.db.object.PayslipPojo;
import com.track.gates.DbBean;
import com.track.service.HrEmpTypeService;
import com.track.service.HrPayrollDETService;
import com.track.service.HrPayrollHDRService;
import com.track.serviceImplementation.HrEmpTypeServiceImpl;
import com.track.serviceImplementation.HrPayrollDETServiceImpl;
import com.track.serviceImplementation.HrPayrollHDRServiceImpl;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.ResultSetToObjectMap;

import net.sf.json.JSONArray;

public class HrPayrollHDRDAO {

	public static String TABLE_HEADER = "HRPAYROLLHDR";
	public String plant = "";
	private MLogger mLogger = new MLogger();
	DateUtils dateutils = new DateUtils();
	private boolean printQuery = MLoggerConstant.HrPayrollHDRDAO_PRINTPLANTMASTERQUERY;
	private boolean printLog = MLoggerConstant.HrPayrollHDRDAO_PRINTPLANTMASTERLOG;
	
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
	
	public int addpayrollhdr(HrPayrollHDR payrollhdr) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "INSERT INTO ["+ payrollhdr.getPLANT() +"_"+TABLE_HEADER+"]([PLANT]" + 
					"           ,[EMPID]" +
					"           ,[PAYROLL]" +
					"           ,[FROMDATE]" +
					"           ,[TODATE]" +
					"           ,[MONTH]" +
					"           ,[YEAR]" +
					"           ,[PAYDAYS]" +
					"           ,[TOTAL_AMOUNT]" +
					"           ,[PAYMENT_DATE]" +
					"           ,[PAYMENT_MODE]" +
					"           ,[PAID_THROUGH]" +
					"           ,[BANK_BRANCH]" +
					"           ,[CHECQUE_NO]" +
					"           ,[CHEQUE_DATE]" +
					"           ,[CHEQUE_AMOUNT]" +
					"           ,[REFERENCE]" +
					"           ,[NOTE]" +
					"           ,[STATUS]" +
					"           ,[CRAT]" + 
					"           ,[CRBY]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ps.setString(1, payrollhdr.getPLANT());
				   ps.setInt(2, payrollhdr.getEMPID());
				   ps.setString(3, payrollhdr.getPAYROLL());
				   ps.setString(4, payrollhdr.getFROMDATE());
				   ps.setString(5, payrollhdr.getTODATE());
				   ps.setString(6, payrollhdr.getMONTH());
				   ps.setString(7, payrollhdr.getYEAR());
				   ps.setDouble(8, payrollhdr.getPAYDAYS());
				   ps.setDouble(9, payrollhdr.getTOTAL_AMOUNT());
				   ps.setString(10, payrollhdr.getPAYMENT_DATE());
				   ps.setString(11, payrollhdr.getPAYMENT_MODE());
				   ps.setString(12, payrollhdr.getPAID_THROUGH());
				   ps.setString(13, payrollhdr.getBANK_BRANCH());
				   ps.setString(14, payrollhdr.getCHECQUE_NO());
				   ps.setString(15, payrollhdr.getCHEQUE_DATE());
				   ps.setDouble(16, payrollhdr.getCHEQUE_AMOUNT());
				   ps.setString(17, payrollhdr.getREFERENCE());
				   ps.setString(18, payrollhdr.getNOTE());
				   ps.setString(19, payrollhdr.getSTATUS());
				   ps.setString(20, dateutils.getDate());
				   ps.setString(21, payrollhdr.getCRBY());
				  
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   ResultSet rs = ps.getGeneratedKeys();
		                if(rs.next())
		                {
		                	HdrId = rs.getInt(1);
		                    
		                }
				   }
				   else
				   {
					   throw new SQLException("Creating payroll failed, no rows affected.");
				   }
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return HdrId;
	}
	
	public List<HrPayrollHDR> getAllpayrollhdr(String plant) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrPayrollHDR> HrPayrollHDRList=new ArrayList<HrPayrollHDR>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] ORDER BY ID DESC";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrPayrollHDR hrPayrollHDR=new HrPayrollHDR();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrPayrollHDR);
	                   HrPayrollHDRList.add(hrPayrollHDR);
	                }   
			this.mLogger.query(this.printQuery, query);
			}
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return HrPayrollHDRList;
	}
	
	public HrPayrollHDR getpayrollhdrById(String plant,int id) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    HrPayrollHDR hrPayrollHDR=new HrPayrollHDR();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE ID="+id;

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, hrPayrollHDR);
	                }   
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return hrPayrollHDR;
	}
	
	
	
	public int updatepayrollhdr(HrPayrollHDR payrollhdr,String user) throws Exception
	{
		boolean flag = false;
		int HdrId = 0;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query="UPDATE ["+ payrollhdr.getPLANT() +"_"+TABLE_HEADER+"] SET PLANT='"+payrollhdr.getPLANT()+"',"
					+ "EMPID ='"+payrollhdr.getEMPID()+"',"
					+ "PAYROLL ='"+payrollhdr.getPAYROLL()+"',"
					+ "FROMDATE ='"+payrollhdr.getFROMDATE()+"',"
					+ "TODATE ='"+payrollhdr.getTODATE()+"',"
					+ "MONTH ='"+payrollhdr.getMONTH()+"',"
					+ "YEAR ='"+payrollhdr.getYEAR()+"',"
					+ "PAYDAYS ='"+payrollhdr.getPAYDAYS()+"',"
					+ "TOTAL_AMOUNT ='"+payrollhdr.getTOTAL_AMOUNT()+"',"
					+ "PAYMENT_DATE ='"+payrollhdr.getPAYMENT_DATE()+"',"
					+ "PAYMENT_MODE ='"+payrollhdr.getPAYMENT_MODE()+"',"
					+ "PAID_THROUGH ='"+payrollhdr.getPAID_THROUGH()+"',"
					+ "BANK_BRANCH ='"+payrollhdr.getBANK_BRANCH()+"',"
					+ "CHECQUE_NO ='"+payrollhdr.getCHECQUE_NO()+"',"
					+ "CHEQUE_DATE ='"+payrollhdr.getCHEQUE_DATE()+"',"
					+ "CHEQUE_AMOUNT ='"+payrollhdr.getCHEQUE_AMOUNT()+"',"
					+ "REFERENCE ='"+payrollhdr.getREFERENCE()+"',"
					+ "NOTE ='"+payrollhdr.getNOTE()+"',"
					+ "STATUS ='"+payrollhdr.getSTATUS()+"',"
					+ "UPAT='"+dateutils.getDateTime()+"',UPBY='"+user+"' WHERE ID="+payrollhdr.getID();
			this.mLogger.query(this.printQuery, query);	
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   int count=ps.executeUpdate();
				   if(count>0)
				   {
					   ResultSet rs = ps.getGeneratedKeys();
		                if(rs.next())
		                {
		                	HdrId = rs.getInt(1);   
		                }  
				   }
				   else
				   {
					   throw new SQLException("Updating payroll failed, no rows affected.");
				   }
				   
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return HdrId;
	}
	
	public boolean Deletepayrollhdr(String plant, int id)
	        throws Exception {
			boolean deletestatus = false;
			PreparedStatement ps = null;
			Connection con = null;
			try {
			        con = DbBean.getConnection();
			        
			        
			        String sQry = "DELETE FROM " + "[" + plant +"_"+TABLE_HEADER+"]"
			                        + " WHERE ID ='"+id+"'";
			        this.mLogger.query(this.printQuery, sQry);
			        ps = con.prepareStatement(sQry);
			        int iCnt = ps.executeUpdate();
			        if (iCnt > 0) {
			        	deletestatus = true;
			        }
			} catch (Exception e) {
			        this.mLogger.exception(this.printLog, "", e);
			} finally {
			        DbBean.closeConnection(con, ps);
			}
			
			return deletestatus;
 	}
	
	public boolean  Ispayrollhdr(String plant,int empid, String month, String year) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean status = false;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE EMPID='"+empid+"' AND MONTH='"+month+"' AND YEAR='"+year+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   status = true;
	                }   
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return status;
	}
	
	public HrPayrollHDR payrollhdrbyempidmonthyear(String plant,int empid, String month, String year) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    HrPayrollHDR hrPayrollHDR=new HrPayrollHDR();
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE EMPID='"+empid+"' AND MONTH='"+month+"' AND YEAR='"+year+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
	                    ResultSetToObjectMap.loadResultSetIntoObject(rst, hrPayrollHDR);
	                }   
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return hrPayrollHDR;
	}
	
	
	
	public List<HrPayrollHDR> payrollhdrbyempid(String plant,int empid) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrPayrollHDR> HrPayrollHDRList=new ArrayList<HrPayrollHDR>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE EMPID='"+empid+"'";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrPayrollHDR hrPayrollHDR=new HrPayrollHDR();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrPayrollHDR);
	                   HrPayrollHDRList.add(hrPayrollHDR);
	                }   
			this.mLogger.query(this.printQuery, query);
			}
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return HrPayrollHDRList;
	}
	
	public List<HrPayrollHDR> payrollhdrbystatus(String plant,String status) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrPayrollHDR> HrPayrollHDRList=new ArrayList<HrPayrollHDR>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE STATUS='"+status+"'";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrPayrollHDR hrPayrollHDR=new HrPayrollHDR();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrPayrollHDR);
	                   HrPayrollHDRList.add(hrPayrollHDR);
	                }   
			this.mLogger.query(this.printQuery, query);
			}
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return HrPayrollHDRList;
	}
	
	public List<HrPayrollHDR>  payrollhdrbymonthyear(String plant,String month, String year) throws Exception {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement ps = null;
	    String query = "";
	    List<HrPayrollHDR> HrPayrollHDRList=new ArrayList<HrPayrollHDR>();
	    
		try {	    
			connection = DbBean.getConnection();
			
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE MONTH='"+month+"' AND YEAR='"+year+"'";
		
			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   HrPayrollHDR hrPayrollHDR=new HrPayrollHDR();
	                   ResultSetToObjectMap.loadResultSetIntoObject(rst, hrPayrollHDR);
	                   HrPayrollHDRList.add(hrPayrollHDR);
	                }   
			this.mLogger.query(this.printQuery, query);
			}
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return HrPayrollHDRList;
	}
	
	public boolean  Ispayrollnumber(String plant,String payroll) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean status = false;
	    String query = "";
		try {	    
			connection = DbBean.getConnection();
			query = "SELECT * FROM ["+ plant +"_"+TABLE_HEADER+"] WHERE PAYROLL='"+payroll+"'";

			if(connection != null){
				   ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				   ResultSet rst = ps.executeQuery();
				   while (rst.next()) {
					   status = true;
	                }   
			this.mLogger.query(this.printQuery, query);
			
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			
			throw e;
		} finally {
			if (connection != null) {
				DbBean.closeConnection(connection);
			}
		}
		return status;
	}
	
	public List<PayslipPojo> getpaysliplist(String plant,String month, String year) throws Exception {
		
		 List<PayslipPojo> PayslipPojoList=new ArrayList<PayslipPojo>();
		 	HrEmpSalaryDetDAO hrEmpSalaryDetDAO = new HrEmpSalaryDetDAO();
			HrPayrollDETService HrPayrollDETService = new HrPayrollDETServiceImpl();
			PlantMstDAO  plantMstDAO = new PlantMstDAO();
			EmployeeDAO employeeDAO = new EmployeeDAO();
			HrDeductionHdrDAO hrDeductionHdrDAO = new HrDeductionHdrDAO();
			HrDeductionDetDAO hrDeductionDetDAO = new HrDeductionDetDAO();
			HrPayrollDETDAO hrPayrollDETDAO = new HrPayrollDETDAO();
		try {
			List<HrPayrollHDR> hrPayrollHDRlist = this.payrollhdrbymonthyear(plant, month, year);
			for (HrPayrollHDR hrpayrollhdr : hrPayrollHDRlist) {
				
				ArrayList arrEmp = employeeDAO.getEmployeeListbyid(String.valueOf(hrpayrollhdr.getEMPID()),plant);
				Map employee=(Map)arrEmp.get(0);
				
				String empcode = (String) employee.get("EMPNO");
				String empname = (String) employee.get("FNAME");
				String department = (String) employee.get("DEPT");
				String designation = (String) employee.get("DESGINATION");
				String passportno = (String) employee.get("PASSPORTNUMBER");
				String doj = (String) employee.get("DATEOFJOINING");
				String lcardno = (String) employee.get("LABOURCARDNUMBER");
				String bank = (String) employee.get("BANKNAME");
				String payperiod = hrpayrollhdr.getFROMDATE() +" to "+ hrpayrollhdr.getTODATE();
				String dateofpay = hrpayrollhdr.getPAYMENT_DATE();
				
				List<HrPayrollDET> allowancelist = hrPayrollDETDAO.getsalaryallowance(plant, hrpayrollhdr.getID(), hrpayrollhdr.getEMPID());
				List<HrPayrollDET> additionlist = hrPayrollDETDAO.getsalaryaddition(plant, hrpayrollhdr.getID(), hrpayrollhdr.getEMPID());
				List<HrPayrollDET> deductionlist = hrPayrollDETDAO.getsalarydeduction(plant, hrpayrollhdr.getID(), hrpayrollhdr.getEMPID(),hrpayrollhdr.getMONTH(),hrpayrollhdr.getYEAR());
		    	
				double allowamt = 0;
				for(HrPayrollDET salaryallow:allowancelist){
					allowamt = allowamt + salaryallow.getAMOUNT();
				}
				
				double netamount = allowamt;
				for(HrPayrollDET addlist:additionlist) {
					netamount = netamount + addlist.getAMOUNT();
				}
				
				for(HrPayrollDET deductlist:deductionlist) {
					netamount = netamount - deductlist.getAMOUNT();
				}
				
				PayslipPojo payslipPojo = new PayslipPojo();
				payslipPojo.setEmpcode(empcode);
				payslipPojo.setName(empname);
				payslipPojo.setDepartment(department);
				payslipPojo.setDesignation(designation);
				payslipPojo.setPassport(passportno);
				payslipPojo.setDoj(doj);
				payslipPojo.setLabourcard(lcardno);
				payslipPojo.setBankname(bank);
				payslipPojo.setPayperiod(payperiod);
				payslipPojo.setPaymentdate(dateofpay);
				payslipPojo.setSalary(allowancelist);
				payslipPojo.setGrosspay(allowamt);
				payslipPojo.setAddition(additionlist);
				payslipPojo.setDeduction(deductionlist);
				payslipPojo.setNetpay(netamount);
				
				PayslipPojoList.add(payslipPojo);
				
			}
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} 
		return PayslipPojoList;
	}
	
	
	public List<PayslipPojo> getpaysliplistfilter(String plant,String filter, String fm, String fy, String tm, String ty, String empid) throws Exception {
		
		 List<PayslipPojo> PayslipPojoList=new ArrayList<PayslipPojo>();
		 	HrEmpSalaryDetDAO hrEmpSalaryDetDAO = new HrEmpSalaryDetDAO();
			HrPayrollDETService HrPayrollDETService = new HrPayrollDETServiceImpl();
			PlantMstDAO  plantMstDAO = new PlantMstDAO();
			EmployeeDAO employeeDAO = new EmployeeDAO();
			HrDeductionHdrDAO hrDeductionHdrDAO = new HrDeductionHdrDAO();
			HrDeductionDetDAO hrDeductionDetDAO = new HrDeductionDetDAO();
			HrPayrollDETDAO hrPayrollDETDAO = new HrPayrollDETDAO();
			HrPayrollPaymentDAO hrPayrollPaymentDAO = new HrPayrollPaymentDAO();
		try {
			List<HrPayrollHDR> hrpayrollhdrlist = new ArrayList<HrPayrollHDR>();
			List<MonthYearPojo> monthyyearlist = new ArrayList<MonthYearPojo>();
			if(filter.equalsIgnoreCase("2") || filter.equalsIgnoreCase("3")) {
				monthyyearlist = getmonthyear(fm, fy, fm, fy);
			}else {
				monthyyearlist = getmonthyear(fm, fy, tm, ty);
			}
			for (MonthYearPojo monthYearPojo : monthyyearlist) {
				List<HrPayrollHDR> hrpayrollhdrmylist = payrollhdrbymonthyear(plant, monthYearPojo.getMONTH(), monthYearPojo.getYEAR());
				hrpayrollhdrmylist = hrpayrollhdrmylist.stream().filter((p)->p.getSTATUS().equalsIgnoreCase("PAID")).collect(Collectors.toList());
				hrpayrollhdrlist.addAll(hrpayrollhdrmylist);
			}
			
			
			String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
			JSONArray payrollarray = new JSONArray();
			for (HrPayrollHDR hrpayrollhdr : hrpayrollhdrlist) {
				
				int payhdrid = hrPayrollPaymentDAO.getHrPayrollPaymendetbypayId(plant, hrpayrollhdr.getID());
				String paymode = "";
				
				if(payhdrid != 0){
					HrPayrollPaymentHdr pphdr = hrPayrollPaymentDAO.getHrPayrollPaymentHdrId(plant, payhdrid);
					paymode = pphdr.getPAYMENT_MODE();
				}
				
				if(empid.equalsIgnoreCase("0")) {
					ArrayList arrEmp = employeeDAO.getEmployeeListbyid(String.valueOf(hrpayrollhdr.getEMPID()),plant);
					Map employee=(Map)arrEmp.get(0);
					
					String empcode = (String) employee.get("EMPNO");
					String empname = (String) employee.get("FNAME");
					String department = (String) employee.get("DEPT");
					String designation = (String) employee.get("DESGINATION");
					String passportno = (String) employee.get("PASSPORTNUMBER");
					String doj = (String) employee.get("DATEOFJOINING");
					String lcardno = (String) employee.get("LABOURCARDNUMBER");
					String bank = (String) employee.get("BANKNAME");
					String payperiod = hrpayrollhdr.getFROMDATE() +" to "+ hrpayrollhdr.getTODATE();
					String dateofpay = hrpayrollhdr.getPAYMENT_DATE();
					
					List<HrPayrollDET> allowancelist = hrPayrollDETDAO.getsalaryallowance(plant, hrpayrollhdr.getID(), hrpayrollhdr.getEMPID());
					List<HrPayrollDET> additionlist = hrPayrollDETDAO.getsalaryaddition(plant, hrpayrollhdr.getID(), hrpayrollhdr.getEMPID());
					List<HrPayrollDET> deductionlist = hrPayrollDETDAO.getsalarydeduction(plant, hrpayrollhdr.getID(), hrpayrollhdr.getEMPID(),hrpayrollhdr.getMONTH(),hrpayrollhdr.getYEAR());
			    	
					double allowamt = 0;
					for(HrPayrollDET salaryallow:allowancelist){
						allowamt = allowamt + salaryallow.getAMOUNT();
					}
					
					double netamount = allowamt;
					for(HrPayrollDET addlist:additionlist) {
						netamount = netamount + addlist.getAMOUNT();
					}
					
					for(HrPayrollDET deductlist:deductionlist) {
						netamount = netamount - deductlist.getAMOUNT();
					}
					
					PayslipPojo payslipPojo = new PayslipPojo();
					payslipPojo.setEmpcode(empcode);
					payslipPojo.setName(empname);
					payslipPojo.setDepartment(department);
					payslipPojo.setDesignation(designation);
					payslipPojo.setPassport(passportno);
					payslipPojo.setDoj(doj);
					payslipPojo.setLabourcard(lcardno);
					payslipPojo.setBankname(bank);
					payslipPojo.setPayperiod(payperiod);
					payslipPojo.setPaymentdate(dateofpay);
					payslipPojo.setSalary(allowancelist);
					payslipPojo.setGrosspay(allowamt);
					payslipPojo.setAddition(additionlist);
					payslipPojo.setDeduction(deductionlist);
					payslipPojo.setNetpay(netamount);
					payslipPojo.setPaidthrough(paymode);
					
					PayslipPojoList.add(payslipPojo);
				}else if(empid.equalsIgnoreCase(String.valueOf(hrpayrollhdr.getEMPID()))) {
					ArrayList arrEmp = employeeDAO.getEmployeeListbyid(String.valueOf(hrpayrollhdr.getEMPID()),plant);
					Map employee=(Map)arrEmp.get(0);
					
					String empcode = (String) employee.get("EMPNO");
					String empname = (String) employee.get("FNAME");
					String department = (String) employee.get("DEPT");
					String designation = (String) employee.get("DESGINATION");
					String passportno = (String) employee.get("PASSPORTNUMBER");
					String doj = (String) employee.get("DATEOFJOINING");
					String lcardno = (String) employee.get("LABOURCARDNUMBER");
					String bank = (String) employee.get("BANKNAME");
					String payperiod = hrpayrollhdr.getFROMDATE() +" to "+ hrpayrollhdr.getTODATE();
					String dateofpay = hrpayrollhdr.getPAYMENT_DATE();
					
					List<HrPayrollDET> allowancelist = hrPayrollDETDAO.getsalaryallowance(plant, hrpayrollhdr.getID(), hrpayrollhdr.getEMPID());
					List<HrPayrollDET> additionlist = hrPayrollDETDAO.getsalaryaddition(plant, hrpayrollhdr.getID(), hrpayrollhdr.getEMPID());
					List<HrPayrollDET> deductionlist = hrPayrollDETDAO.getsalarydeduction(plant, hrpayrollhdr.getID(), hrpayrollhdr.getEMPID(),hrpayrollhdr.getMONTH(),hrpayrollhdr.getYEAR());
			    	
					double allowamt = 0;
					for(HrPayrollDET salaryallow:allowancelist){
						allowamt = allowamt + salaryallow.getAMOUNT();
					}
					
					double netamount = allowamt;
					for(HrPayrollDET addlist:additionlist) {
						netamount = netamount + addlist.getAMOUNT();
					}
					
					for(HrPayrollDET deductlist:deductionlist) {
						netamount = netamount - deductlist.getAMOUNT();
					}
					
					PayslipPojo payslipPojo = new PayslipPojo();
					payslipPojo.setEmpcode(empcode);
					payslipPojo.setName(empname);
					payslipPojo.setDepartment(department);
					payslipPojo.setDesignation(designation);
					payslipPojo.setPassport(passportno);
					payslipPojo.setDoj(doj);
					payslipPojo.setLabourcard(lcardno);
					payslipPojo.setBankname(bank);
					payslipPojo.setPayperiod(payperiod);
					payslipPojo.setPaymentdate(dateofpay);
					payslipPojo.setSalary(allowancelist);
					payslipPojo.setGrosspay(allowamt);
					payslipPojo.setAddition(additionlist);
					payslipPojo.setDeduction(deductionlist);
					payslipPojo.setNetpay(netamount);
					payslipPojo.setPaidthrough(paymode);
					
					PayslipPojoList.add(payslipPojo);
				}
				
			}
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		} 
		return PayslipPojoList;
	}
	
	public List<MonthYearPojo> getmonthyear(String fm,String fy,String tm,String ty) throws ParseException{
		DateUtils dateUtils = new DateUtils();
		String fromdate = "01/"+fm+"/"+fy;
		String todate = "01/"+tm+"/"+ty;
		int tom = Integer.valueOf(tm);
		int toy = Integer.valueOf(ty);
		int fom = 0;
		int foy = 0;
		String tdate ="0";
		
		int i = 0;
		List<MonthYearPojo> monthyearlist = new ArrayList<MonthYearPojo>();
		do {
			
			if(i != 0) {
				
				i++;
				fromdate = dateUtils.addMonth(fromdate, 1);
				String[] splitdate = fromdate.split("/");
				String cyear = splitdate[2];
				String cmonth = splitdate[1];
				MonthYearPojo monthYearPojo = new MonthYearPojo();
				fom = Integer.valueOf(cmonth);
				foy = Integer.valueOf(cyear);
				tdate ="01/"+fom+"/"+foy;
				monthYearPojo.setMONTH(String.valueOf(fom));
				monthYearPojo.setYEAR(cyear);
				monthyearlist.add(monthYearPojo);
			}else {
				i++;
				String[] splitdate = fromdate.split("/");
				String cyear = splitdate[2];
				String cmonth = splitdate[1];
				MonthYearPojo monthYearPojo = new MonthYearPojo();
				fom = Integer.valueOf(cmonth);
				foy = Integer.valueOf(cyear);
				tdate ="01/"+fom+"/"+foy;
				monthYearPojo.setMONTH(String.valueOf(fom));
				monthYearPojo.setYEAR(cyear);
				monthyearlist.add(monthYearPojo);
			}
			
		}while(!tdate.equalsIgnoreCase(todate));
		
		return monthyearlist;
	}
}
