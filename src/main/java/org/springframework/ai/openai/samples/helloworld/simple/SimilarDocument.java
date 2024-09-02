package org.springframework.ai.openai.samples.helloworld.simple;

import lombok.Data;

@Data
public class SimilarDocument {
    String content;
    Float score;
}
