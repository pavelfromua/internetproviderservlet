package my.project.internetprovider.db.entity;

/**
 * Plan entity.
 *
 */
public class Plan {
    private Long id;
    private String name;
    private Double price;
    private Product product;

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Builder newBuilder() {
        return new Plan().new Builder();
    }

    public class Builder {
        private Builder() {}

        public Builder setId(Long id) {
            Plan.this.id = id;

            return this;
        }

        public Builder setName(String name) {
            Plan.this.name = name;

            return this;
        }

        public Builder setPrice(Double price) {
            Plan.this.price = price;

            return this;
        }

        public Builder setProduct(Product product) {
            Plan.this.product = product;

            return this;
        }

        public Plan build() {
            return Plan.this;
        }
    }
}
