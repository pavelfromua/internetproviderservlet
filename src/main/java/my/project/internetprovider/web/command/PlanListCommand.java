package my.project.internetprovider.web.command;

import my.project.internetprovider.db.entity.Plan;
import my.project.internetprovider.service.PlanService;
import my.project.internetprovider.service.impl.PlanServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class PlanListCommand implements Command {
    private PlanService planService = new PlanServiceImpl();

    @Override
    public String execute(HttpServletRequest request) {
        List<Plan> planList = planService.findAll();
        request.setAttribute("planList", planList);

        return "/WEB-INF/views/admin/plans/list.jsp";
    }
}
