package com.baizhi.poetry;

import com.baizhi.poetry.dao.PoetryDAO;
import com.baizhi.poetry.entity.Poetry;
import com.baizhi.poetry.service.PoetryService;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PoetryApplicationTests {
	@Autowired
	private PoetryService poetryService;

	@Test
	public void testFindAll() {
		List<Poetry> poetries = poetryService.queryAll();
		System.out.println(poetries.size());
	}

	@Test
	public void createIndex() throws IOException {
		FSDirectory fsDirectory = FSDirectory.open(Paths.get("D:\\lucene\\index6"));

		IndexWriter indexWriter = new IndexWriter(fsDirectory, new IndexWriterConfig(new StandardAnalyzer()));

		List<Poetry> poetries = poetryService.queryAll();

		Document document = null;

		for (Poetry poetry : poetries) {
			document = new Document();
			document.add(new IntField("id",poetry.getId(), Field.Store.YES));
			document.add(new StringField("title",poetry.getTitle(), Field.Store.YES));
			document.add(new TextField("content",poetry.getContent(), Field.Store.YES));
			document.add(new StringField("name",poetry.getPoet().getName(), Field.Store.YES));

			indexWriter.addDocument(document);
		}
		indexWriter.commit();
		indexWriter.close();
	}

}
