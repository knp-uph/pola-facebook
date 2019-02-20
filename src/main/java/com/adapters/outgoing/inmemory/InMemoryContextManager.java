package com.adapters.outgoing.inmemory;

import com.domain.ports.outgoing.context.Context;
import com.domain.ports.outgoing.context.ContextManager;

import java.util.ArrayList;
import java.util.List;

public class InMemoryContextManager implements ContextManager {
    private List<Context> contextList = new ArrayList<>();

    @Override
    public void saveContext(Context context) {
        for (Context c : contextList) {
            if (c.getUserId().equals(context.getUserId())) {
                c = context;
                return;
            }
        }
        contextList.add(context);
    }

    @Override
    public Context getContext(String id) {
        for (Context c : contextList) {
            if (c.getUserId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public Context getOrCreateContext(String id) {
        Context c = getContext(id);
        if(c != null){
            return c;
        }
        c = new Context(id);
        contextList.add(c);
        return c;
    }

    @Override
    public void deleteContext(String currentId) {
        contextList.remove(getContext(currentId));
    }
}
