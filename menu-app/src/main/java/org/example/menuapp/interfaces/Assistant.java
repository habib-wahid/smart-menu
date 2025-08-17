package org.example.menuapp.interfaces;

import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.memory.ChatMemoryAccess;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import reactor.core.publisher.Flux;

@AiService(wiringMode = AiServiceWiringMode.EXPLICIT,
    streamingChatModel = "chatModel",
    chatMemoryProvider = "chatMemoryProvider",
    contentRetriever = "contentRetriever")
public interface Assistant extends ChatMemoryAccess {


    @SystemMessage("You are a polite assistant")
    TokenStream chat(@MemoryId String  memoryId, @UserMessage String message);

    @SystemMessage("You are a polite assistant")
    Flux<String> chatStream(@MemoryId String  memoryId, @UserMessage String message);
}
