package my.project.internetprovider.web.command;


import javax.servlet.http.HttpServletRequest;

public class NewProductFormCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return "/WEB-INF/views/admin/products/new.jsp";
    }
}
