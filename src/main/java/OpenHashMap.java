public class OpenHashMap {
    private final int startCapacity = 8;
    private int size;
    private int capacity;
    private HashMap[] map;
    private final double fillFactor = 0.75;
    private boolean[] deleted;

    public OpenHashMap() {
        capacity = startCapacity;
        map = new HashMap[capacity];
        deleted = new boolean[capacity];
        size = 0;
        for (int i = 0; i < capacity; i++) {
            map[i] = null;
            deleted[i] = false;
        }
    }

    //поиск значения по ключу
    public Integer search(int key) {
        int hash = hash1(key, capacity);
        while (map[hash] != null) {
            if (map[hash].getKey() == key) {
                return map[hash].getValue();
            }
            hash += hash2(key, capacity);
            hash %= capacity;
        }
        return null;
    }

    public void add(int key, int value) {
        if (fillFactor <= (size * 1.0 / capacity)) { //проверяем, если таблица заполнена на 75%, то увеличиваем размер
            rehash();                                //в 2 раза и проводим рехеширование
        }
        int hash = hash1(key, capacity);
        while (map[hash] != null && !deleted[hash]) {
            if (map[hash].getKey() == key) {
                map[hash].setValue(value);
                return;
            }
            hash += hash2(key, capacity);
            hash %= capacity;
        }
        map[hash] = new HashMap(key, value);
        ++size;
    }

    public void remove(int key) {
        int hash = hash1(key, capacity);
        while (map[hash] != null && !deleted[hash]) {
            if (map[hash].getKey() == key) {
                deleted[hash] = true;
                --size;
                return;
            }
            hash += hash2(key, capacity);
            hash %= capacity;
        }
    }


    //при заполнении таблицы на 75% увеличиваем размер таблицы в два раза и проводим рехэширование
    private void rehash() {
        final int newCapacity = capacity * 2;
        final HashMap[] newMap = new HashMap[newCapacity];
        for (int i = 0; i < capacity; ++i) {
            if (map[i] != null && !deleted[i]) {
                int hash = hash1(map[i].getKey(), newCapacity);
                while (map[hash] != null) {
                    hash += hash2(map[i].getKey(), newCapacity);
                    hash %= newCapacity;
                }
                newMap[hash] = new HashMap(map[i].getKey(), map[i].getValue());
            }
        }
        capacity = newCapacity;
        map = newMap;
    }

    private int hash1(int key, int currentCapacity) {
        return key % currentCapacity;
    }

    private int hash2(int key, int currentCapacity) {
        int hash = key % (currentCapacity - 1);
        if (hash % 2 == 0) ++hash;
        return hash;
    }
}
