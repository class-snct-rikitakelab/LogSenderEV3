/// <reference path="./reference.ts"/>

var bufferString: string = "";
var parseValues = new Object();
var parseFlag: Boolean = false;

// LogViewerから送られた文字列をログ名、ログ値、タイムスタンプに分ける
function parseData(text: String) {
  //console.log(text);
  for (var i = 0; i <= text.length; i++) {// 一文字ずつ取り出す 取り出した文字によって処理を変える
    if (text.charAt(i) == ",") {// 「,」なら貯めた文字をログ名・ログ値とする
      if (!parseFlag) {
        parseValues["name"] = bufferString;
        parseFlag = true;
      }
      else {
        parseValues["value"] = Number(bufferString);
      }
      bufferString = "";
    }
    else if (text.charAt(i) == "}") {// 「}」なら貯めた文字をタイムスタンプとし、グラフに追加する
      parseValues["time"] = Number(bufferString);
      if (parseValues["name"].search("#") != -1) {// ログ名の頭文字が「#」なら、かわりにグラフの見た目を変更する
        parseValues["name"] = parseValues["name"].slice(1);
        changeGlaphDisplay(parseValues);
      }
      else addGlaphData(parseValues);
      bufferString = "";
      parseFlag = false;
      parseValues = new Object();
    }

    else if (text.charAt(i) == "{") { }// 「{」なら何もしない
    else {
      bufferString += text.charAt(i);// その他の文字なら文字をバッファに貯める
    }
  }
  updateGlaph();// ログの追加が終わったらグラフの表示を更新する
}