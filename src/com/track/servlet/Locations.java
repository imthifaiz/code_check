package com.track.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.track.util.StrUtils;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.BillDAO;
import com.track.dao.BillPaymentDAO;
import com.track.dao.InvoicePaymentDAO;
import com.track.dao.LocationsDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.SupplierCreditDAO;
import com.track.dao.TaxReturnDAO;
import com.track.dao.TaxReturnFilingDAO;
import com.track.dao.TaxSettingDAO;
import com.track.db.object.InvPaymentAttachment;
import com.track.db.object.TaxReturnFillAdjust;
import com.track.db.object.TaxReturnFillDet;
import com.track.db.object.TaxReturnFillHdr;
import com.track.db.object.TaxReturnPaymentDet;
import com.track.db.object.TaxReturnPaymentHdr;
import com.track.db.object.TaxReturnTransactionSummary;
import com.track.db.util.BillPaymentUtil;
import com.track.db.util.TaxSettingUtil;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.Numbers;
import com.track.util.StrUtils;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/Locations")
public class Locations  extends HttpServlet implements IMLogger  {
	private static final long serialVersionUID = 1L;
	/*
	 * private boolean printLog =
	 * MLoggerConstant.TaxSettingServlet_PRINTPLANTMASTERLOG; private boolean
	 * printInfo = MLoggerConstant.TaxSettingServlet_PRINTPLANTMASTERINFO;
	 */
	String action = "";
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		action = StrUtils.fString(request.getParameter("action")).trim();
		LocationsDAO locationDAO=new LocationsDAO();
		DateUtils dateutils = new DateUtils();
		List<Hashtable<String,String>> countryList = null;
		List<Hashtable<String,String>> stateList = null;
		Hashtable<String,String> country = null;
		Hashtable<String,String> state = null;
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		System.out.println("Locations Servlet");
		if (action.equalsIgnoreCase("generateLocation")) {
			String path=DbBean.Locations;
			countryList=new ArrayList<Hashtable<String,String>>();
			   // parsing file "locations.json" 
			try(JsonReader jsonReader = new JsonReader(
	                new InputStreamReader(
	                        new FileInputStream(path), StandardCharsets.UTF_8))) {
				locationDAO.truncateCountry();
				locationDAO.truncateState();
	            Gson gson = new GsonBuilder().create();
	            jsonReader.beginArray(); //start of json array
	            while (jsonReader.hasNext()){ //next json array element
	            	JsonObject jsonObject=gson.fromJson(jsonReader, JsonObject.class);
	            	country = new Hashtable<String, String>();
	            	country.put("PLANT",plant);
	            	country.put("COUNTRY_CODE", jsonObject.get("iso2").getAsString());
	            	country.put("COUNTRYNAME", jsonObject.get("name").getAsString());
	            	country.put("REGION", "");
	            	country.put("PHONE_CODE", jsonObject.get("phone_code").getAsString());
	            	country.put("CAPITAL", jsonObject.get("capital").getAsString());
	            	country.put("CURRENCY", jsonObject.get("currency").getAsString());
	            	country.put("CRAT", dateutils.getDateTime());
	            	country.put("CRBY", username);
	            	countryList.add(country);
	            	stateList=new ArrayList<Hashtable<String,String>>();
	            	JsonArray jarray=jsonObject.getAsJsonArray("states");
	            	for(JsonElement stateArr:jarray)
	            	{
	            		JsonObject stateObj=stateArr.getAsJsonObject();
	            		state = new Hashtable<String, String>();
	            		state.put("PLANT", plant);
	            		state.put("COUNTRY_CODE", jsonObject.get("iso2").getAsString());
	            		state.put("STATE", stateObj.get("name").getAsString());
	            		state.put("CRAT", dateutils.getDateTime());
	            		state.put("CRBY", username);
	            		stateList.add(state);
	            	}
	            	locationDAO.addMultipleStates(stateList,plant);	
	            
	            	 //System.out.println("Locations:"+jsonObject.get("name"));
	            }
	            jsonReader.endArray();
	            locationDAO.addMultipleCountries(countryList, plant);
	        }
	        catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		action = StrUtils.fString(req.getParameter("action")).trim();
		
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
}
