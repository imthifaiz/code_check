
<div id="productModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-lg box-body">	
	<form name="productForm" id="productForm" method="post" enctype="multipart/form-data">
	  <!-- Modal content-->
	  <div class="modal-content">
	    <div class="modal-header">
	      <button type="button" class="close" data-dismiss="modal">&times;</button>
	      <h3 class="modal-title">New Product</h3>
	    </div>
	    <div class="modal-body">
	
	      	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2 required">Product ID:</label>
	        	<div class="col-sm-4">
	        		<div class="input-group">
		        		<input class="form-control" name="ITEM" id="ITEM" type="text" onchange="checkprditem(this.value)" onkeypress="return blockSpecialChar(event)" value=""> 
		        		<span class="input-group-addon"  onClick="getNextPrd();" sNewEnb >
					   		<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
					   			<i class="glyphicon glyphicon-edit" style="font-size: 15px;"></i>
					   		</a>
				   		</span>
			   		</div>
	        	</div>
	        	<div class="col-sm-6">
	        		<label class="radio-inline">
			      		<input type="radio" name="NONSTOCKFLAG" type = "radio"  value="N"  id="NotNonStock" checked="checked" onclick="DisplayNonStkType();">Stock
			    	</label>
			    	<label class="radio-inline">
			      		<input type="radio" name="NONSTOCKFLAG" type = "radio" value="Y"  id = "NonStock" onclick="DisplayNonStkType();">Non Stock
			    	</label>
	        	</div>
	        	</div>
	        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2 required">Description:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="DESC" id="DESC" type="text" value=""> 
	        	</div>
        	</div>
        	
        	<div class="row form-group" id="divdesc">
	        	<label class="col-form-modal-label col-sm-2">Detailed Description:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="REMARKS" id="REMARKS" type="text" value=""> 
	        	</div>
	        	
	        	
        	</div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2 required">Base UOM:</label>
	        	<div class="col-sm-4">
	        		<div class="input-group">
		        		<INPUT class="form-control" name="UOM" type="TEXT" value="" size="20" MAXLENGTH=100 id="Basicuom" onchange="checkuom(this.value)"  placeholder="Select a UOM">
				        <span class="select-icon" style="right: 15px;" onclick="$(this).parent().find('input[name=\'UOM\']').focus()">
				        <i class="glyphicon glyphicon-menu-down" id="uombtnpop"></i></span>
			   		</div>
	        	</div>
	        	
	        	<div class="col-sm-6">
	        		<label class="checkbox-inline">
	        			<INPUT Type=Checkbox  style="border:0;" name ="ISBASICUOM" value="ISBASICUOM" 
	        				id="ISBASICUOM" onclick="isbasicuom();" >
                     	<b>Apply to all UOM</b>
                   	</label>
	        	</div>
        	</div>
        	
        	<div class="row form-group" style="display: none;">
					<label class="col-form-modal-label col-sm-2" for="supplier name">Supplier Name/ID:</label>
        			<div class="col-sm-4">
        			<div class="input-group">
        					<input type="hidden" name="vendno" value="" >
        					<input type = "hidden" name="custModal">
        					<input type="hidden" name="PRD_CLS_ID" value="">
 		                    <input type="hidden" name="PRD_DEPT_ID" value="">
 		                    <input type="hidden" name="PRD_BRAND" value="">
 		                    <input type="hidden" name="ARTIST" value="">
 		                    <input type = "hidden" name="uomModal">
    		 				<INPUT class=" form-control" id="vendname_itemmst" value="" name="vendname_itemmst" onchange="checkItemsupplier(this.value)" type="TEXT" size="30" MAXLENGTH=100 placeholder="Select Supplier Name/ID">
    		 				<span class="select-icon" style="right: 15px;" onclick="$(this).parent().find('input[name=\'vendname_itemmst\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
							</span>    						
        			</div>
				</div>
				<!-- <div class="col-sm-6">
	        		<label class="checkbox-inline">
	        			<INPUT Type=Checkbox  style="border:0;" name ="ISPOSDISCOUNT" value="ISPOSDISCOUNT" 
	        				id="ISPOSDISCOUNT"  onclick="isposdiscount();">
                     	<b>Allow POS Terminal Discount</b>
                   	</label>
	        	</div> -->
				
				</div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">Net Weight:</label>
	        	<div class="col-sm-4">
	        		<div class="input-group">
		        		<input class="form-control" name="NETWEIGHT" id="NETWEIGHT" type="text" value="" onkeypress="return isNumberKey(event,this,4)">
			   		</div>
	        	</div>
	        	<div class="col-sm-6" id="ISarrival">
	        		<label class="checkbox-inline">
	        			<INPUT Type=Checkbox  style="border:0;" name ="ISNEWARRIVAL" value="ISNEWARRIVAL" 
	        				id="ISNEWARRIVAL"  onclick="isnewarrival();">
                     	<b>New Arrival</b>
                   	</label>
	        	</div>
	        	</div>
	        	
	        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">Gross Weight (KG):</label>
	        	<div class="col-sm-4">
	        		<div class="input-group">
		        	<input class="form-control" name="GROSSWEIGHT" id="GROSSWEIGHT" type="text" value="" onkeypress="return isNumberKey(event,this,4)">	
			   		</div>
	        	</div>
	        	<div class="col-sm-6" id="ISselling">
	        		<label class="checkbox-inline">
	        			<INPUT Type=Checkbox  style="border:0;" name ="ISTOPSELLING" value="ISTOPSELLING" 
	        				id="ISTOPSELLING"  onclick="istopselling();">
                     	<b>Top Selling Product</b>
                   	</label>
	        	</div>
	        	</div>
	        	
	        	<div class="row form-group" id="divlocid">
      <label class="col-form-modal-label col-sm-2" for="Location">Location:</label>
      <div class="col-sm-4">
      <div class="input-group">                
			<INPUT class="form-control" name="LOC_ID" type="TEXT" value="" size="20" MAXLENGTH=100 id="LOC_ID" onchange="checkprddept(this.value)" placeholder="Select Location">
			<span class="select-icon" style="right: 15px;" onclick="$(this).parent().find('input[name=\'LOC_ID\']').focus()">
        <i class="glyphicon glyphicon-menu-down" id="prdeptbtnpop"></i></span>				
      </div>
    </div>
    </div>
	        	
	        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">Dimension</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="DIMENSION" id="DIMENSION" type="text" value=""> 
	        	</div>
				<div class="col-sm-6">
	        		<label class="checkbox-inline">
	        			<INPUT Type=Checkbox  style="border:0;" name ="ISPOSDISCOUNT" value="ISPOSDISCOUNT" 
	        				id="ISPOSDISCOUNT"  onclick="isposdiscount();">
                     	<b>Allow POS Terminal Discount</b>
                   	</label>
	        	</div> 
	        	</div>
	        	<div class="row form-group">
      <label class="col-form-modal-label col-sm-2" for="Product Department">Product Department:</label>
      <div class="col-sm-4">
      <div class="input-group">                
			<INPUT class="form-control" name="PRD_DEPT_DESC" type="TEXT" value="" size="20" MAXLENGTH=100 id="PRD_DEPT_DESC" onchange="checkprddept(this.value)" placeholder="Select Product Department">
			<span class="select-icon" style="right: 15px;" onclick="$(this).parent().find('input[name=\'PRD_DEPT_DESC\']').focus()">
        <i class="glyphicon glyphicon-menu-down" id="prdeptbtnpop"></i></span>				
      </div>
    </div>
    </div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">Product Category:</label>
	        	<div class="col-sm-4">
	        		<div class="input-group">
		        		<INPUT class="form-control" name="PRD_CLS_DESC" type="TEXT" value="" size="20" MAXLENGTH=100 id="PRD_CLS_DESC" onchange="checkprdcat(this.value)" placeholder="Select Product Category">
						<span class="select-icon" style="right: 15px;" onclick="$(this).parent().find('input[name=\'PRD_CLS_DESC\']').focus()">
			        	<i class="glyphicon glyphicon-menu-down" id="prclassbtnpop"></i></span>
			   		</div>
	        	</div>
	        	</div>
	        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">Product Sub Category:</label>
	        	<div class="col-sm-4">
	        		<div class="input-group">
		        		<INPUT class="form-control" name="PRD_TYPE_DESC" type="TEXT" value="" size="20" MAXLENGTH=100 id="PRD_TYPE_DESC" onchange="checkprdtype(this.value)" placeholder="Select Product Sub Category">
						<span class="select-icon" style="right: 15px;" onclick="$(this).parent().find('input[name=\'PRD_TYPE_DESC\']').focus()">
			           	<i class="glyphicon glyphicon-menu-down" id="prtypbtnpop"></i></span>
			   		</div>
	        	</div>
        	</div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">Product Brand:</label>
	        	<div class="col-sm-4">
	        		<div class="input-group">
		        		<INPUT class="form-control" name="PRD_BRAND_DESC" type="TEXT" value="" size="20" MAXLENGTH=100 id="PRD_BRAND_DESC" onchange="checkprdbrand(this.value)" placeholder="Select Product Brand">
						<span class="select-icon" style="right: 15px;" onclick="$(this).parent().find('input[name=\'PRD_BRAND_DESC\']').focus()">
			        	<i class="glyphicon glyphicon-menu-down" id="prbadbtnpop"></i></span>
			   		</div>
	        	</div>
	        	</div>
	        	<div class="row form-group" style="display:none">
	        	<label class="col-form-modal-label col-sm-2" id="TaxLabelOrderManagement" value="" ></label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="PRODGST" id="PRODGST" type="text" value="" onkeypress="return isNumberKey(event,this,4)"> 
	        	</div>
        	</div>
        	
        	<div class="row form-group" id ="divimg">
	        	<label class="col-form-modal-label col-sm-2">Upload Image:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="IMAGE_UPLOAD" id="IMAGE_UPLOAD" type="File"> 
	        	</div>
        	</div>
        	
        	<div class="bs-example">
	<ul class="nav nav-tabs" id="myTab" style="font-size: 94%;"> 
     	<li class="nav-item active">
            <a href="#others" class="nav-link" data-toggle="tab" aria-expanded="true">Other Details</a>
        </li>
        <li class="nav-item">
            <a href="#purchase" class="nav-link" data-toggle="tab">Purchase</a>
        </li>
        <li class="nav-item">
            <a href="#sales" class="nav-link" data-toggle="tab">Sales</a>
        </li>
      <!--   <li class="nav-item">
            <a href="#rental" class="nav-link" data-toggle="tab">Rental</a>
        </li> -->
        <li class="nav-item">
            <a href="#inventoryps" class="nav-link" data-toggle="tab">Inventory</a>
        </li>
        <li class="nav-item" id = "descid">
            <a href="#additionaldetail" class="nav-link" data-toggle="tab">Additional Detail Description</a>
        </li>
        <li class="nav-item" id = "catid">
            <a href="#catalogues" class="nav-link" data-toggle="tab">Catalogues</a>
        </li>
        <li class="nav-item" id ="prdid">
            <a href="#prds" class="nav-link" data-toggle="tab">Additional Products</a>
        </li>
        <li class="nav-item">
            <a href="#remarks" class="nav-link" data-toggle="tab">Remarks</a>
        </li>
        </ul>
        
        <div class="tab-content clearfix">
        <div class="tab-pane active" id="others">
        <br>
        
        <div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">HS Code:</label>
	        	<div class="col-sm-4">
	        		<div class="input-group">
		        		<INPUT class="form-control" name="HSCODE" type="TEXT" value="" size="20" MAXLENGTH=100 id="HSCODE" onchange="checkhscode(this.value)" placeholder="Select HS Code">
						<span class="select-icon" style="right: 15px;" onclick="$(this).parent().find('input[name=\'HSCODE\']').focus()">
			        	<i class="glyphicon glyphicon-menu-down" id="hsbtnpop"></i></span> 
			   		</div>
	        	</div>
	        	</div>
	        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">COO:</label>
	        	<div class="col-sm-4">
	        		<div class="input-group">
		        		<INPUT class="form-control" name="COO" type="TEXT" value="" size="20" MAXLENGTH=100 id="COO" onchange="checkcoo(this.value)" placeholder="Select COO">
						<span class="select-icon" style="right: 15px;" onclick="$(this).parent().find('input[name=\'COO\']').focus()">
			        	<i class="glyphicon glyphicon-menu-down" id="coobtnpop"></i></span>
			   		</div>
	        	</div>
        	</div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">VIN NO:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="VINNO" id="VINNO" type="text" value=""> 
	        	</div>
	        	</div>
	        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">MODEL:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="MODEL" id="MODEL" type="text" value=""> 
	        	</div>
        	</div>
        
        </div>
        
        <div class="tab-pane fade" id="purchase">
        <br>
        
        <div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2 required">UOM:</label>
	        	<div class="col-sm-4">
	        		<div class="input-group"> 
	        		<INPUT class="form-control" name="PURCHASEUOM" type="TEXT" value="" size="20" MAXLENGTH=100 id="purchaseuom" onchange="checkpuruom(this.value)" placeholder="Select UOM">
			        <span class="select-icon" style="right: 15px;" onclick="$(this).parent().find('input[name=\'PURCHASEUOM\']').focus()">
			        <i class="glyphicon glyphicon-menu-down" id="purchaseuombtnpop"></i></span>
			   		</div>
	        	</div>
        	</div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">Cost:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="COST" id="COST" type="text" value="" onkeypress="return isNumberKey(event,this,4)"> 
	        	</div>	        	
        	</div>
        	
	<INPUT type="hidden" name="DYNAMIC_ITEMSUPPLIER_SIZE">     
        <div class="row form-group">
      <label class="col-form-modal-label col-sm-2" for="Item Supplier Discount">Supplier Name/ID:</label>
      <div class="col-sm-4">
			<TABLE id="multiitemsup">
		<TR>
		<TD align="center"><div class="input-group"><span class="glyphicon glyphicon-remove-circle emptype-action" aria-hidden="true" onclick="deleteItemSupplier('multiitemsup');return false;"></span><INPUT class="form-control vendorSearch" name="ITEMSUPPLIER_0" id="ITEMSUPPLIER_0"  type = "TEXT" value="" size="55" placeholder="Select Supplier" MAXLENGTH=50
		onkeypress="if((event.keyCode=='13') && ( document.form.ITEMSUPPLIER_0.value.length > 0)){validateItemSupplier(0);}">
       	</div>&nbsp;
        </TD>
       	</TR>
		</TABLE>				
      </div>
    </div>

	   	<div class="row form-group">
		<div class="row">
		<div class="col-sm-2"></div>
			<div class="col-sm-6">
				<a class="add-line"
					style="text-decoration: none; cursor: pointer;"
					onclick="addItemSupplier('multiitemsup');return false;">+ Add another Supplier</a>
			</div>
		</div>
	</div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">Supplier Discount:</label>
	        	<div class="col-sm-4">
	        		<label class="radio-inline">
			      	<input name="IBDISCOUNT" type = "radio"  value="BYCOST"  id="BYCOST" checked="checked">By Cost
			    	</label>
			    	<label class="radio-inline">
			      	<input  name="IBDISCOUNT" type = "radio" name="IBDISCOUNT" type = "radio" value="BYPERCENTAGE"  id = "BYPERCENTAGE">By Percentage
			    	</label>
	        	</div>	        	
        	</div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2"></label>
	        	<div class="col-sm-4">
			<TABLE id="supplierDiscount">
		<TR>
		<TD><INPUT class="form-control" name="DYNAMIC_SUPPLIER_DISCOUNT" id="DYNAMIC_SUPPLIER_DISCOUNT_0" type="TEXT" value=""
		size="20" MAXLENGTH="50"  onkeypress="return isNumberKey(event,this,4)" />&nbsp;</TD>
		<TD align="center"><div class="input-group"><span class="glyphicon glyphicon-remove-circle emptype-action" aria-hidden="true" onclick="deleteRowCost('supplierDiscount');return false;"></span><INPUT class="form-control supplierSearch" name="SUPPLIER" id="SUPPLIER_0"  type = "TEXT" value="" size="20" placeholder="Select Supplier" MAXLENGTH=50
		onkeypress="if((event.keyCode=='13') && ( document.form.SUPPLIER.value.length > 0)){validateSupplier(0);}">
		<INPUT type="hidden" name="DYNAMIC_SUPPLIER_DISCOUNT_SIZE">      
       	</div>&nbsp;
        </TD>
       	</TR>
		</TABLE>				
      </div>     	
        	</div>
        	
        	<div class="row form-group">
					<div class="row">
					<div class="col-sm-2"></div>
						<div class="col-sm-6">
							<a class="add-line"
								style="text-decoration: none; cursor: pointer;"
								onclick="addRowCost('supplierDiscount');return false;">+ Add another discount</a>
						</div>
					</div>
				</div>
        
        </div>
        <div class="tab-pane fade" id="sales">
        <br>
        
        <div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2 required">UOM:</label>
	        	<div class="col-sm-4">
	        		<div class="input-group">
		        		<INPUT class="form-control" name="SALESUOM" type="TEXT" value="" size="20" MAXLENGTH=100 id="salesuom" onchange="checksaluom(this.value)" placeholder="Select UOM">
				        <span class="select-icon" style="right: 15px;" onclick="$(this).parent().find('input[name=\'SALESUOM\']').focus()">
				        <i class="glyphicon glyphicon-menu-down" id="salesuombtnpop"></i></span>
			   		</div>
	        	</div>
	        		        	
        	</div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">List Price:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="PRICE" id="PRICE" type="text" value="" onkeypress="return isNumberKey(event,this,4)"> 
	        	</div>
	        	
        	</div>
        	
        	
    <div class="row form-group">
      <!-- <label class="col-form-modal-label col-sm-2" for="List Price">Is Combination Product</label>
      <div class="form-inline">
	     <div class="col-sm-2">
	     	<label class="checkbox-inline" style="margin-bottom: 16px;"><INPUT Type=Checkbox  name ="ISCOMPRO" style="border:0;" value="0" id="ISCOMPRO" onclick="iscombpro();"></label>
         </div>
	   </div> -->
      
      <!-- <div class="col-sm-4">
      		<INPUT Type=Checkbox  name ="ISCOMPRO" value="0" id="ISCOMPRO" onclick="iscombpro();">
      </div> -->
    </div>
    
         <div class="row form-group">
     <label class="col-form-modal-label col-sm-2 required">Product Type</label>
      <div class="col-sm-6">
      	<label class="radio-inline"><INPUT Type=radio  name ="ISCOMPRO" style="border:0;" value="NONE" id="NONE" checked="checked"  onclick="iscombpro();">None</label>
    	<label class="radio-inline"><INPUT Type=radio  name ="ISCOMPRO" style="border:0;" value="ISCOMPRO" id="ISCOMPRO" onclick="iscombprofin();">Is Finished Product</label>				
    	<label class="radio-inline"><INPUT Type=radio  name ="ISCOMPRO" style="border:0;" value="ISCOMPRO_SEMI" id="ISCOMPRO_SEMI" onclick="iscombprosem();">Is Semi Finished Product</label>		
      </div>
     </div>
    
     <div class="row form-group comproprice">
      <label class="col-form-modal-label col-sm-2 required">Product Price Increase</label>
      <div class="col-sm-6">
			<label class="radio-inline">
      	<INPUT type = "radio"  name="CPPI" type = "radio"  value="BYPRICE"  id="CPPIBYPRICE" checked="checked">By Price
    	</label>
    	<label class="radio-inline">
      	<INPUT type = "radio" name="CPPI" type = "radio" value="BYPERCENTAGE"  id = "CPPIBYPERCENTAGE" >By Percentage
    	</label>
<!--     	<label class="radio-inline"><INPUT Type=Checkbox  name ="ISCOMPRO" style="border:0;" value="0" id="ISCOMPRO" onclick="iscombpro();">Is Finished Product</label>				 -->
      </div>
    </div>
     <div class="row form-group comproprice">
      <label class="control-label col-form-label col-sm-2" for="Customer Discount">Product Price Increased Value</label>
      <div class="col-sm-3">
      		<INPUT  class="form-control"  Type="text"  name ="INCPRICE" id="INCPRICE" style="width: 115%;" onchange="isNumChecks();" onkeypress="return isNumberKey(event,this,4)">
      </div>
      <div class="col-sm-1">
      		<INPUT  class="form-control"  Type="text"  name ="INCPRICEUNIT" id="INCPRICEUNIT" value="" readonly>
      </div>
    </div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">Minimum Selling Price:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="MINSELLINGPRICE" id="MINSELLINGPRICE" type="text" value="" onkeypress="return isNumberKey(event,this,4)"> 
	        	</div>
	        	</div>
	        	<div class="row form-group">
	        	
	        	<label class="col-form-modal-label col-sm-2">Customer Discount:</label>
	        	<div class="col-sm-4">
	        		<label class="radio-inline">
			     	<input name="OBDISCOUNT" type = "radio"  name="OBDISCOUNT" type = "radio"  value="BYPRICE"  id="BYPRICE" checked="checked">By Price
			    	</label>
			    	<label class="radio-inline">
			      	<input  name="OBDISCOUNT" type = "radio" name="OBDISCOUNT" type = "radio" value="BYPERCENTAGE"  id = "BYPERCENTAGE" >By Percentage
			    	</label>
	        	</div>
        	</div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2"></label>
	        	<div class="col-sm-4">
			<TABLE id="customerDiscount">
		<TR>
		<TD><INPUT class="form-control" name="DYNAMIC_CUSTOMER_DISCOUNT" id="DYNAMIC_CUSTOMER_DISCOUNT_0" type="TEXT" value="" onkeypress="return isNumberKey(event,this,4)"
		size="20" MAXLENGTH="50"  />&nbsp;</TD>
		<TD align="center"><div class="input-group"><span class="glyphicon glyphicon-remove-circle emptype-action" aria-hidden="true" onclick="deleteRow('customerDiscount');return false;"></span><INPUT class="form-control customerSearch" name="CUSTOMER_TYPE_ID" id="CUSTOMER_TYPE_ID_0" placeholder="Select Customer Type" type = "TEXT" value="" size="20"  MAXLENGTH=50
		onkeypress="if((event.keyCode=='13') && ( document.form.CUSTOMER_TYPE_ID.value.length > 0)){validateCustomerType(0);}">
		<INPUT type="hidden" name="DYNAMIC_CUSTOMER_DISCOUNT_SIZE">        
       	</div>&nbsp;
        </TD>
       	</TR>
		</TABLE>				
      </div>
	        	
        	</div>
        	
        	<div class="row form-group">
					<div class="row">
					<div class="col-sm-2"></div>
						<div class="col-sm-6">
							<a class="add-line"
								style="text-decoration: none; cursor: pointer;"
								onclick="addRowSales('customerDiscount');return false;">+ Add another discount</a>
						</div>
					</div>
				</div>
        
        </div>
        <div class="tab-pane fade" id="inventoryps">
        <br>
        
        <div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2 required">UOM:</label>
	        	<div class="col-sm-4">
	        		<div class="input-group"> 
		        		<INPUT class="form-control" name="INVENTORYUOM" type="TEXT" value="" size="20" MAXLENGTH=100 id="inventoryuom" onchange="checkinvuom(this.value)" placeholder="Select UOM">
				        <span class="select-icon" style="right: 15px;" onclick="$(this).parent().find('input[name=\'INVENTORYUOM\']').focus()">
				        <i class="glyphicon glyphicon-menu-down" id="inventoryeuombtnpop"></i></span>
			   		</div>
	        	</div>
        	</div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">Min Stock Quantity:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="STKQTY" id="STKQTY" type="text" value="" MAXLENGTH=50 onkeypress="return isNumberKey(event,this,4)"> 
	        	</div>
        	</div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">Max Stock Quantity:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="MAXSTKQTY" id="MAXSTKQTY" type="text" value="" MAXLENGTH=50 onkeypress="return isNumberKey(event,this,4)"> 
	        	</div>
        	</div>
        
        </div>
        
        <div class="tab-pane fade" id="additionaldetail">
        <br>
     
    
    <div class="form-group">
        <TABLE id="descriptiontbl" width="100%"  style="border-spacing: 0px 8px;">
		<TR>
		<TD>
		<label for="Description 1">Detail Description 1</label></TD>		
		<TD align="center"  style="width: 85%;"><div class="col-sm-10"><div class="input-group">
		<span class="glyphicon glyphicon-remove-circle emptype-action" style="left: 500px;" aria-hidden="true" onclick="deleteRows('descriptiontbl');return false;"></span>
		<INPUT class="form-control" name="DESCRIPTION" id="DESCRIPTION0"  placeholder="Max 1000 Characters" type = "TEXT" value="" size="100"  MAXLENGTH=1000>		        
       	</div>
       	</div>&nbsp;
        </TD>
       	</TR>
		</TABLE>
    		<INPUT type="hidden" name="DESCRIPTION_SIZE" >  		
       
    </div>

    <div class="form-group">
					<div class="row">
					<div class="col-sm-1"></div>
						<div class="col-sm-4">
							<a class="add-line"
								style="text-decoration: none; cursor: pointer;"
								onclick="addRowDesc('descriptiontbl','');return false;">+ Add another Description</a>
						</div>
					</div>
				</div>
        </div>
        
        <div class="tab-pane fade" id="catalogues">
        <br>
         <div class="row form-group">
      	<label class="col-form-modal-label col-sm-2 " for="Upload Image">Upload Product Image 1</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control" name="IMAGE_UPLOAD" type="File" size="20" MAXLENGTH=100>
      		</div>
    	</div>
    	
         <div class="row form-group">
      	<label class="col-form-modal-label col-sm-2 " for="Upload Image">Upload Product Image 2</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control" name="IMAGE_UPLOAD2" type="File" size="20" MAXLENGTH=100>
      		</div>
    	</div>
    	
         <div class="row form-group">
      	<label class="col-form-modal-label col-sm-2 " for="Upload Image">Upload Product Image 3</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control" name="IMAGE_UPLOAD3" type="File" size="20" MAXLENGTH=100>
      		</div>
    	</div>
    	
         <div class="row form-group">
      	<label class="col-form-modal-label col-sm-2 " for="Upload Image">Upload Product Image 4</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control" name="IMAGE_UPLOAD4" type="File" size="20" MAXLENGTH=100>
      		</div>
    	</div>
    	
         <div class="row form-group">
      	<label class="col-form-modal-label col-sm-2 " for="Upload Image">Upload Product Image 5</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control" name="IMAGE_UPLOAD5" type="File" size="20" MAXLENGTH=100>
      		</div>
    	</div>
    	
         <div class="row form-group">
      	<label class="col-form-modal-label col-sm-2 " for="Upload Image">Upload Product Image 6</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control" name="IMAGE_UPLOAD6" type="File" size="20" MAXLENGTH=100>
      		</div>
    	</div>
    	
        </div>
        
                
        <div class="tab-pane fade" id="prds">
        <br>
     
    
    <div class="form-group">
        <TABLE id="prdtbl" width="100%"  style="border-spacing: 0px 8px;">
		<TR>
		<TD>
		<label for="Product 1">Product 1</label></TD>		
		<TD align="center"  style="width: 93%;"><div class="col-sm-6"><div class="input-group">
		<span class="glyphicon glyphicon-remove-circle emptype-action" style="left: 350px;" aria-hidden="true" onclick="deleteRowPrd('prdtbl');return false;"></span>
		<INPUT class="form-control additemSearch" name="PRODUCT" id="PRODUCT0"  placeholder="Select Product" type = "TEXT" value="" size="100"  MAXLENGTH=200>		        
       	</div>
       	</div>&nbsp;
        </TD>
       	</TR>
		</TABLE>
    		<INPUT type="hidden" name="PRD_SIZE" >  		
       
    </div>

    <div class="form-group">
					<div class="row">
					<div class="col-sm-1"></div>
						<div class="col-sm-4">
							<a class="add-line"
								style="text-decoration: none; cursor: pointer;"
								onclick="addRowPrd('prdtbl','');return false;">+ Add another Product</a>
						</div>
					</div>
				</div>
        </div>
        
        <div class="tab-pane fade" id="remarks">
        <br>
        
        <div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">Remarks1:</label>
	        	<div class="col-sm-4">
	        		<textarea class="form-control" name="ITEM_CONDITION" id="ITEM_CONDITION" placeholder="Max 100 Characters" MAXLENGTH=100> </textarea>
	        	</div>
	        	</div>
	    <div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">Remarks2:</label>
	        	<div class="col-sm-4">
	        		<textarea class="form-control" name="TITLE" id="TITLE" placeholder="Max 100 Characters" MAXLENGTH=100> </textarea>
	        	</div>
        	</div>
        
        </div>
        <div class="tab-pane fade" id="rental" type="hidden">
        <br>
        
        </div>
        </div>
        
        </div>
        	
        	
        	<div class="panel panel-default" style="display:none">
    <div class="panel-heading" style="background: #eaeafa; height: 36px"><strong>Rental</strong></div>
    <div class="panel-body">
        	<div class="row">
        	<div class="col-sm-6">
        	
		    
		    <div class="row form-group">
	        	<label class="col-form-modal-label col-sm-4 required">Rental UOM:</label>
	        	<div class="col-sm-8">
	        		<div class="input-group"> 
		        		<input class="form-control" name="RENTALUOM" id="rentaluom" type="text" value="" readonly>
		        		<span class="input-group-addon"  onClick="UOMpopUpwin('PRDMODALRENTALUOM');">
				   		<a href="#" data-toggle="tooltip" data-placement="top" title="Rental UOM Details">
				   			<i class="glyphicon glyphicon-log-in" style="font-size: 15px;"></i>
				   		</a>
				   		</span>
			   		</div>
	        	</div>
        	</div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-4">Rental Price:</label>
	        	<div class="col-sm-8">
	        		<input class="form-control" name="RENTALPRICE" id="RENTALPRICE" type="text" value=""> 
	        	</div>
        	</div>
        	</div>
        	</div>
        	
        	
        	</div>
        	</div>
        	
		     
	    </div>
	    <div class="modal-footer">
	    		<button id="btnBillOpen" type="button" class="btn btn-success" onClick="productValidNumber();">Save</button>
		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	    </div>
	  </div>
	  <INPUT class="form-control" name="SERVICEUOM" type="hidden" value="" size="20" MAXLENGTH=100 readonly id="serviceuom">
	  <INPUT class="form-control" name="DISCOUNT" id="DISCOUNT" type="hidden" value="" size="20" MAXLENGTH=50>
	  <INPUT class="form-control" name="SERVICEPRICE" id="SERVICEPRICE" type="hidden" value="" size="20" MAXLENGTH=50 onchange="validDecimal(this.value)">
		<INPUT type="hidden" name="DYNAMIC_CUSTOMERDISCOUNT_SIZE">
		<INPUT type="hidden" name="DYNAMIC_SUPPLIERDISCOUNT_SIZE">
	  </form>
	</div>
</div>

<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   

    if (document.getElementById("NONE").checked == true) {
		document.getElementById("NONE").value = "NONE";
		/* $(".comproprice").hide(); */
	}
    //imti added 
    var COMP_INDUSTRY = $("input[name=COMP_INDUSTRY]").val();
    if(COMP_INDUSTRY=="Retail"){
    	document.getElementById('divdesc').style.display = 'none';
    	document.getElementById('divimg').style.display = 'none';
    }else{
    	document.getElementById('descid').style.display = 'none';
    	document.getElementById('catid').style.display = 'none';
    	document.getElementById('prdid').style.display = 'none';
    	document.getElementById('ISarrival').style.display = 'none';
    	document.getElementById('ISselling').style.display = 'none';
    }
    if(COMP_INDUSTRY=="Centralised Kitchen")
    	document.getElementById('divlocid').style.display = 'none';
    //imti end
    
	var  d = document.getElementById("TaxByLabelOrderManagement").value;
    document.getElementById('TaxLabelOrderManagement').innerHTML = "Product " + d +" (%):";
    
    var declength =	parseInt(document.getElementById("numberOfDecimal").value);
    var decVal = parseFloat(0).toFixed(declength);
    
    $("#NETWEIGHT").val("0.000");
    $("#GROSSWEIGHT").val("0.000");
    $("#PRODGST").val("0.000");
    $("#COST").val(decVal);
    $("#PRICE").val(decVal);
    $("#MINSELLINGPRICE").val(decVal);
    $("#RENTALPRICE").val(decVal);
    $("#INCPRICEUNIT").val($("input[name ='CURRENCYID']").val());
    $("input[name ='INCPRICE']").val(decVal);
    
    
    var plant = document.form.plant.value;
	/* Product Number Auto Suggestion */
	$('#item').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'ITEM',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCT_LIST_FOR_SUGGESTION",
				ITEM : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.items);
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
			return '<div><p class="item-suggestion">'+data.ITEM+'</p></div>';
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
		});
	
	/* Basic UOM Auto Suggestion */
	$('#Basicuom').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'UOM',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_UOM_DATA",
				UOM : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.UOMMST);
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
			return '<div><p class="item-suggestion">'+data.UOM+'</p><br/><p class="item-suggestion">DESC: '+data.UOMDESC+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="uomAddBtn footer"  data-toggle="modal" data-target="#prduomModal"  onclick="document.productForm.uomModal.value=\'basicuom\'"> <a href="#"> + Add UOM</a></div>');
		//	menuElement.after( "<div class=\"uomAddBtn footer\" ><a href=\"#\"  onClick=\"UOMpopUpwin('PRDMODALUOM');return false;\"> + Add UOM</a></div>");
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();   
		  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		});
	
	/* Product Class Auto Suggestion */
	$('#PRD_CLS_DESC').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_CLS_DESC',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRD_CLS_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_CLSMST);
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
				return '<div onclick="document.productForm.PRD_CLS_ID.value = \''+data.PRD_CLS_ID+'\'"><p class="item-suggestion">' + data.PRD_CLS_ID + '</p><br/><p class="item-suggestion">DESC: '+data.PRD_CLS_DESC+'</p></div>';
//				return '<div><p class="item-suggestion">'+data.PRD_CLS_ID+'</p><br/><p class="item-suggestion">DESC: '+data.PRD_CLS_DESC+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="PRD_CLS_IDAddBtn footer"  data-toggle="modal" data-target="#prdcatModal"> <a href="#"> + Add Product Category</a></div>');
	//		menuElement.after( "<div class=\"PRD_CLS_IDAddBtn footer\" ><a href=\"#\"  onClick=\"CLASSpopUpwin('PRDMODALUOM');return false;\"> + Add Product Category</a></div>");
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == "")
				document.productForm.PRD_CLS_ID.value = "";
		});

	/* Product Sub Category Auto Suggestion */
	$('#PRD_TYPE_DESC').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_TYPE_DESC',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRD_TYPE_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_TYPEMST);
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
				return '<div onclick="document.productForm.ARTIST.value = \''+data.PRD_TYPE_ID+'\'"><p class="item-suggestion">' + data.PRD_TYPE_ID + '</p><br/><p class="item-suggestion">DESC: '+data.PRD_TYPE_DESC+'</p></div>';
//			return '<div><p class="item-suggestion">'+data.PRD_TYPE_ID+'</p><br/><p class="item-suggestion">DESC: '+data.PRD_TYPE_DESC+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="ARTISTAddBtn footer"  data-toggle="modal" data-target="#prdsubcatModal"> <a href="#"> + Add Product Sub Category</a></div>');
//			menuElement.after( "<div class=\"ARTISTAddBtn footer\" ><a href=\"#\"   onClick=\"TYPEpopUpwin('PRDMODALUOM');return false;\">+ Add Product Sub Category</a></div>");
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == "")
				document.productForm.ARTIST.value = "";
		});
	/* Product Brand Auto Suggestion */
	$('#PRD_BRAND_DESC').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_BRAND_DESC',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRD_BRAND_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_BRANDMST);
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
				return '<div onclick="document.productForm.PRD_BRAND.value = \''+data.PRD_BRAND_ID+'\'"><p class="item-suggestion">' + data.PRD_BRAND_ID + '</p><br/><p class="item-suggestion">DESC: '+data.PRD_BRAND_DESC+'</p></div>';
//			return '<div><p class="item-suggestion">'+data.PRD_BRAND_ID+'</p><br/><p class="item-suggestion">DESC: '+data.PRD_BRAND_DESC+'</p></div>';
			}    
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="PRD_BRANDAddBtn footer"  data-toggle="modal" data-target="#prdbrandModal"> <a href="#"> + Add Product Brand</a></div>');
//			menuElement.after( "<div class=\"PRD_BRANDAddBtn footer\" ><a href=\"#\"  onClick=\"BRANDpopUpwin('PRDMODALUOM');return false;\"> + Add Product Brand</a></div>");
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block") 
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == "")
				document.productForm.PRD_BRAND.value = "";
		});
	
	/* HSCODE Auto Suggestion */
	$('#HSCODE').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'HSCODE',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_HSCODE_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.HSCODEMST);
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
			return '<div><p class="item-suggestion">'+data.HSCODE+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="HSCODEAddBtn footer"  data-toggle="modal" data-target="#prdhscodeModal"> <a href="#"> + Add HS Code</a></div>');
//			menuElement.after( "<div class=\"HSCODEAddBtn footer\" ><a href=\"#\"  onClick=\"HSCODEpopUpwin('PRDMODALUOM');return false;\"> + Add HS Code</a></div>");
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		});
	
	/* COO Auto Suggestion */
	$('#COO').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'COO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_COO_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.COOMST);
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
			return '<div><p class="item-suggestion">'+data.COO+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="cooAddBtn footer"  data-toggle="modal" data-target="#prdcooModal"> <a href="#"> + Add COO</a></div>');
//			menuElement.after( "<div class=\"cooAddBtn footer\" ><a href=\"#\"   onClick=\"COOpopUpwin('PRDMODALUOM');return false;\"> + Add COO Code</a></div>");
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		});
	
	/* Purchase UOM Auto Suggestion */
	$('#purchaseuom').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'UOM',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_UOM_DATA",
				UOM : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.UOMMST);
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
			return '<div><p class="item-suggestion">'+data.UOM+'</p><br/><p class="item-suggestion">DESC: '+data.UOMDESC+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="uomAddBtn footer"  data-toggle="modal" data-target="#prduomModal"  onclick="document.productForm.uomModal.value=\'purchaseuom\'"> <a href="#"> + Add UOM</a></div>');
//			menuElement.after( "<div class=\"uomAddBtn footer\" ><a href=\"#\"  onClick=\"UOMpopUpwin('PRDMODALPURCHASEUOM');return false;\"> + Add UOM</a></div>");
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		});

	/* Sales UOM Auto Suggestion */
	$('#salesuom').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'UOM',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_UOM_DATA",
				UOM : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.UOMMST);
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
			return '<div><p class="item-suggestion">'+data.UOM+'</p><br/><p class="item-suggestion">DESC: '+data.UOMDESC+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="uomAddBtn footer"  data-toggle="modal" data-target="#prduomModal"  onclick="document.productForm.uomModal.value=\'salesuom\'"> <a href="#"> + Add UOM</a></div>');
//			menuElement.after( "<div class=\"uomAddBtn footer\" ><a href=\"#\"  onClick=\"UOMpopUpwin('PRDMODALSALESUOM');return false;\"> + Add UOM</a></div>");
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		});

	/* Rental UOM Auto Suggestion */
	$('#rentaluom').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'UOM',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_UOM_DATA",
				UOM : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.UOMMST);
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
			return '<div><p class="item-suggestion">'+data.UOM+'</p><br/><p class="item-suggestion">DESC: '+data.UOMDESC+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="uomAddBtn footer"  data-toggle="modal" data-target="#prduomModal"  onclick="document.productForm.uomModal.value=\'rentuom\'"> <a href="#"> + Add UOM</a></div>');
//			menuElement.after( "<div class=\"uomAddBtn footer\" ><a href=\"#\"  onClick=\"UOMpopUpwin('PRDMODALRENTALUOM');return false;\"> + Add UOM</a></div>");
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		});
	
	
	/* Inventory UOM Auto Suggestion */
	$('#inventoryuom').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'UOM',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_UOM_DATA",
				UOM : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.UOMMST);
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
			return '<div><p class="item-suggestion">'+data.UOM+'</p><br/><p class="item-suggestion">DESC: '+data.UOMDESC+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="uomAddBtn footer"  data-toggle="modal" data-target="#prduomModal"  onclick="document.productForm.uomModal.value=\'inventuom\'"> <a href="#"> + Add UOM</a></div>');
//			menuElement.after( "<div class=\"uomAddBtn footer\" ><a href=\"#\"  onClick=\"UOMpopUpwin('PRDMODALINVENTORYUOM');return false;\"> + Add UOM</a></div>");
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		});

	/* location Auto Suggestion */
	$('#LOC_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOC_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.LOC_MST);
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
			return '<div><p class="item-suggestion">'+data.LOC+'</p></div>';
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
		});

	/* Product Department Auto Suggestion */
	$('#PRD_DEPT_DESC').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_DEPT_DESC',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRD_DEPT_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_DEPTMST);
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
				return '<div onclick="document.productForm.PRD_DEPT_ID.value = \''+data.PRD_DEPT_ID+'\'"><p class="item-suggestion">' + data.PRD_DEPT_ID + '</p><br/><p class="item-suggestion">DESC: '+data.PRD_DEPT_DESC+'</p></div>';
//			return '<div><p class="item-suggestion">'+data.PRD_DEPT_ID+'</p><br/><p class="item-suggestion">DESC: '+data.PRD_DEPT_DESC+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="PRD_DEPT_IDAddBtn footer"  data-toggle="modal" data-target="#prddepModal"> <a href="#"> + Add Product Department</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			$('.PRD_DEPT_IDAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.PRD_DEPT_IDAddBtn').hide();}, 180);
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == "")
				document.productForm.PRD_DEPT_ID.value = "";
		});
	
	
	/* Supplier Auto Suggestion */
	$('#vendname_itemmst').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'VNAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_SUPPLIER_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.VENDMST);
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
			return '<div onclick="document.productForm.vendno.value = \''+data.VENDO+'\'"><p class="item-suggestion">Name: ' + data.VNAME + '</p><br/><p class="item-suggestion">Code: ' + data.VENDO + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Supplier Type: ' + data.SUPPLIERTYPE + '</p></div>';
		    }
		  }
		}).on('typeahead:render',function(event,selection){
			
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			//menuElement.after( '<div class="supplieritmAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + Add Supplier</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		}).on('typeahead:open',function(event,selection){
			$('.supplieritmAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.supplieritmAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.productForm.vendno.value = "";
			}
		});
	
	removeSuggestionSearch();
	addSuggestionSearch();
	addSuggestionprd();
});


function addRowDesc(tableID,footer) {
	
  	
	var table = document.getElementById(tableID);
	
	var rowCount = table.rows.length;
	if(rowCount!=10)
		{
	var row = table.insertRow(rowCount);
	var newRowCount = rowCount + 1; 
	
	var itemCell = row.insertCell(0);
	var itemCellText =  "<label for=\"Detail Description"+rowCount+"\">Detail Description "+newRowCount+"\</label>&nbsp; ";
	itemCell.innerHTML = itemCellText;
	
	
	var itemCell = row.insertCell(1);
	var itemCellText =  "<div class=\"col-sm-10\"><div class=\"input-group\"><span class=\"glyphicon glyphicon-remove-circle emptype-action\" style=\"left: 559px;\" aria-hidden=\"true\" onclick=\"deleteRowDesc('descriptiontbl');return false;\"></span><INPUT class=\"form-control\" name=\"DESCRIPTION\" ";
	itemCellText = itemCellText+ " id=\"DESCRIPTION"+newRowCount+"\" type = \"TEXT\" size=\"100\" value=\""+footer+"\" placeholder=\"Max 1000 Characters\" MAXLENGTH=\"1000\"></div></div>&nbsp;";
	itemCell.innerHTML = itemCellText;
		}
	else
		{
		alert("Can not add more then 10 detail description ");
		}
}
function deleteRowDesc(tableID) {
	try {
	var table = document.getElementById(tableID);	
	var rowCount = table.rows.length;
	rowCount = rowCount*1 - 1;
	if(rowCount==0){
		alert("Can not remove the default Item");
	}else{
		table.deleteRow(rowCount);
	}
	}catch(e) {
		alert(e);
	}
}

function addRowPrd(tableID,footer) {
	
  	
	var table = document.getElementById(tableID);
	
	var rowCount = table.rows.length;
	if(rowCount!=10)
		{
	var row = table.insertRow(rowCount);
	var newRowCount = rowCount + 1; 
	
	var itemCell = row.insertCell(0);
	var itemCellText =  "<label for=\"Product"+rowCount+"\">Product "+newRowCount+"\</label>&nbsp; ";
	itemCell.innerHTML = itemCellText;
	
	
	var itemCell = row.insertCell(1);
	var itemCellText =  "<div class=\"col-sm-6\"><div class=\"input-group\"><span class=\"glyphicon glyphicon-remove-circle emptype-action\" style=\"left: 370px;\" aria-hidden=\"true\" onclick=\"deleteRowPrd('prdtbl');return false;\"></span><INPUT class=\"form-control additemSearch\" name=\"PRODUCT\" ";
	itemCellText = itemCellText+ " id=\"PRODUCT"+newRowCount+"\" type = \"TEXT\" size=\"100\" value=\""+footer+"\" placeholder=\"Select Product\" MAXLENGTH=\"200\"></div></div>&nbsp;";
	itemCell.innerHTML = itemCellText;
		}
	else
		{
		alert("Can not add more then 10 Product ");
		}
	$(".additemSearch").typeahead('destroy');
	addSuggestionprd();
}

function deleteRowPrd(tableID) {
	try {
	var table = document.getElementById(tableID);	
	var rowCount = table.rows.length;
	rowCount = rowCount*1 - 1;
	if(rowCount==0){
		alert("Can not remove the default Item");
	}else{
		table.deleteRow(rowCount);
	}
	}catch(e) {
		alert(e);
	}
}

function addSuggestionprd()
{
	var plant = document.form.plant.value;
	$(".additemSearch").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true,
		  classNames: {
			 	menu: 'bigdrop'
			  }
		},
		{
		  display: 'ITEM',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "../ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				ACTION : "GET_PRODUCT_LIST_SUGGESTION",
				ITEM : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.items);
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
				return '<div><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Brand:'+data.PRD_BRAND_ID+'</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right"> Category:'+data.PRD_CLS_ID+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".bigdrop");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".bigdrop").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			var menuElement = $(this).parent().find(".bigdrop");
			menuElement.next().show();
		}).on('typeahead:select',function(event,selection){
			$(this).closest('tr').find("input[name ='ITEMDES']").val(selection.ITEMDESC);
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".bigdrop");
			setTimeout(function(){ menuElement.next().hide();}, 150);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
// 					$(this).closest('tr').find('input[name="ITEMDES"]').val("");
			}
		});

}

function addSuggestionSearch()
{
	var plant = document.form.plant.value;
	/* To get the suggestion data for Product */
	
	/* Item Supplier Auto Suggestion */
	$('.vendorSearch').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'VENDO',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_SUPPLIER_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.VENDMST);
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
			return '<div onclick="document.form.vendno.value = \''+data.VENDO+'\'"><p class="item-suggestion">Name: ' + data.VNAME + '</p><br/><p class="item-suggestion">Code: ' + data.VENDO + '</p><p class="item-suggestion pull-right">Currency: ' + data.CURRENCY + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Supplier Type: ' + data.SUPPLIERTYPE + '</p></div>';
		    }
		  }
		}).on('typeahead:render',function(event,selection){
			
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
//			menuElement.after( '<div class="itemsupplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#" onclick="document.form.custModal.value=\'cust\'"> + New Supplier</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		}).on('typeahead:open',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 150);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
			}
		});
		
	$(".supplierSearch").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'VENDO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_SUPPLIER_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.VENDMST);
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
				return '<div><p class="item-suggestion">'+data.VENDO+'</p><br/><p class="item-suggestion">DESC: '+data.VNAME+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			//menuElement.after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + Add Supplier</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		});
	
	/* Customer Auto Suggestion */
	$('.customerSearch').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'CUSTOMER_TYPE_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_CUSTOMERTYPE_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.CUSTYPEMST);
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
				return '<div><p class="item-suggestion">'+data.CUSTOMER_TYPE_ID+'</p><br/><p class="item-suggestion">DESC: '+data.CUSTOMER_TYPE_DESC+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		});
}
function removeSuggestionSearch()
{
	$(".vendorSearch").typeahead('destroy');
	$(".supplierSearch").typeahead('destroy');
	$(".customerSearch").typeahead('destroy');
	$(".additemSearch").typeahead('destroy');
}

function popUpWin(URL) {
 	subWin = window.open(URL, 'Items', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
 	
}
function isNumberKey(evt, element, id) {
	  var charCode = (evt.which) ? evt.which : event.keyCode;
	  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
		  {
	    	return false;
		  }
	  return true;
	}

function productValidNumber() {
	
	var urlStr = "/track/ItemMstServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			ACTION : "NOOFPRODUCT_CHECK"
		},
			dataType : "json",
			success : function(data) {
				if (data.status == "100") {
					alert("You have reached the limit of "+data.ValidNumber+" products you can create");
					return false;
				}
				else
					onAddproduct();
			}
	});
    		
}
function productdepartmentCallback(data){
	if(data.STATUS="SUCCESS"){
		$("input[name=PRD_DEPT_DESC]").typeahead ('val',data.PRD_DEPT_DESC);
		$("input[name ='PRD_DEPT_ID']").val(data.PRD_DEPT_ID);
//		$("input[name=PRODUCTDEPARTMENTDESC]").typeahead ('val',data.PRD_DEPT_DESC);
	}
}
function productcategoryCallback(data){
	if(data.STATUS="SUCCESS"){
		$("input[name=PRD_CLS_DESC]").typeahead ('val',data.PRD_CLS_DESC);
		$("input[name ='PRD_CLS_ID']").val(data.PRD_CLS_ID);
	}
}
function productsubcategoryCallback(data){
	if(data.STATUS="SUCCESS"){
		$("input[name=PRD_TYPE_DESC]").typeahead ('val',data.PRD_TYPE_DESC);
		$("input[name ='ARTIST']").val(data.PRD_TYPE_ID);
	}
}

function productbrandCallback(data){
	if(data.STATUS="SUCCESS"){
		$("input[name=PRD_BRAND_DESC]").typeahead ('val',data.PRD_BRAND_DESC);
		$("input[name ='PRD_BRAND']").val(data.PRD_BRAND_ID);
	}
}
function producthscodeCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#HSCODE").typeahead('val', data.HSCODE);
			}
}
function productcooCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#COO").typeahead('val', data.COO);
			}
}
function productuomCallback(data){
	if(data.STATUS="SUCCESS"){
	if(document.productForm.uomModal.value=="basicuom"){
		$("#Basicuom").typeahead('val', data.UOMCODE);
		}else if(document.productForm.uomModal.value=="purchaseuom"){
		$("input[name ='PURCHASEUOM']").typeahead('val', data.UOMCODE);
		}else if (document.productForm.uomModal.value=="salesuom"){
		$("input[name ='SALESUOM']").typeahead('val', data.UOMCODE);
		}else if(document.productForm.uomModal.value=="inventuom"){
		$("input[name ='INVENTORYUOM']").typeahead('val', data.UOMCODE);
		}
		else{
		$("#rentaluom").typeahead('val', data.UOMCODE);
		}
	}
	}


function onAddproduct(){
	 var ITEM   = document.productForm.ITEM.value;
	 var DESC = document.productForm.DESC.value;
	 var uom = document.productForm.UOM.value;
	 var purchaseuom = document.productForm.PURCHASEUOM.value;
	 var salesuom = document.productForm.SALESUOM.value;
	 var rentaluom = document.productForm.RENTALUOM.value;
	 var serviceuom = document.productForm.SERVICEUOM.value;
	 var inventoryuom = document.productForm.INVENTORYUOM.value;
	  var netweight   = document.productForm.NETWEIGHT.value;
	 var grossweight = document.productForm.GROSSWEIGHT.value;
	 var hscode   = document.productForm.HSCODE.value;
	 var coo = document.productForm.COO.value; 
	 var price = parseFloat(document.getElementById('PRICE').value);
	 var mprice = parseFloat(document.getElementById('MINSELLINGPRICE').value);
	 var discount=parseFloat(document.getElementById('DISCOUNT').value);
	 var productgst=parseFloat(document.getElementById('PRODGST').value);
	 var PRODGST=parseFloat(document.getElementById('PRODGST').value);
	 var hh = DESC.charCodeAt(0);
	 var minqty = parseFloat(document.productForm.STKQTY.value);
	 var maxqty = parseFloat(document.productForm.MAXSTKQTY.value);
	 var rentalprice = parseFloat(document.getElementById('RENTALPRICE').value);
	 var serviceprice = parseFloat(document.getElementById('SERVICEPRICE').value);
	 var declength =	parseInt(document.getElementById("numberOfDecimal").value);
	 
	 if(ITEM == "" || ITEM == null)
	{
		alert("Please key in Product");
		document.productForm.ITEM.focus(); return false;
	}
	else if(DESC == "" || DESC == null)
	 {
		alert("Please key in Description");
		document.productForm.DESC.focus(); return false;
	 }
	else if(uom=="" || uom == null)
	{
		alert("Please key in UOM");
		return false;
	}
	else if(purchaseuom=="" || purchaseuom == null)
	{
		alert("Please key in PURCHASEUOM");
		return false;
	}
	else if(salesuom=="" || salesuom == null)
	{
		alert("Please key in SALESUOM");
		return false;
	}
	/* else if(rentaluom=="" || rentaluom == null)
	{
		alert("Please key in RENTALUOM");
		return false;
	} */
	else if(inventoryuom=="" || inventoryuom == null)
	{
		alert("Please key in INVENTORYUOM");
		return false;
	}

	
	else if(!IsNumeric(document.productForm.NETWEIGHT.value))
	  	{
	  		alert("Please Enter Valid NetWeight");
	  		form.NETWEIGHT.focus();
	  		return false;
	  	}
		else if(!IsNumeric(document.productForm.GROSSWEIGHT.value))
	  	{
	  		alert("Please Enter Valid Grossweight");
	  		form.GROSSWEIGHT.focus();
	  		return false;
	  	}
		else if(!IsNumeric(document.productForm.STKQTY.value))
	  	{
	  		alert("Please Enter Valid Min Stock Quantity");
	  		form.STKQTY.focus();
	  		return false;
	  	}
		else if(!IsNumeric(document.productForm.MAXSTKQTY.value))
	  	{
	  		alert("Please Enter Valid Max Stock Quantity");
	  		form.MAXSTKQTY.focus();
	  		return false;
	  	}
	else if(document.productForm.PRICE.value == ""){
	    alert("Please enter Price");
	    document.productForm.PRICE.focus();
	    return false;
	  }
	
	else if(!IsNumeric(document.productForm.PRICE.value))
	{
		alert("Please Enter Valid Price");
		form.PRICE.focus();
		return false;
	}
	
	else if(document.productForm.COST.value == ""){
	    alert("Please enter Cost");
	    document.productForm.COST.focus();
	    return false;
	  }
	else if(!IsNumeric(document.productForm.COST.value))
	{
		alert("Please Enter Valid Cost");
		form.COST.focus();
		return false;
	}
	else if(!IsNumeric(document.productForm.MINSELLINGPRICE.value))
	{
		alert("Please Enter Valid Minimum Selling Price");
		form.MINSELLINGPRICE.focus();
		return false;
	}
	else if(price < mprice)
 	{
		alert("Price Should not be Less than Min Selling Price");
 	    document.productForm.PRICE.focus();
 	    return false;
 	}
	else if(!IsNumeric(document.productForm.PRODGST.value))
 	{
 		alert("Please Enter Valid Product VAT");
 		form.PRODGST.focus();
 		return false;
 	}		
	
	else if(document.productForm.RENTALPRICE.value == ""){
	    alert("Please enter Rental Price");
	    document.productForm.RENTALPRICE.focus();
	    return false;
	  }
	
	else if(!IsNumeric(document.productForm.RENTALPRICE.value))
	{
		alert("Please Enter Valid Rental Price");
		form.RENTALPRICE.focus();
		return false;
	}
		
	if (!validToThreeDecimal(document.getElementById("NETWEIGHT").value)) {
		alert("Not more than 3 digits are allowed after decimal value in Net Weight");
		form.NETWEIGHT.focus();
		return false;
	} else if (!validToThreeDecimal(document.getElementById("GROSSWEIGHT").value)) {
		alert("Not more than 3 digits are allowed after decimal value in Gross Weight");
		form.GROSSWEIGHT.focus();
		return false;
	} 
	else if (!validDecimal(document.getElementById("PRICE").value)) {
			alert("Not more than "+declength+" digits are allowed after decimal value in List Price");
			document.productForm.PRICE.focus();
			return false;
		} else if (!validDecimal(document.getElementById("COST").value)) {
			alert("Not more than "+declength+" digits are allowed after decimal value in Cost");
			document.productForm.COST.focus();
			return false;
		} else if (!validDecimal(document.getElementById("MINSELLINGPRICE").value)) {
			alert("Not more than "+declength+" digits are allowed after decimal value in Minimum Selling Price");
			form.MINSELLINGPRICE.focus();
			return false;
		} else if (!validToThreeDecimal(document.getElementById("PRODGST").value)) {
			alert("Not more than 3 digits are allowed after decimal value in Product VAT");
			form.PRODGST.focus();
			return false;
		}
		
		else if (!validDecimal(document.getElementById("RENTALPRICE").value)) {
			alert("Not more than "+declength+" digits are allowed after decimal value in Rental Price");
			document.productForm.RENTALPRICE.focus();
			return false;
		}

		else {
			var val = 0;
			var table = document.getElementById("customerDiscount");
			var rowCount = table.rows.length;
			var discounttype = document.productForm.OBDISCOUNT.value;
			for (var r = 0, n = table.rows.length; r < n; r++) {
				for (var c = 0, m = table.rows[r].cells.length; c < m; c++) {
					var discount = parseFloat(document
							.getElementById("DYNAMIC_CUSTOMER_DISCOUNT_" + r).value);
					var supplier = document.getElementById("CUSTOMER_TYPE_ID_" + r).value;
					if(supplier!="") {
						if(Number.isNaN(discount))
						{
						alert("Please enter customer discount value ");
						document.getElementById("DYNAMIC_CUSTOMER_DISCOUNT_" + r)
								.focus();
						return false;
						}
				} else {
					if(!Number.isNaN(discount))
					{
					alert("Select Customer Type");
					document.getElementById("CUSTOMER_TYPE_ID_" + r)
							.focus();
					return false;
					}
				}
					if (document.productForm.PRICE.value == ""
							|| document.productForm.PRICE.value == "0.00"
							|| document.productForm.PRICE.value == "0") {
						if (discount >= 1) {
							alert("Price Should not be Less than Customer Discount Sales");
							document.productForm.PRICE.focus();
							return false;
						}
					}
					if (discounttype == "BYPERCENTAGE") {
						if (discount > 100) {
							alert("Customer Discount Sales Order not more than 100%");
							document.getElementById(
									"DYNAMIC_CUSTOMER_DISCOUNT_" + (r)).focus();
							return false;
						}
					} else {
						if (discount > parseFloat(document.productForm.PRICE.value)) {
							alert("Customer Discount Sales Order not more than List Price");
							document.getElementById(
									"DYNAMIC_CUSTOMER_DISCOUNT_" + (r)).focus();
							return false;
						}
						if (discount < parseFloat(document
								.getElementById('MINSELLINGPRICE').value)) {
							alert("Customer Discount Sales Order not less than Min Selling Price");
							document.getElementById(
									"DYNAMIC_CUSTOMER_DISCOUNT_" + (r)).focus();
							return false;
						}
						var disctprice = document
								.getElementById("DYNAMIC_CUSTOMER_DISCOUNT_"
										+ r).value;
						if (disctprice.indexOf('.') == -1)
							disctprice += ".";
						var disdecNum = disctprice.substring(disctprice
								.indexOf('.') + 1, disctprice.length);

					}
					if (!IsNumeric(document
							.getElementById("DYNAMIC_CUSTOMER_DISCOUNT_" + r).value)) {
						alert("Please Enter Valid Customer Discount Sales Order");
						document.getElementById(
								"DYNAMIC_CUSTOMER_DISCOUNT_" + (r)).focus();
						return false;
					}

				}
			}

			for (var r = 0, n = table.rows.length - 1; r < n; r++) {
				for (var c = 0, m = table.rows[r].cells.length; c < m; c++) {
					if (r != rowCount) {
						// alert(c);

						if (document.getElementById("CUSTOMER_TYPE_ID_" + r).value == document
								.getElementById("CUSTOMER_TYPE_ID_" + (r + 1)).value) {
							alert("Customer Type already exists for the product");
							document.getElementById(
									"CUSTOMER_TYPE_ID_" + (r + 1)).focus();
							return false;
						}
					}
				}
			}
			// to validate customer discount sales  end  

			// to validate supplier discount sales 
			var table = document.getElementById("supplierDiscount");
			var rowCount = table.rows.length;
			var discounttype = document.productForm.IBDISCOUNT.value;
			for (var r = 0, n = table.rows.length; r < n; r++) {
				for (var c = 0, m = table.rows[r].cells.length; c < m; c++) {
					var discount = parseFloat(document
							.getElementById("DYNAMIC_SUPPLIER_DISCOUNT_" + r).value);
					var supplier = document.getElementById("SUPPLIER_" + r).value;
					if(supplier!="") {
							if(Number.isNaN(discount))
							{
							alert("Please enter supplier discount value ");
							document.getElementById("DYNAMIC_SUPPLIER_DISCOUNT_" + r)
									.focus();
							return false;
							}
					} else {
						if(!Number.isNaN(discount))
						{
						alert("Select Supplier");
						document.getElementById("SUPPLIER_" + r)
								.focus();
						return false;
						}
					}
					if (document.productForm.COST.value == ""
							|| document.productForm.COST.value == "0.00"
							|| document.productForm.COST.value == "0") {
						if (discount >= 1) {
							alert("Cost Should not be Less than Supplier Discount Purchase");
							document.productForm.COST.focus();
							return false;
						}
					}
					if (discounttype == "BYPERCENTAGE") {
						if (discount > 100) {
							alert("Supplier Discount Purchase Order not more than 100%");
							document.getElementById(
									"DYNAMIC_SUPPLIER_DISCOUNT_" + (r)).focus();
							return false;
						}
					} else {
						if (discount > parseFloat(document.productForm.COST.value)) {
							alert("Supplierr Discount Purchase Order not more than Cost");
							document.getElementById(
									"DYNAMIC_SUPPLIER_DISCOUNT_" + (r)).focus();
							return false;
						}
						var disctcost = document
								.getElementById("DYNAMIC_SUPPLIER_DISCOUNT_"
										+ r).value;
						if (disctcost.indexOf('.') == -1)
							disctcost += ".";
						var disdecNum = disctcost.substring(disctcost
								.indexOf('.') + 1, disctcost.length);

					}
					if (!IsNumeric(document
							.getElementById("DYNAMIC_SUPPLIER_DISCOUNT_" + r).value)) {
						alert("Please Enter Valid Supplier Discount Purchase Order");
						document.getElementById(
								"DYNAMIC_SUPPLIER_DISCOUNT_" + (r)).focus();
						return false;
					}

				}
			}

			for (var r = 0, n = table.rows.length - 1; r < n; r++) {
				for (var c = 0, m = table.rows[r].cells.length; c < m; c++) {
					if (r != rowCount) {
						// alert(c);

						if (document.getElementById("SUPPLIER_" + r).value == document
								.getElementById("SUPPLIER_" + (r + 1)).value) {
							alert("Supplier already exists for the product");
							document.getElementById("SUPPLIER_" + (r + 1))
									.focus();
							return false;
						}
					}
				}
			}

			// to validate Item supplier 
			var table = document.getElementById("multiitemsup");
			var rowCount = table.rows.length;
			for (var r = 0, n = table.rows.length - 1; r < n; r++) {
				for (var c = 0, m = table.rows[r].cells.length; c < m; c++) {
					if (r != rowCount) {
						if (document.getElementById("ITEMSUPPLIER_" + r).value == document
								.getElementById("ITEMSUPPLIER_" + (r + 1)).value) {
							alert("Supplier already exists for the product");
							document.getElementById("ITEMSUPPLIER_" + (r + 1)).focus();
							return false;
						}
					}
				}
			}
			
			// to validate supplier discount sales  end  
			var priceamt = document.getElementById('PRICE').value;
			var mpriceamt = document.getElementById('MINSELLINGPRICE').value;
			var costamt = document.getElementById('COST').value;
			if (priceamt.indexOf('.') == -1)
				priceamt += ".";
			var decNum = priceamt.substring(priceamt.indexOf('.') + 1,
					priceamt.length);

			document.productForm.DYNAMIC_CUSTOMERDISCOUNT_SIZE.value = document
					.getElementById("customerDiscount").rows.length;
			document.productForm.DYNAMIC_SUPPLIERDISCOUNT_SIZE.value = document
					.getElementById("supplierDiscount").rows.length;
			document.productForm.DYNAMIC_ITEMSUPPLIER_SIZE.value = document
			.getElementById("multiitemsup").rows.length;
			/* document.productForm.action = "/track/ItemMstServlet?ACTION=ADD";
			document.productForm.submit(); */
			var formData = new FormData($('form#productForm')[0]);
			var urlStr = "/track/ItemMstServlet?ACTION=ADD_PRODUCT";
			$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : formData,
				dataType : "json",
				contentType: false,
		        processData: false,
		        success : function(data) {
					if (data.STATUS == "FAIL") {		                               
						alert(data.MESSAGE);
					}
					else{
						$('#productModal').modal('toggle');
						//productCallback(data);	/* Define this method in parent page*/
// 						productCallbackwithall(data);
						var page =	document.getElementById("PageName").value;
						if(page=="Purchase")
							productCallbackpotable(data);
						else
							productCallbackwithall(data);
						
						onClear();
				
						
					}
											
				},
		        error: function (data) {	        	
		        	alert(data.Message);
		        }
			});	
			return true;
		}
}

function isbasicuom(){
	if (document.getElementById("ISBASICUOM").checked == true) {
		document.getElementById("ISBASICUOM").value = "1";
		var basicuom = document.getElementById("Basicuom").value;
			     
		document.getElementById("purchaseuom").value = basicuom;
		document.getElementById("salesuom").value = basicuom;
		document.getElementById("rentaluom").value = basicuom;
		document.getElementById("serviceuom").value = basicuom;
		document.getElementById("inventoryuom").value = basicuom;
				 
		}  else {
		document.getElementById("ISBASICUOM").value = "0";
		document.getElementById("purchaseuom").value = "";
		document.getElementById("salesuom").value = "";
		document.getElementById("rentaluom").value = "";
		document.getElementById("serviceuom").value = "";
		document.getElementById("inventoryuom").value = "";
		    }
}
function isposdiscount(){
	if (document.getElementById("ISPOSDISCOUNT").checked == true) {
		document.getElementById("ISPOSDISCOUNT").value = "1";
		}  else {
			(document.getElementById("ISPOSDISCOUNT").checked == false)
		document.getElementById("ISPOSDISCOUNT").value = "0";
		    }
}

function isnewarrival(){
	if (document.getElementById("ISNEWARRIVAL").checked == true) {
		document.getElementById("ISNEWARRIVAL").value = "1";
		}  else {
			(document.getElementById("ISNEWARRIVAL").checked == false)
		document.getElementById("ISNEWARRIVAL").value = "0";
		    }
}

function istopselling(){
	if (document.getElementById("ISTOPSELLING").checked == true) {
		document.getElementById("ISTOPSELLING").value = "1";
		}  else {
			(document.getElementById("ISTOPSELLING").checked == false)
		document.getElementById("ISTOPSELLING").value = "0";
		    }
}

function getNextPrd(){
	$.ajax( {
		type : "GET",
		url : "/track/ItemMstServlet?ACTION=NEXT_SEQUENCE",
		async : true,
		dataType : "json",
		contentType: false,
        processData: false,
        success : function(data) {
			if (data.ERROR_CODE == "100") {
				document.productForm.ITEM.value = data.ITEM;
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

function UOMpopUpwin(UOMTYPE){
	var uom = document.productForm.UOM.value;		
	popUpWin('../jsp/list/uomlist_save.jsp?UOM='+uom+'&UOMTYPE='+UOMTYPE);
}

function CLASSpopUpwin(CLASSTYPE){
var prd_cls_id = document.productForm.PRD_CLS_ID.value;		
popUpWin('../jsp/list/classlist_save.jsp?PRD_CLS_ID='+prd_cls_id+'&CLASSTYPE='+CLASSTYPE);
}

function TYPEpopUpwin(PRDTYPE){
	var prd_type_id = document.productForm.ARTIST.value;		
	popUpWin('../jsp/list/typelist_save.jsp?ARTIST='+prd_type_id+'&PRDTYPE='+PRDTYPE);
	}

function BRANDpopUpwin(BRANDTYPE){
	var PRD_BRAND_ID = document.productForm.PRD_BRAND.value;		
	popUpWin('../jsp/list/brandlist_save.jsp?PRD_BRAND='+PRD_BRAND_ID+'&BRANDTYPE='+BRANDTYPE);
	}

function HSCODEpopUpwin(HSCCODETYPE){
	var HSCODE = document.productForm.HSCODE.value;		
	popUpWin('../jsp/list/hscodelist_save.jsp?HSCODE='+HSCODE+'&HSCCODETYPE='+HSCCODETYPE);
	}

function COOpopUpwin(COOTYPE){
	var COO = document.productForm.COO.value;		
	popUpWin('../jsp/list/coolist_save.jsp?COO='+COO+'&COOTYPE='+COOTYPE);
	}

function addItemSupplier(tableID) {
	var table = document.getElementById(tableID);
	var rowCount = table.rows.length;
	var row = table.insertRow(rowCount);
	var form = document.forms['form'];
	var itemCell = row.insertCell(0);
	var itemCellText =  "<div class=\"input-group\"> <span class=\"glyphicon glyphicon-remove-circle emptype-action\" aria-hidden=\"true\" onclick=\"deleteItemSupplier('multiitemsup');return false;\"></span> <INPUT class=\"form-control vendorSearch\" name=\"ITEMSUPPLIER_"+rowCount+"\" ";
	itemCellText = itemCellText+ " id=\"ITEMSUPPLIER_"+rowCount+"\" type = \"TEXT\" size=\"55\" placeholder=\"Select Supplier\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateItemSupplier("+rowCount+");}\" MAXLENGTH=\"50\"></div>&nbsp;";
	itemCell.innerHTML = itemCellText;
	removeSuggestionSearch();
	addSuggestionSearch();
}

function deleteItemSupplier(tableID) {
	try {
	var table = document.getElementById(tableID);	
	var rowCount = table.rows.length;
	rowCount = rowCount*1 - 1;
	if(rowCount==0){
		alert("Can not remove the default Item");
	}else{
		table.deleteRow(rowCount);
	}
	}catch(e) {
		alert(e);
	}
}

function addRowCost(tableID) {
	
  	
	var table = document.getElementById(tableID);
	var rowCount = table.rows.length;
	var row = table.insertRow(rowCount);
	 var form = document.forms['form'];


	var itemCell = row.insertCell(0);
	var itemCellText =  "<INPUT name=\"DYNAMIC_SUPPLIER_DISCOUNT\" ";
	itemCellText = itemCellText+ " id=\"DYNAMIC_SUPPLIER_DISCOUNT_"+rowCount+"\" type = \"TEXT\" size=\"20\" MAXLENGTH=\"50\" class=\"form-control\">&nbsp;";
	itemCell.innerHTML = itemCellText;
	
	
	var itemCell = row.insertCell(1);
	var itemCellText =  "<div class=\"input-group\"> <span class=\"glyphicon glyphicon-remove-circle emptype-action\" aria-hidden=\"true\" onclick=\"deleteRowCost('supplierDiscount');return false;\"></span> <INPUT class=\"form-control supplierSearch\" name=\"SUPPLIER\" ";
	itemCellText = itemCellText+ " id=\"SUPPLIER_"+rowCount+"\" type = \"TEXT\" size=\"20\" placeholder=\"Select Supplier\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateSupplier("+rowCount+");}\" MAXLENGTH=\"50\"></div>&nbsp;";
	/* itemCellText = itemCellText+ "<span class=\"input-group-addon\"><a href=\"#\" onClick=\"javascript:popUpWin('list/multiSupplierList.jsp?SUPPLIER='+form.SUPPLIER"+'_'+rowCount+".value+'&INDEX="+rowCount+"');return false;\"><i class=\"glyphicon glyphicon-log-in\" style=\"font-size: 20px;\"></i></a></span></div>&nbsp;"; */
	itemCell.innerHTML = itemCellText;

	removeSuggestionSearch();
	addSuggestionSearch();
}

function deleteRowCost(tableID) {
	try {
	var table = document.getElementById(tableID);	
	var rowCount = table.rows.length;
	rowCount = rowCount*1 - 1;
	if(rowCount==0){
		alert("Can not remove the default Item");
	}else{
		table.deleteRow(rowCount);
	}
	}catch(e) {
		alert(e);
	}
}

function addRowSales(tableID) {
	
  	
	var table = document.getElementById(tableID);
	var rowCount = table.rows.length;
	var row = table.insertRow(rowCount);
	 var form = document.forms['form'];


	var itemCell = row.insertCell(0);
	var itemCellText =  "<INPUT class=\"form-control\" name=\"DYNAMIC_CUSTOMER_DISCOUNT\" ";
	itemCellText = itemCellText+ " id=\"DYNAMIC_CUSTOMER_DISCOUNT_"+rowCount+"\" type = \"TEXT\" size=\"20\" MAXLENGTH=\"50\">&nbsp;";
	itemCell.innerHTML = itemCellText;
	
	
	var itemCell = row.insertCell(1);
	var itemCellText =  "<div class=\"input-group\"><span class=\"glyphicon glyphicon-remove-circle emptype-action\" aria-hidden=\"true\" onclick=\"deleteRowSales('customerDiscount');return false;\"></span><INPUT class=\"form-control customerSearch\" name=\"CUSTOMER_TYPE_ID\" ";
	itemCellText = itemCellText+ " id=\"CUSTOMER_TYPE_ID_"+rowCount+"\" type = \"TEXT\" size=\"20\"   placeholder=\"Select Customer Type\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateCustomerType("+rowCount+");}\" MAXLENGTH=\"50\"></div>&nbsp;";
	/* itemCellText = itemCellText+ "<span class=\"input-group-addon\"><a href=\"#\" onClick=\"javascript:popUpWin('list/multiCustomerTypeList.jsp?CUSTOMERTYPE='+form.CUSTOMER_TYPE_ID"+'_'+rowCount+".value+'&INDEX="+rowCount+"');return false;\"><i class=\"glyphicon glyphicon-log-in\" style=\"font-size: 20px;\"></i></a></span></div>&nbsp;"; */
	itemCell.innerHTML = itemCellText;

	removeSuggestionSearch();
	addSuggestionSearch();
}

function deleteRowSales(tableID) {
	try {
	var table = document.getElementById(tableID);	
	var rowCount = table.rows.length;
	rowCount = rowCount*1 - 1;
	if(rowCount==0){
		alert("Can not remove the default Item");
	}else{
		table.deleteRow(rowCount);
	}
	}catch(e) {
		alert(e);
	}
}
function validDecimal(str) {
	if (str.indexOf('.') == -1) str += ".";
	var decNum = str.substring(str.indexOf('.')+1, str.length);
	var declength =	parseInt(document.getElementById("numberOfDecimal").value);
	if (decNum.length > declength)
	{
		return false;
		
	}
	return true;
}

function validToThreeDecimal(str) {
	if (str.indexOf('.') == -1)
		str += ".";
	var decNum = str.substring(str.indexOf('.') + 1, str.length);
	if (decNum.length > 3) {
		return false;

	}
	return true;
}

// function productCallbackwithall(productData){
// 	if(productData.STATUS="SUCCESS"){
// 		//alert(productData.MESSAGE);
// 		/*$("input[name ='item']").typeahead('val', productData.ITEM);*/
// 		var $tbody = $(".bill-table tbody");
// 		var $last = $tbody.find('tr:last');
// 		 $last.remove();
		
// 		var curency = document.form.curency.value;
// 		var body="";
// 	/* 	body += '<tr>';
// 		body += '<td class="item-img text-center">';
// 		body += '<img alt="" src="'+productData.CATLOGPATH+'" style="width: 100%;">';
// 		body += '</td>';
// 		body += '<td class="bill-item">';
// 		body += '<input type="text" name="item" class="form-control itemSearch" value="'+productData.ITEM+'" placeholder="Type or click to select an item." onchange="calculateAmount(this)">';
// 		body += '</td>';	
// 		body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="1.000" onchange="calculateAmount(this)"></td>';
// 		body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+productData.UNITCOST+'" onchange="calculateAmount(this)"></td>';
// 		body += '<td class="item-discount text-right">';
// 		body += '<div class="row">';							
// 		body += '<div class=" col-lg-12 col-sm-3 col-12">';
// 		body += '<div class="input-group my-group" style="width:120px;">';
// 		body += '<input name="item_discount" type="text" class="form-control text-right" value="0.00" onchange="calculateAmount(this)">';
// 		//body += '<textarea  rows="1" maxlength="3" style="resize: none;padding: 5px;" class="discountPicker form-control item_discounttypeSearch" name="item_discounttype" onchange="calculateAmount(this)"></textarea>';
// 		//body += "<input type=\"text\" class=\"discountPicker form-control item_discounttypeSearch\" id=\"item_discounttype\" value="+curency+" name=\"item_discounttype\" onchange=\"calculateAmount(this)\">";
// 		body += '<select name="item_discounttype" class="discountPicker form-control" onchange="calculateAmount(this)">';
// 		body += "<option value="+curency+">"+curency+"</option>";
// 		body += '<option>%</option>';										
// 		body += '</select>';
// 		body += '</div>';
// 		body += '</div>'; 
// 		body += '</div>';
// 		body += '</td>';
// 		body += '<td class="item-tax">';
// 		body += '<input type="hidden" name="tax_type">';
// 		body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax">';
// 		body += '</td>';
// 		body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
// 		body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
// 		body += '<input name="amount" type="text" class="form-control text-right" value="'+productData.UNITCOST+'" readonly="readonly" style="display:inline-block;"></td>';
// 		body += '</tr>'; */
		
		
// 		body += '<tr>';
// 		body += '<td class="item-img text-center">';
// 		body += '<img alt="" src="'+productData.CATLOGPATH+'" style="width: 100%;">';
// 		body += '</td>';
// 		body += '<td class="bill-item">';
// 		body += '<input type="text" name="item" class="form-control itemSearch" value="'+productData.ITEM+'" placeholder="Type or click to select an item." onchange="calculateAmount(this)">';
// 		body += '</td>';
// 		body += '<td class="bill-acc">';
// 		body += '<input type="text" name="account_name" class="form-control accountSearch" placeholder="Select Account">';
// 		body += '</td>';
// 		body += '<td class="invEl">';
// 		body += '<input type="text" name="UOM" class="form-control uomSearch"  value="'+productData.PURCHASEUOM+'"  placeholder="UOM">';
// 		body += '</td>';
// 		body += '<td class="invEl">';
// 		body += '<input type="text" name="loc" class="form-control locSearch" placeholder="Location">';
// 		body += '</td>';
// 		body += '<td class="invEl"><div class="input-group">';
// 		body += '<input type="text" name="batch" value="NOBATCH" class="form-control batchSearch" placeholder="Batch">';
// 		body += '<span class="input-group-addon" onclick="javascript:generateBatch(this);return false;" id="actionBatch" name="actionBatch">';
// 		body += '<a href="#" data-toggle="tooltip" data-placement="top" title="Generate">';
// 		body += '<i class="glyphicon glyphicon-edit"></i></a></span>';
// 		body += '</div></td>';
// 		body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="1.000" onchange="calculateAmount(this)"></td>';
// 		body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+productData.UNITCOST+'" onchange="calculateAmount(this)"></td>';
// 		body += '<td class="item-discount text-right">';
// 		body += '<div class="row">';							
// 		body += '<div class=" col-lg-12 col-sm-3 col-12">';
// 		body += '<div class="input-group my-group" style="width:120px;">';
// 		body += '<input name="item_discount" type="text" class="form-control text-right" value="0.00" onchange="calculateAmount(this)">';
// 		body += '<select name="item_discounttype" class="discountPicker form-control" onchange="calculateAmount(this)">';
// 		body += "<option value="+curency+">"+curency+"</option>";
// 		body += '<option>%</option>';										
// 		body += '</select>';
// 		body += '</div>';
// 		body += '</div>'; 
// 		body += '</div>';
// 		body += '</td>';
// 		body += '<td class="item-tax">';	
// 		body += '<input type="hidden" name="tax_type">';
// 		body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax" readonly>';
// 		body += '</td>';
// 		body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
// 		body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
// 		body += '<input name="amount" type="text" class="form-control text-right" value="'+productData.UNITCOST+'" readonly="readonly" style="display:inline-block;">';
// 		body += '<input name="landedCost" type="text" value="0.0" hidden>';
// 		body += '</td>';
// 		body += '</tr>';
		
		
// 		$(".bill-table tbody").append(body);
// 		calculateTotal();
// 		removeSuggestionToTable();
// 		addSuggestionToTable();
// 	}
// }
$('[data-dismiss=modal]').on('click', function (e) {
	onClear();
	});

	function onClear()
	{
	var declength = parseInt(document.getElementById("pronumberOfDecimal").value);
	var decVal = parseFloat(0).toFixed(declength);

	document.productForm.ITEM.value="";
	document.productForm.DESC.value="";
	document.productForm.TITLE.value="";
	document.productForm.REMARKS.value="";
	document.productForm.STKQTY.value="";
	document.productForm.ITEM_CONDITION.value="";
	document.productForm.COST.value="";
	document.productForm.MINSELLINGPRICE.value="";
	document.productForm.DISCOUNT.value="";
	document.productForm.PRODGST.value="";
	document.productForm.MAXSTKQTY.value="";
	document.productForm.NETWEIGHT.value="0.000";
	document.productForm.GROSSWEIGHT.value="0.000";
	document.productForm.HSCODE.value="";
	document.productForm.COO.value="";
	document.productForm.PRICE.value=decVal;
	document.productForm.COST.value=decVal;
	document.productForm.MINSELLINGPRICE.value=decVal;
	document.productForm.PRODGST.value="0.000";
	document.productForm.DYNAMIC_CUSTOMER_DISCOUNT.value="";
	document.productForm.DYNAMIC_SUPPLIER_DISCOUNT.value="";
	document.productForm.UOM.value="";
	document.productForm.PRD_CLS_DESC.value="";
	document.productForm.PRD_TYPE_DESC.value="";
	document.productForm.PRD_BRAND_DESC.value="";
	document.productForm.LOC_ID.value="";
	document.productForm.PRD_DEPT_DESC.value="";
	document.productForm.VINNO.value="";
	document.productForm.MODEL.value="";
	document.productForm.DIMENSION.value="";
	document.productForm.RENTALPRICE.value=decVal;
	document.productForm.SERVICEPRICE.value=decVal;
	document.productForm.PURCHASEUOM.value="";
	document.productForm.SALESUOM.value="";
	document.productForm.RENTALUOM.value="";
	document.productForm.SERVICEUOM.value="";
	document.productForm.INVENTORYUOM.value="";
	document.productForm.ISPOSDISCOUNT.checked =false;
	document.productForm.ISNEWARRIVAL.checked =false;
	document.productForm.ISTOPSELLING.checked =false;
	document.productForm.ISBASICUOM.checked =false;	
	document.getElementById("ISBASICUOM").value = "0";
	removeSuggestionSearch();
	addSuggestionSearch();
	addSuggestionprd();
	$("#purchaseuom").typeahead('val', '"');
	$("#purchaseuom").typeahead('val', '');
	$("#salesuom").typeahead('val', '"');
	$("#salesuom").typeahead('val', '');
	$("#INVENTORYUOM").typeahead('val', '"');
	$("#INVENTORYUOM").typeahead('val', '');
	$("#UOM").typeahead('val', '"');
	$("#UOM").typeahead('val', '');
	}
	
// 	function iscombpro(){
// 		if (document.getElementById("ISCOMPRO").checked == true) {
// 			document.getElementById("ISCOMPRO").value = "1";
// 			//$(".comproprice").show();
			
// 		}  else {
// 			document.getElementById("ISCOMPRO").value = "0";
// 			//$(".comproprice").hide();
// 		}
		
// 	}

	function iscombpro(){
		if (document.getElementById("NONE").checked == true) {
			document.getElementById("NONE").value = "NONE";
		/* 	$(".comproprice").hide(); */
		}  else {
			document.getElementById("NONE").value = "NONE";
			$(".comproprice").show();
		}
	}
	
	function iscombprofin(){
		if (document.getElementById("ISCOMPRO").checked == true) {
			document.getElementById("ISCOMPRO").value = "ISCOMPRO";
			$(".comproprice").show();		
		}
	}
	
	function iscombprosem(){
		if (document.getElementById("ISCOMPRO_SEMI").checked == true) {
			document.getElementById("ISCOMPRO_SEMI").value = "ISCOMPRO_SEMI";
			$(".comproprice").show();
		}
	}
	
	$('input[type=radio][name=CPPI]').change(function() {
		if (this.value == 'BYPRICE') {
			$("input[name ='INCPRICE']").val(parseFloat("0.00000").toFixed(document.getElementById("numberOfDecimal").value));
			$("input[name ='INCPRICEUNIT']").val($("input[name ='CURRENCYID']").val());
		}
		else if (this.value == 'BYPERCENTAGE') {
			$("input[name ='INCPRICE']").val(parseFloat("0.00000").toFixed(3));
			$("input[name ='INCPRICEUNIT']").val("%");
		}
	});
	
	function isNumChecks(){	
		var baseamount = $("input[name=INCPRICE]").val();
		var amounttype = document.productForm.CPPI.value;
		var zeroval = "0";
		if(baseamount != ""){
			var decimal =  /^[-+]?[0-9]+\.[0-9]+$/; 
			var numbers = /^[0-9]+$/;
			if(baseamount.match(decimal) || baseamount.match(numbers)) 
			{ 
				if (amounttype == 'BYPRICE') {
					$("input[name ='INCPRICE']").val(parseFloat(baseamount).toFixed(document.getElementById("numberOfDecimal").value));
				}
				else if (amounttype == 'BYPERCENTAGE') {
					$("input[name ='INCPRICE']").val(parseFloat(baseamount).toFixed(3));
				}
			}else{
				alert("Please Enter Valid Price");
				if (amounttype == 'BYPRICE') {
					$("input[name ='INCPRICE']").val(parseFloat(zeroval).toFixed(document.getElementById("numberOfDecimal").value));
				}
				else if (amounttype == 'BYPERCENTAGE') {
					$("input[name ='INCPRICE']").val(parseFloat(zeroval).toFixed(3));
				}
			}
		}else{
			if (amounttype == 'BYPRICE') {
				$("input[name ='INCPRICE']").val(parseFloat(zeroval).toFixed(document.getElementById("numberOfDecimal").value));
			}
			else if (amounttype == 'BYPERCENTAGE') {
				$("input[name ='INCPRICE']").val(parseFloat(zeroval).toFixed(3));
			}
		}
		
	}
</script>
<style>
.emptype-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -15%;
    top: 10px;
}
</style>