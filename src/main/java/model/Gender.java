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

    /**
     * getGen
     * @param s the persons gender as a String
     * @return the Persons gender as a "g" Enum
     */
    public static g getGen(String s){
        return switch (s) {
            case "male" -> g.male;
            case "female" -> g.female;
            default -> g.other;
        };
    }

}
