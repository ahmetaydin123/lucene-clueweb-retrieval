package edu.anadolu.ltr;

import edu.anadolu.similarities.BM25;

import java.io.IOException;
import java.util.List;

public class WMWD_BM25 implements IQDFeature {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    @Override
    public double calculate(QDFeatureBase base, String word, List<String> subParts) throws IOException {
        double keyFreq = 0.0;
        for(String part : subParts){
            if(part.equals(word))   keyFreq+=1.0;
        }
        long sumTotalTermFreq = base.collectionStatistics.sumTotalTermFreq();
        long docCount = base.collectionStatistics.docCount();
        double tfForT = base.getTf(word,base.listContent);
        System.out.println("Doc : " + base.docId + " QID : "+ base.query.id() + " TF : " + base.tf + " DL : " + base.dl);
        return new BM25().score(tfForT,base.dl,(double) sumTotalTermFreq / docCount, keyFreq, base.termStatisticsMap.get(word).docFreq(), base.termStatisticsMap.get(word).totalTermFreq(),
                docCount,sumTotalTermFreq);
    }
}
