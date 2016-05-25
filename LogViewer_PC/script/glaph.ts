/// <reference path="./reference.ts"/>

// ログをグラフ表示するクラス 一つのクラスで一つのグラフ
// MetricsGraphics.jsを使用しているのでそれのAPIも参照のこと
class logGlaph {
  public static logNumber: number = 1;// グラフの番号
  private mgdata;// MetricsGraphics.jsのグラフ表示用
  private name: String;// グラフ名
  private div_element;// グラフを表示するhtmlの領域 グラフ生成時に追加される
  private data = [];// ログデータ

  constructor(glaphName: String = "glaph" + String(logGlaph.logNumber)) {
    this.name = glaphName;
    this.generate();
    ++logGlaph.logNumber;
  }

  // グラフを生成する
  public generate() {
    this.div_element = document.createElement("div");
    this.div_element.id = this.name;
    var my_div = document.getElementById("chart");
    document.body.insertBefore(this.div_element, my_div);
    var idname: String = "#" + this.name;
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
  }

  // グラフにデータを追加する
  public addData(data) {
    this.data.push(data);
  }

  // グラフの見た目を変更する
  public changeDisplay(data) {
    this.mgdata[data.type] = data.value;
  }

  // グラフを更新する
  public update() {
    this.mgdata.data = this.data;
    MG.data_graphic(this.mgdata);
  }

  public getName() {
    return this.name;
  }

  public getdata() {
    return this.data;
  }

  public setdata(data) {
    this.data = data;
  }

  public getmgdata() {
    return this.mgdata;
  }

  public setmgdata(data) {
    this.mgdata = data;
  }
}



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
function getGlaphNumberFromName(name: String) {
  var i = 0;
  for (i = 0; i < glaph.length; ++i) {
    if (glaph[i].getName() == name) {
      return i;
    }
  }
  glaph[i] = new logGlaph(name);
  return i;
}