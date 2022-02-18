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


public class Knapsack2 implements Problem<ISeq<Knapsack3.Item>, BitGene,Double> {

    private final Codec<ISeq<Knapsack3.Item>,BitGene> _codec;
    private final double _knapsackSize;
    public ISeq<Knapsack3.Item> items;

    public Knapsack2(final ISeq<Knapsack3.Item> items,final double knapsackSize){
        _codec= Codecs.ofSubSet(items);
        _knapsackSize=knapsackSize;
        this.items=items;
    }


    @Override
    public Function<ISeq<Knapsack3.Item>, Double> fitness() {
//        return new MyFunction(_knapsackSize);
        return items->{
            final Knapsack3.Item sum=items.stream().collect(Knapsack3.Item.toSum());
            return sum.size<=_knapsackSize? sum.value : 0;
        };
    }

//    static class MyFunction implements Function<ISeq<Item>, Double> {
//        double _knapsackSize;
//        MyFunction(double ns){
//            _knapsackSize = ns;
//        }
//
//        @Override
//        public Double apply(ISeq<Item> items) {
//            final Item sum=items.stream().collect(Item.toSum());
//            return sum.size<=? sum.value : 0;
//        }
//    }

    @Override
    public Codec<ISeq<Knapsack3.Item>, BitGene> codec() {
        return _codec;
    }

    public static Knapsack2 of(final int itemCount,final Random random){
        requireNonNull(random);
        return new Knapsack2(
                Stream.generate(()->Knapsack3.Item.random(random))
                .limit(itemCount)
                .collect(ISeq.toISeq()),
                        itemCount*100.0/3.0
        );
    }

    public static void main(String[] args) {

        //ILOSC ITEMOW
        int itemCount=8;

        final Knapsack2 knapsack=Knapsack2.of(itemCount,new Random(System.currentTimeMillis()));


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
       // bestString=new StringBuilder(bestString).reverse().toString();
       //System.out.println("BESTSTRING: "+bestString);

        double sumSize=0.0;
        double sumValue=0.0;
        int i=0;
        //WYSWIETLIC ITEMS
        for(Knapsack3.Item item:knapsack.items){
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

        System.out.println("++++++++++++++++");
        for(boolean b:chromosom){
            System.out.println(b);
        }

//Użycie costFunction
        double sumofValue;
       sumofValue= CostFunctionMain.costFunction(knapsack.items,chromosom);
        System.out.println("COST FUNCTION: "+sumofValue);



    }



}
