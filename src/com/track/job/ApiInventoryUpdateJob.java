package com.track.job;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import com.track.constants.IConstants;
import com.track.dao.IntegrationsDAO;
import com.track.dao.ItemMstDAO;
import com.track.db.util.InvUtil;
import com.track.service.ShopeeService;
import com.track.service.ShopifyService;

public class ApiInventoryUpdateJob implements Runnable{
	private String ITEM_NUM = "", PLANT = "";
	public ApiInventoryUpdateJob(String item, String plant){
		ITEM_NUM = item;
		PLANT = plant;
	}
	
	@Override
	public void run() {
		try{
			String availqty ="0";
			ArrayList invQryList = null;
			Hashtable<String,String> htCond = new Hashtable<String,String>();
			invQryList= new InvUtil().getInvListSummaryAvailableQtyforShopify(PLANT, ITEM_NUM, 
					new ItemMstDAO().getItemDesc(PLANT, ITEM_NUM),htCond);
			htCond.put(IConstants.PLANT, PLANT);
			if(new IntegrationsDAO().getShopifyConfigCount(htCond,"")) {
				if(new IntegrationsDAO().getShopifyItemCount(ITEM_NUM, PLANT)) {
					if (invQryList.size() > 0) {					
						for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
                            Map lineArr = (Map) invQryList.get(iCnt);
                            availqty = (String)lineArr.get("AVAILABLEQTY");
                            System.out.println(availqty);
						}
						double availableqty = Double.parseDouble(availqty);
   						new ShopifyService().UpdateShopifyInventoryItem(PLANT, ITEM_NUM, availableqty);
					}
				}
			}
			if(new IntegrationsDAO().getShopeeConfigCount(htCond,"")) {
				if(new IntegrationsDAO().getShopeeItemCount(ITEM_NUM, PLANT)) {
					if (invQryList.size() > 0) {					
						for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
                            Map lineArr = (Map) invQryList.get(iCnt);
                            availqty = (String)lineArr.get("AVAILABLEQTY");
                            System.out.println(availqty);
						}
						double availableqty = Double.parseDouble(availqty);
   						new ShopeeService().UpdateShopeeInventoryItem(PLANT, ITEM_NUM, availableqty);
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}