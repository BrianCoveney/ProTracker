package ie.cit.architect.protracker.model;

import ie.cit.architect.protracker.helpers.Consts;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by brian on 01/05/17.
 */
public class EmployeeUser implements IUser{

    private String name;
    private String password;
    private String emailAddress;


    public EmployeeUser() {
    }


    public EmployeeUser(String emailAddress) {
        this.emailAddress = emailAddress;
    }


    public EmployeeUser(String emailAddress, String password) {
        super();
        this.emailAddress = emailAddress;
        this.password = password;
    }



    @Override
    public String getName() {
        return name;
    }


    @Override
    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public String getEmailAddress() {
        return emailAddress;
    }


    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEmailValid(String eMail) {
        if(eMail != null) {
            Pattern pattern = Pattern.compile(Consts.VALID_EMAIL_REGEX);
            Matcher matcher = pattern.matcher(eMail);
            return matcher.matches();
        }
        return false;
    }


    public boolean isNameValid() {
        if (getName().matches(Consts.VALID_NAME)) {
            return true;
        }
        return false;
    }
}
