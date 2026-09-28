class MyStack {
    Queue<Integer> q = new LinkedList<>();
    Queue<Integer> w = new LinkedList<>();

    public MyStack() {
        
    }
    
    public void push(int x) {
        q.add(x);
    }
    
    public int pop() {
        
            while(q.size() > 1){
                w.add(q.remove());
            }
            int ans = q.remove();

            Queue<Integer> temp = q;
            q = w;
            w = temp;

        return ans;
    }
    
    public int top() {
        
            while(q.size() > 1){
                w.add(q.remove());
            }
            int ans = q.peek();
            w.add(q.remove());
            Queue<Integer> temp = q;
            q = w;
            w = temp;
        
        return ans;
    }
    
    public boolean empty() {
        return q.isEmpty() && w.isEmpty();
    }
}

/**
 * Your MyStack object will be instantiated and called as such:
 * MyStack obj = new MyStack();
 * obj.push(x);
 * int param_2 = obj.pop();
 * int param_3 = obj.top();
 * boolean param_4 = obj.empty();
 */