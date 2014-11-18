package cn.xjtuse.junit;

import org.junit.Test;

import cn.xjtuse.classify.svm.Trainer;

public class TrainerTest {

	@Test
	public void test() {
		Trainer trainer = new Trainer() ;
		trainer.doTrain() ;
	}

}
