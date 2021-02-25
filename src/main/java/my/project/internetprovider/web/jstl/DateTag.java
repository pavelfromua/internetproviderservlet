package my.project.internetprovider.web.jstl;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Custom tag for formatting data
 *
 */
public class DateTag extends BodyTagSupport {
    @Override
    public int doAfterBody() throws JspException {
        BodyContent bc = getBodyContent();
        JspWriter out = bc.getEnclosingWriter();

        try {
            LocalDateTime body = LocalDateTime.parse(bc.getString());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            out.print(body.format(formatter));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }
}
