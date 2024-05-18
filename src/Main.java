import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.*;
import java.util.List;

public class Main {
    private static Graph graph;

    public static void main(String[] args) {
        // 创建主窗口框架
        JFrame frame = new JFrame("Graph Operations");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        // 创建文本区域用于显示输出信息
        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        // 创建各功能按钮
        JButton loadFileButton = new JButton("Load File");
        JButton showGraphButton = new JButton("Show Graph");
        JButton queryBridgeWordsButton = new JButton("Query Bridge Words");
        JButton generateTextButton = new JButton("Generate New Text");
        JButton shortestPathButton = new JButton("Calculate Shortest Path");
        JButton randomWalkButton = new JButton("Random Walk");
        // 将按钮添加到面板中
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));
        panel.add(loadFileButton);
        panel.add(showGraphButton);
        panel.add(queryBridgeWordsButton);
        panel.add(generateTextButton);
        panel.add(shortestPathButton);
        panel.add(randomWalkButton);
        // 将面板和文本区域添加到框架中
        frame.getContentPane().add(BorderLayout.CENTER, scrollPane);
        frame.getContentPane().add(BorderLayout.EAST, panel);
        // 加载文件并生成图
        loadFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                graph = new Graph();
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] words = line.toLowerCase().replaceAll("[^a-z\\s]", "").split("\\s+");
                        for (int i = 0; i < words.length - 1; i++) {
                            graph.addEdge(words[i], words[i + 1]);
                        }
                    }
                    textArea.append("File loaded and graph created.\n");
                } catch (IOException ex) {
                    textArea.append("Error loading file: " + ex.getMessage() + "\n");
                }
            }
        });
        // 显示有向图
        showGraphButton.addActionListener(e -> {
            if (graph != null) {
                try {
                    GraphVisualizer.showDirectedGraph(graph);
                    textArea.append("Graph visualization saved as src/image/graph.png.\n");
                } catch (IOException | InterruptedException ex) {
                    textArea.append("Error visualizing graph: " + ex.getMessage() + "\n");
                }
            } else {
                textArea.append("No graph to visualize.\n");
            }
        });
        // 查询桥接词
        queryBridgeWordsButton.addActionListener(e -> {
            if (graph != null) {
                String word1 = JOptionPane.showInputDialog("Enter the first word:");
                String word2 = JOptionPane.showInputDialog("Enter the second word:");
                if (word1 != null && word2 != null) {
                    List<String> bridgeWords = graph.getBridgeWords(word1.toLowerCase(), word2.toLowerCase());
                    if (bridgeWords.isEmpty()) {
                        textArea.append("No bridge words from " + word1 + " to " + word2 + ".\n");
                    } else {
                        textArea.append("The bridge words from " + word1 + " to " + word2 + " are: " + String.join(", ", bridgeWords) + ".\n");
                    }
                }
            } else {
                textArea.append("No graph to query.\n");
            }
        });
        // 生成新文本
        generateTextButton.addActionListener(e -> {
            if (graph != null) {
                String inputText = JOptionPane.showInputDialog("Enter the text:");
                if (inputText != null) {
                    String newText = graph.generateNewText(inputText);
                    textArea.append("New text: " + newText + "\n");
                }
            } else {
                textArea.append("No graph to generate text.\n");
            }
        });
        // 计算最短路径
        shortestPathButton.addActionListener(e -> {
            if (graph != null) {
                String word1 = JOptionPane.showInputDialog("Enter the first word:");
                String word2 = JOptionPane.showInputDialog("Enter the second word:");
                if (word1 != null && word2 != null) {
                    String result = graph.calcShortestPath(word1.toLowerCase(), word2.toLowerCase());
                    textArea.append(result + "\n");
                }
            } else {
                textArea.append("No graph to calculate shortest path.\n");
            }
        });
        // 随机游走
        randomWalkButton.addActionListener(e -> {
            if (graph != null) {
                String walk = graph.randomWalk();
                textArea.append("Random walk: " + walk + "\n");
                try (FileWriter fileWriter = new FileWriter("src/image/random_walk.txt")) {
                    fileWriter.write(walk);
                } catch (IOException ex) {
                    textArea.append("Error saving random walk to file: " + ex.getMessage() + "\n");
                }
            } else {
                textArea.append("No graph for random walk.\n");
            }
        });

        frame.setVisible(true);
    }
}
