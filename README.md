MedSearchEngine
===============

A search engine for medical documents, created using Apache Lucene.

This project was developed for the purposes of the course 'Information Retrieval' of cs department, Athens University of Economics and Business, during the Winter semester 2013 - 2014.

## Concept

Given the medlars collection, create a search engine by using Apache Lucene that will give us the most relevant documents for every query.
The index and serch process must be implemented in two ways:

* A simple text index/search.

* An index/search using bigrams. The bigrams used were found in wikipedia's medical collection and matching bigrams found in our medlars collection.

The results of our retrieval process are evaluated using the trec_eval program.

## Implementation

The implementation consists of two parts :

* Part1 is used to retrieve all the bigrams found in a subset of wikipedia's medica articles. Note: some articles are not related to medical science at all, so we will get a lot of irrelevant bigrams.

* Part2 uses Apache Lucene in order to create two application for indexing and searching. The first does simple search, while the second uses the bigrams found earlier that also match bigrams found in the collection(so that irrelevant bigrams do not affect our results).

## Contents of the project

* src : The source code folder.
* bindex : A folder that contains the bigrams found in part1.
* docs : A folder that an xml file with wikipedia's medical articles.
* index : A folder that keeps the index created by Lucene.
* lib : A folder that contains the necessary .jar files for the project.
* medlars : A folder that contains the medlars collection's documents
* stopwords : A folder that contains some stopwords that we want to ignore.
* trec_eval : A folder that contains the results from the evaluation of the documents that we have retrieved, comparing to the correct answers.
