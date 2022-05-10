import io.jenetics.*;
import io.jenetics.engine.*;
import io.jenetics.util.ISeq;
import java.util.function.Function;
import io.jenetics.BitGene;
import static io.jenetics.engine.EvolutionResult.*;
import static io.jenetics.engine.Limits.bySteadyFitness;


public class ISProblem implements Problem<ISeq<Integer>, BitGene,Double> {

    private final Codec<ISeq<Integer>,BitGene> _codec;
    public ISeq<Integer> items;
    public int dataSize;
    Function<boolean[], Double> costFunction;


    public ISProblem(final ISeq<Integer> items, Function<boolean[], Double> costFunction){
        _codec= Codecs.ofSubSet(items);
        this.items=items;
        dataSize= items.size();
        this.costFunction = costFunction;
    }

    @Override
    public Function<ISeq<Integer>, Double> fitness() {
        return items->{
            boolean[] chromosome=new boolean[dataSize];
            for(Integer i: items){
                chromosome[i]=true;
            }
            return costFunction.apply(chromosome);
        };
    }


    @Override
    public Codec<ISeq<Integer>, BitGene> codec() {
        return _codec;
    }

    static int counter=1;
    static double MyCostFunction(boolean[] chromosome, double[] data){
        System.out.println("COST FUNCTION: "+counter++);
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
        double[] data = {-15,-7, -1, -1,7,100,200,300};
        Integer[] items = new Integer[data.length];
        int i = 0;
        for (double d : data){
            items[i] = i;
            i++;
        }

        ISeq<Integer> zbiorDlaGA = ISeq.of(items);

        final ISProblem knapsack= new ISProblem(zbiorDlaGA,
                chromosome -> ISProblem.MyCostFunction(  chromosome,  data)
        );

        final Engine<BitGene,Double> engine= Engine.builder(knapsack)
                .executor( Runnable::run)
                .populationSize(5)
                .survivorsSelector(new TournamentSelector<>(3))
                .offspringSelector(new RouletteWheelSelector<>())
                .alterers(
                        new Mutator<>(1.0),
                        new SinglePointCrossover<>(0.0)
                ).build();



        final EvolutionStatistics<Double,?> statistics=EvolutionStatistics.ofNumber();

        final  Phenotype<BitGene,Double> best=engine.stream()
               // .limit(bySteadyFitness(15))
                .limit(5)
                .peek(r-> System.out.println("########CURRENT GEN: "
                        +r.generation()+
                        ": "+ r.totalGenerations()+
                        ": "+r.bestPhenotype()+
                        " ALTERED: "+r.alterCount()+
                        " INVALID: "+r.invalidCount()+
                        " GENOTYPE: "+r.genotypes()))
                .peek(statistics)
                .collect(toBestPhenotype());

        System.out.println(statistics);
        System.out.println(best);
    }
}

//class MyFunction implements Function<boolean[],Double> {
//    double[] data;
//
//    public MyFunction(double[] data) {
//        this.data = data;
//    }
//
//    @Override
//    public Double apply(boolean[] chromosome) {
//        return ISProblem.MyCostFunction(  chromosome,  data);
//    }
//}

