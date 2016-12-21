package com.lie_party;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;

import com.lie_party.controllers.Question;

public class GameSession {
	private int id;
	//private ArrayList<Player> players;
	private String code;
	private Timestamp timeStamp;
	private byte[] currentQuestion;
	private int round;
	
	private byte[] answerReview;
	private byte[] answerReviewSug;
	
	public GameSession(){
		SecureRandom random = new SecureRandom();
		this.code = new BigInteger(25, random).toString(32).toUpperCase();
		this.timeStamp = new Timestamp(System.currentTimeMillis());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	public byte[] getCurrentQuestion() {
		return currentQuestion;
	}

	public void setCurrentQuestion(byte[] currentQuestion) {
		this.currentQuestion = currentQuestion;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public byte[]  getAnswerReview() {
		return answerReview;
	}

	public void setAnswerReview(byte[]  answerReview) {
		this.answerReview = answerReview;
	}

	public byte[]  getAnswerReviewSug() {
		return answerReviewSug;
	}

	public void setAnswerReviewSug(byte[]  answerReviewSug) {
		this.answerReviewSug = answerReviewSug;
	}
	
	

	public static byte[] toByteArray(Hashtable<String, ArrayList<String>> table){
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 try{
		    ObjectOutputStream os = new ObjectOutputStream(out);
		    os.writeObject(table);
		    return out.toByteArray();
			 
		 }catch (Exception e){
			 return null;
		 }
	}
	@SuppressWarnings("unchecked")
	public static Hashtable<String, ArrayList<String>> readByteArray(byte[] data){
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		try{
			ObjectInputStream is = new ObjectInputStream(in);
			return (Hashtable<String, ArrayList<String>>) is.readObject();
		}catch(Exception e){
			return null;
		}
	    
	}
	
	
	
}
