package net.sf.dframe.lulu.spring;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.drools.decisiontable.ExternalSpreadsheetCompiler;
import org.kie.api.definition.KiePackage;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class DroolsFactory4Spring {
	
    private static final Logger log = LoggerFactory.getLogger(DroolsFactory4Spring.class);

    private static final String RULES_DRL_PATH = "rules/drl/";
    private static final String RULES_DRT_PATH = "rules/drt/";
    private static  int startRow = 2;
	private static int startCol = 1;
	
	
	private KieSession kieSession;
	private StatelessKieSession statelessKieSession;
	
	private DroolsFactory4Spring(){
		kieSession = createSession();
		statelessKieSession = createStatelessSession();
	}
	
	private static DroolsFactory4Spring instance = new DroolsFactory4Spring ();
	
	
	public static DroolsFactory4Spring getInstance() {
		return instance;
	}
	
	
	public KieSession getKieSession() {
		return kieSession;
	}
	
	public StatelessKieSession getStatelessKieSession() {
		return statelessKieSession;
	}
	
	public void reload () {
		if (kieSession == null)
			kieSession.destroy();
		kieSession = null;
		statelessKieSession = null;
		kieSession = createSession();
		statelessKieSession = createStatelessSession();
	}
	
	private  KieSession createSession() {
	        KieSession kieSession = null;
	        try {
	            KnowledgeBuilder builder = getKieBuilder();
	            //load rules
	        	loadRules(builder);
	        	//load template rules
	        	loadTemplateRules(builder);
	            InternalKnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
	            Collection<KiePackage> packages = builder.getKnowledgePackages();
	            knowledgeBase.addPackages(packages);
	            kieSession = knowledgeBase.newKieSession();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return kieSession;
	    }
	    
	    
	  private StatelessKieSession createStatelessSession() {
	    	StatelessKieSession kieSession = null;
	        try {
	            KnowledgeBuilder builder = getKieBuilder();
	            //load rules
	        	loadRules(builder);
	        	//load template rules
	        	loadTemplateRules(builder);
	            InternalKnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
	            Collection<KiePackage> packages = builder.getKnowledgePackages();
	            knowledgeBase.addPackages(packages);
	            kieSession = knowledgeBase.newStatelessKieSession();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return kieSession;
	    }

	  private KnowledgeBuilder getKieBuilder() throws IOException {
	    	KnowledgeBuilder kieBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();; 
	        return kieBuilder;
	    }
	    
	    /**
	     * 加载规则
	     * @param builder
	     * @throws IOException
	     */
		private void loadRules(KnowledgeBuilder builder) throws IOException {
			for (Resource file : getRuleFiles()) {
				builder.add(ResourceFactory.newFileResource(file.getFile()),ResourceType.DRL);
			}
		}
		
		/**
		 * 加载 模板规则
		 * @param builder
		 * @throws IOException 
		 */
		private void loadTemplateRules (KnowledgeBuilder builder) throws IOException {
			ExternalSpreadsheetCompiler converter = new ExternalSpreadsheetCompiler();
			
			Map<Resource,Resource> resources = getTempRuleFiles();
			for (Resource r: resources.keySet()) {
				String rule = converter.compile(r.getInputStream(), resources.get(r).getInputStream(), startRow, startCol);
				builder.add(ResourceFactory.newByteArrayResource(rule.getBytes("UTF-8")), ResourceType.DRL);
				log.info("load template : "+r.getURL());
				log.info("load rule: "+rule);
			}
		}
	    
	    /**
	     * 读取规则资源
	     */
	    private Resource[] getRuleFiles() throws IOException {
	        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	        return resourcePatternResolver.getResources("classpath*:" + RULES_DRL_PATH + "**/*.*");
	    }
	    
	    /**
	       * 读取模板规则资源
	     */
	    private Map<Resource,Resource> getTempRuleFiles() throws IOException {
	    	Map<Resource,Resource> resources = new HashMap<Resource,Resource>();
	        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	       
	        Resource[] drts =  resourcePatternResolver.getResources("classpath*:" + RULES_DRT_PATH + "**/*.drt");
	        Resource[] xls =  resourcePatternResolver.getResources("classpath*:" + RULES_DRT_PATH + "**/*.xls");
	        for (Resource r:xls) {
	        	resources.put(r, findResourceByName(drts,r.getFilename().replace(".xls", ".drt")));
	        }
	        return resources;
	    }
	    
	    /**
	     * 查找
	     * @param resources
	     * @param filename
	     * @return
	     */
	    private Resource findResourceByName(Resource[] resources,String filename) {
	    	for (Resource r: resources) {
	    		if ( r.getFilename().equals(filename))
	    			return r;
	    	}
	    	return null;
	    }
}
