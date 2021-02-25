package my.project.internetprovider.service.impl;

import my.project.internetprovider.db.dao.DaoFactory;
import my.project.internetprovider.db.dao.ProductDao;
import my.project.internetprovider.db.entity.Product;
import my.project.internetprovider.exception.NotFoundException;
import my.project.internetprovider.exception.RegistrationException;
import my.project.internetprovider.exception.UpdateException;
import my.project.internetprovider.service.ProductService;

import java.util.List;

public class ProductServiceImpl implements ProductService {
    private boolean testMode = false;
    DaoFactory daoFactory = DaoFactory.getInstance();

    public ProductServiceImpl() {}

    public ProductServiceImpl(boolean testMode) {
        this.testMode = testMode;
    }

    @Override
    public List<Product> findAll() {
        List<Product> products;

        try (ProductDao dao = daoFactory.createProductDao(testMode)) {
            products = dao.findAll();
        }

        return products;
    }

    @Override
    public Product create(Product product) {
        try (ProductDao dao = daoFactory.createProductDao(testMode)) {
            return dao.create(product);
        }
    }

    @Override
    public Product findById(Long id) throws NotFoundException {
        try (ProductDao dao = daoFactory.createProductDao(testMode)) {
            Product product = dao.findById(id).orElseThrow(() ->
                    new NotFoundException("Product not found"));

            return product;
        }
    }

    @Override
    public void update(Product product) throws UpdateException {
        try (ProductDao dao = daoFactory.createProductDao(testMode)) {
            if (product.getName().isEmpty()) {
                throw new UpdateException("Name can't be empty");
            }
            dao.update(product);
        }
    }

    @Override
    public void delete(Long id) {
        try (ProductDao dao = daoFactory.createProductDao(testMode)) {
            dao.delete(id);
        }
    }
}
