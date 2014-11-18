package cn.xjtuse.classify.svm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.xjtuse.pretreat.TFIDFCalc;
import cn.xjtuse.util.logging.Log;


public class Trainer {
	
	public void doTrain(){
		prepareTrainFile("E:\\train.txt");
		String []arg = {"E:\\train.txt",   
	     "E:\\model.model"}; 
		try {
			svm_train.main(arg);
			} catch (IOException e) {
				e.printStackTrace();
			}  
		}
	
	private void prepareTrainFile(String destPath){
		List<String> posList = loadRecord("G:\\实验室\\+1.txt") ;
		List<String> negList = loadRecord("G:\\实验室\\-1.txt") ;
		List<String> neuList = loadRecord("G:\\实验室\\0.txt") ;
		List<String> allList = new ArrayList<String>() ;
//		allList.addAll(posList) ;
//		allList.addAll(negList) ;
//		allList.addAll(neuList) ;
		
		for (String string : posList) {
			allList.add(string) ;
		}
		for (String string : negList) {
			allList.add(string) ;
		}
		for (String string : neuList) {
			allList.add(string) ;
		}
		
		Log.logger.info("posList::size [" + posList.size() + "]");
		Log.logger.info("negList::size [" + negList.size() + "]");
		Log.logger.info("neuList::size [" + neuList.size() + "]");
		Log.logger.info("allList::size [" + allList.size() + "]");
		
		TFIDFCalc.calcTFOfLibarary(allList);
		TFIDFCalc.calcIDF() ;
		TFIDFCalc.calcTFIDF() ;
		
		TFIDFCalc.printTFIDF() ;
		
		Map<Integer,Map<String, Float>> TF_IDF = TFIDFCalc.TF_IDF ;
		
		PrintWriter pw = null ;
		int count = 1 ;
	
		
		try {
			pw = new PrintWriter(destPath) ;
			
			for (Map.Entry<Integer, Map<String, Float>> tfidfs : TF_IDF.entrySet()) {
				
				Integer recordId = tfidfs.getKey() ;
				Log.logger.info("recordID = " + recordId);
				Map<String, Float>  tfidf = tfidfs.getValue() ;
				
				count = 1 ;
				if(recordId < posList.size()){
					pw.write("+1") ;
					Log.logger.info("+1") ;
				}else if(recordId < negList.size()){
					pw.write("-1") ;
					Log.logger.info("-1") ;
				}else //if(recordId < neuList.size()){
				{
					pw.write("0") ;
					Log.logger.info("0") ;
				}
				for (Map.Entry<String, Float> entry : tfidf.entrySet()) {
					String item = entry.getKey() ;
					Float traitValue = entry.getValue() ;
					pw.write(" " + count + ":" + traitValue) ;
					Log.logger.info(" " + item + "[" + count + "]" + ":" + traitValue);
					count ++ ;
				}
				pw.write("\r\n") ;
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally{
			pw.flush() ;
			pw.close() ;
		}
	}
	
	private  List<String> loadRecord(String strPath) {
		File resFile = new File(strPath) ;
		List<String> resList = new ArrayList<String>() ;
		BufferedReader br = null ;
		try {
			String line = null ;
			br = new BufferedReader(new InputStreamReader(new FileInputStream(resFile),"UTF-8")) ;
			while((line = br.readLine()) != null){
				resList.add(line) ;
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				br.close() ;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resList;
	}
}
