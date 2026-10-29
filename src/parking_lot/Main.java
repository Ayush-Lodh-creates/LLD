package parking_lot;

import parking_lot.entities.Parking;
import parking_lot.entities.Ticket;
import parking_lot.entities.vehicle.Vehicle;
import parking_lot.entities.discount_responsibility.MembershipManager;
import parking_lot.entities.discount_responsibility.FlatManager;
import parking_lot.entities.price_decorator.*;
import parking_lot.enums.*;
import parking_lot.factory.VehicleFactory;
import parking_lot.strategy.HourlyPriceStrategy;
import parking_lot.strategy.PeakPriceStrategy;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        Parking parking = new Parking(2, 8);
        ExecutorService workers = Executors.newFixedThreadPool(4);
        try {
            Vehicle bike = VehicleFactory.createVehicle("BIKE-1", "Bike", VehicleType.TWO_WHEELER, 1, EngineType.MOTOR);
            Vehicle ev = VehicleFactory.createVehicle("EV-1", "Electric sedan", VehicleType.SEDAN, 1, EngineType.ELECTRIC);
            Vehicle truck = VehicleFactory.createVehicle("TRUCK-1", "Truck", VehicleType.LARGE_VEHICLE, 3, EngineType.MOTOR);
            Vehicle car = VehicleFactory.createVehicle("CAR-1", "Sedan", VehicleType.SEDAN, 1, EngineType.MOTOR);

            // Submit all bookings before waiting. Different vehicles run concurrently.
            CompletableFuture<Ticket> bikeBooking = bookAsync(parking, bike, workers);
            CompletableFuture<Ticket> evBooking = bookAsync(parking, ev, workers);
            CompletableFuture<Ticket> truckBooking = bookAsync(parking, truck, workers);
            CompletableFuture<Ticket> carBooking = bookAsync(parking, car, workers);
            CompletableFuture<Ticket> duplicateBike = bookAsync(parking, bike, workers);
            // Keep the bike reservation active until both competing requests finish.
            CompletableFuture.allOf(bikeBooking, evBooking, truckBooking, carBooking, duplicateBike).join();
            System.out.println("Availability after booking:");
            parking.getBoardDisplayListener().display();

            CompletableFuture<Void> bikeVisit = bikeBooking.thenAcceptAsync(t -> payAndExit(parking, t, false), workers);
            CompletableFuture<Void> otherBikeVisit = duplicateBike.thenAcceptAsync(t -> payAndExit(parking, t, false), workers);
            CompletableFuture<Void> evVisit = evBooking.thenAcceptAsync(t -> payAndExit(parking, t, true), workers);
            CompletableFuture<Void> cancellation = truckBooking.thenAcceptAsync(t -> {
                if (t != null) parking.cancelTicket(t);
            }, workers);
            CompletableFuture<Void> expiry = carBooking.thenAcceptAsync(t -> {
                if (t == null) return;
                // Demo only: this ticket has no other users. Simulate an old hold without sleeping.
                t.setEntryAt(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(16));
                parking.expireTickets();
            }, workers);
            // Wait for dependent lifecycle operations as well as booking.
            CompletableFuture.allOf(bikeVisit, otherBikeVisit, evVisit, cancellation, expiry).join();
            System.out.println("Final availability (expected: 2W=2, 4W=8):");
            parking.getBoardDisplayListener().display();
        } finally {
            workers.shutdown();
            try {
                if (!workers.awaitTermination(30, TimeUnit.SECONDS)) workers.shutdownNow();
            } catch (InterruptedException e) {
                workers.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    private static CompletableFuture<Ticket> bookAsync(Parking parking, Vehicle vehicle, ExecutorService workers) {
        return CompletableFuture.supplyAsync(() -> {
            Ticket ticket = parking.bookSlots(vehicle, BookingType.RESERVATION);
            if (ticket == null) {
                System.out.println("Booking rejected or unavailable: " + vehicle.getVehicleNumber());
            } else {
                System.out.println("Ticket received: " + ticket.getTicketId() + " for " + vehicle.getVehicleNumber());
            }
            return ticket;
        }, workers);
    }

    private static void payAndExit(Parking parking, Ticket ticket, boolean chargingAndValet) {
        if (ticket == null) return;
        Price price = new BasePrice(chargingAndValet ? new PeakPriceStrategy() : new HourlyPriceStrategy(), ticket);
        if (chargingAndValet) price = new ValetDecorator(new EVDecorator(price));
        System.out.println("Quote for " + ticket.getVehicleNumber() + ": " + price.getPrice() + " | " + price.getDescription());
        MembershipManager discounts = new MembershipManager();
        discounts.setNextManager(new FlatManager());
        // Discounts currently print a quote; processPayment does not accept a billed amount.
        discounts.processDiscount(price, DiscountCoupon.MEMBERSHIP);
        parking.processPayment(ticket);
        parking.freeSlots(ticket);
    }
}
