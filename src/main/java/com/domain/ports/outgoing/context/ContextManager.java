package com.domain.ports.outgoing.context;

public interface ContextManager {
    public void saveContext(Context context);
    public Context getContext(String id);
    public Context getOrCreateContext(String id);

    public void deleteContext(String id);
}
