package mii.bsi.apiportal.utils;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class QueryUtils {
    public String generatePagination(String sql, String orderBy, String sort) {
        String order = "";
        if (!"".equalsIgnoreCase(sort) && orderBy != null && sort != null) {
            if ("asc".equalsIgnoreCase(sort)){
                sort = "asc nulls last";
            } else {
                sort = "desc nulls last";
            }
            orderBy = filterAlphanumericOnly(orderBy);
            orderBy = camelToSnake(orderBy);
            order = " ORDER BY "+ orderBy + " " + sort + "";
        }
        String pagination = "SELECT * FROM\r\n" +
                "(\r\n" +
                "    SELECT a.*, ROW_NUMBER() OVER () AS i\r\n" +
                "    FROM\r\n" +
                "    (\r\n" +sql + order+
                "    ) a \n" +
                ") a\r\n" +
                "WHERE i < ((:pageNumber * :pageSize) + 1 ) \r\n" +
                "\r\n" +
                "AND i >= (((:pageNumber-1) * :pageSize) + 1) ";

        return pagination;
    }

    public String countTotalRows(String sql) {
        String total = "select count(*) as total_record\r\n" +
                "from \r\n" +
                "(\r\n" +
                sql +
                ") as a";
        return total;
    }

    public static String camelToSnake(String str){

        // Empty String
        String result = "";

        // Append first character(in lower case)
        // to result string
        char c = str.charAt(0);
        result = result + Character.toLowerCase(c);

        // Traverse the string from
        // ist index to last index
        for (int i = 1; i < str.length(); i++) {

            char ch = str.charAt(i);

            // Check if the character is upper case
            // then append '_' and such character
            // (in lower case) to result string
            if (Character.isUpperCase(ch)) {
                result = result + '_';
                result
                        = result
                        + Character.toLowerCase(ch);
            }

            // If the character is lower case then
            // add such character into result string
            else {
                result = result + ch;
            }
        }

        // return the result
        return result;
    }

    public static String camelToSnakeRegex(String str)
    {
        // Regular Expression
        String regex = "([a-z])([A-Z]+)";

        // Replacement string
        String replacement = "$1_$2";

        // Replace the given regex
        // with replacement string
        // and convert it to lower case.
        str = str
                .replaceAll(
                        regex, replacement)
                .toLowerCase();

        // return string
        return str;
    }

    public static String filterAlphanumericOnly(String string) {
        final String regex = "([a-zA-Z0-9]+).*";
        final String subst = "$1";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);

        return matcher.replaceAll(subst);
    }
}
