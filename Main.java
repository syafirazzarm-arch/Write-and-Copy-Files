
package handson7;

import java.io.File;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Menentukan lokasi file
        File report = new File("handson7", "report.txt");
        File backupOld = new File("handson7", "backup_report.txt");

        Path source = Paths.get("handson7", "report.txt");
        Path backup = Paths.get("handson7", "backup", "report.txt");
        Path logPath = Paths.get("handson7", "log.txt");

        // Menyimpan catatan operasi
        List<String> logs = new ArrayList<>();


        // =====================================
        // 1. PRE-NIO.2: WRITE FILE
        // =====================================

        System.out.println("=== PRE-NIO.2: WRITE FILE ===");

        try {
            FileWriter writer = new FileWriter(report);

            writer.write("Report Generated\nStatus: OK");

            writer.close();

            System.out.println("Laporan berhasil dibuat!");
            logs.add("Pre-NIO.2: Laporan berhasil dibuat.");

        } catch (IOException e) {

            System.out.println("Gagal membuat laporan: " + e.getMessage());
            logs.add("Pre-NIO.2: Gagal membuat laporan.");
        }


        // =====================================
        // 2. PRE-NIO.2: COPY FILE
        // =====================================

        System.out.println("\n=== PRE-NIO.2: COPY FILE ===");

        try {
            FileInputStream fis = new FileInputStream(report);
            FileOutputStream fos = new FileOutputStream(backupOld);

            int data;

            while ((data = fis.read()) != -1) {
                fos.write(data);
            }

            fis.close();
            fos.close();

            System.out.println("Backup berhasil dibuat!");
            System.out.println("Lokasi: " + backupOld.getPath());

            logs.add("Pre-NIO.2: Backup berhasil dibuat.");

        } catch (IOException e) {

            System.out.println("Gagal membuat backup: " + e.getMessage());
            logs.add("Pre-NIO.2: Gagal membuat backup.");
        }


        // =====================================
        // 3. NIO.2: WRITE FILE
        // =====================================

        System.out.println("\n=== NIO.2: WRITE FILE ===");

        try {
            List<String> reportLines = new ArrayList<>();

            reportLines.add("Report Generated");
            reportLines.add("Status: OK");

            Files.write(
                source,
                reportLines,
                StandardCharsets.UTF_8
            );

            System.out.println("Laporan berhasil dibuat menggunakan NIO.2!");

            logs.add("NIO.2: Laporan berhasil dibuat.");

        } catch (IOException e) {

            System.out.println("Gagal membuat laporan: " + e.getMessage());
            logs.add("NIO.2: Gagal membuat laporan.");
        }


        // =====================================
        // 4. NIO.2: CHECK AND COPY FILE
        // =====================================

        System.out.println("\n=== NIO.2: CHECK AND COPY FILE ===");

        try {

            // Memeriksa apakah file sumber tersedia
            if (Files.exists(source)) {

                // Membuat folder backup jika belum tersedia
                if (Files.notExists(backup.getParent())) {
                    Files.createDirectories(backup.getParent());
                }

                // Menyalin file ke folder backup
                Files.copy(
                    source,
                    backup,
                    StandardCopyOption.REPLACE_EXISTING
                );

                System.out.println("Backup berhasil dibuat!");
                System.out.println("Lokasi: " + backup);

                logs.add("NIO.2: File sumber ditemukan dan backup berhasil dibuat.");

            } else {

                System.out.println("File report.txt tidak ditemukan!");

                logs.add("NIO.2: File sumber tidak ditemukan. Backup dibatalkan.");
            }

        } catch (IOException e) {

            System.out.println("Gagal menyalin file: " + e.getMessage());
            logs.add("NIO.2: Gagal menyalin file.");
        }


        // =====================================
        // 5. WRITE OPERATION LOG
        // =====================================

        System.out.println("\n=== OPERATION LOG ===");

        try {

            // Menambahkan hasil operasi ke log.txt
            Files.write(
                logPath,
                logs,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
            );

            System.out.println("Log berhasil disimpan!");
            System.out.println("Lokasi: " + logPath);

        } catch (IOException e) {

            System.out.println("Gagal menyimpan log: " + e.getMessage());
        }
    }
}
