import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, TelegramApiRequestException {

      Driver driver=new com.mysql.cj.jdbc.Driver();
      DriverManager.registerDriver(driver);


        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        BotStart bot = new BotStart();
        telegramBotsApi.registerBot(bot);
        }
    }

