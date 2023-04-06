/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/AnnotationType.java to edit this template
 */
package nostr.bot.core.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author eric
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {
    /**
     * Command id, must be unique.
     * @return 
     */
    String id();
    
    /**
     * Command name.
     * @return 
     */
    String name();
    
    /**
     * Use this to define the sequence of execution. The sources-array contains the list of commands that can be executed prior to the current one.
     * @return 
     */
    String[] sources();
}
