package br.unisinos.simcop.core;

import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.ContextSequence;
import java.util.ArrayList;
import java.util.List;

/**
 * Results of the similarity analysis
 * @author tiago
 */
public class SimilarityResult {

    private Double calculatedValue;
    private ContextSequence s1;
    private ContextSequence s2;
    private List<ContextPair> contextPairs;

    public SimilarityResult(ContextSequence s1, ContextSequence s2) {
        this.s1 = s1;
        this.s2 = s2;
    }


    public Double getCalculatedValue() {
        return calculatedValue;
    }

    public void setCalculatedValue(Double calculatedValue) {
        this.calculatedValue = calculatedValue;
    }

    public List<ContextPair> getContextPairs() {
        if (contextPairs == null) {
            contextPairs = new ArrayList<ContextPair>();
        }
        return contextPairs;
    }

    public void setContextPairs(List<ContextPair> contextPairs) {
        this.contextPairs = contextPairs;
    }

    public ContextSequence getS1() {
        return s1;
    }

    public void setS1(ContextSequence s1) {
        this.s1 = s1;
    }

    public ContextSequence getS2() {
        return s2;
    }

    public void setS2(ContextSequence s2) {
        this.s2 = s2;
    }

    public int size() {
        return getContextPairs().size();
    }

    public boolean isEmpty() {
        return contextPairs == null || contextPairs.isEmpty();
    }

    public ContextPair add(Context c1, Context c2, Double calculatedValue) {
        ContextPair cp = new ContextPair(size(), c1, c2, calculatedValue);
        getContextPairs().add(cp);
        return cp;
    }

}
