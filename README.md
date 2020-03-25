# lulu
simple way to use drools rule 

#start 
#1. set the rule in xls file like the score.xls in the rules/drt 

1	跟读	0	10	3	5	0	这次没读好不要泄气，下次重新再来。;只要仔细听，认真读，会越来越棒的！;敢读就行，加油！;	%1$s的读音注意下哦
2	跟读	11	30	3	5	1	小朋友还不错哦，继续加油！;离成功还差一点点，要多加练习！;不错哦，能听又能读。;	%1$s的读音注意下哦
3	跟读	31	50	3	5	2	好可惜呀，就差一点点了。;你表现得不错哦，再继续加油！;棒棒哒，努力会读得更好。;	%1$s的读音注意下哦
4	跟读	51	70	3	5	3	真棒，继续保持好成绩哦。;你表现得真棒，继续努力！;非常好，读得很清！;	%1$s的读音注意下哦


#2. edit drt file in drt file like the score.drt in the rules/drt
template header
id
type
score_from
score_to
age_from
age_to
star
tips
tips_wrong_word

package com.template;

import net.sf.dframe.lulu.ScoreRuleMessage;

template "score-rules"

rule "score_rules_@{row.rowNumber}"
no-loop true
when
       $s: ScoreRuleMessage(
        type == "@{type}" ,
       	score >= @{score_from} ,
       	score <=  @{score_to} , 
      	age >= @{age_from},
       	age <=  @{age_to}
       );
     
then

       $s.setType("@{type}");
       $s.setStart( @{star});
       $s.setTip("@{tips}");
       $s.setWrongWordTips("@{tips_wrong_word}");

end
end template
# java code
public class ScoreRuleMessage implements RuleMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String type;
	
	private int score;
	
	private int age;
	
	private String node;
	
	private int start;
	
	private String wrongWordTips;
	
	private String tip;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String getWrongWordTips() {
		return wrongWordTips;
	}

	public void setWrongWordTips(String wrongWordTips) {
		this.wrongWordTips = wrongWordTips;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
}

# use
  RlueProcess rp = RlueProcessCreator.createRlueProcess();
		ScoreRuleMessage message = new ScoreRuleMessage();
		message.setAge(4);
		message.setScore(70);
		message.setType("跟读");
		rp.StatelessProcess(message );
		
		System.out.println(message.getStart());
		System.out.println(message.getType());
		System.out.println(message.getTip());
		System.out.println(message.getWrongWordTips());
