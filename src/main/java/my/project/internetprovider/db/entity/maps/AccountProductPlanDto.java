package my.project.internetprovider.db.entity.maps;

import my.project.internetprovider.db.entity.Plan;
import my.project.internetprovider.db.entity.Product;

public class AccountProductPlanDto {
    private Product product;
    private Plan plan;
    private String description;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
