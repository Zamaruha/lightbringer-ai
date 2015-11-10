package api;

public abstract class MSAgent {
	
	MSField field = null;
	
	public void setField(MSField field) {
		this.field = field;
	}	
	
	public abstract boolean solve();



}
