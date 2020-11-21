const SVG_SIZE = 300;
const CANVAS_R_VALUE = 100;
const DEFAULT_R_VALUE = 0.1;

// Функции для вычисления Y и X

function fromTableToSvgX(x){
    return x / getRValue() * CANVAS_R_VALUE + SVG_SIZE / 2;
}

function fromTableToSvgY(y){
    return SVG_SIZE / 2 - y / getRValue() * CANVAS_R_VALUE;
}

function fromSvgToRX(x){
    return getRValue() * (x - SVG_SIZE / 2) / CANVAS_R_VALUE;
}

function fromSvgToRY(y){
    return getRValue() * (SVG_SIZE / 2 - y) / CANVAS_R_VALUE;
}


// Рисует с таблицы точки
function drawDotsFromTable(){
    deleteAllPointsFromPlot()
    setTimeout(() =>

    $("tbody tr").each(function (){
        const point = $(this);

        const x = parseFloat(point.find("td:first-child").text());
        const y = parseFloat(point.find("td:nth-child(2)").text());

        if (isNaN(x) || isNaN(y)) return;

        const color = checkResult(x, y, getRValue()) ? 'green' : 'red';

        const plot = $(".svg-wrapper svg");

        const existingContent = plot.html();
        const contentToInsert = `<circle class="dot" 
                                         r="4" 
                                         cx="${fromTableToSvgX(x)}" 
                                         cy="${fromTableToSvgY(y)}" 
                                         fill="${color}"/>`;
        plot.html(existingContent + contentToInsert);
    }), 200);
}

// Удаляет точки с графика
function deleteAllPointsFromPlot(){
    $(".dot").remove();
}

// Нажатие на график и отправка формы
function clickPlotHandler(e){
    const offset = $(this).offset();
    const x = e.pageX - offset.left;
    const y = e.pageY - offset.top;

    let xValue = fromSvgToRX(x);
    let yValue = fromSvgToRY(y);
    const rValue = getRValue();

    $(".pointX").val(xValue.toFixed(2));
    $(".pointY").val(yValue.toFixed(2));
    $(".pointR").val(rValue);
    $(".submitSvg").click();

    drawDotsFromTable();
}