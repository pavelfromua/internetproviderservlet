package my.project.internetprovider.web.command;

import my.project.internetprovider.db.Role;
import my.project.internetprovider.db.entity.Plan;
import my.project.internetprovider.db.entity.User;
import my.project.internetprovider.service.PlanService;
import my.project.internetprovider.service.impl.PlanServiceImpl;
import my.project.internetprovider.util.PlanPDFExporter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PlanListExportPDFCommand implements Command {
    private PlanService planService = new PlanServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=plans_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Plan> listPlans = planService.findAll();

        PlanPDFExporter exporter = new PlanPDFExporter(listPlans);
        try {
            exporter.export(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("user");

        if (loggedUser == null)
            return "/WEB-INF/views/plans.jsp";

        switch (Role.getRole(loggedUser)) {
            case ADMIN: return "/WEB-INF/views/admin/plans/list.jsp";
            case CLIENT: return "/WEB-INF/views/client/plans/list.jsp";
            default: return "/WEB-INF/views/plans.jsp";
        }
    }
}
