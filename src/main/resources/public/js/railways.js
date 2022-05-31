let railwayTunnelsLayer = new ol.layer.Vector({
    source: new ol.source.Vector({
        format: new ol.format.GeoJSON(),
        url: "/railwayTunnels",
    }),
    style: new ol.style.Style({stroke: new ol.style.Stroke({color: 'red', width: 2})})
});

let railwayTracksLayer = new ol.layer.Vector({
    source: new ol.source.Vector({
        format: new ol.format.GeoJSON(),
        url: "/railwayTracks",
    }),
    style: new ol.style.Style({stroke: new ol.style.Stroke({color: 'black', width: 2})})
});

let stationImage = new ol.style.Circle({
    radius: 5,
    fill: null,
    stroke: new ol.style.Stroke({color: 'red', width: 2})
});

let railwayStationsLayer = new ol.layer.Vector({
    source: new ol.source.Vector({
        format: new ol.format.GeoJSON(),
        url: "/railwayStations",
    }),
    style: new ol.style.Style({image: stationImage})
});