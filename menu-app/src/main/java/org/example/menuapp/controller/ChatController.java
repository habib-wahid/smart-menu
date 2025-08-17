package org.example.menuapp.controller;

import dev.langchain4j.service.TokenStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.menuapp.interfaces.Assistant;
import org.example.menuapp.llm.PersonalDocumentService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

  private final Assistant assistant;
  private final PersonalDocumentService personalDocumentService;
//    public ChatController(OllamaChatModel ollamaChatModel) {
//        this.ollamaChatModel = ollamaChatModel;
//    }

  @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public ResponseBodyEmitter model(
      @RequestParam(value = "userId") String userId,
      @RequestParam(value = "message") String message) {
    log.info("Request came here");
    log.info("Streaming request for userId: {}", userId);

    ResponseBodyEmitter emitter = new ResponseBodyEmitter();

    TokenStream tokenStream = assistant.chat(userId, message);
    tokenStream
            .onPartialResponse(response -> {
              try {
                emitter.send("data: " + response + "\n\n");
              } catch (Exception e) {
                emitter.completeWithError(e);
              }
            })
            .onCompleteResponse(response -> {
              try {
                emitter.send("data: " + response + "\n\n");
              } catch (Exception e) {
                emitter.completeWithError(e);
              }
            })
            .onError((Throwable error) -> error.printStackTrace())
            .start();

    return emitter;

  }

  @GetMapping(value = "/flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<String> chat(
          @RequestParam(value = "userId") String userId,
          @RequestParam(value = "message") String message
  ) {
    log.info("Request came here");
    log.info("Streaming request for userId: {}", userId);

    return assistant.chatStream(userId, message);
  }

  @GetMapping("/embed")
  public void embed() {
    personalDocumentService.embedPersonalDocuments();
  }
}
