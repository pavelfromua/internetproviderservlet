package my.project.internetprovider.web.listener;

import my.project.internetprovider.db.entity.User;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashSet;

public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HashSet<String> loggedUsers = (HashSet<String>) httpSessionEvent
                .getSession().getServletContext()
                .getAttribute("loggedUsers");
        User user = (User) httpSessionEvent.getSession()
                .getAttribute("user");
        loggedUsers.remove(user.getLogin());
        httpSessionEvent.getSession().setAttribute("loggedUsers", loggedUsers);
    }
}
