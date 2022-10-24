import java.io.*;
import java.util.*;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {

        String path = "resources/testfile.txt";
        measureTime("FileInputStream.read() into ArrayList", Main::fileInputToList, path);
        measureTime("BufferedFileInputStream.read() into ArrayList", Main::bufferedFileInputToList, path);
        System.out.println("-----------------------------------------------------------");
    }

    private static void measureTime(String name, Function<String, List<byte[]>> fn, String path) {
        System.out.println("-----------------------------------------------------------");
        System.out.println("run: " + name);
        long startTime = System.nanoTime();
        List<byte[]> l = fn.apply(path);
        long estimatedTime = System.nanoTime() - startTime;
        System.out.println("lines: " + l.size());
        System.out.println("estimatedTime: " + estimatedTime / 1_000_000_000.);
    }

    private static List<byte[]> evaluateInputStream(InputStream is, List<byte[]> content) throws IOException {
        int bs = 8;

        byte[] bb = new byte[bs];
        while (is.available() >= bs) {
            is.read(bb, 0, bs);
            content.add(bb.clone());
        }

        return content;
    }

    private static List<byte[]> bufferedFileInputToList(String path) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path))) {
            List<byte[]> content = new ArrayList<>();
            return evaluateInputStream(bis, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<byte[]> fileInputToList(String path) {
        try (FileInputStream fis = new FileInputStream(path)) {
            List<byte[]> content = new ArrayList<>();
            return evaluateInputStream(fis, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}