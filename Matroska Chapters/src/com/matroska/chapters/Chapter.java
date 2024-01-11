package com.matroska.chapters;

import org.joda.time.LocalTime;

/** Stores general information about a single chapter.
 *  @author Felipe André - felipeandre.eng@gmail.com
 *  @version 1.0 - 10/JAN/2024 */
public class Chapter {
	
	private final LocalTime startTime;
	private final String chapterTitle; 
	
	/** Extracts data from a <code>row</code> containing the chapter start time and title.
	 *  @param row - String containing the chapter start time and title */
	public Chapter(final String row) {
		
		int index = row.indexOf('\t');
		
		this.startTime = LocalTime.parse(row.substring(0, index).replace(';','.'));
    	this.chapterTitle = row.substring(index).trim();
		
	}

	/** Generates the chapter information according to the YouTube format.
	 *  @see <a href="https://www.youtube.com/watch?v=b1Fo_M_tj6w">How to Add Chapters to Your YouTube Videos Using Timestamps</a>
	 *  @return A formatted String containing chapter information for YouTube. */
	public String toYoutube() {
		
		StringBuilder builder = new StringBuilder();
		
		if (startTime.getHourOfDay() > 0)
			builder.append(startTime.toString("HH:mm:ss "));
		else
			builder.append(startTime.toString("m:ss "));
		
		builder.append(chapterTitle);
		
		return builder.toString();
	}

	/** @return A formatted String containing a timestamp according to the Matroska format 'HH:mm:ss.SSSSSSSSS'. */
	public String getMatroskaTime() {
		return startTime.toString("HH:mm:ss.SSSSSSSSS");
	}

	/** @return The chapter title. */
	public String getChapterTitle() {
		return this.chapterTitle;
	}
	
}