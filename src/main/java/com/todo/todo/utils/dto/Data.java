package com.todo.todo.utils.dto;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Value;

@Value
@Component
@Scope(value = "prototype")
public class Data<K, V> {

    Map<K, V> data;

    public Data(){
        data = new HashMap<>();
    }

    public void fill(Data<K, V> dataToFill){
        data.putAll(dataToFill.getData());
    }

    public Boolean has(K key){
        return data.containsKey(key);
    }

    public V get(K key){
        return data.get(key);
    }

    public void put(K key, V value){
        data.put(key, value);
    }

    public void remove(K key){
        data.remove(key);
    }

    public Boolean isEmpty(){
        return data.isEmpty();
    }
    
}
