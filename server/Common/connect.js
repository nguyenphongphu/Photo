var mysql = require('mysql');
// connect to Mysql
var con = mysql.createConnection({
	host:'localhost',
	user:'root',
	password:'',
	database:'test'
});
con.connect(function(error){
	if(error)
	{
		throw error;
	}
	else
	{
		console.log('MySQL Database is connected Successfully');
	}
});
module.exports=con
