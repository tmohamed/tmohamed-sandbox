package generics.generic_method;

import lombok.ToString;

public class TypeEraserApplication {
    public static void main(String[] args) {
        GenType<Integer, Integer> genType = new GenType<>(10, 30);
        System.out.println(genType);
    }
}

@ToString
class GenType<K, V>{
    private K key;
    private V value;

    public GenType(K key, V value){
        this.key = key;
        this.value = value;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}