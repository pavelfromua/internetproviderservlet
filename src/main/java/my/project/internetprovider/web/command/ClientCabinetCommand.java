package my.project.internetprovider.web.command;

import my.project.internetprovider.db.entity.User;
import my.project.internetprovider.service.AccountService;
import my.project.internetprovider.service.UserService;
import my.project.internetprovider.service.impl.AccountServiceImpl;
import my.project.internetprovider.service.impl.UserServiceImpl;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class ClientCabinetCommand implements Command {
    private UserService userService = new UserServiceImpl();
    private AccountService accountService = new AccountServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("user");
        request.setAttribute("client", loggedUser);

        Map<String, ?> accountData = accountService.getDataForClientCabinet(loggedUser.getId());
        accountData.entrySet().stream().forEach(e -> request.setAttribute(e.getKey(), e.getValue()));

        return "/WEB-INF/views/client/users/show.jsp";
    }
}
