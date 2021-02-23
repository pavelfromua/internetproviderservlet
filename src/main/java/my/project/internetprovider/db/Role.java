package my.project.internetprovider.db;


import my.project.internetprovider.db.entity.User;

/**
 * Role entity.
 * 
 *
 * 
 */

public enum Role {
	ADMIN, CLIENT, GUEST;
	
	public static Role getRole(User user) {
		int roleId = user.getRoleId();
		return Role.values()[roleId];
	}
	
	public String getName() {
		return name().toLowerCase();
	}
	
}
