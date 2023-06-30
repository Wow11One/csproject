package servlets;

import entity.db.DBConnection;
import entity.db.DBProduct;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/good")
public class ProductServlet extends HttpServlet {
    DBConnection dbConnection = new DBConnection("ShopDB");
    DBProduct dbProduct = new DBProduct(dbConnection);

    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "*");
        resp.addHeader("Access-Control-Allow-Headers", "*");
        resp.setStatus(200);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            PrintWriter writer = resp.getWriter();
            System.out.println(dbProduct.getAllProductsJson());
            writer.print(dbProduct.getAllProductsJson());
            writer.close();
            resp.addHeader("Access-Control-Allow-Origin", "*");
            resp.setStatus(200);
        }catch (Exception exception)
        {
            exception.printStackTrace();
        }

    }
}
