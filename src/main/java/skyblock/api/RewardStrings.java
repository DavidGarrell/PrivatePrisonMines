package skyblock.api;

import java.util.Random;

public enum RewardStrings {
    MESSAGE_1("Great Job! "),
    MESSAGE_2("Awesome! "),
    MESSAGE_3("Nice! "),

    MESSAGE_4("Spectacular! "),

    MESSAGE_5("Stunning! "),

    MESSAGE_6("Nice work! "),

    MESSAGE_7("Good work! "),

    MESSAGE_8("Excellent job! "),

    MESSAGE_9("Fantastic! "),
    MESSAGE_10("A++! "),


    COLOR_1("§b"),
    COLOR_2("§2"),
    COLOR_3("§4"),
    COLOR_4("§6"),
    COLOR_5("§a"),
    COLOR_6("§c"),
    COLOR_7("§e"),
    COLOR_8("§1"),
    COLOR_9("§3"),
    COLOR_10("§5"),
    COLOR_11("§9"),

    COLOR_12("§b"),
    COLOR_13("§d"),


    ;

    private final String message;

    RewardStrings(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static String getRandomMessage() {
        String[] messages = {MESSAGE_1.getMessage(), MESSAGE_2.getMessage(), MESSAGE_3.getMessage(), MESSAGE_4.getMessage(), MESSAGE_5.getMessage(), MESSAGE_6.getMessage(), MESSAGE_7.getMessage(), MESSAGE_8.getMessage(), MESSAGE_9.getMessage(), MESSAGE_10.getMessage()};
        int randomIndex = new Random().nextInt(messages.length);
        return messages[randomIndex];
    }

    public static String getRandomColor(){
        String[] color = {COLOR_1.getMessage(), COLOR_2.getMessage(), COLOR_3.getMessage(), COLOR_4.getMessage(), COLOR_5.getMessage(), COLOR_6.getMessage(), COLOR_7.getMessage(), COLOR_8.getMessage(), COLOR_9.getMessage(), COLOR_10.getMessage(), COLOR_11.getMessage(), COLOR_12.getMessage(), COLOR_13.getMessage()};
        int randomIndex = new Random().nextInt(color.length);
        return color[randomIndex];
    }
}

