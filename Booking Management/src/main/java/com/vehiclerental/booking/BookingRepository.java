package com.vehiclerental.booking;

import com.vehiclerental.shared.FileRepository;
import org.springframework.stereotype.Repository;

@Repository
    //inheritance
public class BookingRepository extends FileRepository<Booking> {

    private static final String FILE_PATH = "data/bookings.txt";
//polymorphysm
    @Override protected String getFilePath() { return FILE_PATH; }
    @Override protected Booking fromLine(String line) { return Booking.fromFileString(line); }
    @Override protected String toLine(Booking b) { return b.toFileString(); }
}
