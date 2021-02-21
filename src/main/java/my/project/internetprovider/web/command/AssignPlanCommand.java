package my.project.internetprovider.web.command;

import my.project.internetprovider.db.entity.Plan;
import my.project.internetprovider.db.entity.Product;
import my.project.internetprovider.service.AccountService;
import my.project.internetprovider.service.PlanService;
import my.project.internetprovider.service.ProductService;
import my.project.internetprovider.service.impl.AccountServiceImpl;
import my.project.internetprovider.service.impl.PlanServiceImpl;
import my.project.internetprovider.service.impl.ProductServiceImpl;

import javax.servlet.http.HttpServletRequest;

public class AssignPlanCommand implements Command {
    private AccountService accountService = new AccountServiceImpl();

    @Override
    public String execute(HttpServletRequest request) {
        String btnValue = request.getParameter("btnValue");
        Long planId = Long.valueOf(request.getParameter("plan"));
        Long userId = Long.valueOf(request.getParameter("uid"));
        Long accountId = Long.valueOf(request.getParameter("aid"));
        Long productId = Long.valueOf(request.getParameter("pid"));

        if ("assign".equals(btnValue) && planId > 0)
            accountService.assignPlanToAccount(accountId, planId, productId);

        return "redirect:/admin/users/cab?id=" + userId;
    }
}
