package com.track.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class ShopeeService {
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

	public JSONObject getShopeeProductsList(String pageOffset, String plant) throws Exception{
		System.setProperty("https.protocols", "TLSv1.2");
		String url = "";
		JSONObject respoData = null;		
		url = "https://api-integration-shopee-sg.u-clo.com/Middleware_Service/api/v1/products?pagination_entries_per_page=50&pagination_offset="+pageOffset+"&plant="+plant;		
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
		/*String jsonString = "{\r\n" + 
				"  \"items\": [\r\n" + 
				"    {\r\n" + 
				"      \"item_id\": 100019490,\r\n" + 
				"      \"shopid\": 9995,\r\n" + 
				"      \"update_time\": 1624805112,\r\n" + 
				"      \"status\": \"NORMAL\",\r\n" + 
				"      \"item_sku\": \"\",\r\n" + 
				"      \"variations\": [\r\n" + 
				"        {\r\n" + 
				"          \"variation_sku\": \"P210627006\",\r\n" + 
				"          \"variation_id\": 226672,\r\n" + 
				"          \"is_set_item\": false\r\n" + 
				"        },\r\n" + 
				"        {\r\n" + 
				"          \"variation_sku\": \"P210627007\",\r\n" + 
				"          \"variation_id\": 226673,\r\n" + 
				"          \"is_set_item\": false\r\n" + 
				"        },\r\n" + 
				"        {\r\n" + 
				"          \"variation_sku\": \"P210627008\",\r\n" + 
				"          \"variation_id\": 226674,\r\n" + 
				"          \"is_set_item\": false\r\n" + 
				"        }\r\n" + 
				"      ],\r\n" + 
				"      \"is_2tier_item\": true,\r\n" + 
				"      \"tenures\": []\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"item_id\": 100019489,\r\n" + 
				"      \"shopid\": 9995,\r\n" + 
				"      \"update_time\": 1624804512,\r\n" + 
				"      \"status\": \"NORMAL\",\r\n" + 
				"      \"item_sku\": \"P210627005\",\r\n" + 
				"      \"variations\": [],\r\n" + 
				"      \"is_2tier_item\": false,\r\n" + 
				"      \"tenures\": []\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"item_id\": 100019488,\r\n" + 
				"      \"shopid\": 9995,\r\n" + 
				"      \"update_time\": 1624803705,\r\n" + 
				"      \"status\": \"NORMAL\",\r\n" + 
				"      \"item_sku\": \"P210627002\",\r\n" + 
				"      \"variations\": [],\r\n" + 
				"      \"is_2tier_item\": false,\r\n" + 
				"      \"tenures\": []\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"item_id\": 100019487,\r\n" + 
				"      \"shopid\": 9995,\r\n" + 
				"      \"update_time\": 1624803461,\r\n" + 
				"      \"status\": \"NORMAL\",\r\n" + 
				"      \"item_sku\": \"P210627001\",\r\n" + 
				"      \"variations\": [],\r\n" + 
				"      \"is_2tier_item\": false,\r\n" + 
				"      \"tenures\": []\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"      \"item_id\": 129655,\r\n" + 
				"      \"shopid\": 9995,\r\n" + 
				"      \"update_time\": 1624466840,\r\n" + 
				"      \"status\": \"NORMAL\",\r\n" + 
				"      \"item_sku\": \"\",\r\n" + 
				"      \"variations\": [\r\n" + 
				"        {\r\n" + 
				"          \"variation_sku\": \"P210623001\",\r\n" + 
				"          \"variation_id\": 225805,\r\n" + 
				"          \"is_set_item\": false\r\n" + 
				"        },\r\n" + 
				"        {\r\n" + 
				"          \"variation_sku\": \"P210623002\",\r\n" + 
				"          \"variation_id\": 225806,\r\n" + 
				"          \"is_set_item\": false\r\n" + 
				"        },\r\n" + 
				"        {\r\n" + 
				"          \"variation_sku\": \"P210623003\",\r\n" + 
				"          \"variation_id\": 225807,\r\n" + 
				"          \"is_set_item\": false\r\n" + 
				"        }\r\n" + 
				"      ],\r\n" + 
				"      \"is_2tier_item\": true,\r\n" + 
				"      \"tenures\": []\r\n" + 
				"    }\r\n" + 
				"  ],\r\n" + 
				"  \"request_id\": \"c744d992d52e986dafc04938a1073483\",\r\n" + 
				"  \"more\": false,\r\n" + 
				"  \"total\": 5\r\n" + 
				"}";
		return respoData = new org.json.JSONObject(jsonString);*/
		return respoData;
     }
	
	public boolean addShopeeProducts(String itemId, String plant) throws Exception{
		System.setProperty("https.protocols", "TLSv1.2");
		String url = "";
		boolean isAdded = false;
		url = "https://api-integration-shopee-sg.u-clo.com/Middleware_Service/api/v1/product-update?itemId="+itemId+"&plant="+plant;
		
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
	
	public JSONObject UpdateShopeeInventoryItem(String plant,String item,double adjustment) throws Exception{
		System.setProperty("https.protocols", "TLSv1.2");
			String url = "";
			JSONObject respoData = null;
			url = "https://api-integration-shopee-sg.u-clo.com/Middleware_Service/api/v1/inventory-update";
			
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
}
