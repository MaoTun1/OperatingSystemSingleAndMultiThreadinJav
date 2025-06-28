package opsys_project;

import java.io.*;
import java.util.Scanner;

public class MatrixMultiplier_MultiThread {

    private static int[][] matrixB;
    private static int[][] resultMatrix;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter matrix file path: ");
        String filePath = scanner.nextLine();

        System.out.print("Enter number of threads: ");
        int numThreads = scanner.nextInt();

        long startTime = System.currentTimeMillis();

        BufferedReader buffreader = new BufferedReader(new FileReader(filePath));

        // Read matrix A header
        String line = buffreader.readLine();
        int rowsA = Integer.parseInt(line.split(" ")[2].split("x")[0]);
        int colsA = Integer.parseInt(line.split(" ")[2].split("x")[1].replace(":", ""));
        resultMatrix = new int[rowsA][colsA];

        File tempFile = File.createTempFile("matrixA_rows", ".tmp");
        BufferedWriter tempWriter = new BufferedWriter(new FileWriter(tempFile));

        for (int i = 0; i < rowsA; i++) {
            line = buffreader.readLine();
            tempWriter.write(line);
            tempWriter.newLine();
        }
        tempWriter.close();

        // Skip empty line
        buffreader.readLine();

        // Load matrix B from remaining lines
        loadMatrixB(buffreader);

        buffreader.close();

        Thread[] threads = new Thread[numThreads];
        int rowsPerThread = rowsA / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int startRow = i * rowsPerThread;
            int endRow = (i == numThreads - 1) ? rowsA : startRow + rowsPerThread;
            threads[i] = new Thread(new Worker(tempFile, startRow, endRow, colsA));
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        tempFile.delete();

        long endTime = System.currentTimeMillis();
        System.out.println("Matrix multiplication completed (multi-threaded).");
        System.out.println("Execution Time (Multithreaded): " + (endTime - startTime) + " ms");
    }

    private static void loadMatrixB(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        int rowsB = Integer.parseInt(line.split(" ")[2].split("x")[0]);
        int colsB = Integer.parseInt(line.split(" ")[2].split("x")[1].replace(":", ""));
        matrixB = new int[rowsB][colsB];

        for (int i = 0; i < rowsB; i++) {
            line = reader.readLine();
            String[] values = line.trim().split(" ");
            for (int j = 0; j < colsB; j++) {
                matrixB[i][j] = Integer.parseInt(values[j]);
            }
        }
    }

    static class Worker implements Runnable {
        private final File inputFile;
        private final int startRow, endRow, colsA;

        Worker(File inputFile, int startRow, int endRow, int colsA) {
            this.inputFile = inputFile;
            this.startRow = startRow;
            this.endRow = endRow;
            this.colsA = colsA;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
                for (int i = 0; i < startRow; i++) reader.readLine();

                for (int i = startRow; i < endRow; i++) {
                    String[] rowData = reader.readLine().trim().split(" ");
                    for (int j = 0; j < matrixB[0].length; j++) {
                        for (int k = 0; k < colsA; k++) {
                            resultMatrix[i][j] += Integer.parseInt(rowData[k]) * matrixB[k][j];
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
