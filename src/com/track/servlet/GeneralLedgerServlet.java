package com.track.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import com.track.constants.MLoggerConstant;
import com.track.dao.CoaDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.JournalDAO;
import com.track.dao.POSShiftAmountHdrDAO;
import com.track.dao.PlantMstDAO;
import com.track.db.object.JournalDetail;
import com.track.db.object.LedgerDetailsWithId;
import com.track.db.object.LedgerWithDetId;
import com.track.db.object.POSShiftAmountHdr;
import com.track.db.util.PlantMstUtil;
import com.track.gates.DbBean;
import com.track.service.JournalService;
import com.track.service.LedgerService;
import com.track.serviceImplementation.JournalEntry;
import com.track.serviceImplementation.LedgerServiceImpl;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/GeneralLedgerServlet")
public class GeneralLedgerServlet extends HttpServlet implements IMLogger{
	
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.GeneralLedgerServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.GeneralLedgerServlet_PRINTPLANTMASTERINFO;
	String action = "";
	private LedgerService ledgerService=new LedgerServiceImpl();
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		action = StrUtils.fString(request.getParameter("action")).trim();
	
	}
	
	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		action = StrUtils.fString(req.getParameter("action")).trim();
		String plant = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();
		String user = StrUtils.fString((String) req.getSession().getAttribute("LOGIN_USER")).trim();
		String type="ALL";
		
		if(action.equalsIgnoreCase("getLedgerDetails")) {
			JSONArray jarray=new JSONArray();
			String fromDate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(req.getParameter("toDate")).trim();
			String Search = StrUtils.fString(req.getParameter("chartofAccount")).trim();
			
			CoaDAO coaDAO=new CoaDAO();
			List<LedgerDetailsWithId> ledgerDetails=new ArrayList<LedgerDetailsWithId>();
			List<LedgerWithDetId> ledgerList=new ArrayList<LedgerWithDetId>();
			PlantMstUtil plantmstutil = new PlantMstUtil();
			try {
				String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				String pfdate = "";
				List viewlistQry = plantmstutil.getPlantMstDetails(plant);
			    for (int i = 0; i < viewlistQry.size(); i++) {
			        Map map = (Map) viewlistQry.get(i);
			        pfdate=StrUtils.fString((String)map.get("STARTDATE"));			       
				}
				//ledgerDetails=ledgerService.getGeneralLedgerDetailsByDate(plant, fromDate, toDate, Search);
				//Map<String,List<LedgerDetails>> ledgerDetailsGrouped=ledgerDetails.stream().collect(Collectors.groupingBy(LedgerDetails::getACCOUNT));
				ledgerDetails = ledgerService.getGeneralLedgerIdDetailsByDate(plant, fromDate, toDate, Search);
				//Map<Integer, List<LedgerDetailsWithId>> ledgerDetailsGrouped=ledgerDetails.stream().collect(Collectors.groupingBy(LedgerDetailsWithId::getACCOUNT_ID));
				Map<Integer, List<LedgerDetailsWithId>> ledgerDetailsGrouped=ledgerDetails.stream().collect(Collectors.groupingBy(LedgerDetailsWithId::getACCOUNT_ID));
						
				for (Entry<Integer, List<LedgerDetailsWithId>> entry : ledgerDetailsGrouped.entrySet()) {
					
					Double openingBalance=0.00;
					LedgerWithDetId ledger=new LedgerWithDetId();
					String accountcode = coaDAO.GetAccountCodeByID(String.valueOf(entry.getKey()), plant);
					ledger.setACCOUNT(accountcode+" "+entry.getKey());
					List<LedgerDetailsWithId> ledgerdet = entry.getValue();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
					//Collections.sort(ledgerdet, (s1, s2) -> LocalDateTime.parse(s1.getDATE(), formatter).compareTo(LocalDateTime.parse(s2.getDATE(), formatter)));
					List<LedgerDetailsWithId> ledgerdetsort= ledgerdet.stream().sorted((o1, o2)->LocalDate.parse(o1.getDATE(), formatter).compareTo(LocalDate.parse(o2.getDATE(), formatter))).collect(Collectors.toList());
					
					//LocalDate yearStartDay=LocalDate.parse(fromDate).withDayOfYear(1);
					
					LocalDate yearStartDay=LocalDate.parse(pfdate, formatter);
					
					//JSONObject caoRecord=coaDAO.getCOAByName(plant, entry.getKey());
					//if(caoRecord.get("id")!=null)
					//{
						
						if(LocalDate.parse(fromDate).equals(yearStartDay)){
							LocalDate ydate = LocalDate.parse(pfdate, formatter);
							LocalDate ysdate=yearStartDay.minus(1, ChronoUnit.YEARS);
							LocalDate lday = ysdate.with(TemporalAdjusters.lastDayOfYear());
							openingBalance=ledgerService.openingBalance(plant,String.valueOf(entry.getKey()),ydate.toString(),lday.toString());
						}else {
							LocalDate lday = LocalDate.parse(fromDate).minus(1, ChronoUnit.DAYS);
							openingBalance=ledgerService.openingBalance(plant,String.valueOf(entry.getKey()),yearStartDay.toString(),lday.toString());
						}
						
						
					//}
					ledger.setOPENING_BALANCE(openingBalance);
					Double totalDebit=0.00;
					Double totalCredit=0.00;
					for(LedgerDetailsWithId ledgerDet:entry.getValue())
					{
						totalDebit+=ledgerDet.getDEBIT();
						totalCredit+=ledgerDet.getCREDIT();
					}
					Double closingBalance=totalDebit-totalCredit+openingBalance;
					ledger.setCLOSING_BALANCE(closingBalance);
					
					double balamt = openingBalance;
					for (LedgerDetailsWithId ledDet : ledgerdetsort) {
						balamt = balamt + ledDet.getDEBIT() - ledDet.getCREDIT();
						if(balamt < 0) {
							String balval = StrUtils.addZeroes(balamt, numberOfDecimal)+"Cr";
							ledDet.setBALANCEAMT(balval);
						}else if(balamt > 0) {
							String balval = StrUtils.addZeroes(balamt, numberOfDecimal)+"Dr";
							ledDet.setBALANCEAMT(balval);
						}else {
							String balval = StrUtils.addZeroes(balamt, numberOfDecimal);
							ledDet.setBALANCEAMT(balval);
						}
					}
					ledger.setLEDGER_DETAILS(ledgerdetsort);
					ledgerList.add(ledger);
				}
				jarray.addAll(ledgerList);
				//jarray.add(trialBalArr.values().toArray());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(jarray.toString());
			resp.getWriter().flush();
			resp.getWriter().close();
		}
		
		if (action.equalsIgnoreCase("getjournalDetails")) {
		 	JSONObject jsonObjectResult = new JSONObject();
			jsonObjectResult = getjournalDetails(req, resp);
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(jsonObjectResult.toString());
			resp.getWriter().flush();
			resp.getWriter().close();
		}
		
		if (action.equalsIgnoreCase("getjournalDetailsByDetId")) {
		 	JSONObject jsonObjectResult = new JSONObject();
			jsonObjectResult = getjournalDetailsdetid(req, resp);
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(jsonObjectResult.toString());
			resp.getWriter().flush();
			resp.getWriter().close();
		}
		
		else if(action.equalsIgnoreCase("getAccountOpeningBalance")) {
			JSONObject jObject=new JSONObject();
			String fromDate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(req.getParameter("toDate")).trim();
			String account = StrUtils.fString(req.getParameter("account")).trim();
			CoaDAO coaDAO=new CoaDAO();
			Double openingBalance=0.00;
			try {
				LocalDate yearStartDay=LocalDate.parse(fromDate).withDayOfYear(1);
				JSONObject caoRecord=coaDAO.getCOAByName(plant, account);
				openingBalance=ledgerService.openingBalance(plant,caoRecord.getString("id"),yearStartDay.toString(),fromDate);
				jObject.put("openingbalance", openingBalance);
				
				//jarray.add(trialBalArr.values().toArray());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(jObject.toString());
			resp.getWriter().flush();
			resp.getWriter().close();
		}
	}
	
	
	public JSONObject posrecivedjournal(HttpServletRequest request, HttpServletResponse response) {
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		
		String id = StrUtils.fString(request.getParameter("sid"));

		UserTransaction ut = null;
		DateUtils dateutils = new DateUtils();
		POSShiftAmountHdrDAO pOSShiftAmountHdrDAO = new POSShiftAmountHdrDAO();
		EmployeeDAO employeeDAO = new EmployeeDAO();
		String result = "";
		JSONObject resultJson = new JSONObject();
	
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
			
			POSShiftAmountHdr pOSShiftAmountHdr = pOSShiftAmountHdrDAO.getbyid(plant, Integer.valueOf(id));
			String scname = employeeDAO.getEmpnameByempid(plant, pOSShiftAmountHdr.getCRBY(), "");
			pOSShiftAmountHdr.setCRBY(scname);
			
			JournalService journalService = new JournalEntry();
			List<JournalDetail> jdetail = journalService.getJournalDetailsBySalesRecipt(plant, Integer.valueOf(id));

			DbBean.CommitTran(ut);
			resultJson.put("JOURNAL", jdetail);
			resultJson.put("SHIFTHDR", pOSShiftAmountHdr);
			if(jdetail.size() > 0) {
				resultJson.put("DETSTATUS", "1");
				String detname = jdetail.get(0).getCRBY();
				String detdatetime = dateutils.parsecratDate(jdetail.get(0).getCRAT());
				resultJson.put("DETNAME",detname);
				resultJson.put("DETDATETIME", detdatetime);
			}else {
				resultJson.put("DETSTATUS", "0");
			}
			
			resultJson.put("STATUS", "SUCCESS");
			
		} catch (Exception e) {
			e.printStackTrace();
			DbBean.RollbackTran(ut);
			resultJson.put("STATUS", "FAIL");
		}
		return resultJson;
	}
	
	
	public JSONObject getjournalDetails(HttpServletRequest request, HttpServletResponse response) {
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		
		String id = StrUtils.fString(request.getParameter("hid"));

		UserTransaction ut = null;
		DateUtils dateutils = new DateUtils();
		POSShiftAmountHdrDAO pOSShiftAmountHdrDAO = new POSShiftAmountHdrDAO();
		EmployeeDAO employeeDAO = new EmployeeDAO();
		String result = "";
		JSONObject resultJson = new JSONObject();
	
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
			
			
			JournalService journalService = new JournalEntry();
			List<JournalDetail> jdetail = journalService.getJournalDetailsByHdrId(plant, Integer.valueOf(id));

			DbBean.CommitTran(ut);
			resultJson.put("JOURNAL", jdetail);
			if(jdetail.size() > 0) {
				resultJson.put("DETSTATUS", "1");
			}else {
				resultJson.put("DETSTATUS", "0");
			}
			
			resultJson.put("STATUS", "SUCCESS");
			
		} catch (Exception e) {
			e.printStackTrace();
			DbBean.RollbackTran(ut);
			resultJson.put("STATUS", "FAIL");
		}
		return resultJson;
	}
	
	public JSONObject getjournalDetailsdetid(HttpServletRequest request, HttpServletResponse response) {
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		
		String did = StrUtils.fString(request.getParameter("did"));
		
		

		UserTransaction ut = null;
		DateUtils dateutils = new DateUtils();
		POSShiftAmountHdrDAO pOSShiftAmountHdrDAO = new POSShiftAmountHdrDAO();
		EmployeeDAO employeeDAO = new EmployeeDAO();
		String result = "";
		JSONObject resultJson = new JSONObject();
	
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
			
			JournalDetail jdet = new JournalDAO().getJournalDetailsById(plant, Integer.valueOf(did));
			
			JournalService journalService = new JournalEntry();
			List<JournalDetail> jdetail = journalService.getJournalDetailsByHdrId(plant, jdet.getJOURNALHDRID());

			DbBean.CommitTran(ut);
			resultJson.put("JOURNAL", jdetail);
			if(jdetail.size() > 0) {
				resultJson.put("DETSTATUS", "1");
			}else {
				resultJson.put("DETSTATUS", "0");
			}
			
			resultJson.put("STATUS", "SUCCESS");
			
		} catch (Exception e) {
			e.printStackTrace();
			DbBean.RollbackTran(ut);
			resultJson.put("STATUS", "FAIL");
		}
		return resultJson;
	}
	
	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		// TODO Auto-generated method stub
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE,userCode);
		return loggerDetailsHasMap;
	}

	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		this.mLogger.setLoggerConstans(dataForLogging);
	}

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
	
	static <T> Collector<T,?,List<T>> toSortedList(Comparator<? super T> c) {
	    return Collectors.collectingAndThen(
	        Collectors.toCollection(()->new TreeSet<>(c)), ArrayList::new);
	}

}
