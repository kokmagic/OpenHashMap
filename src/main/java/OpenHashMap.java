import java.util.*;

public class OpenHashMap<K, V> implements Map<K, V> {
    private final int startCapacity = 8;
    private int size;
    private int capacity;
    private Entry<K, V>[] storage;
    private final double fillFactor = 0.75;
    private Entry<K, V> DELETED;

    public OpenHashMap() {
        capacity = startCapacity;
        storage = new Entry[capacity];
        size = 0;
    }

    //при заполнении таблицы на 75% увеличиваем размер таблицы в два раза и проводим рехэширование
    private void rehash() {
        final int newCapacity = capacity * 2;
        final Entry<K, V>[] newStorage = new Entry[newCapacity];
        for (int i = 0; i < capacity; ++i) {
            if (storage[i] != null && storage[i] != DELETED) {
                int hash = storage[i].getKey().hashCode() % newCapacity;
                while (newStorage[hash] != null || newStorage[hash] != DELETED) {
                    hash = (hash + 1) % newCapacity;
                }
                newStorage[hash] = new Entry<K, V>(storage[i].getKey(), storage[i].getValue());
            }
        }
        capacity = newCapacity;
        storage = newStorage;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object o) {
        if (o == null)
            return false;
        K key = (K) o;
        int hash = key.hashCode() % capacity;
        int start = hash;
        do {
            if (storage[hash] != null && key.equals(storage[hash].getKey()))
                return true;
            hash = (hash + 1) % capacity;
        } while (hash != start);
        return false;
    }

    @Override
    public boolean containsValue(Object o) { //тут не используем хэш функцию, потому что не знаем ключ
        if (o == null)
            return false;
        V value = (V) o;
        for (int i = 0; i < capacity; i++) {
            if (storage[i] != null && value.equals(storage[i].getValue()))
                return true;
        }
        return false;
    }

    @Override
    public V get(Object o) {
        if (o == null)
            throw new IllegalArgumentException();
        K key = (K) o;
        int hash = key.hashCode() % capacity;
        int start = hash;
        do {
            if (storage[hash] != null && key.equals(storage[hash].getKey()))
                return storage[hash].getValue();
            hash = (hash + 1) % capacity;
        } while (hash != start);
        return null;
    }

    @Override
    public Object put(Object o, Object o2) {
        if (o == null || o2 == null)
            throw new IllegalArgumentException();
        K key = (K) o;
        V value = (V) o2;
        if (fillFactor <= (size * 1.0 / capacity)) { //проверяем, если таблица заполнена на 75%, то увеличиваем размер
            rehash();                                //в 2 раза и проводим рехеширование
        }
        if (containsKey(key)) remove(key);
        int hash = key.hashCode() % capacity;
        while (storage[hash] != null || storage[hash] != DELETED) {
            hash = (hash + 1) % capacity;
        }
        storage[hash] = new Entry<K, V>(key, value);
        ++size;
        return value;
    }


    @Override
    public V remove(Object o) {
        if (o == null)
            throw new IllegalArgumentException();
        K key = (K) o;
        int hash = key.hashCode() % capacity;
        int start = hash;
        do {
            if (storage[hash] != null && key.equals(storage[hash].getKey())) {
                if (storage[hash] != null && key.equals(storage[hash].getKey())) {
                    V temp = storage[hash].getValue();
                    storage[hash] = DELETED;
                    size--;
                    return temp;
                }
               hash = (hash + 1) % capacity;
            }
        } while (hash != start); //проходимся по всем элементам и, либо находим и удаляем, либо возвращаемся в старт и ничего не происходит
        return null;
    }


    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        for(Map.Entry<? extends K, ? extends V> entry : map.entrySet())
            put(entry.getKey(), entry.getValue());
    }

    @Override
    public void clear() {
        storage = new Entry[capacity];
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<K>();
        for (int i = 0; i < capacity; i++){
            if (storage[i] != null)
                set.add(storage[i].getKey());
        }
        return set;
    }

    @Override
    public Collection<V> values() {
        Collection<V> result = new ArrayList<V>();
        for (int i = 0; i < capacity; i++){
            if (storage[i] != null)
                result.add(storage[i].getValue());
        }
        return result;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> result = new HashSet<Map.Entry<K, V>>();
        for (int i = 0; i < capacity; i++)
            if (storage[i] != null)
                result.add(storage[i]);
        return result;
    }


    @Override
    public int hashCode(){
        int hash = 0;
        for (int i = 0; i < capacity; i++){
            if (storage[i] != null && storage[i] != DELETED){
                hash += (storage[i].hashCode() / size);
            }
        }
        return (hash + 31);
    }

    @Override
    public boolean equals(Object o){
        if (o.getClass() != this.getClass())
            return false;
        OpenHashMap<K,V> forEquals = (OpenHashMap<K, V>) o;
        if (size() != forEquals.size())
            return false;
        for (Entry<K, V> kvPair : storage) {
            if (kvPair != null && kvPair != DELETED) {
                if (!forEquals.get(kvPair.getKey()).equals(kvPair.getValue()))
                    return false;
            }
        }
        return true;
    }

    static class Entry<K, V> implements Map.Entry<K ,V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V v) {
            value = v;
            return value;
        }

        @Override
        public boolean equals(Object o){
            if (o == null || getClass() != o.getClass())
                return false;
            Entry<K, V> entry = (Entry) o;
            return getValue().equals(entry.getValue()) && getKey().equals(entry.getKey());
        }

        @Override
        public int hashCode(){
            int hash = key.hashCode() + value.hashCode();
            return (31 + hash / 2);
        }
    }
}

