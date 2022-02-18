import io.jenetics.util.ISeq;

import java.util.Random;
import java.util.stream.Stream;

public class CostFunctionMain {

    //Funkcja kosztu, ktora jako argument dostaje listę itemów i binarny chromosom
    public static double costFunction(ISeq<Knapsack3.Item> itemsGA, boolean[] chromosome){
        int iterator=0;
        double sumOfValue=0.0;
        itemsGA.forEach(System.out::println);
        for(boolean b:chromosome){
            System.out.println(b+": "+(chromosome.length-iterator-1));
            Knapsack3.Item item= itemsGA.get(chromosome.length-iterator-1);
            if(b){
                sumOfValue+= item.getValue();
            }
            iterator++;
        }

        return sumOfValue;
    }



    public static void main(String[] args) {
        //Parametry
        Random random=new Random(System.currentTimeMillis());
        int itemAmount=8;
        //Utworzenie listy Itemow
        final ISeq<Knapsack3.Item> items= Stream.generate(()-> Knapsack3.Item.random(random))
                .limit(itemAmount)
                .collect(ISeq.toISeq());
        //Binarny chromosom(przykladowy)
        boolean[] chromosome=new boolean[itemAmount];
        for(int i=0;i<itemAmount;i++){
            chromosome[i]= random.nextBoolean();
        }
        //Wywołanie funkcji

        double test=costFunction(items,chromosome);
        System.out.println("RESULT: "+test);

    }
}
