package my.project.internetprovider.web.command;

import my.project.internetprovider.db.Role;
import my.project.internetprovider.db.entity.Plan;
import my.project.internetprovider.db.entity.User;
import my.project.internetprovider.service.PlanService;
import my.project.internetprovider.service.impl.PlanServiceImpl;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class PlanListCommand implements Command {
    private PlanService planService = new PlanServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("user");

        List<Plan> planList = planService.findAll();
        request.setAttribute("planList", planList);

        if (loggedUser == null)
            return "/WEB-INF/views/plans.jsp";

        switch (Role.getRole(loggedUser)) {
            case ADMIN: return "/WEB-INF/views/admin/plans/list.jsp";
            case CLIENT: return "/WEB-INF/views/client/plans/list.jsp";
            default: return "/WEB-INF/views/plans.jsp";
        }
    }
}
