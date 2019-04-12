package com.stas.telegrambots;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.Map;

public class BotStart extends TelegramLongPollingBot {
    private String year = "";
    private String month = "";
    private String day = "";

     private BaseDate baseDate;

    BotStart() {
        baseDate=new BaseDate();
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage message = new SendMessage();


        if (update.hasMessage() && update.getMessage().hasText()) {
            String name=update.getMessage().getFrom().getFirstName();
            name=name.replaceAll("[^\\w]"," ");
            long chatId = update.getMessage().getChatId();
            executeText(name, update.getMessage().getText(), message, chatId);
        }

        if (update.hasCallbackQuery()) {
            String call_data = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();


            if (!executeCommand(call_data, message, chatId)) {
                DateDetect.dateFind(call_data);
                year = DateDetect.yearD;
                day = DateDetect.dayD;
                month = DateDetect.monthD;

            }

        }
    }


            private void lider(SendMessage message, long chatId) {

                try {
                    Map<Integer, String> data = baseDate.getData("select Username,data from users ORDER BY data asc");



                    message.setChatId(chatId).setText("Лидер :\n " + data.get(1) + " Бросил курить : " + data.get(2) + "\n  Наш канал @nosmok1ng");

                    execute(message);
                } catch (TelegramApiException | SQLException e) {
                    e.printStackTrace();
                }

            }

    private void enter(SendMessage message, long chatId)  {
        String d = year + "-" + month + "-" + day + " 12:00:00";
        message.setChatId(chatId).setText("Вы ввели дату :" + d);

        try {
            baseDate.update("update  users set data ='" + d + "' where userId=" + chatId + " ");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        message.setText("ваша дата " + d);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
    private void day(SendMessage message, long chatId) {
        message.setChatId(chatId).setText("Выберете дату");
        message.setReplyMarkup(new KeyBoard().markupInline(Key.days));
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void month(SendMessage message, long chatId) {
        message.setChatId(chatId).setText("Выберете месяц");
        message.setReplyMarkup(new KeyBoard().markupInline(Key.months));
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void year(SendMessage message, long chatId) {
        message.setChatId(chatId).setText("Выберете год");
        message.setReplyMarkup(new KeyBoard().markupInline(Key.years));
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void time1(SendMessage message, long chatId) {
        message.setChatId(chatId).setText("Выберете дату");
        message.setReplyMarkup(new KeyBoard().markupInline(Key.choice));
        try {
            execute(message);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private void time(SendMessage message, long chatId) {
        message.setChatId(chatId).setText("Вы не курите");

        try {

            Map<Integer, String> data =baseDate.getData("SELECT\n" +
                    "CONCAT(DATEDIFF(now(),users.data), 'д ', REPLACE(LEFT(TIMEDIFF(now(),users.data + INTERVAL DATEDIFF(now(), users.data) DAY), 5), ':', 'ч '), 'м')\n" +
                    "from users where userId=" + chatId + "");
            message.setText(data.get(1));
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
    }
    private void go(SendMessage message, long chatId) {
        message.setChatId(chatId).setText("Вы бросили курить");
        try {
            baseDate.update("insert into users (userId,data) values (" + chatId + ",now())");
        } catch (SQLException e) {
            try {
                baseDate.update("update  users set data =now() where userId=" + chatId + "");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void error(SendMessage message, long chatId) {
        message.setChatId(chatId).setText("Недопустимая команда!\n Для вызова меню набери команду /help\n  У нас есть канал @nosmok1ng ");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void start(String firstName, SendMessage message, long chatId) {
            message.setChatId(chatId).setText("Привет ," + firstName + " ,для вызова меню набери команду /help.Значение кнопок: **Бросаю курить** дает старт твоему некурению ,кнопка **Уже бросил** позволяет ввести дату и время ,когда ты бросил вручную,кнопка **Сколько не курю** выводит время ,сколько ты являешься некурящим человеком,ну а **Список лидеров** выводит самых крутых.\n  У нас есть канал @nosmok1ng ))");

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
    private void help(String firstName, SendMessage message, long chatId) {
        try {
            baseDate.update("insert into users (userId,data,UserName) values (" + chatId + ",now(),'" + firstName + "')");
        } catch (SQLException e) {
            try {
                baseDate.update("update  users set UserName ='" + firstName + "' where userId=" + chatId + " ");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

        message.setChatId(chatId).setText("Ну что ж " + firstName + " начни свой путь к здоровому образу жизни");
        message.setReplyMarkup(new KeyBoard().markupInline(Key.start));
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private boolean executeCommand(String command, SendMessage message, long chatId) {
        boolean ret = true;

        switch (command) {
            case "go":
                go(message, chatId);
             break;
            case "time":
                time(message, chatId);
             break;
            case "time1":
                time1(message, chatId);
            break;
            case "year":
                year(message, chatId);
            break;
            case "month":
                month(message, chatId);
            break;
            case "day":
                day(message, chatId);
                break;
            case "enter":
                enter(message, chatId);
             break;
            case "lider":
                lider(message, chatId);
            break;
            default:
                ret = false;
            break;
        }

        return ret;
    }

    private boolean executeText(String firstName, String command, SendMessage message, long chatId) {
        boolean ret = true;

        switch (command) {
            case "/help":
                help(firstName, message, chatId);
                break;
            case "/start":
                start(firstName, message, chatId);
                break;
            default:
                error(message, chatId);
                ret = false;
                break;
        }

        return ret;
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
