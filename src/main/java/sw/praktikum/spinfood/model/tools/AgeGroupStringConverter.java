package sw.praktikum.spinfood.model.tools;

import javafx.util.StringConverter;
import sw.praktikum.spinfood.model.AgeGroup;

public class AgeGroupStringConverter extends StringConverter<AgeGroup.AgeRange> {
    public AgeGroupStringConverter() {}
    @Override
    public String toString(AgeGroup.AgeRange ageRange) {
        return ageRange == null? "" : ageRange.toString();
    }

    @Override
    public AgeGroup.AgeRange fromString(String s) {
        return switch (s) {
            case "Age 0-17" -> AgeGroup.AgeRange.AGE_0_17;
            case "Age 18-23" -> AgeGroup.AgeRange.AGE_18_23;
            case "Age 24-27" -> AgeGroup.AgeRange.AGE_24_27;
            case "Age 28-30" -> AgeGroup.AgeRange.AGE_28_30;
            case "Age 31-35" -> AgeGroup.AgeRange.AGE_31_35;
            case "Age 36-41" -> AgeGroup.AgeRange.AGE_36_41;
            case "Age 42-46" -> AgeGroup.AgeRange.AGE_42_46;
            case "Age 47-56" -> AgeGroup.AgeRange.AGE_47_56;
            case "Age 57-Inf" -> AgeGroup.AgeRange.AGE_57_Infin;
            default -> null;
        };
    }
}
