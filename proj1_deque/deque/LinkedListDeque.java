package deque;
//import deque.Deque<T>;


public class LinkedListDeque<T> implements Deque<T>{

    private DequeNode sentinel;
    private int size;

    private class DequeNode<T>{
        T item;
        DequeNode next;
        DequeNode prev;

        public DequeNode(T item, DequeNode next, DequeNode prev){
            this.item = item;
            this.next = next;
            this.prev = prev;
        }

        @Override
        public boolean equals(Object o) {
            Deque dq = (Deque) o;
            if (size() != dq.size()) return false;
            for (int i = 0; i < size; i++){
                if (!(get(i)).equals(dq.get(i))) return false;
            }
            return true;
        }


        public T getItem(){
            return item;
        }
    }

//    public LinkedListDeque(T item){
//        sentinel = new DequeNode(99, null, null);
//        sentinel.next = new DequeNode(item, sentinel, sentinel);
//        this.size = 1;
//    }

    public LinkedListDeque(){
        sentinel = new DequeNode(99, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }


    public boolean equals(Object o){
        if (!(o instanceof Deque)) return false;
        Deque dq = (Deque) o;
//        Deque lld = (Deque) this;
        if ((this == null) ^ (dq == null)) return false;
        if (this.size() != dq.size()) return false;
        int index = 0;
        while(index < size){
            if (!get(index).equals(dq.get(index))) return false;
            index += 1;
        }
        return true;
    }


    public void addFirst(T item){
        DequeNode ptr = sentinel.next;
        sentinel.next = new DequeNode(item, ptr, sentinel);
        ptr.prev = sentinel.next;
        size += 1;
    }

    public void addLast(T item){
        DequeNode ptr = sentinel.prev;
        sentinel.prev = new DequeNode(item, sentinel, ptr);
        ptr.next = sentinel.prev;
        size += 1;
    }


    public int size(){
        return size;
    }

    public T removeFirst(){
        if(isEmpty()) return null;
        T res = (T) sentinel.next.getItem();
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size -= 1;
        return res;
    }

    public T removeLast(){
        if(isEmpty()) return null;
        T res = (T) sentinel.prev.getItem();
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size -= 1;
        return res;
    }

    public T get(int index){
        if ((index < 0) || (index > size)){
            return null;
        }
        else{
            int i = 0;
            DequeNode ptr = sentinel.next;
            while(i < index){
                ptr = ptr.next;
                i += 1;
            }
            return (T) ptr.getItem();
        }
    }

    public void printDeque(){
        if (isEmpty()) return;
        DequeNode ptr = sentinel.next;
        while (ptr != sentinel){
            System.out.print(ptr.item + " ");
            ptr = ptr.next;
        }
        System.out.println();
    }

    public T getRecursive(int index){
        return (T) helper(index, sentinel.next).getItem();
    }

    private DequeNode helper(int i, DequeNode ptr){
        if (i== 0){
            return ptr;
        }
        else{
            i -= 1;
            ptr = ptr.next;
            return helper(i, ptr);
        }
    }

}

