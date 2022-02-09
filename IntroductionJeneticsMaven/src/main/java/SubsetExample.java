import io.jenetics.EnumGene;
import io.jenetics.Mutator;
import io.jenetics.PartiallyMatchedCrossover;
import io.jenetics.Phenotype;
import io.jenetics.engine.*;
import io.jenetics.util.ISeq;

import java.util.function.Function;

import static java.lang.Math.abs;

public class SubsetExample implements Problem<ISeq<Integer>, EnumGene<Integer>,Integer> {
    private final ISeq<Integer> basicSet=ISeq.empty();
    private final int size=5;

    @Override
    public Function<ISeq<Integer>, Integer> fitness() {
        /*return subSet -> {
            assert (subSet.size() == size);
            int fitness = 0;
            for (Integer obj : subSet) {

            }
            return fitness;

        };
        */
        return subset->abs(subset.stream().mapToInt(Integer::intValue).sum());
    }
    @Override
    public Codec<ISeq<Integer>, EnumGene<Integer>> codec() {
        return Codecs.ofSubSet(basicSet, size);
    }

    public static void main(String[] args) {
        final SubsetExample problem=new SubsetExample();
        final Engine<EnumGene<Integer>,Integer> engine=Engine.builder(problem)
                .minimizing().maximalPhenotypeAge(5)
                .alterers(
                        new PartiallyMatchedCrossover<>(0.4),
                        new Mutator<>(0.3)
                ).build();


    final Phenotype<EnumGene<Integer>,Integer> result=engine.stream()
            .limit(Limits.bySteadyFitness(55))
            .collect(EvolutionResult.toBestPhenotype());

        System.out.println(result);
    }
}
