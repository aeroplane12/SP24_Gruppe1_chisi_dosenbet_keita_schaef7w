package sw.praktikum.spinfood.model;

import sw.praktikum.spinfood.model.tools.NumberBox;

import java.util.ArrayList;
import java.util.List;

public class CoupleManager {

    private static CoupleManager instance;
    private Location partyLoc;

    private Person[] arrNoKitchen;
    private Person[] arrWithKitchen;

    CoupleManager() {
        instance = this;
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

    void givePeopleWithoutPartner(List<Person> singles, Strictness strictnessLevel, Location partyLoc) {
        if (!(strictnessLevel == Strictness.A || strictnessLevel == Strictness.B || strictnessLevel == Strictness.C))
            throw new IllegalArgumentException("Strictness level hast to be one of A,B or C");

        this.partyLoc = partyLoc;
        allSingleParticipants = singles;
        calcCouples(strictnessLevel);

    }

    void calcCouples(Strictness strictnessLevel) {

        if (allSingleParticipants.size() < 2)
            return;



        if (strictnessLevel == Strictness.A){
            List<Person> vegans = allSingleParticipants.stream().filter(x -> x.getFoodPreference().equals(FoodPreference.FoodPref.VEGAN)).toList();
            List<Person> vegetarians = allSingleParticipants.stream().filter(x -> x.getFoodPreference().equals(FoodPreference.FoodPref.VEGGIE)).toList();
            List<Person> MeantAndAny = allSingleParticipants.stream().filter(x -> x.getFoodPreference().equals(FoodPreference.FoodPref.MEAT) ||
                    x.getFoodPreference().equals(FoodPreference.FoodPref.NONE)).toList();
            bringSingleTogether(createNumberBoxMatrix(vegans), vegans);
            bringSingleTogether(createNumberBoxMatrix(vegetarians), vegetarians);
            bringSingleTogether(createNumberBoxMatrix(MeantAndAny), MeantAndAny);
            bringSingleTogether(createNumberBoxMatrix(MeantAndAny), MeantAndAny);
        }

        else if (strictnessLevel == Strictness.B) {
            List<Person> withKitchen = allSingleParticipants.stream().filter(x -> x.getKitchen() != null).toList();
            List<Person> noKitchen = allSingleParticipants.stream().filter(x -> x.getKitchen() == null).toList();

            arrWithKitchen = new Person[withKitchen.size()];
            for (int i = 0; i < withKitchen.size(); i++)
                arrWithKitchen[i] = withKitchen.get(i);

            arrNoKitchen = new Person[noKitchen.size()];
            for (int i = 0; i < noKitchen.size(); i++)
                arrNoKitchen[i] = noKitchen.get(i);
            matchingSingleTogether(subtractSmallestNoKitchen(createNumberBoxMatrix(arrWithKitchen, arrNoKitchen), false), arrWithKitchen, arrNoKitchen);
            if (allSingleParticipants.size() == 2) {
                couples.add(new Couple(Manager.couple_Counter++, allSingleParticipants.get(0), allSingleParticipants.get(1), allSingleParticipants.get(0).getKitchen(), allSingleParticipants.get(1).getKitchen(), getFoodPref(allSingleParticipants.get(0), allSingleParticipants.get(1)), partyLoc));
                allSingleParticipants.get(0).setPartner(allSingleParticipants.get(1));
                allSingleParticipants.get(1).setPartner(allSingleParticipants.get(0));
                allSingleParticipants.clear();
            }
        }

         else if(strictnessLevel == Strictness.C)
            bringSingleTogether(createNumberBoxMatrix(allSingleParticipants), allSingleParticipants);
        else
            throw new IllegalArgumentException("Please give a Strictness level A, B or C not");
        }


    private boolean bothKitchenNull(List<Person> allSingleParticipants) {
        return allSingleParticipants.stream().allMatch(x -> x.getKitchen() == null);
    }

    //This is correct

    private NumberBox[][] createNumberBoxMatrix(List<Person> people) {
        NumberBox[][] matrix = new NumberBox[people.size()][people.size()];
        for (int i = 0; i < people.size(); i++)
            for (int j = 0; j < people.size(); j++)
                matrix[i][j] = new NumberBox(calculateCost(people.get(i), people.get(j)));


        return matrix;
    }

    private NumberBox[][] createNumberBoxMatrix(Person[] withKitchen, Person[] arrNoKitchen) {
        NumberBox[][] matrix = new NumberBox[withKitchen.length][arrNoKitchen.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = new NumberBox(calculateCostWithoutKitchen(withKitchen[i], arrNoKitchen[j], false));
            }
        }
        return matrix;
    }

    private NumberBox[][] subtractSmallestNoKitchen(NumberBox[][] matrix) {
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

    private NumberBox[][] subtractSmallestNoKitchen(NumberBox[][] matrix, boolean withKitch) {

        for (NumberBox[] numberBoxes : matrix) {
            // Find the smallest number in the row
            double smallestNumberRow = Integer.MAX_VALUE;
            for (NumberBox numberBox : numberBoxes) {
                if (numberBox.getNumber() < smallestNumberRow)
                    smallestNumberRow = numberBox.getNumber();
            }
            if (smallestNumberRow < 0)
                throw new IllegalArgumentException("Smallest number in row is negative");

            // Subtract the smallest number from all numbers in the row
            for (NumberBox numberBox : numberBoxes) {
                numberBox.setNumber(numberBox.getNumber() - smallestNumberRow);
            }
        }
        return matrix;
    }

    private void bringSingleTogether(NumberBox[][] matrix, List<Person> people) {
        crossingOutZeros(subtractSmallestNoKitchen(matrix));

        matrix = splitDiagonal(matrix);
        matchingSingleTogether(matrix, people);
        //printMatrix(createNumberBoxMatrix(stillSingle));
    }


    //This is correct
    private void crossingOutZeros(NumberBox[][] matrix) {
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

        int numberOfLines = 0;
        // now we need to cross out the zeros by the row or column with the most zeros
        while (!allZerosHaveALine(matrix) && numberOfLines != matrix.length) {
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
        indexOfNumberOfZerosInColumns.clear();
        indexOfNumberOfZerosInRow.clear();
    }

    //This is correct
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

    //This is correct, I think
    private void matchingSingleTogether(NumberBox[][] numberBox, List<Person> people) {
        List<Person> stillSingle = new ArrayList<>();
        for (int i = 0; i < numberBox.length; i++) {
            for (int j = 0; j < numberBox[i].length; j++) {
                if (numberBox[i][j].getNumber() == 0.0 && people.get(j).getPartner() == null && people.get(i).getPartner() == null) {

                    FoodPreference.FoodPref foodPref = getFoodPref(people, i, j);

                    Couple couple = new Couple(Manager.couple_Counter++,
                            people.get(i),
                            people.get(j),
                            people.get(i).getKitchen(),
                            people.get(j).getKitchen(),
                            foodPref,
                            partyLoc);
                    people.get(i).setPartner(people.get(j));
                    people.get(j).setPartner(people.get(i));
                    couples.add(couple);
                    for (int k = 0; k < numberBox.length; k++) {
                        numberBox[i][k].setNumber(-1);
                        numberBox[k][j].setNumber(-1);
                    }
                    break;
                }
            }
        }

        for (int i = 0; i < numberBox.length; i++) {
            if (people.get(i).getPartner() == null)
                stillSingle.add(people.get(i));
        }

        allSingleParticipants = stillSingle;
    }

    private void matchingSingleTogether(NumberBox[][] numberBoxes, Person[] arrWithKitchen, Person[] arrNoKitchen) {
        //In each column, find the smallest number and the x and y coordinates are then made to -1 for each person in arrWithKitchen and arrNoKitchen
        for (int i = 0; i < numberBoxes.length; i++) {
            double smallestNumber = Integer.MAX_VALUE;
            int x = 0;
            int y = 0;
            for (int j = 0; j < numberBoxes[i].length; j++) {
                if (numberBoxes[i][j].getNumber() > -1 && numberBoxes[i][j].getNumber() < smallestNumber) {
                    smallestNumber = numberBoxes[i][j].getNumber();
                    x = i;
                    y = j;
                }
            }

            for (int j = 0; j < numberBoxes[x].length; j++) {
                numberBoxes[x][j].setNumber(-1);
                crossOutColumn(numberBoxes, y);
            }
            if (arrWithKitchen[x] == null || arrNoKitchen[y] == null)
                continue;
            FoodPreference.FoodPref foodPref = getFoodPref(arrWithKitchen[x], arrNoKitchen[y]);

            Couple couple = new Couple(Manager.couple_Counter++,
                    arrWithKitchen[x],
                    arrNoKitchen[y],
                    arrWithKitchen[x].getKitchen(),
                    arrNoKitchen[y].getKitchen(),
                    foodPref,
                    partyLoc);
            arrWithKitchen[x].setPartner(arrNoKitchen[y]);
            arrNoKitchen[y].setPartner(arrWithKitchen[x]);
            couples.add(couple);
            arrWithKitchen[x] = null;
            arrNoKitchen[y] = null;
        }

        List<Person> stillSingle = new ArrayList<>();
        for (Person person : arrWithKitchen) {
            if (person != null)
                stillSingle.add(person);
        }
        for (Person person : arrNoKitchen) {
            if (person != null)
                stillSingle.add(person);
        }
        allSingleParticipants = stillSingle;


    }

    private void crossOutColumn(NumberBox[][] numberBoxes, int column) {
        for (NumberBox[] numberBox : numberBoxes) {
            numberBox[column].setNumber(-1);
        }
    }
    //This is correct

    public double calculateCost(Person person1, Person person2) {
        int cost = 0;

        if (person1.equals(person2))
            return -1;

        if (person1.getKitchen() == null && person2.getKitchen() == null) {
            return -1;
        }

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

        if (person1.getKitchen() != null && person2.getKitchen() != null) {
            cost += person1.getKitchen().distance(person2.getKitchen());
        } else if ((person1.getKitchen() != null && person2.getKitchen() == null) || (person1.getKitchen() == null && person2.getKitchen() != null)) {
            if (cost < 100)
                cost = 0;
            else
                cost -= 100;
        }

        //If age difference is too big
        cost += Math.abs(person1.getAge().ordinal() - person2.getAge().ordinal()) * 20;

        if (person1.getGender().equals(person2.getGender()))
            cost += 200;

        return cost;
    }

    private double calculateCostWithoutKitchen(Person person1, Person person2, boolean withKitchenCost) {
        if (withKitchenCost)
            return calculateCost(person1, person2);

        int cost = 0;

        if (person1.equals(person2))
            return -1;

        if (person1.getKitchen() == null && person2.getKitchen() == null) {
            return -1;
        }

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

        cost += Math.abs(person1.getAge().ordinal() - person2.getAge().ordinal()) * 20;

        if (person1.getGender().equals(person2.getGender()))
            cost += 200;

        return cost;
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

    private static FoodPreference.FoodPref getFoodPref(List<Person> people, int i, int j) {
        FoodPreference.FoodPref foodPref = people.get(i).getFoodPreference();
        // Determine the main food preference for the couple
        if (people.get(i).getFoodPreference().equals(FoodPreference.FoodPref.NONE) && people.get(j).getFoodPreference().equals(FoodPreference.FoodPref.NONE)) {
            foodPref = FoodPreference.FoodPref.MEAT;
        } else if (people.get(i).getFoodPreference().equals(FoodPreference.FoodPref.NONE)) {
            return people.get(j).getFoodPreference();
        } else if (people.get(j).getFoodPreference().equals(FoodPreference.FoodPref.NONE)) {
            return people.get(i).getFoodPreference();
        } else if (people.get(i).getFoodPreference().equals(FoodPreference.FoodPref.VEGGIE) && people.get(j).getFoodPreference().equals(FoodPreference.FoodPref.VEGAN)) {
            return FoodPreference.FoodPref.VEGAN;
        } else if (people.get(i).getFoodPreference().equals(FoodPreference.FoodPref.VEGAN) && people.get(j).getFoodPreference().equals(FoodPreference.FoodPref.VEGGIE)) {
            return FoodPreference.FoodPref.VEGAN;
        } else if (people.get(i).getFoodPreference().equals(FoodPreference.FoodPref.VEGAN) && people.get(j).getFoodPreference().equals(FoodPreference.FoodPref.MEAT)) {
            return FoodPreference.FoodPref.VEGAN;
        } else if (people.get(i).getFoodPreference().equals(FoodPreference.FoodPref.MEAT) && people.get(j).getFoodPreference().equals(FoodPreference.FoodPref.VEGAN)) {
            return FoodPreference.FoodPref.VEGAN;
        } else if (people.get(i).getFoodPreference().equals(FoodPreference.FoodPref.MEAT) && people.get(j).getFoodPreference().equals(FoodPreference.FoodPref.VEGGIE)) {
            return FoodPreference.FoodPref.VEGGIE;
        } else if (people.get(i).getFoodPreference().equals(FoodPreference.FoodPref.VEGGIE) && people.get(j).getFoodPreference().equals(FoodPreference.FoodPref.MEAT)) {
            return FoodPreference.FoodPref.VEGGIE;
        } else {

            return foodPref;
        }


        return foodPref;
    }

    private static FoodPreference.FoodPref getFoodPref(Person person1, Person person2) {
        FoodPreference.FoodPref foodPref = person1.getFoodPreference();
        // Determine the main food preference for the couple
        if (person1.getFoodPreference().equals(FoodPreference.FoodPref.NONE) && person2.getFoodPreference().equals(FoodPreference.FoodPref.NONE)) {
            foodPref = FoodPreference.FoodPref.MEAT;
        } else if (person1.getFoodPreference().equals(FoodPreference.FoodPref.NONE)) {
            return person2.getFoodPreference();
        } else if (person2.getFoodPreference().equals(FoodPreference.FoodPref.NONE)) {
            return person1.getFoodPreference();
        } else if (person1.getFoodPreference().equals(FoodPreference.FoodPref.VEGGIE) && person2.getFoodPreference().equals(FoodPreference.FoodPref.VEGAN)) {
            return FoodPreference.FoodPref.VEGAN;
        } else if (person1.getFoodPreference().equals(FoodPreference.FoodPref.VEGAN) && person2.getFoodPreference().equals(FoodPreference.FoodPref.VEGGIE)) {
            return FoodPreference.FoodPref.VEGAN;
        } else if (person1.getFoodPreference().equals(FoodPreference.FoodPref.VEGAN) && person2.getFoodPreference().equals(FoodPreference.FoodPref.MEAT)) {
            return FoodPreference.FoodPref.VEGAN;
        } else if (person1.getFoodPreference().equals(FoodPreference.FoodPref.MEAT) && person2.getFoodPreference().equals(FoodPreference.FoodPref.VEGAN)) {
            return FoodPreference.FoodPref.VEGAN;
        } else if (person1.getFoodPreference().equals(FoodPreference.FoodPref.MEAT) && person2.getFoodPreference().equals(FoodPreference.FoodPref.VEGGIE)) {
            return FoodPreference.FoodPref.VEGGIE;
        } else if (person1.getFoodPreference().equals(FoodPreference.FoodPref.VEGGIE) && person2.getFoodPreference().equals(FoodPreference.FoodPref.MEAT)) {
            return FoodPreference.FoodPref.VEGGIE;
        } else {

            return foodPref;
        }


        return foodPref;

    }


    <T> void printMatrix(T[][] matrix) {
        for (T[] ints : matrix) {
            for (T anInt : ints) {
                System.out.print(anInt + " ");
            }

            System.out.println();
        }
    }


    public void addPerson(Person person, Strictness strictness) {
        allSingleParticipants.add(person);
        if (allSingleParticipants.size() > 1)
            calcCouples(strictness);
    }

    public void removeSinglePerson(Person person, Strictness strictness) {
        if (!allSingleParticipants.contains(person))
            throw new IllegalArgumentException("Person not found");
        else
            allSingleParticipants.remove(person);
        calcCouples(strictness);
    }

    public void cancelPerson(Person person, Strictness strictness) {

        if (!allSingleParticipants.contains(person) && person.hasPartner()) {
            allSingleParticipants.add(person.getPartner());
            couples.stream().reduce((x, y) -> x.getPerson1().equals(person) || x.getPerson2().equals(person) ? x : y).ifPresent(couples::remove);
            calcCouples(strictness);
        } else if (allSingleParticipants.contains(person)) {
            throw new IllegalArgumentException("Person already is single");
        } else {
            throw new IllegalArgumentException("Person not found");
        }

    }


    public Person getSinglePerson(String ID) {
        return allSingleParticipants.stream()
                .filter(x -> x.getID().equals(ID))
                .findAny()
                .orElse(null);
    }

    public List<Person> getSingleList() {
        return allSingleParticipants;
    }

    public List<Couple> getCouples() {
        return couples;
    }

    public List<Person> getAllSingleParticipants() {
        return allSingleParticipants;
    }

    public void cancelCouple(Couple couple) {
        if (couples.contains(couple))
            couples.remove(couple);
        else throw new IllegalArgumentException("Couple not found");
    }

    public void restManager (){
        couples.clear();
        allSingleParticipants.clear();
    }

}
