package com.adapters.outgoing.facebook;

import java.util.LinkedList;
import java.util.List;

class TextSplitter {
    private final int maxLength;

    public TextSplitter(int maxLength) {
        this.maxLength = maxLength;
    }

    public List<String> split(String text) {
        List<String> parts = new LinkedList<>();
        for (String line : text.split("\n")) {
            StringBuilder sb = new StringBuilder();
            for (String sentence : line.split("(?<=\\.)")) {
                if (sb.length() + sentence.length() >= maxLength) {
                    parts.add(sb.toString());
                    sb.setLength(0);
                }
                sb.append(sentence);
            }
            if (sb.length() != 0) {
                parts.add(sb.toString());
            }
        }
        return parts;
    }
}
