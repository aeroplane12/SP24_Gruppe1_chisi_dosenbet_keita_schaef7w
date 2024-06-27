package sw.praktikum.spinfood.model;

public class Gender {
    public enum genderValue{
        male(0),
        female(1),
        other(2);
        public final int value;

        genderValue(int value) {
            this.value = value;
        }
    }

    public static genderValue getGen(String s){
        return switch (s) {
            case "male" -> genderValue.male;
            case "female" -> genderValue.female;
            default -> genderValue.other;
        };
    }

}
