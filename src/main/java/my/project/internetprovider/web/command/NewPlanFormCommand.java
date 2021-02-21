package my.project.internetprovider.web.command;

import my.project.internetprovider.service.ProductService;
import my.project.internetprovider.service.impl.ProductServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NewPlanFormCommand implements Command {
    private ProductService productService = new ProductServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("productList", productService.findAll());
        request.setAttribute("price", 0.01);

        return "/WEB-INF/views/admin/plans/new.jsp";
    }
}
