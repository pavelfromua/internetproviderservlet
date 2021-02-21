package my.project.internetprovider.web.command;

import my.project.internetprovider.db.Role;
import my.project.internetprovider.db.entity.Plan;
import my.project.internetprovider.db.entity.User;
import my.project.internetprovider.service.AccountService;
import my.project.internetprovider.service.impl.AccountServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class AssignPlanFormCommand implements Command {
    private AccountService accountService = new AccountServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Long userId = Long.valueOf(request.getParameter("uid"));
        Long accountId = Long.valueOf(request.getParameter("aid"));
        Long productId = Long.valueOf(request.getParameter("pid"));
        Long planId = Long.valueOf(request.getParameter("plid"));

        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("user");

        List<Plan> plans = accountService.findPlansByProductId(productId);
        if (plans.size() == 0) {
            request.setAttribute("uid", userId);

            switch (Role.getRole(loggedUser)) {
                case ADMIN: return "/WEB-INF/views/admin/plans/noassign.jsp";
                case CLIENT: return "/WEB-INF/views/client/plans/noassign.jsp";
                default: return "/index.jsp";

            }
        }

        request.setAttribute("planList", plans);
        request.setAttribute("uid", userId);
        request.setAttribute("aid", accountId);
        request.setAttribute("pid", productId);
        request.setAttribute("plid", planId);

        switch (Role.getRole(loggedUser)) {
            case ADMIN: return "/WEB-INF/views/admin/plans/assign.jsp";
            case CLIENT: return "/WEB-INF/views/client/plans/assign.jsp";
            default: return "/index.jsp";

        }
    }
}
