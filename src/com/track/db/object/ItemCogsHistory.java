package com.track.db.object;

public class ItemCogsHistory {
	
	@DBTable(columnName ="SNO")
	private int sno;
	
	@DBTable(columnName ="PRODUCT_ID")
	private String product_id;
	
	@DBTable(columnName ="DATE")
	private String date;
	
	@DBTable(columnName ="DESCRIPTION")
	private String description;
	
	@DBTable(columnName ="OPS_QTY")
	private String ops_qty;
	
	@DBTable(columnName ="OPS_AMOUNT")
	private String ops_amount;
	
	@DBTable(columnName ="OPS_AVG")
	private String ops_avg;
	
	@DBTable(columnName ="MOS_QTY")
    private String mos_qty;
	
	@DBTable(columnName ="MOS_AMOUNT")
	private String mos_amount;
	
	@DBTable(columnName ="MOS_AVG")
	private String mos_avg;
	
	@DBTable(columnName ="COS_QTY")
    private String cos_qty;
	
	@DBTable(columnName ="COS_AMOUNT")
	private String cos_amount;
	
	@DBTable(columnName ="COS_AVG")
	private String cos_avg;

	public int getSno() {
		return sno;
	}

	public void setSno(int sno) {
		this.sno = sno;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOps_qty() {
		return ops_qty;
	}

	public void setOps_qty(String ops_qty) {
		this.ops_qty = ops_qty;
	}

	public String getOps_amount() {
		return ops_amount;
	}

	public void setOps_amount(String ops_amount) {
		this.ops_amount = ops_amount;
	}

	public String getOps_avg() {
		return ops_avg;
	}

	public void setOps_avg(String ops_avg) {
		this.ops_avg = ops_avg;
	}

	public String getMos_qty() {
		return mos_qty;
	}

	public void setMos_qty(String mos_qty) {
		this.mos_qty = mos_qty;
	}

	public String getMos_amount() {
		return mos_amount;
	}

	public void setMos_amount(String mos_amount) {
		this.mos_amount = mos_amount;
	}

	public String getMos_avg() {
		return mos_avg;
	}

	public void setMos_avg(String mos_avg) {
		this.mos_avg = mos_avg;
	}

	public String getCos_qty() {
		return cos_qty;
	}

	public void setCos_qty(String cos_qty) {
		this.cos_qty = cos_qty;
	}

	public String getCos_amount() {
		return cos_amount;
	}

	public void setCos_amount(String cos_amount) {
		this.cos_amount = cos_amount;
	}

	public String getCos_avg() {
		return cos_avg;
	}

	public void setCos_avg(String cos_avg) {
		this.cos_avg = cos_avg;
	}


	
	

}
