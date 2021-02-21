package my.project.internetprovider.web.command;

import my.project.internetprovider.db.entity.Plan;
import my.project.internetprovider.service.AccountService;
import my.project.internetprovider.service.impl.AccountServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class AssignPlanFormCommand implements Command {
    private AccountService accountService = new AccountServiceImpl();

    @Override
    public String execute(HttpServletRequest request) {
        Long userId = Long.valueOf(request.getParameter("uid"));
        Long accountId = Long.valueOf(request.getParameter("aid"));
        Long productId = Long.valueOf(request.getParameter("pid"));
        Long planId = Long.valueOf(request.getParameter("plid"));

        List<Plan> plans = accountService.findPlansByProductId(productId);
        if (plans.size() == 0) {
            request.setAttribute("uid", userId);

            return "/WEB-INF/views/admin/plans/noassign.jsp";
        }

        request.setAttribute("planList", plans);
        request.setAttribute("uid", userId);
        request.setAttribute("aid", accountId);
        request.setAttribute("pid", productId);
        request.setAttribute("plid", planId);

        return "/WEB-INF/views/admin/plans/assign.jsp";
    }
}
