package com.goormy.hackathon.handler;

import org.springframework.cloud.function.adapter.aws.FunctionInvoker;

public class LikeHandler extends FunctionInvoker{
    private static String LikeHandler = "likeFunction";

    public LikeHandler() {
        super(LikeHandler);
    }
}
