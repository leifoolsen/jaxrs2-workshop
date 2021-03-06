package no.javabin.jaxrs.hateoas.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAdapter extends XmlAdapter<String, Date> {
    private Date date;

    public DateAdapter(String v) { this.date = stringToDate(v); }

    public DateAdapter(Date v) { this.date = v; }

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
            return new SimpleDateFormat("yyyy-MM-dd").format(v);
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
