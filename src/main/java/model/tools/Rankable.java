package model.tools;

@FunctionalInterface
public interface Rankable<E> {
    Double rank(E value,E comparedValue);
}
