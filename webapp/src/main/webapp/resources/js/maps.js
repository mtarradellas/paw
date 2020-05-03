const Feature = ol.Feature;
const Map = ol.Map;
const View = ol.View;
const Overlay = ol.Overlay;
const Polyline = ol.format.Polyline;
const Point = ol.geom.Point;
const Transform = ol.proj.transform;
const Marker = ol.marker;
const {circular} = ol.geom.Polygon;
const {fromLonLat, toLonLat} = ol.proj;
const {toStringHDMS} = ol.coordinate;
const {Tile, Vector} = ol.layer;
const OSM = ol.source.OSM;
const VectorSource = ol.source.Vector;
const Control = ol.control.Control;
const {Circle, Fill, Icon, Stroke, Style} = ol.style;
const TileLayer = Tile;
const VectorLayer = Vector;
const CircleStyle = Circle;


let map = new Map({
    target: 'map',
    layers: [
        new TileLayer({
            source: new OSM()
        })
    ],
    //Starting in Capital Federal, the main target
    view: new View({
        center: fromLonLat([-58.381592, -34.603722]),
        zoom: 12
    })
});

const source = new VectorSource();
const layer = new VectorLayer({
    source: source
});
map.addLayer(layer);

navigator.geolocation.watchPosition(function(pos) {
    const coords = [pos.coords.longitude, pos.coords.latitude];
    const accuracy = circular(coords, pos.coords.accuracy);
    source.clear(true);
    source.addFeatures([
        new Feature(accuracy.transform('EPSG:4326', map.getView().getProjection())),
        new Feature(new Point(fromLonLat(coords)))
    ]);
}, function(error) {
    alert(`ERROR: ${error.message}`);
}, {
    enableHighAccuracy: true
});

const locate = document.createElement('div');
locate.className = 'ol-control ol-unselectable locate';
locate.innerHTML = '<button title="Locate me">◎</button>';
locate.addEventListener('click', function() {
    if (!source.isEmpty()) {
        map.getView().fit(source.getExtent(), {
            maxZoom: 20,
            duration: 500
        });
    }
});
map.addControl(new Control({
    element: locate
}));


//Drop a pin and search the address of place where user clicks
map.on('click', function(evt) {
    const coords = evt.coordinate;
    const point = new Point(coords);

    map.getLayers().getArray()
        .filter(layer => layer.get('name') === 'dropped pin')
        .forEach(layer => map.removeLayer(layer));

    map.addLayer(new VectorLayer({
        name: 'dropped pin',
        source: new VectorSource({
            features: [
                new Feature(point)
            ]
        }),
        style: new Style({
            image: new Circle({
                radius: 9,
                fill: new Fill({color: 'red'})
            })
        })
    }));


    //Find the users location

    const coords_click = Transform(evt.coordinate, 'EPSG:3857', 'EPSG:4326');
    console.log("Mouse Click coordinates: " + coords_click);

    const lon = coords_click[0];
    const lat = coords_click[1];

    const data_for_url = {lon: lon, lat: lat, format: "json", limit: 1};

    const encoded_data = Object.keys(data_for_url).map(function (k) {
        return encodeURIComponent(k) + '=' + encodeURIComponent(data_for_url[k])
    }).join('&');


    const url_nominatim = 'https://nominatim.openstreetmap.org/reverse?' + encoded_data;
    console.log("URL Request NOMINATIM-Reverse: " + url_nominatim);

    httpGet(url_nominatim, function (response_text) {

        const data_json = JSON.parse(response_text);

        const res_lon = data_json.lon;
        const res_lat = data_json.lat;

        const res_address = data_json.address;


        const address_display_name  = data_json.display_name;
        const address_country       = res_address.country;
        const address_country_code  = res_address.country_code;
        const address_postcode      = res_address.postcode;
        const address_state         = res_address.state;
        const address_town          = res_address.town;
        const address_city          = res_address.city;
        const address_city_district = res_address.city_district;
        const address_suburb        = res_address.suburb;
        const address_neighbourhood = res_address.neighbourhood;
        const address_footway       = res_address.footway;
        const address_house_number  = res_address.house_number;
        const address_pedestrian    = res_address.pedestrian;
        const address_road          = res_address.road;

        console.log("Longitude    : " + res_lon);
        console.log("Longitude    : " + res_lat);
        console.log("Name         : " + address_display_name);
        console.log("Country      : " + address_country);
        console.log("Count. Code  : " + address_country_code);
        console.log("Postcode     : " + address_postcode);
        console.log("State        : " + address_state);
        console.log("Town         : " + address_town);
        console.log("City         : " + address_city);
        console.log("City District: " + address_city_district);
        console.log("Suburb       : " + address_suburb);
        console.log("Neighbourhood: " + address_neighbourhood);
        console.log("Road         : " + address_road);
        console.log("Footway      : " + address_footway);
        console.log("Pedestrian   : " + address_pedestrian);
        console.log("House Number : " + address_house_number);

        const country = document.getElementById("country");
        country.setAttribute("value", "");
        if(address_country !== undefined){
            country.setAttribute("value", address_country);
        }

        const state = document.getElementById("state");
        state.setAttribute("value", "");
        if(address_state !== undefined){
            state.setAttribute("value", address_state);
        }

        const townCity = document.getElementById("town-city");
        townCity.setAttribute("value", "");
        if(address_town !== undefined){
            townCity.setAttribute("value", address_town);
        }else if (address_city !== undefined){
            townCity.setAttribute("value", address_city);
        }

        const suburb = document.getElementById("suburb");
        suburb.setAttribute("value", "");
        if(address_suburb !== undefined){
            suburb.setAttribute("value", address_suburb);
        }

        const road = document.getElementById("road");
        road.setAttribute("value", "");
        if(address_road !== undefined && address_house_number !== undefined){
            road.setAttribute("value", address_road + ", "+ address_house_number);
        }else if(address_road !== undefined){
            road.setAttribute("value", address_road);
        }

    });


});


function httpGet(url, callback_function) {

    const getRequest = new XMLHttpRequest();
    getRequest.open("get", url, true);

    getRequest.addEventListener("readystatechange", function () {

        if (getRequest.readyState === 4 && getRequest.status === 200) {

            callback_function(getRequest.responseText);
        }
    });

    getRequest.send();
}

