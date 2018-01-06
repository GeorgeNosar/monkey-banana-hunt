	//Объявляем глобальные переменные
	var isGame = false;
    var mapSize = 500;

    var img = new Image;
    img.src = "res/monkey.png";
    
    var background = new Image;
    var banana = new Image;
    background.src = "res/map-bg.jpg";
    banana.src = "res/banana.png";

    var canv = document.getElementById("map");
    var mainContext = canv.getContext('2d');

    //Создаем подключение к вебсокету
    var wsUrl = "ws://" + "localhost:8080/agents-war/" + "control";
    console.log(wsUrl);
    var ws = new WebSocket(wsUrl);

    //Отправляем команды
    function start() {
        ws.send("Start");
        console.log("Send: Start");
    }
    function stop() {
        ws.send("Stop");
        console.log("Send: Stop");
    }
    //обработчики onopen onclose onmessage
    ws.onopen = function(evt) { console.log("connect onOpen") };
    ws.onclose = function(evt) { console.log("connect onClose") };

    ws.onmessage = function (evt) {
        agents = [];
        foods = [];
        var m = JSON.parse(evt.data, function(k, v) { //получаем json о текущем состоянии игры и парсим его
            if (k == "agents") {
                agents = v;
            } else if (k == "foods") {
                foods = v;
            } else if (k == "isGame") {
                isGame = v
            }
            return v;
        });
        if (isGame){
            setTimeout(drawAgents, 15); //обновляем игровое поле
        }
    }
    
    //Начальное поле при загрузке страницы
    window.onload = function (){
        canv.width = mapSize;
        canv.height = mapSize;
        mainContext.drawImage(background, 0,0,mapSize, mapSize);
    }

    //Контейнеры для полученных объектов
    var agents = [];
    var foods = [];
    agentSize = 80;
    foodSize = 40;

    //Функция, рисующая игровое поле
    function drawAgents() {
        mainContext.drawImage(background, 0, 0, mapSize, mapSize);
        for (var i = 0; i < agents.length; i++) {
        	mainContext.drawImage(img, agents[i]["x"], agents[i]["y"], agentSize, agentSize);
            mainContext.fillText(parseInt(agents[i]["energy"]), agents[i]["x"], agents[i]["y"]);
        }
		
        for (var i = 0; i < foods.length; i++) {
            size = foods[i]["size"];
            mainContext.drawImage(banana, foods[i]["x"], foods[i]["y"], foodSize, foodSize);
        }
    }