import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.nio.file.*;
import java.util.Map;

public class GraphVisualizer {
    public static void showDirectedGraph(Graph graph) throws IOException, InterruptedException {
        String dotPath = "dot"; // 如果已添加到系统路径中

        StringBuilder dotFormat = new StringBuilder();
        dotFormat.append("digraph G {\n");

        for (Map.Entry<String, Map<String, Integer>> entry : graph.getAdjacencyList().entrySet()) {
            String from = entry.getKey();
            for (Map.Entry<String, Integer> edge : entry.getValue().entrySet()) {
                String to = edge.getKey();
                int weight = edge.getValue();
                dotFormat.append(String.format("  \"%s\" -> \"%s\" [label=\"%d\"];\n", from, to, weight));
            }
        }

        dotFormat.append("}\n");

        // Ensure the image directory exists
        Path imageDir = Paths.get("src", "image");
        if (!Files.exists(imageDir)) {
            Files.createDirectories(imageDir);
        }

        String dotFilePath = "src/image/graph.dot";
        String pngFilePath = "src/image/graph.png";

        try (FileWriter fileWriter = new FileWriter(dotFilePath)) {
            fileWriter.write(dotFormat.toString());
        }

        Runtime.getRuntime().exec(dotPath + " -Tpng " + dotFilePath + " -o " + pngFilePath).waitFor();

        // 显示图像
        displayGraphImage(pngFilePath);
    }


    private static void displayGraphImage(String imagePath) {
        JFrame frame = new JFrame("Graph Visualization");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageIcon icon = new ImageIcon(imagePath);
        JLabel label = new JLabel(icon);
        frame.getContentPane().add(label);
        frame.pack();
        frame.setVisible(true);
    }
}
