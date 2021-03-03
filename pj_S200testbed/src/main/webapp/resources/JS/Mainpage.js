$.fn.dataTable.ext.errMode = 'throw';
	

//googl map 사이즈 옵션
function resizeMap() {
	var totalw = window.innerWidth - 590			
	var mapContainer = document.getElementById('map');
	mapContainer.style.width = totalw + 'px';
	mapContainer.style.left = 590;
    
}

/*사이드 menu 토글  */
function myFunction(x) {
	var len = document.getElementsByClassName("dropdown-content").length;
	document.getElementsByClassName("dropdown-content")[x].classList.toggle("show");
	document.getElementsByClassName("dropbtn")[x].classList.toggle("csstestev");

	resizeMap();

	if (document.getElementsByClassName("dropdown-content")[x].getAttribute('id') != 'dataset') {
		$('.feature_table').css('display', 'none');
	} 	
	if(document.getElementsByClassName("show").length == 0) {		
		var mapContainer = document.getElementById('map');
		mapContainer.style.width = '100%';
		mapContainer.style.left = '0';
	}
	for (var i = 0; i < len; i++) {
		if (i != x) {
			document.getElementsByClassName("dropdown-content")[i].classList.remove("show");			
			document.getElementsByClassName("dropbtn")[i].classList.remove("csstestev");		
			$('.feature_table').css('display', 'none');
		}
	}
}

	
	
// 설명 : point, line 등 버튼클릭시  변경색상 유지 
var div2 = document.getElementsByClassName('searchbtn_css');

for (var i = 0; i < div2.length; i++) {
  div2[i].addEventListener('click', function(){
    for (var j = 0; j < div2.length; j++) {
      div2[j].style.background = "#fff";
      div2[j].style.color = "#0F263D";
    }
    this.style.background = "#0F263D";
    this.style.color = "#fff";
  })
}

	
 
$('.databtn').on('click', function() {
	dataset_table();
});

// Dataset list 불러오는 함수	
function dataset_table() {
	$.ajax({
		type : "get",
		async : false,
		url : "dataset",
		success : function(result) {			
			$(".dataset_table").html(result);
		}
	});
}

var datasetmarker = null;
var tempArea = null;
var temObj = null;
var table;
var lines = [];
var rects = [];
var polys = [];
var button = '';
var newArray = [];
var detail ;
var datanumarray = []; /* dataset에서 checkbox checked  되어있는 행 */
var numarray = {}; /* ajax 에 보내기 위해  checked 된 num 값 을 push */
var buttonbox = []; /* click 한 button 둘울 한번에 담음  */
var searchGeometry = null;

// 설명 : Search 패널의 Point, Line, Rect, Poly, Clear 버튼을 누르면 실행
$('.testset2 div').on('click', function() {
	button = $(this).text().trim();
	buttonbox.push(button);
});
	
// 설명 : Search 패널 옵션 변경시 goolemap에 그려진 marker 삭제

$('#clearbtn').on('click', function() {
	table.clear().draw();
	if (searchGeometry != null) {
		searchGeometry.setMap(null);		
		for (var s = 0; s < gmarkers.length; s++) {
			gmarkers[s].setMap(null);
		}
		searchGeometry = null;
	}
});

/* datatable library 설정 */	
var table = $("#serach_table_wrap").DataTable({
	data : [],
	columns : [ 
	 { data : "id"},
	 { data : "name" },
	 { data : "featureType"}
	
	],
	rowCallback : function(row, data) {},
	filter : false,
	info : false,
	dom :   'rtp',
	ordering : false,
	processing : true,
	 language: {
		    paginate: {
		      next: '>', // or '→'
		      previous: '<' // or '←' 
		    }
		  },
	autoWidth : false,
	columnDefs : [ {
		targets : 0,
		width : "25%"
	}, {
		targets : 1,
		width : "15%"
	}, {
		targets : 2,
		width : "15%"
	}]
});

	
function numset() {
	var red = document.getElementsByClassName("active");
	for (var i = 0; i < red.length; i++) {
		var price = red[i].parentElement.childNodes[3].innerText;
		datanumarray.push(price); /* datset table에서 dataset 을 선택했는지 유무 판단 */
		numarray[i] = price; /* ajax 에 보내기 위해 배열에 push */
	}
}

// 설명 : 검색 지오메트리 마커와 검색 결과 지오메트리 마커를 초기화
function initSearchGeometry() {
	if (searchGeometry != null) {
		searchGeometry.setMap(null);
		for (var s = 0; s < gmarkers.length; s++) {
			gmarkers[s].setMap(null);
		}
		searchGeometry = null;
	}
}
	
/* 써클 */
// 설명 : Point Search 모드로 포인트 클릭하면 실행되는 함수 
// 인자 : lat(), lon() 함수를 사용할 수 있는 위경도 객체
// 반환 : 실패하면 false, 성공하면 반환 안함
// 비고 : 성공하면 true를 반환하도록 수정 필요해 보임 
function circlePoint(latLng) {
	if (button != "Point") {
		return false;
	}
	numset();
	if (datanumarray.length == 0) {
		alert("No data selected ")
	} else {
		initSearchGeometry();
		searchGeometry = new google.maps.Circle({
			strokeColor : '#337ab7',
			strokeOpacity : 0.8,
			strokeWeight : 0.5,
			fillColor : '#337ab7',
			fillOpacity : 0.35,
			map : map,
			center : {
				lat : latLng.lat(),
				lng : latLng.lng()
			},
			radius : 10000,
			visible : true,
			clickable : false
		});

		$.ajax({
			url : 'searchbtn/point',
			method : 'post',
			dataType : 'json',
			data : {
				"point" : latLng.lng() + ' ' + latLng.lat(),
				"numarray" : numarray
			}
		}).done(function(data) {
			datasetmarker = data;
			markerplus();
			table.clear().draw();
			table.rows.add(data).draw();
		})
	}
};

/* line */
// 설명 : Line Search 모드로 첫번째 점을 클릭하면 실행되는 함수 
// 인자 : lat(), lon() 함수를 사용할 수 있는 위경도 객체
// 반환 : 실패하면 false, 성공하면 반환 안함
// 비고 : 성공하면 true를 반환하도록 수정 필요해 보임 
function createLine(latLng) {
	numset();
	if (button != "Line") {
		return false;
	}
	numset();
	if (datanumarray.length == 0) {
		alert("No data selected ")
	} else {
		if (tempArea == null) {
			tempArea = [ {
				lat : latLng.lat(),
				lng : latLng.lng()
			}, {} ];

			tempObj = new google.maps.Polyline({
				path : tempArea,
				geodesic : true,
				strokeColor : '#337ab7',
				strokeOpacity : 0.8,
				strokeWeight : 1,
				visible : true,
				clickable : false
			});
			tempObj.setMap(map);
		} else {
			tempArea[tempArea.length - 1] = {
				lat : latLng.lat(),
				lng : latLng.lng()
			};
			tempObj.setPath(tempArea);
			tempArea[tempArea.length] = {};
		}
	}
};

// 설명 : Line Search 모드로 두 번째 점부터 클릭하면 실행되는 함수 
// 인자 : lat(), lon() 함수를 사용할 수 있는 위경도 객체
// 반환 : 실패하면 false, 성공하면 반환 안함
// 비고 : 성공하면 true를 반환하도록 수정 필요해 보임 
function moveLine(latLng) {
	if (button != "Line") {
		return false;
	} else {
		if (tempArea != null && tempArea.length > 0) {
			tempArea[tempArea.length - 1] = {
				lat : latLng.lat(),
				lng : latLng.lng()
			};
			tempObj.setPath(tempArea);
		}
	}
}

// 설명 : Line Search 모드로 마지막 점을 추가하면 실행되는 함수 
// 인자 : lat(), lon() 함수를 사용할 수 있는 위경도 객체
// 반환 : 실패하면 false, 성공하면 반환 안함
// 비고 : 성공하면 true를 반환하도록 수정 필요해 보임 
function addLine(latLng) {
	if (button != "Line") {
		return false
	}
	initSearchGeometry();			
	tempArea.splice(tempArea.length - 1, 1);
	searchGeometry = new google.maps.Polyline({
		path : tempArea,
		geodesic : true,
		map : map,
		strokeColor : '#337ab7',
		strokeOpacity : 0.8,
		strokeWeight : 1,
		visible : true,
		clickable : false
	});

	/* 객체에 담긴 좌표를 ajax 를 통해 전달하여 받도록 데이터 수정  */
	tempArea.forEach(function(i) {
		var x = i['lat'];
		var y = i['lng'];
		var xy = y + ' ' + x;
		newArray.push(xy);
	});
	var polylist = newArray.join(',');
	$.ajax({
		url : 'searchbtn/point',
		method : 'post',
		dataType : 'json',
		data : {
			'numarray' : numarray,
			'polylist' : polylist
		}
	}).done(function(data) {
		datasetmarker = data;
		markerplus();
		table.clear().draw();
		table.rows.add(data).draw();
		newArray = [];
	})
	tempObj.setMap(null);
	tempObj = null;
	tempArea = null
};

/*Rect  */
// 설명 : Rect Search 모드로 중간 점을 추가하면 실행되는 함수 
// 인자 : lat(), lon() 함수를 사용할 수 있는 위경도 객체
// 반환 : 실패하면 false, 성공하면 반환 안함
// 비고 : 성공하면 true를 반환하도록 수정 필요해 보임 
function moveRect(latLng) {
	if (button != "Rect") {
		return false;
	} else {
		if (tempArea != null && tempArea.length > 0) {
			tempArea[2] = {
				lat : latLng.lat(),
				lng : latLng.lng()
			};
			tempArea[1] = {
				lat : tempArea[0].lat,
				lng : tempArea[2].lng
			};
			tempArea[3] = {
				lat : tempArea[2].lat,
				lng : tempArea[0].lng
			};
			tempObj.setPath(tempArea);
		}
	}
}

// 설명 : Line Search 모드로 첫 번째 점을 추가하면 실행되는 함수 
// 인자 : lat(), lon() 함수를 사용할 수 있는 위경도 객체
// 반환 : 실패하면 false, 성공하면 반환 안함
// 비고 : 성공하면 true를 반환하도록 수정 필요해 보임 
function createRect(latLng) {
	if (button != "Rect") {
		return false;
	}
	numset();
	if (datanumarray.length == 0) {
		alert("No data selected ")
	} else {
		if (tempArea == null) {

			tempArea = [ {
				lat : latLng.lat(),
				lng : latLng.lng()
			}, {}, {}, {} ];
			tempObj = new google.maps.Polygon({
				path : tempArea,
				strokeColor : "#337ab7",
				strokeOpacity : 0.8,
				strokeWeight : 0.5,
				fillColor : "#337ab7",
				fillOpacity : 0.4,
				visible : true,
				clickable : false
			});
			tempObj.setMap(map);
		} else {
			addRect(latLng);
		}
	}
}

// 설명 : Rect Search 모드로 마지막 점을 추가하면 실행되는 함수 
// 인자 : lat(), lon() 함수를 사용할 수 있는 위경도 객체
// 반환 : 실패하면 false, 성공하면 반환 안함
// 비고 : 성공하면 true를 반환하도록 수정 필요해 보임 
function addRect(latLng) {
	initSearchGeometry();
	searchGeometry = new google.maps.Polygon({
		path : tempArea,
		map : map,
		strokeColor : "#337ab7",
		strokeOpacity : 0.8,
		strokeWeight : 0.5,
		fillColor : "#337ab7",
		fillOpacity : 0.4,
		visible : true,
		clickable : false
	});
	tempArea.forEach(function(i) {
		var x = i['lat'];
		var y = i['lng'];
		var xy = y + ' ' + x;
		newArray.push(xy);
	});
	newArray.push(newArray[0]);
	var rectlist = newArray.join(',');
	$.ajax({
		url : 'searchbtn/point',
		method : 'post',
		dataType : 'json',
		data : {
			'numarray' : numarray,
			'rectlist' : rectlist
		}
	}).done(function(data) {
		datasetmarker = data;
		markerplus();
		table.clear().draw();
		table.rows.add(data).draw();
		newArray = [];
	})
	tempObj.setMap(null);
	tempObj = null;
	tempArea = null;
};

/*  Poly*/
function movePoly(latLng) {
	if (button != "Poly") {
		return false;
	}
	if (tempArea != null && tempArea.length > 0) {
		tempArea[tempArea.length - 1] = {
			lat : latLng.lat(),
			lng : latLng.lng()
		};
		tempObj.setPath(tempArea);
	}
}
function createPoly(latLng) {
	if (button != "Poly") {
		return false;
	}
	numset();
	if (datanumarray.length == 0) {
		alert("No data selected ")
	} else {
		if (tempArea == null) {
			tempArea = [ {
				lat : latLng.lat(),
				lng : latLng.lng()
			}, {} ];
			tempObj = new google.maps.Polygon({
				path : tempArea,
				strokeColor : "#337ab7",
				strokeOpacity : 0.8,
				strokeWeight : 0.5,
				fillColor : "#337ab7",
				fillOpacity : 0.4,
				visible : true,
				clickable : false
			});
			tempObj.setMap(map);
		} else {
			tempArea[tempArea.length - 1] = {
				lat : latLng.lat(),
				lng : latLng.lng()
			};
			tempObj.setPath(tempArea);
			tempArea[tempArea.length] = {};
		}
	}
}

function addPoly(latLng) {
	if (searchGeometry != null) {
		searchGeometry.setMap(null);
		for (var s = 0; s < gmarkers.length; s++) {
			gmarkers[s].setMap(null);
		}
		searchGeometry = null;
	}
	tempArea.splice(tempArea.length - 1, 1);
	searchGeometry = new google.maps.Polygon({
		path : tempArea,
		map : map,
		strokeColor : "#337ab7",
		strokeOpacity : 0.8,
		strokeWeight : 0.5,
		fillColor : "#337ab7",
		fillOpacity : 0.4,
		visible : true,
		clickable : false
	});
	tempArea.forEach(function(i) {
		var x = i['lat'];
		var y = i['lng'];
		var xy = y + ' ' + x;
		newArray.push(xy);
	});
	newArray.push(newArray[0]);
	var polygone = newArray.join(',');
	$.ajax({
		url : 'searchbtn/point',
		method : 'post',
		dataType : 'json',
		data : {
			'numarray' : numarray,
			'polygone' : polygone
		}
	}).done(function(data) {
		datasetmarker = data;
		markerplus();
		table.clear().draw();
		table.rows.add(data).draw();
		newArray = [];
	})
	tempObj.setMap(null);
	tempObj = null;
	tempArea = null
}

var gmarkers = [];
// 설명 : Geometry Search 결과를 구글맵에 마커로 추가 
function markerplus() {	
	if (datasetmarker != null) {
		for (var i = 0; i < datasetmarker.length; i++) {
			var marker = new google.maps.Marker({
				position : new google.maps.LatLng(datasetmarker[i].y,
						datasetmarker[i].x),
				map : map,
				disableDoubleClickZoom : true
			});	
			gmarkers.push(marker);
		}
	}
}