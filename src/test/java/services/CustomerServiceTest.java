package services;

import entities.Customer;
import entities.Product;
import org.junit.Test;
import tests.DataProducer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Mateusz on 27.04.2017.
 */
public class CustomerServiceTest {

    private final static int CUSTOMER_COUNT = 10;

    @Test
    public void testFindByName() {
        CustomerServiceInterface cs = new CustomerService(DataProducer.getTestData(CUSTOMER_COUNT));
        List<Customer> res = cs.findByName("Customer: 1");

        assertNotNull("Result can't be null", res);
        assertEquals(1, res.size());
    }

    @Test
    public void findByField() throws Exception {
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < CUSTOMER_COUNT; i++) {
            customers.add(new Customer(i, "Customer: " + i));
        }

        CustomerServiceInterface cs = new CustomerService(customers);
        List<Customer> res = cs.findByField("id", 0);
        assertEquals(1, res.size());
        assertTrue(res.contains(customers.get(0)));
    }

    @Test
    public void customersWhoBoughtMoreThan() throws Exception {
        CustomerServiceInterface cs = new CustomerService(DataProducer.getTestData(CUSTOMER_COUNT));
        List<Customer> res = cs.customersWhoBoughtMoreThan(1);

        assertNotNull("Result can't be null", res);
        assertEquals(CUSTOMER_COUNT - 4, res.size());
    }

    @Test
    public void customersWhoSpentMoreThan() throws Exception {
        List<Customer> customers = new ArrayList<>();
        Product product1 = new Product(0, "Test product", 1);
        Product product2 = new Product(1, "Test product2", 10);
        for (int i = 0; i < CUSTOMER_COUNT; i++) {
            customers.add(new Customer(i, "Customer: " + i));
        }
        for (int i = 0; i < CUSTOMER_COUNT - 5; i++) {
            customers.get(i).addProduct(product1);
        }

        customers.get(0).addProduct(product2);
        customers.get(1).addProduct(product2);

        CustomerServiceInterface cs = new CustomerService(customers);
        List<Customer> res = cs.customersWhoSpentMoreThan(10);
        assertEquals(2, res.size());
    }

    @Test
    public void customersWithNoOrders() throws Exception {
        CustomerServiceInterface cs = new CustomerService(DataProducer.getTestData(CUSTOMER_COUNT));
        List<Customer> res = cs.customersWithNoOrders();

        assertNotNull("Result can't be null", res);
        assertEquals(3, res.size());
    }

    @Test
    public void addProductToAllCustomers() throws Exception {
        List<Customer> customers = new ArrayList<>();
        Product product = new Product(0, "Test product", 1);
        for (int i = 0; i < CUSTOMER_COUNT; i++) {
            customers.add(new Customer(i, "Customer: " + i));
        }

        CustomerServiceInterface cs = new CustomerService(customers);
        cs.addProductToAllCustomers(product);
        for(Customer c : customers){
            assertTrue(c.getBoughtProducts().contains(product));
        }
    }

    @Test
    public void avgOrders() throws Exception {
        List<Customer> customers = new ArrayList<>();
        int productsCount = CUSTOMER_COUNT - 5;

        Product product = new Product(0, "Test product", 2);
        for (int i = 0; i < CUSTOMER_COUNT; i++) {
            customers.add(new Customer(i, "Customer: " + i));
        }
        for (int i = 0; i < CUSTOMER_COUNT - 5; i++) {
            customers.get(i).addProduct(product);
        }

        double expectedAvgNotIncludeEmpty = product.getPrice();
        double expectedAvgIncludeEmpty = (product.getPrice() * productsCount) / CUSTOMER_COUNT;

        CustomerServiceInterface cs = new CustomerService(customers);
        double avgNotIncludeEmpty = cs.avgOrders(false);
        double avgIncludeEmpty = cs.avgOrders(true);
        assertTrue(expectedAvgNotIncludeEmpty == avgNotIncludeEmpty);
        assertTrue(expectedAvgIncludeEmpty == avgIncludeEmpty);
    }

    @Test
    public void wasProductBought() throws Exception {
        List<Customer> customers = new ArrayList<>();
        Product product = new Product(0, "Test product", 1);
        for (int i = 0; i < CUSTOMER_COUNT; i++) {
            customers.add(new Customer(i, "Customer: " + i));
        }
        for (int i = 0; i < CUSTOMER_COUNT - 5; i++) {
            customers.get(i).addProduct(product);
        }

        CustomerServiceInterface cs = new CustomerService(customers);
        boolean bought = cs.wasProductBought(product);
        assertTrue(bought);
    }

    @Test
    public void mostPopularProduct() throws Exception {
        List<Customer> customers = new ArrayList<>();
        Product popularProduct = new Product(0, "Test product", 1);
        Product notPopularProduct = new Product(1, "Test product2", 2);

        int popularProductCount = CUSTOMER_COUNT - 5;
        int notPopularProductCount = popularProductCount - 1;
        for (int i = 0; i < CUSTOMER_COUNT; i++) {
            customers.add(new Customer(i, "Customer: " + i));
        }
        for (int i = 0; i < popularProductCount; i++) {
            customers.get(i).addProduct(popularProduct);
        }
        for (int i = 0; i < notPopularProductCount; i++) {
            customers.get(i).addProduct(notPopularProduct);
        }

        CustomerServiceInterface cs = new CustomerService(customers);
        Product foundMostPopularProduct = cs.mostPopularProduct().get(0);
        assertEquals(popularProduct, foundMostPopularProduct);
    }

    @Test
    public void countBuys() throws Exception {
        List<Customer> customers = new ArrayList<>();
        Product product = new Product(0, "Test product", 1);
        for (int i = 0; i < CUSTOMER_COUNT; i++) {
            customers.add(new Customer(i, "Customer: " + i));
        }
        for (int i = 0; i < CUSTOMER_COUNT - 5; i++) {
            customers.get(i).addProduct(product);
        }

        CustomerServiceInterface cs = new CustomerService(customers);
        int buys = cs.countBuys(product);
        assertEquals(CUSTOMER_COUNT - 5, buys);
    }

    @Test
    public void countCustomersWhoBought() throws Exception {
        List<Customer> customers = new ArrayList<>();
        Product product = new Product(0, "Test product", 1);
        for (int i = 0; i < CUSTOMER_COUNT; i++) {
            customers.add(new Customer(i, "Customer: " + i));
        }
        for (int i = 0; i < CUSTOMER_COUNT - 5; i++) {
            customers.get(i).addProduct(product);
        }

        CustomerServiceInterface cs = new CustomerService(customers);
        int customersWhoBought = cs.countCustomersWhoBought(product);
        assertEquals(CUSTOMER_COUNT - 5, customersWhoBought);

    }

}