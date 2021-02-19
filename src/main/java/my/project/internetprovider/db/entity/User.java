package my.project.internetprovider.db.entity;

/**
 * User entity.
 *
 * @author D.Kolesnikov
 *
 */
public class User extends Entity {
    private Long id;

    private String login;

    private String password;

    private String name;

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private byte[] salt;

    private int roleId;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "User [login=" + login
                + ", name=" + name
                + ", roleId=" + roleId + "]";
    }

    public static Builder newBuilder() {
        return new User().new Builder();
    }

    public class Builder {
        private Builder() {}

        public Builder setId(Long id) {
            User.this.id = id;

            return this;
        }

        public Builder setLogin(String login) {
            User.this.login = login;

            return this;
        }

        public Builder setName(String name) {
            User.this.name = name;

            return this;
        }

        public Builder setEmail(String email) {
            User.this.email = email;

            return this;
        }

        public Builder setPassword(String password) {
            User.this.password = password;

            return this;
        }

        public Builder setRoleId(int roleId) {
            User.this.roleId = roleId;

            return this;
        }

//        public Builder setRoles(Set<Role> roles) {
//            User.this.roles = roles;
//
//            return this;
//        }

//        public Builder setAccount(Account account) {
//            User.this.account = account;
//
//            return this;
//        }

        public User build() {
            return User.this;
        }
    }
}
