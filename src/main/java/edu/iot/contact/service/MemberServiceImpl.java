package edu.iot.contact.service;

import java.util.List;

import edu.iot.contact.dao.ContactDao;
import edu.iot.contact.dao.ContactDaoImpl;
import edu.iot.contact.dao.MemberDao;
import edu.iot.contact.dao.MemberDaoImpl;
import edu.iot.contact.model.Member;
import edu.iot.contact.view.MemberView;
import edu.iot.lib.app.Context;
import edu.iot.lib.db.ConnectionProvider;

public class MemberServiceImpl implements MemberService {
	static final int PER_PAGE = 5;
	MemberDao dao;
	MemberView view;
	ContactDao contactDao; //멤버 삭제시 주소록에서 연관된 행들을 삭제하기 위해 필요
	
	public MemberServiceImpl() {
		dao = new MemberDaoImpl();
		view = MemberView.getInstance();
		contactDao = new ContactDaoImpl();
	}
	
	@Override
	public void printList() {	//모든 멤버 출력
		try {
			int total = dao.getCount(); //주소록 테이블의 전체 연락처 개수
			int totalPage = (int)Math.ceil((double)total/PER_PAGE);
			int page = 1;
			while(true) {
				if(page>=1 && page<=totalPage) {
					List<Member> list;
					int start = (page-1)*PER_PAGE+1;
					int end = start+PER_PAGE-1;

					list = dao.selectList(start, end);

					//리스트 출력
					view.printPage(list, start, page, totalPage, total);
					
					//1페이지밖에 없다면 페이지를 입력받을 필요없음
					if(totalPage<=1) break;
				}else if(page == -1) { //-1을 입력하면 종료
					break;
				}else {
					if(total==0) {
						System.out.println("목록이 비어있습니다.");
						break;
					}
					else System.out.println("잘못된 페이지 번호입니다.");
				}
				page = view.getInt("페이지: ");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void printMyList(String userId) {
		Member member = (Member)Context.getAttribute("USER");
		if(member.getGrade() == 0) { //관리자
			
		}else {	//일반회원
			
		}
	}

	@Override
	public Member login() {
		String userId = view.getString("사용자 ID: ");
		String password = view.getString("비밀번호: ");
		
		Member member = null;
		try {
			member = dao.selectOne(userId);
			if(member == null) {
				System.out.println("존재하지 않는 ID 입니다.");
			}else {
				if(password.equals(member.getPassword())) {
					System.out.println("로그인 성공");
				}else {
					System.out.println("비밀번호가 틀렸습니다.");
					member = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return member;
	}

	@Override
	public Member getMember(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Member> getMemberList(int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void join() {
		String userId = null;
		try {
			while(true) {
				userId = view.getString("사용자 ID: ");
				Member member = dao.selectOne(userId);
				if(member==null) {
					break;
				}else {
					System.out.println("이미 사용중인 ID 입니다.");
				}
			}
			
			//나머지 정보 입력 후 insert
			Member member = view.getNewMember(userId);
			int result = dao.insert(member);
			if(result==1) {
				ConnectionProvider.commit(); //DB에 적용
				System.out.println("사용자 추가 성공");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update() {
		printList(); //처음에 목록을 보여줌
	
		String userId = view.getString("수정할 사용자 ID: ");
		try {
			Member member = dao.selectOne(userId);
			if(member!=null) {
				member = view.getUpdatedMember(member);
				dao.update(member);
				ConnectionProvider.commit(); //DB에 적용
				System.out.println("업데이트 완료");
				printList(); //수정된 목록을 보여줌
			}else {
				System.out.println("존재하지 않는 사용자입니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete() {
		printList(); //처음에 목록을 보여줌
		
		String userId = view.getString("삭제할 사용자 ID: ");
		try {
			Member member = dao.selectOne(userId);
			if(member == null) {
				System.out.println("존재하지 않는 사용자입니다.");
				return;
			}
			
			//외래키 처리(삭제할 멤버가 주소록 테이블에서 참조되고 있으면 주소록 테이블에서 연관된 행들을 모두 삭제해야 멤버 테이블에서 삭제가능)
			contactDao.delete(userId); //주소록 테이블에서 먼저 삭제 후
			dao.delete(userId); //멤버 테이블에서 삭제
			ConnectionProvider.commit(); //DB에 적용
			System.out.println("삭제 완료");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void changePassword() throws Exception {
		// TODO Auto-generated method stub

	}

}
