/// <reference path="./reference.ts"/>
var logGlaph = (function () {
    function logGlaph(glaphName) {
        if (glaphName === void 0) { glaphName = "glaph" + String(logGlaph.logNumber); }
        this.data = [];
        this.x_length = 0;
        this.name = glaphName;
        this.generate();
        ++logGlaph.logNumber;
    }
    logGlaph.prototype.generate = function () {
        this.div_element = document.createElement("div");
        this.div_element.id = this.name;
        var my_div = document.getElementById("chart");
        //this.div_element.clientWidth = 300;
        document.body.insertBefore(this.div_element, my_div);
        //document.getElementById("chart").appendChild(this.div_element);
        var idname = "#" + this.name;
        this.mgdata = {
            title: this.name,
            //description: "説明",
            data: [],
            // width: 800,
            full_width: true,
            height: 250,
            interpolate: "linear",
            // interpolate_tension: 0,
            show_tooltips: false,
            transition_on_update: false,
            //missing_is_zero: true,
            target: idname,
            x_accessor: "time",
            y_accessor: "value",
            area: false
        };
    };
    logGlaph.prototype.addData = function (data) {
        console.log(data);
        //if(this.mgdata.data==[]){this.mgdata.data[0]=data;}
        //else {this.mgdata.data[this.mgdata.length]=data;}
        this.data.push(data);
        //this.data.join
    };
    logGlaph.prototype.changeDisplay = function (data) {
        this.mgdata[data.type] = data.value;
    };
    logGlaph.prototype.update = function () {
        //delete this.mgdata.xax_format;
        this.mgdata.data = this.data;
        MG.data_graphic(this.mgdata);
    };
    logGlaph.prototype.getName = function () {
        return this.name;
    };
    logGlaph.prototype.getdata = function () {
        return this.data;
    };
    logGlaph.prototype.setdata = function (data) {
        this.data = data;
    };
    logGlaph.prototype.getmgdata = function () {
        return this.mgdata;
    };
    logGlaph.prototype.setmgdata = function (data) {
        this.mgdata = data;
    };
    logGlaph.logNumber = 1;
    return logGlaph;
}());
function updateGlaph() {
    for (var i = 0; i < glaph.length; ++i) {
        glaph[i].update();
    }
}
function changeGlaph(data) {
    var glaphid;
    glaphid = getGlaphNumberFromName(data.name);
    switch (data.type) {
        case "Number":
            delete data.name;
            delete data.type;
            glaph[glaphid].addData(data);
            break;
        default:
            delete data.name;
            glaph[glaphid].changeDisplay(data);
    }
}
function getGlaphNumberFromName(name) {
    var i = 0;
    for (i = 0; i < glaph.length; ++i) {
        if (glaph[i].getName() == name) {
            return i;
        }
    }
    glaph[i] = new logGlaph(name);
    return i;
}
