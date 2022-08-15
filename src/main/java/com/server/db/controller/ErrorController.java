package com.server.db.controller;

import com.server.db.annotations.NoOuterAccess;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Hidden
public class ErrorController {

    @NoOuterAccess
    @RequestMapping(value = "/accessDenied",
            method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, produces = "text/plain")
    public String handleAccessDenied() {
        return "access denied";
    }

    @NoOuterAccess
    @RequestMapping(value = "/error",
            method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, produces = "text/plain")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleError() {
        return "unknown request";
    }

    @NoOuterAccess
    @RequestMapping(value = "/noUserId",
            method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, produces = "text/plain")
    public String handleNoUserId() {
        return "connect please [send 'visitor' or your 'login & password' to '/connect']";
    }
}
