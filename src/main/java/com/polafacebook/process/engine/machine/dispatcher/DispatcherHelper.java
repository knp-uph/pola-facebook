package com.polafacebook.process.engine.machine.dispatcher;

import com.polafacebook.process.engine.message.IncomingMessage;
import com.polafacebook.process.engine.message.attachment.Attachment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DispatcherHelper {

    final Set<String> affirmativeWordList;
    final Set<String> negativeWordList;
    final Set<String> submissionWordList;
    final Set<String> cancellationWordList;

    public DispatcherHelper(Set<String> affirmativeWordList, Set<String> negativeWordList, Set<String> submissionWordList, Set<String> cancellationWordList) {
        this.affirmativeWordList = affirmativeWordList;
        this.negativeWordList = negativeWordList;
        this.submissionWordList = submissionWordList;
        this.cancellationWordList = cancellationWordList;
    }

    public DispatcherHelper() {
        affirmativeWordList = new HashSet<>();
        affirmativeWordList.add("TAK");

        negativeWordList = new HashSet<>();
        negativeWordList.add("NIE");

        submissionWordList = new HashSet<>();
        submissionWordList.add("OK");
        submissionWordList.add("KONIEC");
        submissionWordList.add("GOTOWE");

        cancellationWordList = new HashSet<>();
        cancellationWordList.add("STOP");
        cancellationWordList.add("ANULUJ");
    }

    public boolean isNegative(IncomingMessage message) {
        String text = message.getText().toUpperCase();
        String payload = message.getPayload();

        if (startsWithWordList(text, negativeWordList)) {
            return true;
        }
        if (payload != null && payload.equals("NEGATIVE")) {
            return true;
        }
        return false;
    }

    public boolean isAffirmative(IncomingMessage message) {
        String text = message.getText().toUpperCase();
        String payload = message.getPayload();

        if (startsWithWordList(text, affirmativeWordList)) {
            return true;
        }
        if (payload != null && payload.equals("AFFIRMATIVE")) {
            return true;
        }
        return false;
    }

    public boolean isSubmission(IncomingMessage message) {
        String text = message.getText().toUpperCase();
        String payload = message.getPayload();

        if (containsFromWordList(text, submissionWordList)) {
            return true;
        }
        if (payload != null && payload.equals("SUBMIT")) {
            return true;
        }
        return false;
    }

    public boolean isCancellation(IncomingMessage message) {
        String text = message.getText().toUpperCase();
        String payload = message.getPayload();

        if (text.contains("STOP") || text.contains("ANULUJ")) {
            return true;
        }
        if (payload != null && payload.equals("CANCEL")) {
            return true;
        }
        return false;
    }

    private boolean containsFromWordList(String text, Set<String> wordList) {
        for (String word: wordList) {
            if (text.contains(word)) {
                return true;
            }
        }
        return false;
    }

    private boolean startsWithWordList(String text, Set<String> wordList) {
        for (String word: wordList) {
            if (text.startsWith(word)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasImage(IncomingMessage message) {
        return message.hasAttachmentType(Attachment.Type.IMAGE);
    }

    public String[] extractCode(IncomingMessage message) {
        //works for singular codes only
        //preprocessing; remove whitespace between digits in case the user inputs the code in chunks/with whitespace separators
        String text =  message.getText().replaceAll("(?<=\\d) +(?=\\d)", "");
        //find 13 digits in a row:
        Pattern pattern = Pattern.compile("(?<=^|\\s)\\d{13}(?:(?=$|\\s|[.,;?]))");
        Matcher matcher = pattern.matcher(text);
        //https://www.regextester.com/?fam=99340
        ArrayList<String> matchedCodes = new ArrayList<>();
        while (matcher.find()) {     // find the next match
            matchedCodes.add(matcher.group());
        }
        return matchedCodes.toArray(new String[0]);
    }

    public static void main(String... args) {
        DispatcherHelper dispatcherHelper = new DispatcherHelper();
        String text = "1234567890123456sda\n" +
                "1234567890123 1234567890123\n" +
                "1234567890123ass1234567890123\n" +
                "1234567890123";
        text = "1234567890123, 1234567890123, 1234567 890123";
        //text = "1234567 890123";
        String[] results = dispatcherHelper.extractCode(new IncomingMessage(text, "DEV"));
        System.out.println("Result: " + results.length);
        for (String m : results) {
            System.out.println("Code found: " + m);
        }
    }
}
