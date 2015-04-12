package no.javabin.jaxrs.hateoas.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeAdapter extends XmlAdapter<String, Date> {

    // See: http://blog.bdoughan.com/2010/07/xmladapter-jaxbs-secret-weapon.html
    // See: http://blog.bdoughan.com/2011/05/jaxb-and-joda-time-dates-and-times.html
    // See: http://stackoverflow.com/questions/3052513/jax-rs-json-java-util-date-unmarshall
    private Date date;

    public DateTimeAdapter() {}

    public DateTimeAdapter(String v) { this.date = stringToDate(v); }

    public DateTimeAdapter(Date v) { this.date = v; }

    public Date getDate(){
        return this.date;
    }

    @Override
    public Date unmarshal(String v) {
        return stringToDate(v);
    }

    @Override
    public String marshal(Date v) {
        return dateToString(v);
    }

    public static String dateToString(final Date v) {
        if(v != null) {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").format(v);
        }
        return null;
    }

    public static Date stringToDate(String v) {

        final String d = StringUtil.blankToNull(v);
        if(d != null) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse(d);
            }
            catch (ParseException e) {
                try {
                    return new SimpleDateFormat("yyyy-MM-dd").parse(d);
                }
                catch (ParseException e2) {
                    throw new IllegalArgumentException("Unparsable date: " + v, e2);
                }
            }
        }
        return null;
    }

}
