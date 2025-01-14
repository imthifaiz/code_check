package com.track.db.object;

public class ItemCogs {
	
	@DBTable(columnName ="ID")
	private int ID;
	
	@DBTable(columnName ="PLANT")
	private String plant;
	
	@DBTable(columnName ="item")
	private String item;
	
	@DBTable(columnName ="COGS_DATE")
	private String COGS_DATE;
	
	@DBTable(columnName ="DESCRIPTION")
	private String description;
	
	@DBTable(columnName ="OPS_QTY")
	private Double ops_qty;
	
	@DBTable(columnName ="OPS_AMOUNT")
	private Double ops_amount;
	
	@DBTable(columnName ="OPS_AVG")
	private Double ops_avg;
	
	@DBTable(columnName ="MOS_QTY")
    private Double mos_qty;
	
	@DBTable(columnName ="MOS_AMOUNT")
	private Double mos_amount;
	
	@DBTable(columnName ="MOS_AVG")
	private Double mos_avg;
	
	@DBTable(columnName ="COS_QTY")
    private Double cos_qty;
	
	@DBTable(columnName ="COS_AMOUNT")
	private Double cos_amount;
	
	@DBTable(columnName ="COS_AVG")
	private Double cos_avg;

	@DBTable(columnName ="CRBY")
	private String crby;
	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getPlant() {
		return plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getCOGS_DATE() {
		return COGS_DATE;
	}

	public void setCOGS_DATE(String cOGS_DATE) {
		COGS_DATE = cOGS_DATE;
	}

	public Double getOps_qty() {
		return ops_qty;
	}

	public void setOps_qty(Double ops_qty) {
		this.ops_qty = ops_qty;
	}

	public Double getOps_amount() {
		return ops_amount;
	}

	public void setOps_amount(Double ops_amount) {
		this.ops_amount = ops_amount;
	}

	public Double getOps_avg() {
		return ops_avg;
	}

	public void setOps_avg(Double ops_avg) {
		this.ops_avg = ops_avg;
	}

	public Double getMos_qty() {
		return mos_qty;
	}

	public void setMos_qty(Double mos_qty) {
		this.mos_qty = mos_qty;
	}

	public Double getMos_amount() {
		return mos_amount;
	}

	public void setMos_amount(Double mos_amount) {
		this.mos_amount = mos_amount;
	}

	public Double getMos_avg() {
		return mos_avg;
	}

	public void setMos_avg(Double mos_avg) {
		this.mos_avg = mos_avg;
	}

	public Double getCos_qty() {
		return cos_qty;
	}

	public void setCos_qty(Double cos_qty) {
		this.cos_qty = cos_qty;
	}

	public Double getCos_amount() {
		return cos_amount;
	}

	public void setCos_amount(Double cos_amount) {
		this.cos_amount = cos_amount;
	}

	public Double getCos_avg() {
		return cos_avg;
	}

	public void setCos_avg(Double cos_avg) {
		this.cos_avg = cos_avg;
	}

	public String getCrby() {
		return crby;
	}

	public void setCrby(String crby) {
		this.crby = crby;
	}


	
	
	
	

}
