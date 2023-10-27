var db =require('../Common/connect');
const chat=function(chat){
    this.name=chat.name;
    this.email=chat.email;
    this.passwod=chat.password;
    this.salt=chat.salt;
}
chat.add=function(req,res){
    
}