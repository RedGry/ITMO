<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<html lang="ru">
<head>
    <meta charset="UTF-8">
    <link rel="shortcut icon" href="img/redgry.png">
    <title>RedGry and LeartAule Project</title>

    <link rel="stylesheet main" href="<%= request.getContextPath() %>/css/main.css">
    <link rel="stylesheet index" href="<%= request.getContextPath() %>/css/index.css">
    <link rel="stylesheet table" href="<%= request.getContextPath() %>/css/table.css">
    <link rel="stylesheet header" href="<%= request.getContextPath() %>/css/header_style.css">
    <link rel="stylesheet footer" href="<%= request.getContextPath() %>/css/footer_style.css">
</head>

<body>
<header>
    <div class="header">
        <div>
            <span class="lab-name">Лабораторная работа №2</span>
        </div>
        <div>
            <span>
                <a target="_blank" href="https://vk.com/egor_redgry/">Кривоносов Егор</a>
                , P3211
            </span>
            <span>
                <a target="_blank" href="https://vk.com/l514c44660">Кондрашов Кирилл</a>
                , P3211
            </span>
            <span>
                Вариант
                <a target="_blank" href="https://github.com/RedGry/ITMO/tree/master/Web%20development">4556</a>
            </span>
        </div>
    </div>
</header>

<section class="main">
    <div class="container">
        <div class="row">
            <div class="text-center">
                <div class="graph">
                    <div class="svg-wrapper">
                        <svg height="300" width="300" xmlns="http://www.w3.org/2000/svg">
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

                            <!-- Подписи к делениям и осям -->
                            <text fill="black" x="195" y="140">R/2</text>
                            <text fill="black" x="250" y="140">R</text>

                            <text fill="black" x="40" y="140">-R</text>
                            <text fill="black" x="85" y="140">-R/2</text>

                            <text fill="black" x="160" y="55">R</text>
                            <text fill="black" x="160" y="105">R/2</text>

                            <text fill="black" x="160" y="204">-R/2</text>
                            <text fill="black" x="160" y="255">-R</text>

                            <text fill="black" x="285" y="140">X</text>
                            <text fill="black" x="160" y="15">Y</text>

                            <!-- Первая фигура (1 четверть) -->
                            <polygon fill="blue"
                                     fill-opacity="0.2"
                                     stroke="blue"
                                     points="150,150 200,150 150,50"></polygon>

                            <!-- Вторая фигура (2 четверть) -->
                            <polygon fill="blue"
                                     fill-opacity="0.2"
                                     stroke="blue"
                                     points="50,50 50,150 150,150 150,50"></polygon>

                            <!-- Третья фигура (3 четверть) -->
                            <path fill="blue"
                                  fill-opacity="0.2"
                                  stroke="blue"
                                  d="M 50 150 A 100 100, 90, 0, 0, 150 250 L 150 150 Z"></path>

                            <!-- Точки на графике -->
                            <jsp:include page="jsp/dots.jsp"/>

                        </svg>
                    </div>
                </div>
            </div>

            <div class="text-center">
                <div class="inputs">
                    <p>Выберите R:</p>
                    <div class="row">
                        <div class="text-center-button">
                            <label class="button-label">
                                <input type="button" class="button-input" name="r" value="1"/>
                            </label>
                        </div>
                        <div class="text-center-button">
                            <label class="button-label">
                                <input type="button" class="button-input" name="r" value="1.5"/>
                            </label>
                        </div>
                        <div class="text-center-button">
                            <label class="button-label">
                                <input type="button" class="button-input" name="r" value="2"/>
                            </label>
                        </div>
                        <div class="text-center-button">
                            <label class="button-label">
                                <input type="button" class="button-input" name="r" value="2.5"/>
                            </label>
                        </div>
                        <div class="text-center-button">
                            <label class="button-label">
                                <input type="button" class="button-input" name="r" value="3"/>
                            </label>
                        </div>
                    </div>

                    <p>Выберите Х:</p>
                    <div class="row">
                        <div class="text-center">
                            <label class="button-label">
                                <button class="button-input" name="x" value="-4">-4</button>
                            </label>
                            <label class="button-label">
                                <button class="button-input" name="x" value="-3">-3</button>
                            </label>
                            <label class="button-label">
                                <button class="button-input" name="x" value="-2">-2</button>
                            </label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="text-center">
                            <label class="button-label">
                                <button class="button-input" name="x" value="-1">-1</button>
                            </label>
                            <label class="button-label">
                                <button class="button-input" name="x" value="0">0</button>
                            </label>
                            <label class="button-label">
                                <button class="button-input" name="x" value="1">1</button>
                            </label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="text-center">
                            <label class="button-label">
                                <button class="button-input" name="x" value="2">2</button>
                            </label>
                            <label class="button-label">
                                <button class="button-input" name="x" value="3">3</button>
                            </label>
                            <label class="button-label">
                                <button class="button-input" name="x" value="4">4</button>
                            </label>
                        </div>
                    </div>

                    <p>Выберите Y:</p>
                    <div class="row">
                        <label class="text-label">
                            <input id="input-y" class="text-input" type="text" name="y" placeholder="[-5; 3]" maxlength="14"/>
                        </label>
                    </div>
                </div>
                <div class="special-button">
                    <button id="submit-button" type="submit">Отправить на проверку</button>
                </div>
            </div>
        </div>
    </div>

    <div class="container-table">
        <div class="table">
            <div class="text-center">
                <div class="output" id="output">

                    <jsp:include page="jsp/table.jsp"/>

                </div>
            </div>
        </div>
    </div>
</section>

<footer>
    <div class="footer">
        <div class="logos">
            <a class="git-link" target="_blank" href="https://github.com/RedGry">
                <img class="logo" alt="Логотип"  src="<%= request.getContextPath() %>/img/redgry.png">
            </a>
            <span> & </span>
            <a class="git-link" target="_blank" href="https://github.com/LeartAule">
                <img class="logo" alt="Логотип"  src="<%= request.getContextPath() %>/img/leartaule.png">
            </a>
        </div>
    </div>
</footer>

<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/script.js"></script>
</body>
</html>
