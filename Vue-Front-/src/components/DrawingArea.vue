<template>
  <div class="canvas">
    <v-stage :config="configKonva" @click = "addShape">
      <v-layer>
        <template v-for = "shape in shapes" :key ="shape.id">
          <v-line v-if = "shape.type === 'line' " :config="{
            points: shape.points,
            stroke: shape.stroke,
            draggable: true,
            id: shape.id,
            x: shape.x,
            y: shape.y,
          }"
          @click = "handleShapeClicking(shape)" @dragend = "handleDragEnd($event,shape)"></v-line>

          <v-regular-polygon v-if="shape.type === 'triangle' " :config="{
            x: shape.x,
            y: shape.y,
            sides: 3,
            radius: shape.radius,
            fill: shape.fill,
            stroke: shape.stroke,
            id: shape.id,
            draggable: true}"
            @click= "handleShapeClicking(shape)" @dragend = "handleDragEnd($event,shape)"></v-regular-polygon>

          <v-circle v-if="shape.type === 'circle' " :config="{
          x: shape.x,
          y: shape.y,
          radius: shape.radius,
          fill: shape.fill,
          stroke: shape.stroke,
          id: shape.id,
          draggable: true}"
          @click= "handleShapeClicking(shape)" @dragend = "handleDragEnd($event,shape)"></v-circle>

          <v-rect v-if="shape.type === 'rectangle' || shape.type === 'square' "  :config="{
          x: shape.x - shape.width/2,
          y: shape.y - shape.height/2,
          width: shape.width,
          height: shape.height,
          fill: shape.fill,
          stroke: shape.stroke,
          id: shape.id,
          draggable: true}"
          @click= "handleShapeClicking(shape)" @dragend = "handleDragEnd($event,shape)"></v-rect>

          <v-ellipse v-if="shape.type === 'ellipse' "  :config="{
          x: shape.x,
          y: shape.y,
          radiusX: shape.radiusX,
          radiusY: shape.radiusY,
          fill: shape.fill,
          stroke: shape.stroke,
          id: shape.id,
          draggable: true}"
          @click= "handleShapeClicking(shape)" @dragend = "handleDragEnd($event,shape)"></v-ellipse>
        </template>
        <v-transformer ref = "transformer"></v-transformer>
      </v-layer>
    </v-stage>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: 'DrawingArea',
  props:['shapes'],
  emits: ['add','shapeClick'],
  data(){
    return{
      configKonva: {
        width: 1100,
        height: 550
      },
      selectedId:''
    }
  },
  methods:{
    addShape(e){
      this.$emit('add', e)
    },
    handleShapeClicking(shape){
      this.$emit('shapeClick', shape)
    },
    handleDragEnd(e,shape){
      // update shape after moving in front
      if(e.target.attrs.width === undefined){
        shape.x = e.target.attrs.x
        shape.y = e.target.attrs.y
      }
      else{
        shape.x = e.target.attrs.x + shape.width/2
        shape.y = e.target.attrs.y + shape.height/2
      }
      // update shape after moving in back
      axios.post('http://localhost:8080/paint/modify', JSON.stringify(shape),{
        headers: {'Content-Type': 'application/json'}})
          .then((response) => {
            console.log(response.data)
          })
          .catch((error) => console.log(error))
    }
  }
}
</script>

<style scoped>
.canvas{
  margin: auto;
  background-color: azure;
  width: 1100px;
  height: 550px;
  border: 3px solid #000000;
}
</style>