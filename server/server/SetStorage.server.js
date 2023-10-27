const multer = require('multer');
const crypto = require('crypto');
const path = require('path');
var fs=require('fs');
var db=require('../Common/connect');

var storage;
var today = new Date();
// SET STORAGE
 storage = multer.diskStorage({
    filename: function(req, file, cb) {
      return crypto.pseudoRandomBytes(16, function(err, raw) {
        if (err) {
          return cb(err);
        }
        return cb(null, "" + (raw.toString('hex')) + (path.extname(file.originalname)));
      });
    },destination: function (req, file, cb) {
      db.query('SELECT `unique_id` FROM `user` WHERE email=?',[req.body.email],function(err,res,fields){
        db.on('error', function (err) {
          console.log('mysql error', err);
        });
        if(res&&res.length){
          const folderName = '../SERVER/Data/'+res[0].unique_id+'/';
          const folderYear=(today.getFullYear())+'/';
          const url=folderName+folderYear;
          try {
            if (!fs.existsSync(folderName)) {
              fs.mkdirSync(folderName);
              if(!fs.existsSync(url)){
                fs.mkdirSync(url);
              }
            }else if(!fs.existsSync(url)){
              fs.mkdirSync(url);
            }         
          } catch (err) {
            console.error(err);
          }
          cb(null, url)
        }
      });      
    }
  });

  var upload = multer({
    storage: storage
  });
module.exports=upload;