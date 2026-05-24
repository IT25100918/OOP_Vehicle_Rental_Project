package com.vehiclerental.customer;

import com.vehiclerental.shared.FileRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends FileRepository<User> {

    private static final String FILE_PATH = "data/users.txt";

    @Override protected String getFilePath() { return FILE_PATH; }
    @Override protected User fromLine(String line) { return User.fromFileString(line); }
    @Override protected String toLine(User u) { return u.toFileString(); }
}
