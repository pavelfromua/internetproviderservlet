package my.project.internetprovider.web.command;

import my.project.internetprovider.db.entity.Plan;
import my.project.internetprovider.db.entity.Product;
import my.project.internetprovider.exception.ValidationException;
import my.project.internetprovider.service.PlanService;
import my.project.internetprovider.service.ProductService;
import my.project.internetprovider.service.impl.PlanServiceImpl;
import my.project.internetprovider.service.impl.ProductServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public class NewPlanCommand implements Command {
    private PlanService planService = new PlanServiceImpl();
    private ProductService productService = new ProductServiceImpl();

    @Override
    public String execute(HttpServletRequest request) {
        String name = request.getParameter("name");
        String priceStr = request.getParameter("price");
        Double price = Double.valueOf(priceStr.isEmpty() ? "0" : priceStr);
        String[] productItem = request.getParameter("productItem").split(":");
        if (productItem.length == 0)
            productItem = new String[]{"0", ""};

        try {
            planService.create(Plan.newBuilder()
                    .setName(name)
                    .setPrice(price)
                    .setProduct(Product.newBuilder()
                            .setId(Long.valueOf(productItem[0]))
                            .setName(productItem[1])
                            .build())
                    .build());

            return "redirect:/admin/plans";
        } catch (ValidationException e) {
            request.setAttribute("message", e.getMessage());
            request.setAttribute("name", name);
            request.setAttribute("price", priceStr);
            request.setAttribute("productList", productService.findAll());
  //          request.setAttribute("productList", new ArrayList<Product>());

            return "/WEB-INF/views/admin/plans/new.jsp";
        }
    }
}
