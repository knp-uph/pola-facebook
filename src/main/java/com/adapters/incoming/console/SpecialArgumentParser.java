package com.adapters.incoming.console;

/**
 * Created by Piotr on 23.09.2017.
 */

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpecialArgumentParser {

    // name:"value"
    // name:value2
    private final static Pattern REG_EXP_PATTERN = Pattern
            .compile("(?<name>[a-z]+):(?:\"(?<value>.+?)\"|(?<value2>[^ ]+))", Pattern.CASE_INSENSITIVE);
    private String text;

    public SpecialArgumentParser(String text) {
        this.text = text;
    }

    public Map<String, String> getArguments() {
        Matcher matcher = REG_EXP_PATTERN.matcher(text);
        Map<String, String> arguments = new HashMap<>();

        while (matcher.find()) {
            String name = matcher.group("name");
            String value = matcher.group("value");
            if (value == null) {
                value = matcher.group("value2");
            }

            if (value.isEmpty())
                continue;

            arguments.put(name, value);
        }
        return arguments;
    }

    public String getHeaderText() {
        return REG_EXP_PATTERN.matcher(text)
                .replaceAll("")
                .trim();
    }

    public static void main(String[] args) {
        SpecialArgumentParser p = new SpecialArgumentParser("TEXT aaa:\"Aa\" bbb:BB");
        System.out.println("TEXT: " + p.getHeaderText());
        System.out.println("ARGS:" + p.getArguments());
    }

}