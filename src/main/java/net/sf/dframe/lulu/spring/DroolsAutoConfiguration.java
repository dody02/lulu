package net.sf.dframe.lulu.spring;


import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  
 * @author Dody
 *
 */
@Configuration
public class DroolsAutoConfiguration  {
    
	

    
    @Bean(name="kieSession")
    @ConditionalOnMissingBean(KieSession.class)
    public  KieSession getSession() {
       
        return DroolsFactory4Spring.getInstance().getKieSession();
    }
    
    
    @Bean(name="statelessKieSession")
    @ConditionalOnMissingBean(StatelessKieSession.class)
    public  StatelessKieSession getStatelessSession() {
    	return DroolsFactory4Spring.getInstance().getStatelessKieSession();
    }


    
}
