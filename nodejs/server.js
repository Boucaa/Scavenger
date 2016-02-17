var http = require("http")
var request = require('request')
var httpg = require('http-get')
var fs = require('fs')
var url = require('url')
var db = require("./db")
var validator = require("./validator")
var PDFDocument = require('pdfkit')
var NEW_NODE_REQUEST = "/NEW_NODE/"
var REGISTER_REQUEST = "/AUTH:"
var nextID = 0
var TEST_REQUEST = "/TEST"
var MAP_REQUEST = "/MAP"
var NODE_REQUEST = "/NODE:"
console.dir(db)

//FS
function write(data, address) {
	fs.writeFile(address, data, function(err, data) {
		if (err) {
			return console.log(err)
		}
		console.log("Write successful")
	})
}

function read(address) {
	fs.readFile(address, "utf8", function(err, data) {
		if (err) {
			return console.log(err)
		}
		console.log(data)
	})
}

http.createServer(function(request, response) {
	console.log(request.url)
	var data = ""
	if (request.method == "POST") {
		request.on("data", function(chunk) {
			data += chunk
		})
		request.on("end", function() {
			var jsonData = JSON.parse(data)
			if (jsonData.token != undefined) {
				validator.validate(jsonData.token, function(json) {
					if (request.url == "/CLAIM") {
						console.log("CLAIMING")
						db.claimNode(json.email, jsonData.qr, jsonData.nodeID, function(result) {
							if (result == "success") {
								console.log("CLAIM SUCCESSFUL")
								response.end("{\"result\":\"OK\"}")
							}
						})
					}
				})
			} else {
				console.log("POST request didn't contain auth token, request: \n" + request.url + "\n" + "data")
			}
		})
	} else if (request.url == "/favicon.ico") {
		response.writeHead(200, {
			'Content-Type': 'image/x-icon'
		})
		response.end()
		return

		//WEB LOGIN   	
	} else if (request.url == "/SIGNIN") {
		fs.readFile('login.html', "utf8", function(err, data) {
			response.writeHead(200, {
				'Content-Type': 'text/html'
			})
			response.write(data)
			response.end()
		})
	} else if (request.url == MAP_REQUEST) {
		response.writeHead(200, {
			'Content-Type': 'text/plain;charset=utf-8'
		})
		db.getMapNodes(function(docs) {
			console.log(JSON.stringify(docs))
			response.end("{\"result\":" + JSON.stringify(docs) + "}")
		})

		//TODO: popis, obrazky apod.
	} else if (request.url.substring(0, NODE_REQUEST.length) == NODE_REQUEST) {
		db.nodeByID(request.url.substring(NODE_REQUEST.length), function(err, doc) {
			response.end(JSON.stringify(doc))
		})
	} else if (request.url == "QR_PDF") {
		httpg.get('http://api.qrserver.com/v1/create-qr-code/?size=150x150&data=Example', 'img.png', function(error, result) {
			if (error) {
				console.error(error)
			} else {
				console.log('File downloaded at: ' + result.file)
				doc = new PDFDocument()
				doc.pipe(response)
				doc.image('img.png')
				doc.end()
			}
		})
		return
	}
	response.writeHead(200, {
		'Content-Type': 'text/plain;charset=utf-8'
	})
	console.log(request.url)
}).listen(8081)

console.log('Server running at http://127.0.0.1:8081/')