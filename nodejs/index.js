/*
Author: Ana Sundov
Update: 24/05/2020
*/


const express = require('express');
const mysql = require('mysql');
const bodyParser = require('body-parser');
const lovely = require('./queries/lovely.js');



var app = express();
var publicDir=(__dirname + '/public/');

app.use(express.static(publicDir));
app.use(bodyParser.json()); 	//Accept JSON Params
app.use(bodyParser.urlencoded({extended: true}));	// Accept URL Encoded params



app.post('/register_doctor', lovely.register_doctor);
app.post('/register_patient', lovely.register_patient);
app.post('/login', lovely.login);
app.post('/search', lovely.search);
app.post('/reset_password', lovely.reset_password);
app.post('/get_appointment', lovely.get_appointment);
app.post('/get_doctor', lovely.get_doctor);
app.post('/get_all_doctors', lovely.get__all_doctors);
app.post('/add_appointment', lovely.add_appointment);
app.post('/get_hospitalisation', lovely.get_hospitalisation);
app.post('/get_consultation', lovely.get_consultation);
app.post('/get_consultation_doctor', lovely.get_consultation_doctor);
app.post('/relationship', lovely.relationship);
app.post('/get_patient', lovely.get_patient);
app.post('/get_uri', lovely.get_uri);
app.post('/get_uri_for_profile', lovely.get_uri_for_profile);
app.post('/update_patient', lovely.update_patient);













// Start Server
app.listen(3000, function() {
	console.log('ana Restful running on port 3000');
});


























