package cn.xjtuse.util.logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
	public static Logger logger = null ;
	static{
		logger = Logger.getLogger("mylog") ;
		FileHandler fh = null ;
		try {
			fh = new FileHandler("E:/tfidf.log", true);
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.addHandler(fh) ; 
		logger.setLevel(Level.ALL) ;
		SimpleFormatter sf = new SimpleFormatter() ;
		fh.setFormatter(sf) ;
	}
}
