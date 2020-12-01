package com.mentor.club.model.aws;

import com.google.gson.Gson;

public interface ILambdaRequest {
    default String toJson(ILambdaRequest lambdaRequest) {
        return new Gson().toJson(lambdaRequest);
    }
}
