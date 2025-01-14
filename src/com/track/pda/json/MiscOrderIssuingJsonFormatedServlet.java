package com.track.pda.json;

import java.io.IOException;

import javax.naming.Context;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.track.dao.InvMstDAO;
import com.track.db.util.DOUtil;
import com.track.db.util.InvMstUtil;
import com.track.db.util.MiscIssuingUtil;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

@Deprecated
public class MiscOrderIssuingJsonFormatedServlet extends HttpServlet {

	private static final long serialVersionUID = 3903920304478077387L;

	private static final String CONTENT_TYPE = "text/html";

	public void init() throws ServletException {
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
