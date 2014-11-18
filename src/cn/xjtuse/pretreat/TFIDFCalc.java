package cn.xjtuse.pretreat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import cn.xjtuse.util.logging.Log;



public class TFIDFCalc {
	
	
	
	static Map<Integer ,Map<String, Float>> TFS = new HashMap<Integer, Map<String , Float>>() ;

	static Map<String, Float> IDF = new HashMap<String, Float>() ;

	public static Map<Integer,Map<String, Float>> TF_IDF = new HashMap<Integer, Map<String, Float>>() ; 

	public  static Map<String,Float> calcTFOfSingleDoc(String[] splitResult ){

		Map<String,Float> TF_Int = new HashMap<String, Float>() ;
		
		for (String temp : splitResult) {
			if(!TF_Int.containsKey(temp)){ 
				TF_Int.put(temp,new Float(1)) ;
			}else{  
				TF_Int.put(temp, new Float(TF_Int.get(temp).floatValue() + 1.0));
			}
		}
		Iterator<Entry<String, Float>> iter = TF_Int.entrySet().iterator() ;
		while(iter.hasNext()){
			Map.Entry<String, Float> entry = (Map.Entry<String, Float>)iter.next() ;
			String key = entry.getKey() ;
			Float value = entry.getValue() ;
			float tmpVal = (float)(value.floatValue() / (splitResult.length + 0.0)) ;
			TF_Int.put(key,new Float(tmpVal)) ;
		}
		return TF_Int ;
	}
	
	public static void calcTFOfLibarary(List<String> docLibrary){
		
		Map<String, Float> TF = null ;
		
		for(int i = 0; i < docLibrary.size(); i++){
			String docPlain = docLibrary.get(i) ;
			TF = calcTFOfSingleDoc(ChineseSplit.splitChinese(docPlain)) ;
			TFS.put(i, TF) ;
			Log.logger.log(Level.INFO,"正在计算TFS:[" + i + "]");
		}
	}
	
	public static void calcIDF(){
		
		int numOfFiles = TFS.size() ; 
		int docsNumOfIncludedItem = 1 ;
		List<String> located = new ArrayList<String>() ;
		List<Integer > indexList = new ArrayList<Integer>() ;
		for (Integer index : TFS.keySet()) {
			indexList.add(index) ;
		}
		
		for(int i = 0; i < numOfFiles; i++){
			Map<String, Float> temp = TFS.get(indexList.get(i));
            for (String word : temp.keySet()) {
				docsNumOfIncludedItem = 1 ;
				if(!located.contains(word)){
					for(int k = 0; k < numOfFiles; k++){
						if(k != i){
							Map<String, Float>temp2 = TFS.get(indexList.get(k)) ;
							if(temp2.keySet().contains(word)){
								located.add(word) ;
								docsNumOfIncludedItem ++ ;
								continue ;
							}
						}
					}
					IDF.put(word, new Float(docsNumOfIncludedItem)) ;
				}
			}
            Log.logger.log(Level.INFO,"正在计算整型IDF:[" + i + "]");
		}
		Iterator<Map.Entry<String, Float>> inter =  IDF.entrySet().iterator() ;
		int temp = 0 ;
		while (inter.hasNext()) {
			Map.Entry<String, Float> entry = (Map.Entry<String, Float>)inter.next() ;
			String word = entry.getKey() ;
			Float value = entry.getValue() ;
			
			float tmpVar = (float) Math.log10((numOfFiles + 0.0)/(value.floatValue() + 1.0)) ;
			
			IDF.put(word, new Float(tmpVar)) ;
			temp ++ ;
			Log.logger.log(Level.INFO,"正在计算规格化IDF[" + temp + "]");
		}
		Log.logger.log(Level.INFO,"IDF计算结束");
	} 
	
	public static void calcTFIDF(){
		
		int temp = 0 ;
		for (Integer index : TFS.keySet()) {
			Map<String, Float> singelRecord = TFS.get(index);
			for (String word : singelRecord.keySet()) {
				singelRecord.put(word, (IDF.get(word)) * singelRecord.get(word));
				
			}
			TF_IDF.put(index, singelRecord) ;
			temp ++ ;
			Log.logger.log(Level.INFO,"正在计算TFIDF[" + temp + "]");
		}
	}
	
	public static void printTFIDF(){
		
		for(Entry<Integer, Map<String, Float>> tfidf : TF_IDF.entrySet()){
			Integer index = tfidf.getKey() ;
			Map<String, Float> tf = tfidf.getValue() ;
			for(Map.Entry<String, Float> entry : tf.entrySet()){
				Log.logger.log(Level.INFO,"索引值: " + index + " , 词语: " + entry.getKey() + ", TF_IDF值: " +entry.getValue()) ;
			}
		}
	}
}
