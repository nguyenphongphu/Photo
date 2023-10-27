var express = require('express');
const dotenv=require('dotenv');
var fileUpload = require('express-fileupload');
var bodyParser = require('body-parser');

dotenv.config();
var app =express();
var server = require("http").createServer(app);
const{Server} = require("socket.io");
const io =new Server(server);
const SocketServices=require('./server/chat.server')

global._io=io;
// routers
require('./Routes/home.router')(app);
require('./Routes/chat.router')(app);
require('./Routes/file.router')(app);

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

app.use(fileUpload());

global._io.on('connection',SocketServices.connection)

const port=process.env.port;
server.listen(port,function(){
	console.log('good run '+port);
})