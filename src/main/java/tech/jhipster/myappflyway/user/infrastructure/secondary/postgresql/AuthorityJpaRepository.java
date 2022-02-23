package tech.jhipster.myappflyway.user.infrastructure.secondary.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link AuthorityEntity} entity.
 */
public interface AuthorityJpaRepository extends JpaRepository<AuthorityEntity, String> {}
