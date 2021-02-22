package my.project.internetprovider.web.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IndexCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return "redirect:/plans/page?pn=1&sf=name&sd=asc";
    }
}
