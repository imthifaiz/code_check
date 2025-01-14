<%@ include file="header.jsp"%>
<%@ page import="com.track.constants.*"%>
<%
String title = "Banner Master";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/json2.js"></script>
<%
String plant=(String)session.getAttribute("PLANT");
%>
<script type="text/javascript">



function image_deletes(imgcount){
    var formData = $('form').serialize();
    var userId="BANNER";
    var delImg= parseFloat(imgcount)-parseFloat(1);
	if(userId){
	userId = userId+'_'+imgcount;
    $.ajax({
        type: 'post',
        url: "/track/BannerServlet?Submit=itm_img_delete&ITEMM="+userId+"",
       	dataType:'html',
    data:  formData,
      
        success: function (data) {
        	console.log(data)
        	var result =JSON.parse(data).result;
        	$('#msg').html(result.message); 
//         	  $('#item_img1').attr('src',"../jsp/dist/img/NO_IMG.png");
        	  $('#item_img'+imgcount).attr('src',"../jsp/dist/img/NO_IMG.png");
        	  $('#productImg'+'_'+imgcount).val('');
         
        },
        error: function (data) {
        	
            alert(data.responseText);
        }
    });
	}
        return false; 
  }	

function image_edits(imgcount){
	var myForm = document.getElementById('form');
	var formData = new FormData(myForm);
    var userId="BANNER";
	if(userId){
	userId = userId+'_'+imgcount;
    $.ajax({
        type: 'post',
        url: "/track/BannerServlet?Submit=itm_img_edit&ITEMM="+userId+"",
       	dataType:'html',
    data:  formData,//{key:val}
    contentType: false,
    processData: false,
      
        success: function (data) {
        	console.log(data)
        	var result =JSON.parse(data).result;
        	$('#msg').html(result.message); 
       	  	$('#productImg'+'_'+imgcount).val('');
        },
        error: function (data) {
        	
            alert(data.responseText);
        }
    });
	}
        return false; 
  }

</script>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	  <!-- Thanzith Modified on 23.02.2022 -->
            <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <!-- <li><a href="../BannerServlet/summary"><span class="underline-on-hover">Product Summary</span></a></li>  -->                       
                <li><label>Banner Master</label></li>                                   
            </ul>             
    <!-- Thanzith Modified on 23.02.2022 -->
             <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../home'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
		
 <div class="box-body">
  <form class="form-horizontal" name="form" method="post" id="form">
   <div id="catalogues">
      <!--   <br> -->
    	
         <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Upload Image">Upload Banner Image 1</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control btn-sm" name="IMAGE_UPLOAD1" id ="productImg_1" type="File" size="20" MAXLENGTH=100>
      		</div>
      		<div class="col-sm-1">  
    			<img alt="" src="../jsp/dist/img/NO_IMG.png" id="item_img1" style="width: 100%;">
    		</div>
      		<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Remove Banner Image 1" onClick="image_deletes(1);"> 
			<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Upload & Save Banner Image 1" onClick="image_edits(1);">
    	</div>
    	
         <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Upload Image">Upload Banner Image 2</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control btn-sm" name="IMAGE_UPLOAD2" type="File" id ="productImg_2" size="20" MAXLENGTH=100>
      		</div>
      		<div class="col-sm-1">  
    			<img alt="" src="../jsp/dist/img/NO_IMG.png" id="item_img2" style="width: 100%;">
    		</div>
      		<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Remove Banner Image 2" onClick="image_deletes(2);"> 
			<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Upload & Save Banner Image 2" onClick="image_edits(2);">
    	</div>
    	
         <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Upload Image">Upload Banner Image 3</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control btn-sm" name="IMAGE_UPLOAD3" type="File" id ="productImg_3" size="20" MAXLENGTH=100>
      		</div>
      		<div class="col-sm-1">  
    			<img alt="" src="../jsp/dist/img/NO_IMG.png"  id="item_img3" style="width: 100%;">
    		</div>
      		<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Remove Banner Image 3" onClick="image_deletes(3);"> 
			<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Upload & Save Banner Image 3" onClick="image_edits(3);">
    	</div>
    	
         <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Upload Image">Upload Banner Image 4</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control btn-sm" name="IMAGE_UPLOAD4" type="File" id ="productImg_4" size="20" MAXLENGTH=100>
      		</div>
      		<div class="col-sm-1">  
    			<img alt="" src="../jsp/dist/img/NO_IMG.png" id="item_img4" style="width: 100%;">
    		</div>
      		<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Remove Banner Image 4" onClick="image_deletes(4);"> 
			<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Upload & Save Banner Image 4" onClick="image_edits(4);">
    	</div>
    	
         <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Upload Image">Upload Banner Image 5</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control btn-sm" name="IMAGE_UPLOAD5" type="File" id ="productImg_5" size="20" MAXLENGTH=100>
      		</div>
      		<div class="col-sm-1">  
    			<img alt="" src="../jsp/dist/img/NO_IMG.png" id="item_img5" style="width: 100%;">
    		</div>
      		<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Remove Banner Image 5" onClick="image_deletes(5);"> 
			<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Upload & Save Banner Image 5" onClick="image_edits(5);">
    	</div>
         <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Upload Image">Upload Banner Image 6</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control btn-sm" name="IMAGE_UPLOAD6" type="File" id ="productImg_6" size="20" MAXLENGTH=100>
      		</div>
      		<div class="col-sm-1">  
    			<img alt="" src="../jsp/dist/img/NO_IMG.png" id="item_img6" style="width: 100%;">
    		</div>
      		<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Remove Banner Image 6" onClick="image_deletes(6);"> 
			<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Upload & Save Banner Image 6" onClick="image_edits(6);">
    	</div>
         <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Upload Image">Upload Banner Image 7</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control btn-sm" name="IMAGE_UPLOAD7" type="File" id ="productImg_7" size="20" MAXLENGTH=100>
      		</div>
      		<div class="col-sm-1">  
    			<img alt="" src="../jsp/dist/img/NO_IMG.png" id="item_img7" style="width: 100%;">
    		</div>
      		<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Remove Banner Image 7" onClick="image_deletes(7);"> 
			<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Upload & Save Banner Image 7" onClick="image_edits(7);">
    	</div>
         <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Upload Image">Upload Banner Image 8</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control btn-sm" name="IMAGE_UPLOAD8" type="File" id ="productImg_8" size="20" MAXLENGTH=100>
      		</div>
      		<div class="col-sm-1">  
    			<img alt="" src="../jsp/dist/img/NO_IMG.png" id="item_img8" style="width: 100%;">
    		</div>
      		<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Remove Banner Image 8" onClick="image_deletes(8);"> 
			<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Upload & Save Banner Image 8" onClick="image_edits(8);">
    	</div>
         <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Upload Image">Upload Banner Image 9</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control btn-sm" name="IMAGE_UPLOAD9" type="File" id ="productImg_9" size="20" MAXLENGTH=100>
      		</div>
      		<div class="col-sm-1">  
    			<img alt="" src="../jsp/dist/img/NO_IMG.png" id="item_img9" style="width: 100%;">
    		</div>
      		<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Remove Banner Image 9" onClick="image_deletes(9);"> 
			<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Upload & Save Banner Image 9" onClick="image_edits(9);">
    	</div>
         <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Upload Image">Upload Banner Image 10</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control btn-sm" name="IMAGE_UPLOAD10" type="File" id ="productImg_10" size="20" MAXLENGTH=100>
      		</div>
      		<div class="col-sm-1">  
    			<img alt="" src="../jsp/dist/img/NO_IMG.png" id="item_img10" style="width: 100%;">
    		</div>
      		<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Remove Banner Image 10" onClick="image_deletes(10);"> 
			<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Upload & Save Banner Image 10" onClick="image_edits(10);">
    	</div>
    	
        </div>
  
  
  </form>
    </div>
 </div>
  </div>
  
  <script>
  $(document).ready(function(){
	    $('[data-toggle="tooltip"]').tooltip(); 

	    getProductDetail(); 
		   
  }); 

  

   function getProductDetail() {
		
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				async:false ,
				data : {
					PLANT : "<%=plant%>",
					ACTION : "GET_PRODUCT_IMG_DETAILS_FOR_BANNER"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
	                                        var resultVal = data.result;
	                                                 if(resultVal.bannerpath1 == undefined) 
	                                                	 $("#item_img1").attr("src","../jsp/dist/img/NO_IMG.png");
	                                                 else	 
	                                           			 $("#item_img1").attr("src","/track/ReadFileServlet/?fileLocation="+resultVal.bannerpath1);
	                                                 
	                                                 if(resultVal.bannerpath2 == undefined) 
	                                                	 $("#item_img2").attr("src","../jsp/dist/img/NO_IMG.png");
	                                                 else	 
	                                           			 $("#item_img2").attr("src","/track/ReadFileServlet/?fileLocation="+resultVal.bannerpath2);
	                                                 
	                                                 if(resultVal.bannerpath3 == undefined) 
	                                                	 $("#item_img3").attr("src","../jsp/dist/img/NO_IMG.png");
	                                                 else	 
	                                           			 $("#item_img3").attr("src","/track/ReadFileServlet/?fileLocation="+resultVal.bannerpath3);
	                                                 
	                                                 if(resultVal.bannerpath4 == undefined) 
	                                                	 $("#item_img4").attr("src","../jsp/dist/img/NO_IMG.png");
	                                                 else	 
	                                           			 $("#item_img4").attr("src","/track/ReadFileServlet/?fileLocation="+resultVal.bannerpath4);
	                                                 
	                                                 if(resultVal.bannerpath5 == undefined) 
	                                                	 $("#item_img5").attr("src","../jsp/dist/img/NO_IMG.png");
	                                                 else	 
	                                           			 $("#item_img5").attr("src","/track/ReadFileServlet/?fileLocation="+resultVal.bannerpath5);

		                                             if(resultVal.bannerpath6 == undefined) 
	                                                	 $("#item_img6").attr("src","../jsp/dist/img/NO_IMG.png");
	                                                 else	 
	                                           			 $("#item_img6").attr("src","/track/ReadFileServlet/?fileLocation="+resultVal.bannerpath6);

			                                         if(resultVal.bannerpath7 == undefined) 
	                                                	 $("#item_img7").attr("src","../jsp/dist/img/NO_IMG.png");
	                                                 else	 
	                                           			 $("#item_img7").attr("src","/track/ReadFileServlet/?fileLocation="+resultVal.bannerpath7);

	                                                 if(resultVal.bannerpath8 == undefined) 
	                                                	 $("#item_img8").attr("src","../jsp/dist/img/NO_IMG.png");
	                                                 else	 
	                                           			 $("#item_img8").attr("src","/track/ReadFileServlet/?fileLocation="+resultVal.bannerpath8);

	                                                 if(resultVal.bannerpath9 == undefined) 
	                                                	 $("#item_img9").attr("src","../jsp/dist/img/NO_IMG.png");
	                                                 else	 
	                                           			 $("#item_img9").attr("src","/track/ReadFileServlet/?fileLocation="+resultVal.bannerpath9);

		                                             if(resultVal.bannerpath10 == undefined) 
	                                                	 $("#item_img10").attr("src","../jsp/dist/img/NO_IMG.png");
	                                                 else	 
	                                           			 $("#item_img10").attr("src","/track/ReadFileServlet/?fileLocation="+resultVal.bannerpath10);
	                         
						}
					}
				});
		} 

	function readURL1(input) {
	    if (input.files && input.files[0]) {
	        var reader = new FileReader();
	        const allowedExtension = ['image/jpeg', 'image/jpg', 'image/png','image/gif','image/bmp','image/webp'];
	        if(allowedExtension.indexOf(input.files[0].type)>-1)
	        {
	        } else {
	       		//Validate Iamge -Azees 02.02.23
	             alert('Upload valid image file');
	             $('#productImg_1').val('');
	             return false;
	    	}

	        reader.onload = function (e) {
	        	var image = new Image();
	        	image.src = e.target.result;
	        	//Validate the File Height and Width.250px -Azees 02.02.23
	        	  image.onload = function () {
	        	    var height = this.height;
	        	    var width = this.width;
	        	    if (height > 150 || width > 250) {
	        	    $('#productImg_1').val('');
	        	    alert("Image size should be 250 x 150 px");
	        	      return false;
	        	    }
		            $('#item_img1').attr('src', e.target.result);
	        	    return true;
	        	  };
	        }

	        reader.readAsDataURL(input.files[0]);
	    }
	}
	$(document).on('change', '#productImg_1', function (e) {
	    readURL1(this);
	});

	function readURL2(input) {
	    if (input.files && input.files[0]) {
	        var reader = new FileReader();
	        const allowedExtension = ['image/jpeg', 'image/jpg', 'image/png','image/gif','image/bmp','image/webp'];
	        if(allowedExtension.indexOf(input.files[0].type)>-1)
	        {
	        } else {
	       		//Validate Iamge -Azees 02.02.23
	             alert('Upload valid image file');
	             $('#productImg_2').val('');
	             return false;
	    	}

	        reader.onload = function (e) {
	        	var image = new Image();
	        	image.src = e.target.result;
	        	//Validate the File Height and Width.250px -Azees 02.02.23
	        	  image.onload = function () {
	        	var height = this.height;
        	    var width = this.width;
        	    if (height > 150 || width > 250) {
        	    $('#productImg_2').val('');
        	    alert("Image size should be 250 x 150 px");
        	      return false;
        	    }
	            $('#item_img2').attr('src', e.target.result);
	            return true;
	        };
	        }

	        reader.readAsDataURL(input.files[0]);
	    }
	}
	$(document).on('change', '#productImg_2', function (e) {
	    readURL2(this);
	});

	function readURL3(input) {
	    if (input.files && input.files[0]) {
	        var reader = new FileReader();
	        const allowedExtension = ['image/jpeg', 'image/jpg', 'image/png','image/gif','image/bmp','image/webp'];
	        if(allowedExtension.indexOf(input.files[0].type)>-1)
	        {
	        } else {
	       		//Validate Iamge -Azees 02.02.23
	             alert('Upload valid image file');
	             $('#productImg_3').val('');
	             return false;
	    	}

	        reader.onload = function (e) {
	        	var image = new Image();
	        	image.src = e.target.result;
	        	//Validate the File Height and Width.250px -Azees 02.02.23
	        	  image.onload = function () {
	        	var height = this.height;
        	    var width = this.width;
        	    if (height > 150 || width > 250) {
        	    $('#productImg_3').val('');
        	    alert("Image size should be 250 x 150 px");
        	      return false;
        	    }
	            $('#item_img3').attr('src', e.target.result);
	            return true;
	        };
	        }

	        reader.readAsDataURL(input.files[0]);
	    }
	}
	$(document).on('change', '#productImg_3', function (e) {
	    readURL3(this);
	});

	function readURL4(input) {
	    if (input.files && input.files[0]) {
	        var reader = new FileReader();
	        const allowedExtension = ['image/jpeg', 'image/jpg', 'image/png','image/gif','image/bmp','image/webp'];
	        if(allowedExtension.indexOf(input.files[0].type)>-1)
	        {
	        } else {
	       		//Validate Iamge -Azees 02.02.23
	             alert('Upload valid image file');
	             $('#productImg_4').val('');
	             return false;
	    	}

	        reader.onload = function (e) {
	        	var image = new Image();
	        	image.src = e.target.result;
	        	//Validate the File Height and Width.250px -Azees 02.02.23
	        	  image.onload = function () {
	        	var height = this.height;
        	    var width = this.width;
        	    if (height > 150 || width > 250) {
        	    $('#productImg_4').val('');
        	    alert("Image size should be 250 x 150 px");
        	      return false;
        	    }
	            $('#item_img4').attr('src', e.target.result);
	            return true;
	        };
	        }

	        reader.readAsDataURL(input.files[0]);
	    }
	}
	$(document).on('change', '#productImg_4', function (e) {
	    readURL4(this);
	});

	function readURL5(input) {
	    if (input.files && input.files[0]) {
	        var reader = new FileReader();
	        const allowedExtension = ['image/jpeg', 'image/jpg', 'image/png','image/gif','image/bmp','image/webp'];
	        if(allowedExtension.indexOf(input.files[0].type)>-1)
	        {
	        } else {
	       		//Validate Iamge -Azees 02.02.23
	             alert('Upload valid image file');
	             $('#productImg_5').val('');
	             return false;
	    	}

	        reader.onload = function (e) {
	        	var image = new Image();
	        	image.src = e.target.result;
	        	//Validate the File Height and Width.250px -Azees 02.02.23
	        	  image.onload = function () {
	        	var height = this.height;
        	    var width = this.width;
        	    if (height > 150 || width > 250) {
        	    $('#productImg_5').val('');
        	    alert("Image size should be 250 x 150 px");
        	      return false;
        	    }
	            $('#item_img5').attr('src', e.target.result);
	            return true;
	        };
	        }

	        reader.readAsDataURL(input.files[0]);
	    }
	}
	$(document).on('change', '#productImg_5', function (e) {
	    readURL5(this);
	});
	
	function readURL6(input) {
	    if (input.files && input.files[0]) {
	        var reader = new FileReader();
	        const allowedExtension = ['image/jpeg', 'image/jpg', 'image/png','image/gif','image/bmp','image/webp'];
	        if(allowedExtension.indexOf(input.files[0].type)>-1)
	        {
	        } else {
	       		//Validate Iamge -Azees 02.02.23
	             alert('Upload valid image file');
	             $('#productImg_6').val('');
	             return false;
	    	}

	        reader.onload = function (e) {
	        	var image = new Image();
	        	image.src = e.target.result;
	        	//Validate the File Height and Width.250px -Azees 02.02.23
	        	  image.onload = function () {
	        	var height = this.height;
        	    var width = this.width;
        	    if (height > 150 || width > 250) {
        	    $('#productImg_6').val('');
        	    alert("Image size should be 250 x 150 px");
        	      return false;
        	    }
	            $('#item_img6').attr('src', e.target.result);
	            return true;
	        };
	        }

	        reader.readAsDataURL(input.files[0]);
	    }
	}
	$(document).on('change', '#productImg_6', function (e) {
	    readURL6(this);
	});
	
	function readURL7(input) {
	    if (input.files && input.files[0]) {
	        var reader = new FileReader();
	        const allowedExtension = ['image/jpeg', 'image/jpg', 'image/png','image/gif','image/bmp','image/webp'];
	        if(allowedExtension.indexOf(input.files[0].type)>-1)
	        {
	        } else {
	       		//Validate Iamge -Azees 02.02.23
	             alert('Upload valid image file');
	             $('#productImg_7').val('');
	             return false;
	    	}

	        reader.onload = function (e) {
	        	var image = new Image();
	        	image.src = e.target.result;
	        	//Validate the File Height and Width.250px -Azees 02.02.23
	        	  image.onload = function () {
	        	var height = this.height;
        	    var width = this.width;
        	    if (height > 150 || width > 250) {
        	    $('#productImg_7').val('');
        	    alert("Image size should be 250 x 150 px");
        	      return false;
        	    }
	            $('#item_img7').attr('src', e.target.result);
	            return true;
	        };
	        }

	        reader.readAsDataURL(input.files[0]);
	    }
	}
	$(document).on('change', '#productImg_7', function (e) {
	    readURL7(this);
	});
	
	function readURL8(input) {
	    if (input.files && input.files[0]) {
	        var reader = new FileReader();
	        const allowedExtension = ['image/jpeg', 'image/jpg', 'image/png','image/gif','image/bmp','image/webp'];
	        if(allowedExtension.indexOf(input.files[0].type)>-1)
	        {
	        } else {
	       		//Validate Iamge -Azees 02.02.23
	             alert('Upload valid image file');
	             $('#productImg_8').val('');
	             return false;
	    	}

	        reader.onload = function (e) {
	        	var image = new Image();
	        	image.src = e.target.result;
	        	//Validate the File Height and Width.250px -Azees 02.02.23
	        	  image.onload = function () {
	        	var height = this.height;
        	    var width = this.width;
        	    if (height > 150 || width > 250) {
        	    $('#productImg_8').val('');
        	    alert("Image size should be 250 x 150 px");
        	      return false;
        	    }
	            $('#item_img8').attr('src', e.target.result);
	            return true;
	        };
	        }

	        reader.readAsDataURL(input.files[0]);
	    }
	}
	$(document).on('change', '#productImg_8', function (e) {
	    readURL8(this);
	});
	
	function readURL9(input) {
	    if (input.files && input.files[0]) {
	        var reader = new FileReader();
	        const allowedExtension = ['image/jpeg', 'image/jpg', 'image/png','image/gif','image/bmp','image/webp'];
	        if(allowedExtension.indexOf(input.files[0].type)>-1)
	        {
	        } else {
	       		//Validate Iamge -Azees 02.02.23
	             alert('Upload valid image file');
	             $('#productImg_9').val('');
	             return false;
	    	}

	        reader.onload = function (e) {
	        	var image = new Image();
	        	image.src = e.target.result;
	        	//Validate the File Height and Width.250px -Azees 02.02.23
	        	  image.onload = function () {
	        	var height = this.height;
        	    var width = this.width;
        	    if (height > 150 || width > 250) {
        	    $('#productImg_9').val('');
        	    alert("Image size should be 250 x 150 px");
        	      return false;
        	    }
	            $('#item_img9').attr('src', e.target.result);
	            return true;
	        };
	        }

	        reader.readAsDataURL(input.files[0]);
	    }
	}
	$(document).on('change', '#productImg_9', function (e) {
	    readURL9(this);
	});
	
	function readURL10(input) {
	    if (input.files && input.files[0]) {
	        var reader = new FileReader();
	        const allowedExtension = ['image/jpeg', 'image/jpg', 'image/png','image/gif','image/bmp','image/webp'];
	        if(allowedExtension.indexOf(input.files[0].type)>-1)
	        {
	        } else {
	       		//Validate Iamge -Azees 02.02.23
	             alert('Upload valid image file');
	             $('#productImg_10').val('');
	             return false;
	    	}

	        reader.onload = function (e) {
	        	var image = new Image();
	        	image.src = e.target.result;
	        	//Validate the File Height and Width.250px -Azees 02.02.23
	        	  image.onload = function () {
	        	var height = this.height;
        	    var width = this.width;
        	    if (height > 150 || width > 250) {
        	    $('#productImg_10').val('');
        	    alert("Image size should be 250 x 150 px");
        	      return false;
        	    }
	            $('#item_img10').attr('src', e.target.result);
	            return true;
	        };
	        }

	        reader.readAsDataURL(input.files[0]);
	    }
	}
	$(document).on('change', '#productImg_10', function (e) {
	    readURL10(this);
	});
  
  </script>
  
  <jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
  