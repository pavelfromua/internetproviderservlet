package my.project.internetprovider.web.command;

import my.project.internetprovider.service.AccountService;
import my.project.internetprovider.service.impl.AccountServiceImpl;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SetAccountStatusCommand implements Command {
    private AccountService accountService = new AccountServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Long userId = Long.valueOf(request.getParameter("uid"));
        Long accountId = Long.valueOf(request.getParameter("aid"));
        Boolean status = Boolean.valueOf(request.getParameter("as"));

        accountService.setStatus(accountId, status);

        return "redirect:/admin/users/cab?id=" + userId;
    }
}
