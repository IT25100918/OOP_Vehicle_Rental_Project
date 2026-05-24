package com.vehiclerental.shared;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic file-based persistence helper.
 *
 * READ strategy  — checks these locations in order:
 *   1. External data/ folder relative to working directory (user edits go here)
 *   2. src/main/resources/data/ on the classpath (bundled seed data)
 *
 * WRITE strategy — always writes to external data/ folder next to the jar/project
 *   so edits persist across restarts.
 */
public abstract class FileRepository<T> {

    protected abstract String getFilePath();
    protected abstract T fromLine(String line);
    protected abstract String toLine(T entity);

    /** Find a writable external file, creating it from classpath seed if needed. */
    private File resolveExternal() {
        String fileName = new File(getFilePath()).getName();

        // 1. data/ next to working directory
        File cwd = new File(System.getProperty("user.dir"));
        File external = new File(cwd, "data/" + fileName);
        if (external.exists()) return external;

        // 2. Walk up to find backend/data/
        File dir = cwd;
        for (int i = 0; i < 4; i++) {
            File candidate = new File(dir, "data/" + fileName);
            if (candidate.exists()) return candidate;
            candidate = new File(dir, "backend/data/" + fileName);
            if (candidate.exists()) return candidate;
            if (dir.getParentFile() == null) break;
            dir = dir.getParentFile();
        }

        // 3. Create external file seeded from classpath
        File dataDir = new File(cwd, "data");
        dataDir.mkdirs();
        File target = new File(dataDir, fileName);
        try {
            URL resource = getClass().getClassLoader()
                    .getResource("data/" + fileName);
            if (resource != null) {
                try (InputStream in  = resource.openStream();
                     OutputStream out = new FileOutputStream(target)) {
                    byte[] buf = new byte[4096];
                    int n;
                    while ((n = in.read(buf)) != -1) out.write(buf, 0, n);
                }
                System.out.println("[FileRepository] Seeded " + fileName + " → " + target.getAbsolutePath());
            } else {
                target.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("[FileRepository] Seed failed for " + fileName + ": " + e.getMessage());
        }
        return target;
    }

    public List<T> readAll() {
        List<T> list = new ArrayList<>();
        File file = resolveExternal();
        System.out.println("[FileRepository] Reading: " + file.getAbsolutePath()
                + " (exists=" + file.exists() + ", size=" + file.length() + ")");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) list.add(fromLine(line));
            }
        } catch (IOException e) {
            System.err.println("[FileRepository] Read error: " + e.getMessage());
        }
        System.out.println("[FileRepository] Loaded " + list.size() + " from " + file.getName());
        return list;
    }

    public boolean append(T entity) {
        File file = resolveExternal();
        try (BufferedWriter w = new BufferedWriter(new FileWriter(file, true))) {
            w.write(toLine(entity)); w.newLine(); return true;
        } catch (IOException e) {
            System.err.println("[FileRepository] Append error: " + e.getMessage());
            return false;
        }
    }

    public boolean saveAll(List<T> entities) {
        File file = resolveExternal();
        try (BufferedWriter w = new BufferedWriter(new FileWriter(file, false))) {
            for (T entity : entities) { w.write(toLine(entity)); w.newLine(); }
            return true;
        } catch (IOException e) {
            System.err.println("[FileRepository] Save error: " + e.getMessage());
            return false;
        }
    }
}
