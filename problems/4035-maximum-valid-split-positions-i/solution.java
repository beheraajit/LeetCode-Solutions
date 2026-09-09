class Solution {
    public int gcd(int a, int b) {
        while(b != 0) {
            int temp = a % b;
            a = b;
            b = temp;
        }
        return a;
    }
    public int getScore(int[] arr) {
        int n = arr.length;

        if(n <= 1) {
            return 0;
        }
        int[] prefix = new int[n];
        int[] suffix = new int[n];

        prefix[0] = arr[0];

        for(int i=1;i<n;i++) {
            prefix[i] = gcd(prefix[i-1], arr[i]);
        }

        suffix[n - 1] = arr[n - 1];

        for(int i = n-2;i>=0;i--) {
            suffix[i] = gcd(arr[i], suffix[i+1]);
        }

        int score = 0;

        for(int i=0;i<n-1;i++) {
            if(prefix[i] == suffix[i+1]) {
                score++;
            }
        }
        return score;

    }
    public int maxValidSplits(int[] nums) {
        int n = nums.length;

        int answer = getScore(nums);

        for(int remove =0;remove<n;remove++) {
            int[] arr = new int[n-1];

            int index = 0;
            for(int i=0;i<n;i++) {
                if(i != remove) {
                    arr[index] = nums[i];
                    index++;
                }
            }
            answer = Math.max(answer, getScore(arr));
        }
        return answer;
    }

}
