package net.sf.dframe.lulu.dr;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 处理方法
 * @author Dody
 *
 */
@Service
public class RlueProcess {
	
	@Autowired
	private KieSession kSession;
	
	@Autowired
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
