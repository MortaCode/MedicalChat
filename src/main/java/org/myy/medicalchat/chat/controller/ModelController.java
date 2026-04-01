package org.myy.medicalchat.chat.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myy.medicalchat.chat.vo.ChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("model")
public class ModelController {

    @GetMapping("list")
    public ResponseEntity<List<String>> listByModel(){
        List<String> list = Arrays.stream(ChatModel.values())
                .map(ChatModel::getModelName)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }
}
