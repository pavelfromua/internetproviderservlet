package my.project.internetprovider.db;

/**
 * Holder for fields names of DB tables.
 * 
 *
 * 
 */
public final class Fields {
	// entities
	public static final String ENTITY_ID = "id";
	
	public static final String USER_LOGIN = "login";
	public static final String USER_PASSWORD = "password";
	public static final String USER_NAME = "name";
	public static final String USER_EMAIL = "email";
	public static final String USER_SALT = "salt";
	public static final String USER_ROLE_ID = "role_id";

	public static final String PRODUCT_NAME = "name";

	public static final String PLAN_NAME = "name";
	public static final String PLAN_PRICE = "price";
	public static final String PLAN_PRODUCT_ID = "product_id";
	public static final String PLAN_PRODUCT_NAME = "product";

	public static final String PAYMENT_NAME = "name";
	public static final String PAYMENT_AMOUNT = "amount";
	public static final String PAYMENT_DATE = "date";
	public static final String PAYMENT_ACCOUNT_ID = "account_id";

    public static final String ACCOUNT_USER_ID = "user_id";
	public static final String ACCOUNT_ACTIVE = "active";

	public static final String COUNT_OF_ITEMS = "num_rows";
}