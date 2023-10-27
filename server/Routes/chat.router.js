module.exports=function(router){
    var chatcontrolles=require('../Controllers/chat.controllers');
    router.get('/message',chatcontrolles.message);
}