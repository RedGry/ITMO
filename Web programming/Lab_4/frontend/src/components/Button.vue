<template>
  <button :class="color">{{label}}</button>
</template>

<script>
export default {
  name: "Button",
  props:{
    label: String,
    color: String
  }
}
</script>

<style scoped>
button {
  text-align: center;
  text-transform: uppercase;
  cursor: pointer;
  letter-spacing: 2px;
  position: relative;
  border: none;
  color: white;
  padding: 15px;
  min-width: 150px;
  transition-duration: 0.4s;
  overflow: hidden;
  box-shadow: 0 5px 15px #193047;
  border-radius: 4px;
}
button:hover {
  background: white;
  color: black;
}
button::after {
  content: "";
  display: block;
  position: absolute;
  padding-top: 300%;
  padding-left: 350%;
  margin-left: -20px !important;
  margin-top: -120%;
  opacity: 0;
  transition: all 0.8s
}
button:active::after {
  padding: 0;
  margin: 0;
  opacity: 1;
  transition: 0s
}
button:focus {outline: 0}
.white {background-color: lightgrey}
.white:hover {box-shadow: 0 2px 10px 5px lightgrey}
.white::after {background: lightgrey}
.yellow {background-color: #ead541}
.yellow:hover {box-shadow: 0 2px 10px 5px #ead541}
.yellow::after {background: #ead541}
.blue {background-color: #116fbf}
.blue:hover {box-shadow: 0 2px 10px 5px #116fbf}
.blue::after {background: #116fbf}
</style>