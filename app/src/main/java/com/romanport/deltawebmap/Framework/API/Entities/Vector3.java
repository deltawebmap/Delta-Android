package com.romanport.deltawebmap.Framework.API.Entities;

import java.io.Serializable;

public class Vector3 implements Serializable {

    public Float x;
    public Float y;
    public Float z;

    public Vector3() {

    }

    public Vector3(Float x, Float y, Float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
