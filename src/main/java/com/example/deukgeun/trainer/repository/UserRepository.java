package com.example.deukgeun.trainer.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
   
  boolean existsByEmail(String email);
  
  @Modifying
  @Transactional
  @Query("update User m set m.name = :name,"
      + " m.gender = :gender,"
      + " m.postcode = :postcode,"
      + " m.jibunAddress = :jibunAddress,"
      + " m.roadAddress = :roadAddress,"
      + " m.detailAddress = :detailAddress,"
      + " m.extraAddress = :extraAddress,"
      + " m.price = :price,"
      + " m.groupStatus = :groupStatus,"
      + " m.groupName = :groupName,"
      + " m.introduction = :introduction"
      + " where m.email = :email")
  void updateInfo(
      @Param(value = "email")String email,
      @Param(value = "name")String name,
      @Param(value = "gender")Gender gender,
      @Param(value = "postcode")String postcode,
      @Param(value = "jibunAddress")String jibunAddress,
      @Param(value = "roadAddress")String roadAddress,
      @Param(value = "detailAddress")String detailAddress,
      @Param(value = "extraAddress")String extraAddress,
      @Param(value = "price")Integer price,
      @Param(value = "groupStatus")GroupStatus groupStatus,
      @Param(value = "groupName")String groupName,
      @Param(value = "introduction")String introduction
      );
  
  @Modifying
  @Transactional
  @Query("update User m set m.password = :password where m.email = :email")
  void updatePassword(@Param(value = "email")String email, @Param(value = "password")String password);

  @Transactional
  void deleteByEmail(String email);
}
