var map;
var button = null;
var myCity;
var markerlist = [];

function initMap() {
    map = new google.maps.Map(document.getElementById("map"), {
        center: new google.maps.LatLng(36.584657, 127.891846),
        zoom: 8,
        disableDoubleClickZoom: true,
        mapTypeControlOptions: {
            style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
            position: google.maps.ControlPosition.TOP_RIGHT
        }
    });
    
//    var imageMapType = new google.maps.ImageMapType({
//         getTileUrl: function (coord, zoom) {
//             return "http://theprost8004.iptime.org:58780/tiles/" + zoom + "/" + coord.x + "/" + coord.y + ".png"
//         },
//         tileSize: new google.maps.Size(256, 256)
//    });
//    map.overlayMapTypes.push(imageMapType);
        
    pointMap();
}

function pointMap() {
    google.maps.event.addListener(map, 'mousemove', function (e) {
        if (button == null)
            return;
        if (button == "Line") {
            moveLine(e.latLng);
            return false;
        } else if (button == "Rect") {
            moveRect(e.latLng);
            return false;
        } else if (button == "Poly") {
            movePoly(e.latLng);
            return false;
        }
    });

    google.maps.event.addListener(map, 'click', function (e) {
        if (button == null)
            return;
        if (button != null) {
            if (button == "Point") {
                circlePoint(e.latLng);
                return false;
            } else if (button == "Line") {
                createLine(e.latLng);
                return false;
            } else if (button == "Rect") {
                createRect(e.latLng);
            } else if (button == "Poly") {
                createPoly(e.latLng);
                return false;
            }
        }
    });

    google.maps.event.addListener(map, 'dblclick', function (e) {
        if (button == null)
            return;
        if (button == "Line") {
            addLine(e.latLng);
        } else if (button == "Rect") {
            createRect(e.latLng);
        } else if (button == "Poly") {
            addPoly(e.latLng);
            return false;
        }
        return;
    });
}

