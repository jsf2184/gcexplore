package com.jeff.fischman.gc;

public class Buffer {
    int _value;
    char _buf[];

    public Buffer(int value) {
        _value = value;
        _buf = new char[100000];
    }

    public int getValue() {
        return _value;
    }
}
