/// <reference path="./reference.ts"/>


class logGlaph {

   public static logNumber: number = 1;
   //public static zeroTime: number;
   private mgdata;
   private name: String;
   private div_element;
   private data = [];
   private x_length = 0;

   constructor(glaphName: String = "glaph" + String(logGlaph.logNumber)) {
      this.name = glaphName;
      this.generate();
      ++logGlaph.logNumber;
   }

   public generate() {
      this.div_element = document.createElement("div");
      this.div_element.id = this.name;
      var my_div = document.getElementById("chart");
      //this.div_element.clientWidth = 300;
      document.body.insertBefore(this.div_element, my_div);
      //document.getElementById("chart").appendChild(this.div_element);
      var idname: String = "#" + this.name;
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
         //interpolate: "monotone"
      };

   }

   public addData(data) {
      console.log(data);
      //if(this.mgdata.data==[]){this.mgdata.data[0]=data;}
      //else {this.mgdata.data[this.mgdata.length]=data;}
      this.data.push(data);
      //this.data.join
   }

   public changeDisplay(data){
      this.mgdata[data.type] = data.value;
   }

   public update() {
      //delete this.mgdata.xax_format;
      this.mgdata.data = this.data;
      MG.data_graphic(this.mgdata);
   }

   public getName() {
      return this.name;
   }

   public getdata(){
      return this.data;
   }

   public setdata(data){
      this.data = data;
   }

   public getmgdata(){
      return this.mgdata;
   }

   public setmgdata(data){
      this.mgdata = data;
   }


}

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
