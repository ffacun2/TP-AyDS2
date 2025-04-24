package requests;


public class OKResponse {

	private boolean success;
	
	public OKResponse (boolean success) {
		this.success = success;
	}
	
	public boolean isSuccess() {
		return success;
	}
}
