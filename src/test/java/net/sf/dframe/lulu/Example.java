package net.sf.dframe.lulu;

import net.sf.dframe.lulu.dr.RlueProcess;

/**
 * Unit test for simple App.
 */
public class Example {
	public static void main(String[] args) {
		
		RlueProcess rp = RlueProcessCreator.resetRuleProcess("C:\\eclipse-workspace\\lulu\\rules\\drt", null);
//		RlueProcess rp = RlueProcessCreator.createRlueProcess();
		ScoreRuleMessage message = new ScoreRuleMessage();
		message.setAge(4);
		message.setScore(70);
		message.setType("跟读");
		rp.StatelessProcess(message );
		
		System.out.println(message.getStart());
		System.out.println(message.getType());
		System.out.println(message.getTip());
		System.out.println(message.getWrongWordTips());
		
	}
}
