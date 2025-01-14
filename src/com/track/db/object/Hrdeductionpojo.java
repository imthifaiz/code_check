package com.track.db.object;

import java.util.List;

public class Hrdeductionpojo {
	
	private HrDeductionHdr hrDeductionHdr;
	
	private List<HrDeductionDet> hrDeductionDet;

	public HrDeductionHdr getHrDeductionHdr() {
		return hrDeductionHdr;
	}

	public void setHrDeductionHdr(HrDeductionHdr hrDeductionHdr) {
		this.hrDeductionHdr = hrDeductionHdr;
	}

	public List<HrDeductionDet> getHrDeductionDet() {
		return hrDeductionDet;
	}

	public void setHrDeductionDet(List<HrDeductionDet> hrDeductionDet) {
		this.hrDeductionDet = hrDeductionDet;
	}

	@Override
	public String toString() {
		return "Hrdeductionpojo [hrDeductionHdr=" + hrDeductionHdr + ", hrDeductionDet=" + hrDeductionDet + "]";
	}
	
	

}
