package sw.praktikum.spinfood.model.tools;

import javafx.util.StringConverter;
import sw.praktikum.spinfood.model.Gender;

public class GenderStringConverter extends StringConverter<Gender.genderValue> {
    public GenderStringConverter() {}
    @Override
    public String toString(Gender.genderValue gender) {
        return gender == null? "" : gender.toString();
    }

    @Override
    public Gender.genderValue fromString(String s) {
        return switch (s){
            case "male" -> Gender.genderValue.male;
            case "female" -> Gender.genderValue.female;
            case "other" -> Gender.genderValue.other;
            default -> null;
        };
    }
}
