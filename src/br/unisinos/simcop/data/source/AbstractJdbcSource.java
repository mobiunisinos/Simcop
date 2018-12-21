package br.unisinos.simcop.data.source;

import br.unisinos.simcop.Utils;
import br.unisinos.simcop.core.ISimcopClass;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Base class for load entities and contexts representations from a database
 * @author tiago
 */
public abstract class AbstractJdbcSource extends AbstractSequenceSource {

    /**
     * The JDBC Connection
     */
    protected Connection connection;

    /**
     * Begins a transaction by calling {@link #connection}.setAutoCommit(false);
     * @return
     */
    protected boolean beginTransaction() {
        try {
            if (isConnected()) {
                this.connection.setAutoCommit(false);
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AbstractJdbcSource.class.getName()).log(Level.SEVERE, null, ex);
            addError(ex);
            return false;
        }
    }

    /**
     * ends the transaction
     * @throws SQLException
     */
    protected void commit() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            connection.commit();
        }
    }

    /*
     * ends the transaction undoing changes
     */
    protected void rollback() {
        try {
            if (connection != null && !connection.getAutoCommit()) {
                connection.rollback();
            }
        } catch (SQLException ex) {
            Logger.getLogger(AbstractJdbcSource.class.getName()).log(Level.SEVERE, null, ex);
            addError(ex);
        }
    }

    /**
     * Connects to database
     * @param driver
     * @param url
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    protected boolean createConnection(String driver, String url, String username, String password) throws Exception {
        Class.forName(driver);
        if (!Utils.isEmpty(username)) {
            this.connection = DriverManager.getConnection(url, username, password);
        } else {
            this.connection = DriverManager.getConnection(url);
        }
        return this.connection != null && !this.connection.isClosed();
    }

    /**
     * disconect from database
     * @return
     */
    public boolean disconnectFromSource() {
        try {
            if (this.connection != null) {
                this.connection.close();
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(AbstractJdbcSource.class.getName()).log(Level.SEVERE, null, ex);
            addError(ex);
            return false;
        }
    }

    protected ResultSet doQuery(String sql) throws Exception {
        return doQueryThrowable(sql);
    }

    protected PreparedStatement prepareThrowable(String sql) throws Exception {
        return this.connection.prepareStatement(sql);
    }

    protected PreparedStatement prepare(String sql) {
        try {
            return prepareThrowable(sql);
        } catch (Exception ex) {
            Logger.getLogger(AbstractJdbcSource.class.getName()).log(Level.SEVERE, null, ex);
            addError(ex);
            addError(" Query: \"" + sql + "\"");
            return null;
        }
    }

    protected ResultSet doQueryThrowable(String sql) throws Exception {
        if (this.connection != null) {
            PreparedStatement stm = prepareThrowable(sql);
            return stm.executeQuery();
        } else {
            return null;
        }
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException ex) {
            Logger.getLogger(AbstractJdbcSource.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

}
