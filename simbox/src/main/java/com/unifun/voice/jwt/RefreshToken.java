package com.unifun.voice.jwt;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;


@Path("/refresh")
public class RefreshToken {
    @POST
    public Response refreshToken(String req) {
        System.out.println("req:");
        System.out.println(req);
        String token = "";
        if(LoginUser.getRefreshTokenInstance().containsValue(token)){
            return null;

        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Logout").build();
        }
    }
}
/*
app.post('/refresh', function (req, res) {
    const refreshToken = req.body.refreshToken;


    if (refreshToken in refreshTokens) {
      const user = {
        'username': refreshTokens[refreshToken],
        'role': 'admin'
      }
      const token = jwt.sign(user, SECRET, { expiresIn: 600 });
      res.json({jwt: token})
    }
    else {
      res.sendStatus(401);
    }
});
 */