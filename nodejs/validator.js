var https = require("https")
var CLIENT_ID = "25013689568-5p2g0ett1o784rddou1qpcnjgae5f8l8.apps.googleusercontent.com"


function validate(token, callback) {
	console.log(token)
	https.get("https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + token, function(res) {
		res.on("data", function(data) {
			console.log("DATA: " + data)
			var json = JSON.parse(data)
			if (json.error_description == "Invalid Value") {
				console.log("invalid token: \n" + data + "\n")
				callback(null)
			} else if (json.aud == CLIENT_ID) {
				console.log("authentication successful " + json.email)
				callback(json)
			} else {
				console.log("invalid Google Client ID")
				callback(null)
			}
		})
	})
}

module.exports.validate = validate