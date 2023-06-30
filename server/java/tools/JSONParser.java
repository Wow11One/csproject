package tools;

import entity.db.Group;
import entity.db.Product;
import org.json.JSONObject;

public class JSONParser {

    public static String productToJSON(Product product)
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", product.getName());
        jsonObject.put("description", product.getDescription());
        jsonObject.put("producer", product.getProducer());
        jsonObject.put("price", product.getPrice());
        jsonObject.put("amount", product.getAmount());
        jsonObject.put("groupId", product.getGroupId());
        jsonObject.put("id",product.getId());
        return jsonObject.toString();
    }

    public static String groupToJSON(Group group)
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", group.getName());
        jsonObject.put("description", group.getDescription());
        jsonObject.put("id", group.getId());
        return jsonObject.toString();
    }
    public static Group JSONToGroup(String JSON)
    {
        JSONObject jsonObject = new JSONObject(JSON);
        String name = (String) jsonObject.get("name");
        String description = (String) jsonObject.get("description");
        int id = (int) jsonObject.get("id");
        return new Group(id, name, description);
    }

    public static Product JSONToProduct(String JSON)
    {
        JSONObject jsonObject = new JSONObject(JSON);
        String name = (String) jsonObject.get("name");
        String description = (String) jsonObject.get("description");
        String producer = (String) jsonObject.get("producer");
        int price = (int) jsonObject.get("price");
        int amount = (int) jsonObject.get("amount");
        int groupId = (int) jsonObject.get("groupId");
        int id = (int) jsonObject.get("id");
        return new Product(name, description, producer, (double)price, amount, groupId, id);
    }

    public static String idToJSON(int id)
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        return jsonObject.toString();
    }

    public static int  JSONToId(String JSON)
    {
        JSONObject jsonObject = new JSONObject(JSON);
        return (int) jsonObject.get("id");
    }

}
