
<div id="stockTakeModal" class="modal fade" role="dialog">

	<div class="modal-dialog">

		<div class="modal-content">
			<form class="form-horizontal" name="form">
				<div class="modal-header">
				
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h3 class="modal-title">Download Stock Take Template</h3>
				</div>
				<div class="modal-body">

					<div class="form-group">
					<textarea class="form-control" id="action" name="action" style="display:none;"></textarea>
						<label class="control-label col-form-label col-sm-4 ">Product ID</label>
						<div class="col-sm-6">
						<div class="input-group">
							<INPUT class="form-control" name="ITEM" type = "TEXT" value="" size="30"  MAXLENGTH=50>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/item_list_inventory.jsp?TYPE=WIPSUMMARY&ITEM='+form.ITEM.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
   		 	</div>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4">Location</label>
						<div class="col-sm-6">
						<div class="input-group">
							<INPUT class="form-control" name="LOC" type = "TEXT" value="" size="30"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/loc_list_inventory.jsp?TYPE=WIPSUMMARY&LOC='+form.LOC.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
						</div>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4">Batch</label>
						<div class="col-sm-6">
						<div class="input-group">
							<INPUT class="form-control" name="BATCH" type = "TEXT" value="" size="30"  MAXLENGTH=50>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/batch_list_inventory.jsp?TYPE=WIPSUMMARY&BATCH='+form.BATCH.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Batch Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
						</div>
						</div>
					</div>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success" id="create_user"
						onclick="download()">Download</button>
					<button class="btn btn-default" data-dismiss="modal" id="cancel">Cancel</button>
				</div>
			</form>
		</div>

	</div>

</div>
<script>
$(document).ready(function(){
document.getElementById("action").value = "Export_ExcelStockTake";
});
$('[data-dismiss=modal]').on('click', function (e) {
    var $t = $(this),
        target = $t[0].href || $t.data("target") || $t.parents('.modal') || [];

  $(target)
    .find("input,select")
       .val('')
       .end()
    .find("input[type=checkbox], input[type=radio]")
       .prop("checked", "")
       .end();
});
	
	function download() {
		
		document.form.action = "/track/StockTakeInvUploadServlet?action=Export_ExcelStockTake";
		document.form.submit();
			}
	</script>
