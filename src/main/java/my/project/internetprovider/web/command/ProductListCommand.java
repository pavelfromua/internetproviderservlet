package my.project.internetprovider.web.command;

import my.project.internetprovider.db.entity.Product;
import my.project.internetprovider.service.ProductService;
import my.project.internetprovider.service.impl.ProductServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ProductListCommand implements Command {
    private ProductService productService = new ProductServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        List<Product> productList = productService.findAll();
        request.setAttribute("productList", productList);
        request.setAttribute("message", request.getParameter("msg"));

        return "/WEB-INF/views/admin/products/list.jsp";
    }
}
