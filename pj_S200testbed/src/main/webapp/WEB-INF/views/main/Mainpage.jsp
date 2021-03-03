<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include/include.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>s200testbed</title>

<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCg5KvueJvogSG7DkCT2Jun4mMKqit7s-0&language=en&region=KR&callback=initMap&libraries=&v=weekly" defer></script>
<script src="${path}/resources/JS/googlemap.js"></script>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://fonts.googleapis.com/css2?family=Open+Sans&display=swap" rel="stylesheet">
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/css/all.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="${path}/resources/css/mainPage.css">
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/dt/dt-1.10.23/datatables.min.css"/>
<script type="text/javascript" src="https://cdn.datatables.net/v/dt/dt-1.10.23/datatables.min.js"></script>


</head>
<body>
	<div class="total_wrap">
		<div class="side_bar">
			<div class="side_bar_list" id="bar_list">
				<img src="${path}/resources/img/instagram_profile_image.png" style="max-width: 100%; height: auto;">
				<ul>
					<li>
						<div class="dropbtn databtn" onclick="myFunction(0)" >
							<span><i class="fas fa-globe-americas mainmarekr"></i></span>DATASET
						</div>
						<div class="content_box">
							<div id="dataset" class="dropdown-content ">
								<div class="allboxwith">
									<div class="dataset_table"></div>
								</div>
							</div>
							<div class="feature_table"></div>
							<div class="feature_edit"></div>
						</div>
					</li>
					<li>
					    <div onclick="myFunction(1)" class="dropbtn ">
								<span><i class="fas fa-map mainmarekr"></i></span>SEARCH
						</div>
						<div id="search" class="dropdown-content dropstyle2">
							<div class="allboxwith">
								<div class="testset">Search</div>
								<div class="testset2 search_contoent_btn">
									<div id="search_box_btn" class="searchbtn_css">Point</div>
									<div id="search_box_btn" class="searchbtn_css">Line</div>
									<div id="search_box_btn" class="searchbtn_css">Rect</div>
									<div id="search_box_btn" class="searchbtn_css">Poly</div>
									<div id="clearbtn" class="searchbtn_css">Clear</div>
								</div>
								<table style="border-collapse: collapse;" id="serach_table_wrap">
									<thead style="font-size: 12px;">
										<tr>
											<th>ID</th>
											<th>Name</th>
											<th>Feature Type</th>
										</tr>
									</thead>
								</table>
							</div>
						</div>
						</li>
						
				</ul>
			</div>
		</div>
		<div class = "testmapwarp"> 
			<div id="map" class="googlemap" style="/* position: absolute; */"></div>
		</div>
	</div>
</body>
<script src="${path}/resources/JS/googlemap.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js" type="text/javascript"></script>
<script src="${path}/resources/JS/googlemap.js"></script>
<script src="${path}/resources/JS/Mainpage.js"></script>
<script type="text/javascript">

	
</script>
</html>
