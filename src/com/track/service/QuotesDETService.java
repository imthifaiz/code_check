package com.track.service;

import java.util.List;

import com.track.db.object.QuotesDET;

public interface QuotesDETService {

	public int addQuotesDET(QuotesDET quotesDET) throws Exception;
	
	public List<QuotesDET> getAllQuotesDET(String plant) throws Exception;
	
	public QuotesDET getQuotesDETById(String plant,int id) throws Exception;
	
	public int updateQuotesDET(QuotesDET quotesDET,String user) throws Exception;
	
	public boolean DeleteQuotesDET(String plant,int id) throws Exception;
	
	public List<QuotesDET> getAllQuotesDETByQID(String plant, int QUOTESHDRID) throws Exception;
	
}
