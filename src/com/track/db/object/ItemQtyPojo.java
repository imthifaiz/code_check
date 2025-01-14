package com.track.db.object;

public class ItemQtyPojo {
	
	@DBTable(columnName ="item")
	private String item;
		
	@DBTable(columnName ="qty")
	private double qty;

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	@Override
	public String toString() {
		return "ItemQtyPojo [item=" + item + ", qty=" + qty + ", getItem()=" + getItem() + ", getQty()=" + getQty()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}
	
}
