package sw.praktikum.spinfood.model;

import sw.praktikum.spinfood.model.tools.CSVWriter;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        manager.inputLocation("Dokumentation/TestingData/partylocation.csv");
        manager.inputPeople("Dokumentation/TestingData/teilnehmerliste.csv");
        manager.calcAll();
        CSVWriter.write(manager.groups, "Dokumentation/TestingData/outputTest.csv");
        //SP24_Gruppe1_chisi_dosenbet_keita_schaef7w_[A/B/C].csv

        //(A) preference deviation > path length > age differance > gender diversity > number of elements
        manager.changeParameter(new Double[]{400d, //guests
                500000d,//pref
                3000d, //ageDiff
                200d,  //genderDiv
                40000d, //pathLengthWeight
                0d},
                Strictness.A);
        manager.calcAll();
        CSVWriter.write(manager.groups, "Dokumentation/TestingData/SP24_Gruppe1_chisi_dosenbet_keita_schaef7w_A.csv");

        //(B) number of elements > preference deviation > path length > gender diversity > age differance
        manager.changeParameter(new Double[]{400d,
                40000d,
                10d,
                200d,
                3000d,
                0d},
                Strictness.B);
        manager.calcAll();
        CSVWriter.write(manager.groups, "Dokumentation/TestingData/SP24_Gruppe1_chisi_dosenbet_keita_schaef7w_B.csv");

        //(C) gender diversity > age differance > preference deviation > path length > number of elements
        manager.changeParameter(new Double[]{400d,
                3000d,
                40000d,
                500000d,
                200d,
                0d},
                Strictness.C);
        manager.calcAll();
        CSVWriter.write(manager.groups, "Dokumentation/TestingData/SP24_Gruppe1_chisi_dosenbet_keita_schaef7w_C.csv");

    }

}
