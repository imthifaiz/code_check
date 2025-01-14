	// datatable with drap drop column and show/hide feature
	if( $('.datatable-column-interactive').length > 0 ) {
		$('.datatable-column-interactive').dataTable({
			sDom: "RC"+
				"t"+
				"< 'row' <'col-sm-3'f> r> t <'row' <'col-sm-3'i> <'col-sm-12'l> <'col-sm-12'p> > ",
			colVis: {
				buttonText: 'Show / Hide Columns',
				restore: "Restore",
				showAll: "Show all"
			},
			 "bPaginate": false,
			
       
		});
	}
