package data;

import game.Monkey;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/**
 * Класс отвечающий за считывание файла настроект игры
 */
public class PropertiesGame extends Properties {
    private static PropertiesGame pg = new PropertiesGame();

    public static void setGameProperties() {

        try{
            pg.load(new FileInputStream("C:\\Users\\ov3r\\eclipse-workspace\\agents-war\\config.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StaticResource.agentSpeed = Double.valueOf(pg.getProperty("agentSpeed"));
        StaticResource.agentDamage = Double.valueOf(pg.getProperty("agentDamage"));
        StaticResource.energyLost = Double.valueOf(pg.getProperty("energyLost"));
        StaticResource.bananaEnergy = Double.valueOf(pg.getProperty("bananaEnergy"));
        StaticResource.dbUrl = pg.getProperty("dbUrl");
        StaticResource.login = pg.getProperty("login");
        StaticResource.password = pg.getProperty("password");
        StaticResource.timeToMove = Integer.valueOf(pg.getProperty("timeToMove"));
        StaticResource.mapSize = Integer.valueOf(pg.getProperty("mapSize"));

    }

}

