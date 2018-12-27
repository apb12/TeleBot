import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Botstart1 extends TelegramLongPollingBot {
    String year = "";
    String month = "";
    String day = "";




    @Override
    public void onUpdateReceived(Update update) {

        String d = year + "-" + month + "-" + day + " 12:00:00";
        Bdate ss = new Bdate();


        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            if (update.getMessage().getText().equals("/help")) {
                String name = update.getMessage().getFrom().getFirstName();
                try {

                    ss.bd("insert into users (userId,data,UserName) values ("+chat_id+",now(),'"+name+"')");

                } catch (SQLException e) {
                    try {
                        ss.bd("update  users set UserName ='"+name+"' where userId="+chat_id+" ");
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }


                SendMessage message = new SendMessage()
                        .setChatId(chat_id)
                        .setText("Ну что ж " + name + " начни свой путь к здоровому образу жизни");
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
                rowInline.add(new InlineKeyboardButton().setText("Бросаю курить").setCallbackData("go"));
                rowInline.add(new InlineKeyboardButton().setText("Уже бросил").setCallbackData("time1"));
                rowInline1.add(new InlineKeyboardButton().setText("Сколько не курю?").setCallbackData("time"));
                rowInline1.add(new InlineKeyboardButton().setText("Список лидеров").setCallbackData("lider"));
                // Set the keyboard to the markup
                rowsInline.add(rowInline);
                rowsInline.add(rowInline1);
                // Add it to the message
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (update.getMessage().getText().equals("/start")) {
                String name = update.getMessage().getFrom().getFirstName();
                SendMessage message = new SendMessage()
                        .setChatId(chat_id)
                        .setText("Привет ," + name + " ,для вызова меню набери команду /help.Значение кнопок: **Бросаю курить** дает старт твоему некурению ,кнопка **Уже бросил** позволяет ввести дату и время ,когда ты бросил вручную,кнопка **Сколько не курю** выводит время ,сколько ты являешься некурящим человеком,ну а **Список лидеров** выводит самых крутых.\n  У нас есть канал @nosmok1ng и чат @nosmok1ngchat,заходи поговорим))");


                try {
                    execute(message);

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }


            } else {

            }

        } else if (update.hasCallbackQuery()) {

            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();

            if (call_data.equals("go")) {
                SendMessage message = new SendMessage()
                        .setChatId(chat_id)
                        .setText("Вы бросили курить");


                try {

                    ss.bd("insert into users (userId,data) values ("+chat_id+",now())");
                } catch (SQLException e) {
                    try {

                        ss.bd("update  users set data =now() where userId="+chat_id+"");
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
                SendMessage message = new SendMessage()
                        .setChatId(chat_id)
                        .setText("Вы не курите");

                try {

                    ss.bs("SELECT\n" +
                            "CONCAT(DATEDIFF(now(),users.data), 'д ', REPLACE(LEFT(TIMEDIFF(now(),users.data + INTERVAL DATEDIFF(now(), users.data) DAY), 5), ':', 'ч '), 'м')\n" +
                            "from users where userId="+chat_id+"");
                    message.setText(ss.res.getString(1));
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
                SendMessage message = new SendMessage()
                        .setChatId(chat_id)
                        .setText("Выберете дату");
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
                rowInline.add(new InlineKeyboardButton().setText("Год").setCallbackData("year"));
                rowInline.add(new InlineKeyboardButton().setText("Месяц").setCallbackData("month"));
                rowInline.add(new InlineKeyboardButton().setText("День").setCallbackData("day"));

                // Set the keyboard to the markup
                rowsInline.add(rowInline);
                // Add it to the message
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);


                try {
                    execute(message);

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            }
            if (update.hasCallbackQuery()) {


                ArrayList<String> datechange = new ArrayList<>();
                String call_data1 = update.getCallbackQuery().getData();
                long message_id1 = update.getCallbackQuery().getMessage().getMessageId();
                long chat_id1 = update.getCallbackQuery().getMessage().getChatId();
                if (call_data1.equals("year")) {
                    SendMessage message = new SendMessage()
                            .setChatId(chat_id1)
                            .setText("Выберете год");
                    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                    List<InlineKeyboardButton> rowInline = new ArrayList<>();
                    rowInline.add(new InlineKeyboardButton().setText("2016").setCallbackData("2016"));
                    rowInline.add(new InlineKeyboardButton().setText("2017").setCallbackData("2017"));
                    rowInline.add(new InlineKeyboardButton().setText("2018").setCallbackData("2018"));
                    rowInline.add(new InlineKeyboardButton().setText("2019").setCallbackData("2019"));


                    // Set the keyboard to the markup
                    rowsInline.add(rowInline);

                    // Add it to the message
                    markupInline.setKeyboard(rowsInline);
                    message.setReplyMarkup(markupInline);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (call_data1.equals("month")) {
                    SendMessage message = new SendMessage()
                            .setChatId(chat_id1)
                            .setText("Выберете месяц");
                    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                    List<InlineKeyboardButton> rowInline = new ArrayList<>();
                    List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
                    List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
                    rowInline.add(new InlineKeyboardButton().setText("январь").setCallbackData("j"));
                    rowInline.add(new InlineKeyboardButton().setText("февраль").setCallbackData("f"));
                    rowInline.add(new InlineKeyboardButton().setText("март").setCallbackData("m"));
                    rowInline.add(new InlineKeyboardButton().setText("апрель").setCallbackData("a"));
                    rowInline1.add(new InlineKeyboardButton().setText("май").setCallbackData("mm"));
                    rowInline1.add(new InlineKeyboardButton().setText("июнь").setCallbackData("i"));
                    rowInline1.add(new InlineKeyboardButton().setText("июль").setCallbackData("ii"));
                    rowInline1.add(new InlineKeyboardButton().setText("август").setCallbackData("av"));
                    rowInline2.add(new InlineKeyboardButton().setText("сентябрь").setCallbackData("s"));
                    rowInline2.add(new InlineKeyboardButton().setText("октябрь").setCallbackData("o"));
                    rowInline2.add(new InlineKeyboardButton().setText("ноябрь").setCallbackData("n"));
                    rowInline2.add(new InlineKeyboardButton().setText("декабрь").setCallbackData("d"));


                    // Set the keyboard to the markup
                    rowsInline.add(rowInline);
                    rowsInline.add(rowInline1);
                    rowsInline.add(rowInline2);

                    // Add it to the message
                    markupInline.setKeyboard(rowsInline);
                    message.setReplyMarkup(markupInline);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();

                    }

                } else if (call_data1.equals("day")) {
                    SendMessage message = new SendMessage()
                            .setChatId(chat_id1)
                            .setText("Выберете дату");
                    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                    List<InlineKeyboardButton> rowInline = new ArrayList<>();
                    List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
                    List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
                    List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
                    List<InlineKeyboardButton> rowInline4 = new ArrayList<>();
                    rowInline.add(new InlineKeyboardButton().setText("1").setCallbackData("1"));
                    rowInline.add(new InlineKeyboardButton().setText("2").setCallbackData("2"));
                    rowInline.add(new InlineKeyboardButton().setText("3").setCallbackData("3"));
                    rowInline.add(new InlineKeyboardButton().setText("4").setCallbackData("4"));
                    rowInline.add(new InlineKeyboardButton().setText("5").setCallbackData("5"));
                    rowInline.add(new InlineKeyboardButton().setText("6").setCallbackData("6"));
                    rowInline.add(new InlineKeyboardButton().setText("7").setCallbackData("7"));
                    rowInline.add(new InlineKeyboardButton().setText("8").setCallbackData("8"));
                    rowInline2.add(new InlineKeyboardButton().setText("9").setCallbackData("9"));
                    rowInline2.add(new InlineKeyboardButton().setText("10").setCallbackData("10"));
                    rowInline2.add(new InlineKeyboardButton().setText("11").setCallbackData("11"));
                    rowInline2.add(new InlineKeyboardButton().setText("12").setCallbackData("12"));
                    rowInline2.add(new InlineKeyboardButton().setText("13").setCallbackData("13"));
                    rowInline2.add(new InlineKeyboardButton().setText("14").setCallbackData("14"));
                    rowInline2.add(new InlineKeyboardButton().setText("15").setCallbackData("15"));
                    rowInline2.add(new InlineKeyboardButton().setText("16").setCallbackData("16"));
                    rowInline3.add(new InlineKeyboardButton().setText("17").setCallbackData("17"));
                    rowInline3.add(new InlineKeyboardButton().setText("18").setCallbackData("18"));
                    rowInline3.add(new InlineKeyboardButton().setText("19").setCallbackData("19"));
                    rowInline3.add(new InlineKeyboardButton().setText("20").setCallbackData("20"));
                    rowInline3.add(new InlineKeyboardButton().setText("21").setCallbackData("21"));
                    rowInline3.add(new InlineKeyboardButton().setText("22").setCallbackData("22"));
                    rowInline3.add(new InlineKeyboardButton().setText("23").setCallbackData("23"));
                    rowInline3.add(new InlineKeyboardButton().setText("24").setCallbackData("24"));
                    rowInline4.add(new InlineKeyboardButton().setText("25").setCallbackData("25"));
                    rowInline4.add(new InlineKeyboardButton().setText("26").setCallbackData("26"));
                    rowInline4.add(new InlineKeyboardButton().setText("27").setCallbackData("27"));
                    rowInline4.add(new InlineKeyboardButton().setText("28").setCallbackData("28"));
                    rowInline4.add(new InlineKeyboardButton().setText("29").setCallbackData("29"));
                    rowInline4.add(new InlineKeyboardButton().setText("30").setCallbackData("30"));
                    rowInline4.add(new InlineKeyboardButton().setText("31").setCallbackData("31"));
                    rowInline4.add(new InlineKeyboardButton().setText("Ввод").setCallbackData("enter"));


                    // Set the keyboard to the markup
                    rowsInline.add(rowInline);
                    rowsInline.add(rowInline1);
                    rowsInline.add(rowInline2);
                    rowsInline.add(rowInline3);
                    rowsInline.add(rowInline4);

                    // Add it to the message
                    markupInline.setKeyboard(rowsInline);
                    message.setReplyMarkup(markupInline);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();

                    }

                } else if (call_data1.equals("2016")) {
                    year = "2016";
                } else if (call_data1.equals("2017")) {
                    year = "2017";
                } else if (call_data1.equals("2018")) {
                    year = "2018";
                } else if (call_data1.equals("2019")) {
                    year = "2019";
                } else if (call_data1.equals("j")) {
                    month = "01";
                } else if (call_data1.equals("f")) {
                    month = "02";
                } else if (call_data1.equals("m")) {
                    month = "03";
                } else if (call_data1.equals("a")) {
                    month = "04";
                } else if (call_data1.equals("mm")) {
                    month = "05";
                } else if (call_data1.equals("i")) {
                    month = "06";
                } else if (call_data1.equals("ii")) {
                    month = "07";
                } else if (call_data1.equals("av")) {
                    month = "08";
                } else if (call_data1.equals("s")) {
                    month = "09";
                } else if (call_data1.equals("o")) {
                    month = "10";
                } else if (call_data1.equals("n")) {
                    month = "11";
                } else if (call_data1.equals("d")) {
                    month = "12";
                } else if (call_data1.equals("1")) {
                    day = "1";
                } else if (call_data1.equals("2")) {
                    day = "2";
                } else if (call_data1.equals("3")) {
                    day = "3";
                } else if (call_data1.equals("4")) {
                    day = "4";
                } else if (call_data1.equals("5")) {
                    day = "5";
                } else if (call_data1.equals("6")) {
                    day = "6";
                } else if (call_data1.equals("7")) {
                    day = "7";
                } else if (call_data1.equals("8")) {
                    day = "8";
                } else if (call_data1.equals("9")) {
                    day = "9";
                } else if (call_data1.equals("10")) {
                    day = "10";
                } else if (call_data1.equals("11")) {
                    day = "11";
                } else if (call_data1.equals("12")) {
                    day = "12";
                } else if (call_data1.equals("13")) {
                    day = "13";
                } else if (call_data1.equals("14")) {
                    day = "14";
                } else if (call_data1.equals("15")) {
                    day = "15";
                } else if (call_data1.equals("16")) {
                    day = "16";
                } else if (call_data1.equals("17")) {
                    day = "17";
                } else if (call_data1.equals("18")) {
                    day = "18";
                } else if (call_data1.equals("19")) {
                    day = "19";
                } else if (call_data1.equals("20")) {
                    day = "20";
                } else if (call_data1.equals("21")) {
                    day = "21";
                } else if (call_data1.equals("22")) {
                    day = "22";
                } else if (call_data1.equals("23")) {
                    day = "23";
                } else if (call_data1.equals("24")) {
                    day = "24";
                } else if (call_data1.equals("25")) {
                    day = "25";
                } else if (call_data1.equals("26")) {
                    day = "26";
                } else if (call_data1.equals("27")) {
                    day = "27";
                } else if (call_data1.equals("28")) {
                    day = "28";
                } else if (call_data1.equals("29")) {
                    day = "29";
                } else if (call_data1.equals("30")) {
                    day = "30";
                } else if (call_data1.equals("31")) {
                    day = "31";
                } else if (call_data.equals("enter")) {
                    SendMessage message = new SendMessage()
                            .setChatId(chat_id1)
                            .setText("Вы ввели дату :" + d);
                    try {
                        ss.bd("update  users set data ='" + d + "' where userId=" + chat_id + " ");
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
                        ss.bs("select Username,data from users ORDER BY data asc");
                        SendMessage message = new SendMessage()
                                .setChatId(chat_id)
                                .setText("");
                        message.setText("Лидер :\n " + ss.res.getString(1) + " Бросил курить : " + ss.res.getString(2)+"\n  Наш канал @nosmok1ng и чат @nosmok1ngchat");
                        execute(message);
                    } catch (SQLException | TelegramApiException e) {
                        e.printStackTrace();
                    }


                }
            }
        }
    }

    @Override
    public String getBotUsername() {

        return "Brosai_bot";
    }

    @Override
    public String getBotToken() {

        return "626521387:AAGQP0w8IM1qR6UUAew5tAAlS3G_XmAw7A0";
    }
}