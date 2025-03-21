package io.cote.chatdm.oracle.repository;

import io.cote.chatdm.oracle.Oracle;

import java.util.List;
import java.util.Set;

/**
 * A store for {@link Oracle}s. Oracle names should be case-insensitive.
 */
public interface OracleRepository {

    Set<String> findAllNames();
    Oracle findByName(String name);
    boolean existsByName(String name);
    List<Oracle> findAll();
}
