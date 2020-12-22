<template>
 <footer>
   <div class="footer">
     <div class="logos">
       <a class="git-link" target="_blank" v-bind:href="authorUrl1">
         <img class="logo" alt="Логотип"  v-bind:src="authorLogoPath1"/>
       </a>
       <span> &amp; </span>
       <a class="git-link" target="_blank" v-bind:href="authorUrl2">
         <img class="logo" alt="Логотип"  v-bind:src="authorLogoPath2"/>
       </a>
     </div>
   </div>
 </footer>
</template>

<script>
export default {
  name: "Footer",
  props: {
    authorLogoPath1: String,
    authorUrl1: String,
    authorLogoPath2: String,
    authorUrl2: String,
  }
}
</script>

<style scoped>
.footer {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  bottom: 0;
  box-shadow: 0 0 20px 5px darkred;
  background-color: black;
  height: 80px;
  width: 100%;
  position: absolute;
}

.footer span{
  color: white;
  font-family: Arial, cursive;
}

/*
    Логотипы наши
*/

.logo:hover {
  transition: 0.4s;
  border-radius: 40%;
  border: 2px solid red;
  transform: scale(0.9);
  box-shadow: 0 0 20px 2px darkred;
}

.logos {
  display: flex;
  align-items: center;
}

.logo{
  border-radius: 50%;
  width: 50px;
  height: 50px;
  margin: 0 5px 0 5px;
  border: 1px solid white;
}
</style>