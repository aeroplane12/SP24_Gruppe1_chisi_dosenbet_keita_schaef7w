package model.tools;

import model.Person;

/**
 * This class is a placeholder for a number box. It is used for the Hungarian algorithm.
 * To check, if a number in a row or column has already been crossed out.
 * Each time we look for a zero, we need to make sure at least one zero in
 * the row or column has not been crossed out.
 */
public class NumberBox {

    private boolean crossedOut;
    private double number;
    public NumberBox(double number){
        this.number = number;
        this.crossedOut = false;
    }

    public boolean isCrossedOut() {
        return crossedOut;
    }

    public void setCrossedOut(boolean crossedOut) {
        this.crossedOut = crossedOut;
    }

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    @Override
    public String toString() {
        if(crossedOut)
            return "-" + number + "-";
        else
            return  Double.toString(number);

    }
}