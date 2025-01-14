<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set var="title" value="${param.title}" />
<c:set var="PLANTDESC" value="${param.PLANTDESC}" />
<c:set var="username" value="${LOGIN_USER }" />
<div id="common_email_modal" class="modal fade" role="dialog">
<input type="hidden" id="asofDate"/>
	<div class="modal-dialog">
		<div class="modal-content">
			<form class="form-horizontal" name="common_email">
				<div class="modal-header">
					<button type="button" id="close" class="close" data-dismiss="modal">&times;</button>
					<h3 class="modal-title">Email Report</h3>
				</div>
				<div class="modal-body">
					<!-- <div class="alert alert-danger">
					  test
					</div> -->
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4 ">To</label>
	                     <div class="col-sm-12">

								<input class="form-control" name="send_to" id="send_to" style="width: 100%"></input>

							</div>
					</div>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4 ">CC</label>
	                     <div class="col-sm-12">
								<input class="form-control" name="send_cc" id="send_cc" style="width: 100%"></input>
							</div>
					</div>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4 ">Subject</label>
	                     <div class="col-sm-12">
								<input class="form-control" name="send_subject" id="send_subject" value="${title}" style="width: 100%"></input>
							</div>
					</div>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4 ">Body</label>
						<div class="col-sm-12">
							<textarea rows="5" cols="23" class="form-control" name="send_body" id="send_body"></textarea>

						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4 " id="attachment_label">Report</label>
	                     <div class="col-sm-12">
	                     	<div class="row">
	                     		<div class="col-sm-12 form-inline">

									<input class="form-control" name="send_attachment" id="send_attachment" value="${title}" style="width: 95%"></input>
									<label for="send_attachment">.pdf</label>
								</div>
							</div>
							</div>
					</div>
				<div class="progress progress-sm active" style="margin-right: -8px;display:none;" id="progressView" >
                <div class="progress-bar progress-bar-success progress-bar-striped" id="progressBar"
                role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100" >
                  <span class="sr-only">100% Complete</span>
                </div>
              </div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success pull-right" id="send_email">Send</button>
					<button class="btn btn-default pull-left" data-dismiss="modal" id="send_email_cancel">Cancel</button>
				</div>
				<input type="hidden" id="pdfname" value="${title}"/>



			</form>
		</div>


	</div>

</div>
<link rel="stylesheet" type="text/css" href="${context}/jsp/dist/css/bootstrap3-wysihtml5.min.css">
<script type="text/javascript" src="${context}/jsp/dist/js/bootstrap3-wysihtml5.all.min.js"></script>
<link rel="stylesheet" type="text/css" href="${context}/jsp/dist/css/bootstrap-tagsinput.css">
<link rel="stylesheet" type="text/css" href="${context}/jsp/dist/css/bootstrap-multiEmail.css">
<script src="${context}/jsp/dist/js/bootstrap-tagsinput.min.js"></script>
<script src="${context}/jsp/dist/js/bootstrap-multiEmail.js"></script>
<script src="${context}/jsp/dist/js/jquery.toaster.js"></script>
<script>
var i = 0;
function progressBar() {
	$("#progressView").show();
  if (i == 0) {
    i = 1;
    var elem = document.getElementById("progressBar");
    var width = 1;
    var id = setInterval(frame, 50);
    function frame() {
      if (width >= 100) {
        clearInterval(id);
        i = 0;
      } else {
        width++;
        elem.style.width = width + "%";
      }
    }
  }
}
$(document).ready(function($){
	  $("#send_cc").multiEmail();
	  $('#send_email_cancel').click(function(){
			handleClose();
		});
		$('#close').click(function(){
			handleClose();
		});
});
var emailBody;

/* $("#sendemail").click(function(){
	loadBodyStyle();
});
 */
$("#send_email").click(function(){
	$('#send_email').prop('disabled', true);
	$('#send_email_cancel').prop('disabled', true);
	 var date=$('#asofDate').val();
	var mailInnerContent=$('.wysihtml5-sandbox').contents().find('.wysihtml5-editor').html();
	emailBody='<html>'+
	'<head> <title>Page Title</title> </head> '+
	'<body> <table width="100%" border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td align="center" valign="top" bgcolor="#FFFFFF"> '+
	'<table width="553" border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td colspan="3" align="left" valign="top" bgcolor="#6e8ba8"> '+
	'<table border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td height="7" style="font-size:0px;line-height:0px"></td> </tr> </tbody> </table>'+
	'<table border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td width="24"></td>'+
	'<td align="left" style="font-family:"Helvetica Neue",Helvetica,Verdana,sans-serif;font-size:18px;color:#ffffff"> ${PLANTDESC} </td> </tr> </tbody> </table>'+
	'<table border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td height="7" style="font-size:0px;line-height:0px"></td> </tr> </tbody> </table> </td> </tr> <tr> <td width="2" bgcolor="#c0d0e4"></td> <td> <table border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td width="549" bgcolor="#e6ecf4"> '+
	'<table border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td height="15" style="font-size:0px;line-height:0px"></td> </tr> </tbody> </table> <table width="549" border="0" cellpadding="0" cellspacing="0"> <tbody> <tr> <td width="24"></td> ';
	if (location.href.toString().indexOf("InboundOrderBulkReceiptSummary.jsp") != -1){
		emailBody = emailBody +
		'<td align="left" valign="top"> <span style="font-family:"Helvetica Neue",Helvetica,Verdana,sans-serif;font-size:18px;color:#404040;font-weight:bold"> Purchase Order </span> <br> '+
		'<span style="font-size:14px;color:#404040;margin:0;font-family:Helvetica,Helvetica Neue,Verdana;line-height:16px"> '+$('#PONO').val()+'</span> </td><td align="left" valign="top"> <span style="font-family:"Helvetica Neue",Helvetica,Verdana,sans-serif;font-size:18px;color:#404040;font-weight:bold"> GRNO </span> <br> '+
		'<span style="font-size:14px;color:#404040;margin:0;font-family:Helvetica,Helvetica Neue,Verdana;line-height:16px"> '+$('#GRNO').val()+'</span> </td> ';
	}else if (location.href.toString().indexOf("IBReceiptByRange.jsp") != -1 || 
			location.href.toString().indexOf("InboundOrderReceiving.jsp") != -1){
		emailBody = emailBody +
		'<td align="left" valign="top"> <span style="font-family:"Helvetica Neue",Helvetica,Verdana,sans-serif;font-size:18px;color:#404040;font-weight:bold"> Purchase Order </span> <br> '+
		'<span style="font-size:14px;color:#404040;margin:0;font-family:Helvetica,Helvetica Neue,Verdana;line-height:16px"> '+$('#ORDERNO').val()+'</span> </td><td align="left" valign="top"> <span style="font-family:"Helvetica Neue",Helvetica,Verdana,sans-serif;font-size:18px;color:#404040;font-weight:bold"> GRNO </span> <br> '+
		'<span style="font-size:14px;color:#404040;margin:0;font-family:Helvetica,Helvetica Neue,Verdana;line-height:16px"> '+$('#GRNO').val()+'</span> </td> ';
	}else if (location.href.toString().indexOf("OutBoundsOrderIssue.jsp") != -1){
		emailBody = emailBody +
		'<td align="left" valign="top"> <span style="font-family:"Helvetica Neue",Helvetica,Verdana,sans-serif;font-size:18px;color:#404040;font-weight:bold"> Sales Order </span> <br> '+
		'<span style="font-size:14px;color:#404040;margin:0;font-family:Helvetica,Helvetica Neue,Verdana;line-height:16px"> '+$('#DONO').val()+'</span> </td><td align="left" valign="top"> <span style="font-family:"Helvetica Neue",Helvetica,Verdana,sans-serif;font-size:18px;color:#404040;font-weight:bold"> GINO </span> <br> '+
		'<span style="font-size:14px;color:#404040;margin:0;font-family:Helvetica,Helvetica Neue,Verdana;line-height:16px"> '+$('#INVOICENO').val()+'</span> </td> ';
	}else if (location.href.toString().indexOf("OutBoundsOrderBulkIssue.jsp") != -1){
		emailBody = emailBody +
		'<td align="left" valign="top"> <span style="font-family:"Helvetica Neue",Helvetica,Verdana,sans-serif;font-size:18px;color:#404040;font-weight:bold"> Sales Order </span> <br> '+
		'<span style="font-size:14px;color:#404040;margin:0;font-family:Helvetica,Helvetica Neue,Verdana;line-height:16px"> '+$('#DONO').val()+'</span> </td><td align="left" valign="top"> <span style="font-family:"Helvetica Neue",Helvetica,Verdana,sans-serif;font-size:18px;color:#404040;font-weight:bold"> GINO </span> <br> '+
		'<span style="font-size:14px;color:#404040;margin:0;font-family:Helvetica,Helvetica Neue,Verdana;line-height:16px"> '+$('#INVOICENO').val()+'</span> </td> ';
	}else if ($('#PONO').length > 0){
		emailBody = emailBody +
		'<td align="left" valign="top"> <span style="font-family:"Helvetica Neue",Helvetica,Verdana,sans-serif;font-size:18px;color:#404040;font-weight:bold"> Purchase Order </span> <br> '+
		'<span style="font-size:14px;color:#404040;margin:0;font-family:Helvetica,Helvetica Neue,Verdana;line-height:16px"> '+$('#PONO').val()+'</span> </td> ';
	}else if ($('#DONO').length > 0){
		emailBody = emailBody +
		'<td align="left" valign="top"> <span style="font-family:"Helvetica Neue",Helvetica,Verdana,sans-serif;font-size:18px;color:#404040;font-weight:bold"> Sales Order </span> <br> '+
		'<span style="font-size:14px;color:#404040;margin:0;font-family:Helvetica,Helvetica Neue,Verdana;line-height:16px"> '+$('#DONO').val()+'</span> </td> ';
	}else if ($('#ESTNO').length > 0){
		emailBody = emailBody +
		'<td align="left" valign="top"> <span style="font-family:"Helvetica Neue",Helvetica,Verdana,sans-serif;font-size:18px;color:#404040;font-weight:bold"> Sales Estimate </span> <br> '+
		'<span style="font-size:14px;color:#404040;margin:0;font-family:Helvetica,Helvetica Neue,Verdana;line-height:16px"> '+$('#ESTNO').val()+'</span> </td> ';
	}else if ($('#bill').length > 0){
		emailBody = emailBody +
		'<td align="left" valign="top"> <span style="font-family:"Helvetica Neue",Helvetica,Verdana,sans-serif;font-size:18px;color:#404040;font-weight:bold"> Bill </span> <br> '+
		'<span style="font-size:14px;color:#404040;margin:0;font-family:Helvetica,Helvetica Neue,Verdana;line-height:16px"> '+$('#bill').val()+'</span> </td> ';
	}else if ($('#invoice').length > 0){
		emailBody = emailBody +
		'<td align="left" valign="top"> <span style="font-family:"Helvetica Neue",Helvetica,Verdana,sans-serif;font-size:18px;color:#404040;font-weight:bold"> Invoice </span> <br> '+
		'<span style="font-size:14px;color:#404040;margin:0;font-family:Helvetica,Helvetica Neue,Verdana;line-height:16px"> '+$('#invoice').val()+'</span> </td> ';
	}else if ($('#TONO').length > 0){
		emailBody = emailBody +
		'<td align="left" valign="top"> <span style="font-family:"Helvetica Neue",Helvetica,Verdana,sans-serif;font-size:18px;color:#404040;font-weight:bold"> Consignment Order </span> <br> '+
		'<span style="font-size:14px;color:#404040;margin:0;font-family:Helvetica,Helvetica Neue,Verdana;line-height:16px"> '+$('#TONO').val()+'</span> </td> ';
	}else{
		emailBody = emailBody +
		'<td align="left" valign="top"> <span style="font-family:"Helvetica Neue",Helvetica,Verdana,sans-serif;font-size:18px;color:#404040;font-weight:bold"> ${title} </span> <br> '+
		'<span style="font-size:14px;color:#404040;margin:0;font-family:Helvetica,Helvetica Neue,Verdana;line-height:16px">As of '+date+'</span> </td> ';
	}
	emailBody = emailBody + '</tr> </tbody> </table>'+
	'<table border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td height="15" style="font-size:0px;line-height:0px"></td> </tr> </tbody> </table> </td> </tr> </tbody> </table>'+
	'<table width="549" border="0" cellpadding="0" cellspacing="0"> <tbody> <tr> <td align="left" valign="top" style="padding-left: 23px;"> <table border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td height="20" style="font-size:0px;line-height:0px"></td> </tr> </tbody> </table>'+
	'<table border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td height="15" style="font-size:0px;line-height:0px"></td> </tr> </tbody> </table>'+
	'<div style="font-size:14px;color:#000;font-family:"Helvetica Neue",Helvetica,Verdana,sans-serif;margin-left:22px;margin-right:22px;line-height:16px">'+mailInnerContent+
	'<table border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td height="15" style="font-size:0px;line-height:0px"></td> </tr> </tbody> </table> </td> </tr> </tbody> </table> '+
	'</td> <td width="2" bgcolor="#c0d0e4"></td> </tr> <tr> <td colspan="3" align="right" bgcolor="#1f3246" style="font-family:"Helvetica Neue",Helvetica,Verdana,sans-serif;color:#ffffff;font-size:11px"> '+
	'<table border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td height="8"></td> </tr> </tbody> </table> <table border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td height="8"></td> </tr> </tbody> </table> '+
	'<table border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td height="8"></td> </tr> </tbody> </table> '+
	'<table border="0" cellspacing="0" cellpadding="0"> <tbody> <tr> <td height="8"></td> </tr> </tbody> </table> </td> </tr> </tbody> </table> </td> </tr> </tbody> </table> </body> </html> ';
	var isCheck=false;
	if($("#send_to").val()==null || $("#send_to").val()==""){
	    alert("Enter the to email");
		$("#send_to").focus();
		isCheck=true;
		return false;
	}if($("#send_subject").val()==null || $("#send_subject").val()==""){
		alert("Enter the to subject ");
		$("#send_subject").focus();
		isCheck=true;
		return false;
	} if($("#send_body").val()==null || $("#send_body").val()==""){
		alert("Enter the body message ");
		$("#send_body").focus();
		isCheck=true;
		return false;
	} if($("#send_attachment").val()==null || $("#send_attachment").val()==""){
		alert("Enter the to attachment name ");
		$("#send_attachment").focus();
		isCheck=true;
		return false;
	}if($("#send_to").val()!=null && $("#send_to").val()!=""){
		var emailReg=/^([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9\-]+\.)+([a-zA-Z0-9\-\.]+)+([ ,;]([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9\-]+\.)+([a-zA-Z0-9\-\.]+))*$/;
		var emailaddressVal=$("#send_to").val();
		if(!emailReg.test(emailaddressVal)){
			alert("Enter a valid recipient email id");
			$("#send_to").focus();
			isCheck=true;
			return false;
		} 
	}if($("#send_cc").val()!=null && $("#send_cc").val()!=""){
		var emailReg=/^([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9\-]+\.)+([a-zA-Z0-9\-\.]+)+([ ,;]([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9\-]+\.)+([a-zA-Z0-9\-\.]+))*$/;
		var emailaddressVal=$("#send_cc").val();
		if(!emailReg.test(emailaddressVal)){
			alert("Enter a valid cc email id");
			$("#send_cc").focus();
			isCheck=true;
			return false;
		}
	}
		if(isCheck==false){
	var attachName=$("#send_attachment").val();
	generateEmail(attachName);
	}else{
		$('#send_email').removeProp('disabled');
		$('#send_email_cancel').removeProp('disabled');
		}

});
function sendMailTemplate(pdfForm)
{
	var sendTo=$("#send_to").val();
	var sendCC=$("#send_cc").val();
	var sendSubject=$("#send_subject").val();
	var sendBody=$("#send_body").val();
	pdfForm.append("send_to",sendTo);
	pdfForm.append("send_cc",sendCC);
	pdfForm.append("send_subject",sendSubject);
	pdfForm.append("send_body",emailBody);

	$.ajax({
		type : 'post',
		url : "/track/CommonEmailServlet?action=sendEmail",
		dataType: "json",
	    processData: false,  // Important!
	    contentType: false,
		data : pdfForm,//{key:val}
		success : function(data) {
			var message;
			if (data.STATUS == "200") {
				$("#progressView").hide();
				$.toaster({ priority: 'success', title: '', message: 'Email Sent Successfully!' });
				//alert("Email Send Successfully!");
				$('#common_email_modal').modal('toggle');
				clear();
				$('#send_email').prop('disabled', false);
				$('#send_email_cancel').prop('disabled', false);
				if (location.href.toString().indexOf("InboundOrderBulkReceiptSummary.jsp") != -1 || 
						location.href.toString().indexOf("IBReceiptByRange.jsp") != -1 || 
						location.href.toString().indexOf("InboundOrderReceiving.jsp") != -1){
					message = "Products received successfully and email sent Successfully";
				}else if (location.href.toString().indexOf("OutBoundsOrderIssue.jsp") != -1){
					message = "Goods Issued successfully and email sent Successfully";
				}else if (location.href.toString().indexOf("OutBoundsOrderBulkIssue.jsp") != -1){
					message = "Goods Picked/Issued successfully and email sent Successfully";
				}else if ($('#PONO').length > 0){
					if (location.href.toString().indexOf('purchaseorder/edit') == -1){
						message = "Purchase Order Added and email sent Successfully";
					}else {
						message = "Purchase Order Updated and email sent Successfully";
					}
				}else if ($('#DONO').length > 0){
					if (location.href.toString().indexOf('salesorder/edit') == -1){
						message = "Sales Order Added and email sent Successfully";
					}else{
						message = "Sales Order Updated and email sent Successfully";
					}
				}else if ($('#ESTNO').length > 0){
					if($('#ISPRO').val() == "1"){
						if (location.href.toString().indexOf('salesestimate/edit') == -1){
							message = "Sales Estimate Added and email sent Successfully";
						}else{
							message = "Sales Estimate Updated and email sent Successfully";
						}
					}else{
						if (location.href.toString().indexOf('salesestimate/edit') == -1){
							message = "Sales Estimate Added and email sent Successfully";
						}else{
							message = "Sales Estimate Updated and email sent Successfully";
						}
					}
				}else if ($('#bill').length > 0){
					if (location.href.toString().indexOf('editBill.jsp') == -1){
						message = "Bill Added and email sent Successfully";
					}else{
						message = "Bill Updated and email sent Successfully";
					}
				}else if ($('#invoice').length > 0){
					if (location.href.toString().indexOf('createInvoice.jsp') != -1){
						message = "Invoice Added and email sent Successfully";
					}else{
						message = "Invoice Updated and email sent Successfully";
					}
				}else if ($('#TONO').length > 0){
					if (location.href.toString().indexOf('consignment/edit') == -1){
						message = "Consignment Order Added and email sent Successfully";
					}else{
						message = "Consignment Order Updated and email sent Successfully";
					}
				}
			}
			else{
				if (location.href.toString().indexOf("InboundOrderBulkReceiptSummary.jsp") != -1 || 
						location.href.toString().indexOf("IBReceiptByRange.jsp") != -1 || 
						location.href.toString().indexOf("InboundOrderReceiving.jsp") != -1){
					message = "Products received successfully and email not sent";
				}else if (location.href.toString().indexOf("OutBoundsOrderIssue.jsp") != -1){
					message = "Goods Issued successfully and email not sent";
				}else if (location.href.toString().indexOf("OutBoundsOrderBulkIssue.jsp") != -1){
					message = "Goods Picked/Issued successfully and email not sent";
				}else if ($('#PONO').length > 0){
					if (location.href.toString().indexOf('purchaseorder/edit') == -1){
						message = "Purchase Order Added Successfully and email not sent";
					}else{
						message = "Purchase Order Updated Successfully and email not sent";
					}
				}else if ($('#DONO').length > 0){
					if (location.href.toString().indexOf('salesorder/edit') == -1){
						message = "Sales Order Added Successfully and email not sent";
					}else{
						message = "Sales Order Updated Successfully and email not sent";
					}
				}else if ($('#ESTNO').length > 0){
					if($('#ISPRO').val() == "1"){
						if (location.href.toString().indexOf('salesestimate/edit1') == -1){
							message = "Sales Estimate Added and email not sent";
						}else{
							message = "Sales Estimate Updated and email not sent";
						}
					}else{
						if (location.href.toString().indexOf('salesestimate/edit') == -1){
							message = "Sales Estimate Added and email not sent";
						}else{
							message = "Sales Estimate Updated and email not sent";
						}
					}
				}else if ($('#bill').length > 0){
					if (location.href.toString().indexOf('editBill.jsp') == -1){
						message = "Bill Added Successfully and email not sent";
					}else{
						message = "Bill Updated Successfully and email not sent";
					}
				}else if ($('#invoice').length > 0){
					if (location.href.toString().indexOf('createInvoice.jsp') != -1){
						message = "Invoice Added Successfully and email not sent";
					}else{
						message = "Invoice Updated Successfully and email not sent";
					}
				}else if ($('#TONO').length > 0){
					if (location.href.toString().indexOf('consignment/edit') == -1){
						message = "Consignment Order Added and email not sent";
					}else{
						message = "Consignment Order Updated and email not sent";
					}
					
				}
			}
			if (location.href.toString().indexOf("InboundOrderBulkReceiptSummary.jsp") != -1 || 
					location.href.toString().indexOf("InboundOrderReceiving.jsp") != -1){
				//  No navigation
			}else if (location.href.toString().indexOf("IBReceiptByRange.jsp") != -1){
				location.href = "../jsp/IBOrderSummaryForRangeReceipt.jsp?PONO=" + $("#ORDERNO").val() + "&action=View&msg=" + message;
			}else if (location.href.toString().indexOf("OutBoundsOrderIssue.jsp") != -1){
				//	No navigation
			}else if (location.href.toString().indexOf("OutBoundsOrderBulkIssue.jsp") != -1){
				location.href = 'OutBoundsOrderBulkIssue.jsp?action=View&PLANT=${PLANTDESC}&DONO=' + $('#DONO').val();
			}else if ($('#PONO').length > 0){
				location.href="../purchaseorder/summary?msg=" + message;
			}else if ($('#DONO').length > 0){
				location.href="../salesorder/summary?msg=" + message;
			}else if ($('#ESTNO').length > 0){
				if($('#ISPRO').val() == "1"){
					location.href="../salesestimate/summaryPro?msg=" + message;
				}else{
					location.href="../salesestimate/summary?msg=" + message;
				}
			}else if ($('#bill').length > 0){
				location.href="../bill/summary?result=" + message;
			}else if ($('#invoice').length > 0){
				location.href="../invoice/summary?result=" + message;
			}else if ($('#TONO').length > 0){
				location.href="../consignment/summary?msg=" + message;
			}
		},
		error : function(data) {
			if (location.href.toString().indexOf("InboundOrderBulkReceiptSummary.jsp") != -1 || 
					location.href.toString().indexOf("InboundOrderReceiving.jsp") != -1 || 
					location.href.toString().indexOf("IBReceiptByRange.jsp") != -1){
				message = "Products received successfully and email not sent";
			}else if (location.href.toString().indexOf("OutBoundsOrderIssue.jsp") != -1){
				message = "Goods Issued successfully and email not sent";
			}else if (location.href.toString().indexOf("OutBoundsOrderBulkIssue.jsp") != -1){
				message = "Goods Picked/Issued successfully and email not sent";
			}else if ($('#PONO').length > 0){
				if (location.href.toString().indexOf('purchaseorder/edit') == -1){
					message = "Purchase Order Added Successfully and email not sent";
				}else{
					message = "Purchase Order Updated Successfully and email not sent";
				}
			}else if ($('#DONO').length > 0){
				if (location.href.toString().indexOf('salesorder/edit') == -1){
					message = "Sales Order Added Successfully and email not sent";
				}else{
					message = "Sales Order Updated Successfully and email not sent";
				}
			}
			else if ($('#ESTNO').length > 0){
				if($('#ISPRO').val() == "1"){
					if (location.href.toString().indexOf('salesestimate/edit1') == -1){
						message = "Sales Estimate Added Successfully and email not sent";
					}else{
						message = "Sales Estimate Updated Successfully and email not sent";
					}
				}else{
					if (location.href.toString().indexOf('salesestimate/edit') == -1){
						message = "Sales Estimate Added Successfully and email not sent";
					}else{
						message = "Sales Estimate Updated Successfully and email not sent";
					}
				}
			}else if ($('#bill').length > 0){
				if (location.href.toString().indexOf('editBill.jsp') == -1){
					message = "Bill Added Successfully and email not sent";
				}else{
					message = "Bill Updated Successfully and email not sent";
				}
			}else if ($('#invoice').length > 0){
				if (location.href.toString().indexOf('createInvoice.jsp') != -1){
					message = "Invoice Added Successfully and email not sent";
				}else{
					message = "Invoice Updated Successfully and email not sent";
				}
			}else if ($('#TONO').length > 0){
				if (location.href.toString().indexOf('consignment/edit') == -1){
					message = "Consignment Order Added Successfully and email not sent";
				}else{
					message = "Consignment Order Updated Successfully and email not sent";
				}
			}
			if (location.href.toString().indexOf("InboundOrderBulkReceiptSummary.jsp") != -1 || 
					location.href.toString().indexOf("InboundOrderReceiving.jsp") != -1){
				//  No navigation
			}else if (location.href.toString().indexOf("IBReceiptByRange.jsp") != -1){
				location.href = "../jsp/IBOrderSummaryForRangeReceipt.jsp?PONO=" + $("#ORDERNO").val() + "&action=View&msg=" + message;
			}else if (location.href.toString().indexOf("OutBoundsOrderIssue.jsp") != -1){
				//	No navigation
			}else if (location.href.toString().indexOf("OutBoundsOrderBulkIssue.jsp") != -1){
				location.href = 'OutBoundsOrderBulkIssue.jsp?action=View&PLANT=${PLANTDESC}&DONO=' + $('#DONO').val();
			}else 			if ($('#PONO').length > 0){
				if ($('#GRNO').length > 0){
					//	No navigation here
				}else{
					location.href="../purchaseorder/summary?msg=" + message;
				}
			}else if ($('#DONO').length > 0){
				location.href="../salesorder/summary?msg=" + message;
			}else if ($('#ESTNO').length > 0){
				if($('#ISPRO').val() == "1"){
					location.href="../salesestimate/summaryPro?msg=" + message;
				}else{
					location.href="../salesestimate/summary?msg=" + message;
				}
			}else if ($('#bill').length > 0){
				location.href="../bill/summary?result=" + message;
			}else if ($('#invoice').length > 0){
				location.href="../invoice/summary?result=" + message;
			}else if ($('#TONO').length > 0){
				location.href="../consignment/summary?msg=" + message;
			}
		}
	});
}
function handleClose(){
	if (location.href.toString().indexOf("InboundOrderBulkReceiptSummary.jsp") != -1 || 
			location.href.toString().indexOf("InboundOrderReceiving.jsp") != -1){
		//  No navigation
	}else if (location.href.toString().indexOf("IBReceiptByRange.jsp") != -1){
		message = "Products received successfully and email not sent";
		location.href = "../jsp/IBOrderSummaryForRangeReceipt.jsp?PONO=" + $("#ORDERNO").val() + "&action=View&msg=" + message;
	}else if (location.href.toString().indexOf("OutBoundsOrderIssue.jsp") != -1){
		//	No navigation
	}else if (location.href.toString().indexOf("OutBoundsOrderBulkIssue.jsp") != -1){
		message = "Goods Picked/Issued successfully and email not sent";
		location.href = 'OutBoundsOrderBulkIssue.jsp?action=View&PLANT=${PLANTDESC}&DONO=' + $('#DONO').val();
	}else 	if ($('#PONO').length > 0){
		if (location.href.toString().indexOf('purchaseorder/edit') == -1){
			message = "Purchase Order Added Successfully";
		}else{
			message = "Purchase Order Updated Successfully";
		}
		location.href="../purchaseorder/summary?msg=" + message;
	}else if ($('#DONO').length > 0){
		if (location.href.toString().indexOf('salesorder/edit') == -1){
			message = "Sales Order Added Successfully";
		}else{
			message = "Sales Order Updated Successfully";
		}
		location.href="../salesorder/summary?msg=" + message;
	}else if ($('#ESTNO').length > 0){
		if($('#ISPRO').val() == "1"){
			if (location.href.toString().indexOf('salesestimate/edit1') == -1){
				message = "Sales Estimate Added Successfully";
			}else{
				message = "Sales Estimate Updated Successfully";
			}
			location.href="../salesestimate/summaryPro?msg=" + message;
		}else{
			if (location.href.toString().indexOf('salesestimate/edit') == -1){
				message = "Sales Estimate Added Successfully";
			}else{
				message = "Sales Estimate Updated Successfully";
			}
			location.href="../salesestimate/summary?msg=" + message;
		}
	}else if ($('#bill').length > 0){
		if (location.href.toString().indexOf('editBill.jsp') == -1){
			message = "Bill Added Successfully";
		}else{
			message = "Bill Updated Successfully";
		}
		location.href="../bill/summary?result=" + message;
	}else if ($('#invoice').length > 0){
		if (location.href.toString().indexOf('createInvoice.jsp') != -1){
			message = "Invoice Added Successfully";
		}else{
			message = "Invoice Updated Successfully";
		}
		location.href="../invoice/summary?result=" + message;
	}else if ($('#TONO').length > 0){
		if (location.href.toString().indexOf('consignment/edit') == -1){
			message = "Consignment Added Successfully";
		}else{
			message = "Consignment Updated Successfully";
		}
		location.href="../consignment/summary?msg=" + message;
	}
}
function clear(){
	$("#send_to").val('');
	$("#send_cc").val('');
	$("#send_subject").val('');
	$("#send_attachment").val('');
	$("#send_subject").val($("#pdfname").val());
	$("#send_attachment").val($("#pdfname").val());
}
$(document).ready(function() {
	$('#send_body').wysihtml5();

});
function loadBodyStyle(){
	//$('#send_body').val('');

	var periodlabel="As of "+$('#asofDate').val();
	var htmlbody="";
	var plant="${PLANTDESC}";
	var title="${title}";
	var user="${username}";

	htmlbody='<div>'+plant+'</div>'+
	'<div>'+title+'</div>'+
	'<div>'+periodlabel+'</div>' +
	'<div>Hello</div><br/>'+
	'<div>Attached is '+title+ ' for '+plant+ '</div><br/>'+
	'<div>Regards</div>'+
	'<div>'+user+'</div><br/>';

	$('.wysihtml5-sandbox').contents().find('.wysihtml5-editor').html(htmlbody);

}

function loadBodyStyleREC(){
	//$('#send_body').val('');
	var htmlbody="";
	var plant="${PLANTDESC}";
	var title="${title}";
	var user="${username}";
	htmlbody='<div>'+plant+'</div>'+
	'<div>'+title+'</div>'+
	'<div>Hello</div><br/>'+
	'<div>Attached is '+title+ ' for '+plant+ '</div><br/>'+
	'<div>Regards</div>'+
	'<div>'+user+'</div><br/>';
	$('.wysihtml5-sandbox').contents().find('.wysihtml5-editor').html(htmlbody);
}
</script>
