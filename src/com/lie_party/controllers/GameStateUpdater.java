package com.lie_party.controllers;

import java.io.Serializable;
import java.util.Iterator;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Session;
import org.hibernate.type.Type;
import com.lie_party.*;
 
public class GameStateUpdater extends EmptyInterceptor{
	
	private static final long serialVersionUID = 1L;
 
      @Override
      public void onDelete(Object entity, Serializable id, Object[] state,
             String[] propertyNames, Type[] types) {
             System.out.println("onDelete Method is getting called");
             System.out.println("==== DETAILS OF ENTITY ARE ====");
             if(entity instanceof Player)
             {
                System.out.println("Id of an Entity is :" + id);
                System.out.println("Property Names ");
             
                for(int i=0;i<propertyNames.length;i++)
                {
                System.out.println(propertyNames[i] );
                }
 
                Player book = (Player) entity;            
                System.out.println("BOOK STATE is ");
                System.out.println(book);
        }
      }
 
     @Override
     public boolean onLoad(Object entity, Serializable id, Object[] state,
                String[] propertyNames, Type[] types) {
            System.out.println("onLoad Method is getting called");
 
            System.out.println("==== DETAILS OF ENTITY ARE ====");
            if(entity instanceof Player)
            {
               System.out.println("Id of an Entity is :" + id);
               System.out.println("Property Names ");
 
               for(int i=0;i<propertyNames.length;i++)
               {
                  System.out.println(propertyNames[i] );
               }
  
               Player book = (Player) entity;
 
              System.out.println("BOOK STATE is ");
              System.out.println(book);
        }
        return true;
     }
 
     @Override
     public boolean onSave(Object entity, Serializable id, Object[] state,
            String[] propertyNames, Type[] types) {
        System.out.println("onsave Method is getting called");
        System.out.println("==== DETAILS OF ENTITY ARE ====");
        if(entity instanceof Player)
        {
            System.out.println("Id of an Entity is :" + id);
            System.out.println("Property Names ");
            
            for(int i=0;i<propertyNames.length;i++)
            {
                //System.out.println(propertyNames[i] );
 
//                if("name".equals(propertyNames[i]))
//                {
//                    state[i]= "Hibernate Tutorial Updated";
//                }
            }
 
            Player book = (Player)entity;
            System.out.println("BOOK STATE is ");
            System.out.println(book);
        }
        return true;
    }    
}