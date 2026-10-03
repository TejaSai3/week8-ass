import java.util.ArrayList;
import java.util.List;

enum OrderStatus {
    PENDING,
    PAID
}

interface PaymentMethod {
    boolean processPayment(double amount);
}

class CreditCardPayment implements PaymentMethod {
    private String cardNumber;

    public CreditCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing credit card payment of $" + amount);
        return true;
    }
}

class PayPalPayment implements PaymentMethod {
    private String email;

    public PayPalPayment(String email) {
        this.email = email;
    }

    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing PayPal payment of $" + amount);
        return false;
    }
}

class Product {
    private String name;
    private double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}

class OrderItem {
    private Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }
}

class Customer {
    private String name;

    public Customer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class Order {
    private Customer customer;
    private List<OrderItem> items = new ArrayList<>();
    private OrderStatus status;

    public Order(Customer customer) {
        this.customer = customer;
        this.status = OrderStatus.PENDING;
        System.out.println("Order created for " + customer.getName() + ".");
    }

    public void addItem(Product product, int quantity) {
        items.add(new OrderItem(product, quantity));
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public double calculateTotal() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void processPayment(PaymentMethod paymentMethod, String methodType) {
        if (isEmpty()) {
            System.out.println("Cannot process payment for an empty order.");
            return;
        }

        System.out.println("Payment initiated via " + methodType + " for Order " + customer.getName() + ".");
        boolean success = paymentMethod.processPayment(calculateTotal());

        if (success) {
            this.status = OrderStatus.PAID;
            System.out.println("Payment for Order " + customer.getName() + " successful. Order status: " + status + ".");
        } else {
            System.out.println("Payment for Order " + customer.getName() + " failed. Order status: " + status + ".");
        }
    }
}

public class MainPaymentProcessing {
    public static void main(String[] args) {
        Product prodA = new Product("Product A", 50.0);
        Product prodB = new Product("Product B", 30.0);
        Product prodC = new Product("Product C", 100.0);

        Customer customerX = new Customer("X");
        Order orderX = new Order(customerX);
        orderX.addItem(prodA, 2);
        orderX.addItem(prodB, 1);
        orderX.processPayment(new CreditCardPayment("1234-5678-9012-3456"), "Credit Card");

        Customer customerY = new Customer("Y");
        Order orderY = new Order(customerY);
        orderY.processPayment(new CreditCardPayment("1111-2222-3333-4444"), "Credit Card");

        Customer customerZ = new Customer("Z");
        Order orderZ = new Order(customerZ);
        orderZ.addItem(prodC, 1);
        orderZ.processPayment(new PayPalPayment("userZ@example.com"), "PayPal");
    }
}