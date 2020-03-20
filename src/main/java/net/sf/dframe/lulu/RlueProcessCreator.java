package net.sf.dframe.lulu;

import net.sf.dframe.lulu.dr.DroolsFactory;
import net.sf.dframe.lulu.dr.RlueProcess;
/**
 * 规则处理器构建
 * @author dy02
 *
 */
public class RlueProcessCreator {
	
	/**
	 * 重新设置规则文件路径并获取处理实例
	 * @param drtDirPath
	 * @param drlDirPath
	 * @return
	 */
	public static RlueProcess resetRuleProcess (String drtDirPath,String drlDirPath) {
		if (drlDirPath != null)
			DroolsFactory.setRulesDrlDirPath(drlDirPath);
		if (drtDirPath != null)
			DroolsFactory.setRulesDrtDirPath(drtDirPath);
		return createRlueProcess();
	}
	
	/**
	 * 获取一个规则处理器
	 * @return
	 */
	public static RlueProcess createRlueProcess () {
		RlueProcess rp = new RlueProcess();
		rp.setkSession(DroolsFactory.getInstance().getKieSession());
		rp.setStatelessKieSession(DroolsFactory.getInstance().getStatelessKieSession());
		return rp;
	}
	
	/**
	 * 重新加载规则处理器
	 * @return
	 */
	public static RlueProcess reloadAndCreateRlueProcess () {
		RlueProcess rp = new RlueProcess();
		DroolsFactory.getInstance().reload();
		rp.setkSession(DroolsFactory.getInstance().getKieSession());
		rp.setStatelessKieSession(DroolsFactory.getInstance().getStatelessKieSession());
		return rp;
	}
	
}
