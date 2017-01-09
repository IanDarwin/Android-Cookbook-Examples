package nl.codestone.recipelist;

public class Datum {
    
    int id;
    String title;

    public Datum(int id, String title) {
        this.id = id;
        this.title = title;
    }
    
    public String toString() {
        return title;
    }

    public int getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }

}
