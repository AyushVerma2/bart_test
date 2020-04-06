import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Test01 {

    public String getDestination() {
        return destination;
    }

    private String destination;
    private String minute;

    public Test01(String destination, String time) {
        this.destination = destination;
        this.minute = time;
    }

    public String getMinute() {
        return minute;
    }

    @Override
    public String toString() {

        return minute;// + (minute.equals("Leaving") ? "  now  " : "  min  ") + destination;
    }
    public static void main(String a[]){

        Test01 t1 = new Test01("a","12");
        Test01 t2 = new Test01("b","Leaving");
        Test01 t3 = new Test01("g","134");
        Test01 t4 = new Test01("c","1");
        Test01 t5 = new Test01("y","100");
        List<Test01> all = new ArrayList<>();
        all.add(t1);
        all.add(t2);
        all.add(t3);
        all.add(t4);
        all.add(t5);
        all.stream().forEach(System.out::println);
        System.out.println("*********************");
        all.stream().sorted(Comparator.comparing(Test01::getMinute));

        List<Test01> leavingList = all.stream()
                .filter(o -> o.getMinute().equals("Leaving")).collect(Collectors.toList());

        List<Test01> numberList = all.stream()
                .filter(o -> !(o.getMinute().equals("Leaving"))).collect(Collectors.toList());

        List<Test01> sortedList= numberList.stream()
                .sorted(Comparator.comparing(test -> Integer.parseInt(test.getMinute())))
                .collect(Collectors.toList());
        List<Test01> all11= new ArrayList<>();
        all11.addAll(leavingList);
        all11.addAll(sortedList);


        for(Test01 t:all11){
            System.out.println(t.minute);
        }
    }


//    @Override
//    public int compareTo(Object o) {
//        o=(Test01)o;
//
//    }
}