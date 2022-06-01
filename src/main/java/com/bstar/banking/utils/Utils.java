package com.bstar.banking.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class Utils {

    /**
     * This method return a String text from file
     * @param file
     * @return JsonString
     */
    public static String getJsonStringFromFile(String file) {
        Utils ut = new Utils();
        try {
            return ut.getJsonString(file);
        } catch (Exception e) {
            return null;
        }
    }

    private String getJsonString(String file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Resource resource = new ClassPathResource(file, this.getClass().getClassLoader());
        return mapper.writeValueAsString(mapper.readValue(resource.getFile(), Object.class));
    }

    public static String toString(Exception e) {

        StringWriter s = new StringWriter();
        e.printStackTrace(new PrintWriter(s));
        return s.toString();
    }

    /**
     * Check a String starts with another string ignoring the case.
     *
     * @param str
     * @param prefix
     * @return
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {

        if (str == null || prefix == null) {
            return false;
        }
        if (str.startsWith(prefix)) {
            return true;
        }
        if (str.length() < prefix.length()) {
            return false;
        } else {
            return str.toLowerCase().startsWith(prefix.toLowerCase());
        }
    }

    /**
     * Check a String ends with another string ignoring the case.
     *
     * @param str
     * @param suffix
     * @return
     */
    public static boolean endsWithIgnoreCase(String str, String suffix) {

        if (str == null || suffix == null) {
            return false;
        }
        if (str.endsWith(suffix)) {
            return true;
        }
        if (str.length() < suffix.length()) {
            return false;
        } else {
            return str.toLowerCase().endsWith(suffix.toLowerCase());
        }
    }

    /**
     * This method is used to split the given string into different tokens at
     * the occurrence of specified delimiter
     * An example :
     * "abcdzefghizlmnop" and using a delimiter "z"
     * would give following output
     * "abcd" "efghi" "lmnop"
     *
     * @param str The string that needs to be broken
     * @param delimeter The delimiter used to break the string
     * @return a string array
     */
    public static String[] getTokensArray(String str, String delimeter) {

        String[] data;
        if(str == null){
            return new String[0];
        }

        if (delimeter == null || "".equals(delimeter)
                || "".equals(str)) {
            data = new String[1];
            data[0] = str;
            return data;
        } else {
            StringTokenizer st = new StringTokenizer(str, delimeter);
            int tokenCount = st.countTokens();
            data = new String[tokenCount];
            for (int i = 0; st.hasMoreTokens(); i++) {
                data[i] = st.nextToken();
            }
            return data;
        }
    }

    /**
     * This String Util method convert Date to String with format pattern
     * @param date date to convert
     * @param format pattern to convert to
     * @return String date
     *
     */
    public static String convertDateToString(Date date, String format) {
        Format f = new SimpleDateFormat(format);
        return f.format(date);
    }

    public static Date addMonthToData(Date date, long m) {
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        localDateTime = localDateTime.plusMonths(m);

        return java.sql.Timestamp.valueOf(localDateTime);
    }

    public static Date addDateToData(Date date, long d) {
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        localDateTime = localDateTime.plusDays(d);

        return java.sql.Timestamp.valueOf(localDateTime);
    }

    public static Date addWeekToDate(Date date, long w) {
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        localDateTime = localDateTime.plusWeeks(w);

        return java.sql.Timestamp.valueOf(localDateTime);
    }

    public static Date addYearToDate(Date date, long y) {
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        localDateTime = localDateTime.plusYears(y);

        return java.sql.Timestamp.valueOf(localDateTime);
    }

    /**
     * This String util method removes single or double quotes
     * from a string if its quoted.
     * for input string = "mystr1" output will be = mystr1
     * for input string = 'mystr2' output will be = mystr2
     *
     * @param s value to be unquoted.
     * @return value unquoted, null if input is null.
     *
     */
    public static String unquote(String s) {

        if (s != null
                && ((s.startsWith("\"") && s.endsWith("\""))
                || (s.startsWith("'") && s.endsWith("'")))) {

            s = s.substring(1, s.length() - 1);
        }
        return s;
    }

    /**
     * Same method as above but using the ?: syntax which is shorter. You can use whichever you prefer.
     * This String util method removes single or double quotes from a string if
     * its quoted. for input string = "mystr1" output will be = mystr1 for input
     *
     * string = 'mystr2' output will be = mystr2
     *
     * @param s
     *            value to be unquoted.
     * @return value unquoted, null if input is null.
     *
     */
    public static String unquote2(String s) {

        return (s != null && ((s.startsWith("\"") && s.endsWith("\"")) || (s
                .startsWith("'") && s.endsWith("'")))) ? s = s.substring(1, s
                .length() - 1) : s;

    }

    /**
     * This String utility or util method can be used to
     * trim all the String values in the string array.
     * For input {" a1 ", "b1 ", " c1"}
     * the output will be {"a1", "b1", "c1"}
     * Method takes care of null values.
     * @param values
     * @return
     */
    public static String[] trim(final String[] values) {

        for (int i = 0, length = values.length; i < length; i++) {
            if (values[i] != null) {
                values[i] = values[i].trim();
            }
        }
        return values;

    }

    /**
     * This String utility or util method can be used to trim all the String
     * values in list of Strings. For input [" a1 ", "b1 ", " c1"] the output
     * will be {"a1", "b1", "c1"} Method takes care of null values. This method
     * is collections equivalent of the trim method for String array.
     *
     * @param values
     * @return
     */
    public static List<Object> trim(final List values) {

        List newValues = new ArrayList();
        for (int i = 0, length = values.size(); i < length; i++) {
            String v = (String) values.get(i);
            if (v != null) {
                v = v.trim();
            }
            newValues.add(v);
        }
        return newValues;
    }

    /**
     * This String utility or util method can be used to merge 2 arrays of
     * string values. If the input arrays are like this array1 = {"a", "b" ,
     * "c"} array2 = {"c", "d", "e"} Then the output array will have {"a", "b" ,
     * "c", "d", "e"}
     *
     * This takes care of eliminating duplicates and checks null values.
     *
     * @param array1
     * @param array2
     * @return String
     */
    public static String[] mergeStringArrays(String array1[], String array2[]) {

        if (array1 == null || array1.length == 0)
            return array2;
        if (array2 == null || array2.length == 0)
            return array1;
        List array1List = Arrays.asList(array1);
        List array2List = Arrays.asList(array2);
        List result = new ArrayList(array1List);
        List tmp = new ArrayList(array1List);
        tmp.retainAll(array2List);
        result.removeAll(tmp);
        result.addAll(array2List);
        return ((String[]) result.toArray(new String[result.size()]));
    }

    public static final String FULL_DATE_FORMAT = "E-dd-MMM-yyyy-hh:mm:ss.SSSZ";
    public static final String BASIC_DATE_FORMAT = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd hh:mm:ss";
    public static final String YYMMDDMMSSSSS = "yyyyMMddhhmmss.SSS";
}
