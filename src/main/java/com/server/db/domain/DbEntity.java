package com.server.db.domain;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data base entry. Basically, each entity should implement this interface " +
        "to provide method for checking weather user has enough right to manage with this object or not.")
public interface DbEntity {

    @Operation(description = "check weather a user has enough rights to update this entity object")
    boolean checkPrivacy(User user);
}
