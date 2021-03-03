<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../include/include.jsp"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/dt/dt-1.10.21/datatables.min.css" />
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/css/all.min.css" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Open+Sans&display=swap" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="${path}/resources/css/mainPage.css">
<link rel="stylesheet" type="text/css" href="${path}/resources/css/Dataset.css">
<style type="text/css">
.thiscolor{
	color: tomato;
}

.on_off{
    cursor: pointer;
	height: 100%;
	position: relative;
}

.on_off i{
	position: absolute;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
}

.noclick {
	color: black;
}

.menutomato{
	color: tomato;
}
/* page-loading */
#page-loading {
    width: 100%;
    height: 100%;
    top: 0px;
    left: 0px;
    position: fixed;
    display: none;
    background-color: #fff;
    z-index: 9999;
    text-align: center;
    opacity: 0.7;
}
 
#page-loading i{
    vertical-align: middle;
    text-align: center;
    display: table-cell;
    font-size: 60px;
}
	
</style>
</head>
<body>
	<div class="testset">Dataset</div>
	<div class="testset2 button_box_style2">
		
		<button class="save_data_btn Upload">Upload</button>
		<div class="save_data_btn create_dataset">Create <br>dataset</div>
		<button class="save_data_btn save_gml">Save to S201</button>
		<button class="save_data_btn delete_gml">Delete</button>
	</div>
	<div class="table_top">
		<table style="border-collapse: collapse; width: 100%; font-size: 12px;"
			class="click_table" id="datasetlist">
			<tr style="background: white" class="checktable allcheck">
				<th style="width: 46px"><input type="checkbox" name="checkAll" id="checkAll"></th>
				<th  style="width: 46px">ID</th>
				<th style="width: 158px">Name</th>
				<th style="width: 68px">Type</th>
				<th style="width: 68px">FC ver</th>
				<th style="width: 68px">On / Off</th>
			</tr>
			<c:forEach items="${map.list}" var="list">
				<tr>
					<td ><input type="checkbox" name="checkOne" class="checkSelect">
					</td>
					<td class="data_ds_dix">${list.ds_idx}</td>
					<td class="testtd" >${list.id}</td>
					<td >${list.productidentifier}</td>
					<td >${list.value}</td>
					<td ><div class="on_off"style="font-size: 22px;" ><i class="fas fa-info-circle"></i></div></td>
				</tr>
			</c:forEach>
		</table>
	</div>
	<div class="testset2">
		<form action="${path}/" method="GET" style="maring: 0;">
			<div class="bufferinput">
				<input type="text" id="data_search" name="keyword"
					   placeholder="Please enter a name to search" value="${map.keyword}"style="width: 78.2%">
				<button type="button" class="search_input" onclick="inputbox();"style="width: 58px;">Search</button>
				<button type="button" class="search_input" onclick="dataset_table();" style="width: 58px;">Return</button>
			</div>
		</form>
	</div>
	<div class="pagination">
		<c:if test="${map.pager.curBlock > 1}">
			<button value="${path}/dataset&curPage=${map.pager.blockBegin-10}"onclick="address(this);">&laquo;</button>
			<button value="${path}/dtaset&curPage=1" onclick="address(this);">1</button>
			<span>...</span>
		</c:if>
		<c:forEach var="num" begin="${map.pager.blockBegin}" end="${map.pager.blockEnd}">
			<c:choose>
				<c:when test="${num == map.pager.curPage}">
					<button value="${path}/dataset&curPage=${num}" onclick="address(this);">${num}</button>
				</c:when>
				<c:otherwise>
					<button value="${path}/dataset&curPage=${num}" onclick="address(this);">${num}</button>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		<c:if test="${map.pager.curBlock < map.pager.totBlock}">
			<span>...</span>
			<button value="${path}/dataset&curPage=${map.pager.totPage}"onclick="address(this);">${map.pager.totPage}</button>
			<button value="${path}/dataset&curPage=${map.pager.blockEnd + 1}"onclick="address(this);">&raquo;</button>
		</c:if>
	</div>
	
	<div id="page-loading">
    	<i class="fa fa-spinner fa-pulse fa-3x fa-fw margin-bottom"></i>    
	</div>
	
<!-- create datatset modal -->
	 <div class="modal_new">
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
	</div>	

<!-- create datatset modal -->
	 <div class="modal_create_name">
		<div class ="new_Set" >
			<div clss="map_plus" style="text-align: center; font-size: 41px;margin: 4px;">
				<div>
					<div class="modal_create_name_close">
					 &times;
					</div>
				</div>
				<i class="fas fa-map-marked"></i>
				<div style="font-size: 16px;">Upload to file Name <span><i class="fas fa-pencil-alt"></i></span></div>
			</div>
			<div class="bufferinput" style="margin-bottom: 0.4em;">
				<input type="text" class="datauploadname" placeholder="Enter the file name to upload" style="width: 319px; height: 25px;">
				<button class="Select_input_name" style="width: 58px; font-size: 12px;">Select</button>
				<button class="create_input_name" style="width: 58px; font-size: 12px;">Upload</button>
				<form id="fileForm" enctype="multipart/form-data" method="post" accept-charset="UTF-8">
					<input type="file" class="upload_gml" name="inputFile" id="inputFile" style="display: none;" onchange="uploadForm();" accept=".gml, .xlsx" >
				</form>
			</div>
			<div style="width: 435px; margin: auto; color: tomato; font-size: 12">* Dataset names can only be written in English </div>
			<div>
				
				<div class = "selectname" style="width: 435px; margin: auto"> Selected file name : </div>
			</div>
		</div>
	</div>		
	
</body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js" type="text/javascript"></script>
<script src="${path}/resources/JS/totalcript.js"></script>
<script src="${path}/resources/JS/Dataset.js"></script>
<script type="text/javascript">

//# sourceURL=Dataset.jsp
</script>
</html>