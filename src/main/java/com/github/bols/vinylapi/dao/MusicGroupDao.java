package com.github.bols.vinylapi.dao;

import com.github.bols.vinylapi.model.MusicGroup;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MusicGroupDao extends CrudRepository<MusicGroup, Integer> {

    Optional<MusicGroup> findByName(String name);
}
