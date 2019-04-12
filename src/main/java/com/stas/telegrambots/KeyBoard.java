package com.stas.telegrambots;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class KeyBoard implements Key {
    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

    @Override
    public InlineKeyboardMarkup markupInline(String[][]str) {


        for (int i = 0; i < str.length; i++) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            for (int j = 0; j < str[i].length; j++) {

                rowInline.add(new InlineKeyboardButton()
                        .setText(str[i][j].split(":", 2)[0])
                        .setCallbackData(str[i][j].split(":", 2)[1])
                );
            }
            rowsInline.add(rowInline);

        }
        return markupInline.setKeyboard(rowsInline);


    }
}




