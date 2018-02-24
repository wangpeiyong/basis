package org.wpy.orika;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wpy on 2017/6/13.
 */
public class Person {
    private Name name;
    private List<Name> knownAliases;
    private Date birthDate;

    public Person(Name name, Date birthDate, List<Name> knownAliases) {
        this.name = name;
        this.birthDate = (Date) birthDate.clone();
        this.knownAliases = new ArrayList<Name>(knownAliases);
    }

    public Person() {
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public List<Name> getKnownAliases() {
        return knownAliases;
    }

    public void setKnownAliases(List<Name> knownAliases) {
        this.knownAliases = knownAliases;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name=" + name +
                ", knownAliases=" + knownAliases +
                ", birthDate=" + birthDate +
                '}';
    }
}

