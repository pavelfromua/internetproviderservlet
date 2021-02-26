package my.project.internetprovider.web.command;

import my.project.internetprovider.db.Role;
import my.project.internetprovider.db.entity.Page;
import my.project.internetprovider.db.entity.Plan;
import my.project.internetprovider.db.entity.User;
import my.project.internetprovider.service.PlanService;
import my.project.internetprovider.service.impl.PlanServiceImpl;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class PlanPageCommand implements Command {
    private PlanService planService = new PlanServiceImpl();

    private void setLocale(HttpServletRequest request) {
        HttpSession session = request.getSession();

        String language = request.getParameter("language");
        if (language == null)
            session.setAttribute("language", "en");
        else
            session.setAttribute("language", language);
    }

    private int initCurrentPage(HttpServletRequest request) {
        String pn = request.getParameter("pn");
        if (pn == null)
            return 1;

        int currentPage = Integer.valueOf(pn);
        if (currentPage <= 0)
            currentPage = 1;

        return currentPage;
    }

    private String initSortedField(HttpServletRequest request) {
        String sf = request.getParameter("sf");
        if (sf == null || sf.isEmpty())
            return "name";

        return sf;
    }

    private String initSortDirection(HttpServletRequest request) {
        String sd = request.getParameter("sd");
        if (sd == null || sd.isEmpty())
            return "asc";

        return sd;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        setLocale(request);
        int currentPage = initCurrentPage(request);
        String sf = initSortedField(request);
        String sd = initSortDirection(request);

        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("user");

        Page<Plan> page = planService.findAllForPage(2, currentPage, sf, sd);

        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();

        List<Plan> planList = page.getElements();
        String rsd = "asc".equals(sd) ? "desc" : "asc";

        request.setAttribute("planList", planList);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalItems", totalItems);
        request.setAttribute("sf", sf);
        request.setAttribute("sd", sd);
        request.setAttribute("rsd", rsd);

        if (loggedUser == null)
            return "/WEB-INF/views/plans.jsp";

        switch (Role.getRole(loggedUser)) {
            case ADMIN: return "/WEB-INF/views/admin/plans/list.jsp";
            case CLIENT: return "/WEB-INF/views/client/plans/list.jsp";
            default: return "/WEB-INF/views/plans.jsp";
        }
    }
}
