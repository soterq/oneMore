package com.unifun.voice.endpoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/simboxlist")
public class SimBoxList {
    @GET
    public String doGet() {
        return "[{\"id\":1,\"name\":\"simbox1\"},{\"id\":12,\"name\":\"simbox2\"}]";
    }
}