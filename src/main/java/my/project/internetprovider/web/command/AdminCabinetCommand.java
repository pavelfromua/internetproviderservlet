package my.project.internetprovider.web.command;

import javax.servlet.http.HttpServletRequest;

public class AdminCabinetCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return "redirect:/admin/users";
    }
}
