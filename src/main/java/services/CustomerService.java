package services;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import entities.Customer;
import entities.Product;

public class CustomerService implements CustomerServiceInterface {

	private List<Customer> customers;

	public CustomerService(List<Customer> customers) {
		this.customers = customers;
	}

	@Override
	public List<Customer> findByName(String name) {
		return customers.stream()
				.filter(c -> c.getName().equals(name))
				.collect(Collectors.toList());
	}

	@Override
	public List<Customer> findByField(String fieldName, Object value) {
		return customers.stream().filter(p -> {
			try {
				Field f = p.getClass().getDeclaredField(fieldName);
				f.setAccessible(true);
				return f.get(p).equals(value);
			} catch (NoSuchFieldException | IllegalAccessException e){
				return false;
			}
		}).collect(Collectors.toList());
	}

	@Override
	public List<Customer> customersWhoBoughtMoreThan(int number) {
		return customers.stream()
				.filter(c -> c.getBoughtProducts().size() > number)
				.collect(Collectors.toList());
	}

	@Override
	public List<Customer> customersWhoSpentMoreThan(double price) {
		return customers.stream()
				.filter(c -> c.getBoughtProducts().stream()
						.map(p -> price)
						.reduce(Double::sum)
						.orElse(0.0) > price)
				.collect(Collectors.toList());
	}

	@Override
	public List<Customer> customersWithNoOrders() {
		return customers.stream()
				.filter(c -> c.getBoughtProducts().isEmpty())
				.collect(Collectors.toList());
	}

	@Override
	public void addProductToAllCustomers(Product p) {
		customers.stream().forEach(c -> c.addProduct(p));

	}

	@Override
	public double avgOrders(boolean includeEmpty) {
		double total = customers.stream()
				       .map(Customer::getBoughtProducts)
				       .flatMap(List::stream)
				       .map(Product::getPrice)
				       .reduce(Double::sum)
				       .orElse(0.0);

		int customerCount = customers.stream()
				.filter(c -> ! c.getBoughtProducts().isEmpty() || includeEmpty)
				.map(c -> 1)
				.reduce(Integer::sum).orElse(0);

		return total / customerCount;
	}

	@Override
	public boolean wasProductBought(Product p) {
		return customers.stream().anyMatch(c -> c.getBoughtProducts().contains(p));
	}

	@Override
	public List<Product> mostPopularProduct() {
		// This method return list of Products, but only with single, most popular item.
		Product mostPopular = customers.stream()
				.map(Customer::getBoughtProducts)
				.flatMap(List::stream)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
				.entrySet().stream()
				.max(Comparator.comparing(Map.Entry::getValue)).get().getKey();

		List<Product> mostPopularProducts = new ArrayList<>();
		mostPopularProducts.add(mostPopular);
		return mostPopularProducts;
	}

	@Override
	public int countBuys(Product p) {
		return customers.stream()
				.filter(c -> c.getBoughtProducts().contains(p))
				.map(Customer::getBoughtProducts)
				.flatMap(List::stream)
				.filter(i -> i.equals(p))
				.map(i -> 1)
				.reduce(Integer::sum).orElse(0);
	}

	@Override
	public int countCustomersWhoBought(Product p) {
		return customers.stream()
				.filter(c -> c.getBoughtProducts().contains(p))
				.collect(Collectors.toList()).size();
	}

}
