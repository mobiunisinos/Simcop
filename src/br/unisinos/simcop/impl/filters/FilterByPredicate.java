package br.unisinos.simcop.impl.filters;

import br.unisinos.simcop.Utils;
import br.unisinos.simcop.core.Parameter;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.data.model.Context;
import java.util.Iterator;

/**
 * Sample class that allows only contexts that have at least one of the predicates configured
 * @author tiago
 */
public class FilterByPredicate extends AbstractInputFilter {

    public Parameters getDefaultParameters() {
        Parameters pars = new Parameters();
        pars.addParameter("filterType", "AND");
        pars.addParameter("predicate1", "<predicate_name>");
        /* sample:
         * pars.addParameter("predicate2", "<predicate_name>");
         * pars.addParameter("predicate<...>", "<predicate_name>");
         * pars.addParameter("predicateN", "<predicate_name>");
         */
        return pars;
    }

    public boolean[] accept(int globalIndex, Context ctx1, Context ctx2) {
        boolean[] result = new boolean[2];
        result[0] = testContext(ctx1);
        result[1] = testContext(ctx2);
        return result;
    }

    private boolean testContext(Context ctx) {
        if (ctx == null) {
            return false;
        }
        String filterType = Utils.getNotNull(getSimpleParameter("filterType"));
        boolean isAnd = filterType.equalsIgnoreCase("AND");

        Iterator<String> it = getParameters().keySet().iterator();
        Boolean result = null;
        while (result == null && it.hasNext()) {
            String parName = it.next();
            if (parName.startsWith("predicate")) {
                boolean ctxHasPredicate = (ctx.findSituation(getSimpleParameter(parName)) != null);
                if (isAnd) {
                    if (!ctxHasPredicate) {
                        result = false;
                    }
                } else {
                    if (ctxHasPredicate) {
                        result = true;
                    }
                }
            }
        }
        if (result == null) {
            if (isAnd) {
                result = true;
            } else {
                result = false;
            }
        }

        return result;


    }
}
