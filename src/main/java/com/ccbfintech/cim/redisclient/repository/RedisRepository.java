package com.ccbfintech.cim.redisclient.repository;

import com.ccbfintech.cim.redisclient.model.Cstinfo;
import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<Cstinfo, String> {

}