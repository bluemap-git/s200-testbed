var on_num;
var on_name;
var makemarker;
var on_check;
var femarker;

// markerclick
function featurelist(num, name, mtitle) {
    lastDataID = num;
    document.getElementById('page-loading').style.display = "table";
    $.ajax({
        type: "get",
        data: {
            'num': num,
            'name': name,
            'mtitle': mtitle,
        },
        async: false,
        url: "featureset",
        success: function (result) {
            $(".feature_table").html(result);
            document.getElementById('page-loading').style.display = "none";
        }
    });
}

//marker 생성
function datamarkerlist(num, textid) {
    $.ajax({
        url: "search",
        data: { "num": num },
        type: "post",
        async: false,
        dataType: "json",
        success: function (data) {
            if (textid == "geo") {
                xy = data.list;
                makemarker = data
            } else {
                makerMake(data);
            }
        }
    });
}
//# sourceURL=total.js