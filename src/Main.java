import java.io.*;
import java.util.*;

class Main {

    /**
     * @param n the number of houses
     * @param k the number of stores to place
     * @param houses the houses, indexed 0 to n (with ids 0 to n)
     * @return the places to put the stores.
     */
    public static Set<Store> donutTime(int n, int k, List<House> houses) {
        int m = n * (n - 1) / 2;

        List<Distance> distances = new ArrayList<>(m);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                distances.add(new Distance(houses.get(i), houses.get(j)));
            }
        }
        UnionFind unionFind = new UnionFind(houses);
        Collections.sort(distances, new Comparator<Distance>() {
            @Override
            public int compare(Distance o1, Distance o2) {
                return Long.compare(o1.distance, o2.distance);
            }
        });
        System.out.println(distances);
        List<Distance> MST = new ArrayList<Distance>();
        for (Distance d : distances) {
            //The algorithm initially treats each house as its own cluster (or group).
            // The goal is to reduce the number of clusters to k by "joining" them.
            // Hence:
            if (MST.size() == n - k) break;
            if (unionFind.join(d.a, d.b)) {
                MST.add(d);
            }
        }

        System.out.println(MST);

        Set<Store> stores = new HashSet<Store>();
        for (List<House> cluster : unionFind.clusters()) {
            long c = 0, sumX = 0, sumY = 0;
            for (House house : cluster) {
                c++;
                sumX += house.x;
                sumY += house.y;
            }
            // Adding 1e-6 to test if it's accepted
            stores.add(new Store(sumX * 1.0 / c + 1e-6, sumY * 1.0 / c));
        }

        return stores;
    }
}

class House {

    int id, x, y;

    public House(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        House house = (House) o;
        return id == house.id && x == house.x && y == house.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, x, y);
    }
}

class Distance implements Comparable<Distance> {

    House a, b;

    long distance;

    public Distance(House a, House b) {
        this.a = a;
        this.b = b;
        // Square Euclidean distance, to avoid floating-point errors
        this.distance = (long) (a.x - b.x) * (a.x - b.x) + (long) (a.y - b.y) * (a.y - b.y);
    }

    public String toString() {
        return "distance: " + String.valueOf(this.distance);
    }


    @Override
    public int compareTo(Distance d) {
        return Long.compare(this.distance, d.distance);
    }
}

class UnionFind {

    private List<House> houses;

    private int[] parent;

    private int[] rank;

    UnionFind(List<House> houses) {
        this.houses = houses;
        int n = houses.size();
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
    }

    /**
     * Joins two disjoint sets together, if they are not already joined.
     *
     * @return false if x and y are in same set, true if the sets of x and y are now joined.
     */
    boolean join(House x, House y) {
        int xrt = find(x.id);
        int yrt = find(y.id);
        if (rank[xrt] > rank[yrt]) parent[yrt] = xrt;
        else if (rank[xrt] < rank[yrt]) parent[xrt] = yrt;
        else if (xrt != yrt) rank[parent[yrt] = xrt]++;
        return xrt != yrt;
    }

    /** @return The house that is indicated as the "root" of the set of house h. */
    House find(House h) {
        return houses.get(find(h.id));
    }

    private int find(int x) {
        return parent[x] == x ? x : (parent[x] = find(parent[x]));
    }

    /** @return All clusters of houses */
    Collection<List<House>> clusters() {
        Map<Integer, List<House>> map = new HashMap<>();
        for (int i = 0; i < parent.length; i++) {
            int root = find(i);
            if (!map.containsKey(root)) map.put(root, new ArrayList<>());
            map.get(root).add(houses.get(i));
        }
        return map.values();
    }
}

class Store {

    double x, y;

    public Store(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return Math.abs(store.x - x) <= 1e-4 && Math.abs(store.y - y) <= 1e-4;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Math.floor(x), Math.floor(y));
    }

    @Override
    public String toString() {
        return "Store{" + "x=" + x + ", y=" + y + '}';
    }
}

