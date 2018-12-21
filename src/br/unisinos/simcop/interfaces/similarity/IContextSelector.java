package br.unisinos.simcop.interfaces.similarity;

import br.unisinos.simcop.data.model.Context;

public interface IContextSelector {
    public abstract boolean isSelectContext(double value, IContextSimilarity similarityFunction, Context c1, Context c2);
}
