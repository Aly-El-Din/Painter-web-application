<template>

  <NavBar :currentOperation = "currentOperation"
          @selectColor = "currentOperation = 'color'"
          @delete = "currentOperation = 'delete'"
          @copy = "currentOperation = 'copy'"
          @resize = "currentOperation = 'resize'"
          @save = "handleSave"
          @load = "handleLoad"
          @undo = "undo"
          @redo = "redo"
          @clear = "clear"
  ></NavBar>

  <CP v-if = "currentOperation === 'color'"  v-model = "color" class="color-picker"/>
  <DrawingArea :shapes="shapes" @add = "addShape" @shapeClick = "handleShapeClicking"></DrawingArea>

  <ShapesButtons :currentOperation = "currentOperation"
                 :selectedShape = "selectedShape"
                 @select = "selectShape"></ShapesButtons>

</template>

<script>
import NavBar from "@/components/NavBar.vue";
import DrawingArea from "@/components/DrawingArea.vue";
import ShapesButtons from "@/components/ShapesButtons.vue";
import axios from "axios";
export default {
  name: 'App',
  components: {ShapesButtons, DrawingArea, NavBar},
  mounted() {
    axios.get('http://localhost:8080/paint/refresh')
        .then((response) => {
          this.shapes = response.data
        })
        .catch((error) => console.log(error))
  },
  data () {
    return {
      shapes:[],
      configKonva: {
        width: 1200,
        height: 550
      },
      selectedShape:'',
      color:'ff3200',
      currentOperation:'',
    }
  },
  methods:{
    addShape(e){
      if(this.currentOperation !== 'draw') return
      const shapeRequest = {
        type : this.selectedShape,
        id: Date.now().toString(),
        x : e.evt.offsetX,
        y : e.evt.offsetY,
      }
      //add shape to list of shapes in backend and list of shapes in frontend
      axios.post('http://localhost:8080/paint/create', JSON.stringify(shapeRequest),{
        headers: {'Content-Type': 'application/json'}})
          .then((response) => {
            console.log(response.data)
            this.shapes.push(response.data)
          })
          .catch((error) => console.log(error))
    },
    selectShape(shape){
      this.selectedShape = shape
      this.currentOperation = 'draw'
    },
    handleShapeClicking(shape){
      //handle the shape clicking based on the selected option from the navbar
      switch (this.currentOperation) {
        case 'color':
          this.colorShape(shape)
          break;
        case 'delete':
          this.deleteShape(shape)
          break;
        case 'copy':
          this.copyShape(shape)
          break;
        case 'resize':
          this.resizeShape(shape);
          break;
        default:
          break;
      }

    },
    colorShape(shape){
      shape.fill = '#' + this.color
      //change color of shape in backend
      axios.post('http://localhost:8080/paint/modify', JSON.stringify(shape),{
        headers: {'Content-Type': 'application/json'}})
          .then((response) => {
            console.log(response.data)
          })
          .catch((error) => console.log(error))
    },
    deleteShape(shape){
      //delete shape from shapes array
      let shapeToBeRemoved = this.shapes.findIndex(s => s.id === shape.id)
      this.shapes.splice(shapeToBeRemoved,1)

      //delete shape from list of shapes in backend
      axios.delete('http://localhost:8080/paint/delete/' + shape.id)
          .then((response) => console.log(response.data))
          .catch((error) => console.log(error))
    },
    copyShape(shape){
      //copy shape in backend and add it to shapes array in frontend
      axios.get('http://localhost:8080/paint/copy/' + shape.id)
          .then((response) => {
            this.shapes.push(response.data)
          })
          .catch((error) => console.log(error))
    },
    resizeShape(shape){
      const canvas = document.querySelector('.konvajs-content');
      const canvasRect = canvas.getBoundingClientRect();
      const updateShapeSize = (event) => {
        const mouseX = event.clientX - canvasRect.left;
        const mouseY = event.clientY - canvasRect.top;
        const centerDistance = Math.sqrt(
            Math.pow(mouseX - shape.x, 2) + Math.pow(mouseY - shape.y, 2)
        );
        const ax = mouseX-shape.x;
        const ay = mouseY-shape.y;
        switch(shape.type){
          case 'triangle':
          case 'circle':
            shape.radius = centerDistance;
            break;
          case 'rectangle':
            var Thetax = Math.atan2(ay,ax)*180/Math.PI;
            var Theta = Math.atan(shape.height/shape.width)*180/Math.PI;
            if(Math.abs(Thetax)<Theta||Math.abs(Thetax)>(180-Theta))
              shape.width = Math.abs(ax)*2;
            else
              shape.height = Math.abs(ay)*2;
            break;
          case 'square':
            shape.height = 2*Math.max(Math.abs(ax),Math.abs(ay));
            shape.width = shape.height;
            break;
          case 'line':
            shape.points = [0,0,ax,ay];
            break;
          case 'ellipse':
            if(Math.abs(ax)<Math.abs(ay))
              shape.radiusY = centerDistance;
            else
              shape.radiusX = centerDistance;
            break;
          default:
            break;
        }
        document.addEventListener('click', handleClick);
      };
      const handleClick = () => {
        document.removeEventListener('mousemove', updateShapeSize);
        document.removeEventListener('click', handleClick);
        console.log(shape)
        axios.post('http://localhost:8080/paint/modify', JSON.stringify(shape),{
          headers: {'Content-Type': 'application/json'}})
            .then((response) => {
              console.log(response.data)
            })
            .catch((error) => console.log(error))
      };
      document.addEventListener('mousemove', updateShapeSize);
      this.currentOperation = '';
    },
    undo(){
      console.log('undo');
      axios.get('http://localhost:8080/paint/undo')
          .then((response) => {
             console.log(response.data)
             this.shapes = response.data
          })
          .catch((error) => console.log(error))
    },
    redo(){
      console.log('redo');
      axios.get('http://localhost:8080/paint/redo')
          .then((response) => {
            if(response.data !== ''){
              console.log(response.data)
              this.shapes = response.data
            }
          })
          .catch((error) => console.log(error))
    },
    clear(){
      axios.delete('http://localhost:8080/paint/clear')
          .then((response) => {
            this.shapes = response.data
          })
          .catch((error) => console.log(error))
    },
    handleSave(filetype) {
      let file = prompt("Please enter path to save file");
      if(file == null)return;
      file=file.replace(/"/g,'')
      if(!(/\.[^/.]+$/).test(file)){
        file += "."+filetype;
      }else if(!file.toLowerCase().endsWith(".xml")&&!file.toLowerCase().endsWith(".json")){
        return;
      }
      if(file.split('.').pop().toLowerCase()!=filetype){
        return;
      }
      fetch(`http://localhost:8080/paint/save?filePath=${encodeURIComponent(file)}&fileType=${filetype}`, { method: 'get' })
          .then(res => res.text())
          .then(data => {
            console.log(data)
          })
          .catch(error => {
            console.error('Error:', error)
          });
    },
    handleLoad() {
      let file = prompt("Please enter path to load file");
      if(file == null)return;
      file=file.replace(/"/g,'')
      let fileType = file.split('.').pop().toLowerCase();
      if(fileType.toLowerCase()!="xml"&&fileType.toLowerCase()!="json")return;
      axios.get(`http://localhost:8080/paint/load?filePath=${encodeURIComponent(file)}&fileType=${fileType}`,{
        headers: {'Content-Type': 'application/json'}})
          .then((response) => {
            //this.shapes.push(...response.data)
            console.log(response.data)
            this.shapes = response.data
            console.log(this.shapes)
          })
          .catch((error) => console.log(error))
    },
  }
}
</script>

<style>
body{
  background-color: rgb(243, 243, 243);
}
#app{
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  margin-top: 35px;
}
.color-picker{
  margin-bottom: 4px;
}
</style>