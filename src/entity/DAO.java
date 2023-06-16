package entity;
import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    Optional<T> get(int id);

    default List<T> getAll() {
        return null;
    }

    void insert(T t);
    void update(T t);
    void delete(T t);

    List<String> getColumnNames();
}
