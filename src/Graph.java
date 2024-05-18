import java.util.*;

public class Graph {
    private Map<String, Map<String, Integer>> adjacencyList = new HashMap<>();
    //获取图的邻接表
    public Map<String, Map<String, Integer>> getAdjacencyList() {
        return adjacencyList;
    }
    //添加一条有向边到图中
    public void addEdge(String from, String to) {
        adjacencyList.computeIfAbsent(from, k -> new HashMap<>()).merge(to, 1, Integer::sum);
    }
    //检查图中是否包含指定的节点
    public boolean containsNode(String node) {
        return adjacencyList.containsKey(node);
    }
    //查询桥接词
    public List<String> getBridgeWords(String word1, String word2) {
        //初始化桥接词列表
        List<String> bridgeWords = new ArrayList<>();
        //获取word1的相邻节点
        Map<String, Integer> word1Edges = adjacencyList.get(word1);
        //检查word1是否有相邻节点
        if (word1Edges != null) {
            //遍历这些节点
            for (String word3 : word1Edges.keySet()) {
                //查找桥接词
                Map<String, Integer> word3Edges = adjacencyList.get(word3);
                if (word3Edges != null && word3Edges.containsKey(word2)) {
                    //如果word3有相邻节点，并且其中包含word2，则将word3加入bridgeWords列表
                    bridgeWords.add(word3);
                }
            }
        }
        return bridgeWords;
    }

    public String generateNewText(String inputText) {
        String[] words = inputText.toLowerCase().split("\\s+");
        StringBuilder newText = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < words.length - 1; i++) {
            newText.append(words[i]).append(" ");//将当前单词追加到newText中，并追加一个空格
            List<String> bridgeWords = getBridgeWords(words[i], words[i + 1]);//查找当前单词和下一个单词之间的桥接词
            if (!bridgeWords.isEmpty()) {
                String bridgeWord = bridgeWords.get(rand.nextInt(bridgeWords.size()));
                newText.append(bridgeWord).append(" ");
            }
        }
        newText.append(words[words.length - 1]);
        return newText.toString();
    }

    //计算在有向图中从一个单词到另一个单词的最短路径
    public String calcShortestPath(String word1, String word2) {
        if (!containsNode(word1) || !containsNode(word2)) {
            return "No word1 or word2 in the graph!";
        }
        Map<String, Integer> distances = new HashMap<>();//存储从起始单词到每个单词的最短距离
        Map<String, String> predecessors = new HashMap<>();//记录每个单词的前驱节点,构造最短路径
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));//向队列里面添加当前距离最短的单词进行扩展
        distances.put(word1, 0);
        queue.add(word1);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            int currentDistance = distances.get(current);
            for (Map.Entry<String, Integer> neighbor : adjacencyList.getOrDefault(current, Collections.emptyMap()).entrySet()) {
                int newDist = currentDistance + neighbor.getValue();
                if (newDist < distances.getOrDefault(neighbor.getKey(), Integer.MAX_VALUE)) {
                    distances.put(neighbor.getKey(), newDist);
                    predecessors.put(neighbor.getKey(), current);
                    queue.add(neighbor.getKey());
                }
            }
        }

        if (!distances.containsKey(word2)) {
            return "No path from " + word1 + " to " + word2 + "!";
        }

        List<String> path = new LinkedList<>();
        for (String at = word2; at != null; at = predecessors.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);

        return "Shortest path: " + String.join(" -> ", path) + " with total weight " + distances.get(word2);
    }

    public String randomWalk() {
        List<String> nodes = new ArrayList<>(adjacencyList.keySet());
        if (nodes.isEmpty()) {
            return "The graph is empty!";
        }
        Random rand = new Random();
        String current = nodes.get(rand.nextInt(nodes.size()));
        Set<String> visitedEdges = new HashSet<>();
        StringBuilder walk = new StringBuilder();
        walk.append(current);

        while (true) {
            Map<String, Integer> edges = adjacencyList.getOrDefault(current, Collections.emptyMap());
            if (edges.isEmpty()) {
                break;
            }
            List<String> neighbors = new ArrayList<>(edges.keySet());
            String next = neighbors.get(rand.nextInt(neighbors.size()));
            String edge = current + "->" + next;
            if (visitedEdges.contains(edge)) {
                break;
            }
            visitedEdges.add(edge);
            current = next;
            walk.append(" -> ").append(current);
        }

        return walk.toString();
    }
}
