package com.track.service;

import java.util.List;

import com.track.db.object.HrDeductionHdr;

public interface HrDeductionHdrService {

	public int adddeductionhdr(HrDeductionHdr deductionhdr) throws Exception;
	
	public List<HrDeductionHdr> getAlldeductionhdr(String plant) throws Exception;
	
	public HrDeductionHdr getdeductionhdrById(String plant,int id) throws Exception;
	
	public int updatedeductionhdr(HrDeductionHdr deductionhdr,String user) throws Exception;
	
	public boolean Deletedeductionhdr(String plant,int id) throws Exception;
	
}
