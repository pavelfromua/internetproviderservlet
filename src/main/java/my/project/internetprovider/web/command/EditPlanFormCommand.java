package my.project.internetprovider.web.command;

import my.project.internetprovider.db.entity.Plan;
import my.project.internetprovider.exception.CheckException;
import my.project.internetprovider.service.PlanService;
import my.project.internetprovider.service.ProductService;
import my.project.internetprovider.service.impl.PlanServiceImpl;
import my.project.internetprovider.service.impl.ProductServiceImpl;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditPlanFormCommand implements Command {
    private PlanService planService = new PlanServiceImpl();
    private ProductService productService = new ProductServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Long planId = Long.valueOf(request.getParameter("id"));
        try {
            Plan plan = planService.findById(planId);
            request.setAttribute("plan", plan);
            request.setAttribute("productList", productService.findAll());

            return "/WEB-INF/views/admin/plans/edit.jsp";
        } catch (CheckException e) {
            request.setAttribute("message", e.getMessage());
            return "redirect:/admin/plans";
        }
    }
}
