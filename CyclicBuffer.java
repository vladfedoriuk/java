import java.io.*;
import java.sql.SQLOutput;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class CyclicBuffer {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Circular<Integer> circ = new Circular<>(5);
        circ.offer(1);
        circ.offer(3);
        circ.offer(2);
        circ.offer(46);
        circ.offer(56);
        circ.remove();
        circ.offer(-1);
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(new File("object.txt")));
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        try {
            circ.writeObject(out);
        }catch(IOException e){
            e.printStackTrace();
        }
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File("object.txt")));
        Circular<Integer> circ_copy = new Circular<>(7);
        circ_copy.readObject(in);
        circ_copy.print();
        circ_copy.remove();
        circ_copy.offer(-2);
        circ_copy.print();
        Integer[] arr = new Integer[2];
        arr = circ_copy.toArray(arr);
        for(Integer e: arr){
            System.out.print(e+" ");
        }
        System.out.println("\n");
        System.out.println(circ.toString());
        System.out.println(circ.getReadPos()+" "+circ.getWritePos());
        System.out.println(circ.toString());
        Thread t = new Thread(()-> {
            try {
                for (Integer e : circ) {
                    System.out.print(e + " ");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }catch(ConcurrentModificationException e){
                e.printStackTrace();
            }
            System.out.println();
        });
        Thread t1 = new Thread(()-> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
           // circ.remove();
        });
        t.start();
        t1.start();
        Circular<String>  circ_str = new Circular<>(3);
        circ_str.offer("a");
        circ_str.offer("b");
        circ_str.offer("c");
        circ_str.remove();
        circ_str.offer("d");
        System.out.println(circ_str);
    }
}

class CircularIterator<E extends Serializable> implements Iterator<E>{

    private int size;
    private int count = 0;
    private Circular<E> circ;
    private int it;
    private boolean flipped;

    CircularIterator(Circular<E> circ){
        this.circ = circ;
        size = circ.getSize();
        it = circ.getReadPos();
        flipped = false;
    }
    @Override
    public boolean hasNext() {
        return (count < size);
    }

    @Override
    public E next() {
        int it1 = it;
        synchronized(circ) {
            if(circ.getSize() != size) throw new ConcurrentModificationException();
            if (circ.getWritePos() <= circ.getReadPos() && !flipped) {
               //it =  it + 1 == cap ? 0: it+1;
                if (it + 1 == circ.getCapacity()) {
                    it = 0;
                    flipped = true;
                } else {
                    it++;
                }
            } else {
                if (it < circ.getWritePos()) {
                    it++;
                }
            }
            count++;
        }
        return circ.getBuf()[it1];
    }
}

class Circular<E extends Serializable> implements Serializable, BlockingQueue<E> {

    private E[] buf;
    private int capacity;
    private int writePos = 0;
    private int readPos  = 0;
    private boolean flipped = false;
    private int size=0;

    boolean getFlipped(){
        return flipped;
    }

    E[] getBuf(){
        return buf;
    }
    int getWritePos(){
        return writePos;
    }
    int getReadPos(){
        return readPos;
    }
    int getCapacity(){
        return capacity;
    }
    int getSize(){
        return size;
    }

    Circular(Integer capacity){
        buf = (E[]) new Serializable[capacity];
        this.capacity = capacity;
    }
    public String toString(){
        String s = "Circular Buffer:\n";
        for(int i = 0; i< capacity;i++) {
            s+=String.format("|index:%-5d|distance:%-5d|type:%-15s|%-5s|\n", i, Math.abs(i-readPos), buf[i].getClass(), buf[i].toString());
        }
        return s;
    }

    void print(){
        IntStream.range(0,size).forEach((i)->System.out.print(buf[i]+" "));
        System.out.println();
    }

    void readObject(ObjectInputStream in)throws IOException, ClassNotFoundException {
        capacity = in.readInt();
        flipped = false;
        size = in.readInt();
        writePos = size;
        for(int i = 0; i<size; i++)
            buf[i] = (E) in.readObject();

    }
    void writeObject(ObjectOutputStream out)throws IOException{
        out.writeInt(capacity);
        out.writeInt(size);
        for(int i = 0; i < capacity; i++){
            if(buf[i]!=null){
                out.writeObject(buf[i]);
            }
        }
    }
    @Override
    public boolean add(E e) {
        boolean attempt = offer(e);
        if(!attempt){
            throw new IllegalStateException();
        }
        return true;
    }

    @Override
    public boolean offer(E e) {
        if (!flipped) {
            if (writePos == capacity) {
                writePos = 0;
                flipped = true;
                if (writePos < readPos) {
                    buf[writePos++] = e;
                    size++;
                    return true;
                } else {
                    return false;
                }
            } else {
                buf[writePos++] = e;
                size++;
                return true;
            }
        } else {
            if (writePos < readPos) {
                buf[writePos++] = e;
                size++;
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public E remove() {
        if (size == 0){
            throw new NoSuchElementException();
        }
        return poll();
    }

    @Override
    public E poll() {
        if(!flipped){
            if(readPos < writePos){
                size--;
                E e = buf[readPos+1];
                buf[readPos++] = null;
                return e;
            } else {
                return null;
            }
        } else {
            if(readPos == capacity){
                readPos = 0;
                flipped = false;
                if(readPos < writePos){
                    size--;
                    E e = buf[readPos+1];
                    buf[readPos++] = null;
                    return e;
                } else {
                    return null;
                }
            } else {
                size--;
                E e = buf[readPos+1];
                buf[readPos++] = null;
                return e;
            }
        }
    }

    @Override
    public E element() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public synchronized void put(E e) throws InterruptedException {
        synchronized(this){
            if(size == capacity){
                this.wait();
            } else{
                offer(e);
            }
            this.notifyAll();
        }
    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public E take() throws InterruptedException {
        E e = null;
        synchronized(this){
            if(size==0){
                this.wait();
            } else
                e = poll();
                this.notifyAll();
        }
        return e;
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public int remainingCapacity() {
        return 0;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new CircularIterator<E>(this);
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        for(int i = 0; i< capacity; i++){
            if(buf[i]!=null) {
                arr[i] = buf[i];
            }
        }
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T[] arr = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        for(int i = 0; i< capacity; i++){
            if(buf[i]!=null){
                arr[i] = (T)buf[i];
            }
        }
        return arr;
    }

    @Override
    public int drainTo(Collection<? super E> c) {
        return 0;
    }

    @Override
    public int drainTo(Collection<? super E> c, int maxElements) {
        return 0;
    }
}
