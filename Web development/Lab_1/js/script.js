let X, Y, R;
const GRAPH_WIDTH = 300;
const GRAPH_HEIGHT = 300;
const yTextField = document.getElementById("Y-text");
const rTextField = document.getElementById("R-text")

document.addEventListener("DOMContentLoaded", function (){
    document.getElementById("submit-button").addEventListener("click", submit);
    document.getElementById("clear-button").addEventListener("click", clearButton);
});

function checkY(){
    let Y_text = document.getElementById("Y-text");
    Y = Y_text.value.replace(",", ".");
    if (Y.trim() === ""){
        Y_text.setCustomValidity("Заполните поле");
        return false;
    } else if (!isFinite(Y)){
        Y_text.setCustomValidity("Должно быть число!");
        return false;
    } else if (Y > 5 || Y < -5){
        Y_text.setCustomValidity("Вы вышли за диапазон [-5; 5]!");
        return false;
    }
    Y_text.setCustomValidity("");
    return true;
}

function checkR(){
    let R_text = document.getElementById("R-text");
    R = R_text.value.replace(",", ".");
    if (R.trim() === ""){
        R_text.setCustomValidity("Заполните поле");
        return false;
    } else if (!isFinite(R)){
        R_text.setCustomValidity("Должно быть число!");
        return false;
    } else if (R < 0){
        R_text.setCustomValidity("Радиус не может быть отрицательным")
        return false;
    } else if (R > 4 || R < 1){
        R_text.setCustomValidity("Вы вышли за диапазон [1; 4]!");
        return false;
    }
    R_text.setCustomValidity("");
    return true;
}

function setX(){
    let formData = new FormData(document.getElementById("coordinates-form"));
    X = formData.get("x");
}

function calculateX(x, r){
    return x / r * 100 + GRAPH_WIDTH / 2;
}

function calculateY(y, r){
    return GRAPH_HEIGHT / 2 - y / r * 100;
}

const submit = function (e){
    if (!checkY()) return;
    if (!checkR()) return;
    setX();
    e.preventDefault();

    let point = $("#point");
    let request = ("?x=" + X + "&y=" + Y + "&r=" + R);
    const xGraph = calculateX(X, R), yGraph = calculateY(Y, R);

    point.attr({
        cx: xGraph,
        cy: yGraph,
        visibility: "visible"
    });

    fetch("php/check.php" + request)
        .then(response => response.text())
        .then(response => document.getElementById("check").innerHTML = response);
};

const clearButton = function (e){
    e.preventDefault();
    fetch("php/clear_table.php")
        .then(response => response.text())
        .then(response => document.getElementById("check").innerHTML = response)
};

function changePoint(){
    let point = $("#point");
    let formData = new FormData(document.getElementById("coordinates-form"));
    X = formData.get("x").replace(",", ".");
    Y = formData.get("y").replace(",", ".");
    R = formData.get("r").replace(",", ".");
    const xGraph = calculateX(X, R), yGraph = calculateY(Y, R);

    point.attr({
        cx: xGraph,
        cy: yGraph,
        visibility: "visible"
    });
}

$("input:checkbox").click(function (){
    let group = "input:checkbox[name='" + $(this).prop("name") + "']";
    $(group).prop("checked", false);
    $(this).prop("checked", true);
}).on("change", e =>{
    changePoint();
});

yTextField.addEventListener("change", e => {
   changePoint();
});

rTextField.addEventListener("change", e => {
    changePoint();
});