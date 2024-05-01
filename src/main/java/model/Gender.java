package model;

public class Gender {
    public enum g{
        male(0),
        female(1),
        other(2);
        private final int value;

        g(int value) {
            this.value = value;
        }
    }

    public static g getGen(String s){
        return switch (s) {
            case "male" -> g.male;
            case "female" -> g.female;
            default -> g.other;
        };
    }

}
