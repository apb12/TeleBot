import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class BaseDate  {

   private static final String url = "jdbc:mysql://us-cdbr-iron-east-01.cleardb.net:3306/heroku_839483b01dcbc34?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
   private static final String name=;
   private static final String password=;













    public  void update(String sql) throws SQLException  {
       try (Connection con = DriverManager.getConnection(url, name, password);
               Statement statement=con.createStatement()){
           statement.executeUpdate(sql);
       }
       }





    public Map<Integer, String> getData(String sql) throws SQLException {
        Map<Integer, String> data = new HashMap<>();
        try (Connection con=DriverManager.getConnection(url,name,password);
                Statement statement = con.createStatement()) {
            ResultSet res = statement.executeQuery(sql);
            res.next();
            ResultSetMetaData resultSetMetaData = res.getMetaData();
            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                data.put(i, res.getString(i));
            }
        }return data;
    }
//public static void getData(String sql) throws SQLException{
//        try (Connection con = DriverManager.getConnection(url, name, password);
//                Statement statement=con.createStatement()){
//         res= statement.executeQuery(sql);
//            res.next();
//        }
//
//
//}



}


