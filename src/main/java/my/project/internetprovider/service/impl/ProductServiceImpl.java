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
    DaoFactory daoFactory = DaoFactory.getInstance();

    @Override
    public List<Product> findAll() {
        List<Product> products;

        try (ProductDao dao = daoFactory.createProductDao()) {
            products = dao.findAll();
        }

        return products;
    }

    @Override
    public Product create(Product product) {
        try (ProductDao dao = daoFactory.createProductDao()) {
            return dao.create(product);
        }
    }

    @Override
    public Product findById(Long id) throws NotFoundException {
        try (ProductDao dao = daoFactory.createProductDao()) {
            Product product = dao.findById(id).orElseThrow(() ->
                    new NotFoundException("Product not found"));

            return product;
        }
    }

    @Override
    public void update(Product product) throws UpdateException {
        try (ProductDao dao = daoFactory.createProductDao()) {
            if (product.getName().isEmpty()) {
                throw new UpdateException("Name can't be empty");
            }
            dao.update(product);
        }
    }

    @Override
    public void delete(Long id) {
        try (ProductDao dao = daoFactory.createProductDao()) {
            dao.delete(id);
        }
    }
}
