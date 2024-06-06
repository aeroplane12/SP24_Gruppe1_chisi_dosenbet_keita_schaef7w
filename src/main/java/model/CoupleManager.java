package model;

import model.tools.NumberBox;

import java.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class  CoupleManager {

    private static final List<List<NumberBox[][]>> matrixList = new ArrayList<>();
    private static CoupleManager instance;
    private int strictnessLevel = 0;

    /**
     * CoupleManager() is a singleton class
     * Has different levels of strictness, the higher the number, the more strict the algorithm in terms of pairing of food preferences
     * lowStrictness = 0 and means that the algorithm will pair any two people together
     * mediumStrictness = 1 and means that the algorithm will pair people vegan and vegetarian together and meat with any
     * highStrictness = 2 and means that the algorithm will pair people with the same food preference together
     * @return the instance of the CoupleManager
     */
    public static CoupleManager getInstance() {
        if (instance == null)
            instance = new CoupleManager();


        return instance;
    }

    List<Couple> couples = new ArrayList<>();

    // might need overview over all People
    List<Person> allSingleParticipants = new ArrayList<>();
    // everyone who is left
    private List<Person> any;

    // Index numbers of Zeros in each row

    List<Couple> givePeopleWithoutPartner(List<Person> singles, int strictnessLevel) {
        if (strictnessLevel < 0 || strictnessLevel > 2)
            throw new IllegalArgumentException("Strictness level must be between 0 and 2");

        this.strictnessLevel = strictnessLevel;
        allSingleParticipants.addAll(singles);
        calcCouples();
        return couples;
    }

    void calcCouples() {
        if(strictnessLevel == 0)
            bringSingleTogether(createNumberBoxMatrix(allSingleParticipants));
        else if(strictnessLevel == 1) {
            List<Person> veganAndVeggie = allSingleParticipants.stream().filter( x -> x.getFoodPreference().equals(FoodPreference.FoodPref.VEGAN) || x.getFoodPreference().equals(FoodPreference.FoodPref.VEGGIE)).toList();
            List<Person> meatAndAny = allSingleParticipants.stream().filter( x -> x.getFoodPreference().equals(FoodPreference.FoodPref.MEAT) || x.getFoodPreference().equals(FoodPreference.FoodPref.NONE)).toList();
            bringSingleTogether(createNumberBoxMatrix(veganAndVeggie));
            bringSingleTogether(createNumberBoxMatrix(meatAndAny));
        } else if(strictnessLevel == 2) {
            List<Person> vegan = allSingleParticipants.stream().filter(x -> x.getFoodPreference().equals(FoodPreference.FoodPref.VEGAN)).toList();
            List<Person> meat = allSingleParticipants.stream().filter(x -> x.getFoodPreference().equals(FoodPreference.FoodPref.MEAT)).toList();
            List<Person> veggie = allSingleParticipants.stream().filter(x -> x.getFoodPreference().equals(FoodPreference.FoodPref.VEGGIE)).toList();
            this.any = allSingleParticipants.stream().filter(x -> x.getFoodPreference().equals(FoodPreference.FoodPref.NONE)).toList();
            bringSingleTogether(createNumberBoxMatrix(vegan));
            bringSingleTogether(createNumberBoxMatrix(meat));
            bringSingleTogether(createNumberBoxMatrix(veggie));
        }
    }

    private NumberBox[][] createNumberBoxMatrix(List<Person> people){
        NumberBox[][] matrix = new NumberBox[people.size()][people.size()];
        for (int i = 0; i < people.size(); i++) {
            for (int j = 0; j < people.size(); j++) {
                matrix[i][j] = new NumberBox(calculateCost(people.get(i), people.get(j)));
            }
        }
        //Here we cut the matrix diagonally
        for (int i = 0; i < people.size(); i++) {
            for (int j = 0; j < people.size(); j++) {
                if (i == j)
                    matrix[i][j] = new NumberBox(-1);
            }
        }

        return matrix;
    }

    private void bringSingleTogether(NumberBox[][] matrix) {
        couples.addAll(crossingOutZeros(subtractSmallest(matrix)));
    }

    private NumberBox[][] subtractSmallest(NumberBox[][] matrix) {

        for (int i = 0; i < matrix.length; i++) {
            // Find the smallest number in the row
            int smallestNumberRow = Integer.MAX_VALUE;
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j].getNumber() < smallestNumberRow && matrix[i][j].getNumber() > -1)
                    smallestNumberRow = matrix[i][j].getNumber();
            }
            if(smallestNumberRow < 0)
                throw new IllegalArgumentException("Smallest number in row is negative");

            // Subtract the smallest number from all numbers in the row
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j].getNumber() != -1)
                    matrix[i][j].setNumber(matrix[i][j].getNumber() - smallestNumberRow);
            }

            // Find the smallest number in the column
            int smallestNumberColumn = Integer.MAX_VALUE;
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[j][i].getNumber() < smallestNumberColumn && matrix[j][i].getNumber() > -1)
                    smallestNumberColumn = matrix[j][i].getNumber();
            }
            if(smallestNumberColumn < 0)
                throw new IllegalArgumentException("Smallest number in column is negative");

            // Subtract the smallest number from all numbers in the column
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[j][i].getNumber() > -1)
                    matrix[j][i].setNumber(matrix[j][i].getNumber() - smallestNumberColumn);
            }
        }
        return matrix;
    }

    private List<Couple> crossingOutZeros(NumberBox[][] matrix) {
        List<int[]> indexOfNumberOfZerosInRow = new ArrayList<>();
        // Index numbers of Zeros in each column
        List<int[]> indexOfNumberOfZerosInColumns = new ArrayList<>();

        // Find the number of zeros in each row
        for (int i = 0; i < matrix.length; i++) {
            int numberOfZerosInRow = 0;
            int numberOfZerosInColumn = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j].getNumber() == 0)
                    numberOfZerosInRow++;
                if (matrix[j][i].getNumber() == 0)
                    numberOfZerosInColumn++;

            }
            int[] storageRow = new int[2];
            storageRow[0] = i;
            storageRow[1] = numberOfZerosInRow;
            indexOfNumberOfZerosInRow.add(storageRow);

            int[] storageColumn = new int[2];
            storageColumn[0] = i;
            storageColumn[1] = numberOfZerosInColumn;
            indexOfNumberOfZerosInColumns.add(storageColumn);
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
        while (!allZerosHaveALine(matrix)){
            // Find the row or column with the most zeros
            int row = indexOfNumberOfZerosInRow.get(0)[0];
            int column = indexOfNumberOfZerosInColumns.get(0)[0];
            // Find the smallest number in the row or column
            int smallestNumber = Integer.MAX_VALUE;
            for (int i = 0; i < matrix.length; i++) {
                if (matrix[row][i].getNumber() < smallestNumber && matrix[row][i].getNumber() != -1)
                    smallestNumber = matrix[row][i].getNumber();
                if (matrix[i][column].getNumber() < smallestNumber && matrix[i][column].getNumber() != -1)
                    smallestNumber = matrix[i][column].getNumber();
            }
            // Subtract the smallest number from all numbers in the row and add it to all numbers in the column
            for (int i = 0; i < matrix.length; i++) {
                if (matrix[row][i].getNumber() != -1)
                    matrix[row][i].setNumber(matrix[row][i].getNumber() - smallestNumber);
                if (matrix[i][column].getNumber() != -1)
                    matrix[i][column].setNumber(matrix[i][column].getNumber() + smallestNumber);
            }
            // Cross out the zero
            matrix[row][column].setCrossedOut(true);
            // Remove the row and column from the list
            indexOfNumberOfZerosInRow.remove(0);
            indexOfNumberOfZerosInColumns.remove(0);
        }



        indexOfNumberOfZerosInColumns.clear();
        indexOfNumberOfZerosInRow.clear();
        return null;
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

        if(person1.equals(person2))
            return -1;

        //Can't have singles build a pair if they are in the same building
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
        allSingleParticipants.add(person);
        calcCouples();
    }

    public void removeSinglePerson(String personID) {
        if (allSingleParticipants.stream().noneMatch(x -> Objects.equals(x.getID(), personID)))
            throw new IllegalArgumentException("Person not found");
        else
            allSingleParticipants.remove(personID);
        calcCouples();
    }


    public Person getSinglePerson(String ID) {
        return allSingleParticipants.stream()
                .filter(x -> x.getID().equals(ID))
                .findAny()
                .orElse(null);
    }


    public int getStrictnessLevel() {
        return strictnessLevel;
    }

    public void setStrictnessLevel(int strictnessLevel) {
        this.strictnessLevel = strictnessLevel;
    }

    public List<Person> getSinglesList() {
        return allSingleParticipants;
    }

}
