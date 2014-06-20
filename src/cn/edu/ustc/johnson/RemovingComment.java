package cn.edu.ustc.johnson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemovingComment {
	// is file or directory. true -- file, false -- directory
	private boolean m_bIsFile;
	// path
	private String m_strPath;
	
	public RemovingComment(String strPath){
		m_strPath = strPath;
	}
	
	public void parseAndRemove(){
		if(m_strPath == null){ 
			System.out.println("Please input valid path!");
		}
		
		File file = new File(m_strPath);
		if(!file.exists()){
			System.out.println("The file/direcotry does not exist!");
			return;
		}
		
		// whether the path be a file or not
		if(file.isDirectory()){
			m_bIsFile = false;
		}else{
			m_bIsFile = true;
		}		
	
		if(m_bIsFile){
			if(isCppFile(file)){
				readwriteFile(m_strPath);
			}
		}else{
			File[] strAll = file.listFiles();
			for(int i = 0; i < strAll.length; i++){
				m_strPath = strAll[i].getAbsolutePath();
				parseAndRemove();
			}
		}
		
	}
	
	// support .h or .cpp file temporarily
	private boolean isCppFile(File file){
		String strName = file.getName();
		
		String strhPattern = ".+\\.h";
		String strcppPattern = ".+\\.cpp";
		
		Pattern ptnH = Pattern.compile(strhPattern);
		Pattern ptnCpp = Pattern.compile(strcppPattern);
		
		Matcher mH = ptnH.matcher(strName);
		Matcher mCpp = ptnCpp.matcher(strName);
				
		if(mH.find() || mCpp.find()){
			return true;
		}
		
		return false;
	}
	
	// read file content and write to the file
	private void readwriteFile(String strPath){
		File file = new File(strPath); 
		
		FileInputStream in = null;
		//FileOutputStream out = null;
		
		// read
		Long fileLength = file.length();
		byte[] fileContent = new byte[fileLength.intValue()];
		
		try{
			in = new FileInputStream(file);
			in.read(fileContent);
			in.close();
		}catch(IOException e){
			System.out.println("io exception" + e.getMessage());
		}
		
		String str = new String(fileContent);
		
		// deal with comments
		StartEndPair pair = regexParse(str);
		
		if(pair.getStart() == -1 || pair.getEnd() == -1){
			return;
		}
		
		String strWrite = str.substring(pair.getEnd());
		
		// write file
		FileWriter fw = null;
		try{
			fw = new FileWriter(file);
			fw.write(strWrite);
			fw.close();
		}catch(IOException e){
			System.out.println("io exception" + e.getMessage());
		}
	}
	
	private StartEndPair regexParse(String str){
		StartEndPair rt = new StartEndPair(-1, -1);
		
		//String pattern = "^/\\*+\\s+Copyright.+\\*+/\\s+";
		//String pattern = "^/\\*+\\s+Copyright(.+\\s+)+\\*+/\\s+";
		//String pattern = "abc";
		
		//String pattern = "^/\\*+\\s+Copyright[(.+\\s+)&&[^(\\*+/)]]\\*+/\\s+";
		String patternStart = "^/\\*+\\s+";
		String patternEnd = "\\*+/\\s+";
		Pattern rStart = Pattern.compile(patternStart);
		Pattern rEnd = Pattern.compile(patternEnd);
		
		//str = "abc";
		
		Matcher mStart = rStart.matcher(str);
		Matcher mEnd = rEnd.matcher(str);
		if(mStart.find() && mEnd.find()){
			int iStart = mStart.start();
			int iEnd = mEnd.end();
			rt.setStart(iStart);	
			rt.setEnd(iEnd);
			//System.out.println("find");
		}			
		
		return rt;		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RemovingComment rc = new RemovingComment("/home/johnson/comment/TAC/");
		rc.parseAndRemove();
	}

}
