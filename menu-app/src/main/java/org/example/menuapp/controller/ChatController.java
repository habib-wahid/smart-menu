package org.example.menuapp.controller;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.ollama.OllamaChatModel;
import lombok.RequiredArgsConstructor;
import org.example.menuapp.interfaces.Assistant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final Assistant assistant;

//    public ChatController(OllamaChatModel ollamaChatModel) {
//        this.ollamaChatModel = ollamaChatModel;
//    }

    @GetMapping
    public String model(@RequestParam(value = "message", defaultValue = "Hello") String message) {
        return assistant.chat(1, message);
    }
}
