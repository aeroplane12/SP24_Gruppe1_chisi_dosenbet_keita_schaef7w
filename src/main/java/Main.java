import model.*;
import model.tools.CSVWriter;


public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        manager.inputLocation("Dokumentation/TestingData/partylocation.csv");
        manager.inputPeople("Dokumentation/TestingData/teilnehmerliste.csv");
        manager.calcAll();
        CSVWriter.write(manager.getGroups(), "Dokumentation/TestingData/teilnehmerlisteProcessed.csv");
    }
}
