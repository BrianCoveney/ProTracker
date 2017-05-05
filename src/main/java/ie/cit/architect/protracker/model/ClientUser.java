package ie.cit.architect.protracker.model;

/**
 * Created by brian on 01/05/17.
 */
public class ClientUser implements IUser{

    private String name;
    private String password;
    private String emailAddress;


    public ClientUser() {
    }


    public ClientUser(String emailAddress) {
        this.emailAddress = emailAddress;
    }


    public ClientUser(String emailAddress, String password) {
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


    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getEmailAddress() {
        return emailAddress;
    }


    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
