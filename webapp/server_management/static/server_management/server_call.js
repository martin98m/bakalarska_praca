function setListenersForCommands(){
    let items = document.getElementsByClassName('commandSendToInput');

    for (let i = 0; i < items.length; i++) {
        console.log(i, '|', items[i]);
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

console.log('hello a ');

    function scrollToBottom() {
        let obj = document.getElementById("scroll");
        console.log(obj);
        console.log(obj.scrollTop);
        console.log(obj.scrollHeight);
        obj.scrollTop = obj.scrollHeight;
        console.log(obj.scrollTop);
    }
    scrollToBottom();

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
        // console.log(table);
        let rowA = row.parentElement.parentElement.parentElement;
        // console.log(rowA);

        table.removeChild(rowA);
        let all = document.cookie;
        // console.log(all)
    }