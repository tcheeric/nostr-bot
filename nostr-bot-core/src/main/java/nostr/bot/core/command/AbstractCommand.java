/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.bot.core.command;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import lombok.Data;
import lombok.extern.java.Log;
import nostr.bot.core.Context;
import nostr.bot.core.command.annotation.Command;
import nostr.bot.core.command.annotation.Param;

/**
 *
 * @author eric
 * @param <T>
 */
@Log
@Data
public abstract class AbstractCommand<T> implements ICommand<T> {

    public AbstractCommand() {
        this.init();
    }

    protected final void init() {
    }

    @Override
    public void setParameters(Object[] params, Context context) {
        if (params[0].toString().substring(1).equals(this.getId())) {
            List<Field> fields = getParamFields();
            int i = 1;
            for (Field f : fields) {
                
                // Missing parameter - break to prevent an OoBE
                if (i == params.length) {
                    break;
                }
                
                try {
                    new PropertyDescriptor(f.getName(), this.getClass()).getWriteMethod().invoke(this, getParameterValue(params[i].toString(), f));

                    // Add parameters to context
                    Object attr = new PropertyDescriptor(f.getName(), this.getClass()).getReadMethod().invoke(this);
                    context.addParamValue(this.getId() + "#" + f.getName(), attr);

                    // Increment
                    i++;
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | IntrospectionException ex) {
                    log.log(Level.SEVERE, null, ex);
                }
            }

            return;
        }

        throw new RuntimeException("Invalid command");
    }

    @Override
    public String getId() {
        final Command command = this.getClass().getDeclaredAnnotation(Command.class);
        return command != null ? command.id() : null;
    }

    @Override
    public String[] getSources() {
        final Command command = this.getClass().getDeclaredAnnotation(Command.class);
        return command != null ? command.sources() : new String[]{};
    }

    @Override
    public String toString() {
        return "Command [" + getId() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof AbstractCommand ac) {
            return ac.getId().equalsIgnoreCase(getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 17 * getId().hashCode();
    }

    private List<Field> getParamFields() {
        Field[] fields = this.getClass().getDeclaredFields();
        List<Field> result = new ArrayList<>();
        Arrays.asList(fields).stream().filter(f -> f.getDeclaredAnnotation(Param.class) != null).forEach(f -> result.add(f));
        Collections.sort(result, (Field t, Field t1) -> t.getDeclaredAnnotation(Param.class).index() - t1.getDeclaredAnnotation(Param.class).index());
        return result;
    }

    private Object getParameterValue(String value, Field f) {
        if (String.class.isAssignableFrom(f.getType())) {
            return value;
        } else if (Number.class.isAssignableFrom(f.getType())) {
            if (Integer.class.equals(f.getType())) {
                return Integer.valueOf(value);
            } else if (Long.class.equals(f.getType())) {
                return Long.valueOf(value);
            } else if (Double.class.equals(f.getType())) {
                return Double.valueOf(value);
            } else if (Float.class.equals(f.getType())) {
                return Float.valueOf(value);
            } else if (Byte.class.equals(f.getType())) {
                return Byte.valueOf(value);
            } else if (Short.class.equals(f.getType())) {
                return Short.valueOf(value);
            }
        }

        throw new RuntimeException("Unsupported attribute type");
    }
}
