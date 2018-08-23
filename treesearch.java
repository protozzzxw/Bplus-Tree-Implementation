import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class treesearch {
	Node root; 			// pointer to the root of the tree
	Node target; 		// pointer to the target of insert operation
	int order; 			// Specify the order of the whole B+ tree

	public static void main(String args[]) throws IOException {
		File file1 = new File(args[0]);
		File file2 = new File("output_file.txt");
		if (!file2.exists()) {
			file2.createNewFile();
		}
		BufferedReader br = null;
		BufferedWriter bw = null;
		String str = "";
		int row = 1; 									// record which line it is reading now
		int b1, e1, b2, e2 = 0;
		double key = 0, key1 = 0, key2 = 0;
		String value = "";
		treesearch BPT = new treesearch();
		try {
			br = new BufferedReader(new FileReader(file1));
			bw = new BufferedWriter(new FileWriter(file2));
			while ((str = br.readLine()) != null) {
				if (row++ == 1) { 						// determine it it is the first line of input file
					int m = Integer.parseInt(str);
					BPT.Initialize(m);
				} else {
					if (str.charAt(0) == 'I') { 		// determine if the operation is Insert
						b1 = str.indexOf('(') + 1;
						e1 = str.indexOf(',');
						b2 = e1 + 1;
						e2 = str.indexOf(')');
						key = Double.parseDouble(str.substring(b1, e1));
						value = str.substring(b2, e2);
						BPT.Insert(key, value);
					} else if (str.contains(",")) { 	// determine if the operation is range search
						b1 = str.indexOf('(') + 1;
						e1 = str.indexOf(',');
						b2 = e1 + 1;
						e2 = str.indexOf(')');
						key1 = Double.parseDouble(str.substring(b1, e1));
						key2 = Double.parseDouble(str.substring(b2, e2));
						ArrayList<Pair> pairs = BPT.Search(key1, key2);
						if (pairs.size() == 0) {
							bw.write("null"); 			// output "null" if the search result is empty
							bw.newLine();
							continue;
						}
						int n = pairs.size();
						String result = "";
						for (int i = 0; i < n; i++) {
							result += "(" + pairs.get(i).key + "," + pairs.get(i).value + ")" + ",";
						}
						result = result.substring(0, result.length() - 1);
						bw.write(result);
						bw.newLine();
					} else {
						b1 = str.indexOf('(') + 1;
						e1 = str.indexOf(')');
						key = Double.parseDouble(str.substring(b1, e1));
						ArrayList<String> dat = BPT.Search(key);
						if (dat == null) {
							bw.write("null"); 			// output "null" if the search result is empty
							bw.newLine();
							continue;
						}
						String result = "";
						int n = dat.size();
						for (int i = 0; i < n; i++) {
							result += dat.get(i) + ",";
						}
						result = result.substring(0, result.length() - 1);
						bw.write(result);
						bw.newLine();
					}
				}
			}
			br.close();
			bw.close();
		} catch (IOException ex) {
			throw new IOException(ex);
		}
	}

	/**
	 * Initialize a B+ tree: set the order of the tree to m; create a new Leaf, and
	 * set root point to it.
	 */
	public void Initialize(int m) {
		order = m;
		root = new Leaf(order - 1);
	}

	/** Insert a new pair of key and value to the tree. */
	public void Insert(double key, String value) {
		target = root.findTarget(key);
		target.addValue(key);
		Leaf target1 = (Leaf) target;
		target1.addPair(key, value);
		while (target.isOverFlow()) {
			int m = target.val.size();
			int mid = m / 2;
			double pivot = target.val.get(mid);
			Node sibling = target.split(mid);
			if (target == root) {
				root = new Index(order - 1);
				target.Parent = root;
				root.Child.add(target);
			}
			target.Parent.addValue(pivot);
			sibling.Parent = target.Parent;
			int index = target.Parent.Child.indexOf(target) + 1;
			target.Parent.Child.add(index, sibling);
			target = target.Parent;
		}
	}

	/**
	 * Search all the values belong to the key, and save them into an ArrayList,
	 * return the Array List.
	 */
	public ArrayList<String> Search(double key) {
		target = root.findTarget(key);
		if (!target.val.contains(key))
			return null;
		int index = target.val.indexOf(key);
		Leaf target1 = (Leaf) target;
		ArrayList<Pair> pairs = target1.data.get(index);
		int n = pairs.size();
		ArrayList<String> result = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			result.add(pairs.get(i).value);
		}
		return result;

	}

	/**
	 * Search all the pairs such their keys satisfy key1=<key<=key2, save them into
	 * an ArrayList and return it.
	 */
	public ArrayList<Pair> Search(double key1, double key2) {
		ArrayList<Pair> result = new ArrayList<>();
		target = root.findTarget(key1);
		Leaf target1 = (Leaf) target;
		int index = target1.searchValue(key1);
		if (index >= target1.val.size()) {
			target1 = target1.Next;
			index = 0;
		}
		double k = target1.val.get(index);
		while (true) {
			if (k > key2)
				break;
			result.addAll(target1.data.get(index));
			if (++index > target1.val.size() - 1) {
				target1 = target1.Next;
				index = 0;
			}
			k = target1.val.get(index);
		}
		return result;
	}

}
