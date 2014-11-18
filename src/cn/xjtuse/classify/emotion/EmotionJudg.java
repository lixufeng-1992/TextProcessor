package cn.xjtuse.classify.emotion;

import cn.xjtuse.pretreat.ChineseSplit;

public class EmotionJudg {
	private float calaWeight(String words[]){ // words = {"����","��","�е�","��","����"} ;
		float weight = (float)0.0 ;
		float posWeight = (float) 0.0 ;
		float negWeight = (float) 0.0 ;
		for (int i = 0; i < words.length; i++) {
			//System.out.print(words[i] + "  ");
			//System.out.println("i=" + i);
			if (DictConstrucion.posSet.contains(words[i])) {  //��ǰ����POSITIVE
				//System.out.print("1" + DictConstrucion.posSet.contains(words[i]) + "  ");
				posWeight += WeightLevel.LEVLE_POS ;    //Ȩ�ؼ�1
				if(i > 0){
					if(DictConstrucion.heavyDegreeSet.contains(words[i - 1])){  
						//System.out.print("2" + DictConstrucion.heavyDegreeSet.contains(words[i - 1]) + "  "); //ǰ׺����Ҫ�̶ȴ�
						posWeight *= WeightLevel.LEVEL_HEAVY ;
						if(i > 1){
							
						}
					}else if (DictConstrucion.lightDegreeSet.contains(words[i - 1])) {
						//System.out.print("3" + DictConstrucion.lightDegreeSet.contains(words[i - 1]) + "  ");//ǰ׺����ȳ̶ȴ�
						posWeight *= WeightLevel.LEVEL_LIGHT ;
						if(i > 1){
							
						}
					}else if (DictConstrucion.negPrefixSet.contains(words[i - 1])) { //ǰ׺�Ƿ񶨴�
						//System.out.print("4" + DictConstrucion.negPrefixSet.contains(words[i - 1]) + "  ");
						posWeight *= -1 ;   //���з񶨴ʣ����Ա䷴
						if(i > 1){
							if(DictConstrucion.negPrefixSet.contains(words[i - 2])){ //����˫�ط񶨣����Իָ�
								//System.out.print("5" + DictConstrucion.negPrefixSet.contains(words[i - 2]) + "  ");
								posWeight *= -1 ;
							}
						}
					}
				}
			}else if (DictConstrucion.negSet.contains(words[i])) { //��ǰ�ʸ����
				//System.out.print("6" + DictConstrucion.negSet.contains(words[i]) + "  ");
				negWeight += WeightLevel.LEVEL_NEG;
				if(i > 0){
					if(DictConstrucion.heavyDegreeSet.contains(words[i - 1])){
						//System.out.print("7" + DictConstrucion.heavyDegreeSet.contains(words[i - 1])  + "  ");  //ǰ������Ҫ�̶ȴ�
						negWeight *= WeightLevel.LEVEL_HEAVY ;
					}else if (DictConstrucion.lightDegreeSet.contains(words[i - 1])) {
						//System.out.println("8" + DictConstrucion.lightDegreeSet.contains(words[i - 1]) + "  "); //ǰ������ȳ̶ȴ�
						negWeight *= WeightLevel.LEVEL_LIGHT ;
					}else if (DictConstrucion.negPrefixSet.contains(words[i - 1])) {
						negWeight *= -1 ;
					}
				}
			}else {//������дʣ�ʲô������
				
			}
		}
		//System.out.println("posWeight=" + posWeight) ;
		//System.out.println("negWeight=" + negWeight) ;
		weight = posWeight + negWeight ;
		return weight ;
	}
	
	
	public String judgPolarity(String sentence){
		String[] words = ChineseSplit.splitChinese(sentence) ;
		float weight = calaWeight(words) ;
		System.out.println("weight=" + weight) ;
		if(weight > 0){
			return "POSIVIVE" ;
		}else if (weight < 0) {
			return "NEGTIVE" ;
		}else {
			return "UNKNOWN" ;
		}
	}
	
	public static void main(String args[]){
		System.out.println(new EmotionJudg().judgPolarity("�������Ǻܸ���"));  //����
		System.out.println(new EmotionJudg().judgPolarity("����ܲ�����")) ; //����  ����  ���� //����
		System.out.println(new EmotionJudg().judgPolarity("���첻�û�")); //����(�����ǰ����ǰ׺)
		System.out.println(new EmotionJudg().judgPolarity("����û�����"));  //����
		
		System.out.println(new EmotionJudg().judgPolarity("���첻�ǲ�����"));  //˫�ط񶨣�����
	}
}
