public class Trio<O,A,E> {


	private E right;
	private A center;
	private O left;

	public Trio(O left,A center,E right){
		this.right= right;
		this.center= center;
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
	
	public A getCenter() {
		return center;
	}

	public void setCenter(A center) {
		this.center = center;
	}


	

}
