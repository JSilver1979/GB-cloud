package ru.geekbrains.cloud.cloudapp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class FileInfo {
    public enum FileType {
        FILE("F"), DIRECTORY("D");

        private String name;

        public String getName() {
            return name;
        }

        FileType(String name) {
            this.name = name;
        }
    }

    private String fileName;
    private FileType type;
    private long fileSize;
    private LocalDateTime fileModifiedAt;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public LocalDateTime getFileModifiedAt() {
        return fileModifiedAt;
    }

    public void setFileModifiedAt(LocalDateTime fileModifiedAt) {
        this.fileModifiedAt = fileModifiedAt;
    }

    public FileInfo(Path path) {
        try {
            this.fileName = path.getFileName().toString();
            this.fileSize = Files.size(path);
            if (Files.isDirectory(path)) {
                this.type = FileType.DIRECTORY;
                this.fileSize = -1L;
            } else  {
                this.type = FileType.FILE;
            }
            this.fileModifiedAt = LocalDateTime.ofInstant(Files.getLastModifiedTime(path).toInstant(), ZoneId.systemDefault());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
