<template>
  <section class="main">
    <div class="container">
      <div class="row">
        <div class="text-center">
          <div class="graph">
            <div class="svg-wrapper">
              <svg height="300" width="300" xmlns="http://www.w3.org/2000/svg" id="graph" @click="validateFromGraph">
                <!-- Стерлки и оси -->
                <line stroke="black" x1="0" x2="300" y1="150" y2="150"></line>
                <line stroke="black" x1="150" x2="150" y1="0" y2="300"></line>
                <polygon fill="black" points="150,0 144,15 156,15" stroke="black"></polygon>
                <polygon fill="black" points="300,150 285,156 285,144" stroke="black"></polygon>

                <!-- Деления -->
                <line stroke="black" x1="200" x2="200" y1="155" y2="145"></line>
                <line stroke="black" x1="250" x2="250" y1="155" y2="145"></line>

                <line stroke="black" x1="50" x2="50" y1="155" y2="145"></line>
                <line stroke="black" x1="100" x2="100" y1="155" y2="145"></line>

                <line stroke="black" x1="145" x2="155" y1="100" y2="100"></line>
                <line stroke="black" x1="145" x2="155" y1="50" y2="50"></line>

                <line stroke="black" x1="145" x2="155" y1="200" y2="200"></line>
                <line stroke="black" x1="145" x2="155" y1="250" y2="250"></line>

                <!-- Подписи к осям -->
                <text fill="black" x="285" y="140">X</text>
                <text fill="black" x="160" y="15">Y</text>

                <!-- Первая фигура (1 четверть) -->
                <path id="circle"
                      fill="blue"
                      fill-opacity="0.2"
                      stroke="blue"
                      d="M 200 150 A 50 50, 90, 0, 0, 150 100 L 150 150 Z"></path>

                <!-- Вторая фигура (2 четверть) -->
                <polygon id="rectangle"
                         fill="blue"
                         fill-opacity="0.2"
                         stroke="blue"
                         points="100,50 100,150 150,150 150,50"></polygon>

                <!-- Третья фигура (4 четверть) -->
                <polygon id="triangle"
                         fill="blue"
                         fill-opacity="0.2"
                         stroke="blue"
                         points="150,150 250,150 150,200"></polygon>

                <!-- Точки на графике -->

              </svg>
            </div>
          </div>
        </div>

        <div class="text-center">

          <div class="inputs">
            <p>Выберите R:</p>
            <div class="row">
              <label class="text-label">
                <input id="input-r" class="text-input" type="number" step="0.1" name="r" placeholder="[-3; 5]" maxlength="14" v-model="r"/>
              </label>
            </div>

            <p>Выберите Х:</p>
            <div class="row">
              <label class="text-label">
                <input id="input-x" class="text-input" type="text" name="x" placeholder="[-5; 3]" maxlength="14" v-model="x"/>
              </label>
            </div>

            <p>Выберите Y:</p>
            <div class="row">
              <label class="text-label">
                <input id="input-y" class="text-input" type="text" name="y" placeholder="[-3; 5]" maxlength="14" v-model="y"/>
              </label>
            </div>
          </div>
          <div class="special-button">
            <button id="submit-button" @click="validateForm" type="submit">Отправить на проверку</button>
            <button id="remove-button" @click="deleteDots" >Удалить все точки</button>
          </div>

          <div class="special-button">
            <Button id="logout" color="blue" label="Выйти из аккаунта" @click.native="logout"/>
          </div>
        </div>
      </div>
    </div>

    <div class="container-table">
      <div class="text-center">
        <p style="font-size: 20px" v-if="dots.length === 0">Точек нет :(</p>
        <table v-else class="table-check">
          <thead class="thead">
          <tr class="table-header">
            <th scope="col">X</th>
            <th scope="col">Y</th>
            <th scope="col">R</th>
            <th scope="col">Текущее время</th>
            <th scope="col">Результат попадания</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="dot in dots" :key="dot" class="table-row">
            <td>{{ dot.x }}</td>
            <td>{{ dot.y }}</td>
            <td>{{ dot.r }}</td>
            <td>{{ dot.time }}</td>
            <td>{{ dot.result ? "\u2713" : "\u2717" }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </section>
</template>

<script>
import Button from "@/components/Button";

export default {
  name: "Main",
  components: {
    Button,
  },
  data(){
    return {
      x: "",
      y: "",
      r: "5", // максимальный размер графика
      xGraph: "", // Х из графика
      yGraph: "", // Y из графика
      dots: new Array(0), // Список всех точек пользователя
    }
  },
  watch: {
    x(val){
      this.validateInput(-5, 3, 'input-x', val);
    },
    y(val){
      this.validateInput(-3, 5, 'input-y', val);
    },
    r(val){
      if(this.validateInput(-3, 5, 'input-r', val)) {
        let r = parseFloat(this.r);
        let circle = document.getElementById("circle");
        let rectangle = document.getElementById("rectangle");
        let triangle = document.getElementById("triangle");
        circle.setAttribute("d", `M ${150 + 10 * r} 150 A ${r * 10} ${r * 10}, 90, 0, 0, 150 ${150 - 10 * r} L 150 150 Z`);
        rectangle.setAttribute("points", `${150 - 10 * r},${150 - 20 * r} ${150 - 10 * r},150 150,150 150,${150 - 20 * r}`);
        triangle.setAttribute("points", `150,150 ${150 + 20 * r},150 150,${150 + 10 * r}`);
        this.drawDots();
      }
    }
  },
  methods: {
    // Взаимодействие с формой и графиком
    addDots(x, y){
      this.$axios.put("http://localhost:8890/api/point",
          {x: x, y: y, r: this.r},
          {headers: {"Authorization": "Bearer " + localStorage.getItem("jwt")}
      }).then(() => {
        this.loadDots();
        this.$notify({
          group: 'success',
          title: 'Добавление точки',
          text: 'Успешно',
          type: 'success'
        });
      }).catch(() => {
        this.AxiosErrorHandler("Точку не удалось добавить")
      });
    },
    deleteDots(){
      this.$axios.delete("http://localhost:8890/api/point",
          {headers: {Authorization: "Bearer " + localStorage.getItem("jwt")}
      }).then(() => {
        this.loadDots();
        this.$notify({
          group: 'success',
          title: 'Удаление точек',
          text: 'Успешно',
          type: 'success'
        });
      }).catch(() => {
        this.AxiosErrorHandler("Точки не удалось удалить");
      });
    },
    logout(){
      this.$notify({
        group: 'success',
        title: 'Выход из аккаунта',
        text: 'Успешно',
        type: 'success'
      });
      this.$router.push({name: "auth-page"}, () => localStorage.clear());
    },

    // Загрузка и прорисовка точек на графике
    loadDots(){
      this.$axios.get("http://localhost:8890/api/point", {
        headers: {Authorization: "Bearer " + localStorage.getItem("jwt")}
      }).then(response => {
        this.dots = response.data;
        this.drawDots();
      }).catch(() => {
        this.AxiosErrorHandler("Точки не удалось загрузить");
      });
    },
    drawDots(){
      let r = parseFloat(this.r);
      let svg = document.getElementById("graph")
      let oldDots = document.querySelectorAll("circle");
      oldDots.forEach(oldDot => oldDot.parentNode.removeChild(oldDot));

      if(this.dots.length !== 0){
        this.dots.forEach(dot => {
          let newDot = document.createElementNS("http://www.w3.org/2000/svg", "circle");
          newDot.setAttribute("id", "target-dot");
          newDot.setAttribute("r", "3.5");
          if (r >= 0) {
            newDot.setAttribute("cx", `${dot.x / 5 * 100 + 150}`);
            newDot.setAttribute("cy", `${150 - dot.y / 5 * 100}`);
          } else {
            newDot.setAttribute("cx", `${(-1.8 * dot.x) / 5 * 100 + 150}`);
            newDot.setAttribute("cy", `${150 - (-1.8 * dot.y) / 5 * 100}`);
          }
          if (dot.r == r) {
            newDot.setAttribute("fill", dot.result === true ? "green" : "red");
            newDot.setAttribute("r", "4.5");
          } else {
            newDot.setAttribute("fill", "black");
            newDot.setAttribute("opacity", `${((r - 0.5 < dot.r) && (r + 0.5 > dot.r)) ? "0.5" : "0.1"}`);
          }
          svg.appendChild(newDot);
        })
      }
    },

    // Различные виды валидации
    validateForm(){
      if ((this.x >= -5 && this.x <= 3) &&
          (this.y >= -3 && this.y <= 5) &&
          (this.r >= -3 && this.r <= 5) &&
          (!isNaN(this.x) && !isNaN(this.y) && !isNaN(this.r)) &&
          (this.x.trim() !== '' && this.y.trim() !== '' && this.r.trim() !== '')){
        this.addDots(this.x, this.y);
      } else {
        this.AxiosErrorHandler("Проверте введенные данные");
      }
    },
    validateFromGraph(){
      let position = getMousePosition(document.getElementById("graph"), event);
      this.xGraph = ((position.x - 150) / 100 * this.r).toFixed(2);
      this.yGraph = ((150 - position.y) / 100 * this.r).toFixed(2);
      if (this.r >= -3 && this.r <= 5) {
        this.addDots(this.xGraph, this.yGraph);
      } else {
        this.AxiosErrorHandler("Проверте значение R");
      }
      function getMousePosition(element, event) {
        let rect = element.getBoundingClientRect();
        return {
          x: event.clientX - rect.left,
          y: event.clientY - rect.top
        };
      }
    },
    validateInput(firstNumber, secondNumber, className, val){
      let element = document.getElementById(className);
      let buttonSubmit = document.getElementById("submit-button")
      if (parseFloat(val) < firstNumber || parseFloat(val) > secondNumber || isNaN(val)){
        element.classList.add("red");
        element.setAttribute("style", "color: red");
        buttonSubmit.setAttribute("disabled", "true");
        console.log(buttonSubmit)
        return false;
      } else {
        element.classList.remove("red");
        element.removeAttribute("style");
        buttonSubmit.removeAttribute("disabled");
        return true;
      }
    },

    // Вывод сообщений ошибок
    AxiosErrorHandler(errorText){
      this.$notify({
        group: 'error',
        title: 'Error',
        text: errorText,
        type: 'error'
      })
    }
  },
  mounted() {
    this.loadDots();
    this.drawDots();
  }
}
</script>

<style scoped>
.container{
  margin: 30px auto auto;
  background-color: ghostwhite;
  padding: 20px;
  display: table;
  text-align: center;
  box-shadow: 0 0 10px 1px black;
}

/*
    Отвечает за график (левая часть контейнера)
*/
.svg-wrapper{
  background-color: white;
  border: 2px solid aqua;
  box-shadow: 0 0 5px 0 aqua;
  border-radius: 20%;
  padding: 15px;
  margin-bottom: 10px;
}

/*
    Ввод данных (Правая часть)
*/
.inputs{
  background-color: pink;
  padding: 1px 50px 15px 50px;
  border-radius: 5%;
}

.text-center{
  margin: auto 30px;
  display: inline-block;
}

.text-input{
  text-align: center;
}

/*
    Для кнопок отправки и очистки таблицы
*/
.special-button button{
  color: white;
  margin: 20px 5px 0;
  letter-spacing: 1px;
  display: inline-block;
  text-decoration: none;
  background-color: black;
  border: 2px solid black;
  border-radius: 6px;
}

.special-button button:hover{
  color: black;
  transition: 0.5s;
  border: 2px solid red;
  background-color: ghostwhite;
}

/*
    Формат таблицы на планшетах/компьютерах
 */

@media(min-width: 800px) {
  .table-check {
    color: white;
    margin: 20px auto 0;
    border: 1px solid black;
    background-color: black;
  }

  .table-header th {
    padding: 5px 25px 5px 25px;
    font-size: 1.2em;
    font-weight: lighter;
  }

  .table-row td {
    font-size: 1.2em;
    text-align: center;
    font-weight: lighter;
  }

  .table-row:nth-of-type(odd) {
    margin: 0;
    background-color: #1f1f1f;
  }

  .container-table {
    margin: 30px auto 0 auto;
    padding: 20px 10px 10px 5px;
    display: table;
    text-align: center;
  }
}

/*
    Формат таблицы на телефонах
 */

@media(max-width: 799px) {
  .table-check {
    color: white;
    margin: 20px auto 0;
    border: 1px solid black;
    background-color: black;
  }

  .table-header th {
    padding: 5px 10px 5px 10px;
    font-size: 1.2em;
    font-weight: lighter;
  }

  .table-row td {
    font-size: 1.2em;
    text-align: center;
    font-weight: lighter;
  }

  .table-row:nth-of-type(odd) {
    margin: 0;
    background-color: #1f1f1f;
  }

  .container-table {
    margin: 30px auto 0 auto;
    padding: 20px 10px 10px 5px;
    display: table;
    text-align: center;
  }
}

.red{
  border: 2px solid red;
}
</style>