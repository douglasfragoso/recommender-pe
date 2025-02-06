package com.recommendersystempe.enums;

public enum Roles {
    
    ADMIN(1),
    USER(2), 
    MASTER(3);
    
    private int code; 

    private Roles(int code) { 
        this.code = code;
    }	

    public int getCode() {
        return code;
    }

    //static cause it's not related to a specific instance
    public static Roles valueOf(int code) { 
        for (Roles value : Roles.values()) { 
            if (value.getCode() == code) { 
                return value; 
            }
        }
        throw new IllegalArgumentException("Invalid OrderStatus code"); 
    }
}

