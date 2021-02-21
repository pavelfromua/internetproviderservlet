package my.project.internetprovider.web.command;

import com.sun.xml.internal.bind.v2.TODO;
import my.project.internetprovider.db.Role;
import my.project.internetprovider.db.entity.User;
import my.project.internetprovider.exception.AuthenticationException;
import my.project.internetprovider.exception.NotFoundException;
import my.project.internetprovider.service.AccountService;
import my.project.internetprovider.service.UserService;
import my.project.internetprovider.service.impl.AccountServiceImpl;
import my.project.internetprovider.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
        } catch (NotFoundException e) {
            request.setAttribute("message", e.getMessage());
            return "redirect:/admin/users";
        }
    }
}
