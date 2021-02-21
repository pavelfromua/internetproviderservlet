package my.project.internetprovider.web.command;

import my.project.internetprovider.service.AccountService;
import my.project.internetprovider.service.impl.AccountServiceImpl;

import javax.servlet.http.HttpServletRequest;

public class PayCommand implements Command {
    private AccountService accountService = new AccountServiceImpl();

    @Override
    public String execute(HttpServletRequest request) {
        Long userId = Long.valueOf(request.getParameter("uid"));
        Long accountId = Long.valueOf(request.getParameter("aid"));
        Double amount = Double.valueOf(request.getParameter("amount"));

        accountService.savePayment(accountId, amount);

        return "redirect:/admin/users/cab?id=" + userId;
    }
}
