
let serverObj = document.getElementsByClassName('server');
serverObj = Array.prototype.slice.call(serverObj);

    function updateShowedData(checkbox, stat){

        if(document.getElementsByName("checkbox"+checkbox)[0].checked){
            let list = document.getElementsByClassName("shownStatFor"+stat);
            for(let item of list)
                item.style.display = 'flex';
        }
        else{
            let list = document.getElementsByClassName("shownStatFor"+stat);
            for(let item of list)
                item.style.display = 'none';
        }
    }

    function sortByCpuLowHigh() {

        // serverObj = document.getElementsByClassName('server');
        // serverObj = Array.prototype.slice.call(serverObj);
        serverObj.sort(compareCPU_lowToHigh);
        document.getElementsByClassName("flex-container")[0].innerHTML = '';

        for (let serverX of serverObj) {
            document.getElementsByClassName("flex-container")[0].append(serverX);
        }
    }

    function sortByCpuHighLow() {

        // serverObj = document.getElementsByClassName('server');
        // serverObj = Array.prototype.slice.call(serverObj);
        serverObj.sort(compareCPU_highToLow);
        document.getElementsByClassName("flex-container")[0].innerHTML = '';

        for (let serverX of serverObj) {
            document.getElementsByClassName("flex-container")[0].append(serverX);
        }
    }

    function compareCPU_lowToHigh(a, b) {

        const cpuA = a.children[1].innerHTML.trim();
        const cpuB = b.children[1].innerHTML.trim();

        if(cpuA > cpuB){
            return 1;
        }
        else if(cpuA < cpuB) {
            return -1;
        }
        else {
            return 0;
        }
    }

    function compareCPU_highToLow(a, b) {

        const cpuA = a.children[1].innerHTML.trim();
        const cpuB = b.children[1].innerHTML.trim();

        if(!isNaN(cpuA) && !isNaN(cpuB)){
            if (cpuA > cpuB) return -1;
            else return 1;
        }else {
            if (!isNaN(cpuA) && isNaN(cpuB)) return -1;
            if (isNaN(cpuA) && !isNaN(cpuB)) return 1;
        }
    }