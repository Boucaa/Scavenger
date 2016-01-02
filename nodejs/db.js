var mongo = require('mongodb')
var MongoClient = require('mongodb').MongoClient
var assert = require('assert')
var ObjectId = require('mongodb').ObjectID
var db
var nodes
var users

function nodesByUser(user, callback) {
	users.find({
		name: user
	}).each(function(err, doc) {
		if (doc != null) {
			var out = []
			for (var i = 0; i < doc.nodes.length; i++) {
				console.dir(i + " " + doc.nodes[i])
				nodeByID(doc.nodes[i], function(err, docum) {
					console.log(i)
					out[i] = docum
					if (i == doc.nodes.length - 1) {
						callback(err, out)
					}
				})
			}
		}
	})
}

function nodeByID(id, callback) {
	console.log("finding " + id)
	nodes.find({
		_id: new mongo.ObjectID(id)
	}).each(function(err, doc) {
		if (doc != null) {
			console.dir(doc)
			callback(err, doc)
		}
	})
}

function addUser(name, hash, GID, email) {
	users.insert({
		name: name,
		hash: hash,
		gID: GID,
		email: mail,
		nodes: []
	}, {}, function(err, doc) {
		console.log("NEW USER: " + name)
	})
}

function addNode(user, qr, lat, lng) {
	nodes.insert({
		user: user, //founder of the node
		qr: qr, //the confirmation QR code
		lat: lat, //location lat
		lng: lng, //location lng
		owner: null, //current owner
		lastClaim: new Date().getMilliseconds-86400000 //now minus 1 day
	}, {}, function(err, doc) {
		if (err) {
			console.log(err)
		} else {
			console.log("NEW NODE: user: " + user + " qr: " + qr)
			users.update({
				"name": user
			}, {
				"$push": {
					"nodes": doc.ops[0]._id
				}
			})
		}
	})
}

function claimNode()

function checkUser(name) {
	//TODO: Check if the user already exists when creating a new user
}

function getUserID(user, callback) {
	users.find({
		name: user
	}).each(function(err, doc) {
		if (doc != null) {
			callback(err, doc.name)
		}
	})
}

function getMapNodes(callback) {
	nodes.find().toArray(function(err, docs) {
		callback(docs.map(function(doc) {
			return {
				lat: doc.lat,
				lng: doc.lng,
				id: doc._id
			}
		}))
	})
}

MongoClient.connect("mongodb://localhost:27017/scavenger", function(err, database) {
	db = database;
	nodes = db.collection("nodes")
	users = db.collection("users")
	if (!err) {
		console.log("Databese connected")
		nodeByID("565b3a35074452f938336a1e", function(err, doc) {
				console.dir(doc)
			})
			/*getUserID("testUser", function(err, name) {
				console.log(name)
			})*/
			/*nodesByUser("testUser", function(err, docs) {
					console.log("lel")
					console.dir(docs)
				})*/
			/*nodeByID("5655ce73d98bf50d15284cd4", function(err,doc){
				console.dir(doc)
			})*/
			//nodesByUser("testUser")
			//getMapNodes(console.dir)
			//addNode("testUser", "testQR", [], 50.07554, 14.43780)
			//	addUser("testUser","dawdaw","random idAWDAWD", "e.mail@email.com")
			//var id = users.find()[1]
			//console.dir(id)
	}
})
module.exports.getMapNodes = getMapNodes
module.exports.nodeByID = nodeByID