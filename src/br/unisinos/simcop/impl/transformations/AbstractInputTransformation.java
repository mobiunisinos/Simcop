/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unisinos.simcop.impl.transformations;

import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.interfaces.transformations.ITransformInput;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tiago
 */
public abstract class AbstractInputTransformation extends AbstractTransformation implements ITransformInput {

    protected ContextSequence[] init(ContextSequence ctx1, ContextSequence ctx2) {
        ContextSequence[] result = new ContextSequence[2];
            result[0] = new ContextSequence(ctx1.getEntity());
            result[1] = new ContextSequence(ctx2.getEntity());
        return result;
    }
}
