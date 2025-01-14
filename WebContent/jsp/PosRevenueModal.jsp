<div id="posRevenueModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-lg box-body">	
	<form name="posRevenueForn" id="posRevenueForn" method="post" enctype="multipart/form-data">
	  <!-- Modal content-->
	  <div class="modal-content">
	    <div class="modal-header">
	      <button type="button" class="close" data-dismiss="modal">&times;</button>
	      <h3 class="modal-title">Ledger Info</h3>
	    </div>
	    <div class="modal-body">
	
			<div class="row form-group">
        	<label class="col-form-modal-label col-sm-2">GL DOC</label>
        		<div class="col-sm-2">
        			<div class="input-group">
	        			<input class="form-control" name="GL" id="GL" type="text"  value=""> 
			   		</div>
	        	</div>
         			<div class="col-sm-2">
        			<div class="input-group">
	        			<input class="form-control" name="GLNO" id="GLNO" type="text" value="">
		   			</div>
        		</div>
        		<div class="col-sm-6">
   				<label class="col-form-modal-label col-sm-2">Description</label>
        			<label class="radio-inline">
		      			<textarea rows="2" name="DESC" class="ember-text-area form-control ember-view" placeholder="Max 100 characters" maxlength="100" style="height: 72px; width: 175px;"></textarea></label>
        		</div>
        	</div>
        	
	      	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">Doc Date</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="DOCDATE" id="DOCDATE" type="text" value=""> 
	        	</div>
        	</div>
        	
      		<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">Doc No</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="DOCNO" id="DOCNO" type="text" value=""> 
	        	</div>
           </div>
        	
			<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">Amount</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="AMOUNT" id="AMOUNT" type="text" value=""> 
	        	</div>
          		<label class="col-form-modal-label col-sm-2">Created By/Dt</label>
          		<div class="col-sm-2">
	        		<div class="input-group">
		        		<input class="form-control" name="CRBY" id="CRBY" type="text" value=""> 
			   		</div>
	        	</div>
	        	<div class="col-sm-2">
	        		<div class="input-group">
		        		<input class="form-control" name="CRDT" id="CRDT" type="text" value=""> 
			   		</div>
	        	</div>
        	</div>

        	<div class="bs-example">
        <table id="table" class="table table-bordred table-striped"  style="width: 100%;">  
     			<thead style="text-align: center">  
		          <tr id="headerRow">  
		            <th style="font-size: smaller;">Code</th>  
		            <th style="font-size: smaller;">Name</th>
		            <th style="font-size: smaller;">Dr</th>
		            <th style="font-size: smaller;">Cr</th>
		          </tr>  
		        </thead> 
        </table>
    </div>
    
	    </div>
	  </div>
	  </form>
	</div>
</div>

<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
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