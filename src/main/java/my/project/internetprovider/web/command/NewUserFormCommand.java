package my.project.internetprovider.web.command;


import javax.servlet.http.HttpServletRequest;

public class NewUserFormCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return "/WEB-INF/views/admin/users/new.jsp";
    }
}
