package model.tools;

import java.util.HashMap;
import java.util.Map;

public class LanguageSetting {
    public enum Language{
        ENGLISH,
        GERMAN
    }
    static final private Map<Language,Map<Integer,String>> languageIDMap = new HashMap<>(
            Map.ofEntries(
                    Map.entry(Language.ENGLISH,new HashMap<Integer,String>(
                            // for example
                            Map.ofEntries(
                                    Map.entry(0,"input Party Location"),
                                    Map.entry(1,"input Participants"),
                                    Map.entry(2,"calculate")
                            ))),
                    Map.entry(Language.GERMAN,new HashMap<Integer,String>(
                            // for example:
                            Map.ofEntries(
                                    Map.entry(0,"Eingabe des Party-Ortes"),
                                    Map.entry(1,"Eingabe der Teilnehmer"),
                                    Map.entry(2,"berechne")//idk
                            )))
            ));
    // Integer ID for buttons,etc.

    public Map<Integer,String> getSelectLanguage(Language language){
        return languageIDMap.get(language);
    }
}
