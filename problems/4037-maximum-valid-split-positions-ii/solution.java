class Solution {
    int[][] st;
    int[] log;

    public int gcd(int a, int b) {
        while(b != 0) {
            int temp = a % b;
            a = b;
            b = temp;
        }
        return a;
    }

    public int rangeGcd(int l, int r) {
        if(l > r) {
            return 0;
        }
        int len = r - l +1;
        int k = log[len];

        return gcd(st[k][l], st[k][r-(1<<k) + 1]);
    }

    public int scoreWithoutDelete(int n) {
        int totalGcd = rangeGcd(0, n -1);

        int lo = 0;
        int hi = n -1;

        while(lo < hi) {
            int mid = (lo + hi) / 2;
            if(rangeGcd(0, mid) == totalGcd) {
                hi = mid;
            }
            else {
                lo = mid + 1;
            }
        }
        int p = lo;

        lo = 0;
        hi = n -1;

        while(lo < hi) {
            int mid = (lo + hi + 1) / 2;

            if(rangeGcd(mid, n - 1) == totalGcd) {
                lo = mid;
            }
            else {
                hi = mid - 1;
            }
        }
        int q = lo;
        return Math.max(0, q-p);
    }

    public int scoreAfterDelete(int[] nums, int remove) {
        int n = nums.length;

        int leftGcd = 0;
        if(remove > 0) {
            leftGcd = rangeGcd(0, remove - 1);
        }
        int rightGcd = 0;
        if(remove < n-1) {
            rightGcd = rangeGcd(remove + 1, n - 1);
        }

        int totalGcd = gcd(leftGcd, rightGcd);

        int p;

        if(remove > 0 && leftGcd == totalGcd) {
            int lo = 0;
            int hi = remove - 1;

            while(lo < hi) {
                int mid = (lo + hi) / 2;
                if(rangeGcd(0, mid) == totalGcd) {
                    hi = mid;
                } else {
                    lo = mid + 1;
                }
            }
            p = lo;
        } else {
            int lo = remove + 1;
            int hi = n - 1;

            while(lo < hi) {
                int mid = (lo + hi) / 2;

                int g = gcd(leftGcd, rangeGcd(remove + 1, mid));

                if(g == totalGcd) {
                    hi = mid;
                } else {
                    lo = mid + 1;
                }
            }
            p = lo;
        }
        int q;

        if(remove < n-1 && rightGcd == totalGcd) {
            int lo = remove + 1;
            int hi = n - 1;

            while(lo < hi) {
                int mid = (lo + hi + 1) / 2;
                if(rangeGcd(mid, n - 1) == totalGcd) {
                    lo = mid;
                } else {
                    hi = mid - 1;
                }
            }
            q = lo;
        } else {
            int lo = 0;
            int hi = remove - 1;

            while(lo < hi) {
                int mid = (lo + hi + 1 ) / 2;

                int g = gcd(rangeGcd(mid, remove - 1), rightGcd);
                if(g == totalGcd) {
                    lo = mid;
                } else {
                    hi = mid - 1;
                }
            }
            q = lo;
        }
        if(p > remove) {
            p--;
        }
        if(q > remove) {
            q--;
        }
        return Math.max(0, q-p);
    }
    public int maxValidSplits(int[] nums) {
        int n = nums.length;

        int levels = 1;
        while((1 << levels) <= n) {
            levels++;
        }
        st = new int[levels][n];

        for(int i=0;i<n;i++) {
            st[0][i] = nums[i];
        }
        for(int k= 1;k<levels;k++) {
            int len = 1 << k;
            int half = len >> 1;

            for(int i =0;i + len <= n;i++) {
                st[k][i] = gcd(st[k-1][i],st[k-1][i+half]);
            }
        }
        log = new int[n + 1];
        for(int i =2;i<=n;i++) {
            log[i] = log[i / 2] + 1;
        }
            int answer = scoreWithoutDelete(n);

            for(int remove = 0;remove < n;remove++) {
                int score = scoreAfterDelete(nums, remove);

                answer = Math.max(answer, score);
            }
        return answer;
        }
    }
