package requests;

import interfaces.IEnviable;

public abstract class Request implements IEnviable{

	private String nickname;
	
	public Request() {
		super();
	}
	
	public String getNickname() {
		return this.nickname;
	}
}
