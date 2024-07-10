# SP24_Gruppe1_chisi_dosenbet_keita_schaef7w

## ModelTests
1. [Pseudo-Code Couple Manager](#pseudo-code-couple-manager)
2. [Pseudo-Code Group Manager](#pseudo-code-group-manager)


## Aufteilung des Projects und Aufträge
### Mitglieder
+ Andreas
+ Felix
+ Keita
+ Tobias
### Aufbau
Gemäß Model-View-Control Modells
### Aufteilung
| Meilenstein 1   | Andreas | Felix | Keita | Tobias |
|-----------------|:-------:|:-----:|:-----:|:------:|
| csv. reader     |         |   x   |       |        |
| unit-tests      |         |       |   x   |   x    |
| user-Diagram    |         |       |       |   x    |
| basic-structure |    x    |       |       |        |

| Meilenstein 2 | Andreas | Felix | Keita | Tobias |
|---------------|:-------:|:-----:|:-----:|:------:|
| csv. writer   |    x    |       |       |        |
| CoupleManager |         |       |       |   x    |
| GroupManager  |         |   x   |       |        |
| UML-Diagram   |    x    |       |       |        |
| Tests & stuff |         |   x   |   x   |   x    |

| Meilenstein 3 (changes pending)   | Andreas | Felix | Keita | Tobias |
|-----------------------------------|:-------:|:-----:|:-----:|:------:|
| View                              |    x    |       |   x   |        |
| Controller                        |    x    |       |       |        |
| Group-Manager changes             |         |   x   |       |        |
| Couple-Manager changes            |         |       |       |   x    |
| Documentation <br/> (pseudo-Code) |         |   x   |       |   x    |



### To-Do
- [x] Grundstruktur
- [ ] GUI
- [ ] Controller
- [x] Model
- - [x] Pers. to Couple
- - [x] Couple to Group
- - [x] General Manager

## Pseudo-Code Couple-Manager

    void givePeopleWithoutPartner(List<Person> singles, Strictness strictnessLevel, Location partyLoc) {
    calcCouples(strictnessLevel);
    }
    
    void calcCouples(Strictness strictnessLevel) {
    switch (strictnessLevel) {
    case A:
    matchSingles(createMatrix(vegetarians), vegetarians);
    matchSingles(createMatrix(mixedGroup), mixedGroup);
    break;
    case B:
    //with and without kitchen
    break;
    case C:
    //maximum number off matches
    break;
    default:
    throw new IllegalArgumentException("Invalid Strictness level");
    }
    }
    
    private NumberBox[][] createMatrix(List<Person> singles) {
    int size = singles.size();
    NumberBox[][] matrix = new NumberBox[size][size];
    for (int i = 0; i < size; i++) {
    for (int j = 0; j < size; j++) {
    matrix[i][j] = new NumberBox(calculateCost(singles.get(i), singles.get(j)));
    }
    }
    return matrix;
    }
    
    public double calculateCost(Person p1, Person p2) {
    int cost = 0;

    if (!p1.hasSameFoodPreference(p2)) {
        cost += calculateFoodPreferenceCost(p1, p2);
    }
    cost += calculateKitchenDistanceCost(p1, p2);
    cost += calculateAgeDifferenceCost(p1, p2);
    cost += calculateGenderCost(p1, p2);

    return cost;
    }
    
    private void matchSingles(NumberBox[][] matrix, List<Person> people) {
    adjustMatrix(matrix);
    formCouples(matrix, people);
    }
    
    private void matchSinglesWithAdjustment(NumberBox[][] matrix, List<Person> group1, List<Person> group2) {
    adjustMatrix(matrix);
    matrix = splitMatrix(matrix);
    formCouples(findZeroCoordinates(matrix), group1, group2);
    }
    
    // Helper Methods
    private List<Person> filterByKitchenAvailability(List<Person> participants, boolean hasKitchen) {
    return participants.stream().filter(p -> p.hasKitchen() == hasKitchen).collect(Collectors.toList());
    }
    
    private int calculateFoodPreferenceCost(Person p1, Person p2) {
    // Implementation for calculating cost based on food preference
    }
    
    private int calculateKitchenDistanceCost(Person p1, Person p2) {
    // Implementation for calculating cost based on kitchen distance
    }
    
    private int calculateAgeDifferenceCost(Person p1, Person p2) {
    // Implementation for calculating cost based on age difference
    }
    
    private int calculateGenderCost(Person p1, Person p2) {
    // Implementation for calculating cost based on gender
    }
    
    private void adjustMatrix(NumberBox[][] matrix) {
    // Implementation for adjusting the matrix
    }
    
    private void formCouples(NumberBox[][] matrix, List<Person> singles) {
        for (int i = 0; i < numberBox.length; i++) {
            for (int j = 0; j < numberBox[i].length; j++) 
                if (numberBox[i][j].getNumber() == 0.0) 
                    Couple couple = new Couple(people.get(i), people.get(j));
        }

        for (int i = 0; i < numberBox.length; i++) {
            if (people.get(i).getPartner() == null)
                stillSingle.add(people.get(i));
        }
    }

```

```
## Pseudo-Code Group-Manager
```
//preprocess input
input = filterUnusableKitchen(allCouples)
while !(input|9)
    removePersonFrom(input)
rank = x,y->(x.attibute0-y.attribute0)*attribute0Weight
        +...
        + (x.attibuten-y.attributen)*attributenWeight
//match 9 couples
sortByLocation(input)
for (input.size / 9)
    cluster = Couple[9]
    foreach course
        //fill each course-layer in cluster, 
        // a,b,c are the hosts of each layer
         a = minRankComparedToCluster(input)
         cluster.add(a)
         b = minRankComparedToCluster(input)
         cluster.add(b)
         c = minRankComparedToCluster(input)
         cluster.add(c)
    // create 9 groups, in accordance to the cluster
    output.add(createGroupCluster(cluster))
    
return output
```
