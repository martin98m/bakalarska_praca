
function setListenersForCommands(){
    let items = document.getElementsByClassName('commands_body_command');

    for (let i = 0; i < items.length; i++) {
        // console.log(i, '|', items[i]);
        let x = items[i];
        items[i].addEventListener(
            'click',
            function () {
                document.getElementById('server_cmd_input').value = x.innerHTML;
                },
            false);
    }
}
setListenersForCommands();

// function scrollToBottom() {
//     let obj = document.getElementById("scroll");
//     obj.scrollTop = obj.scrollHeight;
// }

function hideTables() {
    document.getElementById("commands_body_global").style.display = 'none';
    document.getElementById("commands_body_user").style.display = 'none';
    document.getElementById("commands_body_recent").style.display = 'none';

    document.getElementById("commands_head_global").classList.remove("buttonDataSelected");
    document.getElementById("commands_head_user").classList.remove("buttonDataSelected");
    document.getElementById("commands_head_recent").classList.remove("buttonDataSelected");

    // document.cookie = 'table='+table+';'
}

document.getElementById("commands_head_global").addEventListener("click", function () {
    hideTables();
    document.getElementById("commands_body_global").style.display = 'flex';
    document.getElementById("commands_head_global").classList.add("buttonDataSelected");
});
document.getElementById("commands_head_user").addEventListener("click", function () {
    hideTables();
    document.getElementById("commands_body_user").style.display = 'flex';
    document.getElementById("commands_head_user").classList.add("buttonDataSelected");
});
document.getElementById("commands_head_recent").addEventListener('click', function () {
    hideTables();
    document.getElementById("commands_body_recent").style.display = 'flex';
    document.getElementById("commands_head_recent").classList.add("buttonDataSelected");
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

    const messageInputDom = document.querySelector('#command_save_command');
    const message = messageInputDom.value;

    let description = document.getElementById("command_save_description").value;

    let radio = document.getElementById('target1').checked;
    let type = "user";

    let new_command_row = document.createElement("div");
    new_command_row.classList.add("commands_command_row");

    let new_command_command = document.createElement("div");
    new_command_command.classList.add("commands_body_command");
    new_command_command.innerText = message;

    let new_command_clear = document.createElement("div");
    new_command_clear.classList.add("commands_body_clear");
    new_command_clear.innerHTML = "<a>clear</a>";

    new_command_row.appendChild(new_command_command);
    new_command_row.appendChild(new_command_clear);

    new_command_row.addEventListener(
            'click',
            function () {
                document.getElementById('server_cmd_input').value = message;
                },
            false);

    let command_recent_clone = new_command_row.cloneNode(true);

    command_recent_clone.addEventListener(
            'click',
            function () {
                document.getElementById('server_cmd_input').value = message;
                },
            false);

    let recent = document.getElementById("commands_body_recent");
    recent.appendChild(command_recent_clone);

    if (radio === true) {
        type = "global";
        document.getElementById("commands_body_global").appendChild(new_command_row);
    }
    else {
        type = "user";
        document.getElementById("commands_body_user").appendChild(new_command_row);
    }

    closeCommandAdd();
    commandSocket.send(JSON.stringify({
        'info': 'ADD',
        'command': message,
        'description': description,
        'type': type
    }));
 };

 let command_rows_clear = document.getElementsByClassName('commands_body_clear');

 for (let i = 0; i < command_rows_clear.length; i++){
     command_rows_clear[i].addEventListener('click', function () {
        let type = command_rows_clear[i].children[1].innerHTML;
        console.log("Test");
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

 let x = document.getElementById("server-call-grid");
 let y = document.getElementById("learn-command");
 let z = document.getElementById("learn-command-2");
let w = document.getElementById("command_list")

 function openCommandAdd() {
    w.style.display = "none";
    x.classList.add("grid-container-learn");
    x.classList.remove("grid-container");
    y.classList.add("learn_hidden");
    z.classList.remove("learn_hidden");
 }

 function closeCommandAdd() {
    console.log("IDEEE");
    w.style.display = "grid";
     x.classList.add("grid-container");
     x.classList.remove("grid-container-learn");
     y.classList.remove("learn_hidden");
     z.classList.add("learn_hidden");
 }