import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.engine.Limits;

import java.util.Random;
import java.util.stream.Stream;

public class Knapsack {

    public static void main(String[] args) {
        int nItems=15;
        double ksSize=nItems*100.0/3.0;

        KnapsackFF ff=new KnapsackFF(Stream.generate(KnapsackItem::random)
                .limit(nItems)
                .toArray(KnapsackItem[]::new),ksSize);



        Engine<BitGene,Double> engine=Engine.builder(ff, BitChromosome.of(nItems,0.5))
                .populationSize(50)
                .survivorsSelector(new TournamentSelector<>(5))
                .offspringSelector(new RouletteWheelSelector<>())
                .alterers(new Mutator<>(0.115),new SinglePointCrossover<>(0.16))
                .build();

        EvolutionStatistics<Double,?> stats=EvolutionStatistics.ofNumber();

        Phenotype<BitGene, Double> best=engine.stream()
                .limit(Limits.bySteadyFitness(17))
                .limit(100)
                .peek(stats)
                .collect(EvolutionResult.toBestPhenotype());

        System.out.println(stats);
        System.out.println(best);


    }
}
