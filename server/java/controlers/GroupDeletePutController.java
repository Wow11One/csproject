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

public class GroupDeletePutController implements HttpHandler {
    public final static String PUT_DELETE_PATH = "/api/group/";
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        OutputStream os = httpExchange.getResponseBody();
        try {
            if(httpExchange.getRequestMethod().equalsIgnoreCase("options"))
            {
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "*");
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
                httpExchange.sendResponseHeaders(200,0);
                os.close();
            }
            else if(httpExchange.getRequestMethod().equalsIgnoreCase("delete"))
            {
                int id = Integer.parseInt(httpExchange.getRequestURI().getPath().substring(PUT_DELETE_PATH.length()));
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                Server.dbGroup.deleteGroup(id);
                httpExchange.sendResponseHeaders(204, 0);
                os.close();
            }
            else if(httpExchange.getRequestMethod().equalsIgnoreCase("get"))
            {
                int id = Integer.parseInt(httpExchange.getRequestURI().getPath().substring(PUT_DELETE_PATH.length()));
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                Group group = Server.dbGroup.getGroup(id);
                String json = JSONParser.groupToJSON(group);
                byte[] jsonBytes = json.getBytes();
                httpExchange.sendResponseHeaders(200, jsonBytes.length);
                System.out.println(json);
                os.write(jsonBytes);
                os.close();
            }
            else if(httpExchange.getRequestMethod().equalsIgnoreCase("put")){
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
                System.out.println(json);
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                int put = Server.dbGroup.updateGroup(group);
                if(put == 1)
                    httpExchange.sendResponseHeaders(200, 0);
                else if(put == 0)
                    httpExchange.sendResponseHeaders(404, 0);
                else
                    httpExchange.sendResponseHeaders(400, 0);
                os.close();
            }
        }
        catch (Exception ex)
        {
            httpExchange.sendResponseHeaders(400, 0);
            os.close();
            ex.printStackTrace();
        }

    }
}
