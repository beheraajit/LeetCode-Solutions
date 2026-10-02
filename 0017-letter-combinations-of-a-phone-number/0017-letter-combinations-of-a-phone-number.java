class Solution {
    public List<String> letterCombinations(String digits) {
        List<String> result = new ArrayList<>();

        if(digits.length() == 0) {
            return result;
        }

        String[] list = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

        function(digits,0,"",list,result);

        return result;
        


    }

    public void function(String digits, int idx, String curr, String[] list, List<String> result) {


        if(idx == digits.length()) {
            result.add(curr);
            return;
        }


        String letter = list[digits.charAt(idx) - '0'];

        for(int i=0;i<letter.length();i++) {
            function(digits,idx + 1,curr + letter.charAt(i),list, result);
        }
    }
}