package br.unisinos.simcop.data.source;

import br.unisinos.simcop.core.ISimcopClass;
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.data.model.Entity;
import java.util.List;

public interface ISequenceSource extends ISimcopClass {
    /**
     * Open a connection to a Context Data Source (file, database, webservice, rmi, in-memory, ...)
     * @return
     */
   public boolean connectToSource();
   public List<Entity> getListEntities(Object... queryParameters) throws Exception;
   public Entity loadEntity(Object uid) throws Exception;
   public void loadIntoSequence(ContextSequence sequence) throws Exception;
   public Entity loadEntityAndSequence(Object entityUID) throws Exception;
   public boolean disconnectFromSource();
   public boolean isConnected();
   public List<String> getErrors();
   public void resetErrors();
   public void addError(Throwable ex);
   public void addError(String msg);
}
 
