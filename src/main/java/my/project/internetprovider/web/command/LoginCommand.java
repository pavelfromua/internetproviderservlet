package my.project.internetprovider.web.command;

import my.project.internetprovider.db.Role;
import my.project.internetprovider.db.entity.User;
import my.project.internetprovider.exception.AuthenticationException;
import my.project.internetprovider.service.UserService;
import my.project.internetprovider.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class LoginCommand implements Command {
    private UserService userService = new UserServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        try {
            User user = userService.login(login, password);
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            switch (Role.getRole(user)) {
                case ADMIN: return "redirect:/admin";
                case CLIENT: return "redirect:/client/cab";
                default: return "/index.jsp";

            }
        } catch (AuthenticationException e) {
            request.setAttribute("message", e.getMessage());
            return "/login.jsp";
        }
    }
}
