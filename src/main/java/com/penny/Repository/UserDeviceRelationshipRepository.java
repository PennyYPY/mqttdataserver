package com.penny.Repository;

import com.penny.domain.UserDeviceRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.integration.dsl.jpa.Jpa;

import java.util.List;

/**
 * Created by Administrator on 2017/12/19.
 */
public interface UserDeviceRelationshipRepository extends JpaRepository<UserDeviceRelationship,String>{

    List<UserDeviceRelationship> findUserDeviceRelationshipsBySnCodeAndIsUsed(String snCode,Integer isUsed);

    List<UserDeviceRelationship> searchUserName(String snCode, Integer isUsed);

}
