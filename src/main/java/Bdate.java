import java.sql.*;

public class Bdate{

    String url = "jdbc:mysql://us-cdbr-iron-east-01.cleardb.net:3306/heroku_839483b01dcbc34?useUnicode=true&characterEncoding=UTF-8";
    String name="b3fd7df0612459";
    String password="7ee5e40e1";
    Connection con;

    {
        try {
            con = DriverManager.getConnection(url,name,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ResultSet res;


    public void bd(String sql) throws SQLException {





        con.createStatement().execute(sql);








    }
    public  void bs(String sql)throws SQLException{



        res=con.createStatement().executeQuery(sql);
        res.next();








    }

}
