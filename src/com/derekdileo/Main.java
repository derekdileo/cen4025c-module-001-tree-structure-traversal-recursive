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

        // Skip printing hidden files and folders
        // (although numFiles and totalSize still account for them.)
        if (attr.isDirectory()) {
            // Use File Class to examine contents of directory
            File file = new File(String.valueOf(path));
            long totalSize = getFolderSize(file);
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

    private static long getFolderSize(File folder) {
        long length = 0;

        File[] files = folder.listFiles();

        int count = files.length;

        for (int i = 0; i < count; i++) {
            if (files[i].isFile()) {
                length += files[i].length();
            } else {
                length += getFolderSize(files[i]);
            }
        }
        return length;
    }

    private static int getFileCountInFolder(File folder) {
        if (folder.exists()) {
            File[] files = folder.listFiles();
            return files.length;
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

}
