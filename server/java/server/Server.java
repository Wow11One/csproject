package server;

import com.sun.net.httpserver.*;
import controlers.*;
import entity.db.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import tools.JSONParser;
import tools.JWTHandler;
import tools.QueryParamsParser;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import org.apache.commons.codec.digest.DigestUtils;

public class Server {
   public static DBUser dbUser;
   public static DBProduct dbProduct;
   public static DBGroup dbGroup;
   private final static String AUTHORIZATION = "AUTHORIZATION";


    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8765), 0);
        HttpContext getPostProductsContext = server.createContext(ProductController.POST_GET_PATH, new ProductController());
        HttpContext putDeleteProductsContext = server.createContext(ProductDeletePutController.PUT_DELETE_PATH, new ProductDeletePutController());
        HttpContext postGetGroupContext = server.createContext(GroupPostGetController.POST_GET_PATH, new GroupPostGetController());
        HttpContext putDeleteGroupContext = server.createContext(GroupDeletePutController.PUT_DELETE_PATH, new GroupDeletePutController());
        HttpContext productsByGroupContext = server.createContext(ProductsByGroupController.PATH, new ProductsByGroupController());

        DBConnection dbConnection = new DBConnection("ShopDB");
        dbUser = new DBUser(dbConnection);
        dbGroup = new DBGroup(dbConnection);
        dbProduct = new DBProduct(dbConnection);
        //dbProduct.insertProduct(new Product("хліб", "смачний хліб", "ЖитомирХлібЗавод", 22,23,2,1));
        //dbProduct.insertProduct(new Product("молоко", "смачне молоко", "рудь", 25,19,3,2));
        //dbProduct.insertProduct(new Product("пиво", "смачне пиво", "бердичівський завод пива", 342,119,4,3));
        //dbProduct.insertProduct(new Product("морозиво", "смачне морозиво", "рудь", 125,911,2,4));
        //dbGroup.insertGroup(new Group(2, "Кисломолочні вироби", "найкращі вироби з молока"));
        //dbGroup.insertGroup(new Group(4, "Алкогольні вироби", "найкращі алкогольні вироби"));

        dbProduct.getAllProductsJson();
        server.setExecutor(null);
        server.start();
    }

    static class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            OutputStream responseBody = exchange.getResponseBody();
            if(!exchange.getRequestMethod().equalsIgnoreCase("post"))
            {
                exchange.sendResponseHeaders(400,0);
                responseBody.close();
                return;
            }
            try {
                QueryParamsParser queryParamsParser = new QueryParamsParser(exchange.getRequestURI().getQuery());

                User user = dbUser.getUserByLogin(queryParamsParser.getLoginValue());
                if(user == null){
                    exchange.sendResponseHeaders(401,0);
                    responseBody.close();
                }
                else{
                    System.out.println(DigestUtils.md5Hex(queryParamsParser.getPasswordValue()));
                    if(user.getPassword().equals(DigestUtils.md5Hex(queryParamsParser.getPasswordValue())))
                    {
                        String jwtToken = new JWTHandler().generateToken(user);
                        exchange.getResponseHeaders().add(AUTHORIZATION, jwtToken);
                        exchange.sendResponseHeaders(200,0);
                        responseBody.close();
                    }
                    else {
                        exchange.sendResponseHeaders(401,0);
                        responseBody.close();
                    }
                }
            }
            catch (Exception exception){
                exception.printStackTrace();
            }
        }
    }

   // static class GoodsHandler implements HttpHandler {
   //     @Override
   //     public void handle(HttpExchange exchange) throws IOException {
   //         String method = exchange.getRequestMethod().toLowerCase();
   //         OutputStream os = exchange.getResponseBody();
   //         InputStream is = exchange.getRequestBody();
   //         BufferedReader br = new BufferedReader(new InputStreamReader(is));
   //         try {
   //             int id = Integer.parseInt(exchange.getRequestURI().toString().substring(API_PATH.length()));
   //             switch (method)
   //             {
   //                 case "get":
   //                     Product product = dbProduct.getProduct(id);
   //                     String productToJson = JSONParser.productToJSON(product);
   //                     exchange.sendResponseHeaders(200, productToJson.getBytes().length);
   //                     os.write(productToJson.getBytes());
   //                     os.close();
   //                     break;
   //                 case "put":
   //                     product = JSONParser.JSONToProduct(getJson(br));
   //                     dbProduct.updateProduct(product);
   //                     exchange.sendResponseHeaders(204, -1);
   //                     os.close();
   //                     break;
   //                 case "delete":
   //                     dbProduct.deleteProduct(id);
   //                     exchange.sendResponseHeaders(204,-1);
   //                     break;
   //                 default:
   //                     exchange.sendResponseHeaders(400,0);
   //                     os.close();
   //             }
   //         }
   //         catch (Exception ex)
   //         {
   //             exchange.sendResponseHeaders(400,0);
   //             os.close();
   //             ex.printStackTrace();
   //         }
//
   //     }
   //     private String getJson(BufferedReader br) throws IOException {
   //         StringBuilder json = new StringBuilder();
   //         String line = null;
   //         while (true)
   //         {
   //             line = br.readLine();
   //             if (line == null)
   //                 break;
   //             json.append(line);
   //         }
   //         return json.toString();
   //     }
//
//
   // }


    static class GoodsPostHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod().toLowerCase();
            OutputStream os = exchange.getResponseBody();
            InputStream is = exchange.getRequestBody();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            try {
            if(!method.equalsIgnoreCase("post"))
            {
                exchange.sendResponseHeaders(404,0);
                os.close();
                return;
            }
                Product product = JSONParser.JSONToProduct(getJson(br));
                dbProduct.insertProduct(product);
                exchange.sendResponseHeaders(201, 0);
                os.close();
            }
            catch (Exception ex)
            {
                exchange.sendResponseHeaders(400,0);
                os.close();
                ex.printStackTrace();
            }

        }
        private String getJson(BufferedReader br) throws IOException {
            StringBuilder json = new StringBuilder();
            String line = null;
            while (true)
            {
                line = br.readLine();
                if (line == null)
                    break;
                json.append(line);
            }
            return json.toString();
        }


    }


    static class Auth extends Authenticator {
        @Override
        public Result authenticate(HttpExchange httpExchange) {
            String jwtToken = httpExchange.getRequestHeaders().getFirst(AUTHORIZATION);
            if(jwtToken == null)
                return new Failure(403);
            try {
                String login = new JWTHandler().getLoginFromJwt(jwtToken);
                User user = dbUser.getUserByLogin(login);
                if(user == null)
                    return new Failure(403);
                return new Success(new HttpPrincipal("username", user.getLogin()));
            }
            catch (Exception ex)
            {
            return new Failure(403);
            }

        }
    }
}