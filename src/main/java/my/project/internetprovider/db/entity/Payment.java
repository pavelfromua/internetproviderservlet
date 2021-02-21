package my.project.internetprovider.db.entity;

import java.sql.Date;
import java.time.LocalDateTime;

public class Payment {
    private Long id;
    private String name;
    private Double amount;
    private LocalDateTime date;
    private Long accountId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getAccount() {
        return accountId;
    }

    public void setAccount(Long accountId) {
        this.accountId = accountId;
    }

    public static Builder newBuilder() {
        return new Payment().new Builder();
    }

    public class Builder {
        private Builder() {}

        public Builder setId(Long id) {
            Payment.this.id = id;

            return this;
        }

        public Builder setName(String name) {
            Payment.this.name = name;

            return this;
        }

        public Builder setAmount(Double amount) {
            Payment.this.amount = amount;

            return this;
        }

        public Builder setDate(LocalDateTime date) {
            Payment.this.date = date;

            return this;
        }

        public Builder setAccount(Long accountId) {
            Payment.this.accountId = accountId;

            return this;
        }

        public Payment build() {
            return Payment.this;
        }
    }
}
