<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../include/include.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="${path}/resources/css/Featuredatalist.css">
<link href="https://fonts.googleapis.com/css2?family=Open+Sans&display=swap" rel="stylesheet">
 
<style type="text/css">

.backgorund{
	background-color: tomato;
}

.hide_column{
	display: none;
}

.menutomato{
	color: tomato;
}

#page-loading {
    width: 100%;
    height: 100%;
    top: 0px;
    left: 0px;
    position: fixed;
    display: none;
    background-color: black;
    z-index: 9999;
    text-align: center;
    opacity: 0.7;
}
 
#page-loading i{
	color: wheat;
    vertical-align: middle;
    text-align: center;
    display: table-cell;
    font-size: 60px;
}

</style>
</head>
<body>
	<span class="featureclose">&times;</span>
		 <div class="feature_table_top">
		 <div>
			 <div class="feature_type_name" style="color: tomato; font-weight: bold; font-size: 16px;" >${map.name}</div>
		     <div class="feature_create_btn" style="display: flex">
				<div class="feature_btn_wrap">
					<button id="feature_create_btn">Create Feature</button>
				</div>
				<div class="feature_btn_wrap">
					<button class="feature_del_btn" id="feature_del">Delete Feature</button>
				</div>
			 </div>
			 <table style="border-collapse: collapse; width: 100%; font-size: 12px;" class="feturedata_table" id="featuredata_list">
				<tr style="background: white" class="checktable allcheck">
					<th></th>
					<th>Name</th>
					<th style="display: none">ID</th>
					<th>Type</th>
					<th></th>
				</tr>
				<c:forEach items="${map.list}" var="list">
					<tr
						 <c:choose>
							 <c:when test="${map.mtitle eq list.f_idx}">
			          			 style="background-color: #EEF7E8"
			        		</c:when>					       
						</c:choose> >
						<td style="width: 52px"><input type="checkbox" name="featureone"></td>
						<td style="width: 52px">${list.name}</td>							
						<td style="width: 52px ; display: none" >${list.f_idx}</td>
						<td style="width: 172px">${list.featuretype}</td>
						<td style="width: 68px">
							<div class="mapcss">
								<i class="fas fa-map-marker-alt"></i>
							</div>
						</td>
						<td style="display: none">${list.name}</td>
					</tr>
				  </c:forEach>
			  </table>
			</div>
			<div class="features_search_div">
				<form action="${path}/" method="GET" style="maring: 0;">
					<div class="feature_search_keyword">
						<input type="text" id="feature_search_input" name="keyword"
							placeholder="Please enter a name to search" value="${map.keyword}"
							style="width: 81.2%">
						<button type="button" class="search_input"
							onclick="searchinput_box();" style="width: 58px;">Search</button>
					</div>
				</form>
			</div>
			<div class="pagination">
				<c:if test="${map.pager.curBlock > 1}">
					<button class="featurepage_btn" value="${path}/featureset&curPage=${map.pager.blockBegin-10}"
						onclick="featureaddress(this);">&laquo;</button>
					<button class="featurepage_btn" value="${path}/featureset&curPage=1" onclick="featureaddress(this);">1</button>
					<span>...</span>
				</c:if>
				<c:forEach var="num" begin="${map.pager.blockBegin}" end="${map.pager.blockEnd}">
					<c:choose>
						<c:when test="${num == map.pager.curPage}">
							<button class="featurepage_btn"value="${path}/featureset&curPage=${num}"
								    onclick="featureaddress(this);">${num}</button>
						</c:when>
						<c:otherwise>
							<button class="featurepage_btn" value="${path}/featureset&curPage=${num}" onclick="featureaddress(this);">${num}</button>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<c:if test="${map.pager.curBlock < map.pager.totBlock}">
					<span>...</span>
					<button class="featurepage_btn"
						value="${path}/featureset&curPage=${map.pager.totPage}"
						onclick="featureaddress(this);">${map.pager.totPage}</button>
					<button class="featurepage_btn"
						value="${path}/featureset&curPage=${map.pager.blockEnd + 1}"
						onclick="featureaddress(this);">&raquo;</button>
				</c:if>
			</div>
		</div>
		<div class="xywrap">
			<div class="feature_lat"></div>
			<div class="feature_lon"></div>
		</div>
		
		<div class="featruedetail"></div>
	<div id="page-loading">
	    <i class="fa fa-spinner fa-pulse fa-3x fa-fw margin-bottom"></i>    
	</div>
<!-- The Modal -->
	<div id="myModal"  >
		<!-- Modal content -->
		<div class="modal-content" >
			<span class="close">&times;</span>
			<div class="create_feature_warp">
				<p style="text-align: center; font-size: 32px;">Create Feature</p>
				<div class="new_name">
					<div class="div_wrap_box">Name</div>
					<input type="text" name="" placeholder="Name" style="width: 287px;" id="objectName">
				</div>
				<div class="new_Type">
					<div class="div_wrap_box">Type</div>
					<select id="new_type_option" style="width: 287px;">
						<option value="Light_buoy" selected="selected">Light buoy</option>
						<option value="Light_beacon">Light beacon</option>
						<option value="Light_house">Light house</option>
						<option value="Buoy">Buoy</option>
						<option value="Beacon">Beacon</option>
					</select>
				</div>
				<div class="type_box_select">
					<div class="new_Landmark_Color">
						<div class="div_wrap_box" >Structure Colour</div>
						<select name="new_Landmark_Color_select" style="width: 286px;"id="landmar_coclor" >
							<option value="null"></option>
							<option value="1">Green</option>
							<option value="2">Red</option>
							<option value="3">Red with one broad green horizontal band</option>
							<option value="4">Green with one broad red horizontal band</option>
							<option value="5">Black above yellow</option>
							<option value="6">Black with a single broad horizontal yellow band</option>
							<option value="7">Yellow above black</option>
							<option value="8">Yellow with a single broad horizontal black band</option>
							<option value="9">Black with one or more broad horizontal red bands</option>
							<option value="10">Red and white vertical stripes</option>
							<option value="11">Yellow</option>
						</select>
					</div>
					<div class="light_hous_Color" style="display: none;">
						<div class="div_wrap_box">Structure Colour</div>
						<select name="light_hous_structures" style="width: 286px;" id="lighthos_sturcutre_coclor">
							<option value="null"></option>
							<option value="1">white</option>
							<option value="2">black</option>
							<option value="3">red</option>
							<option value="4">green</option>
							<option value="5">blue</option>
							<option value="6">yellow</option>
							<option value="7">grey</option>
							<option value="8">brown</option>
							<option value="9">amber</option>
							<option value="10">violet</option>
							<option value="11">orange</option>
							<option value="12">magenta</option>
							<option value="13">pink</option>
						</select>
					</div>
					<div class="lighthouse_pattern" style="display: none;">
						<div class="div_wrap_box">Colour pattern</div>
						<select name="light_hous_pattern" style="width: 286px;" id="light_hos_coclor">
							<option value="null"></option>
							<option value="1">horizontal stripes</option>
							<option value="2">vertical stripes</option>
							<option value="3">diagonal stripes</option>
							<option value="4">squared</option>
							<option value="5">stripes (direction unknown)</option>
							<option value="6">border stripe</option>
							<option value="7">single colour</option>
							<option value="8">rectangle</option>
							<option value="9">triangle</option>
						</select>
					</div>
					<div class="catagory_Special_Purpos" style="display: none;">
						<div class="div_wrap_box" >Category of special purpose mark</div>
						<select name="Special_Purpos" style="width: 286px; height: 21px; margin-top: 11px;" id="Special_Purpos">
							<option value="firing_danger_mark">firing danger mark</option>
							<option value="target_mark">target mark</option>
							<option value="marker_ship_mark">marker ship mark</option>
							<option value="degaussing_range_mark">degaussing range mark</option>
							<option value="barge_mark">barge mark</option>
							<option value="cable_mark">cable mark</option>
							<option value="spoil_ground_mark">spoil ground mark</option>
							<option value="outfall_mark">outfall mark</option>
							<option value="ODAS">ODAS</option>
							<option value="recording">recording mark</option>
							<option value="seaplane_anchorage_mark">seaplane anchorage mark</option>
							<option value="recreation_zone_mark">recreation zone mark</option>
							<option value="private_mark">private mark</option>
							<option value="mooring_mark">mooring mark</option>
							<option value="LANBY">LANBY</option>
							<option value="leading_mark">leading mark</option>
							<option value="measured_distance_mark">measured distance mark</option>
							<option value="notice_mark">notice mark</option>
							<option value="tss_mark">TSS mark</option>
							<option value="anchoring_prohibited_mark">anchoring prohibited mark</option>
							<option value="berthing_prohibited_mark">berthing prohibited mark</option>
							<option value="overtaking_prohibited_mark">overtaking prohibited mark</option>
							<option value="two-way_traffic_prohibited_mark">two-way traffic prohibited mark</option>
							<option value="reduced_wake_mark">“reduced wake” mark</option>
							<option value="speed_limit_mark">speed limit mark</option>
							<option value="stop _mark">stop mark</option>
							<option value="general_warning_mark">general warning mark</option>
							<option value="sound_ships_siren_mark">“sound ships siren” mark</option>
							<option value="restricted_vertical_clearance_mark">restricted vertical clearance mark</option>
							<option value="maximum_vessel’s_draught_mark">maximum vessel's draught mark</option>
							<option value="restricted_horizontal_clearance_mark">restricted horizontal clearance mark</option>
							<option value="strong _urrent_warning_mark">strong current warning mark</option>
							<option value="berthing_permitted_mark">berthing permitted mark</option>
							<option value="overhead_power_cable_mark">overhead power cable mark</option>
							<option value="channel_edge_gradient_mark">“channel edge gradient” mark</option>
							<option value="telephone_mark">telephone mark</option>
							<option value="ferry_crossing_mark">ferry crossing mark</option>
							<option value="pipeline_mark">pipeline mark</option>
							<option value="anchorage_mark">anchorage mark</option>
							<option value="clearing_mark">clearing mark</option>
							<option value="control_mark">control mark</option>
							<option value="diving_mark">diving mark</option>
							<option value="refuge_beacon">refuge beaon</option>
							<option value="foul_ground_mark">foul ground mark</option>
							<option value="yatching_mark">yatching mark</option>
							<option value="heliport_mark">heliport mark</option>
							<option value="GNSS_mark">GNSS mark</option>
							<option value="seaplane_lading_mark">seaplane landing mark</option>
							<option value="entry_prohibited_mark">entry prohibited mark</option>
							<option value="work_in_progress_mark">work in progress mark</option>
							<option value="mark_with_unknown_purpose">mark with unknown purpose</option>
							<option value="wellhead_mark">wellhead mark</option>
							<option value="channel_separation_mark">channel separation mark</option>
							<option value="marine_farm_mark">marine farm mark</option>
							<option value="artificial_reef_mark">artificial reef mark</option>
							<option value="jetski_prohibited">jetski prohibited</option>
						</select>
					</div>
					<div class="buoy_shape">
						<div class="div_wrap_box">Buoy shape</div>
						<select name="buoy_select" style="width: 286px;" id = "buoy_select">
							<option value="Conical">conical (nun, ogival)</option>
							<option value="Can">can (cylindrical)</option>
							<option value="Spherical">spherical</option>
							<option value="Pillar">pillar</option>
							<option value="Spar">spar (spindle)</option>
							<option value="Barrel(tun)">barrel (tun)</option>
							<option value="Super-buoy">super-buoy</option>
							<option value="Ice">ice buoy</option>
						</select>
					</div>
					<div class="beacon_shape" style="display: none">
						<div class="div_wrap_box">Beacon shape</div>
						<select name="beacon_select" style="width: 286px;" id="beacon_select">
							<option value="Stake">stake, pole, perch, post</option>
							<option value="Withy">withy</option>
							<option value="Beacon">beacon tower</option>
							<option value="Lattice">lattice beacon</option>
							<option value="Pile">pile beacon</option>
							<option value="Cairn">cairn</option>
							<option value="Buoyant">buoyant beacons</option>
						</select>
					</div>
					<div class="Installation_Date">
						<div class="div_wrap_box">Installation Date</div>
						<input type="date" name="" placeholder="Installation Date" style="width: 286px;" id="Installation">
					</div>
					<div class="new_Postion">
						<div>
							<div class="div_wrap_box">Position</div>
							<div style = "display: flex; justify-content: center;">
								<button class="getMapBtn">Select from Map</button>
							</div>
						</div>
						<div class="postion_selection">						
							<div class="new_lat">
								<div class="" style="width: 27px;">Lat</div>
								<input type="text" name="lat" id= lat1> 
								<input type="text" name="lat" id= lat2> 
								<input type="text" name="lat" id= lat3> 
								<select id="new_type_NPostion" style="width: 37px;">
									<option value="N">N</option>
									<option value="S">S</option>
								</select>
							</div>
							<div class="new_Lot">
								<div class="" style="width: 27px;">Lon</div>
								<input type="text" name="lot" id= lot1> 
								<input type="text" name="lot" id= lot2>
								<input type="text" name="lot" id= lot3> 
								<select id="new_type_SPostion">
									<option value="E">E</option>
									<option value="W">W</option>
								</select>
							</div>
						</div>
					</div>
					<div class="Category_of_landmark" style="display: none;">
						<div class="div_wrap_box">Category of landmark</div>
						<select name="Category_of_light" style="width: 286px;" id="Category_of_light">
							<option value="null"></option>
							<option value="1">caim</option>
							<option value="2">cemetery</option>
							<option value="3">chimney</option>
							<option value="4">dish aerial</option>
							<option value="5">flagstaff (flagpole)</option>
							<option value="6">flare stack</option>
							<option value="7">mast</option>
							<option value="8">windsock</option>
							<option value="9">monument</option>
							<option value="10">column (pillar)</option>
							<option value="11">memorial plaque</option>
							<option value="12">obelisk</option>
							<option value="13">statue</option>
							<option value="14">cross</option>
							<option value="15">dome</option>
							<option value="16">radar scanner</option>
							<option value="17">tower</option>
							<option value="18">windmill</option>
							<option value="19">windmotor</option>
							<option value="20">spire/minaret</option>
							<option value="21">large rock or boulder on land</option>
						</select>
					</div>
					<div class="visually" style="display: none;">
						<div class="ligth_hosbox">Visually conspicuous</div>
						<input type="checkbox" name="visually" id="visually" value="HTML">
					</div>
					<div class="tompmarker_info" style="display: flex;">
						<div class="div_wrap_box">Topmark</div>
						<input type="checkbox" name="chk_info" value="HTML">
					</div>
					<div class="topmrn" style="display: none;">
						<div class="Topmark_MRN" >
							<div class="div_wrap_box">Topmark MRN</div>
							<input type="text" name="" placeholder="urn:mrn:iala:aton:kr:1234" style="width: 286px;" id="Topmark">
						</div>
					</div>
					<div class="new_Light_characteristic">
						<div class="div_wrap_box">Light Characteristic</div>
						<select id="new_Light" style="width: 286px;" name="new_Light">
							<option value="null"></option>
							<option value="fixed">fixed</option>
							<option value="flashing">flashing</option>
							<option value="long-flashing">long-flashing</option>
							<option value="quick-flashing">quick-flashing</option>
							<option value="very_quick_flashing ">very quick flashing</option>
							<option value="ultra_quick_flashing">ultra quick flashing</option>
							<option value="isophased">isophased</option>
							<option value="occulting">occulting</option>
							<option value="interrupted_quick_flashing">interrupted quick flashing</option>
							<option value="interrupted_very_quick_flashing">interrupted very quick flashing</option>
							<option value="interrupted_ultra_quick_flashing">interrupted ultra quick flashing</option>
							<option value="morse">morse</option>
							<option value="fixed/flash">fixed/flash</option>
							<option value="flash/long-flash">flash/long-flash</option>
							<option value="occulting/flash">occulting/flash</option>
							<option value="fixed/long-flash">fixed/long-flash</option>
							<option value="occulting alternating">occulting alternating</option>
							<option value="long-flash_alternating">long-flash alternating</option>
							<option value="flash_alternating">flash alternating</option>
							<option value="quick_flash">quick-flash plus long-flash</option>
							<option value="very_quick_flash_plus">very quick-flash plus long-flash</option>
							<option value="ultra_quick_flash_plus">ultra quick-flash plus long-flash</option>
							<option value="alternating">alternating</option>
							<option value="fixed_and_alternating">fixed and alternating flashing</option>
						</select>
					</div>
					<div class="loght_color">
						<div class="div_wrap_box">Light color</div>
						<select name="light_color_option" style="width: 286px;" id="Category_of_light">
							<option value="null"></option>
							<option value="1">green</option>
							<option value="2">white</option>
							<option value="3">reds</option>
						</select>
					</div>
					<div class="Signal_Group">
						<div class="div_wrap_box">Signal Group</div>
						<input type="text" name="" placeholder="Ex) (6)(1), (1)(2+3), (2)(1), etc."
							style="width: 286px;" id="Group">
					</div>
					<div class="Signal Period">
						<div class="div_wrap_box">Signal Period</div>
						<input type="text" name="" placeholder="Ex) 4, 8, etc."
							style="width: 286px;" id="Period">
					</div>
					<div class="Signal Period">
						<div class="div_wrap_box">Signal Sequence</div>
						<input type="text" name="" placeholder="Ex) 0.80+(2.20)+0.80+(5.20) etc."
							style="width: 286px;" id="Sequence">
					</div>
					<div class="AtoN_MRN">
						<div class="div_wrap_box">AtoN MRN</div>
						<input type="text" name="" placeholder="urn:mrn:iala:aton:kr:1234" style="width: 286px;" id="AtoN">
					</div>
					<div class="Buoy_MRN" style="display: flex">
						<div class="div_wrap_box">Buoy MRN</div>
						<input type="text" name="" placeholder="urn:mrn:iala:aton:kr:1234 style="width: 286px;" id="Buoy">
					</div>
					<div class="Beacon_MRN"  id="Beacon_MRN " style="display: none;">
						<div class="div_wrap_box">Beacon MRN</div>
						<input type="text" name="" placeholder="Beacon MRN" style="width: 286px;" id="Buoy">
					</div>
					<div class="Light_MRN" >
						<div class="div_wrap_box">Light MRN</div>
						<input type="text" name="" placeholder="urn:mrn:iala:aton:kr:1234" style="width: 286px;" id="Light_Mrn">
					</div>
					<div class="feature_btn_wrap_submit">
						<button id="create_clear">Create</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js" type="text/javascript"></script>
<script src="${path}/resources/JS/totalcript.js"></script>

<script src="${path}/resources/JS/Featuredatalist.js"></script>
<script type="text/javascript">
</script>

</html>