package requests;

import interfaces.IEnviable;

public abstract class Request implements IEnviable {

	private String nickname;
	
	public Request(String nickname) {
		this.nickname = nickname;
	}
	
	public String getNickname() {
		return this.nickname;
	}
}
