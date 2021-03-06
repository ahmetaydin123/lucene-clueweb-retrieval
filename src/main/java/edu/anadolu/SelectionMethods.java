package edu.anadolu;

import edu.anadolu.stats.TermStats;
import org.apache.commons.math3.stat.correlation.KendallsCorrelation;
import java.util.*;


public class SelectionMethods {
    public static String CONTENT_FIELD = "contents";
    public static double KendallTauThreshold = 1.0;

    public enum SelectionTag {

        MSTTF, MSTDF, TFOrder, DFOrder, KendallTauTFOrder, KendallTauDFOrder,MSTTFBinning,MSTDFBinning,
        TFOrderBinning, DFOrderBinning, KendallTauTFOrderBinning, KendallTauDFOrderBinning;

        public static SelectionTag tag(String selectionTag) {
           return valueOf(selectionTag);
        }
    }

    public static class TermTFDF {
        public static int NumberOfBIN = 10;
        public static int maxDF=20450000; // /1.7
        public static int maxTF=893148000; // /6


        private int indexID;
        private long TF;
        private long DF;
        private int binTF;
        private int binDF;

        public TermTFDF(int indexID) {
            this.indexID = indexID;
        }

        public int getIndexID() {
            return indexID;
        }

        public long getTF() {
            return TF;
        }

        public void setTF(long TF) {
            this.TF = TF;
            setBinTF(TF);
        }

        public long getDF() {
            return DF;
        }

        public void setDF(long DF) {
            this.DF = DF;
            setBinDF(DF);
        }

        public int getBinTF() {
            return binTF;
        }

        public void setBinTF(long TF) {
            int size=maxTF/NumberOfBIN;
            this.binTF=(int)TF/size;
        }

        public int getBinDF() {
            return binDF;
        }

        public void setBinDF(long DF) {
            int size=maxDF/NumberOfBIN;
            this.binDF=(int)DF/size;
        }
    }

    public static String getPredictedTag(String selectionTag,Map<String, ArrayList<TermStats>> tagTermTermStats, String[] tagsArr){
        switch (SelectionTag.tag(selectionTag)) {
            case MSTTF: return MSTTermFreq(tagTermTermStats,tagsArr);
            case MSTDF: return MSTDocFreq(tagTermTermStats, tagsArr);
            case TFOrder: return TFOrder(tagTermTermStats, tagsArr);
            case DFOrder: return DFOrder(tagTermTermStats, tagsArr);
            case KendallTauTFOrder: return KendallTauTFOrder(tagTermTermStats, tagsArr);
            case KendallTauDFOrder: return KendallTauDFOrder(tagTermTermStats, tagsArr);
            case MSTTFBinning: return MSTTFBinning(tagTermTermStats, tagsArr);
            case MSTDFBinning: return MSTDFBinning(tagTermTermStats, tagsArr);
            case TFOrderBinning: return TFOrderBinning(tagTermTermStats, tagsArr);
            case DFOrderBinning: return DFOrderBinning(tagTermTermStats, tagsArr);
            case KendallTauTFOrderBinning: return KendallTauTFOrderBinning(tagTermTermStats, tagsArr);
            case KendallTauDFOrderBinning: return KendallTauDFOrderBinning(tagTermTermStats, tagsArr);

            default: throw new AssertionError(SelectionMethods.class);
        }
    }

    private static String MSTDFBinning(Map<String, ArrayList<TermStats>> tagTermTermStats, String[] tagsArr) {
        ArrayList<TermTFDF> listTermTag1 = new ArrayList<TermTFDF>();
        ArrayList<TermTFDF> listTermTag2 = new ArrayList<TermTFDF>();

        ArrayList<TermStats> tsList = tagTermTermStats.get(tagsArr[0]);
        for (int i = 0; i < tsList.size(); i++) {
            TermTFDF termTFDF = new TermTFDF(i);
            termTFDF.setDF(tsList.get(i).docFreq());
            listTermTag1.add(termTFDF);
        }

        tsList = tagTermTermStats.get(tagsArr[1]);
        for (int i = 0; i < tsList.size(); i++) {
            TermTFDF termTFDF = new TermTFDF(i);
            termTFDF.setDF(tsList.get(i).docFreq());
            listTermTag2.add(termTFDF);
        }

        listTermTag1.sort((t1, t2) -> Integer.compare(t1.getBinDF(), t2.getBinDF()));
        listTermTag2.sort((t1, t2) -> Integer.compare(t1.getBinDF(), t2.getBinDF()));

        if (listTermTag1.get(0).getIndexID() != listTermTag2.get(0).getIndexID())
            return tagsArr[0]; //Nostem
        return tagsArr[1];
    }

    private static String MSTTFBinning(Map<String, ArrayList<TermStats>> tagTermTermStats, String[] tagsArr) {
        ArrayList<TermTFDF> listTermTag1 = new ArrayList<TermTFDF>();
        ArrayList<TermTFDF> listTermTag2 = new ArrayList<TermTFDF>();

        ArrayList<TermStats> tsList = tagTermTermStats.get(tagsArr[0]);
        for (int i = 0; i < tsList.size(); i++) {
            TermTFDF termTFDF = new TermTFDF(i);
            termTFDF.setTF(tsList.get(i).totalTermFreq());
            listTermTag1.add(termTFDF);
        }

        tsList = tagTermTermStats.get(tagsArr[1]);
        for (int i = 0; i < tsList.size(); i++) {
            TermTFDF termTFDF = new TermTFDF(i);
            termTFDF.setTF(tsList.get(i).totalTermFreq());
            listTermTag2.add(termTFDF);
        }

        listTermTag1.sort((t1, t2) -> Integer.compare(t1.getBinTF(), t2.getBinTF()));
        listTermTag2.sort((t1, t2) -> Integer.compare(t1.getBinTF(), t2.getBinTF()));

        if (listTermTag1.get(0).getIndexID() != listTermTag2.get(0).getIndexID())
            return tagsArr[0]; //Nostem
        return tagsArr[1];
    }

    private static String KendallTauDFOrderBinning(Map<String, ArrayList<TermStats>> tagTermTermStats, String[] tagsArr) {
        ArrayList<TermTFDF> listTermTag1 = new ArrayList<TermTFDF>();
        ArrayList<TermTFDF> listTermTag2 = new ArrayList<TermTFDF>();

        ArrayList<TermStats> tsList = tagTermTermStats.get(tagsArr[0]);
        for (int i = 0; i < tsList.size(); i++) {
            TermTFDF termTFDF = new TermTFDF(i);
            termTFDF.setDF(tsList.get(i).docFreq());
            listTermTag1.add(termTFDF);
        }

        tsList = tagTermTermStats.get(tagsArr[1]);
        for (int i = 0; i < tsList.size(); i++) {
            TermTFDF termTFDF = new TermTFDF(i);
            termTFDF.setDF(tsList.get(i).docFreq());
            listTermTag2.add(termTFDF);
        }

        listTermTag1.sort((t1, t2) -> Integer.compare(t1.getBinDF(), t2.getBinDF()));
        listTermTag2.sort((t1, t2) -> Integer.compare(t1.getBinDF(), t2.getBinDF()));

        if(listTermTag1.size() == 1) return tagsArr[0]; //One-term Stem
        else {
            if(KendallVal(listTermTag1,listTermTag2,1)-KendallTauThreshold >=0 ) return tagsArr[0]; //korelasyon var no stem
            else return tagsArr[1];
        }

    }

    private static String KendallTauTFOrderBinning(Map<String, ArrayList<TermStats>> tagTermTermStats, String[] tagsArr) {
        ArrayList<TermTFDF> listTermTag1 = new ArrayList<TermTFDF>();
        ArrayList<TermTFDF> listTermTag2 = new ArrayList<TermTFDF>();

        ArrayList<TermStats> tsList = tagTermTermStats.get(tagsArr[0]);
        for (int i = 0; i < tsList.size(); i++) {
            TermTFDF termTFDF = new TermTFDF(i);
            termTFDF.setTF(tsList.get(i).totalTermFreq());
            listTermTag1.add(termTFDF);
        }

        tsList = tagTermTermStats.get(tagsArr[1]);
        for (int i = 0; i < tsList.size(); i++) {
            TermTFDF termTFDF = new TermTFDF(i);
            termTFDF.setTF(tsList.get(i).totalTermFreq());
            listTermTag2.add(termTFDF);
        }

        listTermTag1.sort((t1, t2) -> Integer.compare(t1.getBinTF(), t2.getBinTF()));
        listTermTag2.sort((t1, t2) -> Integer.compare(t1.getBinTF(), t2.getBinTF()));

        if(listTermTag1.size() == 1) return tagsArr[0]; //One-term Stem
        else {
            if(KendallVal(listTermTag1,listTermTag2,1)-KendallTauThreshold >=0 ) return tagsArr[0]; //korelasyon var no stem
            else return tagsArr[1];
        }

    }

    private static String DFOrderBinning(Map<String, ArrayList<TermStats>> tagTermTermStats, String[] tagsArr) {
        ArrayList<TermTFDF> listTermTag1 = new ArrayList<TermTFDF>();
        ArrayList<TermTFDF> listTermTag2 = new ArrayList<TermTFDF>();

        ArrayList<TermStats> tsList = tagTermTermStats.get(tagsArr[0]);
        for (int i = 0; i < tsList.size(); i++) {
            TermTFDF termTFDF = new TermTFDF(i);
            termTFDF.setDF(tsList.get(i).docFreq());
            listTermTag1.add(termTFDF);
        }

        tsList = tagTermTermStats.get(tagsArr[1]);
        for (int i = 0; i < tsList.size(); i++) {
            TermTFDF termTFDF = new TermTFDF(i);
            termTFDF.setDF(tsList.get(i).docFreq());
            listTermTag2.add(termTFDF);
        }

        listTermTag1.sort((t1, t2) -> Integer.compare(t1.getBinDF(), t2.getBinDF()));
        listTermTag2.sort((t1, t2) -> Integer.compare(t1.getBinDF(), t2.getBinDF()));

        boolean orderChanged = false;
        for(int i=0; i<listTermTag1.size(); i++){
            if(listTermTag1.get(i).getIndexID()!= listTermTag2.get(i).getIndexID()){
                orderChanged = true;
                break;
            }
        }
        if(orderChanged) return tagsArr[1]; //Stem
        else return tagsArr[0];
    }

    private static String TFOrderBinning(Map<String, ArrayList<TermStats>> tagTermTermStats, String[] tagsArr) {
        ArrayList<TermTFDF> listTermTag1 = new ArrayList<TermTFDF>();
        ArrayList<TermTFDF> listTermTag2 = new ArrayList<TermTFDF>();

        ArrayList<TermStats> tsList = tagTermTermStats.get(tagsArr[0]);
        for (int i = 0; i < tsList.size(); i++) {
            TermTFDF termTFDF = new TermTFDF(i);
            termTFDF.setTF(tsList.get(i).totalTermFreq());
            listTermTag1.add(termTFDF);
        }

        tsList = tagTermTermStats.get(tagsArr[1]);
        for (int i = 0; i < tsList.size(); i++) {
            TermTFDF termTFDF = new TermTFDF(i);
            termTFDF.setTF(tsList.get(i).totalTermFreq());
            listTermTag2.add(termTFDF);
        }
        listTermTag1.sort((t1, t2) -> Integer.compare(t1.getBinTF(), t2.getBinTF()));
        listTermTag2.sort((t1, t2) -> Integer.compare(t1.getBinTF(), t2.getBinTF()));

        boolean orderChanged = false;
        for(int i=0; i<listTermTag1.size(); i++){
            if(listTermTag1.get(i).getIndexID()!= listTermTag2.get(i).getIndexID()){
                orderChanged = true;
                break;
            }
        }
        if(orderChanged) return tagsArr[1]; //Stem
        else return tagsArr[0];
    }

    private static String KendallTauDFOrder(Map<String, ArrayList<TermStats>> tagTermTermStats, String[] tagsArr) {
        ArrayList<TermTFDF> listTermTag1 = new ArrayList<TermTFDF>();
        ArrayList<TermTFDF> listTermTag2 = new ArrayList<TermTFDF>();

        ArrayList<TermStats> tsList = tagTermTermStats.get(tagsArr[0]);
        for (int i = 0; i < tsList.size(); i++) {
            TermTFDF termTFDF = new TermTFDF(i);
            termTFDF.setDF(tsList.get(i).docFreq());
            listTermTag1.add(termTFDF);
        }

        tsList = tagTermTermStats.get(tagsArr[1]);
        for (int i = 0; i < tsList.size(); i++) {
            TermTFDF termTFDF = new TermTFDF(i);
            termTFDF.setDF(tsList.get(i).docFreq());
            listTermTag2.add(termTFDF);
        }

        listTermTag1.sort((t1, t2) -> Long.compare(t1.getDF(), t2.getDF()));
        listTermTag2.sort((t1, t2) -> Long.compare(t1.getDF(), t2.getDF()));

        if(listTermTag1.size() == 1) return tagsArr[0]; //One-term Stem
        else {
            if(KendallVal(listTermTag1,listTermTag2,1)-KendallTauThreshold >=0 ) return tagsArr[0]; //korelasyon var no stem
            else return tagsArr[1];
        }

    }

    private static String KendallTauTFOrder(Map<String, ArrayList<TermStats>> tagTermTermStats, String[] tagsArr) {
        ArrayList<TermTFDF> listTermTag1 = new ArrayList<TermTFDF>();
        ArrayList<TermTFDF> listTermTag2 = new ArrayList<TermTFDF>();

        ArrayList<TermStats> tsList = tagTermTermStats.get(tagsArr[0]);
        for (int i = 0; i < tsList.size(); i++) {
            TermTFDF termTFDF = new TermTFDF(i);
            termTFDF.setTF(tsList.get(i).totalTermFreq());
            listTermTag1.add(termTFDF);
        }

        tsList = tagTermTermStats.get(tagsArr[1]);
        for (int i = 0; i < tsList.size(); i++) {
            TermTFDF termTFDF = new TermTFDF(i);
            termTFDF.setTF(tsList.get(i).totalTermFreq());
            listTermTag2.add(termTFDF);
        }

        listTermTag1.sort((t1, t2) -> Long.compare(t1.getTF(), t2.getTF()));
        listTermTag2.sort((t1, t2) -> Long.compare(t1.getTF(), t2.getTF()));

        if(listTermTag1.size() == 1) return tagsArr[0]; //One-term Stem
        else {
            if(KendallVal(listTermTag1,listTermTag2,1)-KendallTauThreshold >=0 ) return tagsArr[0]; //korelasyon var no stem
            else return tagsArr[1];
        }
    }

    private static String DFOrder(Map<String, ArrayList<TermStats>> tagTermTermStats, String[] tagsArr) {
        ArrayList<TermTFDF> listTermTag1 = new ArrayList<TermTFDF>();
        ArrayList<TermTFDF> listTermTag2 = new ArrayList<TermTFDF>();

        ArrayList<TermStats> tsList = tagTermTermStats.get(tagsArr[0]);
        for (int i = 0; i < tsList.size(); i++) {
            TermTFDF termTFDF = new TermTFDF(i);
            termTFDF.setDF(tsList.get(i).docFreq());
            listTermTag1.add(termTFDF);
        }

        tsList = tagTermTermStats.get(tagsArr[1]);
        for (int i = 0; i < tsList.size(); i++) {
            TermTFDF termTFDF = new TermTFDF(i);
            termTFDF.setDF(tsList.get(i).docFreq());
            listTermTag2.add(termTFDF);
        }

        listTermTag1.sort((t1, t2) -> Long.compare(t1.getDF(), t2.getDF()));
        listTermTag2.sort((t1, t2) -> Long.compare(t1.getDF(), t2.getDF()));

        boolean orderChanged = false;
        for(int i=0; i<listTermTag1.size(); i++){
            if(listTermTag1.get(i).getIndexID()!= listTermTag2.get(i).getIndexID()){
                orderChanged = true;
                break;
            }
        }
        if(orderChanged) return tagsArr[1]; //Stem
        else return tagsArr[0];
    }

    private static String TFOrder(Map<String, ArrayList<TermStats>> tagTermTermStats, String[] tagsArr) {
        ArrayList<TermTFDF> listTermTag1 = new ArrayList<TermTFDF>();
        ArrayList<TermTFDF> listTermTag2 = new ArrayList<TermTFDF>();

        ArrayList<TermStats> tsList = tagTermTermStats.get(tagsArr[0]);
        for (int i = 0; i < tsList.size(); i++) {
            TermTFDF termTFDF = new TermTFDF(i);
            termTFDF.setTF(tsList.get(i).totalTermFreq());
            listTermTag1.add(termTFDF);
        }

        tsList = tagTermTermStats.get(tagsArr[1]);
        for (int i = 0; i < tsList.size(); i++) {
            TermTFDF termTFDF = new TermTFDF(i);
            termTFDF.setTF(tsList.get(i).totalTermFreq());
            listTermTag2.add(termTFDF);
        }
        listTermTag1.sort((t1, t2) -> Long.compare(t1.getTF(), t2.getTF()));
        listTermTag2.sort((t1, t2) -> Long.compare(t1.getTF(), t2.getTF()));


        boolean orderChanged = false;
        for(int i=0; i<listTermTag1.size(); i++){
            if(listTermTag1.get(i).getIndexID()!= listTermTag2.get(i).getIndexID()){
                orderChanged = true;
                break;
            }
        }
        if(orderChanged) return tagsArr[1]; //Stem
        else return tagsArr[0];

    }

    private static String MSTDocFreq(Map<String, ArrayList<TermStats>> tagTermTermStats, String[] tagsArr) {
        ArrayList<TermTFDF> listTermTag1 = new ArrayList<TermTFDF>();
        ArrayList<TermTFDF> listTermTag2 = new ArrayList<TermTFDF>();

        ArrayList<TermStats> tsList = tagTermTermStats.get(tagsArr[0]);
        for (int i = 0; i < tsList.size(); i++) {
            TermTFDF termTFDF = new TermTFDF(i);
            termTFDF.setDF(tsList.get(i).docFreq());
            listTermTag1.add(termTFDF);
        }

        tsList = tagTermTermStats.get(tagsArr[1]);
        for (int i = 0; i < tsList.size(); i++) {
            TermTFDF termTFDF = new TermTFDF(i);
            termTFDF.setDF(tsList.get(i).docFreq());
            listTermTag2.add(termTFDF);
        }

        listTermTag1.sort((t1, t2) -> Long.compare(t1.getDF(), t2.getDF()));
        listTermTag2.sort((t1, t2) -> Long.compare(t1.getDF(), t2.getDF()));

        if (listTermTag1.get(0).getIndexID() != listTermTag2.get(0).getIndexID())
            return tagsArr[0]; //Nostem
        return tagsArr[1];

    }

    /**
     * This method returns one of the given IndexSearchers due to the changing of the most specific term in term frequency
     * If the most specific term changes in term frequency, the method returns stemmed tag
     */
    private static String MSTTermFreq(Map<String, ArrayList<TermStats>> tagTermTermStats, String[] tagsArr) {
        ArrayList<TermTFDF> listTermTag1 = new ArrayList<TermTFDF>();
        ArrayList<TermTFDF> listTermTag2 = new ArrayList<TermTFDF>();

        ArrayList<TermStats> tsList = tagTermTermStats.get(tagsArr[0]);
//        System.out.print(tagsArr[0]);
        for (int i = 0; i < tsList.size(); i++) {
            TermTFDF termTFDF = new TermTFDF(i);
            termTFDF.setTF(tsList.get(i).totalTermFreq());
            listTermTag1.add(termTFDF);
//            System.out.print(" "+tsList.get(i).term().utf8ToString());
        }
//        System.out.println();

        tsList = tagTermTermStats.get(tagsArr[1]);
//        System.out.print(tagsArr[1]);
        for (int i = 0; i < tsList.size(); i++) {
            TermTFDF termTFDF = new TermTFDF(i);
            termTFDF.setTF(tsList.get(i).totalTermFreq());
            listTermTag2.add(termTFDF);
//            System.out.print(" "+tsList.get(i).term().utf8ToString());
        }
//        System.out.println();
//        System.out.println("======= Before ======");
//        listTermTag1.stream().forEach(t -> System.out.println(t.indexID + " " + t.getTF()));
//        listTermTag2.stream().forEach(t -> System.out.println(t.indexID + " " + t.getTF()));

        listTermTag1.sort((t1, t2) -> Long.compare(t1.getTF(), t2.getTF()));
        listTermTag2.sort((t1, t2) -> Long.compare(t1.getTF(), t2.getTF()));

//        System.out.println("======= Afer ======");
//        listTermTag1.stream().forEach(t -> System.out.println(t.indexID + " " + t.getTF()));
//        listTermTag2.stream().forEach(t -> System.out.println(t.indexID + " " + t.getTF()));

        if (listTermTag1.get(0).getIndexID() != listTermTag2.get(0).getIndexID())
            return tagsArr[0]; //Nostem
        return tagsArr[1];

    }

    private static double KendallVal(ArrayList<TermTFDF> l1, ArrayList<TermTFDF> l2, int opt){
        double[] v1 = new double[l1.size()];
        double[] v2 = new double[l2.size()];

        if(opt==1){
            for (int i = 0; i < v1.length; ++i) v1[i] = l1.get(i).getIndexID();
            for (int i = 0; i < v2.length; ++i) v2[i] = l2.get(i).getIndexID();
        }
        else if(opt==2) {
            for (int i = 0; i < v1.length; ++i) v1[i] = l1.get(i).getBinDF();
            for (int i = 0; i < v2.length; ++i) v2[i] = l2.get(i).getBinDF();
        }else if(opt==3) {
            for (int i = 0; i < v1.length; ++i) v1[i] = l1.get(i).getBinTF();
            for (int i = 0; i < v2.length; ++i) v2[i] = l2.get(i).getBinTF();
        }
        if(Arrays.equals(v1, v2)) return 1;
        //if(Arrays.stream(v1).allMatch(d -> d == v1[0])) v1[0]=v1[0]-1;
        //if(Arrays.stream(v2).allMatch(d -> d==v2[0])) v2[0]=v2[0]-1;
        KendallsCorrelation corr = new KendallsCorrelation();
        double tau = corr.correlation(v1,v2);
        return tau;
    }
}
