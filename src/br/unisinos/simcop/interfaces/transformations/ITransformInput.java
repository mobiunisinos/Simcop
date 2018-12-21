package br.unisinos.simcop.interfaces.transformations;

import br.unisinos.simcop.core.ISimcopClass;
import br.unisinos.simcop.data.model.ContextSequence;

public interface ITransformInput extends ISimcopClass {
   public ContextSequence[] transform(ContextSequence contextSequence1, ContextSequence contextSequence2);
}
 
