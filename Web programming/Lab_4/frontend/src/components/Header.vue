<template>
  <header class="header">
      <div>
        <span class="lab-name">{{title}}</span>
      </div>
      <div>
        <span>
          <a target="_blank" v-bind:href="authorUrl1">{{authorName1}}</a>
          , P3211
        </span>
        <span>
          <a target="_blank" v-bind:href="authorUrl2">{{authorName2}}</a>
          , P3211
        </span>
        <span>
          Вариант
          <a target="_blank" v-bind:href="varUrl">6112</a>
        </span>
      </div>
  </header>
</template>

<script>
export default {
  name: "Header",
  props: {
    title: String,
    authorName1: String,
    authorUrl1: String,
    authorName2: String,
    authorUrl2: String,
    varUrl: String
  }
}
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Bebas+Neue&family=MuseoModerno&display=swap");

header{
  margin: 0;
  padding: 0;
}

.header {
  height: 25%;
  min-width: 440px;
  display: flex;
  background-color: black;
  justify-content: space-between;
  box-shadow: 0 0 20px 5px darkred;
  font-family: "MuseoModerno", cursive;
}

/*
  Название проекта:
*/

.header .lab-name{
  color: whitesmoke;
  font-size: 1.5em;
  font-weight: bold;
  margin-top: 10px;
}

/*
  Имена, Фамилии, вариант лабы:
*/

.header > div {
  display: flex;
  align-items: center;
  margin: 5px 20px 5px;
  flex-direction: column;
}

.header span, .header a{
  color: white;
  font-size: 0.9em;
  font-weight: bold;
  text-decoration: none;
}

.header a:hover {
  color: orange;
  font-size: 0.95em;
  transition: 0.5s;
  text-shadow: 0 0 20px red;
  text-decoration: none;
}
</style>