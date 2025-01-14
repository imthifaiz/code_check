</body>
<script>
    function openNav() {
        document.getElementById("mySidenav").style.width = "350px";
        document.getElementById("main").style.marginLeft = "350px";
    }

    function closeNav() {
        document.getElementById("mySidenav").style.width = "0";
        document.getElementById("main").style.marginLeft = "0";
    }
	function logout(){
		window.location.href="index.html";
	}
</script>
<style>
	.align_middle{
		position: relative;
		top: 30%;
		transform: translateY(-50%);
	}
	.home_icons{
		border:1px solid  #33687A;
		height:150px;
		cursor: pointer;
	}
	.home_icons:hover{
		background: #2c2c2c;
		color: #FFF;
	}
	.img{
	 content: url(images/user-b.png);
	}
	.img2{
	 content: url(images/sysuser-b.png);
	}
	.img3{
	 content: url(images/sysadm-b.png);
	}
	.img4{
	 content: url(images/orderadmin-b.png);
	}
	.img5{
	 content: url(images/orderconfig-b.png);
	}
	.img6{
	 content: url(images/ordermgt-b.png);
	}
	.img7{
	 content: url(images/inpremises-b.png);
	}
	.img8{
	 content: url(images/sysadm-b.png);
	}
	.img9{
	 content: url(images/outbound-b.png);
	}
	.img10{
	 content: url(images/reports-b.png);
	}
	.home_icons:hover
	.img{
    content:url(images/user-w.png);    
		}
	.home_icons:hover
	.img2{
		content: url(images/sysuser-w.png);
	}
	.home_icons:hover
	.img3{
		content: url(images/sysadm-w.png);
	}
	.home_icons:hover
	.img4{
		content: url(images/orderadmin-w.png);
	}
	.home_icons:hover
	.img5{
		content: url(images/orderconfig-w.png);
	}
	.home_icons:hover
	.img6{
		content: url(images/ordermgt-w.png);
	}
	.home_icons:hover
	.img7{
		content: url(images/inpremises-w.png);
	}
	.home_icons:hover
	.img8{
		content: url(images/sysadm-w.png);
	}
	.home_icons:hover
	.img9{
		content: url(images/outbound-w.png);
	}
	.home_icons:hover
	.img10{
		content: url(images/reports-w.png);
	}
	
	.user_box{
		border: 1px solid #33687A;
		padding:15px;
	}
	</style>
</html>
