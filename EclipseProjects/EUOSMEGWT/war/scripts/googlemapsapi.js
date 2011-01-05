var mapOne = null;
var MAPFILES_URL = "http://www.google.com/mapfiles/";
var markers = null;
var polylines = null;
var bounds = null;
var info = null;
var details = null;
var selected = null;
var reverseIcon = null;
var clickMarker = null;
var hashFragment = "";

var GGeoStatusCode = {
	"200": "G_GEO_SUCCESS",
	"400": "G_GEO_BAD_REQUEST",
	"500": "G_GEO_SERVER_ERROR",
	"601": "G_GEO_MISSING_QUERY",
	"602": "G_GEO_UNKNOWN_ADDRESS",
	"603": "G_GEO_UNAVAILABLE_ADDRESS",
	"604": "G_GEO_UNKNOWN_DIRECTIONS",
	 "610": "G_GEO_BAD_KEY",
	"620": "G_GEO_TOO_MANY_QUERIES"
};

var GGeoStatusDescription = {
	"200": "No errors occurred. The address was successfully parsed and its geocode has been returned.",
	"400": "A directions request could not be successfully parsed.",
	"500": "A geocoding or directions request could not be successfully processed,<br/>yet the exact reason for the failure is not known.",
	"601": "The HTTP q parameter was either missing or had no value.<br/>For geocoding requests, this means that an empty address was specified as input.</br>For directions requests, this means that no query was specified in the input.",
	"602": "No corresponding geographic location could be found for the specified address.<br/>This may be due to the fact that the address is relatively new, or it may be incorrect.",
	"603": "The geocode for the given address or the route for the given<br/>directions query cannot be returned due to legal or contractual reasons.",
	"604": "The GDirections object could not compute directions between the points mentioned in the query.<br/>This is usually because there is no route available between the two points, or because we do not have data for routing in that region.",
	"610": "The given key is either invalid or does not match the domain for which it was given.",
	"620": "The given key has gone over the requests limit in the 24 hour period." 
};

var GGeoAddressAccuracy = {
	"0": "Unknown location.",
	"1": "Country",
	"2": "Region<br/>(state, province, prefecture, etc.) ",
	"3": "Sub-region<br/>(county, municipality, etc.)",
	"4": "Town (city, village)",
	"5": "Post code (zip code)",
	"6": "Street level accuracy.",
	"7": "Intersection level accuracy.",
	"8": "Address level accuracy.",
	"9": "Premise<br/>(building name, property name, shopping center, etc.)"
}
function selectMarker(n) {
	GEvent.trigger(markers[n], "click");
}
    	