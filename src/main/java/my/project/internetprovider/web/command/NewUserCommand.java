package my.project.internetprovider.web.command;

import my.project.internetprovider.db.entity.User;
import my.project.internetprovider.exception.CheckException;
import my.project.internetprovider.service.UserService;
import my.project.internetprovider.service.impl.UserServiceImpl;
import my.project.internetprovider.util.Language;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

public class NewUserCommand implements Command {
    private UserService userService = new UserServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        HttpSession session = request.getSession();
        Locale language = Language.getLocale((String) session.getAttribute("language"));

        try {
            User user = userService.register(login, password, name, email);

            return "redirect:/admin/users";
        } catch (CheckException e) {
            request.setAttribute("login", login);
            request.setAttribute("name", name);
            request.setAttribute("email", email);

            String errMessage = e.getMessage();
            if (errMessage.contains("="))
                request.setAttribute("messages",
                        Language.getLocalizedMessages(
                                CheckException.fromSingleToMultipleMessage(errMessage), language));
            else
                request.setAttribute("message", Language.getLocalizedMessage(e.getMessage(), language));

            return "/WEB-INF/views/admin/users/new.jsp";
        }
    }
}
