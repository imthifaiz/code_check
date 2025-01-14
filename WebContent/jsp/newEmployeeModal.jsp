<div id="employeeModal" class="modal fade" role="dialog">
	  <div class="modal-dialog modal-lg">	
	  <form name="employeeForm" method="post" >
	    <!-- Modal content-->
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	        <h4 class="modal-title">New Employee</h4>
	      </div>
	      <div class="modal-body">
	        <div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2 required">Employee Id:</label>
	        	<div class="col-sm-4">
		        	<div class="input-group">   
		        		<input class="form-control" name="CUST_CODE_EMP" id="CUST_CODE_EMP" type="text" MAXLENGTH=50  
		        			value="" onchange="checkemployee(this.value)"> 
	        			<span class="input-group-addon"  onClick="getNextEmployee();">
				   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
				   		 		<i class="glyphicon glyphicon-edit" style="font-size: 15px;"></i>
				   		 	</a>
			   		 	</span>
		   		 	</div>
		   		 	<INPUT type="hidden" name="COUNTRY" value="">
	        	</div>
	        	
	        	<label class="col-form-modal-label col-sm-2 required" for="Employee Name">Employee Name</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="CUST_NAME" id="CUST_NAME" MAXLENGTH=100 type="text" value=""> 
	        	</div>
        	</div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2 required">Gender:</label>
	        	<div class="col-sm-4">
	        		<div class="input-group">    
		        		<label class="radio-inline">
					      <INPUT name="GENDER"  type = "radio" value="M">Male 
					    </label>
					    <label class="radio-inline">
					      <INPUT name="GENDER" type = "radio" value="F">Female
					    </label>		        		
		   		 	</div>
		   		 	</div>
	        	
	        	<label class="col-form-modal-label col-sm-2">Date of Birth:</label>
	        	<div class="col-sm-4">	        		    
		        		<input class="form-control datepicker" name="DOB" id="DOB" MAXLENGTH=10  type="text" value="" readonly>
	        	</div>
        	</div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">Employee Phone:</label>
	        	<div class="col-sm-4">
	        	<div class="input-group">
	        		<INPUT name="TELNO" type="TEXT" value="" size="50" class="form-control" onkeypress="return isNumber(event)"
			MAXLENGTH="30"> 
	        	</div>
	        	</div>
	        	
	        	<label class="col-form-modal-label col-sm-2">Employee Email:</label>
	        	<div class="col-sm-4">   
		        <input class="form-control" name="EMAIL" id="EMAIL" type="text"  MAXLENGTH="50"  value="">
	        	</div>
        	</div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">Passport Number:</label>
	        	<div class="col-sm-4">
	        	<div class="input-group">
	        	  <input class="form-control" name="PASSPORTNUMBER" id="PASSPORTNUMBER" MAXLENGTH="50" type="text" value="">
	        	</div>
	        	</div>
	        	
	        	<label class="col-form-modal-label col-sm-2">Country of Issue:</label>
	        	<div class="col-sm-4">	        		
	        		<SELECT class="form-control" data-toggle="dropdown" data-placement="right" id="COUNTRYOFISSUE" name="COUNTRYOFISSUE" value="" style="width: 100%">
				<OPTION style="display:none;">Select Country</OPTION>
				</SELECT> 
	        	</div>
        	</div>
        	
        	
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2"></label>
	        	<div class="col-sm-4">
	        	<div class="input-group">
	        	<INPUT type="hidden" name="ISPOSCUSTOMER" value="0">
		    	<lable class="checkbox"><input type="checkbox" class="form-check-input" id=POSCUSTOMER name="POSCUSTOMER" value="0"  Onclick="setposcustomer(this)">Allow to Create POS Customer</lable>
	        		 
	        		 <INPUT type="hidden" name="ISEDITPOSPRODUCTPRICE" value="0">
		    	<lable class="checkbox"><input type="checkbox" class="form-check-input" id=EDITPOSPRODUCTPRICE name="EDITPOSPRODUCTPRICE" value="0"  Onclick="setproductprice(this)">Allow to Edit POS Product Prices</lable>
	        	</div>
	        	</div>
	        	<label class="col-form-modal-label col-sm-2">Passport ExpiryDate:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control datepicker" name="PASSPORTEXPIRYDATE" id="PASSPORTEXPIRYDATE" type="text" value="" MAXLENGTH=10 readonly> 
	        	</div>
        	</div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-8">POS Outlets</label>
	        	<div class="col-sm-4">
	        	<div class="input-group">
	        	  <INPUT class=" form-control" id="OUTLET_NAME" value="" name="OUTLET_NAME" type="TEXT" MAXLENGTH=100  placeholder="Select Outlet">
    		 		<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'OUTLET_NAME\']').focus()">
					<i class="glyphicon glyphicon-menu-down"></i></span>    
					<INPUT type="hidden" name="OUTCODE" value="">	
	        	</div>
	        	</div>
	        	</div>
        	
        	
        	
        	
        	<div class="bs-example">
     <ul class="nav nav-tabs" id="myTab"> 
     	<li class="nav-item active">
            <a href="#home_emp" class="nav-link" data-toggle="tab" aria-expanded="true">Address Details</a>
        </li>
        <li class="nav-item">
            <a href="#identity" class="nav-link" data-toggle="tab"><label id="identity_tab"></label></a>
        </li>
        <li class="nav-item">
            <a href="#employment" class="nav-link" data-toggle="tab">Employment Details</a>
        </li>
        <li class="nav-item">
            <a href="#contract" class="nav-link" data-toggle="tab">Contract Details</a>
        </li>
        <li class="nav-item">
            <a href="#bank" class="nav-link" data-toggle="tab">Bank Account Details</a>
        </li>
        <li class="nav-item">
            <a href="#salary" class="nav-link" data-toggle="tab">Salary Details</a>
        </li>        
        <li class="nav-item">
            <a href="#remark_emp" class="nav-link" data-toggle="tab">Remarks</a>
        </li>
        </ul>
        
        <div class="tab-content clearfix">
        <div class="tab-pane active" id="home_emp">
        <br>
        <div class="row form-group">
      <label class="col-form-modal-label col-sm-2" for="Country">Country</label>
      <div class="col-sm-4">
      <div class="input-group">  
       <SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnCountry_emp(this.value)" id="COUNTRY_CODE" name="COUNTRY_CODE" value="" style="width: 100%">
				<OPTION style="display:none;">Select Country</OPTION>
				</SELECT>
      </div>
      </div>
      <label class="col-form-modal-label col-sm-2" for="Unit No">Unit No.</label>
      <div class="col-sm-4">  
               
        <INPUT name="ADDR1" type="TEXT" value="" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
    </div>
      
    <div class="row form-group">
      <label class="col-form-modal-label col-sm-2" for="Building">Building</label>
      <div class="col-sm-4">
        <div class="input-group">      
        <INPUT name="ADDR2" type="TEXT" value="" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
      </div>
      
      <label class="col-form-modal-label col-sm-2" for="Street">Street</label>
      <div class="col-sm-4">
                
        <INPUT name="ADDR3" type="TEXT" value="" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
    </div>
    
    <div class="row form-group">
      <label class="col-form-modal-label col-sm-2" for="City">City</label>
      <div class="col-sm-4"> 
        <div class="input-group">        
        <INPUT name="ADDR4" type="TEXT" value="" size="50"
			MAXLENGTH=80  class="form-control">
      </div>
      </div>
      <label class="col-form-modal-label col-sm-2" for="State">State</label>
      <div class="col-sm-4">
      <SELECT class="form-control" data-toggle="dropdown" data-placement="right" id="STATE_EMP" name="STATE_EMP" value="" style="width: 100%">
				<OPTION style="display:none;">Select State</OPTION>
				</SELECT>
      </div>
    </div>
    
    
    
    <div class="row form-group">
      <label class="col-form-modal-label col-sm-2" for="Postal Code">Postal Code</label>
      <div class="col-sm-4">
        <div class="input-group">        
        <INPUT name="ZIP" type="TEXT" value="" size="50"
			MAXLENGTH=10 class="form-control">
      </div>
      </div>
      
      <label class="col-form-modal-label col-sm-2" for="Facebook">Facebook Id</label>
      	<div class="col-sm-4">  
        <INPUT name="FACEBOOK" type="TEXT" value=""	size="50" MAXLENGTH=50 class="form-control">
      	</div>
    </div>
    
    
        <div class="row form-group">
      	<label class="col-form-modal-label col-sm-2" for="Twitter">Twitter Id</label>
      	<div class="col-sm-4">  
        <div class="input-group">
        <INPUT name="TWITTER" type="TEXT" value=""	size="50" MAXLENGTH=50 class="form-control">
      	</div>
      	</div>
      	<label class="col-form-modal-label col-sm-2" for="Linkedin">LinkedIn Id</label>
      	<div class="col-sm-4">  
        <INPUT name="LINKEDIN" type="TEXT" value=""	size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
    
        <div class="row form-group">
      	<label class="col-form-modal-label col-sm-2" for="SkypeId">Skype Id</label>
      	<div class="col-sm-4">  
        <div class="input-group">
        <INPUT name="SKYPE" type="TEXT" value=""	size="50" MAXLENGTH=50 class="form-control">
      	</div>
      	</div>
      	
    	</div>
    	
        </div>
        
        <div class="tab-pane fade" id="identity">
        <br>
        
        <div class="row form-group">
      	<label class="col-form-modal-label col-sm-2" for="EmiratesId NO" id="id_no"></label>
      	<div class="col-sm-4">  
        <div class="input-group">
        <INPUT name="EMIRATESID" type="TEXT" value=""	size="50" MAXLENGTH=50 class="form-control">
      	</div>
      	</div>
      	<label class="col-form-modal-label col-sm-2" for="Emirates ExpiryDate" id="cout_expiryDate"></label>
      <div class="col-sm-4">                
        <input name="EMIRATESIDEXPIRY" id="EMIRATESIDEXPIRY" type="TEXT" value=""	size="50" MAXLENGTH=10 class="form-control datepicker" readonly>
      </div>
    	</div>
       
       <div class="row form-group">
      	<label class="col-form-modal-label col-sm-2" for="Visa NO">Visa Number</label>
      	<div class="col-sm-4">  
        <div class="input-group">
        <INPUT name="VISANUMBER" type="TEXT" value=""	size="50" MAXLENGTH=50 class="form-control">
      	</div>
      	</div>
      	<label class="col-form-modal-label col-sm-2" for="Visa ExpiryDate">Visa ExpiryDate</label>
      <div class="col-sm-4">                
        <input name="VISAEXPIRYDATE" id="VISAEXPIRYDATE" type="TEXT" value=""	size="50" MAXLENGTH=10 class="form-control datepicker" readonly>
      </div>
    	</div>
    	
        
        </div>        
        
        <div class="tab-pane fade" id="employment">
        <br>
        
        <div class="row form-group">
      	<label class="col-form-modal-label col-sm-2" for="Department">Department</label>
      	<div class="col-sm-4">  
        <div class="input-group">
        <INPUT name="DEPT" type="TEXT" value=""	size="50" MAXLENGTH=100 class="form-control">
      	</div>
      	</div>
      	<label class="col-form-modal-label col-sm-2" for="Date of Joining">Date of Joining</label>
      	<div class="col-sm-4">  
        <input name="DATEOFJOINING" id="DATEOFJOINING" type="TEXT" value=""	size="50" MAXLENGTH=10 class="form-control datepicker" readonly>
        
      	</div>
    	</div>
    	
    	
    	<div class="row form-group">
    	<label class="col-form-modal-label col-sm-2" for="Designation">Designation</label>      
      <div class="col-sm-4">                
        <div class="input-group">
        <INPUT name="DESGINATION" type="TEXT" value=""	size="50" MAXLENGTH=100 class="form-control">
      </div>
      </div>
      <label class="col-form-modal-label col-sm-2" for="Date of Leaving">Date of Leaving</label>
      <div class="col-sm-4">                
        <input name="DATEOFLEAVING" id="DATEOFLEAVING" type="TEXT" value=""	size="50" MAXLENGTH=10 class="form-control datepicker" readonly>
      </div>
       </div>
       
       <div class="col-form-modal-label col-sm-4" style="padding-right: 0%;">
      	<INPUT type="hidden" name="ISCASHIER" value="0">
		<lable class="checkbox-inline"><input type="checkbox" class="form-check-input" id="CASHIER" name="CASHIER"  Onclick="setcashier(this)">Cashier</lable>
		
      	<INPUT type="hidden" name="ISSALESMAN" value="0">
		<lable class="checkbox-inline"><input type="checkbox" class="form-check-input" id="SALESMAN" name="SALESMAN"   Onclick="setsalesman(this)">Sales Man</lable>
        </div>
        </div>
        
        <div class="tab-pane fade" id="contract">
        <br>
        
        <div class="row form-group">
      	<label class="col-form-modal-label col-sm-2" for="Labour Card Number">Labour Card Number</label>
      	<div class="col-sm-4">  
        <div class="input-group">
        <INPUT name="LABOURCARDNUMBER" type="TEXT" value=""	size="50" MAXLENGTH=100 class="form-control">
      	</div>
    	</div>
    	<label class="col-form-modal-label col-sm-2" for="Contract StartDate">Contract StartDate</label>
      	<div class="col-sm-4">  
        <input name="CONTRACTSTARTDATE" type="TEXT" value=""	size="50" MAXLENGTH=10 class="form-control datepicker" readonly>
      	</div>
    	</div>
    	
    	<div class="row form-group">
      <label class="col-form-modal-label col-sm-2" for="Work Permit Number">Work Permit Number</label>
      <div class="col-sm-4">                
        <div class="input-group">
        <INPUT name="WORKPERMITNUMBER" type="TEXT" value=""	size="50" MAXLENGTH=100 class="form-control">
      </div>
      </div>
      <label class="col-form-modal-label col-sm-2" for="Contract ExpiryDate">Contract ExpiryDate</label>
      <div class="col-sm-4">                
        <input name="CONTRACTENDDATE" id="CONTRACTENDDATE" type="TEXT" value=""	size="50" MAXLENGTH=10 class="form-control datepicker" readonly>
      </div>
       </div>  
        </div>
        
        <div class="tab-pane fade" id="bank">
        <br>
        
        <div class="row form-group">
      	<label class="col-form-modal-label col-sm-2" for="IBAN">IBAN</label>
      	<div class="col-sm-4">  
        <div class="input-group">
        <INPUT name="IBAN" type="TEXT" value=""	size="50" MAXLENGTH=100 class="form-control">
      	</div>
      	</div>
      	<label class="col-form-modal-label col-sm-2">Bank</label>
			<div class="col-sm-4 ac-box">				
				<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnBank(this.value)" id="BANKNAME" name="BANKNAME" value="" style="width: 100%">
				<OPTION style="display:none;">Select Bank</OPTION>
				</SELECT>
			</div>
    	</div>
		
		<div class="row form-group">
      	<label class="col-form-modal-label col-sm-2" for="Branch">Branch</label>
      	<div class="col-sm-4">  
        <div class="input-group">
        <INPUT name="BRANCH" type="TEXT" value=""	size="50" MAXLENGTH=100 class="form-control" readonly>
      	</div>
      	</div>
      	<label class="col-form-modal-label col-sm-2" for="Routing Code">Routing Code</label>
      	<div class="col-sm-4">  
        <INPUT name="BANKROUTINGCODE" type="TEXT" value=""	size="50" MAXLENGTH=100 class="form-control">
      	</div>
    	</div>
        
        </div>
        
        <div class="tab-pane fade" id="salary">
        <br>
        
        <div class="row form-group">
      	<label class="col-form-modal-label col-sm-2" for="Basic Salary">Basic Salary</label>
      	<div class="col-sm-4">  
        <div class="input-group">
        <INPUT name="BASICSALARY" id="BASICSALARY" type="TEXT" value="" onkeypress="return isNumberKey(event,this,4)"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
      	</div>
      	<label class="col-form-modal-label col-sm-2" for="House Rent Allowance">House Rent Allowance</label>
      	<div class="col-sm-4">  
        <INPUT name="HOUSERENTALLOWANCE" id="HOUSERENTALLOWANCE" type="TEXT" value="" onkeypress="return isNumberKey(event,this,4)"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
    	
    	<div class="row form-group">
      	<label class="col-form-modal-label col-sm-2" for="Transport Allowance">Transport Allowance</label>
      	<div class="col-sm-4">  
        <div class="input-group">
        <INPUT name="TRANSPORTALLOWANCE" id="TRANSPORTALLOWANCE" type="TEXT" value="" onkeypress="return isNumberKey(event,this,4)"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
      	</div>
      	<label class="col-form-modal-label col-sm-2" for="Communication Allowance">Communication Allowance</label>
      	<div class="col-sm-4">  
        <INPUT name="COMMUNICATIONALLOWANCE" id="COMMUNICATIONALLOWANCE" type="TEXT" value="" onkeypress="return isNumberKey(event,this,4)"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
    	
    	<div class="row form-group">
      	<label class="col-form-modal-label col-sm-2" for="Other Allowance">Other Allowance</label>
      	<div class="col-sm-4">  
        <div class="input-group">
        <INPUT name="OTHERALLOWANCE" id="OTHERALLOWANCE" type="TEXT" value="" onkeypress="return isNumberKey(event,this,4)"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
      	</div>
      	<label class="col-form-modal-label col-sm-2" for="Bonus">Bonus</label>
      	<div class="col-sm-4">  
        <INPUT name="BONUS" id="BONUS" type="TEXT" value="" onkeypress="return isNumberKey(event,this,4)"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
    	
    	<div class="row form-group">
      	<label class="col-form-modal-label col-sm-2" for="Commission">Commission</label>
      	<div class="col-sm-4">  
        <div class="input-group">
        <INPUT name="COMMISSION" id="COMMISSION" type="TEXT" value=""	size="50" MAXLENGTH=50 class="form-control">
      	</div>
      	</div>
      	
    	</div>
        
        </div>        
        
        <div class="tab-pane fade" id="remark_emp">
        <br>
       <div class="form-group">
      <label class="col-form-modal-label col-sm-2" for="Remarks">Remarks</label>
      <div class="col-sm-4">
        <textarea  class="form-control" name="REMARKS"   MAXLENGTH=1000></textarea>
      </div>
    </div>
		     
        </div>
        
        </div>
        
        </div>
        	        	
        	
	      </div>
	      <div class="modal-footer">
	      	<button id="btnBillOpen" type="button" class="btn btn-success" onClick="EmployeeValidNumber();">Save</button>
			<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	      </div>
	    </div>
	    </form>
	  </div>
	</div>
	<script>
	$(document).ready(function(){
		getdata();		
		getCountry();
		getCountryIssue();
		getBank();
		$("#DOB").datepicker({dateFormat: 'dd/mm/yy',changeMonth: true, changeYear: true, yearRange: '-100:+0'});
		$('#PASSPORTEXPIRYDATE').datepicker({dateFormat: 'dd/mm/yy',changeMonth: true, changeYear: true, yearRange: '-10:+20'});
		$('#EMIRATESIDEXPIRY').datepicker({dateFormat: 'dd/mm/yy',changeMonth: true, changeYear: true, yearRange: '-10:+20'});
		$('#VISAEXPIRYDATE').datepicker({dateFormat: 'dd/mm/yy',changeMonth: true, changeYear: true, yearRange: '-10:+20'});
		$('#DATEOFJOINING').datepicker({dateFormat: 'dd/mm/yy',changeMonth: true, changeYear: true, yearRange: '-10:+10'});
		$('#DATEOFLEAVING').datepicker({dateFormat: 'dd/mm/yy',changeMonth: true, changeYear: true, yearRange: '-10:+20'});
		$('#CONTRACTSTARTDATE').datepicker({dateFormat: 'dd/mm/yy',changeMonth: true, changeYear: true, yearRange: '-10:+10'});
		$('#CONTRACTENDDATE').datepicker({dateFormat: 'dd/mm/yy',changeMonth: true, changeYear: true, yearRange: '-10:+20'});
		
		var nofdecimal= document.form1.NOFO_DEC.value;
		var decVal = parseFloat(0).toFixed(nofdecimal);
		
		  $("#BASICSALARY").val(decVal);
		  $("#HOUSERENTALLOWANCE").val(decVal);
		  $("#TRANSPORTALLOWANCE").val(decVal);
		  $("#COMMUNICATIONALLOWANCE").val(decVal);
		  $("#OTHERALLOWANCE").val(decVal);
		  $("#BONUS").val(decVal);
		  $("#COMMISSION").val(decVal);

		  var plant = document.form1.plant.value;
		  $('#OUTLET_NAME').typeahead({
    	  	  hint: true,
    	  	  minLength:0,  
    	  	  searchOnFocus: true
    	  	},
    	  	{
    	  	  display: 'OUTLET_NAME',  
    	  	  async: true,   
    	  	  source: function (query, process,asyncProcess) {
    	  		var urlStr = "/track/MasterServlet";
    	  		$.ajax( {
    	  		type : "POST",
    	  		url : urlStr,
    	  		async : true,
    	  		data : {
    	  			PLANT : plant,
    	  			ACTION : "GET_OUTLET_DATA",
    	  			QUERY : query
    	  		},
    	  		dataType : "json",
    	  		success : function(data) {
    	  			return asyncProcess(data.POSOUTLETS);
    	  		}
    	  		});
    	  	  },
    	  	  limit: 9999,
    	  	  templates: {
    	  	  empty: [
    	  	      '<div style="padding:3px 20px">',
    	  	        'No results found',
    	  	      '</div>',
    	  	    ].join('\n'),
    	  	    suggestion: function(data) {
//    	   		return '<div onclick="document.form.OUTLET_NAME.value = \''+data.OUTLET_NAME+'\'"><p class="item-suggestion">Name: ' + data.OUTLET_NAME + '</p><br/><p class="item-suggestion">Code: ' + data.OUTLET + '</p></div>';
    	  	    	return '<div onclick="setOutletData(\''+data.OUTLET+'\',\''+data.OUTLET_NAME+'\')"><p class="item-suggestion">Name: ' + data.OUTLET_NAME + '</p><br/><p class="item-suggestion">Code: ' + data.OUTLET + '</p></div>';
    	  		}
    	  	  }
    	  	}).on('typeahead:open',function(event,selection){
    	  		var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
    	  		element.toggleClass("glyphicon-menu-up",true);
    	  		element.toggleClass("glyphicon-menu-down",false);
    	  	}).on('typeahead:close',function(){
    	  		var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
    	  		element.toggleClass("glyphicon-menu-up",false);
    	  		element.toggleClass("glyphicon-menu-down",true);
    	  	}).on('typeahead:change',function(event,selection){
    			if($(this).val() == ""){
    				document.form.OUTCODE.value = "";
    			}
    	  	});
	});
	
	function EmployeeValidNumber() {
		
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				ACTION : "NOOFEMPLOYEE_CHECK"
			},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						alert("You have reached the limit of "+data.ValidNumber+" employees you can create");
						return false;
					}
					else
						onEmployeeAdd();
				}
		});
	    		
	}
	
	function onEmployeeAdd(){
	   var CUST_CODE = document.employeeForm.CUST_CODE_EMP.value;
	   var CUST_NAME = document.employeeForm.CUST_NAME.value;
	   
	   if(CUST_CODE == "" || CUST_CODE == null) {
		   alert("Please Enter Employee ID");
		   document.employeeForm.CUST_CODE_EMP.focus(); 
		   return false; 
	   }	   
	   if(CUST_NAME == "" || CUST_NAME == null) {
	   	alert("Please Enter Employee Name"); 
	   	document.employeeForm.CUST_NAME.focus();
	   	return false; 
	   }
	   var radio_choice = false;
	   for (i = 0; i < document.employeeForm.GENDER.length; i++)
	   {
	       if (document.employeeForm.GENDER[i].checked)
	       radio_choice = true; 
	   }
	   if (!radio_choice)
	   {
	   alert("Please Select Gender.");
	   return (false);
	   }
	   
	   var data = $("form[name ='employeeForm']").serialize();
	   var urlStr = "/track/MasterServlet?action=CREATE_EMPLOYEE";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : data,
				dataType : "json",
				success : function(data) {
					if (data.STATUS == "FAIL") {		                               
						alert(data.MESSAGE);
					}
					else{
						document.employeeForm.reset();
						$('#employeeModal').modal('toggle');
						employeeCallback(data);	/* Define this method in parent page*/
					}						
				}
		});
		getdata();
		return false;
	}
	
	function checkemployee(aCustCode)
	{	
		 if(aCustCode=="" || aCustCode.length==0 ) {
			alert("Enter Employee ID!");
			document.getElementById("CUST_CODE_EMP").focus();
			return false;
		 }else{ 
			var urlStr = "/track/MasterServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					CUST_CODE : aCustCode,
	                USERID : document.form1.username,/* create this field in parent page*/
					PLANT : document.form1.plant,/* create this field in parent page*/
					ACTION : "EMPLOYEE_CHECK"
				},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {		                               
							alert("Employee Already Exists");
							document.getElementById("CUST_CODE_EMP").focus();
							return false;
						}
						else
							return true;
					}
			});	
			return true;
		}
	}
	
	function getNextEmployee(){
		$.ajax( {
			type : "GET",
			url : "/track/MasterServlet?ACTION=GET_EMPLOYEE_SEQUENCE",
			async : true,
			dataType : "json",
			contentType: false,
	        processData: false,
	        success : function(data) {
				if (data.ERROR_CODE == "100") {
					document.employeeForm.CUST_CODE_EMP.value = data.CUSTCODE;
				}
				else{
					alert("Something went wrong. Please try again later.");
				}						
			},
	        error: function (data) {	        	
	        	alert(data.Message);
	        }
		});	
		return false;
	}
	
	function OnBank(BankName)
	{
		$.ajax({
			type : "POST",
			url: "/track/MasterServlet?ACTION=GET_BANK_DATA&NAME="+BankName,
			async : true,
			dataType: 'json',
			contentType: false,
	        processData: false,
			success : function(dataitem) {
				var BankList=dataitem.BANKMST;
				var myJSON = JSON.stringify(BankList);						
				var dt = JSON.stringify(BankList).replace('[', '').replace(']', '');
				if (dt) {
				  var result = jQuery.parseJSON(dt);			  
				  var val = result.BRANCH;			  
				  $("input[name ='BRANCH']").val(val);
				}
			}
		});		
	}
	function OnCountry_emp(Country)
	{
		$.ajax({
			type : "POST",
			url: "/track/MasterServlet?ACTION=GET_STATE_DATA&COUNTRY="+Country,
			async : true,
			dataType: 'json',
			contentType: false,
	        processData: false,			
			success : function(dataitem) {
				var StateList=dataitem.STATEMST;
				var myJSON = JSON.stringify(StateList);
				$('#STATE_EMP').empty();
			//	$('#STATE_EMP').append('<OPTION style="display:none;">Select State</OPTION>');
				$('#STATE').append('<OPTION>Select State</OPTION>');
					 $.each(StateList, function (key, value) {
						   $('#STATE_EMP').append('<option value="' + value.text + '">' + value.text + '</option>');
						});
			}
		});	
		
	}
	function getCountry()
	{

		$.ajax({
			type : "POST",			
			url : "/track/MasterServlet?ACTION=GET_COUNTRY_DATA",
			async : true,
			dataType: 'json',
			contentType: false,
	        processData: false,
			success : function(dataitem) {
				var CountryList=dataitem.COUNTRYMST;
				var myJSON = JSON.stringify(CountryList);
				$('#COUNTRY_CODE').empty();
				$('#COUNTRY_CODE').append('<OPTION style="display:none;">Select Country</OPTION>');
					 $.each(CountryList, function (key, value) {
						   $('#COUNTRY_CODE').append('<option value="' + value.COUNTRY_CODE + '">' + value.COUNTRYNAME + '</option>');
						});
			}
		});	
		
	}
	function getCountryIssue()
	{

		$.ajax({
			type : "POST",			
			url : "/track/MasterServlet?ACTION=GET_COUNTRY_DATA",
			async : true,
			dataType: 'json',
			contentType: false,
	        processData: false,
			success : function(dataitem) {
				var CountryList=dataitem.COUNTRYMST;
				var myJSON = JSON.stringify(CountryList);
				$('#COUNTRYOFISSUE').empty();
				$('#COUNTRYOFISSUE').append('<OPTION style="display:none;">Select Country</OPTION>');
					 $.each(CountryList, function (key, value) {
						   $('#COUNTRYOFISSUE').append('<option value="' + value.COUNTRYNAME + '">' + value.COUNTRYNAME + '</option>');
						});
			}
		});	
		
	}
	function getBank()
	{
		$.ajax({
			type : "POST",
			url: "/track/MasterServlet?ACTION=GET_BANK_DATA_ALL",
			async : true,
			dataType: 'json',
			contentType: false,
	        processData: false,
			success : function(dataitem) {
				var BankList=dataitem.BANKMST;
				var myJSON = JSON.stringify(BankList);
				$('#BANKNAME').empty();
				$('#BANKNAME').append('<OPTION style="display:none;">Select Bank</OPTION>');
					 $.each(BankList, function (key, value) {
						   $('#BANKNAME').append('<option value="' + value.text + '">' + value.text + '</option>');
						});
			}
		});	
		
	}
	 function setposcustomer(obj) {
			if ($(obj).is(":checked")) {
				$("input[name ='ISPOSCUSTOMER']").val("1");
			} else {
				$("input[name ='ISPOSCUSTOMER']").val("0");
			}
		} 

	 function setproductprice(obj) {
			if ($(obj).is(":checked")) {
				$("input[name ='ISEDITPOSPRODUCTPRICE']").val("1");
			} else {
				$("input[name ='ISEDITPOSPRODUCTPRICE']").val("0");
			}
		} 

	 function setcashier(obj) {
			if ($(obj).is(":checked")) {
				$("input[name ='ISCASHIER']").val("1");
			} else {
				$("input[name ='ISCASHIER']").val("0");
			}
		}
	    function setsalesman(obj) {
			if ($(obj).is(":checked")) {
				$("input[name ='ISSALESMAN']").val("1");
			} else {
				$("input[name ='ISSALESMAN']").val("0");
			}
		}

	 function setOutletData(OUTLET,OUTLET_NAME){
	    	$("input[name=OUTCODE]").val(OUTLET);
	    	$("input[name=OUTLET_NAME]").val(OUTLET_NAME);
	    }

	function getdata()
	{
		var  d = document.getElementById("COUNTRY_REG").value;
	    document.getElementById('id_no').innerHTML = d+" ID Number";	
	    document.getElementById('cout_expiryDate').innerHTML = d+" ExpiryDate";
	    document.getElementById('identity_tab').innerHTML = d+" Identity";
	}
	$('select[name="COUNTRY_CODE"]').on('change', function(){
	    var text = $("#COUNTRY_CODE option:selected").text();
	    $("input[name ='COUNTRY']").val(text.trim());
	});
	
	</script>
