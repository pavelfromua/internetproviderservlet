package my.project.internetprovider.web.command;

import my.project.internetprovider.db.entity.User;
import my.project.internetprovider.exception.RegistrationException;
import my.project.internetprovider.exception.UpdateException;
import my.project.internetprovider.service.UserService;
import my.project.internetprovider.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;

public class UpdateUserCommand implements Command {
    private UserService userService = new UserServiceImpl();

    @Override
    public String execute(HttpServletRequest request) {
        Long userId = Long.valueOf(request.getParameter("userId"));
        String login = request.getParameter("login");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String cpassword = request.getParameter("cpassword");

        try {
            userService.updateProfile(userId, name, email, password, cpassword);

            return "redirect:/admin/users/cab?id=" + userId;
        } catch (UpdateException e) {
            request.setAttribute("message", e.getMessage());
            request.setAttribute("login", login);
            request.setAttribute("name", name);
            request.setAttribute("email", email);

            return "/WEB-INF/views/admin/users/edit.jsp";
        }
    }
}
