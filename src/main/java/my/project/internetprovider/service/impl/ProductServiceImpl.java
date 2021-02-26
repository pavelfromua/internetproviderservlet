package my.project.internetprovider.service.impl;

import my.project.internetprovider.db.dao.DaoFactory;
import my.project.internetprovider.db.dao.ProductDao;
import my.project.internetprovider.db.entity.Product;
import my.project.internetprovider.exception.CheckException;
import my.project.internetprovider.service.ProductService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Product create(Product product) throws CheckException {
        String name = product.getName();

        try (ProductDao dao = daoFactory.createProductDao(testMode)) {
            Map<String, String> messages = new HashMap<>();

            if (name.length() < 2 || name.length() > 120)
                messages.put("name", "new.item.product.name.size");

            if (name.isEmpty())
                messages.put("name", "new.item.product.name.notEmpty");

            if (messages.size() > 0)
                throw new CheckException(CheckException.fromMultipleToSingleMessage(messages));

            return dao.create(product);
        }
    }

    @Override
    public Product findById(Long id) throws CheckException {
        try (ProductDao dao = daoFactory.createProductDao(testMode)) {
            Product product = dao.findById(id).orElseThrow(() ->
                    new CheckException("productNotFound"));

            return product;
        }
    }

    @Override
    public void update(Product product) throws CheckException {
        String name = product.getName();

        try (ProductDao dao = daoFactory.createProductDao(testMode)) {
            Map<String, String> messages = new HashMap<>();

            if (name.length() < 2 || name.length() > 120)
                messages.put("name", "edit.item.product.name.size");

            if (name.isEmpty())
                messages.put("name", "edit.item.product.name.notEmpty");

            if (messages.size() > 0)
                throw new CheckException(CheckException.fromMultipleToSingleMessage(messages));

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
