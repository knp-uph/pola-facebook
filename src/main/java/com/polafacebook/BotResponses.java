package com.polafacebook;

public class BotResponses {

    public static final String genericInvalidInputText = "Nie rozumiem, sprawdź moją instrukcję!";

    private static final String genericSubmitQuickReply = "Gotowe.";
    private static final String genericCancelQuickReply = "Anuluj.";

    private static final String genericReportQuickReply = "Tak. Chcę zgłosić.";
    private static final String genericGoBackQuickReply = "Nie. Wróć.";

    public static final class Setup {
        public static final String greetingText = "Prześlij kod kreskowy produktu i dowiedz się, jak polski jest jego producent!";
    }

    public static final class SuggestionController {
        public static final class onAskForChangesOrAction {
            public static final String text = "Czy chcesz zgłosić poprawkę do przedstawionych informacji?";
            public static final String quickReplyNo = "Nie. Jest dobrze.";
            public static final String quickReplyYes = genericReportQuickReply;
        }

        public static final class onInvalidInput1 {
            public static final String text = "Nie rozumiem. Tak czy nie? A może chcesz podać kolejny kod i wrócić do początku?";
            public static final String quickReplyNo = genericGoBackQuickReply;
            public static final String quickReplyYes = genericReportQuickReply;
        }

        public static final class onInvalidInput2 {
            public static final String text = "Nie rozumiem. Może po prostu mi coś napisz?";
        }

        public static final class onAffirmative {
            public static final String text = "Super! Powiedz mi proszę, co się nie zgadza lub czego brakuje!";
        }

        public static final class onText {
            public static final String text = "Dziękuję. Wysłałem sugestię.";
            public static final String error = "Ups! Mamy usterkę, nie udało mi się wysłać sugestii. Może spróbuj później?";
        }
    }

    public static final class InfoController {

        public static final class onWelcome {
            public static final String text = "Witaj w Poli! Prześlij nam zdjęcie kodu kreskowego lub wpisz kod ręcznie, a my powiemy Ci czy to polski produkt. Możesz też wybrać którąś z poniższych opcji.";
        }

        public static final class onMethodology {
            public static final String text = "Każdemu producentowi Pola przypisuje od 0 do 100 punktów. Pierwsze 35 punktów przyznaje proporcjonalnie do udziału polskiego kapitału w konkretnym przedsiębiorstwie. Dalsze 10 punktów otrzymuje ta firma, która jest zarejestrowana w Polsce, a kolejne 30, o ile produkuje w naszym kraju. Dalsze 15 punktów zależy od tego, czy zatrudnia w naszym kraju w obszarze badań i rozwoju. Wreszcie ostatnie 10 punktów otrzymują firmy, które nie są częścią zagranicznych koncernów.";

        }

        public static final class onInfo {
            public static final String text = "Masz dość masówki globalnych koncernów? Szukasz lokalnych firm tworzących unikatowe produkty? Pola pomoże Ci odnaleźć polskie wyroby. Zabierając Polę na zakupy, odnajdujesz produkty „z duszą” i wspierasz polską gospodarkę.\n" +
                    "Zeskanuj kod kreskowy z dowolnego produktu i dowiedz się więcej o firmie, która go wyprodukowała. Zeskanuj kod kreskowy z dowolnego produktu i dowiedz się więcej o firmie, która go wyprodukowała. Pola powie Ci, ile dany producent posiada polskiego kapitału, czy w Polsce ulokował swoją produkcję, tworzy wykwalifikowane miejsca pracy w dziale badań i rozwoju, w końcu czy zarejestrował swoją działalność na terenie naszego kraju oraz czy jest częścią zagranicznego koncernu.\n" +
                    "Jeśli znajdziesz firmę, której nie ma w naszej bazie, koniecznie zgłoś ją do nas. Pomożesz nam w ten sposób uzupełniać unikatową bazę polskich producentów. Więcej informacji na www.pola-app.pl/. Jeśli reprezentujesz firmę i chcesz się z nami skontaktować prosimy o wiadomość na: aplikacja.pola@gmail.com";
        }

        public static final class onHelp {
            public static final String text = "Aby uzyskać informacje o producencie danego produktu, wystarczy wysłać nam zdjęcie z kodem kreskowym lub wpisać kod ręcznie. Możesz ponadto zapytać się o metodykę naszej oceny czy uzyskać informacje o usłudze.";
        }

        public static final String infoQuickReply = "Informacje";
        public static final String methodologyQuickReply = "Metodyka";
        public static final String helpQuickReply = "Pomoc";
    }

    public static final class ProductController {
        public static final class onImage {
            public static final String text = "Znaleziono kod: ";
        }

        public static final class onText {
            public static final String text = "Kod produktu otrzymany: ";
        }

        public static final class onNotRecognized {
            public static final String text = "Nie udało nam się pobrać kodu z obrazka. Możesz spróbować wpisać kod ręcznie lub wysłać nowe zdjęcie, na którym lepiej widać kod kreskowy.";
        }

        public static final class handleDBError {
            public static final String text = "Mamy usterkę i nie możemy w tym momencie uzyskać informacji na temat tego kodu. Spróbuj ponownie później. Przepraszamy!";
        }

        public static final class onDisplayResults {
            public static final String ratingText = "Ocena: ";
            public static final String manufacturerText = "Producent: ";
            public static final String polishCapitaltext = "Udział polskiego kapitału: ";
            public static final String manufacturesInPolandText = "Produkuje w Polsce";
            public static final String hasRndInPolandText = "Prowadzi badania i rozwój w Polsce";
            public static final String isRegisteredInPolandText = "Zarejestrowana w Polsce";
            public static final String isPartOfForeignCorporationText = "Jest częścią zagranicznego koncernu";
        }
    }

    public static final class ReportController {
        public static final String submitQuickReply = genericSubmitQuickReply;
        public static final String cancelQuickReply = genericCancelQuickReply;

        public static final class onInvalidInput1 {
            public static final String text = "Nie rozumiem. Tak czy nie? A może chcesz podać kolejny kod i wrócić do początku?";
            public static final String quickReplyNo = "Nie zgłaszam. Wróć.";
            public static final String quickReplyYes = "Tak, zgłaszam brak.";
        }

        public static final class onInvalidInput2 {
            public static final String text = "Nie rozumiem. Może po prostu mi coś napisz?";
        }

        public static final class onInvalidInput3 {
            public static final String text = "Nie rozumiem. Jeśli chcesz zakończyć wypełniane raportu, to napisz 'ok' albo 'gotowe'. Możesz też go anulować lub dodać kolejne zdjęcie.";
        }

        public static final class onReportPromptImage {
            public static final String text = "Nie znaleźliśmy tego produktu w naszej bazie danych. Może chcesz zgłosić ten brak?";
            public static final String quickReplyNo = genericGoBackQuickReply;
            public static final String quickReplyYes = "Tak. Chcę zgłosić.";
        }

        public static final class onAffirmative {
            public static final String text = "Super! Podaj nam krótki opis brakującego produktu.";
        }

        public static final class onText {
            public static final String text = "Super! Prześlij nam teraz jakieś fotki kodu kreskowego i etykiety produktu do raportu. ;)";
        }

        public static final class onImage {
            public static final String text = "Przyjąłem! Zdjęć łącznie: ";
        }

        public static final class onCancel {
            public static final String text = "Nie ma problemu, raport anulowany. Dzięki!";
        }

        public static final class onSubmit {
            public static final String text = "Dziękuję. Wysłałem raport. Jak mogę Ci jeszcze pomóc?";
            public static final String error = "Ups! Mamy usterkę, nie udało mi się wysłać raportu. Może spróbuj później?";
        }
    }

    public static final class ControllerExceptionHandler {
        public static final String text = "Przepraszamy, wystąpił po naszej stronie nieoczekiwany problem. Nasze stado małpich programistów pracuje nad rozwiązaniem! Spróbuj ponownie później.";
        public static final String quickReply = "Spróbujmy od nowa.";
    }


    private BotResponses() {
    }
}
