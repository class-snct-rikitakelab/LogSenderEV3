/// <reference path="./reference.ts"/>

var bufferString: string = "";
function parseData(text: String) {
   var parseValues = [];
   console.log(text);
   for (var i = 0; i <= text.length; i++) {
      bufferString += text.charAt(i);
      if (text.charAt(i) == "}") {
         bufferString = bufferString.replace(/\\'/g, "'");
         parseValues = JSON.parse(bufferString);
         changeGlaph(parseValues);
         bufferString = "";
      }
   }
   updateGlaph();
}
