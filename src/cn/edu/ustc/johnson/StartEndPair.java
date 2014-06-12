package cn.edu.ustc.johnson;

// indicate start and end of the comment
public class StartEndPair {
	private int m_iStart;
	private int m_iEnd;
	
	public StartEndPair(int iStart, int iEnd){
		m_iStart = iStart;
		m_iEnd = iEnd;
	}
	
	public int getStart(){
		return m_iStart;
	}
	public int getEnd(){
		return m_iEnd;
	}
	
	public void setStart(int iStart){
		m_iStart = iStart;
	}
	public void setEnd(int iEnd){
		m_iEnd = iEnd;
	}
}
