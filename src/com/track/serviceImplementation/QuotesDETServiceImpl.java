package com.track.serviceImplementation;

import java.util.List;

import com.track.dao.QuotesDetDAO;
import com.track.db.object.QuotesDET;
import com.track.service.QuotesDETService;

public class QuotesDETServiceImpl implements QuotesDETService {
	
	private QuotesDetDAO quotesDetDAO = new QuotesDetDAO();

	@Override
	public int addQuotesDET(QuotesDET quotesDET) throws Exception {
		return quotesDetDAO.addQuotesDET(quotesDET);
	}

	@Override
	public List<QuotesDET> getAllQuotesDET(String plant) throws Exception {
		return quotesDetDAO.getAllQuotesDET(plant);
	}

	@Override
	public QuotesDET getQuotesDETById(String plant, int id) throws Exception {
		return quotesDetDAO.getQuotesDETById(plant, id);
	}

	@Override
	public int updateQuotesDET(QuotesDET quotesDET, String user) throws Exception {
		return quotesDetDAO.updateQuotesDET(quotesDET, user);
	}

	@Override
	public boolean DeleteQuotesDET(String plant, int id) throws Exception {
		return quotesDetDAO.DeleteQuotesDET(plant, id);
	}

	@Override
	public List<QuotesDET> getAllQuotesDETByQID(String plant, int QUOTESHDRID) throws Exception {
		return quotesDetDAO.getAllQuotesDETByQID(plant, QUOTESHDRID);
	}

}
