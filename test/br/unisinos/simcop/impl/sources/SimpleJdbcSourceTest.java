package br.unisinos.simcop.impl.sources;

import br.unisinos.simcop.data.model.TimeDescription;
import br.unisinos.simcop.data.model.Context;
import java.sql.PreparedStatement;
import br.unisinos.simcop.tests.BaseUnitTest;
import br.unisinos.simcop.data.model.Entity;
import java.sql.ResultSet;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author tiago
 */
public class SimpleJdbcSourceTest extends BaseUnitTest {

    private static String driver;
    private static String url;
    private static String entityQuery;
    private static String contextQuery;
    private static String user;
    private static String password;

    public SimpleJdbcSourceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        driver = "org.postgresql.Driver";
        url = "jdbc:postgresql://localhost/SimcopTest";
        user = "postgres";
        password = "postgres";
        entityQuery = "select username, address, city, country, birthday, age, income from userentity where id = ?";
        contextQuery = "select instant, product_id, product_category, product_price, time_elapsed_minutes from usershopping where user_id = ? order by instant";
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testConnectToSource() {
        showBeginTest();
        SimpleJdbcSource instance = new SimpleJdbcSource(user, password);
        boolean expResult = true;
        try {
            instance.setParameter(SimpleJdbcSource.P_DRIVER, driver);
            instance.setParameter(SimpleJdbcSource.P_URL, url);
            boolean result = instance.connectToSource();
            assertEquals(expResult, result);
            showEndTest();
        } finally {
            instance.disconnectFromSource();
        }
    }

    @Test
    public void testDisconnectFromSource() {
        showBeginTest();
        SimpleJdbcSource instance = new SimpleJdbcSource(user, password);
        try {
            instance.setParameter(SimpleJdbcSource.P_DRIVER, driver);
            instance.setParameter(SimpleJdbcSource.P_URL, url);
            assertTrue(instance.connectToSource());
            assertTrue(instance.disconnectFromSource());
            assertFalse(instance.isConnected());
            showEndTest();
        } finally {
            instance.disconnectFromSource();
        }
    }

    @Test
    public void testLoadEntity() throws Exception {
        showBeginTest();
        SimpleJdbcSource instance = new SimpleJdbcSource(user, password);
        try {
            instance.setParameter(SimpleJdbcSource.P_DRIVER, driver);
            instance.setParameter(SimpleJdbcSource.P_URL, url);
            instance.setParameter(SimpleJdbcSource.P_ENTITY_QUERY, entityQuery);

            boolean result = instance.connectToSource();
            assertEquals(true, result);

            Entity entity = instance.loadEntity(1L);

            assertEquals("Joe", entity.getName());
            assertEquals(6, entity.getAttributes().size());
            assertEquals("728 Fifth Avenue, 44th floor", entity.getAttributes().get("address").getValue().getStringValue());
            assertEquals("New York", entity.getAttributes().get("city").getValue().getStringValue());
            assertEquals("USA", entity.getAttributes().get("country").getValue().getStringValue());
            assertEquals(33, entity.getAttributes().get("age").getValue().getIntegerValue(), 0);
            assertEquals(50394.21, entity.getAttributes().get("income").getValue().getDoubleValue(), 0);

            TimeDescription td = entity.getAttributes().get("birthday").getValue().getDateTimeValue();
            assertEquals(1980, td.getYear(), 0);
            assertEquals(3, td.getMonth(), 0);
            assertEquals(7, td.getDay(), 0);

            showEndTest();
        } finally {
            instance.disconnectFromSource();
        }
    }

    @Test
    public void testCreateContext() throws Exception {
        showBeginTest();
        SimpleJdbcSource instance = new SimpleJdbcSource(user, password);
        try {
            instance.setParameter(SimpleJdbcSource.P_DRIVER, driver);
            instance.setParameter(SimpleJdbcSource.P_URL, url);
            boolean result = instance.connectToSource();
            assertEquals(true, result);

            //get a specific context for test
            StringBuilder testQuery = new StringBuilder();
            testQuery.append("select ");
            testQuery.append("  instant"); //time
            testQuery.append(", walking_speed"); //situation 1
            testQuery.append(", direction"); //situation 2
            testQuery.append(", heart_beats"); //situation 3
            testQuery.append("  from usersituation");
            testQuery.append(" where user_id = 1 ");
            testQuery.append("   and instant = '2013-10-01 08:00:00'");
            PreparedStatement stm = instance.getConnection().prepareStatement(testQuery.toString());
            ResultSet rs = stm.executeQuery();
            assertTrue(rs.next());
            //--------------------------------

            //--------------
            Context ctx = instance.createContext(rs);
            //--------------

            assertNotNull("context null", ctx);
            assertNotNull("time null", ctx.getTime());
            assertEquals(3, ctx.getSituations().size(), 0);
            assertEquals(2013, ctx.getTime().getYear(), 0);
            assertEquals(10, ctx.getTime().getMonth(), 0);
            assertEquals(1, ctx.getTime().getDay(), 0);
            assertEquals(8, ctx.getTime().getHour(), 0);
            assertEquals(0, ctx.getTime().getMinute(), 0);
            assertEquals(0, ctx.getTime().getSecond(), 0);
            assertEquals(5.1202, ctx.findSituation("walking_speed").getValue().getDoubleValue(), 0);
            assertEquals("east", ctx.findSituation("direction").getValue().getStringValue());
            assertEquals(86, ctx.findSituation("heart_beats").getValue().getIntegerValue(), 0);


            showEndTest();
        } finally {
            instance.disconnectFromSource();
        }
    }

    @Test
    public void testLoadEntityAndSequence() throws Exception {
        showBeginTest();
        SimpleJdbcSource instance = new SimpleJdbcSource(user, password);
        try {
            instance.setParameter(SimpleJdbcSource.P_DRIVER, driver);
            instance.setParameter(SimpleJdbcSource.P_URL, url);
            instance.setParameter(SimpleJdbcSource.P_ENTITY_QUERY, entityQuery);
            instance.setParameter(SimpleJdbcSource.P_SEQUENCE_QUERY, contextQuery);

            boolean result = instance.connectToSource();
            assertEquals(true, result);

            Entity entity = instance.loadEntityAndSequence(1L);
            assertNotNull("entity null", entity);
            assertNotNull("no sequence", entity.getSequence());
            assertEquals(4, entity.getSequence().size());
            Context ctx1 = entity.getContext(0);
            Context ctx2 = entity.getContext(1);
            Context ctx3 = entity.getContext(2);
            Context ctx4 = entity.getContext(3);

            assertNotNull("context 1 null", ctx1);
            assertNotNull("context 2 null", ctx2);
            assertNotNull("context 3 null", ctx3);
            assertNotNull("context 4 null", ctx4);

            assertNotNull("context time 1 null", ctx1.getTime());
            assertNotNull("context time 2 null", ctx2.getTime());
            assertNotNull("context time 3 null", ctx3.getTime());
            assertNotNull("context time 4 null", ctx4.getTime());

            assertEquals(4, ctx1.getSituations().size());
            assertEquals(4, ctx2.getSituations().size());
            assertEquals(4, ctx3.getSituations().size());
            assertEquals(4, ctx4.getSituations().size());

            showEndTest();
        } finally {
            instance.disconnectFromSource();
        }
    }
}
