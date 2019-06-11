package com.unifun.voice.jwt;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/logout")
public class LogoutUser {
    @POST
    public Response logoutUser(String req) {
        System.out.println("logout-ep:");
        System.out.println(req);
        String token = ""; // req.body refresh token
        if(LoginUser.getRefreshTokenInstance().containsValue(token)) {
            LoginUser.getRefreshTokenInstance().entrySet()
                    .removeIf(
                            entry -> (token
                                    .equals(entry.getValue())));

        }
        return Response.status(Response.Status.NO_CONTENT).entity("Logout").build();
    }
}
