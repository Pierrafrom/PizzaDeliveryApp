package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BDD {
    public static void main(String[] args) {
        // Informations de connexion
        String url = "jdbc:mariadb://192.168.1.43:40000/PIZZERIA";
        String username = "remi";
        String password = "kaaris";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String selectQuery = "SELECT * FROM PIZZA";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                System.out.println("Pizza Table:");
                System.out.printf("%-10s %-30s %-15s %-10s%n", "pizzaId", "pizzaName", "pizzaPrice", "spotlight");

                while (resultSet.next()) {
                    int pizzaId = resultSet.getInt("pizzaId");
                    String pizzaName = resultSet.getString("pizzaName");
                    double pizzaPrice = resultSet.getDouble("pizzaPrice");
                    boolean spotlight = resultSet.getBoolean("spotlight");

                    System.out.printf("%-10s %-30s %-15.2f %-10s%n", pizzaId, pizzaName, pizzaPrice, spotlight);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
