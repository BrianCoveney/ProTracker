package ie.cit.architect.protracker.model;

import java.util.Date;

/**
 * Created by brian on 23/03/17.
 */
public interface IProject {

    String getLocation();

    String getName();

    void setName(String name);

    Date getDate();

    void setDate();

    String getAuthor();

    String getClientName();

    int getProjectId();

    double getFee();

    void setFee(double fee);

    double getVat();

    double getTotal();

    void setVat();

    void setTotal();


}
