<%
		java.util.List do_list = new java.util.ArrayList();
		java.util.List do_list1 = null;
		com.track.util.StrUtils strUtils     = new com.track.util.StrUtils();
		com.track.util.DateUtils dateUtils = new com.track.util.DateUtils();
		com.track.db.util.HTReportUtil  movHisUtil= new com.track.db.util.HTReportUtil();
		java.util.Hashtable ht = new java.util.Hashtable();
	        java.util.HashMap<String, String> loggerDetailsHasMap = new java.util.HashMap<String, String>();
		loggerDetailsHasMap.put(com.track.util.MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
		loggerDetailsHasMap.put(com.track.util.MLogger.USER_CODE, com.track.util.StrUtils.fString((String) session.getAttribute("LOGIN_USER")).trim());
		com.track.util.MLogger mLogger = new com.track.util.MLogger();
		mLogger.setLoggerConstans(loggerDetailsHasMap);
		movHisUtil.setmLogger(mLogger);
               
		String FROM_DATE ="",TO_DATE = "", DIRTYPE ="",ITEM="",BATCH="",fdate="",tdate="",JOBNO="",ITEMNO="",DESC="",ORDERNO="",
		CUSTOMER="",xlaction="",USER="",LOC="",REASONCODE="",LOC_TYPE_ID="",TYPE="",PRD_CLS_ID="",PRD_TYPE_ID="",PRD_BRAND_ID= "",PRD_DEPT_ID="",REMARKS="";
		FROM_DATE     = strUtils.fString(request.getParameter("FROM_DATE"));

		if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();

		if (FROM_DATE.length()>5)
			fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

		TO_DATE       = strUtils.fString(request.getParameter("TO_DATE"));

		if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();

		if (TO_DATE.length()>5)
			tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

		session=request.getSession();
		String plant = (String)session.getAttribute("PLANT");
                 String USERID= session.getAttribute("LOGIN_USER").toString();
		DIRTYPE       = strUtils.fString(request.getParameter("DIRTYPE"));
		JOBNO         = strUtils.fString(request.getParameter("JOBNO"));
		USER          = strUtils.fString(request.getParameter("USER"));
		ITEMNO        = strUtils.fString(request.getParameter("ITEM"));
        DESC          = strUtils.fString(request.getParameter("DESC"));
		ORDERNO       = strUtils.fString(request.getParameter("ORDERNO"));
		CUSTOMER      = strUtils.fString(request.getParameter("CUSTOMER"));
        BATCH         = strUtils.fString(request.getParameter("BATCH"));
        LOC           = strUtils.fString(request.getParameter("LOC_0"));   
        REASONCODE    = strUtils.fString(request.getParameter("REASONCODE")); 
        LOC_TYPE_ID =  strUtils.fString(request.getParameter("LOC_TYPE_ID"));
        TYPE =  strUtils.fString(request.getParameter("TYPE"));
        PRD_CLS_ID =  strUtils.fString(request.getParameter("PRD_CLS_ID"));
        PRD_TYPE_ID =  strUtils.fString(request.getParameter("PRD_TYPE_ID"));
        PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
        PRD_DEPT_ID = strUtils.fString(request.getParameter("PRD_DEPT_ID"));
        REMARKS= strUtils.fString(request.getParameter("REMARKS"));
                
		xlaction=strUtils.fString(request.getParameter("xlAction"));
                
                
       
                //if(strUtils.fString(DIRTYPE).length() > 0)  ht.put("DIRTYPE",DIRTYPE);
                if(strUtils.fString(JOBNO).length() > 0)    ht.put("JOBNUM",JOBNO);
                if(strUtils.fString(USER).length() > 0)     ht.put("CRBY",USER);
                if(strUtils.fString(ITEMNO).length() > 0)   ht.put("ITEM",ITEMNO);
                if(strUtils.fString(ORDERNO).length() > 0)  ht.put("ORDNUM",ORDERNO);
                if(strUtils.fString(CUSTOMER).length() > 0) ht.put("CUSTNO",CUSTOMER);
                if(strUtils.fString(BATCH).length() > 0)    ht.put("BATNO",BATCH);
               // if(strUtils.fString(LOC).length() > 0)      ht.put("LOC",LOC);
                
		if(xlaction.equalsIgnoreCase("GenerateXLSheet")){
		try{
			//do_list=movHisUtil.getMovHisList(ht,plant,fdate,tdate,USERID,DESC);
			do_list=movHisUtil.getMovHisListWithRemarks(ht,plant,fdate,tdate,USERID,DESC,REASONCODE,LOC_TYPE_ID,"","",LOC,DIRTYPE,TYPE,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID,PRD_DEPT_ID,REMARKS);

		if(do_list.size() > 0)
		{ 
  			response.setContentType("application/vnd.ms-excel");
  			response.setHeader("Content-disposition", "attachment; filename=MovementReport.xls");
  			java.util.Vector v =  null;
                        %>
                         <TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
                                <TR BGCOLOR="#000066">
                                
                                <TH width="3%"><font color="#ffffff" align="center">S/N</TH>
                                <TH width="10%"><font color="#ffffff" align="left"><b>Date</TH>
                                <TH width="10%"><font color="#ffffff" align="left"><b>Time</TH>
                                <TH width="10%"><font color="#ffffff" align="left"><b>Logs Type</TH>
                                <TH width="10%"> <FONT color="#ffffff">Order No</FONT></TH>
                                <TH width="10%"><font color="#ffffff" align="left"><b>Loc</TH>
                                <TH width="10%"><font color="#ffffff" align="left"><b>Product ID</TH>
                                <TH width="10%"><font color="#ffffff" align="left"><b>Description</TH>
                                <TH width="10%"><font color="#ffffff" align="left"><b>Batch</TH>
                                <TH width="4%"><font color="#ffffff" align="left"><b>Qty</TH>
                                <TH width="10%"><font color="#ffffff" align="left"><b>User</TH>
                                <TH width="12%"><font color="#ffffff" align="left"><b>Remarks</TH>
        
                             </tr>
                        <%
  			//out.println("S/N\tDate\tTime\tMovementType\tOrder No\tLocation\tProduct ID\tBatch\tQty\tUser\tRemarks");
  			for(int i = 0; i<do_list.size(); i++){
  			try{
  				java.util.Map  map = (java.util.Map)do_list.get(i);
                                int iIndex = i + 1;
                                String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
     			        String trDate= "";
 				trDate=(String)map.get("CRAT");
          
 				if (trDate.length()>8)
 					trDate    = trDate.substring(8,10)+":"+ trDate.substring(10,12)+":"+trDate.substring(12,14);
                                        %>
                                        <TR bgcolor = "<%=bgcolor%>">
                                            <TD align="center" width="3%"><%=iIndex%></TD>
                                            <TD align= "left" width="10%">&nbsp;&nbsp;&nbsp;<%=(String)map.get("TRANDATE")%></TD>
                                            <TD align= "left" width="10%">&nbsp;&nbsp;&nbsp;<%=trDate%></TD>
                                            <TD align= "left" width="1%">&nbsp;&nbsp;&nbsp;<%=(String)map.get("DIRTYPE") %></TD>
                                            <TD align="left" width="10%">&nbsp;&nbsp;&nbsp;<%=(String)map.get("ORDNUM") %></TD>
                                             <TD align= "left" width="10%">&nbsp;&nbsp;&nbsp;<%=(String)map.get("LOC") %></TD>
                                              <TD align= "left" width="10%">&nbsp;&nbsp;&nbsp;<%=(String)map.get("ITEM") %></TD>
                                           <TD align= "left" width="10%">&nbsp;&nbsp;&nbsp;<%=(String)map.get("ITEMDESC") %></TD>
                                            <TD align= "left" width="10%">&nbsp;&nbsp;&nbsp;<%=(String)map.get("BATNO") %></TD>
                                             <TD align= "left" width="5%">&nbsp;&nbsp;&nbsp;<%=(String)map.get("QTY").toString() %></TD>
                                            <TD align= "left" width="10%">&nbsp;&nbsp;&nbsp;<%=(String)map.get("CRBY")%></TD>
                                             <TD align= "left" width="12%">&nbsp;&nbsp;&nbsp;<%=(String)map.get("REMARKS")%></TD>
                                          
                                           </TR>
                                        <%
 					//out.println(String.valueOf(i)+"\t"+(String)map.get("TRANDATE")+"\t"+trDate+"\t"+ (String)map.get("DIRTYPE")+"\t" + (String)map.get("ORDNUM")+"\t" + (String)map.get("LOC")+"\t"+(String)map.get("ITEM")+"\t"+(String)map.get("BATNO")+"\t"+(String)map.get("QTY").toString()+"\t"+(String)map.get("CRBY")+"\t"+(String)map.get("REMARKS"));
 
 				}catch(Exception ee){System.out.println("######################## MovementExcel ################ :"+ee.toString());}
  			}%></TABLE>
                        <%
 		}else if(do_list.size() < 1){
  			response.setContentType("application/vnd.ms-excel");
  			response.setHeader("Content-disposition", "attachment; filename=MovementReport.xls");
  			out.println("No Records Found To List");
	}

	}catch(Exception e){throw new Exception ("Error Occurred");}
}


%>
