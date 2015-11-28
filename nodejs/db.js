var MongoClient = require('mongodb').MongoClient
var assert = require('assert')
var ObjectId = require('mongodb').ObjectID
var db
var nodes
var users
	// Connect to the db

function nodesByUser(user, callback) {
	users.find({
		name: user
	}).each(function(err, doc) {
		if (doc != null) {
			var out = []
			for (var i = 0; i < doc.nodes.length; i++) {
				console.dir(i + " " + doc.nodes[i])
				nodeByID(doc.nodes[i], function(err, docum) {
					//console.dir(docum)
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
		_id: id
	}).each(function(err, doc) {
		if (doc != null) {
			
			console.dir(doc)
			callback(err, doc)
				//console.dir(doc)
		}
	})
}

function addUser(name, hash) {
	users.insert({
		name: name,
		hash: hash,
		nodes: []
	}, {}, function(err, doc) {
		console.log("NEW USER: " + name)
	})
}

function addNode(user, qr, links) {
	nodes.insert({
		user: user,
		qr: qr,
		links: links,
		visitors: []
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

	//return found
	//console.dir(found[1])
}

MongoClient.connect("mongodb://localhost:27017/scavenger", function(err, database) {
	db = database;
	nodes = db.collection("nodes")
	users = db.collection("users")
	if (!err) {
		console.log("Databese connected")
			/*getUserID("testUser", function(err, name) {
				console.log(name)
			})*/
		/*nodesByUser("testUser", function(err, docs) {
				console.log("lel")
				console.dir(docs)
			})*/
nodeByID("5655ce73d98bf50d15284cd4", function(err,doc){
	console.dir(doc)
})
			//nodesByUser("testUser")
			//addNode("testUser", "testQR", [])
			//	addUser("testUser","dawdaw")
			//var id = users.find()[1]
			//console.dir(id)

		/*var cursor = db.collection('nodes').find({
		})
		cursor.each(function(err, doc) {
			if (doc != null) {
				console.dir(doc)
			}
		})*/
	}
})