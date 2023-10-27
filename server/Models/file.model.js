var db =require('../Common/connect')
var crypto = require('crypto');
var uuid = require('uuid');
var fs=require('fs');

var today = new Date();

const data=function(data){
    this.user_id=data.user_id;
    this.url=data.url;
    this.data_type=data.data_type;
}

data.insert_data=function(email,file,result){

    console.log(file.mimetype);
    var url = '../SERVER/Data/'+email+'/'+today.getFullYear()+'/'+file.filename;
    db.query('SELECT `unique_id` FROM `user` WHERE email=?',[email],function(err,res,fields){
        db.on('error', function (err) {
            console.log('mysql error', err);
        });
        if(res&&res.length){
            var user_id=res[0].unique_id;
            var filetype=file.mimetype;
            db.query('INSERT INTO `data`( `user_id`,`url`, `data_type`, `date`) VALUES (?,?,?,NOW())' 
            ,[user_id,url,filetype],function(req,res,fields){
                db.on('error', function (err) {
                    console.log('[Mysql error]',err);
                    result(0);
                });
                console.log('register successful');
                result(1);
            });           
        }else{
            result(0);
        }
    });    
}
data.load_data=function(email,result){
    db.query('SELECT * FROM `user` WHERE email=?',[email],function(err,res,fields){
        db.on('error', function (err) {
            console.log('mysql error', err);
        });
        if(res&&res.length){
            db.query('SELECT * FROM data WHERE email=?',[email],function(req,res){
                if(res&&res.length){
                    console.log(res);
                    var js={"data":res};
                    result(js);      
                }else{
                    result(0);      
                }
            });           
        }else{
            result(0);
        }
    });
}
data.delete_data=function(email,url,result){
    db.query('SELECT `unique_id` FROM `user` WHERE email=?',[email],function(err,res,fields){
        db.on('error', function (err) {
            console.log('mysql error', err);
        });
        if(res&&res.length){
            var user_id=res[0].unique_id;
            console.log(user_id);
            db.query('DELETE FROM `data` WHERE `user_id`=?and url=?'
            ,[user_id,url],function(req,res,fields){
                db.on('error', function (err) {
                    console.log('[Mysql error]',err);
                    result(0);
                });
            if (fs.existsSync(url)) {
                fs.unlink(url, (err) => {
                    if (err) {
                        console.log(err);
                    }
                    console.log('deleted');
                    result(1);
                })
            }
                result(0);
            });           
        }else{
            result(0);
        }
    });    
}
module.exports=data;