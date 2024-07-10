# SP24_Gruppe1_chisi_dosenbet_keita_schaef7w

## Table of Content
1. [Aufteilung des Projekts](#aufteilung-des-projects-und-aufträge)
2. [Pseudo-Code Couple Manager](#pseudo-code-couple-manager)
3. [Pseudo-Code Group Manager](#pseudo-code-group-manager)

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
