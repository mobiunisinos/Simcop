package br.unisinos.simcop.interfaces.filters;

import br.unisinos.simcop.core.ISimcopClass;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.ContextSequence;

/**
 *
 * @author tiago
 */
public interface IFilterInput extends ISimcopClass {
    public boolean[] accept(int globalIndex, Context ctx1, Context ctx2);
}
