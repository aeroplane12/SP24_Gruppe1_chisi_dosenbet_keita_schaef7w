package sw.praktikum.spinfood.model.tools;

import java.util.HashMap;
import java.util.Map;

public class LanguageSetting {
    public enum Language{
        ENGLISH,
        GERMAN;

        @Override
        public String toString() {
            return switch (this){
                case ENGLISH -> "English";
                case GERMAN -> "Deutsch";
            };
        }
    }
    static final private Map<Language,Map<Integer,String>> languageIDMap = new HashMap<>(
            Map.ofEntries(
                    Map.entry(Language.ENGLISH,new HashMap<Integer,String>(
                            // for example
                            Map.ofEntries(
                                    // Buttons
                                    Map.entry(0,"Upload Location"),
                                    Map.entry(1,"Upload Participants"),
                                    Map.entry(2,"Calculate"),
                                    Map.entry(3,"Save"),
                                    Map.entry(4,"Calculation Configuration"),
                                    Map.entry(5,"Remove Participant"),
                                    Map.entry(6,"Create new Couples/Groups Tab"),
                                    Map.entry(7,"Delete current Couples/Groups Tab"),
                                    Map.entry(8,"Current Couples/Groups Tab"),
                                    // Tabs
                                    Map.entry(9,"Participants"),
                                    Map.entry(10,"Couples"),
                                    Map.entry(11,"Groups"),
                                    // Column names of table Participants
                                    Map.entry(12,"Person ID"),
                                    Map.entry(13,"Name"),
                                    Map.entry(14,"Kitchen"),
                                    // Kitchen Fields
                                    Map.entry(15,"Longitude"),
                                    Map.entry(16,"Latitude"),
                                    Map.entry(17,"Story"),

                                    Map.entry(18,"Food Preference"),
                                    Map.entry(19,"Age Range"),
                                    Map.entry(20,"Gender"),
                                    Map.entry(21,"Reg. with Partner"),
                                    // Column names of table Couples
                                    Map.entry(22,"Couple ID"),
                                    Map.entry(23,"Person 1 ID"),
                                    Map.entry(24,"Person 2 ID"),
                                    Map.entry(25,"Kitchen 1"),
                                    Map.entry(26,"Kitchen 2"),
                                    Map.entry(27,"Whose Kitchen"),
                                    // Column names of table Groups
                                    Map.entry(28,"Group ID"),
                                    Map.entry(29,"Host ID"),
                                    Map.entry(30,"Guest 1 ID"),
                                    Map.entry(31,"Guest 2 ID"),
                                    Map.entry(32,"Course"),
                                    // Configurations window
                                    Map.entry(33,"Food Preference Weight"),
                                    Map.entry(34,"Age Group Weight"),
                                    Map.entry(35,"Gender Weight"),
                                    Map.entry(36,"Distance Weight"),
                                    Map.entry(37,"Optimal Distance"),
                                    Map.entry(38,"Strictness Level"),
                                    // Save menu
                                    Map.entry(39,"Choose Directory"),
                                    Map.entry(40,"Filename"),
                                    Map.entry(41,"Save")
                            ))),
                    Map.entry(Language.GERMAN,new HashMap<Integer,String>(
                            // for example:
                            Map.ofEntries(
                                    Map.entry(0,"Partyort hochladen"),
                                    Map.entry(1,"Teilnehmerliste hochladen"),
                                    Map.entry(2,"Gruppen berechnen"),
                                    Map.entry(3,"Speichern"),
                                    Map.entry(4,"Berechnungseinstellungen"),
                                    Map.entry(5,"Teilnehmer entfernen"),
                                    Map.entry(6,"Neuen Partner/Gruppen Tab"),
                                    Map.entry(7,"Partner/Gruppen Tab löschen"),
                                    Map.entry(8,"Ausgewählter Partner/Gruppen Tab"),
                                    // Tabs
                                    Map.entry(9,"Teilnehmer"),
                                    Map.entry(10,"Paare"),
                                    Map.entry(11,"Gruppen"),
                                    // Column names of table Participants
                                    Map.entry(12,"Personen ID"),
                                    Map.entry(13,"Name"),
                                    Map.entry(14,"Küche"),
                                    // Kitchen Fields
                                    Map.entry(15,"Längengrad"),
                                    Map.entry(16,"Breitengrad"),
                                    Map.entry(17,"Stockwerk"),

                                    Map.entry(18,"Essensvorliebe"),
                                    Map.entry(19,"Altersspanne"),
                                    Map.entry(20,"Geschlecht"),
                                    Map.entry(21,"Reg. mit Partner"),
                                    // Column names of table Couples
                                    Map.entry(22,"Paar ID"),
                                    Map.entry(23,"Person 1 ID"),
                                    Map.entry(24,"Person 2 ID"),
                                    Map.entry(25,"Küche 1"),
                                    Map.entry(26,"Küche 2"),
                                    Map.entry(27,"Wessen Küche"),
                                    // Column names of table Groups
                                    Map.entry(28,"Gruppen ID"),
                                    Map.entry(29,"Gastgeber ID"),
                                    Map.entry(30,"Gast 1 ID"),
                                    Map.entry(31,"Gast 2 ID"),
                                    Map.entry(32,"Speisegang"),
                                    // Configurations window
                                    Map.entry(33,"Gewichtung Essensvorliebe"),
                                    Map.entry(34,"Gewichtung Altersspanne"),
                                    Map.entry(35,"Gewichtung Geschlect"),
                                    Map.entry(36,"Gewichtung Distanz"),
                                    Map.entry(37,"Optimale Distanz"),
                                    Map.entry(38,"Strengengrad"),
                                    // Save menu
                                    Map.entry(39,"Wähle Verzeichnis"),
                                    Map.entry(40,"Dateiname"),
                                    Map.entry(41,"Speichern")
                            )))
            ));
    // Integer ID for buttons,etc.

    public static Map<Integer,String> getSelectLanguage(Language language){
        return languageIDMap.get(language);
    }
}
