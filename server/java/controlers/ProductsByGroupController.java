package controlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.Server;

import java.io.IOException;
import java.io.OutputStream;

public class ProductsByGroupController implements HttpHandler {
    public static final String PATH = "/api/products/groups/";
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
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
            int id = Integer.parseInt(httpExchange.getRequestURI().getPath().substring(PATH.length()));
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String json = Server.dbProduct.getProductsByGroupJson(id);
            byte[] jsonBytes = json.getBytes();
            httpExchange.sendResponseHeaders(200, jsonBytes.length);
            os.write(jsonBytes);
            os.close();
        }
    }
}
