package my.project.internetprovider.web.command;

import my.project.internetprovider.db.entity.User;
import my.project.internetprovider.service.UserService;
import my.project.internetprovider.service.impl.UserServiceImpl;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class UserListCommand implements Command {
    private UserService userService = new UserServiceImpl();

    @Override
    public String execute(HttpServletRequest request) {
        List<User> userList = userService.findAll();
        //HttpSession session = request.getSession();
        //session.setAttribute("userList", userList);
        request.setAttribute("userList", userList);

        return "/WEB-INF/views/admin/users/list.jsp";
    }
}
