package net.sf.dframe.lulu.dr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
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

public class DroolsFactory {
	
    private static final Logger log = LoggerFactory.getLogger(DroolsFactory.class);

    private static final String RULES_DRL_PATH = "rules/drl/";
    private static final String RULES_DRT_PATH = "rules/drt/";
    
    private static final  String FILE_DRT = ".drt";
    private static final  String FILE_XLS = ".xls";
    
    private static  int startRow = 2;
	private static int startCol = 1;
	
	
	private static String drlDir = RULES_DRL_PATH;
	private static String drtDir = RULES_DRT_PATH;
	
	
	private KieSession kieSession;
	private StatelessKieSession statelessKieSession;
	
	private DroolsFactory(){
		kieSession = createSession();
		statelessKieSession = createStatelessSession();
	}
	
	private static DroolsFactory instance = new DroolsFactory();
	
	
	/**
	 * set drt path before getInstance()
	 * @param drlDirPath
	 */
	public static void setRulesDrtDirPath(String drtDirPath) {
		drtDir = drtDirPath;
	}
	
	/**
	 * set drl path before getInstance()
	 * @param drlDirPath
	 */
	public static void setRulesDrlDirPath(String drlDirPath) {
		drlDir = drlDirPath;
	}
	
	
	public static  DroolsFactory getInstance() {
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
			for (File file : getRuleFiles()) {
				builder.add(ResourceFactory.newFileResource(file),ResourceType.DRL);
			}
		}
		
		/**
		 * 加载 模板规则
		 * @param builder
		 * @throws IOException 
		 */
		private void loadTemplateRules (KnowledgeBuilder builder) throws IOException {
			ExternalSpreadsheetCompiler converter = new ExternalSpreadsheetCompiler();
			
			Map<File,File> resources = getTempRuleFiles();
			for (File xlsFile: resources.keySet()) {
				String rule = converter.compile(new FileInputStream(xlsFile), new FileInputStream(resources.get(xlsFile)), startRow, startCol);
				builder.add(ResourceFactory.newByteArrayResource(rule.getBytes("UTF-8")), ResourceType.DRL);
				log.info("load template : "+xlsFile.getAbsolutePath());
				log.info("load rule: "+rule);
			}
		}
	    
	    /**
	     * 读取规则资源
	     */
	    private File[] getRuleFiles() throws IOException {
	    	File dir = new File(drlDir);
	    	return dir.listFiles();
	    }
	    
	    /**
	       * 读取模板规则资源
	     */
	    private Map<File,File> getTempRuleFiles() throws IOException {
	    	Map<File,File> resources = new HashMap<File,File>();
	       
	        
			File[] drts =  getResources( drtDir , FILE_DRT);
	        File[] xls =  getResources(drtDir ,FILE_XLS);
	        for (File xlsFile:xls) {
	        	
				resources.put(xlsFile, findResourceByName(drts,xlsFile.getName().replace(FILE_XLS, FILE_DRT)));
	        }
	        return resources;
	    }
	    
	    /**
	     * 
	     * @param dirPath
	     * @param patter  后缀名
	     * @return
	     */
	    private File[] getResources(String dirPath, final String pattern) {
			File dir = new File(dirPath);
			return dir.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(pattern);
				}});
		}


		/**
	     * 查找
	     * @param resources
	     * @param filename
	     * @return
	     */
	    private File findResourceByName(File[] resources,String filename) {
	    	for (File r: resources) {
	    		if ( r.getName().equals(filename))
	    			return r;
	    	}
	    	return null;
	    }
}
