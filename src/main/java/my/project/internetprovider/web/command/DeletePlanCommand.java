package my.project.internetprovider.web.command;

import my.project.internetprovider.exception.DataProcessingException;
import my.project.internetprovider.service.PlanService;
import my.project.internetprovider.service.impl.PlanServiceImpl;

import javax.servlet.http.HttpServletRequest;

public class DeletePlanCommand implements Command {
    private PlanService planService = new PlanServiceImpl();

    @Override
    public String execute(HttpServletRequest request) {
        Long planId = Long.valueOf(request.getParameter("planId"));
        try {
            planService.delete(planId);
        } catch (DataProcessingException e) {
            return "redirect:/admin/plans?msg="+e.getMessage();
        }

        return "redirect:/admin/plans";
    }
}
