package org.myy.medicalchat.chat.vo;

import lombok.Getter;

@Getter
public enum ChatModel {
    DEEPSEEK("deepseek_"),
    QWEN("qwen_");

    private String modelName;

    ChatModel(String modelName) {
        this.modelName = modelName;
    }

    public String getModelName(){return this.modelName;}

    public static ChatModel fromString(String modelName){
        for (ChatModel model : ChatModel.values()){
            if (model.getModelName().equalsIgnoreCase(modelName)){
                return model;
            }
        }
        return DEEPSEEK;
    }
}
