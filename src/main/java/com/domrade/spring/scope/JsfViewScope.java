package com.domrade.spring.scope;

import com.domrade.spring.confg.Application;
import java.util.List;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import javax.faces.context.FacesContext;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

// copied from https://cagataycivici.wordpress.com/2010/02/17/port-jsf-2-0s-viewscope-to-spring-3-0/
public class JsfViewScope implements Scope {
    
    static final Logger LOGGER = Logger.getLogger(Application.class.getName());
    
    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getViewRoot() != null) {
            Map<String, Object> viewMap = context.getViewRoot().getViewMap();
            Set<String> myList = viewMap.keySet();
            for(String s: myList)
                LOGGER.log(Level.INFO, "#### viewMap " + s);

            if (viewMap.containsKey(name)) {
                return viewMap.get(name);
            } else {
                Object object = objectFactory.getObject();
                viewMap.put(name, object);

                return object;
            }
        }
        return null;
    }

    @Override
    public Object remove(String name) {
        return FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove(name);
    }

    @Override
    public void registerDestructionCallback(String s, Runnable runnable) {

    }

    @Override
    public Object resolveContextualObject(String s) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }
}
