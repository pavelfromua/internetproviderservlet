package my.project.internetprovider;

/**
 * Path holder (jsp pages, controller commands).
 * 
 * @author D.Kolesnikov
 * 
 */
public final class Path {
	
	// pages
	public static final String PAGE_INDEX = "/index.jsp";
	public static final String PAGE_LOGIN = "/login.jsp";
	public static final String PAGE_ERROR_PAGE = "/WEB-INF/views/error_page.jsp";
//	public static final String PAGE_LIST_MENU = "/WEB-INF/jsp/client/list_menu.jsp";
//	public static final String PAGE_LIST_ORDERS = "/WEB-INF/jsp/admin/list_orders.jsp";
	public static final String PAGE_LIST_USERS = "/WEB-INF/views/admin/users/list.jsp";
	public static final String PAGE_SETTINGS = "/WEB-INF/views/settings.jsp";

	// commands
//	public static final String COMMAND_LIST_ORDERS = "/controller?command=listOrders";
	//public static final String COMMAND_LIST_USERS = "/admin/users?command=users";
	//public static final String COMMAND_LIST_USERS = "/controller?command=users";
	public static final String COMMAND_LIST_USERS = "/controller/users";
//	public static final String COMMAND_LIST_MENU = "/controller?command=listMenu";

}