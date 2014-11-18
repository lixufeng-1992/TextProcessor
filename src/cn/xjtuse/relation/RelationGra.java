package cn.xjtuse.relation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.xjtuse.pretreat.ChineseSplit;

public class RelationGra {


	static int NUM=10;
	List<String> article;
	String keyword;
	List<String>key_assemble;
	List<List<String> >sub_assemble;
	List<String>word;


	
	List<segmentInfo> segInfo = new ArrayList<segmentInfo>();//�ִ�֮�����Ϣ��

	
	void init()
	{
		key_assemble = new ArrayList<String>() ;
		sub_assemble = new ArrayList<List<String>>() ;
		keyword = "����" ;
		article = new ArrayList<String>() ;
		article.add("��������") ;
		article.add("˯��") ;
		article.add("�Է�") ;
		article.add("���") ;
		article.add("����") ;
		article.add("��������") ;
		article.add("�̷�") ;
		article.add("�Ŵ�") ;
		article.add("�Ʒ�") ;
		article.add("���") ;
	}

	List<String> GetSegmentation(List<String> article,int num)
	{
		List<String> res = new ArrayList<String>() ;
		for (String oriPlain : article) {
			String[] text = ChineseSplit.splitChinese(oriPlain) ;
			for (int i = 0; i < text.length; i++) {
				res.add(text[i]) ;
			}
		}
		return res;
	}

	//ͳ��ÿ���ִ�����Щ�����г��ֹ���
	/* 
	*article,num ͬ��
	*par3:word�ǵ���GetSegmentation����֮��ķ���ֵ���������·ִʵ����ս��
	*par4:segInfo����Ҫͳ�Ƶ�ÿһ���ִ���Ϣ��struct�е�is_appear��ʶ����ִ��Ƿ��ڵ�iƪ�����г��ֹ�
	*/
	void GetSegmentationInof(List<String> article,int num,List<String>word,List<segmentInfo> segInfo)
	{
		for(int k=0;k<word.size();k++)
		{//����K�������Ƿ��ڵ�I�������г��ֹ���
			segmentInfo si = new segmentInfo() ;
			si.word=word.get(k);
			for(int i=0;i<num;i++)
			{
				if(article.get(i).indexOf(word.get(k)) != -1)
					si.is_appear[i]=true;//����word[k]�ڵ�iƪ�����г��ֹ�
				else
					si.is_appear[i]=false;
			}
			segInfo.add(si);
		}
	}


	/*����word1��word2�������
	* par1: ����
	* par2: ���ʴ洢��vector�����飩
	* return: ��������
	*/
	int FindWordFromSegmentInfo(String word,List<segmentInfo>  segInfo)
	{
		for(int i=0;i<segInfo.size();i++)
		{
			if(word.equals(segInfo.get(i).word))
				return i;
		}
		return -1;
	}
	/*
	*return �����
	*par1 ,par2 ��������
	*par3:��������
	*par4:���¼�������
	*/
	double CalcToWordRelevancy(String word1,String word2,List<segmentInfo> segInfo,int articlenum)
	{
		//����word1���ֵ�������
		int word1num =0;
		//�ҵ�word1�ڵ���������±�
		int word1_index=FindWordFromSegmentInfo(word1,segInfo);
		if(word1_index == -1){
			return 0 ;
		}

		//����word1���ֵ�������
		int word2num =0;
		//�ҵ�word1�ڵ���������±�
		int word2_index=FindWordFromSegmentInfo(word2,segInfo);

		//ͳ���������������¼����г��ֵĴ���
		//��ͬ���ֵĴ���
		int allappear=0;
		for(int i=0;i<articlenum;i++)
		{
			if(segInfo.get(word1_index).is_appear[i])
				word1num++;
			if(segInfo.get(word2_index).is_appear[i])
				word2num++;
			if(segInfo.get(word1_index).is_appear[i] && segInfo.get(word2_index).is_appear[i])
				allappear++;
		}
		//CorrAB= Nab/(Na+Nb-Nab)-(Na*Nb)/(N*N)  
		//����Լ���
		double CorrAB=word2num/(word2num+word1num-allappear)-(word1num*word2num)/(articlenum*articlenum);
		if(CorrAB<0)
		//CorrAB= Nab/(Na+Nb-Nab)
			CorrAB=allappear/(word1num+word2num-allappear);
		return CorrAB;
	}



		
	//��ֵ��
	double threshold=0.9;

	//��ʼ���ؼ�������Ӽ���
	/*
	*par1:�ؼ���
	*par2:��������
	* par3:���¼�������
	*/
	//�洢����������ؼ��ʵ������
	Map<String,Double> relevancy = new HashMap<String, Double>() ;
	void initKeywordArray(String keyword,List<segmentInfo>  segInfo,int articlenum)
	{
		for(int i=0;i<segInfo.size();i++)
		{
			double res=CalcToWordRelevancy(keyword,segInfo.get(i).word,segInfo,articlenum);
			if(res > threshold)
			{
				key_assemble.add(segInfo.get(i).word);
				relevancy.put(segInfo.get(i).word,res);
			}
		}
	}


	void CalcSubwordArray(List<segmentInfo>  segInfo,int articlenum)
	{
		for(int i=0;i<segInfo.size();i++)
		{
			//���㵥��segInfo[i].word���������ʵ������
			for(int j=i+1;j<segInfo.size();j++)
			{
				double res=CalcToWordRelevancy(segInfo.get(i).word,segInfo.get(j).word,segInfo,articlenum);
				//�������i�뵥��j������Դ��ڵ���i��ؼ��ʵ�����ԣ�����뵽���������
				if(relevancy.containsKey(segInfo.get(i).word) == true && relevancy.get(segInfo.get(i).word) < res)
					key_assemble.add(segInfo.get(i).word);
			}
		}
	}


	public void main(String[] args)
	{
		//1. ��һ����ʼ���������ݣ��ṩ����Ϊarticle[NUM];������NUMΪ10�������¼��ϴ�СΪ10��
		init();
		//2. �ִʣ��õ����зִʽ��
		word=GetSegmentation(article,NUM);
		//3. ��ʼ����������,ǰ����ǰ�����Ѿ��ɹ�
		System.out.println("word=" + word);
		System.out.println("article=" + article);
		System.out.println("segInfo=" + segInfo.isEmpty());
		GetSegmentationInof(article,NUM,word,segInfo);
		//4.���㲢�洢����������ؼ��ʵ������
		initKeywordArray(keyword,segInfo,NUM);
	    //5.���������ӵ���֮�������ԣ����������ս����key_assemble
		CalcSubwordArray(segInfo,NUM);
		
		for (String string : key_assemble) {
			System.out.println(string);
		}
		
	}
	
}

class segmentInfo
{
	public segmentInfo(){
		is_appear = new Boolean[RelationGra.NUM] ;
	}
	String word;//�ִ�����
	Boolean is_appear[];//�ִ��Ƿ��ڵ�i�������г���
};
