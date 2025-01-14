package com.track.tables;

import java.io.Serializable;

public class CATALOGMST implements Serializable{
	private static final long serialVersionUID = 1L;
	private String catlogID;
	private String catlogPath;
	private String productID;
	private String description1;
	private String description2;
	private String description3;
	private float price;
	private int quantity;
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getCatlogID() {
		return catlogID;
	}
	public void setCatlogID(String catlogID) {
		this.catlogID = catlogID;
	}
	public String getCatlogPath() {
		return catlogPath;
	}
	public void setCatlogPath(String catlogPath) {
		this.catlogPath = catlogPath;
	}
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public String getDescription1() {
		return description1;
	}
	public void setDescription1(String description1) {
		this.description1 = description1;
	}
	public String getDescription2() {
		return description2;
	}
	public void setDescription2(String description2) {
		this.description2 = description2;
	}
	public String getDescription3() {
		return description3;
	}
	public void setDescription3(String description3) {
		this.description3 = description3;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
}
