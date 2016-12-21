package com.lie_party;

import javax.servlet.http.HttpSession;

public class UserSessions {
	
	private int id;
	
	private int hashCode;
	
	private String roomCode;
	
	private boolean refresh;
	
	private boolean redirect;
	
	public UserSessions(){
		
	}
	
	public UserSessions(int hashCode, String roomCode){
		this.hashCode = hashCode;
		this.roomCode = roomCode;
		this.refresh = false;
		this.redirect = false;
	}
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}



	public int getHashCode() {
		return hashCode;
	}


	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}


	public String getRoomCode() {
		return roomCode;
	}

	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}
	

	public boolean getRefresh() {
		return refresh;
	}

	public void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}

	public boolean getRedirect() {
		return redirect;
	}

	public void setRedirect(boolean redirect) {
		this.redirect = redirect;
	}
	
	
	
	
	
	
}
