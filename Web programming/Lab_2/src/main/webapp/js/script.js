$(document).ready(function () {
    let x, y, r;
    const svgSize = 300;

    // При загрузски страницы брать прошлое значение R из таблицы

    let rPrevious = document.querySelector("#output > table > tbody > tr:nth-child(1) > td:nth-child(3)");
    if (rPrevious) {
        r = Number(rPrevious.textContent);
        $('.button-input[name=\"r\"]' + '[value=\"' + Number(rPrevious.textContent) + '\"]')
            .toggleClass('button-input-selected')
            .toggleClass('button-input');
    }

    function doRequest(x, y, r){
        $.ajax({
            type: 'GET',
            url: 'controller',
            data: {
                'x': x,
                'y': y,
                'r': r
            },
            success: function () {
                window.location.href = 'jsp/result.jsp';
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert("Проверка на Сервер не прошла :(");
            }
        });
    }

    // Нажатие на график -> добавление точки и проверка

    $('svg').mousedown(function (e) {
        if (r){
            const position = $('.svg-wrapper').offset();
            const rowX = e.pageX - position.left;
            const rowY = e.pageY - position.top;

            //const xGraph = svgSize - (svgSize / 2 - rowX + 167).toFixed(1);
            //const yGraph = svgSize - (svgSize / 2 - rowY + 167).toFixed(1);

            const x = (((r / 50) * (svgSize / 2 - rowX) * -1) / 2 - 0.2 * r).toFixed(2);
            const y = (((r / 50) * (svgSize / 2 - rowY)) / 2 + 0.2 * r).toFixed(2);

            doRequest(x, y, r);
        } else {
            alert('Выберите пожалуйства радиус (R)');
        }
    })

    //Выбор R и X

    $('.button-input[name=\"r\"]').click(function () {
        $('.button-input-selected[name=\"r\"]')
            .add(this)
            .toggleClass('button-input-selected')
            .toggleClass('button-input');
        r = Number($('.button-input-selected[name=\"r\"]').attr('value'));
    });

    $('.button-input-selected[name=\"r\"]').click(function () {
        $('.button-input-selected[name=\"r\"]')
            .add(this)
            .toggleClass('button-input-selected')
            .toggleClass('button-input');
        r = Number($('.button-input-selected[name=\"r\"]').attr('value'));
    });

    $('.button-input[name=\"x\"]').click(function () {
        $('.button-input-selected[name=\"x\"]')
            .add(this)
            .toggleClass('button-input-selected')
            .toggleClass('button-input');
        x = Number($('.button-input-selected[name=\"x\"]').attr('value'));
    });

    // Отправка на проверку данных

    $('#submit-button').on('click', function () {
        let value = $('#input-y').val().replace(',', '.').trim();

        if (value !== ''){
            value = Number(value);
            if ((value >= -5) && (value <= 3)){
                y = value;
            } else {
                alert('Введен некорректный Y');
                $('input-y').val('');
                return;
            }
        }

        if (x !== undefined && y !== undefined && r !== undefined){
            doRequest(x, y, r);
        } else {
            alert('Не все поля заполнены!');
        }
    })
})