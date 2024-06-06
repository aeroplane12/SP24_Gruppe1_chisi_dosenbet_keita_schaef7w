import model.*;

public class Main {
    public static void main(String[] args) {
        Manager Manager = new Manager();
        Manager.inputPeople("Dokumentation/TestingData/teilnehmerliste.csv");
        Manager.inputLocation("Dokumentation/TestingData/partylocation.csv");
    }
}
