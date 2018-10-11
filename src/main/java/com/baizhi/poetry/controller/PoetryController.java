package com.baizhi.poetry.controller;

import com.baizhi.poetry.entity.Result;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PoetryController {

    @RequestMapping("/search")
    public ModelAndView search(ModelAndView modelAndView, String keyword) {
        try {
            FSDirectory fsDirectory = FSDirectory.open(Paths.get("D:\\lucene\\index6"));
            IndexReader indexReader = DirectoryReader.open(fsDirectory);
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);

//        Query query = new TermQuery(new Term("content",keyword));
           Query query = new QueryParser("content", new StandardAnalyzer()).parse(keyword);

            Highlighter highlighter = new Highlighter(new SimpleHTMLFormatter("<span style='color:red'>", "</span>"), new QueryScorer(query));

            TopDocs topDocs = indexSearcher.search(query, 100);
            System.out.println("命中的结果数量：" + topDocs.totalHits);

            ScoreDoc[] scoreDocs = topDocs.scoreDocs;

            List<Result> resultList = new ArrayList<Result>();

            Result result = null;

            for (ScoreDoc scoreDoc : scoreDocs) {

                result = new Result();

                int docID = scoreDoc.doc;
                Document document = indexReader.document(docID);

                String bestFragment = highlighter.getBestFragment(new StandardAnalyzer(), "content", document.get("content"));

                result.setId(Integer.parseInt(document.get("id")));
                result.setTilte(document.get("title"));
                result.setContent(bestFragment);
                result.setName(document.get("name"));

                resultList.add(result);
            }

            modelAndView.addObject("resultList",resultList);
            modelAndView.setViewName("result");
           indexReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (InvalidTokenOffsetsException e) {
            e.printStackTrace();
        }

        return modelAndView;
    }

    /**
     * 分页查询
     * @param modelAndView
     * @param keyword
     * @return
     */
    @RequestMapping("/searchByPage")
    public ModelAndView searchByPage(ModelAndView modelAndView, String keyword,Integer nowPage,Integer pageSize) {
        try {
            FSDirectory fsDirectory = FSDirectory.open(Paths.get("D:\\lucene\\index6"));
            IndexReader indexReader = DirectoryReader.open(fsDirectory);
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);

//        Query query = new TermQuery(new Term("content",keyword));
            Query query = new QueryParser("content", new StandardAnalyzer()).parse(keyword);

            Highlighter highlighter = new Highlighter(new SimpleHTMLFormatter("<span style='color:red'>", "</span>"), new QueryScorer(query));

            //=============================== 分页查询 =====================================
            TopDocs topDocs = null;

            if(nowPage == 1){
                topDocs = indexSearcher.search(query,pageSize);
            } else{

                topDocs = indexSearcher.search(query,(nowPage-1)*pageSize);

                ScoreDoc lastScoreDoc = topDocs.scoreDocs[topDocs.scoreDocs.length-1];

                topDocs = indexSearcher.searchAfter(lastScoreDoc,query,pageSize);
            }
            //====================================================================


            System.out.println("命中的结果数量：" + topDocs.totalHits);

            modelAndView.addObject("count",topDocs.totalHits);

            ScoreDoc[] scoreDocs = topDocs.scoreDocs;

            List<Result> resultList = new ArrayList<Result>();

            Result result = null;

            for (ScoreDoc scoreDoc : scoreDocs) {

                result = new Result();

                int docID = scoreDoc.doc;
                Document document = indexReader.document(docID);

                String bestFragment = highlighter.getBestFragment(new StandardAnalyzer(), "content", document.get("content"));

                result.setId(Integer.parseInt(document.get("id")));
                result.setTilte(document.get("title"));
                result.setContent(bestFragment);
                result.setName(document.get("name"));

                resultList.add(result);
            }

            modelAndView.addObject("resultList",resultList);

            modelAndView.setViewName("result");
            indexReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (InvalidTokenOffsetsException e) {
            e.printStackTrace();
        }

        return modelAndView;
    }
}
