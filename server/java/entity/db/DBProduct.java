package entity.db;

import org.json.JSONArray;
import org.json.JSONObject;
import tools.JSONParser;

import java.sql.*;
import java.util.ArrayList;


public class DBProduct {
    private final Connection database;
    public DBProduct(DBConnection dbConnection)
    {
        database = dbConnection.getDatabase();
        createTable();
    }
    private void createTable()
    {
        try{
            Statement statement = database.createStatement();
            String query = "create table if not exists 'products' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'productName' text not null, 'price' decimal not null," +
                    " 'amount' integer not null, 'description' text not null, 'producer' text not null, " +
                    "'group_id' integer not null, foreign key(group_id) references groups(id) ON UPDATE CASCADE ON DELETE CASCADE);";
            statement.execute(query);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteProductTable(){
        try{
            Statement statement = database.createStatement();
            String query = "drop table 'products'";
            statement.execute(query);
            statement.close();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public Product getProduct(int id){
        try
        {
            Statement statement = database.createStatement();
            String query = "select * from 'products' where id = " + id;
            ResultSet resultSet = statement.executeQuery(query);
            Product res = new Product(resultSet.getString("productName"), resultSet.getString("description"),
                    resultSet.getString("producer"), resultSet.getDouble("price"), resultSet.getInt("amount"), resultSet.getInt("group_id"), resultSet.getInt("id"));
            resultSet.close();
            statement.close();
            return res;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public int deleteProduct(int id){

        try {
            Statement statement = database.createStatement();
            String query = "delete from 'products' where id = " + id;
            statement.execute(query);
            statement.close();
            return id;
        } catch (SQLException exception) {
            return -1;
        }
    }
    public int getProductAmount(int id)
    {
        try
        {
            Statement statement = database.createStatement();
            String query = "select amount from 'products' where id = " + id;
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.close();
            statement.close();
            return resultSet.getInt("amount");
        }
        catch (SQLException exception)
        {
            throw new RuntimeException(exception);
        }

    }
    public String getAllProductsJson()
    {
        try
        {
            Statement statement = database.createStatement();
            String query = "select * from 'products' as P inner join 'groups' as G on P.group_id = G.id";
            ResultSet resultSet = statement.executeQuery(query);
            JSONArray array = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            Product product = null;
            ArrayList<JSONObject> objects = new ArrayList<>();

            while (resultSet.next())
            {
                product = new Product(resultSet.getString("productName"), resultSet.getString("description"), resultSet.getString("producer"), resultSet.getDouble("price"), resultSet.getInt("amount"), resultSet.getInt("group_id"), resultSet.getInt("id"));
                jsonObject = new JSONObject(JSONParser.productToJSON(product));
                jsonObject.put("groupName", resultSet.getString("groupName"));
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

    public String getProductsByGroupJson(int groupId)
    {
        try
        {
            Statement statement = database.createStatement();
            String query = "select * from 'products' as P inner join 'groups' as G on P.group_id = G.id where group_id = "+groupId;
            ResultSet resultSet = statement.executeQuery(query);
            JSONArray array = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            Product product = null;
            ArrayList<JSONObject> objects = new ArrayList<>();

            while (resultSet.next())
            {
                product = new Product(resultSet.getString("productName"), resultSet.getString("description"), resultSet.getString("producer"), resultSet.getDouble("price"), resultSet.getInt("amount"), resultSet.getInt("group_id"), resultSet.getInt("id"));
                jsonObject = new JSONObject(JSONParser.productToJSON(product));
                jsonObject.put("groupName", resultSet.getString("groupName"));
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
    public boolean includes(Product product)
    {
        try
        {
            Statement statement = database.createStatement();
            String query = "select count(*) as result from 'products' where id = " + product.getId();
            ResultSet set = statement.executeQuery(query);
            int res = set.getInt("result");
            set.close();
            statement.close();
            return res != 0;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
    public void setAmountById(int id, int amount)
    {
        try {
            Product product = getProduct(id);
            product.setAmount(amount);
            updateProduct(product);
        }
        catch (Exception exception)
        {
            throw new RuntimeException(exception);
        }
    }

    public  int insertProduct(Product product){
        if(isUnique(product.getName(), product.getId())){
            String query = "insert into 'products' ('productName', 'price', 'amount', 'description', 'producer', 'group_id') values (?, ?, ?, ?, ?, ? );";
            try
            {
                PreparedStatement insertStatement = database.prepareStatement(query);
                insertStatement.setString(1, product.getName());
                insertStatement.setDouble(2, product.getPrice());
                insertStatement.setInt(3, product.getAmount());
                insertStatement.setString(4, product.getDescription());
                insertStatement.setString(5, product.getProducer());
                insertStatement.setInt(6, product.getGroupId());
                insertStatement.execute();
                insertStatement.close();
                return 1;
            } catch (SQLException exception) {
                exception.printStackTrace();
                return 0;
            }
        }
        return -1;
    }
    public boolean isUnique(String productName, int id)
    {
        try
        {
            Statement statement = database.createStatement();
            String query = "select count(*) as result from 'products'  where productName = '" + productName +"' and id != " + id;
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


   public int updateProduct(Product product){
        if(isUnique(product.getName(),product.getId())){
           try  {
               String query = "update 'products' set productName = ?, price = ?, amount = ?, description = ?, producer = ?, group_id = ?, id = ?  where id = ?";
               PreparedStatement preparedStatement = database.prepareStatement(query);
               preparedStatement.setString(1, product.getName());
               preparedStatement.setDouble(2, product.getPrice());
               preparedStatement.setDouble(3, product.getAmount());
               preparedStatement.setString(4, product.getDescription());
               preparedStatement.setString(5, product.getProducer());
               preparedStatement.setInt(6, product.getGroupId());
               preparedStatement.setInt(7, product.getId());
               preparedStatement.setInt(8, product.getId());
               preparedStatement.executeUpdate();
               preparedStatement.close();
               return 1;
           } catch (SQLException exception) {
               exception.printStackTrace();
               return 0;
           }
        }
        return -1;
   }
    //list by criteria
   public ArrayList<Double> productsCheaperThan(int number)
   {
       try
       {
           Statement statement = database.createStatement();
           String query = "select price from 'products'  where price <= " + number;
           ResultSet resultSet = statement.executeQuery(query);
           ArrayList<Double> productsPrice = new ArrayList<>();
           while(resultSet.next())
               productsPrice.add(resultSet.getDouble("price"));
           resultSet.close();
           statement.close();
           return productsPrice;
       }
       catch (SQLException exception)
       {
           throw new RuntimeException(exception);
       }

   }



}
