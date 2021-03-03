
var num = $
{
    map.num
};

var name = $('.feature_type_name').text(); // feature id
var curPage; // 현제 페이지
var featureDelArray = []; /* checked 된 feature id를 담는 배열 */
var search = "";

/* feature data load 할 데이터를 담는 함수 */
function featureaddress(featuretest) {
    var txt = featuretest.value;
    var test = txt.split('=');
    curPage = test[1];
    searchinput_box();
}

/* feautre type 검색어를 받아서 ajax로 넘겨주는 함수 */
function searchinput_box() {
    var search = $.trim($('#feature_search_input').val())
    setfeaturelist(lastDataID, name, curPage, search)
}

/* feature data load 할 데이터를 처리하는 함수 */
function setfeaturelist(num, name, curPage, search) {

    var mtitle = "-1";
    $.ajax({
        type: "get",
        data: {
            'num': num,
            'name': name,
            'curPage': curPage,
            'keyword': search,
            'mtitle': mtitle
        },
        async: false,
        url: "featureset",
        success: function (result) {
            $(".feature_table").html(result);
            // initAttributeTable();
        }
    });
}

/* feaure 상세정보 클릭시 수정에 필요한 데이터를 받는 함수 */
$('#feature_detail ').on('dblclick', '.my_class', function (e) {
    var featureTd = $(this)
    var featureTdhtml = $(this).html();
    var FeaturePkid = $(this).closest('tr').find('td:eq(0)').text();
    updatFeatureval(featureTd, featureTdhtml, FeaturePkid);
});

/* feaure 상세정보 클릭시 수정에 inputbox 활성화 함수 */
function updatFeatureval(featureTd, featureTdhtml, FeaturePkid) {

    var oldvalue = featureTdhtml.trim();
    $(featureTd).html('<input class="thVal" style="width: 100%; height: 100%; outline: none;font-size: 16px; type="text" value="' + featureTdhtml + '" />');
    var thVal = $(".thVal");
    thVal.focus();
    thVal.keyup(function (event) { // 수정 사항 입력후 enter 클릭
        if (event.keyCode == 13) {
            $(featureTd).html(thVal.val().trim());
            var newFeaturvalue = $(featureTd).text();
            if (oldvalue != newFeaturvalue) { // 변경값이 있는 경우만
                Featuresave(newFeaturvalue, FeaturePkid);
            }
        }
    });
    thVal.focusout(function () {// 수정 사항 입력후 다른곳으로 마우스 click
        $(featureTd).html(thVal.val().trim());
        var newFeaturvalue = $(featureTd).text();
        if (oldvalue != newFeaturvalue) {
            return Featuresave(newFeaturvalue, FeaturePkid);
        }
    });
}


/* feaure 상세정보 수정된 데이터를 담아 ajax 로 처리하는 함수 */
function Featuresave(newFeaturvalue, FeaturePkid) { // ajax
    $.ajax({
        url: "DeatilUpdate",
        data: {
            "newFeaturvalue": newFeaturvalue,
            "FeaturePkid": FeaturePkid
        },
        type: "post",
        success: function (resule) {
            alert("Modification is complete");
        }
    });
}


$(".feturedata_table tr").each(function (index) {
    $(this).children('td:nth-child(5)').attr('feature-index', index);
})

var thisid;
$('.feturedata_table tr').click(function (event) {
    var lonx;
    var laty;
    thisid = $(this).find('td:eq(2)').text().trim();
    
    var cssnumber = $(this).children('td:nth-child(5)').attr('feature-index');    
    var testresult = cssnumber -1;
    var len = document.getElementsByClassName("mapcss").length;
    document.getElementsByClassName("mapcss")[testresult].classList.toggle("menutomato");
    
    for (var i = 0; i < len; i++) {
        if (i != testresult) {
            document.getElementsByClassName("mapcss")[i].classList.remove("menutomato");
        }
    }
    if (xy != "") {
        $.each(xy, function (key, value) {
            if (value.f_idx == thisid) {
                lonx = value.x
                laty = value.y
                return false;
            }
        });
    }
    ddToDms(laty, lonx)
    featureDetail(thisid);
    
    var moveLatLon = new google.maps.LatLng(laty, lonx);
    if (femarker != null) {
        femarker.setMap(null);
    }
    femarker = new google.maps.Marker({
        position: moveLatLon,
        map: map,        
    });
    
    map.setCenter(moveLatLon, 9);
    
    if ($(this).children('td:nth-child(5)').children().hasClass('menutomato') == true) {
    } else {
        if (femarker != null) {
            femarker.setMap(null);
        }
    }
});

function featureDetail(featureid, on_num) {
    /* feature data 상세정보 표출하는 ajax */
    $.ajax({
        type: "post",
        data: {
            'featureid': featureid,
        },
        async: false,
        url: "featureDetail",
        success: function (data) {
        
            $(".featruedetail").html(data);
        }
    });
}

// 위도 경도로 변경
var latResult, lngResult, dmsResult;
var getmapType// create Feature 일때만 
function ddToDms(lat, lng, getmapType) {

   var lat = lat;
   var lng = lng;
    
   lat = parseFloat(lat);
   lng = parseFloat(lng);
        
   var resultType;
   
   var latResultns = (lat >= 0) ? 'N' : 'S';
   var getDmslat = getDms(lat)
   latResult = getDmslat + " " + latResultns;

   var lngResultew = (lng >= 0) ? 'E' : 'W';
   var getDmslng = getDms(lng)
   lngResult = getDmslng + " " + lngResultew;
    
    
    if(getmapType == null) {
	   $('.feature_lat').text("Lat : " + latResult);
	   $('.feature_lon').text("Lon : " + lngResult);
    } else {
    	var getmaplat = getDmslat.split('-')
    	$('#lat1').val(getmaplat[0]);
    	$('#lat2').val(getmaplat[1]);
    	$('#lat3').val(getmaplat[2]);
    	var getmaplng = getDmslng.split('-')
    	$('#lot1').val(getmaplng[0]);
    	$('#lot2').val(getmaplng[1]);
    	$('#lot3').val(getmaplng[2]);
    	if(latResultns == "N" || latResultns == "S") {    	
    		if(latResultns == "N") {
    			$('#new_type_NPostion option:eq(0)').prop("selected", true)
    		} else {
    			$('#new_type_NPostion option:eq(1)').prop("selected", true)
    		}    		
    	} 
    	if (lngResultew == "E" || lngResultew == "W"){
    		if(lngResultew == "E") {
    			$('#new_type_SPostion option:eq(0)').prop("selected", true)
    		} else {
    			$('#new_type_SPostion option:eq(1)').prop("selected", true)
    		}      		
    	}    	
    }
}

function getDms(val) {

    var valDeg, valMin, valSec, result;
    val = Math.abs(val);

    valDeg = Math.floor(val); // d
    result = valDeg + "-";

    valMin = Math.floor((val - valDeg) * 60); // 분
    result += valMin + "-";

    valSec2 = ((val - valDeg - valMin / 60) * 3600) * 1000 / 1000;  // 초
    var varnum = valSec2.toString();
    var testnumval = varnum.substring(0, varnum.indexOf('.') + 4)
    valSec = Number(testnumval);
    result += valSec;
    
    return result;
}

// validation button
$(".validation_gml").on("click", function () {
    valiinput();
});

/* validation function */
function valiinput() {

    var tableString = "<tr style=\"background: white; height: 2.8em;\" class=\"checktable allcheck\"><th>Feature ID</th><th>Message</th></tr>";
    $('#tablefunction').children().html(tableString);
  
    $.ajax({
        url: "dataset-valid",
        data: {
            'id': lastDataID
        },
        type: "post",
        success: function (result) {
            setValidationResult(result);
        }
    });
}

function featuredelarr() {
    $('input[name=featureone]:checked').each(function () {
        var status = $(this).parent().parent().find('td:eq(2)').text();
        featureDelArray.push(status);
    });
}

/* feature data 삭제 button */
$("#feature_del").on('click', function () {

    featuredelarr();
 var delconf = confirm( 'Do you really want to delete it?' );
    if(delconf) {
	 if (featureDelArray.length == "0") {
	        alert("Choose your data")
	        return false;
	    } else {
		 $.ajax({
	            type: "post",
	            data: { 'featureDelArray': featureDelArray },
	            url: "featureDelete",
	            success: function () {
	            	alert("Select the data to be deleted");
	                var mtilte = "-1";
	                featurelist(on_num, on_name, mtilte);
	                var textid = "geo";
	                datamarkerlist(on_num, textid)
			    featureDelArray = [];
	            }
	       });
	    }
    } else {
    	  return false;
    }
});

/* modal */
var AtoN;
var testList = new Array();
var obj = new Object();

obj.DataSetID = -1;
obj.AtoNNumber = ""

$("#feature_create_btn").on('click', function () {
    $("#myModal").css('display', 'block');
   
});

$(".close").on('click', function () {
    $("#myModal").css('display', 'none');
    
    if(getmapclick !=null) {
    	getmapclick.remove();
    }
    
    if(getMapmarker != null) {
    	getMapmarker.setMap(null)
    }
});



$("#new_type_option").change(function () {
    var thios = $(this).val();
    $("#landmar_coclor option:eq(0)").prop("selected", true);
    $(".catagory_Special_Purpos").css('display', 'none')

    if (thios != "Light_buoy") {
        $('.buoy_shape').css('display', 'none');
    } else if (thios = "Light_buoy") {
        $('.buoy_shape').css('display', 'flex');
        $('.Buoy_MRN').css('display', 'flex');
        $('.Beacon_MRN').css('display', 'none');
        $('.new_Light_characteristic').css('display', 'flex');
        $('.Signal_Group').css('display', 'flex');
        $('.Period').css('display', 'flex');
        $('.Light_MRN').css('display', 'flex');
    }

    if (thios == "Light_beacon") {
        $('.beacon_shape').css('display', 'flex');
        $('.Buoy_MRN').css('display', 'none');
        $('.Beacon_MRN').css('display', 'flex');
        $('.new_Light_characteristic').css('display', 'flex');
        $('.Signal_Group').css('display', 'flex');
        $('.Period').css('display', 'flex');
        $('.Light_MRN').css('display', 'flex');
    } else if (thios != "Light_beacon") {
        $('.beacon_shape').css('display', 'none');
    }

    if (thios != "Light_house") {
        $('.light_hous_Color').css('display', 'none');
        $('.new_Landmark_Color').css('display', 'flex');
        $('.light_hous_Color').css('display', 'none');
        $('.lighthouse_pattern').css('display', 'none');
        $('.Category_of_landmark').css('display', 'none');
        $('.visually').css('display', 'none');
        $('.loght_color').css('display', 'none');
        $('.tompmarker_info').css('display', 'flex');
    } else if (thios == "Light_house") {
        $('.tompmarker_info').css('display', 'none');
        $('.new_Landmark_Color').css('display', 'none');
        $('.light_hous_Color').css('display', 'flex');
        $('.lighthouse_pattern').css('display', 'flex');
        $('.Category_of_landmark').css('display', 'flex');
        $('.visually').css('display', 'flex');
        $('.new_Light_characteristic').css('display', 'flex');
        $('.Signal_Group').css('display', 'flex');
        $('.Period').css('display', 'flex');
        $('.Light_MRN').css('display', 'flex');
        $('.loght_color').css('display', 'flex');
    }

    if (thios == "Buoy") {
        $('.new_Light_characteristic').css('display', 'none');
        $('.Signal_Group').css('display', 'none');
        $('.Period').css('display', 'none');
        $('.Buoy_MRN').css('display', 'flex');
        $('.Beacon_MRN').css('display', 'none');
        $('.Light_MRN').css('display', 'none');
    }

    if (thios == "Beacon") {
        $('.new_Light_characteristic').css('display', 'none');
        $('.Signal_Group').css('display', 'none');
        $('.Period').css('display', 'none');
        $('.Buoy_MRN').css('display', 'none');
        $('.Beacon_MRN').css('display', 'flex');
        $('.Light_MRN').css('display', 'none');
    }
});

// testcssbtn
$('.featurepage_btn').on('click', function () {
    $(this).css('color', 'red');
});

//google map을 클릭하여 좌표를 얻어오는 함수
var getmapclick 
var getMapmarker
$('.getMapBtn').on('click', function(){
	 $("#myModal").css('display', 'none');
	 
	 	getmapclick = map.addListener( 'click', function(e) {  
		var mapBtnlag = e.latLng.lat();
		var mapBtnlng = e.latLng.lng();
		
		if(getMapmarker != null) {
			getMapmarker.setMap(null);
		}
		
		getMapmarker = new google.maps.Marker({
		        position: { lat:mapBtnlag, lng: mapBtnlng}, 
		        map: map
		    });
		 
		getmapType = "getmap"
		var result = ddToDms(mapBtnlag, mapBtnlng, getmapType)
		if(mapBtnlag != null) {
			 $("#myModal").css('display', 'block');
		}
	});
});





$('input[name="chk_info"]').change(function () {
    var value = $(this).val();
    var checked = $(this).prop('checked');
    if (checked) {
        checktru = "1";
        $(".topmrn").css('display', 'block')
    } else {
        checktru = null;
        $(".topmrn").css('display', 'none')
    }
});

$('#landmar_coclor').change(function () {
    if ($("#new_type_option").val() != "Light_house") {
        if ($(this).val() == "11") {
            $(".catagory_Special_Purpos").css('display', 'flex')
            if ($("#new_type_option option:checked").text() == "Buoy") {
                $(".buoy_shape").css('display', 'flex')
            }
            if ($("#new_type_option option:checked").text() == "Beacon") {
                $(".beacon_shape").css('display', 'flex')
            }
        } else {
            $(".catagory_Special_Purpos").css('display', 'none')
            $(".light_hous_Color").css('display', 'none')
        }
    }
});

var pattern = [];/* colourPattern */
var categoryOfLight = []; /* categoryOfLight */
var geo; /* geometry */
var colors = []; /* color */
var checktru;
$("#create_clear").on('click', function () {

    $("#myModal").css('display', 'none')
    document.getElementById('page-loading').style.display = "table";
    obj.DataSetID = lastDataID;
    obj.AtoNNumber = $("#AtoN").val();
    var landcolor = $("#landmar_coclor").val(); // Structure Color
    var slic = landcolor.split(",");
    var color = $("#new_Color_select").val();
    var featruetype = $("#new_type_option option:checked").text(); // 타입
    pattern.push("horizontal stripes");

    var memeberArray = new Array();
    if (featruetype == "Light house") {
        var member = new Object();
        member.Type = "Landmark";
        member.idCode = $("#Buoy").val();
        member.categoryOfLandmark = $("#Category_of_light option:checked").text();
        member.installationDate = $("#Installation").val();
        if ($("input:checkbox[name=visually]").is(":checked") == true) {
            member.visuallyConspicuous = "visually conspicuous";
        } else {
            member.visuallyConspicuous = "not visually conspicuous";
        }
        member.colour = $("#lighthos_sturcutre_coclor option:checked").text();
        member.objectName = $("#objectName").val();
        geometry();
        member.geometry = geo;
        memeberArray.push(member)
        member = new Object();
        member.Type = "Light";
        member.idCode = $("#Light_Mrn").val();
        member.installationDate = $("#Installation").val();
        member.catagoryofLight = $("#Category_of_light option:checked").text();
        member.Lightcolour = $("#light_color_option option:checked").text();
        member.lightCharacteristic = $("#new_Light option:checked").text();
        member.signalGroup = $("#Group").val();
        member.signalPeriod = $("#Period").val();
        member.geometry = geo
        memeberArray.push(member);
    } else {
        var member = new Object();
        if (landcolor == 1) {
            if (featruetype == "Light beacon") {
                member.Type = "BeaconLateral";
                member.beaconShape = $("#beacon_select option:checked").text();
            } else if (featruetype == "Light buoy") {
                member.Type = "BuoyLateral";
                member.buoyShape = $("#buoy_select option:checked").text();
            } else if (featruetype == "Beacon") {
                member.Type = "BeaconLateral";
                member.beaconShape = "pile baecon";
            } else if (featruetype == "Buoy") {
                member.Type = "BuoyLateral";
                member.buoyShape = "can (cylindrical)";
            }
            member.idCode = $("#Buoy").val();
            member.categoryOfLateralMark = "port-hand lateral mark";
            member.installationDate = $("#Installation").val();
            member.colour = ["green"];

        } else if (landcolor == 2) {
            if (featruetype == "Light beacon") {
                member.Type = "BeaconLateral";
                member.beaconShape = $("#beacon_select option:checked").text();
            } else if (featruetype == "Light buoy") {
                member.Type = "BuoyLateral";
                member.buoyShape = $("#buoy_select option:checked").text();
            } else if (featruetype == "Beacon") {
                member.Type = "BeaconLateral";
                member.beaconShape = "pile baecon";
            } else if (featruetype == "Buoy") {
                member.Type = "BuoyLateral";
                member.buoyShape = "conical (nun, ogival)";
            }
            member.idCode = $("#Buoy").val();
            member.categoryOfLateralMark = "starboard-hand lateral mark";
            member.installationDate = $("#Installation").val();
            member.colour = ["red"];
        } else if (landcolor == 3) {
            if (featruetype == "Light beacon") {
                member.Type = "BeaconLateral";
                member.beaconShape = $("#beacon_select option:checked").text();
            } else if (featruetype == "Light buoy") {
                member.Type = "BuoyLateral";
                member.buoyShape = $("#buoy_select option:checked").text();
            } else if (featruetype == "Beacon") {
                member.Type = "BeaconLateral";
                member.beaconShape = "pile baecon";
            } else if (featruetype == "Buoy") {
                member.Type = "BuoyLateral";
                member.buoyShape = "conical (nun, ogival)";
            }
            member.idCode = $("#Buoy").val();
            member.categoryOfLateralMark = "preferred channel to port lateral mark";
            member.installationDate = $("#Installation").val();
            member.colour = ["red", "green", "red"]
        } else if (landcolor == 4) {
            if (featruetype == "Light beacon") {
                member.Type = "BeaconLateral";
                member.beaconShape = $("#beacon_select option:checked").text();
            } else if (featruetype == "Light buoy") {
                member.Type = "BuoyLateral";
                member.buoyShape = $("#buoy_select option:checked").text();
            } else if (featruetype == "Beacon") {
                member.Type = "BeaconLateral";
                member.beaconShape = "pile baecon";
            } else if (featruetype == "Buoy") {
                member.Type = "BuoyLateral";
                member.buoyShape = "can (cylindrical)";
            }
            member.idCode = $("#Buoy").val();
            member.categoryOfLateralMark = "preferred channel to starboard lateral mark";
            member.installationDate = $("#Installation").val();
            member.colour = ["green", "red", "green"]
        } else if (landcolor == 5) {
            if (featruetype == "Light beacon") {
                member.Type = "BeaconCardinal";
                member.beaconShape = $("#beacon_select option:checked").text();
            } else if (featruetype == "Light buoy") {
                member.Type = "BuoyCardinal";
                member.buoyShape = $("#buoy_select option:checked").text();
            } else if (featruetype == "Beacon") {
                member.Type = "BeaconCardinal";
                member.beaconShape = "pile baecon";
            } else if (featruetype == "Buoy") {
                member.Type = "BuoyCardinal";
                member.buoyShape = "pillar";
            }
            member.idCode = $("#Buoy").val();
            member.categoryOfCardinalMark = "north cardinal mark";
            member.installationDate = $("#Installation").val();
            member.colour = ["black", "yellow"]
        } else if (landcolor == 6) {
            if (featruetype == "Light beacon") {
                member.Type = "BeaconCardinal";
                member.beaconShape = $("#beacon_select option:checked").text();
            } else if (featruetype == "Light buoy") {
                member.Type = "BuoyCardinal";
                member.buoyShape = $("#buoy_select option:checked").text();
            } else if (featruetype == "Beacon") {
                member.Type = "BeaconCardinal";
                member.beaconShape = "pile baecon";
            } else if (featruetype == "Buoy") {
                member.Type = "BuoyCardinal";
                member.buoyShape = "pillar";
            }
            member.idCode = $("#Buoy").val();
            member.categoryOfCardinalMark = "east cardinal mark";
            member.installationDate = $("#Installation").val();
            member.colour = ["black", "yellow", "black"]
        } else if (landcolor == 7) {
            if (featruetype == "Light beacon") {
                member.Type = "BeaconCardinal";
                member.beaconShape = $("#beacon_select option:checked").text();
            } else if (featruetype == "Light buoy") {
                member.Type = "BuoyCardinal";
                member.buoyShape = $("#buoy_select option:checked").text();
            } else if (featruetype == "Beacon") {
                member.Type = "BeaconCardinal";
                member.beaconShape = "pile baecon";
            } else if (featruetype == "Buoy") {
                member.Type = "BuoyCardinal";
                member.buoyShape = "pillar";
            }
            member.idCode = $("#Buoy").val();
            member.categoryOfCardinalMark = "south cardinal mark";
            member.installationDate = $("#Installation").val();
            member.colour = ["yellow", "black"]
        } else if (landcolor == 8) {
            if (featruetype == "Light beacon") {
                member.Type = "BeaconCardinal";
                member.beaconShape = $("#beacon_select option:checked").text();
            } else if (featruetype == "Light buoy") {
                member.Type = "BuoyCardinal";
                member.buoyShape = $("#buoy_select option:checked").text();
            } else if (featruetype == "Beacon") {
                member.Type = "BeaconCardinal";
                member.beaconShape = "pile baecon";
            } else if (featruetype == "Buoy") {
                member.Type = "BuoyCardinal";
                member.buoyShape = "pillar";
            }
            member.idCode = $("#Buoy").val();
            member.categoryOfCardinalMark = "west cardinal mark";
            member.installationDate = $("#Installation").val();
            member.colour = ["yellow", "black", "yellow"]
        } else if (landcolor == 9) {
            if (featruetype == "Light beacon") {
                member.Type = "BeaconIsolatedDanger";
                member.beaconShape = $("#beacon_select option:checked").text();
            } else if (featruetype == "Light buoy") {
                member.Type = "BuoyIsolatedDanger";
                member.buoyShape = $("#buoy_select option:checked").text();
            } else if (featruetype == "Beacon") {
                member.Type = "BeaconIsolatedDanger";
                member.beaconShape = "pile baecon";
            } else if (featruetype == "Buoy") {
                member.Type = "BuoyIsolatedDanger";
                member.buoyShape = "pillar";
            }
            member.idCode = $("#Buoy").val();
            member.installationDate = $("#Installation").val();
            member.colour = ["black", "red", "black"]
        } else if (landcolor == 10) {
            if (featruetype == "Light beacon") {
                member.Type = "BeaconSafeWater";
                member.beaconShape = $("#beacon_select option:checked").text();
            } else if (featruetype == "Light buoy") {
                member.Type = "BuoySafeWater";
                member.buoyShape = $("#buoy_select option:checked").text();
            } else if (featruetype == "Beacon") {
                member.Type = "BeaconSafeWater";
                member.beaconShape = "pile baecon";
            } else if (featruetype == "Buoy") {
                member.Type = "BuoySafeWater";
                member.buoyShape = "spherical";
            }
            member.idCode = $("#Buoy").val();
            member.installationDate = $("#Installation").val();
            member.colour = ["red", "white"]
        } else if (landcolor == 11) {
            if (featruetype == "Light beacon" || featruetype == "Beacon") {
                member.Type = "BeaconSpecialPurposeGeneral";
                member.beaconShape = $("#beacon_select option:checked").text();
                member.secialpurpos = $("#Special_Purpos option:checked").text();
            } else if (featruetype == "Light buoy" || featruetype == "Buoy") {
                member.Type = "BuoySpecialPurposeGeneral";
                member.buoyShape = $("#buoy_select option:checked").text();
                member.secialpurpos = $("#Special_Purpos option:checked").text();
            }
            member.idCode = $("#Buoy").val();
            member.installationDate = $("#Installation").val();
            member.colour = ["yellow"]
        }
        geometry();
        member.objectName = $("#objectName").val();
        member.geometry = geo;
        memeberArray.push(member)

        if (checktru == "1") { // topmarker 체크 여부
            member = new Object();
            member.Type = "Topmark";
            member.idCode = $("#Topmark").val();
            member.installationDate = $("#Installation").val();
            if (landcolor == 1 || landcolor == 4) {
                member.topmarkDaymarkShape = "cylinder (can)";
                member.color = "green";
            } else if (landcolor == 2 || landcolor == 3) {
                member.topmarkDaymarkShape = "cone, point up";
                member.color = "red";
            } else if (landcolor == 5) {
                member.topmarkDaymarkShape = "2 cones (points upward";
                member.color = "black";
            } else if (landcolor == 6) {
                member.topmarkDaymarkShape = "2 cones, base to base";
                member.color = "black";
            } else if (landcolor == 7) {
                member.topmarkDaymarkShape = "2 cones (points downward)";
                member.color = "black";
            } else if (landcolor == 8) {
                member.topmarkDaymarkShape = "2 cones, point to point";
                member.color = "black";
            } else if (landcolor == 9) {
                member.topmarkDaymarkShape = "2 spheres";
                member.color = "black";
            } else if (landcolor == 10) {
                member.topmarkDaymarkShape = "sphere";
                member.color = "red";
            } else if (landcolor == 11) {
                member.topmarkDaymarkShape = "x-shape (St. Andrew's cross)";
                member.color = "yellow";
            }
            member.geometry = geo
            memeberArray.push(member);
        }

        if (featruetype == "Light beacon" || featruetype == "Light buoy") {
            member = new Object();
            member.Type = "Light";
            member.idCode = $("#Light_Mrn").val();
            member.installationDate = $("#Installation").val();
            if (landcolor == 1) {
                member.colour = ["green"];
            } else if (landcolor == 2) {
                member.colour = ["red"];
            } else if (landcolor == 3) {
                member.colour = ["red"]
            } else if (landcolor == 4) {
                member.colour = ["green"]
            } else if (landcolor == 11) {
                member.colour = ["yellow"]
            } else {
                member.colour = ["white"]
            }
            member.lightCharacteristic = $("#new_Light option:checked").text();
            member.signalGroup = $("#Group").val();
            member.signalPeriod = $("#Period").val();
            member.signalSequence = $("#Sequence").val();
            member.geometry = geo
            memeberArray.push(member);
        }
    }
    // var jsonData = JSON.stringify(memeberArray);
    obj.member = memeberArray;// jsonData;
    var jsonStr = JSON.stringify(obj);
    $.ajax({
        type: "post",
        data: {
            json: jsonStr
        },
        url: "create-feature",
        success: function (value) {
            memeberArray = null;
            alert('success');
            document.getElementById('page-loading').style.display = "none";
            $("#myModal").css('display', 'none')

            var mtilte = "-1";
            featurelist(on_num, on_name, mtilte);

            var textid = "geo";
            datamarkerlist(on_num, textid)
            
            makeType = "feature";
            makerMake(makemarker);
            on_check.prop("checked", true);
            
            if(getmapclick !=null) {
            	getmapclick.remove();
            }            
            if(getMapmarker != null) {
            	getMapmarker.setMap(null)
            }
        }
    });
});

/* 좌표 계산 fucntion */
function geometry() {

    var latValue = $("input[name='lat']").length;
    var lotValue = $("input[name='lot']").length;
    var latData = new Array(latValue);
    for (var i = 0; i < latValue; i++) {
        latData[i] = $("input[name='lat']")[i].value;
    }

    /* lat */
    var lat0 = latData[0];
    var lat1 = latData[1] / 60
    var lat2 = latData[2] / 3600;
    var plus = (lat1 + lat2).toFixed(6);
    var lats = parseInt(lat0) + parseFloat(plus)
    var lat = null;
    var lotData = new Array(lotValue);
    for (var i = 0; i < lotValue; i++) {
        lotData[i] = $("input[name='lot']")[i].value;
    }

    /* lang */
    var lot0 = lotData[0];
    var lot1 = lotData[1] / 60
    var lot2 = lotData[2] / 3600;
    var lantplus = (lot1 + lot2).toFixed(6);
    var lots = parseInt(lot0) + parseFloat(lantplus)
    var lot = null;
    var latoptions = $("#new_type_NPostion").val();
    if (latoptions == "S") {
        var lat = "-" + lats
    } else {
        var lat = lats
    }
    var latoptions = $("#new_type_SPostion").val();
    if (latoptions == "W") {
        var lot = "-" + lots
    } else {
        var lot = lots
    }
    geo = lot + "," + lat;
}

$(".featureclose").on('click', function () {
    $(".feature_table").css('display', 'none');
     document.getElementsByClassName("on_off")[onfoffindex].classList.remove("menutomato")
      if(addmarker != null) {
    	addmarker.setMap(null);
    }
    
    if (femarker != null) {
        femarker.setMap(null);
    }
    var totalw = window.innerWidth - 590
    var mapContainer = document.getElementById('map');
    mapContainer.style.width = totalw + 'px';
    mapContainer.style.left = 590;
});

$("#objectName").keyup(function () {
    var objectname = $(this).val().trim();
});


	//# sourceURL=Featuredatalist.js
