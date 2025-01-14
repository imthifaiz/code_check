<%@page import="com.track.db.object.Journal"%>
<%@page import="com.track.db.object.JournalHeader"%>
<%@page import="com.track.db.object.JournalDetail"%>
<%@page import="com.track.db.object.JournalAttachment"%>
<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.text.*"%>
<%@ include file="header.jsp"%>
<%@page import="com.track.db.util.PrdTypeUtil"%>
<%@page import="com.track.db.util.PrdClassUtil"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Manual Journals Detail";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String systatus = session.getAttribute("SYSTEMNOW").toString();
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
int journalid=Integer.parseInt(request.getParameter("ID"));

boolean displaySummaryEdit=false,displayPdfPrint=false;
if(systatus.equalsIgnoreCase("ACCOUNTING")) {
	displaySummaryEdit = ub.isCheckValAcc("editjournal", plant,username);
	displayPdfPrint = ub.isCheckValAcc("printjournal", plant,username);
}
JournalDAO journalDAO=new JournalDAO();
Journal journal=journalDAO.getJournalById(plant, journalid);
JournalHeader journalHeader=journal.getJournalHeader();
List<JournalDetail> journalDetailList=journal.getJournalDetails();
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
ArrayList plntList = _PlantMstDAO.getPlantMstDetails(plant);
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
Map plntMap = (Map) plntList.get(0);
String PLNTDESC = (String) plntMap.get("PLNTDESC");
String ADD1 = (String) plntMap.get("ADD1");
String ADD2 = (String) plntMap.get("ADD2");
String ADD3 = (String) plntMap.get("ADD3");
String ADD4 = (String) plntMap.get("ADD4");
String STATE = (String) plntMap.get("STATE");
String COUNTRY = (String) plntMap.get("COUNTY");
String ZIP = (String) plntMap.get("ZIP");

String fromAddress_BlockAddress = ADD1 + " " + ADD2;
String fromAddress_RoadAddress = ADD3 + " " + ADD4;
String fromAddress_Country = STATE + " " + COUNTRY;

String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + plant.toLowerCase() + DbBean.LOGO_FILE;
File checkImageFile = new File(imagePath);
if (!checkImageFile.exists()) {
	imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
}

DecimalFormat  n = (DecimalFormat)NumberFormat.getCurrencyInstance(Locale.US); 
DecimalFormatSymbols symbols = n.getDecimalFormatSymbols();
symbols.setCurrencySymbol(""); // Don't use null.
n.setDecimalFormatSymbols(symbols);
n.setNegativePrefix("-");
n.setNegativeSuffix("");

boolean editcheck = true;
for (JournalDetail journaldetail : journal.getJournalDetails()) {
	if(journaldetail.getRECONCILIATION() == 1){
		editcheck = false;
	}
}

boolean irevexist = journalDAO.getisexistTridandTrtype(plant, journalid, "CONTRA");

%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.ACCOUNTING%>"/>
    <jsp:param name="mainmenu" value="<%=IConstants.JOURNAL_ENTRY%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/font-awesome.min.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/jspdf.debug.js"></script>
<script src="../jsp/js/jspdf.plugin.autotable.js"></script>
<style>
#table2 thead {
	text-align: center;
	background: black;
	color: white;
}


#table2>tbody>tr>td {
	border-bottom: 1px solid #ddd;
}
.text-ellipsis {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}
.ribbon {
    position: absolute!important;
    top: -5px;
    left: -5px;
    overflow: hidden;
    width: 96px;
    height: 94px;
}
.ribbon .ribbon-draft {
    background-color: #94a5a6;
    border-color: #788e8f;
}
.ribbon .ribbon-inner {
    text-align: center;
    color: #fff;
    top: 24px;
    left: -31px;
    width: 135px;
    padding: 3px;
    position: relative;
    transform: rotate(-45deg);
}
.ribbon .ribbon-inner:before {
    left: 0;
    border-left: 2px solid transparent;
}
.ribbon .ribbon-inner:after {
    right: -2px;
    border-bottom: 3px solid transparent;
}
.ribbon .ribbon-inner:after, .ribbon .ribbon-inner:before {
    content: "";
    border-top: 5px solid transparent;
    border-left: 5px solid;
    border-left-color: inherit;
    border-right: 5px solid transparent;
    border-bottom: 5px solid;
    border-bottom-color: inherit;
    position: absolute;
    top: 20px;
    transform: rotate(-45deg);
}
.ribbon .ribbon-success {
    background-color: #1fcd6d;
    border-color: #18a155;
}
/* @media print {
  @page { margin: 0; }
  body { margin: 1.6cm; }
} */

</style>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>  
                <li><a href="../banking/journalsummary"><span class="underline-on-hover">Manual Journals Summary</span> </a></li>                       
                <li><label>Manual Journals Detail</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class=" pull-right">
				<div class="btn-group" role="group">
				<% if (displaySummaryEdit) { %>
					<%if(editcheck){ %>
					<button type="button" onclick="window.location.href='../banking/journaledit?ID=<%=journalid%>'" class="btn btn-default"
					 data-toggle="tooltip"  data-placement="bottom" title="Edit">
						<i class="fa fa-pencil" aria-hidden="true"></i>
					</button>
					<%} %>
					<% } %>
					<% if (displayPdfPrint) { %>
					<button type="button" class="btn btn-default" onclick="generate()"
					data-toggle="tooltip"  data-placement="bottom" title="PDF">
						<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
					</button>
					<button type="button" class="btn btn-default printMe" 
					 data-toggle="tooltip"  data-placement="bottom" title="print">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button>
					<% } %>
					<button type="button" onclick="window.location.href='../banking/journalcopy?ID=<%=journalid%>'" class="btn btn-default"
					 data-toggle="tooltip"  data-placement="bottom" title="copy">
						<i class="fa fa-clone" aria-hidden="true"></i>
					</button>
					<%if(editcheck){ %>
					<button type="button" onclick="journaldelete()" class="btn btn-default"
					 data-toggle="tooltip"  data-placement="bottom" title="Delete">
						<i class="fa fa-trash" aria-hidden="true"></i>
					</button>
					<%} %>
					<%if(!editcheck){ %>
						<%if(!irevexist){ %>
						<button type="button" onclick="journalReverse()" class="btn btn-default">Journal Reverse</button>
						
						<%} %>
					<%} %>
				</div>
				
				&nbsp;
				<h1 style="font-size: 18px; cursor: pointer; vertical-align:middle;" class="box-title pull-right"
					onclick="window.location.href='../banking/journalsummary'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
			</div>
		</div>
		<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
		<div style="position:relative;padding: 0px 20px;box-shadow: 0 0 6px #ccc;">		
		<div id="ember1308" class="ribbon text-ellipsis tooltip-container ember-view">
			<div class="ribbon-inner ribbon-success"><%=journalHeader.getJOURNAL_STATUS()%></div>
		</div>	
		<div style="height: 0.700000in;"></div>
		<span id="print_id">
		<div class="row">
			<div class="col-xs-6">
				<img src="/track/ReadFileServlet/?fileLocation=<%=imagePath%>"
					style="width: 130.00px;" id="logo_content">
				<div>
					<b><%=PLNTDESC%></b>
				</div>
<span style="white-space: pre-wrap; word-wrap: break-word;">
<%=fromAddress_BlockAddress%>
<%=fromAddress_RoadAddress%>
<%=fromAddress_Country%> <%=ZIP%>
</span>
			</div>
			<div class="col-xs-6" >		
      			<h1 style="font-size: 30px;text-align:right">Journal</h1>
      			<br><p style="font-size: 15px;text-align:right">#<%=journalHeader.getID() %></p>
   			 </div>
    	</div>
    	<br>
	<div class="row">
		<div class="col-xs-6 pull-left">
			<div class="row">
						<div class="col-xs-2">
						<br><p>Notes:</p>
						</div>
						<div class="col-xs-10" style="word-wrap: break-word;text-align:justify;">
						<br><p><%=journalHeader.getNOTE() %></p>
						</div>
			</div>
		</div>
		
		<div class="col-xs-6 pull-right">
			<div class="row">
						<div class="col-xs-6 text-right">
						<p>Date:</p>
						</div>
						<div class="col-xs-6 text-right">
						<p><%=journalHeader.getJOURNAL_DATE() %></p>
						</div>
			</div>
			<div class="row">
						<div class="col-xs-6 text-right">
						<p>Amount:</p>
						</div>
						<div class="col-xs-6 text-right">
						<p><%=n.format(Double.valueOf(String.format("%."+numberOfDecimal+"f", journalHeader.getTOTAL_AMOUNT())))%></p>
						</div>			
			</div>
			<div class="row">
					<div class="col-xs-6 text-right">
					<p>Reference Number:</p>
					</div>
					<div class="col-xs-6 text-right">
					<p style="width:100%;word-break:break-word"><%=journalHeader.getREFERENCE()%></p>
					</div>			
			</div>
		</div>
	</div>
			
			
			
			
<%if(journalDetailList.size()>0) {%>
		<div class="row">
			<div class="col-xs-12">
				<table id="table2" class="table">
					<thead>
						<tr>
							<td>Account Code</td>
							<td>Account</td>
							<td>Description</td>
							<td>Debits</td>
							<td>Credits</td>							
						</tr>
					</thead>
					<tbody>
					<% 
					CustMstDAO custDAO=new CustMstDAO();
					VendMstDAO vendDAO=new VendMstDAO();
					CoaDAO coaDAO=new CoaDAO();
					Hashtable ventHash=new Hashtable();
					
					ventHash.put("PLANT", plant);
					for(JournalDetail journalDetail:journalDetailList)  {
						String accName=journalDetail.getACCOUNT_NAME();
						ventHash.put("VENDNO",journalDetail.getACCOUNT_NAME());
						if(vendDAO.getVendorNameByNo(ventHash)!=null)
						{
							if(!vendDAO.getVendorNameByNo(ventHash).isEmpty())
								accName=vendDAO.getVendorNameByNo(ventHash);
						}
						if(custDAO.getCustName(plant, journalDetail.getACCOUNT_NAME())!=null)
						{
							if(!custDAO.getCustName(plant, journalDetail.getACCOUNT_NAME()).isEmpty())
								accName=custDAO.getCustName(plant, journalDetail.getACCOUNT_NAME());
						}
						//coaDAO.CoaFullRecord(plant, journalDetail)
						//custDAO.getCustName(aPlant, journalDetail.get)
						String accountcode = coaDAO.GetAccountCodeByName(journalDetail.getACCOUNT_NAME(), plant);
						String dec = journalDetail.getDESCRIPTION();
						if(dec.contains("-")){
							String[] decInfo = dec.split("-");
							dec = decInfo[0];
						}
			  		%>
				  		<tr>
							<td class="text-center" style="text-align:left"><%=accountcode%></td>
							<td class="text-center" style="text-align:left"><%=accName%></td>
							<td class="text-center" style="white-space: pre-wrap;word-break: break-word;width:50%;text-align:left"><%=dec%></td>
							<td class="text-center"><%=n.format(Double.valueOf(String.format("%."+numberOfDecimal+"f", journalDetail.getDEBITS())))%></td>
							<td class="text-center"><%=n.format(Double.valueOf(String.format("%."+numberOfDecimal+"f", journalDetail.getCREDITS())))%></td>
							
						</tr>
				  	<%}%>
				  	
					</tbody>
				</table>
			</div>
		</div>
		<%} %>
		<div class="row">
			<div class="col-xs-9"></div>
			<div class="col-xs-3">
			<br><br><br>
			<br><br><br>		
		</div>
		</div>
		
	</div>
	
	
	<div id="journaldeletemodel" class="modal fade" role="dialog">
	  <input type="hidden" name="salespostid" value="" />
	  <div class="modal-dialog modal-sm">	
	    <!-- Modal content-->
	    <div class="modal-content">
	      <div class="modal-body">
	        <div class="row">
			   <div class="col-lg-2">
			      <i>
			         <svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon icon-xxlg-md icon-attention-circle" style="fill: red">
			            <path d="M256 32c30.3 0 59.6 5.9 87.2 17.6 26.7 11.3 50.6 27.4 71.2 48s36.7 44.5 48 71.2c11.7 27.6 17.6 56.9 17.6 87.2s-5.9 59.6-17.6 87.2c-11.3 26.7-27.4 50.6-48 71.2s-44.5 36.7-71.2 48C315.6 474.1 286.3 480 256 480s-59.6-5.9-87.2-17.6c-26.7-11.3-50.6-27.4-71.2-48s-36.7-44.5-48-71.2C37.9 315.6 32 286.3 32 256s5.9-59.6 17.6-87.2c11.3-26.7 27.4-50.6 48-71.2s44.5-36.7 71.2-48C196.4 37.9 225.7 32 256 32m0-32C114.6 0 0 114.6 0 256s114.6 256 256 256 256-114.6 256-256S397.4 0 256 0z"></path>
			            <circle cx="256" cy="384" r="32"></circle>
			            <path d="M256.3 96.3h-.6c-17.5 0-31.7 14.2-31.7 31.7v160c0 17.5 14.2 31.7 31.7 31.7h.6c17.5 0 31.7-14.2 31.7-31.7V128c0-17.5-14.2-31.7-31.7-31.7z"></path>
			         </svg>
			      </i>
			   </div>
			   <div class="col-lg-10" style="padding-left: 2px">
			      <p> Deleted Journal cannot be recovered. Are you sure about deleting journal ?</p>
			      
			      <div class="alert-actions btn-toolbar">
			         <button class="btn btn-primary ember-view" onclick="window.location.href='../banking/journaldelete?ID=<%=journalid%>'" style="background:red;">
			        	Yes 
			         </button>
			         <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
			      </div>
			   </div>
			</div>
	      </div>
	    </div>
	  </div>
	</div>
	
	<div id="journalreversemodel" class="modal fade" role="dialog">
	  <input type="hidden" name="salespostid" value="" />
	  <div class="modal-dialog modal-sm">	
	    <!-- Modal content-->
	    <div class="modal-content">
	      <div class="modal-body">
	        <div class="row">
			   <div class="col-lg-2">
			      <i>
			         <svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon icon-xxlg-md icon-attention-circle" style="fill: red">
			            <path d="M256 32c30.3 0 59.6 5.9 87.2 17.6 26.7 11.3 50.6 27.4 71.2 48s36.7 44.5 48 71.2c11.7 27.6 17.6 56.9 17.6 87.2s-5.9 59.6-17.6 87.2c-11.3 26.7-27.4 50.6-48 71.2s-44.5 36.7-71.2 48C315.6 474.1 286.3 480 256 480s-59.6-5.9-87.2-17.6c-26.7-11.3-50.6-27.4-71.2-48s-36.7-44.5-48-71.2C37.9 315.6 32 286.3 32 256s5.9-59.6 17.6-87.2c11.3-26.7 27.4-50.6 48-71.2s44.5-36.7 71.2-48C196.4 37.9 225.7 32 256 32m0-32C114.6 0 0 114.6 0 256s114.6 256 256 256 256-114.6 256-256S397.4 0 256 0z"></path>
			            <circle cx="256" cy="384" r="32"></circle>
			            <path d="M256.3 96.3h-.6c-17.5 0-31.7 14.2-31.7 31.7v160c0 17.5 14.2 31.7 31.7 31.7h.6c17.5 0 31.7-14.2 31.7-31.7V128c0-17.5-14.2-31.7-31.7-31.7z"></path>
			         </svg>
			      </i>
			   </div>
			   <div class="col-lg-10" style="padding-left: 2px">
			      <p> Are you sure about reversing journal ?</p>
			      
			      <div class="alert-actions btn-toolbar">
			         <button class="btn btn-primary ember-view" onclick="window.location.href='../JournalServlet?action=create_reverse_journal&ID=<%=journalid%>'" style="background:red;">
			        	Yes 
			         </button>
			         <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
			      </div>
			   </div>
			</div>
	      </div>
	    </div>
	  </div>
	</div>
	
	
</div>
<script type="text/javascript">
		/* @license 
		 * jQuery.print, version 1.6.0
		 *  (c) Sathvik Ponangi, Doers' Guild
		 * Licence: CC-By (http://creativecommons.org/licenses/by/3.0/)
		 *--------------------------------------------------------------------------*/
		(function ($) {
		    "use strict";
		    // A nice closure for our definitions

		    function jQueryCloneWithSelectAndTextAreaValues(elmToClone, withDataAndEvents, deepWithDataAndEvents) {
		        // Replacement jQuery clone that also clones the values in selects and textareas as jQuery doesn't for performance reasons - https://stackoverflow.com/questions/742810/clone-isnt-cloning-select-values
		        // Based on https://github.com/spencertipping/jquery.fix.clone
		        var $elmToClone = $(elmToClone),
		            $result           = $elmToClone.clone(withDataAndEvents, deepWithDataAndEvents),
		            $my_textareas     = $elmToClone.find('textarea').add($elmToClone.filter('textarea')),
		            $result_textareas = $result.find('textarea').add($result.filter('textarea')),
		            $my_selects       = $elmToClone.find('select').add($elmToClone.filter('select')),
		            $result_selects   = $result.find('select').add($result.filter('select'));

		        for (var i = 0, l = $my_textareas.length; i < l; ++i) {
		            $($result_textareas[i]).val($($my_textareas[i]).val());
		        }
		        for (var i = 0, l = $my_selects.length;   i < l; ++i) {
		            for (var j = 0, m = $my_selects[i].options.length; j < m; ++j) {
		                if ($my_selects[i].options[j].selected === true) {
		                    $result_selects[i].options[j].selected = true;
		                }
		            }
		        }
		        return $result;
		    }

		    function getjQueryObject(string) {
		        // Make string a vaild jQuery thing
		        var jqObj = $("");
		        try {
		            jqObj = jQueryCloneWithSelectAndTextAreaValues(string);
		        } catch (e) {
		            jqObj = $("<span />")
		                .html(string);
		        }
		        return jqObj;
		    }

		    function printFrame(frameWindow, content, options) {
		        // Print the selected window/iframe
		        var def = $.Deferred();
		        try {
		            frameWindow = frameWindow.contentWindow || frameWindow.contentDocument || frameWindow;
		            var wdoc = frameWindow.document || frameWindow.contentDocument || frameWindow;
		            if(options.doctype) {
		                wdoc.write(options.doctype);
		            }
		            wdoc.write(content);
		            wdoc.close();
		            var printed = false;
		            var callPrint = function () {
		                if(printed) {
		                    return;
		                }
		                // Fix for IE : Allow it to render the iframe
		                frameWindow.focus();
		                try {
		                    // Fix for IE11 - printng the whole page instead of the iframe content
		                    if (!frameWindow.document.execCommand('print', false, null)) {
		                        // document.execCommand returns false if it failed -http://stackoverflow.com/a/21336448/937891
		                        frameWindow.print();
		                    }
		                    // focus body as it is losing focus in iPad and content not getting printed
		                    $('body').focus();
		                } catch (e) {
		                    frameWindow.print();
		                }
		                frameWindow.close();
		                printed = true;
		                def.resolve();
		            }
		            // Print once the frame window loads - seems to work for the new-window option but unreliable for the iframe
		            $(frameWindow).on("load", callPrint);
		            // Fallback to printing directly if the frame doesn't fire the load event for whatever reason
		            setTimeout(callPrint, options.timeout);
		        } catch (err) {
		            def.reject(err);
		        }
		        return def;
		    }

		    function printContentInIFrame(content, options) {
		        var $iframe = $(options.iframe + "");
		        var iframeCount = $iframe.length;
		        if (iframeCount === 0) {
		            // Create a new iFrame if none is given
		            $iframe = $('<iframe height="0" width="0" border="0" wmode="Opaque"/>')
		                .prependTo('body')
		                .css({
		                    "position": "absolute",
		                    "top": -999,
		                    "left": -999
		                });
		        }
		        var frameWindow = $iframe.get(0);
		        return printFrame(frameWindow, content, options)
		            .done(function () {
		                // Success
		                setTimeout(function () {
		                    // Wait for IE
		                    if (iframeCount === 0) {
		                        // Destroy the iframe if created here
		                        $iframe.remove();
		                    }
		                }, 1000);
		            })
		            .fail(function (err) {
		                // Use the pop-up method if iframe fails for some reason
		                console.error("Failed to print from iframe", err);
		                printContentInNewWindow(content, options);
		            })
		            .always(function () {
		                try {
		                    options.deferred.resolve();
		                } catch (err) {
		                    console.warn('Error notifying deferred', err);
		                }
		            });
		    }

		    function printContentInNewWindow(content, options) {
		        // Open a new window and print selected content
		        var frameWindow = window.open();
		        return printFrame(frameWindow, content, options)
		            .always(function () {
		                try {
		                    options.deferred.resolve();
		                } catch (err) {
		                    console.warn('Error notifying deferred', err);
		                }
		            });
		    }

		    function isNode(o) {
		        /* http://stackoverflow.com/a/384380/937891 */
		        return !!(typeof Node === "object" ? o instanceof Node : o && typeof o === "object" && typeof o.nodeType === "number" && typeof o.nodeName === "string");
		    }
		    $.print = $.fn.print = function () {
		        // Print a given set of elements
		        var options, $this, self = this;
		        // console.log("Printing", this, arguments);
		        if (self instanceof $) {
		            // Get the node if it is a jQuery object
		            self = self.get(0);
		        }
		        if (isNode(self)) {
		            // If `this` is a HTML element, i.e. for
		            // $(selector).print()
		            $this = $(self);
		            if (arguments.length > 0) {
		                options = arguments[0];
		            }
		        } else {
		            if (arguments.length > 0) {
		                // $.print(selector,options)
		                $this = $(arguments[0]);
		                if (isNode($this[0])) {
		                    if (arguments.length > 1) {
		                        options = arguments[1];
		                    }
		                } else {
		                    // $.print(options)
		                    options = arguments[0];
		                    $this = $("html");
		                }
		            } else {
		                // $.print()
		                $this = $("html");
		            }
		        }
		        // Default options
		        var defaults = {
		            globalStyles: true,
		            mediaPrint: false,
		            stylesheet: null,
		            noPrintSelector: ".no-print",
		            iframe: true,
		            append: null,
		            prepend: null,
		            manuallyCopyFormValues: true,
		            deferred: $.Deferred(),
		            timeout: 750,
		            title: null,
		            doctype: '<!doctype html>'
		        };
		        // Merge with user-options
		        options = $.extend({}, defaults, (options || {}));
		        var $styles = $("");
		        if (options.globalStyles) {
		            // Apply the stlyes from the current sheet to the printed page
		            $styles = $("style, link, meta, base, title");
		        } else if (options.mediaPrint) {
		            // Apply the media-print stylesheet
		            $styles = $("link[media=print]");
		        }
		        if (options.stylesheet) {
		            // Add a custom stylesheet if given
		            $styles = $.merge($styles, $('<link rel="stylesheet" href="' + options.stylesheet + '">'));
		        }
		        // Create a copy of the element to print
		        var copy = jQueryCloneWithSelectAndTextAreaValues($this);
		        // Wrap it in a span to get the HTML markup string
		        copy = $("<span/>")
		            .append(copy);
		        // Remove unwanted elements
		        copy.find(options.noPrintSelector)
		            .remove();
		        // Add in the styles
		        copy.append(jQueryCloneWithSelectAndTextAreaValues($styles));
		        // Update title
		        if (options.title) {
		            var title = $("title", copy);
		            if (title.length === 0) {
		                title = $("<title />");
		                copy.append(title);                
		            }
		            title.text(options.title);            
		        }
		        // Appedned content
		        copy.append(getjQueryObject(options.append));
		        // Prepended content
		        copy.prepend(getjQueryObject(options.prepend));
		        if (options.manuallyCopyFormValues) {
		            // Manually copy form values into the HTML for printing user-modified input fields
		            // http://stackoverflow.com/a/26707753
		            copy.find("input")
		                .each(function () {
		                    var $field = $(this);
		                    if ($field.is("[type='radio']") || $field.is("[type='checkbox']")) {
		                        if ($field.prop("checked")) {
		                            $field.attr("checked", "checked");
		                        }
		                    } else {
		                        $field.attr("value", $field.val());
		                    }
		                });
		            copy.find("select").each(function () {
		                var $field = $(this);
		                $field.find(":selected").attr("selected", "selected");
		            });
		            copy.find("textarea").each(function () {
		                // Fix for https://github.com/DoersGuild/jQuery.print/issues/18#issuecomment-96451589
		                var $field = $(this);
		                $field.text($field.val());
		            });
		        }
		        // Get the HTML markup string
		        var content = copy.html();
		        // Notify with generated markup & cloned elements - useful for logging, etc
		        try {
		            options.deferred.notify('generated_markup', content, copy);
		        } catch (err) {
		            console.warn('Error notifying deferred', err);
		        }
		        // Destroy the copy
		        copy.remove();
		        if (options.iframe) {
		            // Use an iframe for printing
		            try {
		                printContentInIFrame(content, options);
		            } catch (e) {
		                // Use the pop-up method if iframe fails for some reason
		                console.error("Failed to print from iframe", e.stack, e.message);
		                printContentInNewWindow(content, options);
		            }
		        } else {
		            // Use a new window for printing
		            printContentInNewWindow(content, options);
		        }
		        return this;
		    };
		})(jQuery);
		</script>
		<script>
			$(document).ready(function(){
			  $('[data-toggle="tooltip"]').tooltip();  
			  $('.printMe').click(function(){
				     $("#print_id").print({
				        	globalStyles: true,
				        	mediaPrint: false,
				        	stylesheet: null,
				        	noPrintSelector: ".no-print",
				        	iframe: false,
				        	append: null,
				        	prepend: null,
				        	manuallyCopyFormValues: true,
				        	deferred: $.Deferred(),
				        	timeout: 750,
				        	title: " ",
				        	doctype: '<!doctype html>'
					});
				  
				});
			 
			});
			
			function generatePdf(dataUrl){
				
				var doc = new jsPDF('p', 'mm', 'a4');
				var pageNumber;
			
			
				var pageHeight = doc.internal.pageSize.height || doc.internal.pageSize.getHeight();
				var pageWidth = doc.internal.pageSize.width || doc.internal.pageSize.getWidth();
				/* 
				doc.setFontType("normal"); */
				/* Top Right */
				
				
				/* **** */

				/* Top Left */
				
				//doc.setFont("courier");
				doc.setTextColor(0,0,0);
				doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
				
				doc.setFontSize(10);
				doc.setFontStyle("bold");
				doc.text('<%=PLNTDESC%>', 16, 46);

				doc.setFontSize(9);
				doc.setFontStyle("normal");
				doc.text('<%=fromAddress_BlockAddress%>', 16, 50);

				doc.text('<%=fromAddress_RoadAddress%>', 16, 54);

				doc.text('<%=fromAddress_Country%> <%=ZIP%>', 16, 58);
				
				doc.setFontSize(20);
				doc.setFontStyle("bold");
				doc.text('Journal', 170, 37,{align:'left'});
				doc.setFontSize(10);
				doc.text('#<%=journalHeader.getID()%>', 190, 45);
				
				var date = doc.splitTextToSize('<%=journalHeader.getJOURNAL_DATE()%>', 90);
				var amount = doc.splitTextToSize('<%=n.format(Double.valueOf(String.format("%."+numberOfDecimal+"f", journalHeader.getTOTAL_AMOUNT())))%>', 90);
				var reference = doc.splitTextToSize('<%=journalHeader.getREFERENCE()%>', 30);
				var notesfrom="<%=journalHeader.getNOTE().replaceAll("[\\t\\n\\r]+"," ")%>";
				var note = doc.splitTextToSize(notesfrom, 60);
				
				doc.setFontSize(10);
				doc.text('Date:', 165, 55,{align:'right'});
				doc.text('<%=journalHeader.getJOURNAL_DATE()%>', 195, 55,{align:'right'});
				doc.text('Amount:', 165, 65,{align:'right'});
				doc.text(amount, 195, 65,{align:'right'});
				doc.text('Reference Number:', 165, 75,{align:'right'});
				doc.text(reference, 195, 75,{align:'right'});
				doc.text('Note: ', 15, 75);
				doc.text(note, 25, 75);
				//doc.fromHTML( '<span style="width:30px;">'+note+'</span>', 25, 75);
				doc.setFontStyle("normal");
				var lineHeight = doc.getLineHeight(notesfrom);
				lineHeight=lineHeight/doc.internal.scaleFactor;
				var lines = note.length;  // splitted text is a string array
				var blockHeight = lines*lineHeight;
				var yPos=100;
				yPos += blockHeight;
				
				/* **** */
				var totalPagesExp = "{total_pages_count_string}";
				doc.autoTable({
					html : '#table2',
					startY : yPos,
					headStyles : {
						fillColor : [ 0, 0, 0 ],
						textColor : [ 255, 255, 255 ],
						halign : 'center'
					},
					bodyStyles : {
						fillColor : [ 255, 255, 255 ],
						textColor : [ 0, 0, 0 ]
					},
					theme : 'plain',
					columnStyles: {0: {halign: 'left',cellWidth: 30},1: {halign: 'left',cellWidth: 40},2: {halign: 'left',cellWidth: 60},3: {halign: 'center',cellWidth: 25},4: {halign: 'center',cellWidth: 25}},
					styles: {
				        fontSize: 10,
				       /* font: 'courier' */
				        /*fontStyle:'bold' */
				    },
					didDrawPage : function(data) {
						doc.setFontStyle("normal");
						// Footer
						pageNumber = doc.internal.getNumberOfPages();
						var str = "Page " + doc.internal.getNumberOfPages();
						// Total page number plugin only available in jspdf v1.0+
						if (typeof doc.putTotalPages === 'function') {
							str = str + " of " + totalPagesExp;
						}
						doc.setFontSize(10);

						// jsPDF 1.4+ uses getWidth, <1.4 uses .width
						var pageSize = doc.internal.pageSize;
						var pageHeight = pageSize.height ? pageSize.height
								: pageSize.getHeight();
						doc.text(str, 180,
								pageHeight - 10);
					}
				});

				let finalY = doc.previousAutoTable.finalY;
				
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
				// Total page number plugin only available in jspdf v1.0+
				if (typeof doc.putTotalPages === 'function') {
					doc.putTotalPages(totalPagesExp);
				}
				doc.save('Journal.pdf');
			}
function generate() {
	
	var img = toDataURL($("#logo_content").attr("src"),
			function(dataUrl) {
				generatePdf(dataUrl);
		  	},'image/jpeg');
		
	}
function toDataURL(src, callback, outputFormat) {
	  var img = new Image();
	  img.crossOrigin = 'Anonymous';
	  img.onload = function() {
	    var canvas = document.createElement('CANVAS');
	    var ctx = canvas.getContext('2d');
	    var dataURL;
	    canvas.height = this.naturalHeight;
	    canvas.width = this.naturalWidth;
	    ctx.drawImage(this, 0, 0);
	    dataURL = canvas.toDataURL(outputFormat);
	    callback(dataURL);
	  };
	  img.src = src;
	  if (img.complete || img.complete === undefined) {
	    img.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
	    img.src = src;
	  }
}

function sortTable() {
	  var table, rows, switching, i, x, y, shouldSwitch;
	  table = document.getElementById("table2");
	  switching = true;
	  /* Make a loop that will continue until
	  no switching has been done: */
	  while (switching) {
	    // Start by saying: no switching is done:
	    switching = false;
	    rows = table.rows;
	    /* Loop through all table rows (except the
	    first, which contains table headers): */
	    for (i = 1; i < (rows.length - 1); i++) {
	      // Start by saying there should be no switching:
	      shouldSwitch = false;
	      /* Get the two elements you want to compare,
	      one from current row and one from the next: */
	      x = rows[i].getElementsByTagName("TD")[0];
	      y = rows[i + 1].getElementsByTagName("TD")[0];
	      // Check if the two rows should switch place:
	      if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
	        // If so, mark as a switch and break the loop:
	        shouldSwitch = true;
	        break;
	      }
	    }
	    if (shouldSwitch) {
	      /* If a switch has been marked, make the switch
	      and mark that a switch has been done: */
	      rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
	      switching = true;
	    }
	  }
	}

function balanceDueTable() {
	  var table, rows, switching, i, x, y, shouldSwitch;
	  table = document.getElementById("table2");
	  rows = table.rows;
	    for (i = 1; i < (rows.length - 1); i++) {
	      x = rows[i].getElementsByTagName("TD")[0];
	      y = rows[i + 1].getElementsByTagName("TD")[0];
	      if (x.innerHTML.toLowerCase() == y.innerHTML.toLowerCase()) { 
	       rows[i].getElementsByTagName("TD")[5].innerHTML = "";
	      }
	    }
	    
	 
	}
	
function journaldelete(){
	$('#journaldeletemodel').modal('show');
}

function journalReverse(){
	$('#journalreversemodel').modal('show');
}



</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>