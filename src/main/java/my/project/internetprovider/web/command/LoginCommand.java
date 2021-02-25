package my.project.internetprovider.web.command;

import my.project.internetprovider.db.Role;
import my.project.internetprovider.db.entity.User;
import my.project.internetprovider.exception.AuthenticationException;
import my.project.internetprovider.service.UserService;
import my.project.internetprovider.service.impl.UserServiceImpl;
import my.project.internetprovider.util.Language;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.ResourceBundle;


public class LoginCommand implements Command {
    private UserService userService = new UserServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        HttpSession session = request.getSession();
        HashSet<String> loggedUsers = (HashSet<String>) session
                .getServletContext()
                .getAttribute("loggedUsers");

        ResourceBundle bundle = ResourceBundle.getBundle("messages",
                Language.getLocale((String) session.getAttribute("language")));

        if (loggedUsers.contains(login)) {
            request.setAttribute("message", bundle.getString("alreadyLogged"));
            return "/login.jsp";
        }

        try {
            User user = userService.login(login, password);

            session.setAttribute("user", user);
            loggedUsers.add(login);
            session.setAttribute("loggedUsers", loggedUsers);

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
