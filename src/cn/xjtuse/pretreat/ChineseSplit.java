package cn.xjtuse.pretreat;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import cn.xjtuse.util.logging.Log;
import ICTCLAS.I3S.AC.ICTCLAS50;


public class ChineseSplit {

	public  static String[] splitChinese(String oriPlain) {
		String[] resStr = null ;
		int spaceCnt = 0 ;
		try {
			ICTCLAS50 ictclas50 = new ICTCLAS50() ;
			String argu = "." ;
			if(ictclas50.ICTCLAS_Init(argu.getBytes("GB2312")) == false){
				Log.logger.info("ICTCLAS初始化失败") ;
			}
			String usrDict = "userdict.txt" ;
			int loadCount = 0 ;
			loadCount = ictclas50.ICTCLAS_ImportUserDictFile(usrDict.getBytes(), 0) ;
			if(loadCount > 0){
				Log.logger.info("导入用户词典，导入" + loadCount + "条");
			}
			byte[] splitPlain = ictclas50.ICTCLAS_ParagraphProcess(oriPlain.getBytes("UTF-8"), 0, 0) ;
		    String strSplitPlain = new String(splitPlain,"UTF-8") ;
		    
		    String[] commas = {"，","。","?","\"","\'","；","、","！"} ; 
			Set<String> commaSet = new HashSet<String>() ;
			for (int i = 0; i < commas.length; i++) {
				commaSet.add(commas[i]) ;
			}
			String[] stopWords = {"的","地","得"} ;
		    Set<String> stopSet = new HashSet<String>() ;
		    for (int i = 0; i < stopWords.length; i++) {
		    	stopSet.add(stopWords[i]) ;
			}
			
		    resStr = strSplitPlain.split(" ") ;
		    
		    for(int i = 0; i < resStr.length; i++){
		    	if(commaSet.contains(resStr[i]) || stopSet.contains(resStr[i])){
		    		resStr[i] = "" ;
		    		spaceCnt ++ ;
		    	}
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		int len = 0 ;
		String[] temp = new String[resStr.length - spaceCnt] ;
		for(int i = 0; i < resStr.length; i++){
			if(!"".equals(resStr[i])){
				temp[len] = resStr[i] ;
				len ++ ;
			}
		}
		return temp ;
	}
	
	public static void main(String args[]){
		String[] result = splitChinese("中国是一个，大的得地国家！") ;
		for (int i = 0; i < result.length; i++) {
			System.out.println(result[i]);
		}
	}
}


