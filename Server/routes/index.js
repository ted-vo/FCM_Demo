var express = require('express')
var router = express.Router()

var request = require('request')
var config = require('../configuration/app')


/* GET home page. */
router.get('/', (req, res, next) => {
	return res.render('notification')
})

router.post('/notify', (req, res, next) => {
	sendToOne(req.body.message, "FCM Push Demo", req.body.fcmDeviceToken, res)
	// sendToTopics(req.body.message, "news", res)
})

const sendNotifications = (data, next) => {

	const dataString = JSON.stringify(data)

	const options = {
		uri: 'https://fcm.googleapis.com/fcm/send',
		method: 'POST',
		headers: {
			'Authorization': config.fcmLegacyKey,
			'Content-Type': 'application/json'
		},
		json: data
	}

	request(options, function(err, res, body) {
		if (err) throw err
		// else console.log(body)
	})
}

const sendToTopics = (msg, topic, res) => {

	const data = {
		"to": '/topics/' + topic,
		"notification": {
			"body": msg
		}
	}

	sendNotifications(data)

	res.json('Send Success')
}

const sendToOne = (msg, title, fcmDeviceToken, res) => {

	const data = {
		"notification": {
			"body": msg,
			"title": title
		},
		"to": fcmDeviceToken
	}

	sendNotifications(data)

	res.json('Send Success')
}

module.exports = router