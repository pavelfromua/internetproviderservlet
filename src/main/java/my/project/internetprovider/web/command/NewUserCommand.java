package my.project.internetprovider.web.command;

import my.project.internetprovider.db.entity.User;
import my.project.internetprovider.exception.RegistrationException;
import my.project.internetprovider.service.UserService;
import my.project.internetprovider.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class NewUserCommand implements Command {
    private UserService userService = new UserServiceImpl();

    @Override
    public String execute(HttpServletRequest request) {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        try {
            User user = userService.register(login, password, name, email);

            return "redirect:/admin/users";
        } catch (RegistrationException e) {
            request.setAttribute("message", e.getMessage());
            return "/WEB-INF/views/admin/users/new.jsp";
        }
    }
}
