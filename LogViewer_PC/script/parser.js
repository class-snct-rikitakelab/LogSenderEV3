/// <reference path="./reference.ts"/>
var bufferString = "";
var parseValues = new Object();
var parseFlag = false;
// LogViewerから送られた文字列をログ名、ログ値、タイムスタンプに分ける
function parseData(text) {
    //console.log(text);
    for (var i = 0; i <= text.length; i++) {
        if (text.charAt(i) == ",") {
            if (!parseFlag) {
                parseValues["name"] = bufferString;
                parseFlag = true;
            }
            else {
                parseValues["value"] = Number(bufferString);
            }
            bufferString = "";
        }
        else if (text.charAt(i) == "}") {
            parseValues["time"] = Number(bufferString);
            if (parseValues["name"].search("#") != -1) {
                parseValues["name"] = parseValues["name"].slice(1);
                changeGlaphDisplay(parseValues);
            }
            else
                addGlaphData(parseValues);
            bufferString = "";
            parseFlag = false;
            parseValues = new Object();
        }
        else if (text.charAt(i) == "{") { } // 「{」なら何もしない
        else {
            bufferString += text.charAt(i); // その他の文字なら文字をバッファに貯める
        }
    }
    updateGlaph(); // ログの追加が終わったらグラフの表示を更新する
}
