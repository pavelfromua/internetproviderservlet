package my.project.internetprovider.web;



import my.project.internetprovider.web.command.AdminCabinetCommand;
import my.project.internetprovider.web.command.Command;
import my.project.internetprovider.web.command.DeleteUserCommand;
import my.project.internetprovider.web.command.ExceptionCommand;
import my.project.internetprovider.web.command.LogOutCommand;
import my.project.internetprovider.web.command.LoginCommand;
import my.project.internetprovider.web.command.LoginFormCommand;
import my.project.internetprovider.web.command.NewUserCommand;
import my.project.internetprovider.web.command.NewUserFormCommand;
import my.project.internetprovider.web.command.ShowUserCommand;
import my.project.internetprovider.web.command.EditUserFormCommand;
import my.project.internetprovider.web.command.UpdateUserCommand;
import my.project.internetprovider.web.command.UserListCommand;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Servlet extends HttpServlet {
    private Map<String, Command> commands = new HashMap<>();

    public void init(ServletConfig servletConfig){

        servletConfig.getServletContext()
                .setAttribute("loggedUsers", new HashSet<String>());

        commands.put("/logout", new LogOutCommand());
        commands.put("/login", new LoginFormCommand());
        commands.put("post/login", new LoginCommand());
        commands.put("/admin", new AdminCabinetCommand());
        commands.put("/admin/users", new UserListCommand());
        commands.put("/admin/users/new", new NewUserFormCommand());
        commands.put("/admin/users/cab", new ShowUserCommand());
        commands.put("post/admin/users/new", new NewUserCommand());
        commands.put("post/admin/users/delete", new DeleteUserCommand());
        commands.put("post/admin/users/edit", new EditUserFormCommand());
        commands.put("post/admin/users/cab", new UpdateUserCommand());
        commands.put("exception" , new ExceptionCommand());
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response, "");
        //response.getWriter().print("Hello from servlet");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response, "post");
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response, String method)
            throws ServletException, IOException {
        String path = method + request.getRequestURI();
        //path = method + "/" + path.replaceAll(".*/" , "");
        Command command = commands.getOrDefault(path ,
                (r)->"/index.jsp");
        System.out.println(command.getClass().getName());
        String page = command.execute(request);
        //request.getRequestDispatcher(page).forward(request,response);
        if(page.contains("redirect:")){
            response.sendRedirect(page.replace("redirect:", ""));
        }else {
            request.getRequestDispatcher(page).forward(request, response);
        }
    }
}
