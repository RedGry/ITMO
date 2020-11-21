$('.button-input').click(function () {
    $('.button-input-selected')
        .add(this)
        .toggleClass('button-input-selected')
        .toggleClass('button-input');
})

// Проверка на попадание точки на график
function checkResult(x, y, r){
    let isInCircle = (x, y, r) =>
        x >= 0 &&
        y <= 0 &&
        x*x + y*y <= r*r / 4;
    let isInTriangle = (x, y, r) =>
        x <= 0 &&
        y <= 0 &&
        y >= -r/2 &&
        x >= -r/2 &&
        y >= -x - r/2;
    let isInRectangle = (x, y, r) =>
        x <= 0 &&
        y>= 0 &&
        y <= r &&
        x >= -r/2;
    return isInCircle(x, y, r) || isInTriangle(x, y, r) || isInRectangle(x, y, r);
}

// Получаем значение R
function getRValue(){
    let rValue = parseFloat($("input[name=\"newEntryForm:R_field_input\"]").attr("aria-valuenow"));
    if (isNaN(rValue)){
        rValue = parseFloat($("tbody tr").last().find(">:nth-child(3)").text());
        if (isNaN(rValue)){
            rValue = DEFAULT_R_VALUE;
        }
    }
    return rValue;
}

function fixYField(){
    $("input[name=\"newEntryForm:Y_field\"]").removeAttr("type")
    $("input[name=\"newEntryForm:Y_field\"]").attr("type", "number")
}

// При нажатии на график выполнить функцию из graph.js
$(".svg-wrapper svg").click(clickPlotHandler);

// Перерисовка графика по изменению R
$("input[name=\"newEntryForm:R_field_input\"]").change(function (){
    deleteAllPointsFromPlot();
    drawDotsFromTable();
})

// Выполняемые функции при загрузке
fixYField();
drawDotsFromTable();


