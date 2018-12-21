package br.unisinos.simcop.data.source;

import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.impl.sources.SimpleJdbcSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tiago
 */
public abstract class AbstractFileSource extends AbstractSequenceSource {

    public static final String PAR_FILE_PATH = "filePath";
    protected File file;
    protected BufferedReader fileReader;

    public AbstractFileSource() {
        
    }
    
    public AbstractFileSource(File file) {
        this.file = file;
    }

    public Parameters getDefaultParameters() {
        Parameters pars = new Parameters();
        pars.addParameter(PAR_FILE_PATH, System.getProperty("user.home") + "/sample.csv");
        internalDefaultParameters(pars);
        return pars;
    }    
    
    protected abstract void internalDefaultParameters(Parameters pars);
    
    public void resetReader() throws Exception {
        if (isConnected()) {
            disconnectFromSource();
        }
        connectToSource();
    }
    
    public boolean connectToSource() {
        if (file == null) {
            file = new File(getSimpleParameter(PAR_FILE_PATH));
        }
        
        if (file != null && file.exists() && file.canRead()) {
            try {
                fileReader = new BufferedReader(new FileReader(file));
                return true;
            } catch (Throwable ex) {
                Logger.getLogger(SimpleJdbcSource.class.getName()).log(Level.SEVERE, null, ex);
                addError(ex);
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isConnected() {
        try {
            return fileReader != null && fileReader.ready();
        } catch (IOException ex) {
            Logger.getLogger(AbstractFileSource.class.getName()).log(Level.SEVERE, null, ex);
            addError(ex);
            return false;
        }
    }
    
    public boolean disconnectFromSource() {
        if (isConnected()) {
            try {
                fileReader.close();
            } catch (IOException ex) {
                Logger.getLogger(AbstractFileSource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true;
    }
    

}
