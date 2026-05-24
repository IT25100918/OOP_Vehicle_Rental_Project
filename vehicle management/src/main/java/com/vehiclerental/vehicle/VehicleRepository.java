package com.vehiclerental.vehicle;

import com.vehiclerental.shared.FileRepository;
import org.springframework.stereotype.Repository;

@Repository
public class VehicleRepository extends FileRepository<Vehicle> {

    private static final String FILE_PATH = "data/vehicles.txt";

    @Override
    protected String getFilePath() { return FILE_PATH; }

    @Override
    protected Vehicle fromLine(String line) { return Vehicle.fromFileString(line); }

    @Override
    protected String toLine(Vehicle v) { return v.toFileString(); }
}
