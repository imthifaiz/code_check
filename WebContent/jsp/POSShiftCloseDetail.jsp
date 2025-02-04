<%@ page import="com.track.dao.ShipHisDAO"%>
<%@ page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<%
String title = "POS Shift Close Detail";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
<jsp:param name="mainmenu" value="<%=IConstants.POS_REPORT%>"/>
</jsp:include>
<script language="javascript">

</script>

<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/jspdf.debug.js"></script>
<script src="../jsp/js/jspdf.plugin.autotable.js"></script>
<script src="../jsp/js/Printpagepdf.js"></script>

<div class="container-fluid m-t-20">
			<%
			StrUtils _strUtils = new StrUtils();
			ShipHisDAO _ShipHisDAO = new ShipHisDAO();
			HTReportUtil movHisUtil = new HTReportUtil();
			_ShipHisDAO.setmLogger(mLogger);
			ArrayList movQryList = null;
			DOUtil doUtil = new DOUtil();
			PlantMstDAO _PlantMstDAO = new PlantMstDAO();
			DateUtils dateUtils = new DateUtils();
 
			session = request.getSession();
			String plant = (String) session.getAttribute("PLANT");
			String userID = (String) session.getAttribute("LOGIN_USER");
			String SID = "";
			SID = _strUtils.fString(request.getParameter("SID"));
			String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);

			ArrayList plntList = _PlantMstDAO.getPlantMstDetails(plant);
			Map plntMap = (Map) plntList.get(0);
			String PLNTDESC = (String) plntMap.get("PLNTDESC");
			
			ArrayList shiftcloselist = movHisUtil.getPosShiftCloseDetailByid(Integer.valueOf(SID),plant);
			Map shiftclose = (Map) shiftcloselist.get(0);
			int imti = (int) Double.valueOf((String) shiftclose.get("EXCHANGECOUNT")).doubleValue();
			System.out.println("abcdefghijklmnopqrstuvwxyz "+imti);
			
			String fromdate = dateUtils.getDB2UserDateTime((String) shiftclose.get("FROMDATE"));
			String todate = dateUtils.getDB2UserDateTime((String) shiftclose.get("TODATE"));
			String cashier = (String) shiftclose.get("FNAME");
			
			%>
	<div class="box">
		<!-- Muruganantham Modified on 16.02.2022 -->
		<ol class="breadcrumb backpageul">
			<li><a href="../home"><span class="underline-on-hover">Dashboard</span>
			</a></li>
			<li><a href="../posreports/shiftclose"><span
					class="underline-on-hover">Shift Close Reports</span> </a></li>
			<li><label>Shift Close Details</label></li>
		</ol>

		<!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			
			<div class=" pull-right">
			<div class="btn-group" role="group">
				<button type="button" class="btn btn-default"
					onclick="generatePdf()" data-toggle="tooltip"
					data-placement="bottom" title="PDF">
					<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
				</button>

				<button type="button" class="btn btn-default"
					onclick="generateprintPdf()" data-toggle="tooltip"
					data-placement="bottom" title="Print">
					<i class="fa fa-print" aria-hidden="true"></i>
				</button>

			</div>
		<!-- 	<h1 style="font-size: 18px; cursor: pointer;"
				class="box-title  pull-right" value="PrintWithBatch/Sno"
				name="action"
				onclick="window.location.href='../posreports/shiftclose'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1> -->
			
			<h1 style="font-size: 18px; cursor: pointer; vertical-align:middle;" class="box-title pull-right"
					onClick="window.location.href='../posreports/shiftclose'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
		</div>
			
		</div>
		<div class="box-body">
			<span id="print_id">
				<div class="row">
					<div class="col-xs-3">
					</div>
					<div class="col-xs-6" style="border: 2px solid black">
						<div  style="margin-bottom: 20px;">
						<center>
							<h2><%=PLNTDESC%></h2>
							<h4>End Shift Report</h4>
							<h5>By <%=cashier%></h5>
							<h5><%=fromdate%> - <%=todate%></h5>
						</center>
						</div>
						<table style="font-size: 100%;width: 100%;line-height: 200%;margin-bottom: 15px;" id="table1">
							<tr>
								<td align="left" style="font-weight: bold;">Sales</td>
								<td></td>
								<td></td>
							</tr>
							<tr>
								<td align="left" style="width: 40%;">Total Sales</td>
								<td align="center"><%=(String) shiftclose.get("SALESCOUNT")%></td>
								<td align="right"><%=StrUtils.addZeroes(Double.valueOf((String) shiftclose.get("TOTALSALES")), numberOfDecimal)%></td>
							</tr>
							<tr>
								<td align="left">Voided Sales </td>
								<td align="center"><%=(String) shiftclose.get("VOIDSALESCOUNT")%></td>
								<td align="right"><%=StrUtils.addZeroes(Double.valueOf((String) shiftclose.get("VOIDEDSALES")), numberOfDecimal)%></td>
							</tr>
							<tr>
								<td align="left">Voided Products </td>
								<td align="center"><%=(String) shiftclose.get("VOIDITEMCOUNT")%></td>
								<td align="right"><%=StrUtils.addZeroes(Double.valueOf((String) shiftclose.get("RETURNEDAMOUNT")), numberOfDecimal)%></td>
							</tr>
							<tr>
								<td align="left">Exchanged Products </td>
								<td align="center"><%=(int) Double.valueOf((String) shiftclose.get("EXCHANGECOUNT")).doubleValue()%></td>
								<td align="right"><%=StrUtils.addZeroes(Double.valueOf((String) shiftclose.get("EXCHANGE")), numberOfDecimal)%></td>
							</tr>
							<tr>
								<td align="left" style="font-weight: bold;">Sales By Pay Mode</td>
								<td></td>
								<td></td>
							</tr>
							<%
							movQryList = movHisUtil.getPosShiftClosePaymentModeByHdrid(Integer.valueOf(SID),plant);
							for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
								Map lineArr = (Map) movQryList.get(iCnt);
							%>
							<tr>
								<td align="left"><%=(String) lineArr.get("PAYMENTMODE")%></td>
								<td align="center"><%=(String) lineArr.get("SALESCOUNT")%></td>
								<td align="right"><%=StrUtils.addZeroes(Double.valueOf((String) lineArr.get("AMOUNT")), numberOfDecimal)%></td>
							</tr>
							<%}%>
							<tr>
								<td align="left" style="font-weight: bold;">Discounts</td>
								<td></td>
								<td></td>
							</tr>
							<tr>
								<td align="left">Total Discount</td>
								<td align="center"></td>
								<td align="right"><%=StrUtils.addZeroes(Double.valueOf((String) shiftclose.get("TOTALDISCOUNT")), numberOfDecimal)%></td>
							</tr>
							<tr>
								<td align="left" style="font-weight: bold;">Actual Sales</td>
								<td></td>
								<td></td>
							</tr>
							<%
								double asales = (Double.valueOf((String) shiftclose.get("TOTALSALES")) + Double.valueOf((String) shiftclose.get("TOTALDISCOUNT")))-(Double.valueOf((String) shiftclose.get("RETURNEDAMOUNT")));
								String tsales = StrUtils.addZeroes(asales, numberOfDecimal);
								double fasales = Double.valueOf((String) shiftclose.get("TOTALSALES"))-(Double.valueOf((String) shiftclose.get("RETURNEDAMOUNT")));
							%>
							<tr>
								<td align="left">Total Sales</td>
								<td align="center"><%=(String) shiftclose.get("SALESCOUNT")%></td>
								<td align="right"><%=tsales%></td>
							</tr>
							<tr>
								<td align="left">Total Discount</td>
								<td align="center"></td>
								<td align="right"><%=StrUtils.addZeroes(Double.valueOf((String) shiftclose.get("TOTALDISCOUNT")), numberOfDecimal)%></td>
							</tr>
							
							<tr>
								<td align="left">Actual Sales</td>
								<td align="center"><%=(String) shiftclose.get("SALESCOUNT")%></td>
								<%-- <td align="right"><%=StrUtils.addZeroes(Double.valueOf((String) shiftclose.get("TOTALSALES")), numberOfDecimal)%></td> --%>
								<td align="right"><%=StrUtils.addZeroes(fasales, numberOfDecimal)%></td>
							</tr>
							<tr>
								<td align="left" style="font-weight: bold;">Activity</td>
								<td></td>
								<td></td>
							</tr>
							<tr>
								<td align="left">Float</td>
								<td></td>
								<td align="right"><%=StrUtils.addZeroes(Double.valueOf((String) shiftclose.get("FLOATAMOUNT")), numberOfDecimal)%></td>
							</tr>
							<tr>
								<td align="left">Expense </td>
								<td></td>
								<td align="right"><%=StrUtils.addZeroes(Double.valueOf((String) shiftclose.get("EXPENSE")), numberOfDecimal)%></td>
							</tr>
							<tr>
								<td align="left">Cash </td>
								<td></td>
								<td align="right"><%=StrUtils.addZeroes(Double.valueOf((String) shiftclose.get("CASH")), numberOfDecimal)%></td>
							</tr>
							<tr>
								<td align="left">Expected Drawer amount </td>
								<td></td>
								<td align="right"><%=StrUtils.addZeroes(Double.valueOf((String) shiftclose.get("EXPECTEDDRAWERAMOUNT")), numberOfDecimal)%></td>
							</tr>
							<tr>
								<td align="left">Drawer Amount </td>
								<td></td>
								<td align="right"><%=StrUtils.addZeroes(Double.valueOf((String) shiftclose.get("DRAWERAMOUNT")), numberOfDecimal)%></td>
							</tr>
							<%
							//double se = Double.valueOf((String) shiftclose.get("EXPECTEDDRAWERAMOUNT")) - Double.valueOf((String) shiftclose.get("DRAWERAMOUNT"));
							double se = Double.valueOf((String) shiftclose.get("DRAWERAMOUNT")) - Double.valueOf((String) shiftclose.get("EXPECTEDDRAWERAMOUNT"));
							String shortextra = StrUtils.addZeroes(se, numberOfDecimal);
							/*if(Double.valueOf((String) shiftclose.get("EXPECTEDDRAWERAMOUNT")) > Double.valueOf((String) shiftclose.get("DRAWERAMOUNT"))){
								shortextra = "-"+shortextra;
							}*/
							%>
							<tr>
								<td align="left">Short/Extra</td>
								<td></td>
								<td align="right"><%=shortextra%></td>
							</tr>
							
						</table>
						<div style="margin-bottom: 20px;">
							<center><h5>Shift No:<%=(String) shiftclose.get("SHIFTID")%> | Outlet:<%=(String) shiftclose.get("OUTLET_NAME")%> | Terminal:<%=(String) shiftclose.get("TERMINAL_NAME")%></h5...............................></center>
						</div>
					</div>
					<div class="col-xs-3">
					</div>
				</div>
			</span>
			<!-- <div class="form-group">
					<div class="col-sm-12" align="center">
						  	<button type="button" class="Submit btn btn-default"  value="PrintWithBatch/Sno"  name="action" onclick="window.location.href='javascript:history.back()'"><b>Back</b></button>
					</div>
				</div> -->
				
			<script type="text/javascript">
			
			function printData()
			{
			   var divToPrint=document.getElementById("print_id");
			   newWin= window.open("");
			   newWin.document.write(divToPrint.outerHTML);
			   newWin.print();
			   newWin.close();
			}

			$('#printdetail').on('click',function(){
			printData();
			})
			
			
			
			function generatePdf(){
				var doc = new jsPDF('p', 'mm', 'a4');
				var pageNumber;
				
				var width = doc.internal.pageSize.getWidth();
				/* Top Right */
				doc.setFontSize(15);
				doc.setFontStyle("bold");
				doc.text('<%=PLNTDESC%>', width/2 , 30, {align:'center'});

				doc.setFontSize(13);
				doc.setFontStyle("bold");
				doc.text('End Shift Report', width/2, 37, {align:'center'});

				doc.setFontSize(10);
				doc.setFontStyle("normal");
				doc.text('By <%=cashier%>', width/2, 44, {align:'center'});
				
				doc.setFontSize(10);
				doc.text('<%=fromdate%> - <%=todate%>', width/2, 51, {align:'center'});
				
				doc.autoTable({
					html : '#table1',
					startY : 59,
					columnStyles : {0 : {cellWidth:  width/3,halign : 'left'},1 : {halign : 'center'},2 : {halign : 'right'}},
					theme : 'plain'
				});
				/* **** */

				doc.setFontStyle("normal");
				if(pageNumber < doc.internal.getNumberOfPages()){
					// Footer
					var str = "Page " + doc.internal.getNumberOfPages()
					// Total page number plugin only available in jspdf v1.0+
					if (typeof doc.putTotalPages === 'function') {
						str = str + " of " + totalPagesExp;
					}
					doc.setFontSize(10);
	
					// jsPDF 1.4+ uses getWidth, <1.4 uses .width
					var pageSize = doc.internal.pageSize;
					var pageHeight = pageSize.height ? pageSize.height
							: pageSize.getHeight();
					doc.text(str, 16, pageHeight - 10);
				}
				
				doc.save('ShiftCloseDetailReport.pdf');
			}
			
			
			function generateprintPdf(){
				var doc = new jsPDF('p', 'mm', 'a4');
				var pageNumber;
				
				var width = doc.internal.pageSize.getWidth();
				/* Top Right */
				doc.setFontSize(15);
				doc.setFontStyle("bold");
				doc.text('<%=PLNTDESC%>', width/2 , 30, {align:'center'});

				doc.setFontSize(13);
				doc.setFontStyle("bold");
				doc.text('End Shift Report', width/2, 37, {align:'center'});

				doc.setFontSize(10);
				doc.setFontStyle("normal");
				doc.text('By <%=cashier%>', width/2, 44, {align:'center'});
				
				doc.setFontSize(10);
				doc.text('<%=fromdate%> - <%=todate%>', width/2, 51, {align:'center'});
				
				doc.autoTable({
					html : '#table1',
					startY : 59,
					columnStyles : {0 : {cellWidth:  width/3,halign : 'left'},1 : {halign : 'center'},2 : {halign : 'right'}},
					theme : 'plain'
				});
				/* **** */

				doc.setFontStyle("normal");
				if(pageNumber < doc.internal.getNumberOfPages()){
					// Footer
					var str = "Page " + doc.internal.getNumberOfPages()
					// Total page number plugin only available in jspdf v1.0+
					if (typeof doc.putTotalPages === 'function') {
						str = str + " of " + totalPagesExp;
					}
					doc.setFontSize(10);
	
					// jsPDF 1.4+ uses getWidth, <1.4 uses .width
					var pageSize = doc.internal.pageSize;
					var pageHeight = pageSize.height ? pageSize.height
							: pageSize.getHeight();
					doc.text(str, 16, pageHeight - 10);
				}
				
				doc.autoPrint();
				const hiddFrame = document.createElement('iframe');
				hiddFrame.style.position = 'fixed';
				hiddFrame.style.width = '1px';
				hiddFrame.style.height = '1px';
				hiddFrame.style.opacity = '0.01';
				const isSafari = /^((?!chrome|android).)*safari/i.test(window.navigator.userAgent);
				if (isSafari) {
				  // fallback in safari
				  hiddFrame.onload = () => {
				    try {
				      hiddFrame.contentWindow.document.execCommand('print', false, null);
				    } catch (e) {
				      hiddFrame.contentWindow.print();
				    }
				  };
				}
				hiddFrame.src = doc.output('bloburl');
				document.body.appendChild(hiddFrame);
			}
			</script>
		</div>


	</div>
</div>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="nobackblock" value="1" />
</jsp:include>