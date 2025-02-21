package com.lbcap.common.gradletasks;

import java.io.*;

public class FileUtil {

    static public boolean createMissingParentDirectories(File file) {
        File parent = file.getParentFile();
        if (parent == null) {
            // Parent directory not specified, therefore it's a request to
            // create nothing. Done! ;)
            return true;
        }

        // File.mkdirs() creates the parent directories only if they don't
        // already exist; and it's okay if they do.
        parent.mkdirs();
        return parent.exists();
    }

    public static void saveFile(String contents, String resourceStreamFilePath) throws IOException {
        createMissingParentDirectories(new File(resourceStreamFilePath));

        try (FileWriter fw = new FileWriter(resourceStreamFilePath)) {
            try (BufferedWriter writer = new BufferedWriter(fw)) {
                writer.write(contents);
            }
        }
    }

    public static String readFile(String resourceStreamFilePath) throws IOException {

        try {
            StringBuilder resultStringBuilder = new StringBuilder();
            try (FileReader fr = new FileReader(resourceStreamFilePath)) {
                try (BufferedReader br
                             = new BufferedReader(fr)) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        resultStringBuilder.append(line).append("\n");
                    }
                }
                return resultStringBuilder.toString();
            }
        } catch (Exception ex) {
            System.out.printf("resourceStreamFilePath value: %s%n", resourceStreamFilePath);
            throw ex;
        }
    }
}
