package com.mysite.ordermanagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

enum PaymentType {
    CREDIT_CARD, CASH, CHECK, OTHER
}

enum CustomerType {
    REGULAR, VIP
}

enum OrderType {
    REGULAR, VIP
}

interface CustomerGift {
    void openGift();
}

class Gift implements CustomerGift {
    @Override
    public void openGift() {
        System.out.println("Congratulations! you got a new gift! Enjoy!");
    }
}

class OrderItem {
    private final String id;
    private final String itemName;
    private final double itemPrice;

    public OrderItem(String itemName, double itemPrice) {
        this.id = UUID.randomUUID().toString();
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public String getId() { return id; }
    public String getItemName() { return itemName; }
    public double getItemPrice() { return itemPrice; }
}

class Customer {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String deliveryAddress;
    private final CustomerType type;
    private Double discount;
    private final List<OrderItem> favoriteItems = new ArrayList<>();
    private CustomerGift gift;

    public Customer(String firstName, String lastName, String email, String deliveryAddress, CustomerType type) {
        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.deliveryAddress = deliveryAddress;
        this.type = type;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public CustomerType getType() { return type; }
    public Double getDiscount() { return discount; }
    public List<OrderItem> getFavoriteItems() { return favoriteItems; }
    public CustomerGift getGift() { return gift; }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public void openMyGift() {
        if (gift != null) {
            gift.openGift();
        } else {
            System.out.println("Sorry, you don't have any gift to open!");
        }
    }

    public void addFavoriteItem(OrderItem item) {
        if (!favoriteItems.contains(item)) {
            favoriteItems.add(item);
        }
    }

    public void removeFavoriteItem(OrderItem item) {
        favoriteItems.remove(item);
    }

    public void takeGift(CustomerGift gift) {
        this.gift = gift;
    }
}

class Order {
    private final String id;
    private final String name;
    private final String deliveryAddress;
    private final List<OrderItem> items;
    private final Customer customer;
    private final PaymentType paymentType;
    private final Date orderDate;
    private double totalPrice;
    private final OrderType orderType;

    public Order(String name, String deliveryAddress, List<OrderItem> items, Customer customer, PaymentType paymentType, OrderType orderType) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.deliveryAddress = deliveryAddress;
        this.items = items;
        this.customer = customer;
        this.paymentType = paymentType;
        this.orderDate = new Date();
        this.orderType = orderType;

        if (this.orderType == OrderType.VIP && customer.getType() != CustomerType.VIP) {
            throw new IllegalArgumentException("Only VIP customers can make a VIP order.");
        }

        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        for (OrderItem item : items) {
            totalPrice += item.getItemPrice();
            customer.addFavoriteItem(item);  // Update favorite items
        }

        if (orderType == OrderType.VIP) {
            totalPrice = totalPrice - (totalPrice * customer.getDiscount() / 100);
        }
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public List<OrderItem> getItems() { return items; }
    public Customer getCustomer() { return customer; }
    public PaymentType getPaymentType() { return paymentType; }
    public Date getOrderDate() { return orderDate; }
    public double getTotalPrice() { return totalPrice; }
}

class OrderManagementSystem {
    public static void main(String[] args) {
        OrderItem item1 = new OrderItem("Laptop", 1000.0);
        OrderItem item2 = new OrderItem("Mobile Phone", 500.0);

        List<OrderItem> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        Customer customer1 = new Customer("John", "Doe", "john@example.com", "123 Street", CustomerType.VIP);
        customer1.setDiscount(10.0);  // 10% de réduction

        Order order1 = new Order("Commande de John", "123 Rue", items, customer1, PaymentType.CREDIT_CARD, OrderType.VIP);
        System.out.println("Prix total pour la commande1: " + order1.getTotalPrice());

        Gift gift = new Gift();
        customer1.takeGift(gift);
        customer1.openMyGift();
    }
}

