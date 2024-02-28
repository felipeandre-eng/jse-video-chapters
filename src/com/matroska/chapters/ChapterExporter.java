package com.matroska.chapters;

import org.w3c.dom.*;

import org.apache.commons.io.*;

import java.io.*;
import java.util.*;
import java.nio.charset.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

public class ChapterExporter {

	public static void main(String[] args) throws ParserConfigurationException, TransformerException, IOException {
		
		File file = new File("D:\\chapters.txt");
		File youtube = new File("D:\\youtube.txt");
		File matroska = new File("D:\\matroska.xml");
		
		FileUtils.writeStringToFile(youtube , exportYoutubeChapters (parse(file)),                StandardCharsets.UTF_8);
		FileUtils.writeStringToFile(matroska, exportMatroskaChapters(parse(file) , "sgn", "bzs"), StandardCharsets.UTF_8);
		
	}
	
	public static List<Chapter> parse(final File chaptersTextFile) throws ParserConfigurationException, TransformerException, IOException {
		
		String[] chapterRows = FileUtils.readFileToString(chaptersTextFile, StandardCharsets.UTF_16).split("\n");
		List<Chapter> chapterList = new ArrayList<Chapter>(chapterRows.length);
		
		for (String chapterRow: chapterRows)
			chapterList.add(new Chapter(chapterRow));
		
		return chapterList;
	}

	public static String exportMatroskaChapters(final List<Chapter> chapterList, final String language, final String languageIETF) throws ParserConfigurationException, TransformerException {
		
		Random random = new Random(System.nanoTime());
		
		// XML DOM
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		Document document = docBuilder.newDocument();
		
		Comment comment = document.createComment(" <!DOCTYPE Chapters SYSTEM \"matroskachapters.dtd\"> ");
		document.appendChild(comment);
				
		// Root element
		Element rootChapters = document.createElement("Chapters");
		document.appendChild(rootChapters);
		        
		// Edition Entry
		Element editionEntry = document.createElement("EditionEntry");
		rootChapters.appendChild(editionEntry);
		        
		// Edition UID
		Element editionUID = document.createElement("EditionUID");
		editionUID.setTextContent(Long.toString(random.nextLong() & Long.MAX_VALUE));
		editionEntry.appendChild(editionUID);
		
		// Chapters
		for (Chapter chapter: chapterList) {
			
        	Element chapterAtom = document.createElement("ChapterAtom");
        	editionEntry.appendChild(chapterAtom);
        	
        	Element chapterTimeStart = document.createElement("ChapterTimeStart");
            chapterTimeStart.setTextContent(chapter.getMatroskaTime());
            chapterAtom.appendChild(chapterTimeStart);
            
            Element chapterDisplay = document.createElement("ChapterDisplay");
            chapterAtom.appendChild(chapterDisplay);
            
            Element chapterString = document.createElement("ChapterString");
            chapterString.setTextContent(chapter.getChapterTitle());
            chapterDisplay.appendChild(chapterString);
            
            Element chapterLanguage = document.createElement("ChapterLanguage");
            chapterLanguage.setTextContent(language);
            chapterDisplay.appendChild(chapterLanguage);
            
            Element chapterLanguageIETF = document.createElement("ChapLanguageIETF");
            chapterLanguageIETF.setTextContent(languageIETF);
            chapterDisplay.appendChild(chapterLanguageIETF);
            
            Element chapterUID = document.createElement("ChapterUID");
            chapterUID.setTextContent(Long.toString(random.nextLong() & Long.MAX_VALUE));
            chapterAtom.appendChild(chapterUID);
			
		}
		
		// Final XML Generation
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        
        StringWriter sw = new StringWriter();
        trans.transform(new DOMSource(document), new StreamResult(sw));
        
        return sw.toString().trim();
		
	}
	
	public static String exportYoutubeChapters(final List<Chapter> chapterList) {
		
		StringBuilder builder = new StringBuilder();
		
		for (Chapter chapter: chapterList)
			builder.append(chapter.toYoutube() + "\n");
		
		return builder.toString().trim();
	}
	
}