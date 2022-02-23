package tech.jhipster.myappflyway.user.infrastructure.secondary.postgresql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.filter;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import tech.jhipster.myappflyway.IntegrationTest;

@IntegrationTest
@Transactional
class UserJpaRepositoryIT {

  private static final String ACTIVATION_KEY = "my_activation_key";
  private static final String RESET_KEY = "my_reset_key";
  private static final String LOGIN = "beer";
  private static final String EMAIL = LOGIN + "@localhost";

  @Autowired
  AuthorityJpaRepository authorityJpaRepository;

  @Autowired
  UserJpaRepository userJpaRepository;

  @BeforeEach
  void createUser() {
    AuthorityEntity authorityEntity = authorityJpaRepository.save(new AuthorityEntity().name("ROLE_BEER"));
    UserEntity userEntity = getUserEntity().authorities(Set.of(authorityEntity));
    userJpaRepository.save(userEntity);
  }

  @Test
  void shouldFindOneByActivationKey() {
    UserEntity userEntity = userJpaRepository.findOneByActivationKey(ACTIVATION_KEY).orElse(null);
    assertUserEntity(userEntity);
  }

  @Test
  void shouldFindAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore() {
    List<UserEntity> userEntityList = userJpaRepository.findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(
      Instant.parse("2022-02-22T10:11:12.000Z")
    );
    assertThat(userEntityList).isNotEmpty();
    assertThat(filter(userEntityList).with("login").equalsTo(LOGIN).get()).isNotEmpty();
  }

  @Test
  void shouldFindOneByResetKey() {
    UserEntity userEntity = userJpaRepository.findOneByResetKey(RESET_KEY).orElse(null);
    assertUserEntity(userEntity);
  }

  @Test
  void shouldFindOneByEmailIgnoreCase() {
    UserEntity userEntity = userJpaRepository.findOneByEmailIgnoreCase(EMAIL).orElse(null);
    assertUserEntity(userEntity);
  }

  @Test
  void shouldFindOneByLogin() {
    UserEntity userEntity = userJpaRepository.findOneByLogin(LOGIN).orElse(null);
    assertUserEntity(userEntity);
  }

  @Test
  void shouldFindOneWithAuthoritiesByLogin() {
    UserEntity userEntity = userJpaRepository.findOneWithAuthoritiesByLogin(LOGIN).orElse(null);
    assertUserEntity(userEntity);
  }

  @Test
  void shouldFindOneWithAuthoritiesByEmailIgnoreCase() {
    UserEntity userEntity = userJpaRepository.findOneWithAuthoritiesByEmailIgnoreCase(EMAIL).orElse(null);
    assertUserEntity(userEntity);
  }

  @Test
  void shouldFindAllByIdNotNullAndActivatedIsTrue() {
    UserEntity userEntity = getUserEntity().activated(true).login("beer_disabled").email("beer_disabled@localhost");
    userJpaRepository.save(userEntity);
    assertThat(
      filter(userJpaRepository.findAllByIdNotNullAndActivatedIsTrue(Pageable.ofSize(10)).getContent())
        .with("login")
        .equalsTo("beer_disabled")
        .get()
    )
      .isNotEmpty();
  }

  private void assertUserEntity(UserEntity userEntity) {
    assertThat(userEntity).isNotNull();
    assertThat(userEntity.getLogin()).isEqualTo(LOGIN);
    assertThat(userEntity.getAuthorities()).isNotEmpty();
  }

  private UserEntity getUserEntity() {
    UserEntity userEntity = new UserEntity()
      .activated(false)
      .activationKey(ACTIVATION_KEY)
      .email(EMAIL)
      .login(LOGIN)
      .password("C&At!Ev4&Y7&-CvuGWnyqpmEk*3_w28%7%&df$uFQXTjqzEEwM-v34XW!na6")
      .resetDate(Instant.parse("2022-01-24T10:11:12.000Z"))
      .resetKey(RESET_KEY);

    userEntity
      .lastModifiedBy("modifier")
      .lastModifiedDate(Instant.parse("2022-01-23T10:11:12.000Z"))
      .createdBy("creator")
      .createdDate(Instant.parse("2022-01-22T10:11:12.000Z"));
    return userEntity;
  }
}
