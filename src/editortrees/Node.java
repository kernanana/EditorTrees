package editortrees;

import java.util.ArrayList;

import editortrees.EditTree.HeightandBalance;
import editortrees.EditTree.Nodeinfo;
import editortrees.EditTree.Track;

/**
 * A node in a height-balanced binary tree with rank. Except for the NULL_NODE,
 * one node cannot belong to two different trees.
 * 
 * @author <<Kernan Lee>>
 */
public class Node {

	enum Code {
		SAME, LEFT, RIGHT;

		// Used in the displayer and debug string
		public String toString() {
			switch (this) {
			case LEFT:
				return "/";
			case SAME:
				return "=";
			case RIGHT:
				return "\\";
			default:
				throw new IllegalStateException();
			}
		}
	}

	// The fields would normally be private, but for the purposes of this class,
	// we want to be able to test the results of the algorithms in addition to the
	// "publicly visible" effects
	DisplayableNodeWrapper displayableNodeWrapper;
	char data;
	Node left, right; // subtrees
	int rank; // inorder position of this node within its own subtree.
	Node parent;
	Node child;
	Balance rebalance = new Balance();
	Code balance;
	int height = 1;
	int leftsize= 0;
	int rightsize = 0;

	// Feel free to add other fields that you find useful.
	// You probably want a NULL_NODE, but you can comment it out if you decide
	// otherwise.
	// The NULL_NODE uses the "null character", \0, as it's data and null children,
	// but they could be anything since you shouldn't ever actually refer to them in
	// your code.
	static final Node NULL_NODE = new Node('\0', null, null);
	// Node parent; You may want parent, but think twice: keeping it up-to-date
	// takes effort too, maybe more than it's worth.
	
	public Node() {
		this.data = '\0';
		this.right = null;
		this.left = null;
	}
	
	
	
	public Node(char data, Node left, Node right) {
		// TODO: write this.
		this.data = data;
		this.left = left;
		this.right = right;
		displayableNodeWrapper = new DisplayableNodeWrapper(this);
	}

	public Node(char data) {
		// Make a leaf
		this(data, NULL_NODE, NULL_NODE);
		this.rank = 0;
		this.rebalance.code = Code.SAME;
		this.balance = Code.SAME;
	}
	
	public Node(char data,int rank, Code code) {
		// Make a leaf
		this(data, null, null);

		this.rank = rank;
		this.rebalance.code = code;
		this.balance = code;
	}
	
	

	// Provided to you to enable testing, please don't change.
	int slowHeight() {
		if (this == NULL_NODE) {
			return -1;
		}
		return Math.max(left.slowHeight(), right.slowHeight()) + 1;
	}

	// Provided to you to enable testing, please don't change.
	public int slowSize() {
		if (this == NULL_NODE) {
			return 0;
		}
		return left.slowSize() + right.slowSize() + 1;
	}
	
//	public Node add(char ch, Nodeinfo rotations) {
//		if(this == NULL_NODE) {
//			return new Node(ch);
//		}
//		else if(this.right != NULL_NODE) {
//			right = right.add(ch, rotations);
//			this.updateCodeRight();
//		}
//		else if(this.right == NULL_NODE) {
//			this.updateCodeRight();
//			right = new Node(ch);
//		}
//		if(rebalance.balanced == false) {
//			rotations.count++;
//			return this.singleRotateLeft();
//		}
//		return this;
//	}


	public Node add(char ch, int pos, Nodeinfo rotations) {
		// TODO Auto-generated method stub
		if(this == NULL_NODE && pos != 0) {
			throw new IndexOutOfBoundsException();
		}
		if(this == NULL_NODE) {
			return new Node(ch);
		}
		else if(pos <= this.rank ) {
			this.rank++;
			left = left.add(ch, pos,rotations);
			leftsize++;
			this.height = Math.max(this.left.height, this.right.height) + 1;			
			this.updateCode("Left");
		}
		else if(pos > this.rank) {
			pos = pos - rank - 1;
			right = right.add(ch, pos,rotations);
			rightsize++;
			this.height = Math.max(this.left.height, this.right.height) + 1;	
			this.updateCode("Right");
		}
		if(rebalance.balanced == false) {
			rotations.count++;
			if(this.left.left == NULL_NODE) {
				this.left.left.height = 0;
			}
			if(this.left.right == NULL_NODE) {
				this.left.right.height = 0;
			}
			//single left
			if(this.rebalance.code.equals(Code.RIGHT) && this.right.rebalance.code.equals(Code.RIGHT)) {
				return this.singleRotateLeft();
			}
			//single right
			else if(this.rebalance.code.equals(Code.LEFT) && this.left.rebalance.code.equals(Code.LEFT)){
				return this.singleRotateRight();
			}
			//double left when right left
			else if(this.rebalance.code.equals(Code.RIGHT)  && this.right.rebalance.code.equals(Code.LEFT)) {
				rotations.count++;
				this.right = this.right.singleRotateRight();
				return this.singleRotateLeft();
			}
			//double right when left right
			else {
				rotations.count++;
				this.left = this.left.singleRotateLeft();
				return this.singleRotateRight();
				}
		}
		this.height = Math.max(this.left.height, this.right.height) + 1;
		return this;
	}

	public Node delete(int pos, Nodeinfo rotations) {
		// TODO Auto-generated method stub
		if(this == NULL_NODE) {
			return this;
		}
		//traverse left
		else if(pos < rank) {
			this.rank--;
			this.left = this.left.delete(pos, rotations);
			this.leftsize--;
			this.height = Math.max(left.height, right.height) + 1;
			this.updateCode("Right");
		}
		//traverse right
		else if(pos > rank) {
			pos = pos - rank - 1;
			this.right = this.right.delete(pos, rotations);
			this.rightsize--;
			this.height = Math.max(left.height, right.height) + 1;
			this.updateCode("Left");
		}
		//delete happens here
		else if(pos == rank){
			Node temp;
			if(this.left == NULL_NODE && this.right == NULL_NODE) {
				return NULL_NODE;
			}
			else if(this.left == NULL_NODE) {
				return this.right;
			}
			else if(this.right == NULL_NODE) {
				return this.left;
			}
			else {
				temp = this.right;
				while(temp.left != NULL_NODE) {
					temp = temp.left;
					this.rightsize--;
				}
				this.data = temp.data;
				char ri = this.right.data;
				this.right = this.right.delete(0, rotations);
				
				if(this.right == NULL_NODE || this.right.data != ri) {
					this.rightsize = this.rightsize-1;
				}
				
				this.height = Math.max(this.left.height, this.right.height) + 1;
				if(temp != NULL_NODE) {
					this.updateCode("Left");
				}
//				return this;
				}
			}
		
		if(rebalance.balanced == false) {
			rotations.count++;
			if(this.left.left == NULL_NODE) {
				this.left.left.height = 0;
			}
			if(this.left.right == NULL_NODE) {
				this.left.right.height = 0;
			}
			//single left
			if(this.rebalance.code.equals(Code.RIGHT) && this.right.rebalance.code.equals(Code.RIGHT)) {
				return this.singleRotateLeft();
			}
			//single right
			else if(this.rebalance.code.equals(Code.LEFT) && this.left.rebalance.code.equals(Code.LEFT)){
				return this.singleRotateRight();
			}
			//double left when right left
			else if(this.rebalance.code.equals(Code.RIGHT)  && this.right.rebalance.code.equals(Code.LEFT)) {
				rotations.count++;
				this.right = this.right.singleRotateRight();
				return this.singleRotateLeft();
			}
			//double right when left right
			else {
				rotations.count++;
				this.left = this.left.singleRotateLeft();
				return this.singleRotateRight();
			}
		}
		
		this.height = Math.max(this.left.height, this.right.height) + 1;
		return this;
	}

	class Balance{
		private Code code = Code.SAME;
		private boolean balanced = true;
	}
	
	public void updateCode(String rol) {
		Code b = this.rebalance.code;

		int lh = this.left.height;
		int rh = this.right.height;
		if(this.left == NULL_NODE) {
			lh = 0;
		}
		if(this.right == NULL_NODE) {
			rh = 0;
		}
		if(rol.equals("Right")) {
			if((b.equals(Code.LEFT) && lh == rh) || (b.equals(Code.SAME) && lh == rh)) {
				rebalance.code = Code.SAME;
			}
			else if(b.equals(Code.SAME)) {
				rebalance.code = Code.RIGHT;
			}
			else if(b.equals(Code.RIGHT) && (rh - lh > 1)){
				rebalance.balanced = false;
			}
		}else if(rol.equals("Left")) {
			if((b.equals(Code.SAME) && lh == rh) || (b.equals(Code.RIGHT) && lh == rh)) {
				rebalance.code = Code.SAME;
			}else if(b.equals(Code.SAME)) {
				rebalance.code = Code.LEFT;
				
			}else if(b.equals(Code.LEFT) && (lh - rh > 1) ){
				rebalance.balanced = false;
			}
		}
		this.balance = rebalance.code;
	}
	

	public Node singleRotateLeft() {
		Node parent = this;
		Node child = this.right;
		Node temp = child.left;
		child.left = parent;
		parent.rightsize = 0;
		parent.right = temp;
		parent.rebalance.balanced = true;
		child.rank = parent.rank + child.rank + 1;
		child.leftsize = parent.leftsize+1;

		parent.nullHeight();
		parent.height = Math.max(parent.left.height, parent.right.height) + 1;
		child.height = Math.max(child.left.height, child.right.height) + 1;
		
		child.left.restoreBalance();
		child.restoreBalance();
		return child;
	}

	public void restoreBalance() {
		if(this.left == NULL_NODE) {
			this.left.height = 0;
		}
		if(this.right == NULL_NODE) {
			this.right.height = 0;
		}
		if(this.left.height > this.right.height) {
			this.rebalance.code = Code.LEFT;
		}
		else if(this.left.height < this.right.height) {
			this.rebalance.code = Code.RIGHT;
		}
		else {
			this.rebalance.code = Code.SAME;
		}
		this.balance = this.rebalance.code;
	}

	public Node singleRotateRight() {
		Node parent = this;
		Node child = this.left;
		Node temp = child.right;
		child.right = parent;
		parent.leftsize = 0;
		parent.left = temp;
		parent.rebalance.balanced = true;
		parent.rank = parent.rank - child.rank - 1;
		parent.height = Math.max(parent.left.height, parent.right.height) + 1;
		child.height = Math.max(child.left.height, child.right.height) + 1;
		child.rightsize = parent.rightsize+1;
		child.right.restoreBalance();
		child.restoreBalance();
		return child;
	}
	
	private void nullHeight() {
		// TODO Auto-generated method stub
		if(this.left == NULL_NODE) {
			this.left.height = 0;
		}
		if(this.right == NULL_NODE) {
			this.right.height = 0;
		}
	}
	
	
	
	public StringBuilder toString(StringBuilder string) {
		if(this == NULL_NODE) {
			return string;
		}
		left.toString(string);
		string.append(this.data);
		right.toString(string);
		return string;
	}
	
	public StringBuilder toRankString(StringBuilder string) {
		// TODO Auto-generated method stub
		if(this == NULL_NODE) {
			return string;
		}
		string.append(this.data);
		string.append(this.rank);
		string.append(",");
		string.append(" ");
		left.toRankString(string);
		right.toRankString(string);
		return string;
	}

	public Object toDebugString(StringBuilder string) {
		// TODO Auto-generated method stub
		if(this == NULL_NODE) {
			return string;
		}
		string.append(this.data);
		string.append(this.rank);
		string.append(this.rebalance.code);
		string.append(",");
		string.append(" ");
		left.toDebugString(string);
		right.toDebugString(string);
		return string;
	}
	
	public char get(int pos) {
		// TODO Auto-generated method stub
		if(this == NULL_NODE) {
			throw new IndexOutOfBoundsException();
		}
		if(rank > pos) {
			return left.get(pos);
		}
		else if(rank < pos) {
			pos = pos - rank - 1;
			return right.get(pos);
		}
		return this.data;
	}
	
	public StringBuilder get(int left, int right, StringBuilder string, Track track,int prev) {
		// TODO Auto-generated method stub
		
		if(left<prev && track.tracker != 0 && this.hasLeft()) {

				this.left.get(left,right,string,track,prev+this.left.rank-this.rank);

//			}

		}
		
		if(left <= prev && prev <= right && track.tracker!=0) {
			track.tracker-=1;
			string.append(this.data);
		}
		
		if(this.hasRight() && track.tracker != 0) {
			if(this.rank == track.check) {
				track.lOr = true;
			}
			this.right.get(left, right, string,track,prev+this.right.rank+1);

		}

		
		
		
		
		
		
		return string;
	}


	public HeightandBalance ranksMatchLeftSubtreeSize() {
		// TODO Auto-generated method stub
		if(this == NULL_NODE) {
			return new HeightandBalance(0, true);
		}
		HeightandBalance lefthb = left.ranksMatchLeftSubtreeSize();
		HeightandBalance righthb = right.ranksMatchLeftSubtreeSize();
		boolean bool = false;
		if(lefthb.bool && righthb.bool) {
			if(lefthb.height == rank) {
				bool = true;
			}
		}
		int size = lefthb.height + righthb.height + 1;
		return new HeightandBalance(size, bool);
	}

	public int fastheight(){
		fastHeight count = new fastHeight();
		if(this == NULL_NODE) {
			return -1;
		}
		
		return fastheighthelper(count).fh;
	}
	
	private fastHeight fastheighthelper(fastHeight count) {
		// TODO Auto-generated method stub
		if(!this.hasLeft() && !this.hasRight()) {
			return count;
		}
		
		
		if(this.rebalance.code == Code.RIGHT) {
			count.fh = this.right.fastheighthelper(count).fh + 1;
		}
		else if(this.rebalance.code == Code.LEFT) {
			count.fh = this.left.fastheighthelper(count).fh + 1;
		}
		else {
			count.fh = this.left.fastheighthelper(count).fh + 1;
		}
		return count;
	}

	class fastHeight {
		private int fh = 0;
	}
	
	public HeightandBalance balanceCodesCorrect() {
		// TODO Auto-generated method stub
		if(this == NULL_NODE) {
			return new HeightandBalance(0, true);
		}
		HeightandBalance lefthb = left.balanceCodesCorrect();
		HeightandBalance righthb = right.balanceCodesCorrect();
		if(lefthb.bool == false || righthb.bool == false) {
			return new HeightandBalance(0, false);
		}
		boolean bool = false;
		if(lefthb.height == righthb.height && this.balance.equals(Code.SAME)) {
			bool = true;
		}else if(lefthb.height > righthb.height && this.balance.equals(Code.LEFT)){
			bool = true;
		}else if(lefthb.height < righthb.height && this.balance.equals(Code.RIGHT)) {
			bool = true;
		}else {
			bool =false;
		}
		int size = Math.max(lefthb.height, righthb.height) + 1;
		return new HeightandBalance(size, bool);
	}
		



	
	// You will probably want to add more constructors and many other
	// recursive methods here. I added 47 of them - most were tiny helper methods
	// to make the rest of the code easy to understand. My longest method was
	// delete(): 20 lines of code other than } lines. Other than delete() and one of
	// its helpers, the others were less than 10 lines long. Well-named helper
	// methods are more effective than comments in writing clean code.
	
	// TODO: By the end of milestone 1, consider if you want to use the graphical debugger. See
	// the unit test throwing an error and the README.txt file.
	
	
	
	
	public boolean hasLeft() {
		return this.left != NULL_NODE;
	}

	public boolean hasRight() {
		return this.right != NULL_NODE;
	}

	public boolean hasParent() {
		return false;
	}

	public Node getParent() {
		return NULL_NODE;
	}
	
	public Node stringToTree(int l, int r, String s) {
		// TODO Auto-generated method stub
		int mid = (l + r)/2;
		if(l > r) {
			return this;
		}
		Node node = new Node(s.charAt(mid));
		node.rank = mid - l;
		if(l - r == 1) {
			node.balance = Code.LEFT;
		}
		else if(r - l == 1){
			node.balance = Code.RIGHT;
		}
		else {
			node.balance = Code.SAME;
		}
		if(l == r) {
			return node;
		}
		node.left = node.left.stringToTree(l, mid - 1, s);
		node.right = node.right.stringToTree(mid + 1, r, s);
		return node;
	}
	
//	public Node stringToTree(double index, double left, double right, String s) {
//		// TODO Auto-generated method stub
//		double leftindex = Math.floor((index + left)/2);
//		double rightindex = Math.floor((index + right)/2);
//		if(s.charAt((int)index) != s.charAt((int)leftindex)) {
//			this.left = stringToTree(leftindex, left, index, s);
//		}
//		if(s.charAt((int)index) != s.charAt((int)rightindex)) {
//			this.right = stringToTree(rightindex, index, right, s);
//		}
//		int result = (int) index;
//		Node node = new Node(s.charAt(result));
//		node.left = this.left;
//		node.right = this.right;
//		return node;
//		
//	}





	
}