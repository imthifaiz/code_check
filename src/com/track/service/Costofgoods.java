package com.track.service;

import java.util.List;

import com.track.db.object.CostofgoodsLanded;
import com.track.db.object.ItemCogs;

public interface Costofgoods {
	
	public Double calculateLandedWeightBased(CostofgoodsLanded cogLanded) throws Exception;
	
	public Double calculateLandedCostBased(CostofgoodsLanded cogLanded) throws Exception;
	
	public Double calculateLandedBasedOnBoth(CostofgoodsLanded cogLanded) throws Exception;
	
	public Double calculateTotalCost(List<String> item,CostofgoodsLanded reqObj) throws Exception;
	
	public Double calculateIndividualAmount(CostofgoodsLanded reqObj,int itemCnt,int i) throws Exception;
	
	public Double calculateWeightedQty(List item,List Qty,String plant) throws Exception;
	
	public Double getProductSubtotalAmount(List Amount) throws Exception;
	
	public Double calculateCostAllocaiton(CostofgoodsLanded cogLanded) throws Exception;
	
	public Double calculateWeightAllocaiton(CostofgoodsLanded cogLanded) throws Exception;
	
	public ItemCogs entryProductDetails(String Qty,String Item,String plant,double avg_rate,String due_date) throws Exception;
	
	public ItemCogs soldProductDetails(String Qty,String Item,String plant,String due_date) throws Exception;
	
	public ItemCogs revisedSoldProductDetails(String Qty,String edit_Qty,String Item,String edit_Item,String plant,String due_date) throws Exception;
	
	public Double calculateIndividualAmountForOrderDiscount(CostofgoodsLanded reqObj,int itemCnt,int i) throws Exception;
	
	public Double calculateTotalCostForOrderDiscount(List<String> item,CostofgoodsLanded reqObj) throws Exception;
	
}
