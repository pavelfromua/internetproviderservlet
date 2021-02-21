package my.project.internetprovider.web;



import my.project.internetprovider.web.command.AdminCabinetCommand;
import my.project.internetprovider.web.command.AssignPlanCommand;
import my.project.internetprovider.web.command.AssignPlanFormCommand;
import my.project.internetprovider.web.command.Command;
import my.project.internetprovider.web.command.DeletePlanCommand;
import my.project.internetprovider.web.command.DeleteProductCommand;
import my.project.internetprovider.web.command.DeleteUserCommand;
import my.project.internetprovider.web.command.EditPlanFormCommand;
import my.project.internetprovider.web.command.EditProductFormCommand;
import my.project.internetprovider.web.command.ExceptionCommand;
import my.project.internetprovider.web.command.LogOutCommand;
import my.project.internetprovider.web.command.LoginCommand;
import my.project.internetprovider.web.command.LoginFormCommand;
import my.project.internetprovider.web.command.NewPlanCommand;
import my.project.internetprovider.web.command.NewPlanFormCommand;
import my.project.internetprovider.web.command.NewProductCommand;
import my.project.internetprovider.web.command.NewProductFormCommand;
import my.project.internetprovider.web.command.NewUserCommand;
import my.project.internetprovider.web.command.NewUserFormCommand;
import my.project.internetprovider.web.command.PayCommand;
import my.project.internetprovider.web.command.PlanListCommand;
import my.project.internetprovider.web.command.ProductListCommand;
import my.project.internetprovider.web.command.SetAccountStatusCommand;
import my.project.internetprovider.web.command.ShowUserCommand;
import my.project.internetprovider.web.command.EditUserFormCommand;
import my.project.internetprovider.web.command.UpdatePlanCommand;
import my.project.internetprovider.web.command.UpdateProductCommand;
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
        commands.put("post/admin/users/edit", new EditUserFormCommand());
        commands.put("post/admin/users/cab", new UpdateUserCommand());
        commands.put("post/admin/users/delete", new DeleteUserCommand());

        commands.put("/admin/products/new", new NewProductFormCommand());
        commands.put("post/admin/products/new", new NewProductCommand());
        commands.put("/admin/products", new ProductListCommand());
        commands.put("/admin/products/edit", new EditProductFormCommand());
        commands.put("post/admin/products", new UpdateProductCommand());
        commands.put("post/admin/products/delete", new DeleteProductCommand());

        commands.put("/admin/plans/new", new NewPlanFormCommand());
        commands.put("post/admin/plans/new", new NewPlanCommand());
        commands.put("/admin/plans", new PlanListCommand());
        commands.put("/admin/plans/edit", new EditPlanFormCommand());
        commands.put("post/admin/plans", new UpdatePlanCommand());
        commands.put("post/admin/plans/delete", new DeletePlanCommand());

        commands.put("/admin/users/cab/assign", new AssignPlanFormCommand());
        commands.put("post/admin/users/cab/assign", new AssignPlanCommand());
        commands.put("post/admin/users/cab/status", new SetAccountStatusCommand());
        commands.put("post/admin/users/cab/pay", new PayCommand());

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
