import io.jenetics.internal.util.Requires;

import java.util.Random;
import java.util.stream.Collector;

import static java.lang.String.format;

public class ItemGA {
    static int count=-1;
    private final double size;
    private final double value;
    private final int label;


    private ItemGA(final double size, final double value) {
        count=count+1;
        this.label=count;
        this.size = Requires.nonNegative(size);
        this.value = Requires.nonNegative(value);

    }

    public double getSize() {
        return size;
    }

    public double getValue() {
        return value;
    }


    @Override
    public String toString() {
        return format("Item[ID=%d,size=%f, value=%f]",label,size,value);
    }

    public static ItemGA of(final double size, final double value){
        return new ItemGA(size,value);
    }

    public static ItemGA random(final Random random){
        return new ItemGA(random.nextDouble() *100,random.nextDouble() *100);
    }

    public static Collector<ItemGA,?, ItemGA> toSum(){
        return Collector.of(
                ()->new double[2],
                (a,b)->{a[0]+=b.size;a[1]+=b.value;},
                (a,b)->{a[0]+=b[0];a[1]+=b[1];return a;},
                r->new ItemGA(r[0],r[1])
        );
    }
}
