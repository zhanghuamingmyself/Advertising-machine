package com.example.adapter;

import java.io.Serializable;

/**
 * ================================================

 */
public class ServerModelForFile implements Serializable{

    private static final long serialVersionUID = -632922761336257999L;
    public String url;

    @Override
    public String toString() {
        return url;
    }
}
