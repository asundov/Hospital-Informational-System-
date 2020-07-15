const mysql = require('mysql');

const pool = mysql.createPool({
    connectionLimit: 50,
    host:'localhost', // Replace your host IP
	user: 'root',
	password:'',	//defaut fot xampp
	database:'bis'
});


module.exports = pool;