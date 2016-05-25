/// <reference path="./reference.ts"/>
// ログをグラフ表示するクラス 一つのクラスで一つのグラフ
// MetricsGraphics.jsを使用しているのでそれのAPIも参照のこと
var logGlaph = (function () {
    function logGlaph(glaphName) {
        if (glaphName === void 0) { glaphName = "glaph" + String(logGlaph.logNumber); }
        this.data = []; // ログデータ
        this.name = glaphName;
        this.generate();
        ++logGlaph.logNumber;
    }
    // グラフを生成する
    logGlaph.prototype.generate = function () {
        this.div_element = document.createElement("div");
        this.div_element.id = this.name;
        var my_div = document.getElementById("chart");
        document.body.insertBefore(this.div_element, my_div);
        var idname = "#" + this.name;
        this.mgdata = {
            title: this.name,
            data: [],
            full_width: true,
            height: 250,
            interpolate: "linear",
            show_tooltips: false,
            transition_on_update: false,
            target: idname,
            x_accessor: "time",
            y_accessor: "value",
            area: false
        };
    };
    // グラフにデータを追加する
    logGlaph.prototype.addData = function (data) {
        this.data.push(data);
    };
    // グラフの見た目を変更する
    logGlaph.prototype.changeDisplay = function (data) {
        this.mgdata[data.type] = data.value;
    };
    // グラフを更新する
    logGlaph.prototype.update = function () {
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
    logGlaph.logNumber = 1; // グラフの番号
    return logGlaph;
}());
// すべてのグラフを更新する
function updateGlaph() {
    for (var i = 0; i < glaph.length; ++i) {
        glaph[i].update();
    }
}
// ログ名からグラフを参照し、対応したグラフにデータを追加する
function addGlaphData(data) {
    var glaphid;
    glaphid = getGlaphNumberFromName(data.name);
    delete data.name;
    glaph[glaphid].addData(data);
}
// ログ名からグラフを参照し、対応したグラフの見た目を変更する
function changeGlaphDisplay(data) {
    var glaphid;
    glaphid = getGlaphNumberFromName(data.name);
    delete data.name;
    glaph[glaphid].changeDisplay(data);
}
// ログ名から対応するグラフの番号を返す
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
