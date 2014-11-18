package cn.xjtuse.classify.svm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import cn.xjtuse.pretreat.TFIDFCalc;


public class TextClassifier {
	
	public static String classify(String text){
		List<String> doc = new ArrayList<String>(1) ;
		doc.add(text) ;
		TFIDFCalc.calcTFOfLibarary(doc) ;
		TFIDFCalc.calcIDF() ; 
		TFIDFCalc.calcTFIDF() ;
		
		StringBuffer sb = new StringBuffer() ;
		Map<Integer, Map<String, Float>> tfidfs = TFIDFCalc.TF_IDF ;
		int count = 1 ;
		for(Entry<Integer, Map<String, Float>> tfidf : tfidfs.entrySet()){
			sb.append("+1") ;
			Map<String, Float> tf = tfidf.getValue() ;
			for(Map.Entry<String, Float> entry : tf.entrySet()){
				sb.append(" " + count + ":" + entry.getValue()) ;
				count ++ ;
			}
		}
		
		String wanted = sb.toString() ;
		System.out.println(wanted);
		
		String []arg = {wanted,    //"E:\\test.txt",   
	     "E:\\model.model","E:\\our_r.txt"}; 
		try {
			svm_predict_modified.main(arg) ;
		} catch (IOException e){
			e.printStackTrace();
		}
		Scanner scan = null ;
		try {
			scan = new Scanner(new File("E:\\our_r.txt")) ;
			String result = scan.next() ;
			float res = Float.parseFloat(result) ;
			if(floatEqual(res, (float)+1.0)){
				return "POSITIVE" ;
			}else if(floatEqual(res,(float)-1.0)){
				return "NEGTIVE" ;
			}else {
				return "UNKNOW" ;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally{
			scan.close() ; 
		}
		return "UNKNOW" ;
	}
	
	public static boolean floatEqual(float var1,float var2){
		if(Math.abs(var1 - var2) < 0.000006){
			return true ;
		}
		return false ;
	}
	
	public static void main(String args[]){
		System.out.println(classify("中国是一个好国家"));
		System.out.println("Success");
	}
	
	
}
