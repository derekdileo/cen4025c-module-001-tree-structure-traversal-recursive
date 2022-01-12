package com.derekdileo;

import org.w3c.dom.Node;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

public class Main {

    public static Path currentPath;

    public static void main(String[] args) throws Exception {
        currentPath = Paths.get(System.getProperty("user.dir"));
        System.out.println("\ncurrentPath = " + currentPath + "\n");

        listDir(currentPath, 0);
    }
    
    public static void listDir(Path path, int depth) throws Exception {

        BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);

        if (!path.getFileName().toString().startsWith(".")) {

            if (attr.isDirectory()) {
                File file = new File(String.valueOf(path));
                long totalSize = getFolderSize(file);
                int numFiles = getFileCountInFolder(file);

                // list available sub-directories
                DirectoryStream<Path> paths = Files.newDirectoryStream(path);

                // print directory name, number of files, total size of files, list of child folders
                System.out.println(spacesForDepth(depth) + " >" + path.getFileName() + " [Directory Size: " + totalSize + " B], [Number of Files: " + numFiles + "]");

                // if subdirectory(s) found, call listDir recursively
                for (Path tempPath : paths) {
                    listDir(tempPath, depth + 1);
                }
            } else {
                System.out.println(spacesForDepth(depth) + " - - " + path.getFileName() + " [File Size: " + attr.size() + " B]");
            }

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
