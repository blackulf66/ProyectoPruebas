package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AlbumRepositoryTestU {
    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void test_NotNull(){ assertNotNull(albumRepository);}
    /*
    * @Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
	Page<Album> findByCreatedBy(Long userId, Pageable pageable);
}
*/
    @Test
    void test_albumRepository_success(){

        Album album = new Album();
        album.setTitle("flying grayson");
        album.setUpdatedAt(Instant.now());
        album.setCreatedAt(Instant.now());

        testEntityManager.persist(album);

        User user = new User();
        user.setFirstName("Dick");
        user.setLastName("Grayson");
        user.setUsername("The Robin");
        user.setPassword("batmanisnotbrucewayne");
        user.setEmail("therobin@gmail.com");
        user.setUpdatedAt(Instant.now());
        user.setCreatedAt(Instant.now());

        testEntityManager.persist(user);


        Page<Album> albums = new PageImpl<>(Arrays.asList(album));
        PageRequest pageRequest = PageRequest.of(1, 2);

        assertEquals(0, albumRepository.findByCreatedBy(album.getId(), pageRequest).getTotalElements());

    }


}