package com.track.db.object;

import java.util.List;

public class BillPojo {

	private BillHdr billhdr;
	
	private List<BillDet> billdet;

	public BillHdr getBillhdr() {
		return billhdr;
	}

	public void setBillhdr(BillHdr billhdr) {
		this.billhdr = billhdr;
	}

	public List<BillDet> getBilldet() {
		return billdet;
	}

	public void setBilldet(List<BillDet> billdet) {
		this.billdet = billdet;
	}

	@Override
	public String toString() {
		return "BillPojo [billhdr=" + billhdr + ", billdet=" + billdet + "]";
	}
	
	
}
