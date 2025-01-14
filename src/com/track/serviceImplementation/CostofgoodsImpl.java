package com.track.serviceImplementation;

import java.util.ArrayList;
import java.util.List;

import com.track.dao.BillDAO;
import com.track.dao.ItemMstDAO;
import com.track.db.object.CostofgoodsLanded;
import com.track.db.object.ItemCogs;
import com.track.service.Costofgoods;

public class CostofgoodsImpl implements Costofgoods{

	

	@Override
	public Double calculateLandedWeightBased(CostofgoodsLanded cogLanded) throws Exception {
		// TODO Auto-generated method stub
		double landed_cost=0.0,weighted_quantity=0.0,cost_allocation=0.0,avg_rate=0.0;
		try {
		System.out.println("**** Weight Based **********");
		weighted_quantity=(cogLanded.getWeight()*cogLanded.getQuantity());
		System.out.println("Weighted Quantity Value :"+weighted_quantity);
		if(weighted_quantity>0)
		{
			cost_allocation=((cogLanded.getExpenses_amount()/cogLanded.getWeight_qty())*weighted_quantity);
		}
		else
		{
			cost_allocation=cogLanded.getExpenses_amount();
		}
		
		System.out.println("Allocation Cost Value :"+cost_allocation);
		landed_cost=(cogLanded.getAmount()+cost_allocation);
		System.out.println("Weight Based Landed Cost Value :"+landed_cost);
		avg_rate=(landed_cost/cogLanded.getQuantity());
		System.out.println("Weight Based Avg Value :"+avg_rate);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return avg_rate;
	}

	@Override
	public Double calculateLandedCostBased(CostofgoodsLanded cogLanded) throws Exception {
		double landed_cost=0.0,cost_allocation=0.0,avg_rate=0.0;
		double cost_amount=0.0;
		try {
			System.out.println("**** Cost Based **********");
			cost_amount=((cogLanded.getUnit_cost())*cogLanded.getQuantity());
			System.out.println("Cost Amount Value :"+cost_amount);
			cost_allocation=((cogLanded.getExpenses_amount()/cogLanded.getTotal_cost())*cost_amount);
			System.out.println("Allocation Cost Value :"+cost_allocation);
			landed_cost=(cost_amount+cost_allocation);
			System.out.println("Cost Based Landed Cost Value :"+landed_cost);
			avg_rate=(landed_cost/cogLanded.getQuantity());
			System.out.println("Cost Based Avg Value :"+avg_rate);
	
		}catch(Exception e) {
			e.printStackTrace();
		}
		return avg_rate;
	}

	@Override
	public Double calculateLandedBasedOnBoth(CostofgoodsLanded cogLanded) throws Exception {
		double landed_cost=0.0,weight_allocation=0.0,weighted_quantity=0.0,cost_allocation=0.0,avg_rate=0.0;
		double cost_amount=0.0;
		try {
		
			
			landed_cost=(cost_amount+cost_allocation+weight_allocation);
			System.out.println("Cost Based Landed Cost Value :"+landed_cost);
			avg_rate=(landed_cost/cogLanded.getQuantity());
			System.out.println("LandedBasedOnBoth Avg Value :"+avg_rate);
	
		}catch(Exception e) {
			e.printStackTrace();
		}
		return avg_rate;
	}

	@Override
	public Double calculateTotalCost(List<String> item,CostofgoodsLanded reqObj) throws Exception {
		Double totalCost=0.0,orderDiscount=0.0,discntVal=0.0,shipAmt=0.0,finalAmt=0.0;
		int itemCnt=0;
		List lstamount = new ArrayList();
		try {
				itemCnt=item.size();
			if(!item.isEmpty() && item.size()>0 ) {
				for(int i=0;i<item.size();i++) {
					/*if(reqObj.getOrderdiscount()>0) {
						orderDiscount=((Double.parseDouble(reqObj.getLstamount().get(i).toString()))/100)*(reqObj.getOrderdiscount());
					}*/
					if("%".equals(reqObj.getDiscountType())) {
						if(reqObj.getDiscount()>0) {
						discntVal=((Double.parseDouble(reqObj.getLstamount().get(i).toString()))/100)*(reqObj.getDiscount());
						}
					}else {
						if(reqObj.getDiscount()>0 || reqObj.getOrderdiscount()>0) {
							//discntVal=(reqObj.getDiscount())/itemCnt;
							discntVal=((reqObj.getDiscount()+reqObj.getOrderdiscount())/reqObj.getSub_total()*(Double.parseDouble(reqObj.getLstamount().get(i).toString())));
						}
					}
					/*if(reqObj.getShippingCharge()>0) {
					//shipAmt=reqObj.getShippingCharge()/itemCnt;
					 shipAmt=(reqObj.getShippingCharge()/(reqObj.getSub_total())*(Double.parseDouble(reqObj.getLstamount().get(i).toString())));
					}*/
					
					/*finalAmt=((Double.parseDouble(reqObj.getLstamount().get(i).toString()))-(orderDiscount)-(discntVal)+(shipAmt));*/
					finalAmt=((Double.parseDouble(reqObj.getLstamount().get(i).toString()))-(orderDiscount)-(discntVal));
					totalCost+=finalAmt;
					lstamount.add(finalAmt);
				}
				reqObj.setLstamount(lstamount);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return totalCost;
	}

	@Override
	public Double calculateIndividualAmount(CostofgoodsLanded reqObj,int itemCnt,int i) throws Exception {
		Double finalAmt=0.0,orderDiscount=0.0,discntVal=0.0,shipAmt=0.0;
		try {
				/*if(reqObj.getOrderdiscount()>0) {
					orderDiscount=((Double.parseDouble(reqObj.getLstamount().get(i).toString()))/100)*(reqObj.getOrderdiscount());
				}*/
				if("%".equals(reqObj.getDiscountType())) {
					if(reqObj.getDiscount()>0) {
					discntVal=((Double.parseDouble(reqObj.getLstamount().get(i).toString()))/100)*(reqObj.getDiscount());
					}
				}else {
					if(reqObj.getDiscount()>0 || reqObj.getOrderdiscount()>0) {
					//	discntVal=(reqObj.getDiscount())/itemCnt;
						discntVal=((reqObj.getDiscount()+reqObj.getOrderdiscount())/reqObj.getSub_total()*(Double.parseDouble(reqObj.getLstamount().get(i).toString())));
					}
				}
				/*if(reqObj.getShippingCharge()>0) {
				shipAmt=(reqObj.getShippingCharge()/(reqObj.getSub_total())*(Double.parseDouble(reqObj.getLstamount().get(i).toString())));
				}*/
				
				/*finalAmt=((Double.parseDouble(reqObj.getLstamount().get(i).toString()))-(orderDiscount)-(discntVal)+(shipAmt));*/
				finalAmt=((Double.parseDouble(reqObj.getLstamount().get(i).toString()))-(orderDiscount)-(discntVal));
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return finalAmt;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Double calculateWeightedQty(List item,List Qty,String plant) throws Exception {
		Double weightedQty=0.0;
		ItemMstDAO itemDao=new ItemMstDAO();
		CostofgoodsLanded reqObj=null;
		try {
		for(int i=0;i<item.size();i++) {
			reqObj=itemDao.getItemMSTDetails((String)item.get(i), plant);
			weightedQty+=(reqObj.getNet_weight()*(Double.parseDouble((String) Qty.get(i))));
		}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return weightedQty;
	}
	
	public Double getProductSubtotalAmount(List Amount) {
		double amt=0.0;
		try {
			for(int i=0;i<Amount.size();i++) {
				amt+=(Double.parseDouble((String)Amount.get(i)));
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return amt;
	}
	
	@Override
	public Double calculateCostAllocaiton(CostofgoodsLanded cogLanded) throws Exception {
		double cost_allocation=0.0,cost_amount=0.0,unitCost=0.0;
		try {
			unitCost=cogLanded.getAmount()/cogLanded.getQuantity();
			cost_amount=((cogLanded.getUnit_cost())*cogLanded.getQuantity());
			cost_amount=((unitCost)*cogLanded.getQuantity());
			cost_allocation=((cogLanded.getExpenses_amount()/cogLanded.getTotal_cost())*cost_amount);
			System.out.println("Cost Allocation Amt :"+cost_allocation);
			
	
		}catch(Exception e) {
			e.printStackTrace();
		}
		return cost_allocation;
	}
	
	@Override
	public Double calculateWeightAllocaiton(CostofgoodsLanded cogLanded) throws Exception {
		// TODO Auto-generated method stub
		double weighted_quantity=0.0,weight_allocation=0.0;
		try {
		weighted_quantity=(cogLanded.getWeight()*cogLanded.getQuantity());
		weight_allocation=((cogLanded.getExpenses_amount()/cogLanded.getWeight_qty())*weighted_quantity);
		System.out.println("Weight Allocation Amt :"+weight_allocation);
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return weight_allocation;
	}

	@Override
	public ItemCogs entryProductDetails(String Qty,String Item,String plant,double avg_rate,String due_date) throws Exception {
		double mos_amount=0.0,cos_amount=0.0,cos_qty=0.0,cos_avg=0.0;
		ItemCogs itemCogs=new ItemCogs();
		try {
		BillDAO itemCogsDao=new BillDAO();
		ItemCogs dbItemCogs=itemCogsDao.getItemCogs((String)Item.toString(), plant);
		mos_amount=(avg_rate)*(Double.parseDouble((String)Qty.toString()));
		if(dbItemCogs!=null && dbItemCogs.getOps_qty()!=null) {
			cos_qty=(dbItemCogs.getCos_qty()+Double.parseDouble((String)Qty));
    	    cos_amount=(dbItemCogs.getCos_amount()+mos_amount);
    	    cos_avg=(cos_amount/cos_qty);
    	    
		    itemCogs.setCOGS_DATE(due_date);
		    itemCogs.setPlant(plant);
		    itemCogs.setDescription("Cost of Good Sales");
			itemCogs.setItem((String)Item);
			itemCogs.setOps_amount(dbItemCogs.getCos_amount());
			itemCogs.setOps_avg(dbItemCogs.getCos_avg());
			itemCogs.setOps_qty(dbItemCogs.getCos_qty());
			itemCogs.setMos_qty(Double.parseDouble((String)Qty));
			itemCogs.setMos_amount(mos_amount);
			itemCogs.setMos_avg(avg_rate);
			itemCogs.setCos_amount(cos_amount);
			itemCogs.setCos_avg(cos_avg);
			itemCogs.setCos_qty(cos_qty);
		}else {
    	    itemCogs.setCOGS_DATE(due_date);
		    itemCogs.setPlant(plant);
		    itemCogs.setDescription("Cost of Good Sales");
			itemCogs.setItem((String)Item);
			itemCogs.setOps_amount(0.0);
			itemCogs.setOps_avg(0.0);
			itemCogs.setOps_qty(0.0);
			itemCogs.setMos_qty(Double.parseDouble((String)Qty));
			itemCogs.setMos_amount(mos_amount);
			itemCogs.setMos_avg(avg_rate);
			itemCogs.setCos_amount(mos_amount);
			itemCogs.setCos_avg(avg_rate);
			itemCogs.setCos_qty(Double.parseDouble((String)Qty));
		}
		
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return itemCogs;
	}

	@Override
	public ItemCogs soldProductDetails(String Qty, String Item, String plant, String due_date)
			throws Exception {
		double mos_amount=0.0,cos_amount=0.0,cos_qty=0.0;
		ItemCogs itemCogs=new ItemCogs();
		try {
		BillDAO itemCogsDao=new BillDAO();
		ItemCogs dbItemCogs=itemCogsDao.getItemCogs((String)Item.toString(), plant);
		if(dbItemCogs!=null && dbItemCogs.getOps_qty()!=null) {
			mos_amount=(dbItemCogs.getCos_avg())*(Double.parseDouble((String)Qty.toString()));
			cos_qty=(dbItemCogs.getCos_qty()-(Double.parseDouble((String)Qty)));
    	    cos_amount=(dbItemCogs.getCos_amount()-mos_amount);
    	    
		    itemCogs.setCOGS_DATE(due_date);
		    itemCogs.setPlant(plant);
		    itemCogs.setDescription("Cost of Good Sales - Sold ");
			itemCogs.setItem((String)Item);
			itemCogs.setOps_amount(dbItemCogs.getCos_amount());
			itemCogs.setOps_avg(dbItemCogs.getCos_avg());
			itemCogs.setOps_qty(dbItemCogs.getCos_qty());
			itemCogs.setMos_qty(Double.parseDouble((String)Qty));
			itemCogs.setMos_amount(mos_amount);
			itemCogs.setMos_avg(dbItemCogs.getCos_avg());
			itemCogs.setCos_amount(cos_amount);
			itemCogs.setCos_avg(dbItemCogs.getCos_avg());
			itemCogs.setCos_qty(cos_qty);
		}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return itemCogs;
	}

	@Override
	public ItemCogs revisedSoldProductDetails(String Qty, String edit_Qty, String Item, String edit_Item, String plant,	String due_date) throws Exception {
		double mos_amount=0.0,cos_amount=0.0,cos_qty=0.0,modifyQty=0.0;
		ItemCogs itemCogs=new ItemCogs();
		try {
		if(Qty!=null && edit_Qty!=null) {
			modifyQty=Double.parseDouble(Qty)-Double.parseDouble(edit_Qty);
		}
		BillDAO itemCogsDao=new BillDAO();
		ItemCogs dbItemCogs=itemCogsDao.getItemCogs((String)Item.toString(), plant);
		if(dbItemCogs!=null && dbItemCogs.getOps_qty()!=null) {
			mos_amount=(dbItemCogs.getCos_avg())*(modifyQty); 
			cos_qty=(dbItemCogs.getCos_qty()-(modifyQty)); 
    	    cos_amount=(dbItemCogs.getCos_amount()-mos_amount);
    	    
		    itemCogs.setCOGS_DATE(due_date);
		    itemCogs.setPlant(plant);
		    itemCogs.setDescription("Cost of Good Sales - Sold ");
			itemCogs.setItem((String)Item);
			itemCogs.setOps_amount(dbItemCogs.getCos_amount());
			itemCogs.setOps_avg(dbItemCogs.getCos_avg());
			itemCogs.setOps_qty(dbItemCogs.getCos_qty());
			itemCogs.setMos_qty(Double.parseDouble((String)Qty));
			itemCogs.setMos_amount(mos_amount);
			itemCogs.setMos_avg(dbItemCogs.getCos_avg());
			itemCogs.setCos_amount(cos_amount);
			itemCogs.setCos_avg(dbItemCogs.getCos_avg());
			itemCogs.setCos_qty(cos_qty);
		}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return itemCogs;
	}
	
	@Override
	public Double calculateIndividualAmountForOrderDiscount(CostofgoodsLanded reqObj,int itemCnt,int i) throws Exception {
		Double finalAmt=0.0,orderDiscount=0.0,discntVal=0.0,shipAmt=0.0;
		try {
			if(reqObj.getOrderdiscount()>0) {
				orderDiscount = ((reqObj.getOrderdiscount()/reqObj.getTotal_cost())*reqObj.getAmount());
			}
			finalAmt=((reqObj.getAmount())-(orderDiscount));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return finalAmt;
	}
	
	@Override
	public Double calculateTotalCostForOrderDiscount(List<String> item,CostofgoodsLanded reqObj) throws Exception {
		Double totalCost=0.0,orderDiscount=0.0,discntVal=0.0,shipAmt=0.0,finalAmt=0.0;
		int itemCnt=0;
		try {
				itemCnt=item.size();
			if(!item.isEmpty() && item.size()>0 ) {
				for(int i=0;i<item.size();i++) {
					if(reqObj.getOrderdiscount()>0) {
						orderDiscount = ((reqObj.getOrderdiscount()/reqObj.getTotal_cost())*Double.parseDouble(reqObj.getLstamount().get(i).toString()));
					}
					finalAmt=((Double.parseDouble(reqObj.getLstamount().get(i).toString()))-(orderDiscount));
					totalCost+=finalAmt;
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return totalCost;
	}

}
