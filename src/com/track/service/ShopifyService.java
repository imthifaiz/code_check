package com.track.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.track.util.StrUtils;

import net.sf.json.JSONArray;

public class ShopifyService {
	public JSONObject getShopifyLocationList(String plant) throws Exception{
		System.setProperty("https.protocols", "TLSv1.2");
		String url = "";
		JSONObject respoData = null;		
		url = "https://api-integration-sg.u-clo.com/inventory-0.0.1/api/sales-order/location?plant="+plant;		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();	
		try {		
		con.setRequestMethod("GET");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestProperty("content-type", "application/json");
		con.setDoOutput(true);
	
		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);
		
		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer resp = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				resp.append("{ locations: "+inputLine + "}");
			}
			in.close();
			System.out.println(resp.toString());
			respoData = new org.json.JSONObject(resp.toString());
		} else {
			System.out.println("GET request not worked");
		}	
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respoData;

     }
	
	public JSONObject UpdateShopifyInventoryItem(String plant,String item,double adjustment) throws Exception{
		System.setProperty("https.protocols", "TLSv1.2");
			String url = "";
			JSONObject respoData = null;
			url = "https://api-integration-sg.u-clo.com/inventory-0.0.1/api/sales-order/inventory-update";
			
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			try {					
			JSONObject inventory_Params = new JSONObject();
			inventory_Params.put("plant", plant); 
			inventory_Params.put("sku", item);
			inventory_Params.put("available", adjustment);
				
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("content-type", "application/json");
			con.setDoOutput(true);
			
			OutputStreamWriter wr= new OutputStreamWriter(con.getOutputStream());
			wr.write(inventory_Params.toString());
			wr.flush();
			wr.close();	
		
			int responseCode = con.getResponseCode();
			System.out.println("POST Response Code :: " + responseCode);
			
			if (responseCode == HttpURLConnection.HTTP_OK) { //success
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer resp = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					resp.append(inputLine);
				}
				in.close();
				System.out.println(resp.toString());
				if ("".equals(resp.toString())) {
					System.out.println("POST request not worked. Empty response from POST request.");
				}else {
					respoData = new org.json.JSONObject(resp.toString());
				}
			} else {
				System.out.println("POST request not worked");
			}	
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return respoData;
     }

	public JSONObject getShopifyProductsList(String plant) throws Exception{
		System.setProperty("https.protocols", "TLSv1.2");
		String url = "";
		JSONObject respoData = null;		
		url = "https://api-integration-sg.u-clo.com/inventory-0.0.1/api/sales-order/products?plant="+plant;		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();	
		try {		
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("content-type", "application/json");
			con.setDoOutput(true);
		
			int responseCode = con.getResponseCode();
			System.out.println("POST Response Code :: " + responseCode);
			
			if (responseCode == HttpURLConnection.HTTP_OK) { //success
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer resp = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					resp.append(inputLine);
				}
				in.close();
				System.out.println(resp.toString());
				respoData = new org.json.JSONObject(resp.toString());
			} else {
				System.out.println("GET request not worked");
			}	
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respoData;
     }
	
	public boolean addShopifyProducts(JSONObject jsonData) throws Exception{
		System.setProperty("https.protocols", "TLSv1.2");
		String url = "";
		boolean isAdded = false;
		JSONObject respoData = null;
		url = "https://api-integration-sg.u-clo.com/inventory-0.0.1/api/sales-order/product-update";
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		try {				
		con.setRequestMethod("POST");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestProperty("content-type", "application/json");
		con.setDoOutput(true);
		
		OutputStreamWriter wr= new OutputStreamWriter(con.getOutputStream());
		wr.write(jsonData.toString());
		wr.flush();
		wr.close();	
	
		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);
		
		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer resp = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				resp.append(inputLine);
			}
			in.close();
			System.out.println(resp.toString());
			//respoData = new org.json.JSONObject(resp.toString());
			isAdded = true;
		} else {
			System.out.println("POST request not worked");
		}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isAdded;

     }
}
