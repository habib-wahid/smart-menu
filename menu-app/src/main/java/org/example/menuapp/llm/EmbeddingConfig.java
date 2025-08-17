package org.example.menuapp.llm;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddingConfig {

  @Value("${ollama.base-url:http://localhost:11434}")
  private String ollamaBaseUrl;

  @Value("${langchain4j.ollama.streaming-chat-model.model-name}")
  private String ollamaChatModel;

//  @Value("${langchain4j.ollama.chat-model.temperature}")
//  private Double temperature;

  @Value("${ollama.embedding-model:nomic-embed-text:latest}")
  private String embeddingModelName;


  @Bean
  public OllamaStreamingChatModel chatModel() {
    return OllamaStreamingChatModel.builder()
        .baseUrl(ollamaBaseUrl)
        .modelName("deepseek-r1:1.5b")
        .build();
  }


  @Bean
  public EmbeddingModel embeddingModel() {
    return OllamaEmbeddingModel.builder()
        .baseUrl(ollamaBaseUrl)
        .modelName(embeddingModelName)
        .build();
  }


  @Bean
  public EmbeddingStore<TextSegment> embeddingStore() {
    return new InMemoryEmbeddingStore<>();
  }

  @Bean("contentRetriever")
  public ContentRetriever contentRetriever(
      EmbeddingStore<TextSegment> embeddingStore,
      EmbeddingModel embeddingModel) {

    return EmbeddingStoreContentRetriever.builder()
        .embeddingStore(embeddingStore)
        .embeddingModel(embeddingModel)
        .maxResults(5) // Number of similar documents to retrieve
        .minScore(0.7) // Minimum similarity score threshold
        .build();
  }
}
