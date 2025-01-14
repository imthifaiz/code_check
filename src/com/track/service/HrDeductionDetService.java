package com.track.service;

import java.util.List;

import com.track.db.object.HrDeductionDet;

public interface HrDeductionDetService {
	
	public int adddeductiondet(HrDeductionDet deductiondet) throws Exception;
	
	public List<HrDeductionDet> getAlldeductiondet(String plant) throws Exception;
	
	public HrDeductionDet getdeductiondetById(String plant,int id) throws Exception;
	
	public int updatedeductiondet(HrDeductionDet deductiondet,String user) throws Exception;
	
	public boolean Deletedeductiondet(String plant,int id) throws Exception;

}
