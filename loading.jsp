<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title></title>
	<link rel="stylesheet" href="<c:url value='/resources/css/bootstrap.min.css' /> ">
	<link rel="stylesheet" href="<c:url value='/resources/css/jquery.shCircleLoader.css' /> ">
	<script type="text/javascript" src="<c:url value='/resources/js/jquery-3.2.0.min.js' /> "></script>
	<script type="text/javascript" src="<c:url value='/resources/js/bootstrap.min.js' />" ></script>
	<script type="text/javascript" src="<c:url value='/resources/js/jquery.shCircleLoader.js' />" ></script>
</head>
<body>
	<script>
		$(function(){
			 $('#mask').click(function(){
				$('#loader').empty(); 
				$('#mask').toggle();
			}); 
			$('#button1').click(function(){
				$('#loader').shCircleLoader({
				    namespace: "myns",
				    color: "transparent",
				    dotsRadius: 6
				});
				$('#mask').toggle(); 
			});
		});
	</script>
	<div class="container">
		<input type="button" id="button1" value="click" />
	</div>
	<div id="loader" style="left:50%"></div>
	<div id="mask" style="position:fixed;width:100%;height:100%;top:0px;left:0px;opacity:0.4;background:#000;display:none;">
	</div> 
	<!-- https://github.com/sunhater/shCircleLoader -->
</body>
</html>