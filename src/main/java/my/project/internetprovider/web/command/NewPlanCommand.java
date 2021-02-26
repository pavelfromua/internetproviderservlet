package my.project.internetprovider.web.command;

import my.project.internetprovider.db.entity.Plan;
import my.project.internetprovider.db.entity.Product;
import my.project.internetprovider.exception.CheckException;
import my.project.internetprovider.service.PlanService;
import my.project.internetprovider.service.ProductService;
import my.project.internetprovider.service.impl.PlanServiceImpl;
import my.project.internetprovider.service.impl.ProductServiceImpl;
import my.project.internetprovider.util.Language;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

public class NewPlanCommand implements Command {
    private PlanService planService = new PlanServiceImpl();
    private ProductService productService = new ProductServiceImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String priceStr = request.getParameter("price");
        Double price = Double.valueOf(priceStr.isEmpty() ? "0" : priceStr);
        String[] productItem = request.getParameter("productItem").split(":");
        if (productItem.length == 0)
            productItem = new String[]{"0", ""};

        HttpSession session = request.getSession();
        Locale language = Language.getLocale((String) session.getAttribute("language"));

        try {
            planService.create(Plan.newBuilder()
                    .setName(name)
                    .setPrice(price)
                    .setProduct(Product.newBuilder()
                            .setId(Long.valueOf(productItem[0]))
                            .setName(productItem[1])
                            .build())
                    .build());

            return "redirect:/admin/plans";
        } catch (CheckException e) {
            request.setAttribute("name", name);
            request.setAttribute("price", priceStr);
            request.setAttribute("productList", productService.findAll());

            String errMessage = e.getMessage();
            if (errMessage.contains("="))
                request.setAttribute("messages",
                        Language.getLocalizedMessages(
                                CheckException.fromSingleToMultipleMessage(errMessage), language));
            else
                request.setAttribute("message", Language.getLocalizedMessage(e.getMessage(), language));

            return "/WEB-INF/views/admin/plans/new.jsp";
        }
    }
}
