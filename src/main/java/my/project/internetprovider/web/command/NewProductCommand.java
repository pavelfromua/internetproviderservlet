package my.project.internetprovider.web.command;

import my.project.internetprovider.db.entity.Product;
import my.project.internetprovider.db.entity.User;
import my.project.internetprovider.exception.RegistrationException;
import my.project.internetprovider.exception.ValidationException;
import my.project.internetprovider.service.ProductService;
import my.project.internetprovider.service.impl.ProductServiceImpl;
import my.project.internetprovider.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;

public class NewProductCommand implements Command {
    private ProductService productService = new ProductServiceImpl();

    @Override
    public String execute(HttpServletRequest request) {
        String name = request.getParameter("name");

        try {
            productService.create(Product.newBuilder()
                    .setName(name)
                    .build());

            return "redirect:/admin/products";
        } catch (ValidationException e) {
            request.setAttribute("message", e.getMessage());

            return "/WEB-INF/views/admin/products/new.jsp";
        }
    }
}
