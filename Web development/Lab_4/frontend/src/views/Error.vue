<template>
  <div id='content'>
    <h1>{{ errorCode }}</h1>
    <h2>{{ errorMessage }}</h2>
    <hr>
    <router-link to='/auth' id='button'>Вернуться на главную страницу</router-link>
  </div>
</template>

<script>
export default {
  name: "NotFoundError",
  props: {
    errorCode: String,
    errorMessage: String
  }
}
</script>

<style scoped>
h1 {
  font-size: 8em;
  margin: 0;
  background: linear-gradient(130deg, #2a48eb, #ff0018);
  color: transparent;
  background-clip: text;
  text-transform: uppercase;
}
h2 {
  font-size: 33px;
  font-weight: 200;
  text-transform: uppercase;
  margin: 0;
  letter-spacing: 3px;
}
#button{
  color: white;
  margin: 20px 5px 0;
  padding: 2px;
  letter-spacing: 1px;
  display: inline-block;
  text-decoration: none;
  background-color: black;
  border: 2px solid black;
  border-radius: 6px;
}

#button:hover{
  color: black;
  transition: 0.5s;
  border: 2px solid red;
  background-color: ghostwhite;
}
</style>