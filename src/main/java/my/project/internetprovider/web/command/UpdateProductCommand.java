package my.project.internetprovider.web.command;

import my.project.internetprovider.db.entity.Product;
import my.project.internetprovider.service.ProductService;
import my.project.internetprovider.service.impl.ProductServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateProductCommand implements Command {
    private ProductService productService = new ProductServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Long productId = Long.valueOf(request.getParameter("productId"));
        String name = request.getParameter("name");

        Product product = Product.newBuilder()
                .setId(productId)
                .setName(name)
                .build();

        try {
            productService.update(product);

            return "redirect:/admin/products";
        } catch (Exception e) {
            request.setAttribute("message", e.getMessage());
            request.setAttribute("product", product);

            return "/WEB-INF/views/admin/products/edit.jsp";
        }
    }
}
