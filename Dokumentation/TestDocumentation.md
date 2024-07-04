# Tests

-----------
## ModelTests
1. [AgeGroupTest](#AgeGroup) 
2. [CoupleManager](#CoupleManager)
3. [FoodPreference](#foodpreference)
4. [GenderTest](#gender)
5. [GroupManager](#groupmanager)
6. [KitchenTest](#kitchen)
7. [LocationTest](#location)
8. [ManagerTest](#manager)
9. [PersonTest](#person)
## ViewTests

## ControlTests

------------
<!---

| Test name:               |   |
|--------------------------|---|
| Vorbedingung:            |   |
| Ablauf:                  |   |
| Erwartetes Verhalten:    |   |
| Tatsächliches Verhalten: |   |
-->


## AgeGroup


| Test                | #Tests | #Fehler | voll.<br/> abd. |
|---------------------|--------|---------|-----------------|
| getAgeRange(String) | 9      | 0       | ja              |


| Test name:               | AgeRangeTest                                                                                                                             |
|--------------------------|------------------------------------------------------------------------------------------------------------------------------------------|
| Vorbedingung:            | Ein bestimmtes Alter wird festgelegt, das in einen bestimmten Bereich fällt.                                                             |
| Ablauf:                  | Die Methode "getAgeRange()" wird mit dem festgelegten Alter als Eingabe aufgerufen.                                                      |
| Erwartetes Verhalten:    | festgelegte Alter wird einer bestimmten Altersgruppe zugeordnet, entsprechend den definierten Altersbereichen in der Klasse "AgeGroup".  |
| Tatsächliches Verhalten: | Das tatsächliche Ergebnis der Methode wird mit dem erwarteten Ergebnis verglichen. Es wird erwartet, dass sie übereinstimmen.            |


## CoupleManager
| Test           | #Tests | #Fehler | voll. <br/> abd. |
|----------------|--------|---------|------------------|
| calcCouples()  |        | 0       | nein             |
| addPerson()    |        | 0       | nein             |
| removePerson() |        | 0       | nein             |

| Test name:               | testCalcCouples                                                                                                                                                      |
|--------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Vorbedingung:            | eine liste, gefüllt mit genügend vielen Personen                                                                                                                     |
| Ablauf:                  | Die Methode givePeopleWithoutPartner() wird aufgerufen, um Paare aus den Singles zu bilden, ohne Mindest- oder Höchstanzahl von Paaren festzulegen                   |
| Erwartetes Verhalten:    | Personen werden passend zu ihren vorlieben und ihrem küchenstatus in Paare eingeteilt und dementsprechend 'gematcht', es werden instanzen der Couple Klasse erstellt |
| Tatsächliches Verhalten: | gemäß erwartungen                                                                                                                                                    |


| Test name:               | testCalcCouples |
|--------------------------|-----------------|
| Vorbedingung:            |                 |
| Ablauf:                  |                 |
| Erwartetes Verhalten:    |                 |
| Tatsächliches Verhalten: |                 |

| Test name:               | testAddPerson |
|--------------------------|---------------|
| Vorbedingung:            |               |
| Ablauf:                  |               |
| Erwartetes Verhalten:    |               |
| Tatsächliches Verhalten: |               |

| Test name:               | testRemovePerson |
|--------------------------|------------------|
| Vorbedingung:            |                  |
| Ablauf:                  |                  |
| Erwartetes Verhalten:    |                  |
| Tatsächliches Verhalten: |                  |

## FoodPreference
| Test                     | #Tests | #Fehler | voll. <br/> abd. |
|--------------------------|--------|---------|------------------|
| testGetFoodPref_None()   | 1      | 0       | ja               |
| testGetFoodPref_Meat()   | 1      | 0       | ja               |
| testGetFoodPref_Veggie() | 1      | 0       | ja               |
| testGetFoodPref_Vegan()  | 1      | 0       | ja               |

| Test name:               | FoodPreferenceTest                                                                                                                    |
|--------------------------|---------------------------------------------------------------------------------------------------------------------------------------|
| Vorbedingung:            | Eine bestimmte Nahrungsmittelpräferenz wird festgelegt, die einer der in der Klasse "FoodPreference" definierten Optionen entspricht. |
| Ablauf:                  | Die Methode "getFoodPref()" wird mit der festgelegten Nahrungsmittelpräferenz als Eingabe aufgerufen.                                 |
| Erwartetes Verhalten:    | Die festgelegte Nahrungsmittelpräferenz wird dem entsprechenden FoodPref-Enum-Wert zugeordnet.                                        |
| Tatsächliches Verhalten: | Das tatsächliche Ergebnis der Methode wird mit dem erwarteten Ergebnis verglichen. Es wird erwartet, dass sie übereinstimmen.         |

## Gender
| Test                       | #Tests | #Fehler | voll. <br/> abd. |
|----------------------------|--------|---------|------------------|
| genderValue getGen(String) | 3      | 0       | ja               |

| Test name:               | GenderTest                                                                                                                     |
|--------------------------|--------------------------------------------------------------------------------------------------------------------------------|
| Vorbedingung:            | Ein bestimmtes Geschlecht wird festgelegt, das einer der in der Klasse "Gender" definierten Optionen entspricht.               |
| Ablauf:                  | Die Methode "getGen()" wird mit dem festgelegten Geschlecht als Eingabe aufgerufen.                                            |
| Erwartetes Verhalten:    | Das festgelegte Geschlecht wird dem entsprechenden "genderValue"-Enum-Wert zugeordnet.                                         |
| Tatsächliches Verhalten: | Das tatsächliche Ergebnis der Methode wird mit dem erwarteten Ergebnis verglichen. Es wird erwartet, dass sie übereinstimmen.  |

## GroupManager
| Test               | #Tests | #Fehler | voll. <br/> abd. |
|--------------------|--------|---------|------------------|
| testCalcGroups()   | 8      | 0       |                  |
| testKitchen()      | 1      | 0       |                  |
| testRemoveCouple() | 4      | 0       |                  |

| Test name:               | testCalcGroups                                                                                                                                     |
|--------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------|
| Vorbedingung:            | - GroupManager wurde initialisiert <br/> - Eingabe von einer diversen liste von Couples                                                            |
| Ablauf:                  | Zuerst werden alle überbuchten Küchen entfernt, dann                                                                                               |
| Erwartetes Verhalten:    | x wird aus liste gelöscht, nachrücker ersetzt x in gruppen allokation                                                                              |
| Tatsächliches Verhalten: | nach durchführen der remove() methode, beinhaltet die processed Couple liste nicht mehr das couple x, und die Nachrücker liste ist um 1 verringert |

| Test name:               | testKitchenConflicts                                                                                                                               |
|--------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------|
| Vorbedingung:            | - GroupManager wurde initialisiert <br/> - Eingabe von liste von Couples, in welcher sich sehr viele eine Küche teilen                             |
| Ablauf:                  |                                                                                                                                                    |
| Erwartetes Verhalten:    | x wird aus liste gelöscht, nachrücker ersetzt x in gruppen allokation                                                                              |
| Tatsächliches Verhalten: | nach durchführen der remove() methode, beinhaltet die processed Couple liste nicht mehr das couple x, und die Nachrücker liste ist um 1 verringert |

| Test name:               | Removal of Couple with successor                                                                                                                   |
|--------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------|
| Vorbedingung:            | - GroupManager initialisiert, <br/> - beinhaltet Gruppen mit dem Couple x <br/> - hat mind. ein weiteres Couple als Nachrücker                     |
| Ablauf:                  | Zuerst wird geprüft ob es nachrücker gibt, wenn möglich, wird das Couple x ersetzt, sonst werden die gruppen neu zugeordnet                        |
| Erwartetes Verhalten:    | x wird aus liste gelöscht, nachrücker ersetzt x in gruppen allokation                                                                              |
| Tatsächliches Verhalten: | nach durchführen der remove() methode, beinhaltet die processed Couple liste nicht mehr das couple x, und die Nachrücker liste ist um 1 verringert |

| Test name:               | Removal of Couple with overbooked successor                                                                                                                                            |
|--------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Vorbedingung:            | - GroupManager initialisiert, <br/> - beinhaltet Gruppen mit dem Couple x <br/> - hat keine Nachrücker, aber mind. ein weiteres Couple mit der selben küche wie x in overbookedKitchen |
| Ablauf:                  | Zuerst wird geprüft ob es nachrücker gibt, wenn möglich, wird das Couple x ersetzt, sonst werden die gruppen neu zugeordnet                                                            |
| Erwartetes Verhalten:    | x wird aus liste gelöscht, couple mit der selben küche ersetzt x in gruppen allokation                                                                                                 |
| Tatsächliches Verhalten: |                                                                                                                                                                                        |


## Kitchen

| Test               | #Tests | #Fehler | voll. <br/> abd. |
|--------------------|--------|---------|------------------|
| distance(Location) | 1      | 0       | ja               |
| distance(Location) | 1      | 0       | ja               |


## Location

| Test               | #Tests | #Fehler | voll. <br/> abd. |
|--------------------|--------|---------|------------------|
| distance(Location) | 1      | 0       | ja               |

## Manager

| Test           | #Tests | #Fehler | voll. <br/> abd. |
|----------------|--------|---------|------------------|
| inputPeople()  |        |         |                  |
| calcCouple()   |        |         |                  |
| calcGroups()   |        |         |                  |
| removeCouple() |        |         |                  |
| removePerson() |        |         |                  |

## Person

| Test | #Tests | #Fehler | voll. <br/> abd. |
|------|--------|---------|------------------|
|      |        |         |                  |
