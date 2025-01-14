package com.track.pda.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import net.sf.json.JSONObject;

import com.track.constants.IConstants;
import com.track.dao.InvMstDAO;
import com.track.db.util.MiscReceivingUtil;
import com.track.db.util.POUtil;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

@Deprecated
public class MiscOrderReceivingJsonFormatedServlet extends HttpServlet {

	private static final long serialVersionUID = -8615543381443551729L;

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
