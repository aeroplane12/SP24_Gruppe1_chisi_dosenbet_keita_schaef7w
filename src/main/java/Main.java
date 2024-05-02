import model.Manager;

public class Main {
    public static void main(String[] args) {
        Manager x = new Manager();
        x.csvReaderPeople("Dokumentation/TestingData/teilnehmerliste.csv");
        x.csvReaderPartyLocation("Dokumentation/TestingData/partylocation.csv");

    }
}
