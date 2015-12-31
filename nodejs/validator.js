var https = require("https")
var CLIENT_ID = "25013689568-5p2g0ett1o784rddou1qpcnjgae5f8l8.apps.googleusercontent.com"


function validate(token) {
	console.log(token)
	https.get("https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + token, function(res) {
		res.on("data",function(data){
			console.log("DATA: " + data)
		})
	})
}

module.exports.validate = validate