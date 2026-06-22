package com.rabbiter.music.vo;

import com.rabbiter.music.utils.Consts;
import java.util.HashMap;

public class R extends HashMap<String, Object> {

    public static R success(String msg, int code) {
        R r = new R();
        r.put(Consts.CODE, code);
        r.put(Consts.MSG, msg);
        return r;
    }

    public static R success(String msg, int code, Object data) {
        R r = new R();
        r.put(Consts.CODE, code);
        r.put(Consts.MSG, msg);
        r.put(Consts.DATA, data);
        return r;
    }

    public static R error(String msg) {
        R r = new R();
        r.put(Consts.CODE, 0);
        r.put(Consts.MSG, msg);
        return r;
    }
}


