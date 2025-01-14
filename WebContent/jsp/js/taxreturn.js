$(document).ready(function(){
	$.ajax({
		type : "GET",
		url: '/track/TaxSettingServlet',
		dataType: 'json',
		data : {
			action : "getTaxSetting",
		},
		success : function(data) {
			//alert(data.isTaxReg);
			if(data.isTaxReg=="1")
				{
					$("#isregyes").prop("checked", true);
				}
			else
				{
					$("#isregno").prop("checked", true);
				}
			$("#isregyes").prop("disabled", true);
			$("#isregno").prop("disabled", true);
			$("input[name=taxid]").val(data.ID);
			$("input[name=TaxCode]").val(data.TAXBYLABEL);
			$("input[name=TaxRegNo]").val(data.TAX);
			if(data.ISINTERNATIONALTRADE=='1')
				$("input[name=ENABLE_TRADE]").prop('checked', true);
			else
				$("input[name=ENABLE_TRADE]").prop('checked', false);
			$("input[name=vatregdate]").val(data.VATREGISTEREDON);
			$("input[name=generatefirsttax]").val(data.RETURNFROM);
			$("#reportingperiod").val(data.REPORTINGPERIOD);
			
			//console.log(data);
		}
	});
});