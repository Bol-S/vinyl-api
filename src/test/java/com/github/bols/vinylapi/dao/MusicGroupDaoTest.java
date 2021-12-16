package com.github.bols.vinylapi.dao;

import com.github.bols.vinylapi.model.Artist;
import com.github.bols.vinylapi.model.MusicGroup;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MusicGroupDaoTest {

    @Autowired
    MusicGroupDao musicGroupDao;

    @Test
    void testFindAll() {
        List<MusicGroup> groups = (List<MusicGroup>) musicGroupDao.findAll();

        assertNotNull(groups);
        assertEquals(2, groups.size());
        assertEquals("Onyx", groups.get(0).getName());
        assertEquals(2, groups.get(1).getMiembros().size());
    }

    @Test
    void testFindById() {
        MusicGroup musicGroup = musicGroupDao.findById(2).orElseThrow();

        assertNotNull(musicGroup);
        assertEquals("Das Efx", musicGroup.getName());
    }

    @Test
    void testFindByName() {
        MusicGroup musicGroup = musicGroupDao.findByName("Das Efx").orElseThrow();

        assertNotNull(musicGroup);
        assertEquals(2, musicGroup.getId());
        assertEquals("Das Efx", musicGroup.getName());
    }


    @Order(1)
    @Test
    void testSave() {
        MusicGroup musicGroup = new MusicGroup();
        musicGroup.setName("A Tribe Called Quest");

        MusicGroup savedMusicGroup = musicGroupDao.save(musicGroup);

        assertNotNull(savedMusicGroup);
        assertEquals(3, savedMusicGroup.getId());
        assertEquals("A Tribe Called Quest", savedMusicGroup.getName());
    }

    @Order(2)
    @Test
    void testUpdate() {
        MusicGroup musicGroup = new MusicGroup();
        musicGroup.setName("Lord of the Underground");

        musicGroupDao.save(musicGroup);

        musicGroup.setName("Lords of the Underground");
        musicGroupDao.save(musicGroup);

        assertNotNull(musicGroup);
        assertEquals(4, musicGroup.getId());
        assertEquals("Lords of the Underground", musicGroup.getName());
    }

    @Order(3)
    @Test
    void testDelete() {
        MusicGroup musicGroup = new MusicGroup();
        musicGroup.setName("Mobb Deep");

        musicGroupDao.save(musicGroup);

        musicGroupDao.delete(musicGroup);

        assertThrows(NoSuchElementException.class, () -> musicGroupDao.findByName("Mobb Deep").orElseThrow());
    }
}