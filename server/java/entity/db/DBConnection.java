package entity.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private final Connection database;
    public DBConnection(String dbPath)
    {

        try{
            Class.forName("org.sqlite.JDBC");
            this.database = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        } catch (ClassNotFoundException exception) {
            System.out.println("Can't load SQLite JDBC class");
            throw new RuntimeException(exception);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public Connection getDatabase()
    {
        return database;
    }
}
