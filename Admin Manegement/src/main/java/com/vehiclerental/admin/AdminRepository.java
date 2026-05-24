package com.vehiclerental.admin;

import com.vehiclerental.shared.FileRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AdminRepository extends FileRepository<Admin> {

    private static final String FILE_PATH = "data/admins.txt";

    @Override protected String getFilePath() { return FILE_PATH; }
    @Override protected Admin fromLine(String line) { return Admin.fromFileString(line); }
    @Override protected String toLine(Admin a) { return a.toFileString(); }
}
