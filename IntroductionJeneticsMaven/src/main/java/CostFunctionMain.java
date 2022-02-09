import io.jenetics.util.ISeq;

import java.util.Random;
import java.util.stream.Stream;

public class CostFunctionMain {
    //Parametry listy Itemow
    Random random=new Random(System.currentTimeMillis());
    int itemAmount=8;
    //Utworzenie listy Itemow
    final ISeq<ItemGA> items= Stream.generate(()-> ItemGA.random(random))
            .limit(itemAmount)
            .collect(ISeq.toISeq());


    public static void main(String[] args) {

    }
}
