/*
function setListenersForCommands(){
    let items = document.getElementsByClassName('commandSendToInput');

    for (let i = 0; i < items.length; i++) {
        // console.log(i, '|', items[i]);
        let x = items[i];
        items[i].addEventListener(
            'click',
            function () {
                console.log(x);
                document.getElementById('inputCommand').value = x.innerHTML;
                },
            false);
    }
}
setListenersForCommands();

function scrollToBottom() {
    let obj = document.getElementById("scroll");
    obj.scrollTop = obj.scrollHeight;
}
// scrollToBottom();
*/
function hideTables() {
    document.getElementById("commands_body_global").style.display = 'none';
    document.getElementById("commands_body_user").style.display = 'none';
    document.getElementById("commands_body_recent").style.display = 'none';



    // document.cookie = 'table='+table+';'
}

document.getElementById("commands_head_global").addEventListener("click", function () {
    hideTables();
    document.getElementById("commands_body_global").style.display = 'flex';
});
document.getElementById("commands_head_user").addEventListener("click", function () {
    hideTables();
    document.getElementById("commands_body_user").style.display = 'flex';
});
document.getElementById("commands_head_recent").addEventListener('click', function () {
    hideTables();
    document.getElementById("commands_body_recent").style.display = 'flex';
});

/*
function getCookie(name) {
    let value = "; " + document.cookie;
    let parts = value.split("; " + name + "=");
    if (parts.length === 2) return parts.pop().split(";").shift();
}

const st = document.getElementById('tableUser').style;
let name = getCookie('table');
if (name != null)
    showTable(name);
else
    showTable('Global');


function deleteRow(row) {
    let table = row.parentElement.parentElement.parentElement.parentElement;
    let rowA = row.parentElement.parentElement.parentElement;

    table.removeChild(rowA);
}
*/

 function  disconnectFromServer() {

    controlSocket.send(JSON.stringify({
            'info': 'DISCONNECT',
            'server': serverName
        }));
 }

 function connectToServer() {
    controlSocket.send(JSON.stringify({
            'info': 'CONNECT',
            'server': serverName
        }));
 }

 function restartConnection() {
    controlSocket.send(JSON.stringify({
            'info': 'RESTART',
            'server': serverName
        }));
 }

 function refreshDataGathering() {
    controlSocket.send(JSON.stringify({
        'info': 'REFRESH',
        'server': serverName
    }));
 }

 function clearConsole() {
    document.getElementById('scroll').innerText = null;
 }


 const commandSocket = new WebSocket(
    'ws://'
    + window.location.host
    + '/server_call/ws/command'
 );

 const controlSocket = new WebSocket(
'ws://'
    + window.location.host
    + '/server_call/ws/call/'
    + serverName
    + '/control'
 );

 const serverSocket = new WebSocket(
     'ws://'
     + window.location.host
     + '/server_call/ws/call/'
     + serverName
 )

 document.querySelector('#server_cmd_input').onkeyup = function(e) {
     if (e.keyCode === 13) {  // enter, return
        document.querySelector('#server_cmd_submit').click();
    }
 };

 document.querySelector('#server_cmd_submit').onclick = function(e) {

    document.getElementById("loading_animation").style.display = "block";

    const messageInputDom = document.querySelector('#server_cmd_input');
    const message = messageInputDom.value;
    serverSocket.send(JSON.stringify({
        'message': message,
        'server': serverName
    }));

    messageInputDom.value = '';
 };

 document.querySelector('#command_save').onclick = function(e) {

     console.log("AAAAAAAAAAAAAAAA");
    const messageInputDom = document.querySelector('#command_save_command');
    const message = messageInputDom.value;

    let radio = document.getElementById('target1').checked;
    let type = "user";

    if (radio === true)
        type = "global";
    else
        type = "user";

    commandSocket.send(JSON.stringify({
        'info': 'ADD',
        'command': message,
        'type': type
    }));
 };

 let command_rows_clear = document.getElementsByClassName('commands_body_clear');

 for (let i = 0; i < command_rows_clear.length; i++){
     command_rows_clear[i].addEventListener('click', function () {
        let type = command_rows_clear[i].children[1].innerHTML;
        if (type === "global" || type === "user" ){
            let command = command_rows_clear[i].parentElement.children[0].innerHTML;
            console.log(command);
            commandSocket.send(JSON.stringify({
                'info': 'DELETE',
                'command': command,
                'type': type
            }));
        }
     });
 }


 serverSocket.onmessage = function(e) {

    document.getElementById("loading_animation").style.display = "none";

    const data = JSON.parse(e.data);

    let div_conn = document.getElementById("server_call_connection_connected");
    let div_disc = document.getElementById("server_call_connection_disconnected");

    if (data.connected === "True"){
        div_conn.style.display = 'flex';
        div_disc.style.display = 'none';
    }
    else{
        document.querySelector('#scroll').innerText += (data.response + '\n');
    }
 };

 controlSocket.onmessage = function (e) {

     const  data = JSON.parse(e.data);

     let div_conn = document.getElementById("server_call_connection_connected");
     let div_disc = document.getElementById("server_call_connection_disconnected");


     if (data.connected === "True"){
        div_conn.style.display = 'flex';
        div_disc.style.display = 'none';
     }
     else if (data.connected === "False"){
         div_conn.style.display = 'none';
         div_disc.style.display = 'flex';
     }
     else if (data.connected === "DISCONNECTED"){
         div_conn.style.display = 'none';
         div_disc.style.display = 'flex';
     }
     else if (data.connected === "RESTARTED"){
         clearConsole();
     }
 }
