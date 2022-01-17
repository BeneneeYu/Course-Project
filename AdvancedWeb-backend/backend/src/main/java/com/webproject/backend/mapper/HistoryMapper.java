package com.webproject.backend.mapper;

import com.webproject.backend.entity.MessageHistory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface HistoryMapper {

    @Insert("insert into history (time,userId,messageType,message) values(#{time},#{userId},#{messageType},#{message})")
    void addHistory(MessageHistory messageHistory);

}
