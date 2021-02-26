package my.project.internetprovider.web.command;

import my.project.internetprovider.db.Role;
import my.project.internetprovider.db.entity.User;
import my.project.internetprovider.service.UserService;
import my.project.internetprovider.service.impl.UserServiceImpl;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class EditUserFormCommand implements Command {
    private UserService userService = new UserServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Long userId = Long.valueOf(request.getParameter("userId"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String login = request.getParameter("login");

        request.setAttribute("userId", userId);
        request.setAttribute("name", name);
        request.setAttribute("email", email);
        request.setAttribute("login", login);

        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("user");

        switch (Role.getRole(loggedUser)) {
            case ADMIN: return "/WEB-INF/views/admin/users/edit.jsp";
            case CLIENT: return "/WEB-INF/views/client/users/edit.jsp";
            default: return "/index.jsp";

        }
    }
}
