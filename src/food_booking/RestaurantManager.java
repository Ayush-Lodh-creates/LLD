package food_booking;

import food_booking.entities.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RestaurantManager {

    private Map<Integer, Restaurant> restaurantMap;
    private static RestaurantManager instance;
    public final AtomicInteger orderIdCounter = new AtomicInteger(1);

    private RestaurantManager() {
        restaurantMap = new ConcurrentHashMap<>();
    }

    public static RestaurantManager getInstance() {
        if(instance == null) {
            instance = new RestaurantManager();
        }
        return instance;
    }

    public void addRestaurant(Restaurant restaurant) {
        if(restaurantMap.containsKey(restaurant.getId())) {
            throw new IllegalArgumentException("Restaurant with id " + restaurant.getId() + " already exists.");
        }
        restaurantMap.put(restaurant.getId(), restaurant);
    }

    public Restaurant searchRestaurant(String address) {
        for(Map.Entry<Integer, Restaurant> entry : restaurantMap.entrySet()) {
            if(entry.getValue().getAddress().equalsIgnoreCase(address)) {
                System.out.println("Restaurant found: " + entry.getValue().getName());
                return entry.getValue();
            }
        }
        return null;
    }

    public void bookFood(Restaurant restaurant, Map<Integer, Integer> itemToQuantityMap, User user) {
        Map<Integer, MenuItem> itemMenuMap = restaurant.getMenuItems().stream().collect(java.util.stream.Collectors.toMap(MenuItem::getCode, item -> item));
        if(itemToQuantityMap.isEmpty()) {
            throw new IllegalArgumentException("Selected items for cart cannot be empty");
        }
        double total = 0;
        for(Map.Entry<Integer, Integer> entry : itemToQuantityMap.entrySet()) {
            if(!itemMenuMap.containsKey(entry.getKey())) {
                throw new IllegalArgumentException("Menu item " + entry.getKey() + " is not available in restaurant " + restaurant.getName());
            } else {
                total += itemMenuMap.get(entry.getKey()).getPrice() * entry.getValue();
            }
        }
        Cart cart = user.getCart();
        if(!cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("User " + user.getName() + " already has items in the cart.");
        }
        cart.addItemsToCart(restaurant, itemToQuantityMap, total);
    }

    public void confirmCart(Cart cart) {
        Restaurant restaurant = cart.getRestaurant();
        Map<Integer, Integer> itemsQuantityMap = cart.getItems();
        if(restaurant == null || itemsQuantityMap.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty or restaurant is not selected.");
        }
        Map<Integer, MenuItem> itemMenuMap = restaurant.getMenuItems().stream().collect(java.util.stream.Collectors.toMap(MenuItem::getCode, item -> item));
        double total = 0;
        Map<Integer, Integer> updatedQuantity = new HashMap<>();
        for(Map.Entry<Integer, Integer> entry : itemsQuantityMap.entrySet()) {
            if(!itemMenuMap.containsKey(entry.getKey())) {
                throw new IllegalArgumentException("Menu item " + entry.getKey() + " is not available in restaurant " + restaurant.getName());
            } else {
                int minQuantity = Math.min(entry.getValue(), itemMenuMap.get(entry.getKey()).getQuantity());
                total += itemMenuMap.get(entry.getKey()).getPrice() * minQuantity;
                updatedQuantity.put(entry.getKey(), minQuantity);
                MenuItem item = itemMenuMap.get(entry.getKey());
                item.setQuantity(item.getQuantity() - minQuantity);
            }
        }
        cart.addItemsToCart(restaurant, updatedQuantity, total);
    }

    public void cancelOrder(Order order) {
        Restaurant restaurant = order.getRestaurant();
        Map<Integer, Integer> itemsQuantityMap = order.getItemsToQuantity();
        Map<Integer, MenuItem> itemMenuMap = restaurant.getMenuItems().stream().collect(java.util.stream.Collectors.toMap(MenuItem::getCode, item -> item));
        for(Map.Entry<Integer, Integer> entry : itemsQuantityMap.entrySet()) {
            if(itemMenuMap.containsKey(entry.getKey())) {
                MenuItem item = itemMenuMap.get(entry.getKey());
                item.setQuantity(item.getQuantity() + entry.getValue());
            }
        }
    }
}
