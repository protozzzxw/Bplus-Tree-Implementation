import java.util.ArrayList;

public class Leaf extends Node{
	Leaf Prev;
	Leaf Next;
	ArrayList<ArrayList<Pair>> data;
	public Node split(int split) {
		int m=this.val.size();
		Leaf sibling = new Leaf(capacity);
		if (this.Next != null) {
			Leaf oldNext = this.Next;
			this.Next = sibling;
			sibling.Next = oldNext;
			oldNext.Prev = sibling;
			sibling.Prev = this;
		} else {
			this.Next = sibling;
			sibling.Prev = this;
		}
		sibling.val.addAll(this.val.subList(split, m));
		sibling.data.addAll(this.data.subList(split, m));
		this.val.subList(split, m).clear();
		this.data.subList(split, m).clear();
		return sibling;
	}
	
	public Leaf(int m) {
		capacity=m;	
		this.data=new ArrayList<>();
	}
	
	public void addValue(double key) {
		if(!val.contains(key)) {
			int index = searchValue(key);
			this.val.add(index, key);
			ArrayList<Pair> emptyAl=new ArrayList<>();
			this.data.add(index,emptyAl);
		}	
	}

	public void addPair(double key, String value) {
		Pair e=new Pair(key,value);
		int index=this.val.indexOf(key);
		this.data.get(index).add(e);
	}
}
