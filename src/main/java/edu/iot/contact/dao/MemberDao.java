package edu.iot.contact.dao;

import java.util.List;

import edu.iot.contact.model.Member;

public interface MemberDao {
	int getCount() throws Exception;
	
	List<Member> selectList(int start, int end) throws Exception;
	
	Member selectOne(String userId) throws Exception;
	
	int insert(Member member) throws Exception;
	
	int update(Member member) throws Exception;
	
	int delete(String userId) throws Exception;
}
