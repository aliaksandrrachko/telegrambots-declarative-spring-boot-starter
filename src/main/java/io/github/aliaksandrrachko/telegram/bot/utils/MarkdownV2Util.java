package io.github.aliaksandrrachko.telegram.bot.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class MarkdownV2Util {

    private static final String[] PREPENDING_CHARACTERS = new String[]{
            "_", "*", "[", "]", "(", ")", "~", "`", ">", "#", "+", "-", "=", "|",
            "{", "}", ".", "!"
    };

    private static final String[] PREPENDED_CHARACTERS = new String[]{
            "\\_", "\\*", "\\[", "\\]", "\\(", "\\)", "\\~", "\\`", "\\>", "\\#", "\\+", "\\-", "\\=", "\\|",
            "\\{", "\\}", "\\.", "\\!"
    };

    public String prependCharacters(String source){
        return StringUtils.replaceEach(source, PREPENDING_CHARACTERS, PREPENDED_CHARACTERS);
    }
}
