
import java.util.*;

class Solution {
    private int[] parent, depth, size, heavy, head, pos, nodeAtPos;
    private int[] seg;
    private char[] chars;
    private List<Integer>[] adj;

    public List<Boolean> palindromePath(int n, int[][] edges, String s, String[] queries) {
        adj = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }

        for (int[] e : edges) {
            adj[e[0]].add(e[1]);
            adj[e[1]].add(e[0]);
        }

        chars = s.toCharArray();
        parent = new int[n];
        depth = new int[n];
        size = new int[n];
        heavy = new int[n];
        head = new int[n];
        pos = new int[n];
        nodeAtPos = new int[n];

        Arrays.fill(heavy, -1);

        int[] order = new int[n];
        int[] stack = new int[n];
        int count = 0;
        int top = 0;

        stack[top++] = 0;
        parent[0] = -1;

        while (top > 0) {
            int u = stack[--top];
            order[count++] = u;

            for (int v : adj[u]) {
                if (v == parent[u]) {
                    continue;
                }

                parent[v] = u;
                depth[v] = depth[u] + 1;
                stack[top++] = v;
            }
        }

        for (int i = n - 1; i >= 0; i--) {
            int u = order[i];
            size[u] = 1;
            int largest = 0;

            for (int v : adj[u]) {
                if (parent[v] == u) {
                    size[u] += size[v];

                    if (size[v] > largest) {
                        largest = size[v];
                        heavy[u] = v;
                    }
                }
            }
        }

        int[] nodes = new int[n];
        int[] chainHeads = new int[n];
        top = 0;
        nodes[top] = 0;
        chainHeads[top++] = 0;

        int timer = 0;

        while (top > 0) {
            top--;
            int u = nodes[top];
            int h = chainHeads[top];

            while (u != -1) {
                head[u] = h;
                pos[u] = timer;
                nodeAtPos[timer++] = u;

                for (int v : adj[u]) {
                    if (parent[v] == u && v != heavy[u]) {
                        nodes[top] = v;
                        chainHeads[top++] = v;
                    }
                }

                u = heavy[u];
            }
        }

        seg = new int[4 * n];
        build(1, 0, n - 1);

        List<Boolean> result = new ArrayList<>();

        for (String q : queries) {
            String[] parts = q.split(" ");
            int u = Integer.parseInt(parts[1]);

            if (parts[0].equals("update")) {
                char c = parts[2].charAt(0);
                chars[u] = c;
                update(1, 0, n - 1, pos[u], 1 << (c - 'a'));
            } else {
                int v = Integer.parseInt(parts[2]);
                int mask = pathQuery(u, v);
                result.add((mask & (mask - 1)) == 0);
            }
        }

        return result;
    }

    private void build(int node, int l, int r) {
        if (l == r) {
            int u = nodeAtPos[l];
            seg[node] = 1 << (chars[u] - 'a');
            return;
        }

        int mid = (l + r) / 2;
        build(node * 2, l, mid);
        build(node * 2 + 1, mid + 1, r);
        seg[node] = seg[node * 2] ^ seg[node * 2 + 1];
    }

    private void update(int node, int l, int r, int idx, int val) {
        if (l == r) {
            seg[node] = val;
            return;
        }

        int mid = (l + r) / 2;

        if (idx <= mid) {
            update(node * 2, l, mid, idx, val);
        } else {
            update(node * 2 + 1, mid + 1, r, idx, val);
        }

        seg[node] = seg[node * 2] ^ seg[node * 2 + 1];
    }

    private int rangeQuery(int node, int l, int r, int ql, int qr) {
        if (ql <= l && r <= qr) {
            return seg[node];
        }

        int mid = (l + r) / 2;
        int ans = 0;

        if (ql <= mid) {
            ans ^= rangeQuery(node * 2, l, mid, ql, qr);
        }

        if (qr > mid) {
            ans ^= rangeQuery(node * 2 + 1, mid + 1, r, ql, qr);
        }

        return ans;
    }

    private int pathQuery(int u, int v) {
        int mask = 0;

        while (head[u] != head[v]) {
            if (depth[head[u]] < depth[head[v]]) {
                int temp = u;
                u = v;
                v = temp;
            }

            mask ^= rangeQuery(1, 0, pos.length - 1, pos[head[u]], pos[u]);
            u = parent[head[u]];
        }

        if (depth[u] > depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }

        mask ^= rangeQuery(1, 0, pos.length - 1, pos[u], pos[v]);

        return mask;
    }
}
