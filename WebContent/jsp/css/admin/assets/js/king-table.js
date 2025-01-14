      $(document).ready( function() {
	$('.datatable-column-interactive').dataTable({
		sDom: "RC"+
		"< 'row' <'col-sm-3'l><'col-sm-3'f> B>"+
			"t"+
			"< 'row'<'col-sm-6'i>  <'col-sm-6'p>>",
			
		colVis: {
			buttonText: 'Show / Hide Columns',
			restore: "Restore",
			showAll: "Show all"
		},
		 "bPaginate": true,
    scrollY:        400,
    "pageLength": 100,
    buttons: [
              {
    		className:"btn btn-primary",
                 extend: 'collection',
                 text: 'Export to <span class="caret"></span>',
                buttons: [ 
    	  
                {
    		        extend: 'excelHtml5',
    				text:"<i class='fa fa-file-excel-o' aria-hidden='true'></i> Export To Excel",
    				title: 'Check IN Summary (by quantity)',
                    exportOptions: {
                    columns:  [ ':visible' ]
                    }
                },
                {
    			 orientation: 'landscape',
				 pageSize: 'a4',
    			 text:"<i class='fa fa-file-pdf-o' aria-hidden='true'></i> Export To PDF",
    			 title: 'Check IN Summary (by quantity)',
                 extend: 'pdfHtml5',
                 exportOptions: {
                 columns:  [':visible' ]
                    }
                },        
            ]		
              }
           ]
		
   
	});

  	});
      