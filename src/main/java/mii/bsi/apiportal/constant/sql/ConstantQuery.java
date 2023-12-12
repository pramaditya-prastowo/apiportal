package mii.bsi.apiportal.constant.sql;

public class ConstantQuery {

    public static String user = "SELECT a.id, a.first_name, a.last_name, a.create_date, a.email, " +
            "a.corporate_name, a.account_inactive, a.account_locked, a.mobile_phone, " +
            "a.auth_principal, a.email_verified, a.create_date, a.group_id \r\n" +
            "FROM bsi_user_api_portal a \r\n" +
            "WHERE 1=1 ";

    public static String userOrder = " ORDER BY a.first_name DESC";

    public static String needHelp = "SELECT a.* \r\n" +
            "FROM bsi_need_help_api_portal a \r\n" +
            "WHERE 1=1 ";
    public static String needHelpOrder = " ORDER BY a.status ASC";
}
