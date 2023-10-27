var bodyParser = require('body-parser');

var jsonParser = bodyParser.json()
var urlencodedParser = bodyParser.urlencoded({ extended: false })
 
module.exports=function(router){
    var homeController= require('../Controllers/home.controllers');
    router.post('/register',urlencodedParser,homeController.register);
    router.post('/login',urlencodedParser,homeController.login);
    router.post('/update_user_password',urlencodedParser,homeController.update_user_password);
    router.post('/update_user_name',urlencodedParser,homeController.update_user_name);
    router.post('/update_user_email',urlencodedParser,homeController.update_user_email);
};