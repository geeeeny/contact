package edu.iot.contact.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.iot.contact.model.Member;
import edu.iot.lib.db.ConnectionProvider;

public class MemberDaoImp1 implements MemberDao {

	Connection conn;
	
	public MemberDaoImp1() {
		conn = ConnectionProvider.getConenction();
	}

	@Override
	public int getCount() throws Exception {
		String sql = "select count(*) total from members";
		try(PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();)
		{
			rs.next();
			return rs.getInt("total");
		}
	}
	
	private Member map(ResultSet rs) throws Exception{
		Member member = new Member();
		member.setUserId(rs.getString("user_id"));
		member.setPassword(rs.getString("password"));
		member.setName(rs.getString("name"));
		member.setEmail(rs.getString("email"));
		member.setCellPhone(rs.getString("cell_phone"));
		member.setAddress(rs.getString("address"));
		member.setGrade(rs.getInt("grade"));
		member.setRegDate(rs.getDate("reg_date"));
		member.setUpdateDate(rs.getDate("update_date"));
		
		return member;
	}

	@Override
	public List<Member> selectList(int start, int end) throws Exception {
		List<Member> list = new ArrayList<>();
		String sql = 
				"select * from("+
				" select row_number() over (order by user_id) as seq, "+
				" user_id, name, password, cell_phone, email, address, grade, reg_date, update_date "+
				" from members) "+
				"where seq between ? and ?";
		
		try(PreparedStatement pstmt = conn.prepareStatement(sql))
		{
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			
			try(ResultSet rs = pstmt.executeQuery()){
				while(rs.next()) {
					Member member = map(rs);
					list.add(member);
				}
			}
			return list;
		}
	}

	@Override
	public Member selectOne(String userId) throws Exception {
		Member member = null;
		String sql = "select * from members where user_id=?";
		try(PreparedStatement pstmt = conn.prepareStatement(sql))
		{
			pstmt.setString(1, userId);
			
			try(ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) {
					member = map(rs);
				}
			}
			return member;	//없으면 null 리턴
		}
	}

	@Override
	public int insert(Member member) throws Exception {
		String sql = "insert into members "+
				"(user_id, name, password, cell_phone, email, address, grade)"+
				"values(?,?,?,?,?,?,1)";
		try(PreparedStatement pstmt = conn.prepareStatement(sql))
		{
			pstmt.setString(1, member.getUserId());
			pstmt.setString(2, member.getName());
			pstmt.setString(3, member.getPassword());
			pstmt.setString(4, member.getCellPhone());
			pstmt.setString(5, member.getEmail());
			pstmt.setString(6, member.getAddress());
			
			return pstmt.executeUpdate();
		}
	}

	@Override
	public int update(Member member) throws Exception {
		String sql = "update members set "+
					"cell_phone=?, "+
					"email=?, "+
					"address=?, "+
					"update_date = sysdate "+
					"where user_id=?";
		
		try(PreparedStatement pstmt = conn.prepareStatement(sql))
		{
			pstmt.setString(1, member.getCellPhone());
			pstmt.setString(2, member.getEmail());
			pstmt.setString(3, member.getAddress());
			pstmt.setString(4, member.getUserId());
			
			return pstmt.executeUpdate();
		}
	}

	@Override
	public int delete(String userId) throws Exception {
		String sql = "delete from members where user_id=?";
		try(PreparedStatement pstmt = conn.prepareStatement(sql))
		{
			pstmt.setString(1, userId);
			return pstmt.executeUpdate();
		}
	}

}
