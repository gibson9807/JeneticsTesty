import io.jenetics.*;
import io.jenetics.engine.*;
import io.jenetics.internal.collection.ArrayISeq;
import io.jenetics.util.ISeq;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import io.jenetics.BitGene;

import static io.jenetics.engine.EvolutionResult.*;
import static io.jenetics.engine.Limits.bySteadyFitness;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;


public class ISProblem implements Problem<ISeq<ItemGA>, BitGene,Double> {

    private final Codec<ISeq<ItemGA>,BitGene> _codec;
    public ISeq<ItemGA> items;
    public int dataSize;
    Function<boolean[], Double> costFunction;


    public ISProblem(final ISeq<ItemGA> items, Function<boolean[], Double> costFunction){
        _codec= Codecs.ofSubSet(items);
        this.items=items;
        dataSize= items.size();
        this.costFunction = costFunction;
    }

    @Override
    public Function<ISeq<ItemGA>, Double> fitness() {
        return items->{
            boolean[] chromosome=new boolean[dataSize];
            for(ItemGA i: items){
                chromosome[i.getId()]=true;
            }
            return costFunction.apply(chromosome);
        };
    }


    @Override
    public Codec<ISeq<ItemGA>, BitGene> codec() {
        return _codec;
    }

    static double myCostaFunction( boolean[] chromosome, double[] data){
        double sum = 0;
        int count = 0;
        for (int i =0; i<chromosome.length; i++){
            if (chromosome[i]){
                sum += data[i];
                count ++;
            }
        }
        return  count<=3 ? sum : 0;
    }

    public static void main(String[] args) {
        //               0  1  2  3  4  5
        double[] data = {1, 6, 7, 3, 2, 7};
        ItemGA[] items = new ItemGA[data.length];
        int i = 0;
        for (double d : data){
            items[i] = new ItemGA(i);
            i++; //Brak tej linijki powodował, że items było pełne nulli
        }
        ISeq<ItemGA> zbiorDlaGA = ISeq.of(items);
                //IntStream.range(0,data.length).mapToObj( x-> new ItemGA(x)).collect(ISeq.toISeq());



       //final ISProblem knapsack= new ISProblem(zbiorDlaGA, (x)->myCostaFunction(x,data));
        final ISProblem knapsack= new ISProblem(zbiorDlaGA, new MyFunction(data) );


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

    }



}

class MyFunction implements Function<boolean[],Double> {
    double[] data;

    public MyFunction(double[] data) {
        this.data = data;
    }

    @Override
    public Double apply(boolean[] chromosome) {
        return ISProblem.myCostaFunction(  chromosome,  data);
    }
}

