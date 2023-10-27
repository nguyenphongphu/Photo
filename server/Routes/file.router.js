var bodyParser = require('body-parser');
var setstorage=require('../server/SetStorage.server');

var jsonParser = bodyParser.json();
var urlencodedParser = bodyParser.urlencoded({ extended: false });

module.exports=function(router){
    var filecontroller=require('../Controllers/file.controllers');
    router.post('/upload',setstorage.single('data'),urlencodedParser,filecontroller.upload_data);
    router.post('/load_data',urlencodedParser,filecontroller.load_data);
    router.post('/delete_data',urlencodedParser,filecontroller.delete_data);
}
 