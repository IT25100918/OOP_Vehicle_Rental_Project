# 05 — Booking Management

## Overview
Manages the full booking lifecycle — create, confirm, cancel, complete. Uses a custom LinkedList (DSA) for loading booking records. Fully functional standalone Spring Boot application.

## Features

| Route | Method | Description |
|---|---|---|
| `/bookings` | GET | List bookings (admin: all; user: own) |
| `/bookings/add` | GET/POST | Create booking → redirects to payment |
| `/bookings/edit/{id}` | GET | Edit booking form (admin only) |
| `/bookings/update` | POST | Update dates/status (admin only) |
| `/bookings/confirm/{id}` | POST | Active → Confirmed |
| `/bookings/cancel/{id}` | POST | Cancel booking, release vehicle |
| `/bookings/complete/{id}` | POST | Mark Completed, release vehicle |
| `/bookings/delete/{id}` | POST | Hard delete (admin only) |

## Booking Status Flow
```
Active → Confirmed → Completed
       ↓
    Cancelled
```

## Key Files
- `booking/Booking.java` — Entity with id, userId, vehicleId, userName, vehicleName, startDate, endDate, status, totalPrice
- `booking/BookingController.java` — MVC routes + PaymentService for paidBookingIds
- `booking/BookingService.java` — Uses custom `LinkedList<Booking>` to load records (DSA requirement)
- `booking/BookingRepository.java` — File persistence → `data/bookings.txt`
- `shared/LinkedList.java` — Custom DSA linked list

## How to Run
```bash
mvn spring-boot:run
```
Visit: http://localhost:8080/bookings

## GitHub Setup
```bash
git init
git add .
git commit -m "feat: booking lifecycle management module"
git remote add origin https://github.com/YOUR_USERNAME/05-booking-management.git
git push -u origin main
```
