var lastDataID = -1;

/* Dataset tr clcik 시 체크박스 */
/* feturebox */
var id = [];
var myTableArray = [];
var markers = []; // markers 와 id를 같이 담고 이는 객체, 배열 
var xy; // featue 정보 담고있는 전역변수

// marker가찍힐때 해당되는 num을 기억
for (var m = 0; m < markerlist.length; m++) {
	id.push(markerlist[m].num);
	console.log( id);
}
	
$(".new_data_close").on('click', function() {
	$(".modal_new").css('display','none')
});

$(".create_dataset").on('click', function() {
	$(".modal_new").css('display','block')
});




/* 페이지 이동시에도 체크 박스 유지 */
$("#datasetlist tr").each( function() {
	
	var tableData = $(this).find('td:eq(1)');

	if (tableData.length > 0) {
		 /*테이블 한행씩을 담는다  */
		tableData.each(function() {
			for (var d = 0; d < id.length; d++) {
				var testput = id[d];
				if ($(this).text() == testput) {
                    $(this).parent().find('td:first-child :checkbox').prop("checked", true);
                    $(this).parent().find('td:eq(2)').addClass('active');
				}
			}
		});
	}
});

var details; 
/* 검색어 */
function inputbox(curPage) {
	var search = $.trim($('#data_search').val())
	dataset_table(curPage, search);
}

/* pagenation */
function address(testput) {
	var txt = testput.value;
	var test = txt.split('=');
	var curPage = test[1];
	inputbox(curPage);
}

/*페이지 이동하거나, 검색어 입력한 결과에 맞는 dataset 목록  */
function dataset_table(curPage, search) {
	$.ajax({
		type : "get",
		data : {
			'curPage' : curPage,
			'keyword' : search
		},
		async : false,
		url : "dataset",
		success : function(result) {
			
			$(".dataset_table").html(result);
		}
	})
}

/* 업로드 버튼 */
$('.Upload').click(function() {
	$(".modal_create_name").css('display','block')
	
	//$('.upload_gml').click();
});


$(".modal_create_name_close").on('click', function() {
	$(".modal_create_name").css('display','none')
});




/* dataset 삭제 */
$(".delete_gml").on("click", function() {
	delinput();
});

/* dataset save 버튼 */
$(".save_gml").on("click", function() {
	saveinput();
});

/*  validation 버튼*/
var name; /*dataset Name  */
var num; /*dataset id  */
var deldata;
var count = null;

// 체크박스에 체크된 dataset을 확인하여 배열에 담는 함수
function checkcs() {
	var checkpoint = $("input:checkbox[name=checkOne]:checked")
	count = checkpoint.length;
	deldata = new Array();
	checkpoint.each(function(i) {
		var tr = checkpoint.parent().parent().eq(i);
		var td = tr.children();
		deldata.push(td.eq(1).text());
	});
}


/*dataset upload  */

var form;
var file;
var formData;
var uploadnmae;
$('.create_input_name').on('click', function() {
	var uploadnmae  = $('.datauploadname').val();
	var newname;
	if(uploadnmae == '') {
		alert("data upload name")
		return false
	}
	if(file == null) {
		alert("Choose the data to upload")
	}	
	if(file.indexOf(".gml") != -1){
		newname = uploadnmae + ".gml"
	} else if (file.indexOf(".xlsx") != -1 ) {
		newname = uploadnmae + ".xlsx"
	}
	
	formData = new FormData(form);
	formData.append('file', $("#inputFile")[0].files[0], newname);
	
	$('#page-loading').css('display', 'table');
	$.ajax({
		url : 'filauploadSet',
		data : formData,
		datatype : "text",
		processData : false,
		contentType : false,
		type : 'POST',
		success : function(data) {
			if (data == "fail") {
			    alert("error")
	        	$('#page-loading').css('display', 'none');
	            return;
			}			
			alert("success")
			$('#page-loading').css('display', 'none');
			dataset_table();
        }, 
        error: function() {
			alert("error")
        	$('#page-loading').css('display', 'none');
        }
	})
	
});
$('.Select_input_name').on('click', function() {
	$('.upload_gml').click();	
});


function uploadForm() {
	form = $('#fileForm')[0];
	file = $('#fileForm [name="inputFile"]').val(); 
	var stream = file.slice(file.indexOf(".") +1 ).toLowerCase();
	if(file != null ) {
		 if (file.indexOf(".gml") == -1 && file.indexOf(".xlsx") == -1) {
		        alert("This format is not supported. (Use gml or xlsx)");
		        return false;
		}
	}
	var fileValue = file.split("\\");
	var fileName = fileValue[fileValue.length-1]; // 파일명
	$(".selectname").text("Selected file name : " + fileName);

}

/*google map 사이즈 변경*/
function mapsize(){
	var totalw = window.innerWidth - 590
    var mapContainer = document.getElementById('map');
	mapContainer.style.width = totalw + 'px';
	mapContainer.style.left = 590;
}

/* dataset 삭제 function */
function delinput() {
	
	// 선택된 dataset이있는지 확인하는 함수
	checkcs();
	
	
	var delconfirm = confirm( 'Do you really want to delete it?' );
	if(delconfirm) {
		if (count == null) {
			alert("Select the data to be deleted");
		} else {	
			$('#page-loading').css('display', 'table');
			
			if (deldata.length > 0) {
				$.ajax({
					url : "deldate",
					data : {
						'checkarr' : deldata
					},
					type : "post",
					success : function(result) {
						for(i=0 ; i<deldata.length; i++) {
							makermover(deldata[i])
						}	
						for(i=0 ; i<deldata.length; i++) {
							if(deldata[i] == on_num){
								$(".feature_table").css("display" , "none")									
								 mapsize()
							} 
						}	
						alert("Data deletion is complete")
						if($(".feature_table").css("display") == "block"){
							$('#page-loading').css('display', 'none');
						}					
						dataset_table();
					}
				});
			}
		}
		
	} else {
		return;
	}
}

/* dataset save function */
function saveinput() {
	checkcs();
	$.ajax({
		url : "dataset-zip",
		data : {
			'array' : deldata
		},
		type : "post",
		success : function(result) {
			window.location = "fileDownload.do?name=" + result;
			dataset_table();
		}
	});
}


/*validation function  */
function valiinput() {
	var tableString = "<tr style=\"background: white; height: 2.8em;\" class=\"checktable allcheck\"><th>Feature ID</th><th>Message</th></tr>";
	$('#tablefunction').children().html(tableString);
	$.ajax({
		url : "dataset-valid",
		data : {
			'id' : lastDataID
		},
		type : "post",
		success : function(result) {
			setValidationResult(result);
		}
	});
}

function setValidationResult(resultObject) {
	for (var i = 0; i < resultObject.length; i++) {
		var id = resultObject[i]["id"];
		var message = resultObject[i]["err"][1];
		var record = "<tr class=\"checktable\"><td>" + id + "</td>"+ "<td>" + message + "</td></tr>";
		$('#tablefunction').append(record);
	}
}

/*비어있는 dataset 생성  */
$('.create_input').on('click', function() {
	var create = $('.dataname').val();
	if (create == '') {
		alert("Enter the data to be created")
	} else {
		$.ajax({
			url : "create",
			data : {
				"create" : create
			},
			type : "post",
			success : function(resule) {
				alert("Data creation is complete");
				dataset_table();
			}
		});
	}
});






$(".testtd").dblclick(function(e) {
	e.stopPropagation();  
	var currentEle = $(this);
	var num = $(this).parent().find('td:eq(1)').text().trim(); 
	var value = $(this).html();
	updateVal(currentEle, value, num);
});

function updateVal(currentEle, value, num) {
	var oldvalue = value.trim();

	$(currentEle)
			.html(
					'<input class="thVal" style="width: 100%; height: 100%; outline: none;font-size: 16px; type="text" value="'
							+ value + '" />');

	var thVal = $(".thVal");
	thVal.focus();
	
	// 수정 사항 입력후 enter 입력시
	thVal.keyup(function(event) { 
		if (event.keyCode == 13) {
			$(currentEle).html(thVal.val().trim());
			var newname = $(currentEle).text();
			// dataset의 이름이 변경되었을 경우만 
			if (oldvalue != newname) { 
				save(newname, num);
			}
		}
	});
	
	// 수정 사항 입력후 다른곳으로 마우스 click
	thVal.focusout(function() {
		$(currentEle).html(thVal.val().trim());
		var newname = $(currentEle).text();
		if (oldvalue != newname) {
			return save(newname, num);
		}
	});
}

//수정된 dataset 이름을 저장하는 함수
function save(newname, num) {
	$.ajax({
		url : "update",
		data : {
			"newname" : newname,
			"num" : num
		},
		type : "post",
		success : function(resule) {
			dataset_table();
		}
	});
}

/*체크박스 직접 클릭시 진행되는 함수  */
function makermover(removernum) {
	for (var m = 0; m < markerlist.length; m++) { // marker 제거 
		var marker = markerlist[m].markers;
		var markernumber = markerlist[m].num;	
		if (markernumber == removernum) {
			delete markerlist[m].num 
			for (var mn = 0; mn < marker.length; mn++) {
				marker[mn].setMap(null);
			} 
		} 
	}
}

var putmarkeridx;
// event.target.nodeName이 INPUT이어서 아무 로직도 동작 안
$('.click_table tr').click(function(event) {
	var allcheck
	var checkbox;

	if (event.target.nodeName.toLowerCase() == 'td') {
	
		checkbox = $(this).find('td:first-child :checkbox');
		checkbox.prop('checked', !checkbox.is(':checked'));
		if (checkbox.is(":checked") == false) {
			$("input[name=checkAll]").prop("checked", false);
			$(this).find('td:eq(2)').removeClass('active');
			var removernum = $(this).find('td:eq(1)').text().trim();
			makermover(removernum);						
			if(femarker !=null) {
				femarker.setMap(null);
			}
			if(addmarker != null ) {
				addmarker.setMap(null);
			}
			
//			if($(".feature_table").css("display") == "block"){
//				$(".feature_table").css("display" , "none")									
//				 mapsize()	
//			}		
			
		} else if (checkbox.is(":checked") == true) {
			$(this).find('td:eq(2)').addClass('active');
			num = $(this).find('td:eq(1)').text().trim();
			putmarkeridx = $(this).find('td:eq(1)').text().trim();
			name = $(this).find('td:eq(2)').text().trim();
			
			// google map fitbounds 함수 
			//fitbound(num);
			// marker그리는 함수
			datamarkerlist(num);			
		}
	} 
});

//on/off 버튼 index 부여
$('.on_off').each(function(index) { 
	$(this).attr('menu-index', index);
})

//on/off 버튼 클릭시 feautre 상세 화면 활성화 함수
var onfoffindex;
$('.on_off').on('click', function() {
	onfoffindex = $(this).attr('menu-index'); 
	$('#page-loading').css('display', 'table');
	on_check =   $(this).parent().parent().find('td:eq(0) :checkbox');
	on_num = $(this).parent().parent().find('td:eq(1)').text().trim();
	on_name = $(this).parent().parent().find('td:eq(2)').text().trim();
	var textid = "geo"
	
	datamarkerlist(on_num, textid);
	 
	var len = document.getElementsByClassName("on_off").length;
	document.getElementsByClassName("on_off")[onfoffindex].classList.toggle("menutomato");
	$(".feature_table").css("display", "block");
	var totalw = window.innerWidth - 970
    var mapContainer = document.getElementById('map');
    mapContainer.style.width = totalw + 'px';
    mapContainer.style.left = 970;
    mtitle = "-1";
	
    featurelist(on_num, on_name, mtitle);
		
	if($(this).is(".menutomato") === false) {
		$(".feature_table").css("display", "none");
		if(femarker != null ) {
			femarker.setMap(null);
		}				
		//지도사이즈를 다시 작게		
		mapsize()
	}	
	for (var i = 0; i < len; i++) {
		if (i != onfoffindex) {
			if(femarker != null ) {
				femarker.setMap(null);
			}
			document.getElementsByClassName("on_off")[i].classList.remove("menutomato");
		}
	}
	
});


$("#checkAll").click(function() {
	if($("#checkAll").is(":checked")){
		$('.checkSelect').prop('checked', 'checked');
	} else {
		$('.checkSelect').prop('checked',  false);
	}
});

$('.checkSelect').click(function() {
	$("#checkAll").prop('checked',  false);
	if($(this).is(":checked") == true){
		$(this).parent().parent().find('td:eq(2)').addClass('active');
		num = $(this).parent().parent().find('td:eq(1)').text()		
		var funname = "marker";
		//marker 정보 불러오는 함수 totalcript.js 에 있음
		// 실행후 makerMake()함수 실행  
		datamarkerlist(num, funname);
	} else {
		$(this).parent().parent().find('td:eq(2)').removeClass('active');
		$(this).find('td:eq(2)').removeClass('active');
		var removernum = $(this).parent().parent().find('td:eq(1)').text()
		makermover(removernum);
	}
});
// 
/* maker 생성 함수 */
var markerOrigin; // markerimg 좌표
var markerImg;  // marker 이미지
var addmarker = null;
var makeType = null;
function makerMake(data) {
	
	var list = [];
	var result = data.list
    var feautIdnum = data.num
    var bounds = new google.maps.LatLngBounds();
	
	if (result != "") {
		if(markers.length !=0 ){
			markers = [];
		}			
		for (var i = 0; i < result.length; i++) {			
			if (result[i].featuretype != null && result[i].featuretype != '') {
				markerImg = " ";
				markerOrigin = new google.maps.Point(0, 0);				
				 // featuretype 값에따라 markerImg 변경해주는 함수
				imgvalue(result[i]); 
			}									
			var marker = new google.maps.Marker ({
				title : ""+result[i].f_idx+"",
				position : new google.maps.LatLng(
						result[i].y, result[i].x),
				map : map,
				icon : {
					url : markerImg,
					anchor : markerOrigin,
					scaledSize : new google.maps.Size(25,25)
				},
				disableDoubleClickZoom : true
			});						
			//마커가 찍힌 부분으로 화면 확대
			bounds.extend(marker.position);			
			markers.push(marker);									
			var pnumarker;					
			// 구글멥에 있는 marker 클릭시 상세정보표출			
			google.maps.event.addListener(marker, 'click', (function(marker, i) {				
				return function() {
					var mtitle = marker.title;
					if(addmarker !=null) {
						addmarker.setMap(null);
						addmarker = null;
					}					
		        	addmarker  = new google.maps.Marker ({
	        		    num : feautIdnum,
	        		    position : new google.maps.LatLng(result[i].y, result[i].x),
						map : map
		        	});			   	
		        	var data_ds_dix = document.getElementsByClassName("data_ds_dix");		        	
		        	for (var s=0; s<data_ds_dix.length; s++) {
		        	    if(data_ds_dix[s].innerText == putmarkeridx){
		        			data_ds_dix[s].parentElement.children[5].childNodes[0].classList.add("menutomato");
		        		} else {
		        			data_ds_dix[s].parentElement.children[5].childNodes[0].classList.remove("menutomato");
		        		}
		        	}		        	
			        var textid = "geo"
			        datamarkerlist(putmarkeridx, textid);
			        featurelist(putmarkeridx, name, mtitle);
			        $(".feature_table").css("display", "block");			        	
			        $.ajax({
						type : "post",
						data : {
							'featureid' : result[i].f_idx,
						},
						async : false,
						url : "featureDetail",
						success : function(data) {
							$(".featruedetail").html(data);
						}
					});
			    }
			})  (marker, i));
		}  
		// google map에 마커들 위치를 반영하여 해당영역으로 화면 재설정		
        list = {
            num : feautIdnum,
            markers : markers
        };
		markerlist.push(list);
		
		if(makeType == "feature") {
			makeType = null;
		} else {
			map.fitBounds(bounds);	
		}
		
    }
}
// featuretype 값에따라 markerImg 변경해주는 함수
function imgvalue(result) {	
	if (result.featuretype.indexOf("Light") > -1) {
		markerOrigin = new google.maps.Point(-7, -15);		
		if (result.value == "red") {
			markerImg = 'resources/img/img/LIGHTS113.svg';
		} else if (result.value == "yellow") {
			markerImg = 'resources/img/img/LIGHTS111.svg';
		} else {
			markerImg = 'resources/img/img/LIGHTS112.svg';
		}
	} else if (result.featuretype.indexOf("Landmark") > -1) {
		markerImg = 'resources/img/img/TOWERS03.svg';
	} else if (result.featuretype.indexOf("BuoyLateral") > -1) {
		if (result.value == "can") {
			markerImg = 'resources/img/img/BuoyLateralCan.png';
		} else if (result.value == "pillar") {
			markerImg = 'resources/img/img/BuoyLateralPillar.png';
		} else if (result.value == "concicall") {
			markerImg = 'resources/img/img/BuoyLateralConcicallr.png';
		} else {
			markerImg = 'resources/img/img/BuoyLateralPillar.png';
		}
	} else if (result.featuretype.indexOf("BuoyCardinal") > -1) {
		markerImg = 'resources/img/img/BuoyCardinal.png';
	} else if (result.featuretype.indexOf("BuoySpecialPurposeGeneral") > -1) {
		markerImg = 'resources/img/img/BuoySpecialPurposeGeneral.png';
	} else if (result.featuretype.indexOf("BuoyIsolatedDanger") > -1) {
		markerImg = 'resources/img/img/BuoyIsolatedDanger.png';
	} else if (result.featuretype.indexOf("BuoySafeWater") > -1) {
		markerImg = 'resources/img/img/BuoySafeWater.png';
	} else if (result.featuretype.indexOf("BuoyInstallation") > -1) {
		markerImg = 'resources/img/img/BuoyInstallation.png';
	} else if (result.featuretype.indexOf("BeaconLateral") > -1) {
		markerImg = 'resources/img/img/BeaconLateral.png';
	} else if (result.featuretype.indexOf("BeaconCardinal") > -1) {
		markerImg = 'resources/img/img/BeaconCardinal.png';
	} else if (result.featuretype.indexOf("BeaconSpecialPurposedGeneral") > -1) {
		markerImg = 'resources/img/img/BeaconSpecialPurposedGeneral.png';
	} else if (result.featuretype.indexOf("BeaconIsolatedDanger") > -1) {
		markerImg = 'resources/img/img/BeaconIsolatedDanger.png';
	} else if (result.featuretype.indexOf("BeaconSafeWater") > -1) {
		markerImg = 'resources/img/img/BeaconSafeWater.png';
	}
	else if (result.featuretype.indexOf("LightFloat") > -1) {
		markerImg = 'resources/img/img/LightFloat.png';
	} else if (result.featuretype.indexOf("Pile") > -1) {
		markerImg = 'resources/img/img/Pile.png';
	} else if (result.featuretype.indexOf("Lighthouse") > -1) {
		markerImg = 'resources/img/img/Lighthouse.png';
	} else if (result.featuretype.indexOf("OffShorePlatform") > -1) {
		markerImg = 'resources/img/img/OffShorePlatform.png';
	} else if (result.featuretype.indexOf("LightVessel") > -1) {
		markerImg = 'resources/img/img/LightVessel.png';
	} else if (result.featuretype.indexOf("SiloTank") > -1) {
		markerImg = 'resources/img/img/SiloTank.png';
	} else if (result.featuretype.indexOf("RadarReflector") > -1) {
		markerImg = 'resources/img/img/RadarReflector.png';
	} else if (result.featuretype.indexOf("FogSignal") > -1) {
		markerImg = 'resources/img/img/FogSignal.png';
	} else if (result.featuretype.indexOf("Daymark") > -1) {
		markerImg = 'resources/img/img/Daymark.png';
	} else if (result.featuretype.indexOf("PhysicalAISAidToNavigation") > -1) {
		markerImg = 'resources/img/img/PhysicalAISAidToNavigation.png';
	} else if (result.featuretype.indexOf("VirtualAISAidToNavigation") > -1) {
		markerImg = 'resources/img/img/VirtualAISAidToNavigation.png';
	} else if (result.featuretype.indexOf("SynteticAISAidToNavigation") > -1) {
		markerImg = 'resources/img/img/SynteticAISAidToNavigation.png';
	} else if (result.featuretype.indexOf("RadarBeacon") > -1) {
		markerImg = 'resources/img/img/RadarBeacon.png';
	} else if (result.featuretype.indexOf("LORAN-C") > -1) {
		markerImg = 'resources/img/img/LORAN-C.png';
	} else if (result.featuretype.indexOf("RadioStation") > -1) {
		markerImg = 'resources/img/img/DRFSTA01.svg';
	} else if (result.featuretype.indexOf("DLoran") > -1) {
		markerImg = 'resources/img/img/DLORAN01.svg';
	} else if (result.featuretype.indexOf("ELoran") > -1) {
		markerImg = 'resources/img/img/ELORAN01.svg';
	} else if (result.featuretype.indexOf("Object") > -1) {
		markerImg = 'http://maps.gstatic.com/mapfiles/ridefinder-images/mm_20_red.png';
	}
	
}
	
	//# sourceURL=Dataset.js