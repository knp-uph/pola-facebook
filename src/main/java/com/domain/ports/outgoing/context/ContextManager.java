package com.domain.ports.outgoing.context;

public interface ContextManager {
    public boolean saveContext(Context context);
    public Context getContext(String id);
    public Context getOrCreateContext(String id);
    public boolean deleteContext(String currentId);
}
