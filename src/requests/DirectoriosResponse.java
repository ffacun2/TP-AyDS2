package requests;

import java.util.Set;

public class DirectoriosResponse {
	
	private Set<String> nicks;

	public DirectoriosResponse (Set<String> nicks) {
		this.nicks = nicks;
	}
	
	public Set<String> getNicks () {
		return this.nicks;
	}
}
