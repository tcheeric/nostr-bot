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
 * Security annotation, used to define who can run the command
 * @author eric
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Whitelist {

    /**
     * Select users nip-05 domains
     * @return 
     */
    String[] domains() default {};

    /**
     * Select users by npub
     * @return 
     */
    String[] npubs() default {};
    
    /**
     * Select users by groups. The group is defined in a configuration file.
     * @return 
     */
    String[] groups() default {};
}
