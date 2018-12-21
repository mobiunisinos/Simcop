package br.unisinos.simcop.interfaces.similarity;

import br.unisinos.simcop.core.ISimcopClass;

public interface ISimilarity extends ISimcopClass {
    public boolean isDistanceFunction();
    public void validateParameters() throws Exception;
}
