package my.project.internetprovider.web.command;

import my.project.internetprovider.db.entity.User;
import my.project.internetprovider.exception.UpdateException;
import my.project.internetprovider.service.UserService;
import my.project.internetprovider.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

        try {
            String oldPassword = loggedUser.getPassword();

            userService.updateProfile(userId, name, email, password, cpassword);

            if (!password.isEmpty() && !oldPassword.equals(password))
                return "redirect:/logout";

            loggedUser.setName(name);
            loggedUser.setEmail(email);

            session.setAttribute("user", loggedUser);

            return "redirect:/client/cab";
        } catch (UpdateException e) {
            request.setAttribute("message", e.getMessage());
            request.setAttribute("login", login);
            request.setAttribute("name", name);
            request.setAttribute("email", email);

            return "/WEB-INF/views/client/users/edit.jsp";
        }
    }
}
