package my.project.internetprovider.web.command;

import my.project.internetprovider.db.entity.Product;
import my.project.internetprovider.exception.CheckException;
import my.project.internetprovider.service.ProductService;
import my.project.internetprovider.service.impl.ProductServiceImpl;
import my.project.internetprovider.util.Language;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

public class NewProductCommand implements Command {
    private ProductService productService = new ProductServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");

        HttpSession session = request.getSession();
        Locale language = Language.getLocale((String) session.getAttribute("language"));

        try {
            productService.create(Product.newBuilder()
                    .setName(name)
                    .build());

            return "redirect:/admin/products";
        } catch (CheckException e) {
            request.setAttribute("name", name);

            String errMessage = e.getMessage();
            if (errMessage.contains("="))
                request.setAttribute("messages",
                        Language.getLocalizedMessages(
                                CheckException.fromSingleToMultipleMessage(errMessage), language));
            else
                request.setAttribute("message", Language.getLocalizedMessage(e.getMessage(), language));

            return "/WEB-INF/views/admin/products/new.jsp";
        }
    }
}
