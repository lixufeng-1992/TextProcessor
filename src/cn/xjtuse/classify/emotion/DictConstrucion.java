package cn.xjtuse.classify.emotion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class DictConstrucion {
	//���õ���ģʽ
	private DictConstrucion(){}
	//����ʵ�,Ȩ��Ϊ1
	public static Set<String> posSet = new HashSet<String>() ;   
	//����ʵ�,Ȩ��Ϊ-1
	public static Set<String> negSet = new HashSet<String>() ;
	//�س̶ȴʵ�,Ȩ��Ϊ1.8
	public static Set<String> heavyDegreeSet = new HashSet<String>() ;
	//��̶ȴʵ�,Ȩ��Ϊ1.5
	public static Set<String> lightDegreeSet = new HashSet<String>() ;
	//��ǰ׺�����Ա䷴(Ȩ��*-1)
	public static Set<String> negPrefixSet = new HashSet<String>() ;
	static{
		File dictPath = new File("E:\\Dict\\") ;
		File posFile = new File(dictPath,"pos.txt") ;
		File negFile = new File(dictPath,"neg.txt") ;
		File heavyFile = new File(dictPath,"heavydegree.txt") ;
		File lightFile = new File(dictPath,"lightdegree.txt") ;
		File negPrefixFile = new File(dictPath,"negprefix.txt") ;
		try {
			File curFile = null ;
			Set<String> curDict = null ;
			BufferedReader br = null ;
			String line = null ;
			for(int i = 0; i < 5; i++){
				switch (i) {
				case 0:
					curFile = posFile ;
					curDict = posSet ;
					break;
				case 1:
					curFile = negFile ;
					curDict = negSet ;
					break;
				case 2:
					curFile = heavyFile ;
					curDict = heavyDegreeSet ;
					break;
				case 3:
					curFile = lightFile ;
					curDict = lightDegreeSet ;
					break;
				case 4:
					curFile = negPrefixFile ;
					curDict = negPrefixSet ;
					break;
				default:
					break;
				}
				br = new BufferedReader(new InputStreamReader(new FileInputStream(curFile),"UTF-8")) ;
				while((line = br.readLine()) != null){
					curDict.add(line.trim());
				}
				br.close() ;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		System.out.println(posSet) ;
		System.out.println();
		System.out.println(negSet) ;
		System.out.println();
		System.out.println(lightDegreeSet) ;
		System.out.println();
		System.out.println(heavyDegreeSet) ;
		System.out.println();
		System.out.println(negPrefixSet);
		System.out.println();
		System.out.println(negPrefixSet.size() + " " + negPrefixSet.contains("��"));
		System.out.println(negPrefixSet.contains("����"));
	}
	
}
