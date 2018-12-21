package br.unisinos.simcop.tests;

import br.unisinos.simcop.core.dataType.SimcopValue;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.LocationDescription;
import br.unisinos.simcop.data.model.Situation;
import br.unisinos.simcop.data.model.TimeDescription;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tiago
 */
public class BaseUnitTest {

    public void showBeginTest() {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        String cName = ste.getClassName();
        String mName = ste.getMethodName();

        cName = cName.substring(0, cName.length()-4);
        mName = mName.substring( 4 );

        StringBuilder sb = new StringBuilder();
        sb.append("/************************************************************************************\n");
        sb.append(" * Class...: ").append(cName).append("\n");
        sb.append(" * Method..: ").append(mName).append("\n");
        sb.append(" *************************************************************************************/\n");
        show(sb.toString());

    }
    public void showEndTest() {
        System.out.println("+OK\n");

    }

    public void show(String msg) {
        System.out.println(msg);
    }

   protected Context createContext(TimeDescription td, LocationDescription ld, Situation... situations) {
       return createContext(0, td, ld, situations);
   }

    protected Context createContext(int index, TimeDescription td, LocationDescription ld, Situation... situations) {
        Context result = new Context();
        result.setIndex(index);
        result.setTime(td);
        result.setLocation(ld);
        for (Situation situation : situations) {
            result.addSituation(situation);
        }
        return result;
    }


   protected TimeDescription time(int hour, int minute) {
   return new TimeDescription(hour, minute, 0, 0);
   }

   protected LocationDescription location(String name, String category) {
       return new LocationDescription(name, category, null, null);
   };

   protected LocationDescription location(String name) {
       return new LocationDescription(name, null, null, null);
   };

   protected Situation sit(String predicate, String value) {
       return new Situation(null, null, predicate, null, SimcopValue.createFromObject(value), null);
   }

   protected Situation sit(String auxiliary, String predicate, String value) {
       return new Situation(null, auxiliary, predicate, null, SimcopValue.createFromObject(value), null);
   }

   protected Situation sit(String source, String auxiliary, String predicate, String value) {
       return new Situation(source, auxiliary, predicate, null, SimcopValue.createFromObject(value), null);
   }

   protected void failException(Exception ex) {
            StringBuilder log = new StringBuilder();
            log.append(ex.getClass().getSimpleName());
            log.append(" \"");
            log.append(ex.getMessage());
            log.append("\"");
            log.append(": ");
            StackTraceElement ste = ex.getStackTrace()[0];
            if (ste != null) {
                log.append(ste.getClassName());
                log.append(" line ");
                log.append(ste.getLineNumber());
                log.append(" {").append(ste.getMethodName()).append("}");
            }
            fail(log.toString());

   }

    protected TimeDescription createTimeAge(int age) {
        TimeDescription td = new TimeDescription();
        td.setAge(age);
        return td;
    }
    protected TimeDescription createTimeSeason(int season) {
        TimeDescription td = new TimeDescription();
        td.setSeason(season);
        return td;
    }

    protected TimeDescription createTime(int year, int month, int day, int hour, int minute, int second, int millisecond) {
        TimeDescription td = new TimeDescription();
        td.setYear(year);
        td.setMonth(month);
        td.setDay(day);
        td.setHour(hour);
        td.setMinute(minute);
        td.setSecond(second);
        td.setMillisecond(millisecond);
        return td;
    }


    @Test
    public void dummy() { }
}
