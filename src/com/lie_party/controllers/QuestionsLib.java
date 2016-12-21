package com.lie_party.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;

public class QuestionsLib {
	
    
    private static final JsonArray normalQuestionArray = loadNormalQuestionArray();
    
    public static JsonArray loadNormalQuestionArray(){
    	try{
    		JsonReader reader = Json.createReader(new FileReader("C:/WebApps/Lie_Party/src/questions.json"));
    		JsonStructure jsonst = reader.read();
    		return ((JsonObject) jsonst).getJsonArray("normal");
    	}catch(Exception e){
    		
    	}
    	return null;
    }
    
    private static Random rand = new Random();
    

    
    public static Question getRandomQuestion(){
    	return new Question((JsonObject) normalQuestionArray.get(rand.nextInt(normalQuestionArray.size())));
    }
    
    public static Question getTestQuestion(){
    	return new Question((JsonObject) normalQuestionArray.get(0));
    }
    
    
    
    public static void main(String[] args){
    	QuestionsLib ql = new QuestionsLib();
    	Question q = QuestionsLib.getRandomQuestion();
    	//byte[] temp = q.toByteArray();
    	//System.out.println(q);
    	//System.out.println(Question.readByteArray(temp).toString());
    	//System.out.println(QuestionsLib.getRandomQuestion().getQuestion());
    }
    


}

