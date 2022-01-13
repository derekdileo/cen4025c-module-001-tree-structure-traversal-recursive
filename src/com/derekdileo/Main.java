package com.derekdileo;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter a directory to scan...");
        String userPath = scanner.nextLine();
        Path currentPath = Paths.get(userPath);
        //Path currentPath = Paths.get(System.getProperty("user.dir"));
        System.out.println("\ncurrentPath = " + currentPath + "\n");

        listDir(currentPath, 0);
    }

    public static void listDir(Path path, int depth) throws Exception {
        BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);

        if (attr.isDirectory()) {
            // Use File Class to examine contents of directory
            File file = new File(String.valueOf(path));
            long totalSize = getSizeOfContentsInFolder(file);
            int numFiles = getFileCountInFolder(file);

            // Use DirectoryStream to list available sub-directories
            DirectoryStream<Path> paths = Files.newDirectoryStream(path);

            // Print directory name and number and total size (in Bytes) of enclosed files
            System.out.println(spacesForDepth(depth) + " >" + path.getFileName() + " [Number of Files: " + numFiles + "], [Directory Size: " + totalSize + " B]");

            // If subdirectory(s) found, call listDir recursively
            for (Path tempPath : paths) {
                listDir(tempPath, depth + 1);
            }
        } else {
            // Print file name and size (in Bytes)
            System.out.println(spacesForDepth(depth) + " - - " + path.getFileName() + " [File Size: " + attr.size() + " B]");
        }

    }

    private static long getSizeOfContentsInFolder(File folder) {
        try {
            long length = 0;

            File[] files = folder.listFiles();

            if (files != null) {
                int count = files.length;

                for (int i = 0; i < count; i++) {
                    if (files[i].isFile()) {
                        length += files[i].length();
                    } else {
                        length += getSizeOfContentsInFolder(files[i]);
                    }
                }

                return length;
            }

        } catch(NullPointerException e) {
            System.out.println("Error in getSizeOfContentsInFolder: " + e.getMessage());
        }
        return -1;
    }

    private static int getFileCountInFolder(File folder) {
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if(files != null) {
                return files.length;
            } else {
                return -1;
            }
        } else {
            return -1;
        }

    }

    public static String spacesForDepth(int depth) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            builder.append("  ");
        }
        return builder.toString();
    }

    public static void listDirSkipHidden(Path path, int depth) throws Exception {
        BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);

        // Skip printing hidden files and folders
        // (although numFiles and totalSize still account for them.)
        if (!path.getFileName().toString().startsWith(".")) {
            if (attr.isDirectory()) {
                // Use File Class to examine contents of directory
                File file = new File(String.valueOf(path));
                long totalSize = getSizeOfContentsInFolder(file);
                int numFiles = getFileCountInFolder(file);

                // Use DirectoryStream to list available sub-directories
                DirectoryStream<Path> paths = Files.newDirectoryStream(path);

                // Print directory name and number and total size (in Bytes) of enclosed files
                System.out.println(spacesForDepth(depth) + " >" + path.getFileName() + " [Number of Files: " + numFiles + "], [Directory Size: " + totalSize + " B]");

                // If subdirectory(s) found, call listDir recursively
                for (Path tempPath : paths) {
                    listDirSkipHidden(tempPath, depth + 1);
                }
            } else {
                // Print file name and size (in Bytes)
                System.out.println(spacesForDepth(depth) + " - - " + path.getFileName() + " [File Size: " + attr.size() + " B]");
            }
        }
    }

}
