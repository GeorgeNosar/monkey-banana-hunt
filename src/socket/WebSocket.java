package socket;

import game.Game;
import game.Banana;
import game.Monkey;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Класс отвечающий за соединение веб клиента с серверной частью
 */
@ServerEndpoint("/control")
public class WebSocket {

    /**
     * Текущая сессия подключения
     */
    private static Session peer;

    /**
     * Метод обрабатывающий поступающие сообщение
     * @param message текст сообщения
     */
    @OnMessage
    public void onMessage(String message) {
        if(message.equals("Start")){
            Game.getInstance().restart();
            Game.getInstance().start();
        }else if (message.equals("Stop")){
            Game.getInstance().stop();
        }
    }

    /**
     * Метод срабатывает при закрытии сессии
     * @param session
     */
    @OnClose
    public void close(Session session) {
        System.out.println("close");
    }

    /**
     * Метод присваевает текущую сессию
     * @param s сессия
     */
    @OnOpen
    public void onOpen(Session s) {
        peer = s;
    }

    /**
     * Метод отвечающий за обновление изображения объектов на карте
     * @param agents текущие агенты
     * @param bananas текущие бананы
     * @throws IOException Если вход или выход
     *                       Исключено событие
     */
    public static void updateGame(ArrayList<Monkey> agents, ArrayList<Banana> bananas) throws IOException {
        String json = gameToJson(agents,bananas);
        peer.getBasicRemote().sendText(json);
    }

    /**
     * Преобразует контейнер Агентов и пирогов в тект JSON
     * @param agents контейнер агентов
     * @param bananas контейнер бананов
     * @return строку JSON
     */
    public static String gameToJson(ArrayList<Monkey> agents, ArrayList<Banana> bananas){
        String json = "{\"agents\": [";
        try {
            ArrayList<String> agentsStr = new ArrayList<>();
            for (Monkey agent : agents) {
                if (agent.isAlive()) {
                    agentsStr.add("{\"x\": " + agent.getX()
                            + ", \"y\": " + agent.getY()
                            + ", \"energy\": " + agent.getEnergy()
                            + "}");
                }
            }
            json += String.join(",", agentsStr);
            json += "],\"foods\": [";

            ArrayList<String> foodStr = new ArrayList<>();
            for (Banana banana : bananas) {
                foodStr.add("{\"x\": " + banana.getX() +
                        ", \"y\": " + banana.getY() +
                        ", \"energy\": " + banana.getEnergy() + "}");
            }
            json += String.join(",", foodStr);
            json += "], \"isGame\": " + Game.getInstance().isGame + "}";

        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }

        return json;
    }

}