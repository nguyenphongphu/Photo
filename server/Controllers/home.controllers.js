var home=require('../Models/home.model');

exports.register= function(req,res){
    var post_data=req.body;
	var plaint_password= post_data.password;
	var name=post_data.name;
	var email=post_data.email;
	home.add_user(name,email,plaint_password,function(respnse){
		console.log(respnse);
		res.json(respnse);
	});
}
exports.login=function(req,res){
	var post_data=req.body;
	var plaint_password=post_data.password;
	var email=post_data.email;
	home.login_user(email,plaint_password,function(respnse){
		console.log(respnse);
		res.json(respnse);
	});
}
exports.update_user_password=function(req,res){
	var post_data=req.body;
	var current_password=post_data.current_password;
	var new_password=post_data.new_password;
	var email=post_data.email;
	home.update_user_password(email,current_password,new_password,function(respnse){
		console.log(respnse);
		res.json(respnse);
	});
}
exports.update_user_name=function(req,res){
	var post_data=req.body;
	var name=post_data.name;
	var password=post_data.password;
	var email=post_data.email;
	home.update_user_name(email,name,password,function(respnse){
		console.log(respnse);
		res.json(respnse);
	});
}
exports.update_user_email=function(req,res){
	var post_data=req.body;
	var password=post_data.password;
	var current_email=post_data.current_email;
	var new_email=post_data.new_email;
	home.update_user_email(current_email,new_email,password,function(respnse){
		console.log(respnse);
		res.json(respnse);
	});
}
