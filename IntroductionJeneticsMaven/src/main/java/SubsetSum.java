import io.jenetics.EnumGene;
import io.jenetics.Mutator;
import io.jenetics.PartiallyMatchedCrossover;
import io.jenetics.Phenotype;
import io.jenetics.engine.*;
import io.jenetics.prngine.LCG64ShiftRandom;
import io.jenetics.util.ISeq;
import java.util.Random;
import java.util.function.Function;
import io.jenetics.engine.Limits;
import io.jenetics.util.RandomRegistry;



import static java.lang.Math.abs;
import static java.util.Objects.requireNonNull;

public class SubsetSum implements Problem<ISeq<Integer>, EnumGene<Integer>, Integer> {

        private ISeq<Integer> basicSet;
        private int size;

        public SubsetSum(ISeq<Integer> basicSet, int size){
            this.basicSet=requireNonNull(basicSet);
            this.size=size;
        }

        @Override
        public Function<ISeq<Integer>, Integer> fitness() {
            return subset->abs(subset.stream().mapToInt(Integer::intValue).sum());
        }

        @Override
        public Codec<ISeq<Integer>, EnumGene<Integer>> codec() {
            return Codecs.ofSubSet(basicSet, size);
        }

        public static SubsetSum of(int n, int k, Random random){
            return new SubsetSum(random.doubles().limit(n).mapToObj(d->(int)((d-0.5)*n)).collect(ISeq.toISeq()),k);
        }

    public static void main(String[] args) {
       // SubsetSum problem = of(500, 15, new Random(101010));    Duplicate key -31 (attempted merging values -31 and -31) JKA SIE ZMIENIA W NAWIASIE TO ZMIENIA LICZBY -31
        //SubsetSum problem = of(500, 15,new LCG64ShiftRandom(101010) );  incompatible types: io.jenetics.prngine.LCG64ShiftRandom cannot be converted to java.util.Random
        SubsetSum problem = of(500, 15,new Random(101010) );

        Engine<EnumGene<Integer>,Integer> engine= Engine.builder(problem)
                .minimizing()
                .maximalPhenotypeAge(5)
                .alterers(new PartiallyMatchedCrossover<>(0.4),new Mutator<>(0.3))
                .build();

        Phenotype<EnumGene<Integer>,Integer> result=engine.stream()
                .limit(Limits.bySteadyFitness(55))
                .collect(EvolutionResult.toBestPhenotype());

        System.out.println(result);
    }


}
