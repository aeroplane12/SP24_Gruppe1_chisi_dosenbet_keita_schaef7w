package model;

import model.tools.NumberBox;

import java.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class CoupleManager {


    private static CoupleManager instance;

    public static CoupleManager getInstance() {
        if (instance == null)
            instance = new CoupleManager();

        return instance;
    }

    List<Couple> couples = new ArrayList<>();
    // might need overview over all People
    List<Person> allParticipants = new ArrayList<>();
    // everyone who is not locked in a left
    List<Person> allSingleParticipants = new ArrayList<>();
    // everyone who is left

    private int[][] matrix;

    // Index numbers of Zeros in each row
    private List<int[]> indexOfNumberOfZerosInRow = new ArrayList<>();
    // Index numbers of Zeros in each column
    private List<int[]> indexOfNumberOfZerosInColumns = new ArrayList<>();

    List<Couple> givePeopleWithoutPartner(List<Person> singles) {
        allSingleParticipants.addAll(singles);
        calcCouples();
        return couples;
    }

    void calcCouples() {

        matrix = new int[allSingleParticipants.size()][allSingleParticipants.size()];

        for (int i = 0; i < allSingleParticipants.size(); i++) {
            for (int j = 0; j < allSingleParticipants.size(); j++) {
                // Making sure not to compare person with themselves
                if (i == j)
                    matrix[i][j] = -1;
                else
                    matrix[i][j] = calculateCost(allSingleParticipants.get(i), allSingleParticipants.get(j));
            }
        }

        // Subtract the smallest row value from all values in that row
        initialCalc();
        crossingOutZeros();

        // Check if the sum of the row and column counts is greater than or equal to the number of rows
        //printMatrix();


    }

    private void initialCalc() {

        // Subtract the smallest value in the row from all values in that row
        for (int i = 0; i < matrix.length; i++) {
            int smallestValueInRow = Integer.MAX_VALUE;
            for (int j = 0; j < matrix[i].length; j++) {
                // Find the smallest value in the row that is not -1
                if (matrix[i][j] != -1 && matrix[i][j] < smallestValueInRow)
                    smallestValueInRow = matrix[i][j];

            }

            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] != -1)
                    if (matrix[i][j] < smallestValueInRow)
                        throw new IllegalStateException("This should not happen as we chose the smallest value this was the value!"
                                + matrix[i][j] + " and the smallest value was " + smallestValueInRow);
                    else
                        matrix[i][j] -= smallestValueInRow;
            }
        }

        // Subtract the smallest value in the column from all values in that column
        for (int i = 0; i < matrix.length; i++) {
            int smallestValueInColumn = Integer.MAX_VALUE;
            for (int j = 0; j < matrix[i].length; j++) {
                // Find the smallest value in the column that is not -1
                if (matrix[j][i] != -1 && matrix[j][i] < smallestValueInColumn)
                    smallestValueInColumn = matrix[j][i];

            }

            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[j][i] != -1)
                    if (matrix[j][i] < smallestValueInColumn)
                        throw new IllegalStateException("This should not happen as we chose the smallest value this was the value!"
                                + matrix[j][i] + " and the smallest value was " + smallestValueInColumn);
                    else
                        matrix[j][i] -= smallestValueInColumn;
            }
        }
    }

    void crossingOutZeros() {
        // Find the number of zeros in each row
        for (int i = 0; i < matrix.length; i++) {
            int numberOfZerosInRow = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0)
                    numberOfZerosInRow++;
            }
            int[] storage = new int[2];
            storage[0] = i;
            storage[1] = numberOfZerosInRow;
            indexOfNumberOfZerosInRow.add(storage);
        }


        // Find the number of zeros in each column
        for (int i = 0; i < matrix.length; i++) {
            int numberOfZerosInColumn = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[j][i] == 0)
                    numberOfZerosInColumn++;
            }
            int[] storage = new int[2];
            storage[0] = i;
            storage[1] = numberOfZerosInColumn;
            indexOfNumberOfZerosInColumns.add(storage);
        }

        // Sort the rows and columns by the number of zeros
        indexOfNumberOfZerosInRow.sort(Comparator.comparingInt(o -> o[1]));
        indexOfNumberOfZerosInColumns.sort(Comparator.comparingInt(o -> o[1]));
        //Print the sorted rows and columns
       // for (int[] ints : indexOfNumberOfZerosInRow)
         //   System.out.println("Row: " + ints[0] + " Zeros: " + ints[1]);

       // for (int[] ints : indexOfNumberOfZerosInColumns)
         //   System.out.println("Column: " + ints[0] + " Zeros: " + ints[1]);

        System.out.println(indexOfNumberOfZerosInColumns.size());
        System.out.println(indexOfNumberOfZerosInRow.size());

        // now we need to cross out the zeros by the row or column with the most zeros

        NumberBox[][] tempMatrix = new NumberBox[matrix.length][matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                tempMatrix[i][j] = new NumberBox(matrix[i][j]);
            }
        }
        while(allZerosHaveALine(tempMatrix)) {

        }

    }


     <T> void printMatrix(T[][] matrix) {
        for (T[] ints : matrix) {
            for (T anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
    }

    //TODO Still need to check this method for correctness
    private boolean allZerosHaveALine(NumberBox[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            boolean rowHasZero = false;
            boolean columnHasZero = false;
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j].getNumber() == 0 && !matrix[i][j].isCrossedOut())
                    rowHasZero = true;
                if (matrix[j][i].getNumber() == 0 && !matrix[j][i].isCrossedOut())
                    columnHasZero = true;
            }
            if (!rowHasZero || !columnHasZero)
                return false;
        }
        return true;
    }

    private int calculateCost(Person person1, Person person2) {
        int cost = 0;

        //Can#t have singles build a pair if they are in the same building
        // or have the same kitchen and are not registered as a couple
        if (!(person1.getKitchen() == null) && !(person2.getKitchen() == null) && person1.getKitchen().distance(person2.getKitchen()) == 0)
            return -1;

        //If food preference is not equal
        if (!person1.getFoodPreference().equals(person2.getFoodPreference())) {
            if (person1.getFoodPreference().equals(FoodPreference.FoodPref.MEAT) && person2.getFoodPreference().equals(FoodPreference.FoodPref.VEGAN) ||
                    person1.getFoodPreference().equals(FoodPreference.FoodPref.VEGAN) && person2.getFoodPreference().equals(FoodPreference.FoodPref.MEAT))
                cost += 100;
            else if (person1.getFoodPreference().equals(FoodPreference.FoodPref.MEAT) && person2.getFoodPreference().equals(FoodPreference.FoodPref.VEGGIE) ||
                    person1.getFoodPreference().equals(FoodPreference.FoodPref.VEGGIE) && person2.getFoodPreference().equals(FoodPreference.FoodPref.MEAT))
                cost += 80;
            else if (person1.getFoodPreference().equals(FoodPreference.FoodPref.VEGAN) && person2.getFoodPreference().equals(FoodPreference.FoodPref.VEGGIE) ||
                    person1.getFoodPreference().equals(FoodPreference.FoodPref.VEGGIE) && person2.getFoodPreference().equals(FoodPreference.FoodPref.VEGAN))
                cost += 40;
        }

        // If both have a kitchen distance is the added cost if one doesn't and the other does the cost is reduced
        if (person1.getKitchen() != null && person2.getKitchen() != null)
            cost += (int) Math.round(person1.getKitchen().distance(person2.getKitchen()));
        else if (cost >= 30 && (person1.getKitchen() == null || person2.getKitchen() == null))
            cost -= 30;

        //If age difference is too big
        cost += Math.abs(person1.getAge().ordinal() - person2.getAge().ordinal()) * 10;

        if (person1.getGender().equals(person2.getGender()))
            cost += 50;

        return cost;
    }


    public void addPerson(Person person) {
        if (person.hasPartner()) {
            allParticipants.add(person);
            allParticipants.add(person.getPartner());
        } else {
            allParticipants.add(person);
            allSingleParticipants.add(person);
            calcCouples();
        }
    }

    public void removePerson(String personID) {
        Person person = getPerson(personID);
        allParticipants.remove(person);
        allSingleParticipants.remove(person);
        calcCouples();
    }

    public void removeCouple(String CoupleID) {
        allParticipants = allParticipants.stream()
                .filter(x -> !x.getCoupleIDs().equals(CoupleID))
                .toList();
    }

    public Person getPerson(String string) {
        return allParticipants.stream()
                .filter(x -> x.getID().equals(string))
                .findAny()
                .orElse(null);
    }

    public List<Person> getSinglesList() {
        return allSingleParticipants;
    }

    /** getCouple()
     * returns a Couple by their CoupleID
     *
     * @param string the CoupleID
     * @return the Couple
     */
    public Person[] getCouple(String string) {
        return allParticipants.stream()
                .map(Person::getCouple)
                .filter(Objects::nonNull)
                .filter(x -> x[0].getCoupleIDs().equals(string))
                .findFirst()
                .orElse(null);
    }

    public List<Couple> givePeopleWithoutPartner(List<Person> singles) {
        return null;
    }
}
