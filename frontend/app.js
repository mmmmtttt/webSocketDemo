const socket = io.connect("http://localhost:8099/message",{transports:['websocket','xhr-polling','jsonp-polling']});

socket.on('connect', text => {
    console.log("onconnected");
    const el = document.createElement('li');
    el.innerHTML = "ip: "+ text+" connected";
    document.querySelector('ul').appendChild(el)
});

socket.on('message', text => {
    const el = document.createElement('li');
    el.innerHTML = "new message:" + text;
    document.querySelector('ul').appendChild(el)
});

socket.on('disconnect', text => {
    console.log("ondisconnect");
    const el = document.createElement('li');
    el.innerHTML = "ip: "+text+" disconnected";
    document.querySelector('ul').appendChild(el)

});

document.querySelector('button').onclick = () => {
    const text = document.querySelector('input').value;
    let data = {
        username:"testusername",
        message:text
    }
    socket.emit('message', data);
}

// Regular Websockets

// const socket = new WebSocket('ws://localhost:8080');

// // Listen for messages
// socket.onmessage = ({ data }) => {
//     console.log('Message from server ', data);
// };

// document.querySelector('button').onclick = () => {
//     socket.send('hello');
// }