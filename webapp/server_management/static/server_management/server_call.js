function addRowHandlers(tableName) {
    let table = document.getElementById(tableName);
    let rows = table.getElementsByTagName("tr");
    for (let i = 1; i < rows.length; i++) {
        let currentRow = table.rows[i];
        let createClickHandler = function(row) {
            return function() {
                let cell = row.getElementsByTagName("td")[0];
                console.log(cell);
                document.getElementById('inputCommand').value = cell.innerHTML;
            };
        };
        currentRow.onclick = createClickHandler(currentRow);
    }
}

addRowHandlers('tableGlobal');
addRowHandlers('tableUser');
addRowHandlers('tableUsed');

console.log('hello a ');