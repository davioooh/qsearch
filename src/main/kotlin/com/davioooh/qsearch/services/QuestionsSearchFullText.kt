package com.davioooh.qsearch.services

import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.StringField
import org.apache.lucene.document.TextField
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.store.ByteBuffersDirectory

class QuestionItem(val id: Int, val title: String, val body: String)

fun Questions.toQuestionItemArray() = this.map { QuestionItem(it.questionId, it.title, it.body ?: "") }.toTypedArray()

interface FullTextSearchIndex<in IN, out OUT> {
    fun addToIndex(vararg items: IN)
    fun search(searchKey: String): List<OUT>
}

object QuestionsSearchIndex : FullTextSearchIndex<QuestionItem, Int> {
    private val analyzer = StandardAnalyzer()
    private val index = ByteBuffersDirectory()

    override fun addToIndex(vararg items: QuestionItem) {
        IndexWriter(index, IndexWriterConfig(analyzer))
            .use { writer ->
                items.forEach {
                    val document = Document()
                    document.add(StringField("id", it.id.toString(), Field.Store.YES))
                    document.add(TextField("title", it.title, Field.Store.NO))
                    document.add(TextField("body", it.body, Field.Store.NO))
                    writer.addDocument(document)
                }
            }
    }

    override fun search(searchKey: String): List<Int> {
        val query = MultiFieldQueryParser(
            arrayOf("title", "body"),
            analyzer
        ).parse(searchKey) // TODO refine search results (fuzzy? wildcards?)

        val docs = DirectoryReader.open(index).use { indexReader ->
            val searcher = IndexSearcher(indexReader)
            val topDocs = searcher.search(query, 1_000_000)
            topDocs.scoreDocs.map { searcher.doc(it.doc) }
        }
        return docs.map { it.get("id").toInt() }
    }
}
