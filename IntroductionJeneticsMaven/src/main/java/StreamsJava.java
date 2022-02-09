

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StreamsJava {


    public static void main(String[] args) {
        List<User> users=new ArrayList<>(List.of(// new ArrayList<>();
        new User("Adam","Kowal",22,new ArrayList<>(List.of("pilka","IT","rower"))),
        new User("Anna","Kowalska",12,new ArrayList<>(Arrays.asList("klawiatura","C#","Java","dysk"))),
        new User("Maciej","Kowalik",25,new ArrayList<>(Arrays.asList("prog","pilka","IT","CD"))),
        new User("Jacek","Maciejczyk",52,new ArrayList<>(Arrays.asList("programowanie","prawo jazdy","góry","rower"))),
        new User("Marta","Nowak",44,new ArrayList<>(Arrays.asList(".NET"))),
        new User("Emil","Rybak",10,new ArrayList<>(Arrays.asList("Xiaomi","koszykówka","myszka"))),
        new User("Tomasz","Torbacz",33,new ArrayList<>(Arrays.asList("jedzenie","siłownia","IT","lekarstwa"))),
        new User("Julia","Dudek",76,new ArrayList<>(Arrays.asList("dieta","pilka","siatkówka","kolarstwo"))),
        new User("Kasia","Ekran",55,new ArrayList<>(Arrays.asList("prog","rower","IT","jabłka"))),
        new User("Marta","Kowal",64,new ArrayList<>(Arrays.asList("coś","nic","cokolwiek","jeszcze coś")))
        ));
     //   users.add(user1);users.add(user2);users.add(user3);users.add(user4);users.add(user5);users.add(user6);users.add(user7);users.add(user8);users.add(user9);users.add(user10);



    //    users.forEach(System.out::println);

        //WYSWIETLENIE
//        users.stream().map(User::getSkills)
//                .forEach(System.out::println);

        //LISTA UMIEJETNOSCI
        List<String> listofSkills= users.stream().map(User::getSkills)
                .flatMap(Collection::stream)
                .map(String::toLowerCase)
               // .distinct()
               // .sorted()
                .collect(Collectors.toList());


        //LISTA UMIEJETNOSCI Z PODLICZENIEM  ------------------------JAK POSORTOWAC ALFABETYCZNIE RAZEM Z PODLICZONA ILOSCIA BEZ PRZEPISYWANIA DO TREEMAP----------------------

            //podliczenie dla kazdego z osobna
//        Map<String,Long> result=listofSkills.stream()
//                .collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));

            //alafabetycznie
        TreeMap<String,Long> treeMap=new TreeMap<String,Long>(listofSkills.stream()
                .collect(Collectors.groupingBy(Function.identity(),Collectors.counting())));

        //wg ilosci wystepowania
        LinkedHashMap<String, Long> sortedMap= treeMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1,e2)->e1,LinkedHashMap::new));

 //       System.out.println("LISTA UMIEJETNOSCI: "+listofSkills+" LICZBA: "+ listofSkills.stream().count());
       System.out.println("ALFABETYCZNIE: "+treeMap);
       System.out.println("WG LICZBY WYSTEPOWAN: "+sortedMap);

//        System.out.println("NAJMŁODSI i NAJSTARSI");
//        users.stream().sorted(Comparator.comparing(User::getAge)).limit(3)
//                .forEach(System.out::println);
//        System.out.println("----------------------");
//        users.stream().sorted(Comparator.comparing(User::getAge)).skip(7).forEach(System.out::println);

        ///------------------------------------JAK WYPISAC LICZBE SKILLS ZAMIAST LISTY ALE BEZ MODYFIKOWANIA ISTNIEJACEJ TYLKO TWORZAC NOWA-------------------------------------------------------------------
        System.out.println("---++++++++++++++++-----");
        users.stream().forEach(System.out::println);

//        users.stream().map(User::getSkills)
//                .filter(Objects::nonNull)
//                .map(List::size)
//                .toList();
   //     System.out.println(numOfSkills);

//        List<User> numOfSkillsList=users.stream().
//                map(u-> {
//                             new User(u.getName(), u.getSurname(), u.getAge(),
//                                    users.stream().map(User::getSkills)
//                                            .filter(Objects::nonNull)
//                                            .map(List::size))
//                                           // .mapToInt(List::size))
//
//                                            ;
//                        }
//        List<String> ListNumOfSkills =users.stream()
//                .map(u->u.getName()+" "+ u.getSurname()+" "+ u.getAge()+": "+u.getSkills().size()).
//                        collect(Collectors.toList());

       List<User> ListNumOfSkills =users.stream()
       .map(u->new User(u.getName(), u.getSurname(), u.getAge(),new ArrayList<>(List.of(""+u.getSkills().size())))).
                       collect(Collectors.toList());


        System.out.println("LICZBA SKILLS ZAMIAST LISTY: ");
        ListNumOfSkills.forEach(System.out::println);
        System.out.println("KONIEC LISTY SKILLS");

//                        //ZE ZMIANA LISTY USEROW:
//        users.forEach(u->u.setSkillsNum(u.getSkills().size()));
       // System.out.println("---++++++++++++++++-----");
     //   users.forEach(System.out::println);

       // (u1,u2)->u2.getName().length()-u1.getName().length()
        users.stream().
                filter(u->u.getSkills().size()>3)
                .sorted(Comparator.comparing(User::getName).reversed())
                .forEach(System.out::println);


    }


}
