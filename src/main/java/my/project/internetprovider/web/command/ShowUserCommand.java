package my.project.internetprovider.web.command;

import my.project.internetprovider.db.entity.User;
import my.project.internetprovider.exception.CheckException;
import my.project.internetprovider.service.AccountService;
import my.project.internetprovider.service.UserService;
import my.project.internetprovider.service.impl.AccountServiceImpl;
import my.project.internetprovider.service.impl.UserServiceImpl;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ShowUserCommand implements Command {
    private UserService userService = new UserServiceImpl();
    private AccountService accountService = new AccountServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Long userId = Long.valueOf(request.getParameter("id"));

        try {
            User user = userService.findById(userId);
            request.setAttribute("client", user);

            Map<String, ?> accountData = accountService.getDataForClientCabinet(userId);
            accountData.entrySet().stream().forEach(e -> request.setAttribute(e.getKey(), e.getValue()));

            return "/WEB-INF/views/admin/users/show.jsp";
        } catch (CheckException e) {
            request.setAttribute("message", e.getMessage());
            return "redirect:/admin/users";
        }
    }
}
