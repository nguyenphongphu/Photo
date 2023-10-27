var filemodel=require('../Models/file.model');

exports.upload_data=function(req,res){
    var post_data=req.body;
	var email=post_data.email;
    var tmp_path = req.file;
    console.log(tmp_path);
	filemodel.insert_data(email,tmp_path,function(respnse){
        console.log(respnse);
		res.json(respnse);
    })
}
exports.load_data=function(req,res){
    var post_data=req.body;
	var email=post_data.email;
	filemodel.load_data(email,function(respnse){
        console.log(respnse);
		res.json(respnse);
    })
}
exports.delete_data=function(req,res){
    var post_data=req.body;
    var email=post_data.email;
    var url=post_data.url;
    console.log(email);
    filemodel.delete_data(email,url,function(respnse){
        console.log(respnse);
		res.json(respnse);
    });
}


