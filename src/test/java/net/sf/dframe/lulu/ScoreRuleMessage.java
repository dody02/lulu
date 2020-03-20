package net.sf.dframe.lulu;

import net.sf.dframe.lulu.dr.RuleMessage;

/**
 * 打分规则
 * @author Dody
 *
 */
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
