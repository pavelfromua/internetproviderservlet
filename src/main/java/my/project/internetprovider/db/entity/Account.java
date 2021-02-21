package my.project.internetprovider.db.entity;

import java.util.Set;

public class Account {
    private Long id;
    private Long userId;
    private Set<Payment> payments;
    private Set<Plan> plans;
    private boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser() {
        return userId;
    }

    public void setUser(Long userId) {
        this.userId = userId;
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }

    public Set<Plan> getPlans() {
        return plans;
    }

    public void setPlans(Set<Plan> plans) {
        this.plans = plans;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static Builder newBuilder() {
        return new Account().new Builder();
    }

    public class Builder {
        private Builder() {}

        public Builder setId(Long id) {
            Account.this.id = id;

            return this;
        }

        public Builder setUser(Long userId) {
            Account.this.userId = userId;

            return this;
        }

        public Builder setPayments(Set<Payment> payments) {
            Account.this.payments = payments;

            return this;
        }

        public Builder setPlans(Set<Plan> plans) {
            Account.this.plans = plans;

            return this;
        }

        public Builder setActive(boolean active) {
            Account.this.active = active;

            return this;
        }

        public Account build() {
            return Account.this;
        }
    }
}
