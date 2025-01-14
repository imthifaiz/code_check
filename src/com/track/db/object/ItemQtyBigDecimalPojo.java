package com.track.db.object;

import java.math.BigDecimal;

public class ItemQtyBigDecimalPojo {
	@DBTable(columnName ="item")
	private String item;
		
	@DBTable(columnName ="qty")
	private BigDecimal qty;

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	@Override
	public String toString() {
		return "ItemQtyBigDecimalPojo [item=" + item + ", qty=" + qty + "]";
	}
	
	
}
