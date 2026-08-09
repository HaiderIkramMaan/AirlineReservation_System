public interface DataSerializer<T> {
    String serializeData();
    T deserializeData(String data);
}
