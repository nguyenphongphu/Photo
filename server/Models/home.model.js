var db =require('../Common/connect')
var crypto = require('crypto');
var uuid = require('uuid');

const user=function(user){
    this.name=user.name;
    this.email=user.email;
    this.passwod=user.password;
    this.salt=user.salt;
}
var genrandomstring=function(length){
	return crypto.randomBytes(Math.ceil(length/2))
		.toString('hex')
		.slice(0,length);
};
var sha512 = function (password,salt){
	var hash=crypto.createHmac('sha512',salt);
	hash.update(password);
	var value= hash.digest('hex');
	return{
		salt:salt,
		passwordHash:value
	}
};
function saltHashPassword(userPassword){
	var salt=genrandomstring(16);
	var PasswordData=sha512(userPassword,salt);
	return PasswordData;
}

function checkHashPassword(user_password,salt){
    return sha512(user_password,salt);
}


user.add_user=function (name, email, password,result){
    var hash_data=saltHashPassword(password);
	var plaint_password= hash_data.passwordHash;
	var salt=hash_data.salt;
    var uid= uuid.v4();
    db.query('SELECT * FROM `user` WHERE email=?',[email],function (err,res,fields) {
        db.on('error', function (err) {
            console.log('mysql error', err);
        });
        if (res && res.length){
            console.log('user already exists !!! ');
            result(res.length +1);
        }            
        else
        {
            db.query("INSERT INTO `user`( `unique_id`, `name`, `email`, `encrypted_password`, `salt`, `created_at`, `update_at`) " +
                "VALUES (?,?,?,?,?,NOW(),NOW())", [uid, name, email, plaint_password, salt], function (err, res, fields) {
                db.on('error', function (err) {
                console.log('[Mysql error]',err);
                result(0);
                });
                console.log('register successful');
                result(uid);               
            });               
        }
    });
}
user.login_user=function(email,password,result){
    db.query('SELECT * FROM `user` WHERE email=?',[email],function(err,res,fields){
        db.on('error', function (err) {
            console.log('mysql error', err);
        });
        if(res&&res.length){
            var salt=res[0].salt;
            var encrypted_password=res[0].encrypted_password;
            var check_password=checkHashPassword(password,salt).passwordHash;
            if(encrypted_password==check_password){
                db.query('SELECT * FROM user WHERE email=?',[email],function(req,res){
                    if(res&&res.length){
                        console.log(res);
                        var js={"data":res};
                        result(js);
                        
                    }
                    else{
                        result(0);      
                    }
                });
            }else{
                result(2);
            }
        }else{
            result(0);
        }
    });
}
user.update_user_password=function(email,current_password,new_password,result){
    db.query('SELECT * FROM `user` WHERE email=?',[email],function(err,res,fields){
        db.on('error', function (err) {
            console.log('mysql error', err);
        });
        if(res&&res.length){
            var salt=res[0].salt;
            var encrypted_password=res[0].encrypted_password;
            var check_password=checkHashPassword(current_password,salt).passwordHash;
            if(encrypted_password==check_password){
                var hash_data=saltHashPassword(new_password);
                var new_plaint_password= hash_data.passwordHash;
	            var new_salt=hash_data.salt;
                db.query('UPDATE `user` SET `encrypted_password`=?,`salt`=?,`update_at`=NOW() WHERE email=?',[new_plaint_password,new_salt,email],function(req,res,fields){
                    result(1);
                });
                
            }else{
                result(2);
            }
        }else{
            result(0);
        }
    });

}
user.update_user_name=function(email,name,password,result){
    db.query('SELECT * FROM `user` WHERE email=?',[email],function(err,res,fields){
        db.on('error', function (err) {
            console.log('mysql error', err);
        });
        if(res&&res.length){
            var salt=res[0].salt;
            var encrypted_password=res[0].encrypted_password;
            var check_password=checkHashPassword(password,salt).passwordHash;
            if(encrypted_password==check_password){              
                db.query('UPDATE `user` SET `name`=?,`update_at`=NOW() WHERE email=?',[name,email],function(req,res,fields){
                    result(1);
                });
                
            }else{
                result(2);
            }
        }else{
            result(0);
        }
    });

}
user.update_user_email=function(current_email,new_email,password,result){
    db.query('SELECT * FROM `user` WHERE email=?',[current_email],function(err,res,fields){
        db.on('error', function (err) {
            console.log('mysql error', err);
        });
        if(res&&res.length){
            var salt=res[0].salt;
            var encrypted_password=res[0].encrypted_password;
            var check_password=checkHashPassword(password,salt).passwordHash;
            if(encrypted_password==check_password){              
                db.query('UPDATE `user` SET `email`=?,`update_at`=NOW() WHERE email=?',[new_email,current_email],function(req,res,fields){
                    result(1);
                });
                
            }else{
                result(2);
            }
        }else{
            result(0);
        }
    });

}
module.exports=user;