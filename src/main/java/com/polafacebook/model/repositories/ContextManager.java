package com.polafacebook.model.repositories;

import com.polafacebook.model.Context;

public interface ContextManager {
    public boolean saveContext(Context context);
    public Context getContext(String id);
    public Context getOrCreateContext(String id);
    public boolean deleteContext(String currentId);
}
