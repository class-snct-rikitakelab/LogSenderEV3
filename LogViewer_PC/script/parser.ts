/// <reference path="./reference.ts"/>

var bufferString: string = "";
var parseValues = new Object();
var parseFlag: Boolean = false;
function parseData(text: String) {
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
      else addGlaphData(parseValues);
      bufferString = "";
      parseFlag = false;
      parseValues = new Object();
    }

    // “–‚½‚ñ‚È‚©‚Á‚½Žž
    else if (text.charAt(i) == "{") { }
    else {
      bufferString += text.charAt(i);
    }
  }
  updateGlaph();
}