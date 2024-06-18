package model.tools;

import model.Couple;
import model.Group;
import model.Person;

import java.util.List;

public class Container {

    private List<Person> personList;

    private List<Couple> coupleList;
    private List<Group> groupList;

    public Container(List<Person> personList, List<Couple> coupleList, List<Group> groupList) {
        this.personList = personList;
        this.coupleList = coupleList;
        this.groupList = groupList;

    }

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    public List<Couple> getCoupleList() {
        return coupleList;
    }

    public void setCoupleList(List<Couple> coupleList) {
        this.coupleList = coupleList;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }
}
