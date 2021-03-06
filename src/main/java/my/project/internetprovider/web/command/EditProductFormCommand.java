package my.project.internetprovider.web.command;

import my.project.internetprovider.db.entity.Product;
import my.project.internetprovider.exception.CheckException;
import my.project.internetprovider.service.ProductService;
import my.project.internetprovider.service.impl.ProductServiceImpl;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditProductFormCommand implements Command {
    private ProductService productService = new ProductServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Long productId = Long.valueOf(request.getParameter("id"));
        try {
            Product product = productService.findById(productId);
            request.setAttribute("product", product);

            return "/WEB-INF/views/admin/products/edit.jsp";
        } catch (CheckException e) {
            request.setAttribute("message", e.getMessage());
            return "redirect:/admin/products";
        }
    }
}
