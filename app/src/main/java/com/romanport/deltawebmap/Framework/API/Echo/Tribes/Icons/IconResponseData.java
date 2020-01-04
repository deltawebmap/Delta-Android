package com.romanport.deltawebmap.Framework.API.Echo.Tribes.Icons;

import java.io.Serializable;
import java.util.LinkedList;

public class IconResponseData implements Serializable {

    public Float gameTime;
    public LinkedList<EchoIconData> icons;
    public Integer tribeId;

}
