import java.sql.*;

public class BaseDate {

   private static final String url = "jdbc:mysql://us-cdbr-iron-east-01.cleardb.net:3306/heroku_839483b01dcbc34?useUnicode=true&characterEncoding=UTF-8";
   private static final String name="b3fd7df0612459";
   private static final String password="7ee5e40e";

    private static Connection con;
    static ResultSet res;

   static  {
        try {
            con = DriverManager.getConnection(url, name, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void baseRead(String sql) throws SQLException  {


         con.createStatement()
                .execute(sql);

    }

    public static void baseWrite(String sql)throws SQLException {


        res = con.createStatement()
                .executeQuery(sql);
        res.next();
    }










    }


