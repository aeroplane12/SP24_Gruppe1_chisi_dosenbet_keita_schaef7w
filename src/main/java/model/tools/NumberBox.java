package model.tools;

import model.Person;

/**
 * This class is a placeholder for a number box. It is used for the Hungarian algorithm.
 * To check, if a number in a row or column has already been crossed out.
 * Each time we look for a zero, we need to make sure at least one zero in
 * the row or column has not been crossed out.
 */
public class NumberBox {

    private boolean horizontalCrossedOut;
    private boolean crossedOut;
    private double number;

    public NumberBox(double number) {
        this.number = number;
        this.crossedOut = false;
        this.horizontalCrossedOut = false;
    }

    public boolean isCrossedOut() {
        return crossedOut || horizontalCrossedOut;
    }

    public void setCrossedOut(boolean crossedOut, boolean horizontalCrossedOut) {
        this.horizontalCrossedOut = horizontalCrossedOut;
        this.crossedOut = crossedOut;
    }

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        if (number == -1) {
            this.crossedOut = false;
            this.horizontalCrossedOut = false;
        }
        this.number = number;
    }

    @Override
    public String toString() {
        if (crossedOut)
            if (horizontalCrossedOut)
                return "|" + number + "|";
            else
                return "-" + number + "-";
        else
            return Double.toString(number);

    }
}