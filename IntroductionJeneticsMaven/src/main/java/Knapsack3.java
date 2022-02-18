import io.jenetics.*;
import io.jenetics.engine.*;
import io.jenetics.internal.util.Requires;
import io.jenetics.util.ISeq;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;
import io.jenetics.BitGene;

import static io.jenetics.engine.EvolutionResult.*;
import static io.jenetics.engine.Limits.bySteadyFitness;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;


public class Knapsack3 implements Problem<ISeq<Knapsack3.Item>, BitGene,Double> {


    public static final class Item implements Serializable{
        // private static final long serialVersionUID=1L;
        static int count=-1;
         final double size;
         final double value;
        private int label;


        private Item(final double size, final double value) {
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

        public static Item of(final double size, final double value){
            return new Item(size,value);
        }


        //TU BYLA ZMIANA FUNKCJI random.nextDouble() *100------------------------------------------------------------------------------------------------
        public static Item random(final Random random){
            return new Item(random.nextDouble() *100,random.nextDouble() *100);
        }

        public static Collector<Item,?,Item> toSum(){
            return Collector.of(
                    ()->new double[2],
                    (a,b)->{a[0]+=b.size;a[1]+=b.value;},
                    (a,b)->{a[0]+=b[0];a[1]+=b[1];return a;},
                    r->new Item(r[0],r[1])
            );
        }
    }

    private final Codec<ISeq<Item>,BitGene> _codec;
    private final double _knapsackSize;
    public ISeq<Item> items;

    public Knapsack3(final ISeq<Item> items,final double knapsackSize){
        _codec= Codecs.ofSubSet(items);
        _knapsackSize=knapsackSize;
        this.items=items;
    }


    @Override
    public Function<ISeq<Item>, Double> fitness() {
       // return new MyFunction(_knapsackSize);
        return items->{
            final Item sum=items.stream().collect(Item.toSum());
            return sum.size<=_knapsackSize? sum.value : 0;
        };
    }


    static class MyFunction implements Function<boolean[], Double> {
        //double _knapsackSize;
        boolean[] chromosome;
        MyFunction(boolean[] chromosome){
            this.chromosome=chromosome;
        }

        @Override
        public Double apply(boolean[] booleans) {



//            final Item sum=items.stream().collect(Item.toSum());
//            return sum.size<=? sum.value : 0;
            return 0.0;
        }
    }

    @Override
    public Codec<ISeq<Item>, BitGene> codec() {
        return _codec;
    }

    public static Knapsack3 of(final int itemCount,final Random random){
        requireNonNull(random);
        return new Knapsack3(
                Stream.generate(()->Item.random(random))
                        .limit(itemCount)
                        .collect(ISeq.toISeq()),
                itemCount*100.0/3.0
        );
    }

    public static void main(String[] args) {

        //ILOSC ITEMOW
        int itemCount=8;

        final Knapsack3 knapsack=Knapsack3.of(itemCount,new Random(System.currentTimeMillis()));


        final Engine<BitGene,Double> engine= Engine.builder(knapsack)
                .populationSize(500)
                .survivorsSelector(new TournamentSelector<>(5))
                .offspringSelector(new RouletteWheelSelector<>())
                .alterers(
                        new Mutator<>(0.115),
                        new SinglePointCrossover<>(0.16)
                ).build();



        final EvolutionStatistics<Double,?> statistics=EvolutionStatistics.ofNumber();

        final  Phenotype<BitGene,Double> best=engine.stream()
                .limit(bySteadyFitness(15))
                .limit(100)
                .peek(statistics)
                .collect(toBestPhenotype());

        System.out.println(statistics);
        System.out.println(best);

        //PRZEKSZTALCENIE best na String, tak, by wykorzystac to do obliczenia sumy size Itemow
        String bestString=best.genotype().toString();
        bestString=bestString.replaceAll("[\\D.]" ,"");
        // bestString=bestString.substring(0,itemCount);
        bestString=new StringBuilder(bestString).reverse().toString();
        //System.out.println("BESTSTRING: "+bestString);

        double sumSize=0.0;
        double sumValue=0.0;
        int i=0;
        //WYSWIETLIC ITEMS
        for(Item item:knapsack.items){
            char c=bestString.charAt(i);
            if(c=='1') {
                sumSize += item.getSize();
                sumValue+=item.getValue();
            }
            System.out.println(item+" "+c+"::VALUE TO SIZE: "+String.format("%.5f",(item.getValue()/item.getSize())));
            i++;
        }
        System.out.println("SUM OF SIZE: "+sumSize);
        System.out.println("SUM OF VALUE: "+sumValue);
        System.out.println("BEST: "+best.genotype());
//List<Boolean> lista=best.genotype().stream().collect(Collectors.toList());
        System.out.println("CHROMOSOME: "+best.genotype().chromosome());

//TABLICA Object
        Object[] tablica=best.genotype().chromosome().stream().toArray();

        boolean[] chromosom=new boolean[tablica.length];
        int l=0;

        for(Object o:tablica){
            BitGene b=(BitGene)o;
            chromosom[l]=b.booleanValue();
            l++;
        }
        // System.out.println(Arrays.toString(chromosom));
        //   Collections.reverse(Arrays.asList(chromosom));
        System.out.println("++++++++++++++++");
        for(boolean b:chromosom){
            System.out.println(b);
        }


    }



}
