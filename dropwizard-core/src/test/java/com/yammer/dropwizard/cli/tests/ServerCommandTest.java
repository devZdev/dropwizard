package com.yammer.dropwizard.cli.tests;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.cli.ServerCommand;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import org.codehaus.jackson.annotate.JsonProperty;
import org.junit.Test;

import java.io.File;
import java.net.URI;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ServerCommandTest {
    public static class MockConfiguration extends Configuration {
        @JsonProperty
        private String saying;

        public String getSaying() {
            return saying;
        }
    }
    
    public static class MockService extends Service<MockConfiguration> {
        protected MockService(String name) {
            super(name);
        }

        @Override
        protected void initialize(MockConfiguration configuration, 
                                  Environment environment) throws Exception {
        }
    }
    
    private final String confPath = 
            new File(getConfigURI("example.yml")).getAbsolutePath();
    
    private final ServerCommand serverCommand = 
            new ServerCommand(MockConfiguration.class);
    
    @Test
    public void runs() throws Exception {
        final Service<MockConfiguration> service = new MockService("test");
        Runnable server = new Runnable() {
            public void run() {
                try {
                    serverCommand.run(service, new String[] { confPath });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Thread runner = new Thread(server);
        
        runner.start();
        Thread.sleep(5000);

        // check the runner Thread didn't die
        assertThat(runner.isAlive(), is(true));

        // interrupt the runner Thread, should stop the Server
        runner.interrupt();
        runner.join(10000);

        // check that the runner Thread has stopped
        assertThat(runner.isAlive(), is(false));
        
    }
    
    private URI getConfigURI(String name) throws RuntimeException {
        try {
            return this.getClass().getClassLoader().getResource(name).toURI();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
