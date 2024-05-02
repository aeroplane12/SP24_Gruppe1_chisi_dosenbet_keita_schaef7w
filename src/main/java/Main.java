import model.*;

public class Main {
    public static void main(String[] args) {
        CoupleManager coupleManager = new CoupleManager();
        GroupManager groupManager = new GroupManager();
        Manager Manager = new Manager(groupManager, coupleManager);
        Manager.csvReaderPeople("Dokumentation/TestingData/teilnehmerliste.csv");
        Manager.csvReaderPartyLocation("Dokumentation/TestingData/partylocation.csv");
    }
}
