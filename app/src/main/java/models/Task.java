package models;

/**
 * Created by magupta on 9/26/15.
 */
public class Task {
    public String getName() {
        return name;
    }

    public Task(String name){
        this.name = name;
    }

    public Task(int id, String name){
        this.id = id;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getName();
    }

    private String name;
    private int id;

}
