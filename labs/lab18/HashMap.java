import java.util.Iterator;
import java.util.LinkedList;

public class HashMap<K, V> implements Map61BL<K, V>{

    /* TODO: Instance variables here */
    LinkedList<Entry<K, V>>[] arr;
    int capacity;
    double loadFactor;
    int size = 0;

    /* TODO: Constructors here */
    HashMap(int initialCapacity){
        this(initialCapacity, 0.75);
    }

    HashMap(){
        this(16, 0.75);

    }

    HashMap(int initialCapacity, double loadFactor){
        this.capacity = initialCapacity;
        this.loadFactor = loadFactor;
        this.arr = new LinkedList[capacity];
        for (int i = 0; i < arr.length; i++){
            arr[i] = new LinkedList<Entry<K, V>>();
        }
    };

    /*  Interface methods here */
    public int capacity(){
        return capacity;
    }

    @Override
    public void clear(){
        for (LinkedList<Entry<K, V>> each: arr){
            each.clear();
        }
        size = 0;
    };

    /* Returns true if this map contains a mapping for the specified key KEY. */
    public boolean containsKey(K key){
        int index = Math.floorMod(key.hashCode(), capacity);
        for (Entry<K, V> entry: arr[index]){
            if (entry.key == key)
                return true;
        }
        return false;
    };

    /* Returns the value to which the specified key KEY is mapped, or null if
       this map contains no mapping for KEY. */

    public V get(K key){
        int keyHash = key.hashCode();
        int index = Math.floorMod(keyHash, capacity);
        LinkedList<Entry<K, V>> llist = arr[index];
//        Entry<K, V> compared = new Entry<>(key, null);
        if (llist == null){
            return null;
        }
        for (Entry<K, V> entry : llist){
            if (entry.key.equals(key)){
                return entry.value;
            }
        }
        return null;
    };

    /* Puts the specified key-value pair (KEY, VALUE) in this map. */
    public void put(K key, V value){
        //overwrite old value
        if (containsKey(key)){
            int keyHash = key.hashCode();
            int index = Math.floorMod(keyHash, capacity);
//        LinkedList<Entry<K, V>> llist = arr[index];  Cannot get a new pointer;
            for (Entry<K, V> entry: arr[index]){
                if (entry.key == key){
                    entry.value = value;
                }
            }
            return;
        }
        //key does not exist
        if ((size + 1.0) / capacity > loadFactor){
            resize();
        }
        int keyHash = key.hashCode();
        int index = Math.floorMod(keyHash, capacity);
//        LinkedList<Entry<K, V>> llist = arr[index];  Cannot get a new pointer;
        if (arr[index] == null){
//            llist = new LinkedList<Entry<K, V>>(new Entry<K, V>(key, value);
            arr[index] = new LinkedList<Entry<K, V>>();
            arr[index].add(new Entry<>(key, value));
            return;
        }
        arr[index].addLast(new Entry(key, value));
        this.size += 1;
    };


    public V remove(K key) {
        int keyHash = key.hashCode();
        int index = Math.floorMod(keyHash, capacity);
        LinkedList<Entry<K, V>> llist = arr[index];
        Entry<K, V>toRemove = null;
        for (Entry<K, V> entry: llist){
            if (entry.key == key){
                toRemove = entry;
                llist.remove(toRemove);
                size -= 1;
                return toRemove.value;
            }
        }
        return null;
    }

    /* Removes a particular key-value pair (KEY, VALUE) and returns true if
       successful. */
    public boolean remove(K key, V value){
        int keyHash = key.hashCode();
        int index = keyHash % capacity;
        LinkedList<Entry<K, V>> llist = arr[index];
        Entry<K, V>toRemove = null;
        for (Entry<K, V> entry: llist){
            if ((entry.key.equals(key)) && (entry.value.equals(value))){
                toRemove = entry;
                llist.remove(toRemove);
                size -= 1;
                return true;
            }
        }
        return false;
    };

    /* Returns the number of key-value pairs in this map. */
    public int size(){
        return size;
    };

    public void resize(){
//        int prevCapacity = capacity;
        capacity *= 2;
        //do not add generic type when new creating
        LinkedList<Entry<K, V>>[] newArr = new LinkedList[capacity];
        //initialize newArr
        for (int i = 0; i < newArr.length; i++){
            newArr[i] = new LinkedList<Entry<K, V>>();
        }
        //fillling the new arr
        for (LinkedList<Entry<K, V>> llist: arr){
            for (Entry<K, V> entry: llist){
                int index = Math.floorMod(entry.key.hashCode(), capacity);
                newArr[index].add(entry);
            }
        }
        arr = newArr;
    }


    /* Returns an Iterator over the keys in this map. */
    public Iterator<K> iterator(){
        return null;
    };

    private static class Entry<K, V> {

        private K key;
        private V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /* Returns true if this key matches with the OTHER's key. */
        public boolean keyEquals(Entry other) {
            return key.equals(other.key);
        }

        /* Returns true if both the KEY and the VALUE match. */
        @Override
        public boolean equals(Object other) {
            return (other instanceof Entry
                    && key.equals(((Entry) other).key)
                    && value.equals(((Entry) other).value));
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }



    /* Removes and returns a key KEY and its associated value. */

}
