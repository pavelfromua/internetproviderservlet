package my.project.internetprovider.db.entity;

/**
 * Product entity.
 *
 */
public class Product {
    private Long id;
    private String name;

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

    @Override
    public String toString() {
        return name;
    }

    public static Builder newBuilder() {
        return new Product().new Builder();
    }

    public class Builder {
        private Builder() {}

        public Builder setId(Long id) {
            Product.this.id = id;

            return this;
        }

        public Builder setName(String name) {
            Product.this.name = name;

            return this;
        }

        public Product build() {
            return Product.this;
        }
    }
}
