function init( tokenId, inputId, outputId ) {

	token.id = tokenId;

	var input = $(inputId);
	var timer = null;
	var output = $(outputId);

	input.on("keyup",function(){

		/* Debounce */
		if (timer) clearTimeout(timer);
		timer = setTimeout(function(){
			search(input.val(), output);
		},250);

	})

}

function search(keyword, output) {

	getDataFromStations(keyword);

	var info = "";
	output.html("<h2 style='color: white'>Search results "+info+":</h2>")
	output.append($("<div style='color: white'/>").html("Loading..."))
	output.append($("<div style='color: white'/>").addClass("cp-spinner cp-meter"))

	$.getJSON("//api.waqi.info/search/?token="+token()+"&keyword="+keyword,function(result){

		var info = "";
		output.html("<h2  style='color: white'>Search results "+info+":</h2>")
		if (!result || (result.status!="ok")) {
			output.append("Sorry, something wrong happend: ")
			if (result.data) output.append($("<code>").html(result.data))
			return
		}

		if (result.data.length==0) {
			output.append("Sorry, there is no result for your query!")
			return
		}

		var table = $("<table class='table table-dark'/>").addClass("result")
		output.append(table)

		output.append($("<div style='color: white'/>").html("Click on any of the station to see the detailled AQI"))

		var stationInfo = $("<div style='color: white'/>")
		output.append(stationInfo)
		var j = 0;
		result.data.forEach(function(station,i){
			var tr = $("<tr style='color: white'>");
			tr.append($("<td>").html(station.station.name))
			tr.append($("<td>").html(colorize(station.aqi)))
			tr.append($("<td>").html(station.time.stime))
			tr.append($("<td><a href='CityInfo/"+station.uid+"'> "+station.uid+"</a></td> "));

			tr.on("click",function(){
				showStation(station,stationInfo)
			})

			/*
			var a = $("<a href='/CityInfo/'>")
			a.append(tr)
			table.append(a)
			*/
			table.append(tr);
			if (i==0)
				showStation(station,stationInfo)
		})


	});
}

function showStation(station, output) {

	output.html("<h2>Pollutants & Weather conditions:</h2>")
	output.append($("<div/>").html("Loading..."))
	output.append($("<div/>").addClass("cp-spinner cp-meter"))

	$.getJSON("//api.waqi.info/feed/@"+station.uid+"/?token="+token(),function(result){

		output.html("<h2>Pollutants & Weather conditions:</h2>")
		if (!result || (result.status!="ok")) {
			output.append("An error has occured: ")
			if (result.data) output.append($("<code>").html(result.data))
			return
		}

		var names = {
			pm25: "PM<sub>2.5</sub>",
			pm10: "PM<sub>10</sub>",
			o3: "Ozone",
			no2: "Nitrogen Dioxide",
			so2: "Sulphur Dioxide",
			co: "Carbon Monoxyde",
			t: "Temperature",
			w: "Wind",
			r: "Rain (precipitation)",
			h: "Relative Humidity",
			d: "Dew",
			p: "Atmostpheric Pressure"
		}

		output.append($("<div>").html("Station: "+result.data.city.name+" on "+result.data.time.s))

		var table = $("<table class='table table-dark'/>").addClass("result")
		output.append(table)

		for (var specie in result.data.iaqi) {
			var aqi = result.data.iaqi[specie].v
			var tr = $("<tr>");
			tr.append($("<td>").html(names[specie]||specie))
			tr.append($("<td>").html(colorize(aqi,specie)))
			table.append(tr)
		}
	})
}

function token() {
	return "abc94ddd457d58241e7abae33fd2721b0fb00251";
}


function colorize(aqi, specie ) {
	specie = specie||"aqi"
	if (["pm25","pm10","no2","so2","co","o3","aqi"].indexOf(specie)<0) return aqi;

	var spectrum = [
		{a:0,  b:"#cccccc",f:"#ffffff"},
		{a:50, b:"#009966",f:"#ffffff"},
		{a:100,b:"#ffde33",f:"#000000"},
		{a:150,b:"#ff9933",f:"#000000"},
		{a:200,b:"#cc0033",f:"#ffffff"},
		{a:300,b:"#660099",f:"#ffffff"},
		{a:500,b:"#7e0023",f:"#ffffff"}
	];


	var i = 0;
	for (i=0;i<spectrum.length-2;i++) {
		if (aqi=="-"||aqi<=spectrum[i].a) break;
	};
	return $("<div/>")
		.html(aqi)
		.css("font-size","120%")
		.css("min-width","30px")
		.css("text-align","center")
		.css("background-color",spectrum[i].b)
		.css("color",spectrum[i].f)

}

function postData(id, name, co, h, dew, no2, o3, p, pm10, pm25, so2, t, w, wg, r){
	fetch('http://localhost:8080/city', {
		method: 'POST',
		body:JSON.stringify({"id":id,
			"name":name,
			"co":co,
			"h":h,
			"d":dew,
			"no2":no2,
			"o3":o3,
			"p":p,
			"pm10":pm10,
			"pm25":pm25,
			"so2":so2,
			"t":t,
			"w":w,
			"wg":wg,
			"r":r,
		}),
		headers: {
			'Content-Type': 'application/json'
		}
	}).then((res) => res.text())
		.then((text)=>console.log("text:"+ text))
		.catch((err)=>console.log("error:" + err))
}
function get_specific_data_from_station(station_name, uid){
	let string = 'https://api.waqi.info/feed/@'+uid+'/?token='+token()
	let request_specific = new XMLHttpRequest()
	request_specific.open('GET', string, true)
	request_specific.onload = function() {
		let val = JSON.parse(this.response)

		if (request_specific.status == 200 ) {
			let co_value;
			let dew_value;
			let h_value;
			let no2_value;
			let o3_value;
			let p_value;
			let p10_value;
			let p25_value;
			let so2_value;
			let t_value;
			let w_value;
			let wg_value;
			let r_value;



			if (val.data.iaqi) {

				if (val.data.iaqi.co && val.data.iaqi.co['v']) {
					co_value=val.data.iaqi.co["v"];
				}
				if (val.data.iaqi.r && val.data.iaqi.r['v']) {
					r_value=val.data.iaqi.r["v"];
				}
				if (val.data.iaqi.d && val.data.iaqi.d['v']) {
					dew_value=val.data.iaqi.d["v"];
				}
				if (val.data.iaqi.h && val.data.iaqi.h['v']) {
					h_value=val.data.iaqi.h["v"];
				}
				if (val.data.iaqi.no2 && val.data.iaqi.no2['v']) {
					no2_value=val.data.iaqi.no2["v"];
				}
				if (val.data.iaqi.o3 && val.data.iaqi.o3['v']) {
					o3_value=val.data.iaqi.o3["v"];
				}
				if (val.data.iaqi.p && val.data.iaqi.p['v']) {
					p_value=val.data.iaqi.p["v"];
				}
				if (val.data.iaqi.pm10 && val.data.iaqi.pm10['v']) {
					p10_value=val.data.iaqi.pm10["v"];
				}
				if (val.data.iaqi.pm25 && val.data.iaqi.pm25['v']) {
					p25_value=val.data.iaqi.pm25["v"];
				}
				if (val.data.iaqi.so2 && val.data.iaqi.so2['v']) {
					so2_value=val.data.iaqi.so2["v"];
				}
				if (val.data.iaqi.t && val.data.iaqi.t['v']) {
					t_value=val.data.iaqi.t["v"];
				}
				if (val.data.iaqi.w && val.data.iaqi.w['v']) {
					w_value=val.data.iaqi.w["v"];
				}
				if (val.data.iaqi.wg && val.data.iaqi.wg['v']) {
					wg_value=val.data.iaqi.wg["v"];
				}
				postData(uid, station_name, co_value, h_value, dew_value, no2_value, o3_value, p_value, p10_value, p25_value, so2_value, t_value,  w_value, wg_value, r_value)
			}

		}
	}
	request_specific.send()

}
function getDataFromStations(keyword) {
	let string = 'https://api.waqi.info/search/?token='+token()+'&keyword='+keyword
	let request = new XMLHttpRequest()
	request.open('GET', string, true)
	request.onload = function() {

		let response = JSON.parse(this.response)


		if (request.status == 200 ) {

			response.data.forEach(ci => {

				get_specific_data_from_station(ci.station.name, ci.uid);
			})
		}
	}
	request.send()
}



