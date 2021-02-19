package my.project.internetprovider.web.command;

import com.sun.xml.internal.bind.v2.TODO;
import my.project.internetprovider.db.entity.User;
import my.project.internetprovider.exception.AuthenticationException;
import my.project.internetprovider.exception.NotFoundException;
import my.project.internetprovider.service.UserService;
import my.project.internetprovider.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;

public class ShowUserCommand implements Command {
    private UserService userService = new UserServiceImpl();

    @Override
    public String execute(HttpServletRequest request) {
        Long userId = Long.valueOf(request.getParameter("id"));
        try {
            User user = userService.findById(userId);
            request.setAttribute("client", user);

            return "/WEB-INF/views/admin/users/show.jsp";
        } catch (NotFoundException e) {
            request.setAttribute("message", e.getMessage());
            return "redirect:/admin/users"; // TODO correct if admin. Possible, it needs to logout in any other case
        }
    }
}
