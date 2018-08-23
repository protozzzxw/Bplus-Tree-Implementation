

public class Index extends Node {
	public Node split(int split) {
		int m=this.val.size();
		Index sibling=new Index(capacity);
		sibling.val.addAll(this.val.subList(split + 1, m));
		sibling.Child.addAll(this.Child.subList(split + 1, m + 1));
		this.val.subList(split, m).clear();
		this.Child.subList(split + 1, m + 1).clear();
		int n=sibling.Child.size();
		for(int i=0;i<n;i++) {
			sibling.Child.get(i).Parent=sibling;
		}
		return sibling;
	}
	public Index(int m) {
		capacity=m;
	}
	public void addValue(double key) {
		int index = searchValue(key);
		val.add(index, key);
	}	
}
