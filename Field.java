package sample;

public interface Field<T>{
    T add(T a);
    T sub(T a);
    T mul(T a);
    T div(T a);
    String toString();
}
