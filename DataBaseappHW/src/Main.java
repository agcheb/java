import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;

/**
 * Created by Александр on 20.07.2017.
 */
public class Main {
    private static Connection connection;
    private static Statement stmt;
    private static PreparedStatement ps;

     private static final String exit  = "/выход";
     private static final String newprice  = "/сменитьцену";
     private static final String productlist  = "/товарыпоцене";
     private static final String getcost  = "/цена";



    public static void main(String[] args) throws Exception{

        connect();

        stmt = connection.createStatement();

        stmt.execute("CREATE TABLE IF NOT EXISTS Products (\n" +
                "    id    INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    prodid INTEGER,\n" +
                "    title TEXT,\n" +
                "    cost INTEGER);");

        stmt.execute("DELETE FROM Products");

        connection.setAutoCommit(false);
        ps = connection.prepareStatement("INSERT INTO Products (prodid, title, cost) VALUES (?, ?, ?)");
        for (int i = 1; i <= 10000 ; i++) {
            ps.setInt(1, i);
            ps.setString(2, "товар"+i);
            ps.setInt(3, i*10);
            ps.addBatch();
        }
        ps.executeBatch();
        connection.commit();


        ResultSet rs = stmt.executeQuery("SELECT * FROM Products WHERE cost < 400");


        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Добрый день. доступные команды для работы с базой:");
        System.out.println("/цена имя_товара  - выдает стоимость товара, если такой есть");
        System.out.println("/сменитьцену имя_товара новая цена - меняет цену заданного товара на новую");
        System.out.println("/товарыпоцене min max - выдает товары в заданном диапазоне");
        System.out.println("/выход - выйти из приложения");
        while (true){

            String[] cmds = reader.readLine().split(" ");
            if (cmds[0].equals(exit)) break;

            if (cmds[0].equals(getcost)){
                if(cmds.length!=2) System.out.println("Некорректно задана команда, попробуйте еще раз");
                else {
                    ps = connection.prepareStatement("SELECT cost FROM Products WHERE title = ?");
                    ps.setString(1, cmds[1]);
                    ResultSet rs1 = ps.executeQuery();

                    int i =0;
                    while (rs1.next()) {
                        i++;
                        System.out.println("цена товара: "+cmds[1] +":  "+rs1.getInt("cost"));
                    }
                    if(i==0) System.out.println("Такого товара нет");
                }
            }
            else if(cmds[0].equals(productlist)){
                if(cmds.length!=3) System.out.println("Некорректно задана команда, попробуйте еще раз");
                else {
                    ps = connection.prepareStatement("SELECT * FROM Products WHERE cost BETWEEN ? AND +?");
                    ps.setInt(1,Integer.parseInt(cmds[1]));
                    ps.setInt(2,Integer.parseInt(cmds[2]));
                    ResultSet rs2 = ps.executeQuery();
                    int i =0;
                    while (rs2.next()) {
                        i++;
                        System.out.println("Product: "+rs2.getInt("prodid") +
                                " "+rs2.getString("title") +
                                " "+rs2.getInt("cost"));
                    }
                    if(i==0) System.out.println("В таком диапазоне цен товаров не нашлось.");
                }
            }
            else if(cmds[0].equals(newprice)){
                if(cmds.length!=3) System.out.println("Некорректно задана команда, попробуйте еще раз");
                else {
                    ps = connection.prepareStatement("UPDATE Products SET cost = ? WHERE title = ?");
                    ps.setInt(1,Integer.parseInt(cmds[2]));
                    ps.setString(2,cmds[1]);
                    int cnt = ps.executeUpdate();
                    System.out.println("Поменялась цена в "+cnt+" записях");
                }
            }
            else {
                System.out.println("Неизвестная команда. Попробуйте еще раз.");
            }


        }

        disconnect();

    }

    public static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection("jdbc:sqlite:Products.db");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
