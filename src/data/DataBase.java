package data;

import game.Monkey;
import game.Game;

import java.sql.*;
import java.util.ArrayList;

/**
 * Класс отвечающий за подключение к базе данных и запись выживших Агентов в нее
 */
public class DataBase {
	
	static private int gameId = 0;

    /**
     * Подключение к базе данных
     * @return <code>Connection</code>
     * @throws ClassNotFoundException
     * @throws SQLException
     * @see Connection
     */
    private static Connection getConn() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");

        String dbUrl = StaticResource.dbUrl + StaticResource.login + StaticResource.password;

        Connection connect = DriverManager.getConnection(dbUrl);

        return connect;
    }

    /**
     * Добавляет Агентов в базу данных
     * @param agents массив Агетов
     */
    public static void addAgents(ArrayList<Monkey> agents) {
        try {
            Connection conn = getConn();

            String query = "insert into agents (id, gameId, name, x, y, energy)"
                    + " values (DEFAULT, ?, ?, ?, ?, ?)";


            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, gameId);
            gameId++;
            

            for (Monkey agent : agents) {
            	preparedStmt.setString(2, agent.getName());
                preparedStmt.setDouble(3, agent.getX());
                preparedStmt.setDouble(4, agent.getY());
                preparedStmt.setDouble(5, agent.getEnergy());

                preparedStmt.execute();
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
