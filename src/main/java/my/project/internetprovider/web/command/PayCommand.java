package my.project.internetprovider.web.command;

import my.project.internetprovider.db.Role;
import my.project.internetprovider.db.entity.User;
import my.project.internetprovider.service.AccountService;
import my.project.internetprovider.service.impl.AccountServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PayCommand implements Command {
    private AccountService accountService = new AccountServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Long userId = Long.valueOf(request.getParameter("uid"));
        Long accountId = Long.valueOf(request.getParameter("aid"));
        Double amount = Double.valueOf(request.getParameter("amount"));

        accountService.savePayment(accountId, amount);


        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("user");

        switch (Role.getRole(loggedUser)) {
            case ADMIN: return "redirect:/admin/users/cab?id=" + userId;
            case CLIENT: return "redirect:/client/cab";
            default: return "/index.jsp";
        }
    }
}
