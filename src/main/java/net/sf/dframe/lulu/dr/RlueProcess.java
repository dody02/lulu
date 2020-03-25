package net.sf.dframe.lulu.dr;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
/**
 * 处理方法
 * @author Dody
 *
 */
public class RlueProcess {
	
	private KieSession kSession;
	
	private StatelessKieSession statelessKieSession;
	
	public void StatefulProcess (RuleMessage message) {
		kSession.insert(message);
		kSession.fireAllRules();
	}
	
	public void StatelessProcess (RuleMessage message) {
		statelessKieSession.execute(message);
	}
	
	public KieSession getkSession() {
		return kSession;
	}
	public void setkSession(KieSession kSession) {
		this.kSession = kSession;
	}
	public StatelessKieSession getStatelessKieSession() {
		return statelessKieSession;
	}
	public void setStatelessKieSession(StatelessKieSession statelessKieSession) {
		this.statelessKieSession = statelessKieSession;
	}
}
