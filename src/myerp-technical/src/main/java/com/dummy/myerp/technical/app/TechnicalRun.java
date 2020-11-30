/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dummy.myerp.technical.app;

import com.dummy.myerp.technical.exception.FunctionalException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author pi
 */
public class TechnicalRun {
    
    public static void main(String[] args) {
        
        System.out.println("com.dummy.myerp.technical.app.technicalRun.main()");
        
        try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("com/dummy/myerp/technical/applicationContext-technical.xml")) {
            
            FunctionalException feComponant = context.getBean(FunctionalException.class);
            
            System.out.println(feComponant);
            
        } catch (Exception e) {
            
            
            System.out.println(e.getMessage());
            
           
        }
        
    }
    
}
