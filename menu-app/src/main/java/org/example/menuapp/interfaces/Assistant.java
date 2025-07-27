package org.example.menuapp.interfaces;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.memory.ChatMemoryAccess;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface Assistant extends ChatMemoryAccess {

    @SystemMessage("You are a polite assistant")
    String chat(@MemoryId int memoryId, @UserMessage String message);
}
