package com.track.service;

import java.util.List;

import com.track.db.object.QuotesHDR;

public interface QuotesHDRService {

	public int addQuotesHDR(QuotesHDR quotesHDR) throws Exception;
	
	public List<QuotesHDR> getAllQuotesHDR(String plant) throws Exception;
	
	public QuotesHDR getQuotesHDRById(String plant,int id) throws Exception;
	
	public int updateQuotesHDR(QuotesHDR quotesHDR,String user) throws Exception;
	
	public boolean DeleteQuotesHDR(String plant,int id) throws Exception;
}
