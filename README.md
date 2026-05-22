# Component 7: Review Management

## What This Component Contains
Full standalone Spring Boot application focused on **customer review management**:

### Review Features
- **List Reviews** — Admins see all reviews; users see only their own
- **Add Review** — Rate a vehicle (1–5 stars) with a comment
- **Edit Review** — Update comment and rating
- **Approve Review** — Admin approves pending reviews
- **Delete Review** — Admin or review owner can delete
- **Sort by Rating** — Selection sort descending by star rating

### Key Files
- `review/Review.java` — Review entity (id, userId, vehicleId, userName, vehicleName, rating, comment, reviewDate, status)
- `review/ReviewController.java` — All review routes
- `review/ReviewService.java` — Business logic + rating sort
- `review/ReviewRepository.java` — File persistence (`data/reviews.txt`)
- `templates/review/` — index, add-review, edit-review pages

## How to Run
```bash
cd backend
mvn spring-boot:run
```
Visit: http://localhost:8080/reviews

Log in first at: http://localhost:8080/login
