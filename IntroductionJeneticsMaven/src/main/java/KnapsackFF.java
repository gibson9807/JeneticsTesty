import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.Genotype;

import java.util.function.Function;

public class KnapsackFF implements Function<Genotype<BitGene>,Double> {

    private KnapsackItem[] items;
    private double size;

    public KnapsackFF(KnapsackItem[] items, double size) {
        this.items = items;
        this.size = size;
    }

    @Override
    public Double apply(Genotype<BitGene> chromosomes) {
        KnapsackItem sum=((BitChromosome)chromosomes.chromosome()).ones()
                .mapToObj(i->items[i])
                .collect(KnapsackItem.toSum());
        return sum.size<=this.size? sum.value :0;
    }
}
