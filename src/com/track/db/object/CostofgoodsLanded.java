package com.track.db.object;

import java.util.ArrayList;
import java.util.List;

public class CostofgoodsLanded {
	
	private String prod_id;
	
	private Double weight;
	
	private Double cost;
	
	private Double quantity;
	
	private Double subtotal;
	
	private String landed_type;
	
	private Double amount; 
	
	private Double discount;
	
	private Double orderdiscount;
	
	private String discountType;
	
	private Double shippingCharge;
	
	private String account_name;
	
	private String landedcostcal;
	
	private String item;
	
	private Double expenses_amount;
	
	private Double weight_qty;
	
	private Double total_cost;
	
	private String billhdrid;
	
	private Double landed_cost;
	
	private Double avg_rate;
	
	private Double unit_cost;
	
	private Double net_weight;
	
	private Double sub_total;
	
	private List lstamount=new ArrayList<>();
	
	

	public String getProd_id() {
		return prod_id;
	}

	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	
	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public String getLanded_type() {
		return landed_type;
	}

	public void setLanded_type(String landed_type) {
		this.landed_type = landed_type;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getOrderdiscount() {
		return orderdiscount;
	}

	public void setOrderdiscount(Double orderdiscount) {
		this.orderdiscount = orderdiscount;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public Double getShippingCharge() {
		return shippingCharge;
	}

	public void setShippingCharge(Double shippingCharge) {
		this.shippingCharge = shippingCharge;
	}

	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	public String getLandedcostcal() {
		return landedcostcal;
	}

	public void setLandedcostcal(String landedcostcal) {
		this.landedcostcal = landedcostcal;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Double getExpenses_amount() {
		return expenses_amount;
	}

	public void setExpenses_amount(Double expenses_amount) {
		this.expenses_amount = expenses_amount;
	}

	public Double getWeight_qty() {
		return weight_qty;
	}

	public void setWeight_qty(Double weight_qty) {
		this.weight_qty = weight_qty;
	}

	
	public List getLstamount() {
		return lstamount;
	}

	public void setLstamount(List lstamount) {
		this.lstamount = lstamount;
	}

	public Double getTotal_cost() {
		return total_cost;
	}

	public void setTotal_cost(Double total_cost) {
		this.total_cost = total_cost;
	}

	public Double getLanded_cost() {
		return landed_cost;
	}

	public void setLanded_cost(Double landed_cost) {
		this.landed_cost = landed_cost;
	}

	public String getBillhdrid() {
		return billhdrid;
	}

	public void setBillhdrid(String billhdrid) {
		this.billhdrid = billhdrid;
	}

	public Double getAvg_rate() {
		return avg_rate;
	}

	public void setAvg_rate(Double avg_rate) {
		this.avg_rate = avg_rate;
	}

	public Double getUnit_cost() {
		return unit_cost;
	}

	public void setUnit_cost(Double unit_cost) {
		this.unit_cost = unit_cost;
	}

	public Double getNet_weight() {
		return net_weight;
	}

	public void setNet_weight(Double net_weight) {
		this.net_weight = net_weight;
	}

	public Double getSub_total() {
		return sub_total;
	}

	public void setSub_total(Double sub_total) {
		this.sub_total = sub_total;
	}

	@Override
	public String toString() {
		return "CostofgoodsLanded [prod_id=" + prod_id + ", weight=" + weight + ", cost=" + cost + ", quantity="
				+ quantity + ", subtotal=" + subtotal + ", landed_type=" + landed_type + ", amount=" + amount
				+ ", discount=" + discount + ", orderdiscount=" + orderdiscount + ", discountType=" + discountType
				+ ", shippingCharge=" + shippingCharge + ", account_name=" + account_name + ", landedcostcal="
				+ landedcostcal + ", item=" + item + ", expenses_amount=" + expenses_amount + ", weight_qty="
				+ weight_qty + ", total_cost=" + total_cost + ", billhdrid=" + billhdrid + ", landed_cost="
				+ landed_cost + ", avg_rate=" + avg_rate + ", unit_cost=" + unit_cost + ", net_weight=" + net_weight
				+ ", sub_total=" + sub_total + ", lstamount=" + lstamount + "]";
	}
}
