package com.server.db.domain;

/**
 * Database entry. Basically, each entity should implement this interface
 * to provide method for checking weather user has enough right to manage with
 * this object or not
 */
public interface DbEntity {

    /**
     * Check weather a user has enough rights to update this entity object
     */
    boolean checkPrivacy(User user);
}
