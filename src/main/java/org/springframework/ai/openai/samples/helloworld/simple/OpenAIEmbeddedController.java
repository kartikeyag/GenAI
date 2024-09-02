package org.springframework.ai.openai.samples.helloworld.simple;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class OpenAIEmbeddedController {

	@Autowired
	private final EmbeddingModel embeddingClient;

	@Autowired
	VectorStore vectorStoreDoc;

	public OpenAIEmbeddedController(EmbeddingModel embeddingClient) {
		this.embeddingClient = embeddingClient;
	}

	@GetMapping("/ai/embedding")
	public Map embed(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
		EmbeddingResponse embeddingResponse = this.embeddingClient.embedForResponse(List.of(message));
		return Map.of("embedding", embeddingResponse);
	}

	@GetMapping("test")
	public String test () {
		return "test5 ";
	}

	@GetMapping("/ai/similarDocs")
	public List<SimilarDocument> getSimilarDocument(){
		List <Document> documents = List.of(
				new Document("Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!", Map.of("meta1", "meta1")),
				new Document("The World is Big and Salvation Lurks Around the Corner"),
				new Document("You walk forward facing the past and you turn back toward the future.", Map.of("meta2", "meta2")));

// Add the documents to PGVector
		vectorStoreDoc.add(documents);

// Retrieve documents similar to a query
		List<Document> results = vectorStoreDoc.similaritySearch(SearchRequest.query("Spring").withTopK(5));
		//return results;

		List<SimilarDocument> similarDocumentList = new ArrayList<SimilarDocument>();

		for(Document document:results)
		{
			SimilarDocument similarDocument = new SimilarDocument();
			similarDocument.setContent(document.getContent());
			similarDocument.setScore((Float) document.getMetadata().get("distance"));
			similarDocumentList.add(similarDocument);
		}
		return similarDocumentList;
	}


}
