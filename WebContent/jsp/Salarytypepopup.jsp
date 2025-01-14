
<div id="create_salary_type" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h3 class="modal-title">Add Salary Type</h3>
			</div>
			<div class="modal-body">
				<form class="form-horizontal" name="salarytypeformpopup">
					<input type="hidden" class="form-control" id="ukeylt" name="ukeylt" value="">
					
					<div class="form-group">
						<label class="control-label col-form-label col-sm-3 required">Salary Type</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" name="salarytypelt" maxlength="100" placeholder="Enter Salary Type">
						</div>
					</div>
					
					<div class="form-group">
					<div class="col-sm-offset-3 col-sm-9">
					
 					<input type="hidden" name="PAYROLL_BY_BASIC_SALARY" value = "0">
 					<label class="control-label col-form-label"  >
       		  <input type="checkbox" value="1"  name="ISPAYROLL_BY_BASIC_SALARY" id="ISPAYROLL_BY_BASIC_SALARY"  onclick="payrollbybasicsalary(this)"><span id="BY_BASIC_SALARY"> Deduct CPF Contribution&nbsp;&nbsp; </span> 
              </label>
              		</div>
					</div>

					<div class="row">
						<div class="col-sm-12 txn-buttons">
							<button type="button" class="btn btn-success" onclick="create_salarytype()">Save and Close</button>
							<button class="btn btn-default" data-dismiss="modal" id="cancel">Cancel</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

<script>

$(document).ready(function(){

	$(".table tbody").on('click','.salarytypepopup',function(){	
		 var obj = $(this);
		 var timestamp = new Date().getUTCMilliseconds();
		 kayid = "key"+timestamp;
		 $(obj).closest('td').attr('id', kayid); 
		 $("input[name ='ukeylt']").val(kayid);
	});

	//var  d = document.getElementById("BASIC_SALARY").value;
   // document.getElementById('BY_BASIC_SALARY').innerHTML = d ;
	//$('#BY_BASIC_SALARY').text(d);
	   
});
	function create_salarytype() {
		
		var salarytype=$('input[name = "salarytypelt"]').val();
		if(salarytype == ""){
			alert("Please enter salary type.");
			document.salarytypeformpopup.salarytypelt.focus();
			return false;
		}
		
		
		var formData = $('form[name="salarytypeformpopup"]').serialize();
		$.ajax({
			type : 'post',
			url : "/track/HrSalaryServlet?Submit=Savepopup",
			dataType : 'json',
			data : formData,//{key:val}
			success : function(data) {
				if (data.STATUS == "FAIL") {		                               
					alert(data.MESSAGE);
				}else{
					var ukey = document.salarytypeformpopup.ukeylt.value;
					ukey = "#"+ukey;
					$(ukey).parents("tr").find('input[name="empSalary"]').val(data.SALARYTYPE);
					$(ukey).parents("tr").find('input[name="empSalaryid"]').val(data.SALARYTYPEID);
					 if(data.ISPAYROLL_BY_BASIC_SALARY == "1") {
					$(ukey).parents("tr").find("input[name ='ISPAYROLL_BY_BASIC_SALARY']").prop('checked', true);
					}
					 document.salarytypeformpopup.reset(); 
					$('#create_salary_type').modal('toggle');
				}
			},
			error : function(data) {

				alert(data.responseText);
			}
		});
		return false;
	}
	
	function payrollbybasicsalary(obj){
		var manageapp = $(obj).closest('tr').find('input[name=PAYROLL_BY_BASIC_SALARY]').val();
		if(manageapp == 0)
		$(obj).closest('tr').find('input[name=PAYROLL_BY_BASIC_SALARY]').val(1);
		else
		$(obj).closest('tr').find('input[name=PAYROLL_BY_BASIC_SALARY]').val(0);
		
	}
	
	
</script>
