package my.project.internetprovider.web.command;

import my.project.internetprovider.db.entity.Plan;
import my.project.internetprovider.db.entity.Product;
import my.project.internetprovider.service.PlanService;
import my.project.internetprovider.service.ProductService;
import my.project.internetprovider.service.impl.PlanServiceImpl;
import my.project.internetprovider.service.impl.ProductServiceImpl;

import javax.servlet.http.HttpServletRequest;

public class UpdatePlanCommand implements Command {
    private ProductService productService = new ProductServiceImpl();
    private PlanService planService = new PlanServiceImpl();

    @Override
    public String execute(HttpServletRequest request) {
        Long planId = Long.valueOf(request.getParameter("planId"));
        String name = request.getParameter("name");
        String priceStr = request.getParameter("price");
        Double price = Double.valueOf(priceStr.isEmpty() ? "0" : priceStr);
        String[] productItem = request.getParameter("productItem").split(":");

        Plan plan = Plan.newBuilder()
                .setId(planId)
                .setName(name)
                .setPrice(price)
                .setProduct(Product.newBuilder()
                        .setId(Long.valueOf(productItem[0]))
                        .setName(productItem[1])
                        .build())
                .build();

        try {
            planService.update(plan);

            return "redirect:/admin/plans";
        } catch (Exception e) {
            request.setAttribute("message", e.getMessage());
            request.setAttribute("plan", plan);
            request.setAttribute("productList", productService.findAll());

            return "/WEB-INF/views/admin/plans/edit.jsp";
        }
    }
}
