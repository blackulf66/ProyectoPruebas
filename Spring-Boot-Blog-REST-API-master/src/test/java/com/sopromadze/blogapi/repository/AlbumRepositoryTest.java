package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.user.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AlbumRepositoryTest {

    @Autowired
    private AlbumRepository albumRepository;

    @Test
    void test_NotNull(){ assertNotNull(albumRepository);}

    @Test
    void test_commentrepository() {

        Album album = new Album();
        album.setTitle("titulon");
        album.setId(1L);

        User user = new User();
        user.setId(1L);

        Page<Album> albums = new PageImpl<>(Arrays.asList(album));
        PageRequest pageRequest = PageRequest.of(1, 2);

        Mockito.lenient().when(albumRepository.findByCreatedBy(user.getId(), pageRequest )).thenReturn(albums);
        assertNotEquals(0, albumRepository.findByCreatedBy(user.getId(), pageRequest).getTotalElements());

    }
}