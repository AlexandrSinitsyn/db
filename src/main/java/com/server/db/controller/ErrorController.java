package com.server.db.controller;

import com.server.db.annotations.NoOuterAccess;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController {

    @NoOuterAccess
    @RequestMapping(value = "/accessDenied", method = {RequestMethod.GET, RequestMethod.POST}, produces = "text/plain")
    public String handleAccessDenied() {
        return "access denied";
    }

    @NoOuterAccess
    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST}, produces = "text/plain")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleError() {
        return "unknown request";
    }

    @NoOuterAccess
    @RequestMapping(value = "/noUserId", method = {RequestMethod.GET, RequestMethod.POST}, produces = "text/plain")
    public String handleNoUserId() {
        return "connect please [send 'visitor' or your 'login & password' to '/connect']";
    }
}
