package com.lie_party;

public class Player {
	private int id;
	private String name;
	private int score;
	private String answer;
	private int likes;
	private String roomCode;
	private boolean standBy;
	private boolean isHost;
	
	public Player(){
		
	}
	
	public Player(String name){
		this.name = name;
	}
	
	
	
	//getters and setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}

	

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public String getRoomCode() {
		return roomCode;
	}

	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}
	

	public boolean isStandBy() {
		return standBy;
	}

	public void setStandBy(boolean standBy) {
		this.standBy = standBy;
	}

	public boolean getIsHost() {
		return isHost;
	}

	public void setIsHost(boolean isHost) {
		this.isHost = isHost;
	}
	
	public void adjustScore(int delta){
		this.score+=delta;
	}


	
	
	
}
