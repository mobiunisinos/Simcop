package br.unisinos.simcop.impl.transformations;

import br.unisinos.simcop.Utils;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.ContextSequence;
import java.util.Stack;

/**
 * Aligns two sequences by inserting of gaps to obtain the best possible alignment, considering the time.<br/>
 * Eg:<br/>
 * <tt>
 * Sequence A = {05:00, 05:30, 06:00}<br/>
 * Sequence B = {05:15, 05:27, 05:38}
 * </tt>
 * <br/><b>After the alignement:</b><br/>
 * <tt>
 * Sequence A = {05:00,  null,  null, 05:30, 06:00}<br/>
 * Sequence B = {null , 05:15, 05:27,  null, 05:38}
 * </tt>
 *
 * @author tiago
 */
public class DefaultTimeAlign extends AbstractInputTransformation {

    public Parameters getDefaultParameters() {
        return null;
    }

    private int minTime(Context ctx1, Context ctx2) {
        int compare = ctx1.getTime().compareTo(ctx2.getTime());
        if (compare < 0) {
            return 1;
        } else if (compare > 0) {
            return 2;
        } else {
            return 0;
        }
    }

    public ContextSequence[] transform(ContextSequence contextSequence1, ContextSequence contextSequence2) {
        ContextSequence[] result = init(contextSequence1, contextSequence2);

        Stack<Context> a = Utils.convertToStack(contextSequence1);
        Stack<Context> b = Utils.convertToStack(contextSequence2);

        Context ctx1 = null;
        Context ctx2 = null;
        //read contexts
        while (!a.isEmpty() || !b.isEmpty()) {
            //contexts selected
            Context selectedA = null;
            Context selectedB = null;

            //get next contexts from sequences
            if (ctx1 == null && !a.isEmpty()) {
                ctx1 = a.pop();
            }
            if (ctx2 == null && !b.isEmpty()) {
                ctx2 = b.pop();
            }

            if (ctx1 != null) {
                //sequence A has a context
                if (ctx2 != null) {
                    //sequence B has a context
                    int minor = minTime(ctx1, ctx2);
                    switch (minor) {
                        case 0:
                            //same time
                            selectedA = ctx1;
                            selectedB = ctx2;
                            ctx1 = null;
                            ctx2 = null;
                            break;
                        case 1:
                            //context A before context B
                            selectedA = ctx1;
                            ctx1 = null;
                            break;
                        case 2:
                            //context B before context A
                            selectedB = ctx2;
                            ctx2 = null;
                            break;
                    }

                } else {
                    //sequence B is empty
                    selectedA = ctx1;
                    ctx1 = null;
                }
            } else {
                //sequence A is empty
                if (ctx2 != null) {
                    //sequence B has a context
                    selectedB = ctx2;
                    ctx2 = null;
                }
            }

            result[0].addContext(selectedA);
            result[1].addContext(selectedB);

        }

        return result;
    }
}
