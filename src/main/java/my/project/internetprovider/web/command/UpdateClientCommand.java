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

public class UpdateClientCommand implements Command {
    private UserService userService = new UserServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Long userId = Long.valueOf(request.getParameter("userId"));
        String login = request.getParameter("login");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String cpassword = request.getParameter("cpassword");

        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("user");
        Locale language = Language.getLocale((String) session.getAttribute("language"));

        try {
            String oldPassword = loggedUser.getPassword();

            userService.updateProfile(userId, name, email, password, cpassword);

            if (!password.isEmpty() && !oldPassword.equals(password))
                return "redirect:/logout";

            loggedUser.setName(name);
            loggedUser.setEmail(email);

            session.setAttribute("user", loggedUser);

            return "redirect:/client/cab";
        } catch (Exception e) {
            request.setAttribute("userId", userId);
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

            return "/WEB-INF/views/client/users/edit.jsp";
        }
    }
}
