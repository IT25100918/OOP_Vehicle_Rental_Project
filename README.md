# Booking Component

## Files Included

### Java Backend
- `Booking.java` — Entity model with file serialization
- `BookingRepository.java` — File-based persistence (extends FileRepository)
- `BookingService.java` — Business logic (create, confirm, cancel, complete, delete, sort)
- `BookingController.java` — Spring MVC controller (GET/POST endpoints)

### Shared Dependencies
- `FileRepository.java` — Generic file-based CRUD helper
- `LinkedList.java` — Custom DSA linked list used in service
- `FieldCodec.java` — Encodes/decodes comma-delimited fields safely

### Templates
- `booking/index.html` — List all bookings
- `booking/add-booking.html` — Create new booking form
- `booking/edit-booking.html` — Edit booking dates

### Data
- `data/bookings.txt` — Seed data file

## Endpoints
| Method | Path | Description |
|--------|------|-------------|
| GET | /bookings | List bookings |
| GET | /bookings/add | Show add form |
| POST | /bookings/add | Create booking → redirects to payment |
| GET | /bookings/edit/{id} | Show edit form (admin only) |
| POST | /bookings/update | Update dates (admin only) |
| POST | /bookings/confirm/{id} | Confirm booking (admin only) |
| POST | /bookings/cancel/{id} | Cancel booking |
| POST | /bookings/complete/{id} | Complete booking (admin only) |
| POST | /bookings/delete/{id} | Delete booking (admin only) |

## External Dependencies
BookingService uses `VehicleService` and BookingController uses `UserService`.
These must be present in the same Spring context.
