package ie.cit.architect.protracker.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by brian on 13/03/17.
 */
public class EmployeeList implements Serializable{

    private ArrayList<IUser> employeeUsers;

    public EmployeeList() {
        this.employeeUsers = new ArrayList<>();
    }

    public ArrayList<IUser> getEmployeeUsers() {
        return employeeUsers;
    }

    public void setEmployeeUsers(ArrayList<IUser> employeeUsers) {
        this.employeeUsers = employeeUsers;
    }

    public void add(IUser user) { this.employeeUsers.add(user); }
}
