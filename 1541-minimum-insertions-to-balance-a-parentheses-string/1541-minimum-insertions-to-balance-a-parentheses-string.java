class Solution {
    public int minInsertions(String s) {
        int x = 0;
        int y = 0;

        for(int i=0;i<s.length();i++){
            char ch = s.charAt(i);
            if(ch == '(') {
                if(y % 2 != 0) {
                    x++;
                    y--;
                }
                y = y + 2;
            } else {
                y--;
                if(y < 0) {
                    x++;
                    y = 1;
                }
            }
        }
        return x + y;
    }
}