import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.EnumGene;
import io.jenetics.Genotype;
import io.jenetics.engine.*;
import io.jenetics.util.Factory;
import io.jenetics.util.ISeq;


import java.util.Scanner;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public final class HelloWorld  {

//BIT COUNTING PROBLEM

    private static int eval(final Genotype<BitGene> gt){
        return gt.chromosome().as(BitChromosome.class).bitCount();
    }

    public static void main(String[] args) {
        final Factory<Genotype<BitGene>> gtf=Genotype.of(BitChromosome.of(10,0.5));
        System.out.println("Before: "+gtf);

        final Engine<BitGene,Integer> engine=Engine.builder(HelloWorld::eval, gtf).build();

        final Genotype<BitGene> result=engine.stream().limit(10).collect(EvolutionResult.toBestGenotype());

        System.out.println("After: " +result);
    }

}


