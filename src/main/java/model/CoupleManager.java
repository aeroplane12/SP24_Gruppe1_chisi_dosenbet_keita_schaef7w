package model;

import model.tools.NumberBox;

import java.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class CoupleManager {

    private static final List<List<NumberBox[][]>> matrixList = new ArrayList<>();
    private static CoupleManager instance;
    private int strictnessLevel = 0;
    CoupleManager(){
        instance=this;
    }

    /**
     * CoupleManager() is a singleton class
     * Has different levels of strictness, the higher the number, the more strict the algorithm in terms of pairing of food preferences
     * lowStrictness = 0 and means that the algorithm will pair any two people together
     * mediumStrictness = 1 and means that the algorithm will pair people vegan and vegetarian together and meat with any
     * highStrictness = 2 and means that the algorithm will pair people with the same food preference together
     *
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
        if (strictnessLevel == 0)
            bringSingleTogether(createNumberBoxMatrix(allSingleParticipants));
        else if (strictnessLevel == 1) {
            List<Person> veganAndVeggie = allSingleParticipants.stream().filter(x -> x.getFoodPreference().equals(FoodPreference.FoodPref.VEGAN) || x.getFoodPreference().equals(FoodPreference.FoodPref.VEGGIE)).toList();
            List<Person> meatAndAny = allSingleParticipants.stream().filter(x -> x.getFoodPreference().equals(FoodPreference.FoodPref.MEAT) || x.getFoodPreference().equals(FoodPreference.FoodPref.NONE)).toList();
            bringSingleTogether(createNumberBoxMatrix(veganAndVeggie));
            bringSingleTogether(createNumberBoxMatrix(meatAndAny));
        } else if (strictnessLevel == 2) {
            List<Person> vegan = allSingleParticipants.stream().filter(x -> x.getFoodPreference().equals(FoodPreference.FoodPref.VEGAN)).toList();
            List<Person> meat = allSingleParticipants.stream().filter(x -> x.getFoodPreference().equals(FoodPreference.FoodPref.MEAT)).toList();
            List<Person> veggie = allSingleParticipants.stream().filter(x -> x.getFoodPreference().equals(FoodPreference.FoodPref.VEGGIE)).toList();
            this.any = allSingleParticipants.stream().filter(x -> x.getFoodPreference().equals(FoodPreference.FoodPref.NONE)).toList();
            bringSingleTogether(createNumberBoxMatrix(vegan));
            bringSingleTogether(createNumberBoxMatrix(meat));
            bringSingleTogether(createNumberBoxMatrix(veggie));
        }
    }
    //This is correct
    private NumberBox[][] createNumberBoxMatrix(List<Person> people) {
        NumberBox[][] matrix = new NumberBox[people.size()][people.size()];
        for (int i = 0; i < people.size(); i++)
            for (int j = 0; j < people.size(); j++)
                matrix[i][j] = new NumberBox(calculateCost(people.get(i), people.get(j)));


        return matrix;
    }

        //This is correct
    private NumberBox[][] subtractSmallest(NumberBox[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            // Find the smallest number in the row
            double smallestNumberRow = Integer.MAX_VALUE;
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j].getNumber() < smallestNumberRow && matrix[i][j].getNumber() > -1.0)
                    smallestNumberRow = matrix[i][j].getNumber();
            }
            if (smallestNumberRow < 0)
                throw new IllegalArgumentException("Smallest number in row is negative");

            // Subtract the smallest number from all numbers in the row
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j].getNumber() != -1)
                    matrix[i][j].setNumber(matrix[i][j].getNumber() - smallestNumberRow);
            }

            // Find the smallest number in the column
            double smallestNumberColumn = Integer.MAX_VALUE;
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[j][i].getNumber() < smallestNumberColumn && matrix[j][i].getNumber() > -1)
                    smallestNumberColumn = matrix[j][i].getNumber();
            }
            if (smallestNumberColumn < 0)
                throw new IllegalArgumentException("Smallest number in column is negative");

            // Subtract the smallest number from all numbers in the column
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[j][i].getNumber() > -1)
                    matrix[j][i].setNumber(matrix[j][i].getNumber() - smallestNumberColumn);
            }
        }

        return matrix;
    }

    private void bringSingleTogether(NumberBox[][] matrix) {
        //TODO: Till here everything is correct
        System.out.println(crossingOutZeros(subtractSmallest(matrix)));

    }


    private int crossingOutZeros(NumberBox[][] matrix) {
        List<int[]> indexOfNumberOfZerosInRow = new ArrayList<>();
        // Index numbers of Zeros in each column
        List<int[]> indexOfNumberOfZerosInColumns = new ArrayList<>();

        // Find the number of zeros in each row and column
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

        // Sort the rows and columns by the number of zeros from most zeros to least
        indexOfNumberOfZerosInRow.sort((x, y) -> Integer.compare(y[1], x[1]));
        indexOfNumberOfZerosInColumns.sort((x, y) -> Integer.compare(y[1], x[1]));

        //Print the sorted rows and columns
//        for (int[] ints : indexOfNumberOfZerosInRow)
//            System.out.println("Row: " + ints[0] + " Zeros: " + ints[1]);
//
//        for (int[] ints : indexOfNumberOfZerosInColumns)
//            System.out.println("Column: " + ints[0] + " Zeros: " + ints[1]);

        System.out.println(indexOfNumberOfZerosInColumns.size());
        System.out.println(indexOfNumberOfZerosInRow.size());
        int numberOfLines = 0;
        // now we need to cross out the zeros by the row or column with the most zeros
        while (!allZerosHaveALine(matrix)){
            int[] row = indexOfNumberOfZerosInRow.get(0);
            int[] column = indexOfNumberOfZerosInColumns.get(0);
            if (row[1] > column[1]) {
                // Cross out the zeros in the row
                for (int i = 0; i < matrix[row[0]].length; i++)
                    if (matrix[row[0]][i].getNumber() != -1)
                        matrix[row[0]][i].setCrossedOut(true, false);

                indexOfNumberOfZerosInRow.remove(0);
            } else {
                // Cross out the zeros in the column
                for (int i = 0; i < matrix[column[0]].length; i++)
                    if (matrix[i][column[0]].getNumber() != -1)
                        matrix[i][column[0]].setCrossedOut(true, true);

                indexOfNumberOfZerosInColumns.remove(0);
            }

            ++numberOfLines;

        }
        System.out.println(indexOfNumberOfZerosInColumns.size());
        System.out.println(indexOfNumberOfZerosInRow.size());
        indexOfNumberOfZerosInColumns.clear();
        indexOfNumberOfZerosInRow.clear();

        return numberOfLines;
    }


    private boolean allZerosHaveALine(NumberBox[][] matrix) {

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j].getNumber() == 0.0 && !matrix[i][j].isCrossedOut())
                    return false;
                if (matrix[j][i].getNumber() == 0.0 && !matrix[j][i].isCrossedOut())
                    return false;
            }
        }
        return true;
    }

    private void givePeoplePartner(NumberBox[][] numberBox) {

    }

    private NumberBox[][] splitDiagonal(NumberBox[][] matrix) {
        NumberBox[][] resultMatrix = new NumberBox[matrix.length][matrix.length];
        //Here we cut the matrix diagonally
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (i <= j)
                    resultMatrix[i][j] = matrix[i][j];
                else
                    resultMatrix[i][j] = new NumberBox(-1);
            }
        }
        return resultMatrix;
    }


    <T> void printMatrix(T[][] matrix) {
        for (T[] ints : matrix) {
            for (T anInt : ints) {
                System.out.print(anInt + " ");
            }

            System.out.println();
        }
    }
    //This is correct
    private double calculateCost(Person person1, Person person2) {
        int cost = 10;

        if (person1.equals(person2))
            return -1;

        //Can't have singles build a pair if they are in the same building
        // or have the same kitchen and are not registered as a couple
        if (!(person1.getKitchen() == null) && !(person2.getKitchen() == null) && person1.getKitchen().distance(person2.getKitchen()) == 0)
            return -1;

        //If food preference is not equal
        if (!(person1.getFoodPreference().equals(person2.getFoodPreference()))) {
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
        if (person1.getKitchen() != null && person2.getKitchen() != null) {
            cost +=  person1.getKitchen().distance(person2.getKitchen());
        } else if ((person1.getKitchen() != null && person2.getKitchen() == null) || (person1.getKitchen() == null && person2.getKitchen() != null)) {
            if (cost < 30)
                cost = 0;
            else
                cost -= 30;
        }
        else
            return -1;


        //If age difference is too big
        cost += Math.abs(person1.getAge().ordinal() - person2.getAge().ordinal()) * 20;

        if (person1.getGender().equals(person2.getGender()))
            cost += 200;
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
