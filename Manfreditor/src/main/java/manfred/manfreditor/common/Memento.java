package manfred.manfreditor.common;

public interface Memento<T> {

    Memento<T> backup();

    void restoreStateOf(T statefulObject);
}
