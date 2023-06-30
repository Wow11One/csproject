package controlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import entity.db.Group;
import entity.db.Product;
import server.Server;
import tools.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class GroupPostGetController  implements HttpHandler {
    public final static String POST_GET_PATH = "/api/group";

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            OutputStream os = httpExchange.getResponseBody();
            if(httpExchange.getRequestMethod().equalsIgnoreCase("options"))
            {
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "*");
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
                httpExchange.sendResponseHeaders(200,0);
                os.close();
            }
            else if(httpExchange.getRequestMethod().equalsIgnoreCase("get"))
            {
                String allProductsJson = Server.dbGroup.getAllGroupsJSON();
                byte[] jsonBytes = allProductsJson.getBytes();
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                httpExchange.sendResponseHeaders(200, jsonBytes.length);
                os.write(jsonBytes);
                os.close();
            }
            else if(httpExchange.getRequestMethod().equalsIgnoreCase("post"))
            {
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "*");
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));
                String line = null;
                String json = "";
                while (true){
                    line = reader.readLine();
                    if(line == null)
                        break;
                    json += line;
                }
                Group group = JSONParser.JSONToGroup(json);
                int addGroup = Server.dbGroup.insertGroup(group);
                if(addGroup == 1){
                    httpExchange.sendResponseHeaders(200, 0);
                    os.close();
                } else if (addGroup == -1) {
                    httpExchange.sendResponseHeaders(400, 0);
                    os.close();
                }
                else {
                    httpExchange.sendResponseHeaders(404, 0);
                    os.close();
                }

            }
            else {
                httpExchange.sendResponseHeaders(400, 0);
                os.close();
            }

        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }
}
