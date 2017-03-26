package ie.cit.architect.protracker.model;

/**
 * Created by brian on 08/03/17.
 */
public class Project implements IProject{

    private String name;
    private String date;
    private String author;
    private String location;
    private String clientName;


    public Project() {}

    public Project(String location) {
        this.location = location;
    }

    public Project(String name, String date, String author, String location, String clientName) {
        this.name = name;
        this.date = date;
        this.author = author;
        this.location = location;
        this.clientName = clientName;
    }


    @Override
    public String getLocation() {
        return location;
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
    public String getDate() {
        return date;
    }

    @Override
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String getClientName() {
        return clientName;
    }

    @Override
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", author='" + author + '\'' +
                ", location='" + location + '\'' +
                ", clientName='" + clientName + '\'' +
                '}';
    }



}
