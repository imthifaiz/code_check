<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.constants.MenuConstants"%>
<%@ page language="java"
	import="java.util.*,java.sql.*,java.io.*,java.net.*"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%--New page design begin --%>
<%
	String title = "Dashboard";
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>
<%@include file="header.jsp"%>
<%--New page design end --%>
<jsp:useBean id="gn" class="com.track.gates.Generator" />
<%
	//String company = "";
	//company=request.getParameter(COMPANY);
	//System.out.println("company....indexpage3.."+company);
	String imageRootPath = "images/IndexPage/";
	String tableWidth = "85%";
	String tableWidthLi = "85%";
%>
<style>
.nav>li>a:hover, .nav>li>a:active, .nav>li>a:focus {
    color: #444;
    background: #f7f7f7;
}
</style>
<link rel="stylesheet" href="css/dashboard.css">

<link rel="stylesheet" href="css/core_main.css">
<link rel="stylesheet" href="css/daygrid_main.css">
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
StrUtils strUtils = new StrUtils();
String plant = strUtils.fString((String) session.getAttribute("PLANT"));
String username = strUtils.fString((String) session.getAttribute("LOGIN_USER"));
String defaultTab="";
boolean accMgt=false, activeClass=true, activeSection=true;
accMgt = ub.isCheckVal("homeaccountingmanaget", plant,username);
	if(accMgt==true)
	{		
		System.out.println("homeaccountingmanaget");
	}
	
	boolean acc=false;
	acc = ub.isCheckVal("homeaccounting", plant,username);
	if(acc==true)
	{		
		System.out.println("homeaccounting");
	}
	
	boolean pur=false;
	pur = ub.isCheckVal("homepurchase", plant,username);
	if(pur==true)
	{		
		System.out.println("homepurchase");
	}
	
	boolean sal=false;
	sal = ub.isCheckVal("homesales", plant,username);
	if(sal==true)
	{		
		System.out.println("homesales");
	}
	
	boolean war=false;
	war = ub.isCheckVal("homewarehouse", plant,username);
	if(war==true)
	{		
		System.out.println("homewarehouse");
	}
	PlantMstDAO plantMstDAO = new PlantMstDAO();
    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
	%>
	
<form name="form">
	<input name="numberOfDecimal" type="hidden" value="<%=numberOfDecimal%>">
	<input name="baseCurrency" type="hidden" value="<%=session.getAttribute("BASE_CURRENCY")%>">	
</form>

<section class="content dashboard">
	<ul class="nav nav-tabs">
		<% if (accMgt) { %>
	    <li class="<%= (activeClass) ? "active" : "" %>"><a data-toggle="tab" href="#accountMgtDashboard">Accounting Management</a></li>
	    <% activeClass=false;
	    }%>
	    <% if (acc) { %>
	    <li class="<%= (activeClass) ? "active" : "" %>"><a data-toggle="tab" href="#accountsDashboard">Accounting</a></li>
	    <% activeClass=false;
	    }%>
	    <% if (pur) { %>
	    <li class="<%= (activeClass) ? "active" : "" %>"><a data-toggle="tab" href="#purchaseDashboard">Purchase</a></li>
	    <% activeClass=false;
	    }%>
	    <% if (sal) { %>
	    <li class="<%= (activeClass) ? "active" : "" %>"><a data-toggle="tab" href="#salesDashboard">Sales</a></li>
	     <% activeClass=false;
	    }%>
	    <% if (war) { %>
	    <li class="<%= (activeClass) ? "active" : "" %>"><a data-toggle="tab" href="#warehouseDashboard">Warehouse</a></li>
  		<% activeClass=false;
	    }%>
  	</ul>

  <div class="tab-content">
  	
  	<div id="accountMgtDashboard" class="tab-pane fade<%= (accMgt) ? " in active" : "" %>">
  		<div class="row">
      		<br>
      		<div class="col-xs-12 col-sm-3">
	      		<div class="dashboard-stats__item dashboard-stats__item_red">
	            	<span class="dashboard_heading">Total Asset</span>
	            	<div class="custom-select totalAssetSel">
		            	<select style="width: 100%;">
							<option selected="selected">Last 30 days</option>
							<option>Last 30 days</option>
							<option>This month</option>
							<option>This quarter</option>
							<option>This year</option>
							<option>Last month</option>
							<option>Last quarter</option>
							<option>Last year</option>
						</select>
	            	</div>
	              <i class="fa fa-home"></i>
	              <h3 class="dashboard-stats__title">
	              	<span style="float: left;font-size: 0.5em;font-weight: 600;"><%=session.getAttribute("BASE_CURRENCY")%></span>
	                <span id="totalAsset" data-decimals="<%=numberOfDecimal%>"></span>
	              </h3>
	            </div>
      		</div>
      		
      		<div class="col-xs-12 col-sm-3">			
            <div class="dashboard-stats__item dashboard-stats__item_dark-green">
            	<span class="dashboard_heading">Total Liability</span>
            	<div class="custom-select totalLiabilitySel">
	            	<select style="width: 100%;">
						<option selected="selected">Last 30 days</option>
						<option>Last 30 days</option>
						<option>This month</option>
						<option>This quarter</option>
						<option>This year</option>
						<option>Last month</option>
						<option>Last quarter</option>
						<option>Last year</option>
					</select>
            	</div>
              <i class="fa fa-balance-scale"></i>
              <h3 class="dashboard-stats__title">
              	<span style="float: left;font-size: 0.5em;font-weight: 600;"><%=session.getAttribute("BASE_CURRENCY")%></span>
                <span id="totalLiability" data-decimals="<%=numberOfDecimal%>"></span>
              </h3>
            </div>            
         </div>
         
         <div class="col-xs-12 col-sm-3">			
            <div class="dashboard-stats__item dashboard-stats__item_light-blue">
            	<span class="dashboard_heading">Net Profit</span>
            	<div class="custom-select netProfitSel">
	            	<select style="width: 100%;">
						<option selected="selected">Last 30 days</option>
						<option>Last 30 days</option>
						<option>This month</option>
						<option>This quarter</option>
						<option>This year</option>
						<option>Last month</option>
						<option>Last quarter</option>
						<option>Last year</option>
					</select>
            	</div>
              <i class="fa fa-line-chart"></i>
              <h3 class="dashboard-stats__title">
              	<span style="float: left;font-size: 0.5em;font-weight: 600;"><%=session.getAttribute("BASE_CURRENCY")%></span>
                <span id="netProfit" data-decimals="<%=numberOfDecimal%>"></span>
              </h3>
            </div>            
         </div>
         
         <div class="col-xs-12 col-sm-3">			
            <div class="dashboard-stats__item dashboard-stats__item_purple">
            	<span class="dashboard_heading">Gross Profit</span>
            	<div class="custom-select grossProfitSel">
	            	<select style="width: 100%;">
						<option selected="selected">Last 30 days</option>
						<option>Last 30 days</option>
						<option>This month</option>
						<option>This quarter</option>
						<option>This year</option>
						<option>Last month</option>
						<option>Last quarter</option>
						<option>Last year</option>
					</select>
            	</div>
              <i class="fa fa-money"></i>
              <h3 class="dashboard-stats__title">
              	<span style="float: left;font-size: 0.5em;font-weight: 600;"><%=session.getAttribute("BASE_CURRENCY")%></span>
                <span id="grossProfit" data-decimals="<%=numberOfDecimal%>"></span>
              </h3>
            </div>            
         </div>
      	</div>
      	
      	<div class="row">
	      	<div class="col-xs-12 col-sm-6">
	       		<div class="panel panel-default">
	       			<div class="panel-heading dashboard-stats__item_purple">
	             		<h3 class="panel-title">
	               			Total Purchases <span id="totalPurchaseByBillSumryForMgt"></span>
		               </h3>
		               <div class="custom-select totalPurchaseByBillSumrySelForMgt">
			            	<select style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
		           		</div>
	            	</div>
		            <div class="panel-body">
		             	<canvas id="canvas17"></canvas>
		            </div>  			
	       		</div>	          	
	        </div>
	        
	        <div class="col-xs-12 col-sm-6">
	       		<div class="panel panel-default">
	       			<div class="panel-heading dashboard-stats__item_green2">
	             		<h3 class="panel-title">
	               			Total Sales <span id="totalSalesByInvoiceSumryForMgt"></span>
		               </h3>
		               <div class="custom-select totalSalesByInvoiceSumrySelForMgt">
			            	<select style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
		           		</div>
	            	</div>
		            <div class="panel-body">
		             	<canvas id="canvas18"></canvas>
		            </div>  			
	       		</div>	          	
	        </div>
        </div>
        
        <div class="row">
	      	<div class="col-xs-12 col-sm-6">
	       		<div class="panel panel-default" style="min-height:319px;">
	       			<div class="panel-heading dashboard-stats__item_green2">
	             		<h3 class="panel-title">
	               			Total Income <span id="totalIncomeForMgt"></span>
		               </h3>
		               <div class="custom-select totalIncomeForMgtSel">
			            	<select style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
		           		</div>
	            	</div>
		            <div class="panel-body">
		             	<canvas id="canvas19"></canvas>
		             	<h5 class="totalIncomeMsg" style="position: absolute;width: 100%;top: 50%;text-align: center;right: 0;">
		            		No Income were found in this time frame
	            		</h5>
		            </div>  			
	       		</div>	          	
	        </div>
	        
	        <div class="col-xs-12 col-sm-6">
	       		<div class="panel panel-default account-panel">
	       			<div class="panel-heading dashboard-stats__item_purple">
	             		<h3 class="panel-title">
	               			Total Income
		               </h3>
		               <div class="custom-select totalIncomeByInvoiceForMgtSumrySel">
			            	<select style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
		           		</div>
	            	</div>
		            <div class="panel-body">
		             	<canvas id="canvas20"></canvas>
		            </div>  			
	       		</div>	          	
        	</div>
        </div>
        
        <div class="row">
	      	<div class="col-xs-12 col-sm-6">
	       		<div class="panel panel-default" style="min-height:319px;">
	       			<div class="panel-heading dashboard-stats__item_purple">
	             		<h3 class="panel-title">
	               			Total Expense <span id="totalExpenseForMgt"></span>
		               </h3>
		               <div class="custom-select totalExpenseForMgtSel">
			            	<select style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
		           		</div>
	            	</div>
		            <div class="panel-body">
		             	<canvas id="canvas21"></canvas>
		             	<h5 class="totalExpenseMsg" style="position: absolute;width: 100%;top: 50%;text-align: center;right: 0;">
		            		No Expense were found in this time frame
	            		</h5>
		            </div>  			
	       		</div>	          	
	        </div>
	        
	        <div class="col-xs-12 col-sm-6">
	       		<div class="panel panel-default account-panel">
	       			<div class="panel-heading dashboard-stats__item_green2">
	             		<h3 class="panel-title">
	               			Total Expense
		               </h3>
		               <div class="custom-select totalExpenseByBillSumryForMgtSel">
			            	<select style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
		           		</div>
	            	</div>
		            <div class="panel-body">
		             	<canvas id="canvas22"></canvas>
		            </div>  			
	       		</div>	          	
	        </div>
        </div>
        
        <div class="row">
	      <div class="col-xs-12 col-sm-6">
		      	<div class="col-xs-12 no-padding">
		      		<div class="panel panel-default account-panel">
		       			<div class="panel-heading dashboard-stats__item_green2">
		             		<h3 class="panel-title">
		               			Total Account Payable <span id="totalAccPayForMgt"></span>
			               </h3>
			               <div class="custom-select totalAccPaySelForMgt">
				            	<select style="width: 100%;">
									<option selected="selected">Last 30 days</option>
									<option>Last 30 days</option>
									<option>This month</option>
									<option>This quarter</option>
									<option>This year</option>
									<option>Last month</option>
									<option>Last quarter</option>
									<option>Last year</option>
								</select>
			           		</div>
		            	</div>
			            <div class="panel-body">
			             	<canvas id="canvas23"></canvas>
			             	<h5 class="totalAccPayMsg" style="position: absolute;width: 100%;top: 50%;text-align: center;right: 0;">
			            		No Account Payable were found in this time frame
		            		</h5>
			            </div>  			
		       		</div>
		      	</div>
		      	
		      	<div class="col-xs-12 no-padding">
       			<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_purple">
                		<h3 class="panel-title">
                  			Account Payable <span id="accPayForMgt"></span>
		                </h3>
		                <div class="custom-select accPaySelForMgt">
			            	<select style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
		            	</div>
		             </div>
		             <div class="panel-body">
		              	<div class="table-responsive">
							<table id="tableAccPayForMgt" class="table table-bordred table-striped">
								<thead>
									<tr>
										<th style="width:40%">Supplier</th>
										<th style="width:20%">Total Payable</th>
										<th colspan="2" style="width:40%">Chart</th>
									</tr>
								</thead>
							</table>
						</div>
						<div class="col-xs-12">
							<div style="height:13px;width:30px;display:inline-block;background:#3cb371;vertical-align:middle">
							</div>
							<span>NON PDC PAYMENT</span>
							&nbsp;&nbsp;
							<div style="height:13px;width:30px;display:inline-block;background:#e41b1b;vertical-align:middle">
							</div>
							<span>PDC PAYMENT</span>
						</div>
		             </div>          			
         		</div>       			
       		</div>
      		</div>
      		
      		<div class="col-xs-12 col-sm-6">
	     		<div class="col-sm-12 no-padding">
	   				<div class="panel panel-default">
	          			<div class="panel-heading dashboard-stats__item_purple">
	                		<h3 class="panel-title">
	                  			Supplier Ageing Summary
		                </h3>
		                <div class="custom-select supAgeSelForMgt">
			            	<select style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
		            	</div>
		             </div>
		             <div class="panel-body">
		              	<div class="table-responsive">
							<table id="tableSupAgeForMgt" class="table table-bordred table-striped">
								<thead>
									<tr>
										<th>Supplier</th>
										<th>Total Amount Due</th>
										<th>30 days</th>
										<th>31-60 days</th>
										<th>61-90 days</th>
										<th>90+ days</th>
									</tr>
								</thead>
							</table>
						</div>
		             </div>          			
         		</div>       			
       		</div>
     	</div>
    	</div>
    	
    	<div class="row">
	      <div class="col-xs-12 col-sm-6">
	      	<div class="col-xs-12 no-padding">
	      		<div class="panel panel-default account-panel">
	       			<div class="panel-heading dashboard-stats__item_purple">
	             		<h3 class="panel-title">
	               			Total Account Receivable <span id="totalAccRecvForMgt"></span>
		               </h3>
		               <div class="custom-select totalAccRecvForMgtSel">
			            	<select style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
		           		</div>
	            	</div>
		            <div class="panel-body">
		             	<canvas id="canvas24"></canvas>
		             	<h5 class="totalAccRecvMsg" style="position: absolute;width: 100%;top: 50%;text-align: center;right: 0;">
		            		No Account Receivable were found in this time frame
	            		</h5>
		            </div>  			
	       		</div>
	      	</div>
	      	
	      	<div class="col-xs-12 no-padding">
	       			<div class="panel panel-default">
	          			<div class="panel-heading dashboard-stats__item_green2">
	                		<h3 class="panel-title">
	                  			Account Receivable <span id="accRecvForMgt"></span>
			                </h3>
			                <div class="custom-select accRecvSelForMgt">
				            	<select style="width: 100%;">
									<option selected="selected">Last 30 days</option>
									<option>Last 30 days</option>
									<option>This month</option>
									<option>This quarter</option>
									<option>This year</option>
									<option>Last month</option>
									<option>Last quarter</option>
									<option>Last year</option>
								</select>
			            	</div>
			             </div>
			             <div class="panel-body">
			              	<div class="table-responsive">
								<table id="tableRecvPayForMgt" class="table table-bordred table-striped">
									<thead>
										<tr>
											<th style="width:40%">Customer</th>
											<th style="width:20%">Total Receivable</th>
											<th colspan="2" style="width:40%">Chart</th>
										</tr>
									</thead>
								</table>
							</div>
							<br/>
							<div class="col-xs-12">
								<div style="height:13px;width:30px;display:inline-block;background:#e41b1b;vertical-align:middle">
								</div>
								<span>NON PDC PAYMENT</span>
								&nbsp;&nbsp;
								<div style="height:13px;width:30px;display:inline-block;background:#3cb371;vertical-align:middle">
								</div>
								<span>PDC PAYMENT</span>
							</div>
			             </div>          			
	         		</div>       			
	       		</div>
       	</div>
       	<div class="col-xs-12 col-sm-6">
       		<div class="col-sm-12 no-padding">
   			<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_green2">
                		<h3 class="panel-title">
                  			Customer Ageing Summary
		                </h3>
		                <div class="custom-select custAgeSelForMgt">
			            	<select style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
		            	</div>
		             </div>
		             <div class="panel-body">
		              	<div class="table-responsive">
							<table id="tablecustAgeForMgt" class="table table-bordred table-striped">
								<thead>
									<tr>
										<th>Customer</th>
										<th>Total Amount Due</th>
										<th>30 days</th>
										<th>31-60 days</th>
										<th>61-90 days</th>
										<th>90+ days</th>
									</tr>
								</thead>
							</table>
						</div>
		             </div>          			
         		</div>       			
       		</div>
       	</div>
       	</div>
       	
       	<div class="row">
      	<div class="col-sm-6">
       			<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_green2">
                		<h3 class="panel-title">
                  			PDC Payable
		                </h3>
		                <div class="custom-select payPdcSelForMgt">
			            	<select style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
		            	</div>
		             </div>
		             <div class="panel-body">
		              	<div class="table-responsive">
							<table id="tablePaymentPdcForMgt" class="table table-bordred table-striped">
								<thead>
									<tr>
										<th>Payment ID</th>
										<th>Payment Date</th>
										<th>Supplier</th>
										<th>Bank</th>
										<th>Cheque No</th>
										<th>Cheque Date</th>
										<th>Cheque Reversed Date</th>
										<th>Amount</th>
									</tr>
								</thead>
							</table>
							<br>
							<button class="btn btn-sm btn-default pull-right" onclick="redirectToPaymentPdcSummary()" style="color: #337ab7;">Process Payment</button>
						</div>
		             </div>          			
         		</div>       			
       		</div>
       		
       		<div class="col-sm-6">
       			<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_purple">
                		<h3 class="panel-title">
                  			PDC Receivable
		                </h3>
		                <div class="custom-select payRecvPdcForMgtSel">
			            	<select style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
		            	</div>
		             </div>
		             <div class="panel-body">
		              	<div class="table-responsive">
							<table id="tablePaymentRecvPdcForMgt" class="table table-bordred table-striped">
								<thead>
									<tr>
										<th>Payment ID</th>
										<th>Payment Date</th>
										<th>Customer</th>
										<th>Bank</th>
										<th>Cheque No</th>
										<th>Cheque Date</th>
										<th>Cheque Reversed Date</th>
										<th>Amount</th>
									</tr>
								</thead>
							</table>
							<br>
							<button class="btn btn-sm btn-default pull-right" onclick="redirectToPaymentReceivePdcSummary()" style="color: #337ab7;">Process Payment</button>
						</div>
		             </div>          			
         		</div>       			
       		</div>
     	</div>
 	</div>
 	<% if (accMgt) { 
 		activeSection=false;
 		defaultTab="accountMgtDashboard";
    }%>
  	
    <div id="accountsDashboard" class="tab-pane fade<%= (acc && activeSection) ? " in active" : "" %>">
      <div class="row">
      	<br>
      	<div class="col-xs-12  col-sm-offset-1 col-sm-4">
      		<div class="dashboard-stats__item dashboard-stats__item_red">
            	<span class="dashboard_heading">Total Purchases</span>
            	<div class="custom-select totalPurchaseByBillSel">
	            	<select style="width: 100%;">
						<option selected="selected">Last 30 days</option>
						<option>Last 30 days</option>
						<option>This month</option>
						<option>This quarter</option>
						<option>This year</option>
						<option>Last month</option>
						<option>Last quarter</option>
						<option>Last year</option>
					</select>
            	</div>
              <i class="fa fa-money"></i>
              <h3 class="dashboard-stats__title">
              	<span style="float: left;font-size: 0.5em;font-weight: 600;"><%=session.getAttribute("BASE_CURRENCY")%></span>
                <span id="total_purchase_by_bill" data-decimals="<%=numberOfDecimal%>"></span>
              </h3>
            </div>
      	</div>
      	
      	<div class="col-xs-12  col-sm-offset-2 col-sm-4">			
            <div class="dashboard-stats__item dashboard-stats__item_dark-green">
            	<span class="dashboard_heading">Total Sales</span>
            	<div class="custom-select totalSalesByInvoiceSel">
	            	<select style="width: 100%;">
						<option selected="selected">Last 30 days</option>
						<option>Last 30 days</option>
						<option>This month</option>
						<option>This quarter</option>
						<option>This year</option>
						<option>Last month</option>
						<option>Last quarter</option>
						<option>Last year</option>
					</select>
            	</div>
              <i class="fa fa-truck fa-rotate-180" style="transform: rotate(180deg) scaleY(-1);"></i>
              <h3 class="dashboard-stats__title">
              	<span style="float: left;font-size: 0.5em;font-weight: 600;"><%=session.getAttribute("BASE_CURRENCY")%></span>
                <span id="total_sales_by_invoice" data-decimals="<%=numberOfDecimal%>"></span>
              </h3>
            </div>            
         </div>
      </div>
      
      <div class="row">
      	<div class="col-xs-12 col-sm-6">
       		<div class="panel panel-default">
       			<div class="panel-heading dashboard-stats__item_purple">
             		<h3 class="panel-title">
               			Total Purchases
	               </h3>
	               <div class="custom-select totalPurchaseByBillSumrySel">
		            	<select style="width: 100%;">
							<option selected="selected">Last 30 days</option>
							<option>Last 30 days</option>
							<option>This month</option>
							<option>This quarter</option>
							<option>This year</option>
							<option>Last month</option>
							<option>Last quarter</option>
							<option>Last year</option>
						</select>
	           		</div>
            	</div>
	            <div class="panel-body">
	             	<canvas id="canvas9"></canvas>
	            </div>  			
       		</div>	          	
        </div>
      	
          
          <div class="col-xs-12 col-sm-6">
       		<div class="panel panel-default">
       			<div class="panel-heading dashboard-stats__item_green2">
             		<h3 class="panel-title">
               			Total Sales
	               </h3>
	               <div class="custom-select totalSalesByInvoiceSumrySel">
		            	<select style="width: 100%;">
							<option selected="selected">Last 30 days</option>
							<option>Last 30 days</option>
							<option>This month</option>
							<option>This quarter</option>
							<option>This year</option>
							<option>Last month</option>
							<option>Last quarter</option>
							<option>Last year</option>
						</select>
	           		</div>
            	</div>
	            <div class="panel-body">
	             	<canvas id="canvas10"></canvas>
	            </div>  			
       		</div>	          	
        </div>
      </div>
      
      <div class="row">
      	<div class="col-xs-12 col-sm-6">
       		<div class="panel panel-default" style="min-height:319px;">
       			<div class="panel-heading dashboard-stats__item_green2">
             		<h3 class="panel-title">
               			Total Income <span id="totalIncome"></span>
	               </h3>
	               <div class="custom-select totalIncomeSel">
		            	<select style="width: 100%;">
							<option selected="selected">Last 30 days</option>
							<option>Last 30 days</option>
							<option>This month</option>
							<option>This quarter</option>
							<option>This year</option>
							<option>Last month</option>
							<option>Last quarter</option>
							<option>Last year</option>
						</select>
	           		</div>
            	</div>
	            <div class="panel-body">
	             	<canvas id="canvas11"></canvas>
	             	<h5 class="totalIncomeMsg" style="position: absolute;width: 100%;top: 50%;text-align: center;right: 0;">
	            		No Income were found in this time frame
            		</h5>
	            </div>  			
       		</div>	          	
        </div>
        
        <div class="col-xs-12 col-sm-6">
       		<div class="panel panel-default account-panel">
       			<div class="panel-heading dashboard-stats__item_purple">
             		<h3 class="panel-title">
               			Total Income
	               </h3>
	               <div class="custom-select totalIncomeByInvoiceSumrySel">
		            	<select style="width: 100%;">
							<option selected="selected">Last 30 days</option>
							<option>Last 30 days</option>
							<option>This month</option>
							<option>This quarter</option>
							<option>This year</option>
							<option>Last month</option>
							<option>Last quarter</option>
							<option>Last year</option>
						</select>
	           		</div>
            	</div>
	            <div class="panel-body">
	             	<canvas id="canvas13"></canvas>
	            </div>  			
       		</div>	          	
        </div>
      </div>
      
      <div class="row">
      	<div class="col-xs-12 col-sm-6">
       		<div class="panel panel-default" style="min-height:319px;">
       			<div class="panel-heading dashboard-stats__item_purple">
             		<h3 class="panel-title">
               			Total Expense <span id="totalExpense"></span>
	               </h3>
	               <div class="custom-select totalExpenseSel">
		            	<select style="width: 100%;">
							<option selected="selected">Last 30 days</option>
							<option>Last 30 days</option>
							<option>This month</option>
							<option>This quarter</option>
							<option>This year</option>
							<option>Last month</option>
							<option>Last quarter</option>
							<option>Last year</option>
						</select>
	           		</div>
            	</div>
	            <div class="panel-body">
	             	<canvas id="canvas12"></canvas>
	             	<h5 class="totalExpenseMsg" style="position: absolute;width: 100%;top: 50%;text-align: center;right: 0;">
	            		No Expense were found in this time frame
            		</h5>
	            </div>  			
       		</div>	          	
        </div>
        
        <div class="col-xs-12 col-sm-6">
       		<div class="panel panel-default account-panel">
       			<div class="panel-heading dashboard-stats__item_green2">
             		<h3 class="panel-title">
               			Total Expense
	               </h3>
	               <div class="custom-select totalExpenseByBillSumrySel">
		            	<select style="width: 100%;">
							<option selected="selected">Last 30 days</option>
							<option>Last 30 days</option>
							<option>This month</option>
							<option>This quarter</option>
							<option>This year</option>
							<option>Last month</option>
							<option>Last quarter</option>
							<option>Last year</option>
						</select>
	           		</div>
            	</div>
	            <div class="panel-body">
	             	<canvas id="canvas14"></canvas>
	            </div>  			
       		</div>	          	
        </div>
      </div>
      
      <div class="row">
      <div class="col-xs-12 col-sm-6">
      	<div class="col-xs-12 no-padding">
      		<div class="panel panel-default account-panel">
       			<div class="panel-heading dashboard-stats__item_green2">
             		<h3 class="panel-title">
               			Total Account Payable <span id="totalAccPay"></span>
	               </h3>
	               <div class="custom-select totalAccPaySel">
		            	<select style="width: 100%;">
							<option selected="selected">Last 30 days</option>
							<option>Last 30 days</option>
							<option>This month</option>
							<option>This quarter</option>
							<option>This year</option>
							<option>Last month</option>
							<option>Last quarter</option>
							<option>Last year</option>
						</select>
	           		</div>
            	</div>
	            <div class="panel-body">
	             	<canvas id="canvas15"></canvas>
	             	<h5 class="totalAccPayMsg" style="position: absolute;width: 100%;top: 50%;text-align: center;right: 0;">
	            		No Account Payable were found in this time frame
            		</h5>
	            </div>  			
       		</div>
      	</div>
      	
      	<div class="col-xs-12 no-padding">
       			<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_purple">
                		<h3 class="panel-title">
                  			Account Payable <span id="accPay"></span>
		                </h3>
		                <div class="custom-select accPaySel">
			            	<select style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
		            	</div>
		             </div>
		             <div class="panel-body">
		              	<div class="table-responsive">
							<table id="tableAccPay" class="table table-bordred table-striped">
								<thead>
									<tr>
										<th style="width:40%">Supplier</th>
										<th style="width:20%">Total Payable</th>
										<th colspan="2" style="width:40%">Chart</th>
									</tr>
								</thead>
							</table>
						</div>
						<div class="col-xs-12">
							<div style="height:13px;width:30px;display:inline-block;background:#3cb371;vertical-align:middle">
							</div>
							<span>NON PDC PAYMENT</span>
							&nbsp;&nbsp;
							<div style="height:13px;width:30px;display:inline-block;background:#e41b1b;vertical-align:middle">
							</div>
							<span>PDC PAYMENT</span>
						</div>
		             </div>          			
         		</div>       			
       		</div>
     	</div>
     	<div class="col-xs-12 col-sm-6">
     	<div class="col-sm-12 no-padding">
   			<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_purple">
                		<h3 class="panel-title">
                  			Supplier Ageing Summary
		                </h3>
		                <div class="custom-select supAgeSel">
			            	<select style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
		            	</div>
		             </div>
		             <div class="panel-body">
		              	<div class="table-responsive">
							<table id="tableSupAge" class="table table-bordred table-striped">
								<thead>
									<tr>
										<th>Supplier</th>
										<th>Total Amount Due</th>
										<th>30 days</th>
										<th>31-60 days</th>
										<th>61-90 days</th>
										<th>90+ days</th>
									</tr>
								</thead>
							</table>
						</div>
		             </div>          			
         		</div>       			
       		</div>
     	</div>
      </div>
      
      <div class="row">
      <div class="col-xs-12 col-sm-6">
      	<div class="col-xs-12 no-padding">
      		<div class="panel panel-default account-panel">
       			<div class="panel-heading dashboard-stats__item_purple">
             		<h3 class="panel-title">
               			Total Account Receivable <span id="totalAccRecv"></span>
	               </h3>
	               <div class="custom-select totalAccRecvSel">
		            	<select style="width: 100%;">
							<option selected="selected">Last 30 days</option>
							<option>Last 30 days</option>
							<option>This month</option>
							<option>This quarter</option>
							<option>This year</option>
							<option>Last month</option>
							<option>Last quarter</option>
							<option>Last year</option>
						</select>
	           		</div>
            	</div>
	            <div class="panel-body">
	             	<canvas id="canvas16"></canvas>
	             	<h5 class="totalAccRecvMsg" style="position: absolute;width: 100%;top: 50%;text-align: center;right: 0;">
	            		No Account Receivable were found in this time frame
            		</h5>
	            </div>  			
       		</div>
      	</div>
      	
      	<div class="col-xs-12 no-padding">
       			<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_green2">
                		<h3 class="panel-title">
                  			Account Receivable <span id="accRecv"></span>
		                </h3>
		                <div class="custom-select accRecvSel">
			            	<select style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
		            	</div>
		             </div>
		             <div class="panel-body">
		              	<div class="table-responsive">
							<table id="tableRecvPay" class="table table-bordred table-striped">
								<thead>
									<tr>
										<th style="width:40%">Customer</th>
										<th style="width:20%">Total Receivable</th>
										<th colspan="2" style="width:40%">Chart</th>
									</tr>
								</thead>
							</table>
						</div>
						<br/>
						<div class="col-xs-12">
							<div style="height:13px;width:30px;display:inline-block;background:#e41b1b;vertical-align:middle">
							</div>
							<span>NON PDC PAYMENT</span>
							&nbsp;&nbsp;
							<div style="height:13px;width:30px;display:inline-block;background:#3cb371;vertical-align:middle">
							</div>
							<span>PDC PAYMENT</span>
						</div>
		             </div>          			
         		</div>       			
       		</div>
       	</div>
       	<div class="col-xs-12 col-sm-6">
       	<div class="col-sm-12 no-padding">
   			<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_green2">
                		<h3 class="panel-title">
                  			Customer Ageing Summary
		                </h3>
		                <div class="custom-select custAgeSel">
			            	<select style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
		            	</div>
		             </div>
		             <div class="panel-body">
		              	<div class="table-responsive">
							<table id="tablecustAge" class="table table-bordred table-striped">
								<thead>
									<tr>
										<th>Customer</th>
										<th>Total Amount Due</th>
										<th>30 days</th>
										<th>31-60 days</th>
										<th>61-90 days</th>
										<th>90+ days</th>
									</tr>
								</thead>
							</table>
						</div>
		             </div>          			
         		</div>       			
       		</div>
       	</div>
      </div>
      
      <div class="row">
      	<div class="col-sm-6">
       			<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_green2">
                		<h3 class="panel-title">
                  			PDC Payable
		                </h3>
		                <div class="custom-select payPdcSel">
			            	<select style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
		            	</div>
		             </div>
		             <div class="panel-body">
		              	<div class="table-responsive">
							<table id="tablePaymentPdc" class="table table-bordred table-striped">
								<thead>
									<tr>
										<th>Payment ID</th>
										<th>Payment Date</th>
										<th>Supplier</th>
										<th>Bank</th>
										<th>Cheque No</th>
										<th>Cheque Date</th>
										<th>Cheque Reversed Date</th>
										<th>Amount</th>
									</tr>
								</thead>
							</table>
							<br>
							<button class="btn btn-sm btn-default pull-right" onclick="redirectToPaymentPdcSummary()" style="color: #337ab7;">Process Payment</button>
						</div>
		             </div>          			
         		</div>       			
       		</div>
       		
       		<div class="col-sm-6">
       			<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_purple">
                		<h3 class="panel-title">
                  			PDC Receivable
		                </h3>
		                <div class="custom-select payRecvPdcSel">
			            	<select style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
		            	</div>
		             </div>
		             <div class="panel-body">
		              	<div class="table-responsive">
							<table id="tablePaymentRecvPdc" class="table table-bordred table-striped">
								<thead>
									<tr>
										<th>Payment ID</th>
										<th>Payment Date</th>
										<th>Customer</th>
										<th>Bank</th>
										<th>Cheque No</th>
										<th>Cheque Date</th>
										<th>Cheque Reversed Date</th>
										<th>Amount</th>
									</tr>
								</thead>
							</table>
							<br>
							<button class="btn btn-sm btn-default pull-right" onclick="redirectToPaymentReceivePdcSummary()" style="color: #337ab7;">Process Payment</button>
						</div>
		             </div>          			
         		</div>       			
       		</div>
      </div>
      
    </div>
    <% if (acc) { 
    	activeSection=false;
    	defaultTab = (defaultTab == "") ? "accountsDashboard" : defaultTab;
    }
    %>
    

    <div id="purchaseDashboard" class="tab-pane fade<%= (pur && activeSection) ? " in active" : "" %>">
      <div class="row">
      <br>
          <div class="col-xs-12 col-sm-offset-1 col-sm-4">			
            <div class="dashboard-stats__item dashboard-stats__item_red">
            	<span class="dashboard_heading">Total Purchases</span>
            	<div class="custom-select totalPurchaseSel">
	            	<select id="purchaseRange" style="width: 100%;">
						<option selected="selected">Last 30 days</option>
						<option>Last 30 days</option>
						<option>This month</option>
						<option>This quarter</option>
						<option>This year</option>
						<option>Last month</option>
						<option>Last quarter</option>
						<option>Last year</option>
					</select>
            	</div>
              <i class="fa fa-money"></i>
              <h3 class="dashboard-stats__title">
              	<span style="float: left;font-size: 0.5em;font-weight: 600;"><%=session.getAttribute("BASE_CURRENCY")%></span>
                <span id="total_receipt" data-decimals="<%=numberOfDecimal%>"></span>
              </h3>
            </div>            
          </div>
          
          
          <div class="col-xs-12 col-sm-offset-2 col-sm-4">			
            <div class="dashboard-stats__item dashboard-stats__item_dark-green">
            	<span class="dashboard_heading">Number Of Product Purchased</span>
            	<div class="custom-select NoItemPurSel">
	            	<select id="purchaseRange" style="width: 100%;" onchange="{getTotals(this.value);}">
						<option selected="selected">Last 30 days</option>
						<option>Last 30 days</option>
						<option>This month</option>
						<option>This quarter</option>
						<option>This year</option>
						<option>Last month</option>
						<option>Last quarter</option>
						<option>Last year</option>
					</select>
            	</div>
              <i class="fa fa-shopping-basket"></i>
              <h3 class="dashboard-stats__title">
                <span id="total_recv_qty" data-decimals="3"></span> 
              </h3>
            </div>            
          </div>
          
          <!-- <div class="col-xs-12 col-sm-4 text-center" style="font-size: 16px;padding: 1em;">
          <form name="frmViewMovementHistory" method="post"
						action="view_movhis_list.jsp" style="margin: 0px; padding: 0px;">
						<input type="hidden" name="PGaction" value="View" /> <input
							type="hidden" name="FROM_DATE" value="" /> <input type="hidden"
							name="TO_DATE" value="" /> <input type="hidden" name="USERID"
							value="" />
					</form>
          	<a href="#" class="link" onclick="{navigateToMovementHistory();}">See All Activity</a>
          </div> -->
          </div>
          
          <div class="row">
          
          <div class="col-xs-12 col-sm-6">
          		<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_purple">
                		<h3 class="panel-title">
                  			Purchase Summary
		                </h3>
		                <div class="custom-select PurSumrySel">
			            	<select style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
		            	</div>
		             </div>
		             <div class="panel-body">
		              	<canvas id="canvas"></canvas>
		              	<button class="btn btn-sm btn-default pull-right" 
		              	onclick="redirectToPurchaseSummary()" style="color: #337ab7;">See More</button>
		             </div>          			
          		</div>	          	
	        </div>
	        
	        <div class="col-xs-12 col-sm-6">
          		<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_green2">
                		<h3 class="panel-title">
                  			Top Suppliers
		                </h3>
		                <div class="custom-select TopSupSel">
			            	<select style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
		            	</div>
		             </div>
		             <div class="panel-body">
		              	<canvas id="canvas2"></canvas>
		             </div>  			
          		</div>	          	
	        </div>
	        
          </div>
          
          <div class="row">
          	<div class="col-xs-12 col-sm-6">
	        	<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_green2">
                		<h3 class="panel-title">
                  			Stock Replenishment Products
		                </h3>
		             </div>
		             <div class="panel-body">
		             	<div class="table-responsive" style="overflow-y: hidden;">
		              		<table class="table table-striped no-margin stkReplshPrd">
								<thead>
									<tr>
										<th>Product</th>
										<th>Description</th>
										<th>Record Point</th>
										<th colspan="2" class="text-center">Stock On Hand</th>
										<th>UOM</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
		             </div>          			
          		</div>	
	        </div>
	        
	        <div class="col-xs-12 col-sm-6">
          		<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_purple">
                		<h3 class="panel-title">
                  			New Suppliers
		                </h3>
		             </div>
		             <div class="panel-body">
		             	<div>
		             		<h4 style="text-align: center;font-weight: 600;font-size: 14px;">Number of Suppliers</h4> 
						    <h3 id="total_suppliers" style="text-align: center;font-weight: 600;margin-top: 0px;color: #4e9251;">150</h3>
							<hr>						
						</div>
		              	<table id="newSuppliers" class="table table-striped no-margin">
								<thead>
									<tr>
										<th>Supplier ID</th>
										<th>Supplier Name</th>
										<th>Contact Name</th>
										<th>Mobile Number</th>
										<th>Email</th>
									</tr>
								</thead>
								<tbody>
									
								</tbody>
								<tfoot>
									<tr>
									    <td colspan="5"><button class="btn btn-sm btn-default pull-right" onclick="window.location.href='vendorSummary.jsp'" style="color: #337ab7;">See More</button></td>
									</tr>
								</tfoot>
							</table>
		             </div>        			
          		</div>	          	
	        </div>
          </div>
          
          <div class="row">
          	<div class="col-sm-6">
          		<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_purple">
                		<h3 class="panel-title">
                  			PO Delivery Dates
		                </h3>
		             </div>
		             <div class="panel-body">
          				<div id='calendar'></div>
          			</div>
       			</div>
          	</div>
          
          	<div class="col-sm-6">
          		
          	</div>
          </div>
    </div>
   <% if (pur) { 
     activeSection=false;
     defaultTab = (defaultTab == "") ? "purchaseDashboard" : defaultTab;
    }
   %>
    <div id="salesDashboard" class="tab-pane fade<%= (sal && activeSection) ? " in active" : "" %>">
      <div class="row">
      <br>
          <div class="col-xs-12 col-sm-offset-1 col-sm-4">			
            <div class="dashboard-stats__item dashboard-stats__item_red">
            	<span class="dashboard_heading">Total Sales</span>
            	<div class="custom-select totalSalesSel">
	            	<select id="salesRange" style="width: 100%;">
						<option selected="selected">Last 30 days</option>
						<option>Last 30 days</option>
						<option>This month</option>
						<option>This quarter</option>
						<option>This year</option>
						<option>Last month</option>
						<option>Last quarter</option>
						<option>Last year</option>
					</select>
            	</div>
              <i class="fa fa-money"></i>
              <h3 class="dashboard-stats__title">
              	<span style="float: left;font-size: 0.5em;font-weight: 600;"><%=session.getAttribute("BASE_CURRENCY")%></span>
                <span id="total_issue" data-decimals="<%=numberOfDecimal%>"></span>
              </h3>
            </div>            
          </div>
          
          
          <div class="col-xs-12 col-sm-offset-2 col-sm-4">			
            <div class="dashboard-stats__item dashboard-stats__item_dark-green">
            	<span class="dashboard_heading">Number Of Product Sold</span>
            	<div class="custom-select NoItemSaleSel">
	            	<select id="purchaseRange" style="width: 100%;">
						<option selected="selected">Last 30 days</option>
						<option>Last 30 days</option>
						<option>This month</option>
						<option>This quarter</option>
						<option>This year</option>
						<option>Last month</option>
						<option>Last quarter</option>
						<option>Last year</option>
					</select>
            	</div>
              <i class="fa fa-truck fa-rotate-180" style="transform: rotate(180deg) scaleY(-1);"></i>
              <h3 class="dashboard-stats__title">
                <span id="total_sales_qty" data-decimals="3"></span> 
              </h3>
            </div>            
          </div>
          
          <!-- <div class="col-xs-12 col-sm-4 text-center" style="font-size: 16px;padding: 1em;">
          <form name="frmViewMovementHistory" method="post"
						action="view_movhis_list.jsp" style="margin: 0px; padding: 0px;">
						<input type="hidden" name="PGaction" value="View" /> <input
							type="hidden" name="FROM_DATE" value="" /> <input type="hidden"
							name="TO_DATE" value="" /> <input type="hidden" name="USERID"
							value="" />
					</form>
          	<a href="#" class="link" onclick="{navigateToMovementHistory();}">See All Activity</a>
          </div> -->
		</div>
		<div class="row">
          
          <div class="col-xs-12 col-sm-6">
          		<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_purple">
                		<h3 class="panel-title">
                  			Sales Summary
		                </h3>
		                <div class="custom-select saleSumrySel">
			            	<select style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
		            	</div>
		             </div>
		             <div class="panel-body">
		              	<canvas id="canvas3"></canvas>
		              	<button class="btn btn-sm btn-default pull-right" 
		              	onclick="redirectToSalesSummary()" style="color: #337ab7;">See More</button>
		             </div>          			
          		</div>	          	
	        </div>
	        
	        <div class="col-xs-12 col-sm-6">
          		<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_green2">
                		<h3 class="panel-title">
                  			Top Customers
		                </h3>
		                <div class="custom-select TopCustSel">
			            	<select style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
		            	</div>
		             </div>
		             <div class="panel-body">
		              	<canvas id="canvas4"></canvas>
		             </div>  			
          		</div>	          	
	        </div>	        
          </div>
          
          <div class="row">
	        <div class="col-xs-12 col-sm-6">
				<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_green2">
                		<h3 class="panel-title">
                  			Top Sales Product
		                </h3>
		                <div class="custom-select TopSalPrdSel">
			            	<select style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
		            	</div>
		             </div>
		             <div class="panel-body">
		              	<canvas id="canvas5"></canvas>
		             </div>  			
          		</div>
	        </div>
	        <div class="col-xs-12 col-sm-6">
          		<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_purple">
                		<h3 class="panel-title">
                  			New Customers
		                </h3>
		             </div>
		             <div class="panel-body">
		             	<div>
		             		<h4 style="text-align: center;font-weight: 600;font-size: 14px;">Number of Customers</h4> 
						    <h3 id="total_customers" style="text-align: center;font-weight: 600;margin-top: 0px;color: #4e9251;">150</h3>
							<hr>						
						</div>
		              	<table id="newcustomers" class="table table-striped no-margin">
								<thead>
									<tr>
										<th>Customer ID</th>
										<th>Customer Name</th>
										<th>Contact Name</th>
										<th>Mobile Number</th>
										<th>Email</th>
									</tr>
								</thead>
								<tbody>
									
								</tbody>
								<tfoot>
									<tr>
									    <td colspan="5"><button class="btn btn-sm btn-default pull-right" onclick="window.location.href='custmerSummary.jsp'" style="color: #337ab7;">See More</button></td>
									</tr>
								</tfoot>
							</table>
		             </div>        			
          		</div>	          	
	        </div>        
	        
	        </div>
	        
	      <div class="row">
	        <div class="col-sm-6">
          		<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_purple">
                		<h3 class="panel-title">
                  			Sales Order Delivery Dates
		                </h3>
		             </div>
		             <div class="panel-body">
          				<div id='calendar4'></div>
          			</div>
       			</div>
          	</div>
          	<div class="col-sm-6">
          		<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_green2">
                		<h3 class="panel-title">
                  			Sales Estimate Order Pending
		                </h3>
		             </div>
		             <div class="panel-body">
          				<div id='calendar3'></div>
          			</div>
       			</div>
          	</div>
	      </div> 
    </div>
    <%   
    if (sal) { 
    	activeSection=false;
    	defaultTab = (defaultTab == "") ? "salesDashboard" : defaultTab;
    }
    %>
    <div id="warehouseDashboard" class="tab-pane fade<%= (war && activeSection) ? " in active" : "" %>">
      <div class="row">
      	<br>
      	<div class="col-xs-12 col-sm-offset-1 col-sm-4">			
            <div class="dashboard-stats__item dashboard-stats__item_red">
            	<span class="dashboard_heading">Total Received Items</span>
            	<div class="custom-select NoRcvItemSel">
	            	<select style="width: 100%;">
						<option selected="selected">Last 30 days</option>
						<option>Last 30 days</option>
						<option>This month</option>
						<option>This quarter</option>
						<option>This year</option>
						<option>Last month</option>
						<option>Last quarter</option>
						<option>Last year</option>
					</select>
            	</div>
              <i class="fa fa-shopping-basket"></i>
              <h3 class="dashboard-stats__title">
                <span id="total_items_recv_qty" data-decimals="3"></span> 
              </h3>
            </div>            
          </div>
          
          <div class="col-xs-12 col-sm-offset-2 col-sm-4">			
            <div class="dashboard-stats__item dashboard-stats__item_dark-green">
            	<span class="dashboard_heading">Total Issued Items</span>
            	<div class="custom-select NoItemIssueSel">
	            	<select style="width: 100%;">
						<option selected="selected">Last 30 days</option>
						<option>Last 30 days</option>
						<option>This month</option>
						<option>This quarter</option>
						<option>This year</option>
						<option>Last month</option>
						<option>Last quarter</option>
						<option>Last year</option>
					</select>
            	</div>
              <i class="fa  fa-truck fa-rotate-180" style="transform: rotate(180deg) scaleY(-1);"></i>
              <h3 class="dashboard-stats__title">
                <span id="total_items_issue_qty" data-decimals="3"></span> 
              </h3>
            </div>            
          </div>
          <!-- <div class="col-xs-12 col-sm-4 text-center" style="font-size: 16px;padding: 1em;">
          <form name="frmViewMovementHistory" method="post"
						action="view_movhis_list.jsp" style="margin: 0px; padding: 0px;">
						<input type="hidden" name="PGaction" value="View" /> <input
							type="hidden" name="FROM_DATE" value="" /> <input type="hidden"
							name="TO_DATE" value="" /> <input type="hidden" name="USERID"
							value="" />
					</form>
          	<a href="#" class="link" onclick="{navigateToMovementHistory();}">See All Activity</a>
          </div> -->
      </div>
      
      <div class="row">
          <div class="col-xs-12 col-sm-6">
          		<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_purple">
                		<h3 class="panel-title">
                  			PO Without Price Summary
		                </h3>
		                <div class="custom-select PoWopriceSel">
			            	<select style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
		            	</div>
		             </div>
		             <div class="panel-body">
		              	<canvas id="canvas6"></canvas>
		              	<button class="btn btn-sm btn-default pull-right" 
		              	onclick="redirectToPurchaseWOPSummary()" style="color: #337ab7;">See More</button>
		             </div>          			
          		</div>	          	
	        </div>
	        
	        <div class="col-xs-12 col-sm-6">
          		<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_green2">
                		<h3 class="panel-title">
                  			GRN Summary
		                </h3>
		                <div class="custom-select grnSel">
			            	<select style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
		            	</div>
		             </div>
		             <div class="panel-body">
		              	<canvas id="canvas7"></canvas>
		              	<button class="btn btn-sm btn-default pull-right" 
		              	onclick="redirectToGrnSummary()" style="color: #337ab7;">See More</button>
		             </div>          			
          		</div>	          	
	        </div>
        </div>
        <div class="row">
          	<div class="col-xs-12 col-sm-6">
	        	<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_green2">
                		<h3 class="panel-title">
                  			Stock Replenishment Products
		                </h3>
		             </div>
		             <div class="panel-body">
		             	<div class="table-responsive" style="overflow-y: hidden;">
		              	<table class="table table-striped no-margin stkReplshPrd">
								<thead>
									<tr>
										<th>Product</th>
										<th>Description</th>
										<th>Record Point</th>
										<th colspan="2" class="text-center">Stock On Hand</th>
										<th>UOM</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
						</table>
						</div>
		             </div>          			
          		</div>	
	        </div>
	        <div class="col-xs-12 col-sm-6">
          		<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_purple">
                		<h3 class="panel-title">
                  			GI Summary
		                </h3>
		                <div class="custom-select griSel">
			            	<select style="width: 100%;">
								<option selected="selected">Last 30 days</option>
								<option>Last 30 days</option>
								<option>This month</option>
								<option>This quarter</option>
								<option>This year</option>
								<option>Last month</option>
								<option>Last quarter</option>
								<option>Last year</option>
							</select>
		            	</div>
		             </div>
		             <div class="panel-body">
		              	<canvas id="canvas8"></canvas>
		              	<button class="btn btn-sm btn-default pull-right" 
		              	onclick="redirectToGiSummary()" style="color: #337ab7;">See More</button>
		             </div>          			
          		</div>	          	
	        </div>
	     </div>
	     <div class="row">
          	<div class="col-sm-6">
          		<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_purple">
                		<h3 class="panel-title">
                  			PO Delivery Dates
		                </h3>
		             </div>
		             <div class="panel-body">
          				<div id='calendar5'></div>
          			</div>
       			</div>
          	</div>
          	<div class="col-sm-6">
          		<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_green2">
                		<h3 class="panel-title">
                  			Sales Order Delivery Date
		                </h3>
		             </div>
		             <div class="panel-body">
          				<div id='calendar6'></div>
          			</div>
       			</div>
          	</div>
       	</div>
       	<div class="row">
       		<div class="col-sm-6">
       			<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_green2">
                		<h3 class="panel-title">
                  			Products Ready To Pack From Sales Order
		                </h3>
		                <div class="custom-select rtpSel">
			            	<select style="width: 100%;">
								<option selected="selected">Today</option>
								<option>Today</option>
								<option>This week</option>
								<option>This month</option>
							</select>
		            	</div>
		             </div>
		             <div class="panel-body">
		              	<div class="table-responsive">
							<table id="tableReadtToPackProducts" class="table table-bordred table-striped">
								<thead>
									<tr>
										<th>Order No</th>
										<th>Customer Name</th>
										<th>Line No</th>
										<th>Order Date</th>
										<th>UOM</th>
										<th>Total Qty</th>
									</tr>
								</thead>
							</table>
						</div>
		             </div>          			
         		</div>       			
       		</div>
       		
       		<div class="col-sm-6">
       			<div class="panel panel-default">
          			<div class="panel-heading dashboard-stats__item_purple">
                		<h3 class="panel-title">
                  			Expiring Products
		                </h3>
		                <div class="custom-select expPrdSel">
			            	<select style="width: 100%;">
								<option selected="selected">Today</option>
								<option>Today</option>
								<option>This week</option>
								<option>This month</option>
							</select>
		            	</div>
		             </div>
		             <div class="panel-body">
		              	<div class="table-responsive">
							<table id="tableExpProducts" class="table table-bordred table-striped" style="width:100%">
								<thead>
									<tr>
										<th>Product</th>
										<th>Location</th>
										<th>Batch</th>
										<th>Expiry Date</th>
										<th>Quantity</th>
									</tr>
								</thead>
							</table>
						</div>
		             </div>          			
         		</div>       			
       		</div>
       	</div>
    </div>
    <% 
    if (war) { 
    	activeSection=false;
    	defaultTab = (defaultTab == "") ? "warehouseDashboard" : defaultTab;
    }%>
  </div>
</section>
<!-- /.content -->
<%
	//      Freeing up unused memory
	session.removeAttribute("MSG_SEARCH");
	session.removeAttribute("LOG_SEARCH");
	session.removeAttribute("RESULT");
%>
<script type="text/javascript" src="dist/js/moment.min.js"></script>
<script type="text/javascript" src="js/jquery.countTo.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.3/Chart.min.js"></script>
<script type="text/javascript" src="js/utils.js"></script>
<script type="text/javascript" src="js/core_main.js"></script>
<script type="text/javascript" src="js/daygrid_main.js"></script>
<script src="https://unpkg.com/@popperjs/core@2"></script>
<script src="https://unpkg.com/tippy.js@6"></script>
<script type="text/javascript" src="js/dashboard_new.js"></script>
<script>$(document).ready(function(){
	getCurrentServerTime();
});
var maintexpdayscount='<%=session.getAttribute("MAINTEXPCOUNT")%>';
var maintexpdate='<%=session.getAttribute("MAINTEXPDATE")%>';


if(parseInt(maintexpdayscount,10)<=30 && parseInt(maintexpdayscount,10)> 0)
{
	alert("Your maintenance is due for renewal on " + maintexpdate+ ".");
}
else if(parseInt(maintexpdayscount,10)<=0)
{
	alert("Your maintenance renewal is currently overdue since " + maintexpdate + ".");
}


<%session.setAttribute("MAINTEXPCOUNT", "");%>
<%session.setAttribute("MAINTEXPDATE", "");%>

var currentServerTime, tableTopIssuedProducts, tableTopReceivedProducts, tableExpProducts,tableReadyToPackOrders,
tableLowStockProducts,tablePaymentPdc,tablePaymentRecvPdc,tableAccPay,tableRecvPay,tableSupAge,tableAccPayForMgt,
tablecustAge,tableSupAgeForMgt,tableRecvPayForMgt,tablecustAgeForMgt,tablePaymentPdcForMgt,tablePaymentRecvPdcForMgt;
var calendar;
var currentServerTime;
function getCurrentServerTime(){
	var dataURL = '../DashboardServlet?ACTION=CURRENT_DATE';
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data) {
		currentServerTime = data.time[0].CURRENT_DATE;
		doRestOfInitialization();
	});
}

function getFormattedDate(momentObj, formatString){
	if (typeof formatString === 'undefined'){
		return momentObj.utcOffset(currentServerTime, true).format("DD-MMM-YYYY");
	}else{
		return momentObj.utcOffset(currentServerTime, true).format(formatString);
	}
}
function getTopReceivedProducts(period){
	var dataURL = '../DashboardServlet?ACTION=TOP_RECEIVED_PRODUCTS&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	if (tableTopReceivedProducts){
		tableTopReceivedProducts.ajax.url( dataURL ).load();
	}else{
		tableTopReceivedProducts = $('#tableTopReceivedProducts').DataTable({
			"processing": true,
			"ajax": {
				"type": "GET",
				"url": dataURL,
				"contentType": "application/json; charset=utf-8",	
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if(typeof data.products[0].PRODUCT === 'undefined'){
		        		return [];
		        	}
		        	else {
		        		return data.products;
		        	}
		        }
		    },
			"columns": [{'data': 'PRODUCT', "orderable": false},{'data': 'ITEMDESC', "orderable": false},{'data': 'RECEIVEDQTY', "orderable": false}],
			"columnDefs": [{"className": "t-right", "targets": [2]}]
		});
	}
}

function getTopIssuedProducts(period){
	var dataURL = '../DashboardServlet?ACTION=TOP_ISSUED_PRODUCTS&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	if (tableTopIssuedProducts){	
		tableTopIssuedProducts.ajax.url( dataURL ).load();
	}else{
		tableTopIssuedProducts = $('#tableTopIssuedProducts').DataTable({
			"processing": true,
			"ajax": {
				"type": "GET",
				"url": dataURL,
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if(typeof data.products[0].PRODUCT === 'undefined'){
		        		return [];
		        	}
		        	else {
		        		return data.products;
		        	}
		        }
		    },
			"columns": [{'data': 'PRODUCT', "orderable": false},{'data': 'ITEMDESC', "orderable": false},{'data': 'ISSUEDQTY', "orderable": false}],
			"columnDefs": [{"className": "t-right", "targets": [2]}]
		});
	}
}


	

	function doRestOfInitialization() {		
		if("<%=defaultTab%>" == "accountMgtDashboard"){
			getTotalPurchaseByBillForMgt('Last 30 days');
			getTotalSalesByInvoiceForMgt('Last 30 days');
			getTotalPurchaseSummaryByBillForMgt('Last 30 days');
			getTotalSalesSummaryByInvoiceForMgt('Last 30 days');
			getTotalIncomeForMgt('Last 30 days');
			getTotalIncomeSummaryByInvoiceForMgt('Last 30 days');
			getTotalExpenseForMgt('Last 30 days');
			getTotalExpenseSummaryByBillForMgt('Last 30 days');
			getAccountpayableForMgt('Last 30 days');
			getAccountPayableBySupplierForMgt('Last 30 days');
			getSupplierAgeingForMgt('Last 30 days',1);
			getAccountReceivableForMgt('Last 30 days');
			getAccountReceivableByCustomerForMgt('Last 30 days');
			getPaymentPdcForMgt('Last 30 days');
			getPaymentRecvPdcForMgt('Last 30 days');
			getTotalAsset('Last 30 days');
		    getTotalLiability('Last 30 days');
		    getNetProfit('Last 30 days');
		    getGrossProfit('Last 30 days');
		}else if("<%=defaultTab%>" == "accountsDashboard"){
			getTotalPurchaseByBill('Last 30 days');
			  getTotalSalesByInvoice('Last 30 days');
			  getTotalPurchaseSummaryByBill('Last 30 days');
			  getTotalSalesSummaryByInvoice('Last 30 days');
			  getTotalIncome('Last 30 days');
			  getTotalExpense('Last 30 days');
			  getTotalIncomeSummaryByInvoice('Last 30 days');
			  getTotalExpenseSummaryByBill('Last 30 days');
			  getAccountpayable('Last 30 days');
			  getAccountReceivable('Last 30 days');
			  getAccountPayableBySupplier('Last 30 days');
			  getAccountReceivableByCustomer('Last 30 days');
			  getPaymentPdc('Last 30 days');
			  getPaymentRecvPdc('Last 30 days');
			  getSupplierAgeing('Last 30 days',1);
		}else if("<%=defaultTab%>" == "purchaseDashboard"){
			  getTotals('Last 30 days');
			  getTopIssuedProducts('Last 30 days');
			  getTopReceivedProducts('Last 30 days');
			  getExpiringProducts('Tomorrow');
			  getStockReplanishmentProducts();
			  getNewSuppliers();
			  getTotalSuppliers();
			  calendar.destroy();
			  calendar.render();
		}else if("<%=defaultTab%>" == "salesDashboard"){
			  getTotalIssue('Last 30 days');
			  getTotalNumberSales('Last 30 days');
			  getSalesSummary('Last 30 days');
			  getTopCustomers('Last 30 days');
			  getTopSalesPrd('Last 30 days');
			  getNewCustomers();
			  getTotalCustomers();
			  calendar3.render();
			  calendar4.render();
		}else if("<%=defaultTab%>" == "warehouseDashboard"){
			  getTotalReceivedItems('Last 30 days');
			  getTotalIssuedItems('Last 30 days');
			  getPoWoPriceSummary('Last 30 days');
			  getGrnSummary('Last 30 days');
			  getGriSummary('Last 30 days');
			  getReadyToPackOrders('Today');
			  getExpiringProducts('Today');
			  getStockReplanishmentProducts();
			  calendar5.render();
			  calendar6.render();
		}
		
	}
	
	function count(options) {
		var $this = $(this);
		options = $.extend({}, options || {}, $this.data('countToOptions') || {});
		$this.countTo(options);
    }	
</script>

<script>
var x, i, j, l, ll, selElmnt, a, b, c;
/*look for any elements with the class "custom-select":*/
x = document.getElementsByClassName("custom-select");
l = x.length;
for (i = 0; i < l; i++) {
  selElmnt = x[i].getElementsByTagName("select")[0];
  ll = selElmnt.length;
  /*for each element, create a new DIV that will act as the selected item:*/
  a = document.createElement("DIV");
  a.setAttribute("class", "select-selected");
  a.innerHTML = selElmnt.options[selElmnt.selectedIndex].innerHTML;
  x[i].appendChild(a);
  /*for each element, create a new DIV that will contain the option list:*/
  b = document.createElement("DIV");
  b.setAttribute("class", "select-items select-hide");
  for (j = 1; j < ll; j++) {
    /*for each option in the original select element,
    create a new DIV that will act as an option item:*/
    c = document.createElement("DIV");
    c.innerHTML = selElmnt.options[j].innerHTML;
    c.addEventListener("click", function(e) {
        /*when an item is clicked, update the original select box,
        and the selected item:*/
        var y, i, k, s, h, sl, yl;
        s = this.parentNode.parentNode.getElementsByTagName("select")[0];
        sl = s.length;
        h = this.parentNode.previousSibling;
        for (i = 0; i < sl; i++) {
          if (s.options[i].innerHTML == this.innerHTML) {
            s.selectedIndex = i;
            h.innerHTML = this.innerHTML;
            y = this.parentNode.getElementsByClassName("same-as-selected");
            yl = y.length;
            for (k = 0; k < yl; k++) {
              y[k].removeAttribute("class");
            }
            this.setAttribute("class", "same-as-selected");
            break;
          }
        }
        h.click();
    });
    b.appendChild(c);
  }
  x[i].appendChild(b);
  a.addEventListener("click", function(e) {
      /*when the select box is clicked, close any other select boxes,
      and open/close the current select box:*/
      e.stopPropagation();
      closeAllSelect(this);
      this.nextSibling.classList.toggle("select-hide");
      this.classList.toggle("select-arrow-active");
    });
}
function closeAllSelect(elmnt) {
  /*a function that will close all select boxes in the document,
  except the current select box:*/
  var x, y, i, xl, yl, arrNo = [];
  x = document.getElementsByClassName("select-items");
  y = document.getElementsByClassName("select-selected");
  xl = x.length;
  yl = y.length;
  for (i = 0; i < yl; i++) {
    if (elmnt == y[i]) {
      arrNo.push(i)
    } else {
      y[i].classList.remove("select-arrow-active");
    }
  }
  for (i = 0; i < xl; i++) {
    if (arrNo.indexOf(i)) {
      x[i].classList.add("select-hide");
    }
  }
}
/*if the user clicks anywhere outside the select box,
then close all select boxes:*/
document.addEventListener("click", closeAllSelect);
</script>

<%--New page design begin --%>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>
<%--New page design end --%>