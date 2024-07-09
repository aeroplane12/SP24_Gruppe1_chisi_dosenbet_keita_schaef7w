package sw.praktikum.spinfood.model;

public class AgeGroup {
    public enum AgeRange {
        AGE_0_17(0),
        AGE_18_23(1),
        AGE_24_27(2),
        AGE_28_30(3),
        AGE_31_35(4),
        AGE_36_41(5),
        AGE_42_46(6),
        AGE_47_56(7),
        AGE_57_Infin(8);

        public final int value;

        AgeRange(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return switch (this.value) {
                case 0 -> "Age 0-17";
                case 1 -> "Age 18-23";
                case 2 -> "Age 24-27";
                case 3 -> "Age 28-30";
                case 4 -> "Age 31-35";
                case 5 -> "Age 36-41";
                case 6 -> "Age 42-46";
                case 7 -> "Age 47-56";
                default -> "Age 57-Inf";
            };
        }
    }

    /**
     * getAgeRange
     * @param s the String which holds the age of the person
     * @return a fitting ageRange enum
     */
    public static AgeRange getAgeRange(String s) {
        int i = Math.round(Float.parseFloat(s));
        if (i < 18){
            return AgeRange.AGE_0_17;
        } else if (i <24) {
            return AgeRange.AGE_18_23;
        } else if (i < 28) {
            return AgeRange.AGE_24_27;
        } else if (i < 31) {
            return AgeRange.AGE_28_30;
        } else if (i < 36) {
            return AgeRange.AGE_31_35;
        } else if (i < 42) {
            return AgeRange.AGE_36_41;
        } else if (i < 47) {
            return AgeRange.AGE_42_46;
        } else if (i < 57) {
            return AgeRange.AGE_47_56;
        } else {
            return AgeRange.AGE_57_Infin;
        }
    }

}
