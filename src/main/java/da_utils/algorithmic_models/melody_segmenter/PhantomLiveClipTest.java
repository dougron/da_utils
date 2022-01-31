package main.java.da_utils.algorithmic_models.melody_segmenter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
import main.java.da_utils.xml_maker.MXM;
import main.java.da_utils.xml_maker.MusicXMLMaker;
import main.java.da_utils.xml_maker.key.XMLKeyZone;
import main.java.da_utils.xml_maker.time_signature.XMLTimeSignatureZone;

public class PhantomLiveClipTest extends ConsoleProgram {

	String testClipPath = "C:/Users/Doug/Documents/_MASTER OF UNIBERSE/Repetition text files/CorpusMelodyFiles/Confirmation.liveclip";
	String xmlPath = "C:/Users/Doug/Documents/_MASTER OF UNIBERSE/Repetition text files/SegmentetXMLs";
	String xmlFileName = "segmenterTest.xml";
	String xlFileName = "segmenterTest.xlsx";
	int textSize = 7;
	
	public void run(){
		setSize(700, 700);
		try {
			LiveClip lc = new LiveClip(0, 0);
			lc.instantiateClipFromBufferedReader(new BufferedReader(new FileReader(new File(testClipPath))));
			PhantomLiveClip plc= new PhantomLiveClip(lc);
//			println(plc.toString());
			InterOnsetPeakBoundary iopb = new InterOnsetPeakBoundary();
			OffsetToOnsetBoundary otob = new OffsetToOnsetBoundary();
			BatchSegmenter bs = new BatchSegmenter(new SegmentationModel[]{iopb, otob}, new double[]{0.5, 0.5}, 0.6, plc);
			println(iopb.toString());
			println(otob.toString());
			
			MusicXMLMaker mxm = new MusicXMLMaker(MXM.KEY_OF_C);
			//mxm.setLandscapePageOrientation();	// does not work. need to tweak import settings in Sibelius to get landscape orientation

			mxm.measureMap.addNewTimeSignatureZone(new XMLTimeSignatureZone(lc.signatureNumerator, lc.signatureDenominator, lc.loopEndBarCount())); 
			mxm.keyMap.addNewKeyZone(new XMLKeyZone(MXM.KEY_OF_C, lc.barCount()));
			
			String partName = plc.name;
			mxm.addPart(partName, plc.lc); //, int signatureNumerator, int signatureDenominator, String fileName, int barCount);
			
			for (String key: bs.getBoundaryListMap().keySet()){
				for (Double d: bs.getBoundaryListMap().get(key)){
					mxm.addTextDirection(partName, key, d, MXM.PLACEMENT_ABOVE, textSize);					
				}
			}
			for (Double d: bs.boundaryList()){
				mxm.addTextDirection(partName, "xxx", d, MXM.PLACEMENT_ABOVE, textSize);
				
			}
			
//			for (int i = 0; i < plc.noteList.size(); i++){
//				LiveMidiNote lmn = plc.noteList.get(i);
//				println(i + ": " + lmn.toString());
//				if (!(lmn instanceof PhantomNote)){
//					println("adding otob data to mxm");
//					mxm.addTextDirection(partName, "OOI:" + plc.offsetToOnsetIntervalList.get(i), lmn.position, MXM.PLACEMENT_ABOVE, 6);
//				}
//				
//			}
			
			
			mxm.makeXML(xmlPath + "/" + xmlFileName);
			makeXLoutput(new BatchSegmenter[]{bs});
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
// excel maker stuff------------------------------------------------------------- 
	
	String[] headerArr = new String[]{
			"note", "pos", "len", "vel", "mute", 
			"inter onset gap to previous", "preceding rest length",};
//			"previous interval", "abs previous interval", "change in length",
//			"inter onset peak boundary GPR2b", "preceding rest boundary GPR2a", "preceding abs interval",
//			"LBDM", "LBDM with STDEV peak filter", "LBDM total", "STDEV", "", "LDBM pitch interval", "",
//			"LBDM IOI", "", "LBDM rest"};
	CellStyle csContent;
	CellStyle csContentUnderline;
	CellStyle csPhantom;
	CellStyle csPhantomUnderline;
	
	
	private void makeXLoutput(BatchSegmenter[] bsArr) {
		XSSFWorkbook wb = new XSSFWorkbook();
		Font fullFont = getFullFont(wb);
		Font greyFont = getGreyFont(wb);
		csContent = makeContentStyles(wb, fullFont, false);
		csContentUnderline = makeContentStyles(wb, fullFont, true);
		csPhantom = makeContentStyles(wb, greyFont, false);
		csPhantomUnderline = makeContentStyles(wb, greyFont, true);
		int rowStartIndex = 0;
		int dataHeaderRowIndex = 6;
		int phantomRowCount = 12;
		boolean showAllPhantomRows = true;
		for (BatchSegmenter bs: bsArr){
			XSSFSheet sheet = wb.createSheet(bs.plc().name);
			makeHeaders(sheet, bs.plc().lc);			
			makeDataHeaders(sheet, dataHeaderRowIndex, bs);
			addContent(sheet, dataHeaderRowIndex + 1, bs, showAllPhantomRows, phantomRowCount);
		}
		try {
			FileOutputStream out = new FileOutputStream( 
					new File(xmlPath + "/" + xlFileName));
			wb.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void addContent(XSSFSheet sheet, int startRow, BatchSegmenter bs, boolean showAllPhantomRows, int phantomRowCount) {
		int start;
		int end;
		if (showAllPhantomRows){
			if (bs.plc().noteList.size() < phantomRowCount){
				start = 0;
				end = bs.plc().noteList.size();
			} else {
				start = bs.plc().phantomStartIndex - phantomRowCount;
				end = bs.plc().phantomEndIndex + phantomRowCount;
			}
		} else {
			start = 0;
			end = bs.plc().noteList.size();
		}
		CellStyle style;
		int rowIndex = startRow;
		SegmentationModel[] smArr = bs.segmentationModelArr();
		for (int index = start; index < end; index++){
			LiveMidiNote lmn = bs.plc().noteList.get(index);
			boolean doUnderline = false;
			if (index < bs.plc().phantomStartIndex - 1 || index > bs.plc().phantomEndIndex - 1){
				style = csPhantom;
			} else if (index == bs.plc().phantomStartIndex - 1){
				style = csPhantomUnderline;
				doUnderline = true;
			} else if (index == bs.plc().phantomEndIndex - 1){
				style = csContentUnderline;
				doUnderline = true;
			} else {
				style = csContent;
			}
			XSSFRow row = sheet.createRow((short)rowIndex);
			makeCell(row, 0, lmn.note, style);
			makeCell(row, 1, lmn.position, style);
			makeCell(row, 2, lmn.length, style);
			makeCell(row, 3, lmn.velocity, style);
			makeCell(row, 4, lmn.mute, style);
			makeCell(row, 5, bs.plc().interOnsetIntervalList.get(index), style);
			makeCell(row, 6, bs.plc().offsetToOnsetIntervalList.get(index), style);
			int rrow = 7;
			for (SegmentationModel sm: smArr){
				if (sm.boundaryList().contains(lmn.position)){
					makeCell(row, rrow, sm.xmlTag(), style);
				} else if (doUnderline){
					makeCell(row, rrow, "", style);
				}
				rrow++;
			}
			if (bs.boundaryList().contains(lmn.position)){
				makeCell(row, rrow, "xxx", style);
			} else if (doUnderline){
				makeCell(row, rrow, "", style);
			}
			
			rowIndex++;
		}
		
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

	private Font getGreyFont(XSSFWorkbook wb) {
		Font font = wb.createFont ();
		font.setColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex()); 
		return font;
	}

	private Font getFullFont(XSSFWorkbook wb) {
		Font font = wb.createFont ();
//	    font.setFontName ( "Arial" );
		return font;

	}
	private CellStyle makeContentStyles(XSSFWorkbook wb, Font f, boolean underline){
		CellStyle style = wb.createCellStyle ();
		style.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
		style.setFont(f);
		if (underline) style.setBorderBottom(BorderStyle.THIN);
		
		return style;
		
	}

	private void makeDataHeaders(XSSFSheet sheet, int rowIndex, BatchSegmenter bs) {
		XSSFRow row = sheet.createRow((short)rowIndex);
		XSSFRow weightingRow = sheet.createRow((short)rowIndex - 1);
		
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
		int smIndex = 0;
		for (SegmentationModel sm: bs.segmentationModelArr()){
			Cell cell = row.createCell(col);
			cell.setCellValue(sm.xmlTag());
			cell.setCellStyle(style);
			
			Cell cell2 = weightingRow.createCell(col);
			cell2.setCellValue(bs.getWeighting(smIndex));
			cell2.setCellStyle(style);
			col++;
			
			smIndex++;
		}
		Cell cell = row.createCell(col);
		cell.setCellValue("final");
		col++;
		cell.setCellStyle(style);
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
