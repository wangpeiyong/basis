package org.wpy.orika;

import java.util.Date;

/**
 * Created by wpy on 2017/6/13.
 */

public class PersonDto {
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String[][] aliases;

    public PersonDto(String firstName, String lastName, Date birthDate, String[][] aliases) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.aliases = aliases;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String[][] getAliases() {
        return aliases;
    }

    public void setAliases(String[][] aliases) {
        this.aliases = aliases;
    }
}
