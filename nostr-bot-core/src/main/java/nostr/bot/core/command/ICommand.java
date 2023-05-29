/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package nostr.bot.core.command;

import nostr.bot.core.Context;

/**
 *
 * @author eric
 * @param <T>
 */
public interface ICommand<T> {

    public T execute(Context context);

    public String getHelp();

    public void setParameterValues(Object[] params, Context context);

    public String getId();
    
    public String[] getSources();
}
