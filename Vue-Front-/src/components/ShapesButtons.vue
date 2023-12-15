<template>
  <div class = "shapes-buttons">
    <button class="ShapeButton"
            :class="{selected: currentOperation === 'draw' && selectedShape === 'line'}"
            @click="this.$emit('select','line')">Line</button>

    <button class="ShapeButton"
            :class="{selected: currentOperation === 'draw' && selectedShape === 'square'}"
            @click="this.$emit('select','square')">Square</button>

    <button class="ShapeButton"
            :class="{selected: currentOperation === 'draw' && selectedShape === 'rectangle'}"
            @click="this.$emit('select','rectangle') ">Rectangle</button>

    <button class="ShapeButton"
            :class="{selected: currentOperation === 'draw' && selectedShape === 'triangle'}"
            @click="this.$emit('select','triangle')">Triangle</button>

    <button class="ShapeButton"
            :class="{selected: currentOperation === 'draw' && selectedShape === 'circle'}"
            @click="this.$emit('select','circle')">Circle</button>

    <button class="ShapeButton"
            :class="{selected: currentOperation === 'draw' && selectedShape === 'ellipse'}"
            @click="this.$emit('select','ellipse')">Ellipse</button>
  </div>
</template>

<script >
export default {
  emits:['select'],
  props:['currentOperation','selectedShape']
}
</script>

<style scoped>
.selected{
  border: 4px solid rgb(9, 218, 9) !important;
  border-radius: 10px !important;
  background-color: aquamarine !important;
  font-size: 24px !important;
}
.ShapeButton{
  width: 180px;
  height: 100%;
  border: none;
  border-radius: 10px;
  background-color: rgb(41, 173, 178);
  margin: 5px;
  font-size: 25px;
  font-weight: 600;
  color: rgb(7, 102, 173);
  cursor: pointer;
}
.shapes-buttons{
  width: 1100px;
  height: 40px;
  margin: auto;
  display: flex;
  justify-content: space-around;
}
</style>