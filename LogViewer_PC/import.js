var importfile =
["script/socket.js",
"script/parser.js",
"script/glaph.js",
"script/file.js"];

for(i=0;i<importfile.length;++i){
   document.write('<script src="',importfile[i],'"></script>');
}
