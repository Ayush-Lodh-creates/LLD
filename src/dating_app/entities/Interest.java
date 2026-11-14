package dating_app.entities;

public class Interest {
    private final String name;
    private final String category;

    public Interest(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
}

