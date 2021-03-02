import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class CheckTime {

    private static byte[] serialize(Collection coll) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(coll);
        oos.flush();
        return bos.toByteArray();
    }

    public static void main(String[] args) {

        ArrayList<Integer> arrayList = new ArrayList<>();
        HashSet<Integer> hashSet = new HashSet<>();
        LinkedList<Integer> linkedList = new LinkedList<>();
        Stack<Integer> stack = new Stack<>();
        Vector<Integer> vector = new Vector<>();
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
        TreeSet<Integer> treeSet = new TreeSet<>();

        Map<Class, Map<String, Integer>> data = new HashMap<>();
        int operations = 100000;
        Consumer<Collection<Integer>> measure = (coll)->{
            data.put(coll.getClass(), new HashMap<String, Integer>());
            long time_start = System.currentTimeMillis();
            IntStream.range(0, operations).forEach(coll::add);
            long time_end = System.currentTimeMillis();
            Integer delta = (int)(time_end - time_start);
            data.get(coll.getClass()).put("add", delta);
            byte[] arr = null;
            try {
                arr = serialize(coll);
            }catch(IOException e){
                e.printStackTrace();
            }
            data.get(coll.getClass()).put("bytes", arr.length);
            time_start = System.currentTimeMillis();
            IntStream.range(0, operations).forEach(coll::contains);
            time_end = System.currentTimeMillis();
            delta = (int)(time_end - time_start);
            data.get(coll.getClass()).put("contains", delta);
            time_start = System.currentTimeMillis();
            IntStream.range(0, operations).forEach(coll::remove);
            time_end = System.currentTimeMillis();
            delta = (int)(time_end - time_start);
            data.get(coll.getClass()).put("remove", delta);
        };

        Consumer<Collection<Integer>> display = (coll)->{
            String output = String.format("|%-30s|%-15s|%-15s|%-15s|%-15s|",coll.getClass(),
                    data.get(coll.getClass()).get("add"),
                    data.get(coll.getClass()).get("contains"),
                    data.get(coll.getClass()).get("remove"),
                    data.get(coll.getClass()).get("bytes"));
            System.out.println(output);
        };

        String output = String.format("|%-30s|%-15s|%-15s|%-15s|%-15s|","class","add","contains","remove","bytes");
        System.out.println(output);
        measure = measure.andThen(display);
        measure.accept(arrayList);
        measure.accept(hashSet);
        measure.accept(linkedList);
        measure.accept(stack);
        measure.accept(vector);
        measure.accept(priorityQueue);
        measure.accept(treeSet);

    }
}
