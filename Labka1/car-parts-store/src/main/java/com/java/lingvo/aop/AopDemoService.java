package com.java.lingvo.aop;

import org.springframework.stereotype.Service;

@Service
public class AopDemoService {

    @SanitizeText
    public String getDirtyString() {
        return "   Hello    World   ";
    }

    //  SELF-INVOCATION
    public void selfInvocationDemo() {
        System.out.println("Внутрішній виклик (self-invocation): " + this.getDirtyString());
    }
}