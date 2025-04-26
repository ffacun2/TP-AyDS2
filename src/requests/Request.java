package requests;

import java.io.Serializable;

import interfaces.IEnviable;

public abstract class Request implements IEnviable, Serializable {
	static final long serialVersionUID = 1L;
	private String nickname;
	
	public Request(String nickname) {
		this.nickname = nickname;
	}
	
	public String getNickname() {
		return this.nickname;
	}
}
