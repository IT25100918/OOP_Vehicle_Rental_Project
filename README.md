# Prime Vehicle Rentals

## Requirements
- Java 21 (JDK, not JRE) — download from https://adoptium.net
- Maven 3.8+ — download from https://maven.apache.org/download.cgi

Check you have both installed:
```
java -version
mvn -version
```

---

## How to Run

### Step 1 — Open a terminal inside the backend folder
```
cd vehicle-rental-structured/backend
```

### Step 2 — Run with Maven (first run downloads dependencies, takes ~2 min)
```
mvn spring-boot:run
```

### Step 3 — Open the app
```
http://localhost:8080
```

---

## Login Credentials

| Role        | Email                              | Password    |
|-------------|------------------------------------|-------------|
| Super Admin | admin@primevehiclerentals.com      | Admin@2026  |
| Admin       | admin@admin.com                    | admin123    |
| User        | kaweesha@email.com                 | pass123     |

---

## Common Errors

### "Could not find or load main class"
You are NOT inside the `backend/` folder, or you ran `java -jar` before building.
Fix: `cd vehicle-rental-structured/backend` then `mvn spring-boot:run`

### "No such file: data/users.txt"
You ran the app from the wrong directory.
Fix: Always run from inside the `backend/` folder where the `data/` folder lives.

### Port 8080 already in use
Another app is using port 8080. Either stop it, or run on a different port:
```
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=9090
```
Then open http://localhost:9090

### Maven not found
Install Maven from https://maven.apache.org/download.cgi
Or on Windows with Chocolatey: `choco install maven`
Or on Mac with Homebrew: `brew install maven`

---

## Project Structure

```
vehicle-rental-structured/
├── backend/                          ← Run from HERE
│   ├── pom.xml
│   ├── data/                         ← Flat-file database
│   │   ├── admins.txt
│   │   ├── bookings.txt
│   │   ├── payments.txt
│   │   ├── reviews.txt
│   │   ├── users.txt
│   │   └── vehicles.txt
│   └── src/main/
│       ├── java/com/vehiclerental/
│       │   ├── VehicleRentalApplication.java
│       │   ├── admin/
│       │   ├── booking/
│       │   ├── customer/
│       │   ├── payment/
│       │   ├── review/
│       │   ├── shared/
│       │   └── vehicle/
│       └── resources/
│           ├── application.properties
│           └── templates/
└── frontend/web/                     ← CSS and JS modules
    ├── css/
    └── js/
```
