package h6;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Container class to different classes, that makes the whole
 * set of classes one class formally.
 * <p>
 * Koostada meetod, mis leiab etteantud sidusas lihtgraafis lühima tee
 * kahe teineteisest maksimaalselt kaugel paikneva tipu vahel.
 */
public class GraphTask {

   /**
    * Main method.
    */
   public static void main(String[] args) {
      GraphTask a = new GraphTask();
      a.run();
   }

   /**
    * Actual main method to run examples and everything.
    */
   public void run() {
      // Test Case 1: Small graph with 6 vertices and 9 edges
      System.out.println("Test Case 1: Small graph (6 vertices, 9 edges)");
      Graph g1 = new Graph("G1");
      g1.createRandomSimpleGraph(6, 9);
      System.out.println("Antud graaf: " + g1);

      long startTime = System.nanoTime();
      List<Arc> path1 = g1.findPathBetweenMostDistantVertices();
      long endTime = System.nanoTime();

      System.out.println("Tulemus: " + pathToString(path1));
      System.out.println("Tee pikkus: " + path1.size());
      System.out.println("Aeg: " + (endTime - startTime) / 1000000.0 + " ms");
      System.out.println();

      // Test Case 2: Medium graph with 10 vertices and 20 edges
      System.out.println("Test Case 2: Medium graph (10 vertices, 20 edges)");
      Graph g2 = new Graph("G2");
      g2.createRandomSimpleGraph(10, 20);
      System.out.println("Antud graaf: " + g2);

      startTime = System.nanoTime();
      List<Arc> path2 = g2.findPathBetweenMostDistantVertices();
      endTime = System.nanoTime();

      System.out.println("Tulemus: " + pathToString(path2));
      System.out.println("Tee pikkus: " + path2.size());
      System.out.println("Aeg: " + (endTime - startTime) / 1000000.0 + " ms");
      System.out.println();

      // Test Case 3: Line graph where the most distant vertices are obvious
      System.out.println("Test Case 3: Line graph");
      Graph g3 = new Graph("G3");
      Vertex v1 = g3.createVertex("v1");
      Vertex v2 = g3.createVertex("v2");
      Vertex v3 = g3.createVertex("v3");
      Vertex v4 = g3.createVertex("v4");

      g3.createArc("a1_2", v1, v2);
      g3.createArc("a2_1", v2, v1);
      g3.createArc("a2_3", v2, v3);
      g3.createArc("a3_2", v3, v2);
      g3.createArc("a3_4", v3, v4);
      g3.createArc("a4_3", v4, v3);

      System.out.println("Antud graaf: " + g3);

      startTime = System.nanoTime();
      List<Arc> path3 = g3.findPathBetweenMostDistantVertices();
      endTime = System.nanoTime();

      System.out.println("Tulemus: " + pathToString(path3));
      System.out.println("Tee pikkus: " + path3.size());
      System.out.println("Aeg: " + (endTime - startTime) / 1000000.0 + " ms");
      System.out.println();

      // Test Case 4: Circle graph where multiple pairs have the same maximum distance
      System.out.println("Test Case 4: Circle graph");
      Graph g4 = new Graph("G4");
      Vertex c1 = g4.createVertex("c1");
      Vertex c2 = g4.createVertex("c2");
      Vertex c3 = g4.createVertex("c3");
      Vertex c4 = g4.createVertex("c4");
      Vertex c5 = g4.createVertex("c5");
      Vertex c6 = g4.createVertex("c6");

      g4.createArc("a1_2", c1, c2);
      g4.createArc("a2_1", c2, c1);
      g4.createArc("a2_3", c2, c3);
      g4.createArc("a3_2", c3, c2);
      g4.createArc("a3_4", c3, c4);
      g4.createArc("a4_3", c4, c3);
      g4.createArc("a4_5", c4, c5);
      g4.createArc("a5_4", c5, c4);
      g4.createArc("a5_6", c5, c6);
      g4.createArc("a6_5", c6, c5);
      g4.createArc("a6_1", c6, c1);
      g4.createArc("a1_6", c1, c6);

      System.out.println("Antud graaf: " + g4);

      startTime = System.nanoTime();
      List<Arc> path4 = g4.findPathBetweenMostDistantVertices();
      endTime = System.nanoTime();

      System.out.println("Tulemus: " + pathToString(path4));
      System.out.println("Tee pikkus: " + path4.size());
      System.out.println("Aeg: " + (endTime - startTime) / 1000000.0 + " ms");
      System.out.println();

      // Test Case 5: Large graph with 2000+ vertices (only measure time)
      System.out.println("Test Case 5: Large graph (2000 vertices, 5000 edges)");
      Graph g5 = new Graph("G5");
      g5.createRandomSimpleGraph(2000, 5000);

      startTime = System.nanoTime();
      List<Arc> path5 = g5.findPathBetweenMostDistantVertices();
      endTime = System.nanoTime();

      System.out.println("Tee pikkus: " + path5.size());
      System.out.println("Aeg: " + (endTime - startTime) / 1000000.0 + " ms");
   }

   /**
    * Converts a path (list of arcs) to a readable string representation.
    *
    * @param path The list of arcs representing a path
    * @return A string representation of the path
    */
   private String pathToString(List<Arc> path) {
      if (path == null || path.isEmpty()) {
         return "No path found";
      }

      StringBuilder sb = new StringBuilder();
      sb.append(path.get(0).source);

      for (Arc arc : path) {
         sb.append(" --(").append(arc).append(")--> ").append(arc.target);
      }

      return sb.toString();
   }

   /**
    * Represents a vertex in the graph with its connections and metadata.
    */
   class Vertex {
      private String id;
      private Vertex next;
      private Arc first;
      private int info = 0;
      private int distance; // Used for BFS
      private Arc pathArc; // Arc that leads to this vertex in the shortest path

      Vertex(String s, Vertex v, Arc e) {
         id = s;
         next = v;
         first = e;
      }

      Vertex(String s) {
         this(s, null, null);
      }

      @Override
      public String toString() {
         return id;
      }

      /**
       * Reset the metadata used for pathfinding.
       */
      public void reset() {
         distance = Integer.MAX_VALUE;
         pathArc = null;
      }
   }

   /**
    * Arc represents one arrow in the graph. Two-directional edges are
    * represented by two Arc objects (for both directions).
    */
   class Arc {
      private String id;
      private Vertex target;
      private Arc next;
      private int info = 0;
      private Vertex source; // Added to track the source vertex

      Arc(String s, Vertex v, Arc a) {
         id = s;
         target = v;
         next = a;
      }

      Arc(String s) {
         this(s, null, null);
      }

      @Override
      public String toString() {
         return id;
      }
   }

   /**
    * Represents a graph with methods for path finding and graph operations.
    */
   class Graph {
      private String id;
      private Vertex first;
      private int info = 0;

      Graph(String s, Vertex v) {
         id = s;
         first = v;
      }

      Graph(String s) {
         this(s, null);
      }

      @Override
      public String toString() {
         String nl = System.getProperty("line.separator");
         StringBuffer sb = new StringBuffer(nl);
         sb.append(id);
         sb.append(nl);
         Vertex v = first;
         while (v != null) {
            sb.append(v.toString());
            sb.append(" -->");
            Arc a = v.first;
            while (a != null) {
               sb.append(" ");
               sb.append(a.toString());
               sb.append(" (");
               sb.append(v.toString());
               sb.append("->");
               sb.append(a.target.toString());
               sb.append(")");
               a = a.next;
            }
            sb.append(nl);
            v = v.next;
         }
         return sb.toString();
      }

      public Vertex createVertex(String vid) {
         Vertex res = new Vertex(vid);
         res.next = first;
         first = res;
         return res;
      }

      public Arc createArc(String aid, Vertex from, Vertex to) {
         Arc res = new Arc(aid);
         res.next = from.first;
         from.first = res;
         res.target = to;
         res.source = from; // Set the source vertex
         return res;
      }

      /**
       * Create a connected undirected random tree with n vertices.
       * Each new vertex is connected to some random existing vertex.
       *
       * @param n number of vertices added to this graph
       */
      public void createRandomTree(int n) {
         if (n <= 0)
            return;
         Vertex[] varray = new Vertex[n];
         for (int i = 0; i < n; i++) {
            varray[i] = createVertex("v" + String.valueOf(n - i));
            if (i > 0) {
               int vnr = (int) (Math.random() * i);
               createArc("a" + varray[vnr].toString() + "_" + varray[i].toString(), varray[vnr], varray[i]);
               createArc("a" + varray[i].toString() + "_" + varray[vnr].toString(), varray[i], varray[vnr]);
            } else {
            }
         }
      }

      /**
       * Create an adjacency matrix of this graph.
       * Side effect: corrupts info fields in the graph
       *
       * @return adjacency matrix
       */
      public int[][] createAdjMatrix() {
         info = 0;
         Vertex v = first;
         while (v != null) {
            v.info = info++;
            v = v.next;
         }
         int[][] res = new int[info][info];
         v = first;
         while (v != null) {
            int i = v.info;
            Arc a = v.first;
            while (a != null) {
               int j = a.target.info;
               res[i][j]++;
               a = a.next;
            }
            v = v.next;
         }
         return res;
      }

      /**
       * Create a connected simple (undirected, no loops, no multiple
       * arcs) random graph with n vertices and m edges.
       *
       * @param n number of vertices
       * @param m number of edges
       */
      public void createRandomSimpleGraph(int n, int m) {
         if (n <= 0)
            return;
         if (n > 2500)
            throw new IllegalArgumentException("Too many vertices: " + n);
         if (m < n - 1 || m > n * (n - 1) / 2)
            throw new IllegalArgumentException("Impossible number of edges: " + m);
         first = null;
         createRandomTree(n); // n-1 edges created here
         Vertex[] vert = new Vertex[n];
         Vertex v = first;
         int c = 0;
         while (v != null) {
            vert[c++] = v;
            v = v.next;
         }
         int[][] connected = createAdjMatrix();
         int edgeCount = m - n + 1; // remaining edges
         while (edgeCount > 0) {
            int i = (int) (Math.random() * n); // random source
            int j = (int) (Math.random() * n); // random target
            if (i == j)
               continue; // no loops
            if (connected[i][j] != 0 || connected[j][i] != 0)
               continue; // no multiple edges
            Vertex vi = vert[i];
            Vertex vj = vert[j];
            createArc("a" + vi.toString() + "_" + vj.toString(), vi, vj);
            connected[i][j] = 1;
            createArc("a" + vj.toString() + "_" + vi.toString(), vj, vi);
            connected[j][i] = 1;
            edgeCount--; // a new edge happily created
         }
      }

      /**
       * Get all vertices in the graph as a list.
       *
       * @return List of all vertices
       */
      public List<Vertex> getAllVertices() {
         List<Vertex> vertices = new ArrayList<>();
         Vertex v = first;
         while (v != null) {
            vertices.add(v);
            v = v.next;
         }
         return vertices;
      }

      /**
       * Run BFS from a source vertex to find shortest paths to all other vertices.
       *
       * @param source the starting vertex
       */
      private void bfs(Vertex source) {
         // Reset all vertices
         Vertex v = first;
         while (v != null) {
            v.reset();
            v = v.next;
         }

         // Initialize BFS for source
         source.distance = 0;
         Queue<Vertex> queue = new LinkedList<>();
         queue.add(source);

         // BFS traversal
         while (!queue.isEmpty()) {
            Vertex current = queue.poll();
            Arc arc = current.first;

            while (arc != null) {
               Vertex neighbor = arc.target;

               // If we haven't visited this neighbor yet
               if (neighbor.distance == Integer.MAX_VALUE) {
                  neighbor.distance = current.distance + 1;
                  neighbor.pathArc = arc;
                  queue.add(neighbor);
               }

               arc = arc.next;
            }
         }
      }

      /**
       * Find the shortest path between two vertices as a list of arcs.
       *
       * @param start starting vertex
       * @param end   ending vertex
       * @return List of arcs representing the path
       */
      public List<Arc> findShortestPath(Vertex start, Vertex end) {
         // Run BFS from start
         bfs(start);

         // Check if end is reachable
         if (end.distance == Integer.MAX_VALUE) {
            return new ArrayList<>(); // No path exists
         }

         // Reconstruct the path
         List<Arc> path = new ArrayList<>();
         Vertex current = end;

         while (current != start) {
            Arc arc = current.pathArc;
            path.add(0, arc); // Add to beginning of list
            current = arc.source;
         }

         return path;
      }

      /**
       * Find the shortest path between two vertices that are maximally distant in the graph.
       *
       * @return List of arcs representing the path
       */
      public List<Arc> findPathBetweenMostDistantVertices() {
         List<Vertex> vertices = getAllVertices();

         if (vertices.size() <= 1) {
            return new ArrayList<>(); // No path exists for 0 or 1 vertices
         }

         int maxDistance = -1;
         Vertex farthestStart = null;
         Vertex farthestEnd = null;

         // Find the pair of vertices with maximum distance
         for (Vertex start : vertices) {
            bfs(start);

            for (Vertex end : vertices) {
               if (end != start && end.distance != Integer.MAX_VALUE && end.distance > maxDistance) {
                  maxDistance = end.distance;
                  farthestStart = start;
                  farthestEnd = end;
               }
            }
         }

         // If we found a pair of vertices with maximum distance
         if (farthestStart != null && farthestEnd != null) {
            System.out.println("Maximum distance in the graph: " + maxDistance);
            System.out.println("Most distant vertices: " + farthestStart.id + " and " + farthestEnd.id);
            return findShortestPath(farthestStart, farthestEnd);
         } else {
            return new ArrayList<>(); // No path exists
         }
      }
   }
}