package entity.db;

import java.sql.*;

public class DBUser {
    private final Connection database;
    public DBUser(DBConnection dbConnection)
    {
        database = dbConnection.getDatabase();
        createTable();
    }

    private void createTable() {
        try{
            Statement statement = database.createStatement();
            String query = "create table if not exists 'users' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'login' text not null, 'password' text not null, 'role' text not null, 'username' text not null);";
            statement.execute(query);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createUser(User user)
    {
        try {
            String query = "insert into 'users' ('id', 'login', 'password', 'role', 'username') values (?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = database.prepareStatement(query);
            preparedStatement.setInt(1,user.getId());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getRole());
            preparedStatement.setString(5, user.getName());
            preparedStatement.execute();
            preparedStatement.close();
        }
        catch (Exception ex)
        {
         throw new RuntimeException(ex);
        }
    }

    public User getUser(int id) {
        try {
            String query = "select * from 'users' where id = ?";
            PreparedStatement preparedStatement = database.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            User resultUser = new User(resultSet.getString("password"), resultSet.getString("login"), resultSet.getString("role"), resultSet.getInt("id"), resultSet.getString("username"));
            resultSet.close();
            preparedStatement.close();
            return resultUser;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    public void updateUser(User user)
    {
        try {
            String query = "update 'users' set id = ? login = ? password = ? role = ? username = ? where id = ?;";
            PreparedStatement preparedStatement = database.prepareStatement(query);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getRole());
            preparedStatement.setString(5, user.getName());
            preparedStatement.setInt(6, user.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }
    public void deleteUser(int id)
    {
        try {
            String query = "delete 'users' where id = ?;";
            PreparedStatement preparedStatement = database.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            preparedStatement.close();
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public User getUserByLogin(String login)
    {
        try {
            String query = "select * from 'users' where login = ?";
            PreparedStatement preparedStatement = database.prepareStatement(query);
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();

            User resultUser;
            if (resultSet.next())
                resultUser = new User(resultSet.getString("password"), resultSet.getString("login"), resultSet.getString("role"), resultSet.getInt("id"), resultSet.getString("username"));
            else
                resultUser = null;
            resultSet.close();
            preparedStatement.close();
            return resultUser;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }
}
