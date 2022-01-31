package main.java.da_utils.algorithmic_models.melody_segmenter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.poi.hssf.record.cf.BorderFormatting;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import acm.program.ConsoleProgram;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;

public class LiveClipExcelMaker extends ConsoleProgram{

	String corpusPath = "C:/Users/Doug/Documents/_MASTER OF UNIBERSE/Repetition text files/CorpusMelodyFiles";
	String fileName = "Anthropology.liveclip";
	String outputPath = "C:/Users/Doug/Documents/_DOCTOR OF UNIBERSE/doctor_repo/experiments/CorpusMelodySegmentationXLFiles/MelodyCorpusForSegmentation.xlsx";
	int extraInfo = 10;
	int dataStartRow = 10;
	String[] headerArr = new String[]{
			"note", "pos", "len", "vel", "mute", "form offset", 
			"actual pos", "inter onset gap to previous", "preceding rest length",
			"previous interval", "abs previous interval", "change in length",
			"inter onset peak boundary GPR2b", "preceding rest boundary GPR2a", "preceding abs interval",
			"LBDM", "LBDM with STDEV peak filter", "LBDM total", "STDEV", "", "LDBM pitch interval", "",
			"LBDM IOI", "", "LBDM rest"};
	double LBDM_STDEV_WEIGHTING = 0.9;
	double LBDM_PITCH_WEIGHTING = 0.25;
	double LDBM_IOI_WEIGHTING = 0.5;
	double LDBM_REST_WEIGHTING = 0.25;
	int LBDM_STDEV_COL = 18;
	int LBDM_PITCH_COL = 20;
	int LBDM_IOI_COL = 22;
	int LBDM_REST_COL = 24;
	
	public void run(){
		setSize(700, 700);
		File dir = new File(corpusPath);
		File[] files = dir.listFiles(new FilenameFilter(){
			public boolean accept(File dir, String name){
				return name.toLowerCase().endsWith(".liveclip");
			}
		});
		
		XSSFWorkbook wb = new XSSFWorkbook();
		
		for (File file: files){
			println(file.getName());
			addSheet(wb, file);
		}
		
		try {
			FileOutputStream out = new FileOutputStream( 
					new File(outputPath));
			wb.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	private void addSheet(XSSFWorkbook wb, File file) {
		try {
			LiveClip lc = new LiveClip(0, 0);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			lc.instantiateClipFromBufferedReader(br);
			//println(lc.toString());
			XSSFSheet sheet = wb.createSheet(lc.name);
			makeHeaders(sheet, lc);
			makeDataHeaders(sheet, 9);
			int nextrow = addPreamble(sheet, lc, dataStartRow, extraInfo);
			nextrow = addMainData(sheet, lc, nextrow);
			addPostamble(sheet, lc, nextrow, extraInfo);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void addPostamble(XSSFSheet sheet, LiveClip lc, int rowIndex, int count) {
		final Font font = sheet.getWorkbook ().createFont ();
//	    font.setFontName ( "Arial" );
	    font.setColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex()); 
		final CellStyle style = sheet.getWorkbook ().createCellStyle ();
		style.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
		style.setFont(font);
		
		final CellStyle style2 = sheet.getWorkbook ().createCellStyle ();
		style2.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
		style2.setFont(font);
		style2.setBorderTop(BorderStyle.THIN);
		CellStyle xxx = style2;
		if (lc.noteList.size() < count) count = lc.noteList.size();
		for (int i = 0; i < count; i++){
			if (i > 0) xxx = style;
			XSSFRow row = sheet.createRow((short)rowIndex);
			LiveMidiNote lmn = lc.noteList.get(i);
			makeCell(row, 0, lmn.note, xxx);
			makeCell(row, 1, lmn.position, xxx);
			makeCell(row, 2, lmn.length, xxx);
			makeCell(row, 3, lmn.velocity, xxx);
			makeCell(row, 4, lmn.mute, xxx);
			makeCell(row, 5, -1, xxx);
			makeFormulaCell(row, 6, "B" + (rowIndex + 1) + "+(F" + (rowIndex + 1) +"*$D$2)", xxx);
			//makeCell(row, 7, "", xxx);		// only main data
			if (i < count - 1){
				makeFormulaCell(row, 7, "G" + (rowIndex + 1) + "-G" + rowIndex, xxx);
				makeFormulaCell(row, 8, "ROUND((G" + (rowIndex + 1) + "-(G" + rowIndex + "+C" + rowIndex + "))*100,1)/100", xxx);
				makeFormulaCell(row, 9, "A" + (rowIndex + 1) + "-A" + rowIndex, xxx);
				makeFormulaCell(row, 10, "ABS(A" + (rowIndex + 1) + "-A" + rowIndex + ")", xxx);			
				makeFormulaCell(row, 17, "U" + (rowIndex + 1) + "*U$9+W" + (rowIndex + 1) + "*W$9+Y" + (rowIndex + 1) + "*Y$9", xxx);
				makeFormulaCell(row, 19, "ABS(J" + (rowIndex + 1) + ")+0.1", xxx);
				makeFormulaCell(row, 20, "(ABS(T" + rowIndex + "-T" + (rowIndex + 1) + "))/(T" + rowIndex + "+T" + (rowIndex + 1) + ")", xxx);
				makeFormulaCell(row, 21, "H" + (rowIndex + 1) + "+0.1", xxx);
				makeFormulaCell(row, 22, "(ABS(V" + rowIndex + "-V" + (rowIndex + 1) + "))/(V" + rowIndex + "+V" + (rowIndex + 1) + ")", xxx);
				makeFormulaCell(row, 23, "I" + (rowIndex + 1) + "+0.1", xxx);
				makeFormulaCell(row, 24, "(ABS(X" + rowIndex + "-X" + (rowIndex + 1) + "))/(X" + rowIndex + "+X" + (rowIndex + 1) + ")", xxx);
				
			}
			//makeCell(row, 9, "", xxx);		// only main data has actual value. this is there to make sure there is a line
			//makeCell(row, 10, "", xxx);		// only main data has actual value. this is there to make sure there is a line
			makeCell(row, 11, "", xxx);		// only main data has actual value. this is there to make sure there is a line
			makeCell(row, 12, "", xxx);		// only main data has actual value. this is there to make sure there is a line
			makeCell(row, 13, "", xxx);		// only main data has actual value. this is there to make sure there is a line
			makeCell(row, 14, "", xxx);		// only main data has actual value. this is there to make sure there is a line
			makeCell(row, 15, "", xxx);		// only main data has actual value. this is there to make sure there is a line
			makeCell(row, 16, "", xxx);		// only main data has actual value. this is there to make sure there is a line
			// 17 in if then above
			makeCell(row, 18, "", xxx);		// only main data has actual value. this is there to make sure there is a line
			// 19 in if then above 	
			// 20 in if then above 
			// 21 in if then above 
			// 22 in if then above 
			// 23 in if then above 
			// 24 in if then above
			rowIndex++;
		}
	}
	
	private int addPreamble(XSSFSheet sheet, LiveClip lc, int rowIndex, int count) {
		final Font font = sheet.getWorkbook ().createFont ();
//	    font.setFontName ( "Arial" );
	    font.setColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex()); 
		final CellStyle style = sheet.getWorkbook ().createCellStyle ();
		style.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
		style.setFont(font);
		
		final CellStyle style2 = sheet.getWorkbook ().createCellStyle ();
		style2.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
		style2.setFont(font);
		style2.setBorderBottom(BorderStyle.THIN);
		CellStyle xxx = style;
		if (lc.noteList.size() < count) count = lc.noteList.size();
		for (int i = lc.noteList.size() - count; i < lc.noteList.size(); i++){
			if (i == lc.noteList.size() - 1) xxx = style2;
			XSSFRow row = sheet.createRow((short)rowIndex);
			LiveMidiNote lmn = lc.noteList.get(i);
			makeCell(row, 0, lmn.note, xxx);
			makeCell(row, 1, lmn.position, xxx);
			makeCell(row, 2, lmn.length, xxx);
			makeCell(row, 3, lmn.velocity, xxx);
			makeCell(row, 4, lmn.mute, xxx);
			makeCell(row, 5, -1, xxx);
			makeFormulaCell(row, 6, "B" + (rowIndex + 1) + "+(F" + (rowIndex + 1) +"*$D$2)", xxx);
			//makeCell(row, 7, "", xxx);		// only main data has actual value. this is there to make sure there is a line
			//makeCell(row, 8, "", xxx);		// only main data has actual value. this is there to make sure there is a line
			if (i > lc.noteList.size() - count){
				makeFormulaCell(row, 7, "G" + (rowIndex + 1) + "-G" + rowIndex, xxx);
				makeFormulaCell(row, 8, "ROUND((G" + (rowIndex + 1) + "-(G" + rowIndex + "+C" + rowIndex + "))*100,1)/100", xxx);
				makeFormulaCell(row, 9, "A" + (rowIndex + 1) + "-A" + rowIndex, xxx);
				makeFormulaCell(row, 10, "ABS(A" + (rowIndex + 1) + "-A" + rowIndex + ")", xxx);			
				makeFormulaCell(row, 17, "U" + (rowIndex + 1) + "*U$9+W" + (rowIndex + 1) + "*W$9+Y" + (rowIndex + 1) + "*Y$9", xxx);
				makeFormulaCell(row, 19, "ABS(J" + (rowIndex + 1) + ")+0.1", xxx);
				makeFormulaCell(row, 20, "(ABS(T" + rowIndex + "-T" + (rowIndex + 1) + "))/(T" + rowIndex + "+T" + (rowIndex + 1) + ")", xxx);
				makeFormulaCell(row, 21, "H" + (rowIndex + 1) + "+0.1", xxx);
				makeFormulaCell(row, 22, "(ABS(V" + rowIndex + "-V" + (rowIndex + 1) + "))/(V" + rowIndex + "+V" + (rowIndex + 1) + ")", xxx);
				makeFormulaCell(row, 23, "I" + (rowIndex + 1) + "+0.1", xxx);
				makeFormulaCell(row, 24, "(ABS(X" + rowIndex + "-X" + (rowIndex + 1) + "))/(X" + rowIndex + "+X" + (rowIndex + 1) + ")", xxx);
				
				
			}
			//makeCell(row, 9, "", xxx);		// only main data has actual value. this is there to make sure there is a line
			//makeCell(row, 10, "", xxx);		// only main data has actual value. this is there to make sure there is a line
			makeCell(row, 11, "", xxx);		// only main data has actual value. this is there to make sure there is a line
			makeCell(row, 12, "", xxx);		// only main data has actual value. this is there to make sure there is a line
			makeCell(row, 13, "", xxx);		// only main data has actual value. this is there to make sure there is a line
			makeCell(row, 14, "", xxx);		// only main data has actual value. this is there to make sure there is a line
			makeCell(row, 15, "", xxx);		// only main data has actual value. this is there to make sure there is a line
			makeCell(row, 16, "", xxx);		// only main data has actual value. this is there to make sure there is a line
			// 17 in if then above
			makeCell(row, 18, "", xxx);		// only main data has actual value. this is there to make sure there is a line
			// 19 in if then above 
			// 20 in if then above 	
			// 21 in if then above 
			// 22 in if then above 
			// 23 in if then above 
			// 24 in if then above 
			rowIndex++;
		}
		return rowIndex;
	}

	private int addMainData(XSSFSheet sheet, LiveClip lc, int rowIndex) {
		
		final CellStyle style = sheet.getWorkbook ().createCellStyle ();
		style.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
		int mainDataStartRow = rowIndex + 1;
		for (LiveMidiNote lmn: lc.noteList){
			XSSFRow row = sheet.createRow((short)rowIndex);
			makeCell(row, 0, lmn.note, style);
			makeCell(row, 1, lmn.position, style);
			makeCell(row, 2, lmn.length, style);
			makeCell(row, 3, lmn.velocity, style);
			makeCell(row, 4, lmn.mute, style);
			makeCell(row, 5, 0, style);
			makeFormulaCell(row, 6, "B" + (rowIndex + 1) + "+(F" + (rowIndex + 1) +"*$D$2)", style);
			makeFormulaCell(row, 7, "G" + (rowIndex + 1) + "-G" + rowIndex, style);
			makeFormulaCell(row, 8, "ROUND((G" + (rowIndex + 1) + "-(G" + rowIndex + "+C" + rowIndex + "))*100,1)/100", style);
			makeFormulaCell(row, 9, "A" + (rowIndex + 1) + "-A" + rowIndex, style);
			makeFormulaCell(row, 10, "ABS(A" + (rowIndex + 1) + "-A" + rowIndex + ")", style);			
			makeFormulaCell(row, 11, "C" + (rowIndex + 1) + "-C" + rowIndex, style);
			
			makeFormulaCell(row, 12, "IF(AND(ROUND((H" + (rowIndex + 1) + "- H" + rowIndex + ")*100,1)/100>0,ROUND((H" + (rowIndex + 2) + "- H" + (rowIndex + 1) + ")*100,1)/100<0),\"start\",\"\")", style);
			makeFormulaCell(row, 13, "IF(AND(I" + (rowIndex + 1) + "- I" + rowIndex + ">0,I" + (rowIndex + 2) + "- I" + (rowIndex + 1) + "<0),\"start\",\"\")", style);
			makeFormulaCell(row, 14, "IF(AND(K" + (rowIndex + 1) + "- K" + rowIndex + ">0,K" + (rowIndex + 2) + "- K" + (rowIndex + 1) + "<0),\"start\",\"\")", style);
			
			makeFormulaCell(row, 19, "ABS(J" + (rowIndex + 1) + ")+0.1", style);
			makeFormulaCell(row, 20, "(ABS(T" + rowIndex + "-T" + (rowIndex + 1) + "))/(T" + rowIndex + "+T" + (rowIndex + 1) + ")", style);
			makeFormulaCell(row, 21, "H" + (rowIndex + 1) + "+0.1", style);
			makeFormulaCell(row, 22, "(ABS(V" + rowIndex + "-V" + (rowIndex + 1) + "))/(V" + rowIndex + "+V" + (rowIndex + 1) + ")", style);
			makeFormulaCell(row, 23, "I" + (rowIndex + 1) + "+0.1", style);
			makeFormulaCell(row, 24, "(ABS(X" + rowIndex + "-X" + (rowIndex + 1) + "))/(X" + rowIndex + "+X" + (rowIndex + 1) + ")", style);
			
			makeFormulaCell(row, 15, "IF(AND(ROUND((R" + (rowIndex + 1) + "- R" + rowIndex + ")*100,1)/100>0,ROUND((R" + (rowIndex + 2) + "- R" + (rowIndex + 1) + ")*100,1)/100<0),\"start\",\"\")", style);
			makeFormulaCell(row, 16, "IF(AND(P" + (rowIndex + 1) + "=\"start\",R" + (rowIndex + 1) + ">S" + (rowIndex + 1) + "),\"start\",\"\")", style);
			makeFormulaCell(row, 17, "U" + (rowIndex + 1) + "*U$9+W" + (rowIndex + 1) + "*W$9+Y" + (rowIndex + 1) + "*Y$9", style);
			makeFormulaCell(row, 18, "AVERAGE(R" + (rowIndex - 2) + ":R" + (rowIndex + 4) + ")+STDEV(R$" + mainDataStartRow + ":R$" + (mainDataStartRow + lc.noteList.size()) + ")*S$9", style);
			
		
			
			rowIndex++;
		}
		return rowIndex;
	}

	private void makeFormulaCell(XSSFRow row, int col, String string, CellStyle style) {
		System.out.println(string);
		Cell cell = row.createCell(col);
		cell.setCellFormula(string);
		cell.setCellStyle(style);
		
	}
	private void makeCell(XSSFRow row, int col, String value, CellStyle style) {
		Cell cell = row.createCell(col);
		cell.setCellValue(value);
		cell.setCellStyle(style);
		
	}
	private void makeCell(XSSFRow row, int col, int value, CellStyle style) {
		Cell cell = row.createCell(col);
		cell.setCellValue(value);
		cell.setCellStyle(style);
		
	}
	private void makeCell(XSSFRow row, int col, double value, CellStyle style) {
		Cell cell = row.createCell(col);
		cell.setCellValue(value);
		cell.setCellStyle(style);
		
	}

	private void makeDataHeaders(XSSFSheet sheet, int rowIndex) {
		XSSFRow row = sheet.createRow((short)rowIndex);
		
		int col = 0;
		final CellStyle style = sheet.getWorkbook ().createCellStyle ();
		style.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);		
		style.setBorderBottom(BorderStyle.THIN);
		style.setWrapText(true);
		for (String head: headerArr){
			Cell cell = row.createCell(col);
			cell.setCellValue(head);
			col++;
			cell.setCellStyle(style);
		}
		doLBDMweightings(rowIndex - 1, sheet);
	}

	private void doLBDMweightings(int rowIndex, XSSFSheet sheet) {
		XSSFRow row = sheet.createRow((short)rowIndex);
		final CellStyle style = sheet.getWorkbook ().createCellStyle ();
		style.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
		makeCell(row, LBDM_STDEV_COL, LBDM_STDEV_WEIGHTING, style);
		makeCell(row, LBDM_PITCH_COL, LBDM_PITCH_WEIGHTING, style);
		makeCell(row, LBDM_IOI_COL, LDBM_IOI_WEIGHTING, style);
		makeCell(row, LBDM_REST_COL, LDBM_REST_WEIGHTING, style);
	}

	private void makeHeaders(XSSFSheet sheet, LiveClip lc) {
		makeHeaderRow(sheet, "name", lc.name, 0, 0);
		makeHeaderRow(sheet, "length", lc.length, 1, 0);
		makeHeaderRow(sheet, "loopStart", lc.loopStart, 2, 0);
		makeHeaderRow(sheet, "loopEnd", lc.loopEnd, 3, 0);
		makeHeaderRow(sheet, "startMarker", lc.startMarker, 0, 5);
		makeHeaderRow(sheet, "endMarker", lc.endMarker, 1, 5);
		makeHeaderRow(sheet, "signatureNumerator", lc.signatureNumerator, 2, 5);
		makeHeaderRow(sheet, "signatureDenominator", lc.signatureDenominator, 3, 5);

	}
	private void makeHeaderRow(XSSFSheet sheet, String name, double value, int rowIndex, int colIndex) {
		XSSFRow row = sheet.createRow((short)rowIndex);
		
		Cell cell = row.createCell(colIndex);
		cell.setCellValue(name);
		cell = row.createCell(colIndex + 3);
		cell.setCellValue(value);

		final CellStyle style = sheet.getWorkbook ().createCellStyle ();
		style.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
		cell.setCellStyle(style);

	}
	private void makeHeaderRow(XSSFSheet sheet, String name, int value, int rowIndex, int colIndex) {
		XSSFRow row = sheet.createRow((short)rowIndex);
		
		Cell cell = row.createCell(colIndex);
		cell.setCellValue(name);
		cell = row.createCell(colIndex + 3);
		cell.setCellValue(value);

		final CellStyle style = sheet.getWorkbook ().createCellStyle ();
		style.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
		cell.setCellStyle(style);

	}

	private void makeHeaderRow(XSSFSheet sheet, String name, String value, int rowIndex, int colIndex) {
		XSSFRow row = sheet.createRow((short)rowIndex);
		
		Cell cell = row.createCell(colIndex);
		cell.setCellValue(name);
		cell = row.createCell(colIndex + 3);
		cell.setCellValue(value);

		final CellStyle style = sheet.getWorkbook ().createCellStyle ();
		style.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
		cell.setCellStyle(style);

	}
}
