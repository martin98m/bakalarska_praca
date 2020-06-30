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

function showTable(table) {
    document.getElementById('tableGlobal').style.display = 'none';
    document.getElementById('tableUser').style.display = 'none';
    document.getElementById('tableUsed').style.display = 'none';

    document.getElementById('table'+table).style = st;

    document.cookie = 'table='+table+';'
}

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

 function  disconnectFromServer() {

    chatSocket.send(JSON.stringify({
            'info': 'DISCONNECT',
            'server': serverName
        }));
 }

 function connectToServer() {
    chatSocket.send(JSON.stringify({
            'info': 'CONNECT',
            'server': serverName
        }));
 }

 function restartConnection() {
    chatSocket.send(JSON.stringify({
            'info': 'RESTART',
            'server': serverName
        }));
 }

 function clearConsole() {
    document.getElementById('scroll').innerText = null;
 }

     const chatSocket = new WebSocket(
        'ws://'
            + window.location.host
            + '/server_call/'
            + serverName
            + '/ws/call'
     );

    chatSocket.onclose = function(e) {
        console.error('Chat socket closed unexpectedly');
    };

    document.querySelector('#server_cmd_input').onkeyup = function(e) {
        if (e.keyCode === 13) {  // enter, return
            document.querySelector('#server_cmd_submit').click();
        }
    };

    document.querySelector('#server_cmd_submit').onclick = function(e) {

        document.getElementById("loading_animation").style.display = "block";

        const messageInputDom = document.querySelector('#server_cmd_input');
        const message = messageInputDom.value;
        chatSocket.send(JSON.stringify({
            'message': message,
            'server': serverName
        }));
        messageInputDom.value = '';
    };

    chatSocket.onmessage = function(e) {

        document.getElementById("loading_animation").style.display = "none";

        const data = JSON.parse(e.data);
        if (data.info === "True") {
            console.log("Connection to server confirmed");
            let div_conn = document.getElementById("server_call_connection_connected");
            let div_disc = document.getElementById("server_call_connection_disconnected");

            div_conn.hidden = !div_conn.hidden;
            div_disc.hidden = !div_disc.hidden;
        }
        else
            if (data.info === "False")
                console.log("Connection failed");
            else if (data.info === "DISCONNECTED")
                console.log("Disconnected from server")
            else if (data.info === "RESTARTED")
                console.log("Connection restarted");
            else {
                document.querySelector('#scroll').innerText += (data.response + '\n');
            }
        };
