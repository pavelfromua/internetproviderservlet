package my.project.internetprovider.web.command;

import my.project.internetprovider.db.Role;
import my.project.internetprovider.db.entity.User;
import my.project.internetprovider.exception.CheckException;
import my.project.internetprovider.service.AccountService;
import my.project.internetprovider.service.impl.AccountServiceImpl;
import my.project.internetprovider.util.Language;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

public class PayCommand implements Command {
    private AccountService accountService = new AccountServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Long userId = Long.valueOf(request.getParameter("uid"));
        Long accountId = Long.valueOf(request.getParameter("aid"));
        Double amount = Double.valueOf(request.getParameter("amount"));

        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("user");
        Locale language = Language.getLocale((String) session.getAttribute("language"));

        try {
            accountService.savePayment(accountId, amount);
        } catch (Exception e) {
            String errMessage = e.getMessage();
            if (errMessage.contains("="))
                request.setAttribute("messages",
                        Language.getLocalizedMessages(
                                CheckException.fromSingleToMultipleMessage(errMessage), language));
            else
                request.setAttribute("message", Language.getLocalizedMessage(e.getMessage(), language));
        }

        switch (Role.getRole(loggedUser)) {
            case ADMIN: return "redirect:/admin/users/cab?id=" + userId;
            case CLIENT: return "redirect:/client/cab";
            default: return "/index.jsp";
        }
    }
}
