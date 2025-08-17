package org.example.menuapp.llm;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingStore;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PersonalDocumentService {

  private final EmbeddingStore<TextSegment> embeddingStore;
  private final EmbeddingModel embeddingModel;
  private final DocumentSplitter documentSplitter;
  //private final Assistant assistant;

  public PersonalDocumentService(
      EmbeddingStore<TextSegment> embeddingStore,
      EmbeddingModel embeddingModel) {
    this.embeddingStore = embeddingStore;
    this.embeddingModel = embeddingModel;
   // this.assistant = assistant;
    // Split documents into chunks of 500 characters with 100 character overlap
    this.documentSplitter = DocumentSplitters.recursive(500, 100);
  }

  public void embedPersonalDocuments() {
    List<Document> personalDocs = createPersonalDocuments();

    for (Document doc : personalDocs) {
      // Split document into smaller segments
      List<TextSegment> segments = documentSplitter.split(doc);

      // Generate embeddings and store them
      Response<List<Embedding>> embeddings = embeddingModel.embedAll(segments);
      embeddingStore.addAll(embeddings.content(), segments);
    }

    System.out.println("Embedded " + personalDocs.size() + " personal documents");
  }


  private List<Document> createPersonalDocuments() {
    return List.of(
        Document.from(
            "My name is John Doe. I am a software engineer with 8 years of experience. " +
                "I specialize in Java, Spring Boot, and microservices architecture. " +
                "I have worked at tech companies like TechCorp and InnovateLabs.",
            Metadata.from("type", "professional_info")
        ),

        Document.from(
            "I graduated from Stanford University with a Computer Science degree in 2016. " +
                "During college, I was part of the robotics club and led several AI projects. " +
                "My thesis was on machine learning applications in natural language processing.",
            Metadata.from("type", "education")
        ),

        Document.from(
            "My technical skills include: Java, Python, JavaScript, Spring Framework, " +
                "React, Docker, Kubernetes, AWS, PostgreSQL, MongoDB, Redis. " +
                "I'm also experienced with machine learning libraries like TensorFlow and PyTorch.",
            Metadata.from("type", "skills")
        ),

        Document.from(
            "I enjoy hiking, photography, and reading science fiction novels. " +
                "I volunteer at local coding bootcamps teaching programming fundamentals. " +
                "I'm passionate about sustainable technology and green computing initiatives.",
            Metadata.from("type", "personal_interests")
        ),

        Document.from(
            "My recent projects include: Building a microservices platform that reduced " +
                "deployment time by 60%, implementing a real-time chat system using WebSockets, " +
                "and developing a machine learning model for fraud detection with 95% accuracy.",
            Metadata.from("type", "recent_projects")
        )
    );
  }
}
