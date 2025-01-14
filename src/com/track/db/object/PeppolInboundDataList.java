package com.track.db.object;

import java.util.List;

public class PeppolInboundDataList {
	private List<PeppolInboundData> peppollist;

	public List<PeppolInboundData> getPeppollist() {
		return peppollist;
	}

	public void setPeppollist(List<PeppolInboundData> peppollist) {
		this.peppollist = peppollist;
	}

	@Override
	public String toString() {
		return "{\"peppollist\":" + peppollist + "}";
	}
	

	
	
	

}
