import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
public class Main {
    static Scanner sc = new Scanner(System.in);
    static HashMap<String,Integer> mpp  = new HashMap<>();
    static int total = 0;
    static void Menu() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("\t\t Welcome to Budget Mart");
        System.out.println();
        System.out.println("1. Groceries");
        System.out.println("2. Fashion");
        System.out.println("3. Beauty");
        System.out.println("0. Exit");
        System.out.println();
    }

    static void choice() {
        System.out.println("1.Search");
        System.out.println("2.Show");
        System.out.println("3.Billing");
        System.out.println("0.Exit");
    }

    static void finalChoice()
    {
        System.out.println("1.Buy\n0.Exit");
    }
    static void Search(String table)
    {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "Gagan@20");

            String MyName  = sc.nextLine();
            System.out.print("Enter Product :=");
            String product  = sc.nextLine();
            String query = "SELECT * FROM " + table + " WHERE name = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, product);
            ResultSet resultSet = statement.executeQuery();
            if(!resultSet.next()) {
                System.out.println("No result found!");
                return ;
            }
            else{
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    int price = resultSet.getInt("price");
                    float rating = resultSet.getFloat("rating");
                    mpp.put(name, price);
                    total += price;
                    System.out.println();
                    System.out.println("name: " + name);
                    System.out.println("price: " + price);
                    System.out.println("rating: " + rating);
                    System.out.println();
                }
                finalChoice();
            }
            int x = sc.nextInt();
            switch (x){
                case 1 -> System.out.println("Item Added to cart");
                case 0 -> System.out.println("Not Added!");
                default -> System.out.println("Invalid choice");
            }
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    static  void Show(String table) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "Gagan@20");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name,price,rating FROM "+table);
            ResultSetMetaData ResultSetMetaData = rs.getMetaData();
            int numColumns = ResultSetMetaData.getColumnCount();
            int[] columnWidths = new int[numColumns];
            for (int i = 1; i <= numColumns; i++)
                columnWidths[i-1] = Math.max(ResultSetMetaData.getColumnName(i).length(), ResultSetMetaData.getColumnDisplaySize(i));
            for (int i = 1; i <= numColumns; i++)
                System.out.printf("%-" + columnWidths[i-1] + "s", ResultSetMetaData.getColumnName(i));
            System.out.println();
            for (int i = 0; i < numColumns; i++){
                for (int j = 0; j < columnWidths[i]; j++)
                    System.out.print("-");
                System.out.print("  ");
            }
            System.out.println();
            while (rs.next()) {
                for (int i = 1; i <= numColumns; i++)
                    System.out.printf("%-" + columnWidths[i-1] + "s", rs.getString(i));
                System.out.println();
            }
                System.out.println();
        }catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    static void SwitchCase(String table)
    {
        choice();
        boolean GoBack = false;
        do {
            int choice = sc.nextInt();
            switch (choice) {
                case 0 -> GoBack = true;
                case 1 -> {
                    Search(table);
                    choice();
                }
                case 2 -> {
                    Show(table);
                    choice();
                }
                case 3 -> {
                    buy();
                    GoBack = true;
                }
                default -> {
                    System.out.println("Invalid Choice");
                    choice();
                }
            }
        } while (!GoBack);
    }
    static void buy(){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("myFile.txt"));
            writer.write("Product\t\t"+"Price\t\t\n");
            System.out.println();
            for (Map.Entry<String, Integer> entry : mpp.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();
                writer.write(key + "\t\t" + value);
                writer.newLine();
            }
            writer.write("Total :- "+total);
            total = 0;
            mpp.clear();
            writer.close();
            System.out.println("Thanks for shopping");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Menu();
            boolean GoBack = false;
            do {
                int choice = sc.nextInt();
                switch (choice) {
                    case 0 -> GoBack = true;
                    case 1 -> {
                        SwitchCase("grocery");
                        Menu();
                    }
                    case 2 -> {
                        SwitchCase("fashion");
                        Menu();
                    }
                    case 3 -> {
                        SwitchCase("beauty");
                        Menu();
                    }
                    default -> {
                        System.out.println("Invalid choice");
                        Menu();
                    }
                }
            } while (!GoBack);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}