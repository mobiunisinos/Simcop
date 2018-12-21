package br.unisinos.simcop.impl.filters;

import br.unisinos.simcop.core.Parameter;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.interfaces.filters.IFilterInput;

/**
 *
 * @author tiago
 */
public class RemoveGaps extends AbstractFilter implements IFilterInput {

    public boolean[] accept(int globalIndex, Context ctx1, Context ctx2) {
        boolean[] result = new boolean[2];
        boolean ok = ctx1 != null && ctx2 != null;
        result[0] = ok;
        result[1] = ok;
        return result;
    }

    public Parameters getDefaultParameters() {
        return null;
    }

}
