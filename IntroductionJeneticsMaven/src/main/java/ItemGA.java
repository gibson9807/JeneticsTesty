import static java.lang.String.format;

public class ItemGA {
    private int id;

    public ItemGA(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return format("Item[ID=%d]", id);
    }


}
