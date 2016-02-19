package reactive.model;

public class Deal {
    public final int id;
    public final String data;

    public Deal(int id, String data) {
        this.id = id;
        this.data = data;
    }

    @Override
    public String toString() {
        return String.format("{\"id\": %d, \"data\": %s}", id, data);
    }
}
