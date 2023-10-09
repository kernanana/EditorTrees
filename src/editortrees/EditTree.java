package editortrees;

import buildtree.BinaryTree.Node;
import editortrees.Node.Code;

/**
 * A height-balanced binary tree with rank that could be the basis for a text
 * editor.
 * 
 * @author <<Kernan Lee>>
 * @author <<Jinyoung Choi>>
 * TODO: Acknowledge anyone else you got help
 *         from here, along with the help they provided:
 * 
 * 
 * 
 */
public class EditTree {
	private DisplayableBinaryTree display;
	Node root;
	private int size = 0;
	private int count = 0;
	private Object Track;
	/**
	 * MILESTONE 1 Construct an empty tree
	 */
	public EditTree() {
		root = Node.NULL_NODE;
	}

	/**
	 * MILESTONE 1 Construct a single-node tree whose element is ch
	 * 
	 * @param ch
	 */
	public EditTree(char ch) {
		root = new Node(ch);
	}

	/**
	 * MILESTONE 2 Make this tree be a copy of e, with all new nodes, but the same
	 * shape and contents. You can write this one recursively, but you may not want
	 * your helper to be in the Node class.
	 * 
	 * @param e
	 */
	public EditTree(EditTree e) {
		if(e.root == Node.NULL_NODE) {
			 root = Node.NULL_NODE;
		}else {
			Node temp =e.root;
			this.size = e.size;
			root = copyTree(temp);
		}
		
	}


	private Node copyTree(Node e) {
		
		Node temp = new Node(e.data,e.rank,e.balance);
		if(e.left == Node.NULL_NODE) {
			temp.left = Node.NULL_NODE;
		}
		if(e.right == Node.NULL_NODE) {
			temp.right = Node.NULL_NODE;
		}
		
		if(e.hasLeft()) {
			temp.left = copyTree(e.left);
		}
		if(e.hasRight()) {
			temp.right = copyTree(e.right);
		}

		
		
		return temp;
	}

	/**
	 * MILESTONE 3 Create an EditTree whose toString is s. This can be done in O(N)
	 * time, where N is the size of the tree (note that repeatedly calling insert()
	 * would be O(N log N), so you need to find a more efficient way to do this.
	 * 
	 * @param s
	 */
	public EditTree(String s) {
		root = Node.NULL_NODE;
		if(s.length() > 0) {
			root = root.stringToTree(0, s.length() - 1, s);
			this.size = s.length();
			this.count = 0;
		}
	}

	/**
	 * MILESTONE 1 return the string produced by an in-order traversal of this tree
	 */
	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		return root.toString(string).toString();
	}

	/**
	 * MILESTONE 1 Just modify the value of this.size whenever adding or removing a
	 * node. This is O(1).
	 * 
	 * @return the number of nodes in this tree, not counting the NULL_NODE if you
	 *         have one.
	 */
	public int size() {
		return this.size; // nothing else to do here.
	}

	/**
	 * MILESTONE 1
	 * 
	 * @param ch character to add to the end of this tree.
	 */
	public void add(char ch) {
		// Notes:
		// 1. Please document chunks of code as you go. Why are you doing what
		// you are doing? Comments written after the code is finalized tend to
		// be useless, since they just say WHAT the code does, line by line,
		// rather than WHY the code was written like that. Six months from now,
		// it's the reasoning behind doing what you did that will be valuable to
		// you!
		// 2. Unit tests are cumulative, and many things are based on add(), so
		// make sure that you get this one correct.
		add(ch,size);
		
		
	}

	/**
	 * MILESTONE 1
	 * 
	 * @param ch  character to add
	 * 
	 * @param pos character added in this in-order position Valid positions range
	 *            from 0 to the size of the tree, inclusive (if called with size, it
	 *            will append the character to the end of the tree).
	 * @throws IndexOutOfBoundsException if pos is negative or too large for this
	 *                                   tree.
	 */
	public void add(char ch, int pos) throws IndexOutOfBoundsException {
		// You can use your O(1) size field/method to determine if the index is valid.
		Nodeinfo count = new Nodeinfo();
		root = root.add(ch, pos,count);
		size++;
		this.count += count.count;
	}

	/**
	 * MILESTONE 1 This one asks for more info from each node. You can write it
	 * similar to the arraylist-based toString() method from the BinarySearchTree
	 * assignment. However, the output isn't just the elements, but the elements AND
	 * ranks. Former students recommended that this method, while making it a little
	 * harder to pass tests initially, saves them time later since it catches weird
	 * errors that occur when you don't update ranks correctly. For the tree with
	 * root b and children a and c, it should return the string: [b1, a0, c0] There
	 * are many more examples in the unit tests.
	 * 
	 * @return The string of elements and ranks, given in an PRE-ORDER traversal of
	 *         the tree.
	 */
	public String toRankString() {
		StringBuilder string = new StringBuilder();
		String output = (String) root.toRankString(string).toString(); // replace by a real calculation.
		if(output.length() > 1) {
			return "[" + output.substring(0, output.length() -2)  + "]";
		}
		return "[" + output + "]";
	}

	/**
	 * MILESTONE 1
	 * 
	 * @param pos position in the tree
	 * @return the character at that position
	 * @throws IndexOutOfBoundsException if pos is negative or too big. Note that
	 *                                   the pos is now EXclusive of the size of the
	 *                                   tree, since there is no character there.
	 *                                   But you can still use your size
	 *                                   field/method to determine this.
	 */
	public char get(int pos) throws IndexOutOfBoundsException {
		return root.get(pos); // replace by a real O(log n) calculation.
	}

	// MILESTONE 1: They next two "slow" methods are useful for testing, debugging 
	// and the graphical debugger. They are each O(n) and don't make use of rank or 
	// size. In fact, they are the same as you used in an earlier assignment, so we 
	// are providing them for you.
	// Please do not modify them or their recursive helpers in the Node class.
	public int slowHeight() {
		return root.slowHeight();
	}

	public int slowSize() {
		return root.slowSize();
	}

	/**
	 * MILESTONE 1 Returns true iff (read as "if and only if") for every node in the
	 * tree, the node's rank equals the size of the left subtree. This will be used
	 * to check that your ranks are being updated correctly. So when you get a
	 * subtree's size, you should NOT refer to rank but find it brute-force, similar
	 * to slowSize(), and actually calling slowSize() might be a good first-pass.
	 * 
	 * For full credit, then refactor it to make it more efficient: do this in O(n)
	 * time, so in a single pass through the tree, and with only O(1) extra storage
	 * (so no temp collections).
	 * 
	 * Instead of using slowSize(), use the same pattern as the sum of heights
	 * problem in HW5. We put our helper class inside the Node class, but you can
	 * put it anywhere it's convenient.
	 * 
	 * PLEASE feel free to call this method (or its recursive helper) in your code
	 * while you are writing your add() method if rank isn't working correctly. You
	 * may also modify it to print WHERE it is failing. It may be most important to
	 * use in Milestone 2, when you are updating ranks during rotations. (We added
	 * some commented-out calls to this method there so show you how it can be
	 * used.)
	 * 
	 * @return True iff each node's rank correctly equals its left subtree's size.
	 */
	
	public boolean ranksMatchLeftSubtreeSize() {
		return root.ranksMatchLeftSubtreeSize().bool; // replace by a real calculation.
	}
	
	static class HeightandBalance {
		int height;
		boolean bool;
		public HeightandBalance(int height, boolean bool) {
			this.height = height;
			this.bool = bool;
		}
	}

	/**
	 * MILESTONE 2 Similar to toRankString(), but adding in balance codes too.
	 * 
	 * For the tree with root b and a left child a, it should return the string:
	 * [b1/, a0=] There are many more examples in the unit tests.
	 * 
	 * @return The string of elements and ranks, given in an pre-order traversal of
	 *         the tree.
	 */
	public String toDebugString() {
		// replace by a real calculation.
		StringBuilder string = new StringBuilder();
		String output =  root.toDebugString(string).toString();
		if(output.length() > 1) {
			output = output.substring(0, output.length() -2);
		}
		return "[" + output  + "]";
	}
	

	/**
	 * MILESTONE 2 returns the total number of rotations done in this tree since it
	 * was created. A double rotation counts as two.
	 *
	 * @return number of rotations since this tree was created.
	 */
	public int totalRotationCount() {
// replace by a real calculation.
		return count;

	}


	class Nodeinfo{
		int count = 0;
	}
	
	/**
	 * MILESTONE 2 Returns true iff (read as "if and only if") for every node in the
	 * tree, the node's balance code is correct based on its childrens' heights.
	 * Like ranksMatchLeftSubtreeSize() above, you'll need to compare your balance
	 * code to the actual brute-force height calculation. You may start with calling
	 * slowHeight(). But then, for full credit, do this in O(n) time, so in a single
	 * pass through the tree, and with only O(1) extra storage (so no temp
	 * collections). Instead of slowHeight(), use the same pattern as the sum of
	 * heights problem in HW5. We put our helper class inside the Node class, but
	 * you can put it anywhere it's convenient.
	 * 
	 * The notes for ranksMatchLeftSubtreeSize() above apply here - this method is
	 * to help YOU as the developer.
	 * 
	 * @return True iff each node's balance code is correct.
	 */
	public boolean balanceCodesAreCorrect() {
		return root.balanceCodesCorrect().bool; // replace by a real calculation.
	}

	/**
	 * MILESTONE 2 Only write this one once your balance codes are correct. It will
	 * rely on correct balance codes to find the height of the tree in O(log n)
	 * time.
	 * 
	 * @return the height of this tree
	 */
	public int fastHeight() {
		return root.fastheight(); // replace by a real calculation.
	}

	/**
	 * MILESTONE 3
	 * 
	 * @param pos position of character to delete from this tree
	 * @return the character that is deleted
	 * @throws IndexOutOfBoundsException
	 */
	public char delete(int pos) throws IndexOutOfBoundsException {
		// Implementation requirement:
		// When deleting a node with two children, you normally replace the
		// node to be deleted with either its in-order successor or predecessor.
		// The tests assume assume that you will replace it with the
		// *successor*.
		if(pos > this.size) {
			throw new IndexOutOfBoundsException();
		}
		char value = root.get(pos);
		Nodeinfo count = new Nodeinfo();
		count.count = 0;
		this.size--;
		root = root.delete(pos, count); // replace by a real calculation.
		this.count = this.count + count.count;
		return value;
	}

	/**
	 * MILESTONE 3 This method operates in O(length), where length is the
	 * parameter provided. The way to do this is to recurse/iterate only
	 * over the nodes of the tree (and possibly their children) that
	 * contribute to the output string.
	 * 
	 * @param pos    location of the beginning of the string to retrieve
	 * @param length length of the string to retrieve
	 * @return string of length that starts in position pos
	 * @throws IndexOutOfBoundsException unless both pos and pos+length-1 are
	 *                                   legitimate indexes within this tree.
	 */
	public String get(int pos, int length) throws IndexOutOfBoundsException {
		StringBuilder string = new StringBuilder();
		Track track = new Track(length);
		track.check = root.rank;
		if(pos<0 || pos>=root.leftsize+root.rightsize+1 ||pos+length-1>=root.leftsize+root.rightsize+1|| root == Node.NULL_NODE ) {
			throw new IndexOutOfBoundsException();
		}
		return root.get(pos, pos+length-1,string,track,root.rank).toString(); // replace by a real calculation.
	}
	
	// Feel free to add whatever other methods and helpers you need,
	// like for the graphical debugger.
	public class Track {
		int tracker;
		boolean lOr = false;
		int check = 0; 
		public Track(int length) {
			this.tracker = length;
		}
		
	}
	
	public void show() {
		if (this.display == null) {
			this.display = new DisplayableBinaryTree(this, 960, 1080, true);
		} else {
			this.display.show(true);
		}
	}
}
