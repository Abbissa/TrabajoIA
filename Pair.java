package main;
public class Pair<O,E> {


	private E right;
	private O left;

	public Pair(O left,E right){
		this.right= right;
		this.left = left;

	}

	public E getRight() {
		return right;
	}

	public void setRight(E right) {
		this.right = right;
	}

	public O getLeft() {
		return left;
	}

	public void setLeft(O left) {
		this.left = left;
	}


	

}