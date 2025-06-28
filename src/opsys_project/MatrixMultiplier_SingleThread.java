package opsys_project;
//imports here
import java.io.*;
import java.util.Scanner;

public class MatrixMultiplier_SingleThread {

    private static int[][] matrixA;
    private static int[][] matrixB;
    private static int[][] resultMatrix;
    private static int MATRIX_SIZE;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the matrix input file path: ");
        String filePath = scanner.nextLine();

        System.out.print("Enter the matrix size (e.g., 1000 for 1000x1000): ");
        MATRIX_SIZE = scanner.nextInt();

        // Start timer for multiplication
        long startTime = System.currentTimeMillis();

        // Perform multiplication
        loadMatrixA(filePath);
        loadMatrixB(filePath);

        resultMatrix = new int[matrixA.length][matrixB[0].length];

        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixB[0].length; j++) {
                for (int k = 0; k < matrixB.length; k++) {
                    resultMatrix[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }

        // End timer and calculate duration
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("Matrix multiplication completed (single-threaded).");
        System.out.println("Time taken (single-threaded): " + duration + " ms");
    }

    public static void loadMatrixA(String filePath) throws IOException {
        try (BufferedReader buffreader = new BufferedReader(new FileReader(filePath))) {
            String line = buffreader.readLine();
            String[] dimsA = line.split(" ")[2].split("x");
            int rowsA = Integer.parseInt(dimsA[0]);
            int colsA = Integer.parseInt(dimsA[1].replace(":", ""));
            matrixA = new int[rowsA][colsA];

            for (int i = 0; i < rowsA; i++) {
                line = buffreader.readLine();
                String[] values = line.trim().split(" ");
                for (int j = 0; j < colsA; j++) {
                    matrixA[i][j] = Integer.parseInt(values[j]);
                }
            }
        }
    }

    public static void loadMatrixB(String filePath) throws IOException {
        try (BufferedReader buffreader = new BufferedReader(new FileReader(filePath))) {
            // Skip matrix A
            buffreader.readLine(); // header
            for (int i = 0; i < MATRIX_SIZE; i++) {
                buffreader.readLine();
            }
            buffreader.readLine(); // empty line

            // Now read matrix B
            String line = buffreader.readLine();
            String[] dimsB = line.split(" ")[2].split("x");
            int rowsB = Integer.parseInt(dimsB[0]);
            int colsB = Integer.parseInt(dimsB[1].replace(":", ""));
            matrixB = new int[rowsB][colsB];

            for (int i = 0; i < rowsB; i++) {
                line = buffreader.readLine();
                String[] values = line.trim().split(" ");
                for (int j = 0; j < colsB; j++) {
                    matrixB[i][j] = Integer.parseInt(values[j]);
                }
            }
        }
    }
}
