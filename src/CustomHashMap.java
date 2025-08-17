public class CustomHashMap {
    private static class Entry {
        char key;
        int value;
        Entry next;

        Entry(char key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private Entry[] table;
    private final int capacity;
    private int size;

    public CustomHashMap(int capacity) {
        this.capacity = capacity; // fixed max unique keys
        table = new Entry[capacity];
        size = 0;
    }

    private int hash(char key) {
        return Math.abs(Character.hashCode(key)) % capacity;
    }

    public void put(char key, int value) {
        int index = hash(key);
        Entry current = table[index];
        Entry prev = null;

        // Search for existing key to update
        while (current != null) {
            if (current.key == key) {
                current.value = value; // update
                return;
            }
            prev = current;
            current = current.next;
        }

        // Adding new key
        if (size >= capacity) {
            throw new RuntimeException("CustomHashMap is full (max size " + capacity + ")");
        }

        Entry newEntry = new Entry(key, value);
        if (prev == null) {
            table[index] = newEntry;
        } else {
            prev.next = newEntry;
        }
        size++;
    }

    public int get(char key) {
        int index = hash(key);
        Entry current = table[index];
        while (current != null) {
            if (current.key == key) {
                return current.value;
            }
            current = current.next;
        }
        return 0; // default if not found
    }

    public int size() {
        return size;
    }
}
