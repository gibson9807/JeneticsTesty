import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LambdaListSort {

    public static void main(String[] args) {
        List<String> list=new ArrayList<>();
        Scanner sc=new Scanner(System.in);
        for(int i=0;i<4;i++) {
            System.out.println("Podaj ciag znaków: ");
            String s = sc.next();
            list.add(s);
        }
        list.sort((s1,s2)->s2.length()-s1.length());
        System.out.println(list);

    }
}
