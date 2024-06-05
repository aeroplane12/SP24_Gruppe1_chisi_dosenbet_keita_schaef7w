import model.*;

public class Main {
    public static void main(String[] args) {
        CoupleManager coupleManager = new CoupleManager();
        GroupManager groupManager = new GroupManager();
        Manager Manager = new Manager(groupManager, coupleManager);
        Manager.inputLocation("Dokumentation/TestingData/partylocation.csv");
        Manager.inputPeople("Dokumentation/TestingData/teilnehmerliste.csv");
    }
}
