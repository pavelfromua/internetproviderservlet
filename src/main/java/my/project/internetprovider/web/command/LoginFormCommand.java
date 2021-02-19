package my.project.internetprovider.web.command;

import javax.servlet.http.HttpServletRequest;

public class LoginFormCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return "/login.jsp";
    }
}
