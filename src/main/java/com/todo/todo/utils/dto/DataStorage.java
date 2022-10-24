package com.todo.todo.utils.dto;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.Value;

@Value
public final class DataStorage {

    Data<String, Object> data;
    Data<String, Exception> exceptions;

    @Autowired
    public DataStorage(Data<String, Object> data, Data<String, Exception> exceptions){
        this.data = data;
        this.exceptions = exceptions;
    }

    public void fill(DataStorage storage){
        data.fill(storage.getData());
        exceptions.fill(storage.getExceptions());
    }

    // data

    public Boolean hasData(String key){
        return data.has(key);
    }

    public Object getData(String dataName){
        return data.get(dataName);
    }

    public void putData(String key, Object object){
        data.put(key, object);
    }

    public void removeData(String key){
        data.remove(key);
    }

    // exceptions

    public Boolean hasExceptions(){
        return !exceptions.isEmpty();
    }

    public Boolean hasException(String exceptionName){
        return exceptions.has(exceptionName);
    }

    public Exception getException(String exceptionName){
        return exceptions.get(exceptionName);
    }

    public void putException(String key, Exception exception){
        exceptions.put(key, exception);
    }

}
