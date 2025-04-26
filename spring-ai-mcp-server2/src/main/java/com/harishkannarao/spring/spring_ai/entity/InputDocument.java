package com.harishkannarao.spring.spring_ai.entity;

import com.harishkannarao.spring.spring_ai.entity.InputMetaData;

import java.util.List;

public record InputDocument(
	String content,
	List<InputMetaData> metaData
) {
}
