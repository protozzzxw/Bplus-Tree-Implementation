import java.util.ArrayList;

public abstract class Node {
	protected int capacity;     //Capacity means the max amount of index keys one node can have.  //
	Node Parent;                //Pointer  to the parent, if exists, of a node. //
	ArrayList<Node> Child=new ArrayList<>();     //Pointer  to the parent, if exists, of a node. //
	ArrayList<Double> val=new ArrayList<>();     //Pointer  to the parent, if exists, of a node. //
	public abstract void addValue(double key);
	public abstract Node split(int p);
	public Node findTarget(double key) {
		Node target=this;
		while(true) {
			if(target instanceof Leaf) break;
			int position=0;
			if(target.val.contains(key)) position=target.val.indexOf(key)+1;
			else position=target.searchValue(key);
			target=target.Child.get(position);
		}
		return target;
	}
	public boolean isOverFlow() {
		return val.size()>capacity;
	}	
	public int searchValue(double key) {
		if(val.isEmpty()) return 0;
		if(val.contains(key)) return val.indexOf(key);
		int n = val.size();
		int pivot = 0;
		int s = 0, e = n - 1;
		if (val.get(s) > key) {
			return s;
		} else if (val.get(e) < key) {
			return e+1;
		} else {
			while (true) {
				pivot = (s + e) / 2;
				if (val.get(pivot) < key) {
					s = pivot;
				} else
					e = pivot;
				if(e-s==1) break;
			}
			return e;
		}
	}
}
