import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

public class BotStart extends TelegramLongPollingBot {
    String year = "";
    String month = "";
    String day = "";

    @Override
    public void onUpdateReceived(Update update) {

        String d = year + "-" + month + "-" + day + " 12:00:00";
        SendMessage message = new SendMessage();


        if (update.hasMessage() && update.getMessage().hasText()) {
            long chat_id = update.getMessage().getChatId();
            if (update.getMessage().getText().equals("/help")) {
                String name = update.getMessage().getFrom().getFirstName();
                try {

                    BaseDate.baseRead("insert into users (userId,data,UserName) values (" + chat_id + ",now(),'" + name + "')");

                } catch (SQLException e) {
                    try {
                        BaseDate.baseRead("update  users set UserName ='" + name + "' where userId=" + chat_id + " ");
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }

                message.setChatId(chat_id).setText("Ну что ж " + name + " начни свой путь к здоровому образу жизни");

                message.setReplyMarkup(new KeyBoard().markupInline(Key.start));
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (update.getMessage().getText().equals("/start")) {
                String name = update.getMessage().getFrom().getFirstName();
                message.setChatId(chat_id).setText("Привет ," + name + " ,для вызова меню набери команду /help.Значение кнопок: **Бросаю курить** дает старт твоему некурению ,кнопка **Уже бросил** позволяет ввести дату и время ,когда ты бросил вручную,кнопка **Сколько не курю** выводит время ,сколько ты являешься некурящим человеком,ну а **Список лидеров** выводит самых крутых.\n  У нас есть канал @nosmok1ng ))");


                try {
                    execute(message);

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }


            }

        }
        if (update.hasCallbackQuery()) {


            String call_data = update.getCallbackQuery().getData();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();


            if (call_data.equals("go")) {
                message.setChatId(chat_id).setText("Вы бросили курить");

                try {

                    BaseDate.baseRead("insert into users (userId,data) values (" + chat_id + ",now())");
                } catch (SQLException e) {
                    try {

                        BaseDate.baseRead("update  users set data =now() where userId=" + chat_id + "");
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (call_data.equals("time")) {

                message.setChatId(chat_id).setText("Вы не курите");

                try {

                    BaseDate.baseWrite("SELECT\n" +
                            "CONCAT(DATEDIFF(now(),users.data), 'д ', REPLACE(LEFT(TIMEDIFF(now(),users.data + INTERVAL DATEDIFF(now(), users.data) DAY), 5), ':', 'ч '), 'м')\n" +
                            "from users where userId=" + chat_id + "");
                    message.setText(BaseDate.res.getString(1));
                    execute(message);
                } catch (SQLException | TelegramApiException e) {
                    message.setText("Полегче ,ты еще не бросил.Сначала нажми кнопку **Бросаю курить** или введи правильный формат даты вручную");
                    try {
                        execute(message);
                    } catch (TelegramApiException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
            } else if (call_data.equals("time1")) {

                message.setChatId(chat_id).setText("Выберете дату");

                message.setReplyMarkup(new KeyBoard().markupInline(Key.choice));


                try {
                    execute(message);

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            }

          else   if (call_data.equals("year")) {
                message.setChatId(chat_id).setText("Выберете год");

                message.setReplyMarkup(new KeyBoard().markupInline(Key.years));
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (call_data.equals("month")) {
                message.setChatId(chat_id).setText("Выберете месяц");

                message.setReplyMarkup(new KeyBoard().markupInline(Key.months));
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();

                }

            } else if (call_data.equals("day")) {
                message.setChatId(chat_id).setText("Выберете дату");

                message.setReplyMarkup(new KeyBoard().markupInline(Key.days));
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();

                }


            } else if (call_data.equals("enter")) {

                message.setChatId(chat_id).setText("Вы ввели дату :" + d);
                try {
                    BaseDate.baseRead("update  users set data ='" + d + "' where userId=" + chat_id + " ");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                message.setText("ваша дата " + d);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            } else if (call_data.equals("lider")) {
                try {
                    BaseDate.baseWrite("select Username,data from users ORDER BY data asc");

                    message.setChatId(chat_id).setText("");
                    message.setText("Лидер :\n " + BaseDate.res.getString(1) + " Бросил курить : " + BaseDate.res.getString(2) + "\n  Наш канал @nosmok1ng");
                    execute(message);
                } catch (SQLException | TelegramApiException e) {
                    e.printStackTrace();
                }


            } else  {

                DateDetect.dateFind(call_data);
                year = DateDetect.yearD;
                day = DateDetect.dayD;
                month = DateDetect.monthD;
            }
        }
    }

    @Override
    public String getBotUsername() {

        return "";
    }

    @Override
    public String getBotToken() {

        return "";
    }
}
