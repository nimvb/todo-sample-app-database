package com.nimvb.app.database.document;

import com.nimvb.app.database.exception.KeyAlreadyExistsException;
import com.nimvb.app.database.exception.KeyNotFoundException;

import java.util.Collection;

public interface Document<TDomain,TId> {

    /**
     * Inserts the domain object to the storage. If the <code>domain</code> key is null, then a new key will
     * be obtained from the sequence generator
     *
     * @param domain the domain object
     * @return the wrapped persisted domain object
     * @throws KeyAlreadyExistsException thrown when the key is already exists.
     */
    TDomain insert(TDomain domain) throws KeyAlreadyExistsException;

    /**
     * Inserts the domain object to the storage. If the <code>domain</code> key is null, then a new key will
     * be obtained from the sequence generator
     *
     * @param domain the domain object
     * @param wrap wrap the persisted object or not
     * @return the persisted domain object
     * @throws KeyAlreadyExistsException thrown when the key is already exists.
     */
    TDomain insert(TDomain domain,boolean wrap) throws KeyAlreadyExistsException;

    /**
     * Updates the existing domain object.
     *
     * @param key the key of existing domain
     * @param domain the domain object to be replaced by th existing one
     * @return the wrapped updated domain object
     * @throws KeyNotFoundException thrown when the key is not exists
     */
    TDomain update(TId key,TDomain domain) throws KeyNotFoundException;

    /**
     * Updates the existing domain object.
     *
     * @param key the key of existing domain
     * @param domain the domain object to be replaced by th existing one
     * @param wrap wrap the persisted object or not
     * @return the updated domain object
     * @throws KeyNotFoundException thrown when the key is not exists
     */
    TDomain update(TId key,TDomain domain,boolean wrap) throws KeyNotFoundException;

    /**
     * Find the target domain.
     *
     * @param key the target domain key
     * @return the wrapped domain object or null if not exist
     */
    TDomain find(TId key);

    /**
     * Fetch the actual existing domain
     *
     * @param key the target domain key
     * @return the actual domain object or null if not exist
     */
    TDomain fetch(TId key);

    /**
     * Fetch all records
     *
     * @return list of all domain objects which are wrapped or cloned
     */
    Collection<TDomain> all();

    /**
     * Delete the existing domain object. On the condition where the target domain object is not exists, nothing
     * will be deleted
     *
     * @param key key of the target entity
     * @throws NullPointerException if the <code>key</code> is null
     */
    void delete(TId key) throws NullPointerException;

    /**
     * Calculate the records count in the storage
     *
     * @return the records count
     */
    Integer count();
}

