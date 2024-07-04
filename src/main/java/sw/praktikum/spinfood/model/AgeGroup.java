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
