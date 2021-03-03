<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="${path}/resources/css/mainPage.css">
</head>
<body>
	<div class ="new_Set" >
		<div clss="map_plus" style="text-align: center; font-size: 45px;margin: 4px;">
			<div>
				<div class="new_data_close">
				 &times;
				</div>
			</div>
			<i class="fas fa-map-marked"></i>
			<div style="font-size: 16px;">Dataset Name <span><i class="fas fa-pencil-alt"></i></span></div>
		</div>
		<div class="bufferinput" style="margin-bottom: 0.4em;">
			<input type="text" class="dataname" placeholder="Enter the name of dataset" style="width: 319px; height: 25px;">
			<button class="create_input" style="width: 58px; font-size: 12px;">Create</button>
		</div>
	</div>
</body>
</html>