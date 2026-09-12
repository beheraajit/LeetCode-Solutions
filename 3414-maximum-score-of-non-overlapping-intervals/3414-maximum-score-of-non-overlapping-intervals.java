import java.util.*;

class Solution {
    static class Interval {
        int l, r, w, idx;

        Interval(int l, int r, int w, int idx) {
            this.l = l;
            this.r = r;
            this.w = w;
            this.idx = idx;
        }
    }

    static class Result {
        long weight;
        List<Integer> indices;

        Result(long weight, List<Integer> indices) {
            this.weight = weight;
            this.indices = indices;
        }
    }

    Interval[] arr;
    Result[][] dp;

    public int[] maximumWeight(List<List<Integer>> intervals) {
        int n = intervals.size();

        arr = new Interval[n];

        for (int i = 0; i < n; i++) {
            arr[i] = new Interval(
                intervals.get(i).get(0),
                intervals.get(i).get(1),
                intervals.get(i).get(2),
                i
            );
        }

        Arrays.sort(arr, (a, b) -> {
            if (a.l != b.l) return Integer.compare(a.l, b.l);
            if (a.r != b.r) return Integer.compare(a.r, b.r);
            return Integer.compare(a.idx, b.idx);
        });

        dp = new Result[n + 1][5];

        Result ans = solve(0, 4);

        int[] result = new int[ans.indices.size()];

        for (int i = 0; i < ans.indices.size(); i++) {
            result[i] = ans.indices.get(i);
        }

        return result;
    }

    Result solve(int i, int k) {
        if (i == arr.length || k == 0) {
            return new Result(0, new ArrayList<>());
        }

        if (dp[i][k] != null) {
            return dp[i][k];
        }

        Result skip = solve(i + 1, k);

        int next = findNext(i);

        Result nextResult = solve(next, k - 1);

        List<Integer> selected = new ArrayList<>(nextResult.indices);
        selected.add(arr[i].idx);
        Collections.sort(selected);

        Result take = new Result(
            arr[i].w + nextResult.weight,
            selected
        );

        if (take.weight > skip.weight) {
            dp[i][k] = take;
        } else if (take.weight < skip.weight) {
            dp[i][k] = skip;
        } else {
            if (compare(take.indices, skip.indices) < 0) {
                dp[i][k] = take;
            } else {
                dp[i][k] = skip;
            }
        }

        return dp[i][k];
    }

    int findNext(int i) {
        int target = arr[i].r;
        int left = i + 1;
        int right = arr.length;

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (arr[mid].l > target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        return left;
    }

    int compare(List<Integer> a, List<Integer> b) {
        int n = Math.min(a.size(), b.size());

        for (int i = 0; i < n; i++) {
            if (!a.get(i).equals(b.get(i))) {
                return Integer.compare(a.get(i), b.get(i));
            }
        }

        return Integer.compare(a.size(), b.size());
    }
}