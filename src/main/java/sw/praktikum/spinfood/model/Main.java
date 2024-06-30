package sw.praktikum.spinfood.model;

import sw.praktikum.spinfood.model.tools.CSVWriter;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        manager.inputLocation("Dokumentation/TestingData/partylocation.csv");
        manager.inputPeople("Dokumentation/TestingData/teilnehmerliste.csv");
        manager.calcAll();
        CSVWriter.write(manager.groups,"Dokumentation/TestingData/outputTest.csv");
        //SP24_Gruppe1_chisi_dosenbet_keita_schaef7w_[A/B/C].csv

        //(A) preference deviation > path length > age differance > gender diversity > number of elements // A
        manager.changeParameter(new Double[]{400d,5000d,30d,20d,400d,0d}, Strictness.A);
        manager.calcAll();
        CSVWriter.write(manager.groups,"Dokumentation/TestingData/SP24_Gruppe1_chisi_dosenbet_keita_schaef7w_A.csv");

        //(B) number of elements > preference deviation > path length > gender diversity > age differance
        manager.changeParameter(new Double[]{400d,400d,1d,20d,30d,0d}, Strictness.B);
        manager.calcAll();
        CSVWriter.write(manager.groups,"Dokumentation/TestingData/SP24_Gruppe1_chisi_dosenbet_keita_schaef7w_B.csv");

        //(C) gender diversity > age differance > preference deviation > path length > number of elements
        manager.changeParameter(new Double[]{400d,30d,400d,5000d,20d,0d}, Strictness.C);
        manager.calcAll();
        CSVWriter.write(manager.groups,"Dokumentation/TestingData/SP24_Gruppe1_chisi_dosenbet_keita_schaef7w_C.csv");

    }

}
