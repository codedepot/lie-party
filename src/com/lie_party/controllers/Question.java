package com.lie_party.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.json.JsonArray;
import javax.json.JsonObject;

import com.lie_party.Player;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

public class Question implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String category;
	private String question;
	private String answer;
	private String[] altSpelling;
	private String[] suggestions;
	
	public Question(JsonObject jb){
		this.category = jb.getString("category");
		this.question = jb.getString("question");
		this.answer = jb.getString("answer");
		//JsonArray altSpell = jb.getJsonArray("alternateSpellings");
		this.altSpelling =  Question.parseJsonArray(jb.getJsonArray("alternateSpellings"));
		//this.suggestions = Question.parseJsonArray(jb.getJsonArray("suggestions"));
		this.suggestions = Question.parseJsonArrayShuffle(jb.getJsonArray("suggestions"));
		
	}
	
	
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String[] getAltSpelling() {
		return altSpelling;
	}
	public void setAltSpelling(String[] altSpelling) {
		this.altSpelling = altSpelling;
	}
	public String[] getSuggestions() {
		return suggestions;
	}
	public void setSuggestions(String[] suggestions) {
		this.suggestions = suggestions;
	}
	
	public static String[] parseJsonArray(JsonArray ja){
		String[] s = new String[ja.size()];
		for(int i = 0; i <ja.size(); i++){
			s[i] = ja.getString(i);
		}
		return s;
	}
	
	public static String[] parseJsonArrayShuffle(JsonArray ja){
		ArrayList<String> al = new ArrayList<String>();
		//System.out.println("size of jarray " + ja.size() + "m and size of al " + al.size());
		for(int i = 0; i <ja.size(); i++){
			//s[i] = ja.getString(i);
			al.add(ja.getString(i));
		}
		
		java.util.Collections.shuffle(al);
		return al.toArray(new String[0]);
	}
	
	public String toString(){
		
		return "Question: " + this.question + " Answer: " + this.answer;
	}
	
	//two methods for serializing and saving the object into database
	public byte[] toByteArray(){
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 try{
		    ObjectOutputStream os = new ObjectOutputStream(out);
		    os.writeObject(this);
		    return out.toByteArray();
			 
		 }catch (Exception e){
			 return null;
		 }
	}
	public static Question readByteArray(byte[] data){
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		try{
			ObjectInputStream is = new ObjectInputStream(in);
			return (Question) is.readObject();
		}catch(Exception e){
			return null;
		}
	    
	}
	
	public static String[] insertRandomly(String[] sug, String[] userSug){
		List<String> sugAl = Arrays.asList(sug);
		Random rand = new Random();
		int index =0;
		for(int i = 0; i<userSug.length;i++){
			index = rand.nextInt(sugAl.size());
			String temp = sugAl.get(index);
			sugAl.set(index, userSug[i]);
			sugAl.add(temp);
		}
		return sugAl.toArray(new String[0]);
	}
	
	public static List<String> insertRandomlyList(String[] sug, String[] userSug){
		List<String> sugAl = Arrays.asList(sug);
		Random rand = new Random();
		int index =0;
		for(int i = 0; i<userSug.length;i++){
			index = rand.nextInt(sugAl.size());
			String temp = sugAl.get(index);
			sugAl.set(index, userSug[i]);
			sugAl.add(temp);
		}
		return sugAl;
	}
	
	public static List<String> insertRandomlyList(String[] sug, List<String> userSug){
		List<String> sugAl = Arrays.asList(sug);
		Random rand = new Random();
		int index =0;
		for(int i = 0; i<userSug.size();i++){
			index = rand.nextInt(sugAl.size());
			String temp = sugAl.get(index);
			sugAl.set(index, userSug.get(i));
			sugAl.add(temp);
		}
		return sugAl;
	}
	
	public static List<String> insertRandomlyListP(String[] sug, List<Player> userSug){
		List<String> sugAl = new ArrayList<String>(Arrays.asList(sug));
		Random rand = new Random();
		int index =0;
		for(int i = 0; i<userSug.size();i++){
			index = rand.nextInt(sugAl.size());
			String temp = sugAl.get(index);
			sugAl.set(index, userSug.get(i).getAnswer());
			sugAl.add(temp);
		}
		return sugAl;
	}
	
	public boolean checkAnswer(String answer){
		if(this.getAnswer().toLowerCase().equals(answer.toLowerCase())){
			return true;
		}else{
			for(int i=0; i< this.getAltSpelling().length; i++){
				if(this.getAltSpelling()[i].toLowerCase().equals(answer.toLowerCase())){
					return true;
				}
			}
			return false;
		}
	}
	
	
	
//	public String[] getRandomSuggestions(int length){
//		String[] sug = new String[length];
//		Random rand = new Random();
//		int start = rand.nextInt(length);
//		for(int i = 0; i<length; i++){
//			//sug[i] = this.getSuggestions()[]
//		}
//		return sug;
//	}
	
}

