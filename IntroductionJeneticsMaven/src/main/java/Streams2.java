import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Streams2 {

    static void setUp(int size, int bound,List<Integer> list){

        Random r=new Random(System.currentTimeMillis());
        for(int i=0;i<size;i++){
                list.add(r.nextInt(bound));
        }
    }

    public static void main(String[] args) {
        List<Integer> list=new ArrayList<>();
        setUp(20,101,list);

        String stringList=list.stream().map(String::valueOf).collect(Collectors.joining(","));
        System.out.println(stringList);

        TreeMap<Integer,Long> treeMap=new TreeMap<> (list.stream().collect(Collectors.groupingBy(Function.identity(),Collectors.counting())));
        LinkedHashMap<Integer,Long> sortedMap=treeMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1,e2)->e1,LinkedHashMap::new));
        System.out.println(treeMap);
        System.out.println(sortedMap);
        int minimal=list.stream().min(Comparator.comparing(Integer::valueOf)).get();
        System.out.println(minimal);

    }
}
