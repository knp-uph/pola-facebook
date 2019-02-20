package com.adapters.incoming.console;

/**
 * Created by Piotr on 23.09.2017.
 */
public class Main {

    /*public static void main(String... args) throws IOException {

        //
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        String senderId = "console";

        StringToIncomingMessageConverter toIncomingMessage = new StringToIncomingMessageConverter(senderId);
        OutgoingMessageToStringConverter messageToString = new OutgoingMessageToStringConverter();
        AbstractEngine engine = new ConversationEngine(new Pola(), new BarCodeService(), System.out::println);

        System.out.print(">");

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if(line.startsWith("change_user;")) {
                String[] parts = line.split(";");
                toIncomingMessage.setSenderId(parts[1]);
            } else {
                IncomingMessage in = toIncomingMessage.produceIncomingMessage(line);
                System.out.println(in);
                engine.onNewMessage(in);
            }

            System.out.print(">");
        }

    }*/
}
