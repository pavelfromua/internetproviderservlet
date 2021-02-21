package my.project.internetprovider.web.command;

import my.project.internetprovider.exception.DataProcessingException;
import my.project.internetprovider.service.ProductService;
import my.project.internetprovider.service.impl.ProductServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteProductCommand implements Command {
    private ProductService productService = new ProductServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Long productId = Long.valueOf(request.getParameter("productId"));
        try {
            productService.delete(productId);
        } catch (DataProcessingException e) {
            return "redirect:/admin/products?msg="+e.getMessage();
        }

        return "redirect:/admin/products";
    }
}
