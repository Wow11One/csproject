package entity.db;

import org.json.JSONArray;
import org.json.JSONObject;
import tools.JSONParser;

import java.sql.*;
import java.util.ArrayList;

public class DBGroup {
    private final Connection database;
    public DBGroup(DBConnection dbConnection)
    {
        database = dbConnection.getDatabase();
        createTable();
    }

    private void createTable() {
        try{
            Statement statement = database.createStatement();
            String query = "create table if not exists 'groups' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'groupName' text not null, 'description' text not null);";
            statement.execute(query);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Group getGroup(int id){
        try
        {
            Statement statement = database.createStatement();
            String query = "select * from 'groups' where id = " + id;
            ResultSet resultSet = statement.executeQuery(query);
            Group res = new Group(resultSet.getInt("id"), resultSet.getString("groupName"), resultSet.getString("description"));
            resultSet.close();
            statement.close();
            return res;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
    public boolean isUnique(String groupName, int id)
    {
        try
        {
            Statement statement = database.createStatement();
            String query = "select count(*) as result from 'groups'  where groupName = '" + groupName +"' and id != " + id;
            ResultSet resultSet = statement.executeQuery(query);
            int amount = resultSet.getInt("result");
            resultSet.close();
            statement.close();
            return amount == 0;
        }
        catch (SQLException exception)
        {
            throw new RuntimeException(exception);
        }
    }
    public int getProductsAmount(int id)
    {
        try
        {
            Statement statement = database.createStatement();
            String query = "select sum(amount) as res from 'products' where group_id = " + id;
            ResultSet resultSet = statement.executeQuery(query);
            int res = resultSet.getInt("res");
            resultSet.close();
            statement.close();
            return res;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
    public int getProductsPrice(int id)
    {
        try
        {
            Statement statement = database.createStatement();
            String query = "select amount,price  from 'products' where group_id = " + id;
            ResultSet resultSet = statement.executeQuery(query);
            int sum = 0;
            while (resultSet.next()){
                sum += resultSet.getInt("amount") * resultSet.getDouble("price");

            }
            resultSet.close();
            statement.close();
            return sum;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public String getAllGroupsJSON()
    {
        try
        {
            Statement statement = database.createStatement();
            String query = "select * from 'groups' ";
            ResultSet resultSet = statement.executeQuery(query);
            JSONArray array = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            Group group = null;
            ArrayList<JSONObject> objects = new ArrayList<>();
            while (resultSet.next())
            {
                group = new Group(resultSet.getInt("id"), resultSet.getString("groupName"),resultSet.getString("description"));
                jsonObject = new JSONObject(JSONParser.groupToJSON(group));
                jsonObject.put("amount", getProductsAmount(resultSet.getInt("id")));
                jsonObject.put("price", getProductsPrice(resultSet.getInt("id")));
                array.put(jsonObject);
            }
            resultSet.close();
            statement.close();
            return array.toString();
        }
        catch (Exception exception)
        {
            throw new RuntimeException(exception);
        }
    }


    public int deleteGroup(int id){

        try {
            String query1 = "delete from 'products' where group_id = " + id;
            Statement statement1 = database.createStatement();
            statement1.execute(query1);
            statement1.close();

            Statement statement = database.createStatement();
            String query = "delete from 'groups' where id = " + id;
            statement.execute(query);
            statement.close();

            return id;
        } catch (SQLException exception) {
            return -1;
        }
    }

    public  int insertGroup(Group group){
        if(isUnique(group.getName(), group.getId())){
            String query = "insert into 'groups' ('groupName',  'description') values (?, ?);";
            try
            {
                PreparedStatement insertStatement = database.prepareStatement(query);
                insertStatement.setString(1, group.getName());
                insertStatement.setString(2, group.getDescription());
                insertStatement.execute();
                insertStatement.close();
                return 1;
            } catch (SQLException exception) {
                return 0;
            }
        }
            return -1;
    }
    public boolean includes(Group group)
    {
        try
        {
            Statement statement = database.createStatement();
            String query = "select count(*) as result from 'groups' where id = " + group.getId();
            ResultSet set = statement.executeQuery(query);
            int res = set.getInt("result");
            set.close();
            statement.close();
            return res != 0;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
    public int updateGroup(Group group){
            if(isUnique(group.getName(), group.getId()))
                try  {
                 PreparedStatement preparedStatement = database.prepareStatement("update 'groups' set groupName = ?, description = ?  where id = ?");
                preparedStatement.setString(1, group.getName());
                preparedStatement.setString(2, group.getDescription());
                preparedStatement.setInt(3, group.getId());
                preparedStatement.executeUpdate();
                preparedStatement.close();
                return 1;
            } catch (SQLException exception) {
                return 0;
            }
            return -1;
    }
    public void deleteGroupTable(){
        try{
            Statement statement = database.createStatement();
            String query = "drop table 'groups'";
            statement.execute(query);
            statement.close();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }





}
