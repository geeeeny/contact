package edu.iot.contact.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.iot.contact.model.Contact;
import edu.iot.contact.model.Contact;
import edu.iot.lib.db.ConnectionProvider;

public class ContactDaoImp1 implements ContactDao {

	Connection conn;
	
	public ContactDaoImp1() {
		conn = ConnectionProvider.getConenction();
	}

	@Override
	public int getCount() throws Exception {
		String sql = "select count(*) total from contacts";
		try(PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();)
		{
			rs.next();
			return rs.getInt("total");
		}
	}
	
	@Override
	public int getCount(String userId) throws Exception {
		String sql = "select count(*) total from contacts " + 
					"where owner=?";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, userId);
			try(ResultSet rs = pstmt.executeQuery()){
				rs.next();
				return rs.getInt("total");
			}
		}
	}
	
	private Contact map(ResultSet rs) throws Exception{
		Contact contact = new Contact();
		contact.setContactId(rs.getInt("contact_id"));
		contact.setOwner(rs.getString("owner"));
		contact.setName(rs.getString("name"));
		contact.setEmail(rs.getString("email"));
		contact.setCellPhone(rs.getString("cell_phone"));
		contact.setAddress(rs.getString("address"));
		contact.setRegDate(rs.getDate("reg_date"));
		contact.setUpdateDate(rs.getDate("update_date"));
		
		return contact;
	}

	@Override
	public List<Contact> selectList(int start, int end) throws Exception {
		List<Contact> list = new ArrayList<>();
		String sql = 
				"select * from("+
				" select row_number() over (order by contact_id) as seq, "+
				" contact_id, owner, name, cell_phone, email, address, reg_date, update_date "+
				" from contacts) "+
				"where seq between ? and ?";
		
		try(PreparedStatement pstmt = conn.prepareStatement(sql))
		{
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			
			try(ResultSet rs = pstmt.executeQuery()){
				while(rs.next()) {
					Contact contact = map(rs);
					list.add(contact);
				}
			}
			return list;
		}
	}

	@Override
	public List<Contact> selectList(String owner, int start, int end) throws Exception{
		List<Contact> list = new ArrayList<>();
		
		String sql = "select * from("+
					"select row_number() over(order by contact_id) as seq, "+
					"contact_id, owner, name, cell_phone, email, address, reg_date, update_date "+
					"from contacts where owner=?) "+
					"where seq between ? and ?";
		
		try(PreparedStatement pstmt = conn.prepareStatement(sql))
		{
			pstmt.setString(1, owner);
			pstmt.setInt(2, start);
			pstmt.setInt(3, end);
			try(ResultSet rs = pstmt.executeQuery()){
				while(rs.next()) {
					Contact contact = map(rs);
					list.add(contact);
				}
			}
			return list;
		}
	}
	
	@Override
	public Contact selectOne(String owner, int contactId) throws Exception {
		Contact contact = null;
		String sql = "select * from contacts where owner=? and contact_id=?";
		try(PreparedStatement pstmt = conn.prepareStatement(sql))
		{
			pstmt.setString(1, owner);	//자기 소유의 연락처인지 확인
			pstmt.setInt(2, contactId);
			
			try(ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) {
					contact = map(rs);
				}
			}
			return contact;	//없으면 null 리턴
		}
	}

	@Override
	public int insert(Contact contact) throws Exception {
		String sql = "insert into contacts "+
				"(contact_id, owner, name, email, cell_phone, address)"+
				"values(contacts_seq.nextval,?,?,?,?,?)";
		try(PreparedStatement pstmt = conn.prepareStatement(sql))
		{
			pstmt.setString(1, contact.getOwner());
			pstmt.setString(2, contact.getName());
			pstmt.setString(3, contact.getEmail());
			pstmt.setString(4, contact.getCellPhone());
			pstmt.setString(5, contact.getAddress());
			
			return pstmt.executeUpdate();
		}
	}

	@Override
	public int update(Contact contact) throws Exception {
		String sql = "update contacts set "+
					"name=?, "+
					"email=?, "+
					"cell_phone=?, "+
					"address = ?, "+
					"update_date = sysdate "+
					"where owner = ? and contact_id = ?";
		
		try(PreparedStatement pstmt = conn.prepareStatement(sql))
		{
			pstmt.setString(1, contact.getName());
			pstmt.setString(2, contact.getEmail());
			pstmt.setString(3, contact.getCellPhone());
			pstmt.setString(4, contact.getAddress());
			pstmt.setString(5, contact.getOwner());
			pstmt.setInt(6, contact.getContactId());
			
			return pstmt.executeUpdate();
		}
	}

	@Override
	public int delete(String owner, int contactId) throws Exception {
		String sql = "delete from contacts where owner = ? and contact_id=?";
		try(PreparedStatement pstmt = conn.prepareStatement(sql))
		{
			pstmt.setString(1, owner);
			pstmt.setInt(2, contactId);
			return pstmt.executeUpdate();
		}
	}

	@Override
	public int delete(String owner) throws Exception {
		String sql = "delete from contacts where owner = ?";
		try(PreparedStatement pstmt = conn.prepareStatement(sql))
		{
			pstmt.setString(1, owner);
			return pstmt.executeUpdate();
		}
	}

	@Override
	public List<Contact> search(String owner, String keyword) throws Exception {
		// TODO dao 검색
		List<Contact> list = new ArrayList<>();
		String sql = "select * from contacts where owner = ? and name like ?";
		try(PreparedStatement pstmt = conn.prepareStatement(sql))
		{
			pstmt.setString(1, owner);
			pstmt.setString(2, keyword);
			try(ResultSet rs = pstmt.executeQuery()){
				while(rs.next()) {
					Contact contact = map(rs);
					list.add(contact);
				}
			}
			return list;
		}
	}

}
