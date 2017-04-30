package ie.cit.architect.protracker.model;

/**
 * Created by brian on 07/02/17.
 */
public interface IUser {

    String getName();

    String getEmailAddress();

    String getPassword();

    boolean isEmployeeEmail(String email);

    boolean isEmployeePassword(String password);
}
