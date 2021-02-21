package my.project.internetprovider.web.command;

import javax.servlet.http.HttpServletRequest;

public class LogOutCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        // ToDo delete current user (context & session)
        request.getSession().invalidate();
        return "redirect:/index.jsp";
    }
}
