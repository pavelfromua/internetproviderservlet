package my.project.internetprovider.web.command;

import my.project.internetprovider.service.UserService;
import my.project.internetprovider.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;

public class EditUserFormCommand implements Command {
    private UserService userService = new UserServiceImpl();

    @Override
    public String execute(HttpServletRequest request) {
        Long userId = Long.valueOf(request.getParameter("userId"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String login = request.getParameter("login");

        request.setAttribute("userId", userId);
        request.setAttribute("name", name);
        request.setAttribute("email", email);
        request.setAttribute("login", login);

        return "/WEB-INF/views/admin/users/edit.jsp";
    }
}
