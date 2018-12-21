package br.unisinos.simcop.interfaces.filters;

import br.unisinos.simcop.core.ContextPair;
import br.unisinos.simcop.core.ISimcopClass;
import br.unisinos.simcop.core.SimilarityResult;

/**
 *
 * @author tiago
 */
public interface IFilterOutput extends ISimcopClass {
   public boolean accept(SimilarityResult result, ContextPair resultPair, int pairIndex);
}
