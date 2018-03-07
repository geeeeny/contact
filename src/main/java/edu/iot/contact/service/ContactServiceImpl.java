package edu.iot.contact.service;

import java.util.List;

import edu.iot.contact.dao.ContactDao;
import edu.iot.contact.dao.ContactDaoImpl;
import edu.iot.contact.model.Contact;
import edu.iot.contact.model.Member;
import edu.iot.contact.view.ContactView;
import edu.iot.lib.app.Context;
import edu.iot.lib.db.ConnectionProvider;

public class ContactServiceImpl implements ContactService {
	static final int PER_PAGE = 5;
	ContactDao dao;
	ContactView view;
	
	public ContactServiceImpl() {
		dao = new ContactDaoImpl();
		view = ContactView.getInstance();
	}
	
	@Override
	public void printMyList(String userId) {
	}

	@Override
	public void printList() {
		Member member = (Member) Context.getAttribute("USER"); //현재 사용자의 정보를 얻어옴
		if(member.getGrade()==0) {
			printList(null);
		}else {
			printList(member.getUserId());
		}
	}
	
	@Override
	public void printPerOwner() {
		String userId = view.getString("검색할 사용자 ID: ");
		try {
			int result = dao.getCount(userId);
			if(result!=0) {
				printList(userId);
			}else {
				System.out.println("존재하지 않는 사용자입니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//printList()와 printPerOwner()에서 공통으로 사용할 함수
	private void printList(String userId) {
		try {
			int total;
			if(userId==null) { //관리자
				total = dao.getCount(); //주소록 테이블의 전체 연락처 개수
			}else { 
				total = dao.getCount(userId); //해당 회원이 소유한 연락처 개수
			}
			
			int totalPage = (int)Math.ceil((double)total/PER_PAGE);
			int page = 1;
			while(true) {
				if(page>=1 && page<=totalPage) {
					List<Contact> list;
					int start = (page-1)*PER_PAGE+1;
					int end = start+PER_PAGE-1;
					
					if(userId==null) { //관리자는 모든 회원들의 주소록을 볼 수 있지만
						list = dao.selectList(start, end);
					}else { //일반회원은 자신이 소유한 주소록만 볼 수 있다.
						list = dao.selectList(userId, start, end);
					}
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
	
	//연락처 추가하기
	@Override
	public void add() {
		Member member = (Member) Context.getAttribute("USER");//현재 사용자의 정보를 얻어옴
		Contact contact = view.getNewContact(member.getUserId());
		try {
			dao.insert(contact);
			ConnectionProvider.commit(); //DB에 적용
			System.out.println("연락처 추가 완료");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//주소록에서 선택한 연락처 수정하기
	@Override
	public void update() {
		printList(); //처음에 목록을 보여줌
		Member member = (Member) Context.getAttribute("USER");//현재 사용자의 정보를 얻어옴
		int contactId = view.getInt("수정할 연락처 ID: ");
		
		try {
			Contact contact = dao.selectOne(member.getUserId(), contactId);
			if(contact!=null) {
				contact = view.getUpdatedContact(contact);
				dao.update(contact);
				ConnectionProvider.commit(); //DB에 적용
				System.out.println("업데이트 완료");
				printList(); //수정된 목록을 보여줌
			}else {
				System.out.println("존재하지 않는 연락처입니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//주소록에서 선택한 연락처 삭제하기
	@Override
	public void delete() {
		printList(); //처음에 목록을 보여줌
		int contactId = view.getInt("삭제할 연락처 ID: ");
		try {
			Member member = (Member) Context.getAttribute("USER");//현재 사용자의 정보를 얻어옴
			int result = dao.delete(member.getUserId(), contactId);
			if(result == 1) {
				ConnectionProvider.commit(); //DB에 적용
				System.out.println("삭제 완료");
				printList(); //수정된 목록을 보여줌
			}else {
				System.out.println("삭제 실패");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//자기 소유 연락처 중에서 해당 이름을 가진 사람들의 연락처 출력
	@Override
	public void search() {
		// TODO 검색하기
		try {
			Member member = (Member) Context.getAttribute("USER"); //현재 사용자의 정보를 얻어옴
			String keyword = view.getString("검색할 이름: ");
			keyword = "%" + keyword + "%";

			List<Contact> list = dao.search(member.getUserId(), keyword);
			view.printList(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
