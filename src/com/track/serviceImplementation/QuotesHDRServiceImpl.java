package com.track.serviceImplementation;

import java.util.List;

import com.track.dao.QuotesHdrDAO;
import com.track.db.object.QuotesHDR;
import com.track.service.QuotesHDRService;

public class QuotesHDRServiceImpl implements QuotesHDRService {
	
	private QuotesHdrDAO quotesHdrDAO = new QuotesHdrDAO();

	@Override
	public int addQuotesHDR(QuotesHDR quotesHDR) throws Exception {
		return quotesHdrDAO.addQuotesHDR(quotesHDR);
	}

	@Override
	public List<QuotesHDR> getAllQuotesHDR(String plant) throws Exception {
		return quotesHdrDAO.getAllQuotesHDR(plant);
	}

	@Override
	public QuotesHDR getQuotesHDRById(String plant, int id) throws Exception {
		return quotesHdrDAO.getQuotesHDRById(plant, id);
	}

	@Override
	public int updateQuotesHDR(QuotesHDR quotesHDR, String user) throws Exception {
		return quotesHdrDAO.updateQuotesHDR(quotesHDR, user);
	}

	@Override
	public boolean DeleteQuotesHDR(String plant, int id) throws Exception {
		return quotesHdrDAO.DeleteQuotesHDR(plant, id);
	}

}
