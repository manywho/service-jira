package com.manywho.services.jira;

import com.manywho.sdk.services.ServiceApplication;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class Service extends ServiceApplication {
    public Service() {
        this.setModule(new ServiceModule());
        this.initialize();
    }

    public static void main(String[] args) throws Exception {
        Service service = new Service();
        service.setModule(new ServiceModule());
        service.startServer("/api/jira/2");
    }
}
